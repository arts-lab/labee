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
import java.util.HashMap;

/**
 * リダイレクト情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: RedirectContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class RedirectContext implements Serializable {

	private static final long serialVersionUID = 6686880992621914678L;

	/** SSLフラグ<br>
	 *  true:HTTPS false:http */
	private boolean sslFlg = false;

	/** ターゲット */
	private String target = null;

	/** 実行メソッド */
	private String execute = null;

	/** 追加パラメータ */
	private String[] param = null;

	/** URLに直接リダイレクトする場合のURL */
	private String redirectURL = null;

	/** ステータスコード */
	private String statusCode = null;

	/** ヘッダー登録情報 */
	private HashMap<String, String> headerMap = null;

	/**
	 * コンストラクタ
	 */
	public RedirectContext() {
		super();
		headerMap = new HashMap<String, String>();
	}

	public void addHeader(String headerName, String headerParam) {
		headerMap.put(headerName, headerParam);
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
	 * targetの値を返す。
	 *
	 * @return target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target targetの値をセットする。
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * executeの値を返す。
	 *
	 * @return execute
	 */
	public String getExecute() {
		return execute;
	}

	/**
	 * @param execute executeの値をセットする。
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}

	/**
	 * paramの値を返す。
	 *
	 * @return param
	 */
	public String[] getParam() {
		return param;
	}

	/**
	 * @param param paramの値をセットする。
	 */
	public void setParam(String[] param) {
		this.param = param;
	}

	/**
	 * redirectURLの値を返す。
	 *
	 * @return redirectURL
	 */
	public String getRedirectURL() {
		return redirectURL;
	}

	/**
	 * @param redirectURL redirectURLの値をセットする。
	 */
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	/**
	 * statusCodeの値を返す。
	 *
	 * @return statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode statusCodeの値をセットする。
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * headerMapの値を返す。
	 *
	 * @return headerMap
	 */
	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}

	/**
	 * @param headerMap headerMapの値をセットする。
	 */
	public void setHeaderMap(HashMap<String, String> headerMap) {
		this.headerMap = headerMap;
	}

}
