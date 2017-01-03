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
package com.bee_wkspace.labee_fw.core.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;

/**
 * メッセージコード情報コンテキスト。<br>
 * ロケール言語を指定する事で多言語に対応する。
 *
 * @author ARTS Laboratory
 *
 * $Id: MessageCodeContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class MessageCodeContext implements Serializable {

	private static final long serialVersionUID = 5214424696727028612L;

	/** メッセージコード */
	private String msgCode;

	/** メッセージ言語マップ */
	private HashMap<String, String> messageMap;

	/**
	 * コンストラクタ。
	 *
	 * @param msgCode メッセージコード
	 */
	public MessageCodeContext(String msgCode) {
		this.msgCode = msgCode;
		messageMap = new HashMap<String, String>();
	}

	/**
	 * デフォルトロケ―ル言語コードのメッセージを返す。
	 *
	 * @return message メッセージ
	 */
	public String getMessage() {
		String msg = messageMap.get(SystemConfigMng.getSystemLocale().getLanguage());
		if (StringUtil.isNull(msg)) {
			return msg;
		} else {
			return "Undefined message :" + msgCode;
		}

	}

	/**
	 * デフォルトローケール言語コードのメッセージをセットする。
	 *
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
		messageMap.put(SystemConfigMng.getSystemLocale().getLanguage(), message);
	}

	/**
	 * 指定したロケール言語コードのメッセージをセットする。<br>
	 * たとえば日本語の場合は"ja"、英語の場合は"en"となる。
	 *
	 * @param localeCd ロケール言語コード
	 * @param message メッセージ
	 */
	public void setMessage(String localeCd, String message) {
		messageMap.put(localeCd, message);
	}

	/**
	 * 指定したロケールのメッセージをセットする。<br>
	 *
	 * @param locale ロケール
	 * @param message メッセージ
	 */
	public void setMessage(Locale locale, String message) {
		messageMap.put(locale.getLanguage(), message);
	}

	/**
	 * 指定したロケ―ル言語コードのメッセージを返す。
	 *
	 * @param localeCd ロケール
	 * @return message メッセージ
	 */
	public String getMessage(String localeCd) {
		String msg = messageMap.get(localeCd);
		if (StringUtil.isNull(msg)) {
			return msg;
		} else {
			return "Undefined message :" + msgCode;
		}
	}

	/**
	 * 指定したロケ―ルのメッセージを返す。
	 *
	 * @param locale ロケール
	 * @return message メッセージ
	 */
	public String getMessage(Locale locale) {
		String msg = messageMap.get(locale.getLanguage());
		if (StringUtil.isNull(msg)) {
			return msg;
		} else {
			return "Undefined message :" + msgCode;
		}
	}

	/**
	 * メッセージコード値を返す。
	 *
	 * @return msgCode メッセージ
	 */
	public String getMsgCode() {
		return msgCode;
	}

}
