/*
file of LaBeeFramework
https://www.bee-wkspace.com/

Copyright (C) 2016- Naoki Yoshioka (ARTS Laboratory)
http://www.arts-lab.net/dev/

This library is free software; you can redistribute it and/or 
modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; 
either version 3 of the License, or any later version.

This library is distributed in the hope that it will be useful, 
but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.
*/
package com.bee_wkspace.labee_fw.app.base;

import javax.servlet.http.Cookie;

import com.bee_wkspace.labee_fw.common.LogWriter;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.SysMsgCodeDefine;
import com.bee_wkspace.labee_fw.common.db.DBAccess;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.base.BaseBlogic;
import com.bee_wkspace.labee_fw.core.context.JsonValueContext;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;
import com.bee_wkspace.labee_fw.exception.FwException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Webアプリ用基底ビジネスロジック。<br>
 * <br>
 * 本クラスはビジネスロジックと対になるビーンクラスの生成管理やセッターメソッドの呼び出し、 レスポンス情報の設定処理等の<br>
 * 基底処理を実装定義している。<br>
 * <br>
 * 新規ビジネスロジックを作成する場合は本クラスを継承し、クラス名の末尾に<b>Blogic</b>を付加する必要がある<br>
 * (例)<br>
 *
 * <b>@FwBlogic(beanReuse = true)</b><br>
 * public class <b>TestBlogic</b> extends BaseBlogic &lt; TestBean &gt; { } <br>
 * <br>
 *
 * ビジネスロジックとは画面からの要求に対し、メインとなるプログラムを記述する処理である。<br>
 * たとえば画面からのログイン認証に対し、ユーザ情報をDB検索し認証チェックを行い結果を画面に返す等の処理である。<br>
 * 本フレームワークは画面(JSP)からのTGT(処理ターゲット)、EXE(処理対象)という名のHTMLフォームパラメータを送信する事で<br>
 * コントローラサーブレットを介して対象のビジネスロジックを実行するという仕組みを取っている。<br>
 * 1つのビジネスロジックは対となる1つのビーンクラス(結果データの入れ物)を持ち、処理後にビーンオブジェクトを<br>
 * JSPに渡す事で結果データをJSPで処理する。これらを1つのアクションの循環として動作する。<br>
 * <br>
 * 対となるビーンクラスは総称型に設定し、毎回インスタンスを生成するか、インスタンスを使いまわすかを選択設定する事が出来る<br>
 * インスタンスを使いまわす設定にした場合はビジネスロジックを呼びだした際に前回のビーン内容をそのまま保持している為<br>
 * 検索結果一覧等のデータを保持したまま、次の呼び出しでデータ操作したりと便利に使用出来る様になる。<br>
 * ただしビーン情報は任意でクリア処理を行なわない限りセッション情報にずっと保持される為、不要になった場合はclearSessionBeanDataメソッド<br>
 * 等を呼び出し、任意にクリア処理を行なう。<br>
 * ビーンインスタンス再利用設定はビジネスロジックのFwBlogicアノテーション内のbeanReuseパラメータで設定し<br>
 * beanReuseがtrueの場合は再利用し、falseの場合は毎回生成する。<br>
 *
 * <br>
 *
 * 画面からの要求に対するビジネスロジック処理メソッドを実装する際は<b>@FwExeMethod</b>アノテーションを付加し<br>
 * 戻り値にResponseContextを返却するメソッドを作成する。<br>
 * 呼び出しの要求に任意パラメータ(P1,P2,P3)が送信されている場合は実装するメソッドも同じ数の文字列引数を受ける必要がある。<br>
 * (例)<br>
 *
 * <b>@FwExeMethod</b><br>
 * public <b>ResponseContext start</b>(String param1) {<br>
 * 　　System.out.println(param1);<br>
 * 　　return <b>responseContext;</b><br>
 * }<br>
 * <br>
 * <b>ビーンパラメータの自動セット</b><br>
 * 例えばビーンクラスに画面のテキストボックス入力値を保持するパラメータ変数がある場合に、自動でテキストボックス値を<br>
 * パラメータ変数にセットする事が出来る。(セッターメソッド自動実行機能)<br>
 * またビーンのセッターメソッドに入力値のチェック処理を加える事でパラメータ受信、設定、入力チェックを簡潔に行う事が出来る。<br>
 * 例としてJSP(HTML)でフォーム名が"userId"という名のテキストボックスの値を自動受信設定するにはビーン側でsetUserId(String
 * value)もしくは<br>
 * setUserId(String paramName, String value )のメソッドを定義し<br>
 * ビジネスロジック側でdoAutoBeanSetter()メソッドを実行する事でsetUserIdメソッドが実行される。<br>
 * 定義するメソッド名はset + パラメータ名(先頭を大文字にする)の書式となる。<br>
 * (ビーンのセッターメソッドには<b>@AutoSetterParam</b>アノテーションを付加する必要がある)<br>
 * ビーンクラスのセッターメソッド定義例<br>
 * <b>@AutoSetterParam</b><br>
 * public void <b>setUserId</b>(String value) {<br>
 * 　　this.userId = value;<br>
 * }<br>
 * <br>
 *
 * 任意ビジネス処理実行後にJSPに遷移するか、他ビジネスロジックに処理委譲するか等は戻り値のResponseContextの設定により<br>
 * 切り替える事が可能だが、手動設定は行なわずに本クラス内で定義している設定メソッドを呼んで設定するのが望ましい<br>
 * <b>JSP遷移</b><br>
 * デフォルトではビジネスロジッククラス名から"Blogic"文字列を除いた名を画面IDとしてScreenDefine.csvで定義したJSPに遷移する。<br>
 * <br>
 * <b>他ビジネスロジックへ処理直接委譲</b><br>
 * 本クラスのsetDelegateResponseメソッドを呼ぶ事で、実装したビジネスロジック処理が完了した後に、別のビジネスロジッククラス<br>
 * のメソッドを直列呼び出し実行する事が出来る、最終的なレスポンス処理は呼び出し先のレスポンス設定に委譲する。<br>
 * <br>
 * <b>他ビジネスロジックへリダイレクト呼び出し</b><br>
 * 本クラスのsetRedirectResponseメソッドを呼ぶ事で、実装したビジネスロジック処理が完了した後に、別のビジネスロジッククラス<br>
 * のメソッドをリダイレクト呼び出しする事が出来る、リダイレクト処理は一度処理がブラウザに戻り、ブラウザが再度HTTPリダイレクトを行なう<br>
 * <br>
 * <b>URLリダイレクト呼び出し</b><br>
 * 本クラスのsetRedirectURLメソッドを呼ぶ事で、実装したビジネスロジック処理が完了した後に、別のURLへリダイレクト遷移<br>
 * する事が出来る、遷移先URLは外部サイトのURLでも構わない。<br>
 * <br>
 * レスポンス処理には画面遷移以外にCSV、JSON、バイナリ、XMLを返却出力する事も可能である。<br>
 * (例)レスポンスにCSVファイルを出力する場合の設定<br>
 * String csvData = "aaa,bbbb,123,";<br>
 * responseContext.setResponseType(ResponseContext.RESPONSE_TYPE_CSV);<br>
 * responseContext.setResponseEncType(CommonDefine.ENCODE_UTF_8);<br>
 * responseContext.setResponseCsv(csvData);<br>
 * responseContext.setExportFileName("Sample.csv");<br>
 *
 * <br>
 * <br>
 * 本クラスは継承元の基底クラス<b>com.bee_wkspace.labee_fw.core.base.BaseBlogic</b>
 * に対して以下の機能を付加している。<br>
 * データベース操作クラス<b>com.bee_wkspace.labee_fw.common.db.DBAccess</b>をフィールドに持ち(
 * インスタンス名 <b>dba</b>)<br>
 * システム設定ファイルでデータベースを使用する設定にしている場合に<br>
 * <b>preProcess()</b>メソッドでデータベースのコネクションオープン処理を行ない、<br>
 * <b>finallyProcess()</b>メソッドでデータベースのコネクションクローズ処理が確実に行われる。<br>
 * 本クラスを継承したビジネスロジッククラスはデータベースのオープン、クローズを意識する必要がなくなる。<br>
 * <b>preProcess()</b>メソッド、<b>finallyProcess()</b>メソッドはフレームワークが自動的に呼びだすので<br>
 * 実装者が手動で呼びだしてはいけない。<br>
 * (それぞれのメソッドをオーラライドする際は必ず親クラスの同名メソッドを呼びだす事)<br>
 *
 * <br>
 * データベース処理に対し、更新系のSQLを実行した場合は、実装者がコミット処理、ロールバック処理を管理する必要がある。<br>
 * (例) <b>dba.commi()</b>, <b>dba.rollback()</b><br>
 *
 * @author ARTS Laboratory
 *
 * $Id: AppBaseBlogic.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class AppBaseBlogic<T extends AppBaseBean> extends BaseBlogic<T> {

	/** DBアクセスオブジェクト */
	protected DBAccess dba = null;

	/**
	 * コンストラクタ。
	 *
	 */
	public AppBaseBlogic() {
		super();
	}

	/**
	 * 初期化処理<br>
	 *
	 * @throws FwException 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBlogic#init()
	 */
	@Override
	public void init() throws FwException {
		super.init();
		ScreenContext screenContext = settingScreenContext(env.getTargetName());
		bean.setScreenContext(screenContext);
		bean.setEnv(env);
	}

	/**
	 * メインメソッドを実行する前に実行するプリ処理。<br>
	 * (フレームワークにより自動呼び出しされるので実装者が手動で呼びだしてはいけない)<br>
	 * 本メソッド内でデータベースのコネクション接続処理が行われる。<br>
	 * 処理を拡張する場合は本メソッドを実装ビジネスロジックでオーバーライドする。
	 *
	 * @return 処理継続フラグ<br>
	 * 戻り値にfalseを返した場合は以降のメイン処理、ポスト処理は実行されない
	 * @throws Exception 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBlogic#preProcess()
	 */
	@Override
	public boolean preProcess() throws Exception {
		super.preProcess();

		if (SystemConfigMng.isUseDatabaseFlg()) {
			openDB();
		}

		// 入力チェックエラー情報をクリア
		bean.initError();
		return true;
	}

	/**
	 * メインメソッドを実行した後に実行するポスト処理。<br>
	 * (フレームワークにより自動呼び出しされるので実装者が手動で呼びだしてはいけない)<br>
	 * 処理を拡張する場合は本メソッドを実装ビジネスロジックでオーバーライドする。
	 *
	 * @throws Exception 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBlogic#postProcess()
	 */
	@Override
	public void postProcess() throws Exception {
		super.postProcess();
	}

	/**
	 * 前処理、メイン処理、後処理を行った後に最後に必ず実行するファイナリー処理。<br>
	 * (フレームワークにより自動呼び出しされるので実装者が手動で呼びだしてはいけない)<br>
	 * 本メソッド内でデータベースのコネクションクローズ処理が行われる。<br>
	 * 処理を拡張する場合は本メソッドを実装ビジネスロジックでオーバーライドする。
	 *
	 * @throws Exception 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBlogic#finallyProcess()
	 */
	@Override
	public void finallyProcess() throws Exception {
		super.finallyProcess();

		// データベースコネクションをクローズ
		if (SystemConfigMng.isUseDatabaseFlg()) {
			closeDB();
		}
	}

	/**
	 * Ajax呼び出しに対しDOM書き換え用JSONを返すレスポンス設定を行なう。<br>
	 * 引数のJsonValueContextはDOMオブジェクトのID、値のリストを持ち、設定した内容をJSONに変換して返す<br>
	 *
	 * @param jsonvalContext DOM書き換え用JSON情報コンテキスト
	 * @return レスポンスコンテキスト
	 */
	protected ResponseContext setAjaxJsonResponse(JsonValueContext jsonvalContext) {
		try {
			if (jsonvalContext != null) {
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(jsonvalContext.getJsonValues());
				responseContext.setResponseType(ResponseContext.RESPONSE_TYPE_JSON);
				responseContext.setResponseJson(json);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseContext;
	}

	/**
	 * 他の画面IDのJSPへ遷移するレスポンス設定を行なう。<br>
	 * JSP遷移先を変更するが、渡すBeanは当ビジネスロジックで使用しているビーンオブジェクト型の為、<br>
	 * JSP側でビーン参照する場合はビーンオブジェクト型をあわせる必要がある。
	 *
	 * @param screenId 画面ID
	 * @return レスポンスコンテキスト
	 * @throws FwException 例外
	 */
	protected ResponseContext setOtherScreenIdResponse(String screenId) throws FwException {
		ScreenContext screenContext = settingScreenContext(screenId);
		bean.setScreenContext(screenContext);

		return responseContext;
	}

	/**
	 * クッキーにパスワード値を書き込み、画面表示時にクッキーからパスワードタグに値を動的セットする。<br>
	 * 短命クッキーを生成し、セット対象のパスワードタグのid名を追加する。<br>
	 * 生成するクッキーの有効期限は5秒に設定し、画面側でクッキー内容をクリアするので<br>
	 * セキュリティー面を考慮している。<br>
	 *
	 * @param idName パスワードタグID名
	 * @param value パスワード値
	 */
	protected void addPasswdSetCookie(String idName, String value) {
		Cookie cookie = new Cookie(idName, value);
		cookie.setMaxAge(5);
		bean.addPasswdSetName(idName);
		super.addCookie(cookie);
	}

	/**
	 * DBオープンする。
	 *
	 * @throws Exception 例外
	 */
	protected void openDB() throws Exception {
		try {
			if (dba == null) {
				dba = new DBAccess();
			}

			dba.openDB();

		} catch (Exception e) {
			LogWriter.error(e);
			throw e;
		}
	}

	/**
	 * DBクローズする。
	 */
	protected void closeDB() {
		try {
			if (dba != null) {
				dba.closeDB();
				dba = null;
			}
		} catch (Exception e) {
			LogWriter.error(e);
		}
	}

	/**
	 * 画面情報を設定して返す。
	 *
	 * @param screnId 画面ID
	 * @return 画面情報
	 * @throws FwException 例外
	 */
	private ScreenContext settingScreenContext(String screnId) throws FwException {
		ScreenContext screenContext = ResourceMng.getScreenContext(screnId);
		if (screenContext == null) {
			throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10012,
					SystemConfigMng.getSystemLocale(), screnId));
		}

		// スマートフォンの場合
		if (env.isSmtPhoneFlg() && StringUtil.isNull(screenContext.getJspSphone())) {
			responseContext.setForwardPath(screenContext.getJspSphone());

			// タブレットの場合
		} else if (env.isTabletPadFlg() && StringUtil.isNull(screenContext.getJspTablet())) {
			responseContext.setForwardPath(screenContext.getJspTablet());

			// PCの場合
		} else {
			responseContext.setForwardPath(screenContext.getJspPC());
		}

		responseContext.setResponseType(ResponseContext.RESPONSE_TYPE_WEB);

		// ブラウザタイトル表示名設定
		if (StringUtil.isNull(screenContext.getViewTitle()) == false) {
			if (StringUtil.isNull(screenContext.getLabelCd())) {
				screenContext.setViewTitle(MessageLabelUtil.getWordLabel(screenContext.getLabelCd(),
						screenContext.getLabelDiv(), bean.getMsgLocale()));

			} else {
				screenContext.setViewTitle(screenContext.getScreenName());
			}
		}
		return screenContext;
	}

}
