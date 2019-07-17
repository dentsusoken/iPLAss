<%--
 Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

 Unless you have purchased a commercial license,
 the following license terms apply:

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
	//データ取得
	String calendarName = request.getParameter(Constants.CALENDAR_NAME);
	String modalTarget = request.getParameter(Constants.MODAL_TARGET);

	if (modalTarget == null) modalTarget = "";
	else modalTarget = StringUtil.escapeHtml(modalTarget);

	String language = TemplateUtil.getLanguage();
	if (StringUtil.isEmpty(language)) {
		language = "ja";
	}
%>
<script type="text/javascript" src="${m:esc(staticContentPath)}/scripts/gem/module/calendar/calendar.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<%
	if (!"en".equals(language)) { //enの場合はデフォルトを利用
%>
<script type="text/javascript" src="${m:esc(staticContentPath)}/scripts/gem/module/calendar/i18n/calendar-<%=language %>.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<%
	}
%>
<script type="text/javascript">
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
$(function() {
	// 描画処理
	$(".calendarFilter").calendarFilterView("<%=calendarName%>");

	// 検索ボタン押下処理
	var func = null;
	var windowManager = document.rootWindow.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		var win = windowManager[modalTarget];
		func = win.scriptContext["editReferenceCallback"];
	} else {
		func = parent.document.scriptContext["editReferenceCallback"];
	}
	if (func && $.isFunction(func)) {
		$("#calendar_search_button").click(function(){
			var $formList = $("form");
			var formList = {};
			var filterMessage = new Array();
			$formList.each(function(){
				var form = $(this);
				var defName = form.attr("defName");
				var displayName = form.attr("displayName");

				// 非表示（未選択タブ）は除外
				var boxSearch = form.parents(".box-search-01");
				var tabMenu = boxSearch.prev().find("ul");
				if (tabMenu.children().index(tabMenu.children(".current")) !== boxSearch.children().index(form.parent())) {
					return true;
				}

				// 除く設定されているエンティティ設定
				if ($(".without-entity[defname='" + form.attr("defName") + "']").prop("checked")) {
					formList[defName] = {withoutEntity:true};
					filterMessage.push(messageFormat(scriptContext.gem.locale.calendar.withoutEntity, displayName));
					return true;
				}

				// 定型フィルター設定時
				if (form.attr("name")) {
					var fixFilter = $("input[type='radio']:checked", form);
					if (fixFilter.length > 0) {
						formList[defName] = {fixFilter:fixFilter.val()};
						filterMessage.push(displayName);
						filterMessage.push(scriptContext.gem.locale.calendar.fixedFilter + ":" + fixFilter.attr("displayName"));
					}
				} else {
					// 通常フィルター設定時
					var filterLineList = $(".filter-line", form);
					var valueList = new Array();
					var count = 0;
					var filterConditionMessage = "";
					filterLineList.each(function() {
						var filterLine = $(this);
						var filterProperty = $(".filter-property", filterLine);
						var filterCondition = $(".filter-condition", filterLine);
						var filterInputField = $(".filter-input-field", filterLine);
						var selectProperty = $("option:selected", filterProperty);
						var selectCondition = $("option:selected", filterCondition);
						var type = selectProperty.attr("type");

						// プロパティを選択していない行は消す
						if (filterProperty.val() == "") {
							$(".delete", filterLine).click();
						} else {

							if (filterCondition.val() == "NL"|| filterCondition.val() == "NNL") {
								var valueobj = {};
								valueobj["property"] = {value:selectProperty.val(),displayName:selectProperty.text(), type:type};
								valueobj["condition"] = {value:selectCondition.val(),displayName:selectCondition.text()};
								valueList[count] = valueobj;
								count++;
								filterConditionMessage = filterConditionMessage + createFilterConditionMessage(valueobj)
								return true;
							}

							var spanList = $("span", filterInputField);
							var keywordList = {};
							spanList.each(function(){
								var $span = $(this);

								// 非表示は除外
								if($span.css("display") == "none") {
									return true;
								}

								if (type == "SELECT" || type == "BOOLEAN") {
									var checklist = $("input:checked", $span);
									if (checklist.length > 0) {
										var list = "";
										var displayList = "";
										checklist.each(function(){
											if($(this).val()) {
												if (list) {
													list = list + "," + $(this).val();
													displayList = displayList + "," + $(this).attr("displayName")
												} else {
													list = $(this).val();
													displayList = $(this).attr("displayName")
												}
											}
										});
										keywordList[$span.attr("class")] = list;
										keywordList[$span.attr("class") + "-display"] = displayList;
									}

								} else if (type == "DATE" || type == "DATETIME" || type == "TIME") {
									var $input = $("input", $span);
									if ($input.val()) {
										keywordList[$span.attr("class")] = $input.attr("data-prevalue");
										keywordList[$span.attr("class") + "-display"] = $input.val();
									}
								} else {
									if ($("input", $span).val()) {
										keywordList[$span.attr("class")] = $("input", $span).val();
									}
								}
							});
							var notEmptyFlg = false;
							for (var j in keywordList) {
								notEmptyFlg = true;
								break;
							}

							if (notEmptyFlg) {
								var valueobj = {};
								valueobj["keyword"] = keywordList;
								valueobj["property"] = {value:selectProperty.val(),displayName:selectProperty.text(), type:type};
								valueobj["condition"] = {value:selectCondition.val(),displayName:selectCondition.text()};
								valueList[count] = valueobj;
								count++;
								filterConditionMessage = filterConditionMessage + createFilterConditionMessage(valueobj);
							} else {
								// 値を設定していない行は消す
								$(".delete", filterLine).click();
							}
						}
					});
					if (valueList.length > 0) {
						formList[defName] = {valueList : valueList};
						filterMessage.push(displayName);
						filterMessage.push(filterConditionMessage);
					}
				}
			});
			func.call(this, formList, filterMessage);
		});
	}

});

function createFilterConditionMessage(valueobj) {

	var propertyName = valueobj["property"].displayName;
	var type = valueobj["property"].type;
	var conditionName = valueobj["condition"].displayName;
	var conditionValue = valueobj["condition"].value;
	var keyword = valueobj["keyword"];
	var separates = ":";

	var message = "[";
	message = message + scriptContext.gem.locale.calendar.searchItem + separates + propertyName + " ";
	message = message + scriptContext.gem.locale.calendar.cond + separates + conditionName;
	if (keyword) {
		var from;
		var to;
		if (type == "DATE" || type == "DATETIME" || type == "TIME" || type == "SELECT" ||type == "BOOLEAN") {
			from = keyword["data-range-from-display"];
			to = keyword["data-range-to-display"];
		} else {
			from = keyword["data-range-from"];
			to = keyword["data-range-to"];
		}

		if (conditionValue != "RG") {
			message = message + " " + scriptContext.gem.locale.calendar.keyword + separates + from + "] ";
		} else {
			message = message + " " + scriptContext.gem.locale.calendar.keyword + separates + from + " " + scriptContext.locale.fromTo + " " + to + "] ";
		}

	} else {
		message = message + "] ";
	}
	return message;
}

</script>
<div class="calendarFilter"></div>