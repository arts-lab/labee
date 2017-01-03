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
 * ポップアップ情報
 *
 * @author ARTS Laboratory
 * 
 * $Id: AppPopupContext.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class AppPopupContext implements Serializable {

	private static final long serialVersionUID = -5096833058685893244L;

	/** ポップアップウインドウのターゲット */
	private String target;

	/** ポップアップウインドウの実行メソッド */
	private String execute;

	/** ポップアップウインドウのパラメータ1 */
	private String param1;

	/** ポップアップウインドウのパラメータ2 */
	private String param2;

	/** ポップアップウインドウのパラメータ3 */
	private String param3;

	/** ポップアップウインドウ表示領域幅 */
	protected int width;

	/** ポップアップウインドウ表示領域高さ */
	protected int height;

	/** 実行ボタン表示名 */
	protected String entryButtonName;

	/** 閉じるボタン表示名 */
	protected String closeButtonName;

	/** 実行ボタン ワードラベルコード */
	protected String entryBtnLabelCd;

	/** 閉じるボタン ワードラベルコード */
	protected String closeBtnLabelCd;

	/** 実行ボタンで実行するフォーム名 */
	protected String nextForm;

	/** 実行ボタンで実行するターゲット名 */
	protected String nextTarget;

	/** 実行ボタンで実行する処理名 */
	protected String nextExecute;

	/** 実行ボタンの実行先に渡すパラメータ1 */
	protected String nextParam1;

	/** 実行ボタンの実行先に渡すパラメータ2 */
	protected String nextParam2;

	/** 実行ボタンの実行先に渡すパラメータ3 */
	protected String nextParam3;

	/** クローズボタンで実行するターゲット名 */
	protected String closeTarget;

	/** クローズボタンで実行する処理名 */
	protected String closeExecute;

	/** 言語ロケールコード */
	protected String localeCd;

	/** ワードラベル区分 */
	protected String labelDiv;

	/**
	 * @return target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return execute
	 */
	public String getExecute() {
		return execute;
	}

	/**
	 * @return param1
	 */
	public String getParam1() {
		return param1;
	}

	/**
	 * @return param2
	 */
	public String getParam2() {
		return param2;
	}

	/**
	 * @return param3
	 */
	public String getParam3() {
		return param3;
	}

	/**
	 * @return entryButtonName
	 */
	public String getEntryButtonName() {
		return entryButtonName;
	}

	/**
	 * @return closeButtonName
	 */
	public String getCloseButtonName() {
		return closeButtonName;
	}

	/**
	 * @return entryBtnLabelCd
	 */
	public String getEntryBtnLabelCd() {
		return entryBtnLabelCd;
	}

	/**
	 * @return closeBtnLabelCd
	 */
	public String getCloseBtnLabelCd() {
		return closeBtnLabelCd;
	}

	/**
	 * @return nextForm
	 */
	public String getNextForm() {
		return nextForm;
	}

	/**
	 * @return nextTarget
	 */
	public String getNextTarget() {
		return nextTarget;
	}

	/**
	 * @return nextExecute
	 */
	public String getNextExecute() {
		return nextExecute;
	}

	/**
	 * @return nextParam1
	 */
	public String getNextParam1() {
		return nextParam1;
	}

	/**
	 * @return nextParam2
	 */
	public String getNextParam2() {
		return nextParam2;
	}

	/**
	 * @return nextParam3
	 */
	public String getNextParam3() {
		return nextParam3;
	}

	/**
	 * @return localeCd
	 */
	public String getLocaleCd() {
		return localeCd;
	}

	/**
	 * @return labelDiv
	 */
	public String getLabelDiv() {
		return labelDiv;
	}

	/**
	 * @param target セットする target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @param execute セットする execute
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}

	/**
	 * @param param1 セットする param1
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * @param param2 セットする param2
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * @param param3 セットする param3
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	/**
	 * @param entryButtonName セットする entryButtonName
	 */
	public void setEntryButtonName(String entryButtonName) {
		this.entryButtonName = entryButtonName;
	}

	/**
	 * @param closeButtonName セットする closeButtonName
	 */
	public void setCloseButtonName(String closeButtonName) {
		this.closeButtonName = closeButtonName;
	}

	/**
	 * @param entryBtnLabelCd セットする entryBtnLabelCd
	 */
	public void setEntryBtnLabelCd(String entryBtnLabelCd) {
		this.entryBtnLabelCd = entryBtnLabelCd;
	}

	/**
	 * @param closeBtnLabelCd セットする closeBtnLabelCd
	 */
	public void setCloseBtnLabelCd(String closeBtnLabelCd) {
		this.closeBtnLabelCd = closeBtnLabelCd;
	}

	/**
	 * @param nextForm セットする nextForm
	 */
	public void setNextForm(String nextForm) {
		this.nextForm = nextForm;
	}

	/**
	 * @param nextTarget セットする nextTarget
	 */
	public void setNextTarget(String nextTarget) {
		this.nextTarget = nextTarget;
	}

	/**
	 * @param nextExecute セットする nextExecute
	 */
	public void setNextExecute(String nextExecute) {
		this.nextExecute = nextExecute;
	}

	/**
	 * @param nextParam1 セットする nextParam1
	 */
	public void setNextParam1(String nextParam1) {
		this.nextParam1 = nextParam1;
	}

	/**
	 * @param nextParam2 セットする nextParam2
	 */
	public void setNextParam2(String nextParam2) {
		this.nextParam2 = nextParam2;
	}

	/**
	 * @param nextParam3 セットする nextParam3
	 */
	public void setNextParam3(String nextParam3) {
		this.nextParam3 = nextParam3;
	}

	/**
	 * @param localeCd セットする localeCd
	 */
	public void setLocaleCd(String localeCd) {
		this.localeCd = localeCd;
	}

	/**
	 * @param labelDiv セットする labelDiv
	 */
	public void setLabelDiv(String labelDiv) {
		this.labelDiv = labelDiv;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param width セットする width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height セットする height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return closeTarget
	 */
	public String getCloseTarget() {
		return closeTarget;
	}

	/**
	 * @return closeExecute
	 */
	public String getCloseExecute() {
		return closeExecute;
	}

	/**
	 * @param closeTarget セットする closeTarget
	 */
	public void setCloseTarget(String closeTarget) {
		this.closeTarget = closeTarget;
	}

	/**
	 * @param closeExecute セットする closeExecute
	 */
	public void setCloseExecute(String closeExecute) {
		this.closeExecute = closeExecute;
	}

}
