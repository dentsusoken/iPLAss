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
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ page import="java.util.Map" %>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
	// データ取得
	String contextPath = TemplateUtil.getTenantContextPath();

	@SuppressWarnings("unchecked") Map<String, String> errorData = (Map<String, String>) request.getAttribute("errorData");
	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);
	DetailFormView form = (DetailFormView) request.getAttribute("detailFormView");

	String defName = ed.getName();
	String viewName = form.getName();

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//キャンセルアクション
	String cancel = "";
	if (StringUtil.isNotBlank(form.getCancelActionName())) {
		cancel = form.getCancelActionName() + urlPath;
	} else {
		cancel = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;
	}

	// 表示名
	String displayName = TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
	String viewTitle = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList());
	if(StringUtil.isNotBlank(viewTitle)) {
		displayName = viewTitle;
	}

	String imageColor = ViewUtil.getEntityImageColor(form);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String className = defName.replaceAll("\\.", "_");
%>

<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_search_csvuploadresult d_<c:out value="<%=className %>"/>">
<script type="text/javascript">
function cancel() {
	submitForm("${m:tcPath()}/<%=StringUtil.escapeJavaScript(cancel)%>", {
			defName:"<%=StringUtil.escapeJavaScript(defName)%>",
			searchCond:$(":hidden[name='searchCond']").val()
		});
}
</script>
<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<i class="far fa-circle default-icon"></i>
</span>
<c:out value="<%=displayName%>"/>
</h2>
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.csvUploadResult.csvUpTitle", displayName) %></h3>

<input type="hidden" name="searchCond" value="${m:esc(searchCond)}" />

<p>${m:rs("mtp-gem-messages", "generic.csvUploadResult.regCnt")}${m:esc(insertCount)}</p>
<p>${m:rs("mtp-gem-messages", "generic.csvUploadResult.upCnt")}${m:esc(updateCount)}</p>
<p>${m:rs("mtp-gem-messages", "generic.csvUploadResult.delCnt")}${m:esc(deleteCount)}</p>

<div class="operation-bar operation-bar_top">
<ul class="list_operation">
<li class="cancel-link"><a href="javascript:void(0)" onclick="cancel();return false;">${m:rs("mtp-gem-messages", "generic.csvUploadResult.back")}</a></li>
</ul>
</div>
</div>
