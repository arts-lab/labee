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
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.DivisionContext;

/**
 * チェックボックス カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: CheckBoxTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class CheckBoxTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -4514242136235020103L;

	/** チェックボックス固有値 */
	protected String value = null;

	/** チェックボックス選択値 */
	protected String checkValue = null;

	/** チェックボックス選択値リスト */
	protected ArrayList<String> checkValues = null;

	/** チェックボックス名 */
	protected String name = null;

	/** オブジェクトID */
	private String objId = null;

	/** スタイルシートクラス */
	protected String styleClass = null;

	/** スタイルシート直接設定値 */
	protected String style = null;

	/** Tabインデックス */
	protected String tabIndex = null;

	/** 有効・無効フラグ */
	protected String desabled = null;

	/** OnClick内容値 */
	protected String onClick = null;

	/** OnChange内容値 */
	protected String onChange = null;

	/** ラジオボタン ラベル名 */
	protected String viewName = null;

	/** 区分値名 */
	protected String divId = null;

	/** 区分アイテムNo */
	protected String divItemNo = null;

	/** 言語ロケールコード */
	protected String localeCd;



	/**
	 *	タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		value = null;
		checkValue = null;
		checkValues = null;
		name = null;
		objId = null;
		styleClass = null;
		style = null;
		tabIndex = null;
		desabled = null;
		onClick = null;
		onChange = null;
		viewName = null;
		divId = null;
		localeCd = null;
		divItemNo = null;
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
			pageContext.getOut().print(makeCheckBox());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeCheckBox() {
		StringBuilder buf = new StringBuilder();

		if (StringUtil.isNull(localeCd) == false) {
			localeCd = SystemConfigMng.getSystemLocale().getLanguage();
		}

		if (StringUtil.isNull(viewName) || (StringUtil.isNull(divId) && StringUtil.isNull(divItemNo))) {
			buf.append("<div style=\"float:left;\">");
		}

		buf.append("<input type=\"checkbox\" ");

		if (StringUtil.isNull(name)) {
			buf.append("name=\"" + name + "\" ");
		}

		if (StringUtil.isNull(objId)) {
			buf.append("id=\"" + objId + "\" ");
		}


		if (StringUtil.isNull(viewName)) {
			if (StringUtil.isNull(value)) {
				buf.append("value=\"" + value + "\" ");
			} else {
				buf.append("value=\"ON\" ");
			}
		} else if (StringUtil.isNull(divId) && StringUtil.isNull(divItemNo)) {
			DivisionContext divContext = ResourceMng.getDivContext(divId, divItemNo);
			value = divContext.getItemValue();
			buf.append("value=\"" + value + "\" ");

		}

		if (StringUtil.isNull(styleClass)) {
			buf.append("class=\"" + styleClass + "\" ");
		}

		if (StringUtil.isNull(style)) {
			buf.append("style=\"" + style + "\" ");
		}

		if (StringUtil.isNull(tabIndex) && StringUtil.isNumber(tabIndex)) {
			buf.append("tabIndex=\"" + tabIndex + "\" ");
		}

		if (StringUtil.isNull(onClick)) {
			buf.append("onClick=\"" + onClick + "\" ");
		}

		if (StringUtil.isNull(onChange)) {
			buf.append("onChange=\"" + onChange + "\" ");
		}

		if (StringUtil.isNull(desabled)) {
			if (desabled.toLowerCase().equals("true")) {
				buf.append("disabled=\"disabled\" ");
			}
		}

		if (StringUtil.isNull(value) && StringUtil.isNull(checkValue) && value.equals(checkValue)) {
			buf.append(" CHECKED ");
		} else if (StringUtil.isNull(value) && checkValues != null) {
			for (String val : checkValues) {
				if (val.equals(checkValue)) {
					buf.append(" CHECKED ");
					break;
				}
			}
		}

		buf.append(">");

		if (StringUtil.isNull(viewName)) {
			buf.append("</div>");
			buf.append("<div style=\"float:left;\">");
			buf.append("<LABEL style=\"cursor: pointer;\" for=\"" + objId + "\">" + viewName + "</LABEL>");
			buf.append("</div>");

		} else if (StringUtil.isNull(divId) && StringUtil.isNull(divItemNo)) {
			DivisionContext divContext = ResourceMng.getDivContext(divId, divItemNo);

			buf.append("</div>");
			buf.append("<div style=\"float:left;\">");
			buf.append("<LABEL style=\"cursor: pointer;\" for=\"" + objId + "\">"
					+ divContext.getItemView(localeCd)
					+ "</LABEL>");
			buf.append("</div>");
		}


		return buf.toString();
	}

	/**
	 * @param value  valueの値をセットする。
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param checkValue  checkValueの値をセットする。
	 */
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}

	/**
	 * @param checkValues  checkValuesの値をセットする。
	 */
	public void setCheckValues(ArrayList<String> checkValues) {
		this.checkValues = checkValues;
	}

	/**
	 * @param name  nameの値をセットする。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param objId  objIdの値をセットする。
	 */
	public void setObjId(String objId) {
		this.objId = objId;
	}

	/**
	 * @param styleClass  styleClassの値をセットする。
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * @param style  styleの値をセットする。
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @param tabIndex  tabIndexの値をセットする。
	 */
	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @param desabled  desabledの値をセットする。
	 */
	public void setDesabled(String desabled) {
		this.desabled = desabled;
	}

	/**
	 * @param onClick  onClickの値をセットする。
	 */
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	/**
	 * @param onChange  onChangeの値をセットする。
	 */
	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	/**
	 * @param viewName  viewNameの値をセットする。
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @param divId  divIdの値をセットする。
	 */
	public void setDivId(String divId) {
		this.divId = divId;
	}

	/**
	 * @param divItemNo  divItemNoの値をセットする。
	 */
	public void setDivItemNo(String divItemNo) {
		this.divItemNo = divItemNo;
	}

	/**
	 * @param localeCd  localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}




}
