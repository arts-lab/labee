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
 * ダイアログメッセージ情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: AppMessageDialogContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class AppMessageDialogContext implements Serializable {

	private static final long serialVersionUID = -3636650094767944332L;
	/** メッセージタイプ コンプリート */
	public static final int MESSAGE_TYPE_COMPLETE = 1;
	/** メッセージタイプ インフォ */
	public static final int MESSAGE_TYPE_INFO = 2;
	/** メッセージタイプ ワーニング */
	public static final int MESSAGE_TYPE_WARN = 3;
	/** メッセージタイプ エラー */
	public static final int MESSAGE_TYPE_ERROR = 4;
	/** メッセージタイプ 問合せ */
	public static final int MESSAGE_TYPE_QUESTION = 5;

	/** メッセージタイプ */
	private int messageType = MESSAGE_TYPE_INFO;

	/** メッセージ */
	private String message = null;

	/**
	 * コンストラクタ。
	 *
	 * @param messageType メッセージタイプ<br>
	 * 本クラスのメッセージタイプ定数(MESSAGE_TYPE_COMPLETE, MESSAGE_TYPE_ERROR等)を設定する。
	 * @param message メッセージ
	 */
	public AppMessageDialogContext(int messageType, String message) {
		this.messageType = messageType;
		this.message = message;
	}

	/**
	 * メッセージタイプの値を返す。
	 *
	 * @return messageType
	 */
	public int getMessageType() {
		return messageType;
	}

	/**
	 * メッセージタイプをセットする。
	 *
	 * @param messageType メッセ―ジ種類
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	/**
	 * メッセージの値を返す。
	 *
	 * @return message メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * メッセージをセットする。
	 *
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
