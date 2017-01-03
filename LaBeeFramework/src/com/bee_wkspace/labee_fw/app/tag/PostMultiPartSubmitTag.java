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

/**
 * マルチパートPOSTサブミットリンク カスタムタグ。<br>
 * ファイルアップロード時に使用する。
 *
 * @author ARTS Laboratory
 *
 * $Id: PostMultiPartSubmitTag.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class PostMultiPartSubmitTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -5828129324500034430L;

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

		buf.append(";sendMultiPartForm(" + formName + ",");
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

}
