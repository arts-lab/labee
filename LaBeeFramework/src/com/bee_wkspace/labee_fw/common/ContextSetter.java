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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.bee_wkspace.labee_fw.common.db.RowColumn;

/**
 * コンテキストセッター実行クラス。
 *
 * @author ARTS Laboratory
 *
 * $Id: ContextSetter.java 559 2016-08-14 12:24:00Z pjmember $
 */
public class ContextSetter {

	/**
	 * DB結果行カラム内容をコンテキストクラスにセットする。
	 *
	 * @param context コンテキストクラス
	 * @param rowColumn DB結果行カラム
	 * @throws Exception 例外
	 */
	public static void set(Object context, RowColumn rowColumn) throws Exception {
		set(context, rowColumn, null);
	}

	/**
	 * DB結果行カラム内容をコンテキストクラスにセットする。(複合化対応)
	 *
	 * @param context コンテキストクラス
	 * @param rowColumn DB結果行カラム
	 * @param decodeColumn 複合化カラム名配列
	 * @throws Exception 例外
	 */
	public static void set(Object context, RowColumn rowColumn, String[] decodeColumn) throws Exception {
		List<String> decodeList = null;
		if (decodeColumn != null) {
			decodeList = Arrays.asList(decodeColumn);
		}

		Class<?> clazz = Class.forName(context.getClass().getName());

		// セッターメソッドを取得する
		HashMap<String, Class<?>> methodMap = new HashMap<String, Class<?>>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				Class<?>[] methodArgs = method.getParameterTypes();
				if (methodArgs.length == 1) {
					methodMap.put(method.getName(), methodArgs[0]);
				}
			}
		}

		// カラム名を元にセッターメソッドを実行する
		HashMap<String, String> columnMap = rowColumn.getColumnMap();
		Set<String> keySet = columnMap.keySet();
		Iterator<String> ite = keySet.iterator();
		while (ite.hasNext()) {
			String key = ite.next();
			String setterName = makeSetterMethodName(key);

			Class<?> methodArg = methodMap.get(setterName);
			if (methodArg != null) {
				String type = methodArg.getTypeName();
				String value = null;

				if (decodeList != null && decodeList.contains(key)) {
					// 複合化する場合
					value = EncodeUtil.decode(columnMap.get(key));
				} else {
					value = columnMap.get(key);
				}

				if (type.equals(String.class.getName())) {
					Method setter = clazz.getMethod(setterName, String.class);
					setter.invoke(context, value);

				} else if (type.equals("int")) {
					if (StringUtil.isNull(value)) {
						Method setter = clazz.getMethod(setterName, int.class);
						setter.invoke(context, Integer.parseInt(value));
					}
				} else if (type.equals("long")) {
					if (StringUtil.isNull(value)) {
						Method setter = clazz.getMethod(setterName, long.class);
						setter.invoke(context, Long.parseLong(value));
					}
				} else if (type.equals("double")) {
					if (StringUtil.isNull(value)) {
						Method setter = clazz.getMethod(setterName, double.class);
						setter.invoke(context, Double.parseDouble(value));
					}
				} else if (type.equals(Date.class.getName())) {
					if (StringUtil.isNull(value)) {
						Method setter = clazz.getMethod(setterName, Date.class);
						setter.invoke(context, DateTimeUtil.parseDate(value, DateTimeUtil.YYYY_MM_DD_HH_MM_SS));
					}
				}
			}
		}
	}

	/**
	 * セッターメソッド名を生成する。
	 *
	 * @param key キー文字列
	 * @return セッター名
	 */
	private static String makeSetterMethodName(String key) {
		StringBuilder buf = new StringBuilder();
		buf.append("set");
		String[] words = key.split("_");
		for (String word : words) {
			buf.append(word.substring(0, 1).toUpperCase());
			buf.append(word.substring(1, word.length()));
		}
		return buf.toString();
	}

}
