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
import java.io.UnsupportedEncodingException;

/**
 * ファイルアップロード情報コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id:
 */
public class FileUploadContext implements Serializable {

	private static final long serialVersionUID = -572628637570235253L;

	/** アップロードファイルのバイトデータ */
	private byte[] uploadFileByte;

	/** アップロードファイルのファイル名 */
	private String fileName;

	/** アップロードファイルのファイルサイズ */
	private int fileSize;

	/**
	 * アップロードファイル内容を文字列で返す。
	 *
	 * @return ファイル内容
	 * @throws UnsupportedEncodingException 例外
	 */
	public String getUploadString() throws UnsupportedEncodingException {
		if (uploadFileByte != null) {
			return new String(uploadFileByte, "JISAutoDetect");
		} else {
			return null;
		}
	}

	/**
	 * @return uploadFileByte
	 */
	public byte[] getUploadFileByte() {
		return uploadFileByte;
	}

	/**
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return fileSize
	 */
	public int getFileSize() {
		return fileSize;
	}

	/**
	 * @param uploadFileByte セットする uploadFileByte
	 */
	public void setUploadFileByte(byte[] uploadFileByte) {
		this.uploadFileByte = uploadFileByte;
	}

	/**
	 * @param fileName セットする fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param fileSize セットする fileSize
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

}
