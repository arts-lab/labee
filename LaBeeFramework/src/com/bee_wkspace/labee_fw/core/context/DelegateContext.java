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

/**
 * 画面委譲コンテキスト。
 *
 * @author ARTS Laboratory
 *
 * $Id: DelegateContext.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class DelegateContext implements Serializable {

	private static final long serialVersionUID = -4043869123232281289L;

	/** ターゲット */
	private String target = null;

	/** 実行メソッド */
	private String execute = null;

	/** パラメータ1 */
	private String param1 = null;

	/** パラメータ2 */
	private String param2 = null;

	/** パラメータ3 */
	private String param3 = null;

	/**
	 * targetの値を返す。
	 *
	 * @return target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target targetの値をセットする。
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * executeの値を返す。
	 *
	 * @return execute
	 */
	public String getExecute() {
		return execute;
	}

	/**
	 * @param execute executeの値をセットする。
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}

	/**
	 * param1の値を返す。
	 *
	 * @return param1
	 */
	public String getParam1() {
		return param1;
	}

	/**
	 * @param param1 param1の値をセットする。
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * param2の値を返す。
	 *
	 * @return param2
	 */
	public String getParam2() {
		return param2;
	}

	/**
	 * @param param2 param2の値をセットする。
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * param3の値を返す。
	 *
	 * @return param3
	 */
	public String getParam3() {
		return param3;
	}

	/**
	 * @param param3 param3の値をセットする。
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

}
