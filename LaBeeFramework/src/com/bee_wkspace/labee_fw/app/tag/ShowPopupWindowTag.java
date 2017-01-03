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
import java.util.Locale;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.app.base.AppEnvironmentBean;
import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;

/**
 * ポップアップウインドウ表示 カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: ShowPopupWindowTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class ShowPopupWindowTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 6062629818990113273L;

	/** ターゲット名 */
	protected String target;

	/** 処理名 */
	protected String execute;

	/** パラメータ1 */
	protected String param1;

	/** パラメータ2 */
	protected String param2;

	/** パラメータ3 */
	protected String param3;

	/** ポップアップウインドウ表示領域幅 */
	protected String width;

	/** ポップアップウインドウ表示領域高さ */
	protected String height;

	/** 実行ボタン表示名 */
	protected String entryButtonName;

	/** 閉じるボタン表示名 */
	protected String closeButtonName;

	/** 実行ボタン ワードラベルコード */
	protected String entryBtnLabelCd;

	/** 閉じるボタン ワードラベルコード */
	protected String closeBtnLabelCd;

	/** 実行ボタンで実行するフォーム名 */
	protected String nextForm;

	/** 実行ボタンで実行するターゲット名 */
	protected String nextTarget;

	/** 実行ボタンで実行する処理名 */
	protected String nextExecute;

	/** 実行ボタンの実行先に渡すパラメータ1 */
	protected String nextParam1;

	/** 実行ボタンの実行先に渡すパラメータ2 */
	protected String nextParam2;

	/** 実行ボタンの実行先に渡すパラメータ3 */
	protected String nextParam3;

	/** 言語ロケールコード */
	protected String localeCd;

	/** ワードラベル区分 */
	protected String labelDiv;

	/** リクエストスコープマップ */
	protected Map<String, Object> requestScopeMap;


	/**
	 *	タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		target= null;
		execute= null;
		param1= null;
		param2= null;
		param3= null;
		width= null;
		height= null;
		entryButtonName= null;
		closeButtonName= null;
		entryBtnLabelCd= null;
		closeBtnLabelCd= null;
		nextForm= null;
		nextTarget= null;
		nextExecute= null;
		nextParam1= null;
		nextParam2= null;
		nextParam3= null;
		localeCd= null;
		labelDiv = null;
	}

	/* (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	@Override
	public void doCatch(Throwable arg0) throws Throwable {
			throw arg0;
	}


	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(makeShowPopupWindow());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeShowPopupWindow() {
		AppEnvironmentBean envBean = (AppEnvironmentBean) requestScopeMap.get(CommonDefine.ENV_BEAN);
		Environment env = envBean.getEnv();

		String[] tgtDatas = target.split("\\.");
		String screenId = tgtDatas[tgtDatas.length - 1];
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);
		if (screenContext == null) {
			screenContext = new ScreenContext();
		}
			

		if (StringUtil.isNull(localeCd) == false) {
			localeCd = SystemConfigMng.getSystemLocale().getLanguage();
		}

		if (StringUtil.isNull(labelDiv) == false) {
			labelDiv = "global";
		}

		StringBuilder url = new StringBuilder();
		if (screenContext.isSslFlg()) {
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

		url.append("?" + CommonDefine.TARGET + "=" + target);
		url.append("&" + CommonDefine.EXECUTE + "=" + execute);
		
		if (StringUtil.isNull(param1)) {
			url.append("&" + CommonDefine.PARAM1 + "=" + param1);
			
			if (StringUtil.isNull(param2)) {
				url.append("&" + CommonDefine.PARAM2 + "=" + param2);
			
				if (StringUtil.isNull(param3)) {
					url.append("&" + CommonDefine.PARAM3 + "=" + param3);
				}
			}
		}

		if (StringUtil.isNull(screenContext.getLabelCd())) {
			screenContext.setViewTitle(MessageLabelUtil.getWordLabel(screenContext.getLabelCd(),
					screenContext.getLabelDiv(), new Locale(localeCd)));

		} else {
			screenContext.setViewTitle(screenContext.getScreenName());
		}
		
		
		StringBuilder buf = new StringBuilder();
		buf.append("showPopupWindow(");
		buf.append("'" + url.toString() + "', ");
		buf.append("'" + screenContext.getViewTitle() + "', ");
		buf.append(width + ", ");
		buf.append(height + ", ");

		if (StringUtil.isNull(entryButtonName)) {
			buf.append("'" + entryButtonName + "', ");
		} else {
			if (StringUtil.isNull(entryBtnLabelCd)) {
				buf.append("'" + MessageLabelUtil.getWordLabel(entryBtnLabelCd, labelDiv, new Locale(localeCd)) + "', ");
			} else{
				buf.append("'', ");
			}
		}

		if (StringUtil.isNull(closeButtonName)) {
			buf.append("'" + closeButtonName + "', ");
		} else {
			if (StringUtil.isNull(closeBtnLabelCd)) {
				buf.append("'" + MessageLabelUtil.getWordLabel(closeBtnLabelCd, labelDiv, new Locale(localeCd)) + "', ");
			}
		}

		buf.append("'" + nextForm + "', ");
		buf.append("'" + nextTarget + "', ");
		buf.append("'" + nextExecute + "'");

		if (StringUtil.isNull(nextParam1)) {
			buf.append(",'" + nextParam1 + "'");

			if (StringUtil.isNull(nextParam2)) {
				buf.append(",'" + nextParam2 + "'");

				if (StringUtil.isNull(nextParam3)) {
					buf.append(",'" + nextParam3 + "'");
				}
			}
		}
		buf.append(",'','');");

		return buf.toString();
	}

	/**
	 * @param target セットする target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @param execute セットする execute
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}

	/**
	 * @param param1 セットする param1
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * @param param2 セットする param2
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * @param param3 セットする param3
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	/**
	 * @param width セットする width
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @param height セットする height
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @param nextTarget セットする nextTarget
	 */
	public void setNextTarget(String nextTarget) {
		this.nextTarget = nextTarget;
	}

	/**
	 * @param nextExecute セットする nextExecute
	 */
	public void setNextExecute(String nextExecute) {
		this.nextExecute = nextExecute;
	}

	/**
	 * @param entryButtonName セットする entryButtonName
	 */
	public void setEntryButtonName(String entryButtonName) {
		this.entryButtonName = entryButtonName;
	}

	/**
	 * @param closeButtonName セットする closeButtonName
	 */
	public void setCloseButtonName(String closeButtonName) {
		this.closeButtonName = closeButtonName;
	}

	/**
	 * @param nextParam1 セットする nextParam1
	 */
	public void setNextParam1(String nextParam1) {
		this.nextParam1 = nextParam1;
	}

	/**
	 * @param nextParam2 セットする nextParam2
	 */
	public void setNextParam2(String nextParam2) {
		this.nextParam2 = nextParam2;
	}

	/**
	 * @param nextParam3 セットする nextParam3
	 */
	public void setNextParam3(String nextParam3) {
		this.nextParam3 = nextParam3;
	}

	/**
	 * @param entryBtnLabelCd entryBtnLabelCdの値をセットする。
	 */
	public void setEntryBtnLabelCd(String entryBtnLabelCd) {
		this.entryBtnLabelCd = entryBtnLabelCd;
	}

	/**
	 * @param closeBtnLabelCd closeBtnLabelCdの値をセットする。
	 */
	public void setCloseBtnLabelCd(String closeBtnLabelCd) {
		this.closeBtnLabelCd = closeBtnLabelCd;
	}

	/**
	 * @param localeCd localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}

	/**
	 * @param labelDiv  labelDivの値をセットする。
	 */
	public void setLabelDiv(String labelDiv) {
		this.labelDiv = labelDiv;
	}

	/**
	 * @param nextForm  nextFormの値をセットする。
	 */
	public void setNextForm(String nextForm) {
		this.nextForm = nextForm;
	}

	/**
	 * @param requestScopeMap セットする requestScopeMap
	 */
	public void setRequestScope(Map<String, Object> requestScopeMap) {
		this.requestScopeMap = requestScopeMap;
	}
}
