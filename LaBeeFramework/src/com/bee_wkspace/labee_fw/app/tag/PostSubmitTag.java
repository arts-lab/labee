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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;

/**
 * POSTサブミットリンク カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: PostSubmitTag.java 566 2016-08-16 00:16:36Z pjmember $
 */
public class PostSubmitTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 8639233186846569341L;

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

	/** フォーム名 */
	protected String formName = null;

	/** 確認ダイアログ表示フラグ */
	protected String confirmFlg = null;

	/** 確認ダイアログ表示メッセージ */
	protected String confirmMsg = null;

	/** 確認ダイアログ表示メッセージラベルコード */
	protected String confirmLabelCd;

	/** 言語ロケールコード */
	protected String localeCd;

	/** ワードラベル区分 */
	protected String labelDiv;

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
		formName = null;
		confirmFlg = null;
		confirmMsg = null;
		confirmLabelCd = null;
		localeCd = null;
		labelDiv = null;
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
			pageContext.getOut().print(makePostSubmit());
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
	 * POSTサブミットリンク描画
	 *
	 * @return
	 */
	private String makePostSubmit() {
		StringBuilder buf = new StringBuilder();

		if (StringUtil.isNull(execute) == false) {
			execute = "start";
		}

		if (StringUtil.isNull(localeCd) == false) {
			localeCd = SystemConfigMng.getSystemLocale().getLanguage();
		}

		if (StringUtil.isNull(labelDiv) == false) {
			labelDiv = "global";
		}

		String[] tgtDatas = target.split("\\.");
		String screenId = tgtDatas[tgtDatas.length - 1];
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);

		
		if (StringUtil.isNull(confirmFlg) && confirmFlg.trim().toLowerCase().equals("true")) {
			if (StringUtil.isNull(param1)) {
				buf.append(";setParam1(" + formName + ",'" + param1 + "')");
			}
			if (StringUtil.isNull(param2)) {
				buf.append(";setParam2(" + formName + ",'" + param2 + "')");
			}
			if (StringUtil.isNull(param3)) {
				buf.append(";setParam3(" + formName + ",'" + param3 + "')");
			}

			if (screenContext != null && screenContext.isSslFlg()) {
				buf.append(";setSubmitFormSSL");
			} else {
				buf.append(";setSubmitForm");
			}
			
			buf.append("(" + formName + ",'" + target + "','" + execute + "') ");

			buf.append(";showConfirmDialogExec(");

			if (StringUtil.isNull(confirmMsg)) {
				buf.append("'" + confirmMsg + "','");
			} else {
				if (StringUtil.isNull(confirmLabelCd)) {
					buf.append("'" + MessageLabelUtil.getWordLabel(confirmLabelCd, labelDiv, new Locale(localeCd))
							+ "','");
				}
			}

			buf.append(formName + ".submit()')");

		} else {
			
			if (screenContext != null && screenContext.isSslFlg()) {
				buf.append(";submitParamFormSSL");
			} else {
				buf.append(";submitParamForm");
			}
			
			buf.append("(" + formName + ",");
			buf.append("'" + target + "','" + execute + "'");

			if (StringUtil.isNull(param1)) {
				buf.append(",'" + param1 + "'");
			} else {
				buf.append(",''");
			}

			if (StringUtil.isNull(param2)) {
				buf.append(",'" + param2 + "'");
			} else {
				buf.append(",''");
			}
		
			if (StringUtil.isNull(param3)) {
				buf.append(",'" + param3 + "'");
			} else {
				buf.append(",''");
			}

			buf.append(")");
		}
		

		return buf.toString();
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
	 * @param formName formNameの値をセットする。
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * @param confirmFlg confirmFlgの値をセットする。
	 */
	public void setConfirmFlg(String confirmFlg) {
		this.confirmFlg = confirmFlg;
	}

	/**
	 * @param confirmMsg confirmMsgの値をセットする。
	 */
	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}



	/**
	 * @param confirmLabelCd セットする confirmLabelCd
	 */
	public void setConfirmLabelCd(String confirmLabelCd) {
		this.confirmLabelCd = confirmLabelCd;
	}

	/**
	 * @param localeCd localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}

	/**
	 * @param labelDiv labelDivの値をセットする。
	 */
	public void setLabelDiv(String labelDiv) {
		this.labelDiv = labelDiv;
	}

}
