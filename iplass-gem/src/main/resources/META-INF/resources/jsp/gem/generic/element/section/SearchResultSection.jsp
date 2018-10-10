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

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.BinaryProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.PropertyColumn"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchResultSection"%>
<%@ page import="org.iplass.mtp.view.generic.element.Element"%>
<%@ page import="org.iplass.mtp.view.generic.element.VirtualPropertyItem"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
﻿<%@ page import="org.iplass.mtp.view.generic.PagingPosition"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil.TokenOutputType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.delete.DeleteAllCommand"%>
<%@ page import="org.iplass.gem.command.generic.delete.DeleteListCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.CountCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchSelectListCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	boolean isDispProperty(PropertyDefinition pd, PropertyColumn property) {
		if (!property.isDispFlag()) return false;
		if (property.getEditor() == null) return false;
		return true;
	}
	String getDefaultValue(HashMap<String, Object> defaultSearchCond, String searchCond, String key) {
		if (StringUtil.isNotBlank(searchCond)) return "";
		if (!defaultSearchCond.containsKey(key)) return "";
		Object value = defaultSearchCond.get(key);
		if (value instanceof Object[] && ((Object[]) value).length > 0) {
			value = ((Object[]) value)[0];
		}
		return value.toString();
	}
%>
<%
	//呼び出し元のJSPからデータ取得
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	String searchCond = request.getParameter(Constants.SEARCH_COND);
	if (searchCond == null) searchCond = "";

	String viewName = request.getParameter(Constants.VIEW_NAME);
	HashMap<String, Object> defaultSearchCond = (HashMap<String, Object>) request.getAttribute(Constants.DEFAULT_SEARCH_COND);
	String executeSearch = getDefaultValue(defaultSearchCond, searchCond, Constants.EXECUTE_SEARCH);

	String multiplicity = request.getParameter(Constants.SELECT_MULTI);
	String _permitConditionSelectAll = request.getParameter(Constants.PERMIT_CONDITION_SELECT_ALL);
	//全選択の範囲、trueの場合は他ページも含む検索条件に一致する全データ
	boolean permitConditionSelectAll = StringUtil.isNotBlank(_permitConditionSelectAll) && "true".equals(_permitConditionSelectAll);

	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);
	SearchFormView view = data.getView();
	SearchResultSection section = view.getResultSection();
	EntityDefinition ed = data.getEntityDefinition();

	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

	AuthContext auth = AuthContext.getCurrentContext();
	boolean canUpdate = auth.checkPermission(new EntityPermission(data.getEntityDefinition().getName(), EntityPermission.Action.UPDATE));
	boolean canDelete = auth.checkPermission(new EntityPermission(data.getEntityDefinition().getName(), EntityPermission.Action.DELETE));

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(ed.getName(), viewName);

	String pagingPosition = PagingPosition.BOTH.name();
	if (section.getPagingPosition() != null) {
		pagingPosition = section.getPagingPosition().name();
	}

	String contextPath = TemplateUtil.getTenantContextPath();

	//詳細表示アクション
	String viewAction = "";
	if (StringUtil.isNotBlank(view.getViewActionName())) {
		viewAction = view.getViewActionName() +  urlPath;
	} else {
		viewAction = DetailViewCommand.VIEW_ACTION_NAME + urlPath;
	}

	//詳細編集アクション
	String detailAction = "";
	if (StringUtil.isNotBlank(view.getEditActionName())) {
		detailAction = view.getEditActionName() +  urlPath;
	} else {
		detailAction = DetailViewCommand.DETAIL_ACTION_NAME + urlPath;
	}

	String deleteListWebapi = "";
	if (StringUtil.isNotBlank(view.getDeleteListWebapiName())) {
		deleteListWebapi = view.getDeleteListWebapiName();
	} else {
		deleteListWebapi = DeleteListCommand.WEBAPI_NAME;
	}

	String deleteAllWebapi = "";
	if (StringUtil.isNotBlank(view.getDeleteAllWebapiName())) {
		deleteAllWebapi = view.getDeleteAllWebapiName();
	} else {
		deleteAllWebapi = DeleteAllCommand.WEBAPI_NAME;
	}

	Boolean showdDetermineButton = (Boolean) request.getAttribute(Constants.SHOW_DETERMINE_BUTTON);
	if (showdDetermineButton == null) showdDetermineButton = false;
%>
<div class="result-block" style="display:none;">
<h3 class="hgroup-02">
${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.srchrslt")}
<span class="chagne-condition" style="display:none;">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.conditionChanged")}</span>
<span class="searching" style="display:none;">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.searching")}</span>
</h3>
<div class="result-data" style="display:none;">
<%
	if (showdDetermineButton) {
		//選択画面の確定ボタンを結果の上にも出す
%>
<p class="btn"><input type="button" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.determine')}" class="gr-btn gr-size-01 mb10" onclick="closeModal()" /></p>
<%
	}
%>
<input type="hidden" name="offset" value="0" >
<input type="hidden" name="limit" value="<c:out value="<%=section.getDispRowCount()%>"/>" >
<input type="hidden" name="noLimit" value="<%=section.isHidePaging()%>">
<script type="text/javascript">
var $pager = null;
var grid = null;
var isloaded = false;
var keepSelectAllStatus = false;
$(function() {
	$(".box-search-01 form").on("change", function() {
		var formName = $(":hidden[name='formName']").val();
		if ($(this).attr("name") == formName) {
			$(".chagne-condition").show();
		};
	});

	var multiSelect = <%=(OutputType.SEARCHRESULT == type && !section.isHideDelete() && canDelete) || OutputType.MULTISELECT == type%>;
	var colModel = new Array();
	colModel.push({name:"orgOid", index:"orgOid", sortable:false, hidden:true, frozen:true, label:"oid"});
	colModel.push({name:"orgVersion", index:"orgVersion", sortable:false, hidden:true, frozen:true, label:"version"});
<%
	if (OutputType.SINGLESELECT == type) {
		//スタイル調整のため、classes、labelClassesに"sel_radio"を指定
%>
	colModel.push({name:'selOid', index:'selOid', width:20, sortable:false, frozen:true, label:"", resizable:false, classes:"sel_radio", labelClasses:"sel_radio"});
<%
	} else if (OutputType.MULTISELECT == type) {
	} else if (OutputType.SEARCHRESULT == type) {
%>
	colModel.push({name:'_mtpDetailLink', index:'_mtpDetailLink', width:${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.detailLinkWidth")}, sortable:false, align:'center', frozen:true, label:"", classes:"detail-links"});
<%
	}

	for (Element element : section.getElements()) {
		if (element instanceof PropertyColumn) {
			PropertyColumn property = (PropertyColumn) element;
			String propName = property.getPropertyName();
			PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
			String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());

			if (isDispProperty(pd, property)) {
				if (!(pd instanceof ReferenceProperty)) {
					String sortPropName = StringUtil.escapeHtml(propName);
					String width = "";
					if (property.getWidth() > 0) {
						width = ", width:" + property.getWidth();
					}
					String align = "";
					if (property.getTextAlign() != null) {
						align = ", align:'" + property.getTextAlign().name().toLowerCase() + "'";
					}
					String style = property.getStyle() != null ? property.getStyle() : "";
					String sortable = "sortable:true";
					if (!ViewUtil.getEntityViewHelper().isSortable(pd)) {
						sortable = "sortable:false";
					}
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
	colModel.push({name:"<%=sortPropName%>", index:"<%=sortPropName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", <%=sortable%><%=width%><%=align%>});
<%
				} else if (property.getEditor() instanceof ReferencePropertyEditor) {
					//参照型のName以外を表示する場合
					List<NestProperty> nest = ((ReferencePropertyEditor) property.getEditor()).getNestProperties();
					if (nest.size() == 0) {
						String sortPropName = StringUtil.escapeHtml(propName);
						String width = "";
						if (property.getWidth() > 0) {
							width = ", width:" + property.getWidth();
						}
						String align = "";
						if (property.getTextAlign() != null) {
							align = ", align:'" + property.getTextAlign().name().toLowerCase() + "'";
						}
						String style = property.getStyle() != null ? property.getStyle() : "";
						String sortable = "sortable:true";
						if (!ViewUtil.getEntityViewHelper().isSortable(pd)) {
							sortable = "sortable:false";
						}
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
	colModel.push({name:"<%=sortPropName%>", index:"<%=sortPropName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", <%=sortable%><%=width%><%=align%>});
<%
					} else if (nest.size() > 0) {
						String style = property.getStyle() != null ? property.getStyle() : "";
						request.setAttribute(Constants.EDITOR_REF_NEST_PROP_NAME, propName);
						request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY, pd);
						request.setAttribute(Constants.EDITOR_REF_NEST_STYLE, style);
						request.setAttribute(Constants.EDITOR_REF_NEST_EDITOR, property.getEditor());
%>
<jsp:include page="SearchResultSection_Nest.jsp" />
<%
						request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
						request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
						request.removeAttribute(Constants.EDITOR_REF_NEST_STYLE);
						request.removeAttribute(Constants.EDITOR_REF_NEST_EDITOR);
					}
				}
			}
		} else if (element instanceof VirtualPropertyItem) {
			VirtualPropertyItem property = (VirtualPropertyItem) element;
			String propName = StringUtil.escapeHtml(property.getPropertyName());
			String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList());
			String width = "";
			if (property.getWidth() > 0) {
				width = ", width:" + property.getWidth();
			}
			String align = "";
			if (property.getTextAlign() != null) {
				align = ", align:'" + property.getTextAlign().name().toLowerCase() + "'";
			}
			String style = property.getStyle() != null ? property.getStyle() : "";
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
colModel.push({name:"<%=propName%>", index:"<%=propName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", sortable:false <%=width%><%=align%>});
<%
		}
	}
%>
	grid = $("#searchResult").jqGrid({
		datatype: "local",
		height: "auto",
		colModel: colModel,
		headertitles: true,
		multiselect: multiSelect,
		caption: "Manipulating Array Data",
		viewrecords: true,
		altRows: true,
		altclass:'myAltRowClass',
		onSortCol: function(index, iCol, sortorder) {
			var sortKey = index;
			var sortType = sortorder.toUpperCase();

			var curSortKey = $(":hidden[name='sortKey']").val();
			var curSortType = $(":hidden[name='sortType']").val();

			<%-- アイコンは表示されていない可能性があるので必ずやる --%>
			$("#gview_searchResult tr.ui-jqgrid-labels th .ui-jqgrid-sortable").removeClass('asc desc');
			$("#gview_searchResult tr.ui-jqgrid-labels th:eq(" + iCol + ") .ui-jqgrid-sortable").addClass(sortType.toLowerCase());

			<%-- ソート条件に変更がある場合のみ実施
				(結果表示用のsetData関数でsortGrid呼び出しによって発生するため) --%>
			if (sortKey !== curSortKey || sortType !== curSortType) {
				sort(sortKey, sortType);
			}
			return "stop";
		}
<%
	if (OutputType.SINGLESELECT == type) {
%>
		,onSelectRow: function(rowid, e) {
			var row = grid.getRowData(rowid);
			var value = row.orgOid + "_" + row.orgVersion;
			$(":radio[name='selOid'][value='" + value + "']").prop("checked", true);
			selectArray.shift();
			selectArray.push(value);
		}
<%
	} else if (OutputType.MULTISELECT == type) {
%>
		,onSelectRow: function(rowid, e) {
			if (!loading) {
				var row = grid.getRowData(rowid);
				var id = row.orgOid + "_" + row.orgVersion;
				if (e) {
					if (multiplicity == -1 || selectArray.length < multiplicity) {
						selectArray.push(id);
					} else {
						alert("${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.notSelect')}");
						grid.setSelection(rowid);
					}
				} else {
					for (var i = 0; i < selectArray.length; i++) {
						if (selectArray[i] == id) {
							selectArray.splice(i, 1);
							break;
						}
					}
				}
				keepSelectAllStatus = false;
			}
		}
<%
	}
%>
	});

<%
	if (!section.isHidePaging()) {
		boolean showItemCount = !section.isHideCount();
		boolean showPageLink = showItemCount ? !section.isHidePageLink() : false;
		boolean showPageJump = showItemCount ? !section.isHidePageJump() : false;
%>
	var limit = <%=section.getDispRowCount()%>;

	$pager = $(".result-block .result-nav").pager({
		limit: limit,
		showPageLink: <%=showPageLink%>,
		showPageJump: <%=showPageJump%>,
		showItemCount: <%=showItemCount%>,
		previewFunc: function(){
			if (keepSelectAllStatus) {
				$(".result-block").on("iplassAfterSearch", loadingOff);
			} else {
				$("#cb_searchResult").prop("checked", false);
			}
			var val = ($(":hidden[name='offset']").val() - 0) - limit;
			doSearch($(":hidden[name='searchType']").val(), val, false, $(".preview"), "pager");
		},
		nextFunc: function() {
			if (keepSelectAllStatus) {
				$(".result-block").on("iplassAfterSearch", loadingOff);
			} else {
				$("#cb_searchResult").prop("checked", false);
			}
			var val = ($(":hidden[name='offset']").val() - 0) + limit;
			doSearch($(":hidden[name='searchType']").val(), val, false, $(".next"), "pager");
		},
		searchFunc: function(currentPage) {
			if (keepSelectAllStatus) {
				$(".result-block").on("iplassAfterSearch", loadingOff);
			} else {
				$("#cb_searchResult").prop("checked", false);
			}
			var val = currentPage * limit;
			doSearch($(":hidden[name='searchType']").val(), val, false, $(".ui-icon-search"), "pager");
		}
	});
<%
	}
%>

	var searchCond = $(":hidden[name='searchCond']").val();
	var executeSearch = $(":hidden[name='executeSearch']").val();
	var params = parseSearchCond(searchCond);
	if (searchCond.length > 0 && params.length > 0) {
		for (var i = 0; i < params.length; i++) {
			var param = params[i];
			if (param.key == "searchType") setSearchTab(param.val);
			$("[name='" + es(param.key) +"']:not([data-norewrite])").each(function() {
				if ($(this).is("input[type='radio']")) {
					$(this).val([param.val]);
				} else if ($(this).is("input[type='checkbox']")) {
				} else {
					$(this).val(param.val);
				}
			});
		}
		doSearch($(":hidden[name='searchType']").val(), $(":hidden[name='offset']").val(), false, "init");
	} else if (executeSearch == "t") {
		doSearch($(":hidden[name='searchType']").val(), $(":hidden[name='offset']").val(), false, "init");
	}
});
function setData(list, count) {
	$("div.result-data").show();
	grid.clearGridData(true);
	$(list).each(function(index) {
		this["searchResultDataId"] = this.orgOid + "_" + this.orgVersion;
<%	if (type == OutputType.SINGLESELECT) { %>
		this["selOid"] = "<span class='singleRowSelect'><input type='radio' value='" + this.searchResultDataId + "' name='selOid'></span>";
<%
	}
	if (type == OutputType.SEARCHRESULT) {
		if (!section.isHideDetailLink() && (canUpdate || canDelete)) {
%>
		this["_mtpDetailLink"] = "<a href='javascript:void(0)' action='<%=StringUtil.escapeJavaScript(viewAction)%>' oid='" + this.orgOid + "' version='" + this.orgVersion + "' class='jqborder detailLink'>${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.detail')}</a><a href='javascript:void(0)' action='<%=StringUtil.escapeJavaScript(detailAction)%>' oid='" + this.orgOid + "' version='" + this.orgVersion + "' class='detailLink editLink'>${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.edit')}</a>";
<% 		} else { %>
		this["_mtpDetailLink"] = "<a href='javascript:void(0)' action='<%=StringUtil.escapeJavaScript(viewAction)%>' oid='" + this.orgOid + "' version='" + this.orgVersion + "' class='detailLink'>${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.detail')}</a>";
<%
		}
	}
%>
		grid.addRowData(index + 1, this);
	});

	if ($("audio, video").length > 0) {
		$('audio, video').mediaelementplayer({
			success: function(player, node) {
				player.addEventListener("loadeddata", function() {
					$('.fixHeight').fixHeight();
				});
			}
		});
	}

<%
	if (OutputType.SINGLESELECT == type) {
%>
	if (selectArray.length > 0) {
		$(":radio[name='selOid'][value='" + selectArray[0] + "']").prop("checked", true).trigger("change");
	}
<%
	} else if (OutputType.MULTISELECT == type) {
%>
	loading = true;
<%
		if (permitConditionSelectAll) {
		// 全ページor現在ページ選択
%>
	$("#cb_searchResult").off("iplassCheckboxPropChange").off("click").on("click", function() {
		if ($(this).is(":checked")) {
			$("#selectSelectAllTypeDialog").dialog("open");
		} else {
			$("#selectDeselectAllTypeDialog").dialog("open");
		}
	});
<%
		} else {
		//現在ページのみ
%>
	$("#cb_searchResult").off("iplassCheckboxPropChange").off("click").on("click", function() {
		if ($(this).is(":checked")) {
			selectCurrentPage();
			$(this).prop("checked", true);
		} else {
			deselectCurrentPage();
			$(this).prop("checked", false);
		}
	});
<%
		}
%>
	if (selectArray.length > 0) {
		applyGridSelection();
	}
	loading = false;
<%
	}
%>
	if ($pager) {
		var limit = $(":hidden[name='limit']").val() - 0;
		var offset = $(":hidden[name='offset']").val() - 0;
		$pager.setPage(offset, list.length, count);
	}

	$("#searchResult .detailLink").click(function(e) {
		var action = $(this).attr("action");
		var oid = $(this).attr("oid");
		var version = $(this).attr("version");
		var isEdit = $(this).is(".editLink");
		if (e.ctrlKey) {
			showDetail(action, oid, version, isEdit, "_blank", {});
		} else {
			showDetail(action, oid, version, isEdit, null, {});
		}
		return false;
	});
	var isSubModal = $("body.modal-body").length != 0;
	if (isSubModal) {
		var a = $("#searchResult .modal-lnk");
		a.subModalWindow();
	} else {
		var a = $("#searchResult .modal-lnk");
		a.modalWindow();
	}
<%	if (OutputType.SEARCHRESULT == type && !section.isHideDelete() && canDelete) { %>
	clearAllDelete();
<%	} %>

	var sortKey = $(":hidden[name='sortKey']").val();
	var sortType = $(":hidden[name='sortType']").val();
	if (sortKey.length > 0 && sortType.length > 0) {
		var $grid = $("#searchResult");
		var colModel = $grid.getGridParam("colModel");
		if (colModel.length > 0) {
			var i;
			for (i = 0; i < colModel.length; i++){
				if (colModel[i].name == sortKey) {
					<%-- 初回表示時のソートアイコン制御のためsortGrid呼び出し --%>
					$grid.sortGrid(colModel[i].index, false, sortType.toLowerCase());
					break;
				}
			}
		}
	}

	$(".fixHeight").fixHeight();
}
function applyGridSelection() {
	$("#searchResult tr[id]").each(function() {
		var rowid = $(this).attr("id");
		var row = grid.getRowData(rowid);
		var id = row.orgOid + "_" + row.orgVersion;
		for (var i = 0; i < selectArray.length; i++) {
			if (id == selectArray[i]) {
				grid.setSelection(rowid);
			}
		}
	});
}
function selectCurrentPage() {
	$("#searchResult tr[id]").each(function() {
		var rowid = $(this).attr("id");
		var row = grid.getRowData(rowid);
		var id = row.orgOid + "_" + row.orgVersion;
		if (selectArray.indexOf(id) == -1 && (multiplicity == -1 || selectArray.length < multiplicity)) {
			grid.setSelection(rowid);
		}
	});
}
function deselectCurrentPage() {
	$("#searchResult tr[id]").each(function() {
		var rowid = $(this).attr("id");
		var row = grid.getRowData(rowid);
		var id = row.orgOid + "_" + row.orgVersion;
		if (selectArray.indexOf(id) != -1) {
			grid.setSelection(rowid);
		}
	});
}
var loadingOff = null;
loadingOff = function(event, src) {
	if (src === "pager") {
		$("#cb_searchResult").prop("checked", true);
		$(".result-block").off("iplassAfterSearch", loadingOff);
	}
}
</script>
<form action="deleteForm" method="POST">
${m:outputToken('FORM_XHTML', false)}
<%
	if (!PagingPosition.BOTTOM.name().equals(pagingPosition)) {
%>
<div class="result-nav"></div><!--result-nav-->
<%
	}
%>
<table id="searchResult"></table>
<%
	if (!PagingPosition.TOP.name().equals(pagingPosition)) {
%>
<div class="result-nav mb15"></div><!--result-nav-->
<%
	}
%>
<%
	if (OutputType.SEARCHRESULT == type && !section.isHideDelete() && canDelete) {
%>
<p><input type="button" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.delete')}" class="gr-btn" onclick="doDelete()" /></p>
<div id="selectDeleteTypeDialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectDeleteType')}" style="display:none;">
<ul style="text-align:left; margin-left:15px;">
<li>
<label><input type="radio" name="deleteType" value="select" checked>${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.deleteRow")}</label>
</li>
<li>
<label><input type="radio" name="deleteType" value="all">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.deleteAll")}<span id="deleteCount"></span></label>
</li>
<li class="chagne-condition" style="display:none;">
${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.displayUnmatch")}
</li>
</ul>
</div>
<script type="text/javascript">
$(function() {
	$("#selectDeleteTypeDialog").dialog({
		resizable: false,
		autoOpen: false,
		height: 180,
		width: 400,
		modal: true,
		buttons: {
			"${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.delete')}": function() {
				var delType = $(":radio[name='deleteType']:checked").val();
				if (delType == "all") {
					deleteByCondition();
				} else {
					deleteRow(true);
				}
				$(this).dialog("close");
			},
			"${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.cancel')}": function() {
				$(this).dialog("close");
			}
		},
		close: function() {
		}
	});
	$("#selectDeleteTypeDialog").on("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
});
function doDelete() {
	if ($("#cb_searchResult").is(":checked")) {
		var type = $(":hidden[name='searchType']").val();
		if (!validation(type)) return;

		count("<%=CountCommand.WEBAPI_NAME%>", type, type + "Form", function(count) {
			var deleteItem = "${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.deleteItem')}";
			deleteItem = deleteItem.replace("{0}", count);
			$("#deleteCount").text(deleteItem);
			$("#selectDeleteTypeDialog").dialog("open");
		});
	} else {
		deleteRow(false);
	}
}
function deleteByCondition() {
	$.blockUI({message: $("#blockLayer"), css: {width: "20px", left: "50%", top: "50%"}});
	var type = $(":hidden[name='searchType']").val();
	var t = $(":hidden[name='_t']").val();
	deleteAll("<%=StringUtil.escapeJavaScript(deleteAllWebapi)%>", type, type + "Form", t, function(message) {
		if (message && message.length > 0) {
			alert(message);
		} else {
			doSearch($(":hidden[name='searchType']").val(), 0, false, "delete");
		}
		$.unblockUI();
	});
}
function deleteRow(isConfirmed) {
	var ids = grid.getGridParam("selarrrow");
	if(ids.length <= 0) {
		alert("${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectMsg')}");
		return;
	}
	if (!isConfirmed && !confirm("${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.deleteMsg')}")) {
		return;
	}

	var oid = [];
	for(var i=0; i<ids.length; ++i) {
		var id = ids[i];
		var row = grid.getRowData(id);
		oid.push(id + "_" + row.orgOid);
	}
	var t = $(":hidden[name='_t']").val();
	deleteList("<%=StringUtil.escapeJavaScript(deleteListWebapi)%>", oid, "<%=StringUtil.escapeJavaScript(viewName)%>", t, function(message) {
		if (message && message.length > 0) {
			alert(message);
		} else {
			doSearch($(":hidden[name='searchType']").val(), $(":hidden[name='offset']").val(), false, "delete");
		}
	});
}
</script>
<%
	}
	if (OutputType.MULTISELECT == type && permitConditionSelectAll) {
		// 全ページor現在ページ選択
%>
<div id="selectSelectAllTypeDialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectSelectAllType')}" style="display:none;">
<ul style="text-align:left; margin-left:15px;">
<li>
<label><input type="radio" name="selectAllType" value="all" checked>${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.selectAllPage")}</label>
</li>
<li>
<label><input type="radio" name="selectAllType" value="current">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.selectCurrentPage")}</label>
</li>
<% if (!"-1".equals(multiplicity)) { %>
<li class="selectalltype-message">
<%= TemplateUtil.getResourceString("mtp-gem-messages", "generic.element.section.SearchResultSection.selectAllTypeMessage", multiplicity) %>
</li>
<% } %>
</ul>
</div>
<div id="selectDeselectAllTypeDialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectDeselectAllType')}" style="display:none;">
<ul style="text-align:left; margin-left:15px;">
<li>
<label><input type="radio" name="deselectAllType" value="all" checked>${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.selectAllPage")}</label>
</li>
<li>
<label><input type="radio" name="deselectAllType" value="current">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.selectCurrentPage")}</label>
</li>
</ul>
</div>
<script type="text/javascript">
$(function() {
	$("#selectSelectAllTypeDialog").dialog({
		resizable: false,
		autoOpen: false,
		height: 180,
		width: 280,
		modal: true,
		buttons: {
			"OK": function() {
				var selectAllType = $(":radio[name='selectAllType']:checked").val();
				if (selectAllType == "all") {
					selectAllPage();
					keepSelectAllStatus = true;
				} else {
					selectCurrentPage();
					keepSelectAllStatus = false;
				}
				$(this).dialog("close");
				$("#cb_searchResult").prop("checked", true);
			},
			"${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.cancel")}": function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#cb_searchResult").prop("checked", false);
		}
	});
	$("#selectSelectAllTypeDialog").bind("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
	$("#selectDeselectAllTypeDialog").dialog({
		resizable: false,
		autoOpen: false,
		height: 160,
		width: 280,
		modal: true,
		buttons: {
			"OK": function() {
				var deselectAllType = $(":radio[name='deselectAllType']:checked").val();
				if (deselectAllType == "all") {
					deselectAllPage();
				} else {
					deselectCurrentPage();
				}
				$(this).dialog("close");
				keepSelectAllStatus = false;
				$("#cb_searchResult").prop("checked", false);
			},
			"${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.cancel")}": function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$("#cb_searchResult").prop("checked", true);
		}
	});
	$("#selectDeselectAllTypeDialog").bind("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
});
function selectAllPage() {
	var type = $(":hidden[name='searchType']").val();
	if (!validation(type)) return;

	searchSelectList("<%=SearchSelectListCommand.WEBAPI_NAME%>", type, type + "Form", function(data) {
		if (multiplicity == -1) {
			selectArray = data;
		} else {
			if (selectArray.length < multiplicity) {<%-- 多重度上限ある場合、既に選択済みのはそのままで、上限まで検索したデータを追加 --%>
				for (var i = 0; i < data.length; i++) {
					if (selectArray.indexOf(data[i]) == -1) {
						selectArray.push(data[i]);
					}
					if (selectArray.length == multiplicity) {
						break;
					}
				}
			}
		}

		loading = true;

		grid.resetSelection();
		applyGridSelection();

		loading = false;

		$("#cb_searchResult").prop("checked", true);
	});
}
function deselectAllPage() {
	selectArray = [];
	grid.resetSelection();
}
</script>
<%
	}
%>
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>">
<input type="hidden" name="executeSearch" value="<c:out value="<%=executeSearch%>"/>">
</form>
</div>
<div id="blockLayer" style="display:none;"><p class="loading"></p></div>
</div>
