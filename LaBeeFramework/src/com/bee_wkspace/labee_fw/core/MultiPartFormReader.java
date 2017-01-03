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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.StringUtil;

/**
 * マルチマートフォーム対応のHTTPフォームパラメータ取得クラス。<br>
 * 受信したパラメータ値はサニタイジング（クロスサイトスクリプティング対策）により<br>
 * &lt; &gt; &quot; &#39; &amp;がエスケープ変換され、半角パーセント"%"は 全角"％"に変換される。<br>
 * 各パラメータはHashMap型にパラメータ名をキーにしてパラメータ値文字列が格納される。<br>
 * 同名のパラメータが複数存在した場合は1つのパラメータ名に対してカンマ区切りで連結された値が設定される。<br>
 * マルチパートでアップロードされたファイルはgetByteArrayInputStream()メソッドでByteArrayInputStream型で取得、<br>
 * 又はgetUploadText()メソッドで文字列型で取得出来る。<br>
 *
 *
 * @author ARTS Laboratory
 *
 * $Id: MultiPartFormReader.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class MultiPartFormReader {

	/** アップロードファイルサイズの上限(Byte) */
	private static int UPLOAD_MAX_BITE = 1024 * 100 * 200; // 2メガバイト

	/** パラメータ文字列の最大許可長(これを超えたら以降文字はカット) */
	public static int MAX_PARAM_LENGTH = 4000;

	/** 使用文字コード */
	public String encodeType = CommonDefine.ENCODE_UTF_8;

	/** ファイルデータ格納バッファ */
	private byte[] buffer;

	/** フォームパラメータ格納Obj */
	private HashMap<String, String> paramsMap = null;

	/** アップロードされたファイル名 */
	private String fileName = null;

	/** アップロードファイル実体バイトストリーム */
	private ByteArrayOutputStream fileObjStream = null;

	/** アップロードファイルサイズのオーバーエラー */
	private boolean sizeOverErrFlg = false;

	/** サニタイジングフィルターを実行するかのフラグ true=実行 */
	private boolean doFilterFlg = true;

	/** パーセント文字列変換 */
	private boolean doPersentFilter = true;

	private static final String CONTENTTYPE = "Content-Type";
	private static final String CONTENTDIPOSITION = "Content-Disposition:";
	private static final String NAMEPART = "name=";
	private static final String FILENAME = "filename=";
	private static final String BOUNDARY = "-----";
	private static final String DATA_END = "--";

	/**
	 * コンストラクタ。
	 *
	 * @param encodeType エンコード種類
	 */
	public MultiPartFormReader(String encodeType) {
		super();
		this.encodeType = encodeType;
		paramsMap = new HashMap<String, String>();
		buffer = new byte[6000 * 10];
		fileObjStream = new ByteArrayOutputStream();
		sizeOverErrFlg = false;

	}

	/**
	 * リクエスト情報からフォーム情報を読み込む<br>
	 * ファイルアップロードしたマルチパート情報にも対応する。
	 *
	 * @param request リクエスト情報
	 * @throws Exception 例外
	 */
	public void parseMultiPartRequest(HttpServletRequest request) throws Exception {

		// サニタイジングフィルターフラグ
		String doFilterFlgVal = request.getParameter("_doFilderFlg");
		if (StringUtil.isNull(doFilterFlgVal) && doFilterFlgVal.toLowerCase().equals("false")) {
			doFilterFlg = false;
		} else {
			doFilterFlg = true;
		}

		String doParsentFlgVal = request.getParameter("_doParsentFlg");
		if (StringUtil.isNull(doParsentFlgVal) && doParsentFlgVal.toLowerCase().equals("false")) {
			doPersentFilter = false;
		} else {
			doPersentFilter = true;
		}

		request.setCharacterEncoding(encodeType);
		ServletInputStream stream = request.getInputStream();
		try {
			int index; // ストリームサーチ位置
			int dotype = 0; // 処理タイプ
			String line; // １行分の値
			String name = null; // パラメータ名

			this.paramsMap.clear();
			index = stream.readLine(buffer, 0, buffer.length);

			// ブラウザのナビゲーションバーでURL直入力で呼び出された場合
			if (index == -1) {
				this.paramsMap = getParameter(request);

				// フォームから呼び出された場合
			} else {
				line = new String(buffer, 0, index);

				// マルチパートフォームか通常フォームかのチェック
				if (line.startsWith(BOUNDARY)) {
					dotype = 0;

					while ((index = stream.readLine(buffer, 0, buffer.length)) != -1) {
						line = new String(buffer, 0, index, encodeType);

						// バウンダリ区切りチェック
						if (line.startsWith(BOUNDARY)) {
							dotype = 0;
						}

						// ファイル終了位置チェック
						if (line.endsWith(DATA_END)) {
							break;
						}

						// アップロードファイル部分開始チェック
						if (dotype == 2 && line.startsWith(CONTENTTYPE)) {
							dotype = 3;
						}

						switch (dotype) {
						case (1): // パラメータ名、バリューが確定した場合

							String chk = (String) paramsMap.get(name);
							line = URLDecoder.decode(persentFilter(line), encodeType);
							if (chk != null && chk.length() != 0) {
								if (line.equals("\r\n")) {
									line = chk + CommonDefine.LINE_SEP;
									paramsMap.put(name, line);
								} else {
									line = chk + CommonDefine.LINE_SEP + doFilter(line.trim());
									paramsMap.put(name, line);
								}

							} else {
								if (line.trim().length() != 0) {
									paramsMap.put(name, doFilter(line.trim()));
								}
							}

							break;
						case (3): // ファイル実体に発生する空行をスルーする場合
							if (!line.startsWith(CONTENTTYPE)) {
								dotype = 4;
							}
							break;
						case (4): // ファイル実体データを書き込む場合
							fileObjStream.write(buffer, 0, index);
							if (fileObjStream.size() > UPLOAD_MAX_BITE) {
								throw new StringIndexOutOfBoundsException();
							}
							break;

						default:
							break;
						}

						if (dotype == 0 && line.startsWith(CONTENTDIPOSITION)) {
							// パラメータ名取得
							int startPoint = (line.indexOf(NAMEPART)) + 6;
							int endPoint = line.indexOf("\"", startPoint);
							name = (line.substring(startPoint, endPoint)).trim();
							dotype = 1;

							// ファイル名取得
							startPoint = line.indexOf(FILENAME, startPoint);
							if (startPoint != -1) {
								endPoint = line.indexOf("\"", startPoint + 10);
								String fileNameWk = checkFileName((line.substring(startPoint + 10, endPoint)).trim());
								if (!fileNameWk.isEmpty()) {
									fileName = fileNameWk;
								}
								dotype = 2;
							}
						}
					}
				} else {
					// 通常フォームの場合はパラメータのみを読み込み＆保持
					this.getParameter(line, request);
				}
			}

			/** ファイルサイズオーバー例外 */
		} catch (StringIndexOutOfBoundsException e) {
			sizeOverErrFlg = true;

		} catch (Exception e) {
			throw e;

		} finally {
			stream.close();
			fileObjStream.close();
			stream = null;
			buffer = null;
		}
	}

	/**
	 * 読み込み行からパラメータ値を分解してHashMapに格納する。
	 *
	 * @param line １行分の文字列
	 * @param request リクエスト情報
	 * @throws Exception 例外
	 */
	private void getParameter(String line, HttpServletRequest request) throws Exception {
		try {
			String[] items = line.split("&");
			int len = items.length;
			for (int i = 0; i < len; i++) {
				int idx = items[i].indexOf("=");
				if (idx != -1) {
					String name = items[i].substring(0, idx);
					if (name.trim().length() != 0) {
						String value = items[i].substring(idx + 1, items[i].length());

						try {
							value = new String(URLDecoder.decode(value, "iso-8859-1").getBytes("iso-8859-1"),
									encodeType);

						} catch (IllegalArgumentException ee) {
							value = "";
							sizeOverErrFlg = true;
						}

						String wk = (String) this.paramsMap.get(name);
						if (wk == null || wk.length() == 0) { // 未登録の場合はそのまま追加
							this.paramsMap.put(name, doFilter(value.trim()));

						} else { // 既に同名で登録済みの場合はカンマを付けて追加する
							wk = wk + "," + value.trim();
							this.paramsMap.put(name, doFilter(wk));
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * パラメータを取得してHashMapに格納する。
	 *
	 * @param request リクエスト情報
	 * @return フォーム内容マップ
	 * @throws Exception 例外
	 */
	@SuppressWarnings("rawtypes")
	private HashMap<String, String> getParameter(HttpServletRequest request) throws Exception {
		HashMap<String, String> reqHash = new HashMap<String, String>();
		try {
			Enumeration e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String[] values = request.getParameterValues(key);
				int len = values.length;

				// 同名で複数のパラメータが存在する場合
				if (len > 1) {
					StringBuilder buf = new StringBuilder();
					for (int i = 0; i < len; i++) {
						String wk = values[i];
						if (wk != null && wk.length() != 0) {
							buf.append(wk.trim());
							if (i < len - 1) {
								buf.append(",");
							}
						}
					}
					reqHash.put(key, doFilter(buf.toString()));

					// 単独パラメータの場合
				} else {
					String value = persentFilter(values[0]).trim();
					String wk = new String(URLDecoder.decode(value, "iso-8859-1").getBytes("iso-8859-1"),
							encodeType);

					if (wk.startsWith("????")) {
						reqHash.put(key, doFilter(value));
					} else {
						reqHash.put(key, doFilter(wk));
					}

				}
			}
		} catch (Exception e) {
			throw e;
		}
		return reqHash;
	}

	/**
	 * パラメータの最大文字長をチェック(バッファオーバーフロー対策)
	 *
	 * @param val パラメータ値
	 * @return 最大値でカットしたパラメータ文字列
	 */
	protected static String checkLen(String val) {
		if (val != null) {
			int len = val.length();
			if (len > MAX_PARAM_LENGTH) {
				val = val.substring(0, MAX_PARAM_LENGTH - 1);
			}
		}
		return val;
	}

	/**
	 * パラメータ中に%がある場合はエスケープ変換。
	 *
	 * @param val パラメータ値
	 * @return 変換後パラメータ文字列
	 */
	protected String persentFilter(String val) {
		if (doPersentFilter) {
			if (val.indexOf("%") != -1) {
				val = val.replaceAll("%", "%25");
			}
		}
		return val;
	}

	/**
	 * サニタイジング（クロスサイトスクリプティング対策）フィルター変換。<br>
	 * "&lt; &gt; &quot; &#39; &amp;を変換する。
	 *
	 * @param val パラメータ値
	 * @return 変換後パラメータ文字列
	 */
	protected String doFilter(String val) {
		if (doFilterFlg) {
			StringBuilder buf = new StringBuilder();
			if (StringUtil.isNull(val)) {
				StringUtil.sanitizing(buf, val);
			}
			return buf.toString();
		} else {
			return val;
		}
	}

	/**
	 * フルパスのファイル文字列からファイル名のみを取得する。
	 *
	 * @param value フルパスのファイル文字列
	 * @return String 抽出されたファイル名
	 */
	private static String checkFileName(String value) {
		if (value != null) {
			int index = value.lastIndexOf(CommonDefine.SEP);
			if (index != -1) {
				value = value.substring(index + 1, value.length());
			}

			index = value.lastIndexOf("\\");
			if (index != -1) {
				value = value.substring(index + 1, value.length());
			}

		}
		return value;
	}

	/**
	 * アップロードしたファイルのストリームオブジェクトを返す。
	 *
	 * @return ストリームオブジェクト
	 * @throws Exception 例外
	 */
	public ByteArrayInputStream getByteArrayInputStream() throws Exception {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(fileObjStream.toByteArray());
			return bais;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * フォームパラメータ格納マップを返す。
	 *
	 * @return フォームパラメータ格納マップ
	 */
	public HashMap<String, String> getParamMap() {
		return paramsMap;
	}

	/**
	 * アップロードしたファイルのファイル名を返す。
	 *
	 * @return ファイル名
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * バイナリ読み込み時のバッファ値をセットする。
	 *
	 * @param size バッファ値
	 */
	public void setBuffer(int size) {
		buffer = new byte[size];
	}

	/**
	 * アップロードしたファイルのバイト配列を返す。
	 *
	 * @return アップロードしたファイルのバイト配列
	 */
	public byte[] getFileObjByte() {
		return fileObjStream.toByteArray();
	}

	/**
	 * アップロードしたテキストを取得する。
	 *
	 * @return ファイル内容
	 * @throws UnsupportedEncodingException 例外
	 */
	public String getUploadText() throws UnsupportedEncodingException {
		return new String(getFileObjByte(), "JISAutoDetect");

	}

	/**
	 * ファイルアップロード最大サイズチェック。
	 *
	 * @return true = サイズオーバー
	 */
	public boolean isSizeOverErrFlg() {
		return sizeOverErrFlg;
	}

	/**
	 * アップロードしたファイルのバイトストリームを返す。
	 *
	 * @return ファイルストリーム
	 */
	public ByteArrayOutputStream getFileObjStream() {
		return fileObjStream;
	}

	/**
	 * バイトストリームを設定する。
	 *
	 * @param fileObjStream バイトストリーム
	 */
	public void setFileObjStream(ByteArrayOutputStream fileObjStream) {
		this.fileObjStream = fileObjStream;
	}

	/**
	 * ファイルアップロード最大サイズを設定する。
	 *
	 * @param upload_max_bite ファイルアップロード最大サイズ
	 */
	public static void setUPLOAD_MAX_BITE(int upload_max_bite) {
		UPLOAD_MAX_BITE = upload_max_bite;
	}

	/**
	 * パラメータ文字列最大長を設定する。
	 *
	 * @param max_param_length パラメータ文字列最大長
	 */
	public static void setMAX_PARAM_LENGTH(int max_param_length) {
		MAX_PARAM_LENGTH = max_param_length;
	}

	/**
	 * @param paramsMap paramsMapの値をセットする。
	 */
	public void setParamMap(HashMap<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

	/**
	 * @param fileName fileNameの値をセットする。
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param sizeOverErrFlg sizeOverErrFlgの値をセットする。
	 */
	public void setSizeOverErrFlg(boolean sizeOverErrFlg) {
		this.sizeOverErrFlg = sizeOverErrFlg;
	}

	/**
	 * @param doFilterFlg doFilterFlgの値をセットする。
	 */
	public void setDoFilterFlg(boolean doFilterFlg) {
		this.doFilterFlg = doFilterFlg;
	}

	/**
	 * @param doPersentFilter doPersentFilterの値をセットする。
	 */
	public void setDoPersentFilter(boolean doPersentFilter) {
		this.doPersentFilter = doPersentFilter;
	}

}