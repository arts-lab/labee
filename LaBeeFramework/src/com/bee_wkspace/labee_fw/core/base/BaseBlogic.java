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
package com.bee_wkspace.labee_fw.core.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.LogWriter;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.SysMsgCodeDefine;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.annotation.AutoSetterParam;
import com.bee_wkspace.labee_fw.core.annotation.FwExeMethod;
import com.bee_wkspace.labee_fw.core.context.DelegateContext;
import com.bee_wkspace.labee_fw.core.context.FileUploadContext;
import com.bee_wkspace.labee_fw.core.context.RedirectContext;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * フレームワーク用ビジネスロジックの基底クラス。<br>
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
 * @author ARTS Laboratory
 *
 * $Id: BaseBlogic.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class BaseBlogic<T extends BaseBean> implements BlogicInterface {

	/** レスポンスコンテキスト */
	protected ResponseContext responseContext;

	/** ビジネスロジックと対になるパラメータ格納ビーンオブジェクト */
	protected T bean;

	/** セッションオブジェクト */
	protected HttpSession session;

	/** アップロードファイル情報 */
	protected FileUploadContext uploadFileContext;

	/** 入力パラメータ格納マップ */
	protected HashMap<String, String> paramsMap;

	/** アクセス環境情報 */
	protected Environment env;

	/** 取得クッキー情報 */
	protected Cookie[] cookies;

	/** 追加クッキーリスト */
	protected ArrayList<Cookie> addCokieList;

	/**
	 * 後処理メソッドを実行するかのフラグ <br>
	 * true:実行する false:実行しない
	 */
	protected boolean postProcessFlg = true;

	/**
	 * コンストラクタ。
	 */
	public BaseBlogic() {
		super();
		responseContext = new ResponseContext();
	}

	/**
	 * 初期化処理。
	 *
	 * @throws FwException 例外
	 */
	@Override
	public void init() throws FwException {
		//
	}

	/**
	 * 初期表示処理イベント。
	 *
	 * @throws FwException 例外
	 * @return レスポンスコンテキスト
	 */
	@FwExeMethod
	public ResponseContext start() throws FwException {
		return responseContext;
	}

	/**
	 * フレームワーク用ビーンクラスを生成する。<br>
	 *
	 * 指定したフルパッケージクラス名を元にビーンオブジェクトを動的生成してインスタンスフィールドにセットする。 <br>
	 * <b>beanReuse</b>が<b>true</b>の場合はセッションに保持しているビーンオブジェクトをそのまま使いまわす<br>
	 * (既にセッションに保持されているビーンには前回格納した変数パラメータ内容はそのまま残っている）<br>
	 * またセッションタイムアウトでセッションに格納していたビーンオブジェクトがクリアされてしまった場合は<br>
	 * シリアライズ化したビーンファイルからビーンオブジェクトを復元する。<br>
	 * <br>
	 * <b>beanReuse</b>が<b> false</b>の場合は毎回新規にビーンインスタンスを生成する。<br>
	 * (前回格納した変数パラメータ内容は消える)<br>
	 *
	 * @param beanClassName ビーンクラス名
	 * @param beanReuse ビーン再利用フラグ
	 * @throws FwException 例外
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void createBean(String beanClassName, boolean beanReuse) throws FwException {
		try {
			boolean createFlg = false;

			if (this.session == null || beanReuse == false) {
				createFlg = true;

			} else if (beanReuse) {
				// ビーンオブジェクトが既にセッションに格納されているかをチェック
				HashMap<String, T> beanMap = (HashMap<String, T>) session.getAttribute(CommonDefine.BEAN_SESSION_MAP_NAME);
				if (beanMap == null) {
					beanMap = new HashMap<String, T>();
					session.setAttribute(CommonDefine.BEAN_SESSION_MAP_NAME, beanMap);
				}

				bean = (T) beanMap.get(beanClassName);
				if (bean == null) {
					// シリアライズ復元を試みる
					bean = (T) decodeSerializeBeanObj(beanClassName);
					if (bean != null) {
						beanMap.put(beanClassName, bean);
					} else {
						createFlg = true;
					}
				}
			}

			// 新規にビーンオブジェクトインスタンスを生成する場合
			if (createFlg) {
				// 引数のクラス名を元にリフレクション機能で動的にビーンを生成してセッションに格納
				Class<?> targetClass = Class.forName(beanClassName);
				// インスタンス生成
				bean = (T) targetClass.newInstance();

				// -----------------------------------------------------------------
				// 入力チェック用セッターメソッドパラメータ名を取得する
				HashMap<String, String> setterParamNameMap = new HashMap<String, String>();
				Method[] methods = targetClass.getMethods();
				for (Method method : methods) {
					String methodName = method.getName();
					Class<? extends Object>[] args = method.getParameterTypes();

					boolean annoFlg = false;
					Annotation[] annotations = method.getDeclaredAnnotations();
					for (Annotation annotation : annotations) {
						if (annotation.annotationType().equals(AutoSetterParam.class)) {
							annoFlg = true;
							break;
						}
					}

					// 条件
					// ・メソッドの頭にsetが付いている
					// ・引数を1個以上持っている
					// ・@AutoSetterParamアノテーションが付いている。
					if (methodName.startsWith("set") && args.length >= 1 && annoFlg) {
						String key = methodName.substring(3, methodName.length());
						setterParamNameMap.put(key, methodName);
					}
				}

				// 親Beanクラスのフィールドを辿る
				Field field = null;
				while (targetClass != null) {
					try {
						field = targetClass.getDeclaredField("setterParamNameMap");
						break;
					} catch (NoSuchFieldException e) {
						targetClass = targetClass.getSuperclass();
					}
				}
				// -----------------------------------------------------------------
				// セッターメソッドマップをprivateフィールドにセット
				if (field != null) {
					field.setAccessible(true);
					field.set(bean, setterParamNameMap);
				}

				if (this.session != null) {
					HashMap<String, T> beanMap = (HashMap<String, T>) session.getAttribute(CommonDefine.BEAN_SESSION_MAP_NAME);
					if (beanMap == null) {
						beanMap = new HashMap<String, T>();
						session.setAttribute(CommonDefine.BEAN_SESSION_MAP_NAME, new HashMap<String, T>());
					}

					beanMap.put(beanClassName, bean);
				}
			}

		} catch (Exception e) {
			LogWriter.error(e);
			throw new FwException(e);
		}
	}

	/**
	 * シリアライズ化されたファイルからビーンオブジェクトを復元する。
	 * 
	 * @param beanClassName ビーンクラス名
	 * @return ビーンオブジェクト
	 * @throws Exception 例外
	 */
	protected Object decodeSerializeBeanObj(String beanClassName) throws Exception {
		Object retBeanObj = null;

		Cookie appSessionCookie = getCookie(CommonDefine.FW_SESSION_COOKIE_NAME);
		if (appSessionCookie != null) {
			String path = SystemConfigMng.getSystemFolderPath()
					+ CommonDefine.SEP
					+ CommonDefine.SERIALIZE_FOLDER_NAME
					+ CommonDefine.SEP
					+ appSessionCookie.getValue()
					+ CommonDefine.SEP
					+ beanClassName;

			File beanFile = new File(path);
			if (beanFile.exists()) {
				FileInputStream inFile = new FileInputStream(path);
				ObjectInputStream inObject = new ObjectInputStream(inFile);
				retBeanObj = inObject.readObject();
				inObject.close();
				inFile.close();
				beanFile.delete();
			}
		}

		return retBeanObj;

	}

	/**
	 * ビジネスロジックに紐づけているビーンクラスのセッターメソッドを動的に実行する。<br>
	 * <br>
	 * 実行されるセッターメソッドはフレームワーク用アノテーション<b>@AutoSetterParam</b>を 付加しているメソッドである。
	 *
	 * @throws FwException 例外
	 */
	protected void doAutoBeanSetter() throws FwException {
		String setterMethodName = null;
		try {
			Class<? extends BaseBean> clazz = bean.getClass();
			Iterator<String> iterator = paramsMap.keySet().iterator();

			for (; iterator.hasNext();) {
				String paramName = iterator.next();

				String parts1 = paramName.substring(0, 1).toUpperCase();
				String parts2 = paramName.substring(1, paramName.length());
				String setterKey = parts1 + parts2;

				// 入力チェック セッターパラメータマップに含まれている場合
				setterMethodName = bean.checkSetterParam(setterKey);
				if (setterMethodName != null) {
					String value = (String) paramsMap.get(paramName);

					// 動的にセッターメソッドを実行する
					try {
						// 入力チェック処理込みセッターメソッド
						Method method = clazz.getMethod(setterMethodName, String.class, String.class);
						method.invoke(bean, paramName, value);

					} catch (NoSuchMethodException ex) {
						try {
							// セッターメソッドのみの場合
							Method method = clazz.getMethod(setterMethodName, String.class);
							method.invoke(bean, value);
						} catch (NoSuchMethodException ex2) {
							throw ex;
						}
					}
				}
			}
		} catch (InvocationTargetException e) {
			LogWriter.error(e);
			throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10020,
					SystemConfigMng.getSystemLocale(), setterMethodName), e);

		} catch (Exception e) {
			LogWriter.error(e);
			throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10021,
					SystemConfigMng.getSystemLocale(), setterMethodName), e);
		}
	}

	/**
	 * セッションから指定クラス名のビーンオブジェクトを取得する。
	 *
	 * @param beanName ビーンクラス名(フルパッケージ名)
	 * @return ビーンインターフェース。<br>
	 * 存在しない場合はnullが返される。
	 */
	@SuppressWarnings("unchecked")
	protected BeanInterface getBeanFromSession(String beanName) {
		BeanInterface beanObj = null;
		try {
			if (this.session != null) {
				HashMap<String, T> beanMap = (HashMap<String, T>) session.getAttribute(CommonDefine.BEAN_SESSION_MAP_NAME);
				if (beanMap != null) {
					beanObj = (BeanInterface) beanMap.get(beanName);
				}

				// シリアライズ復元を試みる
				if (beanObj == null) {
					beanObj = (BeanInterface) decodeSerializeBeanObj(beanName);
					beanMap.put(beanName, (T) beanObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanObj;
	}

	/**
	 * 指定したビーンクラス名以外のセッション保持ビーン情報をすべてクリアする。<br>
	 * <b>beanReuse</b>が<b>true</b>の場合はビーンオブジェクトをセッションに蓄積格納しているので
	 * 本メソッドを呼びだす事で不要になったビーンオブジェクトをセッション(メモリ)上から削除する事が出来る。
	 *
	 * @param beanName ビーンクラス名(フルパッケージ名)
	 */
	@SuppressWarnings("unchecked")
	protected void clearSessionAllOtherBeanData(String beanName) {
		BeanInterface beanObj = getBeanFromSession(beanName);
		if (this.session != null) {
			HashMap<String, BeanInterface> beanMap = (HashMap<String, BeanInterface>) session
					.getAttribute(CommonDefine.BEAN_SESSION_MAP_NAME);
			beanMap.clear();
			beanMap.put(beanName, beanObj);
		}

		// シリアライズ化したビーンファイルを削除
		Cookie appSessionCookie = getCookie(CommonDefine.FW_SESSION_COOKIE_NAME);
		if (appSessionCookie != null) {
			String path = SystemConfigMng.getSystemFolderPath()
					+ CommonDefine.SEP
					+ CommonDefine.SERIALIZE_FOLDER_NAME
					+ CommonDefine.SEP
					+ appSessionCookie.getValue();

			File dir = new File(path);
			if (dir.exists()) {
				File[] files = dir.listFiles();
				for (File file : files) {
					if (file.getName().equals(beanName) == false) {
						file.delete();
					}
				}
			}
		}
	}

	/**
	 * 指定したビーンクラス名のセッション保持ビーン情報をクリアする。<br>
	 *
	 * @param beanName ビーンクラス名(フルパッケージ名)
	 */
	@SuppressWarnings("unchecked")
	protected void clearSessionBeanData(String beanName) {
		if (this.session != null) {
			HashMap<String, BeanInterface> beanMap = (HashMap<String, BeanInterface>) session
					.getAttribute(CommonDefine.BEAN_SESSION_MAP_NAME);
			beanMap.remove(beanName);

			// シリアライズ化したビーンファイルを削除
			Cookie appSessionCookie = getCookie(CommonDefine.FW_SESSION_COOKIE_NAME);
			if (appSessionCookie != null) {
				String path = SystemConfigMng.getSystemFolderPath()
						+ CommonDefine.SEP
						+ CommonDefine.SERIALIZE_FOLDER_NAME
						+ CommonDefine.SEP
						+ appSessionCookie.getValue()
						+ CommonDefine.SEP
						+ beanName;

				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

	/**
	 * レスポンスコンテキストにビジネスロジック処理の直接委譲設定を行なう。<br>
	 * <br>
	 * 本メソッドを設定する事により、自ビジネスロジック処理後に引数で設定した次のビジネスロジックを
	 * 呼び出し、レスポンス結果を次ビジネスロジックに委譲する。(ビジネスロジックを直列で実行する)
	 *
	 * @param target 次ビジネスロジックのパス<br>
	 * ビジネスロジック格納のトップパス(コンフィグファイルで設定定義)を除いた、<br>
	 * パッケージパス + ビジネスロジッククラス名から <b>Blogic</b>部を除いた文字列<br>
	 * (例) <b>com.blogic.sample.TestBlogic.class</b>
	 * というフルパッケージパスのビジネスロジックに処理委譲する場合で<br>
	 * ビジネスロジック格納のトップパスが com.bloic に設定している場合はtargetに設定する値は<br>
	 * <b>sample.Test</b>となる。
	 * @param execute 次ビジネスロジックの呼び出し処理(メソッド名)
	 * @param params 次ビジネスロジックの呼び出し処理(メソッド名)の引数に渡すパラメータ文字列。最大3個まで設定可能。
	 * @return 委譲設定コンテキスト
	 */
	protected DelegateContext setDelegateResponse(String target, String execute, String... params) {
		DelegateContext delegateContext = new DelegateContext();
		delegateContext.setTarget(target);
		delegateContext.setExecute(execute);

		if (params != null) {
			if (params.length >= 1 && StringUtil.isNull(params[0])) {
				delegateContext.setParam1(params[0]);

				if (params.length >= 2 && StringUtil.isNull(params[1])) {
					delegateContext.setParam2(params[1]);

					if (params.length >= 3 && StringUtil.isNull(params[2])) {
						delegateContext.setParam3(params[2]);
					}
				}
			}
		}

		responseContext.setDelegateContext(delegateContext);
		responseContext.setDelegateFlg(true);
		return delegateContext;
	}

	/**
	 * レスポンスコンテキストにビジネスロジック処理のリダイレクト設定を行なう。<br>
	 * <br>
	 * 本メソッドを設定する事により、自ビジネスロジック処理後に引数で設定した次のビジネスロジックへ<br>
	 * リダイレクト呼び出し実行を行なう。<br>
	 *
	 * @param target リダイレクト先ビジネスロジックのパス<br>
	 * ビジネスロジック格納のトップパス(コンフィグファイルで設定定義)を除いた、<br>
	 * パッケージパス + ビジネスロジッククラス名から <b>Blogic</b>部を除いた文字列<br>
	 * (例) <b>com.blogic.sample.TestBlogic.class</b>
	 * というフルパッケージパスのビジネスロジックに処理リダイレクトする場合に<br>
	 * ビジネスロジック格納のトップパスが com.bloic に設定している場合はtargetに設定する値は<br>
	 * <b>sample.Test</b>となる。
	 * @param execute リダイレクト先ビジネスロジックの呼び出し処理(メソッド名)
	 * @param param リダイレクト先ビジネスロジックの呼び出し処理(メソッド名)の引数に渡すパラメータ文字列。最大3個まで設定可能。
	 * @return リダイレクト設定コンテキスト
	 */
	protected RedirectContext setRedirectResponse(String target, String execute, String... param) {
		RedirectContext redirectContext = new RedirectContext();
		redirectContext.setTarget(target);
		redirectContext.setExecute(execute);

		String[] params = new String[param.length];
		int idx = 0;
		for (String p : param) {
			params[idx] = p;
			idx++;
		}
		redirectContext.setParam(params);

		responseContext.setRedirectContext(redirectContext);
		responseContext.setRedirectFlg(true);
		return redirectContext;
	}

	/**
	 * レスポンスコンテキストにビジネスロジック処理のSSLリダイレクト設定を行なう。<br>
	 * <br>
	 * 本メソッドを設定する事により、自ビジネスロジック処理後に引数で設定した次のビジネスロジックへ<br>
	 * リダイレクト呼び出し実行を行なう。<br>
	 *
	 * @param target リダイレクト先ビジネスロジックのパス<br>
	 * ビジネスロジック格納のトップパス(コンフィグファイルで設定定義)を除いた、<br>
	 * パッケージパス + ビジネスロジッククラス名から <b>Blogic</b>部を除いた文字列<br>
	 * (例) <b>com.blogic.sample.TestBlogic.class</b>
	 * というフルパッケージパスのビジネスロジックに処理リダイレクトする場合に<br>
	 * ビジネスロジック格納のトップパスが com.bloic に設定している場合はtargetに設定する値は<br>
	 * <b>sample.Test</b>となる。
	 * @param execute リダイレクト先ビジネスロジックの呼び出し処理(メソッド名)
	 * @param param リダイレクト先ビジネスロジックの呼び出し処理(メソッド名)の引数に渡すパラメータ文字列。最大3個まで設定可能。
	 * @return リダイレクト設定コンテキスト
	 */
	protected RedirectContext setRedirectResponseSSL(String target, String execute, String... param) {
		RedirectContext redirectContext = setRedirectResponse(target, execute, param);
		redirectContext.setSslFlg(true);
		return redirectContext;
	}

	/**
	 * レスポンスコンテキストにURLリダイレクト設定を行なう。<br>
	 * <br>
	 * 引数で指定したターゲットURLにhttpリダイレクト呼び出しを行なう。
	 *
	 * @param redirectURL リダイレクト先のURL、設定する値はhttpから始まるフルURLパス。(外部サイトでも構わない)
	 * @param statusCode httpステータスコード
	 * @param headerMap リダイレクト時に送信するhttpヘッダ情報マップ。ヘッダ項目、ヘッダ値の対でマップに格納。
	 * @return リダイレクト設定コンテキスト
	 */
	protected RedirectContext setRedirectURL(String redirectURL, String statusCode, HashMap<String, String> headerMap) {
		RedirectContext redirectContext = new RedirectContext();
		redirectContext.setRedirectURL(redirectURL);
		redirectContext.setStatusCode(statusCode);
		redirectContext.setHeaderMap(headerMap);

		responseContext.setRedirectContext(redirectContext);
		responseContext.setRedirectFlg(true);

		return redirectContext;
	}

	/**
	 * レスポンスに出力するクッキーオブジェクト情報を追加する。
	 *
	 * @param cookie クッキーオブジェクト
	 */
	protected void addCookie(Cookie cookie) {
		if (addCokieList == null) {
			addCokieList = new ArrayList<Cookie>();
		}
		addCokieList.add(cookie);
	}

	/**
	 * クッキーリスト内から指定した名のクッキーを返す。
	 *
	 * @param name クッキー名
	 * @return クッキーオブジェクト
	 */
	protected Cookie getCookie(String name) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * メインメソッドを実行する前に実行するプリ処理。<br>
	 * (フレームワークにより自動呼び出しされるので実装者が手動で呼びだしてはいけない)<br>
	 * 継承先で実装する。
	 *
	 * @return 処理継続フラグ<br>
	 * 戻り値にfalseを返した場合は以降のメイン処理、ポスト処理は実行されない
	 * @throws Exception 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#preProcess()
	 */
	@Override
	public boolean preProcess() throws Exception {
		return true;
	}

	/**
	 * メインメソッドを実行した後に実行するポスト処理。<br>
	 * (フレームワークにより自動呼び出しされるので実装者が手動で呼びだしてはいけない)<br>
	 * 継承先で実装する。
	 *
	 * @throws Exception 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#postProcess()
	 */
	@Override
	public void postProcess() throws Exception {
		//
	}

	/**
	 * 前処理、メイン処理、後処理を行った後に最後に必ず実行するファイナリー処理。<br>
	 * 継承先で実装する。
	 *
	 * @throws Exception 例外
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#finallyProcess()
	 */
	@Override
	public void finallyProcess() throws Exception {
		//
	}

	/**
	 * クッキー配列をセットする。
	 *
	 * @param cookies クッキーオブジェクト配列
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#setCookies(javax.servlet.http.Cookie[])
	 */
	@Override
	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}

	/**
	 * ポスト処理実行フラグの設定値を返す。<br>
	 *
	 * @return true = ポスト処理を行なう
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#isPostProcessFlg()
	 */
	@Override
	public boolean isPostProcessFlg() {
		return postProcessFlg;
	}

	/**
	 * クッキーオブジェクトリストを返す。
	 *
	 * @return クッキーオブジェクトリスト
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#getAddCokieList()
	 */
	@Override
	public ArrayList<Cookie> getAddCokieList() {
		return addCokieList;
	}

	/**
	 * アクセス環境情報を返す。
	 *
	 * @return env アクセス環境情報
	 */
	@Override
	public Environment getEnv() {
		return env;
	}

	/**
	 * @param session セットする session
	 */
	@Override
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * @param env セットする env
	 */
	@Override
	public void setEnv(Environment env) {
		this.env = env;
		responseContext.setEnv(env);
	}

	/**
	 * 受信したフォームパラメータから指定名のパラメータ値を文字列で取得する。
	 *
	 * @param name パラメータ名
	 * @return パラメータ名に対応するパラメータ値
	 */
	public String getFormParam(String name) {
		return (String) paramsMap.get(name);
	}

	/**
	 * ビジネスロジックと対になるビーンオブジェクトを返す。
	 *
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#getBean()
	 */
	@Override
	public BeanInterface getBean() {
		return bean;
	}

	/**
	 * レスポンスコンテキストを返す。
	 *
	 * @return レスポンスコンテキスト
	 * @see com.bee_wkspace.labee_fw.core.base.BlogicInterface#getResponseContext()
	 */
	@Override
	public ResponseContext getResponseContext() {
		return responseContext;
	}

	/**
	 * アップロードファイル情報をセットする。
	 * 
	 * @param uploadFileContext アップロードファイル情報
	 */
	@Override
	public void setUploadFileContext(FileUploadContext uploadFileContext) {
		this.uploadFileContext = uploadFileContext;
	}

	/**
	 * パラメータ格納マップをセットする。
	 * 
	 * @param paramsMap パラメータ格納マップ
	 */
	@Override
	public void setParamsMap(HashMap<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

}
