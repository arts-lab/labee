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

import com.bee_wkspace.labee_fw.common.db.DBAccess;
import com.bee_wkspace.labee_fw.common.db.DBAccess.DataBaseType;
import com.bee_wkspace.labee_fw.core.annotation.FwExeMethod;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * Webアプリ用基底ビジネスロジック。(一覧表示対応版)<br>
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
 * 本クラスは継承元の基底クラス<b>com.bee_wkspace.labee_fw.app.base.AppBaseBlogic</b>
 * に対して以下の機能を付加している。<br>
 * ・一覧表示メソッドの定義(実装は継承先が行なう)<br>
 * ・ページ移動メソッド<br>
 * ・ソート表示メソッド<br>
 * 
 * @author ARTS Laboratory
 *
 * $Id: AppListBaseBlogic.java 559 2016-08-14 12:24:00Z pjmember $
 */
public abstract class AppListBaseBlogic<T extends AppListBaseBean> extends AppBaseBlogic<T> {

	/**
	 * コンストラクタ。
	 */
	public AppListBaseBlogic() {
		super();
	}

	/**
	 * ソートリンク押下時の処理イベント。
	 * 
	 * @param sortTarget ソート対象
	 * @param sortType ソート順タイプ
	 * @return レスポンス情報
	 * @throws FwException 例外
	 */
	@FwExeMethod
	public ResponseContext sort(String sortTarget, String sortType) throws FwException {
		try {
			AppSearchCondiInterface condition = bean.getSearchCondition();

			// ソート対象が違う場合
			if (!condition.getSortTarget().equals(sortTarget)) {
				// ソート対象、ソート順を設定
				condition.setSortTarget(sortTarget);
				condition.setSortType(AppSearchCondition.ASC);

				// ソート対象が同じ場合
			} else {
				// ソート順を入れ替える
				if (sortType.equals(AppSearchCondition.ASC)) {
					condition.setSortType(AppSearchCondition.DESC);

				} else if (sortType.equals(AppSearchCondition.DESC)) {
					condition.setSortType(AppSearchCondition.ASC);
				}
			}

			condition.setIndexFrom(0);

			// 一覧取得
			makeListData();

		} catch (Exception e) {
			throw new FwException(e);
		}

		return responseContext;
	}

	/**
	 * 表示ページ移動リンク押下時の処理イベント。
	 * 
	 * @param moveType ページ移動タイプ
	 * @return レスポンス情報
	 * @throws FwException 例外
	 */
	@FwExeMethod
	public ResponseContext pageMove(String moveType) throws FwException {
		try {
			AppSearchCondiInterface condition = bean.getSearchCondition();

			// ページ移動タイプ
			int type = Integer.parseInt(moveType);
			int indexFrom = condition.getIndexFrom();

			switch (type) {
			// 次リンク
			case (AppSearchCondition.MOVE_NEXT):
				indexFrom = condition.getIndexFrom()
						+ condition.getPageShowSize();
				if (indexFrom < condition.getResultTotalSize()) {
					condition.setIndexFrom(indexFrom);
				}
				break;

			// 前リンク
			case (AppSearchCondition.MOVE_BEFORE):
				indexFrom = condition.getIndexFrom()
						- condition.getPageShowSize();
				if (indexFrom < 0) {
					indexFrom = 0;
				}
				condition.setIndexFrom(indexFrom);
				break;

			// 最後リンク
			case (AppSearchCondition.MOVE_LAST):
				indexFrom = (condition.getResultTotalSize() / condition.getPageShowSize())
						* condition.getPageShowSize();
				condition.setIndexFrom(indexFrom);
				break;

			// 先頭リンク
			case (AppSearchCondition.MOVE_TOP):
				condition.setIndexFrom(0);
				break;
			default:
				break;

			}

			// 一覧取得
			makeListData();

		} catch (Exception e) {
			throw new FwException(e);
		}

		return responseContext;
	}

	/**
	 * 次・前リンク状態を設定する。
	 *
	 * @param condition 検索条件情報
	 */
	protected void parseCondition(AppSearchCondition condition) {
		int resultTotal = condition.getResultTotalSize();
		int indexFrom = condition.getIndexFrom();

		if (indexFrom > resultTotal) {
			indexFrom = 0;
			condition.setIndexFrom(indexFrom);
		}

		// 次リンク状態 設定
		if (indexFrom + condition.getPageShowSize() < resultTotal) {
			condition.setNextFlg(true);
			condition.setLastFlg(true);
		} else {
			condition.setNextFlg(false);
			condition.setLastFlg(false);
		}

		// 前リンク状態 設定
		if (indexFrom >= condition.getPageShowSize()) {
			condition.setBeforeFlg(true);
			condition.setTopFlg(true);
		} else {
			condition.setBeforeFlg(false);
			condition.setTopFlg(false);
		}

		if (indexFrom + condition.getPageShowSize() > resultTotal) {
			condition.setIndexTo(resultTotal);
		} else {
			condition.setIndexTo(indexFrom + condition.getPageShowSize());
		}
	}

	/**
	 * SQL表示順(ORDER BY) 表示リミット部分生成。
	 *
	 * @param condition 検索条件情報
	 * @return SQL部品
	 */
	protected String makeConditionSQL(AppSearchCondition condition) {
		StringBuilder sql = new StringBuilder();
		sql.append(" ORDER BY ");
		sql.append(condition.getSortTarget() + " " + condition.getSortType());

		if (DBAccess.getDataBaseType().equals(DataBaseType.MYSQL)) {
			sql.append(" LIMIT ");
			sql.append(condition.getIndexFrom());
			sql.append(",");
			sql.append(condition.getPageShowSize());
		}

		return sql.toString();
	}

	/**
	 * 一覧データ作成。 (継承先で内容を実装する)
	 * 
	 * @throws Exception 例外
	 */
	abstract protected void makeListData() throws Exception;

}
