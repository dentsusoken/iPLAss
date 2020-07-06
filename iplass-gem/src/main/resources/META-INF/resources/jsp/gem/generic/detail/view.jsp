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

<%@ page import="java.util.HashMap"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.EntityManager"%>
<%@ page import="org.iplass.mtp.entity.query.Query"%>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.And"%>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.Equals"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView"%>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView.CopyTarget"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.Section"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.LockCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UnlockCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>

<%
	String backPath = request.getParameter(Constants.BACK_PATH);
	String topViewListOffset = request.getParameter(Constants.TOPVIEW_LIST_OFFSET);
	if (topViewListOffset == null) {topViewListOffset = "";}

	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String message = (String) request.getAttribute(Constants.MESSAGE);

	OutputType type = OutputType.VIEW;
	String contextPath = TemplateUtil.getTenantContextPath();
	DetailFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();
	if (viewName == null) viewName = "";


	//表示名
	String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	//css用クラス
	String className = defName.replaceAll("\\.", "_");
	//タイトルのアイコン
	String imageColor = ViewUtil.getEntityImageColor(form);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String iconTag = ViewUtil.getIconTag(form);

	//コピー対象
	CopyTarget copyTarget = form.getCopyTarget();
	if (copyTarget == null) copyTarget = CopyTarget.SHALLOW;

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
	if (StringUtil.isNotBlank(form.getEditActionName())) {
		edit = contextPath + "/" + form.getEditActionName() + urlPath;
	} else {
		edit = contextPath + "/" + DetailViewCommand.DETAIL_ACTION_NAME + urlPath;
	}

	//キャンセルアクション
	String cancel = "";
	if (StringUtil.isEmpty(backPath)) {
		if (StringUtil.isNotBlank(form.getCancelActionName())) {
			cancel = form.getCancelActionName() + urlPath;
		} else {
			cancel = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;
		}
	} else {
		cancel = backPath;
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
<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<%if (StringUtil.isNotBlank(iconTag)) {%>
<%=iconTag%>
<%} else {%>
<i class="far fa-circle default-icon"></i>
<%} %>
</span>
<c:out value="<%= displayName %>"/>
</h2>
<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_detail detail_view v_<c:out value="<%=className %>"/>">
<%@include file="../../layout/resource/mediaelementResource.inc.jsp" %>
<script type="text/javascript">
function onclick_submit() {
	var $form = $("#detailForm");
	$form.append("<input type='hidden' name='fromView' value='true' />");
	$form.submit();
}
function onclick_copy() {
	var copyTarget = $(":hidden[name='_copyTarget']").val();
	if (copyTarget == "Both") {
		$("#selectCopyTargetDialog").dialog("open");
	} else {
		if (copyTarget == "Deep" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.view.copyMsg')}")) {
			return;
		}
		var $form = $("#detailForm");
		$form.append("<input type='hidden' name='copy' value='true' />");
		$form.submit();
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
function cancel() {
	submitForm(contextPath + "/<%=StringUtil.escapeJavaScript(cancel)%>", {
		<%=Constants.SEARCH_COND%>:$(":hidden[name='searchCond']").val(),
		<%=Constants.TOPVIEW_LIST_OFFSET%>:"<%=StringUtil.escapeJavaScript(topViewListOffset)%>"
	});
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
			alert("${m:rs('mtp-gem-messages', 'generic.detail.view.failLock')}");
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
			alert("${m:rs('mtp-gem-messages', 'generic.detail.view.failRelease')}");
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
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.detail.view.show", displayName) %></h3>
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
${m:outputToken('FORM_XHTML', true)}
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>" />
<input type="hidden" name="_copyTarget" value="<c:out value="<%=copyTarget.value()%>"/>" />
<input type="hidden" name="copyTarget" value="<c:out value="<%=copyTarget.value()%>"/>" />
<input type="hidden" name="backPath" value="<c:out value="<%=backPath%>"/>" />
<input type="hidden" name="topViewListOffset" value="<c:out value="<%=topViewListOffset%>"/>" />
<%
	if (oid != null) {
%>
<input type="hidden" name="oid" value="<c:out value="<%=oid%>"/>" />
<%
	}
	if (updateDate != null) {
%>
<input type="hidden" name="timestamp" value="<c:out value="<%=updateDate%>"/>" />
<%
	}
	if (version != null) {
%>
<input type="hidden" name="version" value="<c:out value="<%=version%>"/>" />
<%
	}
%>
<%-- ページトップ部分のボタン --%>
<div class="operation-bar operation-bar_top">
<jsp:include page="viewButton.inc.jsp" />

<ul class="nav-disc-all">
<li class="all-open"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.view.allOpen")}</a></li>
<li class="all-close"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.view.allClose")}</a></li>
</ul>
</div><!--operation-bar-->
<%
	request.setAttribute(Constants.NAV_SECTION, form.getSections());
%>
<jsp:include page="sectionNavi.inc.jsp" />
<%
	for (Section section : form.getSections()) {
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

<div id="selectCopyTargetDialog" title="${m:rs('mtp-gem-messages', 'generic.detail.view.selectCopy')}" style="display:none;">
<ul class="list-input-01 col">
<li><label><input type="radio" name="selCopyTarget" value="Shallow" />${m:rs("mtp-gem-messages", "generic.detail.view.entityCopy")}</label></li>
<li><label><input type="radio" name="selCopyTarget" value="Deep" />${m:rs("mtp-gem-messages", "generic.detail.view.bulkCopy")}</label></li>
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
				if (copyTarget == "Deep" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.view.copyMsg')}")) {
					$(this).dialog("close");
					return;
				}
				$(this).dialog("close");
				var $form = $("#detailForm");
				$form.append("<input type='hidden' name='copy' value='true' />");
				$("#detailForm").submit();
			},
			"${m:rs('mtp-gem-messages', 'generic.detail.view.cancel')}": function() { $(this).dialog("close");}
		}
	});
	$("#selectCopyTargetDialog").on("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
});
</script>
