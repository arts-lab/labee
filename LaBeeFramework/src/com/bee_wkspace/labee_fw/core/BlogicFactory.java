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
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.SysMsgCodeDefine;
import com.bee_wkspace.labee_fw.core.annotation.FwBlogic;
import com.bee_wkspace.labee_fw.core.base.BaseBean;
import com.bee_wkspace.labee_fw.core.base.BlogicInterface;
import com.bee_wkspace.labee_fw.core.context.FileUploadContext;
import com.bee_wkspace.labee_fw.core.context.ResponseContext;
import com.bee_wkspace.labee_fw.exception.FwException;

/**
 * フレームワークビジネスロジック動的生成クラス。<br>
 * <br>
 * 生成するビジネスロジックには<b>@FwBlogic</b>アノテーションが付加されている必要がある。<br>
 * 生成するビジネスロジック名に指定されるターゲット値はビジネスロジック格納のトップパス(コンフィグファイルで設定定義)を除いた、<br>
 * パッケージパス + ビジネスロジッククラス名から <b>Blogic</b>部を除いた文字列<br>
 * (例) <b>com.blogic.sample.TestBlogic.class</b> というフルパッケージパスのクラスの場合は<br>
 * ビジネスロジック格納のトップパスが com.bloic に設定の場合、targetに設定する値は<br>
 * <b>sample.Test</b>となる。
 *
 *
 * @author ARTS Laboratory
 *
 * $Id: BlogicFactory.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class BlogicFactory {

	/** 呼び出し実行対象パラメータに連結する装飾名 */
	public static final String BLOCIC_CONCAT_NAME = "Blogic";

	/** セッションオブジェクト */
	protected HttpSession session;

	/** アクセス環境情報 */
	protected Environment env;

	/** フォームパラメータリーダー */
	protected MultiPartFormReader formReader;

	/** フォームパラメータ情報格納マップ */
	protected HashMap<String, String> paramsMap;

	/** レスポンスコンテキスト */
	protected ResponseContext responseContext;

	/**
	 * コンストラクタ。
	 *
	 * @param _session セッションオブジェクト
	 * @param _formReader フォームパラメータリーダー
	 * @param _paramsMap フォームパラメータ情報格納マップ
	 * @param _env アクセス環境情報
	 */
	public BlogicFactory(HttpSession _session, MultiPartFormReader _formReader, HashMap<String, String> _paramsMap,
			Environment _env) {
		this.session = _session;
		this.formReader = _formReader;
		this.paramsMap = _paramsMap;
		this.env = _env;
	}

	/**
	 * パラメータで指定したビジネスロジッククラスを動的に生成してメソッド処理を実行する。
	 *
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @return ビジネスロジックインスタンス
	 * @throws Throwable 例外
	 */
	public BlogicInterface executeBlogic(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		BlogicInterface blogic = null;
		env.setTargetName(paramsMap.get(CommonDefine.TARGET));
		env.setExecuteName(paramsMap.get(CommonDefine.EXECUTE));

		String param1 = paramsMap.get(CommonDefine.PARAM1);
		String param2 = paramsMap.get(CommonDefine.PARAM2);
		String param3 = paramsMap.get(CommonDefine.PARAM3);
		String[] params = null;

		if (StringUtil.isNull(param1) && StringUtil.isNull(param2) == false && StringUtil.isNull(param3) == false) {
			params = new String[1];
			params[0] = StringUtil.nullToSpace(paramsMap.get(CommonDefine.PARAM1));

		} else if (StringUtil.isNull(param1) && StringUtil.isNull(param2) && StringUtil.isNull(param3) == false) {
			params = new String[2];
			params[0] = StringUtil.nullToSpace(paramsMap.get(CommonDefine.PARAM1));
			params[1] = StringUtil.nullToSpace(paramsMap.get(CommonDefine.PARAM2));

		} else if (StringUtil.isNull(param1) && StringUtil.isNull(param2) && StringUtil.isNull(param3)) {
			params = new String[3];
			params[0] = StringUtil.nullToSpace(paramsMap.get(CommonDefine.PARAM1));
			params[1] = StringUtil.nullToSpace(paramsMap.get(CommonDefine.PARAM2));
			params[2] = StringUtil.nullToSpace(paramsMap.get(CommonDefine.PARAM3));
		}

		// 動的にビジネスロジッククラスのインスタンスを生成
		blogic = instanceBlogic(request, session, env);
		blogic.setParamsMap(paramsMap);

		// アップロードファイル情報セット
		FileUploadContext fileupliadContext = new FileUploadContext();
		fileupliadContext.setFileName(formReader.getFileName());
		byte[] fileByte = formReader.getFileObjByte();
		if (fileByte != null) {
			fileupliadContext.setUploadFileByte(fileByte);
			fileupliadContext.setFileSize(fileByte.length);
		}
		blogic.setUploadFileContext(fileupliadContext);

		// ターゲットメソッドを動的に実行
		if (params == null) {
			// 引数なし
			responseContext = BlogicExecutor.execute(blogic, env.getExecuteName());
		} else {
			try {
				// 引数あり
				responseContext = BlogicExecutor.executeMethod(blogic, env.getExecuteName(), params);

			} catch (NoSuchMethodException e) {
				// 引数なしで再実行
				responseContext = BlogicExecutor.execute(blogic, env.getExecuteName());
			}
		}

		// レスポンスに追加クッキーをセット
		ArrayList<Cookie> responseCookieList = blogic.getAddCokieList();
		if (responseCookieList != null) {
			for (Cookie cookie : responseCookieList) {
				response.addCookie(cookie);
			}
		}
		return blogic;
	}

	/**
	 * ビジネスロジックをインスタンス化して返す。<br>
	 * <br>
	 * 生成するビジネスロジック名に指定される値はビジネスロジック格納のトップパスを除いた、<br>
	 * 以降パッケージパス + ビジネスロジッククラス名から <b>Blogic</b>部を除いた文字列<br>
	 * (例) <b>com.blogic.sample.TestBlogic.class</b> というフルパッケージパスで<br>
	 * ビジネスロジック格納のトップパスが com.bloic の場合(設定ファイルで定義)はtargetに設定する値は<br>
	 * <b>sample.Test</b>となる。
	 *
	 * @param request リクエスト情報
	 * @param session セッションオブジェクト
	 * @param env アクセス環境情報
	 *
	 * @return 生成されたビジネスロジックオブジェクトインターフェイス
	 * @throws Throwable 例外
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static BlogicInterface instanceBlogic(HttpServletRequest request, HttpSession session, Environment env)
			throws Throwable {
		BlogicInterface blogic = null;
		try {
			boolean callOk = false;

			// アノテーションチェック
			Class targetClass = Class
					.forName(SystemConfigMng.getBlogicPackage() + "." + env.getTargetName() + BLOCIC_CONCAT_NAME);
			Annotation[] annotations = targetClass.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(FwBlogic.class)) {
					callOk = true;
					break;
				}
			}

			if (callOk) {
				FwBlogic fwAnnotation = (FwBlogic) targetClass.getAnnotation(FwBlogic.class);
				Constructor ct = targetClass.getConstructor();

				// ビジネスロジックを生成してコンストラクタを実行
				blogic = (BlogicInterface) ct.newInstance();
				blogic.setSession(session);
				blogic.setEnv(env);
				blogic.setCookies(request.getCookies());

				// ビーン生成
				String beanName = null;
				Type targetType = targetClass.getGenericSuperclass();

				// ビーン総称型が設定してある場合
				if (targetType.getTypeName().indexOf("<") != -1 && targetType.getTypeName().indexOf(">") != -1) {
					Type[] types = ((ParameterizedType) targetType).getActualTypeArguments();
					beanName = types[0].getTypeName();

				} else {
					// ビーン総称型が設定してない場合は親クラスの総称型をチェック
					Class parentClass = targetClass.getSuperclass();
					Type parentType = null;
					boolean find = false;
					// 最大5階層上まで検索
					for (int i = 0; i < 5; i++) {
						parentType = parentClass.getGenericSuperclass();
						if (parentType.getTypeName().indexOf("<") != -1 && parentType.getTypeName().indexOf(">") != -1) {
							find = true;
							break;
						}
						parentClass = parentClass.getSuperclass();
					}

					if (find) {
						Type[] types = ((ParameterizedType) parentType).getActualTypeArguments();
						beanName = types[0].getTypeName();
					} else {
						// 最終的に総称型が無い場合は基本ビーンを設定
						beanName = BaseBean.class.getName();
					}
				}
				blogic.createBean(beanName, fwAnnotation.beanReuse());

				// 初期化メソッド実行
				blogic.init();

			} else {
				throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10010,
						SystemConfigMng.getSystemLocale(), env.getTargetName()));
			}
		} catch (FwException e) {
			throw e;
		} catch (Exception e) {
			if (e.getCause() instanceof FwException) {
				throw e.getCause();

			} else {
				throw new FwException(MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS10011,
						SystemConfigMng.getSystemLocale(), env.getTargetName()
								+ CommonDefine.LINE_SEP
								+ e.getClass().getName() + ":" + e.getLocalizedMessage()
						));
			}
		}
		return blogic;
	}

	/**
	 * レスポンスコンテキストを返す。
	 *
	 * @return レスポンスコンテキスト
	 */
	public ResponseContext getResponseContext() {
		return responseContext;
	}

}
