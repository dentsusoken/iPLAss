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

<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.Section"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%
	//データ取得
	String selectType = request.getParameter(Constants.SELECT_TYPE);
	String multiplicity = request.getParameter(Constants.SELECT_MULTI);
	String propName = request.getParameter(Constants.PROP_NAME);
	propName = StringUtil.escapeJavaScript(propName);
	String rootName = request.getParameter(Constants.ROOT_NAME);
	rootName = StringUtil.escapeJavaScript(rootName);
	String modalTarget = request.getParameter(Constants.MODAL_TARGET);
	modalTarget = StringUtil.escapeJavaScript(modalTarget);
	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);

	SearchFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();

	//表示名
	String displayName = TemplateUtil.getMultilingualString(data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	String viewTitle = TemplateUtil.getMultilingualString(data.getView().getTitle(), data.getView().getLocalizedTitleList());
	if(StringUtil.isNotBlank(viewTitle)) {
		displayName = viewTitle;
	}
	displayName = StringUtil.escapeJavaScript(displayName);

	//選択方法
	OutputType outputType = null;
	if ("multi".equals(selectType)) {
		outputType = OutputType.MULTISELECT;
	} else {
		outputType = OutputType.SINGLESELECT;
	}

	int _multiplicity = 1;
	if (multiplicity != null) {
		try {
			_multiplicity = Integer.parseInt(multiplicity);
		} catch (NumberFormatException e) {
		}
	}

	if (modalTarget == null) modalTarget = "";

	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());

	//権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName);	//NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	//editor以下で参照するパラメータ
	request.setAttribute(Constants.VIEW_NAME, viewName);

	String className = defName.replaceAll("\\.", "_");
%>
<div class="generic_select s_<c:out value="<%=className %>"/>">
<%@include file="../../layout/resource/mediaelementResource.inc.jsp" %>
<script type="text/javascript">
var selectArray = new Array();
var multiplicity = <%=_multiplicity%>;
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
	var target = null;
	var windowManager = document.rootWindow.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		$("#modal-title-" + modalTarget, parent.document).text("<%=displayName%>");
		target = windowManager[modalTarget];
	} else {
		$("#modal-title", parent.document).text("<%=displayName%>");
		target = parent.document;
	}
	var propName = "<%=propName%>";
	var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
	var rootName = "<%=rootName%>";
	var _rootName = rootName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
	var $rootNode = $("#" + _rootName, target);
	$(":hidden[name='" + _propName + "']", $rootNode).each(function() {
		selectArray.push($(this).val());
	});
});
function closeModal() {
	var func = null;
	var windowManager = document.rootWindow.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		var win = windowManager[modalTarget];
		func = win.scriptContext["searchReferenceCallback"];
	} else {
		func = parent.document.scriptContext["searchReferenceCallback"];
	}
	if (func && $.isFunction(func)) func.call(this, selectArray);
}
</script>
<%
	if (form.getJavaScript() != null && !form.getJavaScript().isEmpty()) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%-- XSS対応-メタの設定のため対応なし(form.getJavaScript()) --%>
<%=form.getJavaScript() %>
</script>
<%
	}

	Section topSection1 = form.getTopSection1();
	if (topSection1 != null
			&& EntityViewUtil.isDisplayElement(defName, topSection1.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, topSection1);
		String topPath1 = EntityViewUtil.getJspPath(topSection1, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=topPath1 %>" />
<%
	}
	Section topSection2 = form.getTopSection2();
	if (topSection2 != null
			&& EntityViewUtil.isDisplayElement(defName, topSection2.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, topSection2);
		String topPath2 = EntityViewUtil.getJspPath(topSection2, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=topPath2 %>" />
<%
	}

	//検索条件
	Section condition = data.getView().getCondSection();
	String condPath = EntityViewUtil.getJspPath(condition, ViewConst.DESIGN_TYPE_GEM);
	if (condPath != null) {
		request.setAttribute(Constants.OUTPUT_TYPE, OutputType.SEARCHCONDITION);
%>
<jsp:include page="<%=condPath %>" />
<%
		request.removeAttribute(Constants.OUTPUT_TYPE);
	}

	Section centerSection = form.getCenterSection();
	if (centerSection != null
			&& EntityViewUtil.isDisplayElement(defName, centerSection.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, centerSection);
		String centerPath = EntityViewUtil.getJspPath(centerSection, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=centerPath %>" />
<%
	}

	//検索結果
	Section result = data.getView().getResultSection();
	String resPath = EntityViewUtil.getJspPath(result, ViewConst.DESIGN_TYPE_GEM);
	if (resPath != null) {
		request.setAttribute(Constants.OUTPUT_TYPE, outputType);
		request.setAttribute(Constants.SHOW_DETERMINE_BUTTON, true);
%>
<jsp:include page="<%=resPath %>" />
<%
		request.removeAttribute(Constants.OUTPUT_TYPE);
		request.removeAttribute(Constants.SHOW_DETERMINE_BUTTON);
	}

	Section bottomSection = form.getBottomSection();
	if (bottomSection != null
			&& EntityViewUtil.isDisplayElement(defName, bottomSection.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
		request.setAttribute(Constants.ELEMENT, bottomSection);
		String bottomPath = EntityViewUtil.getJspPath(bottomSection, ViewConst.DESIGN_TYPE_GEM);
%>
<jsp:include page="<%=bottomPath %>" />
<%
	}
%>

<p class="btn"><input type="button" value="${m:rs('mtp-gem-messages', 'generic.search.select.determine')}" class="gr-btn gr-size-01 mb10" onclick="closeModal()" /></p>
</div>
