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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.sql.DataSource;

/**
 * データベース接続とSQL実行管理クラス。<br>
 * JDBCデータソースドライバを使用しデータベースのオープン、クローズ、コミット、ロールバック等の管理と<br>
 * SELECT、INSERT, UPDATEのSQLを実行し結果を得る事が出来る。<br>
 * <br>
 * (SELECT文SQLを実行し結果を表示する例)<br>
 * String sql = "SELECT test FROM sample WHERE testid=? AND testnum=? ";<br>
 * dba.setSQL(sql);<br>
 * int idx = 1;<br>
 * dba.bindString(idx++, "TEST");<br>
 * dba.bindInt(idx++, 12345);<br>
 * RowTable resultTable = dba.execute();<br>
 * for (int i=0; i &lt; resultTable.size(); i++) {<br>
 * 　　RowColumn column = resultTable.getRowColumn(i);<br>
 * 　　String value = column.getString("test");<br>
 * 　　System.out.println(value);<br>
 * }<br>
 * <br>
 * 
 * @author ARTS Laboratory
 *
 * $Id: DBAccess.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class DBAccess implements Serializable {

	private static final long serialVersionUID = -5475554072010346264L;

	/** DB接続データソース */
	private static DataSource dataSource = null;

	/** 接続先データベース種別 */
	private static String dataBaseType = DataBaseType.MYSQL;

	/** DB接続コネクション */
	private Connection conn = null;

	/** SQL実行ステートメント */
	private PreparedStatement statement = null;

	/** JDBCドライバーバージョン名称 */
	public static String jdbcDriverName = null;

	/** DB接続状態 */
	public boolean connectStatus = false;

	/**
	 * データベース種別定義クラス。
	 */
	public final class DataBaseType {

		/** MySQLを示す定数 */
		public static final String MYSQL = "mysql";

		/** PostgreSQLを示す定数 */
		public static final String POSTGRESQL = "postgresql";

		/** オラクルを示す定数 */
		public static final String ORACLE = "oracle";

	}

	/**
	 * DBコネクションオープン
	 *
	 * @throws Exception 例外
	 */
	public void openDB() throws Exception {
		try {
			if (conn == null || conn.isClosed()) {
				conn = dataSource.getConnection();
				conn.setAutoCommit(false);
				connectStatus = true;
			}

			// 初期時のみドライバー情報取得
			if (jdbcDriverName == null) {
				DatabaseMetaData meta = conn.getMetaData();
				jdbcDriverName = meta.getDriverName() + " " + meta.getDriverVersion();
			}

		} catch (Exception e) {
			String exName = e.getClass().getSimpleName();
			if (exName.equals("CommunicationsException")) {

				System.out.println("***** Connection Error ******************");
				System.out.println("Connection=" + conn);

				if (conn != null) {
					System.out.println("Connection.isClosed()=" + conn.isClosed());
				}
				System.out.println("statement=" + statement);

				if (statement != null) {
					System.out.println("statement.isClosed()=" + statement.isClosed());
				}

				try {
					// 再接続のリトライを行う
					if (conn != null) {
						conn = null;
						conn = dataSource.getConnection();
						conn.setAutoCommit(false);
						connectStatus = true;
					} else {
						conn = dataSource.getConnection();
						conn.setAutoCommit(false);
						connectStatus = true;
					}
					System.out.println("**** リトライ再接続 *******");
				} catch (Exception ee) {
					ee.printStackTrace();
					throw ee;
				}
			}

			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * DBコネクションオープン<br>
	 * 引数でデータソースを受ける。
	 *
	 * @param _dataSource データソース
	 * @throws Exception 例外
	 */
	public void openDB(DataSource _dataSource) throws Exception {
		try {
			if (conn == null || conn.isClosed()) {
				conn = _dataSource.getConnection();
				conn.setAutoCommit(false);
				connectStatus = true;
			}

			// 初期時のみドライバー情報取得
			if (jdbcDriverName == null) {
				DatabaseMetaData meta = conn.getMetaData();
				jdbcDriverName = meta.getDriverName() + " " + meta.getDriverVersion();
			}

		} catch (Exception e) {
			String exName = e.getClass().getSimpleName();
			if (exName.equals("CommunicationsException")) {

				System.out.println("***** Connection Error ******************");
				System.out.println("Connection=" + conn);

				if (conn != null) {
					System.out.println("Connection.isClosed()=" + conn.isClosed());
				}
				System.out.println("statement=" + statement);

				if (statement != null) {
					System.out.println("statement.isClosed()=" + statement.isClosed());
				}

				try {
					// 再接続のリトライを行う
					if (conn != null) {
						conn = null;
						conn = _dataSource.getConnection();
						conn.setAutoCommit(false);
						connectStatus = true;
					} else {
						conn = _dataSource.getConnection();
						conn.setAutoCommit(false);
						connectStatus = true;
					}
					System.out.println("**** リトライ再接続 *******");
				} catch (Exception ee) {
					ee.printStackTrace();
					throw ee;
				}
			}

			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 引数でDBコネクションを受ける。
	 *
	 * @param _conn コネクション
	 * @throws Exception 例外
	 */
	public void setConnection(Connection _conn) throws Exception {
		this.conn = _conn;
	}

	/**
	 * コネクションの状態を返す。
	 *
	 * @return true = 接続中 false = 未接続
	 */
	public boolean isOpen() {
		if (conn != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * オートコミット設定。
	 *
	 * @param flg 設定値 true = オートコミット
	 * @throws Exception 例外
	 */
	public void setAutoCommit(boolean flg) throws Exception {
		try {
			if (conn != null) {
				conn.setAutoCommit(flg);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ステートメントにSQL文(String)をセットする
	 *
	 * @param sql SQL文字列
	 * @throws Exception 例外
	 */
	public void setSQL(String sql) throws Exception {
		if (conn != null) {
			statement = conn.prepareStatement(sql);
		}
	}

	/**
	 * ステートメントにSQL文(StringBuilder)をセットする
	 *
	 * @param sql SQL文字列バッファ
	 * @throws Exception 例外
	 */
	public void setSQL(StringBuilder sql) throws Exception {
		if (conn != null) {
			statement = conn.prepareStatement(sql.toString());
		}
	}

	/**
	 * SQL構文の置き換え文字箇所にオブジェクトをバインドする(インスタンスでキャスト)
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, Object val) throws Exception {
		if (statement != null) {
			if (val instanceof String) {
				statement.setString(index, val.toString());

			} else if (val instanceof Integer) {
				statement.setInt(index, (Integer) val);

			} else if (val instanceof Long) {
				statement.setLong(index, (Long) val);

			} else if (val instanceof Double) {
				statement.setDouble(index, (Double) val);

			} else if (val instanceof java.sql.Date) {
				statement.setDate(index, (java.sql.Date) val);

			} else if (val instanceof java.sql.Time) {
				statement.setTime(index, (java.sql.Time) val);

			} else if (val instanceof java.sql.Timestamp) {
				statement.setTimestamp(index, (java.sql.Timestamp) val);
			}
		}
	}

	/**
	 * SQL構文の置き換え文字箇所に数値をバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, int val) throws Exception {
		if (statement != null) {
			statement.setInt(index, val);
		}
	}

	/**
	 * SQL構文の置き換え文字箇所にロング数値をバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, long val) throws Exception {
		if (statement != null) {
			statement.setLong(index, val);
		}
	}

	/**
	 * SQL構文の置き換え文字箇所にダブル数値をバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, double val) throws Exception {
		if (statement != null) {
			statement.setDouble(index, val);
		}
	}

	/**
	 * SQL構文の置き換え文字箇所に文字列をバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, String val) throws Exception {
		if (statement != null) {
			statement.setString(index, val);
		}
	}

	/**
	 * SQL構文の置き換え文字箇所に日付をバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, java.sql.Date val) throws Exception {
		if (statement != null) {
			statement.setDate(index, val);
		}
	}

	/**
	 * SQL構文の置き換え文字箇所に時分をバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, java.sql.Time val) throws Exception {
		if (statement != null) {
			statement.setTime(index, val);
		}
	}

	/**
	 * SQL構文の置き換え文字箇所にタイムスタンプをバインドする。
	 *
	 * @param index 置換え文字インデックス
	 * @param val バインド値
	 * @throws Exception 例外
	 */
	public void bind(int index, java.sql.Timestamp val) throws Exception {
		if (statement != null) {
			statement.setTimestamp(index, val);
		}
	}

	/**
	 * SELECT文を実行して結果を返す。
	 *
	 * @return SELECT文実行結果オブジェクト
	 * @throws Exception 例外
	 */
	public RowTable execute() throws Exception {
		ResultSet result = null;
		ResultSetMetaData rsmeta = null;
		RowTable rowTable = new RowTable();
		try {
			result = statement.executeQuery();
			rsmeta = result.getMetaData();

			while (result.next()) {
				RowColumn row = new RowColumn();
				// カラム行をRowColumnに格納する
				for (int i = 1; i <= rsmeta.getColumnCount(); i++) {
					String key = rsmeta.getColumnLabel(i);

					String val = result.getString(i);
					row.setColumn(key, val);
				}
				// 検索結果内容をRowSetに格納する
				rowTable.addRowColumn(row);
			}

			// レコード内容を確定する
			rowTable.fixRow();

			result.close();
			statement.close();

		} catch (Exception e) {

			String exName = e.getClass().getSimpleName();
			if (exName.equals("CommunicationsException")) {

				System.out.println("***** Connection Error ******************");
				System.out.println("Connection=" + conn);

				if (conn != null) {
					System.out.println("Connection.isClosed()=" + conn.isClosed());
				}
				System.out.println("statement=" + statement);

				if (statement != null) {
					System.out.println("statement.isClosed()=" + statement.isClosed());
				}

			}
			e.printStackTrace();
			throw e;
		} finally {
			result = null;
			rsmeta = null;
			statement = null;
		}
		return rowTable;
	}

	/**
	 * 更新系のSQL文を実行して更新件数を返す。
	 *
	 * @return 更新件数
	 * @throws Exception 例外
	 */
	public int executeUpdate() throws Exception {
		int count = -1;
		try {
			if (statement == null) {
				throw new Exception("statement is null");
			}
			count = statement.executeUpdate();
			statement.close();

		} catch (Exception e) {
			throw e;
		} finally {
			statement = null;
		}
		return count;
	}

	/**
	 * DBコネクションクローズ
	 *
	 * @throws Exception 例外
	 */
	public void closeDB() throws Exception {
		try {
			if (conn != null && conn.isClosed() == false) {
				conn.close();
				conn = null;
				connectStatus = false;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ロールバック
	 */
	public void rollBackDB() {
		try {
			if (conn != null) {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * コミット
	 */
	public void commitDB() {
		try {
			if (conn != null) {
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * データソースをセットする。
	 *
	 * @param dataSource データソース
	 */
	public static void setDataSource(DataSource dataSource) {
		DBAccess.dataSource = dataSource;
	}

	/**
	 * @return dataBaseType
	 */
	public static String getDataBaseType() {
		return dataBaseType;
	}

	/**
	 * @param dataBaseType セットする dataBaseType
	 */
	public static void setDataBaseType(String dataBaseType) {
		DBAccess.dataBaseType = dataBaseType;
	}

}
