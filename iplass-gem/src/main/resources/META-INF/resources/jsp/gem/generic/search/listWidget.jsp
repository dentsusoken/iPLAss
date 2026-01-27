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

<%@ page import="java.util.Date"%>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.view.top.parts.EntityListParts"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchNameListCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String contextPath = TemplateUtil.getStaticContentPath();
	EntityListParts parts = (EntityListParts) request.getAttribute("entityListParts");
	if (parts == null) return;

	SearchFormView form = (SearchFormView) request.getAttribute("searchFormView");

	String urlPath = ViewUtil.getParamMappingPath(parts.getDefName(), parts.getViewNameForLink());
	String searchViewAction = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;

	//詳細表示アクション
	String viewAction = "";
	String action = form.getViewActionName();
	if (action != null && !action.isEmpty()) {
		viewAction = action +  urlPath;
	} else {
		viewAction = DetailViewCommand.VIEW_ACTION_NAME + urlPath;
	}

	//Limit
	Integer limit = ViewUtil.getSearchLimit(form.getResultSection());

	String prevLabel = GemResourceBundleUtil.resourceString("generic.search.listWidget.previous");
	String nextLabel = GemResourceBundleUtil.resourceString("generic.search.listWidget.next");
	
	//スタイルシートのクラス名
	String style = "entity-list-widget topview-widget";
	if (StringUtil.isNotBlank(parts.getStyle())) {
		style = style + " " + parts.getStyle();
	}
	
	//パーツの高さスタイル属性
	String styleAttr = ViewUtil.buildHeightStyleAttr(parts.getMaxHeight());
%>
<div class="<c:out value="<%=style %>"/>" data-defName="${m:escJs(entityListParts.defName)}" data-viewName="${m:escJs(entityListParts.viewName)}" data-filterName="${m:escJs(entityListParts.filterName)}"
 data-limit="<%=limit%>" data-prevLabel="<c:out value="<%=prevLabel%>"/>" data-nextLabel="<c:out value="<%=nextLabel%>"/>">
<div class="lyt-shortcut-01 mb05">
${entityListParts.iconTag}
<p class="title">${m:esc(title)}</p>
<div class="widget-contents" style="<%=styleAttr%>">
<ul class="list-entity-name" data-webapiName="<%=SearchNameListCommand.WEBAPI_NAME%>" data-viewAction="<c:out value="<%=viewAction%>" />">
</ul>
</div><!--todo-block-->
</div><!--lyt-shortcut-01-->
<ul class="entity-list link-widget">
<li class="list-paging">
<div class="result-nav"></div>
</li>
<li class="list-view"><a href="javascript:void(0)" data-searchViewAction="<c:out value="<%=searchViewAction%>" />">${m:rs("mtp-gem-messages", "generic.search.listWidget.showSearch")}</a></li>
</ul>
</div>
