<%--
 Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.

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

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
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
<%@ page import="org.iplass.gem.command.fulltext.FullTextSearchViewCommand"%>
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
<%
	for (Map.Entry<String, String> dispEntity : dispEntitiesMap.entrySet()) {
		String targetDefName = dispEntity.getKey();
%>
		<input type="hidden" id="sortKey_<%=targetDefName %>" name="sortKey_<%=targetDefName %>" />
		<input type="hidden" id="sortType_<%=targetDefName %>" name="sortType_<%=targetDefName %>" />
<%
	}
%>
		<input class="form-size inpbr" type="text" name="fulltextKey" id="fulltext" onkeypress="if(event.keyCode == 13){return false;}" />
		<input type="button"  class="btn-search-01 gr-btn" value="${m:rs('mtp-gem-messages', 'fulltext.search.search')}" onclick="resetSortCondition(); fulltextSearchResultView()"/>
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

<%@include file="../layout/resource/dropdownCheckListResource.inc.jsp"%>

<script type="text/javascript">
$(function(){
	var fulltextKey = "${m:escJs(fulltextKey)}";
	var searchDefName = "<%=StringUtil.escapeJavaScript(defNameParam)%>";
	var param = searchDefName + "&fulltextKey=" + fulltextKey;
	execSearch(param);
});

var gridList = [];
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
					grid = createTemplate("list" + i, entityResult.defName, entityResult.displayName, entityResult.crawlDate, entityResult.colModels);
					setData(grid, "list" + i, entityResult.values, entityResult.viewAction, entityResult.detailAction, entityResult.showDetailLink);
				}
				var message = commandResult.message;
				if (message != null) {
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

			if (res != null
					&& res.exceptionType == "org.iplass.mtp.auth.NoPermissionException") {
				alert(scriptContext.gem.locale.error.permissionErrOccurred);
			} else {
				alert(scriptContext.gem.locale.error.errOccurred);
			}

			$("div.fulltext-search-block").show();
			$("div.result-block").hide();
			executeDropdownCheckList();
		}
	});

}

function createTemplate(listId, defName, displayName, crawlDate, colModels) {
	$("#entityList").append("<div class='entity-result'><div class='result-title'>"
			+ "<h3 class='hgroup-02'>" + displayName + "</h3>"
			+ "<span class='crawl-date'>" + crawlDate + "</span>"
			+ "</div><table id='" + listId + "'></table></div>");

	//orgOid列にoidCellFormatterを設定
	if (colModels) {
		$(colModels).each(function(index) {
			if (this.name === "orgOid") {
				this.formatter = oidCellFormatter;
			}
			if (this.grouping === true) {
				this.cellattr = groupingCellAttr;
			} else {
				this.cellattr = noneGroupingCellAttr;
			}
		});
	}

	var $sortKey = $("#fulltextSearchForm").find(":hidden#" + $.escapeSelector("sortKey_" + defName));
	var $sortType = $("#fulltextSearchForm").find(":hidden#" + $.escapeSelector("sortType_" + defName));
	var grid = $("#" + listId).jqGrid({
		datatype: "local",
		height: "auto",
		autoencode: false,
		colModel: colModels,
		headertitles: true,		//HeaderにTooltip表示
		caption: "Fulltext Search Result",
		viewrecords: true,
		altRows: true,
		altclass:'myAltRowClass',
		onSortCol: function(index, iCol, sortorder) {
			var sortKey = index;
			var sortType = sortorder.toUpperCase();

			var curSortKey = $sortKey.val();
			var curSortType = $sortType.val();

			<%-- アイコンは表示されていない可能性があるので必ずやる --%>
			<%-- $("#gview_searchResult tr.ui-jqgrid-labels th .ui-jqgrid-sortable").removeClass('asc desc'); --%>
			<%-- $("#gview_searchResult tr.ui-jqgrid-labels th:eq(" + iCol + ") .ui-jqgrid-sortable").addClass(sortType.toLowerCase()); --%>

			<%-- ソート条件に変更がある場合のみ実施
				(結果表示用のsetData関数でsortGrid呼び出しによって発生するため) --%>
			if (sortKey !== curSortKey || sortType !== curSortType) {
				$sortKey.val(sortKey);
				$sortType.val(sortType);
				fulltextSearchResultView();
			}
			return "stop";
		},
		gridComplete: function() {
			var curSortKey = $sortKey.val();
			var curSortType = $sortType.val();

			if (curSortKey !== "" && curSortType !== "") {
				$("#" + listId).sortGrid(curSortKey, false, curSortType.toLowerCase());
			}
		},
		onSelectRow: function(rowid, e) {
			var row = grid.getRowData(rowid);
			var id = row.orgOid + "_" + row.orgVersion;
			var rowIndex = parseInt(rowid) - 1;

			clearRowHighlight(grid, rowIndex);

			if (e) {
				$("tr[id]", grid).each(function() {
					var _rowid = $(this).attr("id");
					if (_rowid == rowid) return;
					var _row = grid.getRowData(_rowid);
					var _id = _row.orgOid + "_" + _row.orgVersion;
					if (id == _id) $(this).addClass("ui-state-highlight");
				});
			}
		}

	});
	gridList.push({id:listId, grid: grid});
	return grid;
}

function setData(grid, listId, data, viewUrl, editUrl, showEditLink) {

	grid.clearGridData();
	grid.setGridParam({"_data": data}).trigger("reloadGrid");

	//リンク生成
	var $detailLink = $("<p/>");
	var $viewLink = $("<a/>").attr({"href":"javascript:void(0)", "action":viewUrl, "title":"${m:rs('mtp-gem-messages', 'fulltext.search.detail')}"})
			.addClass("detailLink").text("${m:rs('mtp-gem-messages', 'fulltext.search.detail')}").appendTo($detailLink);
	var $editLink = null;
	if (showEditLink) {
		$viewLink.addClass("jqborder"); //真ん中の棒線
		$editLink = $("<a/>").attr({"href":"javascript:void(0)", "action":editUrl, "title":"${m:rs('mtp-gem-messages', 'fulltext.search.edit')}"})
			.addClass("detailLink editLink").text("${m:rs('mtp-gem-messages', 'fulltext.search.edit')}").appendTo($detailLink);
	}

	$(data).each(function(index) {
		$viewLink.attr({"oid":this.orgOid, "version":this.orgVersion});
		if (showEditLink) {
			$editLink.attr({"oid":this.orgOid, "version":this.orgVersion});
		}
		this["id"] = this.orgOid + "_" + this.orgVersion;
		this["_mtpDetailLink"] = $detailLink.html();
		grid.addRowData(index + 1, this);
	});

	<%-- サーバのソート順で表示します。 --%>
	<%-- grid.setGridParam({sortname:"score", sortorder:"desc"}).trigger("reloadGrid"); --%>

	var isSubModal = $("body.modal-body").length != 0;
	if (isSubModal) {
		var a = $("#" + listId + " .modal-lnk");
		a.subModalWindow();
	} else {
		var a = $("#" + listId + " .modal-lnk");
		a.modalWindow();
	}
	$(".fixHeight").fixHeight();
	var option = {"<%=Constants.BACK_PATH%>":"<%=FullTextSearchViewCommand.SEARCH_VIEW_ACTION_NAME%>"};
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

function resetSortCondition() {
	var $form = $("#fulltextSearchForm");
	$(":hidden[name^='sortKey_']", $form).val("");
	$(":hidden[name^='sortType_']", $form).val("");
}
function getGrid(id) {
	var ret = null;
	$(gridList).each(function() {
		var gridData = this;
		if (gridData.id === id) {
			ret = gridData.grid;
			return false;
		}
	});
	return ret;
}
//「検索結果をまとめる」用:列名から親プロパティのパスを取得
//例:
// "mref2.mref3.name" → "mref2.mref3"
// "name" → ""
const getParentPath = (name) => {
	const idx = name.lastIndexOf(".");
	return idx < 0 ? "" : name.substring(0, idx);
};

//「検索結果をまとめる」用:path が parentPath と同じ、またはその祖先にあたる親パスか判定
//例
// parentPath = "mref2.mref3" の場合：
// "mref2.mref3" → true
// "mref2" → true
// "mref4" → false
const isSameOrAncestorPath = (path, parentPath) =>
	path !== "" && (path === parentPath || parentPath.startsWith(path + "."));

//「検索結果をまとめる」用:結合判定の対象とする列名の一覧を取得
//ネストした参照プロパティの列は、親(祖先)の参照プロパティの列も判定対象に含める。
//(親が異なる行同士を、子の値が同じというだけで結合しないようにするため)
//例(colNames = ["name", "mref2.name", "mref2.mref3.name", "mref4.name"]):
//  "name" → ["name"]
//  "mref2.mref3.name" → ["mref2.name", "mref2.mref3.name"]
const getGroupingKeyCols = (colName, colNames) => {
	const parentPath = getParentPath(colName);
	return parentPath === ""
		? [colName]
		: colNames.filter((name) => isSameOrAncestorPath(getParentPath(name), parentPath));
};

function groupingCellAttr(rowId, val, rowObject, colModel, rdata) {
	const rowIndex = parseInt(rowId) - 1;
	const grid = getGrid(this.id);
	const data = grid.getGridParam("_data");
	const row = data[rowIndex];
	const colNames = grid.getGridParam("colModel").map((model) => model.name);
	const colName = colModel.name;
	const keyCols = getGroupingKeyCols(colName, colNames);
	//「検索結果をまとめる」用:2つの行が結合対象(同一)か判定する
	const isSame = (row1, row2) => row1.orgOid == row2.orgOid
		&& row1.orgVersion == row2.orgVersion
		&& keyCols.every((col) => row1[col] == row2[col]);

	//前の行と値が同じ場合(ネストした参照プロパティは親の値も同じ場合)は非表示にする
	//タイトルを指定しないと(""含む)CELL値が設定されるので指定
	if (rowIndex > 0 && isSame(row, data[rowIndex - 1])) {
		return " title=\"&nbsp;\" style=\"display:none;\" ";
	}

	//この行から何行分rowspanを設定するか計算
	const rest = data.slice(rowIndex);
	const diffIndex = rest.findIndex((nextRow) => !isSame(row, nextRow));
	const count = diffIndex < 0 ? rest.length : diffIndex;

	if (count > 1) {
		//タイトルを指定しないと(""含む)CELL値が設定されるので、空の場合は &nbsp; を指定
		const label = $.jgrid.stripHtml(rdata[colName]).replace(/'/g, "&apos;") || "&nbsp;";
		return " title=\"" + label + "\" style=\"vertical-align: center !important;\" rowspan=\"" + count + "\"";
	} else {
		return null;
	}
}

function noneGroupingCellAttr(rowId, val, rowObject, colModel, rdata) {
	return null;
}

function clearRowHighlight(grid, rowIndex) {
	var $rows = $("tr.jqgrow", grid);
	if (rowIndex >= $rows.length) return;
	//選択された行以外にハイライトをクリアします。
	$rows.each(function(index) {
		if (index != rowIndex) $(this).removeClass("ui-state-highlight");
	});
}

</script>
</div>