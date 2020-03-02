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

<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.entity.definition.VersionControlType"%>
<%@ page import="org.iplass.mtp.entity.query.*"%>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.*"%>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.*"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.EntityManager"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.*"%>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView.CopyTarget"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.LockCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UnlockCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	boolean hasLock(String lockId) {
		String userOid = AuthContext.getCurrentContext().getUser().getOid();
		return lockId != null ? userOid.equals(lockId) : false;
	}
%>
<%
	String refEdit = request.getParameter(Constants.REF_EDIT);
	refEdit = StringUtil.escapeHtml(refEdit);
	if (refEdit == null) refEdit = "";
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

	OutputType type = OutputType.VIEW;
	String contextPath = TemplateUtil.getTenantContextPath();
	DetailFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();
	if (viewName == null) viewName = "";

	//表示名
	String displayName = TemplateUtil.getMultilingualString(data.getView().getTitle(), data.getView().getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	//css用クラス
	String className = defName.replaceAll("\\.", "_");
	//編集可否
	boolean isEditable = "true".equals(refEdit);

	//表示対象
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

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//編集アクション
	String edit = "";
	if (StringUtil.isNotBlank(form.getRefEditActionName())) {
		edit = contextPath + "/" + form.getRefEditActionName() + urlPath;
	} else {
		edit = contextPath + "/" + DetailViewCommand.REF_DETAIL_ACTION_NAME + urlPath;
	}

	//各プロパティでの権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName);	//NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット
	//新規時に初期化スクリプト定義が存在する場合、data.getEntity()がnullにならないので、nullに統一するために別のキー名としてスコープに保持しなければなりません。。
	request.setAttribute(Constants.ROOT_ENTITY, data.getEntity()); //NestTableの場合に内部の表示判定スクリプトで利用

	//editor以下で参照するパラメータ
	request.setAttribute(Constants.VIEW_NAME, viewName);

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	request.setAttribute(Constants.ENTITY_DATA, data.getEntity());
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());

	//ボタンのカスタムスタイル用のscriptKey
	request.setAttribute(Constants.FORM_SCRIPT_KEY, form.getScriptKey());
%>
<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_detail detail_view d_<c:out value="<%=className %>"/>">
<%@include file="../../../layout/resource/mediaelementResource.jsp" %>
<script type="text/javascript">
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
var editable = "<%=isEditable%>";
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
function onclick_copy() {
	var copyTarget = $(":hidden[name='_copyTarget']").val();
	if (copyTarget == "Both") {
		$("#selectCopyTargetDialog").dialog("open");
	} else {
		var $form = $("#detailForm");
		$form.append("<input type='hidden' name='copy' value='true' />");
		$("#detailForm").submit();
	}
}
function onclick_newversion() {
	var $form = $("#detailForm");
	$form.append("<input type='hidden' name='newversion' value='true' />");
	$form.submit();
}
function viewref(defName, oid, action) {
	$(":hidden[name='defName']").val(defName);
	$(":hidden[name='oid']").val(oid);
	$("#detailForm").attr("action", action).submit();
}
function dataLock() {
	var defName = $(":hidden[name='defName']").val();
	var viewName = "<%=StringUtil.escapeJavaScript(viewName)%>";
	var oid = $(":hidden[name='oid']").val();
	var token = "${m:fixToken()}";
	_lock("<%=LockCommand.WEBAPI_NAME%>", defName, viewName, oid, token, function(lockResult) {
		if (lockResult) {
			$(":button[name='lock']").hide();
			$(":button[name='unlock']").show();
		} else {
			alert("${m:rs('mtp-gem-messages', 'generic.detail.ref.view.failLock')}");
		}
	});
}
function dataUnlock() {
	var defName = $(":hidden[name='defName']").val();
	var viewName = "<%=StringUtil.escapeJavaScript(viewName)%>";
	var oid = $(":hidden[name='oid']").val();
	var token = "${m:fixToken()}";
	_lock("<%=UnlockCommand.WEBAPI_NAME%>", defName, viewName, oid, token, function(lockResult) {
		if (lockResult) {
			$(":button[name='unlock']").hide();
			$(":button[name='lock']").show();
			$("input.updatable-button").show();
			$("span.error").remove();
		} else {
			alert("${m:rs('mtp-gem-messages', 'generic.detail.ref.view.failRelease')}");
		}
	});
}
</script>
<%
	if (form.isValidJavascriptViewPage() && StringUtil.isNotBlank(form.getJavaScript())) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%-- XSS対応-メタの設定のため対応なし(form.getJavaScript()) --%>
<%=form.getJavaScript() %>
</script>
<%
	}
%>
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.detail.ref.view.show", displayName) %></h3>
<%
	if (StringUtil.isNotBlank(message)) {
%>
<span class="error">
<c:out value="<%= message %>"/>
</span>
<%
	}
%>
<form id="detailForm" method="post" action="<c:out value="<%=edit %>"/>">
<input type="hidden" name="<%=Constants.DEF_NAME%>" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="<%=Constants.REF_EDIT%>" value="<c:out value="<%=refEdit%>"/>" />
<input type="hidden" name="<%=Constants.MODAL_TARGET%>" value="<c:out value="<%=modalTarget%>"/>" />
<input type="hidden" name="<%=Constants.PARENT_DEFNAME%>" value="<c:out value="<%=parentDefName%>" />" />
<input type="hidden" name="<%=Constants.PARENT_VIEWNAME%>" value="<c:out value="<%=parentViewName%>" />" />
<input type="hidden" name="<%=Constants.PARENT_PROPNAME%>" value="<c:out value="<%=parentPropName%>" />" />
<input type="hidden" name="<%=Constants.DISPLAY_SCRIPT_ENTITY_OID%>" value="<c:out value="<%=entityOid%>" />" />
<input type="hidden" name="<%=Constants.DISPLAY_SCRIPT_ENTITY_VERSION%>" value="<c:out value="<%=entityVersion%>" />" />
<input type="hidden" name="<%=Constants.VIEW_TYPE%>" value="<c:out value="<%=viewType%>" />" />
<input type="hidden" name="<%=Constants.REF_SECTION_INDEX %>" value="<c:out value="<%=refSectionIndex%>" />" />
<%	if (oid != null) { %>
<input type="hidden" name="<%=Constants.OID%>" value="<c:out value="<%=oid%>"/>" />
<%	}
	if (updateDate != null) {
%>
<input type="hidden" name="<%=Constants.TIMESTAMP%>" value="<c:out value="<%=updateDate%>"/>" />
<%	}
	if (version != null) {
%>
<input type="hidden" name="<%=Constants.VERSION%>" value="<c:out value="<%=version%>"/>" />
<%	} %>

<%-- ページトップ部分のボタン --%>
<div class="operation-bar operation-bar_top">
<jsp:include page="viewButton.inc.jsp" />

<ul class="nav-disc-all">
<li class="all-open"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.ref.view.allOpen")}</a></li>
<li class="all-close"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.ref.view.allClose")}</a></li>
</ul>
</div><!--operation-bar-->
<%
	request.setAttribute(Constants.NAV_SECTION, data.getView().getSections());
%>
<jsp:include page="../sectionNavi.inc.jsp" />
<%
	for (Section section : data.getView().getSections()) {
		if (!EntityViewUtil.isDisplayElement(defName, section.getElementRuntimeId(), OutputType.VIEW, data.getEntity())) continue;
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
<jsp:include page="viewButton.inc.jsp" />
</div><!--operation-bar-->
</form>
</div>

<div id="selectCopyTargetDialog" title="${m:rs('mtp-gem-messages', 'generic.detail.ref.view.selectCopy')}" style="display:none;">
<ul class="list-input-01 col">
<li><label><input type="radio" name="selCopyTarget" value="Shallow" />${m:rs("mtp-gem-messages", "generic.detail.ref.view.entityCopy")}</label></li>
<li><label><input type="radio" name="selCopyTarget" value="Deep" />${m:rs("mtp-gem-messages", "generic.detail.ref.view.bulkCopy")}</label></li>
</ul>
</div>
<script type="text/javascript">
$(function() {
	$("#selectCopyTargetDialog").dialog({
		autoOpen: false,
		modal: true,
		resizable: false,
		width:350,
		buttons: {
			"OK": function() {
				var copyTarget = $(":radio[name='selCopyTarget']:checked").val();
				$(":hidden[name='copyTarget']").val(copyTarget);
				$(this).dialog("close");
				var $form = $("#detailForm");
				$form.append("<input type='hidden' name='copy' value='true' />");
				$("#detailForm").submit();
			},
			"${m:rs('mtp-gem-messages', 'generic.detail.ref.view.cancel')}": function() { $(this).dialog("close");}
		}
	});
	$("#selectCopyTargetDialog").on("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
});
</script>
