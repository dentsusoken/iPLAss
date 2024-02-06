<%--
 Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
 
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager"%>
<%@ page import="org.iplass.mtp.definition.DefinitionSummary"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants" %>

<%!
	DefinitionSummary getValue(List<DefinitionSummary> list, String value) {
		if (value == null) return null;
		for (DefinitionSummary tmp : list) {
			if (value.equals(tmp.getName())) {
				return tmp;
			}
		}
		return null;
	}
%>

<%
	String propValue = (String) request.getAttribute(Constants.EDITOR_PROP_VALUE);

	AuthenticationPolicyDefinitionManager manager = ManagerLocator.getInstance().getManager(AuthenticationPolicyDefinitionManager.class);
	List<DefinitionSummary> definitionNames = manager.definitionSummaryList();

	DefinitionSummary dn = getValue(definitionNames, propValue);
	String label = dn != null ? dn.getDisplayName() != null ? dn.getDisplayName() : dn.getName() : "";
	String displayName = "";
	if (StringUtil.isNotEmpty(label) && dn != null) {
		displayName = TemplateUtil.getMultilingualString(label, manager.get(dn.getName()).getLocalizedDisplayNameList());
	} else if (StringUtil.isNotEmpty(propValue)) {
		// 紐付け後に消された場合はそのまま出力
		displayName = propValue;
	}
%>
<span class="data-label">
<c:out value="<%=displayName %>"/>
</span>