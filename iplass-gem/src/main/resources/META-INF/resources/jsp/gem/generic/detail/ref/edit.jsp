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

<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.*"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ConsumeTokenCommand"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%
	String modalTarget = request.getParameter(Constants.MODAL_TARGET);
	modalTarget = StringUtil.escapeHtml(modalTarget);
	if (modalTarget == null) modalTarget = "";

	String parentDefName = request.getParameter(Constants.PARENT_DEFNAME);
	parentDefName = StringUtil.escapeHtml(parentDefName);
	if (parentDefName == null) parentDefName = "";

	String parentViewName = request.getParameter(Constants.PARENT_VIEWNAME);
	parentViewName = StringUtil.escapeHtml(parentViewName);
	if (parentViewName == null) parentViewName = "";

	String parentPropName = request.getParameter(Constants.PARENT_PROPNAME);
	parentPropName = StringUtil.escapeHtml(parentPropName);
	if (parentPropName == null) parentPropName = "";

	String entityOid = request.getParameter(Constants.DISPLAY_SCRIPT_ENTITY_OID);
	entityOid = StringUtil.escapeHtml(entityOid);
	if (entityOid == null) entityOid = "";

	String entityVersion = request.getParameter(Constants.DISPLAY_SCRIPT_ENTITY_VERSION);
	entityVersion = StringUtil.escapeHtml(entityVersion);
	if (entityVersion == null) entityVersion = "";

	String viewType = request.getParameter(Constants.VIEW_TYPE);
	viewType = StringUtil.escapeHtml(viewType);
	if (viewType == null) viewType = "";

	String refSectionIndex = request.getParameter(Constants.REF_SECTION_INDEX);
	refSectionIndex = StringUtil.escapeHtml(refSectionIndex);
	if (refSectionIndex == null) refSectionIndex = "";

	//コマンドから
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	String message = (String) request.getAttribute(Constants.MESSAGE);

	OutputType type = OutputType.EDIT;
	DetailFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();

	//表示名
	String displayName = TemplateUtil.getMultilingualString(data.getView().getTitle(), data.getView().getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	//新規or更新
	String execType = data.getExecType();
	//数値のカンマ区切りのセパレータ
	String separator = ViewUtil.getGroupingSeparator();
	//css用クラス
	String className = defName.replaceAll("\\.", "_");

	//編集対象
	String oid = null;
	Long updateDate = null;
	Long version = null;
	if (data.getEntity() != null) {
		oid = data.getEntity().getOid();
		version = data.getEntity().getVersion();
		if (data.getEntity().getUpdateDate() != null) {
			updateDate = data.getEntity().getUpdateDate().getTime();
		}

		//参照プロパティで利用
		request.setAttribute(Constants.EDITOR_PARENT_ENTITY, data.getEntity());
	}

	//権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName);	//NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	Entity rootEntity = null;
	//更新の時のみ、エンティティを設定します。新規の場合はnullを設定します。
	if (Constants.EXEC_TYPE_UPDATE.equals(execType)) {
		rootEntity = data.getEntity();
	}
	//新規時に初期化スクリプト定義が存在する場合、data.getEntity()がnullにならないので、nullに統一するために別のキー名としてスコープに保持しなければなりません。。
	request.setAttribute(Constants.ROOT_ENTITY, rootEntity); //NestTableの場合に内部の表示判定スクリプトで利用

	//editor以下で参照するパラメータ
	request.setAttribute(Constants.VIEW_NAME, viewName);

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	request.setAttribute(Constants.ENTITY_DATA, data.getEntity());
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());
	request.setAttribute(Constants.EXEC_TYPE, execType);

	//ボタンのカスタムスタイル用のscriptKey
	request.setAttribute(Constants.FORM_SCRIPT_KEY, form.getScriptKey());
%>
<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_detail detail_edit d_<c:out value="<%=className %>"/>">
<%@include file="../../../layout/resource/mediaelementResource.inc.jsp" %>
<script type="text/javascript">
separator = "<%= separator %>";
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
<%if (form.isDialogMaximize()) {%>
if (!modalTarget) {
	$("#modal-dialog-root .modal-maximize", parent.document).click();
} else {
	$("#modal-dialog-" + modalTarget + " .modal-maximize", parent.document).click();
}
<%}%>
$(function() {
	if (!modalTarget) {
		$("#modal-title", parent.document).text("<%=displayName%>");<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
	} else {
		$("#modal-title-" + modalTarget, parent.document).text("<%=displayName%>");<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
	}
});
function onclick_save(action, target) {
	if ($("#confirmEditSave").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.ref.detail.saveMsg')}")) {
		return;
	}
	button_onclick(action);
	if (target) $(target).prop("disabled", true);
}
function onclick_insert(action, target) {
	if ($("#confirmEditSave").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.ref.detail.saveMsg')}")) {
		return;
	}
	button_onclick(action);
	if (target) $(target).prop("disabled", true);
}
function button_onclick(action, hidden) {
	var $form = $("#detailForm");
	if (hidden) {
		if ($form) {
			for (var keyString in hidden) {
				$form.prepend("<input type='hidden\' name='" + keyString + "' value='" + hidden[keyString] + "'/>");
			}
		}
	}
	$form.attr("action", contextPath + "/" + action).submit();
}
function onDialogClose() {
	if ($("#confirmEditCancel").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.ref.detail.cancelMsg')}")) {
		return false;
	}
	consumeToken("<%=ConsumeTokenCommand.WEBAPI_NAME%>", $("[name='_t']").val());
	return true;
}
$(function(){
	$('.disabled-btn').removeAttr('disabled');
});
</script>
<%
	if (form.isValidJavascriptDetailPage() && StringUtil.isNotBlank(form.getJavaScript())) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%=form.getJavaScript()%><%-- XSS対応-メタの設定のため対応なし(form.getJavaScript) --%>
</script>
<%
	}
%>
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.detail.ref.detail.edit", displayName) %></h3>
<%
	if (StringUtil.isNotBlank(message)) {
%>
<span class="error">
<c:out value="<%= message %>"/>
</span>
<%
	}
%>
<form id="detailForm" method="post" action="">
<input type="hidden" name="<%=Constants.DEF_NAME%>" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="<%=Constants.MODAL_TARGET%>" value="<c:out value="<%=modalTarget%>"/>" />
<input type="hidden" name="<%=Constants.EXEC_TYPE%>" value="<c:out value="<%=execType%>"/>" />
<input type="hidden" name="<%=Constants.PARENT_DEFNAME%>" value="<c:out value="<%=parentDefName%>" />" />
<input type="hidden" name="<%=Constants.PARENT_VIEWNAME%>" value="<c:out value="<%=parentViewName%>" />" />
<input type="hidden" name="<%=Constants.PARENT_PROPNAME%>" value="<c:out value="<%=parentPropName%>" />" />
<input type="hidden" name="<%=Constants.DISPLAY_SCRIPT_ENTITY_OID%>" value="<c:out value="<%=entityOid%>" />" />
<input type="hidden" name="<%=Constants.DISPLAY_SCRIPT_ENTITY_VERSION%>" value="<c:out value="<%=entityVersion%>" />" />
<input type="hidden" name="<%=Constants.VIEW_TYPE%>" value="<c:out value="<%=viewType%>" />" />
<input type="hidden" name="<%=Constants.REF_SECTION_INDEX%>" value="<c:out value="<%=refSectionIndex%>" />" />
<%
	if (oid != null) {
%>
<input type="hidden" name="<%=Constants.OID%>" value="<c:out value="<%=oid%>"/>" />
<%
	}
	if (updateDate != null) {
%>
<input type="hidden" name="<%=Constants.TIMESTAMP%>" value="<c:out value="<%=updateDate%>"/>" />
<%
	}
	if (version != null) {
%>
<input type="hidden" name="<%=Constants.VERSION%>" value="<c:out value="<%=version%>"/>" />
<%
	}
%>
<input type="hidden" id="confirmEditSave" value="<%=ViewUtil.isConfirmEditSave()%>" />
<input type="hidden" id="confirmEditCancel" value="<%=ViewUtil.isConfirmEditCancel()%>" />
<%-- ページトップ部分のボタン --%>
<div class="operation-bar operation-bar_top">
<jsp:include page="editButton.inc.jsp" />

<ul class="nav-disc-all">
<li class="all-open"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.ref.detail.allOpen")}</a></li>
<li class="all-close"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.ref.detail.allClose")}</a></li>
</ul>
</div><!--operation-bar-->
<%
	request.setAttribute(Constants.NAV_SECTION, data.getView().getSections());
%>
<jsp:include page="../sectionNavi.inc.jsp" />
<%
	for (Section section : data.getView().getSections()) {
		if (!EntityViewUtil.isDisplayElement(defName, section.getElementRuntimeId(), OutputType.EDIT, rootEntity)
				|| !ViewUtil.dispElement(section)) {
			continue;
		}
		request.setAttribute(Constants.ELEMENT, section);

		String path = EntityViewUtil.getJspPath(section, ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
%>
<jsp:include page="<%=path %>" />
<%
		}
	}
%>

<%-- ページボトム部分のボタン --%>
<div class="operation-bar operation-bar_bottom">
<jsp:include page="editButton.inc.jsp" />
</div><!--operation-bar-->
${m:outputToken('FORM_XHTML', true)}
</form>
</div>
