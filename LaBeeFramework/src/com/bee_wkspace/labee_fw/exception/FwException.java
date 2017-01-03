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
package com.bee_wkspace.labee_fw.exception;

/**
 * フレームワーク用例外クラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: FwException.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class FwException extends Exception {

	private static final long serialVersionUID = -7823587074062995468L;

	/** 発生元の例外クラス */
	private Throwable catchException = null;

	/** メッセージ */
	private String appMsg = null;

	/**
	 * コンストラクタ
	 *
	 * @param e 例外オブジェクト
	 */
	public FwException(Throwable e) {
		super(e.getMessage());
		this.catchException = e;
		this.appMsg = e.getMessage();
	}

	/**
	 * コンストラクタ
	 *
	 * @param msg メッセージ
	 * @param e 例外オブジェクト
	 */
	public FwException(String msg, Throwable e) {
		super(msg);
		this.appMsg = msg;
		this.catchException = e;
	}

	/**
	 * コンストラクタ
	 *
	 * @param msg メッセージ文字列
	 */
	public FwException(String msg) {
		super(msg);
		this.appMsg = msg;
		this.catchException = new Exception(msg);
	}

	/**
	 * 例外処理で内部的にキャッチした例外を返す。
	 *
	 * @return 例外
	 */
	public Throwable getCatchException() {
		return catchException;
	}

	/**
	 * 例外処理で内部的にキャッチした例外をセットする。
	 *
	 * @param catchException 例外
	 */
	public void setCatchException(Throwable catchException) {
		this.catchException = catchException;
	}

	/**
	 * 例外処理で内部的に設定したメッセージを返す。
	 *
	 * @return メッセージ
	 */
	public String getAppMsg() {
		return appMsg;
	}

	/**
	 * 例外処理で内部的に設定したメッセージをセットする。
	 *
	 * @param appMsg メッセージ
	 */
	public void setAppMsg(String appMsg) {
		this.appMsg = appMsg;
	}

}
