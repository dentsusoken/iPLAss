<%--
 Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
 
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.menu.ActionMenuAction"%>
<%@ page import="org.iplass.mtp.view.menu.ActionMenuItem"%>
<%@ page import="org.iplass.mtp.view.menu.EntityMenuAction"%>
<%@ page import="org.iplass.mtp.view.menu.EntityMenuItem"%>
<%@ page import="org.iplass.mtp.view.menu.MenuItem"%>
<%@ page import="org.iplass.mtp.view.menu.MenuItemManager"%>
<%@ page import="org.iplass.mtp.view.menu.MenuTree"%>
<%@ page import="org.iplass.mtp.view.menu.NodeMenuItem"%>
<%@ page import="org.iplass.mtp.view.menu.UrlMenuAction"%>
<%@ page import="org.iplass.mtp.view.menu.UrlMenuItem"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
boolean showIcon(MenuItem item) {
	return StringUtil.isNotBlank(item.getImageUrl())
			|| StringUtil.isNotBlank(item.getIconTag());
}
%>
<%
	MenuTree tree = (MenuTree) request.getAttribute(Constants.MENU_TREE);
	NodeMenuItem item = (NodeMenuItem) request.getAttribute("nodeMenuItem");
	if (item == null) return;
	request.removeAttribute("nodeMenuItem");

	Boolean subMenuRoot = (Boolean) request.getAttribute("subMenuRoot");
	if (subMenuRoot == null) subMenuRoot = false;
	request.removeAttribute("subMenuRoot");

	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	MenuItemManager mim = ManagerLocator.getInstance().getManager(MenuItemManager.class);

	String itemId = tree.getName() + "_" + item.getName().replaceAll("/", "_");

	String cls1 = "change-area";
	String cls2 = "listicon";
	String cls3 = "";
	if (subMenuRoot) {
		cls1 = "subMenuRoot";
		cls2 = "navicon";
		cls3 = "subMenuList";
	}

%>
<li class="<%=cls1%> menu-node" id="<c:out value="<%=itemId%>"/>">
<%
	String displayName = TemplateUtil.getMultilingualString(item.getDisplayName(), item.getLocalizedDisplayNameList());
	if (subMenuRoot) {
%>
<a href="javascript:void(0)">
<%
		if (showIcon(item)) {
%>
<span class="<%=cls2%>">
<%
			if (StringUtil.isNotBlank(item.getIconTag())) {
%>
<%=item.getIconTag() %>
<%
			} else if (StringUtil.isNotBlank(item.getImageUrl())) {
				String iconPath = TemplateUtil.getResourceContentPath(item.getImageUrl());
%>
<img src="<c:out value="<%=iconPath%>"/>" alt="" />
<%
			}
%>
</span>
<%
		}
%>
<span>
<c:out value="<%= displayName %>"/>
</span>
</a>
<%
	} else {
%>
<a href="javascript:void(0)">
<%
		if (showIcon(item)) {
%>
<span class="<%=cls2%>">
<%
			if (StringUtil.isNotBlank(item.getIconTag())) {
%>
<%=item.getIconTag() %>
<%
			} else if (StringUtil.isNotBlank(item.getImageUrl())) {
				String iconPath = TemplateUtil.getResourceContentPath(item.getImageUrl());
%>
<img src="<c:out value="<%=iconPath%>"/>" alt="" />
<%
			}
%>
</span>
<%
		}
%>
<c:out value="<%= displayName %>"/>
</a>
<%
	}
%>
<span class="node-cursor"></span>	<%-- 矢印アイコン表示用 --%>

<ul class="<%=cls3%>">
<%
	if (item.hasChild()) {
		for (MenuItem child : item.getChilds()) {
			String childId = tree.getName() + "_" + child.getName().replaceAll("/", "_");
			//EntityMenuItemとActionMenuItem
			if (child instanceof EntityMenuItem) {
				EntityMenuItem ei = (EntityMenuItem) child;
				EntityMenuAction ea = mim.customizeEntityMenu(ei, SearchViewCommand.SEARCH_ACTION_NAME);

				String action = ea.getActionName();
				String defName = ea.getDefName();
				String params = ea.getParameter() != null ? ea.getParameter() : "";
				if (StringUtil.isNotEmpty(ea.getViewName())) {
					//params側に含まれているかはチェックしない
					//ViewNameを指定した場合はparams側を修正すること
					if (StringUtil.isNotEmpty(params)) {
						params += "&";
					}
					params += Constants.VIEW_NAME + "=" + ea.getViewName();
				}
				if (ea.isExecuteSearch()) {
					//params側に含まれているかはチェックしない
					//ExecuteSearchを指定した場合はparams側を修正すること
					if (StringUtil.isNotEmpty(params)) {
						params += "&";
					}
					params += Constants.EXECUTE_SEARCH + "=" + Constants.EXECUTE_SEARCH_VALUE;
				}
%>
<li class="menu-entity" id="<c:out value="<%=childId%>"/>">
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="javascript:void(0)" class="entityLink" data-action="<%=action%>" data-defName="<%=defName%>" data-params="<%=params%>">
<%
				if (showIcon(child)) {
%>
<span class="listicon">
<%
					if (StringUtil.isNotBlank(child.getIconTag())) {
%>
<%=child.getIconTag() %>
<%
					} else if (StringUtil.isNotBlank(child.getImageUrl())) {
						String iconPath = TemplateUtil.getResourceContentPath(child.getImageUrl());
%>
<img src="<c:out value="<%=iconPath%>"/>" alt="" />
<%
					}
%>
</span>
<%
				}

				EntityDefinition entityDefinition = edm.get(defName);
				displayName = TemplateUtil.getMultilingualString(child.getDisplayName(), child.getLocalizedDisplayNameList(), entityDefinition.getDisplayName(), entityDefinition.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
</li>
<%
			} else if (child instanceof ActionMenuItem) {
				ActionMenuItem ai = (ActionMenuItem) child;
				ActionMenuAction aa = mim.customizeActionMenu(ai);

				String params = aa.getParameter() != null ? aa.getParameter() : "";
				String action = aa.getActionName();
				if (action.isEmpty()) action = "gem/";
%>
<li class="menu-action" id="<c:out value="<%=childId%>"/>">
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="javascript:void(0)" class="actionLink" data-action="<%=action%>" data-params="<%=params%>">
<%
				if (showIcon(child)) {
%>
<span class="listicon">
<%
					if (StringUtil.isNotBlank(child.getIconTag())) {
%>
<%=child.getIconTag() %>
<%
					} else if (StringUtil.isNotBlank(child.getImageUrl())) {
						String iconPath = TemplateUtil.getResourceContentPath(child.getImageUrl());
%>
<img src="<c:out value="<%=iconPath%>"/>" alt="" />
<%
					}
%>
</span>
<%
				}

				displayName = TemplateUtil.getMultilingualString(child.getDisplayName(), child.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
</li>
<%
			} else if (child instanceof UrlMenuItem) {
				UrlMenuItem ui = (UrlMenuItem) child;
				UrlMenuAction ua = mim.customizeUrlMenu(ui);

				String url = TemplateUtil.getResourceContentPath(ua.getUrl());
				if (StringUtil.isNotBlank(ua.getParameter())) {
					url = url + "?" + ua.getParameter();
				}
				String target = "";
				if (ua.isShowNewPage()) {
					target = "_blank";
				}
%>
<li class="menu-url">
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="<%=url%>" class="urlLink" target="<%=target%>">
<%
				if (showIcon(child)) {
%>
<span class="listicon">
<%
					if (StringUtil.isNotBlank(child.getIconTag())) {
%>
<%=child.getIconTag() %>
<%
					} else if (StringUtil.isNotBlank(child.getImageUrl())) {
						String iconPath = TemplateUtil.getResourceContentPath(child.getImageUrl());
%>
<img src="<c:out value="<%=iconPath%>"/>" alt="" />
<%
					}
%>
</span>
<%
				}

				displayName = TemplateUtil.getMultilingualString(child.getDisplayName(), child.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
</li>
<%
			} else if (child instanceof NodeMenuItem) {
				request.setAttribute("nodeMenuItem", child);
				request.setAttribute("subMenuRoot", false);
%>
<jsp:include page="nodeMenu.jsp" />
<%
			}
		}
	}
%>
</ul>
</li>
