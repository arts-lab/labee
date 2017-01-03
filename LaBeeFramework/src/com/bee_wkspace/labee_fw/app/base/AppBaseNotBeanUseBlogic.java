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
package com.bee_wkspace.labee_fw.app.base;

/**
 * Webアプリ用基底ビジネスロジック。(ビーンクラスを使用しない)<br>
 *
 * <br>
 * ビーンクラスを必要としないビジネスロジック処理を実装する場合は本クラスを継承し、クラス名の末尾に<b>Blogic</b>を付加する必要がある<br>
 * (例)<br>
 *
 * <b>@FwBlogic(beanReuse = false)</b><br>
 * public class <b>TestBlogic</b> extends AppBaseNotBeanUseBlogic { } <br>
 * <br>
 *
 * ビジネスロジックとは画面からの要求に対し、メインとなるプログラムを記述する処理である。<br>
 * ビジネスロジックでは任意の処理を行ない、JSPに結果を返す必要がない場合に本クラスを継承する。<br>
 * <br>
 *
 * 画面からの要求に対するビジネスロジック処理メソッドを実装する際は<b>@FwExeMethod</b>アノテーションを付加し<br>
 * 戻り値にResponseContextを返却するメソッドを作成する。<br>
 * 呼び出しの要求に任意パラメータ(P1,P2,P3)が送信されている場合は実装するメソッドも同じ数の文字列引数を受ける必要がある。<br>
 * (例)<br>
 *
 * <b>@FwExeMethod</b><br>
 * public <b>ResponseContext start</b>(String param1) {<br>
 * 　　System.out.println(param1);<br>
 * 　　return <b>responseContext;</b><br>
 * }<br>
 * <br>
 *
 * 任意ビジネス処理実行後にJSPに遷移するか、他ビジネスロジックに処理委譲するか等は戻り値のResponseContextの設定により<br>
 * 切り替える事が可能だが、手動設定は行なわずに本クラス内で定義している設定メソッドを呼んで設定するのが望ましい<br>
 * <b>JSP遷移</b><br>
 * デフォルトではビジネスロジッククラス名から"Blogic"文字列を除いた名を画面IDとしてScreenDefine.csvで定義したJSPに遷移する。<br>
 * <br>
 * <b>他ビジネスロジックへ処理直接委譲</b><br>
 * 本クラスのsetDelegateResponseメソッドを呼ぶ事で、実装したビジネスロジック処理が完了した後に、別のビジネスロジッククラス<br>
 * のメソッドを直列呼び出し実行する事が出来る、最終的なレスポンス処理は呼び出し先のレスポンス設定に委譲する。<br>
 * <br>
 * <b>他ビジネスロジックへリダイレクト呼び出し</b><br>
 * 本クラスのsetRedirectResponseメソッドを呼ぶ事で、実装したビジネスロジック処理が完了した後に、別のビジネスロジッククラス<br>
 * のメソッドをリダイレクト呼び出しする事が出来る、リダイレクト処理は一度処理がブラウザに戻り、ブラウザが再度HTTPリダイレクトを行なう<br>
 * <br>
 * <b>URLリダイレクト呼び出し</b><br>
 * 本クラスのsetRedirectURLメソッドを呼ぶ事で、実装したビジネスロジック処理が完了した後に、別のURLへリダイレクト遷移<br>
 * する事が出来る、遷移先URLは外部サイトのURLでも構わない。<br>
 * <br>
 * レスポンス処理には画面遷移以外にCSV、JSON、バイナリ、XMLを返却出力する事も可能である。<br>
 * (例)レスポンスにCSVファイルを出力する場合の設定<br>
 * String csvData = "aaa,bbbb,123,";<br>
 * responseContext.setResponseType(ResponseContext.RESPONSE_TYPE_CSV);<br>
 * responseContext.setResponseEncType(CommonDefine.ENCODE_UTF_8);<br>
 * responseContext.setResponseCsv(csvData);<br>
 * responseContext.setExportFileName("Sample.csv");<br>
 *
 * <br>
 * <br>
 * 本クラスは継承元の基底クラス<b>com.bee_wkspace.labee_fw.core.base.BaseBlogic</b>
 * に対して以下の機能を付加している。<br>
 * データベース操作クラス<b>com.bee_wkspace.labee_fw.common.db.DBAccess</b>をフィールドに持ち(
 * インスタンス名 <b>dba</b>)<br>
 * システム設定ファイルでデータベースを使用する設定にしている場合に<br>
 * <b>preProcess()</b>メソッドでデータベースのコネクションオープン処理を行ない、<br>
 * <b>finallyProcess()</b>メソッドでデータベースのコネクションクローズ処理が確実に行われる。<br>
 * 本クラスを継承したビジネスロジッククラスはデータベースのオープン、クローズを意識する必要がなくなる。<br>
 * <b>preProcess()</b>メソッド、<b>finallyProcess()</b>メソッドはフレームワークが自動的に呼びだすので<br>
 * 実装者が手動で呼びだしてはいけない。<br>
 * (それぞれのメソッドをオーラライドする際は必ず親クラスの同名メソッドを呼びだす事)<br>
 *
 * <br>
 * データベース処理に対し、更新系のSQLを実行した場合は、実装者がコミット処理、ロールバック処理を管理する必要がある。<br>
 * (例) <b>dba.commi()</b>, <b>dba.rollback()</b><br>
 *
 * @author ARTS Laboratory
 *
 * $Id: AppBaseNotBeanUseBlogic.java 554 2016-08-12 21:19:00Z pjmember $
 */
public class AppBaseNotBeanUseBlogic extends AppBaseBlogic<AppBaseBean> {

	/**
	 * コンストラクタ。
	 */
	public AppBaseNotBeanUseBlogic() {
		super();
	}

}
