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
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.function.Supplier"%>
<%@ page import="org.iplass.mtp.impl.util.ConvertUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission" %>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.EntityManager" %>
<%@ page import="org.iplass.mtp.entity.GenericEntity"%>
<%@ page import="org.iplass.mtp.entity.LoadOption"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.IndexType"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.query.PreparedQuery"%>
<%@ page import="org.iplass.mtp.entity.query.Query" %>
<%@ page import="org.iplass.mtp.entity.query.SortSpec"%>
<%@ page import="org.iplass.mtp.entity.query.SortSpec.SortType"%>
<%@ page import="org.iplass.mtp.entity.query.condition.Condition"%>
<%@ page import="org.iplass.mtp.entity.query.condition.expr.And"%>
<%@ page import="org.iplass.mtp.entity.query.condition.predicate.Equals"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.VersionControlReferenceType"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.DetailFormView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityView"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.FormViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.LoadEntityContext"%>
<%@ page import="org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.LinkProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.EditPage"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.UrlParameterActionType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.element.Element"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailCommandContext"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.LoadEntityInterrupterHandler"%>
<%@ page import="org.iplass.gem.command.generic.reflink.GetReferenceLinkItemCommand"%>
<%@ page import="org.iplass.gem.command.generic.reftree.SearchTreeDataCommand"%>
<%@ page import="org.iplass.gem.command.generic.refunique.GetReferenceUniqueItemCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%!
	List<Entity> getSelectItems(ReferencePropertyEditor editor, Condition defaultCondition, Entity entity,
			PropertyEditor upperEditor) {
		Condition condition = defaultCondition;

		boolean doSearch = true;
		LinkProperty linkProperty = editor.getLinkProperty();
		if (linkProperty != null) {
			//連動の場合は上位値を取得して値が設定されている場合のみ検索
			doSearch = false;
			if (entity != null) {
				Object upperValue = entity.getValue(linkProperty.getLinkFromPropertyName());
				if (upperValue != null) {
					//参照元の値を条件に追加
					String upperPropName = linkProperty.getLinkToPropertyName();
					if (upperValue instanceof Entity) {
						upperPropName = upperPropName + "." + Entity.OID;
						upperValue = ((Entity)upperValue).getOid();
					}
					if (condition != null) {
						condition = new And(condition, new Equals(upperPropName, upperValue));
					} else {
						condition = new Equals(upperPropName, upperValue);
					}
					doSearch = true;
				}
			} else {
				//新規時のSelectPropertyEditorのdefaultValueのチェック(ReferencePropertyEditorはdefaultValueはなし)
				if (upperEditor != null) {
					if (upperEditor instanceof SelectPropertyEditor) {
						SelectPropertyEditor spe = (SelectPropertyEditor)upperEditor;
						String defaultValue = spe.getDefaultValue();
						if (defaultValue != null) {
							if (condition != null) {
								condition = new And(condition, new Equals(linkProperty.getLinkToPropertyName(), defaultValue));
							} else {
								condition = new Equals(linkProperty.getLinkToPropertyName(), defaultValue);
							}
							doSearch = true;
						}
					}
				}
			}
		}

		if (doSearch) {
			Query q = new Query();
			q.from(editor.getObjectName());
			q.select(Entity.OID, Entity.VERSION);
			if (editor.getDisplayLabelItem() != null) {
				q.select().add(editor.getDisplayLabelItem());
			} else {
				q.select().add(Entity.NAME);
			}
			if (condition != null) {
				q.where(condition);
			}
			if (editor.getSortType() != null) {
				String sortItem = editor.getSortItem() != null ? editor.getSortItem() : Entity.OID;
				if (!Entity.OID.equals(sortItem) && !Entity.NAME.equals(sortItem)) {
					q.select().add(sortItem);
				}
				SortType sortType = SortSpec.SortType.ASC;
				if ("DESC".equals(editor.getSortType().name())) {
					sortType = SortSpec.SortType.DESC;
				}
				q.order(new SortSpec(sortItem, sortType));
			}

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			return em.searchEntity(q).getList();
		} else {
			return Collections.emptyList();
		}
	}

	PropertyEditor getLinkUpperPropertyEditor(String defName, String viewName, LinkProperty linkProperty, Entity entity) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		return evm.getPropertyEditor(defName, Constants.VIEW_TYPE_DETAIL, viewName, linkProperty.getLinkFromPropertyName(), entity);
	}

	String getLinkUpperType(PropertyEditor editor) {
		if (editor != null) {
			if (editor instanceof SelectPropertyEditor) {
				SelectPropertyEditor spe = (SelectPropertyEditor)editor;
				if (spe.getDisplayType() == SelectDisplayType.SELECT) {
					return "select";
				} else if (spe.getDisplayType() == SelectDisplayType.RADIO
						|| spe.getDisplayType() == SelectDisplayType.CHECKBOX) {
					//CheckBoxの場合も多重度が1の場合のみRadioになるのでラジオで(CheckBoxの場合、反応しない)
					return "radio";
				}
			} else if (editor instanceof ReferencePropertyEditor) {
				ReferencePropertyEditor rpe = (ReferencePropertyEditor)editor;
				if (rpe.getDisplayType() == ReferenceDisplayType.SELECT) {
					return "select";
				} else if (rpe.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
					//CheckBoxの場合も多重度が1の場合のみRadioになるのでラジオで(CheckBoxの場合、反応しない)
					return "radio";
				} else if (rpe.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
					return "select";
				}
			}
		}
		return null;
	}

	//Linkタイプ、Labelタイプの場合の参照Entityのチェック
	//初期値として設定された際に、NameやVersionが未指定の場合を考慮して詰め直す
	List<Entity> getLinkTypeItems(String defName, String viewName, Object propValue, ReferenceProperty pd, ReferencePropertyEditor editor, Element element) {
		if (propValue == null) {
			return Collections.emptyList();
		}

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		LoadEntityInterrupterHandler handler = getLoadEntityInterrupterHandler(defName, viewName, em, edm, evm);

		List<Entity> entityList = new ArrayList<Entity>();
		if (propValue instanceof Entity[]) {
			Entity[] entities = (Entity[]) propValue;
			if (entities != null) {
				for (Entity refEntity : entities) {
					Entity entity = loadItem(refEntity, editor, pd, handler, em, element);
					if (entity != null) {
						entityList.add(entity);
					}
				}
			}
		} else if (propValue instanceof Entity) {
			Entity refEntity = (Entity) propValue;
			if (refEntity != null) {
				Entity entity = loadItem(refEntity, editor, pd, handler, em, element);
				if (entity != null) {
					entityList.add(entity);
				}
			}
		}
		return entityList;
	}

	LoadEntityInterrupterHandler getLoadEntityInterrupterHandler(String defName, String viewName, EntityManager em, EntityDefinitionManager edm, EntityViewManager evm) {
		DetailCommandContext context = new DetailCommandContext(TemplateUtil.getRequestContext(), defName, viewName, em, edm);//ここでこれを作るのはちょっと微妙だが・・・
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));
		return context.getLoadEntityInterrupterHandler();
	}

	Entity loadItem(final Entity refEntity, final ReferencePropertyEditor editor, final ReferenceProperty pd, final LoadEntityInterrupterHandler handler, final EntityManager em, final Element element) {
		//念のためOIDチェック
		if (refEntity.getOid() == null) {
			return null;
		}
		if (getDisplayPropLabel(editor, refEntity) == null || refEntity.getVersion() == null || isUniqueProp(editor)) {
			//name、versionは必須のためどちらかが未指定ならLoadする
			Entity entity = null;
			LoadOption loadOption = new LoadOption(false, false);
			final String refDefName = editor.getObjectName();
			final LoadEntityContext leContext = handler.beforeLoadReference(refDefName, loadOption, pd, element, LoadType.VIEW);
			if (leContext.isDoPrivileged()) {
				entity = AuthContext.doPrivileged(new Supplier<Entity>() {

					@Override
					public Entity get() {
						return em.load(refEntity.getOid(), refEntity.getVersion(), refDefName, leContext.getLoadOption());
					}
				});
			} else {
				entity = em.load(refEntity.getOid(), refEntity.getVersion(), refDefName, leContext.getLoadOption());
			}
			handler.afterLoadReference(entity, loadOption, pd, element, LoadType.VIEW);
			return entity;
		} else {
			return refEntity;
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
%>
<%
	String contextPath = TemplateUtil.getTenantContextPath();
	AuthContext auth = AuthContext.getCurrentContext();

	//Request情報取得
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	ReferenceProperty pd = (ReferenceProperty) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	OutputType outputType = (OutputType)request.getAttribute(Constants.OUTPUT_TYPE);
	String viewName = (String)request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) {
		viewName = "";
	} else {
		viewName = StringUtil.escapeHtml(viewName);
	}

	boolean isInsert = Constants.EXEC_TYPE_INSERT.equals(execType);
	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	if (nest == null) nest = false;
	Boolean useBulkView = (Boolean) request.getAttribute(Constants.BULK_UPDATE_USE_BULK_VIEW);
	if (useBulkView == null) useBulkView = false;

	String viewType = Constants.VIEW_TYPE_DETAIL;
	if (outputType == OutputType.BULK && !useBulkView) {
		viewType = Constants.VIEW_TYPE_BULK;
	} else if (outputType == OutputType.BULK && useBulkView) {
		viewType = Constants.VIEW_TYPE_MULTI_BULK;
	}

	//本体のEntity
	Entity parentEntity = (Entity) request.getAttribute(Constants.EDITOR_PARENT_ENTITY);
	String parentOid = parentEntity != null ? parentEntity.getOid() : "";
	String parentVersion = parentEntity != null && parentEntity.getVersion() != null ? parentEntity.getVersion().toString() : "";

	//表示判断スクリプトエンティティ
	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
	String rootOid = rootEntity != null ? rootEntity.getOid() : "";
	String rootVersion = rootEntity != null && rootEntity.getVersion() != null ? rootEntity.getVersion().toString() : "";

	//Property情報取得
	boolean isMappedby = pd.getMappedBy() != null;
	boolean isMultiple = pd.getMultiplicity() != 1;

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;
	
	//権限チェック
	boolean editable = true;
	if (isVirtual) {
		editable = true;//仮想プロパティは権限チェック要らない
	} else {
		if(isInsert) {
			editable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.CREATE));
		} else {
			editable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.UPDATE));
		}
	}
	boolean updatable = ((pd == null || pd.isUpdatable()) || isInsert) && editable;

	//Editorの設定値取得
	String refDefName = editor.getObjectName();
	String propName = editor.getPropertyName();
	boolean hideDeleteButton = editor.isHideDeleteButton();
	boolean hideRegistButton = editor.isHideRegistButton();
	boolean hideSelectButton = editor.isHideSelectButton();
	boolean refEdit = editor.isEditableReference();
	boolean editPageDetail = editor.getEditPage() == null || editor.getEditPage() == EditPage.DETAIL;

	//ネストプロパティ内でのネスト表示は禁止、ひとまずSelectに（非表示でもいいか？）
	if (nest && editor.getDisplayType() == ReferenceDisplayType.NESTTABLE) {
		editor.setDisplayType(ReferenceDisplayType.SELECT);
	}

	//refSectionIndexがnullではなければ、参照セクション内でネストされています。
	Integer refSectionIndex = (Integer)request.getAttribute(Constants.REF_SECTION_INDEX);

	//Action定義取得
	String _viewName = editor.getViewName() != null ? editor.getViewName() : "";
	String urlPath = ViewUtil.getParamMappingPath(refDefName, _viewName);

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

	Condition condition = null;
	if (editor.getCondition() != null && !editor.getCondition().isEmpty()) {
		condition = new PreparedQuery(editor.getCondition()).condition(null);
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_Edit.pleaseSelect");
	}

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (ViewUtil.isAutocompletionTarget()) {// FIXME テーブルはいらん？
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/reference/ReferencePropertyAutocompletion.jsp");
	}

	//タイプ毎に表示内容かえる
	if (editor.getDisplayType() == ReferenceDisplayType.LINK && updatable && !isMappedby) {
		//リンク
		String ulId = "ul_" + propName;

		if (!editPageDetail) {
			//参照モードなのでカスタムスタイル変更
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, propValue);
			}
		}

		//初期値として設定された際に、NameやVersionが未指定の場合を考慮して詰め直す
		List<Entity> entityList = getLinkTypeItems(rootDefName, viewName, propValue, pd, editor, element);

		//ネストテーブルに使われる際に、プロパティ名にカッコとドット区切りを入れ替える
		String _propName = propName.replace("[", "").replace("]","").replace(".", "_");
		String toggleInsBtnFunc = "toggleInsBtn_" + StringUtil.escapeJavaScript(_propName);
%>
<script type="text/javascript">
<%-- 新規ボタン表示/非表示--%>
function <%=toggleInsBtnFunc%>() {
	var _propName = "<%=StringUtil.escapeJavaScript(propName)%>".replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
	var display = <%=pd.getMultiplicity() == -1 %> || $("#ul_" + _propName).children("li:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#ins_btn_" + _propName).toggle(display);
}
</script>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="<c:out value="<%=(!hideDeleteButton && updatable) %>"/>" class="mb05">
<%
		for (int i = 0; i < entityList.size(); i++) {
			Entity refEntity = entityList.get(i);
			String liId = "li_" + propName + i;
			String linkId = propName + "_" + refEntity.getOid();
			String key = refEntity.getOid() + "_" + refEntity.getVersion();
			String dispPropLabel = getDisplayPropLabel(editor, refEntity);
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<%
			if (editPageDetail) {
				String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
					+ ", '" + refEntity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", " + refEdit 
					+ ", null"
					+ ", '" + rootDefName + "'"
					+ ", '" + viewName + "'"
					+ ", '" + propName + "'"
					+ ", '" + viewType + "'"
					+ ", '" + refSectionIndex + "'"
					+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
					+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'" 
					+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" 
 id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>"/>" 
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=dispPropLabel %>" /></a>
<%
				if (!hideDeleteButton && updatable) {
					String deleteItem = "deleteItem(" 
						+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
						+ ", " + toggleInsBtnFunc
						+ ")";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" 
 onclick="<c:out value="<%=deleteItem %>"/>" />
<%				}
			} else {
				String showReference = "showReference(" 
						+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
						+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
						+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
						+ ", '" + refEntity.getVersion() + "'"
						+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
						+ ", false"  
						+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" 
 id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>"/>" 
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=dispPropLabel %>" /></a>
<%
			}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" />
</li>
<%
		}
%>
</ul>
<%
		if (editPageDetail) {
			EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
			if (!hideSelectButton) {
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
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" id="<c:out value="<%=selBtnId %>"/>" data-propName="<c:out value="<%=propName %>"/>" />
<script type="text/javascript">
$(function() {
	var callback = function(entityList, deleteList, propName) {
		<%=toggleInsBtnFunc%>();
<%
				if (editor.getSelectActionCallbackScript() != null) {
%>
<%-- XSS対応-メタの設定のため対応なし(editor.getSelectActionCallbackScript) --%>
<%=editor.getSelectActionCallbackScript()%>
<%
				}
%>
	};
	var key = "selectActionCallback_" + new Date().getTime();
	scriptContext[key] = callback;
	var delCallback = <%=toggleInsBtnFunc%>;
	var params = {
		selectAction: "<%=StringUtil.escapeJavaScript(selectAction) %>"
		, viewAction: "<%=StringUtil.escapeJavaScript(viewAction) %>"
		, defName: "<%=StringUtil.escapeJavaScript(refDefName) %>"
		, multiplicity: "<%=pd.getMultiplicity() %>"
		, urlParam: "<%=StringUtil.escapeJavaScript(selBtnUrlParam) %>"
		, refEdit: <%=refEdit %>
		, callbackKey: key
		, specVersionKey: "<%=StringUtil.escapeJavaScript(specVersionKey) %>"
		, viewName: "<%=StringUtil.escapeJavaScript(_viewName) %>"
		, permitConditionSelectAll: <%=editor.isPermitConditionSelectAll()%>
		, permitVersionedSelect: <%=editor.isPermitVersionedSelect()%>
		, parentDefName: "<%=StringUtil.escapeJavaScript(rootDefName)%>"
		, parentViewName: "<%=StringUtil.escapeJavaScript(viewName)%>"
		, entityOid: "<%=StringUtil.escapeJavaScript(rootOid)%>"
		, entityVersion: "<%=StringUtil.escapeJavaScript(rootVersion)%>"
		, viewType: "<%=StringUtil.escapeJavaScript(viewType)%>"
		, refSectionIndex: "<c:out value="<%=refSectionIndex%>"/>"
	}
	var $selBtn = $(":button[id='<%=StringUtil.escapeJavaScript(selBtnId) %>']");
	for (key in params) {
		$selBtn.attr("data-" + key, params[key]);
	}
	$selBtn.on("click", function() {
		searchReference(params.selectAction, params.viewAction, params.defName, $(this).attr("data-propName"), params.multiplicity, <%=isMultiple %>,
				 params.urlParam, params.refEdit, callback, this, params.viewName, params.permitConditionSelectAll, params.permitVersionedSelect, 
				 params.parentDefName, params.parentViewName, params.viewType, params.refSectionIndex, delCallback, params.entityOid, params.entityVersion);
	});

});
</script>
<%
			}
			if (auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.CREATE)) && !hideRegistButton) {
				String insBtnId = "ins_btn_" + propName;
				String insBtnStyle = "";
				if (pd.getMultiplicity() != -1 && entityList.size() >= pd.getMultiplicity()) insBtnStyle = "display: none;";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.new')}" class="gr-btn-02 modal-btn ins-btn" id="<c:out value="<%=insBtnId %>"/>" style="<c:out value="<%=insBtnStyle %>"/>" />
<script type="text/javascript">
$(function() {
	var callback = function(entity, propName) {
		<%=toggleInsBtnFunc%>();
<%
				if (editor.getInsertActionCallbackScript() != null) {
%>
<%-- XSS対応-メタの設定のため対応なし(editor.getInsertActionCallbackScript) --%>
<%=editor.getInsertActionCallbackScript()%>
<%
				}

				String insBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.ADD);
%>
	};
	var key = "insertActionCallback_" + new Date().getTime();
	scriptContext[key] = callback;
	var delCallback = <%=toggleInsBtnFunc%>;
	var params = {
		addAction: "<%=StringUtil.escapeJavaScript(addAction) %>"
		, viewAction: "<%=StringUtil.escapeJavaScript(viewAction) %>"
		, defName: "<%=StringUtil.escapeJavaScript(refDefName) %>"
		, propName: "<%=StringUtil.escapeJavaScript(propName) %>"
		, multiplicity: "<%=pd.getMultiplicity() %>"
		, urlParam: "<%=StringUtil.escapeJavaScript(insBtnUrlParam) %>"
		, parentOid: "<%=StringUtil.escapeJavaScript(parentOid)%>"
		, parentVersion: "<%=StringUtil.escapeJavaScript(parentVersion)%>"
		, parentDefName: "<%=StringUtil.escapeJavaScript(rootDefName)%>"
		, parentViewName: "<%=StringUtil.escapeJavaScript(viewName)%>"
		, viewType: "<%=StringUtil.escapeJavaScript(viewType)%>"
		, refSectionIndex: "<c:out value="<%=refSectionIndex%>"/>"
		, entityOid: "<%=StringUtil.escapeJavaScript(rootOid)%>"
		, entityVersion: "<%=StringUtil.escapeJavaScript(rootVersion)%>"
		, refEdit: <%=refEdit %>
		, callbackKey: key
	}
	var $insBtn = $(":button[id='<%=StringUtil.escapeJavaScript(insBtnId) %>']");
	for (key in params) {
		$insBtn.attr("data-" + key, params[key]);
	}
	$insBtn.on("click", function() {
		insertReference(params.addAction, params.viewAction, params.defName, params.propName, params.multiplicity,
				 params.urlParam, params.parentOid, params.parentVersion, params.parentDefName, params.parentViewName, 
				 params.refEdit, callback, this, delCallback, params.viewType, params.refSectionIndex, params.entityOid, params.entityVersion);
	});

});
</script>
<%
			}
		}
	} else if (editor.getDisplayType() == ReferenceDisplayType.SELECT && updatable && !isMappedby) {
		//リスト
		PropertyEditor upperEditor = null;
		String upperType = null;
		if (editor.getLinkProperty() != null) {
			upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty(), rootEntity);
			upperType = getLinkUpperType(upperEditor);
		}

		List<Entity> entityList = getSelectItems(editor, condition, entity, upperEditor);

		//リスト
		List<String> oid = new ArrayList<String>();
		if (propValue instanceof Entity[]) {
			Entity[] entities = (Entity[]) propValue;
			if (entities != null) {
				for (Entity refEntity : entities) {
					if (refEntity != null && refEntity.getOid() != null) {
						oid.add(refEntity.getOid());
						if (editor.getDisplayLabelItem() != null) {
							loadReferenceEntityProperty(refEntity, editor.getDisplayLabelItem());
						}
					}
				}
			}
		} else if (propValue instanceof Entity) {
			Entity refEntity = (Entity) propValue;
			if (refEntity != null && refEntity.getOid() != null) {
				oid.add(refEntity.getOid());
				if (editor.getDisplayLabelItem() != null) {
					loadReferenceEntityProperty(refEntity, editor.getDisplayLabelItem());
				}
			}
		}

		String multiple = isMultiple ? " multiple" : "";
		String size = isMultiple ? "5" : "1";

		if (editor.getLinkProperty() != null && upperType != null && !isMultiple) {
			//連動設定(連動元のタイプがサポートの場合のみ、かつ多重度は1のみサポート)
			LinkProperty link = editor.getLinkProperty();

%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr refLinkSelect" style="<c:out value="<%=customStyle%>"/>" size="<c:out value="<%=size %>"/>"
 data-defName="<c:out value="<%=rootDefName %>"/>"
 data-viewType="<%=viewType %>"
 data-viewName="<c:out value="<%=viewName %>"/>"
 data-propName="<c:out value="<%=pd.getName() %>"/>"
 data-linkName="<c:out value="<%=link.getLinkFromPropertyName() %>"/>"
 data-prefix=""
 data-getItemWebapiName="<%=GetReferenceLinkItemCommand.WEBAPI_NAME %>"
 data-upperType="<c:out value="<%=upperType %>"/>"
 data-entityOid="<c:out value="<%=StringUtil.escapeJavaScript(rootOid) %>"/>"
 data-entityVersion="<c:out value="<%=StringUtil.escapeJavaScript(rootVersion) %>"/>"
>
<%
			if (!isMultiple) {
%>
<option value=""><%= pleaseSelectLabel %></option>
<%
			}
			for (Entity refEntity : entityList) {
				String selected = oid.contains(refEntity.getOid()) ? " selected" : "";
				String _value = refEntity.getOid() + "_" + refEntity.getVersion();
				String displayPropLabel = getDisplayPropLabel(editor, refEntity);
%>
<option value="<c:out value="<%=_value %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=displayPropLabel %>" /></option>
<%
			}
%>
</select>
<%
		} else {
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>" size="<c:out value="<%=size %>"/>" <c:out value="<%=multiple %>"/>>
<%
			if (!isMultiple) {
%>
<option value=""><%= pleaseSelectLabel %></option>
<%
			}
%>
<%
			for (Entity refEntity : entityList) {
				String selected = oid.contains(refEntity.getOid()) ? " selected" : "";
				String _value = refEntity.getOid() + "_" + refEntity.getVersion();
				String displayPropLabel = getDisplayPropLabel(editor, refEntity);
%>
<option value="<c:out value="<%=_value %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=displayPropLabel %>" /></option>
<%
			}
		}
%>
</select>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX && updatable && !isMappedby) {
		//チェックボックス
		PropertyEditor upperEditor = null;
		String upperType = null;
		if (editor.getLinkProperty() != null) {
			upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty(), rootEntity);
			upperType = getLinkUpperType(upperEditor);
		}

		List<Entity> entityList = getSelectItems(editor, condition, entity, upperEditor);

		//リスト
		List<String> oid = new ArrayList<String>();
		if (propValue instanceof Entity[]) {
			Entity[] entities = (Entity[]) propValue;
			if (entities != null) {
				for (Entity refEntity : entities) {
					if (refEntity != null && refEntity.getOid() != null) {
						oid.add(refEntity.getOid());
						if (editor.getDisplayLabelItem() != null) {
							loadReferenceEntityProperty(refEntity, editor.getDisplayLabelItem());
						}
					}
				}
			}
		} else if (propValue instanceof Entity) {
			Entity refEntity = (Entity) propValue;
			if (refEntity != null && refEntity.getOid() != null) {
				oid.add(refEntity.getOid());
				if (editor.getDisplayLabelItem() != null) {
					loadReferenceEntityProperty(refEntity, editor.getDisplayLabelItem());
				}
			}
		}
		String cls = "list-check-01";
		if (!isMultiple) cls = "list-radio-01";

		//選択解除可能か判定
		String radioTogglable = required ? "" : "radio-togglable";
		
		if (editor.getLinkProperty() != null && upperType != null && !isMultiple) {
			//連動設定(連動元のタイプがサポートの場合のみ、かつ多重度は1のみサポート)
			LinkProperty link = editor.getLinkProperty();
%>
<ul class="<c:out value="<%=cls %>"/> refLinkRadio"
 data-itemName="<c:out value="<%=propName %>"/>"
 data-defName="<c:out value="<%=rootDefName %>"/>"
 data-viewType="<%=viewType %>"
 data-viewName="<c:out value="<%=viewName %>"/>"
 data-propName="<c:out value="<%=pd.getName() %>"/>"
 data-radioTogglable="<c:out value="<%=radioTogglable %>"/>"
 data-linkName="<c:out value="<%=link.getLinkFromPropertyName() %>"/>"
 data-prefix=""
 data-getItemWebapiName="<%=GetReferenceLinkItemCommand.WEBAPI_NAME %>"
 data-upperType="<c:out value="<%=upperType %>"/>"
 data-customStyle="<c:out value="<%=customStyle%>"/>"
 data-entityOid="<c:out value="<%=StringUtil.escapeJavaScript(rootOid)%>"/>"
 data-entityVersion="<c:out value="<%=StringUtil.escapeJavaScript(rootVersion)%>"/>"
>
<%
			for (Entity refEntity : entityList) {
%>
<li><label style="<c:out value="<%=customStyle%>"/>">
<%
				String checked = oid.contains(refEntity.getOid()) ? " checked" : "";
				String _value = refEntity.getOid() + "_" + refEntity.getVersion();
				String displayPropLabel = getDisplayPropLabel(editor, refEntity);
				if (isMultiple) {
%>
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=displayPropLabel %>" />
<%
				} else {
%>
<input type="radio" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" class="<%=radioTogglable%>" <c:out value="<%=checked %>"/> /><c:out value="<%=displayPropLabel %>" />
<%
				}
%>
</label></li>
<%
			}
%>
</ul>
<%
		} else {
%>
<ul class="<c:out value="<%=cls %>"/>">
<%
			for (Entity refEntity : entityList) {
%>
<li><label style="<c:out value="<%=customStyle%>"/>">
<%
				String checked = oid.contains(refEntity.getOid()) ? " checked" : "";
				String _value = refEntity.getOid() + "_" + refEntity.getVersion();
				String displayPropLabel = getDisplayPropLabel(editor, refEntity);
				if (isMultiple) {
%>
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=displayPropLabel %>" />
<%
				} else {
%>
<input type="radio" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_value %>"/>" class="<%=radioTogglable%>" <c:out value="<%=checked %>"/> /><c:out value="<%=displayPropLabel %>" />
<%
				}
%>
</label></li>
<%
			}
		}
%>
</ul>
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE && updatable && !isMappedby) {
		//ツリー(基本はリンクと同じ、選択ダイアログを変える)
		String ulId = "ul_" + propName;

		if (!editPageDetail) {
			//参照モードなのでカスタムスタイル変更
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, propValue);
			}
		}

		//初期値として設定された際に、NameやVersionが未指定の場合を考慮して詰め直す
		List<Entity> entityList = getLinkTypeItems(rootDefName, viewName, propValue, pd, editor, element);

		//ネストテーブルに使われる際に、プロパティ名にカッコとドット区切りを入れ替える
		String _propName = propName.replace("[", "").replace("]","").replace(".", "_");
		String toggleInsBtnFunc = "toggleInsBtn_" + StringUtil.escapeJavaScript(_propName);
		//コールバック関数キー
		String delCallbackKey = "delTreeRefCallback_" + StringUtil.escapeJavaScript(propName);
		String selCallbackKey = "selTreeRefCallback_" + StringUtil.escapeJavaScript(propName);
%>
<script type="text/javascript">
<%-- 新規ボタン表示/非表示--%>
function <%=toggleInsBtnFunc%>() {
	var _propName = "<%=StringUtil.escapeJavaScript(propName)%>".replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");	
	toggleRefInsertBtn("ul_" + _propName, <%=pd.getMultiplicity()%>, "ins_btn_" + _propName);
}
</script>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="<c:out value="<%=(!hideDeleteButton && updatable) %>"/>" class="mb05">
<%
		for (int i = 0; i < entityList.size(); i++) {
			Entity refEntity = entityList.get(i);
			String liId = "li_" + propName + i;
			String linkId = propName + "_" + refEntity.getOid();
			String key = refEntity.getOid() + "_" + refEntity.getVersion();
			String displayPropLabel = getDisplayPropLabel(editor, refEntity);
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<%
			if (editPageDetail) {
				String showReference = "showReference(" 
						+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
						+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
						+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
						+ ", '" + refEntity.getVersion() + "'"
						+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
						+ ", " + refEdit 
						+ ", null"
						+ ", '" + rootDefName + "'"
						+ ", '" + viewName + "'"
						+ ", '" + propName + "'"
						+ ", '" + viewType + "'"
						+ ", '" + refSectionIndex + "'"
						+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
						+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'" 
						+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" 
 id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>"/>" 
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
				if (!hideDeleteButton && updatable) {
					String deleteItem = "deleteItem(" 
						+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
						+ ", " + toggleInsBtnFunc
						+ ")";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" 
 onclick="<c:out value="<%=deleteItem %>"/>" />
<%				}
			} else {
				String showReference = "showReference(" 
						+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
						+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
						+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
						+ ", '" + refEntity.getVersion() + "'"
						+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
						+ ", false"  
						+ ")";
%>
<a href="javascript:void(0)" class="modal-lnk" style="<c:out value="<%=customStyle%>"/>" 
 id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>"/>" 
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=displayPropLabel %>" /></a>
<%
			}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" />
</li>
<%
		}
%>
</ul>
<%
		if (editPageDetail) {
			if (!hideSelectButton) {
				String selBtnId = "sel_btn_" + propName;
				String title = getTitle(refDefName, viewName);

				String prefix = "";
				int index = propName.indexOf(pd.getName());
				if (index > 0) {
					//propNameから実際のプロパティ名を除去してプレフィックスを取得
					prefix = propName.substring(0, index);
				}

				String linkPropName = "";
				String upperType = "";
				if (editor.getLinkProperty() != null) {
					linkPropName = editor.getLinkProperty().getLinkFromPropertyName();
					PropertyEditor upperEditor = getLinkUpperPropertyEditor(rootDefName, viewName, editor.getLinkProperty(), rootEntity);
					upperType = getLinkUpperType(upperEditor);
				}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 sel-btn recursiveTreeTrigger" id="<c:out value="<%=selBtnId %>"/>"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<c:out value="<%=viewType%>"/>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=pd.getName()%>"/>"
 data-prefix="<c:out value="<%=prefix%>"/>"
 data-multiplicity="<c:out value="<%=pd.getMultiplicity()%>"/>"
 data-linkPropName="<c:out value="<%=linkPropName%>"/>"
 data-upperType="<c:out value="<%=upperType%>"/>"
 data-webapiName="<%=SearchTreeDataCommand.WEBAPI_NAME %>"
 data-container="<c:out value="<%=ulId %>"/>"
 data-title="<c:out value="<%=title%>"/>"
 data-deletable="<c:out value="<%=(!hideDeleteButton && updatable) %>"/>"
 data-customStyle="<c:out value="<%=customStyle%>"/>"
 data-viewAction="<c:out value="<%=viewAction%>"/>"
 data-refDefName="<c:out value="<%=refDefName%>"/>"
 data-refEdit="<c:out value="<%=refEdit%>"/>"
 data-selCallbackKey="<c:out value="<%=selCallbackKey%>"/>"
 data-delCallbackKey="<c:out value="<%=delCallbackKey%>"/>"
 data-refSectionIndex="<c:out value="<%=refSectionIndex%>"/>"
 data-entityOid="<c:out value="<%=StringUtil.escapeJavaScript(rootOid)%>"/>"
 data-entityVersion="<c:out value="<%=StringUtil.escapeJavaScript(rootVersion)%>"/>"
 />
<%
			}
			if (auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.CREATE)) && !hideRegistButton) {
				String insBtnId = "ins_btn_" + propName;
				String insBtnStyle = "";
				if (pd.getMultiplicity() != -1 && entityList.size() >= pd.getMultiplicity()) insBtnStyle = "display: none;";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.new')}" class="gr-btn-02 modal-btn ins-btn" id="<c:out value="<%=insBtnId %>"/>" style="<c:out value="<%=insBtnStyle %>"/>"/>
<script type="text/javascript">
$(function() {
	var callback = function(entity, propName) {
		<%=toggleInsBtnFunc%>();
<%
				if (editor.getInsertActionCallbackScript() != null) {
%>
<%-- XSS対応-メタの設定のため対応なし(editor.getInsertActionCallbackScript) --%>
<%=editor.getInsertActionCallbackScript()%>
<%
				}

				EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
				String insBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.ADD);
%>
	};
	var key = "insertActionCallback_" + new Date().getTime();
	scriptContext[key] = callback;

	var delCallback = function() {
		<%=toggleInsBtnFunc%>();
	};
	var delCallbackKey = "<%=delCallbackKey%>";
	scriptContext[delCallbackKey] = delCallback;

	var selCallback = function() {
		<%=toggleInsBtnFunc%>();
	};
	var selCallbackKey = "<%=selCallbackKey%>";
	scriptContext[selCallbackKey] = selCallback;
	var params = {
		addAction: "<%=StringUtil.escapeJavaScript(addAction) %>"
		, viewAction: "<%=StringUtil.escapeJavaScript(viewAction) %>"
		, defName: "<%=StringUtil.escapeJavaScript(refDefName) %>"
		, propName: "<%=StringUtil.escapeJavaScript(propName) %>"
		, multiplicity: "<%=pd.getMultiplicity() %>"
		, urlParam: "<%=StringUtil.escapeJavaScript(insBtnUrlParam) %>"
		, parentOid: "<%=StringUtil.escapeJavaScript(parentOid)%>"
		, parentVersion: "<%=StringUtil.escapeJavaScript(parentVersion)%>"
		, parentDefName: "<%=StringUtil.escapeJavaScript(defName)%>"
		, parentViewName: "<%=StringUtil.escapeJavaScript(viewName)%>"
		, viewType: "<%=StringUtil.escapeJavaScript(viewType)%>"
		, refSectionIndex: "<c:out value="<%=refSectionIndex%>"/>"
		, entityOid: "<%=StringUtil.escapeJavaScript(rootOid)%>"
		, entityVersion: "<%=StringUtil.escapeJavaScript(rootVersion)%>"
		, refEdit: <%=refEdit %>
		, callbackKey: key
	}
	var $insBtn = $(":button[id='<%=StringUtil.escapeJavaScript(insBtnId)%>']");
	for (key in params) {
		$insBtn.attr("data-" + key, params[key]);
	}
	$insBtn.on("click", function() {
		insertReference(params.addAction, params.viewAction, params.defName, params.propName, params.multiplicity,
				 params.urlParam, params.parentOid, params.parentVersion, params.parentDefName, params.parentViewName, 
				 params.refEdit, callback, this, delCallback, params.viewType, params.refSectionIndex, params.entityOid, params.entityVersion);
	});

});
</script>
<%
			}
		}
	} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && isUniqueProp(editor) && updatable && !isMappedby) {
		//リンク
		String ulId = "ul_" + propName;

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

		int multiplicity = pd.getMultiplicity() == -1 ? -1 : pd.getMultiplicity() + 1;
		String selUniqueRefCallback = "selUniqueRefCallback_" + StringUtil.escapeJavaScript(propName);
		String insUniqueRefCallback = "insUniqueRefCallback_" + StringUtil.escapeJavaScript(propName);
		String toggleAddBtnFunc = "toggleAddBtn_" + StringUtil.escapeJavaScript(propName);
%>
<script type="text/javascript">
$(function() {
	var selUniqueRefCallback = function(entityList, deleteList, propName) {
<%
		if (editor.getSelectActionCallbackScript() != null) {
%>
<%-- XSS対応-メタの設定のため対応なし(editor.getSelectActionCallbackScript) --%>
<%=editor.getSelectActionCallbackScript()%>
<%
		}
%>
	};
	var selKey = "<%=selUniqueRefCallback%>";
	scriptContext[selKey] = selUniqueRefCallback;
	
	var insUniqueRefCallback = function(entity, propName) {
<%
		if (editor.getInsertActionCallbackScript() != null) {
%>
		<%-- XSS対応-メタの設定のため対応なし(editor.getInsertActionCallbackScript) --%>
		<%=editor.getInsertActionCallbackScript()%>
<%
		}

		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		String selBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.SELECT);
		String insBtnUrlParam = evm.getUrlParameter(rootDefName, editor, parentEntity, UrlParameterActionType.ADD);
%>
	};
	var insKey = "<%=insUniqueRefCallback%>";
	scriptContext[insKey] = insUniqueRefCallback;
});
</script>
<ul id="<c:out value="<%=ulId %>"/>" data-deletable="<c:out value="<%=(!hideDeleteButton && updatable) %>"/>" class="mb05">
<%
		//初期値として設定された際に、NameやVersionが未指定の場合を考慮して詰め直す
		List<Entity> entityList = getLinkTypeItems(rootDefName, viewName, propValue, pd, editor, element);
		int length = entityList.size();
		//多重度が１で、登録されたデータが1件も無い場合、空エンティティ1件を作成します。
		if (!isMultiple && length == 0) {
			entityList = new ArrayList<Entity>();
			entityList.add(new GenericEntity(editor.getObjectName()));
		}

		for (int i = 0; i < entityList.size(); i++) {
			Entity refEntity = entityList.get(i);
			String id = propName + i;
			String liId = "li_" + id;
			String linkId = propName + "_" + refEntity.getOid();
			String dispPropLabel = getDisplayPropLabel(editor, refEntity);

			String key = "";
			if (length > 0) key = refEntity.getOid() + "_" + refEntity.getVersion();

%>
<li id="<c:out value="<%=liId %>"/>" class="list-add unique-list refUnique"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<c:out value="<%=viewType%>"/>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=propName%>"/>"
 data-webapiName="<%=GetReferenceUniqueItemCommand.WEBAPI_NAME%>"
 data-selectAction="<c:out value="<%=selectAction %>"/>"
 data-viewAction="<c:out value="<%=viewAction %>"/>"
 data-addAction="<c:out value="<%=addAction %>"/>"
 data-selectUrlParam="<c:out value="<%=selBtnUrlParam %>"/>"
 data-insertUrlParam="<c:out value="<%=insBtnUrlParam %>"/>"
 data-refDefName="<c:out value="<%=refDefName%>"/>"
 data-refViewName="<c:out value="<%=_viewName%>"/>"
 data-refEdit="<c:out value="<%=refEdit%>"/>"
 data-specVersionKey="<c:out value="<%=specVersionKey%>"/>"
 data-permitConditionSelectAll="<%=editor.isPermitConditionSelectAll()%>"
 data-permitVersionedSelect="<%=editor.isPermitVersionedSelect()%>"
 <%-- 隠されたDummyRowが存在するので、多重度 +1を渡します。 --%>
 data-multiplicity="<%=multiplicity%>"
 data-selUniqueRefCallback="<c:out value="<%=selUniqueRefCallback%>"/>"
 data-insUniqueRefCallback="<c:out value="<%=insUniqueRefCallback%>"/>"
 data-refSectionIndex="<c:out value="<%=refSectionIndex%>"/>"
 data-entityOid="<c:out value="<%=StringUtil.escapeJavaScript(rootOid)%>"/>"
 data-entityVersion="<c:out value="<%=StringUtil.escapeJavaScript(rootVersion)%>"/>"
>
<span class="unique-key">
<%
			String str = getUniquePropValue(editor, refEntity);
%>
<input type="text" id="uniq_txt_<c:out value="<%=liId%>"/>" style="<c:out value="<%=customStyle%>"/>" value="<%=str %>" class="unique-form-size-01 inpbr" />
<%
			if (!hideSelectButton) {
				String selBtnId = "sel_btn_" + propName + i;
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" id="<c:out value="<%=selBtnId %>"/>" data-propName="<c:out value="<%=propName %>"/>" />
<%
			}

			if (auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.CREATE)) && !hideRegistButton) {
				String insBtnId = "ins_btn_" + propName + i;
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.new')}" class="gr-btn-02 modal-btn ins-btn" id="<c:out value="<%=insBtnId %>"/>"
 data-parentOid="<c:out value="<%=StringUtil.escapeJavaScript(parentOid)%>"/>"
 data-parentVersion="<c:out value="<%=StringUtil.escapeJavaScript(parentVersion)%>"/>"
/>
<%
			}

			String showReference = "showReference(" 
					+ "'" + StringUtil.escapeJavaScript(viewAction) + "'" 
					+ ", '" + StringUtil.escapeJavaScript(refDefName) + "'"
					+ ", '" + StringUtil.escapeJavaScript(refEntity.getOid()) + "'"
					+ ", '" + refEntity.getVersion() + "'"
					+ ", '" + StringUtil.escapeJavaScript(linkId) + "'"
					+ ", " + refEdit 
					+ ", null"
					+ ", '" + rootDefName + "'"
					+ ", '" + viewName + "'"
					+ ", '" + propName + "'"
					+ ", '" + viewType + "'"
					+ ", '" + refSectionIndex + "'"
					+ ", '" + StringUtil.escapeJavaScript(rootOid) + "'"
					+ ", '" + StringUtil.escapeJavaScript(rootVersion) + "'" 
					+ ")";
%>
</span>
<span class="unique-ref">
<a href="javascript:void(0)" class="modal-lnk" id="<c:out value="<%=linkId %>"/>" data-linkId="<c:out value="<%=linkId %>"/>" 
 onclick="<c:out value="<%=showReference %>"/>"><c:out value="<%=dispPropLabel %>" /></a>
<%

			if (!hideDeleteButton && updatable) {

				if (isMultiple) {
					String deleteItem = "deleteItem(" 
						+ "'" + StringUtil.escapeJavaScript(liId) + "'" 
						+ ", " + toggleAddBtnFunc
						+ ")";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" 
 onclick="<c:out value="<%=deleteItem %>"/>" />
<%
				} else {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
<%
				}
			}
%>
</span>
<input type="hidden" id="i_<c:out value="<%=liId%>"/>" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=key %>"/>" />
</li>
<%
		}

		String dummyRowId = "id_li_" + propName + "Dummmy";
%>
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add unique-list" style="display: none;"
 data-defName="<c:out value="<%=rootDefName%>"/>"
 data-viewType="<c:out value="<%=viewType%>"/>"
 data-viewName="<c:out value="<%=viewName%>"/>"
 data-propName="<c:out value="<%=propName%>"/>"
 data-webapiName="<%=GetReferenceUniqueItemCommand.WEBAPI_NAME%>"
 data-selectAction="<c:out value="<%=selectAction %>"/>"
 data-viewAction="<c:out value="<%=viewAction %>"/>"
 data-addAction="<c:out value="<%=addAction %>"/>"
 data-selectUrlParam="<c:out value="<%=selBtnUrlParam %>"/>"
 data-insertUrlParam="<c:out value="<%=insBtnUrlParam %>"/>"
 data-refDefName="<c:out value="<%=refDefName%>"/>"
 data-refViewName="<c:out value="<%=_viewName%>"/>"
 data-refEdit="<c:out value="<%=refEdit%>"/>"
 data-specVersionKey="<c:out value="<%=specVersionKey%>"/>"
 data-permitConditionSelectAll="<%=editor.isPermitConditionSelectAll()%>"
 data-permitVersionedSelect="<%=editor.isPermitVersionedSelect()%>"
 <%-- 隠されたDummyRowが存在するので、多重度 +1を渡します。 --%>
 data-multiplicity="<%=multiplicity%>"
 data-selUniqueRefCallback="<c:out value="<%=selUniqueRefCallback%>"/>"
 data-insUniqueRefCallback="<c:out value="<%=insUniqueRefCallback%>"/>"
 data-refSectionIndex="<c:out value="<%=refSectionIndex%>"/>"
 data-entityOid="<c:out value="<%=StringUtil.escapeJavaScript(rootOid)%>"/>"
 data-entityVersion="<c:out value="<%=StringUtil.escapeJavaScript(rootVersion)%>"/>"
>
<span class="unique-key">
<input type="text" style="<c:out value="<%=customStyle%>"/>" class="unique-form-size-01 inpbr" />
<%
		if (!hideSelectButton) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.select')}" class="gr-btn-02 modal-btn sel-btn" data-propName="<c:out value="<%=propName %>"/>" />
<%
		}

		if (auth.checkPermission(new EntityPermission(refDefName, EntityPermission.Action.CREATE)) && !hideRegistButton) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.new')}" class="gr-btn-02 modal-btn ins-btn"
 data-parentOid="<c:out value="<%=StringUtil.escapeJavaScript(parentOid)%>"/>"
 data-parentVersion="<c:out value="<%=StringUtil.escapeJavaScript(parentVersion)%>"/>"
/>
<%
		}
%>
</span>
<span class="unique-ref">
<a href="javascript:void(0)" class="modal-lnk"></a>
<%

		if (!hideDeleteButton && updatable) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
<%
		}
%>
</span>
<input type="hidden" />
</li>
</ul>
<input type="hidden" id="id_count_<c:out value="<%=propName%>"/>" value="<c:out value="<%=entityList.size()%>"/>" />
<%
		if (isMultiple) {
			String addBtnStyle = "";
			if (pd.getMultiplicity() != -1 && length >= pd.getMultiplicity()) addBtnStyle = "display: none;";

			String addUniqueRefItem = "addUniqueRefItem(" 
				+ "'" + StringUtil.escapeJavaScript(ulId) + "'" 
				+ ", " + multiplicity
				+ ", '" + StringUtil.escapeJavaScript(dummyRowId) + "'"
				+ ", '" + StringUtil.escapeJavaScript(propName) + "'"
				+ ", 'id_count_" + StringUtil.escapeJavaScript(propName) + "'"
				+ ", " + toggleAddBtnFunc
				+ ", " + toggleAddBtnFunc
				+ ")";
%>
<script type="text/javascript">
function <%=toggleAddBtnFunc%>() {
	var display = <%=pd.getMultiplicity() == -1 %> || $("#<%=StringUtil.escapeJavaScript(ulId)%> li:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#id_addBtn_<c:out value="<%=propName%>"/>").toggle(display);
}
</script>
<input type="button" id="id_addBtn_<c:out value="<%=propName%>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_Edit.add')}" class="gr-btn-02 add-btn" style="<%=addBtnStyle%>" 
 onclick="<c:out value="<%=addUniqueRefItem %>"/>" />
<%
		}

	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO && updatable && !isMappedby) {
		//連動コンボ
		//多重度1限定
%>
<jsp:include page="ReferencePropertyEditor_RefCombo.jsp" />
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.NESTTABLE) {
		//テーブル
		//include先で利用するためパラメータを詰めなおし
		//updatableかはinclude先で改めて判断しておく
		request.setAttribute(Constants.EDITOR_REF_MAPPEDBY, pd.getMappedBy());
%>
<jsp:include page="ReferencePropertyEditor_Table.jsp" />
<%
	} else {
		//初期値として設定された際に、NameやVersionが未指定の場合を考慮して詰め直す
		List<Entity> entityList = getLinkTypeItems(rootDefName, viewName, propValue, pd, editor, element);
		request.setAttribute(Constants.EDITOR_PROP_VALUE, entityList.toArray(new Entity[0]));
		request.setAttribute(Constants.OUTPUT_HIDDEN, true);
%>
<jsp:include page="ReferencePropertyEditor_View.jsp" />
<%
		request.removeAttribute(Constants.OUTPUT_HIDDEN);
	}
%>
