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

//-------------------------------------------------
// LaBeFramework メインJavaScript
// 
// @author ARTS Laboratory
//
// $Id: LaBeeFramework.js 566 2016-08-16 00:16:36Z pjmember $
// ------------------------------------------------

var dialogWidth = 600;
var popupAnimation = 'fade'
var wkJson = null;
var scrollTtime = 500;

//---------------------------------------------------------
// 初期化処理
// @param SSLスキーマ
// @param SSLポート
//---------------------------------------------------------
function initLaBeeFramework(scheme, port) {
	var forms = document.forms;
	for (i = 0; i < forms.length; i++) {
		var form = document.forms[i];
		createFwHidden(form, 'tgt', '');
		createFwHidden(form, 'exe', '');
		createFwHidden(form, 'p1', '');
		createFwHidden(form, 'p2', '');
		createFwHidden(form, 'p3', '');
		createFwHidden(form, 'screen_id', '');
		createFwHidden(form, 'scheme', scheme);
		createFwHidden(form, 'port', port);

		createFwDiv(form, 'msgDialogPopup');
		createFwDiv(form, 'confirmDialogPopup');
		createFwDiv(form, 'popupWindow');
	}
	
	  var offsetY = -10;
	  $('a[href^=#]').click(function() {
		    var target = $(this.hash);
		    if (!target.length) return ;
		    var targetY = target.offset().top+offsetY;
		    $('html,body').animate({scrollTop: targetY}, scrollTtime, 'swing');
		    window.history.pushState(null, null, this.hash);
		    return false;
	  });


	  if (typeof appInit == "function") {
		  appInit();
	  }

}


function createFwHidden(form, name, value) {
	$('<input>').attr({
	    type: 'hidden',
	    name: name,
	    id: name,
	    value: value
	}).appendTo(form);
}

function createFwDiv(form, id) {
	$('<div></div>').attr({
	    id: id
	}).appendTo(form);
}

// ---------------------------------------------------------
// 通常フォームセンド
// @param form フォームオブジェクト
// @param target ターゲット
// @param execute 実行処理名
// ---------------------------------------------------------
function submitForm(form, target, execute) {
	if (execute == '') {
		execute = "start";
	}

	form.tgt.value = target;
	form.exe.value = execute;

	form.encoding = "application/x-www-form-urlencoded";
	form.submit();
}

// ---------------------------------------------------------
// 通常フォームセンド
// @param form フォームオブジェクト
// @param target ターゲット
// @param execute 実行処理名
// @param param1 パラメータ1
// @param param2 パラメータ2
// @param param3 パラメータ3
// ---------------------------------------------------------
function submitParamForm(form, target, execute, param1, param2, param3) {
	form.p1.value = param1;
	form.p2.value = param2;
	form.p3.value = param3;
	submitForm(form, target, execute);
}

// ---------------------------------------------------------
// マルチパートフォームセンド
// @param form フォームオブジェクト
// @param target ターゲットクラス
// @param execute 実行処理名
// @param param1 パラメータ1
// @param param2 パラメータ2
// @param param3 パラメータ3
// ---------------------------------------------------------
function sendMultiPartForm(form, target, execute, param1, param2, param3) {
	form.tgt.value = target;
	form.exe.value = execute;
	form.p1.value = param1;
	form.p2.value = param2;
	form.p3.value = param3;
	form.encoding = "multipart/form-data"; // IE, FireFox
	form.enctype = "multipart/form-data"; // Safari用
	form.submit();
}

// ---------------------------------------------------------
// SSL暗号化送信用フォームセンド
//
//  @param  form  フォームオブジェクト
//  @param  target  ターゲットクラス
//  @param  execute     実行処理名
//
// ---------------------------------------------------------
function submitFormSSL(form, target, execute) {
    form.action = getSslURL(form);
    submitForm(form, target, execute);
}


function getSslURL(form) {
    var scheme = document.getElementById("scheme").value;
    var port = document.getElementById("port").value;
	
	var action = form.action;
    var idx = action.indexOf("/");
    var url = action.substring(idx+2, action.length);

    var portStartIdx = url.indexOf(":");
    var portEndIdx = url.indexOf("/");


    if (portStartIdx != -1) {
      var parts1 = url.substring(0, portStartIdx);
      var parts2 = url.substring(portEndIdx, url.length);
      url = scheme + parts1 + ":" + port + parts2;

    } else {
      var parts1 = url.substring(0, portEndIdx);
      var parts2 = url.substring(portEndIdx, url.length);
      url = scheme + parts1 + ":" + port + parts2;
    }
    return url;
}


//---------------------------------------------------------
//SSL暗号化送信用フォームセンド
//
//@param  form  フォームオブジェクト
//@param  target  ターゲットクラス
//@param  execute     実行処理名
//@param param1 パラメータ1
//@param param2 パラメータ2
//@param param3 パラメータ3
//
//---------------------------------------------------------
function submitParamFormSSL(form, target, execute,  param1, param2, param3) {
	form.p1.value = param1;
	form.p2.value = param2;
	form.p3.value = param3;
	submitFormSSL(form, target, execute);
}


// ---------------------------------------------------------
// 汎用パラメータのセット
// @param param パラメータ名
// @param val 設定値
//
// ---------------------------------------------------------
function setParam(form, name, value) {
	form.Item(name).value = value;
}

// ---------------------------------------------------------
// パラメータ1のセット
// @param form フォームオブジェクト
// @param val 設定値
//
// ---------------------------------------------------------
function setParam1(form, value) {
	form.p1.value = value;
}

// ---------------------------------------------------------
// パラメータ2のセット
// @param form フォームオブジェクト
// @param val 設定値
//
// ---------------------------------------------------------
function setParam2(form, value) {
	form.p2.value = value;
}

// ---------------------------------------------------------
// パラメータ3のセット
// @param form フォームオブジェクト
// @param val 設定値
//
// ---------------------------------------------------------
function setParam3(form, value) {
	form.p3.value = value;
}


//---------------------------------------------------------
// フォームにターゲット、処理名をセット
//@param form フォームオブジェクト
//@param val 設定値
//
//---------------------------------------------------------
function setSubmitForm(form, target, execute) {
	if (execute == '') {
		execute = "start";
	}

	form.tgt.value = target;
	form.exe.value = execute;
}

//---------------------------------------------------------
//フォームにターゲット、処理名をセット(SSL版)
//@param form フォームオブジェクト
//@param val 設定値
//
//---------------------------------------------------------
function setSubmitFormSSL(form, target, execute) {
	if (execute == '') {
		execute = "start";
	}
	form.action = getSslURL(form);
	form.tgt.value = target;
	form.exe.value = execute;
}


//---------------------------------------------------------
// Ajaxで非同期にフォーム送信を行ないJSONを受信し戻り値に返す。
// JSON内容を基に画面内容を動的に書き換える。
// @param form フォームオブジェクト
// @param target ターゲット画面ID
// @param execute 実行処理名
// @param param1 パラメータ1
// @param param2 パラメータ2
// @param param3 パラメータ3
// @retExecFunc Ajaxレスポンス後に実行するメソッド
//---------------------------------------------------------
function postAjaxRetJsonExec(form, target, execute, param1, param2, param3, retExecFunc) {
	$.ajax({
		type:"POST",
		url: form.action,
		async: true,
		cache: false,
		dataType:"json",
		data:
		{
			"tgt": target,
			"exe": execute,
			"p1": param1,
			"p2": param2,
			"p3": param3,

		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			var msg = "--- Ajax Response Error ---"
				msg += "<br>"+"status:"+ XMLHttpRequest.status;
				msg += "<br>"+"statusText:" + XMLHttpRequest.statusText;
				msg += "<br>"+"textStatus:" + textStatus;
				msg += "<br>"+"errorThrown:" + errorThrown;
				for(var i in errorThrown) {
					msg += "<br>" + "error " + i + ":" + errorThrown[i];
				}
				document.open();
				document.write(msg);
				document.close();
		},
		success: function(json) {
			wkJson = json;
			exeFunc = new Function("return " + retExecFunc);
			exeFunc();
			wkJson = null;
			return json;
		}
	});
}

//---------------------------------------------------------
//Ajaxで非同期にフォーム送信を行ないJSONを受信し戻り値に返す。(SSL版)
//JSON内容を基に画面内容を動的に書き換える。
//@param form フォームオブジェクト
//@param target ターゲット画面ID
//@param execute 実行処理名
//@param param1 パラメータ1
//@param param2 パラメータ2
//@param param3 パラメータ3
//@retExecFunc Ajaxレスポンス後に実行するメソッド
//---------------------------------------------------------
function postAjaxRetJsonExecSSL(form, target, execute, param1, param2, param3, retExecFunc) {
	form.action = getSslURL(form);
	return postAjaxRetJsonExec(form, target, execute, param1, param2, param3, retExecFunc);
}



//----------------------------------------------------
// ビジネスロジックを介せず画面IDに紐づくJSP,HTMLを呼びだす。
//
// @param form フォームオブジェクト
// @param screenId 画面ID
//----------------------------------------------------
function forwardJsp(form, screenId) {
	form.tgt.value = '';
	form.exe.value = '';
	form.screen_id.value = screenId;

	form.encoding = "application/x-www-form-urlencoded";
	form.submit();
}


//----------------------------------------------------
//id,value形式のJSON内容を基に画面のオブジェクト内容を変更する。
//
//@param jsonStr id,value形式のJSON
//----------------------------------------------------
function changetTagValueFromJson(json) {
	if (json) {

	} else {
		json = wkJson;
	}

	var len = json.length;
	for (i=0; i<len; i++) {
		var objId = json[i].objId;
		var obj = document.getElementById(objId);

		if (obj != null) {
			var tagName = obj.tagName.toLowerCase();

			// DIVの場合
			if (tagName == 'div') {
				$('#' + objId).html(json[i].value);

			// 段落文字の場合
			} else if (tagName == 'p') {
				$('#' + objId).text(json[i].value);

			// インプットの場合
			} else if (tagName == 'input') {
				$('#' + objId).val(json[i].value);

			// セレクトボックスの場合
			} else if (tagName == 'select') {
				$('#' + objId).val(json[i].value);

			} else {
				$('#' + objId).html(json[i].value);
			}
		}
	}
}


// ----------------------------------------------------
// ダイアログメッセージ表示
// @param msg メッセージ
// @param iconType アイコンタイプ
// ----------------------------------------------------
function showDialogMessage(msg, iconType) {
	var title = '';
	var iconClass = '';
	if (iconType == '1') {
		iconClass = 'PopupCompleteIcon';
		title = 'Complete';

	} else if (iconType == '2') {
		iconClass = 'PopupInfoIcon';
		title = 'Infomation';

	} else if (iconType == '3') {
		iconClass = 'PopupWarningIcon';
		title = 'Warning';

	} else if (iconType == '4') {
		iconClass = 'PopupErrorIcon';
		title = 'Error';

	} else if (iconType == '5') {
		iconClass = 'PopupQuestionIcon';
		title = 'Confirmation';
	}

	var layer = '';
	layer += '  <div class="' + iconClass + '" style="margin-right:5px"></div>';
	layer += '    <div style="height: 55px; margin-top:10px; margin-left:10px;"><span class="Font14px" style="vertical-align:middle">';
	layer += msg;
	layer += '    </span></div>';

	$("#msgDialogPopup").html(layer);

	$('#msgDialogPopup').dialog({
		autoOpen : true,
		show: popupAnimation,
		hide: popupAnimation,
		width : dialogWidth,
		title : title,
		modal : true,
		resizable : false,
		buttons : {
			"OK" : function() {
				$(this).dialog("close");
			}
		}
	});

}


// ----------------------------------------------------
// 確認ダイアログポップアップ表示し、OKを押下すると指定したメソッドを呼びだす。
// @param msg メッセージ
// @param exeFunction  実行するメソッド
// ----------------------------------------------------
function showConfirmDialogExec(msg, exeFunction) {
	var layer = '';
	layer += '<div>';
	layer += '   <div class="PopupQuestionIcon" style="margin-right:5px"></div>';
	layer += '   <div style="height: 55px; margin-top:10px; margin-left:10px;"><span class="Font14px" style="vertical-align:middle">';
	layer += msg;
	layer += '   </span></div>';
	layer += '</div>';

	$("#confirmDialogPopup").html(layer);


	$('#confirmDialogPopup').dialog({
		autoOpen : true,
		show: popupAnimation,
		hide: popupAnimation,
		width : dialogWidth,
		title : 'Confirmation',
		modal : true,
		resizable : false,

		buttons : {
			"OK" : function() {
				exeFunc = new Function("return " + exeFunction);
				exeFunc();

				$(this).dialog("close");
			},
			"Cancel" : function() {
				$(this).dialog("close");
			}
		}
	});

}




//----------------------------------------------------
//確認ダイアログポップアップ表示し、OKを押下すると指定したURLに遷移する。
//@param msg メッセージ
//@param url  遷移先
//----------------------------------------------------
function showConfirmDialogURL(msg, url) {
	var layer = '';
	layer += '<div>';
	layer += '   <div class="PopupQuestionIcon" style="margin-right:5px"></div>';
	layer += '   <div style="height: 55px; margin-top:10px; margin-left:10px;"><span class="Font14px" style="vertical-align:middle">';
	layer += msg;
	layer += '   </span></div>';
	layer += '</div>';

	$("#confirmDialogPopup").html(layer);


	$('#confirmDialogPopup').dialog({
		autoOpen : true,
		show: popupAnimation,
		hide: popupAnimation,
		width : dialogWidth,
		title : 'Confirmation',
		modal : true,
		resizable : false,

		buttons : {
			"OK" : function() {
				window.location.href= url;
				$(this).dialog("close");
			},
			"Cancel" : function() {
				$(this).dialog("close");
			}
		}
	});

}


//----------------------------------------------------
//確認ダイアログポップアップ表示し、OKを押下すると指定ターゲットにサブミットする。
//@param msg メッセージ
//@param formName フォームオブジェクト
//@param target ターゲット
//@param execute 実行処理名
//@param param1 パラメータ1
//@param param2 パラメータ2
//@param param3 パラメータ3
//----------------------------------------------------
function showConfirmDialogSubmit(msg, formName, target, execute, param1, param2, param3) {
	var layer = '';
	layer += '<div>';
	layer += '   <div class="PopupQuestionIcon" style="margin-right:5px"></div>';
	layer += '   <div style="height: 55px; margin-top:10px; margin-left:10px;"><span class="Font14px" style="vertical-align:middle">';
	layer += msg;
	layer += '   </span></div>';
	layer += '</div>';

	$("#confirmDialogPopup").html(layer);


	$('#confirmDialogPopup').dialog({
		autoOpen : true,
		show: popupAnimation,
		hide: popupAnimation,
		width : dialogWidth,
		title : 'Confirmation',
		modal : true,
		resizable : false,

		buttons : {
			"OK" : function() {
	        	submitParamForm(formName, target, execute, param1, param2, param3);
				$(this).dialog("close");
			},
			"Cancel" : function() {
				$(this).dialog("close");
			}
		}
	});

}




//----------------------------------------------------
// 入力チェックエラー表示ダイアログ表示
// @param   headMsg ヘッダメッセ―ジ
// @param   errMsgs エラーメッセージ（カンマ区切で複数記述）
//----------------------------------------------------
function showPopupErrorMsg(headMsg, errMsgs) {
    var msgArray = errMsgs.split(",");
    var width = 490;
    var height = 300;

	var layer = '';
	layer += '  <div class="PopupErrorIcon" style="margin-right:5px"></div>';
    layer += '  <div class="Font12px SysError">';
    layer += '      <div style="margin-top:12px; margin-left:5px;">' + headMsg + '</div>';
    layer += '      <div class="Raius" style="border:1px solid #FFFFFF; clear:both; width:420px; height:160px;overflow:auto;height:105px;margin-left:10px; background-color:#FFFFFF;">';

    for (var i = 0; i <msgArray.length; i++ ) {
        layer += '      <div class="Font12px" style="clear:both;">・' + msgArray[i] + '</div>';
    }

    layer += '      </div>';
    layer += '  </div>';

	$("#msgDialogPopup").html(layer);

	$('#msgDialogPopup').dialog({
		autoOpen : true,
		show: popupAnimation,
		hide: popupAnimation,
		width : width,
		height: height,
		title : 'Input check error',
		modal : true,
		resizable : false,
		buttons : {
			"OK" : function() {
				$(this).dialog("close");
			}
		}
	});
}


//----------------------------------------------------
//ポップアップウインドウ表示
//
// @param url	ポップアップに表示するURLパス
// @param title	ポップアップウインドウ表示名
// @param width	ポップアップウインドウ表示領域幅
// @param height	ポップアップウインドウ表示領域高さ
// @param EntryButtonName	実行ボタン表示名
// @param CloseButtonName	クローズボタン表示名
// @param nextForm	実行ボタンで実行するフォーム名
// @param nextTarget	実行ボタンで実行するターゲット名
// @param nextExecute	実行ボタンで実行する処理名
// @param nextParam1	実行ボタンの実行先に渡すパラメータ1
// @param nextParam2	実行ボタンの実行先に渡すパラメータ2
// @param nextParam3	実行ボタンの実行先に渡すパラメータ3
// @param closeTarget	クローズボタンで実行するターゲット名
// @param closeExecute	クローズボタンで実行する処理名
//----------------------------------------------------
function showPopupWindow(url, title, width, height, EntryButtonName, CloseButtonName,
		nextForm, nextTarget, nextExecute, nextParam1, nextParam2, nextParam3, closeTarget, closeExecute) {

	if (document.getElementById("popupFrame") != null) {
		parent.$("#popupFrame").remove();
	}

    var layer = '<iframe src="' + url
    	+ '" frameborder="0" scrolling="no" id="popupFrame" width="'
    	+ width + '" height="'
    	+ height + '">Object Tag Not Used</iframe >';


	$("#popupWindow").html(layer);

	if (EntryButtonName != null && EntryButtonName != '') {
		if (closeTarget == null || closeTarget =='') {
			$('#popupWindow').dialog({
				autoOpen : true,
				show: popupAnimation,
				hide: popupAnimation,
				width : width + 45,
				height: height + 145,
				maxWidth:width,
				maxHeight:height,
				title : title,
				modal : true,
				resizable : false,
		
			    buttons:[
			        {
			           text: EntryButtonName,
			           click: function() {
			        	   var ifDocument = document.getElementById('popupFrame').contentWindow.document;
			        	   var ifForm = ifDocument.forms[nextForm];
		
			        	   if (nextParam1 != null) {
			        		   setParam1(ifForm, nextParam1);
		
				        	   if (nextParam2 != null) {
				        		   setParam2(ifForm, nextParam2);
		
					        	   if (nextParam3 != null) {
					        		   setParam3(ifForm, nextParam3);
					        	   }
				        	   }
			        	   }
		
			        	   submitForm(ifForm, nextTarget, nextExecute);
			           }
			        },
			        {
			           text: CloseButtonName,
			           click: function() {
							$("#popupFrame").remove();
							$(this).dialog("close");
			           }
			        }
			    ]
			});
		} else {
			$('#popupWindow').dialog({
				autoOpen : true,
				show: popupAnimation,
				hide: popupAnimation,
				width : width + 45,
				height: height + 145,
				maxWidth:width,
				maxHeight:height,
				title : title,
				modal : true,
				resizable : false,
		
			    buttons:[
			        {
			           text: EntryButtonName,
			           click: function() {
			        	   var ifDocument = document.getElementById('popupFrame').contentWindow.document;
			        	   var ifForm = ifDocument.forms[nextForm];
		
			        	   if (nextParam1 != null) {
			        		   setParam1(ifForm, nextParam1);
		
				        	   if (nextParam2 != null) {
				        		   setParam2(ifForm, nextParam2);
		
					        	   if (nextParam3 != null) {
					        		   setParam3(ifForm, nextParam3);
					        	   }
				        	   }
			        	   }
		
			        	   submitForm(ifForm, nextTarget, nextExecute);
			           }
			        },
			        {
			           text: CloseButtonName,
			           click: function() {
			        	   var ifDocument = document.getElementById('popupFrame').contentWindow.document;
			        	   var ifForm = ifDocument.forms[nextForm];
			        	   submitForm(ifForm, closeTarget, closeExecute);
			           }
			        }
			    ]
			});
		}
	} else {
		$('#popupWindow').dialog({
			autoOpen : true,
			show: popupAnimation,
			hide: popupAnimation,
			width : width + 45,
			height: height + 145,
			maxWidth:width,
			maxHeight:height,
			title : title,
			modal : true,
			resizable : false,
	
		    buttons:[
		        {
		           text: CloseButtonName,
		           click: function() {
						$("#popupFrame").remove();
						$(this).dialog("close");
		           }
		        }
		    ]
		});
	}
}

//----------------------------------------------------
// ポップアップウインドウ表示状態から親ウインドウをサブミットする。
// (フレームワークから呼ばれる）
// @param parentTarget	親ウインドウのターゲット
// @param parentExecute	親ウインドウの処理
// @param parentParam1	親ウインドウへのパラメータ1
// @param parentParam2	親ウインドウへのパラメータ2
// @param parentParam3	親ウインドウへのパラメータ3
//----------------------------------------------------
function popupWindowToParentSubmit(parentTarget, parentExecute, parentParam1, parentParam2, parentParam3) {
	var parentForm = parent.document.forms[0];

	if (parentForm) {
		if (parentParam1 != null) {
    		setParam1(parentForm, parentParam1);

        	if (parentParam2 != null) {
        		setParam2(parentForm, parentParam2);

	        	if (parentParam3 != null) {
	        		   setParam3(parentForm, parentParam3);
	        	}
        	}
    	 }

    	 // ポップアップウインドウを閉じる
    	 if (parent.document.getElementById("popupFrame") != null) {
    		parent.$('#popupWindow').dialog("close");
    		parent.$("#popupFrame").remove();
    	 }

    	 parentForm.target="_parent";
    	 submitForm(parentForm, parentTarget, parentExecute);
	}
}


//----------------------------------------------------
//ポップアップウインドウ表示状態から閉じる。
//(フレームワークから呼ばれる）
//----------------------------------------------------
function popupWindowCloseOnly() {
	var parentForm = parent.document.forms[0];
	if (parentForm) {

 	   // ポップアップウインドウを閉じる
	   var popupFrame = parent.document.getElementById("popupFrame");
		if (popupFrame != null) {
 		   parent.$('#popupWindow').dialog("close");
 		   parent.$("#popupFrame").remove();
 	   }
	}
}


//----------------------------------------------------
//ポップアップウインドウ表示状態から親ウインドウにJSONデータを渡し
// HTMLオブジェクトを動的に内容を変更する。
//(フレームワークから呼ばれる）
// @param jsonStr JSON文字列
//----------------------------------------------------
function popupWindowFromJsonResponse(jsonStr) {
	var json = $.parseJSON(jsonStr);
	var len = json.length;
	for (i=0; i<len; i++) {
		var objId = json[i].objId;
		var obj = parent.document.getElementById(objId);

		if (obj != null) {
			var tagName = obj.tagName.toLowerCase();

			// DIVの場合
			if (tagName == 'div') {
				parent.$('#' + objId).html(json[i].value);

			// 段落文字の場合
			} else if (tagName == 'p') {
				parent.$('#' + objId).text(json[i].value);

			// インプットの場合
			} else if (tagName == 'input') {
				parent.$('#' + objId).val(json[i].value);

			// セレクトボックスの場合
			} else if (tagName == 'select') {
				parent.$('#' + objId).val(json[i].value);

			} else {
				parent.$('#' + objId).html(json[i].value);
			}
		}
	}

	// ポップアップウインドウを閉じる
	popupWindowCloseOnly();

}

//----------------------------------------------------
// 画面表示時にクッキーから値を取得しパスワードタグに値を動的セットする。
// セット後にクッキーをクリアするのでセキュリティー面で考慮している。
// @param パスワードタグID名(カンマ区切りで複数設定)
//----------------------------------------------------
function setPasswdIdNames(idNames) {
	if (idNames != null && idNames.length != 0) {
		var nameList = idNames.split(",");
		for (i=0; i<nameList.length; i++) {
			var objId = nameList[i];
			var cookieData = getCookie(objId);
			if (cookieData != null && cookieData != '' && cookieData !='""') {
				if (document.getElementById(objId) != null) {
					$('#' + objId).val(cookieData);
				}
			}
		}
	}
}


//----------------------------------------------------
//  クッキーから値を取得する。
// @param name クッキー名
// @return クッキー値
//----------------------------------------------------
function getCookie(name){
    var st = '';
    var ed = '';
    if(document.cookie.length > 0){
        st = document.cookie.indexOf(name + "=");
        if(st != -1){
            st = st + name.length + 1;
            ed = document.cookie.indexOf(";", st);
            if(ed==-1) ed=document.cookie.length;
            var data = unescape(document.cookie.substring(st,ed));
            document.cookie = name + "=;";
            return data;
        }
    }
    return null;
}



//----------------------------------------------------
// 画面トップにスクロールする。
//----------------------------------------------------
function scrollTop() {
    $('html,body').animate({ scrollTop: 0 }, 'fast');
}	

//----------------------------------------------------
// 指定DIVターゲット位置にスクロールする。
//----------------------------------------------------
function scrollTarget(target) {
	var targetOffset = $("#"+target).offset();
	$('html,body').animate({ scrollTop: targetOffset.top}, 'fast');
}


