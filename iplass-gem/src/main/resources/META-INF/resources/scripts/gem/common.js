/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

////////////////////////////////////////////////////////
// 共通処理用のJavascript
////////////////////////////////////////////////////////

/**
 * フォームを作成し、指定actionにサブミットする。
 */
function submitForm(action, params, target) {
	var $form = createForm(action, params, target);
	$form.submit();
	return $form;
}

function createForm(action, params, target) {
	var $form = $("<form method='post' />").attr("action", action).appendTo("body");
	if (target) $form.attr("target", target);
	if (params) {
		for (var keyString in params) {
			if (keyString && params[keyString] != null) {
				if ($.isArray(params[keyString])) {
					for (var i = 0; i < params[keyString].length; i++) {
						var $input = $("<input type='hidden' />").attr({name:keyString}).val(params[keyString][i]);
						$input.appendTo($form);
					}
				} else {
					var $input = $("<input type='hidden' />").attr({name:keyString}).val(params[keyString]);
					$input.prependTo($form);
				}
			}
			//$form.prepend("<input type='hidden\' name='" + keyString + "' value='" + params[keyString] + "'/>");
		}
	}
	return $form;
}

////////////////////////////////////////////////////////
//メニュー用のJavascript
////////////////////////////////////////////////////////

/**
* ホーム押下処理
*/
function home(action) {
	clearMenuState();
	submitForm(contextPath + "/" + action, null);
}

/**
* 検索画面表示処理
* @param action
* @param defName
*/
function searchView(action, defName, urlParam, target) {
	var params = {};
	var viewName = "";
	if (urlParam && urlParam.length > 0) {
		var kv = urlParam.split("&");
		if (kv.length > 0) {
			for (var i = 0; i < kv.length; i++) {
				var _kv = kv[i].split("=");
				if (_kv[0] == "viewName") {
					viewName = "/" + _kv[1];
				} else {
					if (params[_kv[0]]) {
						if ($.isArray(params[_kv[0]])) {
							params[_kv[0]].push(_kv[1]);
						} else {
							var ary = [params[_kv[0]], _kv[1]];
							params[_kv[0]] = ary;
						}
					} else {
						params[_kv[0]] = _kv[1];
					}
				}
			}
		}
	}
	submitForm(contextPath + "/" + action + viewName + "/" + defName, params, target);
}

function menuClick(action, urlParam, target) {
	var params = {};
	if (urlParam && urlParam.length > 0) {
		var kv = urlParam.split("&");
		if (kv.length > 0) {
			for (var i = 0; i < kv.length; i++) {
				var _kv = kv[i].split("=");
				if (params[_kv[0]]) {
					if ($.isArray(params[_kv[0]])) {
						params[_kv[0]].push(_kv[1]);
					} else {
						var ary = [params[_kv[0]], _kv[1]];
						params[_kv[0]] = ary;
					}
				} else {
					params[_kv[0]] = _kv[1];
				}
			}
		}
	}
	submitForm(contextPath + "/" + action, params, target);
}

function clearMenuState() {
	// メニュー選択状態削除
	setCookie("currentMenuId", "", -1);
}
////////////////////////////////////////////////////////
//検索画面用のJavascript
////////////////////////////////////////////////////////

/**
* CSVダウンロード実行
* @param searchType
* @param formName
* @param action
*/
function csvDownload(searchType, formName, action, target, interval, isForUpload, characterCode, isNoDispName, isOutputResult, isOutputCodeValue) {
	var $form = $("<form method='POST' />").attr({action:contextPath + action}).appendTo("body");
	$("<input />").attr({type:"hidden", name:"defName", value:$(":hidden[name='defName']").val()}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"searchType", value:searchType}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"isForUpload", value:isForUpload}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"characterCode", value:characterCode}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"isNoDispName", value:isNoDispName}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"isOutputResult", value:isOutputResult}).appendTo($form);
	//カスタムでDL処理を作ってる場合、古いバージョンのパラメータ使ってる可能性あるので念のため
	$("<input />").attr({type:"hidden", name:"isAllProperty", value:isForUpload}).appendTo($form);

	if (typeof isOutputCodeValue === "undefined" || isOutputCodeValue == null)
	{
		isOutputCodeValue = false;
	}
	$("<input />").attr({type:"hidden", name:"isOutputCodeValue", value:isOutputCodeValue}).appendTo($form);

	var _$form = $("[name='" + formName + "']");
	$("select[name]", _$form).each(function() {
		var name = $(this).attr("name");
		var val = $(this).val();
		$("<input />").attr({type:"hidden", name:name, value:val}).appendTo($form);
	});

	$("input[name]:not(:radio):not(:checkbox)", _$form).each(function() {
		var name = $(this).attr("name");
		var val = $(this).val();
		$("<input type='hidden' name='" + name + "' value='" + val + "' />").appendTo($form);
	});

	$(":radio:checked[name]", _$form).each(function() {
		var name = $(this).attr("name");
		var val = $(this).val();
		$("<input type='hidden' name='" + name + "' value='" + val + "' />").appendTo($form);
	});

	$(":checkbox:checked[name]", _$form).each(function() {
		var name = $(this).attr("name");
		var val = $(this).val();
		$("<input type='hidden' name='" + name + "' value='" + val + "' />").appendTo($form);
	});

	// 連打防止
	var token = "" + new Date().getTime();
	$("<input />").attr({type:"hidden", name:"fileDownloadToken", value:token}).appendTo($form);
	if (interval === 0) {
		//待ち時間が指定されてない場合はCookieを監視
		$form.submit(function() {
			var fileDownloadChecker = setInterval(function() {
				var cookieValue = getCookie("fileDownloadToken");
				if (cookieValue === token) {
					clearInterval(fileDownloadChecker);
					setCookie("fileDownloadToken", null, -1);
					$(target).prop("disabled", false);
					$form.remove();
				}
			}, 1000);
		});
	} else {
		//一定期間経過後に解除
		$form.submit(function() {
			//タイマー動作中にダウンロード開始してるかCookieを監視
			var fileDownloadUnlockTimer = null;
			var fileDownloadChecker = setInterval(function() {
				var cookieValue = getCookie("fileDownloadToken");
				if (cookieValue === token) {
					//タイマーとcookie監視解除
					clearInterval(fileDownloadChecker);
					clearTimeout(fileDownloadUnlockTimer);
					setCookie("fileDownloadToken", null, -1);
					$(target).prop("disabled", false);
					$form.remove();
				}
			}, 1000);
			fileDownloadUnlockTimer = setTimeout(function() {
				//cookie監視解除
				clearInterval(fileDownloadChecker);
				$(target).prop("disabled", false);
				$form.remove();
			}, interval);
		});
	}

	$(target).prop("disabled", true);

	$form.submit();
}

/**
 * 新規作成
 * @param action
 * @param defName
 */
function createNewData(action, defName) {
	var searchCond = "";
	//ヘッダの新規作成の場合、searchCondは存在しない可能性があるのでチェック
	if ($(":hidden[name='searchCond']").length) {
		searchCond = $(":hidden[name='searchCond']").val();
	}
	submitForm(contextPath + "/" + action, {searchCond:searchCond});
}

function showDetail(action, oid, version, isEdit, target, options) {
	var searchCond = $(":hidden[name='searchCond']").val();
	var defaults = {version:version, searchCond:searchCond};
	var url = contextPath + "/" + action;
	if (isEdit) {
		defaults.oid = oid;
	} else {
		url += "/" + oid;
	}
	var params = $.extend(defaults, options);
	submitForm(url, params, target);
}

/**
 * QueryStringをdecodeする
 * @param query
 * @returns
 */
function decodeQueryString(query) {
	var ret = "";
	if (query != null && query.length > 0 && query.charAt(0) != "&") {
		query = "&" + query;
	}
	var sr = new StringReader(query);
	var c = -1;
	while (true) {
		c = sr.read();
		if (c == -1) {
			ret = ret.slice(1);
			break;
		}
		if (c == "&") {
			var kv = parseKeyValue(sr);
			if (kv != null) {
				var k = decodeURIComponent(kv.key);
				var v = (kv.val != null) ? decodeURIComponent(kv.val) : undefined;
				//parseKeyValueの処理に併せて、&なら&&にエスケープする
				if (typeof v !== "undefined" && v.indexOf("&") != -1) v = v.replace(/\&/g, "&&");
				ret += "&" + k + "=" + (typeof v === "undefined" ? "" : v);
			}
		}
	}
	return ret;
}

/**
 * searchCond解析
 * @param query
 * @returns {Array}
 */
function parseSearchCond(query) {
	var ret = new Array();
	if (query != null && query.length > 0 && query.charAt(0) != "&") {
		query = "&" + query;
	}
	var sr = new StringReader(query);
	var c = -1;
	while (true) {
		c = sr.read();
		if (c == -1) {
			break;
		}
		if (c == "&") {
			var kv = parseKeyValue(sr);
			if (kv != null) ret.push(kv);
		}
	}
	return ret;
}

/**
 * queryを解析してkey:valueに分解
 * @param sr
 * @returns
 */
function parseKeyValue(sr) {
	var ret = {};
	var key = true;
	var val = false;
	var amp = false;
	var buf = new StringBuilder();
	while (true) {
		var c = sr.read();
		if (key) {
			if (c == -1) {
				//keyの途中で終わり(あるか?)
				return null;
			} else if (c == "=") {
				//key終わり、valの解析に
				ret.key = buf.toString();
				key = false;
				val = true;
				amp = false;
				buf = new StringBuilder();
			} else if (c == "&") {
				c = sr.read();
				if (c == "&") {
					//&&なら&で文字列扱いに
					buf.append("&");
				} else {
					//key(&～=)の間に&があったらキー作り直し
					buf = new StringBuilder();
				}
			} else {
				buf.append(c);
			}
		} else if (val) {
			if (c == -1) {
				ret.val = buf.toString();
				break;
			} else if (c == "&") {
				c = sr.read();
				if (c == "&") {
					//&&なら&で文字列扱いに
					buf.append("&");
				} else {
					//次のキーの開始位置
					sr.back();
					ret.val = buf.toString();
					break;
				}
			} else {
				buf.append(c);
			}
		}
	}
	return ret;
}

function StringReader(str) {
	var _str = str;
	var _length = str.length;
	var _next = 0;
	StringReader.prototype.read = function() {
		if (_next >= _length) return -1;
		return _str.charAt(_next++);
	}
	StringReader.prototype.back = function() {
		_next -= 2;
	}
	StringReader.prototype.pos = function() {
		return _next;
	}
}

function StringBuilder() {
	var _buf = new Array();
	StringBuilder.prototype.length = function() {
		return _buf.length;
	}
	StringBuilder.prototype.append = function(str) {
		_buf.push(str);
	}
	StringBuilder.prototype.toString = function() {
		return _buf.join("");
	}
}

/**
 * 配列タイプのプロパティから指定の入力欄を削除
 * @param id
 * @return
 */
function deleteItem(id) {
	$("#" + es(id)).remove();
	$(".fixHeight").fixHeight();
}

/**
 * 配列タイプで要素の追加が可能かチェック
 * @param ulId
 * @param multiplicity
 * @return
 */
function canAddItem(ulId, multiplicity) {
	if (multiplicity == -1) return true;
	if ($("#" + ulId + " li").length < multiplicity) return true;
	return false;
}

function clone($src, id) {
	var $copy = $src.clone().attr("id", id).css("display", "block");
	$src.parent().append($copy);
	return $copy;
}

function countUp(countId, func) {
	var count = $("#" + countId).val() - 0;
	func.call(this, count);
	$("#" + countId).val(count + 1);
}

function setCookie(name, value, days) {
	var path = contextPath;
	if (typeof path === "undefined" || path === null || path === "") {
		path = "/";
	}
	var str = name + "=" + escape(value) + ";path=" + path + ";";
	if (days != 0) {
		var dt = new Date();
		dt.setDate(dt.getDate() + days);
		str += "expires=" + dt.toGMTString() + ";";
	}
	document.cookie = str;
}

function getCookie(name) {
	var split_len = name.length + 1;
	var ary_cookie = document.cookie.split("; ");
	var str = "";
	for (var i = 0; ary_cookie[i]; i++){
		if (ary_cookie[i].substr(0, split_len) == name + "=") {
			str = ary_cookie[i].substr(split_len, ary_cookie[i].length);
			break;
		}
	}
	return unescape(str);
}

/**
 * 検索に共通のValidatorの追加
 * @param validatorFunc
 */
function addCommonValidator(validatorFunc) {
	if (!scriptContext["validator"]) {
		scriptContext["validator"] = new Array();
	}
	scriptContext["validator"].push(validatorFunc);
}
/**
 * 通常検索Validatorの追加
 * @param validatorFunc
 */
function addNormalValidator(validatorFunc) {
	if (!scriptContext["normal_validation"]) {
		scriptContext["normal_validation"] = new Array();
	}
	scriptContext["normal_validation"].push(validatorFunc);
}
/**
 * 詳細検索Validatorの追加
 * @param validatorFunc
 */
function addDetailValidator(validatorFunc) {
	if (!scriptContext["detail_validation"]) {
		scriptContext["detail_validation"] = new Array();
	}
	scriptContext["detail_validation"].push(validatorFunc);
}

/**
 * 検索条件共通Validate
 * @returns {Boolean} true:OK
 */
function condCommonValidate(searchType) {
	var validators = scriptContext["validator"];
	if (validators && validators.length > 0) {
		for (var i = 0; i < validators.length; i++) {
			if (!validators[i].call(this)) return false;
		}
	}
	return true;
}

/**
 * 検索条件タイプ別Validate
 * @param searchType 検索タイプ
 * @returns {Boolean} true:OK
 */
function condTypeValidate(searchType) {
	var validators = scriptContext[searchType + "_validation"];
	if (validators && validators.length > 0) {
		for (var i = 0; i < validators.length; i++) {
			if (!validators[i].call(this)) return false;
		}
	}
	return true;
}

function normalRequiresAtLeastOneFieldValidate(message, param) {
	var isEmpty = true;

	for (var i = 0; i < param.properties.length; i++) {
		$("[name='sc_" + es(param.properties[i]) + "']").each(function() {
			if ($(this).is(":radio") || $(this).is(":checkbox")) {
				if ($(this).is(":checked")) {
					var val = $(this).val();
					if (typeof val !== "undefined" && val !== null && val !== "") {
						isEmpty = false;
						return false;
					}
				}
			} else {
				var val = $(this).val();
				if (typeof val !== "undefined" && val !== null && val !== "") {
					isEmpty = false;
					return false;
				}
			}
		});
	}

	if (isEmpty) {
		alert(message);
		return false;
	}
	return true;
}

function detailRequiresAtLeastOneFieldValidate(message, param) {
	var isEmpty = true;

	for (var i = 0; i < param.properties.length; i++) {
		var propName = param.properties[i];
		$("select[name^='dtlCndPropNm']").each(function() {
			var $prop = $(this);
			if ($("option:selected", $prop).val() === propName) {
				//項目は選択
				var $cond = $prop.parent().next().children("select");
				if ($cond.val() === "Null" || $cond.val() === "NotNull") {
					// null or not null
					isEmpty = false;
					return false;
				} else {
					// null / not null 以外
					var $value = $cond.parent().next().children("input");
					var val = $value.val();
					if (typeof val !== "undefined" && val !== null && val !== "") {
						isEmpty = false;
						return false;
					}
				}
			}
		});
	}

	if (isEmpty) {
		alert(message);
		return false;
	}
	return true;
}

/**
 * 通常検索条件のリセット用Functionを追加します。
 * @param resetFunc
 */
function addNormalConditionItemResetHandler(resetFunc) {
	if (!scriptContext["normal_resetter"]) {
		scriptContext["normal_resetter"] = new Array();
	}
	scriptContext["normal_resetter"].push(resetFunc);
}

/**
 * 通常検索条件のカスタマイズリセット用Functionを追加します。
 * 標準のリセット処理後に呼び出します。
 * TemplateやScriptなどで独自に入力値を制御している場合に利用します。
 *
 * @param resetFunc
 */
function addNormalConditionItemCustomResetHandler(resetFunc) {
	if (!scriptContext["normal_custom_resetter"]) {
		scriptContext["normal_custom_resetter"] = new Array();
	}
	scriptContext["normal_custom_resetter"].push(resetFunc);
}

/**
*
* 通常検索項目リセット
*/
function resetNormalCondition() {
	var curData = $("[name='normalForm']").serialize();
	var reset = false;
	var resetters = scriptContext["normal_resetter"];
	if (resetters && resetters.length > 0) {
		for (var i = 0; i < resetters.length; i++) {
			resetters[i].call(this);
		}
		reset = true;
	}
	//通常のリセットのあとにカスタム呼び出し
	var customs = scriptContext["normal_custom_resetter"];
	if (customs && customs.length > 0) {
		for (var i = 0; i < customs.length; i++) {
			customs[i].call(this);
		}
		reset = true;
	}
	if (reset) {
		var resetData = $("[name='normalForm']").serialize();
		if (curData != resetData) {
			$(".chagne-condition").show();
		}
		//ReferencePropertyEditorなど要素が増える可能性があるのでfixHeight実行
		$(".fixHeight").fixHeight();
	}
}

/**
 * 詳細検索 行追加
 */
function addDetailCondition() {
	countUp("id_detailConditionCount", function(count) {
		var $table = $(".data-deep-search tbody");
		var $srcRow = $table.children("tr:first");
		var $copyRow = cloneDetailConditionRow($srcRow, "detailCond_" + count).removeAttr("style");

		$copyRow.children("td:nth-child(1)").text(count + 1 + ".");

		$copyRow.children("td:nth-child(2)").children("select").attr("onchange", "propChange(this, '" + count + "')");
		$copyRow.children("td:nth-child(2)").children("select")
			.attr("name","dtlCndPropNm_" + count).each(function() {
			    this.selectedIndex  = 0;
			});
		$copyRow.children("td:nth-child(3)").children("select")
			.attr("name", "dtlCndPrdct_" + count).each(function() {
				this.selectedIndex  = 0;
			});
		$copyRow.children("td:nth-child(4)").attr("id", "dtlCndVl_" + count);
		$copyRow.children("td:nth-child(4)").children(":text")
			.attr("name", "dtlCndVl_" + count)
			.val("")
			.removeAttr("onblur");	//1行目が日付系のPropertyを選択されていた場合を考慮
		$copyRow.children("td:nth-child(4)").children(":hidden").remove();	//1行目が日付系のPropertyを選択されていた場合を考慮
	});
}

/**
 * 詳細検索 行コピー
 */
function cloneDetailConditionRow($src, id) {
	var $copy = $src.clone().attr("id", id).css("display", "block");
	$('.data-deep-search tbody tr:last').after($copy);
	return $copy;
}

/**
 * 詳細検索 行削除
 * @param img
 */
function deleteDetailCondition(img) {
	if ($(".data-deep-search tbody tr").length == 1) {
		$(".data-deep-search select").each(function() {
			$(this).val($("option:first", $(this)).val());
		});
		var $valueCell = $(".data-deep-search tbody tr:first td:nth-child(4)");
		$(":text", $valueCell).removeAttr("onblur").val("");
		$(":hidden", $valueCell).remove();
	} else {
		$(img).parents("tr").remove();
	}
}
/**
 *
 * 詳細検索項目リセット
 */
function resetDetailCondition() {
	var curData = $("[name='detailForm']").serialize();

	var $deepSearchArea = $(".data-deep-search");
	//全部削除(1行だけ残る)
	$("tbody tr .delete", $deepSearchArea).each(function() {
		deleteDetailCondition(this);
	});
	//Counterの初期化
	$("#id_detailConditionCount").val(0);

	//5行分追加
	for (var i = 0; i < 5; i++) {
		addDetailCondition();
	}
	//先頭行を削除(番号が不正のため)
	$("tbody",$deepSearchArea).children("tr:first").remove();

	//Footerのクリア
	var $submitArea = $("tfoot tr.submit-area", $deepSearchArea);
	$(":radio[name='dtlCndExpr']", $submitArea).val([$(":radio[name='dtlCndExpr']:first", $submitArea).val()]);
	$(":text[name='dtlCndFilterExpression']", $submitArea).val("");

	var resetData = $("[name='detailForm']").serialize();
	if (curData != resetData) {
		$(".chagne-condition").show();
	}

	$(".fixHeight").fixHeight();
}

////////////////////////////////////////////////////////
//String・数値用のJavascript
////////////////////////////////////////////////////////

/**
 * テキスト行追加
 * @param ulId
 * @param multiplicity
 * @param liId
 * @param propName
 * @param countId
 * @param selector
 * @return
 */
function addTextItem(ulId, multiplicity, liId, propName, countId, selector, func) {
	if (canAddItem(ulId, multiplicity)) {
		countUp(countId, function(count){
			var $copy = copyText(liId, propName, count, selector);
			if ($(":text", $copy).hasClass("commaFieldDummy")) {
				$(":text", $copy).removeClass("commaFieldDummy").addClass("commaField");
				$(":text", $copy).commaField();
			}
			if (func && $.isFunction(func)) func.call(this, $copy.children(selector));
		});
		$(".fixHeight").fixHeight();
	}
}

/**
 * Stringの配列型プロパティにテキストの入力欄追加
 * @param liId
 * @param propName
 * @param idx
 * @param selector
 * @return
 */
function copyText(liId, propName, idx, selector) {
	var $src = $("#" + liId);
	var copyId = "li_" + propName + idx;
	var $copy = clone($src, copyId);

	var $text = $(selector, $copy);
	var $button = $(":button", $copy);

	$text.attr("name", propName);
	$button.click(function() {deleteItem(copyId);});

	return $copy;
}

/**
 * 数値チェック
 */
function numcheck(element) {
	var v = element.value;
	//空白のみの場合は空をセット
	if (v.trim().length == 0) {
		element.value = "";
		return;
	}
	//数値変換可能かをチェック
	if (isNaN(v)) {
		alert(scriptContext.locale.numcheckMsg);
		element.value = "";
		return;
	}

	//前後の空白や0を消すためparseした値をセット
	//js自体の変換を利用すると、指数表記になったりするので、文字列操作でトリムする
	//※この時点で値が数値である事は保証済み
	element.value = trimZeroDecimalPart(trimZeroIntegerPart(v.trim()));
}

/**
 * 整数部の先頭の0をトリム
 * @param v 数値文字列
 * @returns トリム後の数値
 */
function trimZeroIntegerPart(v) {
	if (!v) return null;

	var array = v.split(".");

	// 整数部の先頭0を削除
	while (array[0].startsWith("0")) {
		array[0] = array[0].slice(1);
	}

	if (array[0] === null || array[0] === "") {
		array[0] = "0";
	}

	return array.join(".");
}

/**
 * 小数部の末尾の0をトリム
 * @param v 数値文字列
 * @returns トリム後の数値
 */
function trimZeroDecimalPart(v) {
	if (!v) return null;

	var array = v.split(".");
	if (array.length < 2) return v;// 整数部のみ

	// 小数部の末尾0を削除
	while (array[1].endsWith("0")) {
		array[1] = array[1].slice(0, -1);
	}

	// 何もない場合は要素自体消しておく
	if (array[1] === null || array[1] === "") {
		array = array.slice(0, 1);
	}

	return array.join(".");
}

////////////////////////////////////////////////////////
//日付・時間用のJavascript
////////////////////////////////////////////////////////

/**
 * 汎用画面 日付型の変更イベント
 *
 * @param id
 * @return
 */
function dateChange(id) {
	var $date = $("#d_" + es(id));
	var $hidden = $("#i_" + es(id));

	dateToHidden($date, $hidden);
}

/**
 * 汎用画面 日付入力のinput値をもとに送信用形式でhiddenに設定する
 *
 * @param $date date用input
 * @param $hidden hidden
 */
function dateToHidden($date, $hidden) {
	if ($date.val() == "") {
		$hidden.val("");
	} else {
		$hidden.val(convertFromLocaleDateString($date.val()));
	}
}

/**
 * 汎用画面 日付行追加
 * 多重度が複数の場合のDatePropertyEditorの追加ボタン処理
 *
 * @param ulId
 * @param multiplicity
 * @param dummyRowId
 * @param propName
 * @param countId
 * @return
 */
function addDateItem(ulId, multiplicity, dummyRowId, propName, countId) {
	if (canAddItem(ulId, multiplicity)) {
		countUp(countId, function(count) {
			copyDate(dummyRowId, propName, count);
		});
		$(".fixHeight").fixHeight();
	}

	function copyDate(dummyRowId, propName, idx) {
		var newId = propName + idx;
		var copyId = "li_" + newId;

		//ソースをコピー
		var $src = $("#" + dummyRowId);
		var $copy = clone($src, copyId);

		//input
		var $text = $(":text", $copy);
		var $hidden = $(":hidden:last", $copy);
		var $button = $(":button", $copy);

		//inputのidを設定
		$text.attr("id", "d_" + newId);
		//hiddenにnameとidを指定
		$hidden.attr({name: propName, id: "i_" + newId});

		//onChangeを設定
		$text.change(function() {dateChange(newId);});

		//削除ボタンのclickを設定
		$button.click(function() {deleteItem(copyId);});

		datepicker($text);
	}
}

/**
 * 日付型の検証
 *
 * @param val        (必須)値(null可)
 * @param dateFormat (必須)入力フォーマット(exDateFormat)
 * @param errMessage (任意)エラー時のメッセージ
 */
function validateDate(val, dateFormat, errMessage) {

	if (typeof dateFormat === "undefined") {
		throw "dateFormat is required.";
	}

	if (val == null || val == "") {
		return true;
	}

	if (val.length != dateFormat.length) {
		//フォーマットエラー
		throw getErrorMessage();
	} else {
		if (!new moment(val, dateFormat).isValid()) {
			//値エラー
			throw getErrorMessage();
		}
	}

	return true;

	function getErrorMessage() {
		if (typeof errMessage === "undefined" || errMessage == null || errMessage == "") {
			errMessage = scriptContext.locale.validateDateErrMsg;
		}
		return messageFormat(errMessage, dateFormat);
	}
}

/**
 * 汎用画面 時間型(Select型)の変更イベント
 * @param id
 * @return
 */
function timeSelectChange(id) {
	var $hour = $("#h_" + es(id));
	var $min = $("#m_" + es(id));
	var $sec = $("#s_" + es(id));
	var $hidden = $("#i_" + es(id));

	timeSelectToHidden($hour, $min, $sec, $hidden);
}

/**
 * 汎用画面 時間型(Select型)のinput値をもとに送信用形式でhiddenに設定する
 *
 * @param $hour 時用input
 * @param $min  分用input
 * @param $sec  秒用input
 * @param $hidden hidden
 */
function timeSelectToHidden($hour, $min, $sec, $hidden) {
	var bHour = $hour.val() == "  ";
	var bMin = $min.is(":hidden") || $min.val() == "  ";
	var bSec = $sec.is(":hidden") || $sec.val() == "  ";

	if (bHour && bMin && bSec) {
		//全て未入力もしくは非表示なら空で
		$hidden.val("");
	} else {
		//一つでも表示されてる項目に値があれば連結
		$hidden.val($hour.val() + $min.val() + $sec.val() + "000");
	}
}

/**
 * 汎用画面 時間型(Select型)入力行追加
 * 多重度が複数の場合のTimePropertyEditorの追加ボタン処理
 *
 * @param ulId
 * @param multiplicity
 * @param dummyRowId
 * @param propName
 * @param countId
 * @return
 */
function addTimeSelectItem(ulId, multiplicity, dummyRowId, propName, countId) {
	if (canAddItem(ulId, multiplicity)) {
		countUp(countId, function(count) {
			copyTime(dummyRowId, propName, count);
		});
		$(".fixHeight").fixHeight();
	}

	function copyTime(dummyRowId, propName, idx) {
		var newId = propName + idx;
		var copyId = "li_" + newId;

		//ソースをコピー
		var $src = $("#" + dummyRowId);
		var $copy = clone($src, copyId);

		var $select = $("label select", $copy);
		var $hidden = $(":hidden:last", $copy);
		var $button = $(":button", $copy);

		var selId = new Array("h_" + newId, "m_" + newId, "s_" + newId);

		// プルダウンの設定
		var i = 0;
		$select.change(function() {
			timeSelectChange(newId);
		});

		//inputのidを設定
		$select.each(function() {
			$(this).attr("id", selId[i]);
			i++;
		});
		//inputのidを設定(非表示の場合の設定)
		$(":hidden:not(:last)", $copy).each(function() {
			$(this).attr("id", selId[i]);
			i++;
		});

		//hiddenにnameとidを指定
		$hidden.attr({name: propName, id: "i_" + newId});

		//削除ボタンのclickを設定
		$button.click(function() {deleteItem(copyId);});
	}
}


/**
 * 汎用画面 時間型(Timepicker型)の変更イベント
 * @param id
 */
function timePickerChange(id) {
	var $time = $("#time_" + es(id));
	var $hidden = $("#i_" + es(id));

	timePickerToHidden($time, $hidden);
}

/**
 * 汎用画面 時間型(Timepicker型)のinput値をもとに送信用形式でhiddenに設定する
 *
 * @param $hour Picker用input
 * @param $hidden hidden
 */
function timePickerToHidden($time, $hidden) {

	if ($time.val() == "") {
		$hidden.val("");
	} else {
		var fixedMin = $time.attr("data-fixedMin") || "";
		var fixedSec = $time.attr("data-fixedSec") || "";
		var fixedMSec = $time.attr("data-fixedMSec") || "000";
		$hidden.val($time.val().split(":").join("") + fixedMin + fixedSec + fixedMSec);
	}

}

/**
 * 汎用画面 時間型(Timepicker型)入力行追加
 * 多重度が複数の場合のTimePropertyEditorの追加ボタン処理
 *
 * @param ulId
 * @param multiplicity
 * @param dummyRowId
 * @param propName
 * @param countId
 * @return
 */
function addTimePickerItem(ulId, multiplicity, dummyRowId, propName, countId) {
	if (canAddItem(ulId, multiplicity)) {
		countUp(countId, function(count) {
			copyTimePicker(dummyRowId, propName, count);
		});
		$(".fixHeight").fixHeight();
	}

	//function copyTimePicker(dummyRowId, propName, idx, timeformat, maxlength, stepmin) {
	function copyTimePicker(dummyRowId, propName, idx) {

		var newId = propName + idx;
		var copyId = "li_" + newId;

		//ソースをコピー
		var $src = $("#" + dummyRowId);
		var $copy = clone($src, copyId);

		var $text = $(":text", $copy);
		var $hidden = $(":hidden:last", $copy);
		var $button = $(":button", $copy);

		//onChangeを設定
		$text.attr("id", "time_" + newId).change(function() {
			timePickerChange(newId);
		});

		//hiddenにnameとidを指定
		$hidden.attr({name : propName,id : "i_" + newId});

		//削除ボタンのclickを設定
		$button.click(function() {deleteItem(copyId);});

		timepicker($text);
	}
}

/**
 * 時間型の検証
 *
 * @param val        (必須)値(null可)
 * @param timeFormat (必須)入力フォーマット
 * @param fixedMin   (任意)固定Minute値
 * @param fixedSec   (任意)固定Second値
 * @param errMessage (任意)エラー時のメッセージ
 */
function validateTimePicker(val, timeFormat, fixedMin, fixedSec, errMessage) {

	if (typeof timeFormat === "undefined") {
		throw "timeFormat is required.";
	}

	if (val == null || val == "") {
		return true;
	}

	fixedMin = fixedMin || "";
	fixedSec = fixedSec || "";

	var timeVal = val.split(":").join("") + fixedMin + fixedSec;
	if (timeVal.length != 6 || numCheck(timeVal) == true) {
		throw getErrorMessage();
	}

	return true;

	function getErrorMessage() {
		if (typeof errMessage === "undefined" || errMessage == null || errMessage == "") {
			errMessage = scriptContext.locale.validateTimeErrMsg;
		}
		return messageFormat(errMessage, timeFormat);
	}
}

/**
 * 汎用画面 日時型(Select型)の変更イベント
 * @param id
 * @return
 */
function timestampSelectChange(id) {
	var $date = $("#d_" + es(id));
	var $hour = $("#h_" + es(id));
	var $min = $("#m_" + es(id));
	var $sec = $("#s_" + es(id));
	var $msec = $("#ms_" + es(id));
	var $hidden = $("#i_" + es(id));

	timestampSelectToHidden($date, $hour, $min, $sec, $msec, $hidden);
}

/**
 * 汎用画面 日時型(Select型)のinput値をもとに送信用形式でhiddenに設定する
 *
 * @param $hour 時用input
 * @param $min  分用input
 * @param $sec  秒用input
 * @param $hidden hidden
 */
function timestampSelectToHidden($date, $hour, $min, $sec, $msec, $hidden) {
	var bDate = $date.val() == "";
	var bHour = $hour.is(":hidden") || $hour.val() == "  ";
	var bMin = $min.is(":hidden") || $min.val() == "  ";
	var bSec = $sec.is(":hidden") || $sec.val() == "  ";

	if (bDate && bHour && bMin && bSec) {
		//全て未入力もしくは非表示なら空で
		$hidden.val("");
	} else {
		//一つでも表示されてる項目に値があれば連結

		var date = $date.val();
		if ($date.val() == "") {
			date = "        ";
		} else {
			// formatをyyyymmddに
			date = convertFromLocaleDateString(date);
		}

		$hidden.val(date + $hour.val() + $min.val() + $sec.val() + $msec.val());
	}
}


/**
 * 汎用画面 日時型(Select型)入力行追加
 * 多重度が複数の場合のTimestampPropertyEditorの追加ボタン処理
 *
 * @param ulId
 * @param multiplicity
 * @param liId
 * @param propName
 * @param countId
 * @return
 */
function addTimestampSelectItem(ulId, multiplicity, dummyRowId, propName, countId) {
	if (canAddItem(ulId, multiplicity)) {
		countUp(countId, function(count) {
			copyTimestamp(dummyRowId, propName, count);
		});
		$(".fixHeight").fixHeight();
	}

	function copyTimestamp(dummyRowId, propName, idx) {
		var newId = propName + idx;
		var copyId = "li_" + newId;

		//ソースをコピー
		var $src = $("#" + dummyRowId);
		var $copy = clone($src, copyId);

		var $text = $(":text", $copy);
		var $select = $("label select", $copy);
		var $hidden = $(":hidden:last", $copy);
		var $button = $(":button", $copy);

		var selId = new Array("h_" + newId, "m_" + newId, "s_" + newId, "ms_" + newId);

		// 日付の設定
		$text.attr("id", "d_" + newId).change(function() {
			timestampSelectChange(newId);
		});

		// プルダウンの設定
		var i = 0;
		$select.change(function() {
			timestampSelectChange(newId);
		});

		//inputのidを設定
		$select.each(function() {
			$(this).attr("id", selId[i]);
			i++;
		});
		//inputのidを設定(非表示の場合の設定)
		$(":hidden:not(:last)", $copy).each(function() {
			$(this).attr("id", selId[i]);
			i++;
		});

		//hiddenにnameとidを指定
		$hidden.attr({name : propName, id : "i_" + newId});

		//削除ボタンのclickを設定
		$button.click(function() {deleteItem(copyId);});

		$text.applyDatepicker();
	}
}

/**
 * 汎用画面 日時型(Datetimepicker型)の変更イベント
 * @param id
 * @return
 */
function timestampPickerChange(id) {

	var $date = $("#datetime_" + es(id));
	var $hidden = $("#i_" + es(id));

	timestampPickerToHidden($date, $hidden);
}

/**
 * 汎用画面 日時型(Datetimepicker型)のinput値をもとに送信用形式でhiddenに設定する
 *
 * @param $hour Picker用input
 * @param $hidden hidden
 */
function timestampPickerToHidden($date, $hidden) {

	if ($date.val() == "") {
		$hidden.val("");
	} else {
		var fixedMin = $date.attr("data-fixedMin") || "";
		var fixedSec = $date.attr("data-fixedSec") || "";
		var fixedMSec = $date.attr("data-fixedMSec") || "000";
		$hidden.val(convertFromLocaleDatetimeString($date.val() + fixedMin + fixedSec) + fixedMSec);
	}

}

/**
 * 汎用画面 日時型(Datetimepicker型)入力行追加
 * 多重度が複数の場合のTimestampPropertyEditorの追加ボタン処理
 *
 * @param ulId
 * @param multiplicity
 * @param liId
 * @param propName
 * @param countId
 * @return
 */
function addTimestampPickerItem(ulId, multiplicity, dummyRowId, propName, countId) {
	if (canAddItem(ulId, multiplicity)) {
		countUp(countId, function(count) {
			copyTimestampPicker(dummyRowId, propName, count);
		});
		$(".fixHeight").fixHeight();
	}

	function copyTimestampPicker(dummyRowId, propName, idx) {

		var newId = propName + idx;
		var copyId = "li_" + newId;

		//ソースをコピー
		var $src = $("#" + dummyRowId);
		var $copy = clone($src, copyId);

		var $text = $(":text", $copy);
		var $hidden = $(":hidden:last", $copy);
		var $button = $(":button", $copy);

		//onChangeを設定
		$text.attr("id", "datetime_" + newId).change(function() {
			timestampPickerChange(newId);
		});

		//hiddenにnameとidを指定
		$hidden.attr({name : propName, id : "i_" + newId});

		//削除ボタンのclickを設定
		$button.click(function() {deleteItem(copyId);});

		datetimepicker($text);
	}
}

/**
 * 日時型の検証
 *
 * @param val        (必須)値(null可)
 * @param dateFormat (必須)日付入力フォーマット(exDateFormat)
 * @param timeFormat (必須)時間入力フォーマット
 * @param fixedMin   (任意)固定Minute値
 * @param fixedSec   (任意)固定Second値
 * @param errMessage (任意)エラー時のメッセージ
 */
function validateTimestampPicker(val, dateFormat, timeFormat, fixedMin, fixedSec, errMessage) {

	if (typeof dateFormat === "undefined") {
		throw "dateFormat is required.";
	}
	if (typeof timeFormat === "undefined") {
		throw "timeFormat is required.";
	}

	if (val == null || val == "") {
		return true;
	}

	if (val.length != (dateFormat.length + 1 + timeFormat.length)) {
		//フォーマットエラー
		throw getErrorMessage();
	}

	//日時分割
	var dateStr = val.substring(0, dateFormat.length);
	var timeStr = val.substring(dateFormat.length + 1);

	var message = getErrorMessage();

	//日付検証
	validateDate(dateStr, dateFormat, message);

	//時間検証
	validateTimePicker(timeStr, timeFormat, fixedMin, fixedSec, message);

	return true;

	function getErrorMessage() {
		if (typeof errMessage === "undefined" || errMessage == null || errMessage == "") {
			errMessage = scriptContext.locale.validateTimestampErrMsg;
		}
		return messageFormat(errMessage, dateFormat + " " + timeFormat);
	}
}

/**
 * 入力と出力のフォーマットを指定して日付文字列を変換する
 *
 * @param str
 * @param inFormat
 * @param outFormat
 * @returns format文字列
 */
function convertDateString(str, inFormat, outFormat) {
	if (str == "") {
		return str;
	}

	var date = dateUtil.newFormatString(str, inFormat, outFormat);
	return date;
}


/**
 * 指定のフォーマットの文字列をLocaleとして指定されているフォーマットに変換する
 * フォーマット未指定時はYYYYMMDD形式になる
 *
 * @param str
 * @param inFormat
 * @returns format文字列
 */
function convertToLocaleDateString(str, inFormat) {
	var outFormat = dateUtil.getInputDateFormat();
	if (inFormat == null || inFormat == "") {
		inFormat = "YYYYMMDD"
	}

	return convertDateString(str, inFormat, outFormat);
}

/**
 * 指定のフォーマットの文字列をLocaleとして指定されているフォーマットに変換する
 * フォーマット未指定時はYYYYMMDDHHmmss形式になる
 *
 * @param str
 * @param inFormat
 * @returns format文字列
 */
function convertToLocaleDatetimeString(str, inFormat, range) {
	var outFormat = dateUtil.getInputDatetimeFormat("sec");
	if (range == 'HOUR') {
		outFormat = dateUtil.getInputDatetimeFormat("hour");
	} else if (range == 'MIN') {
		outFormat = dateUtil.getInputDatetimeFormat("min");
	} else if (range == 'SEC') {
		outFormat = dateUtil.getInputDatetimeFormat("sec");
	} else if (range == 'NONE') {
		outFormat = dateUtil.getInputDateFormat();
	}

	if (inFormat == null || inFormat == "") {
		inFormat = "YYYYMMDDHHmmss";
	}

	return convertDateString(str, inFormat, outFormat);
}

/**
 * Localeとして指定されている文字列を指定のフォーマットに変換する
 * フォーマット未指定時はYYYYMMDD形式になる
 *
 * @param str
 * @param format
 * @returns format文字列
 */
function convertFromLocaleDateString(str, inFormat) {
	if (inFormat == null || inFormat == "") {
		inFormat = dateUtil.getInputDateFormat();
	}

	return convertDateString(str, inFormat, "YYYYMMDD");
}

/**
 * Localeとして指定されている文字列を指定のフォーマットに変換する
 * フォーマット未指定時はYYYYMMDDHHmmss形式になる
 *
 * @param str
 * @param format
 * @returns format文字列
 */
function convertFromLocaleDatetimeString(str, inFormat) {
	if (inFormat == null || inFormat == "") {
		inFormat = dateUtil.getInputDatetimeFormat("sec");
	}

	return convertDateString(str, inFormat, "YYYYMMDDHHmmss");
}

////////////////////////////////////////////////////////
//バイナリ型用のJavascript
////////////////////////////////////////////////////////

function uploadFile(file, token) {
	var $file = $(file);

	var fileId = $file.attr("id");
	var propName = $file.attr("data-pname");
	var max = $file.attr("data-multiplicity") - 0;
	var displayType = $file.attr("data-displayType");
	var fileCount = 0;
	var completeCount = 0

	var $img = $("#img_" + es(propName));
	var $span = $("#em_" + es(propName)).text("");
	$span.hide();

	var option = {};
	if (typeof token !== "undefined" && token != null && token != "") {
		option.formData = {"_t": token};
	}
	$.extend(option, {
		url: $file.attr("data-uploadUrl"),
		dataType: "xml",
		dropZone: $file.parent(),
		change: function(e, data) {//ファイル選択後に1回
			return preSend(e, data);
		},
		drop: function(e, data) {//ファイル選択後に1回
			return preSend(e, data);
		},
		done: function(e, data) {//ファイル単位で発生
			var $_file = $("#" + es(fileId));//plugin内でcloneされ、$fileが別物になる
			completeCount++;

			if (data.result != null) {
				var result = $(data.result).find("Result").text();
				if (result == null || result == "") {
					$span.text(scriptContext.locale.failedToFileUpload).show();
				} else {
					var error = $(data.result).find("error").text();
					if (error == null || error == "") {
						var lobId = $(data.result).find("lobId").text();
						var binaryName = $(data.result).find("name").text();
						var type = $(data.result).find("type").text();

						var count = $_file.attr("data-binCount") - 0;
						addBinaryGroup(propName, count, fileId, binaryName, type, lobId, displayType, null, true);
						$_file.attr("data-binCount", count);

						$("[name='" + es(propName) + "']:eq(0)").trigger("change", {});
					} else {
						$span.text(error).show();
					}
				}
			} else {
				$span.text(scriptContext.locale.failedToFileUpload).show();
			}

			//全て完了(成功or失敗)してから入力欄コントロール
			if (fileCount === completeCount) {
				$img.hide();

				var uploaded = $("#ul_" + es(propName)).children("li").length;
				if (uploaded < max) {
					// file表示
					$_file.show();
				}
			}
		}
	});

	// change,dropから呼ばれる
	function preSend(e, data) {
		fileCount = data.files.length;
		completeCount = 0;

		var uploaded = $("#ul_" + es(propName)).children("li").length;

		if (fileCount + uploaded > max) {
			alert(messageFormat(scriptContext.locale.filesLimitOver, max));
			return false;
		}

		var $_file = $("#" + es(fileId));//plugin内でcloneされ、$fileが別物になる
		$_file.hide();
		$span.text("").hide();
		$img.show();
		return true;
	}

	$file.fileupload(option);
}

/**
 * バイナリデータ追加
 *
 * @param propName
 * @param count
 * @param brName
 * @param brType
 * @param brLobId
 * @param displayType
 * @return
 */
function addBinaryGroup(propName, count, fileId, brName, brType, brLobId, displayType, param, fieldControlFlg) {
	//参照で追加した行で、なぜかjQueryのセレクタでelementが取れない
	var $input = $("#" + es(fileId));
	var $ul = $("#ul_" + es(propName));
	var $li = $("<li id='li_" + propName + count + "'>").addClass("list-bin").appendTo($ul);

	var download = $input.attr("data-downloadUrl") + "?id=" + brLobId;
	var ref = $input.attr("data-refUrl") + "?id=" + brLobId;
	if (param != null) {
		download = download + param;
		ref = ref + param;
	}

	//リンク作成
	var usePdfjs = $input.attr("data-usePdfjs") === "true";
	var pdfviewer = $input.attr("data-pdfviewerUrl");
	if (displayType && (displayType == "BINARY" || displayType == "LINK")) {
		// 編集画面でリンククリックされるとバイナリが表示され、戻れない(ブラウザバックもリンク切れ)
		// 入力内容も消えてしまうので、少なくともアップロード時は別タブ表示にしておく
		if (brType.indexOf("application/pdf") > -1 && usePdfjs) {
			var pdfPath = pdfviewer + "?file=" + encodeURIComponent(download);
			$li.append("<a href='" + pdfPath + "' target='_blank'>" + brName + "</a> ");
		} else {
			$li.append("<a href='" + download + "' target='_blank'>" + brName + "</a> ");
		}
	}

	if (displayType && displayType == "PREVIEW") {
		$li.addClass("noimage");
	}

	$("<a href='javascript:void(0)' class='binaryDelete del-btn'> " + scriptContext.locale.deleteLink + "</a>").appendTo($li).click(function() {
		//liを削除
		$li.remove();

		//file表示
		$("#" + es(fileId)).show();//$input.show();だと表示されない場合がある

		$(".fixHeight").fixHeight();
	});

	if (displayType && (displayType == "BINARY" || displayType == "PREVIEW")) {
		if (brType && brType.indexOf("image") > -1) {
			//imageファイルの場合は画像を表示
			var $p = $("<p id='p_" + propName + count + "' class='mb0' />").appendTo($li);
			var $img = $("<img src='" + download + "' alt='" + brLobId + "' onload='imageLoad()'>").appendTo($p);
			var width = $input.attr("data-binWidth") - 0;
			if (width > 0) $img.attr("width", width);
			var height = $input.attr("data-binHeight") - 0;
			if (height > 0) $img.attr("height", height);
		} else if (brType && brType.indexOf("application/x-shockwave-flash") > -1) {
			// swfファイルの場合はアニメーションを表示
			var width = $input.attr("data-binWidth") - 0;
			var height = $input.attr("data-binHeight") - 0;
			var $p = $("<p id='p_" + propName + count + "' class='mb0' />").appendTo($li);
			var $img = $("<object data='" + ref + "' type='application/x-shockwave-flash' width='" + width + "' height='" + height + "' ><param name='movie' value='" + ref + "' /><param name='quality' value='high'>" + scriptContext.locale.notVewFlash + "</object>").appendTo($p);
		} else if (brType && brType.indexOf("video/mpeg") > -1) {
			// mpegファイルの場合は動画を表示
			var width = $input.attr("data-binWidth") - 0;
			var height = $input.attr("data-binHeight") - 0;
			var $p = $("<p id='p_" + propName + count + "' class='mb0' />").appendTo($li);
			var $img = $("<object classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' width='" + width + "' height='" + height + "'><param name='src' value='" + ref + "' /><param name='autostart' value='true' /><param name='type' value='video/mpeg'><param name='controller' value='true' /><embed src='" + ref + "' scale='ToFit' width='" + width + "' height='" + height + "'></embed></object>").appendTo($p);
		} else if (brType && brType.indexOf("video/quicktime") > -1) {
			// movファイルの場合は動画を表示
			var width = $input.attr("data-binWidth") - 0;
			var height = $input.attr("data-binHeight") - 0;
			var $p = $("<p id='p_" + propName + count + "' class='mb0' />").appendTo($li);
			var $img = $("<object classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' codebase='http://www.apple.com/qtactivex/qtplugin.cab' width='" + width + "' height='" + height + "'><param name='src' value='" + ref + "'><param name='autoplay' value='true'><param name='type' value='video/quicktime'><param name='scale' value='ToFit'><embed src='" + ref + "' autoplay='true' scale='ToFit' type='video/quicktime' pluginspage='http://www.apple.com/quicktime/download/' width='" + width + "' height='" + height + "'></object>").appendTo($p);
		} else if (brType && brType.indexOf("audio/mpeg") > -1) {
			// mp3の場合はaudioタグを利用
			createAudioElement($li, "audio/mpeg", propName + count, ref);
		} else if (brType && brType.indexOf("audio/webm") > -1) {
			// mp3の場合はaudioタグを利用
			createAudioElement($li, "audio/webm", propName + count, ref);
		} else if (brType && brType.indexOf("audio/ogg") > -1) {
			// oggの場合はaudioタグを利用
			createAudioElement($li, "audio/ogg", propName + count, ref);
		} else if (brType && brType.indexOf("video/mp4") > -1) {
			// mp4の場合はvideoタグを利用
			createVideoElement($li, $input, "video/mp4", propName + count, ref);
		} else if (brType && brType.indexOf("video/webm") > -1) {
			// webmの場合はvideoタグを利用
			createVideoElement($li, $input, "video/webm", propName + count, ref);
		} else if (brType && brType.indexOf("video/ogg") > -1) {
			// oggの場合は音声のみの場合もあるので拡張子で判断
			if (brName.indexOf(".") > -1 && brName.substr(brName.indexOf(".")).toLowerCase().indexOf("ogg") > -1) {
				createAudioElement($li, "audio/ogg", propName + count, ref);
			} else {
				createVideoElement($li, $input, "video/ogg", propName + count, ref);
			}
		} else if (brType && brType.indexOf("video/x-ms-wmv") > -1) {
			// wmvの場合はvideoタグを利用
			createVideoElement($li, $input, "video/wmv", propName + count, ref);
		} else if (brType && brType.indexOf("video/flv") > -1) {
			// flvの場合はvideoタグを利用
			createVideoElement($li, $input, "video/flv", propName + count, ref);
		} else if (brType && brType.indexOf("application/octet-stream") > -1) {
			// MIMEタイプが曖昧、拡張子で判断
			if (brName.indexOf(".") > -1) {
				if (brName.substr(brName.indexOf(".")).toLowerCase().indexOf("flv") > -1) {
					//拡張子flvの場合はvideo/flvとしてvideoタグを利用
					createVideoElement($li, $input, "video/flv", propName + count, ref);
				}
			}
		}
	}

	// hiddenタグ追加
	$("<input type='hidden' />").appendTo($li).attr("name", propName).val(brLobId);

	var multiple = $input.attr("data-multiplicity") - 0;
	var length = $ul.children().length;
	if (!fieldControlFlg && length < multiple) {
		// file表示
		$input.show();
	}

	// fileの入力値を空に
	$input.val("");

	$(".fixHeight").fixHeight();
}

function imageLoad() {
	setTimeout(function() {
		$(".fixHeight").fixHeight();
	}, 300);
}

/**
 * Videoタグを生成し、動画再生用のコンテンツを作成
 * @param $parent 親コンテナ
 * @param $input Fileフィールド
 * @param brType MIMEタイプ
 * @param id ユニークID
 * @param src 動画ファイルのURL
 */
function createVideoElement($parent, $input, brType, id, src) {
	var size = "";
	var width = $input.attr("data-binWidth") - 0;
	if (width > 0) size = "width=\"" + width + "\"";
	var height = $input.attr("data-binHeight") - 0;
	if (height > 0) size = size + "height=\"" + height + "\"";

	var $p = $("<p id='p_" + id + "' class='mb0' />").appendTo($parent);
	var $video = $("<video id='" + id + "' controls='controls'" + size + "/>").attr("src", src).appendTo($p);
	$video.mediaelementplayer();
}

/**
 * Audioタグを生成し、音声再生用のコンテンツを作成
 * @param $parent 親コンテナ
 * @param brType MIMEタイプ
 * @param id ユニークID
 * @param src 音声ファイルのURL
 */
function createAudioElement($parent, brType, id, src) {
	var $p = $("<p id='p_" + id + "' class='mb0' />").appendTo($parent);
	var $audio = $("<audio id='" + id + "' controls='controls' />").attr("src", src).appendTo($p);
	$audio.mediaelementplayer();
}

////////////////////////////////////////////////////////
// 参照型用のJavascript
////////////////////////////////////////////////////////

function showReference(viewAction, defName, oid, version, linkId, refEdit, editCallback) {

	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);

	document.scriptContext["editReferenceCallback"] = function(entity) {
		//callbackが指定されていたら呼び出し
		if (editCallback && $.isFunction(editCallback)) {
			editCallback.call(this, entity);
		}

		//リンク先でEntityの名前が変更されている可能性があるので値を反映
		var _id = linkId.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
		$("#" + _id).text(entity.name);

		//起動したtargetに対して再度詳細画面を表示しなおす(versionは戻ってきたentityから)
		var $form = $("<form />").attr({method:"POST", action:viewAction + "/" + oid, target:target}).appendTo("body");
//		$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);
//		$("<input />").attr({type:"hidden", name:"oid", value:oid}).appendTo($form);
		$("<input />").attr({type:"hidden", name:"version", value:entity.version}).appendTo($form);
		$("<input />").attr({type:"hidden", name:"refEdit", value:refEdit}).appendTo($form);
		if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
		$form.submit();
		$form.remove();

	}

	var $form = $("<form />").attr({method:"POST", action:viewAction + "/" + oid, target:target}).appendTo("body");
//	$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);
//	$("<input />").attr({type:"hidden", name:"oid", value:oid}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"version", value:version}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"refEdit", value:refEdit}).appendTo($form);
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	$form.submit();
	$form.remove();
}


/**
 * 参照型の検索
 * @param selectAction
 * @param viewAction
 * @param defName
 * @param ulId
 * @param multiplicity
 * @param multi
 * @param urlParam
 * @param refEdit
 * @param callback
 * @param button
 * @return
 */
function searchReference(selectAction, viewAction, defName, propName, multiplicity, multi, urlParam, refEdit, callback, button, viewName, permitConditionSelectAll) {
	var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
	document.scriptContext["searchReferenceCallback"] = function(selectArray) {
		var $ul = $("#ul_" + _propName);
		var refs = new Array();
		$ul.children("li").children(":hidden").each(function() {
			refs[$(this).val()] = this.parentNode;
		});

		var deleteList = new Array();
		for (var key in refs) {
			var exist = false;
			for (var i = 0; i < selectArray.length; i++) {
				if (key == selectArray[i]) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				$(refs[key]).remove();
				var tmp = keySplit(key);
				var entity = {oid: tmp.oid, version: tmp.version};
				deleteList.push(entity);
			}
		}

		//参照の名前を一括取得
		var entityList = new Array();
		var list = new Array();
		for (var i = 0; i < selectArray.length; i++) {
			var key = selectArray[i];
			if (key in refs) continue;

			list.push(keySplit(key));
		}
		if (list.length > 0) {
			getEntityNameList(defName, viewName, list, function(entities) {
				for (var i = 0; i < entities.length; i++) {
					var entity = entities[i];
					var _key = entity.oid + "_" + entity.version;
					addReference("li_" + propName + _key, viewAction, defName, _key, entity.name, propName, "ul_" + _propName, refEdit);
				}
				entityList = entities;
			});
		}

		if (callback && $.isFunction(callback)) {
			if (typeof button === "undefined" || button == null) {
				callback.call(this, entityList, deleteList, propName);
			} else {
				//引数で渡されたトリガーとなるボタンをthisとして渡す
				callback.call(button, entityList, deleteList, propName);
			}
		}

		if (entityList.length > 0) {
			$("[name='" + _propName + "']:eq(0)").trigger("change", {});// 複数選択だとイベントが複数回発生するので1つだけ
		}
		closeModalDialog();
	};

	var selType = "single";
	if (multi) selType = "multi";

	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);
	var $form = $("<form />").attr({method:"POST", action:selectAction, target:target}).appendTo("body");
	$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);//定義名
	$("<input />").attr({type:"hidden", name:"multiplicity", value:multiplicity}).appendTo($form);//選択可能数
	$("<input />").attr({type:"hidden", name:"selectType", value:selType}).appendTo($form);//単一or複数
	$("<input />").attr({type:"hidden", name:"propName", value:propName}).appendTo($form);//プロパティ名
	$("<input />").attr({type:"hidden", name:"rootName", value:"ul_" + propName}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"permitConditionSelectAll", value:permitConditionSelectAll}).appendTo($form);
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	var kv = urlParam.split("&");
	if (urlParam.length > 0 && kv.length > 0) {
		for (var i = 0; i < kv.length; i++) {
			var _kv = kv[i].split("=");
			if (_kv.length > 0) {
				$("<input />").attr({type:"hidden", name:_kv[0], value:_kv[1]}).appendTo($form);
			}
		}
	}
	var specVersionKey = $(button).attr("data-specVersionKey");
	if (specVersionKey && specVersionKey != null) {
		$("<input />").attr({type:"hidden", name:"specVersion", value:$("[name='" + es(specVersionKey) + "']").val()}).appendTo($form);
	}
	$form.submit();
	$form.remove();
}

/**
 * 参照型の検索
 * @param selectAction
 * @param viewAction
 * @param defName
 * @param ulId
 * @param multiplicity
 * @param multi
 * @param urlParam
 * @param refEdit
 * @param callback
 * @param button
 * @return
 */
function searchReferenceForBi(webapi, selectAction, viewAction, defName, entityDefName, propName, multiplicity, multi, urlParam, refEdit, callback, button) {
	var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
	document.scriptContext["searchReferenceCallback"] = function(selectArray) {
		var $ul = $("#ul_" + _propName);
		var refs = new Array();
		$ul.children("li").children(":hidden").each(function() {
			refs[$(this).val()] = this.parentNode;
		});

		var deleteList = new Array();
		for (var key in refs) {
			var exist = false;
			for (var i = 0; i < selectArray.length; i++) {
				if (key == selectArray[i]) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				$(refs[key]).remove();
				var tmp = keySplit(key);
				var entity = {oid: tmp.oid, version: tmp.version};
				deleteList.push(entity);
			}
		}

		var entityList = new Array();
		var list = new Array();
		for (var i = 0; i < selectArray.length; i++) {
			var key = selectArray[i];
			if (key in refs) continue;

			list.push(keySplit(key));
		}
		if (list.length > 0) {
			getEntityValueList(webapi, defName, entityDefName, "name", list, function(entities) {
				for (var i = 0; i < entities.length; i++) {
					var entity = entities[i];
					var _key = entity.oid + "_" + entity.version;
					addReference("li_" + propName + _key, viewAction, entityDefName, _key, entity.name, propName, "ul_" + _propName, refEdit);
				}
				entityList = entities;
			});
		}

		if (callback && $.isFunction(callback)) {
			if (typeof button === "undefined" || button == null) {
				callback.call(this, entityList, deleteList, propName);
			} else {
				//引数で渡されたトリガーとなるボタンをthisとして渡す
				callback.call(button, entityList, deleteList, propName);
			}
		}

		closeModalDialog();
	};

	var selType = "single";
	if (multi) selType = "multi";

	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);
	var $form = $("<form />").attr({method:"POST", action:selectAction, target:target}).appendTo("body");
//	$("<input />").attr({type:"hidden", name:"defName", value:entityDefName}).appendTo($form);//定義名
	$("<input />").attr({type:"hidden", name:"multiplicity", value:multiplicity}).appendTo($form);//選択可能数
	$("<input />").attr({type:"hidden", name:"selectType", value:selType}).appendTo($form);//単一or複数
	$("<input />").attr({type:"hidden", name:"propName", value:propName}).appendTo($form);//プロパティ名
	$("<input />").attr({type:"hidden", name:"rootName", value:"ul_" + propName}).appendTo($form);
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	var kv = urlParam.split("&");
	if (urlParam.length > 0 && kv.length > 0) {
		for (var i = 0; i < kv.length; i++) {
			var _kv = kv[i].split("=");
			if (_kv.length > 0) {
				$("<input />").attr({type:"hidden", name:_kv[0], value:_kv[1]}).appendTo($form);
			}
		}
	}
	var specVersionKey = $(button).attr("data-specVersionKey");
	if (specVersionKey && specVersionKey != null) {
		$("<input />").attr({type:"hidden", name:"specVersion", value:$("[name='" + es(specVersionKey) + "']").val()}).appendTo($form);
	}
	$form.submit();
	$form.remove();
}

/**
 * 詳細画面の参照型選択処理(EditMode=View)
 *
 * @param selectAction 選択ダイアログ表示Action
 * @param updateAction 参照Entity追加後の参照元Entity更新用Action(画面リロード用Action)
 * @param defName      参照先Entity名
 * @param id           参照一覧のtbodyのID
 * @param propName     参照Property名
 * @param multiplicity 多重度
 * @param multi        複数選択の場合、true
 * @param urlParam     選択ダイアログ表示時の追加パラメータ
 * @param reloadUrl    リロード用URL
 */
function searchReferenceFromView(selectAction, updateAction, defName, id, propName, multiplicity, multi, urlParam, reloadUrl, button, permitConditionSelectAll) {
	document.scriptContext["searchReferenceCallback"] = function(selectArray) {
		//選択されたOID情報をもとに参照元Entityを更新(更新後画面が再表示される)
		var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
		var refLink = new Array();
		var refList = document.getElementById(id);
		$(":hidden[name='" + propName + "']", refList).each(function() {
			refLink["idx_" + this.value] = this;
		});

		for (var key in refLink) {
			var exist = false;
			for (var i = 0; i < selectArray.length; i++) {
				if (key == selectArray[i]) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				$(refLink[key]).remove();
			}
		}

		var $form = $("#detailForm");
		for (var i = 0; i < selectArray.length; i++) {
			var key = selectArray[i];
			if (key in refLink) continue;

			$("<input type='hidden' />").attr("name", propName).val(key).appendTo($form);
		}

		closeModalDialog();

		$("<input type='hidden' name='updatePropertyName' />").val(propName).appendTo($form);
		$("<input type='hidden' name='reloadUrl' />").val(reloadUrl).appendTo($form);
		$form.attr("action", updateAction).submit();

	};

	var selType = "single";
	if (multi) selType = "multi";

	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);
	var $form = $("<form />").attr({method:"POST", action:selectAction, target:target}).appendTo("body");
	$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);//定義名
	$("<input />").attr({type:"hidden", name:"multiplicity", value:multiplicity}).appendTo($form);//選択可能数
	$("<input />").attr({type:"hidden", name:"selectType", value:selType}).appendTo($form);//単一or複数
	$("<input />").attr({type:"hidden", name:"propName", value:propName}).appendTo($form);//プロパティ名
	$("<input />").attr({type:"hidden", name:"rootName", value:id}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"permitConditionSelectAll", value:permitConditionSelectAll}).appendTo($form);
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	var kv = urlParam.split("&");
	if (urlParam.length > 0 && kv.length > 0) {
		for (var i = 0; i < kv.length; i++) {
			var _kv = kv[i].split("=");
			if (_kv.length > 0) {
				$("<input />").attr({type:"hidden", name:_kv[0], value:_kv[1]}).appendTo($form);
			}
		}
	}
	var specVersionKey = $(button).attr("data-specVersionKey");
	if (specVersionKey && specVersionKey != null) {
		$("<input />").attr({type:"hidden", name:"specVersion", value:$("[name='" + es(specVersionKey) + "']").val()}).appendTo($form);
	}
	$form.submit();
	$form.remove();
}

/**
 * 参照型の追加
 * @param defName
 * @param ulId
 * @param propName
 * @param multiplicity
 * @return
 */
function insertReference(addAction, viewAction, defName, propName, multiplicity, urlParam, parentOid, parentVersion, parentDefName, refEdit, callback, button) {
	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);

	var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
	if (canAddItem("ul_" + _propName, multiplicity)) {

		//追加(＝編集)ダイアログで保存された場合は、ダイアログを閉じる
		document.scriptContext["editReferenceCallback"] = function(entity) {
			var $ul = $("#ul_" + _propName);
			var key = entity.oid + "_" + entity.version;
			var linkId = addReference("li_" + propName + key, viewAction, defName, key, entity.name, propName, "ul_" + _propName, refEdit);

			//カスタムのCallbackが定義されている場合に呼び出す
			if (callback && $.isFunction(callback)) {
				if (typeof button === "undefined" || button == null) {
					callback.call(this, entity, propName);
				} else {
					//引数で渡されたトリガーとなるボタンをthisとして渡す
					callback.call(button, entity, propName);
				}
			}

			$("[name='" + _propName + "']:eq(0)").trigger("change", {});

			//起動したtargetに対して再度詳細画面を表示しなおす
			showReference(viewAction, defName, entity.oid, entity.version, linkId, refEdit);
		};

		var $form = $("<form />").attr({method:"POST", action:addAction, target:target}).appendTo("body");
//		$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);//定義名
		$("<input />").attr({type:"hidden", name:"parentOid", value:parentOid}).appendTo($form);//参照元の情報
		$("<input />").attr({type:"hidden", name:"parentVersion", value:parentVersion}).appendTo($form);
		$("<input />").attr({type:"hidden", name:"parentDefName", value:parentDefName}).appendTo($form);
		if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
		var kv = urlParam.split("&");
		if (urlParam.length > 0 && kv.length > 0) {
			for (var i = 0; i < kv.length; i++) {
				var _kv = kv[i].split("=");
				if (_kv.length > 0) {
					$("<input />").attr({type:"hidden", name:_kv[0], value:_kv[1]}).appendTo($form);
				}
			}
		}
		$form.submit();
		$form.remove();
	} else {
		modalCancel();
	}
}

/**
 * 詳細画面の参照型追加処理(EditMode=View)
 *
 * @param addAction     追加ダイアログ表示Action
 * @param defName       参照先Entity名
 * @param id            参照一覧のtbodyのID
 * @param multiplicity  多重度
 * @param mappedBy      非参照
 * @param oid
 * @param updateAction  参照Entity追加後の参照元Entity更新用Action(画面リロード用Action)
 * @param propName      参照Property名
 * @param reloadUrl     リロード用URL
 */
function insertReferenceFromView(addAction, defName, id, multiplicity, urlParam, parentOid, parentVersion, parentDefName, mappedBy, oid, updateAction, propName, reloadUrl) {
	var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");

	var isMappedBy = mappedBy != null && mappedBy != "";
	var refList = document.getElementById(id);
	var count = $(":hidden[name='" + _propName + "']", refList).length;
	if (multiplicity == -1 || count < multiplicity) {

		//追加(＝編集)ダイアログで保存された場合は、ダイアログを閉じる
		document.scriptContext["editReferenceCallback"] = function(entity) {

			closeModalDialog();

			var $form = $("#detailForm");
			if (isMappedBy) {
				//非参照の場合は再ロード
				$form.attr("action", reloadUrl).submit()
			} else {
				//通常参照の場合は参照Propertyを更新（更新Action側で再ロード）
				var key = entity.oid + "_" + entity.version;
				$("<input type='hidden' name='" + propName + "' />").val(key).appendTo($form);
				$("<input type='hidden' name='updatePropertyName' />").val(propName).appendTo($form);
				$("<input type='hidden' name='reloadUrl' />").val(reloadUrl).appendTo($form);
				$form.attr("action", updateAction).submit();
			}
		};

		var isSubModal = $("body.modal-body").length != 0;
		var target = getModalTarget(isSubModal);
		var $form = $("<form />").attr({method:"POST", action:addAction, target:target}).appendTo("body");
		$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);
		$("<input />").attr({type:"hidden", name:"parentOid", value:parentOid}).appendTo($form);//参照元の情報
		$("<input />").attr({type:"hidden", name:"parentVersion", value:parentVersion}).appendTo($form);
		$("<input />").attr({type:"hidden", name:"parentDefName", value:parentDefName}).appendTo($form);
		if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
		if (isMappedBy) {
			$("<input />").attr({type:"hidden", name:"updateByParam", value:true}).appendTo($form);
			$("<input />").attr({type:"hidden", name:mappedBy, value:oid}).appendTo($form);
		}
		var kv = urlParam.split("&");
		if (urlParam.length > 0 && kv.length > 0) {
			for (var i = 0; i < kv.length; i++) {
				var _kv = kv[i].split("=");
				if (_kv.length > 0) {
					$("<input />").attr({type:"hidden", name:_kv[0], value:_kv[1]}).appendTo($form);
				}
			}
		}
		$form.submit();
		$form.remove();
	} else {
		modalCancel();
	}
}

function addReference(id, viewAction, defName, key, label, propName, ulId, refEdit) {
	var tmp = keySplit(key);
	var oid = tmp.oid;
	var ver = tmp.version;

	var $ul = $("#" + ulId);

	var $li = $("<li />").addClass("list-add").attr("id", id);

	//リンク追加
	var linkId = propName + "_" + tmp.oid;
	var $link = $("<a href='javascript:void(0)' />").attr("id", linkId).click(function() {
		showReference(viewAction, defName, oid, ver, linkId, refEdit);
	}).appendTo($li);
	$link.text(label);
	if ($("body.modal-body").length != 0) {
		$link.subModalWindow();
	} else {
		$link.modalWindow();
	}

	//削除ボタン追加
	var deletable = $ul.attr("data-deletable");
	if (deletable != null && deletable == "true") {
		var $btn = $(" <input type='button' />").val(scriptContext.locale.deleteBtn).addClass("gr-btn-02 del-btn ml05").appendTo($li);
		$btn.click(function() {
			$(this).parent().remove();
		});
	}

	//hidden追加
	$("<input type='hidden' />").attr({name:propName, value:key}).appendTo($li);

	$ul.append($li);

	$(".fixHeight").fixHeight();

	//リンクIDを返す
	return linkId;
}

function keySplit(key) {
	var index = key.lastIndexOf("_");
	var oid = key.substr(0, index);
	var version = key.substr(index + 1);
	return {oid: oid, version: version};
}

function getModalTarget(isSubModal) {
	var target = null;
	if (isSubModal) {
		target = $(document).attr("targetName");
	} else {
		target = $(".modal-inner iframe", document).attr("name");
	}
	return target;
}

function closeModalDialog() {
	var target = null;
	var isSubModal = $("body.modal-body").length != 0;
	if (isSubModal) {
		target = "#modal-dialog-" + $(document).attr("targetName") + " p.modal-close";
	} else {
		target = "#modal-close-root";
	}
	$(target, parent.document).click();
}

function modalCancel() {
	var isSubModal = $("body.modal-body").length != 0;
	if (isSubModal) {
		var target = getModalTarget(isSubModal);
		$("#modal-dialog-" + target + " .modal-wrap", parent.document).stop(true,true).fadeOut(0);
		$("#modal-dialog-" + target + " .modal-inner", parent.document).stop(true,true).fadeOut(0);
		$("#modal-dialog-" + target + " p.modal-close", parent.document).click();
	} else {
		$("#modal-dialog-root .modal-wrap", parent.document).stop(true,true).fadeOut(0);
		$("#modal-dialog-root  .modal-inner", parent.document).stop(true,true).fadeOut(0);
		$("#modal-close-root", parent.document).click();
	}
}

function getDialogTrigger($v, option) {
	var $dialogTrigger = null;
	var key = null;
	if (option && option.key) {
		//カスタム用ダイアログとして起動するためのトリガー
		$dialogTrigger = $(".dialogTrigger[key='" + option.key + "']", $v);
		key = option.key;
	} else {
		//通常のダイアログとして起動するためのトリガー
		$dialogTrigger = $(".dialogTrigger[key='defaultDialog']", $v);
		key = "defaultDialog";
	}
	if ($dialogTrigger.length == 0) {
		//ローデータ出力用のダイアログを表示するノード追加
		$dialogTrigger = $("<div />").attr({key:key}).addClass("dialogTrigger").prependTo($v);
		if ($("body.modal-body").length != 0) {
			$dialogTrigger.subModalWindow(option);
		} else {
			$dialogTrigger.modalWindow(option);
		}
	}
	return $dialogTrigger;
}

function viewEditableReference(viewAction, defName, oid, reloadUrl, refEdit) {

	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);
	var edited = false;

	document.scriptContext["editReferenceCallback"] = function(entity) {
		edited = true;
		if ($("body.modal-body").length != 0) {
			//FIXME このパターンはどれか不明？参照ダイアログでNestTableを利用している場合か？

			//refEdit,modalTarget→ダイアログ表示時のパラメータを再利用
			submitForm(reloadUrl, {
				"defName":$(":hidden[name='defName']").val(),
				"oid":$(":hidden[name='oid']").val(),
				"refEdit":editable,
				"modalTarget":modalTarget
				});
			closeModalDialog();

		} else {
			var $form = $("<form />").attr({method:"POST", action:viewAction + "/" + oid, target:target}).appendTo("body");
			$("<input />").attr({type:"hidden", name:"version", value:entity.version}).appendTo($form);
			$("<input />").attr({type:"hidden", name:"refEdit", value:refEdit}).appendTo($form);
			if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);

			$form.submit();
			$form.remove();
		}
	};

	$("iframe[name='" + target + "']").parents(".modal-dialog").off("closeModalDialog").on("closeModalDialog", function() {
		if (edited) {
			submitForm(reloadUrl + "/" + $(":hidden[name='oid']").val(), {
				"defName":$(":hidden[name='defName']").val(),
				"oid":$(":hidden[name='oid']").val()
			});
		}
	});

	var $form = $("<form />").attr({method:"POST", action:viewAction + "/" + oid, target:target}).appendTo("body");
	$("<input />").attr({type:"hidden", name:"refEdit", value:refEdit}).appendTo($form);
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	$form.submit();
	$form.remove();
}

/**
 * NestTableの編集ページがDetailモードの場合の編集リンク処理
 * 参照リンク編集可否がtrueの場合に呼び出され、編集ダイアログを直接表示する。
 * また表示時にはテーブルでの編集中の値をダイアログに引き継ぎ、
 * 編集ダイアログで保存が実行された場合は、一覧に保存結果を反映する。
 */
function editReference(detailAction, defName, oid, trId, propName, index, viewAction, rootDefName, viewName, orgPropName) {

	var isSubModal = $("body.modal-body").length != 0;
	var target = getModalTarget(isSubModal);

	document.scriptContext["editReferenceCallback"] = function(e) {
		getNestTableData(rootDefName, viewName, orgPropName, e.oid, function(entity) {
			if (entity == null) return;

			var $tr = $("#" + trId);
			var $headerRow = $tr.parent().prev("thead").children("tr:first");
			$headerRow.children("th").each(function(num) {
				var type = getType(this);
				var $td = $tr.children(":nth-child(" + (num + 1) + ")");
				var name = $td.attr("data-propName");
				if (type[0] == "String" || type[0] == "LongText") {
					//文字列
					updateNestValue_String(type[1], $td, propName, name, entity);
				} else if (type[0] == "Integer" || type[0] == "Float" || type[0] == "Decimal") {
					//数値
					updateNestValue_Number(type[1], $td, propName, name, entity);
				} else if (type[0] == "AutoNumber" || type[0] == "Expression") {
					//文字列
				} else if (type[0] == "Date") {
					//日付
					updateNestValue_Date(type[1], $td, propName, name, entity);
				} else if (type[0] == "Time") {
					//時間
					updateNestValue_Time(type[1], $td, propName, name, entity);
				} else if (type[0] == "Timestamp") {
					//日時
					updateNestValue_Timestamp(type[1], $td, propName, name, entity);
				} else if (type[0] == "Boolean") {
					//真偽
					updateNestValue_Boolean(type[1], $td, propName, name, entity);
				} else if (type[0] == "Select") {
					//選択
					updateNestValue_Select(type[1], $td, propName, name, entity);
				} else if (type[0] == "Binary") {
					//バイナリ
					updateNestValue_Binary(type[1], $td, propName, name, entity);
				} else if (type[0] == "Reference") {
					//参照
					updateNestValue_Reference(type[1], $td, propName, name, entity);
				} else if (type[0] == "Join") {
					//連結
					updateNestValue_Join(this, $td, propName, name, entity);
				} else if (type[0] == "DateRange") {
					//日付範囲
					updateNestValue_DateRange(this, $td, propName, name, entity);
				} else if (type[0] == "refLink") {
					//編集リンク
					var $link = $($td).children("a");
					$link.removeAttr("onclick").off("click");
					$link.on("click", function() {
						editReference(detailAction, defName, entity.oid, trId, propName, index, viewAction, rootDefName, viewName, orgPropName);
					});
					$link.modalWindow();
				}
			});

			//各プロパティでラベル表示だと、テキスト更新で消えるため最後に設定
			if ($(":hidden[name='" + es(propName + ".oid") + "']", $tr).length == 0) {
				var $td = $tr.children("td:first");
				$("<input type='hidden' />").attr({name:propName + ".oid", value:entity.oid}).appendTo($td);
				$("<input type='hidden' />").attr({name:propName + ".version", value:entity.version}).appendTo($td);
			}

			var $form = $("<form />").attr({method:"POST", action:viewAction, target:target}).appendTo("body");
			$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);
			$("<input />").attr({type:"hidden", name:"oid", value:entity.oid}).appendTo($form);
			$("<input />").attr({type:"hidden", name:"version", value:entity.version}).appendTo($form);
			$("<input />").attr({type:"hidden", name:"refEdit", value:true}).appendTo($form);
			if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);

			$form.submit();
			$form.remove();
		});
	};

	var $form = $("<form />").attr({method:"POST", action:detailAction, target:target}).appendTo("body");
	$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"oid", value:oid}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"refEdit", value:true}).appendTo($form);
	$("<input />").attr({type:"hidden", name:"updateByParam", value:true}).appendTo($form);
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	//テーブル上の項目を取り出してダイアログに反映
	$("#" + trId + " [name]").each(function() {
		var name = replaceAll($(this).attr("name"), propName + ".", "");
		if ($(this).is(":checkbox")) {
			//チェックボックス、選択されてる場合のみ
			if ($(this).is(":checked")) {
				$("<input />").attr({type:"hidden", name:name, value:$(this).val()}).appendTo($form);
			}
		} else if ($(this).is(":radio")) {
			//ラジオ、選択されてる場合のみ
			if ($(this).is(":checked")) {
				$("<input />").attr({type:"hidden", name:name, value:$(this).val()}).appendTo($form);
			}
		} else {
			$("<input />").attr({type:"hidden", name:name, value:$(this).val()}).appendTo($form);
		}
	});
	$form.submit();
	$form.remove();
}

function updateNestValue_String(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	if (val == null) val = "";
	if (type == "TEXT") {
		$node.children(":text").val(val);
	} else if (type == "TEXTAREA" || type == "RICHTEXT") {
		$node.children("textarea").val(val);
	} else if (type == "PASSWORD") {
		$node.children(":password").val(val);
	} else if (type == "SELECT") {
		$node.children("select").val(val);
	} else if (type == "LABEL") {
		$node.text(val);
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:val}).appendTo($node);
	}
}
function updateNestValue_Number(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	if (val == null) val = "";
	if (type == "TEXT") {
		$node.children(":text").val(val);
		let dummyField = $("input.commaField.dummyField", $node);
		if (dummyField.length > 0) {
			dummyField.val(insertComma(val, separator));
		}
	} else if (type == "LABEL") {
		let dummyField = $("span.data-label.commaLabel", $node);
		if (dummyField.length > 0) {
			dummyField.text(insertComma(val, separator));
		} else {
			$node.text(val);
		}
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:val}).appendTo($node);
	}
}

function insertComma(str, separator) {
	let reg = new RegExp(separator, "g");
	let num = new String(str).replace(reg, "");
	while (num != (num = num.replace(/^(-?\d+)(\d{3})/, "$1" + separator + "$2")));
	return num;
}

function updateNestValue_Date(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	var date = "";
	var label = "";
	if (val != null) {
		date = dateUtil.newFormatString(val, "YYYY-MM-DD", "YYYYMMDD");
		label = dateUtil.newFormatString(val, "YYYY-MM-DD", dateUtil.getOutputDateFormat());
	}
	if (type == "DATETIME") {
		$("#d_" + es(parentPropName + "." + name), $node).val(convertToLocaleDateString(date));
		$("#i_" + es(parentPropName + "." + name), $node).val();
	} else if (type == "LABEL") {
		$node.text(label);
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:date}).appendTo($node);
	}
}
function updateNestValue_Time(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	var _date = null;
	if (val == null) val = "";
	else {
		_date = dateUtil.toDate(val, dateUtil.getOutputTimeFormat());
	}

	if (type == "DATETIME") {
		if ($(".timepicker-form-size-01", $node).length > 0) {
			var $time = $("#time_" + es(parentPropName + "." + name), $node);
			var $hidden = $("#i_" + es(parentPropName + "." + name), $node);

			var value = "";
			var time = "";

			if (_date != null) {
				var timeFormat = $time.attr("data-timeformat");
				value = dateUtil.format(_date, dateUtil.getServerTimeFormat());
				time = dateUtil.format(_date, timeFormat);
			}

			$time.val(time);
			$hidden.val(value);
		} else {
			var selectField = $node.children("span").children("label").children("select")
			var hiddenField = $node.children("span").children("input:hidden:not([name$='oid']):not([name$='version'])");

			var hour = _date != null ? dateUtil.formatInputHour(_date) : "";
			var min = _date != null ? dateUtil.formatInputMin(_date) : "";
			var sec = _date != null ? dateUtil.formatInputSec(_date) : "";
			var msec = _date != null ? dateUtil.format(_date, "SSS") : "";
			var hidden = _date != null ? dateUtil.format(_date, dateUtil.getServerTimeFormat()) : "";

			if (selectField.length == 1) {
				$(selectField[0]).val(hour);//時
				$(hiddenField[0]).val(min);//分
				$(hiddenField[1]).val(sec);//秒
				$(hiddenField[2]).val(msec);//ミリ秒
			} else if (selectField.length == 2) {
				$(selectField[0]).val(hour);//時
				$(selectField[1]).val(min);//分
				$(hiddenField[0]).val(sec);//秒
				$(hiddenField[1]).val(msec);//ミリ秒
			} else if (selectField.length == 3) {
				$(selectField[0]).val(hour);//時
				$(selectField[1]).val(min);//分
				$(selectField[2]).val(sec);//秒
				$(hiddenField[0]).val(msec);//ミリ秒
			}

			$node.children("span").children("input:hidden:last").val(hidden);
		}
	} else if (type == "LABEL") {
		var label = _date != null ? dateUtil.format(_date, dateUtil.getOutputTimeFormat()): "";
		$node.text(label);
		var hidden = _date != null ? dateUtil.format(_date, dateUtil.getServerTimeFormat()) : "";
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:hidden}).appendTo($node);
	}
}
function updateNestValue_Timestamp(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	var _date = null;
	if (val == null) val = "";
	else {
		_date = new Date();
		_date.setTime(val);
	}
	if (type == "DATETIME") {
		if ($(".datetimepicker-form-size-01, .datetimepicker-form-size-02", $node).length > 0) {
			var $datetime = $("#datetime_" + es(parentPropName + "." + name), $node);
			var $hidden = $("#i_" + es(parentPropName + "." + name), $node);

			var value = _date != null ? dateUtil.format(_date, dateUtil.getServerDatetimeFormat()) : "";

			var timeFormat = $datetime.attr("data-timeformat");
			var range = "NONE";
			if (timeFormat == "HH:mm:ss") {
				range = "SEC";
			} else if (timeFormat == "HH:mm") {
				range = "MIN";
			} else if (timeFormat == "HH") {
				range = "HOUR";
			}

			$datetime.val(convertToLocaleDatetimeString(value, dateUtil.getServerDatetimeFormat(), range));
			$hidden.val(value);
		} else {
			var selectField = $node.children("span").children("label").children("select")
			var hiddenField = $node.children("span").children("input:hidden:not([name$='oid']):not([name$='version'])");

			var date = _date != null ? dateUtil.format(_date, dateUtil.getInputDateFormat()) : "";
			var hour = _date != null ? dateUtil.format(_date, dateUtil.getInputHourFormat()) : "";
			var min = _date != null ? dateUtil.format(_date, dateUtil.getInputMinFormat()) : "";
			var sec = _date != null ? dateUtil.format(_date, dateUtil.getInputSecFormat()) : "";
			var msec = _date != null ? dateUtil.format(_date, "SSS") : "";
			var hidden = _date != null ? dateUtil.format(_date, dateUtil.getServerDatetimeFormat()) : "";

			$node.children("span").children(":text").val(convertToLocaleDateString(date, dateUtil.getInputDateFormat()));//日
			if (selectField.length == 0) {
				$(hiddenField[0]).val(hour);//時
				$(hiddenField[1]).val(min);//分
				$(hiddenField[2]).val(sec);//秒
				$(hiddenField[3]).val(msec);//ミリ秒
			} else if (selectField.length == 1) {
				$(selectField[0]).val(hour);//時
				$(hiddenField[0]).val(min);//分
				$(hiddenField[1]).val(sec);//秒
				$(hiddenField[2]).val(msec);//ミリ秒
			} else if (selectField.length == 2) {
				$(selectField[0]).val(hour);//時
				$(selectField[1]).val(min);//分
				$(hiddenField[0]).val(sec);//秒
				$(hiddenField[1]).val(msec);//ミリ秒
			} else if (selectField.length == 3) {
				$(selectField[0]).val(hour);//時
				$(selectField[1]).val(min);//分
				$(selectField[2]).val(sec);//秒
				$(hiddenField[0]).val(msec);//ミリ秒
			}

			$node.children("span").children("input:hidden:last").val(hidden);
		}
	} else if (type == "LABEL") {
		var label = _date != null ? dateUtil.format(_date, dateUtil.getOutputDatetimeFormat()): "";
		$node.text(label);
		var hidden = _date != null ? dateUtil.format(_date, dateUtil.getServerDatetimeFormat()) : "";
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:hidden}).appendTo($node);
	}
}
function updateNestValue_Boolean(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	var selectValue = "";
	if (val != null) {
		selectValue = val;
	}
	if (type == "RADIO") {
//		$node.children("ul").children("li").children("label").children(":radio").val([selectValue]);
		$(":radio", $node).val([selectValue]);
	} else if (type == "CHECKBOX") {
		var checked = selectValue === true ? true : false;
		$(":checkbox", $node).prop("checked", checked);
	} else if (type == "LABEL") {
		var $span = $node.children("span.data-label");
		var label = selectValue === "" ? "" : selectValue === true ? $span.attr("data-true-label") : $span.attr("data-false-label");
		$span.empty();
		$span.text(label);
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:selectValue}).appendTo($span);
	}
}
function updateNestValue_Select(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	var displayName = "";
	var selectValue = "";
	if (val != null) {
		displayName = val.displayName;
		selectValue = val.value;
	}
	if (type == "RADIO") {
//		$node.children("ul").children("li").children("label").children(":radio").val([selectValue]);
		$(":radio", $node).val([selectValue]);
	} else if (type == "CHECKBOX") {
		var checked = selectValue == true ? true : false;
		$(":checkbox", $node).prop("checked", checked);
	} else if (type == "SELECT") {
		$node.children("select").val(selectValue);
	} else if (type == "LABEL") {
		var $ul = $node.children("ul");
		$ul.children("li").remove();
		var $li = $("<li />").appendTo($ul);
		$li.text(displayName);
		$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:selectValue}).appendTo($li);
	}
}
function updateNestValue_Binary(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	var $ul = $node.children("ul");
	$ul.children("li").remove();
	var $file = $node.children(":file");
	if (val == null) {
		$file.show();
	} else {
		$file.hide();
		var fileId = $file.attr("id");
		var propName = parentPropName + "." + name;
		var count = $file.attr("data-binCount") - 0;
		var param = "&defName=" + entity.definitionName + "&propName=" + name;
		addBinaryGroup(propName, count, fileId, val.name, val.type, val.lobId, type, param);
	}
}
function updateNestValue_Reference(type, $node, parentPropName, name, entity) {
	var val = entity[name];
	if (type == "LINK") {
		var $button = $node.children(":button");
		$node.children("ul").children("li").remove();
		if (val != null) {
			var viewAction = $button.attr("viewAction");
			var propName = $button.attr("propName");
			var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
			var defName = $button.attr("defName");
			var refEdit = $button.attr("refEdit");
			var key = val.oid + "_" + val.version;
			var label = val.name;
			addReference("li_" + propName, viewAction, defName, key, label, propName, "ul_" + _propName, refEdit);
		}
	} else if (type == "SELECT") {
		var oid = "";
		if (val != null) oid = val.oid + "_" + val.version;
		$node.children("select").val(oid);
	} else if (type == "Checkbox") {
		if (val == null) {
			$node.children(":radio:checked").prop("checked",false);
		} else {
			$node.children(":radio").val(entity.oid);
		}
	} else if (type == "REFCOMBO") {
		//TODO 参照コンボ
	} else if (type == "Label") {
		var $ul = $node.children("ul");
		$ul.children("li").remove();
		if (val != null) {
			var $li = $("<li />").text(val.name).appendTo($ul);
			$("<input />").attr({type:"hidden", name:parentPropName + "." + name, value:val.oid}).appendTo($li);
		}
	}
}
function updateNestValue_Join(head, $node, parentPropName, name, entity) {
	var propCount = $(head).children(":hidden[name='joinPropCount']").val() - 0;
	for (var i = 0; i < propCount; i++) {
		var joinPropName = $(head).children(":hidden[name='joinPropName" + i + "']").val();
		var propType = $(head).children(":hidden[name='joinPropType" + i + "']").val();
		var dispType = $(head).children(":hidden[name='joinDispType" + i + "']").val();

		//.と[]はjQueryのセレクタに使われてるので\\つける
		var propNameIdx = parentPropName.replace("[", "\\[").replace("]", "\\]") + "_" + name;
		var selector = "#join_" + propNameIdx + "_" + joinPropName + "_" + i;
		var $span = $node.children(selector);

		if (propType == "String" || propType == "LongText") {
			//文字列
			updateNestValue_String(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Integer" || propType == "Float" || propType == "Decimal") {
			//数値
			updateNestValue_Number(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Date") {
			//日付
			updateNestValue_Date(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Time") {
			//時間
			updateNestValue_Time(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Timestamp") {
			//日時
			updateNestValue_Timestamp(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Select") {
			//選択
			updateNestValue_Select(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Binary") {
			//バイナリ
			updateNestValue_Binary(dispType, $span, parentPropName, joinPropName, entity);
		} else if (propType == "Reference") {
			//参照
			updateNestValue_Reference(dispType, $span, parentPropName, joinPropName, entity);
		}
	}

}

/**
 * ネストされた参照型の入力テーブルに組み合わせたプロパティの入力欄追加
 * @param head
 * @param cell
 * @param idx
 */
function updateNestValue_DateRange(head, $node, parentPropName, name, entity) {
	var fromPropName = $(head).children(":hidden[name='fromPropName']").val();
	var toPropName = $(head).children(":hidden[name='toPropName']").val();
	var propType = $(head).children(":hidden[name='dateRangePropType']").val();
	var dispType = $(head).children(":hidden[name='dateRangeDispType']").val();

	//.と[]はjQueryのセレクタに使われてるので\\つける
	var fromPropNameIdx = parentPropName.replace("[", "\\[").replace("]", "\\]") + "_" + fromPropName;
	var fromSelector = "#daterange_" + fromPropNameIdx;
	var $from = $node.children(fromSelector);
	var toPropNameIdx = parentPropName.replace("[", "\\[").replace("]", "\\]") + "_" + toPropName;
	var toSelector = "#daterange_" + toPropNameIdx;
	var $to = $node.children(toSelector);

	if (propType == "Date") {
		//日付
		updateNestValue_Date(dispType, $from, parentPropName, fromPropName, entity);
		updateNestValue_Date(dispType, $to, parentPropName, toPropName, entity);
	} else if (propType == "Time") {
		//時間
		updateNestValue_Time(dispType, $from, parentPropName, fromPropName, entity);
		updateNestValue_Time(dispType, $to, parentPropName, toPropName, entity);
	} else if (propType == "Timestamp") {
		//日時
		updateNestValue_Timestamp(dispType, $from, parentPropName, fromPropName, entity);
		updateNestValue_Timestamp(dispType, $to, parentPropName, toPropName, entity);
	}
}

////////////////////////////////////////////////////////
// テーブル行追加用のJavascript
////////////////////////////////////////////////////////
/*
 * テーブル行で追加したノードは何故かjQueryのセレクタがうまく機能しない。
 * document.getElementById等で代用する。
 * →アップロードや選択ダイアログ等のFunction呼出し先でも対応必要。
 * 　.や[]がjQueryのセレクタとして認識される、es()でエスケープすれば問題なし
 */

/**
 * ネストされた参照型の入力テーブルに行を追加
 * @param rowId
 * @param countId
 * @param multiplicy
 * @return
 */
function addNestRow(rowId, countId, multiplicy, insertTop, rootDefName, viewName, orgPropName, callback) {
	var $srcRow = $("#" + rowId);
	$srcRow.parents("table").show();
	var $tbody = $srcRow.parent();
	var rowCount = $tbody.children("tr").length;
	if (multiplicy != 0 && rowCount >= multiplicy) return;

	var $copyRow = $srcRow.clone().removeAttr("style");
	var $headerRow = $tbody.prev("thead").children("tr:first");
	if (insertTop && rowCount.length > 1) {
		$headerRow.after($copyRow);
	} else {
		$tbody.append($copyRow);
	}

	countUp(countId, function(idx) {
		var rowId = replaceDummyAttr($copyRow, "id", idx);
		$("[id]", $copyRow).each(function() {
			replaceDummyAttr(this, "id", idx);
		});
		$("[name]", $copyRow).each(function() {
			replaceDummyAttr(this, "name", idx);
		});
		$copyRow.addClass("stripeRow");

		//形式による置換以外の処理
		$headerRow.children("th").each(function(num) {
			var type = getType(this);
			var $td = $copyRow.children(":nth-child(" + (num + 1) + ")");
			if (type[0] == "String" || type[0] == "LongText") {
				//文字列
				addNestRow_String(type[1], $td, idx);
			} else if (type[0] == "Decimal" || type[0] == "Float" || type[0] == "Integer") {
				//数値
				addNestRow_Number(type[1], $td, idx);
			} else if (type[0] == "Date") {
				//日付
				addNestRow_Date(type[1], $td, idx);
			} else if (type[0] == "Time") {
				//時間
				addNestRow_Time(type[1], $td, idx);
			} else if (type[0] == "Timestamp") {
				//日時
				addNestRow_Timestamp(type[1], $td, idx);
			} else if (type[0] == "Binary") {
				//バイナリ
				$td.children(":file").each(function() {
					replaceDummyAttr(this, "pname", idx);
					var token = $(this).attr("token");
					uploadFile(this, token);
				});
			} else if (type[0] == "Reference") {
				//参照
				addNestRow_Reference(type[1], $td, idx);
			} else if (type[0] == "Join") {
				//連結
				addNestRow_Join(this, $td, idx);
			} else if (type[0] == "DateRange") {
				//日付範囲
				addNestRow_DateRange(this, $td, idx);
			} else if (type[0] == "refLink") {
				//編集リンク
				var $link = $($td).children("a");
				var propName = $link.attr("data-name");
				var defName = $link.attr("data-defName");
				var action = $link.attr("data-action");
				var view = $link.attr("data-view");
				$link.click(function() {
					editReference(action, defName, "", rowId, propName, idx, view, rootDefName, viewName, orgPropName);
				});
				$link.modalWindow();
			} else if (type[0] == "tableOrder") {
				//表示順
				$(".up-icon", $td).on("click", function(){shiftUp(rowId)});
				$(".down-icon", $td).on("click", function(){shiftDown(rowId)});
			} else if (type[0] == "last") {
				//削除ボタン
				$($td).children(":button").on("click", function() {deleteRefTableRow(rowId);});
			}
		});

		if (callback && $.isFunction(callback)) callback.call(this, $copyRow.get(0), idx);
	});
	$(".fixHeight").fixHeight();

	return $copyRow;
}

/**
 * ネストされた参照型の入力テーブルに文字列型の入力欄追加
 * @param cell
 * @param idx
 * @return
 */
function addNestRow_String(type, cell, idx) {
	if (type == "RICHTEXT") {
		var $text = $(cell).children("textarea");
		$text.attr("id", "id_" + $text.attr("name"));
		$text.ckeditor();
	}
}

/**
 * ネストされた参照型の入力テーブルに数値型の入力欄追加
 * @param cell
 * @param idx
 * @return
 */
function addNestRow_Number(type, cell, idx) {
	var $text = $(cell).children(":text");
	if ($text.hasClass("commaFieldDummy")) {
		$text.removeClass("commaFieldDummy").addClass("commaField");
		$text.commaField();
	}
}

/**
 * ネストされた参照型の入力テーブルに日付型の入力欄追加
 * @param cell
 * @param idx
 * @return
 */
function addNestRow_Date(type, cell, idx) {
	if (type == "DATETIME") {
		var id = $(cell).children("input:hidden:last").attr("id").substring(2);
		$(cell).children(":text:first").each(function() {
			this.removeAttribute("onchange");
			$(this).change(function() {dateChange(id);});
			datepicker(this);
		});
	}
}

/**
 * ネストされた参照型の入力テーブルに時間型の入力欄追加
 * @param cell
 * @param idx
 * @return
 */
function addNestRow_Time(type, cell, idx) {
	if (type == "DATETIME") {

		var timeformat = $("span", cell).children(":text:first").attr("data-timeformat");
		var stepmin = $("span", cell).children(":text:first").attr("data-stepmin");

		if (timeformat && timeformat.length > 0 && stepmin && stepmin.length > 0) {

			var id = $("span", cell).children("input:hidden:last").attr("id").substring(2);
			$("span", cell).children(":text:first").each(function() {
				this.removeAttribute("onchange");
				$(this).change(function() {timePickerChange(id);});
				timepicker(this);
			});

		} else {

			var id = $("span", cell).children("input:hidden:last").attr("id").substring(2);
			$("span", cell).children("label").children("select").each(function() {
				this.removeAttribute("onchange");
				$(this).change(function() {timeSelectChange(id);});
			});

		}
	}
}

/**
 * ネストされた参照型の入力テーブルに日時型の入力欄追加
 * @param cell
 * @param idx
 * @return
 */
function addNestRow_Timestamp(type, cell, idx) {
	if (type == "DATETIME") {

		var timeformat = $("span", cell).children(":text:first").attr("data-timeformat");
		var stepmin = $("span", cell).children(":text:first").attr("data-stepmin");

		if (timeformat && timeformat.length > 0 && stepmin && stepmin.length > 0) {

			var id = $("span", cell).children("input:hidden:last").attr("id").substring(2);
			$("span", cell).children(":text:first").each(function() {
				this.removeAttribute("onchange");
				$(this).change(function() {timestampPickerChange(id);});
				datetimepicker(this);
			});

		} else {

			var id = $("span", cell).children("input:hidden:last").attr("id").substring(2);
			$("span", cell).children("label").children("select").each(function() {
				this.removeAttribute("onchange");
				$(this).change(function() {timestampSelectChange(id);});
			});

			$("span", cell).children(":text").each(function() {
				this.removeAttribute("onchange");
				$(this).change(function() {timestampSelectChange(id);});
				datepicker(this);
			});
		}
	}
}

/**
 * ネストされた参照型の入力テーブルに参照型の入力欄追加
 * @param type
 * @param cell
 * @param idx
 * @return
 */
function addNestRow_Reference(type, cell, idx) {
	if (type == "LINK") {
		var $selButton = $(".sel-btn", cell);
		$("[data-specVersionKey]", cell).each(function() {
			replaceDummyAttr(this, "data-specVersionKey", idx);
		});
		$selButton.off("click");

		var $insButton = $(".ins-btn", $(cell));
		$insButton.off("click");

		if ($("body.modal-body").length != 0) {
			$selButton.subModalWindow();
			$insButton.subModalWindow();
		} else {
			$selButton.modalWindow();
			$insButton.modalWindow();
		}

		$selButton.on("click", function() {
			var selectAction = $selButton.attr("data-selectAction");
			var viewAction = $selButton.attr("data-viewAction");
			var defName = $selButton.attr("data-defName");
			var propName = replaceDummyAttr($selButton, "data-propName", idx);
			var multiplicity = $selButton.attr("data-multiplicity");
			var urlParam = $selButton.attr("data-urlParam");
			var refEdit = $selButton.attr("data-refEdit");
			var callbackKey = $selButton.attr("data-callbackKey");
			var viewName = $selButton.attr("data-viewName");
			var permitConditionSelectAll = $selButton.attr("data-permitConditionSelectAll");
			var callback = scriptContext[callbackKey];
			searchReference(selectAction, viewAction, defName, propName, multiplicity, false, urlParam, refEdit, callback, $selButton, viewName, permitConditionSelectAll);
		});

		$insButton.on("click", function() {
			var addAction = $insButton.attr("data-addAction");
			var viewAction = $insButton.attr("data-viewAction");
			var defName = $insButton.attr("data-defName");
			var propName = replaceDummyAttr($insButton, "data-propName", idx);
			var multiplicity = $insButton.attr("data-multiplicity");
			var urlParam = $insButton.attr("data-urlParam");
			var parentOid = $insButton.attr("data-parentOid");
			var parentVersion = $insButton.attr("data-parentVersion");
			var parentDefName = $insButton.attr("data-parentDefName");
			var refEdit = $insButton.attr("data-refEdit");
			var callbackKey = $insButton.attr("data-callbackKey");
			var callback = scriptContext[callbackKey]
			insertReference(addAction, viewAction, defName, propName, multiplicity, urlParam, parentOid, parentVersion, parentDefName, refEdit, callback, $insButton);
		});
	} else if (type == "TREE") {
		var $selBtn = $(".sel-btn", $(cell));
		replaceDummyAttr($selBtn, "data-container", idx);
		replaceDummyAttr($selBtn, "data-uniqueName", idx);
		$selBtn.refRecursiveTree();

		var $insButton = $(".ins-btn", $(cell));
		$insButton.off("click");

		if ($("body.modal-body").length != 0) {
			$insButton.subModalWindow();
		} else {
			$insButton.modalWindow();
		}
		$insButton.on("click", function() {
			var addAction = $insButton.attr("data-addAction");
			var viewAction = $insButton.attr("data-viewAction");
			var defName = $insButton.attr("data-defName");
			var propName = replaceDummyAttr($insButton, "data-propName", idx);
			var multiplicity = $insButton.attr("data-multiplicity");
			var urlParam = $insButton.attr("data-urlParam");
			var parentOid = $insButton.attr("data-parentOid");
			var parentVersion = $insButton.attr("data-parentVersion");
			var parentDefName = $insButton.attr("data-parentDefName");
			var refEdit = $insButton.attr("data-refEdit");
			var callbackKey = $insButton.attr("data-callbackKey");
			var callback = scriptContext[callbackKey]
			insertReference(addAction, viewAction, defName, propName, multiplicity, urlParam, parentOid, parentVersion, parentDefName, refEdit, callback, $insButton);
		});
	} else if (type == "REFCOMBO") {
		//TODO 連動コンボ時の初期ロードで実行されるjavascriptをどうにかする
		$(cell).children("select").each(function() {
		});
	}
}

/**
 * ネストされた参照型の入力テーブルに組み合わせたプロパティの入力欄追加
 * @param head
 * @param cell
 * @param idx
 */
function addNestRow_Join(head, cell, idx) {
	var propName = $(head).children(":hidden[name='joinPropName']").val();
	var propCount = $(head).children(":hidden[name='joinPropCount']").val() - 0;
	for (var i = 0; i < propCount; i++) {
		var joinPropName = $(head).children(":hidden[name='joinPropName" + i + "']").val();
		var propType = $(head).children(":hidden[name='joinPropType" + i + "']").val();
		var dispType = $(head).children(":hidden[name='joinDispType" + i + "']").val();

		//.と[]はjQueryのセレクタに使われてるので\\つける
		var propNameIdx = propName.replace("idx", idx).replace("[", "\\[").replace("]", "\\]");
		var selector = "#join_" + propNameIdx + "_" + joinPropName + "_" + i;
		var $span = $(cell).children(selector);

		if (propType == "String" || propType == "LongText") {
			//文字列
			addNestRow_String(dispType, $span, idx);
		} else if (propType == "Date") {
			//日付
			addNestRow_Date(dispType, $span, idx);
		} else if (propType == "Time") {
			//時間
			addNestRow_Time(dispType, $span, idx);
		} else if (propType == "Timestamp") {
			//日時
			addNestRow_Timestamp(dispType, $span, idx);
		} else if (propType == "Binary") {
			//バイナリ
			$span.children(":file").each(function() {
				replaceDummyAttr(this, "pname", idx);
				var token = $(this).attr("token");
				uploadFile(this, token);
			});
		} else if (propType == "Reference") {
			//参照
			addNestRow_Reference(dispType, $span, idx);
//		} else if (propType == "Join") {
//			//連結→Join>Join>Stringとかは許容しないのでJoin>Stringとかで設定してもらう
//			addNestRow_Join(this, $td, idx);
		}
	}
}

/**
 * ネストされた参照型の入力テーブルに組み合わせたプロパティの入力欄追加
 * @param head
 * @param cell
 * @param idx
 */
function addNestRow_DateRange(head, cell, idx) {
	var prefix = $(head).children(":hidden[name='dateRangePrefix']").val();
	var fromPropName = $(head).children(":hidden[name='fromPropName']").val();
	var toPropName = $(head).children(":hidden[name='toPropName']").val();
	var propType = $(head).children(":hidden[name='dateRangePropType']").val();
	var dispType = $(head).children(":hidden[name='dateRangeDispType']").val();

	//.と[]はjQueryのセレクタに使われてるので\\つける
	var prefixIdx = prefix.replace("idx", idx).replace("[", "\\[").replace("]", "\\]");
	var fromSelector = "#daterange_" + prefixIdx + fromPropName;
	var $from = $(cell).children(fromSelector);
	var toSelector = "#daterange_" + prefixIdx + toPropName;
	var $to = $(cell).children(toSelector);

	if (propType == "Date") {
		//日付
		addNestRow_Date(dispType, $from, idx);
		addNestRow_Date(dispType, $to, idx);
	} else if (propType == "Time") {
		//時間
		addNestRow_Time(dispType, $from, idx);
		addNestRow_Time(dispType, $to, idx);
	} else if (propType == "Timestamp") {
		//日時
		addNestRow_Timestamp(dispType, $from, idx);
		addNestRow_Timestamp(dispType, $to, idx);
	}
}

/**
 * 参照型の入力テーブルのセルの種類を取得
 * @param cell
 * @return
 */
function getType(cell) {
	var ret = new Array();
	$(cell).children(":hidden").each(function(index, elem) {
		ret[index] = $(this).val();
	});
	return ret;
}

/**
 * ネストテーブル表示順アップ（編集画面）
 * @param rowId
 */
function shiftUp(rowId) {
	var $targetRow = $("#" + rowId);
	//対称なしなら終了
	if ($targetRow.length === 0) return;

	var $shiftRow = $targetRow.prev();
	//先頭行なら終了
	if ($shiftRow.is(":hidden")) return;

	//位置とインデックス入れ替え
	var $currentOrder = $("[name^='tableOrderIndex']", $targetRow);
	var currentOrderIndex = $currentOrder.val();

	var $shiftOrder = $("[name^='tableOrderIndex']", $shiftRow);
	var shiftOrderIndex = $shiftOrder.val();

	$shiftRow.before($targetRow);
	$currentOrder.val(shiftOrderIndex);
	$shiftOrder.val(currentOrderIndex);
}

/**
 * ネストテーブル表示順ダウン（編集画面）
 * @param rowId
 */
function shiftDown(rowId) {
	var $targetRow = $("#" + rowId);
	//対称なしなら終了
	if ($targetRow.length === 0) return;

	var $shiftRow = $targetRow.next();
	//最終行なら終了
	if ($shiftRow.length === 0) return;

	//位置とインデックス入れ替え
	var $currentOrder = $("[name^='tableOrderIndex']", $targetRow);
	var currentOrderIndex = $currentOrder.val();

	var $shiftOrder = $("[name^='tableOrderIndex']", $shiftRow);
	var shiftOrderIndex = $shiftOrder.val();

	$shiftRow.after($targetRow);
	$currentOrder.val(shiftOrderIndex);
	$shiftOrder.val(currentOrderIndex);
}

/**
 * ネストテーブル表示順入れ替え（表示画面）
 * @param webapi
 * @param rowId
 * @param orderPropName
 * @param key
 * @param refDefName
 * @param shiftUp
 * @param reloadUrl
 * @param rootDefName
 * @param viewName
 */
function shiftOrder(webapi, rowId, orderPropName, key, refDefName, shiftUp, reloadUrl, rootDefName, viewName) {
	var $targetRow = $("#" + rowId);
	var targetKey = $("[name='" + key + "']", $targetRow).val();
	if (typeof targetKey === "undefined" || targetKey === null || targetKey === "") return;

	var $shiftRow = shiftUp === true ? $targetRow.prev() : $targetRow.next();
	var shiftKey = $("[name='" + key + "']", $shiftRow).val();
	if (typeof shiftKey === "undefined" || shiftKey === null || shiftKey === "") return;

	var param = "{";
	param += "\"defName\":\"" + rootDefName + "\"";
	param += ",\"viewName\":\"" + viewName + "\"";
	param += ",\"refDefName\":\"" + refDefName + "\"";
	param += ",\"targetKey\":\"" + targetKey + "\"";
	param += ",\"shiftKey\":\"" + shiftKey + "\"";
	param += ",\"orderPropName\":\"" + orderPropName + "\"";
	param += ",\"shiftUp\":\"" + shiftUp + "\"";
	param += ",\"_t\":\"" + $(":hidden[name='_t']").val() + "\"";
	param += "}";
	postAsync(webapi, param, function(results) {
		$("#detailForm").attr("action", reloadUrl).submit()
	});
}

function deleteRefTableRow(id) {
	var $tbody = $("#" + id).parent();
	deleteItem(id);
	if ($("tr:not(:hidden)", $tbody).length == 0) {
		$tbody.parent("table").hide();
	};
}

/**
 * 文字列の数値チェック
 * @param str
 * @returns boolean
 */
function numCheck(str) {

	if (str.match(/[^0-9]+/)) {
	  return true;
  }
  return false;
}

function replaceDummyAttr(elem, key, dst) {
	$(elem).attr(key, replaceDummy($(elem).attr(key), dst));
	return $(elem).attr(key);
}

/**
 * src内の文字列(Dummy)をdstに置換
 * @param src
 * @param dst
 * @return
 */
function replaceDummy(src, dst) {
	if (src != null && src != "") {
		return src.replace("Dummy", dst);
	}
	return src;
}

/**
 * src内の文字列(Dummy)をdstに全置換
 * @param src
 * @param dst
 * @return
 */
function replaceAllDummy(src, dst) {
	return replaceAll(src, "Dummy", dst);
}
function replaceAll(src, reg, dst) {
	if (src != null && src != "") {
		return src.split(reg).join(dst);
	}
}

/**
 * 数値の十の桁を0埋め
 * @param num
 * @return
 */
function fill(num, size) {
	var fillStr = "";
	for (var i = 0; i < size; i++) {
		fillStr = fillStr + "0";
	}
	return (fillStr + num).slice(-size);
}


/**
 * バイナリデータのURL生成
 * @param br
 * @param downloadUrl
 * @returns {String}
 */
function url(br, downloadUrl) {
	var url = downloadUrl + "?id=" + br.lobId;
	if (br.definitionName != null) {
		url = url + "&defName=" + br.definitionName + "&propName=" + br.propertyName;
	}
	return url;
}

function uniqueId() {
	// DOMにユニークなIDを割り振るためにランダム関数とタイムスタンプで、簡易にIDを作成
	var randam = Math.floor(Math.random() * 1000).toFixed(0);
	var date = new Date();
	var time = date.getTime();
	return randam + '_' + time.toString();
}

/**
 * jQueryのselector用文字列をエスケープ
 * @param str
 * @returns
 */
function es(str) {
	return str.replace(/[#;&,\.\+\*~':"!\^\$\[\]\(\)=>|\/\\]/g, '\\$&');
}
/**
 * jQueryのselector用文字列をリプレイス
 * 内部でセレクタのエスケープをしていないplugin用
 * @param str
 * @returns
 */
function rs(str) {
	return str.replace(/[#;&,\.\+\*~':"!\^\$\[\]\(\)=>|\/\\]/g, '_');
}

function startsWith(str, prefix) {
	return str.indexOf(prefix, 0) == 0;
}

/**
 * JSONパラメータ用文字列のエスケープ
 * @param value
 * @returns
 */
function escapeJsonParamValue(value) {
	if (typeof value === "undefined" || value == null) {
		return "";
	}
	if (typeof value.replace === "undefined") {
		//replaceがない→文字列ではないのでそのまま返す
		return value;
	}
//	//\をエスケープ後、"をエスケープ、また改行とタブもエスケープ
//	return value.replace(/\\/g, "\\\\").replace(/\"/g, "\\\"").replace(/[\n\r\t]/g,"");
	//\をエスケープ後、"をエスケープ(制御文字はサーバ側で許可されるのでエスケープする必要なし)
	return value.replace(/\\/g, "\\\\").replace(/\"/g, "\\\"");

}

/**
 * 引数の{\d}を呼び出しもとのargumentsで置換。
 * JavaのMessage#formatのJSバージョン。
 *
 * @param str
 * @returns 置換文字列
 */
function messageFormat(str) {
	var args = arguments;
	return str.replace(/{(\d)}/g, function(r, n) { return args[+n+1];});
}

/**
 * Langに該当する文字列を返します。
 *
 * @param viewString View(またはEditor)のデフォルト値
 * @param viewLocalizedStringList View(またはEditor)のLocalizedStringのリスト
 * @param defaultString Propertyなどのデフォルト値
 * @param defaultLocalizedStringList PropertyなどのLocalizedStringのリスト
 * @returns Lang該当文字列
 */
function getMultilingualStringWithView(viewString, viewLocalizedStringList, defaultString, defaultLocalizedStringList) {
	var viewLabel = getMultilingualString(viewString, viewLocalizedStringList);
	if (viewLabel == null || typeof viewLabel === "undefined" || viewLabel == "") {
		return getMultilingualString(defaultString, defaultLocalizedStringList);
	} else {
		return viewLabel;
	}
}

/**
 * Langに該当する文字列を返します。
 *
 * @param defaultString Propertyなどのデフォルト値
 * @param defaultLocalizedStringList PropertyなどのLocalizedStringのリスト
 * @returns Lang該当文字列
 */
function getMultilingualString(defaultString, localizedStringList) {
	var multilingualString = defaultString;
	var lang = scriptContext.locale.defaultLocale;
	if (lang != null && localizedStringList != null) {
		for (var i = 0; i < localizedStringList.length; i++) {
			var lsd = localizedStringList[i];
			if (lang == lsd.localeName) {
				multilingualString = lsd.stringValue;
				break;
			}
		}
	}
	return multilingualString;
}

function adjustDialogLayer($layer) {
	var winHeight = $(window).height();
	var containerHeight = $("#container").height()
	if (winHeight >= containerHeight) {
		$layer.height(winHeight);
	} else {
		$layer.height(containerHeight);
	}

	var winWidth = $(window).width();
	var docWidth = $(document).width();
	var containerWidth = $("#container").width();
	var width = winWidth > docWidth ? winWidth : docWidth;
	if (containerWidth > width) width = containerWidth;
	$layer.width(width);
}


/**
 * TemplateUtil#getResourceContentPathのjs版
 */
function getResourceContentPath(resourcePath) {
	if (resourcePath == null || resourcePath == "") {
		return "";
	}

	if (startsWith(resourcePath, "/")) {
		//静的コンテキストパスからと判断
		return contentPath + resourcePath;
	} else if (startsWith(resourcePath, "http")) {
		//外部リソースなのでそのまま返す
		return resourcePath;
	} else {
		//テナント配下の指定と判断
		return contextPath + "/" + resourcePath;
	}
}

function mePlayer() {

}

var DateUtil = function(option) {
	var util = this;

	this._option = option;

	this._tokenMap = new Map();
	//Java > Moment/Datepickerのフォーマットのマッピング
	this._tokenMap.set("yyyy", {type: "year", moment: "YYYY", datepicker: "yy"});
	this._tokenMap.set("MM", {type: "month", moment: "MM", datepicker: "mm"});
	this._tokenMap.set("MMM", {type: "month", moment: "MMM", datepicker: "M"});
	this._tokenMap.set("MMMM", {type: "month", moment: "MMMM", datepicker: "MM"});
	this._tokenMap.set("dd", {type: "day", moment: "DD", datepicker: "dd"});
	this._tokenMap.set("HH", {type: "hour", moment: "HH", datepicker: "HH"});
	this._tokenMap.set("mm", {type: "min", moment: "mm", datepicker: "mm"});
	this._tokenMap.set("ss", {type: "sec", moment: "ss", datepicker: "si"});
	this._tokenMap.set("SSS", {type: "msec", moment: "SSS", datepicker: "|"});

	this._dateDelimiter = option.dateDelimiter ? option.dateDelimiter : "/";
	this._timeDelimiter = option.timeDelimiter ? option.timeDelimiter : ":";

	this._serverDateTokens = null;
	this._serverTimeTokens = null;

	this._outputDateTokens = null;
	this._outputTimeTokens = null;

	this._inputDateTokens = null;
	this._inputTimeTokens = null;

	this.server = {
		dateFormat: null,
		timeFormat: null,
		getDateFormat: function() { return ""; },
		getTimeFormat: function() { return ""; },
		getDatetimeFormat: function() {
			return this.getDateFormat() + this.getTimeFormat();
		}
	}
	this.output = {
		dateFormat: null,
		timeSecFormat: null,
		timeMinFormat: null,
		timeHourFormat: null,
		getDateFormat: function() { return ""; },
		getTimeFormat: function(range) { return ""; },
		getDatetimeFormat: function(range) {
			return this.getDateFormat() + " " + this.getTimeFormat(range);
		},
		getWeekdayFormat: function() { return "dddd"; }
	};
	this.input = {
		dateFormat: null,
		timeSecFormat: null,
		timeMinFormat: null,
		timeHourFormat: null,
		secFormat: null,
		minFormat: null,
		hourFormat: null,
		getDateFormat: function() { return ""; },
		getTimeFormat: function(range) { return ""; },
		getDatetimeFormat: function(range) {
			return this.getDateFormat() + " " + this.getTimeFormat(range);
		},
		getHourFormat: function(range) { return ""; },
		getMinFormat: function(range) { return ""; },
		getSecFormat: function(range) { return ""; }
	};
	this.datepicker = {
		dateFormat: null,
		getDateFormat: function() { return ""; }
	}

	//サーバ送信用フォーマット
	if (option.server) {
		if (option.server.dateFormat) {
			var serverDateDelimiter = option.server.dateFormat.indexOf(util._dateDelimiter) > -1 ? util._dateDelimiter : "";
			this._serverDateTokens = getTokens(option.server.dateFormat, this._tokenMap);
			this.server.getDateFormat = function() {
				if (!this.dateFormat) {
					this.dateFormat = createDateFormat(util._serverDateTokens, "moment", serverDateDelimiter);
				}
				return this.dateFormat;
			}
		}
		if (option.server.timeFormat) {
			var serverTimeDelimiter = option.server.timeFormat.indexOf(util._timeDelimiter) > -1 ? util._timeDelimiter : "";
			this._serverTimeTokens = getTokens(option.server.timeFormat, this._tokenMap);
			this.server.getTimeFormat = function() {
				if (!this.timeFormat) {
					var targetType = new Array("msec", "sec", "min", "hour");
					this.timeFormat = createTimeFormat(util._serverTimeTokens, "moment", serverTimeDelimiter, targetType);
				}
				return this.timeFormat;
			}
		}
	}
	//表示用フォーマット
	if (option.output) {
		//日付
		if (option.output.dateFormat) {
			var outputDateDelimiter = option.output.dateFormat.indexOf(util._dateDelimiter) > -1 ? util._dateDelimiter : "";
			this._outputDateTokens = getTokens(option.output.dateFormat, this._tokenMap);
			this.output.getDateFormat = function() {
				if (!this.dateFormat) {
					this.dateFormat = createDateFormat(util._outputDateTokens, "moment", outputDateDelimiter);
				}
				return this.dateFormat;
			}
		}
		//時間
		if (option.output.timeFormat) {
			this._outputTimeTokens = getTokens(option.output.timeFormat, this._tokenMap);
			this.output.getTimeFormat = function(range) {
				var _range = range;
				if (!_range) _range = "sec";

				if (_range === "sec") {
					if (!this.timeSecFormat) {
						var targetType = new Array("sec", "min", "hour");
						this.timeSecFormat = createTimeFormat(util._outputTimeTokens, "moment", util._timeDelimiter, targetType);
					}
					return this.timeSecFormat;
				} else if (_range === "min") {
					if (!this.timeMinFormat) {
						var targetType = new Array("min", "hour");
						this.timeMinFormat = createTimeFormat(util._outputTimeTokens, "moment", util._timeDelimiter, targetType);
					}
					return this.timeMinFormat;
				} else if (_range === "hour") {
					if (!this.timeHourFormat) {
						var targetType = new Array("hour");
						this.timeHourFormat = createTimeFormat(util._outputTimeTokens, "moment", util._timeDelimiter, targetType);
					}
					return this.timeHourFormat;
				}
			}
		}
	}
	//入力用フォーマット
	if (option.input) {
		//日付
		if (option.input.dateFormat) {
			var inputDateDelimiter = option.input.dateFormat.indexOf(util._dateDelimiter) > -1 ? util._dateDelimiter : "";
			this._inputDateTokens = getTokens(option.input.dateFormat, this._tokenMap);
			this.input.getDateFormat = function() {
				if (!this.dateFormat) {
					this.dateFormat = createDateFormat(util._inputDateTokens, "moment", inputDateDelimiter);
				}
				return this.dateFormat;
			}
			this.datepicker.getDateFormat = function() {
				if (!this.dateFormat) {
					this.dateFormat = createDateFormat(util._inputDateTokens, "datepicker", inputDateDelimiter);
				}
				return this.dateFormat;
			}
		}
		//時間
		if (option.input.timeFormat) {
			this._inputTimeTokens = getTokens(option.input.timeFormat, this._tokenMap);
			this.input.getTimeFormat = function(range) {
				var _range = range;
				if (!_range) _range = "sec";

				if (_range === "sec") {
					if (!this.timeSecFormat) {
						var targetType = new Array("sec", "min", "hour");
						this.timeSecFormat = createTimeFormat(util._inputTimeTokens, "moment", util._timeDelimiter, targetType);
					}
					return this.timeSecFormat;
				} else if (_range === "min") {
					if (!this.timeMinFormat) {
						var targetType = new Array("min", "hour");
						this.timeMinFormat = createTimeFormat(util._inputTimeTokens, "moment", util._timeDelimiter, targetType);
					}
					return this.timeMinFormat;
				} else if (_range === "hour") {
					if (!this.timeHourFormat) {
						var targetType = new Array("hour");
						this.timeHourFormat = createTimeFormat(util._inputTimeTokens, "moment", util._timeDelimiter, targetType);
					}
					return this.timeHourFormat;
				}
			}
			this.input.getHourFormat = function() {
				if (!this.hourFormat) {
					var token = getTimeFormatToken(util._inputTimeTokens, "hour");
					this.hourFormat = token.moment;
				}
				return this.hourFormat;
			}
			this.input.getMinFormat = function() {
				if (!this.minFormat) {
					var token = getTimeFormatToken(util._inputTimeTokens, "min");
					this.minFormat = token.moment;
				}
				return this.minFormat;
			}
			this.input.getSecFormat = function() {
				if (!this.secFormat) {
					var token = getTimeFormatToken(util._inputTimeTokens, "sec");
					this.secFormat = token.moment;
				}
				return this.secFormat;
			}
		}
	}

	//フォーマットを分解して変換用のトークンを取得
	function getTokens(format, map) {
		var tokens = new Array();
		var formatChars = format.split("");
		var i = 0, beginIndex = -1, currentChar = null, lastChar = null;
		for (; i < formatChars.length; i++) {
			currentChar = formatChars[i];
			if (lastChar === null || lastChar !== currentChar) {
				var token = getMappedToken(format, beginIndex, i, map);
				if (token) {
					tokens.push(token);
				}
				beginIndex = i;
			}
			lastChar = currentChar;
		}
		var token = getMappedToken(format, beginIndex, i, map);
		if (token) {
			tokens.push(token);
		}
		return tokens;
	}
	//フォーマット内の文字列にマッチするトークンを取得
	function getMappedToken(format, beginIndex, currentIndex, map) {
		var tmpStr = format.substr(beginIndex, currentIndex - beginIndex);
		if (map.get(tmpStr)) {
			return map.get(tmpStr);
		}
		return null;
	}
	//日付のフォーマットを作成
	function createDateFormat(tokens, tokenType, delimiter) {
		var ret = new Array();
		for (var i = 0; i < tokens.length; i++) {
			var token = tokens[i];
			if (token && token[tokenType]) {
				ret.push(token[tokenType]);
			}
		}
		return ret.join(delimiter);
	}
	//時間のフォーマットを作成
	function createTimeFormat(tokens, tokenType, delimiter, targetType) {
		var ret = new Array();
		for (var i = 0; i < tokens.length; i++) {
			var token = tokens[i];
			if (token && token[tokenType] && targetType.indexOf(token.type) >= 0) {
				ret.push(token[tokenType]);
			}
		}
		return ret.join(delimiter);
	}
	//時間の各部位のトークンを取得
	function getTimeFormatToken(tokens, tokenType) {
		for (var i = 0; i < tokens.length; i++) {
			var token = tokens[i];
			if (token && token.type === tokenType) {
				return token;
			}
		}
		return null;
	}
}
//サーバ送信用フォーマット
DateUtil.prototype.getServerDateFormat = function() {
	return this.server.getDateFormat();
}
DateUtil.prototype.getServerTimeFormat = function() {
	return this.server.getTimeFormat();
}
DateUtil.prototype.getServerDatetimeFormat = function() {
	return this.server.getDatetimeFormat();
}
//Datepicker用フォーマット
DateUtil.prototype.getDatepickerDateFormat = function() {
	return this.datepicker.getDateFormat();
}
//表示用フォーマット
DateUtil.prototype.getOutputDateFormat = function() {
	return this.output.getDateFormat();
}
DateUtil.prototype.getOutputTimeFormat = function(range) {
	return this.output.getTimeFormat(range);
}
DateUtil.prototype.getOutputDatetimeFormat = function(range) {
	return this.output.getDatetimeFormat(range);
}
DateUtil.prototype.getOutputWeekdayFormat = function() {
	return this.output.getWeekdayFormat();
}
//入力用フォーマット
DateUtil.prototype.getInputDateFormat = function() {
	return this.input.getDateFormat();
}
DateUtil.prototype.getInputTimeFormat = function(range) {
	return this.input.getTimeFormat(range);
}
DateUtil.prototype.getInputDatetimeFormat = function(range) {
	return this.input.getDatetimeFormat(range);
}
DateUtil.prototype.getInputHourFormat = function() {
	return this.input.getHourFormat();
}
DateUtil.prototype.getInputMinFormat = function() {
	return this.input.getMinFormat();
}
DateUtil.prototype.getInputSecFormat = function() {
	return this.input.getSecFormat();
}
//Datepicker用フォーマット
DateUtil.prototype.getDatepickerDateFormat = function() {
	return this.datepicker.getDateFormat();
}

//文字列から文字列に
DateUtil.prototype.newFormatString = function(dateStr, oldFormat, newFormat) {
	var m = new moment(dateStr, oldFormat);
	return m.format(newFormat);
}
//文字列からdateに
DateUtil.prototype.toDate = function(dateStr, format) {
	var m = new moment(dateStr, format);
	return m.toDate();
}
DateUtil.prototype.getWeekday = function(dateStr, format) {
	var m = new moment(dateStr, format);
	m.locale(scriptContext.locale.defaultLocale);
	return m.format(this.getOutputWeekdayFormat());
}
//dateから文字列に
DateUtil.prototype.format = function(dateObj, format) {
	var m = new moment(dateObj);
	return m.format(format);
}
DateUtil.prototype.formatOutputDate = function(dateObj) {
	return this.format(dateObj, this.getOutputDateFormat());
}
DateUtil.prototype.formatOutputTime = function(dateObj, range) {
	return this.format(dateObj, this.getOutputTimeFormat(range));
}
DateUtil.prototype.formatOutputDatetime = function(dateObj, range) {
	return this.format(dateObj, this.getOutputDatetimeFormat(range));
}
DateUtil.prototype.formatInputDate = function(dateObj) {
	return this.format(dateObj, this.getInputDateFormat());
}
DateUtil.prototype.formatInputTime = function(dateObj, range) {
	return this.format(dateObj, this.getInputTimeFormat(range));
}
DateUtil.prototype.formatInputDatetime = function(dateObj, range) {
	return this.format(dateObj, this.getInputDatetimeFormat(range));
}
DateUtil.prototype.formatInputHour = function(dateObj) {
	return this.format(dateObj, this.getInputHourFormat());
}
DateUtil.prototype.formatInputMin = function(dateObj) {
	return this.format(dateObj, this.getInputMinFormat());
}
DateUtil.prototype.formatInputSec = function(dateObj) {
	return this.format(dateObj, this.getInputSecFormat());
}

var dateUtil = new DateUtil({
	//サーバ送信用フォーマット
	server: {
		dateFormat: scriptContext.locale.serverDateFormat,
		timeFormat: scriptContext.locale.serverTimeFormat
	},
	//表示用フォーマット
	output: {
		dateFormat: scriptContext.locale.outputDateFormat,
		timeFormat:scriptContext.locale.outputTimeSecFormat
	},
	//入力用フォーマット
	input: {
		dateFormat: scriptContext.locale.inputDateFormat,
		timeFormat:scriptContext.locale.inputTimeSecFormat
	}
});
