<%--
 Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.FormViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.view.top.parts.FulltextSearchViewParts"%>
<%@ page import="org.iplass.mtp.view.top.parts.TopViewParts"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.gem.command.fulltext.FullTextSearchCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);

	String defNameParam = "";
	@SuppressWarnings("unchecked") List<String> defNameList = (List<String>) request.getAttribute("searchDefName");
	if (!defNameList.isEmpty()) {
		StringBuilder sbDefNames = new StringBuilder();
		for (String defName : defNameList) {
			sbDefNames.append("&searchDefName=" + defName);
		}
		sbDefNames.deleteCharAt(0);
		defNameParam = sbDefNames.toString();
	}

	String roleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);
	if (roleName == null) roleName = "DEFAULT";
	TopViewDefinitionManager manager = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	TopViewDefinition td = manager.get(roleName);
	FulltextSearchViewParts fulltextSearchViewParts = null;
	if (td != null) {
		for (TopViewParts parts : td.getParts()) {
			if (parts instanceof FulltextSearchViewParts) {
				fulltextSearchViewParts = (FulltextSearchViewParts) parts;
				break;
			}
		}
	}

	boolean isDispSearchWindow = true;
	Map<String, Boolean> partsDispEntitiesMap = new HashMap<String, Boolean>();
	Map<String, String> dispEntitiesMap = new HashMap<String, String>();

	if (fulltextSearchViewParts == null) {

		for (String str : edm.definitionList()) {
			EntityDefinition def = edm.get(str);
			if (def != null && def.isCrawl()) {
				String displayName = TemplateUtil.getMultilingualString(def.getDisplayName(), def.getLocalizedDisplayNameList());
				dispEntitiesMap.put(def.getName(), displayName);
			}
		}

	} else {

		partsDispEntitiesMap = fulltextSearchViewParts.getDispEntities();
		isDispSearchWindow = fulltextSearchViewParts.isDispSearchWindow();

		for(Map.Entry<String, String> e : fulltextSearchViewParts.getViewNames().entrySet()) {
			EntityDefinition fulltextDefinition = edm.get(e.getKey());
			EntityView view = evm.get(e.getKey());
			SearchFormView form = null;

			if (fulltextDefinition.isCrawl() && partsDispEntitiesMap.get(fulltextDefinition.getName())) {
				// viewのtitleを取得する
				String viewName = e.getValue();

				if (StringUtil.isEmpty(viewName)) {
					// デフォルトレイアウトを利用
					if (view != null && view.getSearchFormViewNames().length > 0) {
						//1件でもView定義があればその中からデフォルトレイアウトを探す
						form = view.getDefaultSearchFormView();
					} else {
						// 何もなければ自動生成
						form = FormViewUtil.createDefaultSearchFormView(fulltextDefinition);
					}
				} else {
					// 指定レイアウトを利用
					if (view.getSearchFormView(viewName) != null) {
						form = view.getSearchFormView(viewName);
					} else {
						// なければ自動生成
						form = FormViewUtil.createDefaultSearchFormView(fulltextDefinition);
					}
				}

				String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(), fulltextDefinition.getDisplayName(), fulltextDefinition.getLocalizedDisplayNameList());
				dispEntitiesMap.put(fulltextDefinition.getName(), displayName);
			}
		}
	}
%>

<h2 class="hgroup-01">
	<span>
		<i class="far fa-circle default-icon"></i>
	</span>
	${m:rs("mtp-gem-messages", "fulltext.search.srchrslt")}
</h2>
<div class="fulltext-search-inner">
<div class="fulltext-search-block flat-block-top">
	<form method="POST" action="" id="fulltextSearchForm">
		<select class="form-size inpbr" id="entity" name="searchDefName" multiple>
<%
	String allSelected = "";
	if (defNameList.size() == 0) {
		allSelected = "selected";
	}
%>
			<option value="" <%=allSelected %>>${m:rs("mtp-gem-messages", "fulltext.search.all")}</option>
<%
	for (Map.Entry<String, String> dispEntity : dispEntitiesMap.entrySet()) {
		String selected = "";
		String targetDefName = dispEntity.getKey();
		if (defNameList.contains(targetDefName)) {
			selected = "selected";
		}
%>
			<option value="<c:out value="<%=targetDefName %>"/>" <%=selected %>><c:out value="<%=dispEntity.getValue() %>" /></option>
<%
	}
%>
		</select>
		<input class="form-size inpbr" type="text" name="fulltextKey" id="fulltext" onkeypress="if(event.keyCode == 13){return false;}" />
		<input type="button"  class="btn-search-01 gr-btn" value="${m:rs('mtp-gem-messages', 'fulltext.search.search')}" onclick="fulltextSearchResultView()"/>
		<img src="${m:esc(skinImagePath)}/icon-help-01.png" class="vm tp" alt="" title="${m:rs('mtp-gem-messages', 'fulltext.search.hint')}" />
	</form>
</div>
<div class="message-block" style="display:none;"></div>
<div class="result-block" style="display:none;">
	<span class="searching" style="display:none;">${m:rs("mtp-gem-messages", "fulltext.search.searching")}</span>
	<div class="result-data" style="display:none;">
		<div id="entityList"></div>
		<input type="hidden" id="searchCond" name="searchCond">
	</div>
</div>

<%@include file="../layout/resource/dropdownchecklistResource.jsp"%>

<script type="text/javascript">
$(function(){
	var fulltextKey = "${m:escJs(fulltextKey)}";
	var searchDefName = "<%=StringUtil.escapeJavaScript(defNameParam)%>";
	var param = searchDefName + "&fulltextKey=" + fulltextKey;
	execSearch(param);
});

function execSearch(param) {
	$("div.result-block").show();
	$(".searching").show();

	$.ajax({
		type: "POST",
		url: "${m:tcPath()}/api/<%=FullTextSearchCommand.SEARCH_WEB_API_NAME%>",
		dataType: "json",
		data: param,
		success: function(commandResult){

			$("div.result-data").show();
			$(".searching").hide();

			if (commandResult.status == "SUCCESS") {
				var resultData = commandResult.data;

				var grid = null;
				for (var i=0; i<resultData.length; i++) {
					var entityResult = resultData[i];
					grid = createTemplate("list" + i, entityResult.displayName, entityResult.crawlDate, entityResult.colModels);
					setData(grid, "list" + i, entityResult.values, entityResult.viewAction, entityResult.detailAction, entityResult.showDetailLink);
				}
				var message = commandResult.message;
				if (typeof message !== "undeined" && message != null) {
					$("div.message-block").append(message);
					$("div.message-block").show();
				}
			} else {
				var message = commandResult.message;
				$("div.message-block").append(message);
				$("div.message-block").show();
			}

			$("#fulltext").val(commandResult.fulltextKey);
			$("#entity").val(commandResult.searchDefName);
			$("#searchCond").val(commandResult.searchCond);
			$("div.fulltext-search-block").show();
			$('.fixHeight').fixHeight();
			executeDropdownCheckList();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			var res = null;
			try {
				res = (new Function("return " + XMLHttpRequest.responseText))();
			} catch (e) {
			}

			if (typeof res !== "undeined" && res != null
					&& res.exceptionType == "org.iplass.mtp.auth.NoPermissionException") {
				alert(scriptContext.locale.permissionErrOccurred);
			} else {
				alert(scriptContext.locale.errOccurred);
			}

			$("div.fulltext-search-block").show();
			$("div.result-block").hide();
			executeDropdownCheckList();
		}
	});

}

function createTemplate(listId, displayName, crawlDate, colModels) {
	$("#entityList").append("<div class='entity-result'><div class='result-title'>"
			+ "<h3 class='hgroup-02'>" + displayName + "</h3>"
			+ "<span class='crawl-date'>" + crawlDate + "</span>"
			+ "</div><table id='" + listId + "'></table></div>");
	var grid = $("#" + listId).jqGrid({
		datatype: "local",
		height: "auto",
		colModel: colModels,
		headertitles: true,		//HeaderにTooltip表示
		caption: "Fulltext Search Result",
		viewrecords: true,
		altRows: true,
		altclass:'myAltRowClass'
	});
	return grid;
}

function setData(grid, listId, data, viewUrl, detailUrl, detailLink) {

	grid.clearGridData();
	$(data).each(function(index) {
		this["id"] = this.orgOid + "_" + this.orgVersion;
		if (detailLink) {
			this["_mtpDetailLink"] =
				"<a href='javascript:void(0)' action='" + viewUrl + "' oid='" + this.orgOid + "' version='" + this.orgVersion + "' class='jqborder detailLink'>${m:rs('mtp-gem-messages', 'fulltext.search.detail')}</a>" +
				"<a href='javascript:void(0)'  action='" + detailUrl + "' oid='" + this.orgOid + "' version='" + this.orgVersion + "' class='detailLink editLink'>${m:rs('mtp-gem-messages', 'fulltext.search.edit')}</a>";
		} else {
			this["_mtpDetailLink"] =
				"<a href='javascript:void(0)' action='" + viewUrl + "' oid='" + this.orgOid + "' version='" + this.orgVersion + "' class='detailLink'>${m:rs('mtp-gem-messages', 'fulltext.search.detail')}</a>";
		}
		grid.addRowData(index + 1, this);
	});

	grid.setGridParam({sortname:"score", sortorder:"desc"}).trigger("reloadGrid");

	var isSubModal = $("body.modal-body").length != 0;
	if (isSubModal) {
		var a = $("#" + listId + " .modal-lnk");
		a.subModalWindow();
	} else {
		var a = $("#" + listId + " .modal-lnk");
		a.modalWindow();
	}
	$(".fixHeight").fixHeight();
	var option = {"<%=Constants.BACK_PATH%>":"<%=FullTextSearchCommand.SEARCH_VIEW_ACTION_NAME%>"};
	$(".detailLink", $("#" + listId)).click(function(e) {
		var action = $(this).attr("action");
		var oid = $(this).attr("oid");
		var version = $(this).attr("version");
		var isEdit = $(this).is(".editLink");
		if (e.ctrlKey) {
			showDetail(action, oid, version, isEdit, "_blank", option);
		} else {
			showDetail(action, oid, version, isEdit, null, option);
		}
		return false;
	});
}

function executeDropdownCheckList () {
	if (!$("span.ui-dropdownchecklist")[0]) {
		$("#entity").dropdownchecklist({
			forceMultiple: true,
			firstItemChecksAll: true,
			width: 125,
			maxDropHeight: 300,
			onComplete: function(selector) {
				var count = 0;
				$.each(selector.options, function() {
					if ($(this).prop("selected")) {
						count++;
					}
				});
				if (count < 1) {
					$("#entity").val("");
					$("#entity").dropdownchecklist("refresh");
				}
				trimTitle();
			}
		});
		trimTitle();
	}
}

function trimTitle() {
	var titleText = $("span.ui-dropdownchecklist-text").text();
	var trimTitleText = jQuery.trim(titleText);
	$("span.ui-dropdownchecklist-text").text(trimTitleText);
	$("span.ui-dropdownchecklist-text").attr("title", trimTitleText);
}

function fulltextSearchResultView() {
	var keyword = $("#fulltext").val();
	if (keyword != null && keyword != "") {
		var param = $("#fulltextSearchForm").serialize();
		$("#entityList").html("");
		$("div.message-block").html("");
		$("div.message-block").hide();
		$("div.fulltext-search-block").hide();
		execSearch(param);
	}
}
</script>
</div>