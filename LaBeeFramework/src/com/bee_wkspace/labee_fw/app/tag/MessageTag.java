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
 * メッセージ表示タグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: MessageTag.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class MessageTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 169011041129141597L;

	/** メッセージコード */
	protected String msgCd;

	/** 置換え文字1 */
	protected String repWd1;

	/** 置換え文字2 */
	protected String repWd2;

	/** 置換え文字3 */
	protected String repWd3;

	/** 言語ロケールコード */
	protected String localeCd;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		msgCd = null;
		repWd1 = null;
		repWd2 = null;
		repWd3 = null;
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
			pageContext.getOut().print(makeMessageTag());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeMessageTag() {
		if (StringUtil.isNull(localeCd) == false) {
			localeCd = SystemConfigMng.getSystemLocale().getLanguage();
		}

		if (StringUtil.isNull(repWd1) && StringUtil.isNull(repWd2) == false && StringUtil.isNull(repWd3) == false) {
			return MessageLabelUtil.getMessage(msgCd, new Locale(localeCd), repWd1);

		} else if (StringUtil.isNull(repWd1) && StringUtil.isNull(repWd2) && StringUtil.isNull(repWd3) == false) {
			return MessageLabelUtil.getMessage(msgCd, new Locale(localeCd), repWd1, repWd2);

		} else if (StringUtil.isNull(repWd1) && StringUtil.isNull(repWd2) && StringUtil.isNull(repWd3)) {
			return MessageLabelUtil.getMessage(msgCd, new Locale(localeCd), repWd1, repWd2, repWd3);
		} else {
			return MessageLabelUtil.getMessage(msgCd, new Locale(localeCd));
		}
	}

	/**
	 * @param msgCd  msgCdの値をセットする。
	 */
	public void setMsgCd(String msgCd) {
		this.msgCd = msgCd;
	}

	/**
	 * @param repWd1  repWd1の値をセットする。
	 */
	public void setRepWd1(String repWd1) {
		this.repWd1 = repWd1;
	}

	/**
	 * @param repWd2  repWd2の値をセットする。
	 */
	public void setRepWd2(String repWd2) {
		this.repWd2 = repWd2;
	}

	/**
	 * @param repWd3  repWd3の値をセットする。
	 */
	public void setRepWd3(String repWd3) {
		this.repWd3 = repWd3;
	}

	/**
	 * @param localeCd  localeCdの値をセットする。
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}

}
