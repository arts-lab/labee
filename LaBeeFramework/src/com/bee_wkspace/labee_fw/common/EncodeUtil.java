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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 暗号化ユーティリティークラス。<br>
 * <br>
 * 文字列の暗号化、複合化を行なう。本クラスを使用する際はシステム初期時に1度のみ<b>init</b>メソッドを実行し<br>
 * 引数に暗号化キー等を予め設定する必要がある。<br>
 * (フレームワークが初期化時に<b>init</b>メソッドを実行するので手動で実行してはいけない)
 * <br> 
 * 複合化する際に暗号化キーが異なっていると元の値には戻せないので運用後はキーは変更しないををすすめる
 *
 * @author ARTS Laboratory
 *
 * $Id: EncodeUtil.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class EncodeUtil {

	/** 暗号化方式 */
	private static final String ENC_METHOD = "Blowfish";

	/** 暗号化キー */
	private static String encodeKey = null;

	/** 暗号化有効フラグ */
	private static boolean enableFlg = true;

	/** 暗号化、複合化対象文字コード */
	private static String mojiCode = "UTF-8";

	/**
	 * 初期設定。
	 *
	 * @param _encodeKey 暗号化キー
	 * @param _enableFlg 暗号化有効フラグ
	 * @param _mojiCode 暗号化、複合化対象文字コード
	 */
	public static void init(String _encodeKey, boolean _enableFlg, String _mojiCode) {
		encodeKey = _encodeKey;
		enableFlg = _enableFlg;
		mojiCode = _mojiCode;
	}

	/**
	 * 文字列を暗号化して返す。
	 *
	 * @param text 対象文字列
	 * @return 暗号化後の文字列
	 * @throws Exception 例外
	 */
	public static String encode(String text) throws Exception {
		if (text != null) {
			if (enableFlg) {
				// 暗号化するテキストをバイナリ化
				byte[] binary = text.getBytes(mojiCode);

				// 暗号化のキー
				byte[] key = (encodeKey).getBytes();

				// 暗号化の実行
				SecretKeySpec k = new SecretKeySpec(key, ENC_METHOD);
				Cipher c = Cipher.getInstance(ENC_METHOD);
				c.init(Cipher.ENCRYPT_MODE, k);
				byte[] encrypted = c.doFinal(binary);

				// Base64エンコード
				return Base64.encode(encrypted);
			} else {
				return text;
			}
		} else {
			return null;
		}
	}

	/**
	 * 暗号化された文字列を複合化して返す。
	 *
	 * @param encodeText 暗号化された文字列
	 * @return 複合化された文字列
	 * @throws Exception 例外
	 */
	public static String decode(String encodeText) throws Exception {
		if (encodeText != null) {
			if (enableFlg) {
				// Base64エンコードされた文字列をデコード
				byte[] encodeBinary = Base64.decode(encodeText);

				// 暗号化で使用したキー
				byte[] key = (encodeKey).getBytes();

				// 復号化の実行
				SecretKeySpec k = new SecretKeySpec(key, ENC_METHOD);
				Cipher c = Cipher.getInstance(ENC_METHOD);
				c.init(Cipher.DECRYPT_MODE, k);
				byte[] binary = c.doFinal(encodeBinary);

				// 元の文字列を復元
				return new String(binary, mojiCode);
			} else {
				return encodeText;
			}
		} else {
			return null;
		}
	}

}
