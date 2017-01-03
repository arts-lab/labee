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
package com.bee_wkspace.labee_fw.core.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.LogWriter;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.BlogicFactory;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.MultiPartFormReader;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.SystemInitializer;
import com.bee_wkspace.labee_fw.core.SystemUtil;
import com.bee_wkspace.labee_fw.core.base.BaseBean;
import com.bee_wkspace.labee_fw.core.base.BeanInterface;
import com.bee_wkspace.labee_fw.core.base.BlogicInterface;
import com.bee_wkspace.labee_fw.core.context.RedirectContext;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * フレームワーク ベースコントローラーサーブレット。<br>
 * Web画面からのフォームリクエスト要求に対し、パラメータ受信、ビジネスロジックの呼び出し実行、結果レスポンスの返却等の処理を<br>
 * 統括して行うコントローラ処理を担う。<br>
 * <br>
 * HTMLフォームパラメータから以下名のパラメータを受信し、処理を実行する。<br>
 * ・<b>tgt = ターゲット指定パラメータ。</b><br>
 * ビジネスロジック格納のトップパス(コンフィグファイルで設定定義)を除いた、<br>
 * パッケージパス + ビジネスロジッククラス名から <b>Blogic</b>部を除いた文字列<br>
 * (例) <b>com.blogic.sample.TestBlogic</b> というフルパッケージパスのクラスの場合<br>
 * ビジネスロジック格納のトップパス設定が com.bloic の場合はパラメータ値は<br>
 * <b>sample.Test</b>となる。<br>
 * (呼びだすビジネスロジックのクラスには<b>@FwBlogic</b>アノテーションが付加されている必要がある)<br>
 * <br>
 * ・<b>exe = ビジネスロジック内の呼び出し処理(メソッド名)パラメータ</b><br>
 * (呼びだすビジネスロジックのメソッドには<b>@FwExeMethod</b>アノテーションが付加されている必要がある)<br>
 * ・<b>p1 = 呼び出し時任意引数パラメータ1</b><br>
 * ・<b>p2 = 呼び出し時任意引数パラメータ2</b><br>
 * ・<b>p3 = 呼び出し時任意引数パラメータ3</b><br>
 * p1からp3の引数パラメータは任意パラメータとなり、使用しない場合は送信不要。逆にパラメータを送信した場合は<br>
 * 引数の数と合致した呼び出し処理(メソッド)が存在しない場合はエラーとなる。<br>
 * ・<b>screen_id = ダイレクトJSP呼び出し処理</b><br>
 * 設定する値はScreenDefine.csvで設定した画面ID、本パラメータを設定した場合はビジネスロジック処理を<br>
 * 行なわず直接画面IDに紐づくJSPに遷移する。<br>
 *
 * <br>
 * ビジネスロジック戻り値のレスポンス設定により通常のJSPフォワード遷移レスポンスの他に<br>
 * バイナリ、CSV、XML、JSON,をレスポンスとして返す事が出来る。<br>
 * <br>
 *
 * 機能拡張する場合は本クラスを継承したサーブレットを実装する。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: BaseControlerServlet.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class BaseControlerServlet extends HttpServlet {

	private static final long serialVersionUID = -3926300117220312407L;

	/** デフォルト文字コード */
	protected static String encodeType = CommonDefine.ENCODE_UTF_8;

	/** デフォルトターゲット */
	protected String defaultTarget = null;

	/**
	 * サーブレット初期化。
	 *
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/*
	 * doGetの処理。実際の処理はdoPostに委譲する。
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/*
	 * doPostの処理。<br> フレームワークの動作処理の起点となるメイン処理を実行する。
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		Environment env = null;
		try {

			// システム初期化チェック
			if (!SystemInitializer.isStartUpFlg()) {
				SystemInitializer.initializeSystem(getServletContext().getRealPath("/"), this);

				// 初期化エラーの場合
				if (!SystemInitializer.isInitErrFlg()) {
					setErrorResponse(request, response, SystemInitializer.getInitException());
					return;
				}
			}

			// リソース設定情報読み込み(ファイルタイムスタンプ更新時のみ再読み込み)
			ResourceMng.loadResource(getServletContext().getRealPath("/"), this);

			setPreSetting();

			// セッション取得
			HttpSession session = request.getSession(true);
			
			// フレームワーククッキー設定
			Cookie appSessionCookie = SystemUtil.getCookie(request, CommonDefine.FW_SESSION_COOKIE_NAME);
			if (appSessionCookie == null) {
				String appSessionId = StringUtil.createRandom(20);
				appSessionCookie = new Cookie(CommonDefine.FW_SESSION_COOKIE_NAME, appSessionId);
				session.setAttribute(CommonDefine.FW_SESSION_COOKIE_NAME, appSessionId);
				response.addCookie(appSessionCookie);
			} else {
				session.setAttribute(CommonDefine.FW_SESSION_COOKIE_NAME, appSessionCookie.getValue());
			}

			// アクセス環境情報取得
			env = createEnviroment();
			Environment.setEnviromentParam(request, env);

			// リクエストパラメータ情報を取得
			MultiPartFormReader formReader = new MultiPartFormReader(encodeType);
			formReader.parseMultiPartRequest(request);
			HashMap<String, String> reqParamMap = formReader.getParamMap();

			// アクセス環境情報に追加情報を設定
			setExEnvironmentParam(env);

			// 画面IDに紐づくHTML,JSPに直接遷移する場合
			if (StringUtil.isNull(reqParamMap.get(CommonDefine.SCREEN_ID))) {
				setServletForwardResponse(request, response, env, reqParamMap.get(CommonDefine.SCREEN_ID));

				// ターゲット、処理に紐づくビジネスロジックを実行する場合
			} else {
				// サーブレット初期値パラメータ適用
				checkServletInitParam(reqParamMap);

				// ビジネスロジックを動的に生成して実行する
				FactoryExecuteResult factoryExecuteResult = factoryBlogicAndExecute(request, response,
						session, formReader, reqParamMap, env);

				// レスポンス情報セット
				setServletResponse(request, response,
						factoryExecuteResult.bean, factoryExecuteResult.responseContext);
			}

		} catch (FwException e) {
			LogWriter.error(e.getCatchException());
			setErrorResponse(request, response, e);

		} catch (Exception e) {
			LogWriter.error(e);
			setErrorResponse(request, response, e);

		} catch (Throwable e) {
			LogWriter.error(e);
			setErrorResponse(request, response, e);
		}
	}

	/**
	 * 画面処理ビジネスロジックを動的生成して実行する。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param session セッションオブジェクト
	 * @param formReader フォームパラメータリーダ
	 * @param reqParamMap フォームパラメータ情報格納マップ
	 * @param env アクセス環境情報
	 * @return ビジネスロジック実行結果情報
	 * @throws Throwable 例外
	 */
	protected FactoryExecuteResult factoryBlogicAndExecute(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, MultiPartFormReader formReader,
			HashMap<String, String> reqParamMap, Environment env) throws Throwable {

		FactoryExecuteResult factoryExecuteResult = new FactoryExecuteResult();
		BlogicInterface blogic = null;
		BlogicFactory factory = new BlogicFactory(session, formReader, reqParamMap, env);
		blogic = factory.executeBlogic(request, response);
		ResponseContext responseContext = factory.getResponseContext();

		// レスポンスの処理を委譲し、次のビジネスロジックを再帰実行する場合
		if (responseContext.isDelegateFlg()) {
			// パラメータオブジェクトを初期化
			reqParamMap = SystemUtil.getDelegateParamMap(responseContext);
			formReader = new MultiPartFormReader(CommonDefine.ENCODE_UTF_8);

			// 再帰
			factoryExecuteResult = factoryBlogicAndExecute(request, response,
					session, formReader, reqParamMap, env);
		} else {
			factoryExecuteResult.bean = blogic.getBean();
			factoryExecuteResult.responseContext = responseContext;
		}

		return factoryExecuteResult;
	}

	/**
	 * サーブレットレスポンス設定。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param bean ビーンオブジェクト
	 * @param responseContext レスポンスコンテキスト
	 * @throws Exception 例外
	 */
	protected void setServletResponse(HttpServletRequest request, HttpServletResponse response, BeanInterface bean,
			ResponseContext responseContext) throws Exception {

		String responseType = responseContext.getResponseType();

		// Webページ遷移の場合
		if (responseType.equals(ResponseContext.RESPONSE_TYPE_WEB)) {
			responseContext.setContentsType("text/html");
			
			// -----------------------------------
			// リダイレクト遷移の場合
			// -----------------------------------
			if (responseContext.isRedirectFlg()) {
				RedirectContext redirectContext = responseContext.getRedirectContext();

				// HTTPヘッダをセット
				HashMap<String, String> headerMap = redirectContext.getHeaderMap();
				if (headerMap.size() != 0) {
					for (String headerKey : headerMap.keySet()) {
						String headerParam = headerMap.get(headerKey);
						response.setHeader(headerKey, headerParam);
					}
				}

				String redirectURL = SystemUtil.getRedirectURL(request, responseContext);

				// フレームワーク形式のページにリダイレクトする場合
				if (!StringUtil.isNull(redirectContext.getRedirectURL())) {
					response.sendRedirect(redirectURL);

					// 他サイト等のURLへ直接リダイレクトする場合
				} else {
					response.setHeader("Location", redirectURL);
				}

				// -----------------------------------
				// Webページフォワードの場合
				// -----------------------------------
			} else {
				// 遷移先を取得
				String forwardPath = getForwardPath(responseContext);

				// ビーンオブジェクトをリクエストにセット
				request.setAttribute(CommonDefine.BEAN, bean);

				// リクエストに任意のオブジェクトをセット
				setExRequestAttribute(request, responseContext, bean);

				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPath);
				dispatcher.forward(request, response);
			}

			// -----------------------------------
			// バイナリファイルダウンロードの場合
			// -----------------------------------
		} else if (responseType.equals(ResponseContext.RESPONSE_TYPE_BINARY)) {
			response.setHeader("Content-Type", "charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + responseContext.getExportFileName());
			if (StringUtil.isNull(responseContext.getContentsType()) == false) {
				response.setContentType("application/octet-stream");
			} else {
				response.setContentType(responseContext.getContentsType());
			}
			response.getOutputStream().write(responseContext.getResponseBinary());

			// -----------------------------------
			// プレーンテキスト出力の場合
			// -----------------------------------
		} else if (responseType.equals(ResponseContext.RESPONSE_TYPE_TEXT)) {
			response.setContentType("text/plain; charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Type", "charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + responseContext.getExportFileName());
			PrintWriter out = response.getWriter();
			out.println(responseContext.getResponseText());
			out.close();

			// -----------------------------------
			// CSV出力の場合
			// -----------------------------------
		} else if (responseType.equals(ResponseContext.RESPONSE_TYPE_CSV)) {
			response.setContentType("application/csv; charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Type", "charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + responseContext.getExportFileName());
			PrintWriter out = response.getWriter();
			out.println(responseContext.getResponseCsv());
			out.close();

			// -----------------------------------
			// XML出力の場合
			// -----------------------------------
		} else if (responseType.equals(ResponseContext.RESPONSE_TYPE_XML)) {
			Document document = responseContext.getResponseXML();
			response.setContentType("application/xml; charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Type", "charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + responseContext.getExportFileName());

			PrintWriter out = response.getWriter();

			// DOMの内容をクライアントに出力する
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();
			transformer.transform(new DOMSource(document), new StreamResult(out));
			out.close();

			// -----------------------------------
			// JSON出力の場合
			// -----------------------------------
		} else if (responseType.equals(ResponseContext.RESPONSE_TYPE_JSON)) {
			response.setContentType("application/json; charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Type", "charset=" + responseContext.getResponseEncType());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + responseContext.getExportFileName());

			PrintWriter out = response.getWriter();
			out.println(responseContext.getResponseJson());
			out.close();

		}
	}

	/**
	 * サーブレット直接フォワード レスポンス設定。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param env アクセス環境情報
	 * @param screenId 画面ID
	 * @throws Exception 例外
	 */
	protected void setServletForwardResponse(HttpServletRequest request, HttpServletResponse response,
			Environment env, String screenId) throws Exception {
		String forwardPath = null;
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);
		if (screenContext != null) {
			ResponseContext responseContext = new ResponseContext();
			responseContext.setForwardPath(screenContext.getJspPC());
			forwardPath = getForwardPath(responseContext);

			// ビーンオブジェクトをリクエストにセット(ビジネスロジックを介していないので新規生成)
			BaseBean bean = new BaseBean();
			request.setAttribute(CommonDefine.BEAN,bean);

			// リクエストに任意のオブジェクトをセット
			setExRequestAttribute(request, responseContext, bean);

		} else {
			forwardPath = "/";
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPath);
		dispatcher.forward(request, response);
	}

	/**
	 * 遷移先のJSPを返す。
	 *
	 * @param responseContext レスポンスコンテキスト
	 * @return 遷移対象JSPパス
	 */
	protected String getForwardPath(ResponseContext responseContext) {
		return responseContext.getForwardPath();
	}

	/**
	 * リクエストに任意のオブジェクトをセットする。<br>
	 * 継承先クラスでオーバーライドして任意実装。
	 *
	 * @param request リクエストオブジェクト
	 * @param responseContext レスポンスコンテキスト
	 * @param bean ビーンオブジェクト
	 */
	protected void setExRequestAttribute(HttpServletRequest request, ResponseContext responseContext, BeanInterface bean) {
		// 継承先で任意実装
		return;
	}

	/**
	 * 事前設定処理<br>
	 * 継承先クラスでオーバーライドして任意実装。
	 */
	protected void setPreSetting() {
		return;
	}

	/**
	 * アクセス環境情報を生成して返す。<br>
	 * 継承先クラスで任意拡張。
	 *
	 * @return アクセス環境情報
	 */
	protected Environment createEnviroment() {
		Environment env = new Environment();
		return (Environment) env;
	}

	/**
	 * アクセス環境情報に追加情報をセットする<br>
	 * 継承先クラスで任意拡張。
	 *
	 * @param env アクセス環境情報
	 */
	protected void setExEnvironmentParam(Environment env) {
		env.setDocBase(getServletContext().getRealPath("/"));
	}

	/**
	 * サーブレット設定の初期値にパラメータを設定している場合の設定値の反映。
	 *
	 * @param reqParamMap フォームパラメータ格納マップ
	 * @throws FwException 例外
	 */
	protected void checkServletInitParam(HashMap<String, String> reqParamMap) throws FwException {
		String targetName = reqParamMap.get(CommonDefine.TARGET);
		String executeName = reqParamMap.get(CommonDefine.EXECUTE);

		if (StringUtil.isNull(targetName) == false) {

			// サーブレット初期設定値を取得
			String initTarget = super.getInitParameter(CommonDefine.TARGET);
			if (StringUtil.isNull(initTarget)) {
				targetName = initTarget;

				// サーブレット初期設定値にパラメータ1が付加されていた場合
				String initParam1 = super.getInitParameter(CommonDefine.PARAM1);
				if (StringUtil.isNull(initParam1)) {
					reqParamMap.put(CommonDefine.PARAM1, initParam1);

				}
				// サーブレット初期設定値にパラメータ2が付加されていた場合
				String initParam2 = super.getInitParameter(CommonDefine.PARAM2);
				if (StringUtil.isNull(initParam2)) {
					reqParamMap.put(CommonDefine.PARAM2, initParam2);
				}
			} else {
				targetName = SystemConfigMng.getDefaultTarget();
			}
		}

		if (StringUtil.isNull(executeName) == false) {
			// サーブレット初期設定値を取得
			String initAction = super.getInitParameter(CommonDefine.EXECUTE);
			if (StringUtil.isNull(initAction) == false) {
				executeName = CommonDefine.START;
			} else {
				executeName = initAction;
			}
		}

		if (StringUtil.isNull(targetName) == false) {
			targetName = SystemConfigMng.getDefaultTarget();
		}

		reqParamMap.put(CommonDefine.TARGET, targetName);
		reqParamMap.put(CommonDefine.EXECUTE, executeName);

	}

	/**
	 * 実行エラー時のレスポンス設定。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param e 例外情報
	 */
	protected void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable e) {
		try {
			FwException fwEx = null;
			String trace = null;
			if (e instanceof FwException) {
				fwEx = (FwException) e;
				trace = StringUtil.convToHtmlKaigyo(SystemUtil.getStackTraceString(fwEx.getCatchException()));
			} else {
				trace = StringUtil.convToHtmlKaigyo(SystemUtil.getStackTraceString(e));
			}

			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>Internal server error</title></head><body>");
			out.println("<h2>500 Internal server error</h2>");
			out.println("<i>LaBee framework version:" + CommonDefine.VERSION + "</i><hr>");

			if (e instanceof FwException) {
				out.println("<p><b>" + fwEx.getCatchException().getClass().getName() + "</b></p>");
				out.println("<p><b>" + fwEx.getAppMsg() + "</b></p>");

			} else {
				out.println("<p><b>" + e.getClass().getName() + "</b></p>");
				out.println("<p><b>" + e.getMessage() + "</b></p>");
			}

			out.println(trace);
			out.println("</body></html>");
			out.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * ビジネスロジック実行結果用インナークラス。
	 */
	public class FactoryExecuteResult {

		/** 基底ビーンインターフェース */
		public BeanInterface bean;

		/** レスポンスコンテキスト */
		public ResponseContext responseContext;

	}

}
