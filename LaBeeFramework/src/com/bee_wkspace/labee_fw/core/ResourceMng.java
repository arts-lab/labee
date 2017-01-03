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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.jar.JarFile;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.JarUtil;
import com.bee_wkspace.labee_fw.common.LogWriter;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.core.context.DivisionContext;
import com.bee_wkspace.labee_fw.core.context.MessageCodeContext;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;
import com.bee_wkspace.labee_fw.core.context.WordLabelContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * リソース設定情報管理クラス。<br>
 * <br>
 * 画面情報、区分値情報、メッセ―ジ情報の各リソースファイルを読み込みメモリ上に静的格納する。<br>
 * 各リソースファイルのタイムスタンプが更新された場合に再読み込み設定が行われる。<br>
 * 各リソースファイルは<b>/config</b>フォルダ内に格納されている事が必要である。<br>
 * ・DivisionDefine.csv (区分値情報定義ファイル)<br>
 * ・ScreenDefine.csv (画面情報定義ファイル)<br>
 * ・SysMessageDefine.csv (システムメッセージ定義ファイル)<br>
 * ・WordLabelDefine.csv (ワードラベル情報定義ファイル)<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: ResourceMng.java 561 2016-08-14 13:36:29Z pjmember $
 */
public class ResourceMng {

	/** 画面情報を格納するマップ */
	private static HashMap<String, ScreenContext> screenMap = new HashMap<String, ScreenContext>();

	/** 区分値情報を格納するマップ */
	private static HashMap<String, LinkedHashMap<String, DivisionContext>> divMap = new HashMap<String, LinkedHashMap<String, DivisionContext>>();

	/** メッセージ情報を格納するマップ */
	private static HashMap<String, MessageCodeContext> messageCodeMap = new HashMap<String, MessageCodeContext>();

	/** ワードラベル情報を格納するマップ */
	private static HashMap<String, HashMap<String, WordLabelContext>> wordLabelCodeMap = new HashMap<String, HashMap<String, WordLabelContext>>();

	/** リソースファイルの更新日チェック用マップ */
	private static HashMap<String, Date> updateMap = new HashMap<String, Date>();

	/** 画面情報CSVファイル名 */
	private static final String SCREEN_DEFINE_FILE = "ScreenDefine.csv";

	/** 区分値情報CSVファイル名 */
	private static final String DIVISION_FILE = "DivisionDefine.csv";

	/** メッセージCSVファイル名 */
	private static final String MSG_DEFINE_FILE = "MessageDefine.csv";

	/** システムメッセージCSVファイル名 */
	private static final String SYS_MSG_DEFINE_FILE = "SysMessageDefine.csv";

	/** ワードラベルCSVファイル名 */
	private static final String WORD_LABEL_DEFINE_FILE = "WordLabelDefine.csv";

	/**
	 * リソース設定情報読み込み。
	 *
	 * @param docBasePath 基点パス
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト
	 * @throws Exception 例外
	 */
	public static void loadResource(String docBasePath, Object classLoaderMakedObject) throws Exception {

		// 画面設定読み込み
		loadScreenData(classLoaderMakedObject);

		// 区分値設定読み込み
		loadDivData(classLoaderMakedObject);

		// システムメッセージ設定読み込み
		loadSysMessageData(docBasePath);

		// メッセージ設定読み込み
		loadMessageData(classLoaderMakedObject);

		// ワードラベル設定読み込み
		loadWordLabelData(classLoaderMakedObject);
	}

	/**
	 * 画面情報コンテキストを返す。
	 *
	 * @param screenId 画面ID
	 * @return 画面情報コンテキスト
	 */
	public static ScreenContext getScreenContext(String screenId) {
		String[] data = screenId.split("\\.");
		if (data.length > 1) {
			screenId = data[data.length - 1];
		}
		return screenMap.get(screenId);
	}

	/**
	 * 区分値情報コンテキストマップを返す。
	 *
	 * @param divId 区分値ID
	 * @return 区分値情報コンテキストマップ
	 */
	public static LinkedHashMap<String, DivisionContext> getDivMap(String divId) {
		return divMap.get(divId);
	}

	/**
	 * 区分値情報コンテキストを返す。
	 *
	 * @param divId 区分値ID
	 * @param itemNo 区分アイテムNo
	 * @return 区分値情報コンテキスト
	 */
	public static DivisionContext getDivContext(String divId, String itemNo) {
		DivisionContext divContext = null;
		LinkedHashMap<String, DivisionContext> map = divMap.get(divId);
		if (map != null) {
			divContext = map.get(itemNo);
		}
		return divContext;
	}

	/**
	 * 区分値を引数に区分値情報コンテキストを返す。
	 *
	 * @param divId 区分値ID
	 * @param itemValue 区分値
	 * @return 区分値情報コンテキスト
	 */
	public static DivisionContext getDivContextFromValue(String divId, String itemValue) {
		LinkedHashMap<String, DivisionContext> map = divMap.get(divId);
		if (map != null) {
			for (DivisionContext divContext : map.values()) {
				if (divContext.getItemValue().equals(itemValue)) {
					return divContext;
				}
			}
		}
		return null;
	}

	/**
	 * メッセージ情報コンテキストを返す。
	 *
	 * @param mesgCode メッセージコード
	 * @return メッセージ情報コンテキスト
	 */
	public static MessageCodeContext getMessageContext(String mesgCode) {
		return messageCodeMap.get(mesgCode);
	}

	/**
	 * ワードラベル情報コンテキストを返す。
	 *
	 * @param division ワードラベル区分
	 * @param wordLabelCode ワードラベル
	 * @return ワードラベル情報コンテキスト
	 */
	public static WordLabelContext getLabelWordContext(String division, String wordLabelCode) {
		HashMap<String, WordLabelContext> innnerMap = wordLabelCodeMap.get(division);
		if (innnerMap != null) {
			return innnerMap.get(wordLabelCode);
		} else {
			return null;
		}
	}

	/**
	 * ワードラベル情報コンテキストを返す。
	 *
	 * @param wordLabelCode ワードラベル
	 * @return ワードラベル情報コンテキスト
	 */
	public static WordLabelContext getLabelWordContext(String wordLabelCode) {
		return getLabelWordContext("global", wordLabelCode);

	}

	/**
	 * 画面設定情報読み込み設定。
	 *
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト
	 * @throws Exception 例外
	 */
	private static void loadScreenData(Object classLoaderMakedObject) throws Exception {
		try {
			File screenDataFile = SystemUtil.getFileResource(classLoaderMakedObject, "/config/" + SCREEN_DEFINE_FILE);
			if (screenDataFile == null) {
				throw new FwException("画面情報設定ファイルが見つかりません。");
			}

			Date checkDate = updateMap.get("SCREEN_UPDATE");
			if (checkDate == null || screenDataFile.lastModified() > checkDate.getTime()) {
				screenMap.clear();

				// 画面設定CSV情報読み込み
				String screenData = SystemUtil.loadFile(screenDataFile, SystemConfigMng.getResourceEncCode());

				String[] lines = screenData.split(CommonDefine.LINE_SEP);
				for (String line : lines) {
					if (StringUtil.isNull(line) == false) {
						continue;
					}
					line = line.trim();
					if (line.startsWith("#")) {
						continue;
					}

					ScreenContext context = new ScreenContext();
					int idx = 1;
					String[] csv = line.split(CommonDefine.COMMA);
					for (String column : csv) {
						column = column.trim();
						switch (idx) {
						case (1):
							context.setScreenId(column);
							break;
						case (2):
							context.setScreenName(column);
							break;
						case (3):
							context.setLabelDiv(column);
							break;
						case (4):
							context.setLabelCd(column);
							break;
						case (5):
							if (column.toLowerCase().equals("true")) {
								context.setSslFlg(true);
							} else {
								context.setSslFlg(false);
							}
							break;
						case (6):
							context.setJspPC(column);
							break;
						case (7):
							context.setJspSphone(column);
							break;
						case (8):
							context.setJspTablet(column);
							break;
						case (9):
							context.setAddScriptList(StringUtil.trimStringArray(column.split(";")));
							break;
						case (10):
							context.setAddCssList(StringUtil.trimStringArray(column.split(";")));
							break;
						case (11):
							context.setOption1(column);
							break;
						case (12):
							context.setOption2(column);
							break;
						case (13):
							context.setOption3(column);
							break;
						case (14):
							context.setOption4(column);
							break;

						default:
							//
							break;
						}

						idx++;
					}
					screenMap.put(context.getScreenId(), context);
				}
				updateMap.put("SCREEN_UPDATE", new Date());

			}
		} catch (Exception e) {
			LogWriter.error(e);
			throw new FwException("ResourceManager init Error of ScreenData", e);
		}
	}

	/**
	 * 区分値 設定情報読み込み設定。
	 *
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト
	 * @throws Exception 例外
	 */
	private static void loadDivData(Object classLoaderMakedObject) throws Exception {
		try {
			File divDataFile = SystemUtil.getFileResource(classLoaderMakedObject, "/config/" + DIVISION_FILE);
			if (divDataFile != null) {

				Date checkDate = updateMap.get("DIV_UPDATE");
				if (checkDate == null || divDataFile.lastModified() > checkDate.getTime()) {

					// 区分値設定CSV情報読み込み
					String divData = SystemUtil.loadFile(divDataFile, SystemConfigMng.getResourceEncCode());

					String[] lines = divData.split(CommonDefine.LINE_SEP);
					ArrayList<String> localeList = new ArrayList<String>();
					divMap.clear();

					for (String line : lines) {
						if (StringUtil.isNull(line) == false) {
							continue;
						}
						line = line.trim();
						if (line.startsWith("#")) {
							continue;
						}

						// ヘッダの場合
						if (line.trim().toUpperCase().startsWith("$")) {
							String[] csv = line.split(CommonDefine.COMMA);

							// メッセージロケール言語取得
							for (String head : csv) {
								localeList.add(head.trim().toLowerCase());
							}
							continue;

							// データ部の場合
						} else {
							DivisionContext context = new DivisionContext();
							String[] csv = line.split(CommonDefine.COMMA);
							for (int i = 0; i < csv.length; i++) {
								if (i == 0) {
									context.setDivId(csv[0].trim());
								} else if (i == 1) {
									context.setItemNo(csv[1].trim());

								} else if (i == 2) {
									context.setItemValue(csv[2].trim());

								} else {
									context.setItemView(localeList.get(i), csv[i].trim());
								}
							}

							LinkedHashMap<String, DivisionContext> map = divMap.get(context.getDivId());
							if (map == null) {
								map = new LinkedHashMap<String, DivisionContext>();
								divMap.put(context.getDivId(), map);
							}

							map.put(context.getItemNo(), context);
						}
					}
					updateMap.put("DIV_UPDATE", new Date());

				}
			}
		} catch (Exception e) {
			LogWriter.error(e);
			throw new FwException("ResourceManager init Error of DivData", e);
		}
	}

	/**
	 * システムメッセージ 設定情報読み込み設定。
	 *
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト
	 * @throws Exception 例外
	 */
	private static void loadSysMessageData(String docBasePath) throws Exception {
		File sysMsgDataFile = null;
		JarFile libJarFile = JarUtil.getLaBeeLibJarFile(docBasePath);
		if (libJarFile != null) {
			// フレームワーク内のコンフィグファイル一覧を取得
			List<String> libFileList = JarUtil.getJarInFileList(libJarFile, "META-INF/config/");
			for (String path : libFileList) {
				File file = JarUtil.getJarInFile(libJarFile, path, "/");
				// システムメッセージファイルを取得
				if (file.getName().toLowerCase().equals(SYS_MSG_DEFINE_FILE.toLowerCase())) {
					sysMsgDataFile = file;
					break;
				}
			}
		}
		loadMessageDataWork(sysMsgDataFile, "SYS_MSG_UPDATE", CommonDefine.ENCODE_UTF_8);
	}

	/**
	 * メッセージ 設定情報読み込み設定。
	 *
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト
	 * @throws Exception 例外
	 */
	private static void loadMessageData(Object classLoaderMakedObject) throws Exception {
		File msgDataFile = SystemUtil.getFileResource(classLoaderMakedObject, "/config/" + MSG_DEFINE_FILE);
		loadMessageDataWork(msgDataFile, "MSG_UPDATE", SystemConfigMng.getResourceEncCode());
	}

	/**
	 * メッセージ 設定情報読み込み設定ワーク。
	 *
	 * @param msgFile メッセージファイルオブジェクト
	 * @param updateName アップデートマップ識別名
	 * @param encCode	文字コード
	 * @throws Exception 例外
	 */
	private static void loadMessageDataWork(File msgFile, String updateName, String encCode) throws Exception {
		try {
			if (msgFile != null) {
				Date checkDate = updateMap.get(updateName);
				if (checkDate == null || msgFile.lastModified() > checkDate.getTime()) {
					String sysMesData = SystemUtil.loadFile(msgFile, encCode);
					String[] lines = sysMesData.split(CommonDefine.LINE_SEP);
					ArrayList<String> localeList = new ArrayList<String>();

					for (String line : lines) {
						if (StringUtil.isNull(line) == false) {
							continue;
						}
						line = line.trim();

						if (line.startsWith("#")) {
							continue;
						}

						// ヘッダの場合
						if (line.trim().toUpperCase().startsWith("$")) {
							String[] csv = line.split(CommonDefine.COMMA);

							// メッセージロケール言語取得
							for (String head : csv) {
								localeList.add(head.trim().toLowerCase());
							}
							continue;

							// データ部の場合
						} else {

							MessageCodeContext context = null;
							String[] csv = line.split(CommonDefine.COMMA);
							for (int i = 0; i < csv.length; i++) {
								if (i == 0) {
									context = new MessageCodeContext(csv[0].trim());
								} else {
									context.setMessage(localeList.get(i), csv[i].trim());
								}
							}
							messageCodeMap.put(context.getMsgCode(), context);
						}
					}
					updateMap.put(updateName, new Date());

				}
			}
		} catch (Exception e) {
			LogWriter.error(e);
			throw new FwException("ResourceManager init Error of MessageData", e);
		}
	}

	/**
	 * ワードラベル設定情報読み込み設定。
	 *
	 * @param classLoaderMakedObject クラスローダ生成オブジェクト
	 * @throws Exception 例外
	 */
	private static void loadWordLabelData(Object classLoaderMakedObject) throws Exception {
		try {
			File wordLabelDataFile = SystemUtil.getFileResource(classLoaderMakedObject, "/config/"
					+ WORD_LABEL_DEFINE_FILE);
			if (wordLabelDataFile != null) {

				Date checkDate = updateMap.get("WORD_LABEL_UPDATE");
				if (checkDate == null || wordLabelDataFile.lastModified() > checkDate.getTime()) {
					String wordLabelData = SystemUtil.loadFile(wordLabelDataFile, SystemConfigMng.getResourceEncCode());

					String[] lines = wordLabelData.split(CommonDefine.LINE_SEP);
					ArrayList<String> localeList = new ArrayList<String>();
					wordLabelCodeMap.clear();

					for (String line : lines) {
						if (StringUtil.isNull(line) == false) {
							continue;
						}
						line = line.trim();

						if (line.startsWith("#")) {
							continue;
						}

						// ヘッダの場合
						if (line.trim().toUpperCase().startsWith("$")) {
							String[] csv = line.split(CommonDefine.COMMA);

							// メッセージロケール言語取得
							for (String head : csv) {
								localeList.add(head.trim().toLowerCase());
							}
							continue;

							// データ部の場合
						} else {

							WordLabelContext context = null;
							String[] csv = line.split(CommonDefine.COMMA);
							for (int i = 0; i < csv.length; i++) {
								if (i == 0) {
									context = new WordLabelContext(csv[0].trim());

								} else if (i == 1) {
									if (StringUtil.isNull(csv[1].trim())) {
										context.setDivision(csv[1].trim());
									} else {
										context.setDivision("global");
									}

								} else {
									context.setWordLabel(localeList.get(i), csv[i].trim());
								}
							}

							HashMap<String, WordLabelContext> innnerMap = wordLabelCodeMap.get(context.getDivision());
							if (innnerMap == null) {
								innnerMap = new HashMap<String, WordLabelContext>();
								wordLabelCodeMap.put(context.getDivision(), innnerMap);
							}

							innnerMap.put(context.getWordLabelCode(), context);
						}
					}
					updateMap.put("WORD_LABEL_UPDATE", new Date());

				}
			}
		} catch (Exception e) {
			LogWriter.error(e);
			throw new FwException("ResourceManager init Error of wordLabelData", e);
		}
	}

}
