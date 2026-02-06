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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.impl.util.ConvertUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission" %>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.EntityManager"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.VersionControlReferenceType"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.IndexType"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.query.Query"%>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.And"%>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.Equals"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.FormViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.SearchFormView"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferenceComboSetting" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.EditPage"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.InsertType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.UrlParameterActionType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateReferencePropertyCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateTableOrderCommand"%>
<%@ page import="org.iplass.gem.command.generic.reftree.SearchTreeDataCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%!
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
	void searchParent(List<Entity> parentList, List<ReferenceComboSetting> settingList, ReferenceProperty crp, ReferenceComboSetting setting, String oid) {
		//子の親プロパティ
		EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(crp.getObjectDefinitionName());
		ReferenceProperty rp = (ReferenceProperty) ed.getProperty(setting.getPropertyName());
		if (rp != null) {
			String childOid = null;
			if (oid != null) {
				//子階層が指定されてたら、指定の子を持つ親階層を検索
				Query q = new Query().select(rp.getName() + "." + Entity.OID);
				if (setting.getDisplayLabelItem() != null) {
					q.select().add(rp.getName() + "." + setting.getDisplayLabelItem());
				} else {
					q.select().add(rp.getName() + "." + Entity.NAME);
				}
				q.from(crp.getObjectDefinitionName()).where(new Equals(Entity.OID, oid));
				Entity ret = ManagerLocator.getInstance().getManager(EntityManager.class).searchEntity(q).getFirst();
				if (ret != null && ret.getValue(rp.getName()) != null) {
					//最初の項目をデフォルト選択させる
					Entity ref = ret.getValue(rp.getName());
					childOid = ref.getOid();
					parentList.add(0, ref);
					settingList.add(0, setting);
				}
			}

			if (setting.getParent() != null && StringUtil.isNotBlank(setting.getParent().getPropertyName())) {
				searchParent(parentList, settingList, rp, setting.getParent(), childOid);
			}
		}
	}

	Entity[] getParents(Entity entity, ReferencePropertyEditor editor) {
		LinkedList<Entity> ret = new LinkedList<Entity>();
		String propName = editor.getReferenceRecursiveTreeSetting().getChildPropertyName();
		String displayPropName = editor.getDisplayLabelItem();
		searchParent(entity.getOid(), entity.getDefinitionName(), propName, displayPropName, ret);
		return ret.toArray(new Entity[]{});
	}
	void searchParent(String oid, String defName, String childPropName, String displayPropName, LinkedList<Entity> ret) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

		//指定のOIDを子にもつデータを検索
		Query query = new Query().select(Entity.OID, Entity.VERSION);
		if (displayPropName != null) {
			query.select().add(displayPropName);
		} else {
			query.select().add(Entity.NAME);
		}
		query.from(defName).where(new Equals(childPropName + ".oid", oid));
		Entity entity = em.searchEntity(query).getFirst();
		if (entity != null) {
			ret.addFirst(entity);

			//再帰で自分を子にもつデータを検索
			searchParent(entity.getOid(), defName, childPropName, displayPropName, ret);
		}
	}

	void loadReferenceEntityProperty(Entity refEntity, String... propNames) {
		if (refEntity == null || propNames == null || propNames.length == 0) return;
		Query q = new Query().select(propNames);
		q.from(refEntity.getDefinitionName());
		q.where(new And(new Equals(Entity.OID, refEntity.getOid()), new Equals(Entity.VERSION, refEntity.getVersion())));

		Entity ret = ManagerLocator.getInstance().getManager(EntityManager.class).searchEntity(q).getFirst();
		if (ret == null) return;

		for (String propName : propNames) {
			refEntity.setValue(propName, ret.getValue(propName));
		}
	}

	String getTitle(String defName, String viewName) {
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);

		EntityDefinition ed = edm.get(defName);
		EntityView ev = evm.get(defName);
		DetailFormView fv = null;
		if (ev != null) {
			fv = ev.getDetailFormView(viewName);
		}
		if (fv == null) fv = FormViewUtil.createDefaultDetailFormView(ed);

		return TemplateUtil.getMultilingualString(fv.getTitle(), fv.getLocalizedTitleList(), ed.getDisplayName(), ed.getLocalizedDisplayNameList());
	}

	String getDisplayPropLabel(ReferencePropertyEditor editor, Entity refEntity) {
		String displayPropName = editor.getDisplayLabelItem();
		if (displayPropName == null) {
			displayPropName = Entity.NAME;
		}
		return refEntity.getValue(displayPropName);
	}

	String getDisplayPropLabel(ReferenceComboSetting setting, Entity parent) {
		if (setting != null && setting.getDisplayLabelItem() != null) {
			String displayPropName = setting.getDisplayLabelItem();
			return parent.getValue(displayPropName);
		}
		return parent.getName();
	}

	boolean isUniqueProp(ReferencePropertyEditor editor) {
		if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && editor.getUniqueItem() != null) {
			// OIDをユニークキーフィールドとして使えるように
			if (Entity.OID.equals(editor.getUniqueItem())) return true;

			EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(editor.getObjectName());
			PropertyDefinition pd = ed.getProperty(editor.getUniqueItem());
			if (pd.getIndexType() == IndexType.UNIQUE || pd.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
				return true;
			}
		}
		return false;
	}

	String getUniquePropValue(ReferencePropertyEditor editor, Entity refEntity) {
		String uniquePropName = editor.getUniqueItem();
		if (uniquePropName == null || refEntity.getValue(uniquePropName) == null) return "";
		// FIXME ユニークキー項目のプロパティエディター定義が存在しないので、文字列に変換して問題ないかな。。
		String str = ConvertUtil.convertToString(refEntity.getValue(uniquePropName));
		return StringUtil.escapeHtml(str);
	}

	Integer toInteger(Object val) {
		if (val == null) return null;
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

	boolean checkActionType(List<UrlParameterActionType> actions, UrlParameterActionType actionType) {
		if (actions == null) {
			//未指定の場合は、SELECTとADDはOK
			return actionType == UrlParameterActionType.SELECT || actionType == UrlParameterActionType.ADD;
		} else {
			//指定されている場合は、対象かどうかをチェック
			return actions.contains(actionType);
		}
	}
%>
<%
	String contextPath = TemplateUtil.getTenantContextPath();
	AuthContext auth = AuthContext.getCurrentContext();

	//Request情報取得
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType outputType = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	ReferenceProperty pd = (ReferenceProperty) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String viewName = (String)request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) {
		viewName = "";
	} else {
		viewName = StringUtil.escapeHtml(viewName);
	}
	Boolean outputHidden = (Boolean) request.getAttribute(Constants.OUTPUT_HIDDEN);
	if (outputHidden == null) outputHidden = false;
	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	if (nest == null) nest = false;
	String reloadUrl = (String) request.getAttribute(Constants.EDITOR_REF_RELOAD_URL);

	//本体のEntity
	Entity parentEntity = (Entity) request.getAttribute(Constants.EDITOR_PARENT_ENTITY);
	String parentOid = parentEntity != null ? parentEntity.getOid() : "";
	String parentVersion = parentEntity != null && parentEntity.getVersion() != null ? parentEntity.getVersion().toString() : "";

	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
	String rootOid = rootEntity != null ? rootEntity.getOid() : "";
	String rootVersion = rootEntity != null && rootEntity.getVersion() != null ? rootEntity.getVersion().toString() : "";

	//refSectionIndexがnullではなければ、参照セクション内でネストされています。
	Integer refSectionIndex = (Integer)request.getAttribute(Constants.REF_SECTION_INDEX);
	String _refSectionIndex = refSectionIndex != null ? refSectionIndex.toString() : "";

	//Property情報取得
	boolean isMappedby = pd.getMappedBy() != null;
	boolean isMultiple = pd.getMultiplicity() != 1;
	String mappedBy = isMappedby ? pd.getMappedBy() : "";

	//権限チェック
	boolean editable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.UPDATE));
	boolean updatable = ((pd == null || pd.isUpdatable())) && editable;

	//Editorの設定値取得
	String refDefName = editor.getObjectName();
	String propName = editor.getPropertyName();
	boolean hideRegistButton = editor.isHideRegistButton();
	boolean hideSelectButton = editor.isHideSelectButton();
	boolean hideDeleteButton = editor.isHideDeleteButton();
	boolean refEdit = editor.isEditableReference();
	boolean editPageView = editor.getEditPage() != null && editor.getEditPage() == EditPage.VIEW;

	//ネストプロパティ内でのネスト表示は禁止、ひとまずSelectに（非表示でもいいか？）
	if (nest && editor.getDisplayType() == ReferenceDisplayType.NESTTABLE) {
		editor.setDisplayType(ReferenceDisplayType.SELECT);
	}

	//Action定義取得
	String urlPath = ViewUtil.getParamMappingPath(refDefName, editor.getViewName());

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

	//表示用のAction
	String viewAction = "";
	if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
		viewAction = contextPath + "/" + editor.getViewrefActionName() + urlPath;
	} else {
		viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
	}

	//参照プロパティ更新用のAction
	boolean isDialog = ViewUtil.isDialog(TemplateUtil.getRequestContext());
	String updateRefAction = null;
	if (isDialog) {
		updateRefAction = contextPath + "/" + UpdateReferencePropertyCommand.REF_ACTION_NAME;
	} else {
		updateRefAction = contextPath + "/" + UpdateReferencePropertyCommand.ACTION_NAME;
	}
	if (StringUtil.isNotBlank(viewName)) {
		updateRefAction = updateRefAction + "/" + viewName;
	}

	//リロード用URL
	if (reloadUrl == null || reloadUrl.isEmpty()) {
		reloadUrl = getViewAction(defName, viewName, parentOid, isDialog);
	}

	//ロードプロパティ
	List<String> loadPropNames = new ArrayList<String>();
	if (editor.getDisplayLabelItem() != null) loadPropNames.add(editor.getDisplayLabelItem());
	if (isUniqueProp(editor)) loadPropNames.add(editor.getUniqueItem());

	List<Entity> entityList = new ArrayList<Entity>();
	if (propValue instanceof Entity[]) {
		Entity[] entities = (Entity[]) propValue;
		if (entities != null) {
			entityList.addAll(Arrays.asList(entities));
			for (Entity refEntity : entities) {
				loadReferenceEntityProperty(refEntity, loadPropNames.toArray(new String[]{}));
			}
		}
	} else if (propValue instanceof Entity) {
		Entity refEntity = (Entity) propValue;
		if (refEntity != null) {
			entityList.add(refEntity);
			loadReferenceEntityProperty(refEntity, loadPropNames.toArray(new String[]{}));
		}
	}

	//カスタム入力スタイル
	String customStyle = "";
	if (outputType == OutputType.VIEW) {
		if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, propValue);
		}
	} else if (outputType == OutputType.EDIT) {
		//入力不可の場合
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
		}
	}

	if (editor.getDisplayType() == ReferenceDisplayType.LINK
			|| editor.getDisplayType() == ReferenceDisplayType.SELECT
			|| editor.getDisplayType() == ReferenceDisplayType.CHECKBOX
			|| editor.getDisplayType() == ReferenceDisplayType.TREE
			|| editor.getDisplayType() == ReferenceDisplayType.LABEL
			|| editor.getDisplayType() == ReferenceDisplayType.UNIQUE) {

		//リンク or リスト
		String ulId = "ul_" + propName;

		if (editor.getDisplayType() == ReferenceDisplayType.LINK
				&& (updatable && editPageView && outputType == OutputType.VIEW)) {
			//編集モードなのでカスタムスタイル変更
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
			}
		}
%>
<ul name="data-label-<c:out value="<%=propName %>"/>"  class="data-label" id="<c:out value="<%=ulId %>"/>" data-deletable="<c:out value="<%=(!hideDeleteButton && updatable) %>"/>">
<%
		for (int i = 0; i < entityList.size(); i++) {
			Entity refEntity = entityList.get(i);
			String liId = "li_" + propName + i;
			if (refEntity == null) continue;

			String displayPropLabel = getDisplayPropLabel(editor, refEntity);
			if (displayPropLabel == null) {
				displayPropLabel = "";
			}
%>
<li id="<c:out value="<%=liId %>"/>">
<%
			if (editor.getDisplayType() == ReferenceDisplayType.LABEL) {

				if (StringUtil.isNotEmpty(customStyle)) {
%>
<span name="data-label-<c:out value="<%=propName %>"/>"  style="<c:out value="<%=customStyle %>"/>">
<%
				}
%>
<c:out value="<%=displayPropLabel %>" />
<%
				if (StringUtil.isNotEmpty(customStyle)) {
%>
</span>
<%
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.LINK
					|| editor.getDisplayType() == ReferenceDisplayType.TREE
					|| editor.getDisplayType() == ReferenceDisplayType.UNIQUE) {
				if (editor.isShowRefComboParent() && editor.getDisplayType() == ReferenceDisplayType.TREE) {
					//親階層検索
					Entity[] parents = getParents(refEntity, editor);
					if (parents != null && parents.length > 0) {
						for (int j = 0; j < parents.length; j++) {
%>
<span><c:out value="<%=getDisplayPropLabel(editor, parents[j]) %>"/></span>&nbsp;&gt;&nbsp;
<%
						}
					}
				}
				if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && isUniqueProp(editor)) {
%>
<span><c:out value="<%=getUniquePropValue(editor, refEntity) %>" /></span>&nbsp;&nbsp;
<%
				}

				//Viewで編集モードの場合の詳細リンク、削除ボタン制御
				if (updatable && editPageView && outputType == OutputType.VIEW) {
					String _value = refEntity.getOid() + "_" + refEntity.getVersion();

					String _viewAction = StringUtil.escapeJavaScript(viewAction);
					String _refDefName = StringUtil.escapeJavaScript(refDefName);
					String _entityOid = refEntity.getOid() == null ? "" : StringUtil.escapeJavaScript(refEntity.getOid());
					String _reloadUrl = StringUtil.escapeJavaScript(reloadUrl);
					EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
					String _viewUrlParam = StringUtil.escapeJavaScript(
							evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.VIEW));
					String _dynamicParamCallback = "viewDynamicParamCallback_" + StringUtil.escapeJavaScript(propName);

					String viewEditableReference = "viewEditableReference(" 
						+ "'" + _viewAction + "'" 
						+ ", '" + _refDefName + "'"
						+ ", '" + _entityOid + "'"
						+ ", '" + _reloadUrl + "'"
						+ ", true"
						+ ", '" + _viewUrlParam + "'"
						+ ", '" + _dynamicParamCallback + "'"
						+ ")";
%>
<script>
$(function() {
	var dynamicParamCallback = function(urlParam) {
		<%if (checkActionType(editor.getDynamicUrlParameterAction(), UrlParameterActionType.VIEW) && StringUtil.isNotBlank(editor.getDynamicUrlParameter())) {%>
		<%=editor.getDynamicUrlParameter()%>
		<%} else {%>
		return urlParam;
		<%}%>
	};
	var dynamicParamCallbackKey = "<%=_dynamicParamCallback%>";
	scriptContext[dynamicParamCallbackKey] = dynamicParamCallback;
});
</script>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>"
 onclick="<c:out value="<%=viewEditableReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" />
<%
					if (!hideDeleteButton && updatable) {
						//削除ボタン
						String deleteId = "del_" + propName + i;
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_View.delete')}" class="gr-btn-02 reference-view-delete"
 id="<c:out value="<%=deleteId %>"/>" data-propertyName="<%=StringUtil.escapeJavaScript(propName)%>"/>
<script type="text/javascript">
$(function() {
	$(":button[id='<%=StringUtil.escapeJavaScript(deleteId)%>']").click(function() {
		var $tmp = $("#<%=StringUtil.escapeJavaScript(liId)%> :hidden[name='<%=StringUtil.escapeJavaScript(propName)%>']");
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
<%
					}
				} else {
					//参照のみ
					String linkId = propName + "_" + refEntity.getOid();

					String showReference = "showReference(" 
						+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
						+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
						+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
						+ ", '" + refEntity.getVersion() + "'"
						+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
						+ ", " + refEdit
						+ ", null"
						+ ", '" + StringUtil.escapeJavaScript(rootDefName) + "'"
						+ ", '" + StringUtil.escapeJavaScript(viewName) + "'"
						+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
						+ ", 'detail'"
						+ ", '" + _refSectionIndex + "'"
						+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
						+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'"
						+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>" />"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
				}
			} else {
				//Select,Checkbox,RefCombo
				String linkId = propName + "_" + refEntity.getOid();

				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
					+ ", '" + refEntity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", " + refEdit
					+ ", null"
					+ ", '" + StringUtil.escapeJavaScript(rootDefName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(viewName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
					+ ", 'detail'"
					+ ", '" + _refSectionIndex + "'"
					+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
					+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'"
					+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>" />"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
			}
			if (outputHidden) {
				String _value = refEntity.getOid() + "_" + refEntity.getVersion();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" />
<%
			}
%>
</li>
<%
		}
%>
</ul>
<%
		//Viewで編集モードの場合の選択ボタン、新規ボタン制御
		if ((editor.getDisplayType() == ReferenceDisplayType.LINK
					|| editor.getDisplayType() == ReferenceDisplayType.TREE)
				&& updatable && editPageView && outputType == OutputType.VIEW) {

			EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);

			//選択ボタン
			if (!isMappedby && !hideSelectButton) {
				String selBtnId = "sel_btn_" + propName;
				if (editor.getDisplayType() == ReferenceDisplayType.LINK) {
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
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_View.select')}" class="gr-btn-02 modal-btn mt05" id="<c:out value="<%=selBtnId %>"/>" data-specVersionKey="<c:out value="<%=specVersionKey%>" />" />
<script type="text/javascript">
$(function() {
	var dynamicParamCallback = function(urlParam) {
<%if (checkActionType(editor.getDynamicUrlParameterAction(), UrlParameterActionType.SELECT) && StringUtil.isNotBlank(editor.getDynamicUrlParameter())) {%>
<%=editor.getDynamicUrlParameter()%>
<%} else {%>
		return urlParam;
<%}%>
	};
	$(":button[id='<%=StringUtil.escapeJavaScript(selBtnId)%>']").click(function() {
		searchReferenceFromView("<%=StringUtil.escapeJavaScript(selectAction)%>", "<%=StringUtil.escapeJavaScript(updateRefAction)%>", "<%=StringUtil.escapeJavaScript(refDefName)%>", "<%=StringUtil.escapeJavaScript(ulId)%>", "<%=StringUtil.escapeJavaScript(propName)%>",
				<%=pd.getMultiplicity() %>, <%=isMultiple %>, "<%=StringUtil.escapeJavaScript(selBtnUrlParam)%>", "<%=StringUtil.escapeJavaScript(reloadUrl)%>", this, <%=editor.isPermitConditionSelectAll()%>, <%=editor.isPermitVersionedSelect()%>, dynamicParamCallback);
	});
});
</script>
<%
				} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
					String title = getTitle(refDefName, viewName);

					String prefix = "";
					int index = propName.indexOf(pd.getName());
					if (index > 0) {
						//propNameから実際のプロパティ名を除去してプレフィックスを取得
						prefix = propName.substring(0, index);
					}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 mt05 recursiveTreeTrigger" id="<c:out value="<%=selBtnId %>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=pd.getName()%>"/>"
 data-prefix="<c:out value="<%=prefix%>"/>"
 data-multiplicity="<c:out value="<%=pd.getMultiplicity()%>"/>"
 data-linkPropName=""
 data-webapiName="<%=SearchTreeDataCommand.WEBAPI_NAME %>"
 data-container="<c:out value="<%=ulId %>"/>"
 data-title="<c:out value="<%=title%>"/>"
 data-deletable="<c:out value="<%=(!hideDeleteButton && updatable) %>"/>"
 data-customStyle="<c:out value="<%=customStyle%>"/>"
 data-viewAction="<c:out value="<%=viewAction%>"/>"
 data-refDefName="<c:out value="<%=refDefName%>"/>"
 data-refEdit="<c:out value="<%=refEdit%>"/>"
 data-refSectionIndex="<%=_refSectionIndex%>"
 data-updateRefAction="<c:out value="<%=updateRefAction%>"/>"
 data-reloadUrl="<c:out value="<%=reloadUrl%>"/>"
 data-entityOid="<c:out value="<%=StringUtil.escapeJavaScript(rootOid)%>"/>"
 data-entityVersion="<c:out value="<%=StringUtil.escapeJavaScript(rootVersion)%>"/>"
 />
<%
				}
			}
			if (auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.CREATE)) && !nest && !hideRegistButton) {
				//新規ボタン
				String insBtnId = "ins_btn_" + propName;
				String insBtnStyle = "";
				if (pd.getMultiplicity() != -1 && entityList.size() >= pd.getMultiplicity()) insBtnStyle = "display: none;";

				String insBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.ADD);
				Integer orderPropValue = null;
				String orderPropName = StringUtil.escapeJavaScript(editor.getTableOrderPropertyName());
				if (orderPropName != null) {
					if (entityList.size() > 0) {
						if (editor.getInsertType() == InsertType.TOP) {
							Entity firstEntity = entityList.get(0);
							Integer firstOrderPropValue = toInteger(firstEntity.getValue(orderPropName));
							if (firstOrderPropValue != null) orderPropValue = firstOrderPropValue - 1;
						} else {
							Entity lastEntity = entityList.get(entityList.size() -1);
							Integer lastOrderPropValue = toInteger(lastEntity.getValue(orderPropName));
							if (lastOrderPropValue != null) orderPropValue = lastOrderPropValue + 1;
						}
					} else {
						orderPropValue = 0;
					}
				}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.new')}" class="gr-btn-02 modal-btn mt05" id="<c:out value="<%=insBtnId %>"/>" style="<c:out value="<%=insBtnStyle %>"/>" />
<script type="text/javascript">
$(function() {
	var dynamicParamCallback = function(urlParam) {
		<%if (checkActionType(editor.getDynamicUrlParameterAction(), UrlParameterActionType.ADD) && StringUtil.isNotBlank(editor.getDynamicUrlParameter())) {%>
		<%=editor.getDynamicUrlParameter()%>
		<%} else {%>
		return urlParam;
		<%}%>
	};
	$(":button[id='<%=StringUtil.escapeJavaScript(insBtnId)%>']").click(function() {
		insertReferenceFromView("<%=StringUtil.escapeJavaScript(addAction)%>", "<%=StringUtil.escapeJavaScript(refDefName)%>", "<%=StringUtil.escapeJavaScript(ulId)%>", <%=pd.getMultiplicity() %>,
				"<%=StringUtil.escapeJavaScript(insBtnUrlParam)%>", "<%=StringUtil.escapeJavaScript(parentOid)%>", "<%=StringUtil.escapeJavaScript(parentVersion)%>", "<%=StringUtil.escapeJavaScript(defName)%>",
				"<%=StringUtil.escapeJavaScript(mappedBy)%>", $(":hidden[name='oid']").val(), "<%=StringUtil.escapeJavaScript(updateRefAction)%>",
				"<%=StringUtil.escapeJavaScript(propName)%>", "<%=StringUtil.escapeJavaScript(reloadUrl)%>", "<%=StringUtil.escapeJavaScript(rootOid)%>",
				"<%=StringUtil.escapeJavaScript(rootVersion)%>", "<%=UpdateTableOrderCommand.WEBAPI_NAME%>", "<%=orderPropName%>", <%=orderPropValue%>, <%=editor.getInsertType() == InsertType.TOP%>, dynamicParamCallback);
	});
});
</script>
<%
			}
		}
	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
		//参照コンボ
		String ulId = "ul_" + propName;
%>
<ul class="data-label" id="<c:out value="<%=ulId %>"/>">
<%
		for (int i = 0; i < entityList.size(); i++) {
			Entity refEntity = entityList.get(i);
			String liId = "li_" + propName + i;
			if (refEntity == null) continue;

			String displayPropLabel = getDisplayPropLabel(editor, refEntity);
			if (displayPropLabel == null) {
				displayPropLabel = "";
			}
%>
<li id="<c:out value="<%=liId %>"/>">
<%
			if (editor.isShowRefComboParent()) {
				List<Entity> parentList = new ArrayList<Entity>();
				List<ReferenceComboSetting> settingList = new ArrayList<ReferenceComboSetting>();
				if (editor.getReferenceComboSetting() != null && StringUtil.isNotBlank(editor.getReferenceComboSetting().getPropertyName())) {
					searchParent(parentList, settingList, pd, editor.getReferenceComboSetting(), refEntity.getOid());
				}
				for (int j = 0; j < parentList.size(); j++) {
					Entity parent = parentList.get(j);
					ReferenceComboSetting setting = settingList.get(j);
%>
<span><c:out value="<%=getDisplayPropLabel(setting, parent) %>"/></span>&nbsp;&gt;&nbsp;
<%
				}
			}

			String linkId = propName + "_" + refEntity.getOid();

			String showReference = "showReference(" 
				+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
				+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
				+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
				+ ", '" + refEntity.getVersion() + "'"
				+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
				+ ", " + refEdit
				+ ", null"
				+ ", '" + StringUtil.escapeJavaScript(rootDefName) + "'"
				+ ", '" + StringUtil.escapeJavaScript(viewName) + "'"
				+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
				+ ", 'detail'"
				+ ", '" + _refSectionIndex + "'"
				+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
				+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'"
				+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>" />"
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
			if (outputHidden) {
				String _value = refEntity.getOid() + "_" + refEntity.getVersion();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" />
<%
			}
%>
</li>
<%
		}
%>
</ul>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
		//HIDDEN
%>
<ul name="data-hidden-<c:out value="<%=propName %>"/>">
<%
		for (int i = 0; i < entityList.size(); i++) {
			Entity refEntity = entityList.get(i);
			if (refEntity == null) continue;

			String _value = refEntity.getOid() + "_" + refEntity.getVersion();
%>
<li>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" />
</li>
<%
		}
%>
</ul>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.NESTTABLE) {
		//テーブル
		//include先で利用するためパラメータを詰めなおし
		request.setAttribute(Constants.EDITOR_EDITOR, editor);
		request.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
		request.setAttribute(Constants.EDITOR_REF_MAPPEDBY, mappedBy);
%>
<jsp:include page="ReferencePropertyEditor_Table.jsp" />
<%
	}
%>