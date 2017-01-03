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
 * テキストエリア カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: TextAreaTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class TextAreaTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 8013194908079749934L;

	/** デフォルト スタイルシートクラス */
	public static String DEFAULT_TEXTAREA_CSS = "fwTextArea";

	/** デフォルト エラー時スタイルシートクラス */
	public static String DEFAULT_ERROR_TEXTAREA_CSS = "fwTextAreaError";

	/** テキストボックス値 */
	protected String value = null;

	/** テキストボックス名 */
	protected String name = null;

	/** オブジェクトID */
	protected String objId = null;

	/** 横文字数 */
	protected String cols = null;

	/** 縦文字数 */
	protected String rows = null;

	/** スタイルシートクラス */
	protected String styleClass = null;

	/** スタイルシート直接記述値 */
	protected String style = null;

	/** リードオンリー */
	protected String readOnly = null;

	/** Tabインデックス */
	protected String tabIndex = null;

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
		cols = null;
		rows = null;
		styleClass = null;
		style = null;
		readOnly = null;
		tabIndex = null;
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

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(makeTextArea());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeTextArea() {
		StringBuilder buf = new StringBuilder();
		String cssClass = "";
		String onClickStr = "";

		buf.append("<textarea ");

		if (StringUtil.isNull(name)) {
			buf.append("name=\"" + name + "\" ");
		}

		if (StringUtil.isNull(objId)) {
			buf.append("id=\"" + objId + "\" ");
		}

		if (StringUtil.isNull(cols) && StringUtil.isNumber(cols)) {
			buf.append("cols=\"" + cols + "\" ");
		}

		if (StringUtil.isNull(rows) && StringUtil.isNumber(rows)) {
			buf.append("rows=\"" + rows + "\" ");
		}

		if (bean.isError(name)) {
			cssClass = DEFAULT_ERROR_TEXTAREA_CSS;
		} else {
			cssClass = DEFAULT_TEXTAREA_CSS;
		}
		if (StringUtil.isNull(styleClass)) {
			buf.append("class=\"" + cssClass + " " + styleClass + "\" ");
		} else {
			buf.append("class=\"" + cssClass + "\" ");
		}

		if (StringUtil.isNull(style)) {
			buf.append("style=\"" + style);

			if (StringUtil.isNull(imeMode)) {
				if (imeMode.toLowerCase().equals("false")) {
					buf.append(" ;ime-mode:disabled;\" ");
				}
			} else {
				buf.append("\" ");
			}

		} else {
			if (StringUtil.isNull(imeMode)) {
				if (imeMode.toLowerCase().equals("false")) {
					buf.append("style=\"ime-mode:disabled;\" ");
				}
			}
		}

		if (StringUtil.isNull(tabIndex) && StringUtil.isNumber(tabIndex)) {
			buf.append("tabIndex=\"" + tabIndex + "\" ");
		}

		if (bean.isError(name)) {
			onClickStr = " onClick=\"this.className='" + DEFAULT_TEXTAREA_CSS;
			
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

		if (StringUtil.isNull(value)) {
			buf.append(value);
		} else {
			buf.append(nullValue);
		}

		buf.append("</textarea>");

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

	public void setCols(String cols) {
		this.cols = cols;
	}

	public void setRows(String rows) {
		this.rows = rows;
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
