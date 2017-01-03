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
 * JSP URL生成タグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: JspLinkTab.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class JspLinkTab extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 357242389220811735L;

	/** 画面ID */
	protected String screenId;

	/**	リンク文字列	*/
	protected String linkStr;
	
	/**	付加パラメータ	*/
	protected String addParamStr;

	/** リクエストスコープマップ */
	protected Map<String, Object> requestScopeMap;
	
	/**	URLオンリーフラグ	*/
	protected String urlOnly;

	/** SSLフラグ */
	protected String sslFlg;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		screenId = null;
		linkStr = null;
		sslFlg = null;
		urlOnly = null;
		addParamStr = null;
	}

	/**
	 * @param linkStr セットする linkStr
	 */
	public void setLinkStr(String linkStr) {
		this.linkStr = linkStr;
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
			pageContext.getOut().print(makeLinkTab());
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
	 * JSP URL生成
	 *
	 * @return
	 */
	private String makeLinkTab() {
		AppEnvironmentBean envBean = (AppEnvironmentBean) requestScopeMap.get(CommonDefine.ENV_BEAN);
		Environment env = envBean.getEnv();
		StringBuilder buf = new StringBuilder();

		boolean urlOnlyFlg = false;
		if ((StringUtil.isNull(urlOnly) && urlOnly.trim().toLowerCase().equals("true"))) {
			urlOnlyFlg = true;
		}
				
		if (urlOnlyFlg == false) {
			buf.append("<a href=\"");
		}

		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);
		if (screenContext != null) {
			if ((StringUtil.isNull(sslFlg) && sslFlg.trim().toLowerCase().equals("true"))
					|| screenContext.isSslFlg()) {
				buf.append(SystemConfigMng.getHttpsPath());
				buf.append(env.getServerName());
				buf.append(":" + SystemConfigMng.getHttpsPort());
			} else {
				buf.append(SystemConfigMng.getHttpPath());
				buf.append(env.getServerName());
				buf.append(":" + SystemConfigMng.getHttpPort());
			}

			buf.append(env.getWebAppPath());
			buf.append(env.getContextPath());
			buf.append("?" + CommonDefine.SCREEN_ID + "=" + screenId);
			
			if (StringUtil.isNull(addParamStr)) {
				buf.append("&" + addParamStr);
			}
			buf.toString();
		} else {
			buf.append("#");
		}

		if (urlOnlyFlg == false) {
			buf.append("\">");
			buf.append(linkStr);
			buf.append("</a>");
		}
		
		return buf.toString();
	}

	/**
	 * @param screenId セットする screenId
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	/**
	 * @param requestScopeMap セットする requestScopeMap
	 */
	public void setRequestScope(Map<String, Object> requestScopeMap) {
		this.requestScopeMap = requestScopeMap;
	}

	/**
	 * @param sslFlg セットする sslFlg
	 */
	public void setSslFlg(String sslFlg) {
		this.sslFlg = sslFlg;
	}

	/**
	 * @param urlOnly セットする urlOnly
	 */
	public void setUrlOnly(String urlOnly) {
		this.urlOnly = urlOnly;
	}

	/**
	 * @param addParamStr セットする addParamStr
	 */
	public void setAddParamStr(String addParamStr) {
		this.addParamStr = addParamStr;
	}


}
