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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.SysMsgCodeDefine;
import com.bee_wkspace.labee_fw.core.annotation.FwExeMethod;
import com.bee_wkspace.labee_fw.core.base.BlogicInterface;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * フレームワークビジネスロジック動的メソッド実行クラス。<br>
 * <br>
 * 呼びだすビジネスロジックのメインメソッドには<b>@FwExeMethod</b>アノテーションが付加されている必要がある。<br>
 * ビジネスロジックの<b>preProcess()</b>メソッド、メインメソッド、<b>postProcess()</b>メソッド、<b>
 * finallyProcess()</b>メソッド<br>
 * の順に処理を呼びだす。
 *
 * @author ARTS Laboratory
 *
 * $Id: BlogicExecutor.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class BlogicExecutor {

	/**
	 * フレームワーク用ビジネスロジックのメソッドを動的に実行する(引数なしで呼び出し)。<br>
	 * <br>
	 * 呼びだすビジネスロジックのメソッドには<b>@FwExeMethod</b>アノテーションが付加されている必要がある。<br>
	 * プリ処理、メイン処理、ポスト処理、ファイナリー処理の順に実行する。
	 *
	 * @param blogic ビジネスロジックインスタンス
	 * @param executeName 実行する処理(メソッド名)<br>
	 * @return レスポンスコンテキスト
	 * @throws Throwable 例外
	 */
	public static ResponseContext execute(BlogicInterface blogic, String executeName) throws Throwable {
		ResponseContext responseContext = null;
		boolean callOk = false;
		try {
			if (StringUtil.isNull(executeName)) {
				// メインメソッド
				Method method = blogic.getClass().getMethod(executeName);

				// アノテーションチェック
				Annotation[] annotations = method.getDeclaredAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation.annotationType().equals(FwExeMethod.class)) {
						callOk = true;
						break;
					}
				}

				if (callOk) {
					// プリ処理
					boolean preFlg = blogic.preProcess();

					// メイン処理
					if (preFlg) {
						responseContext = (ResponseContext) method.invoke(blogic);

						// ポスト処理
						if (blogic.isPostProcessFlg()) {
							blogic.postProcess();
						}
					} else {
						responseContext = blogic.getResponseContext();
					}

				} else {
					throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10001,
							SystemConfigMng.getSystemLocale(), executeName));
				}
			} else {
				throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10002,
						SystemConfigMng.getSystemLocale(), executeName));
			}

		} catch (NoSuchMethodException e) {
			callOk = false;
			throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10003,
					SystemConfigMng.getSystemLocale(), executeName
							+ CommonDefine.LINE_SEP
							+ e.getClass().getName()
							+ ":" + e.getLocalizedMessage()));
		} catch (FwException e) {
			throw e;

		} catch (Exception e) {
			if (e.getCause() instanceof FwException) {
				throw e.getCause();

			} else {
				throw e.getCause();
			}

		} finally {
			// ファイナリー処理
			if (callOk) {
				blogic.finallyProcess();
			}
		}
		return responseContext;
	}

	/**
	 * フレームワーク用ビジネスロジックのメソッドを動的に実行する(引数ありで呼び出し)。<br>
	 * <br>
	 * 呼びだすビジネスロジックのメソッドには<b>@FwExeMethod</b>アノテーションが付加されている必要があります。<br>
	 * プリ処理、メイン処理、ポスト処理、ファイナリー処理の順に実行する。
	 *
	 * @param blogic ビジネスロジックインスタンス
	 * @param executeName 実行する処理(メソッド名)<br>
	 * @param params 呼び出し処理に渡す引数パラメータ(最大3個まで設定可)
	 * @return レスポンスコンテキスト
	 * @throws Throwable 例外
	 */
	@SuppressWarnings("rawtypes")
	public static ResponseContext executeMethod(BlogicInterface blogic, String executeName, Object[] params)
			throws Throwable {
		ResponseContext responseContext = null;
		boolean callOk = false;
		try {
			if (StringUtil.isNull(executeName)) {
				Class[] clazz = null;
				int size = params.length;
				if (size != 0) {
					clazz = new Class[size];
					for (int i = 0; i < size; i++) {
						clazz[i] = params[i].getClass();
					}
				}

				// メインメソッド
				Method method = blogic.getClass().getMethod(executeName, clazz);

				// アノテーションチェック
				Annotation[] annotations = method.getDeclaredAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation.annotationType().equals(FwExeMethod.class)) {
						callOk = true;
						break;
					}
				}

				if (callOk) {
					// プリ処理
					boolean preFlg = blogic.preProcess();

					// メイン処理
					if (preFlg) {
						responseContext = (ResponseContext) method.invoke(blogic, params);
						// ポスト処理
						if (blogic.isPostProcessFlg()) {
							blogic.postProcess();
						}
					} else {
						responseContext = blogic.getResponseContext();
					}
				} else {
					throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10001,
							SystemConfigMng.getSystemLocale(), executeName));
				}
			} else {
				throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10002,
						SystemConfigMng.getSystemLocale(), executeName));
			}

		} catch (NoSuchMethodException e) {
			callOk = false;
			throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10003,
					SystemConfigMng.getSystemLocale(), executeName
							+ CommonDefine.LINE_SEP
							+ e.getClass().getName()
							+ ":" + e.getLocalizedMessage()));

		} catch (FwException e) {
			throw e;

		} catch (Exception e) {
			if (e.getCause() instanceof FwException) {
				throw e.getCause();

			} else {
				throw e.getCause();
			}

		} finally {
			// ファイナリー処理
			if (callOk) {
				blogic.finallyProcess();
			}
		}
		return responseContext;
	}

}
