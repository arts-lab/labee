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
package com.bee_wkspace.labee_fw.app.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bee_wkspace.labee_fw.app.base.AppBaseBean;
import com.bee_wkspace.labee_fw.app.base.AppEnvironmentBean;
import com.bee_wkspace.labee_fw.app.context.AppResponseContext;
import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.base.BeanInterface;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;
import com.bee_wkspace.labee_fw.core.servlet.BaseControlerServlet;

/**
 * Webアプリ用コントローラサーブレット。<br>
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
 * <br>
 * 基底クラスcom.bee_wkspace.labee_fw.core.servlet.BaseControlerServletに対して、
 * 本クラスは主にポップアップ画面の<br>
 * レスポンス処理の機能追加を行なっている。<br>
 * <br>
 * 本サーブレットをURLで呼びだすにはドメインパス以降に<b>/ctrl</b>のパスを付加する。<br>
 * <br>
 * 実装者が機能拡張する場合は本クラスを継承したサーブレットを実装する。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: AppControlerServlet.java 554 2016-08-12 21:19:00Z pjmember $
 */
@WebServlet(name = "AppControlerServlet", urlPatterns = { "/ctrl" })
public class AppControlerServlet extends BaseControlerServlet {

	private static final long serialVersionUID = -875327631088609487L;

	/**
	 * サーブレット初期化
	 *
	 * @see com.bee_wkspace.labee_fw.core.servlet.BaseControlerServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * リクエストに任意のオブジェクトをセット
	 *
	 * @param request リクエスト情報
	 * @param responseContext レスポンスコンテキスト
	 * @param bean ビーンオブジェクト
	 * @see com.bee_wkspace.labee_fw.core.servlet.BaseControlerServlet#setExRequestAttribute(javax.servlet.http.HttpServletRequest, com.bee_wkspace.labee_fw.core.context.ResponseContext, com.bee_wkspace.labee_fw.core.base.BeanInterface)
	 */
	@Override
	protected void setExRequestAttribute(HttpServletRequest request, ResponseContext responseContext, BeanInterface bean) {

		AppEnvironmentBean envBean = new AppEnvironmentBean();
		AppBaseBean appBaseBean = (AppBaseBean) bean;
		envBean.setEnv(appBaseBean.getEnv());
		envBean.setPasswdSetNameList(appBaseBean.getPasswdSetNameList());

		AppResponseContext appResponseContext = (AppResponseContext) responseContext.getAdditionalObject();
		if (appResponseContext != null) {
			// ポップアップから親画面に遷移する場合
			if (appResponseContext.isPopupToParentFlg()) {
				envBean.setPopupToParentContext(appResponseContext.getPopupToParentContext());
			}
		}

		// 環境情報をリクエストにセット
		request.setAttribute(CommonDefine.ENV_BEAN, envBean);

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
	@Override
	protected void setServletForwardResponse(HttpServletRequest request, HttpServletResponse response,
			Environment env, String screenId) throws Exception {
		String forwardPath = null;
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);
		if (screenContext != null) {
			ResponseContext responseContext = new ResponseContext();
			responseContext.setEnv(env);

			// ブラウザタイトル表示名設定
			if (StringUtil.isNull(screenContext.getViewTitle()) == false) {
				if (StringUtil.isNull(screenContext.getLabelCd())) {
					screenContext.setViewTitle(MessageLabelUtil.getWordLabel(screenContext.getLabelCd(),
							screenContext.getLabelDiv()));

				} else {
					screenContext.setViewTitle(screenContext.getScreenName());
				}
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
			forwardPath = getForwardPath(responseContext);

			// ビーンオブジェクトをリクエストにセット(ビジネスロジックを介していないので新規生成)
			AppBaseBean bean = new AppBaseBean();
			bean.setScreenContext(screenContext);
			bean.setEnv(env);
			request.setAttribute(CommonDefine.BEAN, bean);

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
	@Override
	protected String getForwardPath(ResponseContext responseContext) {
		return "/WEB-INF/" + responseContext.getForwardPath();
	}

}
