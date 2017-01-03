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
package com.bee_wkspace.labee_fw.app.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.app.base.AppEnvironmentBean;
import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;

/**
 * GET URLカスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: GetUrlTag.java 566 2016-08-16 00:16:36Z pjmember $
 */
public class GetUrlTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -6914664655130459457L;

	/** 呼び出しターゲット名 */
	protected String target = null;

	/** 実行処理名 */
	protected String execute = null;

	/** パラメータ1 */
	protected String param1 = null;

	/** パラメータ2 */
	protected String param2 = null;

	/** パラメータ3 */
	protected String param3 = null;

	/** SSLフラグ */
	protected String sslFlg;

	/** リクエストスコープマップ */
	protected Map<String, Object> requestScopeMap;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		target = null;
		execute = null;
		param1 = null;
		param2 = null;
		param3 = null;
		sslFlg = null;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	@Override
	public void doCatch(Throwable arg0) throws Throwable {
		throw arg0;
	}

	/**
	 * 開始タグ。
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(makeGetLink());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	/**
	 * 終了タグ。
	 */
	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	/**
	 * GETリンク描画
	 *
	 * @return
	 */
	private String makeGetLink() {
		AppEnvironmentBean envBean = (AppEnvironmentBean) requestScopeMap.get(CommonDefine.ENV_BEAN);

		if (StringUtil.isNull(execute) == false) {
			execute = "start";
		}

		Environment env = envBean.getEnv();

		String[] tgtDatas = target.split("\\.");
		String screenId = tgtDatas[tgtDatas.length - 1];
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);
		
		StringBuilder url = new StringBuilder();
		if ((StringUtil.isNull(sslFlg) && sslFlg.trim().toLowerCase().equals("true"))
			|| (screenContext != null && screenContext.isSslFlg())) {
			
			url.append(SystemConfigMng.getHttpsPath());
			url.append(env.getServerName());
			url.append(":" + SystemConfigMng.getHttpsPort());
		} else {
			url.append(SystemConfigMng.getHttpPath());
			url.append(env.getServerName());
			url.append(":" + SystemConfigMng.getHttpPort());
		}

		url.append(env.getWebAppPath());
		url.append(env.getContextPath());
		url.append("?" + CommonDefine.TARGET + "=" + target + "&");
		url.append(CommonDefine.EXECUTE + "=" + execute);

		if (StringUtil.isNull(param1)) {
			url.append("&" + CommonDefine.PARAM1 + "=" + param1);

			if (StringUtil.isNull(param2)) {
				url.append("&" + CommonDefine.PARAM2 + "=" + param2);

				if (StringUtil.isNull(param3)) {
					url.append("&" + CommonDefine.PARAM3 + "=" + param3);
				}
			}
		}

		return url.toString();
	}

	/**
	 * @param target targetの値をセットする。
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @param execute executeの値をセットする。
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}

	/**
	 * @param param1 param1の値をセットする。
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * @param param2 param2の値をセットする。
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * @param param3 param3の値をセットする。
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	/**
	 * @param sslFlg sslFlgの値をセットする。
	 */
	public void setSslFlg(String sslFlg) {
		this.sslFlg = sslFlg;
	}

	/**
	 * @param requestScopeMap セットする requestScopeMap
	 */
	public void setRequestScope(Map<String, Object> requestScopeMap) {
		this.requestScopeMap = requestScopeMap;
	}

}
