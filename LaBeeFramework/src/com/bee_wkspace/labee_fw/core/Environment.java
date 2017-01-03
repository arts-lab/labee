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
package com.bee_wkspace.labee_fw.core;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.bee_wkspace.labee_fw.common.DateTimeUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;

/**
 * アクセス環境情報クラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: Environment.java 574 2016-08-16 16:10:05Z pjmember $
 */
public class Environment implements Serializable {

	private static final long serialVersionUID = -8200136671112445387L;

	/** 実行ターゲット名 */
	protected String targetName = null;

	/** 実行メソッド名 */
	protected String executeName = null;

	/** URL */
	protected String URL = null;

	/** URI */
	protected String URI = null;

	/** コンテキストパス */
	protected String contextPath = null;

	/** サーバー名 */
	protected String serverName = null;

	/** スキーマ(HTTP, HTTPS等) */
	protected String scheme = null;

	/** 接続時のポート */
	protected String port = null;

	/** WEB APPパス */
	protected String webAppPath = null;

	/** docBase(server.xmlのContextのdocBase値) */
	protected String docBase = null;

	/**	IPアドレス	*/
	protected String ipAddress = null;
	
	/** ホスト */
	protected String host = null;

	/** クライアント種別 */
	protected String userAgent = null;

	/** リファラー */
	protected String referer = null;

	/** リモートユーザー */
	protected String remoteUser = null;

	/** エラーコード */
	protected String errorCode = null;

	/** クローラロボットフラグ */
	protected boolean robotFlg = false;

	/** スマートフォンアクセスフラグ */
	protected boolean smtPhoneFlg = false;

	/** タブレットパッドアクセスフラグ */
	protected boolean tabletPadFlg = false;

	/** アクセス日付 */
	protected String accessDate = null;

	/** 汎用拡張パラメータ1 */
	protected String exParam1 = null;

	/** 汎用拡張パラメータ2 */
	protected String exParam2 = null;

	/** 汎用拡張パラメータ3 */
	protected String exParam3 = null;
	
	/**	リモートホストのFQDN取得フラグ <br>
	 * trueの場合はrequest.getRemoteHost()を実行して
	 * host変数に格納する
	 * */
	protected boolean remoteHostFlg = false;

	/**
	 * コンストラクタ
	 */
	public Environment() {
		super();
	}

	/**
	 * リクエスト情報をアクセス環境情報クラスオブジェクトにセットする。
	 *
	 * @param request リクエストオブジェクト
	 * @param env アクセス環境情報
	 */
	public static void setEnviromentParam(HttpServletRequest request, Environment env) {

		env.setURL(new String(request.getRequestURL()));
		env.setURI(request.getRequestURI());
		env.setServerName(request.getServerName());
		env.setPort(Integer.toString(request.getServerPort()));
		env.setScheme(request.getScheme());

		String servletPath = request.getServletPath();
		if (StringUtil.isNull(servletPath) && servletPath.length() > 1) {
			servletPath = servletPath.substring(1, servletPath.length());
		}
		env.setContextPath(servletPath);

		if (env.isRemoteHostFlg()) {
			env.setHost(request.getRemoteHost());
		}
		env.setIpAddress(request.getRemoteAddr());
		
		String userAgent = request.getHeader("User-Agent");
		env.setUserAgent(userAgent);
		env.setReferer(request.getHeader("Referer"));
		env.setRemoteUser(request.getRemoteUser());
		env.setAccessDate(DateTimeUtil.getRealTime());

		// String hostName = env.getServerName();
		String path = env.getURI();

		// パスからトップパスのみを抽出
		if (StringUtil.isNull(path) && path.length() > 1) {
			int idx2 = path.indexOf("/", 1);
			if (idx2 != -1) {
				path = path.substring(0, idx2 + 1);
				if (path.equals("//")) {
					path = "/";
				}
			} else {
				path = "/";
			}
		}
		env.setWebAppPath(path);

		// クローラロボットチェック
		int chk = -1;
		boolean robotFlg = false;

		if (SystemConfigMng.getRobotList() != null) {
			int cnt = SystemConfigMng.getRobotList().length;
			for (int i = 0; i < cnt; i++) {
				chk = userAgent.indexOf(SystemConfigMng.getRobotList()[i]);
				if (chk != -1) {
					robotFlg = true;
					break;
				}
			}
		}
		env.setRobotFlg(robotFlg);

		// スマートフォンアクセスチェック
		chk = -1;
		boolean smtPhoneFlg = false;
		if (SystemConfigMng.getSmtPhoneList() != null) {
			int cnt = SystemConfigMng.getSmtPhoneList().length;
			for (int i = 0; i < cnt; i++) {
				chk = userAgent.indexOf(SystemConfigMng.getSmtPhoneList()[i]);
				if (chk != -1) {
					smtPhoneFlg = true;
					break;
				}
			}
		}
		env.setSmtPhoneFlg(smtPhoneFlg);

		// タブレットパッドアクセスチェック
		chk = -1;
		boolean tabletPadFlg = false;
		if (SystemConfigMng.getTabletPadList() != null) {
			int cnt = SystemConfigMng.getTabletPadList().length;
			for (int i = 0; i < cnt; i++) {
				chk = userAgent.indexOf(SystemConfigMng.getTabletPadList()[i]);
				if (chk != -1) {
					tabletPadFlg = true;
					break;
				}
			}
		}
		env.setTabletPadFlg(tabletPadFlg);

	}

	/**
	 * targetNameの値を返す。
	 *
	 * @return targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param targetName targetNameの値をセットする。
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	/**
	 * executeNameの値を返す。
	 *
	 * @return executeName
	 */
	public String getExecuteName() {
		return executeName;
	}

	/**
	 * @param executeName executeNameの値をセットする。
	 */
	public void setExecuteName(String executeName) {
		this.executeName = executeName;
	}

	/**
	 * uRLの値を返す。
	 *
	 * @return uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL uRLの値をセットする。
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * uRIの値を返す。
	 *
	 * @return uRI
	 */
	public String getURI() {
		return URI;
	}

	/**
	 * @param uRI uRIの値をセットする。
	 */
	public void setURI(String uRI) {
		URI = uRI;
	}

	/**
	 * contextPathの値を返す。
	 *
	 * @return contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath contextPathの値をセットする。
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * serverNameの値を返す。
	 *
	 * @return serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName serverNameの値をセットする。
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * schemeの値を返す。
	 *
	 * @return scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme schemeの値をセットする。
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * portの値を返す。
	 *
	 * @return port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port portの値をセットする。
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * webAppPathの値を返す。
	 *
	 * @return webAppPath
	 */
	public String getWebAppPath() {
		return webAppPath;
	}

	/**
	 * @param webAppPath webAppPathの値をセットする。
	 */
	public void setWebAppPath(String webAppPath) {
		this.webAppPath = webAppPath;
	}

	/**
	 * docBaseの値を返す。
	 *
	 * @return docBase
	 */
	public String getDocBase() {
		return docBase;
	}

	/**
	 * @param docBase docBaseの値をセットする。
	 */
	public void setDocBase(String docBase) {
		this.docBase = docBase;
	}

	/**
	 * hostの値を返す。
	 *
	 * @return host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host hostの値をセットする。
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * userAgentの値を返す。
	 *
	 * @return userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent userAgentの値をセットする。
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * refererの値を返す。
	 *
	 * @return referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer refererの値をセットする。
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * remoteUserの値を返す。
	 *
	 * @return remoteUser
	 */
	public String getRemoteUser() {
		return remoteUser;
	}

	/**
	 * @param remoteUser remoteUserの値をセットする。
	 */
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

	/**
	 * errorCodeの値を返す。
	 *
	 * @return errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode errorCodeの値をセットする。
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * robotFlgの値を返す。
	 *
	 * @return robotFlg
	 */
	public boolean isRobotFlg() {
		return robotFlg;
	}

	/**
	 * @param robotFlg robotFlgの値をセットする。
	 */
	public void setRobotFlg(boolean robotFlg) {
		this.robotFlg = robotFlg;
	}

	/**
	 * smtPhoneFlgの値を返す。
	 *
	 * @return smtPhoneFlg
	 */
	public boolean isSmtPhoneFlg() {
		return smtPhoneFlg;
	}

	/**
	 * @param smtPhoneFlg smtPhoneFlgの値をセットする。
	 */
	public void setSmtPhoneFlg(boolean smtPhoneFlg) {
		this.smtPhoneFlg = smtPhoneFlg;
	}

	/**
	 * tabletPadFlgの値を返す。
	 *
	 * @return tabletPadFlg
	 */
	public boolean isTabletPadFlg() {
		return tabletPadFlg;
	}

	/**
	 * @param tabletPadFlg tabletPadFlgの値をセットする。
	 */
	public void setTabletPadFlg(boolean tabletPadFlg) {
		this.tabletPadFlg = tabletPadFlg;
	}

	/**
	 * accessDateの値を返す。
	 *
	 * @return accessDate
	 */
	public String getAccessDate() {
		return accessDate;
	}

	/**
	 * @param accessDate accessDateの値をセットする。
	 */
	public void setAccessDate(String accessDate) {
		this.accessDate = accessDate;
	}

	/**
	 * exParam1の値を返す。
	 *
	 * @return exParam1
	 */
	public String getExParam1() {
		return exParam1;
	}

	/**
	 * @param exParam1 exParam1の値をセットする。
	 */
	public void setExParam1(String exParam1) {
		this.exParam1 = exParam1;
	}

	/**
	 * exParam2の値を返す。
	 *
	 * @return exParam2
	 */
	public String getExParam2() {
		return exParam2;
	}

	/**
	 * @param exParam2 exParam2の値をセットする。
	 */
	public void setExParam2(String exParam2) {
		this.exParam2 = exParam2;
	}

	/**
	 * exParam3の値を返す。
	 *
	 * @return exParam3
	 */
	public String getExParam3() {
		return exParam3;
	}

	/**
	 * @param exParam3 exParam3の値をセットする。
	 */
	public void setExParam3(String exParam3) {
		this.exParam3 = exParam3;
	}

	/**
	 * @return ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress セットする ipAddress
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return remoteHostFlg
	 */
	public boolean isRemoteHostFlg() {
		return remoteHostFlg;
	}

	/**
	 * @param remoteHostFlg セットする remoteHostFlg
	 */
	public void setRemoteHostFlg(boolean remoteHostFlg) {
		this.remoteHostFlg = remoteHostFlg;
	}

}
