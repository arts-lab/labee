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
import com.bee_wkspace.labee_fw.core.SystemConfigMng;

/**
 * サブミットボタン カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: SubmitButtonTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class SubmitButtonTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 2259026383149079086L;

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

	/** ボタン表示値 */
	protected String value = null;

	/** ボタン名 */
	protected String name = null;

	/** オブジェクトID */
	protected String objId = null;

	/** スタイルシートクラス */
	protected String styleClass = null;

	/** スタイルシート直接記述値 */
	protected String style = null;

	/**	ボタンタイプ	*/
	protected String type = null;
	
	/** Tabインデックス */
	protected String tabIndex = null;

	/** 有効・無効 */
	protected String desabled = null;

	/** OnClick処理 */
	protected String onClick = null;

	/** 確認ダイアログ表示フラグ */
	protected String confirmFlg = null;

	/** 確認ダイアログ表示メッセージ */
	protected String confirmMsg = null;

	/** 確認ダイアログ表示メッセージラベルコード */
	protected String confirmLabelCd;

	/** 確認ダイアログワードラベル区分 */
	protected String confirmLabelDiv;

	/** ボタン表示ラベルコード */
	protected String buttonLabelCd;

	/** ワードラベル区分 */
	protected String buttonLabelDiv;

	/** 言語ロケールコード */
	protected String localeCd;

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
		value = null;
		name = null;
		objId = null;
		styleClass = null;
		style = null;
		tabIndex = null;
		desabled = null;
		onClick = null;
		buttonLabelCd = null;
		confirmLabelCd = null;
		confirmFlg = null;
		confirmLabelDiv = null;
		confirmMsg = null;
		localeCd = null;
		buttonLabelDiv = null;
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
			pageContext.getOut().print(makeSubmitButton());
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
	 * テキストボックスタグ描画
	 *
	 * @return
	 */
	private String makeSubmitButton() {
		StringBuilder buf = new StringBuilder();

		if (StringUtil.isNull(localeCd) == false) {
			localeCd = SystemConfigMng.getSystemLocale().getLanguage();
		}

		if (StringUtil.isNull(buttonLabelDiv) == false) {
			buttonLabelDiv = "global";
		}

		if (StringUtil.isNull(confirmLabelDiv) == false) {
			confirmLabelDiv = "global";
		}

		buf.append("<button ");

		if (StringUtil.isNull(name)) {
			buf.append("name=\"" + name + "\" ");
		}

		if (StringUtil.isNull(objId)) {
			buf.append("id=\"" + objId + "\" ");
		}

		if (StringUtil.isNull(styleClass)) {
			buf.append("class=\"" + styleClass + "\" ");
		}

		if (StringUtil.isNull(style)) {
			buf.append("style=\"" + style + "\" ");
		}

		if (StringUtil.isNull(type)) {
			buf.append("type=\"" + type + "\" ");
		} else {
			buf.append("type=\"button\" ");
		}

		if (StringUtil.isNull(tabIndex) && StringUtil.isNumber(tabIndex)) {
			buf.append("tabIndex=\"" + tabIndex + "\" ");
		}

		if (StringUtil.isNull(execute) == false) {
			execute = "start";
		}

		if (StringUtil.isNull(param1) == false) {
			param1 = "";
		}

		if (StringUtil.isNull(param2) == false) {
			param2 = "";
		}
		if (StringUtil.isNull(param3) == false) {
			param3 = "";
		}

		buf.append(" onClick=\"");

		if (StringUtil.isNull(confirmFlg) && confirmFlg.trim().toLowerCase().equals("true")) {

			buf.append(";showConfirmDialogSubmit(");
			if (StringUtil.isNull(confirmMsg)) {
				buf.append("'" + confirmMsg + "',");

			} else {
				if (StringUtil.isNull(confirmLabelCd)) {
					buf.append("'" + MessageLabelUtil.getWordLabel(confirmLabelCd, confirmLabelDiv, new Locale(localeCd))
							+ "',");
				}
			}
			buf.append(formName + ", ");
			buf.append("'" + target + "','" + execute + "', ");
			buf.append("'" + param1 + "', ");
			buf.append("'" + param2 + "', ");
			buf.append("'" + param3 + "'); ");

		} else {

			buf.append(";submitParamForm(" + formName + ",");

			buf.append("'" + target + "','" + execute + "', ");
			buf.append("'" + param1 + "', ");
			buf.append("'" + param2 + "', ");
			buf.append("'" + param3 + "'); ");

		}

		if (StringUtil.isNull(onClick)) {
			buf.append(onClick);
		}

		buf.append("\" ");

		if (StringUtil.isNull(desabled)) {
			if (desabled.toLowerCase().equals("true")) {
				buf.append("disabled=\"disabled\" ");
			}
		}

		buf.append(">");

		if (StringUtil.isNull(value)) {
			buf.append(value);
		} else {
			if (StringUtil.isNull(buttonLabelCd)) {
				buf.append(MessageLabelUtil.getWordLabel(buttonLabelCd, buttonLabelDiv, new Locale(localeCd)));
			}
		}

		buf.append("</button>");

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
	 * @param value valueの値をセットする。
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param name nameの値をセットする。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param objId objIdの値をセットする。
	 */
	public void setObjId(String objId) {
		this.objId = objId;
	}

	/**
	 * @param styleClass styleClassの値をセットする。
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * @param style styleの値をセットする。
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @param tabIndex tabIndexの値をセットする。
	 */
	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @param desabled desabledの値をセットする。
	 */
	public void setDesabled(String desabled) {
		this.desabled = desabled;
	}

	/**
	 * @param onClick onClickの値をセットする。
	 */
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	/**
	 * @param buttonLabelCd buttonLabelCdの値をセットする。
	 */
	public void setButtonLabelCd(String buttonLabelCd) {
		this.buttonLabelCd = buttonLabelCd;
	}

	/**
	 * @param localeCd localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}

	/**
	 * @param confirmFlg セットする confirmFlg
	 */
	public void setConfirmFlg(String confirmFlg) {
		this.confirmFlg = confirmFlg;
	}

	/**
	 * @param confirmMsg セットする confirmMsg
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
	 * @param confirmLabelDiv セットする confirmLabelDiv
	 */
	public void setConfirmLabelDiv(String confirmLabelDiv) {
		this.confirmLabelDiv = confirmLabelDiv;
	}

	/**
	 * @param buttonLabelDiv セットする buttonLabelDiv
	 */
	public void setButtonLabelDiv(String buttonLabelDiv) {
		this.buttonLabelDiv = buttonLabelDiv;
	}

	/**
	 * @param type セットする type
	 */
	public void setType(String type) {
		this.type = type;
	}

}
