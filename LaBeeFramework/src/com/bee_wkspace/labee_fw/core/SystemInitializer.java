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

import java.io.File;
import java.util.List;
import java.util.jar.JarFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.DateTimeUtil;
import com.bee_wkspace.labee_fw.common.JarUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.db.DBAccess;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * フレームワークシステム基底の初期化処理。<br>
 * <br>
 * フレームワークの各初期化処理を実行する。<br>
 * ・システムコンフィグプロパティー読み込み<br>
 * ・データベース接続情報設定<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: SystemInitializer.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class SystemInitializer {

	/**
	 * システム初期化起動済みフラグ<br>
	 * true = 初期化済み起動中<br>
	 * false = 未初期化<br>
	 **/
	protected static boolean startUpFlg = false;

	/**
	 * 起動時初期化エラーフラグ<br>
	 * false = 起動エラー
	 * */
	protected static boolean initErrFlg = false;

	/** 起動時エラー例外クラス */
	protected static FwException initException = null;

	/** 起動ログバッファ文字列 */
	protected static StringBuilder logBuf;

	/** 起点ファイルパス */
	protected static String docBasePath;

	/** クラスローダ生成オブジェクト */
	protected static Object classLoaderMakedObject;

	/**
	 * システム初期化実行。<br>
	 *
	 * @param _docBasePath getServletContext().getRealPathで取得した起点パス
	 * @param _classLoaderMakedObject クラスローダ生成オブジェクト<br>
	 * リソースの取得起点を得る為にクラスローダで生成されたオブジェクトを引数で受ける。<br>
	 * (本システムではTomcatのクラスローダ上でインスタンス化されたサーブレットクラスのインスタンスオブジェクト)
	 */
	public static void initializeSystem(String _docBasePath, Object _classLoaderMakedObject) {
		docBasePath = _docBasePath;
		classLoaderMakedObject = _classLoaderMakedObject;
		logBuf = new StringBuilder();
		try {
			if (startUpFlg == false) {
				// --------------------------------------------------
				// システムコンフィグプロパティー読み込み
				// --------------------------------------------------
				SystemConfigMng.loadSystemConfig(classLoaderMakedObject);

				// --------------------------------------------------
				// データベース接続設定
				// --------------------------------------------------
				if (SystemConfigMng.isUseDatabaseFlg()) {

					// JDBCドライバ クラスパス追加
					if (StringUtil.isNull(SystemConfigMng.getJdbcDriver())) {
						JarUtil.addJarLibralySystemClassPath(new File(SystemConfigMng.getJdbcDriver()));
					}

					// データーソース設定
					initDataSource(SystemConfigMng.getDataSourceName());
				}

				// --------------------------------------------------
				// Jarライブラリ読み込み
				// --------------------------------------------------
				addFwJarLibClassPath();

				// --------------------------------------------------
				// リソースファイルを読み込む
				// --------------------------------------------------
				ResourceMng.loadResource(_docBasePath, classLoaderMakedObject);

				// --------------------------------------------------
				// アプリ初期実行プロセスを実行
				// --------------------------------------------------
				clearSerializeFolder();
				ProcessMng.startAppInitProcess();

				startUpFlg = true;
				initErrFlg = true;

				outInitLog("システム", "[起動完了]"
						+ CommonDefine.LINE_SEP
						+ "＝＝＝＝＝＝＝＝ システム正常起動しました ＝＝＝＝＝＝＝＝＝");
			}

		} catch (Exception e) {
			outInitLog("システム初期化", "[エラー終了]");
			outInitLog("スタックトレース", SystemUtil.getStackTraceString(e));

			initErrFlg = false;
			initException = new FwException("システム初期化処理でエラーが発生しました。", e);
		}
	}

	/**
	 * データーソースを取得して設定する。
	 *
	 * @param dataSouceName データソース名
	 * @throws Exception 例外
	 */
	protected static void initDataSource(String dataSouceName) throws Exception {
		try {
			outInitLog("データソース設定", "[開始]");

			// データソース取得
			Context ctx = new InitialContext();
			String lookUpName = "java:comp/env/" + dataSouceName;
			DataSource dataSource = (DataSource) ctx.lookup(lookUpName);
			if (dataSource == null) {
				outInitLog("データソース値", "[エラー]");
				throw new Exception("initDataSource dataSource is Null");
			}

			// データソースをセット
			DBAccess.setDataSource(dataSource);

			outInitLog("データソース設定", "[正常完了]");

		} catch (NamingException e) {
			outInitLog("データソース設定", "[エラー] ");
			throw e;

		} catch (Exception e) {
			outInitLog("データソース設定", "[エラー] ");
			throw e;
		}
	}

	/**
	 * フレームワークJar内に格納しているJarライブラリを動的にクラスパスに追加する。
	 *
	 * @throws Exception 例外
	 */
	protected static void addFwJarLibClassPath() throws Exception {
		try {
			outInitLog("フレームワーク内JARクラスパス追加", "[開始]");

			JarFile libJarFile = JarUtil.getLaBeeLibJarFile(docBasePath);
			if (libJarFile != null) {
				// フレームワーク内のJarファイル一覧を取得
				List<String> libFileList = JarUtil.getJarInFileList(libJarFile, "META-INF/lib/");
				for (String path : libFileList) {
					File libFile = JarUtil.getJarInFile(libJarFile, path, "/");

					// Jarファイルを動的にクラスパスに追加
					JarUtil.addJarLibralySystemClassPath(libFile);
					outInitLog("Jarライブラリ名: " + path, "[OK] ");
				}
			}

		} catch (Exception e) {
			outInitLog("フレームワーク内JARクラスパス追加", "[エラー] ");
			throw e;

		}
	}

	/**
	 * ビーンシリアライズ退避フォルダ内クリア
	 *
	 * @throws Exception 例外
	 */
	private static void clearSerializeFolder() throws Exception {
		String path = SystemConfigMng.getSystemFolderPath()
				+ CommonDefine.SEP
				+ CommonDefine.SERIALIZE_FOLDER_NAME;
		File dir = new File(path);
		if (dir.exists() == false) {
			dir.mkdirs();
		} else {
			deleteTmpFile(dir);
		}
		outInitLog("ビーンシリアライズ退避フォルダクリア:" + path, "[正常完了]");
	}

	/**
	 * 指定ディレクトリ配下を削除する。
	 *
	 * @param file ファイルオブジェクト
	 */
	private static void deleteTmpFile(File file) {
		if (file.isFile()) {
			file.delete();

		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteTmpFile(f);
			}
			file.delete();
		}
	}

	/**
	 * システム起動デバッグメッセージをコンソール出力する。
	 *
	 * @param name 出力メッセージ項目名
	 * @param msg 出力メッセージ
	 */
	public static void outInitLog(String name, String msg) {
		if (msg != null && msg.length() != 0) {
			StringBuilder buf = new StringBuilder();
			buf.append("-----------------------------------------------------" + CommonDefine.LINE_SEP);
			buf.append("DATE:" + DateTimeUtil.getRealTime() + CommonDefine.LINE_SEP);
			buf.append(name + ": " + msg + CommonDefine.LINE_SEP);

			System.out.println(buf.toString());

			logBuf.append(buf.toString());
		}
	}

	/**
	 * システムが起動した事のフラグを返す。
	 *
	 * @return true = 起動済み
	 */
	public static boolean isStartUpFlg() {
		return startUpFlg;
	}

	/**
	 * 初期化エラーフラグを返す。
	 *
	 * @return false = 初期化エラー
	 */
	public static boolean isInitErrFlg() {
		return initErrFlg;
	}

	/**
	 * 初期化例外オブジェクトを返す。
	 *
	 * @return 例外オブジェクト
	 */
	public static FwException getInitException() {
		return initException;
	}

	/**
	 * ログバッファを返す。
	 *
	 * @return ログバッファ
	 */
	public static StringBuilder getLogBuf() {
		return logBuf;
	}

	/**
	 * @return docBasePath
	 */
	public static String getDocBasePath() {
		return docBasePath;
	}

	/**
	 * @return classLoaderMakedObject
	 */
	public static Object getClassLoaderMakedObject() {
		return classLoaderMakedObject;
	}

}
