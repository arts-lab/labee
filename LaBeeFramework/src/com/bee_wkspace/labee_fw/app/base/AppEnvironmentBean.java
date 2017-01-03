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
import java.util.ArrayList;

import com.bee_wkspace.labee_fw.app.context.AppPopupToParentContext;
import com.bee_wkspace.labee_fw.core.Environment;

/**
 * Webアプリ環境情報を格納するビーン。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: AppEnvironmentBean.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class AppEnvironmentBean implements Serializable {

	private static final long serialVersionUID = 8625953026779535320L;

	/** アクセス情報 */
	private Environment env;

	/** ポップアップから親画面に遷移する際の情報 */
	private AppPopupToParentContext popupToParentContext;

	/**	画面表示時にクッキーから値取得してセットするパスワードタグID名リスト	*/
	private ArrayList<String> passwdSetNameList;
	
	
	/**
	 * @return env
	 */
	public Environment getEnv() {
		return env;
	}

	/**
	 * @param env セットする env
	 */
	public void setEnv(Environment env) {
		this.env = env;
	}

	/**
	 * @return popupToParentContext
	 */
	public AppPopupToParentContext getPopupToParentContext() {
		return popupToParentContext;
	}

	/**
	 * @param popupToParentContext セットする popupToParentContext
	 */
	public void setPopupToParentContext(AppPopupToParentContext popupToParentContext) {
		this.popupToParentContext = popupToParentContext;
	}

	/**
	 * @return passwdSetNameList
	 */
	public ArrayList<String> getPasswdSetNameList() {
		return passwdSetNameList;
	}

	/**
	 * @param passwdSetNameList セットする passwdSetNameList
	 */
	public void setPasswdSetNameList(ArrayList<String> passwdSetNameList) {
		this.passwdSetNameList = passwdSetNameList;
	}

}
