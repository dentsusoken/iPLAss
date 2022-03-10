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

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.function.Supplier"%>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@page import="org.iplass.gem.command.generic.detail.DetailCommandContext"%>
<%@page import="org.iplass.gem.command.generic.detail.DetailFormViewData"%>
<%@page import="org.iplass.gem.command.generic.detail.LoadEntityInterrupterHandler"%>
<%@page import="org.iplass.mtp.ApplicationException"%>
<%@page import="org.iplass.mtp.ManagerLocator"%>
<%@page import="org.iplass.mtp.auth.AuthContext"%>
<%@page import="org.iplass.mtp.entity.Entity"%>
<%@page import="org.iplass.mtp.entity.EntityManager"%>
<%@page import="org.iplass.mtp.entity.LoadOption"%>
<%@page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@page import="org.iplass.mtp.entity.definition.validations.NotNullValidation"%>
<%@page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@page import="org.iplass.mtp.entity.definition.ValidationDefinition"%>
<%@page import="org.iplass.mtp.entity.query.Query"%>
<%@page import="org.iplass.mtp.entity.query.condition.expr.And"%>
<%@page import="org.iplass.mtp.entity.query.condition.predicate.Equals"%>
<%@page import="org.iplass.mtp.util.StringUtil" %>
<%@page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@page import="org.iplass.mtp.view.generic.LoadEntityContext"%>
<%@page import="org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType"%>
<%@page import="org.iplass.mtp.view.generic.OutputType"%>
<%@page import="org.iplass.mtp.view.generic.RequiredDisplayType"%>
<%@page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@page import="org.iplass.mtp.view.generic.element.Element" %>
<%@page import="org.iplass.mtp.view.generic.element.section.ReferenceSection" %>
<%@page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%!
	LoadEntityInterrupterHandler getLoadEntityInterrupterHandler(EntityManager em, EntityDefinitionManager edm, EntityViewManager evm) {
		DetailCommandContext context = new DetailCommandContext(TemplateUtil.getRequestContext(), em, edm);//ここでこれを作るのはちょっと微妙だが・・・
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));
		return context.getLoadEntityInterrupterHandler();
	}
	LoadOption getOption(EntityDefinition ed, ReferenceSection section) {
		List<String> propList = new ArrayList<String>();
		for (NestProperty nProp : section.getProperties()) {
			PropertyDefinition refPd = ed.getProperty(nProp.getPropertyName());
			if (refPd instanceof ReferenceProperty) {
				propList.add(refPd.getName());
			}
		}
		return new LoadOption(propList);
	}
	int getOrderPropertValue(ReferenceSection section, Entity entity, EntityManager em) {
		Query query = new Query().select(section.getOrderPropName())
				.from(entity.getDefinitionName())
				.where(new And(
						new Equals(Entity.OID, entity.getOid()),
						new Equals(Entity.VERSION, entity.getVersion())));
		Object[] ret = em.search(query).getFirst();
		return ret.length > 0 ? toInteger(ret[0]) : -1;
	}
	int toInteger(Object val) {
		if (val instanceof Integer) {
			return (Integer) val;
		} else if (val instanceof Long) {
			return ((Long) val).intValue();
		} else if (val instanceof Float) {
			return ((Float) val).intValue();
		} else if (val instanceof Double) {
			return ((Double) val).intValue();
		} else if (val instanceof BigDecimal) {
			return ((BigDecimal) val).intValue();
		}
		return -1; // 数値以外
	}
%>
<%
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);

	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	Object value = request.getAttribute(Constants.ENTITY_DATA);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);
	String viewName = (String) request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";
	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);

	ReferenceSection section = (ReferenceSection) element;
	ReferenceProperty rp = (ReferenceProperty) ed.getProperty(section.getPropertyName());

	if ((type == OutputType.EDIT && section.isHideDetail())
			|| (type == OutputType.VIEW && section.isHideView())) return;

	//ロード処理のinterrupter
	final EntityManager em = ManagerLocator.manager(EntityManager.class);
	EntityDefinitionManager edm = ManagerLocator.manager(EntityDefinitionManager.class);
	EntityViewManager evm = ManagerLocator.manager(EntityViewManager.class);

	LoadEntityInterrupterHandler handler = getLoadEntityInterrupterHandler(em, edm, evm);

	Entity parent = value instanceof Entity ? (Entity) value : null;
	Entity entity = null;
	if (parent != null && parent.getValue(section.getPropertyName()) != null) {
		Object val = parent.getValue(section.getPropertyName());
		if (rp.getMultiplicity() == 1) {
			// 多重度1はそのまま利用
			if (val instanceof Entity) {
				entity = (Entity) val;
			} else if (val instanceof Entity[]) {
				Entity[] ary = (Entity[]) val;
				entity = ary.length > 0 ? ary[0] : null;
			}
		} else {
			// 多重度1以外は表示順プロパティとデータのインデックスに従う
			if (val instanceof Entity) {
				// ここで配列以外は無いはず・・・
			} else if (val instanceof Entity[]) {
				if (section.getOrderPropName() != null) {
					Entity[] ary = (Entity[]) val;
					for (Entity _entity : ary) {
						int order = getOrderPropertValue(section, _entity, em);
						if (order == section.getIndex()) {
							entity = _entity;
							break;
						}
					}
				}
			}
		}
	}

	int dataIndex = 0;//同一プロパティのものがあるか
	if (request.getAttribute(section.getPropertyName() + "_dataIndex") != null) {
		dataIndex = (Integer) request.getAttribute(section.getPropertyName() + "_dataIndex");
	}

	Boolean reload = false;
	if (entity != null) {
		reload = entity.getValue(Constants.REF_RELOAD);
		if (entity.getOid() != null && (reload == null || reload)) {

			final Entity tmp = entity;
			LoadOption loadOption = getOption(edm.get(section.getDefintionName()), section);

			final LoadEntityContext leContext = handler.beforeLoadReference(entity.getDefinitionName(), loadOption, rp, LoadType.VIEW);
			if (leContext.isDoPrivileged()) {
				entity = AuthContext.doPrivileged(new Supplier<Entity>() {

					@Override
					public Entity get() {
						return em.load(tmp.getOid(), tmp.getVersion(), tmp.getDefinitionName(), leContext.getLoadOption());
					}
				});
			} else {
				entity = em.load(tmp.getOid(), tmp.getVersion(), tmp.getDefinitionName(), leContext.getLoadOption());
			}
			handler.afterLoadReference(entity, loadOption, rp, LoadType.VIEW);
		}
	}

	EntityDefinition red = edm.get(section.getDefintionName());

	//列数で幅調整
	if (section.getColNum() == 0) {
		section.setColNum(1);
	}
	String cellStyle = "section-data col" + section.getColNum();

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

	String title = TemplateUtil.getMultilingualString(section.getTitle(), section.getLocalizedTitleList());

	//定義名を参照型のものに置き換える、後でdefNameに戻す
	String defName = (String) request.getAttribute(Constants.DEF_NAME);
	request.setAttribute(Constants.DEF_NAME, section.getDefintionName());

	//出力対象の選定
	//ReferenceSectionのNestは複数列の場合にblankなどでレイアウト調整できないので、
	//表示対象でないものは除外して、列を詰めていく
	List<NestProperty> nestList = new ArrayList<>();
	List<NestProperty> hiddenList = new ArrayList<>();
	for (NestProperty property : section.getProperties()) {
		if (property.getEditor() != null
				&& ((type == OutputType.EDIT && !property.isHideDetail())
					|| (type == OutputType.VIEW && !property.isHideView()))) {
			//出力対象

			String propName = property.getPropertyName();
			PropertyDefinition pd = red.getProperty(propName);
			if (pd.getMultiplicity() != 1) {
				//参照セクションには多重度1しかおかせない
				continue;
			} else {
				if (property.getEditor().isHide()) {
					//hiddenは別途出力
					hiddenList.add(property);
				} else {
					nestList.add(property);
				}
			}
		}
	}

%>

<div id="<c:out value="<%=id %>"/>" class="<c:out value="<%=style %>"/>">
<div class="hgroup-03 sechead<c:out value="<%=disclosure %>"/>">
<h3><span><c:out value="<%=title %>"/></span></h3>
</div>
<div style="<c:out value="<%=disclosureStyle %>"/>">
<%
	if (StringUtil.isNotBlank(section.getUpperContents())) {
		String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
		evm.executeTemplate(rootDefName, section.getContentScriptKey() + "_UpperContent", request, response, application, pageContext);
	}
	if (dataIndex == 0) {
	//一度だけ参照セクションかを判断するフラグ出力
%>
<input type="hidden" name="isReferenceSection_<c:out value="<%=section.getPropertyName()%>"/>" value="true">
<%
	}
	if (rp.getMultiplicity() != 1) {
%>
<input type="hidden" name="referenceSectionIndex_<c:out value="<%=section.getPropertyName()%>"/>[<%=dataIndex%>]" value="<%=section.getIndex()%>">
<%
	}
%>
<table class="tbl-section multi-col" data-colNum=<%=section.getColNum() %>>
<%
	for (NestProperty property : nestList) {
%>
<tr class="col">
<%
		String propName = property.getPropertyName();
		PropertyDefinition pd = red.getProperty(propName);

		property.getEditor().setPropertyName(section.getPropertyName() + "[" + dataIndex + "]." + propName);

		String description = "";
		if (StringUtil.isNotBlank(property.getDescription())) {
			description = TemplateUtil.getMultilingualString(property.getDescription(), property.getLocalizedDescriptionList());
		}
		boolean showDesc = OutputType.EDIT == type && description != null && description.length() > 0;

		String nestDisplayName = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList());
		String displayName = TemplateUtil.getMultilingualString(pd.getDisplayName(), pd.getLocalizedDisplayNameList());
		String displayLabel = nestDisplayName != null ? nestDisplayName : displayName;
%>
<th id="id_th_<c:out value="<%=propName %>"/>" class="<c:out value="<%=cellStyle %>"/>">
<%-- XSS対応-メタの設定のため対応なし(displayLabel) --%>
<%=displayLabel %>
<%
		if (OutputType.EDIT == type) {
			boolean required = false;
			RequiredDisplayType requiredType = property.getRequiredDisplayType();
			if (requiredType == null) requiredType = RequiredDisplayType.DEFAULT;
			if (requiredType == RequiredDisplayType.DEFAULT) {
				if (pd.getValidations() != null) {
					for (ValidationDefinition validation : pd.getValidations()) {
						if (validation instanceof NotNullValidation) {
							required = true;
						}
					}
				}
			} else if (requiredType == RequiredDisplayType.DISPLAY) {
				required = true;
			}
			if (required) {
%>
<span class="ico-required ml10 vm">${m:rs("mtp-gem-messages", "generic.element.section.ReferenceSection.required")}</span>
<%
			}
			String tooltip = "";
			if (StringUtil.isNotBlank(property.getTooltip())) {
				tooltip = TemplateUtil.getMultilingualString(property.getTooltip(), property.getLocalizedTooltipList());
			}
			if (StringUtil.isNotBlank(tooltip)) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp"  title="<%=tooltip %>" /></span>
<%
			}
		}
%>
</th>
<td id="id_td_<c:out value="<%=propName %>"/>" class="<c:out value="<%=cellStyle %>"/> property-data">
<%
		if (showDesc) {
%>
<p class="mb05">
<%
		}
		String path =  EntityViewUtil.getJspPath(property.getEditor(), ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
			Object propValue = entity != null ? entity.getValue(propName) : null;
			request.setAttribute(Constants.EDITOR_EDITOR, property.getEditor());
			request.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
			request.setAttribute(Constants.EDITOR_DISPLAY_LABEL, displayLabel);
			request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
			request.setAttribute(Constants.EDITOR_REF_NEST, true);
			request.setAttribute(Constants.EDITOR_REF_NEST_VALUE, entity);//JoinProperty用
			request.setAttribute(Constants.AUTOCOMPLETION_SETTING, property.getAutocompletionSetting());
			request.setAttribute(Constants.REF_SECTION_INDEX, new Integer(dataIndex));
%>
<jsp:include page="<%=path %>" />
<%
			request.removeAttribute(Constants.EDITOR_EDITOR);
			request.removeAttribute(Constants.EDITOR_PROP_VALUE);
			request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
			request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
			request.removeAttribute(Constants.EDITOR_REF_NEST);
			request.removeAttribute(Constants.EDITOR_REF_NEST_VALUE);
			request.removeAttribute(Constants.REF_SECTION_INDEX);
		}
		if (showDesc) {
%>
</p>
<%-- XSS対応-メタの設定のため対応なし(description) --%>
<p class="explanation"><%=description %></p>
<%
		}
		if (property.getAutocompletionSetting() != null) {
			request.setAttribute(Constants.AUTOCOMPLETION_DEF_NAME, ed.getName());
			request.setAttribute(Constants.AUTOCOMPLETION_VIEW_NAME, viewName);
			request.setAttribute(Constants.AUTOCOMPLETION_PROP_NAME, property.getPropertyName());
			request.setAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY, 1);
			request.setAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME, section.getPropertyName());
			request.setAttribute(Constants.AUTOCOMPLETION_REF_SECTION_INDEX, new Integer(dataIndex));
			request.setAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA, rootEntity);

			String autocompletionPath = "/jsp/gem/generic/common/ReferenceSectionAutocompletion.jsp";
%>
<jsp:include page="<%=autocompletionPath %>"/>
<%
			request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
			request.removeAttribute(Constants.AUTOCOMPLETION_SETTING);
			request.removeAttribute(Constants.AUTOCOMPLETION_DEF_NAME);
			request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_NAME);
			request.removeAttribute(Constants.AUTOCOMPLETION_PROP_NAME);
			request.removeAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
			request.removeAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME);
			request.removeAttribute(Constants.AUTOCOMPLETION_REF_SECTION_INDEX);
			request.removeAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
			request.removeAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
		}
%>
</td>
</tr>
<%
	}
%>
</table>
<div class="hidden-input-area">
<%
	for (NestProperty property : hiddenList) {
		String propName = property.getPropertyName();
		PropertyDefinition pd = red.getProperty(propName);
		property.getEditor().setPropertyName(section.getPropertyName() + "[" + dataIndex + "]." + propName);

		String path =  EntityViewUtil.getJspPath(property.getEditor(), ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
			Object propValue = entity != null ? entity.getValue(propName) : null;
			request.setAttribute(Constants.EDITOR_EDITOR, property.getEditor());
			request.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
			request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
			request.setAttribute(Constants.EDITOR_REF_NEST, true);
			request.setAttribute(Constants.EDITOR_REF_NEST_VALUE, entity);//JoinProperty用
			request.setAttribute(Constants.AUTOCOMPLETION_SETTING, property.getAutocompletionSetting());
			request.setAttribute(Constants.REF_SECTION_INDEX, new Integer(dataIndex));
%>
<jsp:include page="<%=path %>" />
<%
			request.removeAttribute(Constants.EDITOR_EDITOR);
			request.removeAttribute(Constants.EDITOR_PROP_VALUE);
			request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
			request.removeAttribute(Constants.EDITOR_REF_NEST);
			request.removeAttribute(Constants.EDITOR_REF_NEST_VALUE);
			request.removeAttribute(Constants.REF_SECTION_INDEX);

			if (property.getAutocompletionSetting() != null) {
				request.setAttribute(Constants.AUTOCOMPLETION_DEF_NAME, ed.getName());
				request.setAttribute(Constants.AUTOCOMPLETION_VIEW_NAME, viewName);
				request.setAttribute(Constants.AUTOCOMPLETION_PROP_NAME, property.getPropertyName());
				request.setAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY, 1);
				request.setAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME, section.getPropertyName());
				request.setAttribute(Constants.AUTOCOMPLETION_REF_SECTION_INDEX, new Integer(dataIndex));
				request.setAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA, rootEntity);

				String autocompletionPath = "/jsp/gem/generic/common/ReferenceSectionAutocompletion.jsp";
%>
<jsp:include page="<%=autocompletionPath %>"/>
<%
				request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
				request.removeAttribute(Constants.AUTOCOMPLETION_SETTING);
				request.removeAttribute(Constants.AUTOCOMPLETION_DEF_NAME);
				request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_NAME);
				request.removeAttribute(Constants.AUTOCOMPLETION_PROP_NAME);
				request.removeAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
				request.removeAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME);
				request.removeAttribute(Constants.AUTOCOMPLETION_REF_SECTION_INDEX);
				request.removeAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
				request.removeAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
			}
		}
	}
%>
</div>

<input type="hidden" name="<c:out value="<%=section.getPropertyName() %>"/>_count" value="<%=EntityViewUtil.referenceSectionCount(data.getView(), section.getPropertyName())%>"></input>
<%
	if (entity != null && entity.getOid() != null) {
%>
<input type="hidden" name="<c:out value="<%=section.getPropertyName()%>"/>[<%=dataIndex%>].oid" value="<c:out value="<%=entity.getOid() %>"/>" />
<%
	}
	if (entity != null && entity.getVersion() != null) {
%>
<input type="hidden" name="<c:out value="<%=section.getPropertyName()%>"/>[<%=dataIndex%>].version" value="<%=entity.getVersion() %>" />
<%
	}

	if (StringUtil.isNotBlank(section.getLowerContents())) {
		String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
		evm.executeTemplate(rootDefName, section.getContentScriptKey() + "_LowerContent", request, response, application, pageContext);
	}
%>
</div>
</div>
<%
	//同一プロパティ名のセクションで使用するインデックス
	request.setAttribute(section.getPropertyName() + "_dataIndex", dataIndex + 1);

	//書き換えた定義名を戻す
	request.setAttribute(Constants.DEF_NAME, defName);
%>
