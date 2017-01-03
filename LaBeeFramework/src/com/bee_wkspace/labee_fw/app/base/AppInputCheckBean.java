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
 * パラメータ入力チェック用情報ビーン。
 *
 * @author ARTS Laboratory
 *
 * $Id: AppInputCheckBean.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class AppInputCheckBean implements Serializable {

	private static final long serialVersionUID = 393463440403890813L;

	/** ビーン */
	private AppBaseBean appBean;

	/** パラメータ名 */
	private String paramName;

	/** パラメータ値 */
	private String paramValue;

	/** パラメータ表示名 */
	private String viewName;

	/**
	 * 入力チェックエラー結果フラグ<br>
	 * true = エラーなし、false = エラー有り
	 * */
	private boolean inputErrorFlg = false;

	/**
	 * コンストラクタ。
	 *
	 * @param _appBean ビーンオブジェクト
	 * @param _paramName パラメータ名
	 * @param _paramValue パラメータ値
	 * @param _viewName パラメータ表示名
	 */
	public AppInputCheckBean(AppBaseBean _appBean, String _paramName, String _paramValue, String _viewName) {
		this.appBean = _appBean;
		this.paramName = _paramName;
		this.paramValue = _paramValue;
		this.viewName = _viewName;
	}

	/**
	 * ビーンを返す。
	 *
	 * @return appBean ビーン
	 */
	public AppBaseBean getAppBean() {
		return appBean;
	}

	/**
	 * ビーンをセットする。
	 *
	 * @param appBean ビーン
	 */
	public void setAppBean(AppBaseBean appBean) {
		this.appBean = appBean;
	}

	/**
	 * パラメータ名を返す。
	 *
	 * @return パラメータ名
	 */
	public String getParamName() {
		return paramName;
	}

	/**
	 * パラメータ名をセットする。
	 *
	 * @param paramName パラメータ名
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * パラメータ値を返す。
	 *
	 * @return パラメータ値
	 */
	public String getParamValue() {
		return paramValue;
	}

	/**
	 * パラメータ値をセットする。
	 *
	 * @param paramValue パラメータ値
	 */
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	/**
	 * パラメータ表示名を返す。
	 *
	 * @return パラメータ表示名
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * パラメータ表示名をセットする。
	 *
	 * @param viewName パラメータ表示名
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * 入力チェック結果フラグを返す。
	 *
	 * @return true = エラー無し、false = エラー有り
	 */
	public boolean isInputErrorFlg() {
		return inputErrorFlg;
	}

	/**
	 * 入力チェックエラーフラグをセットする
	 *
	 * @param inputErrorFlg 入力チェックエラーフラグ
	 */
	public void setInputErrorFlg(boolean inputErrorFlg) {
		this.inputErrorFlg = inputErrorFlg;
	}

}
