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
package com.bee_wkspace.labee_fw.common;

/**
 * フレームワーク共通定数クラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: CommonDefine.java 554 2016-08-12 21:19:00Z pjmember $
 */
public final class CommonDefine {

	/**	バージョン	*/
	public static final String VERSION = "1.0.0";

	/** OS依存のファイル区切り文字の取得 */
	public static final String SEP = System.getProperty("file.separator");

	/** OS依存の改行文字の取得 */
	public static final String LINE_SEP = System.getProperty("line.separator");

	/** スラッシュ文字列定義 */
	public static final char SLASH = '/';

	/** ターゲットクラス パラメータ名。<br>固定でTGTが定義 */
	public static final String TARGET = "tgt";

	/** ターゲットメソッド パラメータ名。<br>固定でEXEが定義 */
	public static final String EXECUTE = "exe";

	/** WEB画面ID パラメータ名。<br>固定でSCREEN_IDが定義 */
	public static final String SCREEN_ID = "screen_id";

	/** ビーンオブジェクトの受け渡し用の名前。<br>固定でbeanが定義 */
	public static final String BEAN = "bean";

	/** ビーンオブジェクトの受け渡し用の名前。<br>固定でwebBeanが定義 */
	public static final String ENV_BEAN = "envBean";

	/** ビジネスロジック初期実行メソッド名。<br>固定でstartが定義 */
	public static final String START = "start";

	/** アクセス情報エンバイロメント文字列定義 */
	public static final String ENVIROMENT = "ENVIROMENT";

	/** 汎用追加パラメータ１。<br>固定でP1が定義 */
	public static final String PARAM1 = "p1";

	/** 汎用追加パラメータ２。<br>固定でP2が定義 */
	public static final String PARAM2 = "p2";

	/** 汎用追加パラメータ３。<br>固定でP3が定義 */
	public static final String PARAM3 = "p3";
	
	/**	フレームワークセッション名格納クッキー名	*/
	public static final String FW_SESSION_COOKIE_NAME = "_FwSessionCookie";

	/** Beanインスタンスを格納しているMapのセッション格納名 */
	public static final String BEAN_SESSION_MAP_NAME = "BeanInstanceMap";

	/**	ビーンオブジェクトシリアライズフォルダ名	*/
	public static final String SERIALIZE_FOLDER_NAME ="beanSerialize";
	
	/** 空文字定義 */
	public static final String EMPTY = "";

	/** カンマ文字列定義 */
	public static final String COMMA = ",";

	/** 全角スーペース文字定義 */
	public static final String ZEN_SPACE = "　";

	/** 文字コード UTF-8 */
	public static final String ENCODE_UTF_8 = "UTF-8";

	/** 文字コード Shift-JIS */
	public static final String ENCODE_SHIFT_JIS = "Shift_JIS";

	/** パーミッション権限 */
	public static final String PERMISSION = "rwxrwxrwx";

}
