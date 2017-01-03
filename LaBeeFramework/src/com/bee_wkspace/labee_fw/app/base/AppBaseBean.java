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
import java.util.ArrayList;

import com.bee_wkspace.labee_fw.app.context.AppMessageDialogContext;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.base.BaseBean;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;

/**
 * Webアプリ用 基底ビーンクラス。<br>
 * <br>
 * 継承元の基底クラス<b>com.bee_wkspace.labee_fw.core.base.BaseBean</b>に対して以下の機能を付加している。<br>
 * ・画面情報とメッセージダイアログ情報のフィールドパラメータを保持。<br>
 * <br>
 * ビーンクラスを作成する場合は本クラスを継承する。
 *
 * @author ARTS Laboratory
 *
 * $Id: AppBaseBean.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class AppBaseBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = -7048490533888778823L;

	/** 画面情報コンテキスト */
	protected ScreenContext screenContext;

	/** アクセス環境情報 */
	protected Environment env;

	/** メッセージダイアログ情報 */
	protected AppMessageDialogContext messageDialogContext;

	/** 画面表示時にクッキーから値取得してセットするパスワードタグID名リスト */
	protected ArrayList<String> passwdSetNameList;

	/** 画面個別に動的実行するJavaScriptメソッドリスト */
	protected ArrayList<String> addScriptList;

	/**
	 * コンストラクタ。
	 */
	public AppBaseBean() {
		super();
	}

	/**
	 * 初期化。
	 * 
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBean#init()
	 */
	@Override
	public void init() {
		super.init();
		messageDialogContext = null;
		if (passwdSetNameList == null) {
			passwdSetNameList = new ArrayList<String>();
		} else {
			passwdSetNameList.clear();
		}
	}

	/**
	 * エラー情報初期化
	 * 
	 * @see com.bee_wkspace.labee_fw.core.base.BaseBean#initError()
	 */
	@Override
	public void initError() {
		super.initError();
		messageDialogContext = null;
	}

	/**
	 * JavaScriptメソッドを追加する。
	 * 
	 * @param methodName JavaScriptメソッド名
	 */
	public void addScript(String methodName) {
		if (addScriptList == null) {
			addScriptList = new ArrayList<String>();
		}
		addScriptList.add(methodName);
	}

	/**
	 * 画面表示時にクッキーにパスワード値を書き込み、パスワードタグに値を動的セットする機能で<br>
	 * セット対象のパスワードタグのid名を追加する。<br>
	 * (クッキー寿命は5秒)
	 * 
	 * @param id パスワードタグのid名
	 */
	public void addPasswdSetName(String id) {
		if (passwdSetNameList.contains(id) == false) {
			passwdSetNameList.add(id);
		}
	}

	/**
	 * 画面情報コンテキストを返す。
	 * 
	 * @return 画面情報コンテキスト
	 */
	public ScreenContext getScreenContext() {
		return screenContext;
	}

	/**
	 * 画面情報コンテキストをセットする。
	 * 
	 * @param screenContext 画面情報コンテキスト
	 */
	public void setScreenContext(ScreenContext screenContext) {
		this.screenContext = screenContext;
	}

	/**
	 * アクセス環境情報を返す。
	 * 
	 * @return アクセス環境情報
	 */
	public Environment getEnv() {
		return env;
	}

	/**
	 * アクセス環境情報をセットする。
	 * 
	 * @param env アクセス環境情報
	 */
	public void setEnv(Environment env) {
		this.env = env;
	}

	/**
	 * メッセージダイアログ情報コンテキストを返す。
	 * 
	 * @return メッセージダイアログ情報コンテキスト
	 */
	public AppMessageDialogContext getMessageDialogContext() {
		return messageDialogContext;
	}

	/**
	 * メッセージダイアログ情報コンテキストをセットする。
	 * 
	 * @param messageDialogContext メッセージダイアログ情報コンテキスト
	 */
	public void setMessageDialogContext(AppMessageDialogContext messageDialogContext) {
		this.messageDialogContext = messageDialogContext;
	}

	/**
	 * メッセージダイアログを設定する。
	 *
	 * @param messageType メッセージタイプ
	 * @param message メッセージ内容
	 */
	public void setMessageDiarog(int messageType, String message) {
		setMessageDialogContext(new AppMessageDialogContext(messageType, message));
	}

	/**
	 * @return passwdSetNameList
	 */
	public ArrayList<String> getPasswdSetNameList() {
		return passwdSetNameList;
	}

	/**
	 * @return addScriptList
	 */
	public ArrayList<String> getAddScriptList() {
		return addScriptList;
	}

}
