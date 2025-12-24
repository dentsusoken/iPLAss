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
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.gem.command.generic.selectfilter.ReferenceSelectFilterCommand"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.EntityManager"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferenceSelectFilterSetting" %>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.entity.query.Query" %>
<%@ page import="org.iplass.mtp.entity.query.SortSpec" %>
<%@ page import="org.iplass.mtp.entity.query.SortSpec.SortType" %>
<%@ page import="org.iplass.mtp.entity.query.condition.Condition" %>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.And" %>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.Not" %>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.In" %>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinitionType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType" %>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.util.CollectionUtil"%>

<%!
	private EntityManager em = ManagerLocator.manager(EntityManager.class);
	private EntityDefinitionManager edm = ManagerLocator.manager(EntityDefinitionManager.class);

	/**
     * プロパティ名を EntityDefinition に応じて調整する
     * @param ed EntityDefinition (null の場合は元名をそのまま返す）
     * @param propName 元のプロパティ名
     * @return 調整後のプロパティ名
     */
    private static String adjustPropertyName(EntityDefinition ed, String propName) {
        if (ed == null || propName == null) {
            return propName;
        }
        PropertyDefinition pd = ed.getProperty(propName);
        if (pd == null) {
            return propName;
        }
        if (pd.getType() == PropertyDefinitionType.REFERENCE) {
            return propName + ".name";
        }
        return propName;
    }
	
	/**
	 * 表示用文字列変換
	 * @param v 値
	 * @return 表示用文字列
	 */
	private static String toDisplayString(Object v) {
        if (v == null) {
            return "";
        }
        if (v instanceof SelectValue) {
            return ((SelectValue) v).getValue();
        }
        if (v instanceof String) {
            return (String) v;
        }
        return String.valueOf(v);
	}
	
	List<Entity> searchOptionValues(ReferencePropertyEditor editor, String oid) {
		if (editor == null) {
            return new ArrayList<>();
        }
		ReferenceSelectFilterSetting setting = editor.getReferenceSelectFilterSetting();
		if (setting == null) {
            return new ArrayList<>();
        }

        // プロパティ指定がなければ処理不能なので空結果を返す	
		String propName = setting.getPropertyName();
		if (propName == null || propName.trim().isEmpty()) {
			return new ArrayList<>();
        }

		// 表示ラベル項目
		String labelItem = (editor.getDisplayLabelItem() != null) ? editor.getDisplayLabelItem() : Entity.NAME;

		// エンティティ定義に応じてプロパティ名を調整
		EntityDefinition ed = edm.get(editor.getObjectName());
		propName = adjustPropertyName(ed, propName);
		final String finalPropName = propName;
		
		if (oid == null || oid.trim().isEmpty()) {
			return new ArrayList<>();
		}
		Object[] oidArr = oid.split(",");
		Query q = new Query();
		q.select(Entity.OID, propName, labelItem);
		q.from(editor.getObjectName());
		q.where(new In(Entity.OID, oidArr));
		String sortItem = setting.getSortItem();
		if (sortItem == null || sortItem.isEmpty()) {
			sortItem = Entity.OID;
		}
		SortType sortType = null;
		if (setting.getSortType() == null || setting.getSortType() == RefSortType.ASC) {
			sortType = SortType.ASC;
		} else {
			sortType = SortType.DESC;
		}
		q.order(new SortSpec(sortItem, sortType));

		List<Entity> optionValues = new ArrayList<>();
		em.searchEntity(q, (entity) -> {
			entity.setName(entity.getValue(labelItem));
			entity.setValue("code", toDisplayString(entity.getValue(finalPropName)));
			optionValues.add(entity);
			return true;
		});
		return optionValues;
	}
%>

<%
ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

ReferenceSelectFilterSetting setting = editor.getReferenceSelectFilterSetting();

Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);
ReferenceProperty pd = (ReferenceProperty) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

OutputType outputType = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
Boolean useBulkView = (Boolean) request.getAttribute(Constants.BULK_UPDATE_USE_BULK_VIEW);
if (useBulkView == null) useBulkView = false;

String viewType = Constants.VIEW_TYPE_DETAIL;
if (outputType == OutputType.BULK && !useBulkView) {
	viewType = Constants.VIEW_TYPE_BULK;
} else if (outputType == OutputType.BULK && useBulkView) {
	viewType = Constants.VIEW_TYPE_MULTI_BULK;
}
String rootDefName = (String) request.getAttribute(Constants.ROOT_DEF_NAME);
String scriptKey = (String) request.getAttribute(Constants.SECTION_SCRIPT_KEY);
String viewName = StringUtil.escapeHtml((String) request.getAttribute(Constants.VIEW_NAME), true);
String propertyName = pd.getName();
String propName = setting.getPropertyName();
ReferenceSelectFilterSetting.SelectFilterResearchPattern researchPattern = setting.getSelectFilterResearchPattern() != null 
	? setting.getSelectFilterResearchPattern() 
	: ReferenceSelectFilterSetting.SelectFilterResearchPattern.KEEP;

Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
String rootOid = rootEntity != null ? rootEntity.getOid() : "";
String rootVersion = rootEntity != null && rootEntity.getVersion() != null ? rootEntity.getVersion()
		.toString() : "";


String oid = "";
if (propValue instanceof Entity) {
	Entity refEntity = (Entity) propValue;
	oid =  (refEntity != null && refEntity.getOid() != null) ? refEntity.getOid() : "";
} else if (propValue instanceof Entity[]) {
	List<String> oidList = new ArrayList<>();
	Entity[] entities = (Entity[]) propValue;
    for (Entity o : entities) {
       if (o.getOid() != null) {
    	   oidList.add(o.getOid());
       }
       oid = String.join(",", oidList);
    }
}
List<Entity> optionValues = new ArrayList<>();
if (StringUtil.isNotEmpty(oid)) {
	optionValues = this.searchOptionValues(editor ,oid);
}
String inputId = "input_" + editor.getPropertyName();
String selectId = "select_" + editor.getPropertyName();
String customStyle = "";
if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
	customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
}

Boolean isMultiple = pd.getMultiplicity() != 1;
String cls = isMultiple ? "form-size-12 inpbr ref-select-filter-item" : "form-size-02 inpbr ref-select-filter-item";
String multiple = isMultiple ? " multiple" : "";

String editorPlaceholder = setting.getSelectFilterPlaceholder();
String placeHolder = "";
if (StringUtil.isNotBlank(editorPlaceholder)) {
	placeHolder = TemplateUtil.getMultilingualString(editorPlaceholder, setting.getLocalizedPlaceholderList());
}
%>

<div class="ref-select-filter">
    <input type="text"
        class="form-size-02 inpbr ref-select-filter-item"
   		id="<c:out value="<%=inputId %>"/>"
        style="<c:out value="<%=customStyle%>"/>" 
	    placeholder="<%=org.apache.commons.text.StringEscapeUtils.escapeHtml4(placeHolder) %>"
        data-defName="<c:out value="<%=rootDefName%>"/>"
        data-viewName="<%=viewName %>" 
        data-propName="<c:out value="<%=propertyName%>"/>" 
        data-viewType="<%=viewType %>" 
        data-researchPattern="<c:out value="<%=researchPattern%>"/>"
        data-entityOid="<c:out value="<%=rootOid%>"/>" 
        data-entityVersion="<c:out value="<%=rootVersion%>"/>"
        data-webapiName="<%=ReferenceSelectFilterCommand.WEBAPI_NAME%>"
        autocomplete="off"
    />
    <select
    	id="<c:out value="<%=selectId %>"/>"
        name="<c:out value="<%=propertyName%>"/>"
        class="<c:out value="<%=cls %>"/>"
        style="<c:out value="<%=customStyle%>"/>"
        <c:out value="<%=multiple %>"/>
    >   
<%
    if (CollectionUtil.isNotEmpty(optionValues)) {
    	for (Entity en : optionValues) {
    		if (en == null || StringUtil.isEmpty(en.getOid())) {
    			continue;
    		}
    		String initOid = en.getOid();
    		String name = StringUtil.isNotEmpty(en.getName()) ? en.getName() : "";
    		String code = StringUtil.isNotEmpty(en.getValue("code")) ? en.getValue("code") : "";
%>
 		<option value="<c:out value="<%=initOid%>" />" data-code="<c:out value="<%=code%>" />"  selected ><c:out value="<%=name%>" /></option>
<%	
    	}
    }
%>
	</select>
</div>

