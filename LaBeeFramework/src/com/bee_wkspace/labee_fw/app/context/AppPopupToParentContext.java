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
 * ポップアップ画面を閉じて親画面に遷移する際の情報。
 */
public class AppPopupToParentContext implements Serializable {

	private static final long serialVersionUID = -7579838265966599329L;

	/**	ポップアップを閉じて親ウインドウのフォームに対してサブミットする場合	*/
	public static final int CLOSE_TO_PARENT_SUBMIT = 1;

	/**	ポップアップを閉じるだけの場合	*/
	public static final int CLOSE_ONLY = 3;

	/**	ポップアップを閉じて続けて別のポップアップを表示する場合	*/
	public static final int CLOSE_TO_NEXT_POPUP = 4;

	/**	ポップアップを閉じて親ウインドウのフォームに対してJSONを返す場合	*/
	public static final int CLOSE_TO_PARENT_JSON = 5;

	/**	ポップアップを閉じる際の処理タイプ	*/
	private int popupCloseType = CLOSE_TO_PARENT_SUBMIT;

	/** 親ウインドウのターゲット */
	private String parentTarget = null;

	/** 親ウインドウの実行メソッド */
	private String parentExecute = null;

	/** 親ウインドウのパラメータ1 */
	private String parentParam1 = null;

	/** 親ウインドウのパラメータ2 */
	private String parentParam2 = null;

	/** 親ウインドウのパラメータ3 */
	private String parentParam3 = null;

	/**	親ウインドウに渡すJSON文字列	*/
	private String json = null;
	
	/**	次のポップアップ情報	*/
	private AppPopupContext nextPopupContext;
	

	/**
	 * @return parentTarget
	 */
	public String getParentTarget() {
		return parentTarget;
	}

	/**
	 * @param parentTarget セットする parentTarget
	 */
	public void setParentTarget(String parentTarget) {
		this.parentTarget = parentTarget;
	}

	/**
	 * @return parentExecute
	 */
	public String getParentExecute() {
		return parentExecute;
	}

	/**
	 * @param parentExecute セットする parentExecute
	 */
	public void setParentExecute(String parentExecute) {
		this.parentExecute = parentExecute;
	}

	/**
	 * @return parentParam1
	 */
	public String getParentParam1() {
		return parentParam1;
	}

	/**
	 * @param parentParam1 セットする parentParam1
	 */
	public void setParentParam1(String parentParam1) {
		this.parentParam1 = parentParam1;
	}

	/**
	 * @return parentParam2
	 */
	public String getParentParam2() {
		return parentParam2;
	}

	/**
	 * @param parentParam2 セットする parentParam2
	 */
	public void setParentParam2(String parentParam2) {
		this.parentParam2 = parentParam2;
	}

	/**
	 * @return parentParam3
	 */
	public String getParentParam3() {
		return parentParam3;
	}

	/**
	 * @param parentParam3 セットする parentParam3
	 */
	public void setParentParam3(String parentParam3) {
		this.parentParam3 = parentParam3;
	}

	/**
	 * @return popupCloseType
	 */
	public int getPopupCloseType() {
		return popupCloseType;
	}

	/**
	 * @param popupCloseType セットする popupCloseType
	 */
	public void setPopupCloseType(int popupCloseType) {
		this.popupCloseType = popupCloseType;
	}

	/**
	 * @return json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json セットする json
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * @return nextPopupContext
	 */
	public AppPopupContext getNextPopupContext() {
		return nextPopupContext;
	}

	/**
	 * @param nextPopupContext セットする nextPopupContext
	 */
	public void setNextPopupContext(AppPopupContext nextPopupContext) {
		this.nextPopupContext = nextPopupContext;
	}

}
