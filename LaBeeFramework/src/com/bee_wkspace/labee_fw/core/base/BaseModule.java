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
package com.bee_wkspace.labee_fw.core.base;

import javax.servlet.http.HttpSession;

import com.bee_wkspace.labee_fw.common.db.DBAccess;
import com.bee_wkspace.labee_fw.core.Environment;

/**
 * モジュール基底クラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: BaseModule.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class BaseModule {

	/**	 セッションオブジェクト	*/
	protected HttpSession session = null;

	/**	DB接続クラス	*/
	protected DBAccess dba = null;

	/**	アクセス環境情報	*/
	protected Environment env = null;

	/**
	 * コンストラクタ
	 * @param session セッション
	 * @param dba	DB接続クラス
	 * @param env	アクセス環境情報
	 */
	public BaseModule(HttpSession session, DBAccess dba, Environment env) {
		super();
		this.session = session;
		this.dba = dba;
		this.env = env;
	}



}
