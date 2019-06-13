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
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>

<%@ page import="java.util.Collections"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.mtp.auth.*"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.fulltextsearch.FulltextSearchManager"%>
<%@ page import="org.iplass.mtp.impl.i18n.I18nUtil"%>
<%@ page import="org.iplass.mtp.impl.web.i18n.LangSelector"%>
<%@ page import="org.iplass.mtp.impl.web.WebUtil"%>
<%@ page import="org.iplass.mtp.tenant.Tenant" %>
<%@ page import="org.iplass.mtp.tenant.TenantI18nInfo"%>
<%@ page import="org.iplass.mtp.tenant.web.TenantWebInfo"%>
<%@ page import="org.iplass.mtp.tenant.gem.TenantGemInfo"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts"%>
<%@ page import="org.iplass.mtp.view.top.parts.FulltextSearchViewParts"%>
<%@ page import="org.iplass.mtp.view.top.parts.TopViewParts"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>
<%@ page import="org.iplass.mtp.web.actionmapping.permission.ActionPermission"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.AboutCommand"%>
<%@ page import="org.iplass.gem.command.ChangeRoleCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.MenuCommand"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.gem.command.auth.LogoutCommand"%>
<%@ page import="org.iplass.gem.command.auth.RevokeApplicationCommand"%>
<%@ page import="org.iplass.gem.command.auth.UpdatePasswordCommand"%>
<%@ page import="org.iplass.gem.command.fulltext.FullTextSearchViewCommand"%>

<%!EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);
	TopViewDefinitionManager tvdm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	FulltextSearchManager fsm = ManagerLocator.getInstance().getManager(FulltextSearchManager.class);

	/**
	 * 全文検索で利用するViewNameに対するSearchFormViewの取得
	 */
	SearchFormView getSearchFormView(String defName, String viewName) {
		EntityView ev = evm.get(defName);
		EntityDefinition ed = edm.get(defName);
		SearchFormView view = null;
		if (ev != null) {
			if (viewName == null || viewName.isEmpty()) {
				view = ev.getDefaultSearchFormView();
				if (view == null) {
					view = FormViewUtil.createDefaultSearchFormView(ed);
				}
			} else {
				view = ev.getSearchFormView(viewName);
			}
		} else {
			view = FormViewUtil.createDefaultSearchFormView(ed);
		}
		return view;
	}

	/**
	 * 全文検索対象Entityの取得
	 */
	Map<String, String> getFulltextEntities(TopViewDefinition topView) {
		//全文検索の利用可否チェック
		if (!fsm.isUseFulltextSearch()) {
			return Collections.emptyMap();
		}

		//TopViewのParts取得
		FulltextSearchViewParts fulltextSearchViewParts = null;
		if (topView != null) {
			for (TopViewParts parts : topView.getParts()) {
				if (parts instanceof FulltextSearchViewParts) {
					fulltextSearchViewParts = (FulltextSearchViewParts) parts;
					break;
				}
			}
		}

		Map<String, String> entityMap = new HashMap<String, String>();
		if (fulltextSearchViewParts == null) {
			//Entity定義から指定
			for (String defName : edm.definitionList()) {
				EntityDefinition def = edm.get(defName);
				if (def != null && def.isCrawl()) {
					String displayName = TemplateUtil.getMultilingualString(def.getDisplayName(), def.getLocalizedDisplayNameList());
					entityMap.put(def.getName(), displayName);
				}
			}
		} else {
			if (!fulltextSearchViewParts.isDispSearchWindow()) {
				//検索を表示しない場合は空を返す
				return Collections.emptyMap();
			}

			//Viewの設定があるないに関わらず、全部返ってくる
			Map<String, String> partsViewMap = fulltextSearchViewParts.getViewNames();
			if (partsViewMap != null) {
				//表示対象の取得
				Map<String, Boolean> partsDispMap = fulltextSearchViewParts.getDispEntities();
				if (partsDispMap == null) {
					partsDispMap = Collections.emptyMap();
				}

				for(Map.Entry<String, String> entry : partsViewMap.entrySet()) {
					EntityDefinition ed = edm.get(entry.getKey());

					//表示対象のチェック
					if (ed.isCrawl() && partsDispMap.get(ed.getName())) {
						EntityView view = evm.get(ed.getName());
						SearchFormView form = null;

						//viewを取得する
						String viewName = entry.getValue();

						if (StringUtil.isEmpty(viewName)) {
							// デフォルトレイアウトを利用
							if (view != null && view.getSearchFormViewNames().length > 0) {
								//1件でもView定義があればその中からデフォルトレイアウトを探す
								form = view.getDefaultSearchFormView();
							} else {
								// 何もなければ自動生成
								form = FormViewUtil.createDefaultSearchFormView(ed);
							}
						} else {
							// 指定レイアウトを利用
							if (view.getSearchFormView(viewName) != null) {
								form = view.getSearchFormView(viewName);
							} else {
								// なければ自動生成
								form = FormViewUtil.createDefaultSearchFormView(ed);
							}
						}

						String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(), ed.getDisplayName(), ed.getLocalizedDisplayNameList());
						entityMap.put(ed.getName(), displayName);
					}
				}
			}
		}
		return entityMap;
	}%>
<%
	int tenantId = TemplateUtil.getClientTenantId();
	Tenant tenant = TemplateUtil.getTenant();

	String adminAction = "/admin/index";

	String lang = (String) request.getAttribute(LangSelector.LANG_ATTRIBUTE_NAME);

	AuthContext auth = AuthContext.getCurrentContext();
	User user = auth.getUser();
	String userName = "";
	if (user != null) userName = user.getName();

	boolean isAdmin = false;
	// 管理コンソールの権限チェック
	if(user != null && user.isAdmin() && auth.checkPermission(new ActionPermission(adminAction))) {
		isAdmin = true;
	}

	//ナビメニューを表示するか(通常のlayout.jspからのみ表示)
	Boolean showNavi = (Boolean) request.getAttribute("showNavi");
	if (showNavi == null) showNavi = false;

	// 言語
	Map<String, String> enabelLangeages = TemplateUtil.getEnableLanguages();

	//ロール・メニュー
	LinkedHashMap<String, String> role = (LinkedHashMap<String, String>) request.getAttribute(Constants.ROLE);

	String roleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);
	if (roleName == null) roleName = "DEFAULT";

	//TopView定義の取得
	TopViewDefinition topView = tvdm.get(roleName);

	//全文検索対象の取得
	Map<String, String> fulltextEntities = null;
	if (showNavi) {
		fulltextEntities = getFulltextEntities(topView);
	}

	//アプリ管理
	boolean showAppMentenance = false;
	String titleAppMentenance = null;
	if (showNavi && auth.isAuthenticated() && topView != null) {
		for (TopViewParts parts : topView.getParts()) {
			if (parts instanceof ApplicationMaintenanceParts) {
				ApplicationMaintenanceParts amp = (ApplicationMaintenanceParts)parts;
				showAppMentenance = true;
				titleAppMentenance = I18nUtil.stringDef(amp.getTitle(), amp.getLocalizedTitleList());
				if (titleAppMentenance == null) {
					titleAppMentenance = GemResourceBundleUtil.resourceString("layout.header.appMaintenance");
				}
				break;
			}
		}
	}
%>

<div id="header-container">
<div id="header">
<div id="title" class="header-title">
<h1>
<%
	String imgUrl = ViewUtil.getTenantImgUrl();
	if (request.getAttribute("imgUrl") != null) {
		// 認証前の画面でロゴやタイトルを表示するか判断した結果のロゴURL
		imgUrl = (String)request.getAttribute("imgUrl");
	}
	if (!StringUtil.isEmpty(imgUrl)) {
%>
<img src="<c:out value="<%=imgUrl%>"/>" alt="" class="tenant-image" />
<%
	}

	String imgMiniUrl = ViewUtil.getTenantMiniImgUrl();
	if (!StringUtil.isEmpty(imgMiniUrl)) {
%>
<img src="<c:out value="<%=imgMiniUrl%>"/>" alt="" class="tenant-mini-image" />
<%
	}

	TenantGemInfo gemInfo = ViewUtil.getTenantGemInfo(tenant);
	if (gemInfo.isUseDisplayName()) {
		String title = ViewUtil.getDispTenantName();
		if (request.getAttribute("title") != null) {
	// 認証前の画面でロゴやタイトルを表示するか判断した結果のタイトル
	title = (String)request.getAttribute("title");
		}
%>
<span><c:out value="<%=title%>"/></span>
<%
	}
%>
</h1>
</div><%-- title --%>
<%
	if (showNavi) {
		String additionalStyle = fulltextEntities.isEmpty() ? "unuseFulltextSearch" : "useFulltextSearch" ;
%>
<ul id="user-nav" class="<c:out value="<%=additionalStyle%>"/>">

<li id="account-01" class="hed-pull">
<p><span class="name"><c:out value="<%=userName%>"/></span></p>
<ul>
<%
		if (isAdmin) {
%>
<li class="admin-console">
<a href="javascript:void(0)" onclick="showAdminConsole();">${m:rs("mtp-gem-messages", "layout.header.manage")}</a>
<script>
function showAdminConsole() {
	submitForm(contextPath + "<%=adminAction%>", null, "_blank");
}
</script>
</li>
<%
		}

		TenantWebInfo webInfo = WebUtil.getTenantWebInfo(tenant);
		if (webInfo.isUsePreview()) {
%>
<li class="preview-date">
<a href="javascript:void(0)" onclick="showPreviewDateTimeDialog();">${m:rs("mtp-gem-messages", "layout.header.chngPrvwDate")}</a>
<div id="showPreviewDateTimeDialog" title="${m:rs('mtp-gem-messages', 'layout.header.chngPrvwDate')}" style="display:none;">
<p style="margin-top: 18px;">
<input type="text" class="datetimepicker inpbr" data-timeformat="HH:mm:ss" data-stepmin="1" />
</p>
</div>
<script>
$(function() {
	var $dialog = $("#showPreviewDateTimeDialog");
	var $previewDatetime = $(".datetimepicker", $dialog);
	$dialog.dialog({
		autoOpen: false,
		modal: true,
		resizable: false,
		buttons: {
			"${m:rs('mtp-gem-messages', 'layout.header.change')}": function() {
				var date = $previewDatetime.val();
				if (date != null && date != "") {
					var datetimeString = convertFromLocaleDatetimeString(date);
					setSystemDate(datetimeString, function() {
						$dialog.dialog("close");
						alert("${m:rs('mtp-gem-messages', 'layout.header.chngPrvwDateMsg')}");
					});
				} else {
					alert("${m:rs('mtp-gem-messages', 'layout.header.incorrect')}");
				}
			},
			"${m:rs('mtp-gem-messages', 'layout.header.reset')}": function() {
				var $this = $(this);
				setSystemDate(null, function() {
					$dialog.dialog("close");
					alert("${m:rs('mtp-gem-messages', 'layout.header.chngPrvwDateMsg')}");
				});
			},
			"${m:rs('mtp-gem-messages', 'layout.header.cancel')}": function() { $dialog.dialog("close");}
		},
		open: function() {
			$previewDatetime.show();
			$previewDatetime.val("");
			getSystemDate(true, function(dateTime) {
				if (dateTime) {
					var localizedString = convertToLocaleDatetimeString(dateUtil.format(dateTime, dateUtil.getServerDatetimeFormat()), dateUtil.getServerDatetimeFormat());
					$previewDatetime.val(localizedString);
				}
			});
		}
	});
	$dialog.on("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
});
function showPreviewDateTimeDialog() {
	$("#showPreviewDateTimeDialog").dialog("open");
}
</script>
</li>
<%
		}

		TenantI18nInfo i18nInfo = tenant.getTenantConfig(TenantI18nInfo.class);
		if (i18nInfo.isUseMultilingual() && i18nInfo.getUseLanguageList() != null && i18nInfo.getUseLanguageList().size() > 1) {
%>
<li class="change-area lang">
<span class="txt">${m:rs("mtp-gem-messages", "layout.header.selectLang")}</span>
<span class="node-cursor"></span>
<ul>
<%
			List<String> useLangList = i18nInfo.getUseLanguageList();

			for (String key : enabelLangeages.keySet()) {
				String name = enabelLangeages.get(key);

				if (useLangList.contains(key)) {
					boolean selectLang = key.equals(lang);
%>
<li>
<%
					if (selectLang) {
%>
<span class="icon"></span>
<%
					}
%>
<span class="txt"><c:out value="<%=name%>"/></span>
<input type="hidden" value="<%=key%>"/>
</li>
<%
				}
			}
%>
</ul>

<script>
$(function() {
	$("li.lang ul li").on("click", function() {
		$("li.lang ul li span.icon").remove();
		var $this = $(this);
		var lang = $(":hidden", $this).val();
		var param = "{\"language\":\"" + lang + "\"}";

		$.ajax({
			type: "POST",
			contentType: "application/json",
				url: contextPath + "/api/gem/webapi/SelectLanguageCommand",
				dataType: "json",
			data: param,
			success: function(commandResult){
				$("<span />").addClass("icon").prependTo($this);
			}
		});

	});
});
</script>
</li>

<%
		}

		if (role != null && role.size() > 1) {
%>
<li class="change-area menu">
<span class="txt">${m:rs("mtp-gem-messages", "layout.header.menuChng")}</span>
<span class="node-cursor"></span>
<ul>
<%
			for (String key : role.keySet()) {
				String name = role.get(key);
				boolean selectRole = key.equals(roleName);
%>
<li class="menu">
<%
				if (selectRole) {
%>
<span class="icon"></span>
<%
				}
%>

<span class="txt"><c:out value="<%=name%>"/></span>
<input type="hidden" value="<c:out value="<%=key%>"/>"/>
</li>
<%
			}
%>
</ul>
<script>
$(function() {
	$("li.menu ul li.menu:not(:has(span.icon))").on("click", function() {
		var roleName = $(":hidden", this).val();
		setRole("<%=ChangeRoleCommand.WEBAPI_NAME%>", roleName, function(checkResult) {
			if (checkResult) {
				clearMenuState();
				submitForm(contextPath + "/<%=MenuCommand.ACTION_NAME%>", {});
			} else {
				alert("${m:rs('mtp-gem-messages', 'layout.header.failChngMenu')}");
			}
		});
	});
});
</script>
</li>
<%
		}

		if (!user.isAnonymous()) {
			if(am.canUpdateCredential(user.getAccountPolicy())) {
%>
<li class="password">
<a href="javascript:void(0)" onclick="changePassword()">${m:rs("mtp-gem-messages", "layout.header.passChng")}</a>
<script>
function changePassword() {
	clearMenuState();
	submitForm(contextPath + "/<%=UpdatePasswordCommand.ACTION_VIEW_UPDATE_PASSWORD%>");
}
</script>
</li>
<%
			}
		}

		//Application Maintenance
		if (showAppMentenance) {
%>
<li class="app-maintenance">
<span class="txt about-iplass"><c:out value="<%=titleAppMentenance%>"/></span>
<script>
$(function() {
	$("li.app-maintenance > span").on("click", function() {
		clearMenuState();
		submitForm(contextPath + "/<%=RevokeApplicationCommand.VIEW_ACTION_NAME%>");
	});
});
</script>
</li>
<%
		}

		//About
		String appName = application.getServletContextName();
		if (StringUtil.isEmpty(appName)) {
			appName = "iPLAss";
		}
%>
<c:set var="appName" value="<%=appName%>" />
<li class="about-iplass">
<span class="txt about-iplass">${m:rsp("mtp-gem-messages", "layout.header.about", appName)}</span>
<script>
$(function() {
	$("li.about-iplass > span").on("click", function() {
		var $this = $(this);
		var $dialogTrigger = getDialogTrigger($this.parent());

		$dialogTrigger.trigger("click");

		var isSubModal = $("body.modal-body").length != 0;
		var target = getModalTarget(isSubModal);
		var action = contextPath + "/<%=AboutCommand.ACTION_NAME%>";
		var $form = $("<form />").attr({method:"POST", action:action, target:target}).appendTo("body");
		if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
		$form.submit();
		$form.remove();
	});
});
</script>
</li>
<%
		if (!user.isAnonymous()) {
%>
<li class="logout">
<a href="javascript:void(0)" onclick="logout();return false;">${m:rs("mtp-gem-messages", "layout.header.logOut")}</a>
<script>
function logout() {
	if(window.confirm("${m:rs('mtp-gem-messages', 'layout.header.logOutConfirm')}")) {
		clearMenuState();
		document.location = contextPath + "/<%=LogoutCommand.ACTION_LOOUT%>";
	}
}
</script>
</li>
<%
		}
%>
</ul>
</li><%--account-01--%>

<%
		if (!fulltextEntities.isEmpty()) {
%>
<li class="divider"></li>
<li id="search">
<div class="fullsearch">
<form method="POST" action="">
<div class="selectbox">
<a href="javascript:void(0)" class="select"><span>${m:rs("mtp-gem-messages", "layout.header.search")}</span></a>
<div class="pulldown">
<a href="javascript:void(0)" onclick="changeFulltextSearch('')" >${m:rs("mtp-gem-messages", "layout.header.search")}</a>
<%
			for (Map.Entry<String, String> dispEntity : fulltextEntities.entrySet()) {
%>
<a href="javascript:void(0)" onclick="changeFulltextSearch('<%=StringUtil.escapeJavaScript(dispEntity.getKey())%>')" ><c:out value="<%=dispEntity.getValue() %>" /></a>
<%
			}
%>
</div>
<input type="hidden" value="" />
</div>
<p class="search-text">
<input type="hidden" id="fulltextsearch_type" value="" name="searchDefName" />
<input type="text" class="text" id="fulltextsearch_key" name="fulltextKey" onkeypress="if(event.keyCode == 13){fulltextSearch()}" />
<input type="text" style="display:none;"/>
<input type="button" class="search-submit" value="${m:rs('mtp-gem-messages', 'layout.header.search')}" onclick="fulltextSearch()"/>
</p>
<script>
function changeFulltextSearch(defName) {
	$("#fulltextsearch_type").val(defName);
}
function fulltextSearch() {
	var defName = $("#fulltextsearch_type").val();
	var key = $("#fulltextsearch_key").serialize();
	key = key.replace("fulltextKey=", "");

	if (typeof defName === "undefined" || defName == null) defName = "";
	if (typeof key === "undefined" || key == null) key = "";
	if (key != null && key != "") {
		clearMenuState();
		submitForm(contextPath + "/<%=FullTextSearchViewCommand.SEARCH_VIEW_ACTION_NAME %>", {"searchDefName":defName, "fulltextKey":key});
	}
}
</script>
</form>
</div>
</li>
<%
		}
%>
</ul><%--user-nav--%>
<%
	}
%>
</div><%--header--%>
</div><%--header-container--%>
