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
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.Element" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.property.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.WebRequestConstants" %>
<%@ page import="org.iplass.gem.command.generic.detail.BulkDetailFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.detail.BulkDetailFormViewData.*" %>
<%@ page import="org.iplass.gem.command.generic.detail.BulkUpdateListCommand" %>
<%@ page import="org.iplass.gem.command.generic.detail.BulkUpdateAllCommand" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
	//権限確認用
	AuthContext auth = AuthContext.getCurrentContext();

	//コマンドから
	BulkDetailFormViewData data = (BulkDetailFormViewData) request.getAttribute(Constants.DATA);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String searchType = (String) request.getAttribute(Constants.SEARCH_TYPE);
	String message = (String) request.getAttribute(Constants.MESSAGE);
	String bulkUpdatePropNm = (String) request.getAttribute(Constants.BULK_UPDATE_PROP_NM);

	OutputType type = OutputType.BULKEDIT;
	String contextPath = TemplateUtil.getTenantContextPath();
	EntityDefinition ed = data.getEntityDefinition();
	DetailFormView form = data.getView();

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
	if (data.getEntries() != null) {
		for (SelectedRowEntityEntry entry : data.getEntries()) {
			oids.add(entry.getRow() + "_" + entry.getEntity().getOid());
			versions.add(entry.getRow() + "_" + entry.getEntity().getVersion());
			updateDates.add(entry.getRow() + "_" + entry.getEntity().getUpdateDate().getTime());
		}
	}

	// プロパティリスト
	List<PropertyItem> properties = ViewUtil.collectNestedPropertyItem(form.getSections());
	// Property情報
	Map<String, PropertyDefinition> defMap = new HashMap<String, PropertyDefinition>();
	for (PropertyItem pi : properties) {
		if (pi.isBlank()) continue;
		String propName = pi.getPropertyName();
		PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);
		defMap.put(propName, pd);
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.detail.bulk.pleaseSelect");
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
<h3 class="hgroup-02 hgroup-02-01"><%=GemResourceBundleUtil.resourceString("generic.detail.bulk.title", displayName)%></h3>
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
<%	if (searchCond != null) { %>
<form id="detailForm" method="post" action="<%=contextPath + "/" + BulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME%>" >
<%	
	} else { 
%>
<form id="detailForm" method="post" action="<%=contextPath + "/" + BulkUpdateListCommand.BULK_UPDATE_ACTION_NAME%>" >
<%	} %>
${m:outputToken('FORM_XHTML', true)}
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="execType" value="<c:out value="<%=execType%>"/>" />
<%
	if(searchCond != null) {
%>
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>" />
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
<p class="btn">
<select id="sel_<c:out value="<%=Constants.BULK_UPDATE_PROP_NM%>"/>" name="<c:out value="<%=Constants.BULK_UPDATE_PROP_NM%>"/>" class="inpbr form-size-11" onchange="propChange(this)">
<option value="" selected="selected"><%= pleaseSelectLabel %></option>
<%
	for (PropertyItem pi : properties) {
		if (pi.isBlank() || !ViewUtil.isBulkUpdatable(pi)) continue;
		String propName = pi.getPropertyName();
		//		PropertyDefinition pd = ed.getProperty(propName);
		PropertyDefinition pd = defMap.get(propName);
		String displayLabel = TemplateUtil.getMultilingualString(pi.getDisplayLabel(), pi.getLocalizedDisplayLabelList(), pd.getDisplayName(),
				pd.getLocalizedDisplayNameList());
		if (!pi.isDispFlag() || pi.isHideDetailCondition())
			continue;
		String optClass = pi.isRequiredDetail() ? "required" : "";
		String selected = propName.equals(bulkUpdatePropNm) ? "selected" : "";
%>
<option value="<c:out value="<%=propName%>"/>" class="<c:out value="<%=optClass%>" />" <c:out value="<%=selected%>" />><c:out value="<%=displayLabel%>" /></option>
<%
	}
%>
</select>
</p>
<div>
<table class="tbl-section">
<%
	for (PropertyItem pi : properties) {
		if (pi.isDispFlag() && (type != OutputType.EDIT || ViewUtil.dispElement(pi))) {
			if (!ViewUtil.isBulkUpdatable(pi)) continue;
			request.setAttribute(Constants.ELEMENT, pi);
//				request.setAttribute(Constants.COL_NUM, section.getColNum());
			String path = EntityViewUtil.getJspPath(pi, ViewConst.DESIGN_TYPE_GEM);
			if (path != null) {
%>
<tr id="id_tr_<c:out value="<%=pi.getPropertyName()%>"/>" style="display: none;">
<jsp:include page="<%=path %>" />
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
		String bulkUpdateDisplayLabel = GemResourceBundleUtil.resourceString("generic.detail.bulk.update");
%>
<li class="btn save-btn"><input id="bulkUpdateBtn" type="button" class="gr-btn" value="<c:out value="<%=bulkUpdateDisplayLabel %>" />" /></li>
<%
	}
%>
<li class="mt05 cancel-link"><a href="javascript:void(0)" onclick="cancel();return false;">${m:rs("mtp-gem-messages", "generic.detail.bulk.cancel")}</a></li> 
</ul>
</div>
</div>
</form>
<script type="text/javascript">
function cancel() {
	$("#modal-dialog-root .modal-close", parent.document).click();
}
function onDialogClose() {
	var edited = ("<%=StringUtil.isNotBlank(message)%>" == "true");
	if (!edited) return true;
	var func = parent.window.bulkUpdateModalWindowCallback;
	if (func && $.isFunction(func)) {
		func.call(parent.window);
	}
	return true;
}
function propChange(obj) {
	var propName = obj.options[obj.selectedIndex].value;
	$("table.tbl-section tr").each(function() {
		$(this).css("display", "none").val("");
	});
	$("#id_tr_" + propName).css("display", "");
}
$(function() {
	// 前一括更新画面で更新されたプロパティ
	$("table.tbl-section tr").each(function(){
		$("#id_tr_<%=bulkUpdatePropNm%>").css("display", "");
	});
	$("#bulkUpdateBtn").on("click", function(){
		if ($("#sel_<%=Constants.BULK_UPDATE_PROP_NM%>").val() == "") {
			alert("${m:rs('mtp-gem-messages', 'generic.detail.bulk.pleaseSelect')}");
			return;
		}
		if (!confirm("${m:rs('mtp-gem-messages', 'generic.detail.bulk.bulkUpdateMsg')}")) {
			return;
		}
		var $form = $("#detailForm");
		var searchCond = $(":hidden[name='searchCond']").val();
		// 検索条件を元に一括更新
		if (searchCond && searchCond.length > 0) {
			var params = parseSearchCond(searchCond);
			if (params.length > 0) {
				for (var i = 0; i < params.length; i++) {
					var param = params[i];
					if ($(":hidden[name=" + param.key + "]").length > 0) {
						$(":hidden[name=" + param.key + "]").val(param.val);
					} else {
						$("<input />").attr({type:"hidden", name:param.key, value:param.val}).appendTo($form);
					}
				}
			}
		}
		$form.submit();
	});
})
</script>
