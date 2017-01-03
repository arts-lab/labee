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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.bee_wkspace.labee_fw.app.base.AppInputCheckBean;
import com.bee_wkspace.labee_fw.core.base.BaseBean;

/**
 * フレームワーク用入力チェック用検証クラス。<br>
 * ビーンパラメータの各種入力チェックを行なうメソッドを定義している。<br>
 *
 * @author ARTS Laboratory
 *
 * $Id: Validater.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class Validater {

	/**
	 * 必須入力チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkNull(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {

				if (StringUtil.isNull(inpChkBean.getParamValue()) == false) {
					bean.addErrParamName(inpChkBean.getParamName());
					String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00001,
							bean.getMsgLocale(), inpChkBean.getViewName());
					bean.addErrMsg(inpChkBean.getParamName(), msg);
					bean.setInputChkErrFlg(true);
					inpChkBean.setInputErrorFlg(true);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 禁止文字チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkDbKinshi(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {
					String val = inpChkBean.getParamValue();
					int len = val.length();
					boolean flg = false;
					char c;
					for (int i = 0; i < len; i++) {
						c = val.charAt(i);
						if (c == '%') {
							flg = true;
							break;
						} else if (String.valueOf(c).equals("'")) {
							flg = true;
							break;
						} else if (c == '"') {
							flg = true;
							break;
						}
					}

					if (flg) {
						bean.addErrParamName(inpChkBean.getParamName());
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00002,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 数値入力チェック。(自然数)<br>
	 * マイナス値は許可しない。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checknaturalNumber(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {
					boolean flg = false;
					String val = inpChkBean.getParamValue();
					if (StringUtil.isNumber(val) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00003,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						flg = true;
					}

					if (flg == false) {
						int intVal = Integer.parseInt(val);
						if (intVal < 0) {
							String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00003,
									bean.getMsgLocale(), inpChkBean.getViewName());
							bean.addErrMsg(inpChkBean.getParamName(), msg);
							flg = true;
						}
					}

					if (flg) {
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 数値入力チェック。(整数)<br>
	 * マイナス値も許可する。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkNumber(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					if (StringUtil.isNumber(inpChkBean.getParamValue()) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00003,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 数値入力チェック。(数値上限、下限チェック付き)
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 * @param min 下限値
	 * @param max 最大値
	 */
	public static void checkNumberRange(AppInputCheckBean inpChkBean, int min, int max) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					boolean flg = false;
					String val = inpChkBean.getParamValue();
					if (StringUtil.isNumber(val) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00003,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						flg = true;
					}

					if (flg == false) {
						int intVal = Integer.parseInt(val);
						if (intVal < min || intVal > max) {
							String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00004,
									bean.getMsgLocale(), inpChkBean.getViewName(),
									Integer.toString(min), Integer.toString(max));
							bean.addErrMsg(inpChkBean.getParamName(), msg);
							flg = true;
						}
					}

					if (flg) {
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}

				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 最大文字数入力チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 * @param chkLen チェック最大文字数
	 */
	public static void checkMaxLength(AppInputCheckBean inpChkBean, int chkLen) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {
					String val = inpChkBean.getParamValue();
					val = val.trim();
					if (val.length() > chkLen) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00005,
								bean.getMsgLocale(), inpChkBean.getViewName(), Integer.toString(chkLen));
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 最小文字数入力チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 * @param chkLen チェック最小文字数
	 */
	public static void checkMinLength(AppInputCheckBean inpChkBean, int chkLen) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					String val = inpChkBean.getParamValue();
					val = val.trim();
					int len = val.length();
					if (len < chkLen) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00006,
								bean.getMsgLocale(), inpChkBean.getViewName(), Integer.toString(chkLen));
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 半角英数字チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkHankakuEisuu(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					if (StringUtil.isHankakuEisuu(inpChkBean.getParamValue()) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00007,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 半角英数字(記号含む)チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkHankakuEisuuKigou(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					if (StringUtil.isHankakuEisuuKigou(inpChkBean.getParamValue()) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00008,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 全角チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkZenkaku(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					if (StringUtil.isZenkaku(inpChkBean.getParamValue()) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00009,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 全角カナチェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkZenkakuKana(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					if (StringUtil.isZenkakuKana(inpChkBean.getParamValue()) == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00010,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Eメール書式チェック
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkEmailFormat(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {

					boolean flg = false;
					String[] chk = inpChkBean.getParamValue().split("@");
					if (chk != null) {
						if (StringUtil.isHankakuEisuuKigou(inpChkBean.getParamValue())) {
							if (chk.length == 2) {
								if (chk[1].indexOf(".") != -1) {
									flg = true;
								}
							}
						}
					}
					
					if (flg == false) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00013,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	
	/**
	 * 単語数チェック(1単語のみ)
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */
	public static void checkTangoNum(AppInputCheckBean inpChkBean) {
		try {
			BaseBean bean = inpChkBean.getAppBean();
			if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
				if (StringUtil.isNull(inpChkBean.getParamValue())) {
					String val = inpChkBean.getParamValue().trim().replaceAll("　", " ");

					int chk = val.indexOf(" ");
					if (chk != -1) {
						String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00011,
								bean.getMsgLocale(), inpChkBean.getViewName());
						bean.addErrMsg(inpChkBean.getParamName(), msg);
						bean.addErrParamName(inpChkBean.getParamName());
						bean.setInputChkErrFlg(true);
						inpChkBean.setInputErrorFlg(true);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 年月日存在チェック。
	 *
	 * @param inpChkBean 入力チェック用ビーン
	 */

	public static void checkExsistDate(AppInputCheckBean inpChkBean) {
		BaseBean bean = inpChkBean.getAppBean();
		if (bean.isDoInputChkFlg() && inpChkBean.isInputErrorFlg() == false) {
			if (StringUtil.isNull(inpChkBean.getParamValue())) {

				String val = StringUtil.replaceText(inpChkBean.getParamValue(), "/", "");
				try {
					DateFormat format = new SimpleDateFormat("yyyyMMdd");
					format.setLenient(false);
					format.parse(val);

				} catch (ParseException e) {
					String msg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00012,
							bean.getMsgLocale(), inpChkBean.getViewName());
					bean.addErrMsg(inpChkBean.getParamName(), msg);
					bean.addErrParamName(inpChkBean.getParamName());
					bean.setInputChkErrFlg(true);
					inpChkBean.setInputErrorFlg(true);
				}
			}
		}
	}
}
