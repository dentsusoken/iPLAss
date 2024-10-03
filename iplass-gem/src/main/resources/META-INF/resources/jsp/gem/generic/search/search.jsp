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

<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.Section"%>
<%@ page import="org.iplass.mtp.view.generic.element.Button"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.upload.EntityFileUploadIndexCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String contextPath = TemplateUtil.getTenantContextPath();

	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);
	EntityDefinition ed = data.getEntityDefinition();
	SearchFormView view = data.getView();

	String defName = ed.getName();
	String viewName = view.getName();

	boolean isHideNew = view.isHideNew();
	boolean isHideCsvUpload = view.getCondSection().isHideCsvUpload();

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//詳細表示アクション
	String detail = "";
	if (StringUtil.isNotBlank(view.getNewActionName())) {
		detail = view.getNewActionName() + urlPath;
	} else {
		detail = DetailViewCommand.DETAIL_ACTION_NAME + urlPath;
	}

	//CSVUploadアクション
	String csvUpload = "";
	if (StringUtil.isNotBlank(view.getViewUploadActionName())) {
		csvUpload = view.getViewUploadActionName() + urlPath;
	} else {
		csvUpload = EntityFileUploadIndexCommand.ACTION_NAME + urlPath;
	}

	//表示名
	String displayName = TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
	String viewTitle = TemplateUtil.getMultilingualString(view.getTitle(), view.getLocalizedTitleList());
	if(StringUtil.isNotBlank(viewTitle)) {
		displayName = viewTitle;
	}

	String imageColor = ViewUtil.getEntityImageColor(view);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String iconTag = ViewUtil.getIconTag(view);

	//数値のカンマ区切りのセパレータ
	String separator = ViewUtil.getGroupingSeparator();

	AuthContext auth = AuthContext.getCurrentContext();
	request.setAttribute(Constants.ENTITY_DEFINITION, ed);

	//権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName);	//NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	//editor以下で参照するパラメータ
	request.setAttribute(Constants.VIEW_NAME, viewName);

	String className = defName.replaceAll("\\.", "_");
%>
<div class="generic_search s_<c:out value="<%=className%>"/>">
<script src="${staticContentPath}/webjars/jquery-blockui/2.70/jquery.blockUI.js?cv=${apiVersion}"></script>
<%@include file="../../layout/resource/mediaelementResource.inc.jsp" %>
<%
	if (StringUtil.isNotBlank(view.getJavaScript())) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%-- XSS対応-メタの設定のため対応なし(view.getJavaScript()) --%>
<%=view.getJavaScript() %>
</script>
<%
	}
%>

<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<%if (StringUtil.isNotBlank(iconTag)) {%>
<%=iconTag%>
<%} else {%>
<i class="far fa-circle default-icon"></i>
<%} %>
</span>
<c:out value="<%=displayName%>"/>
</h2>
<%
	Section topSection1 = view.getTopSection1();
	if (topSection1 != null
			&& EntityViewUtil.isDisplayElement(defName, topSection1.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, topSection1);
		String topPath1 = EntityViewUtil.getJspPath(topSection1, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=topPath1 %>" />
<%
	}
%>
<p class="btn">
<%
	if (auth.checkPermission(new EntityPermission(defName, EntityPermission.Action.CREATE))) {
		if (!isHideNew) {
%>
<input class="gr-btn gr-size-01 mb10 gr-size-02" type="button" value="${m:rs('mtp-gem-messages', 'generic.search.search.newReg')}" onclick="createNewData('<%=StringUtil.escapeJavaScript(detail)%>', '<%=StringUtil.escapeJavaScript(defName)%>')" />
<%
		}
		if (!isHideCsvUpload) {
%>
<input class="gr-btn gr-size-02 mb10" type="button" value="${m:rs('mtp-gem-messages', 'generic.search.search.csvUpBtn')}" onclick="csvupload()" />
<script type="text/javascript">
separator = "<%= separator %>";
function csvupload(){
	var csvUpload = contextPath + "/<%=StringUtil.escapeJavaScript(csvUpload)%>";
	submitCsvupload(csvUpload);
}
function submitCsvupload(action) {
	var $form = $("<form method='post' />").attr("action", action).appendTo("body");
	$("<input type='hidden' name='defName' />").val("<%=StringUtil.escapeJavaScript(defName)%>").appendTo($form);
	var searchCond = $(":hidden[name='searchCond']").val();
	$("<input type='hidden' name='searchCond' />").val(searchCond).appendTo($form);
	$form.submit();
}
</script>
<%
		}
	}
	if (view.getButtons().size() > 0) {
		for (Button button : view.getButtons()) {
			if (EntityViewUtil.isDisplayElement(defName, button.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
				String cssClass = (button.isPrimary() ? "gr-btn" : "gr-btn-02") + " gr-size-02 mb10";
				if (StringUtil.isNotBlank(button.getStyle())) {
					cssClass = button.getStyle();
				}
				String customStyle = "";
				if (StringUtil.isNotBlank(button.getInputCustomStyle())) {
					//Entityを渡せないのでcssClass指定で足りる気がするがDetailに合わせる
					customStyle = EntityViewUtil.getCustomStyle(defName, view.getScriptKey(), button.getInputCustomStyleScriptKey(), null, null);
				}
				String displayLabel = TemplateUtil.getMultilingualString(button.getDisplayLabel(), button.getLocalizedDisplayLabelList());
%>
<%-- XSS対応-メタの設定のため対応なし(button.getOnclickEvent()) --%>
<input class="<c:out value="<%=cssClass %>"/>" style="<c:out value="<%=customStyle %>"/>" type="button" value="<c:out value="<%=displayLabel %>"/>" onclick="<%=button.getOnclickEvent() %>" />
<%
			}
		}
	}
%>
</p>
<%
	Section topSection2 = view.getTopSection2();
	if (topSection2 != null
			&& EntityViewUtil.isDisplayElement(defName, topSection2.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, topSection2);
		String topPath2 = EntityViewUtil.getJspPath(topSection2, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=topPath2 %>" />
<%
	}

	//検索条件
	Section condition = view.getCondSection();
	String path = EntityViewUtil.getJspPath(condition, ViewConst.DESIGN_TYPE_GEM);
	if (path != null) {
		request.setAttribute(Constants.OUTPUT_TYPE, OutputType.SEARCHCONDITION);
%>
<jsp:include page="<%=path%>" />
<%
		request.removeAttribute(Constants.OUTPUT_TYPE);
	}

	Section centerSection = view.getCenterSection();
	if (centerSection != null
			&& EntityViewUtil.isDisplayElement(defName, centerSection.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, centerSection);
		String centerPath = EntityViewUtil.getJspPath(centerSection, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=centerPath %>" />
<%
	}

	//検索結果
	Section result = view.getResultSection();
	path  = EntityViewUtil.getJspPath(result, ViewConst.DESIGN_TYPE_GEM);
	if (path != null) {
		request.setAttribute(Constants.OUTPUT_TYPE, OutputType.SEARCHRESULT);
%>
<jsp:include page="<%=path%>" />
<%
		request.removeAttribute(Constants.OUTPUT_TYPE);
	}

	Section bottomSection = view.getBottomSection();
	if (bottomSection != null
			&& EntityViewUtil.isDisplayElement(defName, bottomSection.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, bottomSection);
		String bottomPath = EntityViewUtil.getJspPath(bottomSection, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=bottomPath %>" />
<%
	}
%>
</div>
