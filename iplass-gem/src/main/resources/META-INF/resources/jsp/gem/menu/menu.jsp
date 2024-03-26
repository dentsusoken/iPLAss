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

<%@ page import="java.util.List"%>
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
<%@ page import="org.iplass.mtp.view.menu.UrlMenuItem"%>
<%@ page import="org.iplass.mtp.view.menu.UrlMenuAction"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.MenuCommand"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
boolean showIcon(MenuItem item) {
	return StringUtil.isNotBlank(item.getImageUrl())
			|| StringUtil.isNotBlank(item.getIconTag());
}
%>
<%
	MenuTree tree = (MenuTree) request.getAttribute(Constants.MENU_TREE);

	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	MenuItemManager mim = ManagerLocator.getInstance().getManager(MenuItemManager.class);

	String roleName = (String) request.getAttribute(Constants.ROLE_NAME);
	if (roleName == null || roleName.isEmpty()) roleName = "DEFAULT";

	TopViewDefinitionManager tm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	TopViewDefinition definition = tm.get(roleName);

	String style = ViewUtil.getSkin().getMenuSkinName();
	if (tree.getMenuItems() == null || tree.getMenuItems().isEmpty()) {
		style = " noitem";	//メニューが一つもない場合にメニューがつぶれるので設定
	}


	if (definition != null && definition.getWidgets().size() > 0) {
%>
<ul id="nav-menu" class="tab-menu">
<li class="menu-list"><a href="javascript:void(0)">${m:rs("mtp-gem-messages", "menu.menu.menu")}</a></li>
<li class="menu-shortcut"><a href="javascript:void(0)">${m:rs("mtp-gem-messages", "menu.menu.widget")}</a></li>
</ul><%-- nav-menu --%>
<%
	}

%>
<div id="nav" class="tab-panel <c:out value="<%=style%>" />">
<ul class="nav-wrap clear">
<li class="home"><a href="javascript:void(0)" onclick="home('<%=MenuCommand.ACTION_NAME%>');return false;"><span>${m:rs("mtp-gem-messages", "menu.menu.home")}</span></a></li>
<%
	if (tree.getMenuItems() != null) {
		for (MenuItem item : tree.getMenuItems()) {

			String itemId = tree.getName() + "_" + item.getName().replaceAll("/", "_");
			String nodeImageColorStyle = "bg-default";
			if (item.getImageColor() != null) {
				String colorName = item.getImageColor();
				nodeImageColorStyle = "bg-" + colorName;
			}

			if(item instanceof NodeMenuItem) {
				List<MenuItem> childs = item.getChilds();
%>
<li class="<c:out value="<%=nodeImageColorStyle%>"/> menu_<c:out value="<%=itemId%>"/> menu-node root-menu-node" id="<c:out value="<%=itemId%>"/>">
<p>
<a href="javascript:void(0)">
<%
				if (showIcon(item)) {
%>
<span class="navicon">
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

				String displayName = TemplateUtil.getMultilingualString(item.getDisplayName(), item.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
<span class="curicon"></span>	<%-- 矢印アイコン表示用 --%>
</p>
<ul class="nav-detail">
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
<li class="menu_<c:out value="<%=childId%>"/> menu-entity" id="<c:out value="<%=childId%>"/>">
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="javascript:void(0)" class="entityLink" data-action="<%=action%>" data-defName="<%=defName%>" data-params="<%=params%>">
<%
							if (showIcon(child)) {
%>
<span class="navicon">
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
<li class="menu_<c:out value="<%=childId%>"/> menu-action" id="<c:out value="<%=childId%>"/>">
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="javascript:void(0)" class="actionLink" data-action="<%=action%>" data-params="<%=params%>">
<%
							if (showIcon(child)) {
%>
<span class="navicon">
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
<li class="menu_<c:out value="<%=childId%>"/> menu-url" id="<c:out value="<%=childId%>"/>">
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="<%=url%>" class="urlLink" target="<%=target%>">
<%
							if (showIcon(child)) {
%>
<span class="navicon">
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
							request.setAttribute("subMenuRoot", true);
%>
<jsp:include page="nodeMenu.jsp" />
<%
						}
					}
				}
%>
</ul>
</li>
<%
			} else {
				//EntityMenuItemとActionMenuItem
				if (item instanceof EntityMenuItem) {
					EntityMenuItem ei = (EntityMenuItem) item;
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
<li class="<c:out value="<%=nodeImageColorStyle%>"/> menu_<c:out value="<%=itemId%>"/> menu-entity" id="<c:out value="<%=itemId%>"/>">
<p>
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="javascript:void(0)" class="entityLink" data-action="<%=action%>" data-defName="<%=defName%>" data-params="<%=params%>">
<%
					if (showIcon(item)) {
%>
<span class="navicon">
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
					EntityDefinition entityDefinition = edm.get(defName);
					String displayName = TemplateUtil.getMultilingualString(item.getDisplayName(), item.getLocalizedDisplayNameList(), entityDefinition.getDisplayName(), entityDefinition.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
</p>
</li>
<%
				} else if (item instanceof ActionMenuItem) {
					ActionMenuItem ai = (ActionMenuItem) item;
					ActionMenuAction aa = mim.customizeActionMenu(ai);

					String params = aa.getParameter() != null ? aa.getParameter() : "";
					String action = aa.getActionName();
					if (action.isEmpty()) action = "gem/";
%>
<li class="<c:out value="<%=nodeImageColorStyle%>"/> menu_<c:out value="<%=itemId%>"/> menu-action" id="<c:out value="<%=itemId%>"/>">
<p>
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="javascript:void(0)" class="actionLink" data-action="<%=action%>" data-params="<%=params%>">
<%
					if (showIcon(item)) {
%>
<span class="navicon">
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
					String displayName = TemplateUtil.getMultilingualString(item.getDisplayName(), item.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
</p>
</li>
<%
				} else if (item instanceof UrlMenuItem) {
					UrlMenuItem ui = (UrlMenuItem) item;
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
<li class="<c:out value="<%=nodeImageColorStyle%>"/> menu-url" id="<c:out value="<%=itemId%>"/>">
<p>
<%-- XSS対応-メタの設定のため対応なし(params) --%>
<a href="<%=url%>" class="urlLink" target="<%=target%>">
<%
					if (showIcon(item)) {
%>
<span class="navicon">
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

					String displayName = TemplateUtil.getMultilingualString(item.getDisplayName(), item.getLocalizedDisplayNameList());
%>
<c:out value="<%= displayName %>"/>
</a>
</p>
</li>
<%
				}
			}
		}
	}
%>

</ul><%-- nav-wrap --%>
<script type="text/javascript">
$(function(){
	$(".entityLink").on("click", function(e) {
		var parentId = $(this).parents("li").attr("id");
		setSessionStorage("currentMenuId", parentId);

		var action = $(this).attr("data-action");
		var defName = $(this).attr("data-defName");
		var params = $(this).attr("data-params");
		if (e.ctrlKey) {
			searchView(action, defName, params, "_blank");
		} else {
			searchView(action, defName, params);
		}
		return false;
	});
	$(".actionLink").on("click", function(e) {
		var parentId = $(this).parents("li").attr("id");
		setSessionStorage("currentMenuId", parentId);

		var action = $(this).attr("data-action");
		var params = $(this).attr("data-params");
		if (e.ctrlKey) {
			menuClick(action, params, "_blank");
		} else {
			menuClick(action, params);
		}
		return false;
	});

});
</script>
</div><%--nav--%>
