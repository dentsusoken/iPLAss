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

<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.element.Element" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.VersionSection" %>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.GetVersionCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	Boolean isDialog = (Boolean) request.getAttribute(Constants.DIALOG_MODE);

	EntityDefinition ed = data.getEntityDefinition();

	//詳細表示のみ
	if (type != OutputType.VIEW) return;

	if (isDialog != null && isDialog) return;

	//バージョンベースのデータのみ
	if (ed.getVersionControlType() == VersionControlType.NONE) return;

	String defName = ed.getName();
	String viewName = data.getView().getName();
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);
	
	VersionSection section = (VersionSection) element;
	String id = "id=\"version_section\"";
	if (StringUtil.isNotBlank(section.getId())) {
		id = "id=\"" + StringUtil.escapeHtml(section.getId()) + "\"";
	}

	String style = "";
	if (StringUtil.isNotBlank(section.getStyle())) {
		style = section.getStyle();
	}

	String title = GemResourceBundleUtil.resourceString("generic.element.section.VersionSection.anotherVer");
	if (StringUtil.isNotBlank(section.getTitle())) {
		title = TemplateUtil.getMultilingualString(section.getTitle(), section.getLocalizedTitleList());
	}
%>
<div <%=id %> class="version-section <c:out value="<%=style %>"/>">
<script type="text/javascript">
$(function() {
	$("div.other-version").off("click");
	$("div.other-version").on("click", function() {
		if($(this).hasClass("disclosure-close")) {
			$(this).removeClass("disclosure-close").next().show(0, function() {
				var defName = "${m:escJs(defName)}";
				var viewName = "${m:escJs(viewName)}";
				var oid = $(":hidden[name='oid']").val();
				var version = $(":hidden[name='version']").val();
				getVersion("<%=GetVersionCommand.WEBAPI_NAME%>", defName, viewName, oid, version, function(entities) {
					$(entities).each(function() {
						var name = this.name;
						var version = this.version;
						var $a = $("<a href='javascript:void(0)'>" + name + "(" + version + ")" + "</a>").on("click", function() {
							$(":hidden[name='version']").val(version);
							var urlPath = "<%=StringUtil.escapeJavaScript(urlPath)%>" + "/" + oid;
							var $form = $("#detailForm");
							// 特定バージョンの詳細画面であることを示すフラグを設定
							$form.append("<input type='hidden' name='<%=Constants.VERSION_SPECIFIED%>' value='true' />");
							$form.attr("action", contextPath + "/<%=DetailViewCommand.VIEW_ACTION_NAME%>" + urlPath);
							$form.submit();
						});
						$("<li />").appendTo("ul.other-version-list").append($a);
					});
					$(".fixHeight").fixHeight();
				});
			});
		} else {
			$(this).addClass("disclosure-close").next().hide();
			$(".fixHeight").fixHeight();
		}
	});
});
</script>

<div class="hgroup-03 sechead other-version disclosure-close">
<h3><span><c:out value="<%=title %>"/></span></h3>
</div>

<div class="version-block" style="display:none;">
<table class="tbl-version">
<tr>
<td>
<ul class="other-version-list">
</ul>
</td>
</tr>
</table>
</div>
</div>
