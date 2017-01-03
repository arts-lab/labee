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
package com.bee_wkspace.labee_fw.common;

import java.text.MessageFormat;
import java.util.Locale;

import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.MessageCodeContext;
import com.bee_wkspace.labee_fw.core.context.WordLabelContext;

/**
 * メッセージとワードラベル取得ユーティリティクラス。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: MessageLabelUtil.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class MessageLabelUtil {

	/** 言語ロケール(日本語)を示す定数 */
	public static final Locale JAPANESE = Locale.JAPANESE;

	/** 言語ロケール(英語)を示す定数 */
	public static final Locale ENGLISH = Locale.ENGLISH;

	/**
	 * デフォルト言語ロケールのメッセージを返す。
	 *
	 * @param msgCode メッセージコード
	 * @param params 置き換えパラメータ
	 * @return メッセージ文字列
	 */
	public static String getMessage(String msgCode, String... params) {
		return getMessage(msgCode, SystemConfigMng.getSystemLocale(), msgCode);
	}

	/**
	 * 指定言語ロケールのメッセージを返す。
	 *
	 * @param msgCode メッセージコード
	 * @param locale 言語ロケール
	 * @param params 置き換えパラメータ
	 * @return メッセージ文字列
	 */
	public static String getMessage(String msgCode, Locale locale, String... params) {
		String msg = null;
		MessageCodeContext msgContext = null;
		try {
			msgContext = ResourceMng.getMessageContext(msgCode);
			MessageFormat format = new MessageFormat(msgContext.getMessage(locale));
			msg = format.format(params);

		} catch (Exception e) {
			if (msgContext != null) {
				msg = msgContext.getMessage();
			} else {
				msg = msgCode + "is Undefined";
			}
		}
		return msg;
	}

	/**
	 * デフォルト言語ロケールのワードラベルを返す。
	 *
	 * @param wordLabelCode ワードラベルコード
	 * @return ワードラベル文字列
	 */
	public static String getWordLabel(String wordLabelCode) {
		return getWordLabel(wordLabelCode, "global", SystemConfigMng.getSystemLocale());
	}

	/**
	 * デフォルト言語ロケールのワードラベルを返す。
	 *
	 * @param wordLabelCode ワードラベルコード
	 * @param division ワードラベル区分
	 * @return ワードラベル文字列
	 */
	public static String getWordLabel(String wordLabelCode, String division) {
		return getWordLabel(wordLabelCode, division, SystemConfigMng.getSystemLocale());
	}


	/**
	 * 指定言語ロケールのワードラベルを返す。
	 *
	 * @param wordLabelCode ワードラベルコード
	 * @param division ワードラベル区分
	 * @param locale 言語ロケール
	 * @return ワードラベル文字列
	 */
	public static String getWordLabel(String wordLabelCode, String division, Locale locale) {
		String wordLabel = null;
		WordLabelContext wordLabelContext = null;
		try {
			wordLabelContext = ResourceMng.getLabelWordContext(division, wordLabelCode);
			wordLabel = wordLabelContext.getWordLabel(locale);

		} catch (Exception e) {
			if (wordLabelContext != null) {
				wordLabel = wordLabelContext.getWordLabel();
			} else {
				wordLabel = "Undefined";
			}
		}
		return wordLabel;
	}

}
