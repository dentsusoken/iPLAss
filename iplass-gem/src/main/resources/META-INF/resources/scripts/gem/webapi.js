/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * <p>WebAPIを実行します。</p>
 *
 * <p>
 * optionにはjQuery.ajaxに対するパラメータを指定できます。
 * この関数ではajax呼び出し時に、以下のカスタマイズを行っています。
 * </p>
 *
 * <h3>urlについて</h3>
 * <p>
 * 実行時のurlは指定されたapiName引数を利用して、
 * <pre>
 * [contextPath + "/api/" + apiName]
 * </pre>
 * として実行します。
 * </p>
 *
 * <h3>successについて</h3>
 * <p>
 * success時のcallbackはカスタマイズしています。<br/>
 * 返ってきたresponseを引数として呼び出します。
 * </p>
 *
 * <h3>errorについて</h3>
 * <p>
 * error時のcallbackはカスタマイズしています。<br/>
 * 指定されている場合、XMLHttpRequestを引数として呼び出します。
 * 未指定の場合、エラーメッセージを表示します。
 * </p>
 *
 * @param apiName 実行WebAPI名
 * @param option ajaxカスタムパラメータ
 * @returns XMLHttpRequestオブジェクト(中断時に利用)
 */
function webapi(apiName, option) {
	var okFunc = null;
	if  (option.success) {
		if ($.isFunction(option.success)) {
			okFunc = option.success;
		}
		delete option["success"];	//successはカスタマイズするためパラメータから削除
	}
	var ngFunc = null;
	if  (option.error) {
		if ($.isFunction(option.error)) {
			ngFunc = option.error;
		}
		delete option["error"];	//errorはカスタマイズするためパラメータから削除
	}

	var defaults = {
		success: function(response) {
			if (okFunc && $.isFunction(okFunc)) {
				//responseを返す
				okFunc.call(this, response);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (!isPageBeingRefreshed) {
				//画面遷移によるエラー以外の場合はエラー処理を実行
				if  (ngFunc && $.isFunction(ngFunc)) {
					ngFunc.call(this, XMLHttpRequest);
				} else {
					//未指定の場合は、標準のエラー通知
					alertErrMessage(XMLHttpRequest);
				}
			}
		}
	};
	var options = $.extend(defaults, option);

	//urlはカスタマイズさせない
	options.url = contextPath + "/api/" + apiName;

	return $.ajax(options);
}

/**
 * <p>WebAPIを実行します。</p>
 *
 * <p>
 * WebAPI実行には、webapiを呼び出しますが、
 * contentTypeを["application/json"]、dataTypeを["json"]
 * にカスタマイズしています。
 * </p>
 *
 * @param apiName 実行WebAPI名
 * @param option ajaxカスタムパラメータ
 * @returns XMLHttpRequestオブジェクト(中断時に利用)
 */
function webapiJson(apiName, option) {

	var defaults = {
		contentType: "application/json",
		dataType: "json"
	};
	var options = $.extend(defaults, option);

	return webapi(apiName, options);
}

function postAsync(apiName, params, okFunc, async, ngFunc) {

	var option = {
		data: params,
		type: "POST",
		cache: false,
		async: false
	};

	if (async != null) {
		option.async = async;
	}
	if (okFunc != null) {
		option.success = okFunc;
	}
	if (ngFunc != null) {
		option.error = ngFunc;
	}

	return webapiJson(apiName, option);
}

/**
 * 非同期通信時の画面遷移判断用
 */
var isPageBeingRefreshed = false;
$(window).on('beforeunload', function(){
    isPageBeingRefreshed = true;
});

function alertErrMessage(XMLHttpRequest) {
	var res = null;
	try {
		res = JSON.parse(XMLHttpRequest.responseText);
	} catch (e) {
	}

	if (typeof res !== "undefined" && res != null
			&& res.exceptionType == "org.iplass.mtp.auth.NoPermissionException") {
		alert(scriptContext.gem.locale.error.permissionErrOccurred);
	} else {
		alert(scriptContext.gem.locale.error.errOccurred);
	}
}

/**
 * 未使用のトランザクショントークンを消費する
 * @param webapi
 * @param token
 * @param func
 */
function consumeToken(webapi, token, func) {
	var params = "{";
	params += "\"_t\":\"" + token + "\"";
	params += "}";
	postAsync(webapi, params, function(results){
		if (func && $.isFunction(func)) func.call(this);
	});
}


/**
 * 非同期検索
 * @param searchType
 * @param formName
 * @param func
 */
function search(webapi, searchType, formName, isCount, successFunc, errorFunc) {
	//validation(common.js)
	if (!condCommonValidate()) {
		if (errorFunc && $.isFunction(errorFunc)) {
			errorFunc.call(this);
		}
		return;
	}

	//共通項目defName,searchType,limit,offset,sortKey,sortType
	$(":hidden[name='searchType']").val(searchType);
	$(":hidden[name='formName']").val(formName);
	var data = $("[name='" + formName + "']").serialize();
	data += "&defName=" + $(":hidden[name='defName']").val();
	data += "&searchType=" + searchType;
	data += "&limit=" + $(":hidden[name='limit']").val();
	data += "&offset=" + $(":hidden[name='offset']").val();
	data += "&sortKey=" + $(":hidden[name='sortKey']").val();
	data += "&sortType=" + $(":hidden[name='sortType']").val();
	data += "&noLimit=" + $(":hidden[name='noLimit']").val();
	data += "&isSearch=true";
	data += "&isCount=" + isCount;
	$.ajax({
		type: "POST",
		//contentType: "application/json",
		url: contextPath + "/api/" + webapi,
		dataType: "json",
		data: data,
		success: function(response) {
			if (response.status == "SUCCESS") {
				var count = response.count;
				var htmlData = response.htmlData;
				// フォームパラメータのQueryStringをdecodeする
				$(":hidden[name='searchCond']").val(decodeQueryString(data));
				if (successFunc && $.isFunction(successFunc)) {
					successFunc.call(this, htmlData, count);
				}
			} else {
				if (response.message != null && response.message != "") {
					alert(response.message);
				} else {
					alert(scriptContext.gem.locale.error.errOccurred);
				}

				if (errorFunc && $.isFunction(errorFunc)) {
					errorFunc.call(this);
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (!isPageBeingRefreshed) {
				//画面遷移によるエラー以外の場合はエラー処理を実行
				alertErrMessage(XMLHttpRequest);
				if (errorFunc && $.isFunction(errorFunc)) {
					errorFunc.call(this);
				}
			}
		}
	});
}

/**
 * 非同期検索検証(CSVDownload用)
 */
function searchValidate(webapi, searchType, formName, successFunc, errorFunc) {
	//validation(common.js)
	if (!condCommonValidate()) {
		if (errorFunc && $.isFunction(errorFunc)) {
			errorFunc.call(this);
		}
		return;
	}

	//共通項目defName,searchType,limit,offset,sortKey,sortType
	$(":hidden[name='searchType']").val(searchType);
	$(":hidden[name='formName']").val(formName);
	var data = $("[name='" + formName + "']").serialize();
	data += "&defName=" + $(":hidden[name='defName']").val();
	data += "&searchType=" + searchType;
	$.ajax({
		type: "POST",
		//contentType: "application/json",
		url: contextPath + "/api/" + webapi,
		dataType: "json",
		data: data,
		success: function(response) {
			if (response.status == "SUCCESS") {
				if (successFunc && $.isFunction(successFunc)) {
					successFunc.call(this);
				}
			} else {
				if (response.message != null && response.message != "") {
					alert(response.message);
				} else {
					alert(scriptContext.gem.locale.error.errOccurred);
				}

				if (errorFunc && $.isFunction(errorFunc)) {
					errorFunc.call(this);
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (!isPageBeingRefreshed) {
				//画面遷移によるエラー以外の場合はエラー処理を実行
				alertErrMessage(XMLHttpRequest);
				if (errorFunc && $.isFunction(errorFunc)) {
					errorFunc.call(this);
				}
			}
		}
	});
}

function count(webapi, searchType, formName, func) {
	//共通項目defName,searchType
	$(":hidden[name='searchType']").val(searchType);
	$(":hidden[name='formName']").val(formName);
	var data = $("[name='" + formName + "']").serialize();
	data += "&defName=" + $(":hidden[name='defName']").val();
	data += "&searchType=" + searchType;
	data += "&isSearch=false";
	data += "&isCount=true";
	$.ajax({
		type: "POST",
		//contentType: "application/json",
		url: contextPath + "/api/" + webapi,
		dataType: "json",
		data: data,
		success: function(response) {
			var count = response.count;
			if (func && $.isFunction(func)) func.call(this, count);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (!isPageBeingRefreshed) {
				//画面遷移によるエラー以外の場合はエラー処理を実行
				alertErrMessage(XMLHttpRequest);
			}
		}
	});
}

function searchSelectList(webapi, searchType, formName, func) {
	//共通項目defName,searchType
	$(":hidden[name='searchType']").val(searchType);
	$(":hidden[name='formName']").val(formName);
	var data = $("[name='" + formName + "']").serialize();
	data += "&defName=" + $(":hidden[name='defName']").val();
	data += "&searchType=" + searchType;
	data += "&isSearch=true";
	data += "&isCount=false";
	$.ajax({
		type: "POST",
		//contentType: "application/json",
		url: contextPath + "/api/" + webapi,
		dataType: "json",
		data: data,
		success: function(response) {
			var data = response.data;
			if (func && $.isFunction(func)) func.call(this, data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (!isPageBeingRefreshed) {
				//画面遷移によるエラー以外の場合はエラー処理を実行
				alertErrMessage(XMLHttpRequest);
			}
		}
	});
}

function searchEntityList(webapi, defName, viewName, filterName, offset, sortKey, sortType, searchAsync, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"filterName\":\"" + filterName + "\"";
	params += ",\"offset\":\"" + offset + "\"";
	if (typeof sortKey !== "undefined" && sortKey != null && sortKey != "") {
		params += ",\"sortKey\":\"" + sortKey + "\"";
	}
	if (typeof sortType !== "undefined" && sortType != null && sortType != "") {
		params += ",\"sortType\":\"" + sortType + "\"";
	}
	params += "}";

	postAsync(webapi, params, function(results){
		var count = results.count;
		var list = results.htmlData;
		if (func && $.isFunction(func)) func.call(this, count, list);
	}, searchAsync == "true" ? true : false);
}

function searchNameList(webapi, defName, viewName, filterName, offset, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"filterName\":\"" + filterName + "\"";
	params += ",\"offset\":\"" + offset + "\"";
	params += "}";
	postAsync(webapi, params, function(results){
		var count = results.count;
		var list = results.list;
		if (func && $.isFunction(func)) func.call(this, count, list);
	});
}

function deleteList(webapi, oid, viewName, allVersion, _t, func) {
	var first = true;
	var params = "{";
	params += "\"defName\":\"" + $(":hidden[name='defName']").val() + "\"";
	params += ",\"_t\":\"" + _t + "\"";
	params += ",\"oid\":[";
	$(oid).each(function() {
		if (first) {
			first = false;
		} else {
			params += ",";
		}
		params += "\"" + this + "\"";
	});
	params += "]";
	params += ",\"allVersion\":\"" + allVersion + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var message = results.message;
		if (func && $.isFunction(func)) func.call(this, message);
	});
}

function deleteAll(webapi, searchType, formName, allVersion, _t, func) {
	//共通項目defName,searchType
	$(":hidden[name='searchType']").val(searchType);
	$(":hidden[name='formName']").val(formName);
	var data = $("[name='" + formName + "']").serialize();
	data += "&defName=" + $(":hidden[name='defName']").val();
	data += "&searchType=" + searchType;
	data += "&isSearch=false";
	data += "&isCount=true";
	data += "&allVersion=" + allVersion;
	data += "&_t=" + _t;
	$.ajax({
		type: "POST",
		//contentType: "application/json",
		url: contextPath + "/api/" + webapi,
		dataType: "json",
		data: data,
		success: function(response) {
			var message = response.message;
			if (func && $.isFunction(func)) func.call(this, message);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alertErrMessage(XMLHttpRequest);
		}
	});
}

function getMassReferenceData(webapi, oid, defName, propName, viewName, offset, sortKey, sortType, isCount, outputType, elementId, entityOid, entityVersion, func) {
	var params = "{";
	params += "\"oid\":\"" + escapeJsonParamValue(oid) + "\"";
	params += ",\"defName\":\"" + defName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"offset\":\"" + offset + "\"";
	params += ",\"sortKey\":\"" + sortKey + "\"";
	params += ",\"sortType\":\"" + sortType + "\"";
	params += ",\"isCount\":\"" + isCount + "\"";
	params += ",\"outputType\":\"" + outputType + "\"";
	params += ",\"elementId\":\"" + elementId + "\"";
	if (typeof entityOid !== "undefined" && entityOid != null) {
		params += ",\"entityOid\":\"" + escapeJsonParamValue(entityOid) + "\"";
	}
	if (typeof entityVersion !== "undefined" && entityVersion != null) {
		params += ",\"entityVersion\":\"" + entityVersion + "\"";
	}
	if ($(":hidden[name='searchCond']").length) {
		var searchCond = $(":hidden[name='searchCond']").val();
		params += ",\"searchCond\":\"" + escapeJsonParamValue(searchCond) + "\"";
	}
	params += "}";
	postAsync(webapi, params, function(results){
		var dispInfo = results.dispInfo;
		var count = results.count;
		var list = results.htmlData;
		if (func && $.isFunction(func)) func.call(this, dispInfo, count, list);
	});
}

function removeMappedByReference(webapi, oid, defName, viewName, propName, key, _t, purge, func) {
	var params = "{";
	params += "\"oid\":\"" + escapeJsonParamValue(oid) + "\"";
	params += ",\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"updatePropertyName\":\"" + propName + "\"";
	params += ",\"delCount\":\"" + key.length + "\"";
	params += ",\"_t\":\"" + _t + "\"";
	params += ",\"purge\":\"" + purge + "\"";
	for (var i = 0; i < key.length; i++) {
		//oid_version
		params += ",\"refEntityKey_" + i + "\":\"" + escapeJsonParamValue(key[i]) + "\"";
	}
	params += "}";
	postAsync(webapi, params, function(results) {
		var errors = results.errorProperty;
		var _t = results._t;
		if (func && $.isFunction(func)) func.call(this, errors, _t);
	});
}


function getRecycleBin(webapi, func) {
	var params = "{";
	params += "\"defName\":\"" + $(":hidden[name='defName']").val() + "\"";
	params += ",\"viewName\":\"" + $(":hidden[name='viewName']").val() + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var entity = results.entity;
		if (func && $.isFunction(func)) func.call(this, entity);
	});
}

function purge(webapi, rbid, _t, func, errFunc) {
	var first = true;
	var params = "{";
	params += "\"defName\":\"" + $(":hidden[name='defName']").val() + "\"";
	params += ",\"viewName\":\"" + $(":hidden[name='viewName']").val() + "\"";
	params += ",\"rbid\":[";
	$(rbid).each(function() {
		if (first) {
			first = false;
		} else {
			params += ",";
		}
		params += "\"" + this + "\"";
	});
	params += "]";
	params += ",\"_t\":\"" + _t + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		if (func && $.isFunction(func)) func.call(this);
	}, null, function(result) {
		alert(scriptContext.gem.locale.error.errOccurred);
		if  (errFunc && $.isFunction(errFunc)) {
			errFunc.call(this, XMLHttpRequest);
		}
	});
}

function restore(webapi, rbid, _t, func, errFunc) {
	var first = true;
	var params = "{";
	params += "\"defName\":\"" + $(":hidden[name='defName']").val() + "\"";
	params += ",\"viewName\":\"" + $(":hidden[name='viewName']").val() + "\"";
	params += ",\"rbid\":[";
	$(rbid).each(function() {
		if (first) {
			first = false;
		} else {
			params += ",";
		}
		params += "\"" + this + "\"";
	});
	params += "]";
	params += ",\"_t\":\"" + _t + "\"";
	params += "}";

	postAsync(webapi, params, function(results) {
		var message = results.message;
		var errorRbid = results.errorRbid;
		if (func && $.isFunction(func)) func.call(this, message, errorRbid);
	}, null, function(result) {
		alert(scriptContext.gem.locale.error.errOccurred);
		if  (errFunc && $.isFunction(errFunc)) {
			errFunc.call(this, XMLHttpRequest);
		}
	});
}

function getAutocompletionValue(webapi, defName, viewName, viewType, propName, key, refSectionIndex, pValue, cValue, entity, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"viewType\":\"" + viewType + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"autocompletionKey\":\"" + key + "\"";
	params += ",\"referenceSectionIndex\":" + refSectionIndex;
	params += ",\"entity\":" + JSON.stringify(entity);
	params += ",\"params\":{";
	var isFirst = true;
	for (key in pValue) {
		var ary = pValue[key];
		if (isFirst) {
			isFirst = false;
		} else {
			params += ",";
		}
		params += "\"" + key + "\":[";
		for (var i = 0; i < ary.length; i++) {
			if (i != 0) {
				params += ",";
			}
			params += "\"" + ary[i] + "\"";
		}
		params += "]";
	}
	params += "}";
	params += ",\"currentValue\":[";
	for (var i = 0; i < cValue.length; i++) {
		if (i != 0) {
			params += ",";
		}
		params += "\"" + cValue[i] + "\"";
	}
	params += "]";
	params += "}";

	postAsync(webapi, params, function(result) {
		if (func && $.isFunction(func)) func.call(this, result.value);
	});
}

function getUniqueItem(webapi, defName, viewName, viewType, propName, uniqueValue, entityOid, entityVersion, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"viewType\":\"" + viewType + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	if (uniqueValue == null) {
		params += ",\"uniqueValue\":null";
	} else {
		params += ",\"uniqueValue\":\"" + uniqueValue + "\"";
	}
	if (entityOid == null) {
		params += ",\"entityOid\":null";
	} else {
		params += ",\"entityOid\":\"" + entityOid + "\"";
	}
	if (entityVersion == null) {
		params += ",\"entityVersion\":null";
	} else {
		params += ",\"entityVersion\":\"" + entityVersion + "\"";
	}
	params += "}";

	postAsync(webapi, params, function(result) {
		var entity = result.data;
		if (func && $.isFunction(func)) func.call(this, entity);
	});
}

////////////////////////////////////////////////////////
//バージョン取得用のJavascript
////////////////////////////////////////////////////////

function getVersion(webapi, defName, viewName, oid, version, func) {
	$("ul.other-version-list li").remove();
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"oid\":\"" + oid + "\"";
	params += ",\"version\":\"" + version + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var entities = results.result;
		if (entities == null || entities.length == 0) {
			return;
		}

		if (func && $.isFunction(func)) func.call(this, entities);
	});
}

////////////////////////////////////////////////////////
//データロック用のJavascript
////////////////////////////////////////////////////////
function _lock(webapi, defName, viewName, oid, token, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"oid\":\"" + oid +  "\"";
	params += ",\"_t\":\"" + token +  "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		if (func && $.isFunction(func)) func.call(this, results.lockResult);
	});
}

////////////////////////////////////////////////////////
//参照コンボ用のJavascript
////////////////////////////////////////////////////////
function refComboChange(webapi, defName, viewName, propName, _params, viewType, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	for (var i = 0; i < _params.length; i++) {
		params += ",\"" + _params[i].key + "\":\"" + _params[i].value + "\"";
	}
	params += ",\"viewType\":\"" + viewType + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var selName = results.selName;
		var entities = results.data;
		if (func && $.isFunction(func)) func.call(this, selName, entities);
	});
}

/**
 @deprecated use getReferenceComboSetting
 */
function getPropertyEditor(webapi, defName, viewName, propName, viewType, entityOid, entityVersion, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"viewType\":\"" + viewType + "\"";
	if (typeof entityOid !== "undefined" && entityOid != null) {
		params += ",\"entityOid\":\"" + entityOid + "\"";
	}
	if (typeof entityVersion !== "undefined" && entityVersion != null) {
		params += ",\"entityVersion\":\"" + entityVersion + "\"";
	}
	params += "}";
	postAsync(webapi, params, function(results) {
		var editor = results.editor;
		if (func && $.isFunction(func)) func.call(this, editor);
	});
}

function getReferenceComboSetting(webapi, defName, viewName, propName, viewType, entityOid, entityVersion, func) {
	let params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"viewType\":\"" + viewType + "\"";
	if (typeof entityOid !== "undefined" && entityOid != null) {
		params += ",\"entityOid\":\"" + entityOid + "\"";
	}
	if (typeof entityVersion !== "undefined" && entityVersion != null) {
		params += ",\"entityVersion\":\"" + entityVersion + "\"";
	}
	params += "}";
	postAsync(webapi, params, function(results) {
		const setting = results.setting;
		if (func && $.isFunction(func)) func.call(this, setting);
	});
}

function searchParent(webapi, defName, viewName, propName, viewType, targetPath, childOid, entityOid, entityVersion, func) {
	let params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"viewType\":\"" + viewType + "\"";
	if (typeof entityOid !== "undefined" && entityOid !== null) {
		params += ",\"entityOid\":\"" + entityOid + "\"";
	}
	if (typeof entityVersion !== "undefined" && entityVersion !== null) {
		params += ",\"entityVersion\":\"" + entityVersion + "\"";
	}
	params += ",\"targetPath\":\"" + targetPath + "\"";
	params += ",\"childOid\":\"" + childOid + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		const parent = results.parent;
		if (func && $.isFunction(func)) func.call(this, parent);
	});
}

////////////////////////////////////////////////////////
//連動プロパティ用のJavascript
////////////////////////////////////////////////////////
function getLinkItems(webapi, defName, viewType, viewName, propName, linkValue, entityOid, entityVersion, callback) {

	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewType\":\"" + viewType + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"linkValue\":\"" + linkValue + "\"";
	if (typeof entityOid !== "undefined" && entityOid != null) {
		params += ",\"entityOid\":\"" + entityOid + "\"";
	}
	if (typeof entityVersion !== "undefined" && entityVersion != null) {
		params += ",\"entityVersion\":\"" + entityVersion + "\"";
	}
	params += "}";
	postAsync(webapi, params, function(results) {
		var entities = results.data;
		if (callback && $.isFunction(callback)) callback.call(this, entities);
	});
}

////////////////////////////////////////////////////////
//日付操作用のJavascript
////////////////////////////////////////////////////////
function setSystemDate(dateTime, func) {
	if (dateTime != null) dateTime = "\"" + dateTime + "\"";
	var params = "{";
	params += "\"dateTime\":" + dateTime + "";
	params += "}";
	postAsync("gem/preview/setPreviewDateTime", params, function(results) {
		var val = results.dateTime;
		var date = new Date();
		if (val != null) {
			date.setTime(val);
		}
		sysdate = dateUtil.format(date, "YYYYMMDDHHmmss");

		if (func && $.isFunction(func)) func.call(this);
	});
}

////////////////////////////////////////////////////////
//ロール変更用のJavascript
////////////////////////////////////////////////////////
function setRole(webapi, roleName, func) {
	var params = "{";
	params += "\"roleName\":\"" + roleName + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var checkResult = results.checkResult;

		if (func && $.isFunction(func)) func.call(this, checkResult);
	});
}

////////////////////////////////////////////////////////
//プロパティ値取得用のJavascript
////////////////////////////////////////////////////////

//汎用画面ReferenceEditor用Entity名前取得
function getEntityName(defName, viewName, oid, version, async, func) {
	var _viewName = "";
	if (typeof viewName !== "undefined") {
		if (viewName === null) {
			_viewName = ",\"viewName\":null";
		} else {
			_viewName = ",\"viewName\":\"" + viewName + "\"";
		}

	}
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += _viewName;
	params += ",\"oid\":\"" + oid + "\"";
	params += ",\"version\":\"" + version + "\"";
	params += "}";
	postAsync("gem/generic/common/getEntityName", params, function(results) {
		var value = results.value;
		if (func && $.isFunction(func)) func.call(this, value);
	}, async);
}

//汎用画面ReferenceEditor用Entity名前一括取得
function getEntityNameList(defName, viewName, parentDefName, parentViewName, parentPropName, viewType, refSectionIndex, list, entity, func) {
	var _viewName = "";
	if (typeof viewName !== "undefined") {
		if (viewName === null) {
			_viewName = ",\"viewName\":null";
		} else {
			_viewName = ",\"viewName\":\"" + viewName + "\"";
		}

	}
	var _parentDefName = "";
	if (typeof parentDefName !== "undefined") {
		if (parentDefName === null) {
			_parentDefName = ",\"parentDefName\":null";
		} else {
			_parentDefName = ",\"parentDefName\":\"" + parentDefName +"\"";
		}
	}
	var _parentViewName = "";
	if (typeof parentViewName !== "undefined") {
		if (parentViewName === null) {
			_parentViewName = ",\"parentViewName\":null";
		} else {
			_parentViewName = ",\"parentViewName\":\"" + parentViewName +"\"";
		}
	}
	var _parentPropName = "";
	if (typeof parentPropName !== "undefined") {
		if (parentPropName === null) {
			_parentPropName = ",\"parentPropName\":null";
		} else {
			_parentPropName = ",\"parentPropName\":\"" + parentPropName +"\"";
		}
	}
	var _viewType = "";
	if (typeof viewType !== "undefined") {
		if (viewType === null) {
			_viewType = ",\"viewType\":null";
		} else {
			_viewType = ",\"viewType\":\"" + viewType +"\"";
		}
	}
	var _refSectionIndex = "";
	if (typeof refSectionIndex !== "undefined") {
		if (refSectionIndex === null) {
			_refSectionIndex = ",\"referenceSectionIndex\":null";
		} else {
			_refSectionIndex = ",\"referenceSectionIndex\":\"" + refSectionIndex +"\"";
		}
	}
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += _viewName;
	params += _parentDefName;
	params += _parentViewName;
	params += _parentPropName;
	params += _viewType;
	params += _refSectionIndex;
	params += ",\"list\":" + JSON.stringify(list);
	params += ",\"entity\":" + JSON.stringify(entity);
	params += "}";

	postAsync("gem/generic/common/getEntityNameList", params, function(results) {
		var value = results.value;
		if (func && $.isFunction(func)) func.call(this, value);
	}, false);
}

//汎用画面以外用
function getEntityValue(webapi, defName, entityDefName, propName, oid, version, async, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"entityDefName\":\"" + entityDefName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"oid\":\"" + oid + "\"";
	params += ",\"version\":\"" + version + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var value = results.value;
		if (func && $.isFunction(func)) func.call(this, value);
	}, async);
}

function getEntityValueList(webapi, defName, entityDefName, propName, list, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"entityDefName\":\"" + entityDefName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"list\":" + JSON.stringify(list);
	params += "}";
	postAsync(webapi, params, function(results) {
		var value = results.value;
		if (func && $.isFunction(func)) func.call(this, value);
	}, false);
}

////////////////////////////////////////////////////////
//Entity取得用のJavascript
////////////////////////////////////////////////////////

function getNestTableData(defName, viewName, propName, oid, callback) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"viewName\":\"" + viewName + "\"";
	params += ",\"propName\":\"" + propName + "\"";
	params += ",\"oid\":\"" + oid + "\"";
	params += "}";
	postAsync("gem/generic/detail/getNestTableData", params, function(results) {
		var entity = results.data;
		if (callback && $.isFunction(callback)) callback.call(this, entity);
	});
}

/**
 * Entity名の取得。
 *
 * Referenceなどの複数のEntityデータを一括して取得します。
 * 取得するプロパティは、oid, name, versionのみです。
 *
 * OIDに該当するデータがない場合は、結果のEntity配列に含まれません。
 *
 * @param webapi		WebAPI名
 * @param defName		呼び出し元機能の定義名
 * @param entityDefName	Entity定義名
 * @param oidArray	OIDの配列 (カンマ区切りなど文字列の場合は、strOids.split(",")などで配列に変換して指定してください。
 * @param callback	結果を返します。Entityの配列が引数に渡されます。oid, name, versionのみ取得できます。
 *
 */
function getEntityNames(webapi, defName, entityDefName, oidArray, callback) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += ",\"entityDefName\":\"" + entityDefName + "\"";
	var first = true;
	params += ",\"oid\":[";
	$(oidArray).each(function() {
		if (first) {
			first = false;
		} else {
			params += ",";
		}
		params += "\"" + this + "\"";
	});
	params += "]";
	params += "}";
	postAsync(webapi, params, function(results) {
		var entities = results.data;
		if (callback && $.isFunction(callback)) callback.call(this, entities);
	});
}

////////////////////////////////////////////////////////
//TreeView用のJavascript
////////////////////////////////////////////////////////

function getTreeViewDefinition(webapi, defName, func) {
	var params = "{";
	params += "\"defName\":\"" + defName + "\"";
	params += "}";
	postAsync(webapi, params, function(results) {
		var definition = results.definition;

		if (func && $.isFunction(func)) func.call(this, definition);
	});
}
