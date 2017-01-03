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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bee_wkspace.labee_fw.common.EncodeUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;

/**
 * DB更新系のSQLを生成し実行するヘルパークラス。<br>
 * <br>
 * <b>インサートSQL実行例</b><br>
 * TEST_TABLEテーブルにTEST_ID,TEST_NAMEの値をインサートする場合<br>
 * <br>
 * UpdateHelper helper = new UpdateHelper(UpdateHelper.INSERT, "TEST_TABLE");<br>
 * helper.setParam("TEST_ID", "12345");<br>
 * helper.setParam("TEST_NAME", "テスト");<br>
 * dba.setSQL(helper.createSql()); // SQL生成<br>
 * helper.bindSQL(dba); // バラメータバインド<br>
 * int cnt = dba.executeUpdate(); // インサートの実行<br>
 * <br>
 * <br>
 * <b>アップデートSQL実行例</b><br>
 * TEST_TABLEテーブルのTEST_IDが12345のレコードのTEST_NAME値をアップデートする場合<br>
 * <br>
 * UpdateHelper helper = new UpdateHelper(UpdateHelper.UPDATE, "TEST_TABLE");<br>
 * helper.setParam("TEST_NAME", "サンプル");<br>
 * helper.setWhereCondition("TEST_ID=?");<br>
 * dba.setSQL(helper.createSql()); // SQL生成<br>
 * int idx = helper.bind(dba); // バラメータバインド<br>
 * dba.bindString(idx++, "12345");<br>
 * int cnt = dba.executeUpdate(); // アップデートの実行<br>
 * <br>
 *
 * @author ARTS Laboratory
 *
 * $Id: UpdateHelper.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class UpdateHelper {

	/** インサートを示す定数 */
	public static final String INSERT = "INSERT";

	/** アップデートを示す定数 */
	public static final String UPDATE = "UPDATE";

	/** デリートを示す定数 */
	public static final String DELETE = "DELETE";

	/** カンマ */
	private static final String COMMA = ",";

	/** 登録内容を格納するマップ */
	private ArrayList<HelperContext> paramList = null;

	/** where条件文字列 */
	private String whereStr = null;

	/** 更新タイプ */
	private String entryType = null;

	/** 更新対象テーブル名 */
	private String targetTable = null;

	/**
	 * コンストラクタ。
	 *
	 * @param type 更新処理タイプ<br>
	 * 当クラスの定数 INSERT, UPDATE , DELETEを指定する。
	 * @param tableName 更新対象テーブル名
	 */
	public UpdateHelper(String type, String tableName) {
		entryType = type;
		targetTable = tableName;
		paramList = new ArrayList<HelperContext>();
	}

	/**
	 * 設定した内容を基にSQL文字列を生成する。
	 *
	 * @return SQL文字列
	 */
	public String createSql() {
		StringBuilder sql = new StringBuilder();

		// インサートの場合
		if (entryType.equals(INSERT)) {
			createInsert(sql);

			// アップデートの場合
		} else if (entryType.equals(UPDATE)) {
			createUpdate(sql);

			// デリートの場合
		} else if (entryType.equals(DELETE)) {
			createDelete(sql);
		}

		return sql.toString();
	}

	/**
	 * 登録するカラム情報を指定型でセットする。<br>
	 * 暗号化フラグをtrueに設定する事でカラムデータを暗号化する。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @param columnType カラム型
	 * @param encFlg 暗号化フラグ true=暗号化する false=暗号化しない
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, String columnValue, String columnType, boolean encFlg) throws Exception {
		HelperContext context = new HelperContext();
		context.setColumnName(columnName);
		context.setColumnType(columnType);

		// 暗号化
		if (encFlg) {
			columnValue = EncodeUtil.encode(columnValue);
		}
		if (columnValue == null) {
			columnValue = "";
		}
		
		context.setColumnValue(columnValue);
		paramList.add(context);
	}

	/**
	 * 登録するカラム情報を日付型でセットする。<br>
	 *
	 * @param columnName カラム名
	 * @param columnDate カラム登録値
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, Date columnDate) throws Exception {
		HelperContext context = new HelperContext();
		context.setColumnName(columnName);
		context.setColumnType(HelperContext.DATE_TIME);
		Calendar cal = Calendar.getInstance();
		cal.setTime(columnDate);

		context.setColumnTimeStamp(new java.sql.Timestamp(cal.getTimeInMillis()));
		paramList.add(context);
	}

	/**
	 * 登録するカラム情報を文字列でセットする。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @param columnType カラム型
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, String columnValue, String columnType) throws Exception {
		this.setParam(columnName, columnValue, columnType, false);
	}

	/**
	 * 登録するカラム情報を文字列でセットする。 暗号化フラグをtrueに設定する事でカラムデータを暗号化する。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @param encFlg 暗号化フラグ true:暗号化する
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, String columnValue, boolean encFlg) throws Exception {
		this.setParam(columnName, columnValue, HelperContext.CHAR, encFlg);
	}

	/**
	 * 登録するカラム情報を数値型でセットする。 暗号化フラグをtrueに設定する事でカラムデータを暗号化する。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @param encFlg 暗号化フラグ true:暗号化する
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, int columnValue, boolean encFlg) throws Exception {
		this.setParam(columnName, Integer.toString(columnValue), HelperContext.NUMBER, encFlg);
	}

	/**
	 * 登録するカラム情報をLong数値型でセットする。 暗号化フラグをtrueに設定する事でカラムデータを暗号化する。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @param encFlg 暗号化フラグ true:暗号化する
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, long columnValue, boolean encFlg) throws Exception {
		this.setParam(columnName, Long.toString(columnValue), HelperContext.NUMBER, encFlg);
	}

	/**
	 * 登録するカラム情報をdouble数値型でセットする。 暗号化フラグをtrueに設定する事でカラムデータを暗号化する。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @param encFlg 暗号化フラグ true:暗号化する
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, double columnValue, boolean encFlg) throws Exception {
		this.setParam(columnName, Double.toString(columnValue), HelperContext.NUMBER, encFlg);
	}

	
	/**
	 * 登録するカラム情報を文字列でセットする。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, String columnValue) throws Exception {
		this.setParam(columnName, columnValue, HelperContext.CHAR, false);
	}

	/**
	 * 登録するカラム情報を数値型でセットする。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, int columnValue) throws Exception {
		this.setParam(columnName, Integer.toString(columnValue), HelperContext.NUMBER, false);
	}

	/**
	 * 登録するカラム情報をロング数値型でセットする。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, long columnValue) throws Exception {
		this.setParam(columnName, Long.toString(columnValue), HelperContext.NUMBER, false);
	}

	/**
	 * 登録するカラム情報をダブル数値型でセットする。
	 *
	 * @param columnName カラム名
	 * @param columnValue カラム登録値
	 * @throws Exception 例外
	 */
	public void setParam(String columnName, double columnValue) throws Exception {
		this.setParam(columnName, Double.toString(columnValue), HelperContext.NUMBER, false);
	}

	
	
	/**
	 * where条件パラメータをセットする。<br>
	 * aaaa=? AND bbb=? 等の条件文字列を設定する。(WHERE文言は不要) 
	 *
	 * @param whereStr where条件文字列
	 */
	public void setWhereCondition(String whereStr) {
		this.whereStr = whereStr;
	}

	/**
	 * SQLの置き換えパラメータ部をバインドし<br>
	 * バインドパラメータのインデックス最終値を返す。
	 *
	 * @param dba DBアクセスオブジェクト
	 * @return バインドパラメータのインデックス最終値
	 * @throws Exception 例外
	 */
	public int bind(DBAccess dba) throws Exception {
		int size = paramList.size();

		for (int i = 0; i < size; i++) {
			HelperContext context = paramList.get(i);

			if (context.getColumnType().equals(HelperContext.CHAR)
					|| context.getColumnType().equals(HelperContext.NUMBER)) {
				dba.bind(i + 1, context.getColumnValue());

			} else if (context.getColumnType().equals(HelperContext.DATE_TIME)) {
				dba.bind(i + 1, context.getColumnTimeStamp());
			}

		}
		return size + 1;
	}

	/**
	 * インサートSQL文字列を生成して文字列バッファに格納する。
	 *
	 * @param sql 文字列バッファ
	 */
	private void createInsert(StringBuilder sql) {
		StringBuilder sql2 = new StringBuilder();

		sql.append(" INSERT INTO " + this.targetTable + "(");
		int size = paramList.size();
		for (int i = 0; i < size; i++) {
			HelperContext context = paramList.get(i);
			String columnName = context.getColumnName();

			// カラム名部分を連結
			sql.append(columnName);

			// カラム値部分を連結

			// 数値型カラムの値がnull,空文字の場合はDBのNULLを設定
			if (HelperContext.NUMBER.equals(context.getColumnType())
					&& false == StringUtil.isNull(context.getColumnValue())) {
				sql2.append("NULL");
				paramList.remove(i);
				size--;
				i--;

			} else {
				sql2.append("?");
			}

			// カンマ部分を連結
			if (i < size - 1) {
				sql.append(COMMA);
				sql2.append(COMMA);
			}
		}

		sql.append(") VALUES (" + sql2 + ")");
	}

	/**
	 * アップデートSQL文字列を生成して文字列バッファに格納する。
	 *
	 * @param sql 文字列バッファ
	 */
	private void createUpdate(StringBuilder sql) {
		sql.append(" UPDATE " + this.targetTable + " SET ");
		int size = paramList.size();
		for (int i = 0; i < size; i++) {
			HelperContext context = paramList.get(i);
			String columnName = context.getColumnName();

			// 数値型カラムの値がnull,空文字の場合はDBのNULLを設定
			if (HelperContext.NUMBER.equals(context.getColumnType())
					&& false == StringUtil.isNull(context.getColumnValue())) {
				sql.append(columnName + "= NULL ");
				paramList.remove(i);
				size--;
				i--;

			} else {
				// カラム値セット部分を連結
				sql.append(columnName + "= ?");
			}

			// カンマ部分を連結
			if (i < size - 1) {
				sql.append(COMMA);
			}

		}

		// Where条件を付加
		if (StringUtil.isNull(this.whereStr)) {
			sql.append(" WHERE " + this.whereStr);
		}
	}

	/**
	 * デリートSQL文字列を生成して文字列バッファに格納する。
	 *
	 * @param sql 文字列バッファ
	 */
	private void createDelete(StringBuilder sql) {
		sql.append(" DELETE FROM " + this.targetTable);
		// Where条件を付加
		if (StringUtil.isNull(this.whereStr)) {
			sql.append(" WHERE " + this.whereStr);
		}

	}

}
