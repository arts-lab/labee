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
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;

/**
 * AJax POSTサブミットリンク カスタムタグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: PostAjaxSubmitTag.java 566 2016-08-16 00:16:36Z pjmember $
 */
public class PostAjaxSubmitTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 3336118605730284712L;

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
	 * Ajaxレスポインス後に実行するJavaScriptメソッド<br>
	 * 指定しなければデフォルトでchangetTagValueFromJsonメソッド<br>
	 * (オブジェクト内容書き換え)が実行される
	 */
	protected String retExecFunc = null;

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
	 * AJax POSTサブミットリンク描画
	 *
	 * @return
	 */
	private String makeSubmitButton() {
		StringBuilder buf = new StringBuilder();

		String[] tgtDatas = target.split("\\.");
		String screenId = tgtDatas[tgtDatas.length - 1];
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);

		if (screenContext != null && screenContext.isSslFlg()) {
			buf.append(";postAjaxRetJsonExecSSL");
		} else {
			buf.append(";postAjaxRetJsonExec");
		}
		
		buf.append("(" + formName + ",'" + target + "','" + execute + "',");
		if (StringUtil.isNull(param1)) {
			buf.append("'" + param1 + "', ");
		} else {
			buf.append("'', ");
		}

		if (StringUtil.isNull(param2)) {
			buf.append("'" + param2 + "', ");
		} else {
			buf.append("'', ");
		}

		if (StringUtil.isNull(param3)) {
			buf.append("'" + param3 + "', ");
		} else {
			buf.append("'', ");
		}

		if (StringUtil.isNull(retExecFunc) == false) {
			buf.append("'changetTagValueFromJson()')");
		} else {
			buf.append("'" + retExecFunc + "')");
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
	 * @param retExecFunc retExecFuncの値をセットする。
	 */
	public void setRetExecFunc(String retExecFunc) {
		this.retExecFunc = retExecFunc;
	}

}
