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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.context.DelegateContext;
import com.bee_wkspace.labee_fw.core.context.RedirectContext;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;

/**
 * システム汎用ユーティリティクラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: SystemUtil.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class SystemUtil {

	/**
	 * 相対クラスパスにあるプロパティーファイルを読み込む。 (対象となるクラスローダーで生成されたオブジェクトから相対クラスパス取得）
	 *
	 * @param fileName プロパティー名
	 * @param classLoaderMakedObject 対象のクラスローダーで生成されたオブジェクト(型はなんでも良い)
	 * @return プロパティー
	 * @throws Exception 例外
	 */
	public static Properties loadProperties(Object classLoaderMakedObject, String fileName) throws Exception {
		Properties prop = new Properties();
		if (fileName.startsWith("/") == false) {
			fileName = "/" + fileName;
		}
		InputStream stream = classLoaderMakedObject.getClass().getResourceAsStream(fileName);
		prop.load(stream);
		stream.close();
		return prop;
	}

	/**
	 * 相対クラスパスにあるプロパティーファイルを書き込む。
	 *
	 * @param classLoaderMakedObject 対象のクラスローダーで生成されたオブジェクト(型はなんでも良い)
	 * @param prop プロパティー
	 * @param fileName プロパティー名
	 * @throws Exception 例外
	 */
	public static void saveProperties(Object classLoaderMakedObject, Properties prop, String fileName) throws Exception {

		URLClassLoader classLoader = (URLClassLoader) classLoaderMakedObject.getClass().getClassLoader();
		URL url = classLoader.getResource(fileName);
		FileOutputStream fos = new FileOutputStream(new File(url.toURI()));
		prop.store(fos, "updated");
		fos.close();
		classLoader.close();
	}

	/**
	 * クラスパス上に存在しているファイルリソースをファイルオブジェクトにして返す。
	 * 
	 * @param classLoaderMakedObject 対象のクラスローダーで生成されたオブジェクト(型はなんでも良い)
	 * @param filePath 相対パス
	 * @return リソースファイルオブジェクト
	 * @throws Exception 例外
	 */
	public static File getFileResource(Object classLoaderMakedObject, String filePath) throws Exception {
		URLClassLoader classLoader = (URLClassLoader) classLoaderMakedObject.getClass().getClassLoader();
		URL url = classLoader.getResource(filePath);
		classLoader.close();
		if (url != null) {
			return new File(url.toURI());
		} else {
			return null;
		}
	}

	/**
	 * テキストファイル読み込み。
	 *
	 * @param file ファイル
	 * @param encType 文字コード
	 * @return 読み込んだテキストファイル内容
	 * @throws Exception 例外
	 */
	public static String loadFile(File file, String encType) throws Exception {
		StringBuilder buf = new StringBuilder();
		try {
			// ファイルが存在してるかをチェック
			if (file.exists()) {
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), encType));
				String line = null;
				while ((line = in.readLine()) != null) {
					StringUtil.appendBufLine(buf, line + CommonDefine.LINE_SEP);
				}
				in.close();
			}
		} catch (Exception e) {
			throw e;
		}
		return buf.toString();
	}

	/**
	 * リクエストにパラメータをセットしてリダイレクト先のURLを返す。
	 *
	 * @param request リクエスト情報
	 * @param responseContext レスポンスコンテキスト
	 * @return URL文字列
	 */
	public static String getRedirectURL(HttpServletRequest request, ResponseContext responseContext) {
		StringBuilder url = new StringBuilder();
		Environment env = responseContext.getEnv();

		if (responseContext.isRedirectFlg()) {
			RedirectContext redirectContext = responseContext.getRedirectContext();

			// フレームワーク形式のページにリダイレクトする場合
			if (!StringUtil.isNull(redirectContext.getRedirectURL())) {
				// SSLフラグをチェック
				if (redirectContext.isSslFlg()) {
					url.append(SystemConfigMng.getHttpsPath());
					url.append(env.getServerName());
					url.append(":" + SystemConfigMng.getHttpsPort());

				} else {
					url.append(SystemConfigMng.getHttpPath());
					url.append(env.getServerName());
					url.append(":" + SystemConfigMng.getHttpPort());
				}

				url.append(env.getWebAppPath());
				url.append(env.getContextPath());

				// 基本パラメータをセット
				url.append("?" + CommonDefine.TARGET + "=" + redirectContext.getTarget() + "&");
				url.append(CommonDefine.EXECUTE + "=" + redirectContext.getExecute());

				// オプションパラメータをセット
				String[] param = redirectContext.getParam();
				if (param != null) {
					int size = param.length;
					for (int i = 0; i < size; i++) {
						url.append("&");
						url.append("p" + (i + 1) + "=" + param[i]);
					}
				}

				// 他サイトURLへ直接リダイレクトする場合
			} else {
				url.append(redirectContext.getRedirectURL());
			}

		} else {
			url.append(SystemConfigMng.getHttpsPath());
		}
		return url.toString();
	}

	/**
	 * 画面処理委譲時のパラメータマップを生成する。
	 *
	 * @param responseContext レスポンスコンテキスト
	 * @return パラメータマップ
	 */
	public static HashMap<String, String> getDelegateParamMap(ResponseContext responseContext) {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		DelegateContext delegateContext = responseContext.getDelegateContext();

		paramMap.put(CommonDefine.TARGET, delegateContext.getTarget());
		paramMap.put(CommonDefine.EXECUTE, delegateContext.getExecute());

		paramMap.put(CommonDefine.PARAM1, delegateContext.getParam1());
		paramMap.put(CommonDefine.PARAM2, delegateContext.getParam2());
		paramMap.put(CommonDefine.PARAM3, delegateContext.getParam3());

		return paramMap;
	}

	/**
	 * 引数のビジネスロジッククラス名からターゲット文字列を生成する。
	 * 
	 * @param clazz ビジネスロジッククラス
	 * @return ターゲット文字列
	 */
	public static String getTargetName(Class<?> clazz) {
		if (clazz.getName().lastIndexOf(BlogicFactory.BLOCIC_CONCAT_NAME) != -1) {
			return clazz.getName().substring(SystemConfigMng.getBlogicPackage().length() + 1,
					clazz.getName().length() - BlogicFactory.BLOCIC_CONCAT_NAME.length());
		} else {
			return SystemConfigMng.getDefaultTarget();
		}
	}

	/**
	 * クッキーリスト内から指定した名のクッキーを返す。
	 * 
	 * @param request リクエストオブジェクト
	 * @param name クッキー名
	 * @return クッキーオブジェクト
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * エクセプションの出力したスタックトレースを文字列化して返す。
	 *
	 * @param e 例外オブジェクト
	 * @return スタックトレース文字列
	 */
	public static String getStackTraceString(Throwable e) {
		String retVal = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			retVal = sw.toString();
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			if (sw != null) {
				try {
					sw.flush();
					sw.close();
				} catch (IOException ie) {
					//
				}
			}
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}
		return retVal;
	}

}
