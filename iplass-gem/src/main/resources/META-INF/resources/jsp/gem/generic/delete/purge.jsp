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

<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.FormView"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil.TokenOutputType"%>
<%@ page import="org.iplass.gem.command.generic.delete.GetRecycleBinCommand"%>
<%@ page import="org.iplass.gem.command.generic.delete.PurgeCommand"%>
<%@ page import="org.iplass.gem.command.generic.delete.RestoreCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String defName = request.getParameter(Constants.DEF_NAME);
	String viewName = request.getParameter(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";
	String searchCond = request.getParameter(Constants.SEARCH_COND);
	if (searchCond == null) searchCond = "";

	FormView view = ViewUtil.getFormView(defName, viewName, true);
	String imageColor = ViewUtil.getEntityImageColor(view);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String iconTag = ViewUtil.getIconTag(view);

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	String back = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;
%>
<div class="trash">
<script src="${staticContentPath}/webjars/jquery-blockui/2.70/jquery.blockUI.js?cv=${apiVersion}"></script>
<script type="text/javascript">
$(function() {
	initData();
});
function initData(func) {
	getRecycleBin("<%=GetRecycleBinCommand.WEBAPI_NAME%>", function(list) {
		var $table = $("#recycleBinTable");
		$table.children("tr").remove();
		$(list).each(function() {
			var $tr = $("<tr />").appendTo($table);
			var $id = $("<td />").addClass("ac").appendTo($tr);
			$("<input />").attr({type:"checkbox", name:"rbid", value:this.recycleBinId}).appendTo($id);
			$("<td />").appendTo($tr).text(this.name);
			var date = new Date();
			date.setTime(this.updateDate);
			$("<td />").appendTo($tr).text(dateUtil.formatOutputDatetime(date, "sec"));
			$table.trigger("afterAddRbRow", [$tr.get(0)]);
		});
		// tableStripeクラスを定義しているtableにストライプを付ける
		$(".tableStripe").tableStripe();
		if (func && $.isFunction(func)) func.call(this);
		$(".fixHeight").fixHeight();
	});
	$('.allInput').allInputCheck();
}
function doRestore() {
	$(".error").text("");

	var checkedCount = $(":checkbox[name='rbid']:checked").length;
	if (checkedCount < 1) {
		$(".error").text(messageFormat(scriptContext.locale.notSelected));
		$('body, html').animate({ scrollTop: 0 }, 0);
		return;
	}

	$.blockUI({message: $("#blockLayer"), css: {width: "20px", left: "50%", top: "50%"}});
	setTimeout(function() {
		var rbid = [];
		$(":checkbox[name='rbid']:checked").each(function() {
			rbid.push($(this).val());
		});
		var token = $(":hidden[name='_t']").val();
		restore("<%=RestoreCommand.WEBAPI_NAME%>", rbid, token, function(message, errorRbid) {
			if (message == null) {
				// success
				initData(function() {
				});
			} else {
				// error
				var errorCol = $(":checkbox[name='rbid']").index($(":checkbox[name='rbid'][value='" + errorRbid + "']")) + 1;
				$(".error").text(messageFormat(scriptContext.locale.line, errorCol, message));
				$('body, html').animate({ scrollTop: 0 }, 0);
			}
			$.unblockUI();
		}, function() {
			$.unblockUI();
		});
	}, 1000);
}
function doPurge() {
	$(".error").text("");

	var checkedCount = $(":checkbox[name='rbid']:checked").length;
	if (checkedCount < 1) {
		$(".error").text(messageFormat(scriptContext.locale.notSelected));
		$('body, html').animate({ scrollTop: 0 }, 0);
		return;
	}

	$.blockUI({message: $("#blockLayer"), css: {width: "20px", left: "50%", top: "50%"}});
	setTimeout(function() {
		var rbid = [];
		$(":checkbox[name='rbid']:checked").each(function() {
			rbid.push($(this).val());
		});
		var token = $(":hidden[name='_t']").val();
		purge("<%=PurgeCommand.WEBAPI_NAME%>", rbid, token, function() {
			initData(function() {
				$.unblockUI();
			});
		}, function() {
			$.unblockUI();
		});
	}, 1000);
}
function back() {
	submitForm(contextPath + "/<%=StringUtil.escapeJavaScript(back)%>", {
			searchCond:$(":hidden[name='searchCond']").val()
		});
}
</script>
<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<%if (StringUtil.isNotBlank(iconTag)) {%>
<%=iconTag%>
<%} else {%>
<i class="far fa-circle default-icon"></i>
<%} %>
</span>
${m:rs("mtp-gem-messages", "generic.delete.purge.trash")}
</h2>
<span class="error"></span>
<ul class="link-list-01 mb05 flat-block-top">
<li><a href="javascript:void(0)" onclick="back()">${m:rs("mtp-gem-messages", "generic.delete.purge.back")}</a></li>
</ul>
<form method="POST" action="">
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>">
<input type="hidden" name="viewName" value="<c:out value="<%=viewName%>"/>">
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>">
${m:outputToken('FORM_XHTML', false)}
<table class="tbl-trash tableStripe">
<thead>
<tr>
<th class="col1" nowrap="nowrap"><input type="checkbox" class="allInput" /></th>
<th class="col2 left" nowrap="nowrap">${m:rs("mtp-gem-messages", "generic.delete.purge.name")}</th>
<th class="col3 left" nowrap="nowrap">${m:rs("mtp-gem-messages", "generic.delete.purge.delDate")}</th>
</tr>
</thead>
<tbody class="selectCheck" id="recycleBinTable">
</tbody>
</table>
<p>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.delete.purge.undo')}" class="gr-btn-02" onclick="doRestore()" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.delete.purge.delFromTrash')}" class="gr-btn-02" onclick="doPurge()" />
</p>
</form>
</div>
<div id="blockLayer" style="display:none;"><p class="loading"></p></div>
