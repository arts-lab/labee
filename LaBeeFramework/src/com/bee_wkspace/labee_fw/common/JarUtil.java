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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.bee_wkspace.labee_fw.core.SystemConfigMng;

/**
 * Jarファイル関連ユーティリティークラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: JarUtil.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class JarUtil {

	/** ユニコード */
	public static final String UTF_8 = "UTF-8";

	/** ShiftJis */
	public static final String SHIFT_JIS = "Shift_JIS";

	/** Jarファイル拡張子 */
	public static final String JAR_FILE_EX = ".jar";

	/** フレームワークファイル名 */
	protected static String LibJarName = "LaBeeFramework";

	/**
	 * フレームワークライブラリJarファイルオブジェクトを返す。
	 *
	 * @param docBasePath 基点パス
	 * @return フレームワークライブラリJarファイルオブジェクト
	 * @throws IOException 例外
	 */
	public static JarFile getLaBeeLibJarFile(String docBasePath) throws IOException {
		String libPath = docBasePath + CommonDefine.SEP + "WEB-INF" + CommonDefine.SEP +
				"lib" + CommonDefine.SEP;
		JarFile libJarFile = null;

		File libDir = new File(libPath);
		File files[] = libDir.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isFile() == false) {
					continue;
				}
				if (file.exists() == false) {
					continue;
				}

				String fileName = file.getName().toLowerCase();
				if (fileName.startsWith(LibJarName.toLowerCase())
						&& fileName.lastIndexOf(".jar") != -1) {
					libJarFile = new JarFile(file);
					break;
				}
			}
		}
		return libJarFile;
	}

	/**
	 * 指定ファイル内のクラスを返す。
	 *
	 * @param classLoaderMakedObject 呼び元のクラスローダーで生成されたオブジェクト
	 * @param file ファイルオブジェクト
	 * @param targetClass ターゲットクラス
	 * @return クラス
	 * @throws Exception 例外
	 */
	public static Class<?> getClass(Object classLoaderMakedObject, File file, String targetClass) throws Exception {
		URL[] urls = { file.toURI().toURL() };
		URLClassLoader classLoader = (URLClassLoader) classLoaderMakedObject.getClass().getClassLoader();
		ClassLoader loader = URLClassLoader.newInstance(urls, classLoader);
		Class<?> clazz = loader.loadClass(targetClass);
		return clazz;
	}

	/**
	 * Jarファイル内のプロパティーファイルを読み込んで返す。
	 *
	 * @param jarFile 対象Jarファイル
	 * @param propertyFilePath Jarファイル内のプロパティーファイルパス
	 * @return プロパティーファイル
	 * @throws Exception 例外
	 */
	public static Properties getPropertiesInJar(JarFile jarFile, String propertyFilePath) throws Exception {
		InputStreamReader inputStream = null;
		Properties properties = null;
		try {
			// Jarファイル内のコンフィグファイルを読み込む
			ZipEntry zipEntry = jarFile.getEntry(propertyFilePath);
			inputStream = new InputStreamReader(jarFile.getInputStream(zipEntry), UTF_8);

			properties = new Properties();
			properties.load(inputStream);
		} catch (Exception e) {
			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return properties;
	}

	/**
	 * Jar内の指定テキストファイル内容を別のパスにコピーする。
	 *
	 * @param jarFile 対象Jarファイル
	 * @param targetFilePath Jar内の対象ファイルパス
	 * @param copyFilePath 出力対象ファイルパス
	 * @param chrCode 文字コード
	 * @return true = 成功
	 * @throws Exception 例外
	 */
	public static boolean copyFileInJar(JarFile jarFile, String targetFilePath, String copyFilePath, String chrCode)
			throws Exception {
		boolean flg = false;
		// Jar内の対象ファイル内容を読み込む
		String fileData = loadTextFileInJar(jarFile, targetFilePath, chrCode);

		if (StringUtil.isNull(fileData)) {
			// 読み込んだ内容を別のファイルに書き込む
			exportTextFile(copyFilePath, fileData, chrCode);
			flg = true;
		}
		return flg;
	}

	/**
	 * Jar内の指定バイナリファイル内容を別のパスにコピーする。
	 *
	 * @param jarFile 対象Jarファイル
	 * @param innerfilePath Jar内の対象ファイルパス
	 * @param destPath 出力対象ファイルパス
	 * @return true = 成功
	 * @throws Exception 例外
	 */
	@SuppressWarnings("resource")
	public static boolean copyBinaryFileInJar(JarFile jarFile, String innerfilePath, String destPath) throws Exception {
		boolean flg = false;
		File srcFile = getJarInFile(jarFile, innerfilePath, "/");
		if (srcFile != null) {
			FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
			FileChannel destChannel = new FileOutputStream(destPath).getChannel();
			try {
				srcChannel.transferTo(0, srcChannel.size(), destChannel);
				flg = true;
			} finally {
				srcChannel.close();
				destChannel.close();
			}
		}
		return flg;
	}

	/**
	 * JARファイル内のテキストファイルを読み込んで文字列で返す。
	 *
	 * @param jarFile Jarファイル
	 * @param fileNamePath ファイルパス
	 * @param chrCode 文字コード
	 * @return 読み込んだ文字列
	 * @throws Exception 例外
	 */
	public static String loadTextFileInJar(JarFile jarFile, String fileNamePath, String chrCode) throws Exception {
		BufferedReader bufReader = null;
		StringBuilder buf = new StringBuilder();
		try {

			// Jar内のファイルを読み込む
			ZipEntry zipEntry = jarFile.getEntry(fileNamePath);
			if (zipEntry != null) {
				bufReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(zipEntry), chrCode));
				String line = null;
				while ((line = bufReader.readLine()) != null) {
					buf.append(line + CommonDefine.LINE_SEP);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (bufReader != null) {
				bufReader.close();
			}
		}
		return buf.toString();
	}

	/**
	 * JARファイル内のパスにあるファイルをFileオブジェクトに変換して返す。
	 *
	 * @param jarFile JARファイル
	 * @param innerfilePath JARファイル内のファイルパス
	 * @param delim ファイルパス内の区切り文字
	 * @return ファイルオブジェクト
	 * @throws Exception 例外
	 */
	public static File getJarInFile(JarFile jarFile, String innerfilePath, String delim) throws Exception {
		File file = null;
		ZipEntry zipEntry = jarFile.getEntry(innerfilePath);
		if (zipEntry != null) {
			String copyFileName = null;
			String[] path = innerfilePath.split(delim);
			if (path.length > 1) {
				copyFileName = path[path.length - 1];
			} else {
				copyFileName = innerfilePath;
			}

			String systemLibFolder = SystemConfigMng.getSystemFolderPath() + CommonDefine.SEP + "lib";
			File chkFile = new File(systemLibFolder);
			if (chkFile.exists() == false) {
				chkFile.mkdirs();
			}

			file = new File(systemLibFolder, copyFileName);
			FileOutputStream fos = new FileOutputStream(file);
			try {
				fos.getChannel().transferFrom(Channels.newChannel(jarFile.getInputStream(zipEntry)), 0, Long.MAX_VALUE);
			} catch (Exception e) {
				throw e;
			} finally {
				fos.close();
			}
		}
		return file;
	}

	/**
	 * Jarファイル内の指定パス配下のファイル名リストを返す。
	 *
	 * @param jarFile JARファイル
	 * @param targetPath ターゲットパス
	 * @return ファイル名リスト
	 * @throws Exception 例外
	 */
	public static List<String> getJarInFileList(JarFile jarFile, String targetPath) throws Exception {
		List<String> list = new ArrayList<String>();

		for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
			JarEntry entry = e.nextElement();

			String name = entry.getName();
			if (name.startsWith(targetPath)) {
				list.add(name);
			}
		}
		return list;
	}

	/**
	 * Jarライブラリを動的にシステムクラスパスに追加する。
	 *
	 * @param jarPath Jarライブラリパス
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	public static void addJarLibralySystemClassPath(File jarPath) throws Exception {

		URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		@SuppressWarnings("rawtypes")
		Class classClassLoader = URLClassLoader.class;
		Method methodAddUrl = classClassLoader.getDeclaredMethod("addURL", URL.class);

		methodAddUrl.setAccessible(true);
		methodAddUrl.invoke(classLoader, jarPath.toURI().toURL());

	}

	/**
	 * Jarライブラリを動的にWEBサーバークラスパスに追加する。
	 *
	 * @param classLoaderMakedObject 呼び元のクラスローダーで生成されたオブジェクト
	 * @param jarPath Jarライブラリパス
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	public static void addJarLibralyClassPath(Object classLoaderMakedObject, File jarPath) throws Exception {
		URLClassLoader classLoader = (URLClassLoader) classLoaderMakedObject.getClass().getClassLoader();

		@SuppressWarnings("rawtypes")
		Class classClassLoader = URLClassLoader.class;
		Method methodAddUrl = classClassLoader.getDeclaredMethod("addURL", URL.class);

		methodAddUrl.setAccessible(true);
		methodAddUrl.invoke(classLoader, jarPath.toURI().toURL());
	}

	/**
	 * 文字列データをファイルに出力する。
	 *
	 * @param exportFilePath 出力先パス
	 * @param fileText 出力内容
	 * @param chrCode 文字コード
	 * @throws Exception 例外
	 */
	public static void exportTextFile(String exportFilePath, String fileText, String chrCode) throws Exception {
		PrintWriter pw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			// 出力設定
			fos = new FileOutputStream(exportFilePath);
			osw = new OutputStreamWriter(fos, chrCode);
			BufferedWriter bw = new BufferedWriter(osw);
			pw = new PrintWriter(bw);

			// ファイル内容を書き込み
			pw.println(fileText);

			pw.flush();

		} catch (Exception e) {
			throw e;
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (osw != null) {
				osw.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
}
