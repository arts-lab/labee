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
package com.bee_wkspace.labee_fw.app.base;

import java.io.Serializable;

/**
 * リスト表示 検索条件ベースコンディション。
 * 
 * @author ARTS Laboratory
 *
 * $Id:
 */
public class AppSearchCondition implements AppSearchCondiInterface, Serializable {

	private static final long serialVersionUID = 3933166665469816182L;
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";

	/** 1ページに表示するデフォルト件数 */
	public static final int DEFAULT_PAGE_SHOW_SIZE = 50;

	/** ページ移動 リンクタイプ 次 */
	public static final int MOVE_NEXT = 1;
	/** ページ移動 リンクタイプ 前 */
	public static final int MOVE_BEFORE = 2;
	/** ページ移動 リンクタイプ 最後 */
	public static final int MOVE_LAST = 6;
	/** ページ移動 リンクタイプ 先頭 */
	public static final int MOVE_TOP = 7;

	/** 検索結果総数 */
	protected int resultTotalSize;

	/** 表示要素インデックス から */
	protected int indexFrom;

	/** 表示要素インデックス まで */
	protected int indexTo;

	/** 次リンク フラグ */
	protected boolean nextFlg;

	/** 前リンク フラグ */
	protected boolean beforeFlg;

	/** 最後リンク フラグ */
	protected boolean lastFlg;

	/** 先頭リンク フラグ */
	protected boolean topFlg;

	/** ソートターゲット */
	protected String sortTarget;

	/** ソートタイプ */
	protected String sortType;
	
	/**	1ページに表示する件数	*/
	protected int pageShowSize = DEFAULT_PAGE_SHOW_SIZE; 

	/**
	 * コンストラクタ。
	 */
	public AppSearchCondition() {
		init();
	}

	/**
	 * 初期化。
	 */
	@Override
	public void init() {
		resultTotalSize = 0;
		indexFrom = 0;
		nextFlg = false;
		beforeFlg = false;
		lastFlg = false;
		topFlg = false;
		sortTarget = null;
		sortType = null;
	}

	/**
	 * @return resultTotalSize
	 */
	@Override
	public int getResultTotalSize() {
		return resultTotalSize;
	}

	/**
	 * @param resultTotalSize セットする resultTotalSize
	 */
	@Override
	public void setResultTotalSize(int resultTotalSize) {
		this.resultTotalSize = resultTotalSize;
	}

	/**
	 * @return indexFrom
	 */
	@Override
	public int getIndexFrom() {
		return indexFrom;
	}

	/**
	 * @param indexFrom セットする indexFrom
	 */
	@Override
	public void setIndexFrom(int indexFrom) {
		this.indexFrom = indexFrom;
	}

	/**
	 * @return indexTo
	 */
	@Override
	public int getIndexTo() {
		return indexTo;
	}

	/**
	 * @param indexTo セットする indexTo
	 */
	@Override
	public void setIndexTo(int indexTo) {
		this.indexTo = indexTo;
	}

	/**
	 * @return nextFlg
	 */
	@Override
	public boolean isNextFlg() {
		return nextFlg;
	}

	/**
	 * @param nextFlg セットする nextFlg
	 */
	@Override
	public void setNextFlg(boolean nextFlg) {
		this.nextFlg = nextFlg;
	}

	/**
	 * @return beforeFlg
	 */
	@Override
	public boolean isBeforeFlg() {
		return beforeFlg;
	}

	/**
	 * @param beforeFlg セットする beforeFlg
	 */
	@Override
	public void setBeforeFlg(boolean beforeFlg) {
		this.beforeFlg = beforeFlg;
	}

	/**
	 * @return lastFlg
	 */
	@Override
	public boolean isLastFlg() {
		return lastFlg;
	}

	/**
	 * @param lastFlg セットする lastFlg
	 */
	@Override
	public void setLastFlg(boolean lastFlg) {
		this.lastFlg = lastFlg;
	}

	/**
	 * @return topFlg
	 */
	@Override
	public boolean isTopFlg() {
		return topFlg;
	}

	/**
	 * @param topFlg セットする topFlg
	 */
	@Override
	public void setTopFlg(boolean topFlg) {
		this.topFlg = topFlg;
	}

	/**
	 * @return sortTarget
	 */
	@Override
	public String getSortTarget() {
		return sortTarget;
	}

	/**
	 * @param sortTarget セットする sortTarget
	 */
	@Override
	public void setSortTarget(String sortTarget) {
		this.sortTarget = sortTarget;
	}

	/**
	 * @return sortType
	 */
	@Override
	public String getSortType() {
		return sortType;
	}

	/**
	 * @param sortType セットする sortType
	 */
	@Override
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * @return pageShowSize
	 */
	@Override
	public int getPageShowSize() {
		return pageShowSize;
	}

	/**
	 * @param pageShowSize セットする pageShowSize
	 */
	@Override
	public void setPageShowSize(int pageShowSize) {
		this.pageShowSize = pageShowSize;
	}

}
