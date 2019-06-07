<%--
 Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
<%@ page import="org.iplass.mtp.entity.definition.properties.*"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.Button" %>
<%@ page import="org.iplass.mtp.view.generic.element.Element" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.property.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.WebRequestConstants" %>
<%@ page import="org.iplass.gem.command.generic.bulk.MultiBulkUpdateFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.bulk.MultiBulkUpdateFormViewData.*" %>
<%@ page import="org.iplass.gem.command.generic.bulk.MultiBulkUpdateListCommand" %>
<%@ page import="org.iplass.gem.command.generic.bulk.MultiBulkUpdateAllCommand" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>

<%!
	boolean isSelectAllPage(Boolean selectAllPage) {
		return selectAllPage != null && selectAllPage;
	}

	boolean isSelectAll(String selectAllType) {
		return "all".equals(selectAllType);
	}

	boolean isFirstSelect(String selectAllType) {
		return StringUtil.isEmpty(selectAllType);
	}

	boolean canBulkUpdate(PropertyItem pi) {
		if (!pi.isDispFlag() || pi.getEditor() instanceof UserPropertyEditor || 
			pi.getEditor() instanceof ExpressionPropertyEditor || pi.getEditor() instanceof AutoNumberPropertyEditor) {
			return false;
		}
		return true;
	}
%>
<%
	//権限確認用
	AuthContext auth = AuthContext.getCurrentContext();

	//コマンドから
	MultiBulkUpdateFormViewData data = (MultiBulkUpdateFormViewData) request.getAttribute(Constants.DATA);
	Boolean selectAllPage = (Boolean) request.getAttribute(Constants.BULK_UPDATE_SELECT_ALL_PAGE);
	String selectAllType = (String) request.getAttribute(Constants.BULK_UPDATE_SELECT_TYPE);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String message = (String) request.getAttribute(Constants.MESSAGE);

	boolean isSelectAllPage = isSelectAllPage(selectAllPage);
	//全選択フラグ
	boolean isSelectAll = isSelectAll(selectAllType);

	OutputType type = OutputType.BULK;
	EntityDefinition ed = data.getEntityDefinition();
	BulkFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();
	//表示名
	String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());

	String contextPath = TemplateUtil.getTenantContextPath();

	String updateAction = "";
	if (StringUtil.isNotBlank(form.getUpdateActionName())) {
		updateAction = contextPath + "/" + form.getUpdateActionName();
	} else {
		updateAction = contextPath + "/" + MultiBulkUpdateListCommand.BULK_UPDATE_ACTION_NAME;
	}

	String updateAllAction = "";
	if (StringUtil.isNotBlank(form.getUpdateAllActionName())) {
		updateAllAction = contextPath + "/" + form.getUpdateAllActionName();
	} else {
		updateAllAction = contextPath + "/" + MultiBulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME;
	}

	String action = isSelectAll ? updateAllAction : updateAction;

	//編集対象情報
	List<String> oids = new ArrayList<String>();
	List<String> versions = new ArrayList<String>();
	List<String> updateDates = new ArrayList<String>();
	// 検索結果一覧チェックを付け直すため
	List<String> id = new ArrayList<String>();
	if (data.getEntries() != null) {
		for (SelectedRowEntity entry : data.getEntries()) {
			oids.add(entry.getRow() + "_" + entry.getEntity().getOid());
			versions.add(entry.getRow() + "_" + entry.getEntity().getVersion());
			updateDates.add(entry.getRow() + "_" + entry.getEntity().getUpdateDate().getTime());
			id.add("\"" + entry.getEntity().getOid() + "_" + entry.getEntity().getVersion() + "\"");
		}
	}

	//各プロパティでの権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.VIEW_NAME, viewName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName); //NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	request.setAttribute(Constants.BULK_UPDATE_USE_BULK_VIEW, true); //BulkView利用フラグ
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());
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

	if (form.isValidJavascriptBulkPage() && StringUtil.isNotBlank(form.getJavaScript())) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%=form.getJavaScript()%><%-- XSS対応-メタの設定のため対応なし(form.getJavaScript) --%>
</script>
<%
	}
%>
<form id="detailForm" method="post" action="<%=action%>">
${m:outputToken('FORM_XHTML', true)}
<%
	if (!isSelectAll) {
%>
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="viewName" value="<c:out value="<%=viewName%>"/>" />
<%
	}
	if(selectAllPage != null) {
%>
<input type="hidden" name="selectAllPage" value="<c:out value="<%=selectAllPage%>"/>" />
<%
	}
	if(selectAllType != null) {
%>
<input type="hidden" name="selectAllType" value="<c:out value="<%=selectAllType%>"/>" />
<%
	}
	if(searchCond != null) {
%>
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>" />
<%
	}
	if (!isSelectAll && oids != null && oids.size() > 0) {
		for (String  oid : oids) {
%>
<input type="hidden" name="oid" value="<c:out value="<%=oid%>"/>" />
<%
		}
	}
	if (!isSelectAll && updateDates != null && updateDates.size() > 0) {
		for (String updateDate : updateDates) {
%>
<input type="hidden" name="timestamp" value="<c:out value="<%=updateDate%>"/>" />
<%
		}
	}
	if (!isSelectAll && versions != null && versions.size() > 0) {
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
	if (isSelectAllPage) {
%>
<tr>
<th rowspan="2">${m:rs("mtp-gem-messages", "generic.bulk.selectBulkUpdateType")}</th>
<td><label><input type="radio" name="selectAllType" value="select" <%=!isSelectAll ? "checked" : ""%>>${m:rs("mtp-gem-messages", "generic.bulk.updateRow")}</label></td>
</tr>
<tr>
<td><label><input type="radio" name="selectAllType" value="all" <%=isSelectAll ? "checked" : ""%>>${m:rs("mtp-gem-messages", "generic.bulk.updateAll")}<span id="bulkUpdateCount"></span></label></td>
</tr>
<script>
$(function() {
	var $radio = $(":radio[name='selectAllType']");
	var createSearchCondParams = function() {
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
	};

	$("#detailForm").on("submit", createSearchCondParams);

	$radio.on("change", function(){
		if ($(this).val() == "all") {
			$("#detailForm").attr("action", "<%=StringUtil.escapeJavaScript(updateAllAction) %>").on("submit", createSearchCondParams);
		} else {
			$("#detailForm").attr("action", "<%=StringUtil.escapeJavaScript(updateAction) %>").off("submit", createSearchCondParams);
		}
	});
	//二回目以降は選択不可にする
	if ("<%=isFirstSelect(selectAllType)%>" == "false") {
		$radio.prop("disabled", true);
	}
})
</script>
<%
	}
%>
</tbody>
</table>
</div>
<%
	for (Section section : form.getSections()) {
		if (!section.isDispFlag()) continue;
		request.setAttribute(Constants.ELEMENT, section);

		String path = EntityViewUtil.getJspPath(section, ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
%>
<jsp:include page="<%=path %>" />
<%
		}
	}
%>
<div class="operation-bar operation-bar_bottom">
<ul class="list_operation edit-bar">
<%
	if (form.getButtons().size() > 0) {
		for (Button button : form.getButtons()) {
			if (button.isDispFlag()) {
				String cssClass = button.isPrimary() ? "gr-btn" : "gr-btn-02";
				if (StringUtil.isNotBlank(button.getStyle())) {
					cssClass = button.getStyle();
				}
				String customStyle = "";
				if (StringUtil.isNotBlank(button.getInputCustomStyle())) {
					customStyle = EntityViewUtil.getCustomStyle(defName, form.getScriptKey(), button.getInputCustomStyleScriptKey(), null, null);
				}
				String displayLabel = TemplateUtil.getMultilingualString(button.getDisplayLabel(), button.getLocalizedDisplayLabelList());
%>
<%-- XSS対応-メタの設定のため対応なし(button.getOnclickEvent()) --%>
<li class="btn"><input class="<c:out value="<%=cssClass %>"/>" style="<c:out value="<%=customStyle %>"/>" type="button" value="<c:out value="<%=displayLabel %>"/>" onclick="<%=button.getOnclickEvent() %>" /></li>
<%
			}
		}
	}

	if (auth.checkPermission(new EntityPermission(defName, EntityPermission.Action.UPDATE))) {
		//ボタンの表示ラベル
		String bulkUpdateDisplayLabel = GemResourceBundleUtil.resourceString("generic.bulk.update");
		String localizedUpdateDisplayLabel = TemplateUtil.getMultilingualString(form.getUpdateDisplayLabel(), form.getLocalizedUpdateDisplayLabelList());
		if (StringUtil.isNotBlank(localizedUpdateDisplayLabel)) {
			bulkUpdateDisplayLabel = localizedUpdateDisplayLabel;
		}
%>
<li class="btn save-btn"><input id="bulkUpdateBtn" type="button" class="gr-btn" value="<c:out value="<%=bulkUpdateDisplayLabel %>" />" onclick="onclick_bulkupdate(this)" /></li>
<%
	}
%>
<li class="mt05 cancel-link"><a href="javascript:void(0)" onclick="onclick_cancel()">${m:rs("mtp-gem-messages", "generic.bulk.cancel")}</a></li>
</ul>
</div>
</div>
</form>
<script type="text/javascript">
function onclick_cancel() {
	$("#modal-dialog-root .modal-close", parent.document).trigger("click");
}
function onclick_bulkupdate(target){
	$(target).prop("disabled", true);
	$("#detailForm").submit();
}
function onDialogClose() {
	var edited = ("<%=StringUtil.isNotBlank(message)%>" == "true");
	if (!edited) return true;
	var func = parent.document.scriptContext["bulkUpdateModalWindowCallback"];
	if (func && $.isFunction(func)) {
		var id = "<%=isSelectAll%>" == "true" ? "all" : <%=Arrays.toString(id.toArray())%>;
		func.call(parent.window, id);
	}
	return true;
}
$(function() {
	// タイトルの設定
	$("#modal-title", parent.document).text("<%=displayName%>");<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<%
	if (form.isDialogMaximize()) {
%>
	$("#modal-dialog-root .modal-maximize", parent.document).click();
<%
	}
%>
	// 一括更新件数
	var func = parent.document.scriptContext["countBulkUpdate"];
	if(func && $.isFunction(func)) {
		func(this, function(count){
			var bulkUpdateItem = "${m:rs('mtp-gem-messages', 'generic.bulk.updateItem')}";
			bulkUpdateItem = bulkUpdateItem.replace("{0}", count);
			$("#bulkUpdateCount").text(bulkUpdateItem);
			// 検索条件で一括更新した後、該当するデータの件数が0件になった場合、一括更新ボタンを無効化にします。
			if ("" + count === "0") {
				$("#bulkUpdateBtn").prop("disabled", true);
			}
		});
	} 
})
</script>
