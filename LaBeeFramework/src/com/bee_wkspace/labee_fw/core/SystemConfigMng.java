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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.EncodeUtil;
import com.bee_wkspace.labee_fw.common.LogWriter;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.db.DBAccess;

/**
 * フレームワークコンフィグプロパティー管理クラス。<br>
 * <br>
 * フレームワークで使用する基本的設定値をプロパティーファイルから読み込みメモリ上に静的保持する。<br>
 * 設定ファイルは<b>/config</b>フォルダ内に格納されている事が必要がある。<br>
 * ・LaBeeFramework.properties<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: SystemConfigMng.java 561 2016-08-14 13:36:29Z pjmember $
 */
public class SystemConfigMng {

	/** プロパティーファイル名 */
	private static String CONFIG_FILE = "config/LaBeeFramework.properties";

	/** HTTPパス */
	private static String httpPath;

	/** HTTPSパス */
	private static String httpsPath;

	/** HTTP接続時のポート番号 */
	private static String httpPort;

	/** HTTPS接続時のポート番号 */
	private static String httpsPort;

	/**	ビジネスロジック格納トップパッケージ	*/
	private static String blogicPackage;
	
	/** システムフォルダ */
	private static String SystemFolderPath;

	/** データソース名 */
	private static String dataSourceName;

	/** JDBCドライバJarパス */
	private static String jdbcDriver;

	/** DB使用フラグ */
	private static boolean useDatabaseFlg = false;

	/** 暗号化キー */
	private static String encodeKey;

	/** プリロードJavaScriptリスト(PC) */
	private static String[] preLoadScriptListPC;

	/** プリロードJavaScriptリスト(スマホ、タブレット) */
	private static String[] preLoadScriptListSMP;

	/** プリロード CSSリスト */
	private static String[] preLoadCssList;

	/** クローラロボットリスト */
	private static String[] robotList;

	/** スマートフォン種別リスト */
	private static String[] smtPhoneList;

	/** タブレットパッド種別リスト */
	private static String[] tabletPadList;

	/** デフォルトターゲット */
	private static String defaultTarget;
	
	/**	リソースファイル文字コード	*/
	private static String resourceEncCode;

	/** システム ロケール */
	private static Locale systemLocale = MessageLabelUtil.JAPANESE;

	/**
	 * プロパティーファイルを読み込んで変数にセット。
	 *
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト<br>
	 * リソースの取得起点を得る為にクラスローダで生成されたオブジェクトを引数で受ける。<br>
	 * (本システムではTomcatのクラスローダ上でインスタンス化されたサーブレットクラスのインスタンスオブジェクト)
	 * @throws Exception 例外
	 */
	public static void loadSystemConfig(Object classLoaderMakedObject) throws Exception {
		try {
			// プロパティ読み込み
			Properties prop = SystemUtil.loadProperties(classLoaderMakedObject, CONFIG_FILE);

			// ログ設定
			setLogConfig(prop);

			SystemInitializer.outInitLog("システム", "[起動]"
					+ CommonDefine.LINE_SEP + "＝＝＝＝＝＝＝＝ システム起動開始 ＝＝＝＝＝＝＝＝＝");

			SystemInitializer.outInitLog("プロパティー読み込み", "[開始]");

			httpPath = StringUtil.trim(prop.getProperty("HTTP_PATH"));
			httpsPath = StringUtil.trim(prop.getProperty("HTTPS_PATH"));
			httpPort = StringUtil.trim(prop.getProperty("HTTP_PORT"));
			httpsPort = StringUtil.trim(prop.getProperty("HTTPS_PORT"));
			dataSourceName = StringUtil.trim(prop.getProperty("DATA_SOURCE_NAME"));
			jdbcDriver = StringUtil.trim(prop.getProperty("JDBC_DRIVER"));
			defaultTarget = StringUtil.trim(prop.getProperty("DEFAULT_TARGET"));
			encodeKey = StringUtil.trim(prop.getProperty("ENCODE_KEY"));
			resourceEncCode = StringUtil.trim(prop.getProperty("RESOURCE_ENC_CODE"));
			EncodeUtil.init(encodeKey, true, CommonDefine.ENCODE_UTF_8);

			blogicPackage = StringUtil.trim(prop.getProperty("BLOGIC_PACKAGE"));

			String dbFlg = StringUtil.trim(prop.getProperty("USE_DATABASE_FLG"));
			if (dbFlg != null && dbFlg.toUpperCase().equals("TRUE")) {
				useDatabaseFlg = true;
				String databaseType = StringUtil.trim(prop.getProperty("DATABASE_TYPE"));
				if (StringUtil.isNull(databaseType)) {
					if (databaseType.toLowerCase().equals(DBAccess.DataBaseType.MYSQL)) {
						DBAccess.setDataBaseType(DBAccess.DataBaseType.MYSQL);
						
					} else if (databaseType.toLowerCase().equals(DBAccess.DataBaseType.POSTGRESQL)) {
						DBAccess.setDataBaseType(DBAccess.DataBaseType.POSTGRESQL);
						
					} else if (databaseType.toLowerCase().equals(DBAccess.DataBaseType.ORACLE)) {
						DBAccess.setDataBaseType(DBAccess.DataBaseType.ORACLE);
					}
				}
				
			} else {
				useDatabaseFlg = false;
			}

			String localeStr = StringUtil.trim(prop.getProperty("DEFAULT_LOCALE"));
			if (StringUtil.isNull(localeStr)) {
				Locale locale = new Locale(localeStr.trim().toLowerCase());
				systemLocale = locale;
			}

			// プリロードJavaScript設定(PC)
			String javascriptsPC = prop.getProperty("PRELOAD_SCRIPTS_PC");
			if (StringUtil.isNull(javascriptsPC)) {
				preLoadScriptListPC = StringUtil.trimStringArray(javascriptsPC.split(CommonDefine.COMMA));
			}

			// プリロードJavaScript設定(スマホ、タブレット)
			String javascriptsSMP = prop.getProperty("PRELOAD_SCRIPTS_SMP");
			if (StringUtil.isNull(javascriptsSMP)) {
				preLoadScriptListSMP = StringUtil.trimStringArray(javascriptsSMP.split(CommonDefine.COMMA));
			}


			// プリロードCSS設定
			String css = prop.getProperty("PRELOAD_CSS");
			if (StringUtil.isNull(css)) {
				preLoadCssList = StringUtil.trimStringArray(css.split(CommonDefine.COMMA));
			}

			// クローラロボット種別設定
			String robot = prop.getProperty("ROBOT");
			if (StringUtil.isNull(robot)) {
				robotList = StringUtil.trimStringArray(robot.split(CommonDefine.COMMA));
			}

			// スマートフォン種別設定
			String smtPhone = prop.getProperty("SMT_PHONE");
			if (StringUtil.isNull(smtPhone)) {
				smtPhoneList = StringUtil.trimStringArray(smtPhone.split(CommonDefine.COMMA));
			}

			// タブレットパッド種別設定
			String tabletPad = prop.getProperty("TABLET_PAD");
			if (StringUtil.isNull(tabletPad)) {
				tabletPadList = StringUtil.trimStringArray(tabletPad.split(CommonDefine.COMMA));
			}

			SystemInitializer.outInitLog("プロパティー読み込み", "[正常完了]");
		} catch (MissingResourceException e) {
			throw e;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ログプロパティー情報をセットする。
	 *
	 * @param prop プロパティ情報
	 * @throws Exception 例外
	 */
	protected static void setLogConfig(Properties prop) throws Exception {
		SystemFolderPath = StringUtil.trim(prop.getProperty("SYSTEM_FOLDER"));

		// ログ出力フラグ設定
		String logFileDir = SystemFolderPath + CommonDefine.SEP + "log";
		String logFileName = StringUtil.trim(prop.getProperty("LOG_FILE_NAME"));
		String logEncode = StringUtil.trim(prop.getProperty("LOG_ENCODE"));
		long logFileMaxSize = Long.parseLong(StringUtil.trim(prop.getProperty("LOG_MAX_SIZE")));
		int logLevel = Integer.parseInt(StringUtil.trim(prop.getProperty("LOG_LEVEL")));

		LogWriter.init(logFileDir, logFileMaxSize, logFileName, logLevel, logEncode);

		SystemInitializer.outInitLog("ログ初期化", "[正常完了]");

	}

	/**
	 * httpPathの値を返す。
	 *
	 * @return httpPath
	 */
	public static String getHttpPath() {
		return httpPath;
	}

	/**
	 * httpsPathの値を返す。
	 *
	 * @return httpsPath
	 */
	public static String getHttpsPath() {
		return httpsPath;
	}

	/**
	 * httpPortの値を返す。
	 *
	 * @return httpPort
	 */
	public static String getHttpPort() {
		return httpPort;
	}

	/**
	 * httpsPortの値を返す。
	 *
	 * @return httpsPort
	 */
	public static String getHttpsPort() {
		return httpsPort;
	}

	/**
	 * systemFolderPathの値を返す。
	 *
	 * @return systemFolderPath
	 */
	public static String getSystemFolderPath() {
		return SystemFolderPath;
	}

	/**
	 * dataSourceNameの値を返す。
	 *
	 * @return dataSourceName
	 */
	public static String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * jdbcDriverの値を返す。
	 *
	 * @return jdbcDriver
	 */
	public static String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * useDatabaseFlgの値を返す。
	 *
	 * @return useDatabaseFlg
	 */
	public static boolean isUseDatabaseFlg() {
		return useDatabaseFlg;
	}

	/**
	 * preLoadCssListの値を返す。
	 *
	 * @return preLoadCssList
	 */
	public static String[] getPreLoadCssList() {
		return preLoadCssList;
	}

	/**
	 * robotListの値を返す。
	 *
	 * @return robotList
	 */
	public static String[] getRobotList() {
		return robotList;
	}

	/**
	 * smtPhoneListの値を返す。
	 *
	 * @return smtPhoneList
	 */
	public static String[] getSmtPhoneList() {
		return smtPhoneList;
	}

	/**
	 * tabletPadListの値を返す。
	 *
	 * @return tabletPadList
	 */
	public static String[] getTabletPadList() {
		return tabletPadList;
	}

	/**
	 * defaultTargetの値を返す。
	 *
	 * @return defaultTarget
	 */
	public static String getDefaultTarget() {
		return defaultTarget;
	}

	/**
	 * systemLocaleの値を返す。
	 *
	 * @return systemLocale
	 */
	public static Locale getSystemLocale() {
		return systemLocale;
	}

	/**
	 * encodeKeyの値を返す。
	 *
	 * @return encodeKey
	 */
	public static String getEncodeKey() {
		return encodeKey;
	}

	/**
	 * preLoadScriptListPCの値を返す。
	 * @return preLoadScriptListPC
	 */
	public static String[] getPreLoadScriptListPC() {
		return preLoadScriptListPC;
	}

	/**
	 * preLoadScriptListSMPの値を返す。
	 * @return preLoadScriptListSMP
	 */
	public static String[] getPreLoadScriptListSMP() {
		return preLoadScriptListSMP;
	}

	/**
	 * @return blogicPackage
	 */
	public static String getBlogicPackage() {
		return blogicPackage;
	}

	/**
	 * @return resourceEncCode
	 */
	public static String getResourceEncCode() {
		return resourceEncCode;
	}

}
