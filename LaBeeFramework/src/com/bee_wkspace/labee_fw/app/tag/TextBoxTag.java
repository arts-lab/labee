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
 * テキストボックス カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: TextBoxTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class TextBoxTag extends TagSupport implements TryCatchFinally {

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

	/** リードオンリー */
	protected String readOnly = null;

	/** Tabインデックス */
	protected String tabIndex = null;

	/** オートコンプリート */
	protected String autoComp = null;

	/** NULL時設定値 */
	protected String nullValue = "";

	/** IMEモード */
	protected String imeMode = null;

	/** 有効・無効 */
	protected String desabled = null;

	/** OnClick処理 */
	protected String onClick = null;

	/** OnKeydown処理 */
	protected String onKeydown = null;
	
	/** onkeypress処理 */
	protected String onKeypress = null;

	/** onchange処理 */
	protected String onChange = null;

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
		readOnly = null;
		tabIndex = null;
		autoComp = null;
		nullValue = "";
		imeMode = null;
		desabled = null;
		onClick = null;
		onKeydown = null;
		onKeypress = null;
		onChange = null;
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
			pageContext.getOut().print(makeTextBox());
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
	private String makeTextBox() {
		StringBuilder buf = new StringBuilder();

		buf.append("<input type=\"text\" ");

		if (StringUtil.isNull(name)) {
			buf.append("name=\"" + name + "\" ");
		}

		if (StringUtil.isNull(objId)) {
			buf.append("id=\"" + objId + "\" ");
		}

		if (StringUtil.isNull(value)) {
			buf.append("value=\"" + value + "\" ");
		} else {
			buf.append("value=\"" + nullValue + "\" ");
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
			buf.append("style=\"" + style);

			if (StringUtil.isNull(imeMode)) {
				buf.append(";ime-mode:" + imeMode + ";\" ");

			} else {
				buf.append("\" ");
			}

		} else {
			if (StringUtil.isNull(imeMode)) {
				buf.append("style=\"ime-mode:" + imeMode + "\" ");
			}
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
			String onClickStr = " onClick=\"this.className='" + DEFAULT_TEXTBOX_CSS;
			if (StringUtil.isNull(styleClass)) {
				onClickStr = onClickStr + " " + styleClass + "' ";
			} else {
				onClickStr = onClickStr + "' ";
			}
			
			if (StringUtil.isNull(onClick)) {
				buf.append(onClickStr + onClick + "\" ");
			} else {
				buf.append(onClickStr + "\" ");
			}
		} else {
			if (StringUtil.isNull(onClick)) {
				buf.append("onClick=\"" + onClick + "\" ");
			}
		}

		if (StringUtil.isNull(onKeydown)) {
			buf.append("onKeydown=\"" + onKeydown + "\" ");
		}		
		
		if (StringUtil.isNull(onKeypress)) {
			buf.append("onKeypress=\"" + onKeypress + "\" ");
		}		

		if (StringUtil.isNull(onChange)) {
			buf.append("onChange=\"" + onChange + "\" ");
		}		

		if (StringUtil.isNull(desabled)) {
			if (desabled.toLowerCase().equals("true")) {
				buf.append("disabled=\"disabled\" ");
			}
		}

		if (StringUtil.isNull(readOnly)) {
			if (readOnly.toLowerCase().equals("readonly")) {
				buf.append("readOnly ");
			}
		}

		buf.append(">");

		return buf.toString();
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public void setAutoComp(String autoComp) {
		this.autoComp = autoComp;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public void setImeMode(String imeMode) {
		this.imeMode = imeMode;
	}

	public void setDesabled(String desabled) {
		this.desabled = desabled;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public void setBean(BaseBean bean) {
		this.bean = bean;
	}

	public void setOnKeydown(String onKeydown) {
		this.onKeydown = onKeydown;
	}

	public void setOnKeypress(String onKeypress) {
		this.onKeypress = onKeypress;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}


}
