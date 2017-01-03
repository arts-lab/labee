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
package com.bee_wkspace.labee_fw.core.base;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.context.FileUploadContext;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * フレームワーク用ビジネスロジックの基底インターフェース。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: BlogicInterface.java 554 2016-08-12 21:19:00Z pjmember $
 */
public interface BlogicInterface {

	/**
	 * ビーンインスタンスを生成する。
	 * 
	 * @param beanClassName ビーンクラス名
	 * @param beanReuse ビーン再利用フラグ
	 * @throws FwException 例外
	 */
	void createBean(String beanClassName, boolean beanReuse) throws FwException;

	/**
	 * 初期化処理
	 * 
	 * @throws FwException 例外
	 */
	void init() throws FwException;

	/**
	 * メインメソッドを実行する前に実行するプリ処理。<br>
	 * 継承先で実装する。
	 * 
	 * @return 結果フラグ
	 * @throws Exception 例外
	 */
	boolean preProcess() throws Exception;

	/**
	 * メインメソッドを実行した後に実行するポスト処理<br>
	 * 継承先で実装する。
	 *
	 * @throws Exception 例外
	 */
	void postProcess() throws Exception;

	/**
	 * 前処理、メイン処理、後処理を行った後に最後に必ず実行するファイナリー処理。<br>
	 * 継承先で実装する。
	 *
	 * @throws Exception 例外
	 */
	void finallyProcess() throws Exception;

	/**
	 * ポスト処理実行フラグの設定値を返す。<br>
	 *
	 * @return true = ポスト処理を行なう
	 */
	boolean isPostProcessFlg();

	/**
	 * ビジネスロジックと対になるビーンオブジェクトを返す。
	 *
	 * @return ビーンオブジェクト
	 */
	BeanInterface getBean();

	/**
	 * クッキー配列をセットする。
	 *
	 * @param cookies クッキーオブジェクト配列
	 */
	void setCookies(Cookie[] cookies);

	/**
	 * クッキーオブジェクトリストを返す。
	 *
	 * @return クッキーオブジェクトリスト
	 */
	ArrayList<Cookie> getAddCokieList();

	/**
	 * アップロードファイル情報をセットする。
	 * 
	 * @param uploadFileContext アップロードファイル情報
	 */
	void setUploadFileContext(FileUploadContext uploadFileContext);

	/**
	 * レスポンスコンテキストを返す。
	 *
	 * @return レスポンスコンテキスト
	 */
	ResponseContext getResponseContext();

	/**
	 * セッションオブジェクトをセットする。
	 * 
	 * @param session セッションオブジェクト
	 */
	void setSession(HttpSession session);

	/**
	 * アクセス環境情報をセットする。
	 * 
	 * @param env アクセス環境情報
	 */
	void setEnv(Environment env);

	/**
	 * アクセス環境情報を返す。
	 *
	 * @return env アクセス環境情報
	 */
	Environment getEnv();

	/**
	 * パラメータ格納マップをセットする。
	 * 
	 * @param paramsMap パラメータ格納マップ
	 */
	void setParamsMap(HashMap<String, String> paramsMap);
}
