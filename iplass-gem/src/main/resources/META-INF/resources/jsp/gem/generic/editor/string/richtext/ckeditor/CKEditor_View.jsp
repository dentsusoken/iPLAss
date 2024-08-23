<%--
 Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
 
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

<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%
	// 詳細画面でのCKEditor適用

	boolean allowedContent = (boolean)request.getAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT);
	boolean hideToolBar = (boolean)request.getAttribute(Constants.EDITOR_RICHTEXT_HIDE_TOOL_BAR);
	boolean allowedLinkAction = (boolean)request.getAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_LINK_ACTION);
	String editorOption = (String)request.getAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION);
	String targetName = (String)request.getAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME);
%>
<script type="text/javascript">
$(function() {
	var defaults = {
		readOnly:true,
		allowedContent: <%=allowedContent%>,
		extraPlugins: "autogrow",
		autoGrow_onStartup: true,
		on: {
<%	if (hideToolBar) { %>
			instanceReady: function (evt) {
				<%-- 全体border、ツールバーを非表示 --%>
				var containerId = evt.editor.container.$.id;
				var editorId = evt.editor.id;
				$("#" + containerId).css("border", "none");
				$("#" + editorId + "_top").hide();
				$("#" + editorId + "_bottom").hide();
			},
<%	} %>
			contentDom: function (event) {
				var editor = event.editor;
<%	if (hideToolBar) { %>
				<%-- ツールバーを表示しない場合は、bodyのmargin削除 --%>
				var body = editor.document.getBody();
				body.setStyles({
				    margin: 0,
				});
<%	} %>
<%	if (allowedLinkAction) { %>
				<%-- Link制御 --%>
				var editable = editor.editable();
				editable.attachListener( editable, 'click', function( evt ) {
					var link = new CKEDITOR.dom.elementPath( evt.data.getTarget(), this ).contains('a');
					if ( link && evt.data.$.which == 1 && link.isReadOnly() ) {
						var target = link.getAttribute('target') ? link.getAttribute( 'target' ) : '_self';
						window.open(link.getAttribute('href'), target);
					}
				}, null, null, 15 );
<%	} %>
			},
		},
	};
	
<%	if (StringUtil.isNotBlank(editorOption)) { %>
	var custom = <%=editorOption%>;
<%	} else { %>
	var custom = {}; 
<%	} %>
	var option = $.extend(true, {}, defaults, custom);

	$("textarea[name='<%=StringUtil.escapeJavaScript(targetName)%>']").ckeditor(
		function() {}, option
	);
});
</script>