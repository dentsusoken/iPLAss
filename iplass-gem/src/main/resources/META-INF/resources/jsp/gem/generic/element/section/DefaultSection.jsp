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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="java.util.*" %>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.util.*" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.property.PropertyItem"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	boolean checkDispElement(OutputType type, Element elem) {
		boolean dispElement = true;
		if (elem instanceof PropertyItem) {
			PropertyItem prop = (PropertyItem) elem;
			dispElement = isDisp(type, prop.isHideDetail(), prop.isHideView());
		} else if (elem instanceof ScriptingElement) {
			ScriptingElement se = (ScriptingElement) elem;
			dispElement = isDisp(type, se.isHideDetail(), se.isHideView());
		} else if (elem instanceof TemplateElement) {
			TemplateElement te = (TemplateElement) elem;
			dispElement = isDisp(type, te.isHideDetail(), te.isHideView());
		} else if (elem instanceof VirtualPropertyItem) {
			VirtualPropertyItem prop = (VirtualPropertyItem) elem;
			dispElement = isDisp(type, prop.isHideDetail(), prop.isHideView());
		} else if (elem instanceof DefaultSection) {
			DefaultSection sec = (DefaultSection) elem;
			dispElement = isDisp(type, sec.isHideDetail(), sec.isHideView());
		} else if (elem instanceof ReferenceSection) {
			ReferenceSection sec = (ReferenceSection) elem;
			dispElement = isDisp(type, sec.isHideDetail(), sec.isHideView());
		} else if (elem instanceof MassReferenceSection) {
			MassReferenceSection sec = (MassReferenceSection) elem;
			dispElement = isDisp(type, sec.isHideDetail(), sec.isHideView());
		} else if (elem instanceof ScriptingSection) {
			ScriptingSection sec = (ScriptingSection) elem;
			dispElement = isDisp(type, sec.isHideDetail(), sec.isHideView());
		} else if (elem instanceof TemplateSection) {
			TemplateSection sec = (TemplateSection) elem;
			dispElement = isDisp(type, sec.isHideDetail(), sec.isHideView());
		}
		return dispElement;
	}
	boolean isDisp(OutputType type, boolean hideDetail, boolean hideView) {
		if (type == OutputType.EDIT) {
			return !hideDetail;
		} else if (type == OutputType.VIEW) {
			return !hideView;
		} else if (type == OutputType.BULK) {
			return !hideDetail;
		}
		return false;
	}
%>
<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	Object value = request.getAttribute(Constants.ENTITY_DATA);
	Entity entity = value instanceof Entity ? (Entity)value : null;
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	String execType = (String)request.getAttribute(Constants.EXEC_TYPE);
	boolean isInsert = Constants.EXEC_TYPE_INSERT.equals(execType);

	String defName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);

	//表示判定スクリプトバインド用エンティティ
	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);

	DefaultSection section = (DefaultSection) element;

	if ((type == OutputType.EDIT && section.isHideDetail())
			|| (type == OutputType.VIEW && section.isHideView())) return;

	//新規の場合、システム項目は非表示
	if (isInsert && Constants.AUTO_GENERATE_DETAIL_SYSTEM_SECTION_CSS_CLASS.equals(section.getStyle())) {
		return;
	}

	EntityViewManager evm = ManagerLocator.manager(EntityViewManager.class);

	//列数で幅調整
	if (section.getColNum() == 0) {
		section.setColNum(1);
	}
	String colNumCellStyle = "col" + section.getColNum();
	String cellStyle = "section-data " + colNumCellStyle;

	int rowNum = section.getElements().size() / section.getColNum();
	if (section.getElements().size() % section.getColNum() > 0) {
		//割り切れなければ1行追加
		rowNum++;
	}

	String id = "";
	if (StringUtil.isNotBlank(section.getId())) {
		id = section.getId();
	}

	String style = "";
	if (StringUtil.isNotBlank(section.getStyle())) {
		style = section.getStyle();
	}

	String disclosure = "";
	String disclosureStyle = "";
	if (!section.isExpandable()) {
		disclosure = " disclosure-close";
		disclosureStyle = "display: none;";
	}

	//詳細編集/詳細表示で表示する項目の抽出(非表示の場合ブランク扱い)
	List<Element> elementList = new ArrayList<>();
	List<Element> hiddenList = new ArrayList<>();
	for (Element subElement : section.getElements()) {
		if (checkDispElement(type, subElement)) {
			if (subElement instanceof PropertyItem) {
				PropertyItem property = (PropertyItem) subElement;
				//hiddenはレイアウトを保持するためBlankSpaceに置き換えたうえで退避
				if (property.getEditor() != null && property.getEditor().isHide()) {
					elementList.add(new BlankSpace());
					hiddenList.add(property);
				} else {
					elementList.add(property);
				}
			} else if (subElement instanceof VirtualPropertyItem) {
				VirtualPropertyItem property = (VirtualPropertyItem) subElement;
				//hiddenはレイアウトを保持するためBlankSpaceに置き換えたうえで退避
				if (property.getEditor() != null && property.getEditor().isHide()) {
					elementList.add(new BlankSpace());
					hiddenList.add(property);
				} else {
					elementList.add(property);
				}
			} else {
				elementList.add(subElement);
			}
		} else {
			BlankSpace blank = new BlankSpace();
			blank.setDispFlag(false);
			elementList.add(blank);
		}
	}

	//先頭行にインナーセクションが含まれるか
	boolean hasSectionInFirstRow = false;
	for (int i = 0; i < section.getColNum(); i++) {
		Element subElement = elementList.get(i);
		if (subElement instanceof Section) {
			hasSectionInFirstRow = true;
		}
	}

	String title = TemplateUtil.getMultilingualString(section.getTitle(), section.getLocalizedTitleList());

	//カスタムスタイル用のSectionKEYをセット
	request.setAttribute(Constants.SECTION_SCRIPT_KEY, section.getStyleScriptKey());
%>
<div id="<c:out value="<%=id %>"/>" class="<c:out value="<%=style %>"/>">
<div class="hgroup-03 sechead<c:out value="<%=disclosure %>"/>">

<h3><span><c:out value="<%=title %>"/></span></h3>
</div>
<div style="<c:out value="<%=disclosureStyle %>"/>">
<%
	if (StringUtil.isNotBlank(section.getUpperContents())) {
		evm.executeTemplate(defName, section.getContentScriptKey() + "_UpperContent", request, response, application, pageContext);
	}
%>
<table class="tbl-section">
<%
	if (hasSectionInFirstRow) {
		//列幅崩れ回避のダミー行出力
%>
<thead>
<tr class="layout-row">
<%
		for (int i = 0; i < section.getColNum(); i++) {
%>
<th class="section-data <c:out value="<%=colNumCellStyle %>"/>"></th>
<td class="section-data <c:out value="<%=colNumCellStyle %>"/>"></td>
<%
		}
%>
</tr>
</thead>
<%
	}
	int index = 0;
	for (int i = 0; i < rowNum; i++) {
		//行内のElementがすべて非表示になってないかチェック
		boolean isDispRow = false;
		int _index = index;
		for (int j = 0; j < section.getColNum(); j++) {
			if (elementList.size() > _index) {
				Element subElement = elementList.get(_index++);
				if (!(subElement instanceof BlankSpace)
						&& EntityViewUtil.isDisplayElement(defName, subElement.getElementRuntimeId(), type, rootEntity)
						&& (type != OutputType.EDIT || ViewUtil.dispElement(subElement))) {
					isDispRow = true;
					break;
				}
			}
		}
		if (!isDispRow) {
			index += section.getColNum();
			continue;
		}
%>
<tr>
<%
		for (int j = 0; j < section.getColNum(); j++) {
			if (elementList.size() > index) {
				Element subElement = elementList.get(index++);
				if (EntityViewUtil.isDisplayElement(defName, subElement.getElementRuntimeId(), type, rootEntity)
						&& (type != OutputType.EDIT || ViewUtil.dispElement(subElement))) {
					request.setAttribute(Constants.ELEMENT, subElement);
					request.setAttribute(Constants.COL_NUM, section.getColNum());

					String path = EntityViewUtil.getJspPath(subElement, ViewConst.DESIGN_TYPE_GEM);
					if (path != null) {
						boolean isSection = subElement instanceof Section;
						if (isSection){
							String sectionStyle = cellStyle;
							if (subElement instanceof ScriptingSection && !((ScriptingSection) subElement).isDispBorderInSection()) {
								sectionStyle = colNumCellStyle;
							} else if (subElement instanceof TemplateSection && !((TemplateSection) subElement).isDispBorderInSection()) {
								sectionStyle = colNumCellStyle;
							}
%>
<td class="inner-section <c:out value="<%=sectionStyle %>"/>" colspan="2">
<%
						}
%>
<jsp:include page="<%=path %>" />
<%
						if (isSection){
%>
</td>
<%
						}
					}
				} else {
%>
<th class="<c:out value="<%=cellStyle %>"/>"></th>
<td class="<c:out value="<%=cellStyle %>"/>"></td>
<%
				}
			}
		}
%>
</tr>
<%
	}
%>
</table>
<div class="hidden-input-area">
<%
	//hidden出力
	for (Element hiddenElement : hiddenList) {
		if (EntityViewUtil.isDisplayElement(defName, hiddenElement.getElementRuntimeId(), type, rootEntity)
				&& (type != OutputType.EDIT || ViewUtil.dispElement(hiddenElement))) {
			request.setAttribute(Constants.ELEMENT, hiddenElement);
			request.setAttribute(Constants.COL_NUM, section.getColNum());

			String path = EntityViewUtil.getJspPath(hiddenElement, ViewConst.DESIGN_TYPE_GEM);
			if (path != null) {
%>
<jsp:include page="<%=path %>" />
<%
			}
		}
	}
%>
</div>

<%
	if (StringUtil.isNotBlank(section.getLowerContents())) {
		evm.executeTemplate(defName, section.getContentScriptKey() + "_LowerContent", request, response, application, pageContext);
	}
%>
</div>
</div>
