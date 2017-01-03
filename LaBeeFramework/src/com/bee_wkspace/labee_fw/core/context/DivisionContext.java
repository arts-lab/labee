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
 * 区分値情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: DivisionContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class DivisionContext implements Serializable {

	private static final long serialVersionUID = 6828596259748945814L;

	/** 区分ID */
	private String divId;

	/** アイテムNo */
	private String itemNo;

	/** アイテム値 */
	private String itemValue;

	/** 表示名言語マップ */
	private HashMap<String, String> itemViewMap;


	/**
	 * コンストラクタ。
	 */
	public DivisionContext() {
		itemViewMap = new HashMap<String, String>();
	}

	/**
	 * デフォルトローケール言語コードの表示アイテム名をセットする。
	 *
	 * @param itemView 表示アイテム名
	 */
	public void setItemView(String itemView) {
		itemViewMap.put(SystemConfigMng.getSystemLocale().getLanguage(), itemView);
	}

	/**
	 * 指定したロケール言語コードの表示アイテム名をセットする。<br>
	 * たとえば日本語の場合は"ja"、英語の場合は"en"となる。
	 *
	 * @param localeCd ロケール言語コード
	 * @param itemView 表示アイテム名
	 */
	public void setItemView(String localeCd, String itemView) {
		itemViewMap.put(localeCd, itemView);
	}

	/**
	 * 指定したロケールの表示アイテム名をセットする。<br>
	 *
	 * @param locale ロケール
	 * @param itemView 表示アイテム名
	 */
	public void setItemView(Locale locale, String itemView) {
		itemViewMap.put(locale.getLanguage(), itemView);
	}


	/**
	 * デフォルトロケ―ル言語コードの表示アイテム名を返す。
	 *
	 * @return 表示アイテム名
	 */
	public String getItemView() {
		String viewItem = itemViewMap.get(SystemConfigMng.getSystemLocale().getLanguage());
		if (StringUtil.isNull(viewItem)) {
			return viewItem;
		} else {
			return "Undefined division :" + divId;
		}

	}

	/**
	 * 指定したロケ―ル言語コードの表示アイテム名を返す。
	 *
	 * @param localeCd ロケールコード
	 * @return 表示アイテム名
	 */
	public String getItemView(String localeCd) {
		String viewItem = itemViewMap.get(localeCd);
		if (StringUtil.isNull(viewItem)) {
			return viewItem;
		} else {
			return "Undefined division :" + divId;
		}
	}

	/**
	 * 指定したロケ―ルの表示アイテム名を返す。
	 *
	 * @param locale ロケールコ
	 * @return 表示アイテム名
	 */
	public String getItemView(Locale locale) {
		String viewItem = itemViewMap.get(locale.getLanguage());
		if (StringUtil.isNull(viewItem)) {
			return viewItem;
		} else {
			return "Undefined division :" + divId;
		}
	}


	/**
	 * divIdの値を返す。
	 *
	 * @return divId
	 */
	public String getDivId() {
		return divId;
	}

	/**
	 * @param divId divIdの値をセットする。
	 */
	public void setDivId(String divId) {
		this.divId = divId;
	}

	/**
	 * itemNoの値を返す。
	 *
	 * @return itemNo
	 */
	public String getItemNo() {
		return itemNo;
	}

	/**
	 * @param itemNo itemNoの値をセットする。
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	/**
	 * itemValueの値を返す。
	 *
	 * @return itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemValue itemValueの値をセットする。
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}


}
