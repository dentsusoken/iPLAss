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
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	//表示判定スクリプトバインド用エンティティ
	Entity rootEntity = request.getAttribute(Constants.ROOT_ENTITY) instanceof Entity ? (Entity) request.getAttribute(Constants.ROOT_ENTITY) : null;

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);
	Button button = (Button) element;
	String onclick = button.getOnclickEvent() != null ? button.getOnclickEvent() : "";

	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);

	boolean isDispBitton = false;
	if (EntityViewUtil.isDisplayElement(rootDefName, button.getElementRuntimeId(), type, rootEntity)
			&& (type != OutputType.EDIT || ViewUtil.dispElement(button))) {
		if (button.getDisplayType() == DisplayType.CUSTOM) {
			//スクリプトで判定
			isDispBitton = evm.isDisplayButton(rootDefName, button.getCustomDisplayTypeScriptKey(), type, entity);
		} else {
			if (type == OutputType.EDIT) {
				if (button.getDisplayType() == null
						|| button.getDisplayType() == DisplayType.DETAIL
						|| button.getDisplayType() == DisplayType.BOTH) {
					//未指定or編集or両方
					isDispBitton = true;
				}
			} else if (type == OutputType.VIEW) {
				if (button.getDisplayType() != null
						&& (button.getDisplayType() == DisplayType.VIEW
							|| button.getDisplayType() == DisplayType.BOTH)) {
					//指定and(表示or両方)
					isDispBitton = true;
				}
			}
		}
	}

	//列数で幅調整
	if (colNum == null || colNum < 1) {
		colNum = 1;
	}
	String cellStyle = "section-data col" + colNum;

	String title = TemplateUtil.getMultilingualString(button.getTitle(), button.getLocalizedTitleList());
	String label = TemplateUtil.getMultilingualString(button.getDisplayLabel(), button.getLocalizedDisplayLabelList());

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(button.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, button.getInputCustomStyleScriptKey(), entity, null);
	}
%>
<th class="<c:out value="<%=cellStyle%>"/>"><c:out value="<%=title %>"/></th>
<td class="<c:out value="<%=cellStyle%>"/>">
<%
	if (isDispBitton) {
		String cssClass = button.isPrimary() ? "gr-btn" : "gr-btn-02";
		if (StringUtil.isNotBlank(button.getStyle())) {
			cssClass += " " + button.getStyle();
		}
%>
<%-- XSS対応-メタの設定のため対応なし(onclick) --%>
<input type="button" value="<c:out value="<%=label %>"/>" class="<c:out value="<%=cssClass %>"/>" style="<c:out value="<%=customStyle%>"/>" onclick="<%=onclick %>" />
<%
	}
%>
</td>
