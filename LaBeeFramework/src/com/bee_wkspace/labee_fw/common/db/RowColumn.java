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
package com.bee_wkspace.labee_fw.common.db;

import java.util.HashMap;

import com.bee_wkspace.labee_fw.common.EncodeUtil;

/**
 * DB検索結果カラムデータ１行分を格納するクラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: RowColumn.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class RowColumn {

	/** カラムデータ格納HashMap */
	private HashMap<String, String> columnMap = null;

	/**
	 * コンストラクタ
	 */
	public RowColumn() {
		columnMap = new HashMap<String, String>();
	}

	/**
	 * カラムデータをセット。
	 *
	 * @param name カラム名
	 * @param value カラム値
	 * @throws Exception 例外
	 */
	public void setColumn(String name, String value) throws Exception {
		if (value == null) {
			value = "";
		}
		columnMap.put(name, value);
	}

	/**
	 * 結果データから文字列型で値を返す。
	 *
	 * @param name カラム名
	 * @return 結果カラム値
	 * @throws Exception 例外
	 */
	public String getString(String name) throws Exception {
		String val = (String) columnMap.get(name);
		if (val == null) {
			val = "";
		}
		return val;
	}

	/**
	 * 暗号化された結果データから複合化して文字列型で値を返す。
	 *
	 * @param name カラム名
	 * @param decodeFlg true = 複合化する false = 複合化しない
	 * @return 結果カラム値
	 * @throws Exception 例外
	 */
	public String getString(String name, boolean decodeFlg) throws Exception {
		String val = (String) columnMap.get(name);
		if (val == null || val.trim().length() == 0) {
			val = "";
		} else {
			if (decodeFlg) {
				val = EncodeUtil.decode(val);
			}
		}
		return val;
	}

	/**
	 * 結果データから数値型で値を返す。
	 *
	 * @param name カラム名
	 * @return 結果カラム値
	 * @throws Exception 例外
	 */
	public int getInt(String name) throws Exception {
		String val = (String) columnMap.get(name);
		if (val == null || val.trim().length() == 0) {
			val = "0";
		}
		return Integer.parseInt(val);
	}

	/**
	 * 暗号化された結果データから複合化して数値型で値を返す。
	 *
	 * @param name カラム名
	 * @param decodeFlg true = 複合化する false = 複合化しない
	 * @return 結果カラム値
	 * @throws Exception 例外
	 */
	public int getInt(String name, boolean decodeFlg) throws Exception {
		String val = (String) columnMap.get(name);
		try {
			if (val == null || val.trim().length() == 0) {
				val = "0";
			} else {
				val = EncodeUtil.decode(val);
			}
		} catch (Exception e) {
			System.out.println(decodeFlg);
		}
		return Integer.parseInt(val);
	}

	/**
	 * 結果データから日付型で値を返す。
	 *
	 * @param name カラム名
	 * @return 結果カラム値
	 * @throws Exception 例外
	 */
	public java.sql.Date getDate(String name) throws Exception {
		String val = (String) columnMap.get(name);
		if (val == null) {
			return null;
		}

		return java.sql.Date.valueOf(val);
	}

	/**
	 * 結果データからObject型で値を返す。
	 *
	 * @param name カラム名
	 * @return 結果カラム値
	 * @throws Exception 例外
	 */
	public Object get(String name) throws Exception {
		return columnMap.get(name);
	}

	/**
	 * @return columnMap
	 */
	public HashMap<String, String> getColumnMap() {
		return columnMap;
	}

}
