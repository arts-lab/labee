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

import java.util.Random;

/**
 * 文字列ユーティリティークラス。<br>
 * 各種文字列操作系のメソッドを定義している。
 *
 * @author ARTS Laboratory
 *
 * $Id: StringUtil.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class StringUtil {

	/** 共通で使用するランダム発生オブジェクト */
	public static Random rnd = new Random();

	/**
	 * 文字列のnullチェック。<br>
	 * 対象文字列をトリムし、空文字チェックも行う。
	 *
	 * @param val 対象文字列
	 * @return true = nullではない false = null
	 */
	public static boolean isNull(String val) {
		boolean flg = false;
		if (val != null && val.trim().length() != 0) {
			flg = true;
		}
		return flg;
	}

	/**
	 * 文字列がnull以外の場合に両端の空白をトリムして返す。
	 *
	 * @param val 入力文字列
	 * @return 結果文字列
	 */
	public static String trim(String val) {
		if (isNull(val)) {
			return val.trim();
		} else {
			return val;
		}
	}

	/**
	 * 文字配列の文字列内容をトリムして返す。
	 *
	 * @param vals 入力配列
	 * @return 結果配列
	 */
	public static String[] trimStringArray(String[] vals) {
		if (vals != null) {
			int size = vals.length;
			String[] newVals = new String[size];

			for (int i = 0; i < size; i++) {
				if (vals[i] != null) {
					newVals[i] = vals[i].trim();
				}
			}
			return newVals;
		} else {
			return vals;
		}
	}

	/**
	 * テキストの改行をHTMLの改行(br)に変換して返す。
	 *
	 * @param text 対象文字列
	 * @return 変換文字列
	 */
	public static String convToHtmlKaigyo(String text) {
		StringBuilder buf = new StringBuilder();
		if (text != null) {
			String[] sepVal = text.split(CommonDefine.LINE_SEP);
			int size = sepVal.length;
			for (int i = 0; i < size; i++) {
				buf.append(sepVal[i] + "<br>");
			}
		}
		return buf.toString();
	}

	/**
	 * テキストの改行をHTMLの改行(br)に変換して返す。<br>
	 * http,httpsの文字列をaタグリンクに変換する。
	 *
	 * @param buf 対象文字列バッファ
	 */
	public static void convToHtmlKaigyoLink(StringBuilder buf) {
		String[] sepText = buf.toString().split(CommonDefine.LINE_SEP);
		buf.delete(0, buf.length());
		
		for (String line : sepText) {
			int startIdx = line.indexOf("http", 0);
			if (startIdx != -1) {
				StringBuilder lineBuf = new StringBuilder(line);

				int endIdx = line.length();
				int size = line.length();
				for (int i = startIdx; i < size - 1; i++) {
					String chkStr = line.substring(i, i + 1);
					if (StringUtil.isZenkaku(chkStr)) {
						endIdx = i;
						break;
					} else if (chkStr.equals(" ")) {
						endIdx = i;
						break;
					}
				}

				String url = lineBuf.substring(startIdx, endIdx);
				lineBuf.insert(endIdx, "</a>");
				lineBuf.insert(startIdx, "<a href=\"" + url + "\" target=\"_blank\" >");
				buf.append(lineBuf);

			} else {
				buf.append(line);
			}
			buf.append("<br>");
		}
	}

	/**
	 * テキストの改行をHTMLの改行(br)に変換して返す。<br>
	 * http,httpsの文字列をaタグリンクに変換する。
	 *
	 * @param text 対象文字列
	 * @return 変換文字列
	 */
	public static String convToHtmlKaigyoLink(String text) {
		if (text != null) {
			StringBuilder buf = new StringBuilder();
			convToHtmlKaigyoLink(buf);
			return buf.toString();
			
		} else {
			return text;
		}
	}

	
	/**
	 * 指定文字列バッファ内のhttp,httpsの文字列をaタグリンクに変換する。
	 * @param buf	文字列バッファ
	 * @param idx	検索位置
	 */
	public static void convLinkTag(StringBuilder buf, int idx) {
		int startIdx = buf.indexOf("http", idx);
		if (startIdx != -1) {
			int endIdx = buf.length();
			int size = buf.length();
			for (int i = startIdx; i < size - 1; i++) {
				String chkStr = buf.substring(i, i + 1);
				if (StringUtil.isZenkaku(chkStr)) {
					endIdx = i;
					break;
				} else if (chkStr.equals(" ") || chkStr.equals("<") || chkStr.equals(">") || chkStr.equals(CommonDefine.LINE_SEP)) {
					endIdx = i;
					break;
				}
			}

			String url = buf.substring(startIdx, endIdx);
			buf.insert(endIdx, "</a>");
			buf.insert(startIdx, "<a href=\"" + url + "\" target=\"_blank\" >");

			convLinkTag(buf, buf.indexOf("</a>", startIdx));
		} else {
			return;
		}
	}
	
	
	/**
	 * 文字列内容の数値チェック
	 *
	 * @param val 値
	 * @return true = 数値である
	 */
	public static boolean isNumber(String val) {
		boolean flg = false;
		try {
			// 数値変換して数値か確かめる
			Integer.parseInt(val);
			flg = true;
		} catch (NumberFormatException e) {
			flg = false;
		}
		return flg;
	}

	/**
	 * 文字列の半角英数字チェック
	 *
	 * @param val 値
	 * @return true=半角英数字である
	 */
	public static boolean isHankakuEisuu(String val) {
		boolean flg = false;
		byte[] bytes = val.getBytes();
		int beams = val.length();
		if (beams != bytes.length) {
			flg = false;
		} else if (val.matches("[0-9a-zA-Z]+") == false) {
			flg = false;
		} else {
			flg = true;
		}
		return flg;
	}

	/**
	 * 文字列の半角英数字チェック(半角スペースを含む)
	 *
	 * @param val 値
	 * @return true=半角英数字である
	 */
	public static boolean isHankakuEisuuSpace(String val) {
		boolean flg = false;
		byte[] bytes = val.getBytes();
		int beams = val.length();
		if (beams != bytes.length) {
			flg = false;
		} else if (val.matches("[0-9a-zA-Z ]+") == false) {
			flg = false;
		} else {
			flg = true;
		}
		return flg;
	}

	/**
	 * 文字列の半角英数字チェック(記号を含む)
	 *
	 * @param val 値
	 * @return true=半角英数字記号である
	 */
	public static boolean isHankakuEisuuKigou(String val) {
		boolean flg = false;
		byte[] bytes = val.getBytes();
		int beams = val.length();
		if (beams != bytes.length) {
			flg = false;
		} else if (val.matches("[a-zA-Z_0-9 -/:-@ ]+") == false) {
			flg = false;
		} else {
			flg = true;
		}
		return flg;
	}

	/**
	 * 文字列の全角チェック
	 *
	 * @param val 値
	 * @return true = 全角文字である
	 */
	public static boolean isZenkaku(String val) {
		boolean flg = false;

		if (val.matches("^[^ -~｡-ﾟ]+$")) {
			flg = true;
		}
		return flg;
	}

	/**
	 * 文字列の全角カナチェック
	 *
	 * @param val 値
	 * @return true = 全角カナである
	 */
	public static boolean isZenkakuKana(String val) {
		boolean flg = false;

		if (val.matches("^[ァ-ヶ 　ー]+$") == false) {
			flg = true;
		}
		return flg;
	}

	/**
	 * 指定文字を置き換え文字に置き換える。
	 *
	 * @param val 対象文字列
	 * @param tgtItem 置き換え文字列
	 * @param replaceItem 置き換え後の文字列
	 * @return 結果文字列
	 */
	public static String replaceText(String val, String tgtItem, String replaceItem) {
		if (isNull(val) && isNull(tgtItem)) {
			StringBuilder buf = new StringBuilder(val);
			int len = tgtItem.length();
			boolean loopFlg = true;

			while (loopFlg) {
				int idx = buf.indexOf(tgtItem);
				if (idx != -1) {
					buf.replace(idx, idx + len, replaceItem);
				} else {
					loopFlg = false;
				}
			}
			val = buf.toString();
		}
		return val;
	}

	/**
	 * 引数の文字列が指定数以上の長さの場合は余分をカットして返す。<br>
	 * 半角は2文字で1文字長として判断。
	 *
	 * @param val 文字列
	 * @param num 指定長
	 * @return カット文字列
	 */
	public static String cutString(String val, int num) {
		if (isNull(val)) {
			StringBuilder buf = new StringBuilder();
			double count = 0.0;

			int size = val.length();
			for (int i = 0; i < size; i++) {
				char ch = val.charAt(i);
				if ((ch <= '\u007e') || (ch == '\u00a5') || (ch == '\u203e') || (ch >= '\uff61' && ch <= '\uff9f')) {
					count = count + 0.5;
				} else {
					count = count + 1.0;
				}
				buf.append(ch);

				if (count >= num) {
					buf.append("..");
					break;
				}
			}
			val = buf.toString();
		} else {
			val = "";
		}
		return val;
	}

	/**
	 * 指定した文字列をサニタイジングして文字列バッファに格納する。
	 * 
	 * @param buf 文字列バッファ
	 * @param val 対象文字列
	 */
	public static void sanitizing(StringBuilder buf, String val) {
		if (isNull(val)) {
			char c;
			int len = val.length();
			for (int i = 0; i < len; i++) {
				c = val.charAt(i);
				if (c == '<') {
					buf.append("&lt;");

				} else if (c == '>') {
					buf.append("&gt;");

				} else if (c == '"') {
					buf.append("&quot;");

				} else if (c == '\'') {
					buf.append("&#39;");

				} else if (c == '&') {
					buf.append("&amp;");

				} else if (c == '\t') {
					buf.append("    ");
				} else {
					buf.append(c);
				}
			}
		}
	}

	/**
	 * 16進数値を2進数文字列に変換して返す。
	 *
	 * @param hexVal 16進数値
	 * @return 2進数文字列
	 */
	public static String hexToBinary(String hexVal) {
		String retVal = null;
		if (StringUtil.isNull(hexVal)) {
			// 16進数を10進数に変換
			int octData = Integer.decode("0x" + hexVal);

			// 10進数を2進数に変換する。
			retVal = Integer.toBinaryString(octData);
		}
		return retVal;
	}

	/**
	 * 2進数文字列を16進数文字列に変換して返す。
	 *
	 * @param binVal 2進数値
	 * @return 16進数文字列
	 */
	public static String binaryToHex(String binVal) {
		String retVal = null;
		if (StringUtil.isNull(binVal)) {
			// 2進数を10進数に変換
			int octData = Integer.parseInt(binVal, 2);

			// 10進数を16進数に変換
			retVal = Integer.toHexString(octData);
		}
		return retVal;
	}

	/**
	 * 16進数文字列をバイナリ配列に変換する。
	 *
	 * @param hexVal 6進数文字列
	 * @return バイナリ配列
	 */
	public static byte[] hexTobinArray(String hexVal) {
		byte[] bytes = new byte[hexVal.length() / 2];
		for (int index = 0; index < bytes.length; index++) {
			bytes[index] = (byte) Integer.parseInt(hexVal.substring(index * 2, (index + 1) * 2), 16);
		}
		return bytes;
	}

	/**
	 * 入力値がnullの場合は空文字を返す。
	 *
	 * @param val 値
	 * @return 結果
	 */
	public static String nullToSpace(String val) {
		if (!isNull(val)) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * 文字列バッファーに文字列を追加し、改行も追加する。
	 *
	 * @param buf 文字列バッファ
	 * @param value 追加文字列
	 */
	public static void appendBufLine(StringBuilder buf, String value) {
		buf.append(value);
		buf.append(CommonDefine.LINE_SEP);
	}

	/**
	 * 指定桁のランダム文字列を生成する。
	 *
	 * @param len 出力桁
	 * @return ランダム文字列
	 */
	public static String createRandom(int len) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int numRnd = rnd.nextInt(10) + 48; // 数値乱数値
			int smallRnd = rnd.nextInt(26) + 97; // 小文字乱数値
			int bigRnd = rnd.nextInt(26) + 65; // 大文字乱数値
			int selRnd = rnd.nextInt(3); // 選択乱数値

			char c;

			switch (selRnd) {
			case (0):
				c = (char) numRnd;
				break;
			case (1):
				c = (char) smallRnd;
				break;
			case (2):
				c = (char) bigRnd;
				break;
			default:
				c = ' ';
			}
			buf.append(c);
		}
		return buf.toString();
	}

}
