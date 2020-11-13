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

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.function.Supplier"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.*" %>
<%@ page import="org.iplass.mtp.entity.definition.*" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.VersionControlReferenceType"%>
<%@ page import="org.iplass.mtp.entity.definition.validations.*" %>
<%@ page import="org.iplass.mtp.util.*" %>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.EditPage"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.InsertType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.UrlParameterActionType"%>
<%@ page import="org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ApplicationException"%>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailCommandContext"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.LoadEntityInterrupterHandler"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateReferencePropertyCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateTableOrderCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	boolean isDispProperty(PropertyDefinition pd, String mappedBy, NestProperty property, OutputType type) {
		if (pd == null) return false;
		if (pd.getMultiplicity() != 1) return false;
		if (pd instanceof ReferenceProperty) {
			if (mappedBy != null && pd.getName().equals(mappedBy)) return false;//逆参照が本体の場合(?)
		}
		if (OutputType.EDIT == type) {
			if (property.isHideDetail()) return false;
		} else if (OutputType.VIEW == type) {
			if (property.isHideView()) return false;
		}
		return true;
	}
	String getViewAction(String defName, String viewName, String oid, boolean isDialog) {
		//ビュー名があればアクションの後につける
		String urlPath = ViewUtil.getParamMappingPath(defName, viewName) + "/" + oid;

		EntityView ev = ManagerLocator.getInstance().getManager(EntityViewManager.class).get(defName);
		SearchFormView view = null;
		if (viewName == null || viewName.equals("")) {
			if (ev != null && ev.getSearchFormViewNames().length > 0) {
				view = ev.getDefaultSearchFormView();
			}
		} else {
			if (ev != null) view = ev.getSearchFormView(viewName);
		}
		if (view == null) view = new SearchFormView();

		//詳細表示アクション
		String contextPath = TemplateUtil.getTenantContextPath();
		String viewAction = "";
		if (StringUtil.isNotBlank(view.getViewActionName())) {
			viewAction = contextPath + "/" + view.getViewActionName() +  urlPath;
		} else {
			if (isDialog) {
				viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
			} else {
				viewAction = contextPath + "/" + DetailViewCommand.VIEW_ACTION_NAME + urlPath;
			}
		}

		return viewAction;
	}
	LoadEntityInterrupterHandler getLoadEntityInterrupterHandler(EntityManager em, EntityDefinitionManager edm, EntityViewManager evm) {
		DetailCommandContext context = new DetailCommandContext(TemplateUtil.getRequestContext(), em, edm);//ここでこれを作るのはちょっと微妙だが・・・
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));
		return context.getLoadEntityInterrupterHandler();
	}
	LoadOption getOption(EntityDefinition ed, ReferencePropertyEditor editor, String mappedBy, OutputType type) {
		List<String> propList = new ArrayList<String>();
		for (NestProperty nProp : editor.getNestProperties()) {
			PropertyDefinition refPd = ed.getProperty(nProp.getPropertyName());
			if (isDispProperty(refPd, mappedBy, nProp, type) && refPd instanceof ReferenceProperty) {
				propList.add(refPd.getName());
			}
		}
		return new LoadOption(propList);
	}
%>
<%
	//既にネストされたプロパティからの再ネストは禁止
	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	request.removeAttribute(Constants.EDITOR_REF_NEST);
	if (nest != null && nest) return;

	String contextPath = TemplateUtil.getTenantContextPath();
	AuthContext auth = AuthContext.getCurrentContext();

	//Request情報取得
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	Object value = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	ReferenceProperty pd = (ReferenceProperty) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	boolean isInsert = Constants.EXEC_TYPE_INSERT.equals(execType);
	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	OutputType outputType = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	String mappedBy = (String) request.getAttribute(Constants.EDITOR_REF_MAPPEDBY);
	if (mappedBy == null) mappedBy = "";
	request.removeAttribute(Constants.EDITOR_REF_MAPPEDBY);
	String reloadUrl = (String) request.getAttribute(Constants.EDITOR_REF_RELOAD_URL);

	//本体のEntity
	Entity parentEntity = (Entity) request.getAttribute(Constants.EDITOR_PARENT_ENTITY);
	String parentOid = parentEntity != null ? parentEntity.getOid() : "";
	String parentVersion = parentEntity != null && parentEntity.getVersion() != null ? parentEntity.getVersion().toString() : "";

	//表示判断スクリプトEntity
	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
	String rootOid = parentEntity != null ? parentEntity.getOid() : "";
	String rootVersion = parentEntity != null && parentEntity.getVersion() != null ? parentEntity.getVersion().toString() : "";

	//Property情報取得
	boolean isMappedby = pd.getMappedBy() != null;
	boolean isMultiple = pd.getMultiplicity() != 1;

	//権限チェック
	boolean editable = true;
	if(editable) {
		editable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.CREATE));
	} else {
		editable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.UPDATE));
	}
	boolean updatable = (pd.isUpdatable() || isInsert) && editable;

	//Editorの設定値取得
	String refDefName = editor.getObjectName();
	String propName = editor.getPropertyName();
	boolean hideDeleteButton = editor.isHideDeleteButton();
	boolean hideRegistButton = editor.isHideRegistButton();
	boolean hideSelectButton = editor.isHideSelectButton();
	boolean refEdit = editor.isEditableReference();
	boolean editPageView = editor.getEditPage() != null && editor.getEditPage() == EditPage.VIEW;

	OutputType execOutputType = outputType;
	if (editPageView) {
		//Viewモードの場合は、EDITでもVIEWとして出力させる
		execOutputType = OutputType.VIEW;
		request.setAttribute(Constants.OUTPUT_TYPE, execOutputType);
	}

	// 参照先のEntity情報取得
	final EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	EntityDefinition refEd = edm.get(refDefName);

	//追加ボタン制御(参照先の登録権限)
	boolean refCreatable = false;
	if (!hideRegistButton && updatable) {
		//追加ボタン表示かつプロパティの更新権限がある場合はチェック
		refCreatable = auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.CREATE));
	}
	boolean showInsertBtn = refCreatable;

	//削除ボタン制御
	boolean refDeletable = false;
	if (!hideDeleteButton) {
		if (isMappedby) {
			//非参照の場合は、参照先Entityの参照プロパティの更新権限
			refDeletable = auth.checkPermission(new EntityPropertyPermission(refDefName, pd.getMappedBy(), EntityPropertyPermission.Action.UPDATE));
		} else {
			//通常は更新権限があれば削除可能
			refDeletable = updatable;
		}
	}
	boolean showDeleteBtn = refDeletable;
	if (outputType == OutputType.VIEW) {
		//Viewの場合は、Viewで編集の場合のみ削除ボタン利用可能
		if (!editPageView) {
			showDeleteBtn = false;
		}
	} else if (outputType == OutputType.EDIT) {
		//Editの場合は、Viewで編集の場合は削除ボタン利用不可
		if (editPageView) {
			showDeleteBtn = false;
		}
	}

	//編集リンク制御(参照先の更新権限)
	boolean refUpdatable = false;
	if (refEdit) {
		//参照先Entityが更新できるかをチェック
		refUpdatable = auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.UPDATE));
	}
	boolean showRefEditLink = refUpdatable;

	//Action定義取得
	String urlPath = ViewUtil.getParamMappingPath(refDefName, editor.getViewName());

	//編集用のAction
	String detailAction = "";
	if (StringUtil.isNotBlank(editor.getDetailrefActionName())) {
		detailAction = contextPath + "/" + editor.getDetailrefActionName() + urlPath;
	} else {
		detailAction = contextPath + "/" + DetailViewCommand.REF_DETAIL_ACTION_NAME + urlPath;
	}

	//表示用のAction
	String viewAction = "";
	if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
		viewAction = contextPath + "/" + editor.getViewrefActionName() + urlPath;
	} else {
		viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
	}

	//追加用のAction
	String addAction = "";
	if (StringUtil.isNotBlank(editor.getAddActionName())) {
		addAction = contextPath + "/" + editor.getAddActionName() + urlPath;
	} else {
		addAction = contextPath + "/" + DetailViewCommand.REF_DETAIL_ACTION_NAME + urlPath;
	}

	//選択用のAction
	String selectAction = "";
	if (StringUtil.isNotBlank(editor.getSelectActionName())) {
		selectAction = contextPath + "/" + editor.getSelectActionName() + urlPath;
	} else {
		selectAction = contextPath + "/" + SearchViewCommand.SELECT_ACTION_NAME + urlPath;
	}

	//参照プロパティ更新用のAction
	boolean isDialog = ViewUtil.isDialog(TemplateUtil.getRequestContext());
	String updateRefAction = null;
	if (isDialog) {
		updateRefAction = contextPath + "/" + UpdateReferencePropertyCommand.REF_ACTION_NAME;
	} else {
		updateRefAction = contextPath + "/" + UpdateReferencePropertyCommand.ACTION_NAME;
	}
	String viewName = (String)request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";
	if (StringUtil.isNotBlank(viewName)) {
		updateRefAction = updateRefAction + "/" + viewName;
	}

	//リロード用URL
	if (reloadUrl == null || reloadUrl.isEmpty()) {
		reloadUrl = getViewAction(defName, viewName, parentOid, isDialog);
	}

	//ロード処理のinterrupter
	LoadEntityInterrupterHandler handler = getLoadEntityInterrupterHandler(em, edm, evm);

	//出力データ
	List<Entity> entities = new ArrayList<Entity>();
	if (value instanceof Entity) {
		entities.add((Entity) value);
	} else if (value instanceof Entity[]) {
		entities.addAll(Arrays.asList((Entity[]) value));
	}

	//表示順が指定されてたら並び替え
	boolean showUpDownBtn = StringUtil.isNotBlank(editor.getTableOrderPropertyName());
	if (showUpDownBtn) {
		entities = EntityViewUtil.sortByOrderProperty(entities, editor.getTableOrderPropertyName(), true);
	}

	//定義名を参照型のものに置き換える、後でdefNameに戻す
	request.setAttribute(Constants.DEF_NAME, refDefName);

	if (OutputType.EDIT == execOutputType || OutputType.BULK == execOutputType) {

		//-------------------------
		//Editモード
		//-------------------------

		String countId = "id_" + propName + "_count";
		String dummyRowId = "id_tr_" + propName + "Dummy";
		//追加ボタン
		if (showInsertBtn && (editor.getInsertType() != null && editor.getInsertType() == InsertType.TOP)) {
			String _rootDefName = StringUtil.escapeJavaScript(rootDefName);
			String _propName = StringUtil.escapeJavaScript(propName);
			String _viewName = StringUtil.escapeJavaScript(viewName);
			String addBtnStyle = "";
			if (entities.size() == pd.getMultiplicity()) addBtnStyle = "display: none;";
%>
<p class="mb10">
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.add')}" id="id_<c:out value="<%=propName%>"/>_addButton_top" class="gr-btn-02 add-btn table-top-button" style="<c:out value="<%=addBtnStyle%>" />" />
<script type="text/javascript">
var toggleAddBtn_<%=_propName%> = function() {
	var $tbody = $("#<%=StringUtil.escapeJavaScript(dummyRowId)%>").parent();
	<%-- 参照プロパティで多重度が*指定（値的には-1）可能 --%>
	var display = <%=pd.getMultiplicity() == -1%> || $tbody.children("tr:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#id_<c:out value="<%=propName%>"/>_addButton_top").toggle(display);
}
$(function() {
	$("#id_<%=StringUtil.escapeJavaScript(propName)%>_addButton_top").on("click", function() {
		addNestRow("<%=StringUtil.escapeJavaScript(dummyRowId)%>", "<%=StringUtil.escapeJavaScript(countId)%>", <%=pd.getMultiplicity() + 1%>, true, "<%=_rootDefName%>", "<%=_viewName%>", "<%=_propName%>", function(row, index) {
			toggleAddBtn_<%=_propName%>();
<%
			if (StringUtil.isNotBlank(editor.getAddRowCallbackScript())) {
%>
			<%=editor.getAddRowCallbackScript()%>
<%
			}
%>
		}, toggleAddBtn_<%=_propName%>);
	});
});
</script>
</p>
<%
		}

		String tableStyle = "";
		if (entities.size() == 0) tableStyle = "display: none;";
%>
<div class="box-scroll">
<table class="tbl-reference tableStripe reverse" style="<c:out value="<%=tableStyle%>" />">
<thead>
<tr>
<%
		//ヘッダ部作成

		int colNum = 0;
		for (NestProperty nProp : editor.getNestProperties()) {
			PropertyDefinition refPd = refEd.getProperty(nProp.getPropertyName());
			if (refPd.getMultiplicity() != 1) {
				throw new ApplicationException(GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Table.exceptionMessage")
						+ ":propertyName=[" + refPd.getName() + "]");
			}
			String tooltip = "";
			if (StringUtil.isNotBlank(nProp.getTooltip())) {
				tooltip = TemplateUtil.getMultilingualString(nProp.getTooltip(), nProp.getLocalizedTooltipList());
			}

			if (isDispProperty(refPd, mappedBy, nProp, outputType)) {

				boolean required = false;
				RequiredDisplayType rdType = nProp.getRequiredDisplayType();
				if (rdType == null) rdType = RequiredDisplayType.DEFAULT;
				if (rdType == RequiredDisplayType.DEFAULT) {
					if (refPd.getValidations() != null) {
						for (ValidationDefinition validation : refPd.getValidations()) {
							if (validation instanceof NotNullValidation) {
								required = true;
								break;
							}
						}
					}
				} else if (rdType == RequiredDisplayType.DISPLAY) {
					required = true;
				}

				//表示名
				String title = TemplateUtil.getMultilingualString(
						nProp.getDisplayLabel(), nProp.getLocalizedDisplayLabelList(),
						refPd.getDisplayName(), refPd.getLocalizedDisplayNameList());

				String cls = "col" + colNum;
				String style = "";
				if (nProp.getWidth() > 0) {
					style = "width:" + nProp.getWidth() + "px;";
				}
%>
<th nowrap="nowrap" class="<c:out value="<%=cls%>"/>" style="<c:out value="<%=style%>"/>">
<%-- XSS対応-メタの設定のため対応なし(title) --%>
<%=title%>
<%
				if (required) {
%>
<span class="ico-required ml10 vm">${m:rs("mtp-gem-messages", "generic.editor.reference.ReferencePropertyEditor_Table.required")}</span>
<%
				}
				if (StringUtil.isNotBlank(tooltip)) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp"  title="<%=tooltip%>" /></span>
<%
				}
				String simpleName = nProp.getEditor().getClass().getSimpleName();
				String propType = simpleName.substring(0, simpleName.indexOf("PropertyEditor"));
				String dispType = nProp.getEditor().getDisplayType().name();
%>
<input type="hidden" value="<c:out value="<%=propType%>"/>" />
<input type="hidden" value="<c:out value="<%=dispType%>"/>" />
<%
				if (nProp.getEditor() instanceof JoinPropertyEditor) {
					//結合するプロパティの情報を埋め込む
					JoinPropertyEditor je = (JoinPropertyEditor) nProp.getEditor();
					je.setPropertyName(nProp.getPropertyName());
					List<NestProperty> joinProperties = je.getJoinProperties();
					String nestPropName = propName + "[idx]_" + nProp.getPropertyName();
%>
<input type="hidden" name="joinPropName" value="<c:out value="<%=nestPropName%>"/>" />
<input type="hidden" name="joinPropCount" value="<c:out value="<%=joinProperties.size()%>"/>" />
<%
					for (int i = 0; i < joinProperties.size(); i++) {
						NestProperty joinProperty = joinProperties.get(i);
						PropertyEditor nestEditor = joinProperty.getEditor();
						String jeSimpleName = nestEditor.getClass().getSimpleName();
						String jePropType = jeSimpleName.substring(0, jeSimpleName.indexOf("PropertyEditor"));
						String jeDispType = nestEditor.getDisplayType().name();
%>
<input type="hidden" name="joinPropName<c:out value="<%=i%>"/>" value="<c:out value="<%=joinProperty.getPropertyName()%>"/>" />
<input type="hidden" name="joinPropType<c:out value="<%=i%>"/>" value="<c:out value="<%=jePropType%>"/>" />
<input type="hidden" name="joinDispType<c:out value="<%=i%>"/>" value="<c:out value="<%=jeDispType%>"/>" />
<%
					}
				}
				if (nProp.getEditor() instanceof DateRangePropertyEditor) {
					//結合するプロパティの情報を埋め込む
					DateRangePropertyEditor de = (DateRangePropertyEditor) nProp.getEditor();
					de.setPropertyName(nProp.getPropertyName());
					PropertyEditor nestEditor = de.getEditor();
					String deSimpleName = nestEditor.getClass().getSimpleName();
					String dePropType = deSimpleName.substring(0, deSimpleName.indexOf("PropertyEditor"));
					String deDispType = nestEditor.getDisplayType().name();
%>
<input type="hidden" name="dateRangePrefix" value="<c:out value="<%=propName%>"/>[idx]_" />
<input type="hidden" name="fromPropName" value="<c:out value="<%=nProp.getPropertyName()%>"/>" />
<input type="hidden" name="toPropName" value="<c:out value="<%=de.getToPropertyName()%>"/>" />
<input type="hidden" name="dateRangePropType" value="<c:out value="<%=dePropType%>"/>" />
<input type="hidden" name="dateRangeDispType" value="<c:out value="<%=deDispType%>"/>" />
<%
				}
				if (nProp.getAutocompletionSetting() != null) {

					request.setAttribute(Constants.AUTOCOMPLETION_SETTING, nProp.getAutocompletionSetting());
					request.setAttribute(Constants.AUTOCOMPLETION_DEF_NAME, rootDefName);
					request.setAttribute(Constants.AUTOCOMPLETION_VIEW_NAME, viewName);
					request.setAttribute(Constants.AUTOCOMPLETION_PROP_NAME, nProp.getPropertyName());
					request.setAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY, 1);
					request.setAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME, propName);
					request.setAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA, rootEntity);

					String typePath = null;
					if (nProp.getEditor() instanceof IntegerPropertyEditor
							|| nProp.getEditor() instanceof FloatPropertyEditor) {
						typePath = "/jsp/gem/generic/editor/number/NumberPropertyAutocompletion.jsp";
					} else {
						String fileName = simpleName.substring(0, simpleName.indexOf("Editor")) + "Autocompletion.jsp";
						typePath = "/jsp/gem/generic/editor/" + propType.toLowerCase() + "/" + fileName;
					}
					request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, typePath);
					request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, nProp.getEditor());

					String autocompletionPath = "/jsp/gem/generic/common/NestTableAutocompletion.jsp";
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
					request.removeAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
					request.removeAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
				}
%>
</th>
<%
				colNum++;
			}
		}
		//詳細リンク列
		if (refEdit) {
%>
<th nowrap="nowrap" class="colLink">
${m:rs("mtp-gem-messages", "generic.editor.reference.ReferencePropertyEditor_Table.detail")}
<input type="hidden" value="refLink" />
<input type="hidden" value="refLink" />
</th>
<%
		}
		//表示順
		if (showUpDownBtn) {
%>
<th class="orderCol">
<input type="hidden" value="tableOrder" />
<input type="hidden" value="tableOrder" />
</th>
<%
		}
		//削除ボタン列
		if (showDeleteBtn) {
%>
<th nowrap="nowrap" class="delButton">
<input type="hidden" value="last" />
<input type="hidden" value="last" />
</th>
<%
		}
%>
</tr>
</thead>
<tbody>
<%
		//body部作成

		//Javascriptでコピーするためのテンプレート行作成
		request.setAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW, true);
%>
<tr id="<c:out value="<%=dummyRowId%>"/>" style="display: none;">
<%
		colNum = 0;
		//新規追加のために一時的に書き換える
		request.setAttribute(Constants.EXEC_TYPE, Constants.EXEC_TYPE_INSERT);
		for (NestProperty nProp : editor.getNestProperties()) {
			PropertyDefinition refPd = refEd.getProperty(nProp.getPropertyName());
			if (refPd.getMultiplicity() != 1) {
				throw new ApplicationException(GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Table.exceptionMessage")
						+ ":propertyName=[" + refPd.getName() + "]");
			}
			if (isDispProperty(refPd, mappedBy, nProp, outputType)) {
				String description = "";
				if (StringUtil.isNotBlank(nProp.getDescription())) {
					description = TemplateUtil.getMultilingualString(nProp.getDescription(), nProp.getLocalizedDescriptionList());
				}
				nProp.getEditor().setPropertyName(propName + "[Dummy]." + refPd.getName());
				request.setAttribute(Constants.EDITOR_EDITOR, nProp.getEditor());
				request.setAttribute(Constants.EDITOR_PROP_VALUE, null);
				request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, refPd);
				request.setAttribute(Constants.EDITOR_REF_NEST, true);//2重ネスト防止用フラグ
				String path = EntityViewUtil.getJspPath(nProp.getEditor(), ViewConst.DESIGN_TYPE_GEM);
				if (refPd instanceof ReferenceProperty
						&& nProp.getEditor() instanceof ReferencePropertyEditor) {
					//参照型の場合は定義名保管
					((ReferencePropertyEditor) nProp.getEditor())
						.setObjectName(((ReferenceProperty) refPd).getObjectDefinitionName());
				}
				String cls = "col" + colNum;
				String style = "";
				if (nProp.getWidth() > 0) {
					style = "width:" + nProp.getWidth() + "px;";
				}
%>
<td class="<c:out value="<%=cls%>"/>" data-propName="<c:out value="<%=refPd.getName()%>"/>" style="<c:out value="<%=style%>"/>">
<%
				if (path != null) {
%>
<jsp:include page="<%=path%>" />
<%
				}
				request.removeAttribute(Constants.EDITOR_REF_NEST);

				if (StringUtil.isNotBlank(description)) {
%>
<br />
<%-- XSS対応-メタの設定のため対応なし(description) --%>
<p class="explanation"><%=description%></p>
<%
				}
%>
</td>
<%
				colNum++;
			}
		}
		request.setAttribute(Constants.EXEC_TYPE, execType);

		//編集リンク
		if (refEdit) {
			String idxPropName = propName + "[Dummy]";
			//追加できるということは新規で編集できるので、更新権限がなくても編集側のActionを呼び出す
%>
<td nowrap="nowrap" class="colLink center">
<a href="javascript:void(0);" data-name="<c:out value="<%=idxPropName%>" />"
 data-defName="<c:out value="<%=refDefName%>" />"
 data-action="<c:out value="<%=detailAction%>" />"
 data-view="<c:out value="<%=viewAction%>" />">
 ${m:rs("mtp-gem-messages", "generic.editor.reference.ReferencePropertyEditor_Table.edit")}</a>
</td>
<%
		}

		//表示順
		if (showUpDownBtn) {
%>
<td class="orderCol">
<input type="hidden" name="tableOrderIndex[Dummy]" >
<span class="order-icon up-icon"><i class="fas fa-caret-up"></i></span>
<span class="order-icon down-icon"><i class="fas fa-caret-down"></i></span>
</td>
<%
		}

		//削除ボタン
		if (showDeleteBtn) {
%>
<td nowrap="nowrap">
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.delete')}" class="gr-btn-02 del-btn" />
</td>
<%
		}

		request.removeAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);
		//テンプレート行作成ここまで
%>
</tr>
<%
		//データ出力

		for (int i = 0; i < entities.size(); i++) {
			final Entity tmp = entities.get(i);
			String trId = "id_tr_" + propName + i;
			Entity entity = null;
			boolean insertRow = false;
			Boolean reload = tmp.getValue(Constants.REF_RELOAD);
			if (tmp.getOid() != null) {
				if (reload == null || reload) {
					LoadOption loadOption = getOption(refEd, editor, mappedBy, outputType);
					final LoadEntityContext leContext = handler.beforeLoadReference(tmp.getDefinitionName(), loadOption, pd, LoadType.VIEW);
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
					handler.afterLoadReference(entity, loadOption, pd, LoadType.VIEW);
				}
			} else {
				insertRow = true;
			}
			if (entity == null) entity = tmp;//取れなかったら元データで
			request.setAttribute(Constants.EDITOR_REF_NEST_VALUE, entity);//JoinProperty用
			request.setAttribute(Constants.EDITOR_PARENT_ENTITY, entity);//参照の新規登録時のパラメータ用
			boolean isFirstCol = true;
%>
<tr id="<c:out value="<%=trId%>"/>">
<%
			colNum = 0;
			String idxPropName = propName + "[" + i + "]";
			for (NestProperty nProp : editor.getNestProperties()) {
				PropertyDefinition refPd = refEd.getProperty(nProp.getPropertyName());
				if (refPd.getMultiplicity() != 1) {
					throw new ApplicationException(GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Table.exceptionMessage")
							+ ":propertyName=[" + refPd.getName() + "]");
				}
				if (isDispProperty(refPd, mappedBy, nProp, outputType)) {
					String description = "";
					if (StringUtil.isNotBlank(nProp.getDescription())) {
						description = TemplateUtil.getMultilingualString(nProp.getDescription(), nProp.getLocalizedDescriptionList());
					}
					nProp.getEditor().setPropertyName(idxPropName + "." + refPd.getName());
					request.setAttribute(Constants.EDITOR_EDITOR, nProp.getEditor());
					request.setAttribute(Constants.EDITOR_PROP_VALUE, entity.getValue(refPd.getName()));
					request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, refPd);
					request.setAttribute(Constants.EDITOR_REF_NEST, true);//2重ネスト防止用フラグ
					String path = EntityViewUtil.getJspPath(nProp.getEditor(), ViewConst.DESIGN_TYPE_GEM);

					String cls = "col" + colNum;
					String style = "";
					if (nProp.getWidth() > 0) {
						style = "width:" + nProp.getWidth() + "px;";
					}
%>
<td class="<c:out value="<%=cls%>"/>" data-propName="<c:out value="<%=refPd.getName()%>"/>" style="<c:out value="<%=style%>"/>">
<%
					if (isFirstCol) {
						isFirstCol = false;
						if (!insertRow) {
%>
<input type="hidden" name="<c:out value="<%=idxPropName%>"/>.oid" value="<c:out value="<%=entity.getOid()%>"/>" />
<input type="hidden" name="<c:out value="<%=idxPropName%>"/>.version" value="<%=entity.getVersion()%>" />
<%
						}
					}
					if (insertRow) request.setAttribute(Constants.EXEC_TYPE, Constants.EXEC_TYPE_INSERT);
					if (path != null) {
%>
<jsp:include page="<%=path%>" />
<%
					}
					if (insertRow) request.setAttribute(Constants.EXEC_TYPE, execType);
					request.removeAttribute(Constants.EDITOR_REF_NEST);
					if (StringUtil.isNotBlank(description)) {
%>
<br />
<%-- XSS対応-メタの設定のため対応なし(description) --%>
<p class="explanation"><%=description%></p>
<%
					}
%>
</td>
<%
					colNum++;
				}
			}
			request.removeAttribute(Constants.EDITOR_REF_NEST_VALUE);


			if (refEdit) {
				String _detailAction = StringUtil.escapeJavaScript(detailAction);
				String _viewAction = StringUtil.escapeJavaScript(viewAction);
				String _refDefName = StringUtil.escapeJavaScript(refDefName);
				String _entityOid = entity.getOid() == null ? "" : StringUtil.escapeJavaScript(entity.getOid());
				String _trId = StringUtil.escapeJavaScript(trId);
				String _idxPropName = StringUtil.escapeJavaScript(idxPropName);
				String _reloadUrl = StringUtil.escapeJavaScript(reloadUrl);
				String _rootDefName = StringUtil.escapeJavaScript(rootDefName);
				String _propName = StringUtil.escapeJavaScript(propName);
				String _viewName = StringUtil.escapeJavaScript(viewName);
				if (showRefEditLink) {
					//編集リンク
%>
<td nowrap="nowrap" class="colLink center">
<a href="javascript:void(0);" class="modal-lnk"
 onclick="editReference('<%=_detailAction%>', '<%=_refDefName%>', '<%=_entityOid%>', '<%=_trId%>', '<%=_idxPropName%>', <%=i%>, '<%=_viewAction%>', '<%=_rootDefName%>', '<%=_viewName%>', '<%=_propName%>')">
 ${m:rs("mtp-gem-messages", "generic.editor.reference.ReferencePropertyEditor_Table.edit")}</a>
</td>
<%
				} else {
					//詳細リンク
					String _viewUrlParam = StringUtil.escapeJavaScript(
							evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.VIEW));
%>
<td nowrap="nowrap" class="colLink center">
<a href="javascript:void(0);" class="modal-lnk"
 onclick="viewEditableReference('<%=_viewAction%>', '<%=_refDefName%>', '<%=_entityOid%>', '<%=_reloadUrl%>', true, '<%=_viewUrlParam%>')">
 ${m:rs("mtp-gem-messages", "generic.editor.reference.ReferencePropertyEditor_Table.detail")}</a>
</td>
<%
				}
			}

			//表示順
			if (showUpDownBtn) {
%>
<td class="orderCol">
<input type="hidden" name="tableOrderIndex[<%=i%>]" value="<%=i%>">
<span class="order-icon up-icon"><i class="fas fa-caret-up" onclick="shiftUp('<%=StringUtil.escapeJavaScript(trId)%>')"></i></span>
<span class="order-icon down-icon"><i class="fas fa-caret-down" onclick="shiftDown('<%=StringUtil.escapeJavaScript(trId)%>')"></i></span>
</td>
<%
			}

			//削除ボタン
			if (showDeleteBtn) {
%>
<td nowrap="nowrap">
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.delete')}" class="gr-btn-02 del-btn"
		onclick="deleteRefTableRow('<%=StringUtil.escapeJavaScript(trId)%>', toggleAddBtn_<%=StringUtil.escapeJavaScript(propName)%>)" />
</td>
<%
			}
%>
</tr>
<%
		}

		String hiddenName = propName + "_count";
%>
</tbody>
</table>
</div>
<input type="hidden" name="<c:out value="<%=hiddenName%>"/>" id="<c:out value="<%=countId%>"/>" value="<c:out value="<%=entities.size()%>"/>" />
<%
		//追加ボタン
		if (showInsertBtn && (editor.getInsertType() == null || editor.getInsertType() == InsertType.BOTTOM)) {
			String _rootDefName = StringUtil.escapeJavaScript(rootDefName);
			String _propName = StringUtil.escapeJavaScript(propName);
			String _viewName = StringUtil.escapeJavaScript(viewName);
			String addBtnStyle = "";
			if (entities.size() == pd.getMultiplicity()) addBtnStyle = "display: none;";
%>
<p class="mt10">
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.add')}" id="id_<c:out value="<%=propName%>"/>_addButton_bottom" class="gr-btn-02 add-btn table-bottom-button" style="<c:out value="<%=addBtnStyle%>" />" />
<script type="text/javascript">
var toggleAddBtn_<%=_propName%> = function() {
	var $tbody = $("#<%=StringUtil.escapeJavaScript(dummyRowId)%>").parent();
	<%-- 参照プロパティで多重度が*指定（値的には-1）可能 --%>
	var display = <%=pd.getMultiplicity() == -1%> || $tbody.children("tr:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#id_<c:out value="<%=propName%>"/>_addButton_bottom").toggle(display);
}
$(function() {
	$("#id_<%=StringUtil.escapeJavaScript(propName)%>_addButton_bottom").on("click", function() {
		addNestRow("<%=StringUtil.escapeJavaScript(dummyRowId)%>", "<%=StringUtil.escapeJavaScript(countId)%>", <%=pd.getMultiplicity() + 1%>, false, "<%=_rootDefName%>", "<%=_viewName%>", "<%=_propName%>", function(row, index) {
			toggleAddBtn_<%=_propName%>();
<%
			if (StringUtil.isNotBlank(editor.getAddRowCallbackScript())) {
%>
			<%=editor.getAddRowCallbackScript()%>
<%
			}
%>
		}, toggleAddBtn_<%=_propName%>);
	});
});
</script>
</p>
<%
		}
	} else if (OutputType.VIEW == execOutputType) {

		//-------------------------
		//Viewモード(またはEditモードでView編集指定時)
		//-------------------------

		//ヘッダ作成
		String tableStyle = "";
		if (entities.size() == 0) tableStyle = "display: none;";
%>
<div class="box-scroll">
<table class="tbl-reference tableStripe view" style="<c:out value="<%=tableStyle%>" />">
<thead>
<tr>
<%
		int colNum = 0;
		for (NestProperty nProp : editor.getNestProperties()) {
			PropertyDefinition refPd = refEd.getProperty(nProp.getPropertyName());
			if (refPd.getMultiplicity() != 1) {
				throw new ApplicationException(GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Table.exceptionMessage")
						+ ":propertyName=[" + refPd.getName() + "]");
			}
%>
<th nowrap="nowrap" style="display:none;"></th>
<%
			if (isDispProperty(refPd, mappedBy, nProp, outputType)) {

				//表示名
				String title = TemplateUtil.getMultilingualString(
						nProp.getDisplayLabel(), nProp.getLocalizedDisplayLabelList(),
						refPd.getDisplayName(), refPd.getLocalizedDisplayNameList());

				String cls = "col" + colNum;
				String style = "";
				if (nProp.getWidth() > 0) {
					style = "width:" + nProp.getWidth() + "px;";
				}
%>
<th nowrap="nowrap" class="<c:out value="<%=cls%>"/>" style="<c:out value="<%=style%>"/>">
<%-- XSS対応-メタの設定のため対応なし(title) --%>
<%=title%>
</th>
<%
				colNum++;
			}
		}
		//詳細リンク列
		if (refEdit) {
%>
<th nowrap="nowrap" class="colLink">
${m:rs("mtp-gem-messages", "generic.editor.reference.ReferencePropertyEditor_Table.detail")}
</th>
<%
		}

		//表示順
		if (showUpDownBtn && editPageView && outputType == OutputType.VIEW) {
%>
<th class="orderCol"></th>
<%
		}

		//削除ボタン列
		if (showDeleteBtn) {
%>
<th nowrap="nowrap" class="delButton"></th>
<%
		}
%>
</tr>
</thead>
<%
		String tableId = "td_" + propName;
%>
<tbody id="<c:out value="<%=tableId%>"/>">
<%
		//データを出力
		for (int i = 0; i < entities.size(); i++) {
			final Entity tmp = entities.get(i);
			String trId = "id_tr_" + propName + i;
			Entity entity = null;
			final LoadOption loadOption = getOption(refEd, editor, mappedBy, outputType);
			LoadEntityContext leContext = handler.beforeLoadReference(tmp.getDefinitionName(), loadOption, pd, LoadType.VIEW);
			if (leContext.isDoPrivileged()) {
				entity = AuthContext.doPrivileged(new Supplier<Entity>() {

					@Override
					public Entity get() {
						return em.load(tmp.getOid(), tmp.getVersion(), tmp.getDefinitionName(), loadOption);
					}
				});
			} else {
				entity = em.load(tmp.getOid(), tmp.getVersion(), tmp.getDefinitionName(), loadOption);
			}
			if (entity == null) entity = tmp;//取れなかったら元データで
			handler.afterLoadReference(entity, loadOption, pd, LoadType.VIEW);
			request.setAttribute(Constants.EDITOR_REF_NEST_VALUE, entity);//JoinProperty用
			request.setAttribute(Constants.EDITOR_PARENT_ENTITY, entity);//参照の新規登録時のパラメータ用
%>
<tr id="<c:out value="<%=trId %>"/>">
<%
			String key = entity.getOid() + "_" + entity.getVersion();
%>
<td nowrap="nowrap" style="display:none;">
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" />
</td>
<%
			colNum = 0;
			for (NestProperty nProp : editor.getNestProperties()) {
				PropertyDefinition refPd = refEd.getProperty(nProp.getPropertyName());
				if (refPd.getMultiplicity() != 1) {
					throw new ApplicationException(GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Table.exceptionMessage")
							+ ":propertyName=[" + refPd.getName() + "]");
				}
				if (isDispProperty(refPd, mappedBy, nProp, outputType)) {
					nProp.getEditor().setPropertyName(propName + "[" + i + "]." + refPd.getName());
					request.setAttribute(Constants.EDITOR_EDITOR, nProp.getEditor());
					request.setAttribute(Constants.EDITOR_PROP_VALUE, entity.getValue(refPd.getName()));
					request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, refPd);
					request.setAttribute(Constants.EDITOR_REF_NEST, true);//2重ネスト防止用フラグ
					String path = EntityViewUtil.getJspPath(nProp.getEditor(), ViewConst.DESIGN_TYPE_GEM);

					String cls = "col" + colNum;
					String style = "";
					if (nProp.getWidth() > 0) {
						style = "width:" + nProp.getWidth() + "px;";
					}
%>
<td class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=style %>"/>">
<%
					if (path != null) {
%>
<jsp:include page="<%=path %>" />
<%
					}
					request.removeAttribute(Constants.EDITOR_REF_NEST);
%>
</td>
<%
					colNum++;
				}
			}
			request.removeAttribute(Constants.EDITOR_REF_NEST_VALUE);

			//詳細リンク
			if (refEdit) {
				boolean refEditParam = false;
				String strKey = null;
				if (outputType == OutputType.VIEW) {
					//本当のViewモードの場合は、権限によって文言を変える(refEditはtrueでダイアログ側で制御)
					refEditParam = true;
					if (showRefEditLink) {
						strKey = "generic.editor.reference.ReferencePropertyEditor_Table.edit";
					} else {
						strKey = "generic.editor.reference.ReferencePropertyEditor_Table.detail";
					}
				} else {
					//本当はEditモードでView(編集画面での参照表示)の場合は参照のみ
					strKey = "generic.editor.reference.ReferencePropertyEditor_Table.detail";
				}
				String _viewAction = StringUtil.escapeJavaScript(viewAction);
				String _refDefName = StringUtil.escapeJavaScript(refDefName);
				String _entityOid = entity.getOid() == null ? "" : StringUtil.escapeJavaScript(entity.getOid());
				String _reloadUrl = StringUtil.escapeJavaScript(reloadUrl);
				String _viewUrlParam = StringUtil.escapeJavaScript(
						evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.VIEW));
%>
<td nowrap="nowrap" class="colLink center">
<a href="javascript:void(0);" class="modal-lnk"
 onclick="viewEditableReference('<%=_viewAction%>', '<%=_refDefName%>', '<%=_entityOid%>', '<%=_reloadUrl%>', <%=refEditParam%>, '<%=_viewUrlParam%>')">
 <%= GemResourceBundleUtil.resourceString(strKey) %></a>
</td>
<%
			}

			//表示順
			if (showUpDownBtn && editPageView && outputType == OutputType.VIEW) {
				String _trId = StringUtil.escapeJavaScript(trId);
				String _orderPropName = StringUtil.escapeJavaScript(editor.getTableOrderPropertyName());
				String _propName = StringUtil.escapeJavaScript(propName);
				String _refDefName = StringUtil.escapeJavaScript(refDefName);
				String _reloadUrl = StringUtil.escapeJavaScript(reloadUrl);
				String _rootDefName = StringUtil.escapeJavaScript(rootDefName);
				String _viewName = StringUtil.escapeJavaScript(viewName);
%>
<td class="orderCol">
<span class="order-icon up-icon"><i class="fas fa-caret-up"
 onclick="shiftOrder('<%=UpdateTableOrderCommand.WEBAPI_NAME%>', '<%=_trId%>', '<%=_orderPropName%>', '<%=_propName%>', '<%=_refDefName%>', true, '<%=_reloadUrl%>', '<%=_rootDefName%>', '<%=_viewName%>')">
</i></span>
<span class="order-icon down-icon"><i class="fas fa-caret-down"
 onclick="shiftOrder('<%=UpdateTableOrderCommand.WEBAPI_NAME%>', '<%=_trId%>', '<%=_orderPropName%>', '<%=_propName%>', '<%=_refDefName%>', false, '<%=_reloadUrl%>', '<%=_rootDefName%>', '<%=_viewName%>')">
</i></span>
</td>
<%
			}

			//削除ボタン
			if (showDeleteBtn) {
				String deleteId = "del_" + propName + i;
%>
<td nowrap="nowrap">
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.delete')}" class="gr-btn-02 del-btn" id="<c:out value="<%=deleteId %>"/>" />
<script type="text/javascript">
$(function() {
	$(":button[id='<%=StringUtil.escapeJavaScript(deleteId)%>']").click(function() {
		var $tmp = $("#<%=StringUtil.escapeJavaScript(trId)%> :hidden[name='<%=StringUtil.escapeJavaScript(propName)%>']");
		var key = $tmp.val();
		$tmp.remove();
		var $form = $("#detailForm");
		$("<input type='hidden' name='updatePropertyName' />").val("<%=StringUtil.escapeJavaScript(propName)%>").appendTo($form);
		$("<input type='hidden' name='reloadUrl' />").val("<%=StringUtil.escapeJavaScript(reloadUrl)%>").appendTo($form);
		$("<input type='hidden' name='refEntityKey' />").val(key).appendTo($form);
		$form.attr("action", "<%=StringUtil.escapeJavaScript(updateRefAction)%>").submit();
	});
});
</script>
</td>
<%
			}
%>
</tr>
<%
		}
%>
</tbody>
</table>
</div>
<%
		if (updatable && editPageView && outputType == OutputType.VIEW) {
			if (!isMappedby && !hideSelectButton) {
				String selBtnId = "sel_btn_" + propName;
				String specVersionKey = "";
				if (pd.getVersionControlType() == VersionControlReferenceType.AS_OF_EXPRESSION_BASE) {
					//特定バージョン指定の場合、画面の項目からパラメータ取得
					if (StringUtil.isNotBlank(editor.getSpecificVersionPropertyName())) {
						if (editor.getSpecificVersionPropertyName().startsWith(".")) {
							specVersionKey = editor.getSpecificVersionPropertyName().replace(".", "");//ルートを対象
						} else {
							//editorのプロパティ名の最後の.から先を置きかえる
							if (editor.getPropertyName().indexOf(".") > -1) {
								//nest、同レベルの他のプロパティを対象にする
								String parentPath = editor.getPropertyName().substring(0, editor.getPropertyName().lastIndexOf(".") + 1);
								specVersionKey = parentPath + editor.getSpecificVersionPropertyName();
							} else {
								//nestではないのでそのまま設定
								specVersionKey = editor.getSpecificVersionPropertyName();
							}
						}
					}
				}

				String selBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.SELECT);
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.select')}" class="gr-btn-02 modal-btn mt05" id="<c:out value="<%=selBtnId %>"/>" data-specVersionKey="<c:out value="<%=specVersionKey%>" />" />
<script type="text/javascript">
$(function() {
	$(":button[id='<%=StringUtil.escapeJavaScript(selBtnId)%>']").click(function() {
		searchReferenceFromView("<%=StringUtil.escapeJavaScript(selectAction)%>", "<%=StringUtil.escapeJavaScript(updateRefAction)%>", "<%=StringUtil.escapeJavaScript(refDefName) %>", "<%=StringUtil.escapeJavaScript(tableId) %>", "<%=StringUtil.escapeJavaScript(propName)%>",
				<%=pd.getMultiplicity() %>, <%=isMultiple %>, "<%=StringUtil.escapeJavaScript(selBtnUrlParam) %>", "<%=StringUtil.escapeJavaScript(reloadUrl)%>", this, <%=editor.isPermitConditionSelectAll()%>);
	});
});
</script>
<%
			}
			//新規ボタン
			if (showInsertBtn) {
				String insBtnId = "ins_btn_" + propName;

				String insBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.ADD);
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Table.new')}" class="gr-btn-02 modal-btn mt05" id="<c:out value="<%=insBtnId %>"/>" />
<script type="text/javascript">
$(function() {
	$(":button[id='<%=StringUtil.escapeJavaScript(insBtnId)%>']").click(function() {
		insertReferenceFromView("<%=StringUtil.escapeJavaScript(addAction) %>", "<%=StringUtil.escapeJavaScript(refDefName) %>", "<%=StringUtil.escapeJavaScript(tableId) %>", <%=pd.getMultiplicity() %>,
				"<%=StringUtil.escapeJavaScript(insBtnUrlParam)%>", "<%=StringUtil.escapeJavaScript(parentOid)%>", "<%=StringUtil.escapeJavaScript(parentVersion)%>", "<%=StringUtil.escapeJavaScript(defName)%>",
				"<%=StringUtil.escapeJavaScript(mappedBy) %>", $(":hidden[name='oid']").val(), "<%=StringUtil.escapeJavaScript(updateRefAction)%>",
				"<%=StringUtil.escapeJavaScript(propName) %>", "<%=StringUtil.escapeJavaScript(reloadUrl)%>", "<%=StringUtil.escapeJavaScript(rootOid)%>",
				"<%=StringUtil.escapeJavaScript(rootVersion)%>");
	});
});
</script>
<%
			}
		}
	}

	if (editPageView) {
		//書き換えたOutputTypeを戻す
		request.setAttribute(Constants.OUTPUT_TYPE, outputType);
	}

	//書き換えた定義名を戻す
	request.setAttribute(Constants.DEF_NAME, defName);
	//書き換えた参照元を戻す
	request.setAttribute(Constants.EDITOR_PARENT_ENTITY, parentEntity);
%>
