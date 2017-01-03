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
package com.bee_wkspace.labee_fw.core.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import com.bee_wkspace.labee_fw.core.SystemConfigMng;

/**
 * フレームワーク用ビーンクラスの基底となるクラス。<br>
 * 主にビーンパラメータ変数の入力チェック結果情報を保持している。<br>
 * <br>
 * ビーンクラスを作成する場合は本クラスを継承する。
 *
 * @author ARTS Laboratory
 *
 * $Id: BaseBean.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class BaseBean implements BeanInterface, Serializable {

	private static final long serialVersionUID = -1145690311598095032L;
	/** 入力チェック結果(エラーが無かった事を示す定数) */
	public static final boolean INPUT_CHK_RESULT_SUCCESS = false;
	/** 入力チェック結果(エラーが有った事を示す定数) */
	public static final boolean INPUT_CHK_RESULT_ERROR = true;

	/** 入力チェックを行なう事を示す定数 */
	public static final boolean ENABLE_INPUT_CHK = true;
	/** 入力チェックを行なわない事を示す定数 */
	public static final boolean DISABLE_INPUT_CHK = false;

	/** 入力チェックエラーパラメータを格納するマップオブジェクト */
	protected HashMap<String, String> errParamNameMap;

	/** 入力チェック結果エラーメッセージ内容を格納するマップオブジェクト */
	protected HashMap<String, String> inputErrMsgMap;

	/**
	 * メッセージ言語ロケール<br>
	 * この値を参照してメッセージに出力する言語ロケールを判断する。
	 */
	protected Locale msgLocale = SystemConfigMng.getSystemLocale();

	/**
	 * 入力チェックエラーがあったかどうかのフラグ。<br>
	 * true = エラー有り<br>
	 * false = エラー無し<br>
	 */
	protected boolean inputChkErrFlg;

	/**
	 * 入力チェックを行うかどうかのフラグ<br>
	 * true = 行う<br>
	 * false = 行わない<br>
	 */
	protected boolean doInputChkFlg;

	/** 入力チェック セッターパラメータマップ */
	protected final HashMap<String, String> setterParamNameMap = null;

	/**
	 * コンストラクタ
	 */
	public BaseBean() {
		super();
		init();
	}

	/**
	 * 初期化。
	 */
	public void init() {
		if (errParamNameMap == null) {
			errParamNameMap = new HashMap<String, String>();
		}
		
		if (inputErrMsgMap == null) {
			inputErrMsgMap = new HashMap<String, String>();
		}
		inputChkErrFlg = INPUT_CHK_RESULT_SUCCESS;
		doInputChkFlg = ENABLE_INPUT_CHK;
	}

	/**
	 * エラー情報パラメータの初期化を行なう。
	 */
	public void initError() {
		if (errParamNameMap != null) {
			errParamNameMap.clear();
		}
		
		if (inputErrMsgMap != null) {
			inputErrMsgMap.clear();
		}
		inputChkErrFlg = INPUT_CHK_RESULT_SUCCESS;
	}

	/**
	 * 指定キーが入力エラーに登録されているかを返す。
	 *
	 * @param key パラメータキー名
	 * @return true = 入力エラー有り false = 入力エラー無し
	 * @see com.bee_wkspace.labee_fw.core.base.BeanInterface#isError(java.lang.String)
	 */
	@Override
	public boolean isError(String key) {
		boolean flg = false;
		String val = this.errParamNameMap.get(key);
		if (val != null && val.length() != 0) {
			flg = true;
		}
		return flg;
	}

	/**
	 * 入力チェックエラーパラメータ名を追加する。
	 *
	 * @param key パラメータキー名
	 */
	public void addErrParamName(String key) {
		errParamNameMap.put(key, key);
	}

	/**
	 * 入力チェックエラーメッセージを追加する。
	 *
	 * @param key パラメータキー名
	 * @param val メッセージ内容
	 */
	public void addErrMsg(String key, String val) {
		inputErrMsgMap.put(key, val);
	}

	/**
	 * 入力チェックエラーを追加する。
	 *
	 * @param key パラメータ名
	 * @param _message メッセージ
	 */
	public void addNewError(String key, String _message) {
		addErrParamName(key);
		addErrMsg(key, _message);
		inputChkErrFlg = INPUT_CHK_RESULT_ERROR;
	}

	/**
	 * セッターパラメータ名の存在チェックを行う。
	 *
	 * @param paramName パラメータ名
	 * @return 存在している場合はパラメータ名が返されえる。
	 */
	public String checkSetterParam(String paramName) {
		return setterParamNameMap.get(paramName);
	}

	/**
	 * errParamNameMapの値を返す。
	 *
	 * @return errParamNameMap
	 */
	public HashMap<String, String> getErrParamNameMap() {
		return errParamNameMap;
	}

	/**
	 * @param errParamNameMap errParamNameMapの値をセットする。
	 */
	public void setErrParamNameMap(HashMap<String, String> errParamNameMap) {
		this.errParamNameMap = errParamNameMap;
	}

	/**
	 * inputErrMsgMapの値を返す。
	 *
	 * @return inputErrMsgMap
	 */
	public HashMap<String, String> getInputErrMsgMap() {
		return inputErrMsgMap;
	}

	/**
	 * @param inputErrMsgMap inputErrMsgMapの値をセットする。
	 */
	public void setInputErrMsgMap(HashMap<String, String> inputErrMsgMap) {
		this.inputErrMsgMap = inputErrMsgMap;
	}

	/**
	 * msgLocaleの値を返す。
	 *
	 * @return msgLocale
	 */
	public Locale getMsgLocale() {
		return msgLocale;
	}

	/**
	 * @param msgLocale msgLocaleの値をセットする。
	 */
	public void setMsgLocale(Locale msgLocale) {
		this.msgLocale = msgLocale;
	}

	/**
	 * inputChkErrFlgの値を返す。
	 *
	 * @return inputChkErrFlg
	 */
	public boolean isInputChkErrFlg() {
		return inputChkErrFlg;
	}

	/**
	 * @param inputChkErrFlg inputChkErrFlgの値をセットする。
	 */
	public void setInputChkErrFlg(boolean inputChkErrFlg) {
		this.inputChkErrFlg = inputChkErrFlg;
	}

	/**
	 * doInputChkFlgの値を返す。
	 *
	 * @return doInputChkFlg
	 */
	public boolean isDoInputChkFlg() {
		return doInputChkFlg;
	}

	/**
	 * @param doInputChkFlg doInputChkFlgの値をセットする。
	 */
	public void setDoInputChkFlg(boolean doInputChkFlg) {
		this.doInputChkFlg = doInputChkFlg;
	}

}
