<%--
 Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 
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

<%@ page import="org.iplass.mtp.view.treeview.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.treeview.GetTreeViewListDataCommand"%>
<%@ page import="org.iplass.mtp.view.top.parts.TreeViewParts"%>

<%
	//設定情報取得
	TreeViewParts parts = (TreeViewParts) request.getAttribute("treeViewParts");


	String treeViewName = (String) request.getAttribute("treeViewName");
	if (treeViewName == null) return;

	TreeViewManager tvm = ManagerLocator.getInstance().getManager(TreeViewManager.class);
	TreeView treeView = tvm.get(treeViewName);
	if (treeView == null || treeView.getItems().size() == 0) return;

	String displayName = TemplateUtil.getMultilingualString(treeView.getDisplayName(), treeView.getLocalizedDisplayNameList());
	
	//スタイルシートのクラス名
	String style = "topview-widget";
	if (StringUtil.isNotBlank(parts.getStyle())) {
		style = style + " " + parts.getStyle();
	}
%>
<div class="<c:out value="<%=style %>"/>">
<%
%>
<div class="lyt-shortcut-01">
${treeViewParts.iconTag}
<p class="title"><c:out value="<%=displayName %>" /></p>
<div class="widget-contents">
<div class="treeViewList" data-defName="${m:esc(treeViewName)}"
 data-viewAction="<%=DetailViewCommand.VIEW_ACTION_NAME %>"
 data-webapiName="<%=GetTreeViewListDataCommand.WEBAPI_NAME%>">
</div>
</div>
</div><!-- lyt-shortcut-01 -->
</div>