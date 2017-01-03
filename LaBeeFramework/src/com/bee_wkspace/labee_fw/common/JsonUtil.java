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

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSONユーティリティークラス。<br>
 * jacksonライブラリをラップ使用している。
 *
 * @author ARTS Laboratory
 *
 * $Id: JsonUtil.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class JsonUtil<T> {

	/**
	 * 指定オブジェクトをJSON文字列に変換して返す。<br>
	 * 引数オブジェクト型はビーン、Map、List系が対象となる。変換出来ない場合は例外が発生する。
	 * 
	 * @param targetObject 対象オブジェクト
	 * @return JSON文字列
	 * @throws Exception 例外
	 */
	public String encodeToString(T targetObject) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		if (targetObject != null) {
			json = mapper.writeValueAsString(targetObject);
		}
		return json;
	}

	/**
	 * 指定JSONを文字列を指定オブジェクトに変換する。<br>
	 * 引数のclazzには変換対象となるオブジェクトクラス型を設定する必要がある。<br>
	 * オブジェクト型はビーン、Map、List系が対象となる。変換出来ない場合は例外が発生する。
	 * 
	 * @param json JSON文字列
	 * @param clazz 戻り値のオブジェクト型
	 * @return 変換後オブジェクト
	 * @throws Exception 例外
	 */
	public T endoceToObject(Class<?> clazz, String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		T targetObject = (T) mapper.readValue(json, clazz);
		return targetObject;
	}

}
