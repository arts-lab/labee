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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.DivisionContext;

/**
 * セレクトボックスタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: SelectBoxTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class SelectBoxTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -7303667317870618091L;

	/** セレクトボックス内容値マップ */
	protected Map<String, String> valueMap = null;

	/** 区分値名 */
	protected String divId = null;

	/** 選択値 */
	protected String checkValue = null;

	/** 選択区分No */
	protected String checkDivNo = null;

	/** セレクトボックス名 */
	protected String name = null;

	/** オブジェクトID */
	protected String objId = null;

	/** スタイルシートクラス名 */
	protected String styleClass = null;

	/** スタイルシート直接設定値 */
	protected String style = null;

	/** Tabインデックス */
	protected String tabIndex = null;

	/** 無効フラグ */
	protected String desabled = null;

	/** OnCkick設定内容 */
	protected String onClick = null;

	/** OnChange設定内容 */
	protected String onChange = null;

	/** 1行目ブランクフラグ */
	private String blankFlg = null;

	/** 言語ロケールコード */
	protected String localeCd;


	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		valueMap = null;
		divId = null;
		checkValue = null;
		checkDivNo = null;
		name = null;
		objId = null;
		styleClass = null;
		style = null;
		tabIndex = null;
		desabled = null;
		onClick = null;
		onChange = null;
		blankFlg = null;
		localeCd = null;
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

		buf.append("<select ");

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
		buf.append(">");

		// 区分値IDを設定している場合
		if (StringUtil.isNull(divId)) {
			LinkedHashMap<String, DivisionContext> divMap = ResourceMng.getDivMap(divId);
			if (divMap != null) {
				if (null != blankFlg && blankFlg.toLowerCase().equals("true")) {
					buf.append("<option value=\"-1\">--------</option>");
				}

				for (DivisionContext context : divMap.values()) {
					buf.append("<option value=\"" + context.getItemNo() + "\"");

					if ((StringUtil.isNull(checkValue) && context.getItemValue().equals(checkValue))
							|| (StringUtil.isNull(checkDivNo) && context.getItemNo().equals(checkDivNo))) {
						buf.append(" SELECTED>");
					} else {
						buf.append(">");
					}
					buf.append(context.getItemView(localeCd));
					buf.append("</option>");
				}
			}

			// セレクトボックス値をマップ指定の場合
		} else if (valueMap != null) {
			if (null != blankFlg && blankFlg.toLowerCase().equals("true")) {
				buf.append("<option value=\"-1\">--------</option>");
			}

			Iterator<String> iterator = valueMap.keySet().iterator();
			while (iterator.hasNext()) {
				String itemval = (String) iterator.next();
				String viewName = (String) valueMap.get(itemval);

				// 選択行にSELECTEDを設定
				String selected = null;
				if (StringUtil.isNull(checkValue) && checkValue.equals(itemval)) {
					selected = "SELECTED";
				} else {
					selected = "";
				}

				buf.append("<option value=\"" + itemval + "\" " + selected + ">" + viewName + "</option>");
			}
		}

		buf.append("</select>");

		return buf.toString();
	}

	/**
	 * @param valueMap  valueMapの値をセットする。
	 */
	public void setValueMap(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}

	/**
	 * @param divId  divIdの値をセットする。
	 */
	public void setDivId(String divId) {
		this.divId = divId;
	}

	/**
	 * @param checkValue  checkValueの値をセットする。
	 */
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}

	/**
	 * @param checkDivNo  checkDivNoの値をセットする。
	 */
	public void setCheckDivNo(String checkDivNo) {
		this.checkDivNo = checkDivNo;
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
	 * @param blankFlg  blankFlgの値をセットする。
	 */
	public void setBlankFlg(String blankFlg) {
		this.blankFlg = blankFlg;
	}

	/**
	 * @param localeCd  localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}


}
