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

/**
 * システムメッセージコード定数クラス。<br>
 * システム内で使用するメッセージコード定数を定義している。
 *
 * @author ARTS Laboratory
 *
 * $Id: SysMsgCodeDefine.java 554 2016-08-12 21:19:00Z pjmember $
 */
public final class SysMsgCodeDefine {

	/** 入力した値にエラーがあります。内容を修正してください。 */
	public static final String SYS00000 = "SYS00000";

	/** {0}は必須入力項目です。 */
	public static final String SYS00001 = "SYS00001";

	/** {0}に入力不可の文字が含まれています。 */
	public static final String SYS00002 = "SYS00002";

	/** {0}は数値を入力してください。 */
	public static final String SYS00003 = "SYS00003";

	/** {0}は {1}以上 {2}以下の範囲で入力してください。 */
	public static final String SYS00004 = "SYS00004";

	/** {0}に入力した文字数が最大値を超えています。(最大文字数={1}) */
	public static final String SYS00005 = "SYS00005";

	/** {0}に入力した文字数が最小値より下です。(最小文字数={1}) */
	public static final String SYS00006 = "SYS00006";

	/** {0}は半角英数字で入力してください。 */
	public static final String SYS00007 = "SYS00007";

	/** {0}は半角英数字記号で入力してください。 */
	public static final String SYS00008 = "SYS00008";

	/** {0}は全角文字で入力してください。 */
	public static final String SYS00009 = "SYS00009";

	/** {0}は全角カナ文字で入力してください。 */
	public static final String SYS00010 = "SYS00010";

	/** {0}に入力出来る値は１単語のみです。 */
	public static final String SYS00011 = "SYS00011";

	/** {0}に入力した日付は存在しません。 */
	public static final String SYS00012 = "SYS00012";

	/** {0}に入力した値がEメールの書式ではありません。 */
	public static final String SYS00013 = "SYS00013";

	/** ビジネスロジックのアノテーション実行定義が未設定です:{0} */
	public static final String SYS10001 = "SYS10001";
	/** 実行する処理名が指定されていません:{0} */
	public static final String SYS10002 = "SYS10002";
	/** 存在しない処理呼び出しエラー:{0} */
	public static final String SYS10003 = "SYS10003";

	/** 呼びだしたビジネスロジックはアノテーション定義がされていません:{0} */
	public static final String SYS10010 = "SYS10010";
	/** ターゲット呼び出しエラー:{0} */
	public static final String SYS10011 = "SYS10011";
	/** 指定した画面IDは定義されていません。{0} */
	public static final String SYS10012 = "SYS10012";

	/** パラメータセッター実行時にエラーが発生しました：{0} */
	public static final String SYS10020 = "SYS10020";
	/** パラメータのセッターメソッドが見つかりません：{0} */
	public static final String SYS10021 = "SYS10021";

}
