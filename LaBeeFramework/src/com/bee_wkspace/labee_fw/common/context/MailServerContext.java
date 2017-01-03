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
package com.bee_wkspace.labee_fw.common.context;

import java.io.Serializable;

/**
 * メール送信接続情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: MailServerContext.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class MailServerContext implements Serializable {

	private static final long serialVersionUID = -8481114611187331345L;

	/** メールサーバーホスト */
	protected String serverHost = null;

	/** メールサーバー接続ポート */
	protected String serverPort = null;

	/** メール送信者接続アカウント */
	protected String mailAccount = null;

	/** メール送信者接続パスワード */
	protected String mailPassWord = null;

	/** 送信者メールアドレス */
	protected String senderMailAddr = null;

	/** 送信者名 */
	protected String sendersName = null;

	/** タイムアウト */
	protected String timeOut = "40000";

	/** メール送信処理を実行するかのフラグ */
	protected boolean sendFlg = true;

	/** 送信時に認証を行なうかのフラグ */
	protected boolean authFlg = true;

	/** SSL暗号化フラグ */
	protected boolean sslFlg = false;

	/**
	 * @return serverHost
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * @param serverHost セットする serverHost
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * @return serverPort
	 */
	public String getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort セットする serverPort
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return mailAccount
	 */
	public String getMailAccount() {
		return mailAccount;
	}

	/**
	 * @param mailAccount セットする mailAccount
	 */
	public void setMailAccount(String mailAccount) {
		this.mailAccount = mailAccount;
	}

	/**
	 * @return mailPassWord
	 */
	public String getMailPassWord() {
		return mailPassWord;
	}

	/**
	 * @param mailPassWord セットする mailPassWord
	 */
	public void setMailPassWord(String mailPassWord) {
		this.mailPassWord = mailPassWord;
	}

	/**
	 * @return senderMailAddr
	 */
	public String getSenderMailAddr() {
		return senderMailAddr;
	}

	/**
	 * @param senderMailAddr セットする senderMailAddr
	 */
	public void setSenderMailAddr(String senderMailAddr) {
		this.senderMailAddr = senderMailAddr;
	}

	/**
	 * @return sendersName
	 */
	public String getSendersName() {
		return sendersName;
	}

	/**
	 * @param sendersName セットする sendersName
	 */
	public void setSendersName(String sendersName) {
		this.sendersName = sendersName;
	}

	/**
	 * @return timeOut
	 */
	public String getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut セットする timeOut
	 */
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return sendFlg
	 */
	public boolean isSendFlg() {
		return sendFlg;
	}

	/**
	 * @param sendFlg セットする sendFlg
	 */
	public void setSendFlg(boolean sendFlg) {
		this.sendFlg = sendFlg;
	}

	/**
	 * @return sslFlg
	 */
	public boolean isSslFlg() {
		return sslFlg;
	}

	/**
	 * @param sslFlg セットする sslFlg
	 */
	public void setSslFlg(boolean sslFlg) {
		this.sslFlg = sslFlg;
	}

	/**
	 * @return authFlg
	 */
	public boolean isAuthFlg() {
		return authFlg;
	}

	/**
	 * @param authFlg セットする authFlg
	 */
	public void setAuthFlg(boolean authFlg) {
		this.authFlg = authFlg;
	}

}
