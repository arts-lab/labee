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
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.base.BaseBean;

/**
 * 入力チェックエラーメッセ―ジ表示タグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: InputErrorMsgTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class InputErrorMsgTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -9106784635732689023L;

	/** 入力チェックパラメータ名 */
	protected String paramName;

	/** オブジェクトID */
	protected String objId = null;

	/** スタイルシートクラス */
	protected String styleClass = null;

	/** スタイルシート直接記述値 */
	protected String style = null;

	/** OnClick処理 */
	protected String onClick = null;

	/** ビーン */
	private BaseBean bean;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		paramName = null;
		objId = null;
		styleClass = null;
		style = null;
		onClick = null;
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
		//
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(makeInputErrorMsg());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeInputErrorMsg() {
		StringBuilder buf = new StringBuilder();
		if (bean.isError(paramName)) {
			HashMap<String, String> errMsgMap = bean.getInputErrMsgMap();
			String msg = errMsgMap.get(paramName);
			if (StringUtil.isNull(msg)) {
				buf.append("<div ");

				if (StringUtil.isNull(objId)) {
					buf.append("id=\"" + objId + "\" ");
				}

				if (StringUtil.isNull(styleClass)) {
					buf.append("class=\"" + styleClass + "\" ");
				}

				if (StringUtil.isNull(style)) {
					buf.append("style=\"" + style + "\" ");
				}

				if (StringUtil.isNull(onClick)) {
					buf.append("onClick=\"" + onClick + "\" ");
				}

				buf.append(">" + msg + "</div>");
			}
		}

		return buf.toString();
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public void setBean(BaseBean bean) {
		this.bean = bean;
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

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

}
