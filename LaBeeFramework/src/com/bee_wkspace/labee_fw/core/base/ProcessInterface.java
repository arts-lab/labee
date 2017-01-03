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
 * フレームワーク用実行プロセス基底インターフェース。
 *
 * @author ARTS Laboratory
 *
 * $Id: ProcessInterface.java 559 2016-08-14 12:24:00Z pjmember $
 */
public interface ProcessInterface {

	/**
	 * プロセス起動
	 *
	 * @throws Exception 例外
	 */
	void startProcess() throws Exception;

	/**
	 * プロセス再起動
	 *
	 * @throws Exception 例外
	 */
	void restartProcess() throws Exception;

	/**
	 * プロセス停止
	 *
	 * @throws Exception 例外
	 */
	void stopProcess() throws Exception;

	/**
	 * 実行プロセス名を返す。
	 *
	 * @return 実行プロセス名
	 */
	String getProcessName();

	/**
	 * プロセス状態を返す。
	 *
	 * @return プロセス状態
	 */
	String getState();
}
