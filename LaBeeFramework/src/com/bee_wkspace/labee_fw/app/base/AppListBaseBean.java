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

import com.bee_wkspace.labee_fw.app.base.AppBaseBean;
import com.bee_wkspace.labee_fw.app.base.AppSearchCondiInterface;

/**
 * Webアプリ用 一覧表示 基底ビーンクラス。<br>
 * <br>
 * 継承元の基底クラス<b>com.bee_wkspace.labee_fw.core.base.AppBaseBean</b>に対して以下の機能を付加している。<br>
 * ・検索条件情報を保持<br>
 *<br>
 * ビーンクラスを作成する場合は本クラスを継承する。
 *
 * @author ARTS Laboratory
 *
 * $Id: AppListBaseBean.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class AppListBaseBean extends AppBaseBean implements AppListBeanInterface, Serializable {

	private static final long serialVersionUID = 5229869442620555194L;
	
	/**	検索条件情報	*/
	protected AppSearchCondiInterface searchCondition;

	/**
	 * 初期化。
	 * 
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBean#init()
	 */
	@Override
	public void init() {
		super.init();
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * labee.base.AppListBeanInterface#setSearchCondition(com.bee_wkspace.labee_fw
	 * .app.base.AppSearchCondiInterface)
	 */
	@Override
	public void setSearchCondition(AppSearchCondiInterface searchCondition) {
		this.searchCondition = searchCondition;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see labee.base.AppListBeanInterface#getSearchCondition()
	 */
	@Override
	public AppSearchCondiInterface getSearchCondition() {
		return searchCondition;
	}

}
