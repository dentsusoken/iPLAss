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

<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.*"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%
	//データ取得
	@SuppressWarnings("unchecked") List<Section> sections = (List<Section>) request.getAttribute(Constants.NAV_SECTION);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
%>
<ul class="nav-section">
<%
	for (int i = 0; i < sections.size(); i++) {
		Section section = sections.get(i);
		if (!section.isShowLink()) continue;
		if (section instanceof DefaultSection) {
			if (((DefaultSection) section).getId() == null) {
				((DefaultSection) section).setId("section-" + i);
			}

			String title = TemplateUtil.getMultilingualString(((DefaultSection) section).getTitle(), ((DefaultSection) section).getLocalizedTitleList());
%>
<li>
<a href="#<c:out value="<%=((DefaultSection) section).getId() %>"/>" ><c:out value="<%=title %>"/></a>
</li>
<%
		} else  if (section instanceof ReferenceSection) {
			if (((ReferenceSection) section).getId() == null) {
				((ReferenceSection) section).setId("section-" + i);
			}

			String title = TemplateUtil.getMultilingualString(((ReferenceSection) section).getTitle(), ((ReferenceSection) section).getLocalizedTitleList());
%>
<li>
<a href="#<c:out value="<%=((ReferenceSection) section).getId() %>"/>" ><c:out value="<%=title %>"/></a>
</li>
<%
		} else  if (section instanceof MassReferenceSection) {
			if (((MassReferenceSection) section).getId() == null) {
				((MassReferenceSection) section).setId("section-" + i);
			}

			String title = TemplateUtil.getMultilingualString(((MassReferenceSection) section).getTitle(), ((MassReferenceSection) section).getLocalizedTitleList());
%>
<li>
<a href="#<c:out value="<%=((MassReferenceSection) section).getId() %>"/>" ><c:out value="<%=title %>"/></a>
</li>
<%
		} else  if (section instanceof ScriptingSection) {
			if (((ScriptingSection) section).getId() == null) {
				((ScriptingSection) section).setId("section-" + i);
			}

			String title = TemplateUtil.getMultilingualString(((ScriptingSection) section).getTitle(), ((ScriptingSection) section).getLocalizedTitleList());
%>
<li>
<a href="#<c:out value="<%=((ScriptingSection) section).getId() %>"/>" ><c:out value="<%=title %>"/></a>
</li>
<%
		} else  if (section instanceof TemplateSection) {
			if (((TemplateSection) section).getId() == null) {
				((TemplateSection) section).setId("section-" + i);
			}

			String title = TemplateUtil.getMultilingualString(((TemplateSection) section).getTitle(), ((TemplateSection) section).getLocalizedTitleList());
%>
<li>
<a href="#<c:out value="<%=((TemplateSection) section).getId() %>"/>" ><c:out value="<%=title %>"/></a>
</li>
<%
		} else  if (section instanceof VersionSection) {
			if (OutputType.VIEW == type) {
				String title = GemResourceBundleUtil.resourceString("generic.detail.sectionNavi.diffVersion");
				if (StringUtil.isNotBlank(((VersionSection) section).getTitle())) {
					title = TemplateUtil.getMultilingualString(((VersionSection) section).getTitle(), ((VersionSection) section).getLocalizedTitleList());
				}
%>
<li>
<a href="#version_section" ><c:out value="<%=title %>"/></a>
</li>
<%
			}
		}
	}
%>
</ul>
