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

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.auth.User"%>
<%@ page import="org.iplass.mtp.tenant.Tenant"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.BinaryProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.JoinPropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.PropertyColumn"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchResultSection"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchResultSection.ExclusiveControlPoint"%>
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
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.bulk.MultiBulkUpdateViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.CountCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchSelectListCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	boolean isDispProperty(String defName, PropertyDefinition pd, PropertyColumn property) {
		if (!EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) return false;
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
	/**
	 * 「列の固定を許可」列(=pin対象)のcolModelオプションを返す。
	 * 許可列の場合は副作用としてpin対象列名(frozenPermCols)へ列名を "/" 区切りで追加する。
	 * (プロパティ名に "/" は含まれないため区切り文字として利用可能)
	 */
	String frozenColModelOption(PropertyColumn property, String sortPropName, StringBuilder frozenPermCols) {
		if (!property.isFrozen()) return "";
		if (frozenPermCols.length() > 0) frozenPermCols.append("/");
		frozenPermCols.append(StringUtil.escapeJavaScript(sortPropName));
		return ", frozen:true";
	}
%>
<%
	//呼び出し元のJSPからデータ取得
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	String searchCond = request.getParameter(Constants.SEARCH_COND);
	if (searchCond == null) searchCond = "";

	String viewName = (String) request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";

	HashMap<String, Object> defaultSearchCond = (HashMap<String, Object>) request.getAttribute(Constants.DEFAULT_SEARCH_COND);
	String executeSearch = getDefaultValue(defaultSearchCond, searchCond, Constants.EXECUTE_SEARCH);

	String _multiplicity = request.getParameter(Constants.SELECT_MULTI);
	int multiplicity = 1;
	if (_multiplicity != null) {
		try {
			multiplicity = Integer.parseInt(_multiplicity);
		} catch (NumberFormatException e) {
		}
	}
	
	String _permitConditionSelectAll = request.getParameter(Constants.PERMIT_CONDITION_SELECT_ALL);
	//全選択の範囲、trueの場合は他ページも含む検索条件に一致する全データ
	boolean permitConditionSelectAll = StringUtil.isNotBlank(_permitConditionSelectAll) && "true".equals(_permitConditionSelectAll);

	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);
	SearchFormView view = data.getView();
	SearchResultSection section = view.getResultSection();
	EntityDefinition ed = data.getEntityDefinition();
	String defName = ed.getName();

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

	//Limit件数
	int limit = ViewUtil.getSearchLimit(section);

	//検索結果TABLEの高さ自動調節(section未設定時はServiceConfig既定値)
	SearchResultSection.AutoHeightAdjustMode autoHeightAdjustMode = ViewUtil.getAutoHeightAdjustMode(section);
	//高さ自動調節(画面fit)は「検索結果TABLEの高さ」が0かつ一般検索画面の場合のみ有効(dispHeight>0は固定高優先)
	boolean fitToViewport = section.getDispHeight() == 0
			&& autoHeightAdjustMode == SearchResultSection.AutoHeightAdjustMode.FIT_TO_VIEWPORT
			&& OutputType.SEARCHRESULT == type;

	//「列の固定を許可」された列名の "/" 区切り連結(pin 注入対象。未許可列は pin 非表示)
	//※全ユーザー列(実効固定範囲Kの尺度)はJSP側で収集せず、JSで実際のcolModelから導出する
	//(要素ループで収集すると仮想プロパティやネスト列等が取りこぼれるため)
	StringBuilder frozenPermCols = new StringBuilder();

	//カラム固定の永続化キー接頭辞(tenant/user 単位で分離)
	Tenant frozenTenant = auth.getTenant();
	User frozenUser = auth.getUser();
	String frozenKeyPrefix = (frozenTenant != null ? frozenTenant.getId() : -1)
			+ "." + (frozenUser != null ? frozenUser.getOid() : "anonymous");
	frozenKeyPrefix = StringUtil.escapeJavaScript(frozenKeyPrefix);

	//一括詳細表示アクション
	String bulkEditAction = BulkUpdateViewCommand.BULK_EDIT_ACTION_NAME + urlPath;
	if (section.isUseBulkView()) {
		bulkEditAction = MultiBulkUpdateViewCommand.BULK_EDIT_ACTION_NAME + urlPath;
	}

	Boolean showdDetermineButton = (Boolean) request.getAttribute(Constants.SHOW_DETERMINE_BUTTON);
	if (showdDetermineButton == null) showdDetermineButton = false;

	Boolean multiSelect = OutputType.SEARCHRESULT == type && !section.isHideDelete() && canDelete || OutputType.SEARCHRESULT == type && section.isShowBulkUpdate() && canUpdate || OutputType.MULTISELECT == type;
	if (multiSelect == null) multiSelect = false;

	String id = "";
	if (StringUtil.isNotBlank(section.getId())) {
		id = "id=\"" + StringUtil.escapeHtml(section.getId()) + "\"";
	}

	String secStyle = "";
	if (StringUtil.isNotBlank(section.getStyle())) {
		secStyle = section.getStyle();
	}

	String title = GemResourceBundleUtil.resourceString("generic.element.section.SearchResultSection.srchrslt");
	if (StringUtil.isNotBlank(section.getTitle())) {
		title = TemplateUtil.getMultilingualString(section.getTitle(), section.getLocalizedTitleList());
	}
	
	String styleAttr = ViewUtil.buildHeightStyleAttr(section.getSectionHeight());
%>
<div <%=id %> class="result-block search-result-section <c:out value="<%=secStyle %>"/>" style="display:none;">
<h3 class="hgroup-02">
<c:out value="<%=title %>"/>
<span class="chagne-condition" style="display:none;">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.conditionChanged")}</span>
<span class="searching" style="display:none;">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.searching")}</span>
<span class="paginInputError" style="display:none;">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.paginInputError")}</span>
</h3>
<div class="result-data" style="display:none;<%= styleAttr %>">
<%
	if (showdDetermineButton) {
		//選択画面の確定ボタンを結果の上にも出す
%>
<p class="btn"><input type="button" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.determine')}" class="gr-btn gr-size-01 mb10" onclick="closeModal()" /></p>
<%
	}
%>
<input type="hidden" name="offset" value="0" >
<input type="hidden" name="limit" value="<%=limit%>">
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

	var cellAttrFunc = function (rowId, val, rowObject, colModel, rdata) {
<%
	if (section.isGroupingData()) {
%>
		var rowIndex = parseInt(rowId) - 1;
		var data = grid.getGridParam("_data");
		var row = data[rowIndex];
		var colName = colModel.name;

		if (rowIndex > 0) {
			var beforeRow = data[rowIndex - 1];
			//前の行と値が同じか確認
			var dif = false;
			if (row.orgOid != beforeRow.orgOid || row.orgVersion != beforeRow.orgVersion || row[colName] != beforeRow[colName]) {
				dif = true;
			}
			//同じ場合は非表示にする、タイトルを指定しないと(""含む)CELL値が設定されるので指定
			if (!dif) return " title=\"&nbsp;\" style=\"display:none;\" ";
		}

		//この行から何行分rowspanを設定するか計算
		var count = 0;
		for (var i = rowIndex; i < data.length; i++) {
			if (i >= data.length) break;
			var nextRow = data[i];
			var dif = false;
			if (row.orgOid != nextRow.orgOid || row.orgVersion != nextRow.orgVersion || row[colName] != nextRow[colName]) {
				dif = true;
				break;
			}
			if (!dif) count++;
			else break;
		}
		if (count > 1) {
			var label = $.jgrid.stripHtml(rdata[colModel.name]).replace(/'/g, "&apos;");
			if (!label || typeof label === "undefined") {
				//タイトルを指定しないと(""含む)CELL値が設定されるので指定
				label = "&nbsp;";
			}
			return " title=\"" + label + "\" style=\"vertical-align: center !important;\" rowspan=\"" + count + "\"";
		} else {
			return null;
		}
<%
	} else {
%>
		//definitionの設定がfalseなら結合しない
		return null;
<%
	}
%>
	}

	var multiSelect = <%=multiSelect%>;
	var colModel = new Array();
	colModel.push({name:"orgOid", index:"orgOid", sortable:false, hidden:true, frozen:true, label:"oid", formatter:oidCellFormatter});
	colModel.push({name:"orgVersion", index:"orgVersion", sortable:false, hidden:true, frozen:true, label:"version"});
<%
	if (section.getExclusiveControlPoint() == ExclusiveControlPoint.WHEN_SEARCH) {
%>
	colModel.push({name:"orgTimestamp", index:"orgTimestamp", sortable:false, hidden:true, frozen:true, label:"timestamp"});
<%
	}
	if (OutputType.SINGLESELECT == type) {
		//スタイル調整のため、classes、labelClassesに"sel_radio"を指定
%>
	colModel.push({name:'selOid', index:'selOid', width:20, sortable:false, frozen:true, label:"", resizable:false, classes:"sel_radio", labelClasses:"sel_radio", cellattr: cellAttrFunc});
<%
	} else if (OutputType.MULTISELECT == type) {
	} else if (OutputType.SEARCHRESULT == type) {
%>
	colModel.push({name:'_mtpDetailLink', index:'_mtpDetailLink', width:${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.detailLinkWidth")}, sortable:false, align:'center', frozen:true, label:"", classes:"detail-links", cellattr: cellAttrFunc});
<%
	}

	for (Element element : section.getElements()) {
		if (element instanceof PropertyColumn) {
			PropertyColumn property = (PropertyColumn) element;
			String propName = property.getPropertyName();
			PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
			String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());

			if (isDispProperty(defName, pd, property)) {
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
					if (!property.isSortable() || !ViewUtil.getEntityViewHelper().isSortable(pd)) {
						sortable = "sortable:false";
					}
					String hidden = ", hidden:false";
					if (property.getEditor() != null && property.getEditor().isHide()) {
						hidden = ", hidden:true";
					}
					String frozen = frozenColModelOption(property, sortPropName, frozenPermCols);
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
	colModel.push({name:"<%=sortPropName%>", index:"<%=sortPropName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", <%=sortable%><%=hidden%><%=frozen%><%=width%><%=align%>, cellattr: cellAttrFunc});

<%
				//参照プロパティでJoinPropertyEditorを利用する場合
				} else if (property.getEditor() instanceof JoinPropertyEditor) {
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
					if (!property.isSortable() || !ViewUtil.getEntityViewHelper().isSortable(pd)) {
						sortable = "sortable:false";
					}
					String frozen = frozenColModelOption(property, sortPropName, frozenPermCols);
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
	colModel.push({name:"<%=sortPropName%>", index:"<%=sortPropName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", <%=sortable%><%=frozen%><%=width%><%=align%>, cellattr: cellAttrFunc});
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
						if (!property.isSortable() || !ViewUtil.getEntityViewHelper().isSortable(pd)) {
							sortable = "sortable:false";
						}
						String hidden = ", hidden:false";
						if (property.getEditor() != null && property.getEditor().isHide()) {
							hidden = ", hidden:true";
						}
						String frozen = frozenColModelOption(property, sortPropName, frozenPermCols);
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
	colModel.push({name:"<%=sortPropName%>", index:"<%=sortPropName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", <%=sortable%><%=hidden%><%=frozen%><%=width%><%=align%>, cellattr: cellAttrFunc});
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
			String hidden = ", hidden:false";
			if (property.getEditor() != null && property.getEditor().isHide()) {
				hidden = ", hidden:true";
			}
			String style = property.getStyle() != null ? property.getStyle() : "";
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,style) --%>
colModel.push({name:"<%=propName%>", index:"<%=propName%>", classes:"<%=style%>", label:"<p class='title'><%=displayLabel%></p>", sortable:false <%=hidden%><%=width%><%=align%>, cellattr: cellAttrFunc});
<%
		}
	}
%>
	grid = $("#searchResult").jqGrid({
		datatype: "local",
		autoencode: false,
<%
	if (section.getDispHeight() > 0) {
%>
		height: <%=section.getDispHeight()%>,
<%
	} else {
%>
		height: "auto",
<%
	}
%>
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
			var rowIndex = parseInt(rowid) - 1;

			clearRowHighlight(rowIndex);
<%
		if (section.isGroupingData()) {
			// 結合されたチェックボタンにチェックを入れます。
%>
			for (var i = rowIndex; i >= 0; i--) {
				if ($("#gview_searchResult tr.jqgrow:eq(" + i + ")").find(":radio[name='selOid'][value='" + es(value) + "']").is(":visible")) {
					rowIndex = i;
					break;
				}
			}
<%
		}
%>
			var $selRow = $("#gview_searchResult tr.jqgrow:eq(" + rowIndex + ")");
			$selRow.find(":radio[name='selOid'][value='" + es(value) + "']").prop("checked", true);
<%
		if (section.isGroupingData()) {
%>
			var rowspan = $selRow.children("td.sel_radio").attr("rowspan");
			if (rowspan && e) {
				for (var i = rowIndex; i < rowIndex + parseInt(rowspan); i++) {
					setRowHighlight(i);
				}
			}
<%
		} else {
%>
			setRowHighlight(rowIndex);
<%
		}
%>
			selectArray.splice(0, selectArray.length, value);
		}
<%
	} else if (OutputType.MULTISELECT == type) {
%>
		,onSelectRow: function(rowid, e) {
			if (!loading) {
				var row = grid.getRowData(rowid);
				var id = row.orgOid + "_" + row.orgVersion;
				if (e) {
					<%-- 同じOIDとVersionのレコードを選択配列に追加しません。 --%>
					if (selectArray.indexOf(id) == -1 && (multiplicity == -1 || selectArray.length < multiplicity)) {
						selectArray.push(id);
						<%-- 多重度が複数のデータの場合、行番号が違う同じOIDとVersionのレコードがあるので、チェックを付け直します。 --%>
						grid.resetSelection();
						applyGridSelection(false);
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
					<%-- 多重度が複数のデータの場合、行番号が違う同じOIDとVersionのレコードがあるので、チェックを付け直します。 --%>
					grid.resetSelection();
					applyGridSelection(false);
				}
				keepSelectAllStatus = false;
			}
		}
<%
	} else if (OutputType.SEARCHRESULT == type) {
%>
		,onSelectRow: function(rowid, e) {
			var row = grid.getRowData(rowid);
			var id = row.orgOid + "_" + row.orgVersion;
<%
		if (!multiSelect) {
%>
			var rowIndex = parseInt(rowid) - 1;
			clearRowHighlight(rowIndex);
			if (e) {
				$("#searchResult tr[id]").each(function() {
					var _rowid = $(this).attr("id");
					if (_rowid == rowid) return;
					var _row = grid.getRowData(_rowid);
					var _id = _row.orgOid + "_" + _row.orgVersion;
					if (id == _id) $(this).addClass("ui-state-highlight");
				});
			}
<%
		} else {
			// 多重度が複数のデータの場合、行番号が違う同じOIDとVersionのレコードがあるので、チェックを付け直します。
%>
			$("#searchResult tr[id]").each(function() {
				var _rowid = $(this).attr("id");
				if (_rowid == rowid) return;
				var _row = grid.getRowData(_rowid);
				var _id = _row.orgOid + "_" + _row.orgVersion;
				if (id == _id) grid.setSelection(_rowid, false);
			});
<%
		}
%>
		}
<%
	}

	if (section.isGroupingData()) {
%>
		,gridComplete: function() {
			var data = $("#searchResult").getGridParam("_data");
			if (!data) return;
			//チェックボタン一覧の結合処理を行います。
			//凍結中は clone 側(frozen-bdiv)にも tr.jqgrow が存在するため主表のみを対象とする(二重走査防止)
			$("#gview_searchResult .ui-jqgrid-bdiv:not(.frozen-bdiv) tr.jqgrow").each(function(index){
				var row = data[index];
				if (index > 0) {
					var beforeRow = data[index - 1];
					//前の行と値が同じか確認
					var dif = false;
					if (row.orgOid != beforeRow.orgOid || row.orgVersion != beforeRow.orgVersion) {
						dif = true;
					}
					if (!dif) {
						$(this).children(".td_cbox").hide(); return;
					}
				}

				//この行から何行分rowspanを設定するか計算
				var count = 0;
				for (var i = index; i < data.length; i++) {
					var nextRow = data[i];
					var dif = false;
					if (row.orgOid != nextRow.orgOid || row.orgVersion != nextRow.orgVersion) {
						dif = true;
						break;
					}
					if (!dif) count++;
					else break;
				}
				if (count > 1) $(this).children(".td_cbox").attr("rowspan", count);
			})
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
	var limit = <%=limit%>;

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
		},
		pagingInputErrorFunc: function() {
			$pager.lock();
			$(".paginInputError").show().fadeOut(1500, function() {
				$pager.unlock();
			});
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
	//行クリア後に凍結構成を強制再適用(シグネチャをリセットし destroy→set を促す——
	//addRowData は凍結 clone 再生成のトリガにならないため、再適用なしでは行が復元されない。
	//なお clearGridData は主表と同時に clone 行も削除するため旧データの残留は生じない)
	frozenAppliedSignature = null;
	grid.setGridParam({"_data": list}).trigger("reloadGrid");

<%	if (type == OutputType.SINGLESELECT) { %>
	var $selOid = $("<p/>");
	var $selSingle = $("<span/>").addClass("singleRowSelect").appendTo($selOid);
	var $selRadio = $("<input/>").attr({"type":"radio", "name":"selOid"}).appendTo($selSingle);
<%
	} else if (type == OutputType.SEARCHRESULT) {
%>
	var $viewLink = $("<a/>").attr({"href":"javascript:void(0)", "action":"<%=StringUtil.escapeJavaScript(viewAction)%>", "title":"${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.detail')}"})
			.addClass("detailLink").text("${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.detail')}");
<%
		if (!section.isHideDetailLink() && (canUpdate || canDelete)) {
			//編集表示
%>
	$viewLink.addClass("jqborder"); //真ん中の棒線
	var $editLink = $("<a/>").attr({"href":"javascript:void(0)", "action":"<%=StringUtil.escapeJavaScript(detailAction)%>", "title":"${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.edit')}"})
			.addClass("detailLink editLink").text("${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.edit')}");
<%
		} else {
			//編集非表示
%>
	var $detailLink = $("<p/>");
	$viewLink.appendTo($detailLink);
<%
		}
	}
%>

	$(list).each(function(index) {
		this["searchResultDataId"] = this.orgOid + "_" + this.orgVersion;
<%	if (type == OutputType.SINGLESELECT) { %>
		$selRadio.attr("value", this.searchResultDataId);
		this["selOid"] = $selOid.html();
<%
	} else if (type == OutputType.SEARCHRESULT) {
%>
		$viewLink.attr({"oid":this.orgOid, "version":this.orgVersion});
<%
		if (!section.isHideDetailLink() && (canUpdate || canDelete)) {
%>
		$editLink.attr({"oid":this.orgOid, "version":this.orgVersion});

		var $detailLink = $("<p/>");
		if (this["@canEdit"] === "false" && this["@canDelete"] === "false") {
			$viewLink.removeClass("jqborder");
			$viewLink.appendTo($detailLink);
		} else {
			$viewLink.addClass("jqborder"); //真ん中の棒線
			$viewLink.appendTo($detailLink);
			$editLink.appendTo($detailLink);
		}
<%		} %>
		this["_mtpDetailLink"] = $detailLink.html();
<%
	}
%>
		grid.addRowData(index + 1, this);
	});

<%
	if (section.getDispHeight() > 0) {
%>
	//データ描画後に幅を再計算してjqGridにスクロールバー幅を確保させる(列幅・横スクロールは保持)。
	grid.jqGrid("setGridWidth", $("#gbox_searchResult").width(), false);
<%
	}
%>

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
		var $radio = $(":radio[name='selOid'][value='" + es(selectArray[0]) + "']:visible").prop("checked", true).trigger("change");
		if ($radio.length > 0) {
			var rowIndex = $("#gview_searchResult tr.jqgrow").index($radio.parents("tr.jqgrow"));
			setRowHighlight(rowIndex);
<%
		if (section.isGroupingData()) {
%>
			var rowspan = $radio.parents("td.sel_radio").attr("rowspan");
			if (rowspan) {
				for (var i = rowIndex; i < rowIndex + parseInt(rowspan); i++) {
					setRowHighlight(i);
				}
			}
<%
		}
%>
		}
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



	$(".fixHeight").fixHeight();
	adjustResultGridHeight();
	applyFrozenColumns();
	refreshFrozenPins();
	updateFrozenPinsDisabled();

	//ソート状態の表頭反映(sortGrid)は凍結クローン再構築の後に実施する——
	//凍結中は jqGrid がソートアイコンをクローン側(fhDiv)に表示するため、クローン再構築の前に
	//適用すると表示が失われる(固定適用後ソート矢印が出ない問題の対策)
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

	//高さ自動調節(画面fit)時は、初回計算後にページ全体のレイアウト確定(コンテンツ領域の
	//画面高調整がボタン区等へ波及する)ことで下方要素の高さが変わり得るため、遅延して再計算する
	if (fitToViewportMode) {
		setTimeout(adjustResultGridHeight, 300);
	}
}
function applyGridSelection(onselectrow) {
	$("#searchResult tr[id]").each(function() {
		var rowid = $(this).attr("id");
		var row = grid.getRowData(rowid);
		var id = row.orgOid + "_" + row.orgVersion;
		for (var i = 0; i < selectArray.length; i++) {
			if (id == selectArray[i]) {
				if (typeof onselectrow === "boolean") {
					grid.setSelection(rowid, onselectrow);
				} else {
					grid.setSelection(rowid);
				}
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
var clearRowHighlight = function(rowIndex) {
	var $rows = $("#searchResult tr.jqgrow");
	if (rowIndex >= $rows.length) return;
	//選択された行以外にハイライトをクリアします。
	$rows.each(function(index) {
		if (index != rowIndex) $(this).removeClass("ui-state-highlight");
	});
}
var setRowHighlight = function (rowIndex) {
	var $rows = $("#searchResult tr.jqgrow");
	if (rowIndex >= $rows.length) return;
	$rows.eq(rowIndex).addClass("ui-state-highlight");
}
var fitToViewportMode = <%=fitToViewport%>;
var RESULT_GRID_MIN_HEIGHT = 80;
function adjustResultGridHeight() {
	if (!fitToViewportMode) return;
	if ($("div.result-data").css("display") == "none") return;
	var $gbox = $("#gbox_searchResult");
	if ($gbox.length == 0) return;

	var gridTop = $gbox.offset().top;
	var $win = $(window);
	var viewportBottom = $win.scrollTop() + $win.height();

	//テーブル下端以降の可視要素(ページング・削除/一括更新ボタン区等)の高さを算出
	var belowHeight = 0;
	$gbox.nextAll().each(function() {
		var $elem = $(this);
		if ($elem.css("display") != "none" && $elem.css("visibility") != "hidden") {
			var h = $elem.outerHeight(true);
			if ($elem.hasClass("result-btns")) {
				//ボタン区はページレイアウト(コンテンツ領域の画面高調整)の引き伸ばしにより
				//実際の内容以上の高さで測定される実測がある(内容はボタンのみ)——
				//内容(ボタン)の実高と上下 margin のみを計上し、テーブル高の不当な圧縮を防ぐ
				var btnH = 0;
				$elem.children(":visible").each(function() {
					btnH += $(this).outerHeight(true);
				});
				h = btnH + parseFloat($elem.css("marginTop") || 0) + parseFloat($elem.css("marginBottom") || 0);
			}
			belowHeight += h;
		}
	});

	//gbox全体に割当て可能な高さ
	var gboxHeight = Math.floor(viewportBottom - gridTop - belowHeight);
	var $bdiv = $(grid[0].grid.bDiv);
	var outsideBodyHeight = $gbox.outerHeight(true) - $bdiv.outerHeight(true);
	if (outsideBodyHeight < 0) outsideBodyHeight = 0;

	var height = gboxHeight - outsideBodyHeight;
	if (height < RESULT_GRID_MIN_HEIGHT) height = RESULT_GRID_MIN_HEIGHT;

	grid.jqGrid("setGridHeight", height);
	//スクロールバー出現後の横幅再計算(列幅・横スクロールは保持: 既存dispHeight>0時と同じ考慮)
	grid.jqGrid("setGridWidth", $("#gbox_searchResult").width(), false);
}
//リサイズイベント連続発火時の過剰実行を抑止するためのデバウンス用タイマーID
var resultGridResizeTimerId = null;
$(window).on("resize", function() {
	//リサイズ時も高さを再調節——高さ調節は高さ自動調節(画面fit)モードのみ、
	//ピン操作可否の再判定は全モードで実施
	if (resultGridResizeTimerId != null) {
		clearTimeout(resultGridResizeTimerId);
	}
	resultGridResizeTimerId = setTimeout(function() {
		resultGridResizeTimerId = null;
		if (fitToViewportMode) {
			adjustResultGridHeight();
		}
		updateFrozenPinsDisabled();
	}, 200);
});
//カラム固定:AdminConsole「列の固定を許可」された列のピン操作+初期反映
//仕様: 許可列(frozenPermColumns)のみピンが表示され、既定で固定される。
const pinAvailable = <%=OutputType.SEARCHRESULT == type%>;
//全ユーザー列(実効範囲Kの尺度基準。colModel順)——実際の colModel から導出する。
const frozenSystemColumns = new Set(["orgOid", "orgVersion", "orgTimestamp", "selOid", "_mtpDetailLink"]);
let frozenUserColumnsCache = null;
function frozenUserColumns() {
	if (frozenUserColumnsCache != null) return frozenUserColumnsCache;
	frozenUserColumnsCache = [];
	if (grid != null) {
		const cm = grid.jqGrid("getGridParam", "colModel");
		for (let i = 0; i < cm.length; i++) {
			if (!frozenSystemColumns.has(cm[i].name)) frozenUserColumnsCache.push(cm[i].name);
		}
	}
	return frozenUserColumnsCache;
}
//「列の固定を許可」された列(pin 注入対象)。JSPからは "/" 区切りの列名文字列として出力される
const frozenPermColsSrc = "<%=frozenPermCols.toString()%>";
const frozenPermColumns = new Set(frozenPermColsSrc.length > 0 ? frozenPermColsSrc.split("/") : []);
let frozenColumnCount = 0;
let frozenColumnCountInited = false;
//全ユーザー列中の位置(1始まり)。0=非ユーザー列
function userColPos(name) {
	return frozenUserColumns().indexOf(name) + 1;
}
function isPermColumn(name) {
	return frozenPermColumns.has(name);
}
function initFrozenColumnCount() {
	if (frozenColumnCountInited) return;
	frozenColumnCountInited = true;
	//許可列(colModel.frozen==true は JSP が許可列に出力)の最も右の全ユーザー列位置を固定列数の初期値とする
	const cm = grid.jqGrid("getGridParam", "colModel");
	for (let i = 0; i < cm.length; i++) {
		if (isPermColumn(cm[i].name) && cm[i].frozen === true) {
			const pos = userColPos(cm[i].name);
			if (pos > frozenColumnCount) frozenColumnCount = pos;
		}
	}
	loadFrozenColumnCount();
}
//最後に凍結適用した構成(固定列数+各列のfrozen状態)を表す識別子。
//applyFrozenColumns() で現在の構成と比較し、変化が無い場合に destroy→set の再適用を省く。
//行クリア時(setData)は clone 再生成が必要なため null にリセットして強制再適用させる。
let frozenAppliedSignature = null;
function applyFrozenColumns() {
	if (!pinAvailable || grid == null) return;
	initFrozenColumnCount();
	const $gbox = $("#gbox_searchResult");
	const cm = grid.jqGrid("getGridParam", "colModel");
	let signature = String(frozenColumnCount);
	for (let i = 0; i < cm.length; i++) {
		//ユーザー列のみを実効範囲(先頭から固定列数分の列)で frozen を制御する(最右側連続化——非許可列も含む)。
		const pos = userColPos(cm[i].name);
		if (pos > 0) {
			const f = pos <= frozenColumnCount;
			if (cm[i].frozen !== f) grid.jqGrid("setColProp", cm[i].name, { frozen: f });
			cm[i].frozen = f;
		}
		signature += (cm[i].frozen === true ? "1" : "0");
	}
	if (signature === frozenAppliedSignature) return;
	//凍結構成が変わったら必ず破棄→再適用(適用済み set は no-op、かつ clone は再構築されないため)
	const applied = grid.jqGrid("getGridParam", "frozenColumns") === true;
	if (applied) {
		grid.jqGrid("destroyFrozenColumns");
		//適用中マーカー除去(frozen-ever は行高恒定マーカーのため解除後も残す)
		$gbox.removeClass("frozen-columns");
	}
	if (frozenColumnCount > 0) {
		//main ヘッダーのピンが現状態であることを確認してから set(clone に複製される)
		refreshFrozenPins();
		grid.jqGrid("setFrozenColumns");
		//frozen-columns=適用中マーカー。frozen-ever=行高恒定マーカー(一度適用した grid は解除後も同一行高)
		$gbox.addClass("frozen-columns").addClass("frozen-ever");
		//fhDiv(凍結表頭)の寸法同期——列幅合計を明示。
		//flat skin の module.css(`width:auto !important`)が inline width を打ち負かすため
		//min-width で指定、幅は hidden 列を除いた実効幅として内部 table の実測幅を採用。
		//height 同期は fh 高と主表頭高の差>2px が確認された場合のみ有効化
		const $fh = $gbox.find(".frozen-div");
		if ($fh.length > 0) {
			const $fhTable = $fh.children("table.ui-jqgrid-htable");
			const fhW = $fhTable.length > 0 ? $fhTable[0].offsetWidth : 0;
			if (fhW > 0) $fh.css("min-width", fhW + "px");
			//主表頭は fhDiv も .ui-jqgrid-hdiv を持つため :not(.frozen-div) で除外して取得
			const mainHeaderHeight = $gbox.find(".ui-jqgrid-hdiv:not(.frozen-div)").height();
			if ($fh.height() !== mainHeaderHeight) {
				$fh.height(mainHeaderHeight);
			}
		}
		//両表の行高を1回明示同期(frozen/main の描画差による累積ずれ対策)。
		//主表側セレクタは .frozen-bdiv が ui-jqgrid-bdiv を兼任するため :not で凍結側を除外
		const $frows = $gbox.find(".frozen-bdiv tr.jqgrow");
		$gbox.find(".ui-jqgrid-bdiv:not(.frozen-bdiv) tr.jqgrow").each(function(i) {
			if ($frows.eq(i).length > 0) $frows.eq(i).height($(this).height());
		});
	}
	frozenAppliedSignature = signature;
}
function toggleFrozenColumn(name) {
	if (!pinAvailable || grid == null) return;
	initFrozenColumnCount();
	const n = userColPos(name);
	if (n <= 0 || !isPermColumn(name)) return;
	//クリック列が実効範囲外→その列まで拡張 / 範囲内→その列と右側を解除
	frozenColumnCount = (n > frozenColumnCount) ? n : n - 1;
	applyFrozenColumns();
	refreshFrozenPins();
	updateFrozenPinsDisabled();
	saveFrozenColumnCount();
}
function refreshFrozenPins() {
	if (!pinAvailable || grid == null) return;
	initFrozenColumnCount();
	const $gbox = $("#gbox_searchResult");
	if ($gbox.length == 0) return;
	const cm = grid.jqGrid("getGridParam", "colModel");
	//main ヘッダーのみ対象——fhDiv も .ui-jqgrid-hdiv class を持つため .frozen-div を除外
	const $ths = $gbox.find(".ui-jqgrid-hdiv tr.ui-jqgrid-labels th").filter(function() {
		return jQuery(this).closest(".frozen-div").length === 0;
	});
	let pinCreated = false;
	$ths.each(function(idx) {
		const name = cm[idx] ? cm[idx].name : null;
		//許可列のみ pin を持つ(未許可列は操作の入口が存在しない)
		if (!isPermColumn(name)) return;
		const $th = $(this);
		let $pin = $th.find(".mtp-col-pin");
		if ($pin.length == 0) {
			$pin = $("<a/>").attr({
				href: "javascript:void(0)",
				"class": "mtp-col-pin",
				"data-colname": name,
				"aria-label": name
			}).append($("<i/>").addClass("fas fa-thumbtack"));
			pinCreated = true;
			//th の jqGrid クリック処理が伝播を断つため document 委譲でなく直接結合(clone(true) が handler を複製)
			$pin.on("click", function(e) {
				e.preventDefault();
				e.stopPropagation();
				toggleFrozenColumn($(this).attr("data-colname"));
			});
			//title(p.title) と同一行・sort ボタン(s-ico)の前に挿入
			const $sico = $th.children("div").first().children("span.s-ico");
			if ($sico.length > 0) {
				$pin.insertBefore($sico);
			} else {
				$th.children("div").first().append($pin);
			}
		}
		//実効範囲内(先頭から固定列数分のユーザー列)はON常時表示。範囲外はOFF(hover 時のみ表示)
		const effective = userColPos(name) <= frozenColumnCount;
		$pin.toggleClass("mtp-pin-on", effective);
	});
	//FA(JS版)は <i> を <svg> へ置換する——動的注入分を確実に描画するため gbox 配下を再走査
	//(main で置換済みなら setFrozenColumns の clone は svg を複製する。失敗しても機能には影響なし)
	if (pinCreated && window.FontAwesome && window.FontAwesome.dom && window.FontAwesome.dom.i2svg) {
		try { window.FontAwesome.dom.i2svg({ node: $gbox[0] }); } catch (e) { /* 置換失敗時は無視 */ }
	}
}
function updateFrozenPinsDisabled() {
	if (!pinAvailable || grid == null) return;
	const $gbox = $("#gbox_searchResult");
	if ($gbox.length == 0) return;
	const cm = grid.jqGrid("getGridParam", "colModel");
	let sum = 0;
	for (let i = 0; i < cm.length; i++) {
		if (cm[i].hidden !== true) sum += (cm[i].width ? +cm[i].width : 0);
	}
	//判定基準は結果領域のコンテナ幅——凍結なしの場合 grid は列幅合計まで自動拡張し
	//bdiv 自体が広がるため、bdiv 幅では横スクロール有無を判定できない
	const avail = $gbox.closest(".result-data").width() || $gbox.parent().width() || 0;
	//全列が収まる(横スクロール無し)場合はピンを操作不可とする
	//(表示仕様=非表示。DOM/クラス判定は残し E2E で検証)
	$gbox.find(".mtp-col-pin").toggleClass("mtp-pin-disabled", sum <= avail + 1);
}
//SessionStorage 永続化(common.js の set/getSessionStorage を利用。ブラウザのセッション(タブ)内で保持)
function frozenStorageKey() {
	return "frozenColumns_<%=frozenKeyPrefix%>_<%=StringUtil.escapeJavaScript(defName)%>_<%=StringUtil.escapeJavaScript(viewName)%>";
}
function loadFrozenColumnCount() {
	if (!frozenColumnCountInited) return;
	const saved = getSessionStorage(frozenStorageKey());
	if (saved == null) return;
	const count = parseInt(saved, 10);
	//不正値は無視して許可設定を既定とする
	if (isNaN(count)) return;
	frozenColumnCount = Math.max(0, Math.min(count, frozenUserColumns().length));
}
function saveFrozenColumnCount() {
	if (!frozenColumnCountInited) return;
	setSessionStorage(frozenStorageKey(), frozenColumnCount);
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
<p class="result-btns">
<%
	if (OutputType.SEARCHRESULT == type && !section.isHideDelete() && canDelete) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.delete')}" class="gr-btn" onclick="doDelete()" />
<%
	}
	if (OutputType.SEARCHRESULT == type && section.isShowBulkUpdate() && canUpdate) {
		String bulkUpdateDisplayLabel = GemResourceBundleUtil.resourceString("generic.element.section.SearchResultSection.bulkUpdate");
		String localizedBulkUpdateDisplayLabel = TemplateUtil.getMultilingualString(section.getBulkUpdateDisplayLabel(), section.getLocalizedBulkUpdateDisplayLabel());
		if (StringUtil.isNotBlank(localizedBulkUpdateDisplayLabel)) {
			bulkUpdateDisplayLabel = localizedBulkUpdateDisplayLabel;
		}
%>
<input id="bulkUpdateBtn" type="button" value="<%=bulkUpdateDisplayLabel%>" class="gr-btn" onclick="doBulkUpdate(this)" />
<%	} %>
</p>
<%
	if (OutputType.SEARCHRESULT == type && !section.isHideDelete() && canDelete) {
%>
<div id="selectDeleteTypeDialog" class="mtp-jq-dialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectDeleteType')}" style="display:none;">
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
	var allVersion = $("[name*='allVersion']:checked", $("[name='" + type + "Form']")).val();
	var t = $(":hidden[name='_t']").val();
	deleteAll("<%=StringUtil.escapeJavaScript(deleteAllWebapi)%>", type, type + "Form", allVersion, t, function(message) {
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
		//rowid_oid_version
		oid.push(id + "_" + row.orgOid + "_" + row.orgVersion);
	}
	var type = $(":hidden[name='searchType']").val();
	var allVersion = $("[name*='allVersion']:checked", $("[name='" + type + "Form']")).val();
	var t = $(":hidden[name='_t']").val();
	deleteList("<%=StringUtil.escapeJavaScript(deleteListWebapi)%>", oid, "<%=StringUtil.escapeJavaScript(viewName)%>", allVersion, t, function(message) {
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
	if (OutputType.SEARCHRESULT == type && section.isShowBulkUpdate() && canUpdate) {
%>
<script>
$(function() {
	document.scriptContext["countBulkUpdate"] = function($frame, func) {
		var type = $(":hidden[name='searchType']").val();
		if (!validation(type)) return;

		count("<%=CountCommand.WEBAPI_NAME%>", type, type + "Form", function(count) {
			if(func && $.isFunction(func)){
				func.call($frame, count);
			}
		});
	}

	document.scriptContext["bulkUpdateModalWindowCallback"] = function(id) {
		if (typeof id === "undefined") return;
		// 一括更新後行選択処理を実行する　
		var selectAfterBulkUpdate = function() {
			// 検索条件を元に一括更新の場合
			if (id === "all") {
				$("#cb_searchResult").trigger("click");
			// 選択された行を一括更新
			} else if ($.isArray(id)) {
				selectArray = id;
				applyGridSelection(false);
			}
			$(".result-block").off("iplassAfterSearch", selectAfterBulkUpdate);
		}
		$(".result-block").on("iplassAfterSearch", selectAfterBulkUpdate);
		doSearch($(":hidden[name='searchType']").val(), $(":hidden[name='offset']").val(), false, "bulkUpdate");
	}
});
function doBulkUpdate(target) {
	var searchCondChanged = $(".chagne-condition").css("display") != "none";
	if(searchCondChanged && !confirm('${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.displayUnmatchBulk")}')) {
		return false;
	}

	var type = $(":hidden[name='searchType']").val();
	if (!validation(type)) return;

	var ids = grid.getGridParam("selarrrow");
	if(ids.length <= 0) {
		alert("${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectBulkUpdateMsg')}");
		return false;
	} else {
		<%-- 選択行をソートします --%>
		ids.sort((a, b) => parseInt(a) - parseInt(b));
	}

	var dialogOption = {resizable: true};
<%
		if (!section.isUseBulkView()) {
%>
		dialogOption.dialogHeight = 450;
<%
		}
%>
	var $bulkUpdateDialogTrigger = getDialogTrigger($(target).parent(), dialogOption);
	$bulkUpdateDialogTrigger.click();

	var oid = [];
	var version = [];
<%
		if (section.getExclusiveControlPoint() == ExclusiveControlPoint.WHEN_SEARCH) {
%>
	var timestamp = [];
<%
		}
%>
	for(var i=0; i< ids.length; ++i) {
		var id = ids[i];
		var row = grid.getRowData(id);
<%
		if (section.isGroupingData()) {
%>
		if (i > 0) {
			var beforeRow = grid.getRowData(ids[i - 1]);
			if (beforeRow.orgOid == row.orgOid && beforeRow.orgVersion == row.orgVersion) {
				<%-- 重複データを送信しないように --%>
				continue;
			}
		}
		<%-- まとめモードの場合、何番目の選択項目を行番号として設定します。 --%>
		<%-- 更新ダイアログが開く時に、排他エラーメッセージのパラメータなどとして利用します。 --%>
		id = oid.length + 1;
<%
		}
%>
		oid.push(id + "_" + row.orgOid);
		version.push(id + "_" + row.orgVersion);
<%
		if (section.getExclusiveControlPoint() == ExclusiveControlPoint.WHEN_SEARCH) {
%>
		timestamp.push(id + "_" + row.orgTimestamp);
<%
		}
%>
	}

	var target = getModalTarget(isSubModal);
	var action = contextPath + '/' + '<%=StringUtil.escapeJavaScript(bulkEditAction) %>';
	var $form = $("<form />").attr({method:"POST", action:action, target:target}).appendTo("body");

	$(oid).each(function() {
		$("<input />").attr({type:"hidden", name:"oid", value:this}).appendTo($form);
	});
	$(version).each(function() {
		$("<input />").attr({type:"hidden", name:"version", value:this}).appendTo($form);
	});
<%
		if (section.getExclusiveControlPoint() == ExclusiveControlPoint.WHEN_SEARCH) {
%>
	$(timestamp).each(function() {
		$("<input />").attr({type:"hidden", name:"timestamp", value:this}).appendTo($form);
	});
<%
		}
%>
	var searchCond = $(":hidden[name='searchCond']").val();
	$("<input />").attr({type:"hidden", name:"searchCond", value:searchCond}).appendTo($form);

	if ($("#cb_searchResult").is(":checked")) {
		$("<input />").attr({type:"hidden", name:"selectAllPage", value:true}).appendTo($form);
	}
// 	var execType = $(":hidden[name='execType']").val();
// 	$("<input />").attr({type:"hidden", name:"execType", value:execType}).appendTo($form);
	var isSubModal = $("body.modal-body").length !== 0;
	if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
	$form.submit();
	$form.remove();
}

function closeBulkUpdateModalWindow() {
	var isSubModal = $("body.modal-body").length !== 0;
	var target = getModalTarget(isSubModal);
	$("iframe[name='" + target + "']").parents("div.modal-dialog").find(".modal-close").click();
}
</script>
<%
	}

	if (OutputType.MULTISELECT == type && permitConditionSelectAll) {
		// 全ページor現在ページ選択
%>
<div id="selectSelectAllTypeDialog" class="mtp-jq-dialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectSelectAllType')}" style="display:none;">
<ul style="text-align:left; margin-left:15px;">
<li>
<label><input type="radio" name="selectAllType" value="all" checked>${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.selectAllPage")}</label>
</li>
<li>
<label><input type="radio" name="selectAllType" value="current">${m:rs("mtp-gem-messages", "generic.element.section.SearchResultSection.selectCurrentPage")}</label>
</li>
<% 		if (multiplicity != -1) { %>
<c:set var="multiplicity" value="<%=multiplicity%>" />
<li class="selectalltype-message">
${m:rsp("mtp-gem-messages", "generic.element.section.SearchResultSection.selectAllTypeMessage", multiplicity)}
</li>
<% 		} %>
</ul>
</div>
<div id="selectDeselectAllTypeDialog" class="mtp-jq-dialog" title="${m:rs('mtp-gem-messages', 'generic.element.section.SearchResultSection.selectDeselectAllType')}" style="display:none;">
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
