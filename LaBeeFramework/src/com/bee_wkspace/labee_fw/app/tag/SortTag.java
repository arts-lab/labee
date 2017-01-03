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

import com.bee_wkspace.labee_fw.app.base.AppSearchCondition;
import com.bee_wkspace.labee_fw.common.StringUtil;

/**
 * ソートリンク カスタムタグ。
 *
 * @author ARTS Laboratory
 * $Id: SortTag.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class SortTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 6806737807040213097L;

	/** 呼び出しターゲット名 */
	protected String target = null;

	/** 実行処理名 */
	protected String execute = null;

	/** フォーム名 */
	protected String formName = null;

	/** ソートカラム名 */
	protected String sortColumnName;

	/** ソートターゲット */
	protected String sortTarget;

	/** ソートタイプ */
	protected String sortType;

	/** 表示名 */
	protected String viewName;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		target = null;
		execute = null;
		formName = null;
		sortColumnName = null;
		sortType = null;
		viewName = null;
		sortTarget = null;
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
			pageContext.getOut().print(makeSort());
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
	 * ソートリンク描画
	 *
	 * @return
	 */
	private String makeSort() {
		StringBuilder buf = new StringBuilder();

		if (StringUtil.isNull(execute) == false) {
			execute = "sort";
		}

		buf.append("<div style=\"width:100%; height:100%; cursor:pointer; \" ");
		buf.append("  onClick=\"");

		buf.append("submitParamForm(" + formName + ",");
		buf.append("'" + target + "','" + execute + "', ");
		buf.append("'" + sortColumnName + "', ");
		buf.append("'" + sortType + "',''); \"> ");

		buf.append("<span>");
		buf.append(viewName);
		buf.append("</span>");

		if (sortTarget.equals(sortColumnName)) {
			buf.append("<span>");
			if (AppSearchCondition.ASC.equals(sortType)) {
				buf.append("▲");

			} else if (AppSearchCondition.DESC.equals(sortType)) {
				buf.append("▼");
			}
			buf.append("</span>");
		}
		buf.append("</div>");

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
	 * @param formName セットする formName
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * @param sortColumnName セットする sortColumnName
	 */
	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	/**
	 * @param viewName セットする viewName
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @param sortType セットする sortType
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * @param sortTarget セットする sortTarget
	 */
	public void setSortTarget(String sortTarget) {
		this.sortTarget = sortTarget;
	}

}
