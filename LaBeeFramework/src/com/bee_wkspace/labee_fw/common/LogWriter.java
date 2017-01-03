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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * ログ出力クラス。<br>
 * 指定ディレクトリパスにログファイルを出力する。<br>
 * ログ種類にはエラー、インフォ、デバッグを指定する。<br>
 * 出力したログファイルが指定最大サイズに達した場合は、ログファイル名にその時点の日付文字列を付加した<br>
 * 名前に変更し(ログバックアップファイル)、新規ログファイルにローテーションする。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: LogWriter.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class LogWriter {

	/** ログ出力タイプ デバッグ */
	public static final int DEBUG = 2;

	/** ログ出力タイプ インフォ */
	public static final int INFO = 4;

	/** ログ出力タイプ エラー */
	public static final int ERROR = 6;

	/** システムログ出力パス */
	private static String logFileDir;

	/** ログファイルの最大サイズ(バイト) */
	private static long logFileMaxSize;

	/** ログファイル名 */
	private static String logFileName;

	/** ログ出力文字コード */
	private static String logEncode;

	/** ログ出力レベル */
	private static int logLevel = 0;

	public static SimpleDateFormat sdf_default = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * ログ出力設定初期化。
	 *
	 * @param _logFileDir	ログ出力ディレクトリ
	 * @param _logFileMaxSize	ログファイル最大サイズ
	 * @param _logFileName	ログファイル名
	 * @param _logLevel	ログ出力レベル
	 * @param _logEncode	ログファイル文字コード
	 */
	public static void init(String _logFileDir, long _logFileMaxSize, String _logFileName, int _logLevel,
			String _logEncode) {
		logFileDir = _logFileDir;
		logFileName = _logFileName;
		logEncode = _logEncode;
		logFileMaxSize = _logFileMaxSize;
		logLevel = _logLevel;
	}

	/**
	 * エラーログを出力する。（Exception例外のみ)
	 *
	 * @param exception 例外オブジェクト
	 */
	public static void error(Exception exception) {
		println(ERROR, exception, null, null, null);
	}

	/**
	 * エラーログを出力する。（Throwable例外のみ)
	 *
	 * @param exception 例外オブジェクト
	 */
	public static void error(Throwable exception) {
		println(ERROR, exception, null, null, null);
	}

	/**
	 * エラーログを出力する。（メッセージのみ)
	 *
	 * @param msg メッセージ
	 */
	public static void error(String msg) {
		println(ERROR, null, msg, null, null);
	}

	/**
	 * エラーログを出力する。（Exception例外、メッセージ)
	 *
	 * @param exception 例外オブジェクト
	 * @param msg メッセージ
	 */
	public static void error(Exception exception, String msg) {
		println(ERROR, exception, msg, null, null);
	}

	/**
	 * エラーログを出力する。（Throwable例外、メッセージ)
	 *
	 * @param exception 例外オブジェクト
	 * @param msg メッセージ
	 */
	public static void error(Throwable exception, String msg) {
		println(ERROR, exception, msg, null, null);
	}


	/**
	 * エラーログを出力する。（呼び元クラスオブジェクト、メソッド名, メッセージ)
	 *
	 * @param callClass 呼び元クラスオブジェクト
	 * @param method	呼び元メソッド名
	 * @param msg	メッセージ
	 */
	public static void error(Object callClass, String method, String msg) {
		println(ERROR, null, msg, callClass.getClass().getSimpleName(), method);
	}

	/**
	 * エラーログを出力する。（呼び元クラス名、メソッド名, メッセージ)
	 *
	 * @param callClass 呼び元クラス名
	 * @param method	呼び元メソッド名
	 * @param msg	メッセージ
	 */
	public static void error(String callClass, String method, String msg) {
		println(ERROR, null, msg, callClass, method);
	}

	/**
	 * インフォログを出力する。（メッセージ)
	 *
	 * @param msg	メッセージ
	 */
	public static void info(String msg) {
		println(INFO, null, msg, null, null);
	}


	/**
	 * デバッグログを出力する。（メッセージ)
	 *
	 * @param msg	メッセージ
	 */
	public static void debug(String msg) {
		println(DEBUG, null, msg, null, null);
	}

	/**
	 * デバッグログを出力する。（呼び元クラス名、メソッド名, メッセージ)
	 *
	 * @param callClass 呼び元クラス名
	 * @param method	呼び元メソッド名
	 * @param msg	メッセージ
	 */
	public static void debug(String callClass, String method, String msg) {
		println(DEBUG, null, msg, callClass, method);
	}

	/**
	 * デバッグログを出力する。（呼び元クラスオブジェクト、メッセージ)
	 *
	 * @param clazz	呼び元クラスオブジェクト
	 * @param msg メッセージ
	 */
	public static void debug(Object clazz, String msg) {
		println(DEBUG, null, msg, clazz.getClass().getSimpleName(), null);
	}

	/**
	 * デバッグログを出力する。（呼び元クラス名、メソッド名, メッセージ)
	 *
	 * @param clazz	呼び元クラスオブジェクト
	 * @param method メソッド名
	 * @param msg	メッセージ
	 */
	public static void debug(Object clazz, String method, String msg) {
		println(DEBUG, null, msg, clazz.getClass().getSimpleName(), method);
	}

	/**
	 * ログを出力する。（フル引数)
	 *
	 * @param logType 出力タイプ<br>
	 * 	本クラス定数のDEBUG, INFO, ERRORを指定する。
	 * @param exception	例外オブジェクト
	 * @param msg	出力メッセージ
	 * @param callClass	呼び元クラス名
	 * @param method	呼び元メソッド名
	 */
	public static void println(int logType, Throwable exception, String msg, String callClass, String method) {
		PrintWriter pw = null;
		StringBuilder buf = null;
		try {
			if (logType >= logLevel) {

				// ログファイル存在チェック
				String logFileFullPath = checkLogFileExists();

				// 出力設定
				FileOutputStream fos = new FileOutputStream(logFileFullPath, true);
				OutputStreamWriter osw = new OutputStreamWriter(fos, logEncode);
				BufferedWriter bw = new BufferedWriter(osw);

				pw = new PrintWriter(bw);
				buf = new StringBuilder();

				// 出力内容生成
				buf.append(CommonDefine.LINE_SEP + "-----------------------------------------------------" + CommonDefine.LINE_SEP);
				if (logType == DEBUG) {
					buf.append("[DEBUG] ");

				} else if (logType == INFO) {
					buf.append("[INFO] ");

				} else if (logType == ERROR) {
					buf.append("[ERROR] ");
				}
				buf.append("DATE:" + getRealTime() + CommonDefine.LINE_SEP);

				if (callClass != null) {
					buf.append("   ExecClass :" + callClass);

					if (method != null) {
						buf.append("." + method + "()");
					}
					buf.append(CommonDefine.LINE_SEP);
				}

				if (msg != null) {
					buf.append("   ");
					buf.append(msg + CommonDefine.LINE_SEP);
				}

				if (exception != null) {
					int idx = 2;
					StackTraceElement[] ste = new Exception().getStackTrace();
					String className = ste[idx].getClassName();
					String lineNum = Integer.toString(ste[idx].getLineNumber());
					String methodName = ste[idx].getMethodName();
					String exceptionName = exception.getClass().getSimpleName();
					String exceptionMsg = exception.getMessage();

					buf.append("   Exception:" + exceptionName + CommonDefine.LINE_SEP);
					buf.append("   ErrorMessage:" + exceptionMsg + CommonDefine.LINE_SEP);
					buf.append("   Class:" + className + CommonDefine.LINE_SEP);
					buf.append("   LineNum:" + lineNum + CommonDefine.LINE_SEP);
					buf.append("   Method:" + methodName + CommonDefine.LINE_SEP);
				}

				String logString = new String(buf.toString());

				// ファイル書き込み
				pw.println(logString);

				if (exception != null) {
					// スタックトレース
					exception.printStackTrace(pw);
				}
				// 標準出力
				System.out.println(logString);

				pw.flush();
				pw.close();

				// ファイルサイズをチェックしファイルのローテートを行う
				checkLogFileSize(logFileFullPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			pw = null;
			buf = null;
		}
	}

	/**
	 * エラーログファイル存在確認。ファイルが無い場合はファイルを新規作成する。
	 * @return ログファイルフルパス
	 * @throws Exception 例外
	 */
	protected static String checkLogFileExists() throws Exception {
		String fileFullPath = logFileDir + CommonDefine.SEP + logFileName;
		try {
			// ログディレクトリが無い場合は作る
			File logDir = new File(logFileDir);
			if (logDir.exists() == false) {
				try {
					// パーミッション権限を付加したファイルを作成
					FileAttribute<Set<PosixFilePermission>> attrs
						= PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(CommonDefine.PERMISSION));
					Path dirPath = Paths.get(logFileDir);
					Files.createDirectories(dirPath, attrs);

				// POSIX権限に対応していないWindowsで実行する場合
				} catch(UnsupportedOperationException e) {
					logDir.mkdirs();
				}
			}

			File logFile = new File(fileFullPath);

			// ログファイルが無い場合は作る
			if (logFile.exists() == false) {
				try {
					// パーミッション権限を付加したファイルを作成
					FileAttribute<Set<PosixFilePermission>> attrs
						= PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(CommonDefine.PERMISSION));
					Path path = Paths.get(fileFullPath);
					Files.createFile(path, attrs);

				// POSIX権限に対応していないWindowsで実行する場合
				} catch(UnsupportedOperationException e) {
					logFile.createNewFile();
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return fileFullPath;
	}

	/**
	 * ログファイルのサイズをチェックしてサイズ超の場合は新規ログファイルにローテーションする。
	 *
	 * @param fileName ログファイル名
	 * @throws Exception	例外
	 */
	protected static void checkLogFileSize(String fileName) throws Exception {
		try {
			File file = new File(fileName);
			long size = file.length();

			// ファイル最大バイト数を超えているかチェック
			if (size > logFileMaxSize) {
				// バックアップファイル名
				String bakFileName = logFileDir + CommonDefine.SEP + logFileName + "_" + getYMD() + ".bak";

				// カレントログファイル名をバックアップ名にリネーム
				File bakFile = new File(bakFileName);
				file.renameTo(bakFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 現在時間文字列を得る。
	 *
	 * @return 日付文字列
	 */
	private static String getRealTime() {
		Date currentTime = new Date();
		String dateString = sdf_default.format(currentTime);
		return dateString;
	}

	/**
	 * 現在日付を基にした連結文字列を返す。
	 *
	 * @return 文字列
	 */
	private static String getYMD() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		String ymd = year
			+ String.format("%02d", month)
			+ String.format("%02d", date)
			+ String.format("%02d", hour);
		return ymd;
	}
}