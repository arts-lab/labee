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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.base.BaseBean;

/**
 * パスワード カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: PasswordTag.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class PasswordTag  extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -4687037503389776432L;

	/** デフォルト スタイルシートクラス */
	public static String DEFAULT_TEXTBOX_CSS = "fwTextBox";

	/** デフォルト エラー時スタイルシートクラス */
	public static String DEFAULT_ERROR_TEXTBOX_CSS = "fwTextAreaError";

	/** テキストボックス値 */
	protected String value = null;

	/** テキストボックス名 */
	protected String name = null;

	/** オブジェクトID */
	protected String objId = null;

	/** 表示サイズ数 */
	protected String size = null;

	/** 入力最大値 */
	protected String maxlength = null;

	/** スタイルシートクラス */
	protected String styleClass = null;

	/** スタイルシート直接記述値 */
	protected String style = null;

	/** Tabインデックス */
	protected String tabIndex = null;

	/** オートコンプリート */
	protected String autoComp = null;

	/** 有効・無効 */
	protected String desabled = null;

	/** ビーンオブジェクト */
	protected BaseBean bean = null;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		value = null;
		name = null;
		objId = null;
		size = null;
		maxlength = null;
		styleClass = null;
		style = null;
		tabIndex = null;
		autoComp = null;
		desabled = null;
		bean = null;
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
			pageContext.getOut().print(makePassword());
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
	private String makePassword() {
		StringBuilder buf = new StringBuilder();

		buf.append("<input type=\"password\" ");

		if (StringUtil.isNull(name)) {
			buf.append("name=\"" + name + "\" ");
		}

		if (StringUtil.isNull(objId)) {
			buf.append("id=\"" + objId + "\" ");
		}

		if (StringUtil.isNull(value)) {
			buf.append("value=\"" + value + "\" ");
		} else {
			buf.append("value=\"\" ");
		}

		if (StringUtil.isNull(size) && StringUtil.isNumber(size)) {
			buf.append("size=\"" + size + "\" ");
		}

		if (StringUtil.isNull(maxlength) && StringUtil.isNumber(size)) {
			buf.append("maxlength=\"" + maxlength + "\" ");
		}

		String cssClass = "";
		if (bean.isError(name)) {
			cssClass = DEFAULT_ERROR_TEXTBOX_CSS;
		} else {
			cssClass = DEFAULT_TEXTBOX_CSS;
		}

		if (StringUtil.isNull(styleClass)) {
			buf.append("class=\"" + cssClass + " " + styleClass + "\" ");
		} else {
			buf.append("class=\"" + cssClass + "\" ");
		}

		if (StringUtil.isNull(style)) {
			buf.append("style=\"" + style + "\" ");
		}

		if (StringUtil.isNull(tabIndex) && StringUtil.isNumber(tabIndex)) {
			buf.append("tabIndex=\"" + tabIndex + "\" ");
		}

		if (StringUtil.isNull(autoComp)) {
			if (autoComp.toLowerCase().equals("true")) {
				buf.append("autocomplete=\"on\" ");

			} else if (autoComp.toLowerCase().equals("false")) {
				buf.append("autocomplete=\"off\" ");
			}
		}

		if (bean.isError(name)) {
			buf.append(" onClick=\"this.className='" + DEFAULT_TEXTBOX_CSS);

			if (StringUtil.isNull(styleClass)) {
				buf.append(" " + styleClass + "'\" ");
			} else {
				buf.append("'\" ");
			}

			
			
		}

		if (StringUtil.isNull(desabled)) {
			if (desabled.toLowerCase().equals("true")) {
				buf.append("disabled=\"disabled\" ");
			}
		}

		buf.append(">");

		return buf.toString();
	}

	/**
	 * @param dEFAULT_TEXTBOX_CSS セットする dEFAULT_TEXTBOX_CSS
	 */
	public static void setDEFAULT_TEXTBOX_CSS(String dEFAULT_TEXTBOX_CSS) {
		DEFAULT_TEXTBOX_CSS = dEFAULT_TEXTBOX_CSS;
	}

	/**
	 * @param dEFAULT_ERROR_TEXTBOX_CSS セットする dEFAULT_ERROR_TEXTBOX_CSS
	 */
	public static void setDEFAULT_ERROR_TEXTBOX_CSS(String dEFAULT_ERROR_TEXTBOX_CSS) {
		DEFAULT_ERROR_TEXTBOX_CSS = dEFAULT_ERROR_TEXTBOX_CSS;
	}

	/**
	 * @param value セットする value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param objId セットする objId
	 */
	public void setObjId(String objId) {
		this.objId = objId;
	}

	/**
	 * @param size セットする size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @param maxlength セットする maxlength
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * @param styleClass セットする styleClass
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * @param style セットする style
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @param tabIndex セットする tabIndex
	 */
	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @param autoComp セットする autoComp
	 */
	public void setAutoComp(String autoComp) {
		this.autoComp = autoComp;
	}

	/**
	 * @param desabled セットする desabled
	 */
	public void setDesabled(String desabled) {
		this.desabled = desabled;
	}

	/**
	 * @param bean セットする bean
	 */
	public void setBean(BaseBean bean) {
		this.bean = bean;
	}


}
