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
 * ワードラベル情報コンテキスト。<br>
 * ロケール言語を指定する事で多言語に対応する。
 *
 * @author ARTS Laboratory
 *
 * $Id: WordLabelContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class WordLabelContext implements Serializable {

	private static final long serialVersionUID = 4617713336540876077L;

	/** ワードラベルコード */
	private String wordLabelCode;

	/**	ワードラベル区分	*/
	private String division;

	/** ワードラベル言語マップ */
	private HashMap<String, String> wordLabelMap;

	/**
	 * コンストラクタ。
	 *
	 * @param wordLabelCode ワードラベルコード
	 */
	public WordLabelContext(String wordLabelCode) {
		this.wordLabelCode = wordLabelCode;
		wordLabelMap = new HashMap<String, String>();
	}

	/**
	 * デフォルトロケ―ル言語コードのワードラベルを返す。
	 *
	 * @return ワードラベル
	 */
	public String getWordLabel() {
		String word = wordLabelMap.get(SystemConfigMng.getSystemLocale().getLanguage());
		if (StringUtil.isNull(word)) {
			return word;
		} else {
			return "Undefined word label :" + wordLabelCode;
		}

	}

	/**
	 * デフォルトローケール言語コードのワードラベルをセットする。
	 *
	 * @param wordLabel ワードラベル
	 */
	public void setWordLabel(String wordLabel) {
		wordLabelMap.put(SystemConfigMng.getSystemLocale().getLanguage(), wordLabel);
	}

	/**
	 * 指定したロケール言語コードのワードラベルをセットする。<br>
	 * たとえば日本語の場合は"ja"、英語の場合は"en"となる。
	 *
	 * @param localeCd ロケール言語コード
	 * @param wordLabel ワードラベル
	 */
	public void setWordLabel(String localeCd, String wordLabel) {
		wordLabelMap.put(localeCd, wordLabel);
	}

	/**
	 * 指定したロケールのワードラベルをセットする。<br>
	 *
	 * @param locale ロケール
	 * @param wordLabel ワードラベル
	 */
	public void setWordLabel(Locale locale, String wordLabel) {
		wordLabelMap.put(locale.getLanguage(), wordLabel);
	}

	/**
	 * 指定したロケ―ル言語コードのワードラベルを返す。
	 *
	 * @param localeCd ロケール言語コード
	 * @return ワードラベル
	 */
	public String getWordLabel(String localeCd) {
		String word = wordLabelMap.get(localeCd);
		if (StringUtil.isNull(word)) {
			return word;
		} else {
			return "Undefined word label :" + wordLabelCode;
		}
	}

	/**
	 * 指定したロケ―ルのワードラベルを返す。
	 *
	 * @param locale ロケール
	 * @return ワードラベル
	 */
	public String getWordLabel(Locale locale) {
		String word = wordLabelMap.get(locale.getLanguage());
		if (StringUtil.isNull(word)) {
			return word;
		} else {
			return "Undefined word label :" + wordLabelCode;
		}
	}

	/**
	 * ワードラベルコード値を返す。
	 *
	 * @return ワードラベル
	 */
	public String getWordLabelCode() {
		return wordLabelCode;
	}

	/**
	 * divisionの値を返す。
	 * @return division
	 */
	public String getDivision() {
		return division;
	}

	/**
	 * @param division  divisionの値をセットする。
	 */
	public void setDivision(String division) {
		this.division = division;
	}

}
