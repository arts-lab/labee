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
package com.bee_wkspace.labee_fw.core.context;

import java.io.Serializable;

/**
 * 画面情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: ScreenContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class ScreenContext implements Serializable {

	private static final long serialVersionUID = -304975318278239598L;

	/** 画面ID */
	protected String screenId;

	/** 画面名 */
	protected String screenName;

	/** 画面名ラベルコード */
	protected String labelCd;

	/** 画面名ラベルコード区分 */
	protected String labelDiv;

	/** JSP (PC) */
	protected String jspPC;

	/** JSP (SmartPhone) */
	protected String jspSphone;

	/** JSP (Tablet) */
	protected String jspTablet;

	/**	画面個別の追加スクリプトファイル	*/
	protected String[] addScriptList;

	/**	画面個別の追加スタイルシートファイル	*/
	protected String[] addCssList;

	/** オプションパラメータ1 */
	protected String option1;

	/** オプションパラメータ2 */
	protected String option2;

	/** オプションパラメータ3 */
	protected String option3;

	/** オプションパラメータ4 */
	protected String option4;

	/** ブラウザタイトル表示名 */
	protected String viewTitle;

	/** SSLフラグ */
	protected boolean sslFlg = false;

	/**
	 * screenIdの値を返す。
	 *
	 * @return screenId
	 */
	public String getScreenId() {
		return screenId;
	}

	/**
	 * @param screenId screenIdの値をセットする。
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	/**
	 * screenNameの値を返す。
	 *
	 * @return screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName screenNameの値をセットする。
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * labelCdの値を返す。
	 *
	 * @return labelCd
	 */
	public String getLabelCd() {
		return labelCd;
	}

	/**
	 * @param labelCd labelCdの値をセットする。
	 */
	public void setLabelCd(String labelCd) {
		this.labelCd = labelCd;
	}

	/**
	 * jspPCの値を返す。
	 *
	 * @return jspPC
	 */
	public String getJspPC() {
		return jspPC;
	}

	/**
	 * @param jspPC jspPCの値をセットする。
	 */
	public void setJspPC(String jspPC) {
		this.jspPC = jspPC;
	}

	/**
	 * jspSphoneの値を返す。
	 *
	 * @return jspSphone
	 */
	public String getJspSphone() {
		return jspSphone;
	}

	/**
	 * @param jspSphone jspSphoneの値をセットする。
	 */
	public void setJspSphone(String jspSphone) {
		this.jspSphone = jspSphone;
	}

	/**
	 * jspTabletの値を返す。
	 *
	 * @return jspTablet
	 */
	public String getJspTablet() {
		return jspTablet;
	}

	/**
	 * @param jspTablet jspTabletの値をセットする。
	 */
	public void setJspTablet(String jspTablet) {
		this.jspTablet = jspTablet;
	}

	/**
	 * option1の値を返す。
	 *
	 * @return option1
	 */
	public String getOption1() {
		return option1;
	}

	/**
	 * @param option1 option1の値をセットする。
	 */
	public void setOption1(String option1) {
		this.option1 = option1;
	}

	/**
	 * option2の値を返す。
	 *
	 * @return option2
	 */
	public String getOption2() {
		return option2;
	}

	/**
	 * @param option2 option2の値をセットする。
	 */
	public void setOption2(String option2) {
		this.option2 = option2;
	}

	/**
	 * option3の値を返す。
	 *
	 * @return option3
	 */
	public String getOption3() {
		return option3;
	}

	/**
	 * @param option3 option3の値をセットする。
	 */
	public void setOption3(String option3) {
		this.option3 = option3;
	}

	/**
	 * option4の値を返す。
	 *
	 * @return option4
	 */
	public String getOption4() {
		return option4;
	}

	/**
	 * @param option4 option4の値をセットする。
	 */
	public void setOption4(String option4) {
		this.option4 = option4;
	}

	/**
	 * viewTitleの値を返す。
	 *
	 * @return viewTitle
	 */
	public String getViewTitle() {
		return viewTitle;
	}

	/**
	 * @param viewTitle viewTitleの値をセットする。
	 */
	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	/**
	 * sslFlgの値を返す。
	 *
	 * @return sslFlg
	 */
	public boolean isSslFlg() {
		return sslFlg;
	}

	/**
	 * @param sslFlg sslFlgの値をセットする。
	 */
	public void setSslFlg(boolean sslFlg) {
		this.sslFlg = sslFlg;
	}

	/**
	 * labelDivの値を返す。
	 *
	 * @return labelDiv
	 */
	public String getLabelDiv() {
		return labelDiv;
	}

	/**
	 * @param labelDiv labelDivの値をセットする。
	 */
	public void setLabelDiv(String labelDiv) {
		this.labelDiv = labelDiv;
	}

	/**
	 * addScriptListの値を返す。
	 * @return addScriptList
	 */
	public String[] getAddScriptList() {
		return addScriptList;
	}

	/**
	 * @param addScriptList  addScriptListの値をセットする。
	 */
	public void setAddScriptList(String[] addScriptList) {
		this.addScriptList = addScriptList;
	}

	/**
	 * addCssListの値を返す。
	 * @return addCssList
	 */
	public String[] getAddCssList() {
		return addCssList;
	}

	/**
	 * @param addCssList  addCssListの値をセットする。
	 */
	public void setAddCssList(String[] addCssList) {
		this.addCssList = addCssList;
	}

}
