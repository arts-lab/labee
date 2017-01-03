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
package com.bee_wkspace.labee_fw.core.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * レスポンスJSON情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: JsonValueContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class JsonValueContext implements Serializable {

	private static final long serialVersionUID = 515554298922378367L;
	private List<AppJsonContext> jsonValues;

	/**
	 * コンストラクタ。
	 */
	public JsonValueContext() {
		jsonValues = new ArrayList<AppJsonContext>();
	}

	/**
	 * １個のJSONオブジェクトを追加する。
	 *
	 * @param objId オブジェクトID名
	 * @param value オブジェクト値
	 */
	public void addJsonValue(String objId, String value) {
		AppJsonContext context = new AppJsonContext();
		context.setObjId(objId);
		context.setValue(value);
		jsonValues.add(context);
	}

	/**
	 * １個のJSONオブジェクトを追加する。
	 *
	 * @param context JSON変換用 id,value格納オブジェクト
	 */
	public void addJsonValue(AppJsonContext context) {
		jsonValues.add(context);
	}

	/**
	 * @return jsonValues
	 */
	public List<AppJsonContext> getJsonValues() {
		return jsonValues;
	}

	/**
	 * @param jsonValues セットする jsonValues
	 */
	public void setJsonValues(List<AppJsonContext> jsonValues) {
		this.jsonValues = jsonValues;
	}

	/**
	 * JSON変換用 id,value格納オブジェクト。
	 */
	public class AppJsonContext implements Serializable {

		private static final long serialVersionUID = 8971435340559517297L;

		/**
		 * オブジェクトID名
		 */
		private String objId;

		/**
		 * オブジェクト値
		 */
		private String value;

		/**
		 * objIdの値を返す。
		 *
		 * @return objId
		 */
		public String getObjId() {
			return objId;
		}

		/**
		 * @param objId objIdの値をセットする。
		 */
		public void setObjId(String objId) {
			this.objId = objId;
		}

		/**
		 * valueの値を返す。
		 *
		 * @return value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value valueの値をセットする。
		 */
		public void setValue(String value) {
			this.value = value;
		}

	}

}
