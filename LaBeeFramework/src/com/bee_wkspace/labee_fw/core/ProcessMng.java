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
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.annotation.AppInitProcess;
import com.bee_wkspace.labee_fw.core.base.ProcessInterface;

/**
 * プロセスを管理するクラス。
 *
 * @author ARTS Laboratory
 * 
 * $Id: ProcessMng.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class ProcessMng {

	/** プロセス格納マップ */
	private static HashMap<String, ProcessInterface> processMap = new HashMap<String, ProcessInterface>();

	/**
	 * プロセスマップを返す。
	 * 
	 * @return プロセスマップ
	 */
	public static HashMap<String, ProcessInterface> getProcessMap() {
		return processMap;
	}

	/**
	 * プロセス状態を返す。
	 * 
	 * @param className プロセスクラス名
	 * @return プロセス状態
	 */
	public static String getProcessState(String className) {
		ProcessInterface process = processMap.get(className);
		if (process != null) {
			return process.getState();
		} else {
			return "notfound";
		}
	}

	/**
	 * 初期実行プロセスを起動する。
	 * 
	 * @throws Exception 例外
	 */
	public static void startAppInitProcess() throws Exception {
		if (SystemInitializer.startUpFlg == false) {
			SystemInitializer.outInitLog("アプリ初期起動プロセス", "[開始]");
			StringBuilder path = new StringBuilder();
			path.append(SystemInitializer.getDocBasePath());
			path.append(CommonDefine.SEP);
			path.append("WEB-INF");
			path.append(CommonDefine.SEP);
			path.append("classes");
			exeAppInitProcess(path.toString(), new File(path.toString()));
		}
	}

	/**
	 * 初期実行処理アノテーションを付加された実装クラスを全パスから再起検索して実行する。
	 * 
	 * @param basePath 起点ファイルパス
	 * @param tgtFile 検索された対象ファイルオブジェクト
	 * @throws Exception 例外
	 */
	private static void exeAppInitProcess(String basePath, File tgtFile) throws Exception {
		if (tgtFile.isDirectory()) {
			File[] fileList = tgtFile.listFiles();
			for (File file : fileList) {
				exeAppInitProcess(basePath, file);
			}

		} else if (tgtFile.getName().lastIndexOf(".class") != -1) {
			String path = tgtFile.getAbsolutePath().substring(
					basePath.length(),
					tgtFile.getAbsolutePath().length() - ".class".length());

			String fullPackage = StringUtil.replaceText(path, CommonDefine.SEP, ".");

			Class<?> clazz = Class.forName(fullPackage);
			Annotation[] annotations = clazz.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				// AppInitProcessアノテーションを付加した初期実行クラスを起動
				if (annotation.annotationType().equals(AppInitProcess.class)) {
					Constructor<?> ct = clazz.getConstructor();
					ProcessInterface initializer = (ProcessInterface) ct.newInstance();

					// 初期実行起動
					initializer.startProcess();
					processMap.put(clazz.getSimpleName(), initializer);
					SystemInitializer.outInitLog(initializer.getProcessName() + ":" + clazz.getName(), "[OK] ");
					break;
				}
			}
		}
	}
}
