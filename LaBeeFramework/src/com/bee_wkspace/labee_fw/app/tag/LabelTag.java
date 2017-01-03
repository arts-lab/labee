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
 * ラベル表示タグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: LabelTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class LabelTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = -4464280176864937708L;

	/** ワードラベルコード */
	protected String labelCd;

	/** ラベル区分 */
	protected String labelDiv;

	/** 言語ロケールコード */
	protected String localeCd;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		labelCd = null;
		labelDiv = null;
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
		//
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(makeLabelTag());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeLabelTag() {
		if (StringUtil.isNull(localeCd) == false) {
			localeCd = SystemConfigMng.getSystemLocale().getLanguage();
		}

		if (StringUtil.isNull(labelDiv) == false) {
			labelDiv = "global";
		}

		return MessageLabelUtil.getWordLabel(labelCd, labelDiv, new Locale(localeCd));
	}

	/**
	 * @param labelCd  labelCdの値をセットする。
	 */
	public void setLabelCd(String labelCd) {
		this.labelCd = labelCd;
	}

	/**
	 * @param labelDiv  labelDivの値をセットする。
	 */
	public void setLabelDiv(String labelDiv) {
		this.labelDiv = labelDiv;
	}

	/**
	 * @param localeCd  localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}

}
