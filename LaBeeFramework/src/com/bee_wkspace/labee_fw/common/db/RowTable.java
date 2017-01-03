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

/**
 * DB検索結果の全体を格納するクラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: RowTable.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class RowTable {

	/** カラム行全体を格納するArrayList */
	private ArrayList<RowColumn> dataArray = null;

	/** カラム行の配列 */
	private RowColumn[] rows = null;

	/** カラム行総数 */
	private int size = 0;

	/**
	 * コンストラクタ
	 */
	public RowTable() {
		dataArray = new ArrayList<RowColumn>();
	}

	/**
	 * カラム行を追加する。
	 *
	 * @param row カラム行オブジェクト
	 * @throws Exception 例外
	 */
	public void addRowColumn(RowColumn row) throws Exception {
		dataArray.add(row);
		this.size = dataArray.size();
	}

	/**
	 * 指定行のカラム行を返す。
	 *
	 * @param index 指定行
	 * @return カラム行オブジェクト
	 * @throws Exception 例外
	 */
	public RowColumn getRowColumn(int index) throws Exception {
		if (index <= this.size - 1) {
			return dataArray.get(index);
		} else {
			return null;
		}
	}

	/**
	 * カラム行を配列にセットする。
	 */
	public void fixRow() {
		if (rows != null) {
			rows = null;
		}

		rows = new RowColumn[size];
		for (int i = 0; i < size; i++) {
			rows[i] = dataArray.get(i);
		}
	}

	/**
	 * 保持データを破棄
	 */
	public void dispose() {
		this.dataArray.clear();
		this.size = 0;
		this.rows = null;
	}

	/**
	 * dataArrayの値を返す。
	 * @return dataArray
	 */
	public ArrayList<RowColumn> getDataArray() {
		return dataArray;
	}

	/**
	 * rowsの値を返す。
	 * @return rows
	 */
	public RowColumn[] getRows() {
		return rows;
	}

	/**
	 * sizeの値を返す。
	 * @return size
	 */
	public int getSize() {
		return size;
	}


}
