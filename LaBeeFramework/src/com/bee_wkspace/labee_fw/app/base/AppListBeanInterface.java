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

import com.bee_wkspace.labee_fw.app.base.AppSearchCondiInterface;

/**
 * Webアプリ用 一覧表示 基底ビーンインターフェース。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: AppListBeanInterface.java 559 2016-08-14 12:24:00Z pjmember $
 */
public interface AppListBeanInterface {

	/**
	 * 検索条件情報をセットする。
	 * @param searchCondition 検索条件
	 */
	void setSearchCondition(AppSearchCondiInterface searchCondition);

	/**
	 * 検索条件情報を返す。
	 * @return 検索条件情報
	 */
	AppSearchCondiInterface getSearchCondition();
}
