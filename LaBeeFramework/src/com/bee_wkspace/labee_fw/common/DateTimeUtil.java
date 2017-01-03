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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日付時間ユーティリティークラス。<br>
 * 各種日付操作系のメソッドを定義している。
 *
 * @author ARTS Laboratory
 *
 * $Id: DateTimeUtil.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class DateTimeUtil {

	/** 時分秒までの日付フォーマット */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/** 時分秒までの日付フォーマット2 */
	public static final String YYYY_MM_DD_HH_MM_SS2 = "yyyy/MM/dd HH:mm:ss";

	/** 時分秒までの日付連結フォーマット */
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/** 日付フォーマット YYYY-MM-DD */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/** 日付フォーマット YYYY/MM/DD */
	public static final String YYYY_MM_DD2 = "yyyy/MM/dd";

	/** 日付フォーマットオブジェクト (yyyyMMddHHmmss) */
	public static SimpleDateFormat sdf_YYYYMMDDHHMMSS = new SimpleDateFormat(YYYYMMDDHHMMSS);

	/** 日付フォーマットオブジェクト (yyyy-MM-dd) */
	public static SimpleDateFormat sdf_YYYY_MM_DD = new SimpleDateFormat(YYYY_MM_DD);

	/** 日付フォーマットオブジェクト (yyyy/MM/dd) */
	public static SimpleDateFormat sdf_YYYY_MM_DD2 = new SimpleDateFormat(YYYY_MM_DD);

	/** 日付フォーマットオブジェクト (yyyy-MM-dd HH:mm:ss) */
	public static SimpleDateFormat sdf_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

	/** 日付フォーマットオブジェクト (yyyy/MM/dd HH:mm:ss) */
	public static SimpleDateFormat sdf_YYYY_MM_DD_HH_MM_SS2 = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS2);

	/**
	 * 現在日付を基にした6桁の連結文字列を返す。
	 *
	 * @return 文字列
	 */
	public static String getYMD() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		String ymd = year
				+ setKeta(month, 2)
				+ setKeta(date, 2)
				+ setKeta(hour, 2);
		return ymd;
	}

	/**
	 * 数値を指定桁の0で桁あわせして返す。
	 *
	 * @param val 元の数値
	 * @param len 桁数
	 * @return 桁あわせ後の文字
	 */
	public static String setKeta(int val, int len) {
		String valStr = Integer.toString(val);
		int diff = len - valStr.length();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < diff; i++) {
			buf.append("0");
		}
		buf.append(valStr);
		return buf.toString();
	}

	/**
	 * 指定フォーマットの現在時間文字列を得る。
	 *
	 * @param format 日付フォーマット
	 * @return 日付文字列
	 */
	public static String getRealTime(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 現在時間文字列を得る。 (yyyy/MM/dd)
	 *
	 * @return 日付文字列
	 */
	public static String getRealTime() {
		Date currentTime = new Date();
		String dateString = sdf_YYYY_MM_DD_HH_MM_SS.format(currentTime);
		return dateString;
	}

	/**
	 * YYYYMMDDの日付文字列を年月日に分割して配列で返す。<br>
	 * (時分秒まであればそこまで設定)
	 *
	 * @param val 入力値
	 * @return 結果配列
	 */
	public static String[] separeteYYMMDD(String val) {
		String[] array = new String[6];
		if (val != null && val.length() != 0 && val.length() >= 8) {
			// 年
			array[0] = val.substring(0, 4);
			// 月
			array[1] = val.substring(4, 6);
			// 日
			array[2] = val.substring(6, 8);

			// 時
			if (val.length() >= 10) {
				array[3] = val.substring(8, 10);
			}
			// 分
			if (val.length() >= 12) {
				array[4] = val.substring(10, 12);
			}
			// 秒
			if (val.length() >= 14) {
				array[5] = val.substring(12, 14);
			}
		}
		return array;
	}

	/**
	 * 指定フォーマットの日付文字列をDate型に変換して返す。
	 *
	 * @param val 日付文字列
	 * @param format 日付フォーマット
	 * @return 変換後Dateオブジェクト
	 * @throws ParseException 例外
	 */
	public static Date parseDate(String val, String format) throws ParseException {
		Date formatDate = null;
		if (val != null && val.length() != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			formatDate = sdf.parse(val);
		}
		return formatDate;
	}

	/**
	 * 引数の日付を指定フォーマットの日付文字列に変換して返す。
	 *
	 * @param date 日付オブジェクト
	 * @param formater 日付フォーマット
	 * @return 日付文字列
	 */
	public static String formatDateStr(Date date, SimpleDateFormat formater) {
		if (date != null) {
			return formater.format(date);
		} else {
			return null;
		}
	}

	/**
	 * 指定日付にミリセカンド秒を加算する。
	 *
	 * @param date 元の日付
	 * @param miliSecond 加算ミリセカンド
	 * @return 加算後の日付
	 */
	public static Date addMiliSecond(Date date, long miliSecond) {
		long time = date.getTime();
		time = time + miliSecond;
		return new Date(time);
	}

	/**
	 * YYYYY/M/D等の文字列をYYYY/MM/DD成型して返す。<br>
	 * (例)月や日が1桁の場合に2桁にする。
	 *
	 * @param val 入力値
	 * @return 結果値
	 */
	public static String modifiYyyyMMddStr(String val) {
		if (val != null && val.length() != 0) {
			int idx = val.indexOf("/");
			if (idx != -1) {
				String[] array = val.split("/");
				if (array.length == 3) {
					try {
						StringBuilder buf = new StringBuilder();

						int year = Integer.parseInt(array[0]);
						int month = Integer.parseInt(array[1]);
						int day = Integer.parseInt(array[2]);

						buf.append(year);
						buf.append("/");
						buf.append(setKeta(month, 2));
						buf.append("/");
						buf.append(setKeta(day, 2));
						val = buf.toString();

					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (val.length() == 8) {
					try {
						Integer.parseInt(val);
						StringBuilder buf = new StringBuilder();
						buf.append(val.substring(0, 4));
						buf.append("/");
						buf.append(val.substring(4, 6));
						buf.append("/");
						buf.append(val.substring(6, 8));
						val = buf.toString();

					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return val;
	}

}
