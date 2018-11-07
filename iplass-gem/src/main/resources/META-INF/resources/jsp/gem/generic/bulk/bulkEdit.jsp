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

<%@ page import="java.util.*" %>
<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.entity.BinaryReference"%>
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.*"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.Element" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.property.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.WebRequestConstants" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateFormViewData.*" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkCommandContext.BulkUpdatedProperty"%>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateListCommand" %>
<%@ page import="org.iplass.gem.command.generic.bulk.BulkUpdateAllCommand" %>
<%@ page import="org.iplass.gem.command.generic.search.CountCommand" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	boolean isSelectAll(String selectAllType) {
		return "all".equals(selectAllType) ? true : false;
	}

	boolean isFirstSelect(String selectAllType) {
		return StringUtil.isEmpty(selectAllType) ? true : false;
	}
	
	boolean isUpdateFailed(String bulkUpdatePropNm) {
		return StringUtil.isNotEmpty(bulkUpdatePropNm) ? true : false;
	}
	
	boolean canBulkUpdate(PropertyColumn pc) {
		if (pc.getBulkUpdateEditor() == null || pc.getEditor() instanceof UserPropertyEditor || 
			pc.getEditor() instanceof ExpressionPropertyEditor || pc.getEditor() instanceof AutoNumberPropertyEditor) {
			return false;
		}
		return true;
	}

	//プロパティ値の表示値を取得する
	String getPropertyDisplayValue(PropertyDefinition p, Object propValue) {
		// 表示値に変更済み
		if (propValue instanceof String) return (String)propValue;
		String dispValue = "";
		boolean isMultiple = p.getMultiplicity() != 1;
		// リクエストパラメータから参照型
		if (p instanceof ReferenceProperty) {
			if (isMultiple) {
				StringBuilder strBuilder = new StringBuilder();
				Entity[] tmp = (Entity[])propValue;
				for (Entity e : tmp) {
					strBuilder.append(e.getName() + ",");
				}
				if (strBuilder.length() > 0) {
					strBuilder.deleteCharAt(strBuilder.length() - 1);
					dispValue = strBuilder.toString();
				}
			} else {
				dispValue = ((Entity)propValue).getName();
			}
		} else {
			if (isMultiple) {
				StringBuilder strBuilder = new StringBuilder();
				if (propValue instanceof BinaryReference[]) {
					BinaryReference[] tmp = (BinaryReference[]) propValue;
					for (BinaryReference bf : tmp) {
						strBuilder.append(bf.getName() + ",");
					}
				} else if (propValue instanceof SelectValue[]) {
					SelectValue[] tmp = (SelectValue[]) propValue;
					for (SelectValue sv : tmp) {
						SelectValue lsv = ((SelectProperty) p).getLocalizedSelectValue(sv.getValue(), TemplateUtil.getLanguage());
						strBuilder.append(lsv.getDisplayName() + ",");
					}
				} else {
					strBuilder.append(propValue.toString());
				}
				if (strBuilder.length() > 0) {
					strBuilder.deleteCharAt(strBuilder.length() - 1);
					dispValue = strBuilder.toString();
				}
			} else {
				if (propValue instanceof BinaryReference) {
					dispValue = ((BinaryReference) propValue).getName();
				} else if (propValue instanceof SelectValue) {
					SelectValue sv = (SelectValue)propValue;
					SelectValue lsv = ((SelectProperty) p).getLocalizedSelectValue(sv.getValue(), TemplateUtil.getLanguage());
					dispValue = ((SelectValue) lsv).getDisplayName();
				} else {
					dispValue = propValue.toString();
				}
			}
		}
		return dispValue;
	}%>
<%
	//権限確認用
	AuthContext auth = AuthContext.getCurrentContext();

	//コマンドから
	BulkUpdateFormViewData data = (BulkUpdateFormViewData) request.getAttribute(Constants.DATA);
	String selectAllType = (String) request.getAttribute(Constants.BULK_UPDATE_SELECT_TYPE);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String searchType = (String) request.getAttribute(Constants.SEARCH_TYPE);
	String message = (String) request.getAttribute(Constants.MESSAGE);
	String bulkUpdatePropNm = (String) request.getAttribute(Constants.BULK_UPDATE_PROP_NM);

	OutputType type = OutputType.BULK;
	String contextPath = TemplateUtil.getTenantContextPath();
	EntityDefinition ed = data.getEntityDefinition();
	SearchFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();
	//表示名
	String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	//新規or更新
	String execType = data.getExecType();

	//編集対象情報
	List<String> oids = new ArrayList<String>();
	List<String> versions = new ArrayList<String>();
	List<String> updateDates = new ArrayList<String>();
	// 検索結果一覧チェックを付け直すため
	StringBuilder id = new StringBuilder();
	if (data.getEntries() != null) {
		for (SelectedRowEntity entry : data.getEntries()) {
			oids.add(entry.getRow() + "_" + entry.getEntity().getOid());
			versions.add(entry.getRow() + "_" + entry.getEntity().getVersion());
			updateDates.add(entry.getRow() + "_" + entry.getEntity().getUpdateDate().getTime());
			id.append(",\"" + entry.getEntity().getOid() + "_" + entry.getEntity().getVersion() + "\"");
		}
		if(id.length() >0) id.deleteCharAt(0);
	}

	// プロパティリスト
	List<PropertyColumn> properties = ViewUtil.filterPropertyColumn(form.getResultSection().getElements());
	// Property情報
	Map<String, PropertyDefinition> defMap = new HashMap<String, PropertyDefinition>();
	Map<String, PropertyColumn> colMap = new HashMap<String, PropertyColumn>();
	for (PropertyColumn pc : properties) {
		if (!canBulkUpdate(pc)) continue;
		String propName = pc.getPropertyName();
		colMap.put(propName, pc);

		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
		defMap.put(propName, pd);
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.bulk.pleaseSelect");
	}

	//各プロパティでの権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName); //NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	//	request.setAttribute(Constants.ENTITY_DATA, data.getEntity());
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());
	request.setAttribute(Constants.EXEC_TYPE, execType);
%>
<h3 class="hgroup-02 hgroup-02-01"><%=GemResourceBundleUtil.resourceString("generic.bulk.title", displayName)%></h3>
<%
	if (StringUtil.isNotBlank(message)) {
	if ("SUCCESS".equals(request.getAttribute(WebRequestConstants.COMMAND_RESULT))) {
%>
<span class="success"><c:out value="<%=message%>"/></span>
<%
	} else {
%>
<span class="error"><c:out value="<%=message%>"/></span>
<%
	}
}
%>
<form id="detailForm" method="post" action="<%=contextPath + "/" + BulkUpdateListCommand.BULK_UPDATE_ACTION_NAME%>">
${m:outputToken('FORM_XHTML', true)}
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="execType" value="<c:out value="<%=execType%>"/>" />
<%	if(selectAllType != null) {%>
<input type="hidden" name="selectAllType" value="<c:out value="<%=selectAllType%>"/>" />
<%
	}
	if(searchCond != null) {
%>
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>" />
<script>
$(function() {
	$("#detailForm").on("submit", function() {
		var searchCond = $(":hidden[name='searchCond']").val();
		// 検索条件を元に一括更新
		if (searchCond && searchCond.length > 0) {
			var $form = $(this);
			var params = parseSearchCond(searchCond);
			if (params.length > 0) {
				for (var i = 0; i < params.length; i++) {
					var param = params[i];
					if ($(":hidden[name='" + param.key + "']").length > 0) {
						$(":hidden[name='" + param.key + "']").val(param.val);
					} else {
						$("<input />").attr({type:"hidden", name:param.key, value:param.val}).appendTo($form);
					}
				}
			}
		}
	})
});
</script>
<%
	}
	if (oids != null && oids.size() > 0) {
		for (String  oid : oids) {
%>
<input type="hidden" name="oid" value="<c:out value="<%=oid%>"/>" />
<%
		}
	}
	if (updateDates != null && updateDates.size() > 0) {
		for (String updateDate : updateDates) {
%>
<input type="hidden" name="timestamp" value="<c:out value="<%=updateDate%>"/>" />
<%
		}
	}
	if (versions != null && versions.size() > 0) {
		for (String version : versions) {
%>
<input type="hidden" name="version" value="<c:out value="<%=version%>"/>" />
<%
		}
	}
%>
<div class="formArchive">
<div>
<table class="tbl-maintenance mb10">
<tbody>
<% 
	if (searchCond != null) { 
%>
<tr>
<th rowspan="2">${m:rs("mtp-gem-messages", "generic.bulk.selectBulkUpdateType")}</th>
<td><label><input type="radio" name="selectAllType" value="select" <%=!isSelectAll(selectAllType) ? "checked" : ""%>>${m:rs("mtp-gem-messages", "generic.bulk.updateRow")}</label></td>
</tr>
<tr>
<td><label><input type="radio" name="selectAllType" value="all" <%=isSelectAll(selectAllType) ? "checked" : ""%>>${m:rs("mtp-gem-messages", "generic.bulk.updateAll")}<span id="bulkUpdateCount"></span></label></td>
</tr>
<script>
$(function() {
	var $radio = $(":radio[name='selectAllType']");
	$radio.on("change", function(){
		if ($(this).val() == "all") {
			$("#detailForm").attr("action", "<%=contextPath + "/" + BulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME%>");
		} else {
			$("#detailForm").attr("action", "<%=contextPath + "/" + BulkUpdateListCommand.BULK_UPDATE_ACTION_NAME%>");
		}
	});
	//二回目以降は編集不可にする
	if ("<%=isFirstSelect(selectAllType)%>" == "false") {
		$radio.prop("disabled", true);
	}
})
</script>
<%
	}
%>
<tr>
<th>${m:rs("mtp-gem-messages", "generic.bulk.updatePropName")}</th>
<td>
<select id="sel_<c:out value="<%=Constants.BULK_UPDATE_PROP_NM%>"/>" name="<c:out value="<%=Constants.BULK_UPDATE_PROP_NM%>"/>" class="inpbr form-size-11" onchange="propChange(this)">
<option value="" selected="selected"><%= pleaseSelectLabel %></option>
<%
	for (PropertyColumn pc : colMap.values()) {
		if (!pc.isDispFlag()) continue;
		String propName = pc.getPropertyName();
		//		PropertyDefinition pd = ed.getProperty(propName);
		PropertyDefinition pd = defMap.get(propName);
		String displayLabel = TemplateUtil.getMultilingualString(pc.getDisplayLabel(), pc.getLocalizedDisplayLabelList(), pd.getDisplayName(),
				pd.getLocalizedDisplayNameList());
		String selected = propName.equals(bulkUpdatePropNm) ? "selected" : "";
%>
<option value="<c:out value="<%=propName%>"/>" <c:out value="<%=selected%>" />><c:out value="<%=displayLabel%>" /></option>
<%
	}
%>
</select>
</td>
</tr>
</tbody>
</table>
</div>
<div>
<table id="id_tbl_bulkupdate" class="tbl-section">
<%
	for (PropertyColumn pc : colMap.values()) {
// 		if (pc.isDispFlag() && (type != OutputType.EDIT || ViewUtil.dispElement(pi))) {
		if (pc.isDispFlag()) {
			request.setAttribute(Constants.ELEMENT, pc);
//				request.setAttribute(Constants.COL_NUM, section.getColNum());
			String path = EntityViewUtil.getJspPath(pc, ViewConst.DESIGN_TYPE_GEM);
			if (path != null) {
%>
<tr id="id_tr_<c:out value="<%=pc.getPropertyName()%>"/>" style="display: none;">
<jsp:include page="<%=path%>" />
</tr>
<%
			}
		}
	}
%>
</table>
</div>
<div class="operation-bar operation-bar_bottom">
<ul class="list_operation edit-bar">
<%
	if (Constants.EXEC_TYPE_UPDATE.equals(execType) && auth.checkPermission(new EntityPermission(defName, EntityPermission.Action.UPDATE))) {
		//ボタンの表示ラベル
		String bulkUpdateDisplayLabel = GemResourceBundleUtil.resourceString("generic.bulk.update");
%>
<li class="btn save-btn"><input id="bulkUpdateBtn" type="button" class="gr-btn" value="<c:out value="<%=bulkUpdateDisplayLabel %>" />" /></li>
<%
	}
%>
<li class="mt05 cancel-link"><a href="javascript:void(0)" onclick="cancel();return false;">${m:rs("mtp-gem-messages", "generic.bulk.cancel")}</a></li> 
</ul>
</div>
<div>

<%
	if (data.getUpdatedProperties() != null) {
%>
<table class="tbl-section">
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "generic.bulk.updatedPropName")}</th>
<th class="section-data w-auto">${m:rs("mtp-gem-messages", "generic.bulk.updatedPropValue")}</th>
</tr>
<%
	for (BulkUpdatedProperty updatedProp : data.getUpdatedProperties()) {
		Integer nth = updatedProp.getNth();
		String updatedPropName = updatedProp.getPropertyName();
		PropertyDefinition pd = defMap.get(updatedPropName);
		PropertyColumn pc = colMap.get(updatedPropName);
		String updatedPropDispName = TemplateUtil.getMultilingualString(pc.getDisplayLabel(), pc.getLocalizedDisplayLabelList(),
				pd.getDisplayName(), pd.getLocalizedDisplayNameList());
		Object updatedPropValue = updatedProp.getPropertyValue();
		String updatedPropDispValue = getPropertyDisplayValue(defMap.get(updatedPropName), updatedPropValue);
%>
<tr>
<th class="section-data col1">
<%=updatedPropDispName%>
<input type="hidden" name="<%=Constants.BULK_UPDATED_PROP_NM%>" value="<%=nth + "_" + updatedPropName%>"/>
</th>
<td class="section-data col1">
<%=updatedPropDispValue%>
<input type="hidden" name="<%=Constants.BULK_UPDATED_PROP_VALUE%>" value="<%=nth + "_" + updatedPropDispValue%>"/>
</td>
</tr>
<% 
		}
%>
</table>
<%
	} 
%>
</div>
</div>
</form>
<script type="text/javascript">
function cancel() {
	$("#modal-dialog-root .modal-close", parent.document).trigger("click");
}
function onDialogClose() {
	var edited = ("<%=StringUtil.isNotBlank(message)%>" == "true");
	if (!edited) return true;
	var func = parent.window.bulkUpdateModalWindowCallback;
	if (func && $.isFunction(func)) {
		var id = "<%= isSelectAll(selectAllType)%>" == "true" ? "all" : eval('[<%=id.toString()%>]');
		func.call(parent.window, id);
	}
	return true;
}
function propChange(obj) {
	var propName = obj.options[obj.selectedIndex].value;
	$("table#id_tbl_bulkupdate tr").each(function() {
		$(this).css("display", "none").val("");
	});
	$("#id_tr_" + propName).css("display", "");
} 
$(function() {
<%	if (isUpdateFailed(bulkUpdatePropNm)) {%>
	//前回で更新に失敗したプロパティに対してエラーメッセージを表示する
	$("#id_tr_<%=bulkUpdatePropNm%>").css("display", "");
<%	} %>
	$("#bulkUpdateBtn").on("click", function(){
		if (!confirm("${m:rs('mtp-gem-messages', 'generic.bulk.updateMsg')}")) {
			return;
		}
		if ($("#sel_<%=Constants.BULK_UPDATE_PROP_NM%>").val() == "") {
			alert("${m:rs('mtp-gem-messages', 'generic.bulk.pleaseSelect')}");
			return;
		}
		$("#detailForm").submit();
	});
	// 一括更新件数
	var countBulkUpdate = parent.window.countBulkUpdate;
	if(countBulkUpdate && $.isFunction(countBulkUpdate)) {
		countBulkUpdate(this, function(count){
			var bulkUpdateItem = "${m:rs('mtp-gem-messages', 'generic.bulk.updateItem')}";
			bulkUpdateItem = bulkUpdateItem.replace("{0}", count);
			$("#bulkUpdateCount").text(bulkUpdateItem);
		});
	} 
})
</script>
