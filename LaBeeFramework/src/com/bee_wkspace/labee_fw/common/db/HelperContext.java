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

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * DB更新ヘルパーで使用するコンテキストクラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: HelperContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class HelperContext implements Serializable {

	private static final long serialVersionUID = -5885001539127570511L;

	/** 文字型を示す定数 */
	public static final String CHAR = "CHAR";

	/** 数値型を示す定数 */
	public static final String NUMBER = "NUMBER";

	/** 日付型を示す定数 */
	public static final String DATE = "DATE";

	/** タイムスタンプ型を示す定数 */
	public static final String DATE_TIME = "DATE_TIME";

	/** カラム名 */
	private String columnName = null;

	/** カラム型 */
	private String columnType = null;

	/** カラム文字列値 */
	private String columnValue = null;

	/** 日付カラム値 */
	private Date columnDate = null;

	/** タイムスタンプカラム値 */
	private Timestamp columnTimeStamp = null;

	/**
	 * コンストラクタ。
	 */
	public HelperContext() {
		super();
	}

	/**
	 * columnNameの値を返す。
	 *
	 * @return columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName columnNameの値をセットする。
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * columnTypeの値を返す。
	 *
	 * @return columnType
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * @param columnType columnTypeの値をセットする。
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * @return columnDate
	 */
	public Date getColumnDate() {
		return columnDate;
	}

	/**
	 * @param columnDate セットする columnDate
	 */
	public void setColumnDate(Date columnDate) {
		this.columnDate = columnDate;
	}

	/**
	 * @return columnTimeStamp
	 */
	public Timestamp getColumnTimeStamp() {
		return columnTimeStamp;
	}

	/**
	 * @param columnTimeStamp セットする columnTimeStamp
	 */
	public void setColumnTimeStamp(Timestamp columnTimeStamp) {
		this.columnTimeStamp = columnTimeStamp;
	}

	/**
	 * @return columnValue
	 */
	public String getColumnValue() {
		return columnValue;
	}

	/**
	 * @param columnValue セットする columnValue
	 */
	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

}
