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
package com.bee_wkspace.labee_fw.app.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import com.bee_wkspace.labee_fw.app.base.AppBaseBean;
import com.bee_wkspace.labee_fw.app.base.AppEnvironmentBean;
import com.bee_wkspace.labee_fw.app.context.AppMessageDialogContext;
import com.bee_wkspace.labee_fw.app.context.AppPopupContext;
import com.bee_wkspace.labee_fw.app.context.AppPopupToParentContext;
import com.bee_wkspace.labee_fw.common.CommonDefine;
import com.bee_wkspace.labee_fw.common.MessageLabelUtil;
import com.bee_wkspace.labee_fw.common.StringUtil;
import com.bee_wkspace.labee_fw.common.SysMsgCodeDefine;
import com.bee_wkspace.labee_fw.core.Environment;
import com.bee_wkspace.labee_fw.core.ResourceMng;
import com.bee_wkspace.labee_fw.core.SystemConfigMng;
import com.bee_wkspace.labee_fw.core.context.ScreenContext;

/**
 * JSPヘッドパラメータ設定タグ。
 *
 * @author ARTS Laboratory
 *
 * $Id: LaBeeHeaderTag.java 578 2016-08-16 22:48:45Z pjmember $
 */
public class LaBeeHeaderTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 8908084445518417347L;
	/** リクエストスコープマップ */
	protected Map<String, Object> requestScopeMap;

	/**
	 * タグ終了時の初期化
	 */
	@Override
	public void doFinally() {
		//
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	@Override
	public void doCatch(Throwable arg0) throws Throwable {
		throw arg0;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(makeJspHeadParam());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	private String makeJspHeadParam() {
		StringBuilder buf = new StringBuilder();
		AppBaseBean bean = (AppBaseBean) requestScopeMap.get(CommonDefine.BEAN);
		AppEnvironmentBean envBean = (AppEnvironmentBean) requestScopeMap.get(CommonDefine.ENV_BEAN);
		Environment env = envBean.getEnv();

		// タイトルタグ定義
		ScreenContext screenContext = bean.getScreenContext();

		// ブラウザタイトル表示名設定
		if (StringUtil.isNull(screenContext.getScreenName()) == false) {
			if (StringUtil.isNull(screenContext.getLabelCd())) {
				screenContext.setViewTitle(MessageLabelUtil.getWordLabel(screenContext.getLabelCd(),
						screenContext.getLabelDiv(), bean.getMsgLocale()));

			} else {
				screenContext.setViewTitle(screenContext.getScreenName());
			}
		}		
		
		buf.append("<title>" + screenContext.getViewTitle() + "</title>");
		buf.append(CommonDefine.LINE_SEP);

		// プリロードデフォルト スタイルシートリンク 定義
		String[] cssList = SystemConfigMng.getPreLoadCssList();
		if (cssList != null) {
			for (String css : cssList) {
				buf.append("<link rel=\"stylesheet\" href=\"" + css + "\" type=\"text/css\">");
				buf.append(CommonDefine.LINE_SEP);
			}
		}

		// 画面個別 タイルシートリンク 定義
		String[] addCssList = screenContext.getAddCssList();
		if (addCssList != null) {
			for (String css : addCssList) {
				buf.append("<link rel=\"stylesheet\" href=\"" + css + "\" type=\"text/css\">");
				buf.append(CommonDefine.LINE_SEP);
			}
		}

		// プリロードデフォルト JavaScriptリンク 定義
		String[] scriptList = null;

		// スマホ、タブレットの場合
		if (env.isSmtPhoneFlg() || env.isTabletPadFlg()) {
			scriptList = SystemConfigMng.getPreLoadScriptListSMP();

			// PCの場合
		} else {
			scriptList = SystemConfigMng.getPreLoadScriptListPC();
		}
		if (scriptList != null) {
			for (String script : scriptList) {
				buf.append("<script src=\"" + script + "\"></script>");
				buf.append(CommonDefine.LINE_SEP);
			}
		}

		// 画面個別 JavaScriptリンク定義
		String[] addScrictList = screenContext.getAddScriptList();
		if (addScrictList != null) {
			for (String script : addScrictList) {
				buf.append("<script src=\"" + script + "\"></script>");
				buf.append(CommonDefine.LINE_SEP);
			}
		}

		// jQuery初期処理実行
		buf.append(CommonDefine.LINE_SEP);
		buf.append("<script>" + CommonDefine.LINE_SEP);
		buf.append("$( function() {" + CommonDefine.LINE_SEP);
		buf.append("	initLaBeeFramework(");
		buf.append("'" + SystemConfigMng.getHttpsPath() + "',");
		buf.append("'" + SystemConfigMng.getHttpsPort() + "');");
		buf.append(CommonDefine.LINE_SEP);
		

		// 追加動的JavaScript実行
		ArrayList<String> addScriptList = bean.getAddScriptList();
		if (addScriptList != null) {
			for (String script : addScriptList){
				buf.append(script + ";" + CommonDefine.LINE_SEP);
			}
			addScriptList.clear();
		}

		// ポップアップを閉じる際の処理
		AppPopupToParentContext popupToParentContext = envBean.getPopupToParentContext();
		if (popupToParentContext != null) {

			// ポップアップを閉じて親画面をサブミットする場合
			if (popupToParentContext.getPopupCloseType() == AppPopupToParentContext.CLOSE_TO_PARENT_SUBMIT) {
				buf.append("popupWindowToParentSubmit(");
				buf.append("'" + popupToParentContext.getParentTarget() + "'");
				buf.append(", '" + popupToParentContext.getParentExecute() + "'");

				if (StringUtil.isNull(popupToParentContext.getParentParam1())) {
					buf.append(", '" + popupToParentContext.getParentParam1() + "'");
				} else {
					buf.append(", null");
				}

				if (StringUtil.isNull(popupToParentContext.getParentParam2())) {
					buf.append(", '" + popupToParentContext.getParentParam2() + "'");
				} else {
					buf.append(", null");
				}

				if (StringUtil.isNull(popupToParentContext.getParentParam3())) {
					buf.append(", '" + popupToParentContext.getParentParam3() + "'");
				} else {
					buf.append(", null");
				}

				buf.append(");" + CommonDefine.LINE_SEP);

				// ポップアップを閉じて続けて別のポップアップを表示する場合
			} else if (popupToParentContext.getPopupCloseType() == AppPopupToParentContext.CLOSE_TO_NEXT_POPUP) {
				// buf.append("popupWindowCloseOnly();");
				// buf.append(CommonDefine.LINE_SEP);

				makeNextPopupWindow(popupToParentContext.getNextPopupContext(), buf);

				// ポップアップを閉じるだけの場合
			} else if (popupToParentContext.getPopupCloseType() == AppPopupToParentContext.CLOSE_ONLY) {
				buf.append("popupWindowCloseOnly();");
				buf.append(CommonDefine.LINE_SEP);

				// ポップアップを閉じて親画面にJSONを返す場合
			} else if (popupToParentContext.getPopupCloseType() == AppPopupToParentContext.CLOSE_TO_PARENT_JSON) {
				buf.append("popupWindowFromJsonResponse(");
				buf.append("'" + popupToParentContext.getJson() + "'");
				buf.append(");" + CommonDefine.LINE_SEP);
			}
		}

		// メッセージダイアログ処理
		AppMessageDialogContext messageContext = bean.getMessageDialogContext();
		if (messageContext != null && messageContext.getMessage() != null) {
			int messageType = messageContext.getMessageType();
			String message = messageContext.getMessage();

			// メッセージ初期化
			bean.setMessageDialogContext(null);

			// JavaScript出力
			buf.append("showDialogMessage('" + message + "', '" + messageType + "');");
		}

		// 入力チェックエラーが存在している場合のエラーダイアログ
		if (bean.isInputChkErrFlg()) {
			String headMsg = MessageLabelUtil.getMessage(SysMsgCodeDefine.SYS00000, bean.getMsgLocale());

			// ---------------------------------------------
			// JavaScriptのエラーポップアップウインドウに
			// 渡す為の文字列を作る
			// ---------------------------------------------
			HashMap<String, String> errMsgHash = bean.getInputErrMsgMap();
			Iterator<String> iterator = errMsgHash.keySet().iterator();

			// エラー文字列をカンマで連結する。
			StringBuilder errMsg = new StringBuilder();
			for (; iterator.hasNext();) {
				String key = iterator.next();
				String value = (String) errMsgHash.get(key);
				errMsg.append(value + ",");
			}

			// 最後尾の無用のカンマを削除
			if (errMsg.lastIndexOf(",") != -1) {
				errMsg.deleteCharAt(errMsg.length() - 1);
			}

			buf.append("showPopupErrorMsg(");
			buf.append("'" + headMsg + "', ");
			buf.append("'" + errMsg.toString() + "'");
			buf.append(");" + CommonDefine.LINE_SEP);
		}

		// クッキーから値をセットするパスワードタグ名セット
		ArrayList<String> passwdNameList = envBean.getPasswdSetNameList();
		if (passwdNameList != null && passwdNameList.size() != 0) {
			StringBuilder objId = new StringBuilder();
			for (int i = 0; i < passwdNameList.size(); i++) {
				if (i != 0) {
					objId.append(",");
				}
				objId.append(passwdNameList.get(i));
			}
			buf.append("setPasswdIdNames(");
			buf.append("'" + objId.toString() + "');");
			buf.append(CommonDefine.LINE_SEP);

		}

//		buf.append("	if (typeof appInit == \"function\") {");
//		buf.append("		appInit();");
//		buf.append("	}");

		buf.append("	});" + CommonDefine.LINE_SEP);
		buf.append("</script>" + CommonDefine.LINE_SEP);

		return buf.toString();
	}

	/**
	 * 続けてポップアップを表示する場合
	 * 
	 * @param context ポップアップ情報
	 * @param buf 文字列バッファ
	 * @return
	 */
	private void makeNextPopupWindow(AppPopupContext context, StringBuilder buf) {
		AppEnvironmentBean envBean = (AppEnvironmentBean) requestScopeMap.get(CommonDefine.ENV_BEAN);
		Environment env = envBean.getEnv();

		String[] tgtDatas = context.getTarget().split("\\.");
		String screenId = tgtDatas[tgtDatas.length - 1];
		ScreenContext screenContext = ResourceMng.getScreenContext(screenId);
		if (screenContext == null) {
			screenContext = new ScreenContext();
		}

		if (StringUtil.isNull(context.getLocaleCd()) == false) {
			context.setLocaleCd(SystemConfigMng.getSystemLocale().getLanguage());
		}

		if (StringUtil.isNull(context.getLabelDiv()) == false) {
			context.setLabelDiv("global");
		}

		StringBuilder url = new StringBuilder();
		if (screenContext.isSslFlg()) {
			url.append(SystemConfigMng.getHttpsPath());
			url.append(env.getServerName());
			url.append(":" + SystemConfigMng.getHttpsPort());
		} else {
			url.append(SystemConfigMng.getHttpPath());
			url.append(env.getServerName());
			url.append(":" + SystemConfigMng.getHttpPort());
		}
		url.append(env.getWebAppPath());
		url.append(env.getContextPath());

		url.append("?" + CommonDefine.TARGET + "=" + context.getTarget());
		url.append("&" + CommonDefine.EXECUTE + "=" + context.getExecute());

		if (StringUtil.isNull(context.getParam1())) {
			url.append("&" + CommonDefine.PARAM1 + "=" + context.getParam1());

			if (StringUtil.isNull(context.getParam2())) {
				url.append("&" + CommonDefine.PARAM2 + "=" + context.getParam2());

				if (StringUtil.isNull(context.getParam3())) {
					url.append("&" + CommonDefine.PARAM3 + "=" + context.getParam3());
				}
			}
		}

		if (StringUtil.isNull(screenContext.getLabelCd())) {
			screenContext.setViewTitle(MessageLabelUtil.getWordLabel(screenContext.getLabelCd(),
					screenContext.getLabelDiv(), new Locale(context.getLocaleCd())));

		} else {
			screenContext.setViewTitle(screenContext.getScreenName());
		}

		buf.append("parent.showPopupWindow(");
		buf.append("'" + url.toString() + "', ");
		buf.append("'" + screenContext.getViewTitle() + "', ");
		buf.append(context.getWidth() + ", ");
		buf.append(context.getHeight() + ", ");

		if (StringUtil.isNull(context.getEntryButtonName())) {
			buf.append("'" + context.getEntryButtonName() + "', ");
		} else {
			if (StringUtil.isNull(context.getEntryBtnLabelCd())) {
				buf.append("'" + MessageLabelUtil.getWordLabel(context.getEntryBtnLabelCd(),
						context.getLabelDiv(), new Locale(context.getLocaleCd())) + "', ");
			} else {
				buf.append("'', ");
			}
		}

		if (StringUtil.isNull(context.getCloseButtonName())) {
			buf.append("'" + context.getCloseButtonName() + "', ");
		} else {
			if (StringUtil.isNull(context.getCloseBtnLabelCd())) {
				buf.append("'" + MessageLabelUtil.getWordLabel(context.getCloseBtnLabelCd(),
						context.getLabelDiv(), new Locale(context.getLocaleCd())) + "', ");
			}
		}

		buf.append("'" + context.getNextForm() + "', ");
		buf.append("'" + context.getNextTarget() + "', ");
		buf.append("'" + context.getNextExecute() + "'");

		if (StringUtil.isNull(context.getNextParam1())) {
			buf.append(",'" + context.getNextParam1() + "'");
		} else {
			buf.append(",''");
		}

		if (StringUtil.isNull(context.getNextParam2())) {
			buf.append(",'" + context.getNextParam2() + "'");
		} else {
			buf.append(",''");
		}

		if (StringUtil.isNull(context.getNextParam3())) {
			buf.append(",'" + context.getNextParam3() + "'");
		} else {
			buf.append(",''");
		}

		if (StringUtil.isNull(context.getCloseTarget())) {
			buf.append(",'" + context.getCloseTarget() + "'");
		} else {
			buf.append(",''");
		}

		if (StringUtil.isNull(context.getCloseExecute())) {
			buf.append(",'" + context.getCloseExecute() + "'");
		} else {
			buf.append(",''");
		}

		buf.append(");");
		buf.append(CommonDefine.LINE_SEP);
	}

	/**
	 * @param requestScope セットする requestScope
	 */
	@SuppressWarnings("unchecked")
	public void setRequestScope(Object requestScope) {
		requestScopeMap = (Map<String, Object>) requestScope;

	}

}
