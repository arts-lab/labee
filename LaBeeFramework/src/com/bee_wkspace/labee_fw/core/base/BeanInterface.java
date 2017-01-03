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


/**
 * フレームワーク用ビーン基底インターフェース。
 *
 * @author ARTS Laboratory
 *
 * $Id: BeanInterface.java 554 2016-08-12 21:19:00Z pjmember $
 */
public interface BeanInterface {

	/**
	 * 指定キーが入力エラーに登録されているかを返す。
	 *
	 * @param key パラメータキー名
	 * @return true = 入力エラー有り false = 入力エラー無し
	 */
	boolean isError(String key);
}
