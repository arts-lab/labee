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

/**
 * リスト表示 検索条件ベースコンディション インターフェース
 *
 * @author ARTS Laboratory
 * 
 * $Id: AppSearchCondiInterface.java 559 2016-08-14 12:24:00Z pjmember $
 */
public interface AppSearchCondiInterface {

	/**
	 * @return resultTotalSize
	 */
	public int getResultTotalSize();

	/**
	 * @param resultTotalSize セットする resultTotalSize
	 */
	public void setResultTotalSize(int resultTotalSize);

	/**
	 * @return indexFrom
	 */
	public int getIndexFrom();

	/**
	 * @param indexFrom セットする indexFrom
	 */
	public void setIndexFrom(int indexFrom);

	/**
	 * @return indexTo
	 */
	public int getIndexTo();

	/**
	 * @param indexTo セットする indexTo
	 */
	public void setIndexTo(int indexTo);

	/**
	 * @return nextFlg
	 */
	public boolean isNextFlg();

	/**
	 * @param nextFlg セットする nextFlg
	 */
	public void setNextFlg(boolean nextFlg);

	/**
	 * @return beforeFlg
	 */
	public boolean isBeforeFlg();

	/**
	 * @param beforeFlg セットする beforeFlg
	 */
	public void setBeforeFlg(boolean beforeFlg);

	/**
	 * @return lastFlg
	 */
	public boolean isLastFlg();

	/**
	 * @param lastFlg セットする lastFlg
	 */
	public void setLastFlg(boolean lastFlg);

	/**
	 * @return topFlg
	 */
	public boolean isTopFlg();

	/**
	 * @param topFlg セットする topFlg
	 */
	public void setTopFlg(boolean topFlg);

	/**
	 * @return sortTarget
	 */
	public String getSortTarget();

	/**
	 * @param sortTarget セットする sortTarget
	 */
	public void setSortTarget(String sortTarget);

	/**
	 * @return sortType
	 */
	public String getSortType();

	/**
	 * @param sortType セットする sortType
	 */
	public void setSortType(String sortType);
	
	

	/**
	 * @return pageShowSize
	 */
	public int getPageShowSize();

	/**
	 * @param pageShowSize セットする pageShowSize
	 */
	public void setPageShowSize(int pageShowSize);
	
	/**
	 * 初期化
	 */
	public void init();
}
