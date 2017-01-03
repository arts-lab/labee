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
package com.bee_wkspace.labee_fw.app.context;

import java.io.Serializable;

/**
 * WebApp用レスポンスコンテキスト。
 */
public class AppResponseContext implements Serializable {

	private static final long serialVersionUID = -3854742723241593285L;

	/** ポップアップ画面を閉じて親画面に遷移するかのフラグ */
	private boolean popupToParentFlg = false;

	/** ポップアップを閉じて親ウインドウに遷移する際の情報コンテキスト */
	private AppPopupToParentContext popupToParentContext = null;

	/**
	 * @return popupToParentFlg
	 */
	public boolean isPopupToParentFlg() {
		return popupToParentFlg;
	}

	/**
	 * @param popupToParentFlg セットする popupToParentFlg
	 */
	public void setPopupToParentFlg(boolean popupToParentFlg) {
		this.popupToParentFlg = popupToParentFlg;
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

}
