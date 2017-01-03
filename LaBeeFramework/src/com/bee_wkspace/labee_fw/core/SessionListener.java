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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.bee_wkspace.labee_fw.common.CommonDefine;

/**
 * セッションリスナー管理クラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: SessionListener.java 559 2016-08-14 12:24:00Z pjmember $
 */
@WebListener
public class SessionListener implements HttpSessionListener {

	/**
	 *	セッション作成時のイベント処理。
	 * 
	 * @param sessionEvent セッションイベント
	 */
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		System.out.println("sessioin create id=" + session.getId());

	}

	/**
	 *	セッション破棄時のイベント処理。
	 * 
	 * @param sessionEvent セッションイベント
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		try {
			HttpSession session = sessionEvent.getSession();
			System.out.println("sessioin destroy id=" + session.getId());

			// セッションに格納しているビーンオブジェクトをシリアライズ化してファイルに出力退避する
			String appSessionId = (String) session.getAttribute(CommonDefine.FW_SESSION_COOKIE_NAME);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> beanMap = (HashMap<String, Object>) session.getAttribute(CommonDefine.BEAN_SESSION_MAP_NAME);
			if (beanMap != null) {
				String path = SystemConfigMng.getSystemFolderPath() 
						+ CommonDefine.SEP 
						+ CommonDefine.SERIALIZE_FOLDER_NAME
						+ CommonDefine.SEP 
						+ appSessionId;
				File serializeDir = new File(path);
				if (serializeDir.exists() == false) {
					serializeDir.mkdirs();
				}

				for (Object beanObj : beanMap.values()) {
					objectSerialize(serializeDir, beanObj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ビーンオブジェクトをシリアライズ化してファイルに出力退避する。
	 * @param serializeDir	シリアライズファイル出力先パス
	 * @param beanObj	シリアライズ対象ビーンオブジェクト
	 * @throws Exception	例外
	 */
	private static void objectSerialize(File serializeDir, Object beanObj) throws Exception {
		FileOutputStream outFile = new FileOutputStream(serializeDir + CommonDefine.SEP + beanObj.getClass().getName());
		ObjectOutputStream outObject = new ObjectOutputStream(outFile);
		outObject.writeObject(beanObj);
		outObject.close();
		outFile.close();
		System.out.println(beanObj.getClass().getName());
	}

}