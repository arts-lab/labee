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

import org.w3c.dom.Document;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.core.Environment;

/**
 * レスポンスコンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: ResponseContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class ResponseContext implements Serializable {

	private static final long serialVersionUID = 5373683494759449948L;
	/** レスポンスタイプ WEB */
	public static final String RESPONSE_TYPE_WEB = "responseTypeWeb";
	/** レスポンスタイプ テキスト */
	public static final String RESPONSE_TYPE_TEXT = "responseTypeText";
	/** レスポンスタイプ CSV */
	public static final String RESPONSE_TYPE_CSV = "responseTypeCsv";
	/** レスポンスタイプ XML */
	public static final String RESPONSE_TYPE_XML = "responseTypeXml";
	/** レスポンスタイプ JSON */
	public static final String RESPONSE_TYPE_JSON = "responseTypeJson";
	/** レスポンスタイプ バイナリ */
	public static final String RESPONSE_TYPE_BINARY = "responseTypeBinary";

	/** アクセス環境情報 */
	protected Environment env = null;

	/** レスポンスタイプ */
	private String responseType = RESPONSE_TYPE_WEB;

	/** レスポンスフォワード遷移先 */
	private String forwardPath = "/";

	/** レスポンス出力文字コード */
	private String responseEncType = CommonDefine.ENCODE_UTF_8;

	/** レスポンスコンテンツタイプ */
	private String contentsType = null;

	/** レスポンスにバイナリを返す際の値 */
	private byte[] responseBinary = null;

	/** レスポンスにテキストを返す際の値 */
	private String responseText = null;

	/** レスポンスにCSVを返す際の値 */
	private String responseCsv = null;

	/** レスポンスにJsonを返す際の値 */
	private String responseJson = null;

	/** レスポンスにXMLを返す際の値 */
	private Document responseXML = null;

	/** ファイルダウンロード出力時のファイル名 */
	private String exportFileName = null;

	/** リダイレクト転送するかのフラグ */
	private boolean redirectFlg = false;

	/** ビジネスロジック処理を委譲するかのフラグ */
	private boolean delegateFlg = false;

	/** リダイレクト転送コンテキスト */
	private RedirectContext redirectContext = null;

	/** 画面委譲コンテキスト */
	private DelegateContext delegateContext = null;

	/** 追加拡張用オブジェクト */
	private Object additionalObject = null;

	/**
	 * コンストラクタ。
	 */
	public ResponseContext() {

	}

	/**
	 * envの値を返す。
	 *
	 * @return env
	 */
	public Environment getEnv() {
		return env;
	}

	/**
	 * @param env envの値をセットする。
	 */
	public void setEnv(Environment env) {
		this.env = env;
	}

	/**
	 * responseTypeの値を返す。
	 *
	 * @return responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @param responseType responseTypeの値をセットする。
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * forwardPathの値を返す。
	 *
	 * @return forwardPath
	 */
	public String getForwardPath() {
		return forwardPath;
	}

	/**
	 * @param forwardPath forwardPathの値をセットする。
	 */
	public void setForwardPath(String forwardPath) {
		this.forwardPath = forwardPath;
	}

	/**
	 * responseEncTypeの値を返す。
	 *
	 * @return responseEncType
	 */
	public String getResponseEncType() {
		return responseEncType;
	}

	/**
	 * @param responseEncType responseEncTypeの値をセットする。
	 */
	public void setResponseEncType(String responseEncType) {
		this.responseEncType = responseEncType;
	}

	/**
	 * contentsTypeの値を返す。
	 *
	 * @return contentsType
	 */
	public String getContentsType() {
		return contentsType;
	}

	/**
	 * @param contentsType contentsTypeの値をセットする。
	 */
	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}

	/**
	 * responseBinaryの値を返す。
	 *
	 * @return responseBinary
	 */
	public byte[] getResponseBinary() {
		return responseBinary;
	}

	/**
	 * @param responseBinary responseBinaryの値をセットする。
	 */
	public void setResponseBinary(byte[] responseBinary) {
		this.responseBinary = responseBinary;
	}

	/**
	 * responseTextの値を返す。
	 *
	 * @return responseText
	 */
	public String getResponseText() {
		return responseText;
	}

	/**
	 * @param responseText responseTextの値をセットする。
	 */
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	/**
	 * responseCsvの値を返す。
	 *
	 * @return responseCsv
	 */
	public String getResponseCsv() {
		return responseCsv;
	}

	/**
	 * @param responseCsv responseCsvの値をセットする。
	 */
	public void setResponseCsv(String responseCsv) {
		this.responseCsv = responseCsv;
	}

	/**
	 * responseJsonの値を返す。
	 *
	 * @return responseJson
	 */
	public String getResponseJson() {
		return responseJson;
	}

	/**
	 * @param responseJson responseJsonの値をセットする。
	 */
	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	/**
	 * responseXMLの値を返す。
	 *
	 * @return responseXML
	 */
	public Document getResponseXML() {
		return responseXML;
	}

	/**
	 * @param responseXML responseXMLの値をセットする。
	 */
	public void setResponseXML(Document responseXML) {
		this.responseXML = responseXML;
	}

	/**
	 * exportFileNameの値を返す。
	 *
	 * @return exportFileName
	 */
	public String getExportFileName() {
		return exportFileName;
	}

	/**
	 * @param exportFileName exportFileNameの値をセットする。
	 */
	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	/**
	 * redirectFlgの値を返す。
	 *
	 * @return redirectFlg
	 */
	public boolean isRedirectFlg() {
		return redirectFlg;
	}

	/**
	 * @param redirectFlg redirectFlgの値をセットする。
	 */
	public void setRedirectFlg(boolean redirectFlg) {
		this.redirectFlg = redirectFlg;
	}

	/**
	 * delegateFlgの値を返す。
	 *
	 * @return delegateFlg
	 */
	public boolean isDelegateFlg() {
		return delegateFlg;
	}

	/**
	 * @param delegateFlg delegateFlgの値をセットする。
	 */
	public void setDelegateFlg(boolean delegateFlg) {
		this.delegateFlg = delegateFlg;
	}

	/**
	 * redirectContextの値を返す。
	 *
	 * @return redirectContext
	 */
	public RedirectContext getRedirectContext() {
		return redirectContext;
	}

	/**
	 * @param redirectContext redirectContextの値をセットする。
	 */
	public void setRedirectContext(RedirectContext redirectContext) {
		this.redirectContext = redirectContext;
	}

	/**
	 * delegateContextの値を返す。
	 *
	 * @return delegateContext
	 */
	public DelegateContext getDelegateContext() {
		return delegateContext;
	}

	/**
	 * @param delegateContext delegateContextの値をセットする。
	 */
	public void setDelegateContext(DelegateContext delegateContext) {
		this.delegateContext = delegateContext;
	}

	/**
	 * additionalObjectの値を返す。
	 *
	 * @return additionalObject
	 */
	public Object getAdditionalObject() {
		return additionalObject;
	}

	/**
	 * @param additionalObject additionalObjectの値をセットする。
	 */
	public void setAdditionalObject(Object additionalObject) {
		this.additionalObject = additionalObject;
	}

}
