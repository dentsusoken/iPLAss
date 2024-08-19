/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.gem.command.generic.detail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.HasDisplayScriptBindings;
import org.iplass.gem.command.generic.search.ResponseUtil;
import org.iplass.gem.command.generic.search.ResponseUtil.Func;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.generic.element.section.SortSetting;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebApi(
		name=GetMassReferencesCommand.WEBAPI_NAME,
		displayName="大規模参照プロパティ検索",
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"dispInfo", "count", "htmlData"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/detail/GetMassReferencesCommand", displayName="参照プロパティ検索")
public final class GetMassReferencesCommand extends DetailCommandBase implements HasDisplayScriptBindings{

	public static final String WEBAPI_NAME = "gem/generic/detail/getMassReference";

	/** EntityDefinitionManager */
	private EntityDefinitionManager edm = null;

	/** EntityViewManager */
	private EntityViewManager evm = null;

	/** EntityManager */
	private EntityManager em = null;

	/**
	 * コンストラクタ
	 */
	public GetMassReferencesCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		DetailCommandContext context = getContext(request);
		String defName = context.getDefinitionName();
		String propName = request.getParam(Constants.PROP_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String sortKey = request.getParam(Constants.SEARCH_SORTKEY);
		String sortType = request.getParam(Constants.SEARCH_SORTTYPE);
		String offsetStr = request.getParam(Constants.SEARCH_OFFSET);
		String isCount = request.getParam("isCount");
		String outputTypeStr = request.getParam(Constants.OUTPUT_TYPE);
		String elementId = request.getParam(Constants.ELEMENT_ID);

		int offset = 0;
		try {
			offset = Integer.parseInt(offsetStr);
		} catch (NumberFormatException e1) {
		}
		OutputType outputType = OutputType.valueOf(outputTypeStr);

		//Entity定義取得
		PropertyDefinition pd = context.getProperty(propName);

		if (pd instanceof ReferenceProperty) {
			ReferenceProperty rp = (ReferenceProperty) pd;

			Entity entity = getBindingEntity(request);

			//Section取得
			List<MassReferenceSection> sections = getMassReferenceSection(
					context.getEntityDefinition(), outputType, context.getView(), context.getViewName(), entity);
			MassReferenceSection section = null;
			if (StringUtil.isNotEmpty(elementId)) {
				//ElementIDで検索
				for (MassReferenceSection _section : sections) {
					if (elementId.equals(_section.getElementRuntimeId())) {
						if (_section.getPropertyName().equals(rp.getName())) {
							section = _section;
						}
						break;
					}
				}
			} else {
				//プロパティ名で検索
				for (MassReferenceSection _section : sections) {
					if (_section.getPropertyName().equals(rp.getName())) {
						section = _section;
						break;
					}
				}
			}

			if (section != null) {
				List<String> props = new ArrayList<>();
				props.add(Entity.OID);
				props.add(Entity.NAME);
				props.add(Entity.VERSION);
				List<DisplayInfo> dispInfo = new ArrayList<>();
				EntityDefinition red = edm.get(rp.getObjectDefinitionName());
				for (NestProperty np : section.getProperties()) {
					//表示設定確認
					if (OutputType.EDIT == outputType) {
						if (np.isHideDetail()) continue;
					} else if (OutputType.VIEW == outputType) {
						if (np.isHideView()) continue;
					}

					PropertyDefinition rpd = red.getProperty(np.getPropertyName());
					if (rpd instanceof ReferenceProperty) {
						addReferenceProperty(props, dispInfo, (ReferenceProperty) rpd, np, null, outputType);
					} else {
						if (!props.contains(np.getPropertyName())) {
							props.add(np.getPropertyName());
							if (np.getEditor() instanceof RangePropertyEditor) {
								RangePropertyEditor rpe = (RangePropertyEditor) np.getEditor();
								props.add(rpe.getToPropertyName());
							}
						}
						DisplayInfo di = new DisplayInfo();
						di.setName(np.getPropertyName());
						if (StringUtil.isNotBlank(np.getDisplayLabel())) {
							di.setDisplayName(TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList()));
						} else {
							di.setDisplayName(TemplateUtil.getMultilingualString(rpd.getDisplayName(), rpd.getLocalizedDisplayNameList()));
						}
						di.setWidth(np.getWidth());
						if (np.getEditor() != null && np.getEditor().isHide()) {
							di.setHide(true);
						} else {
							di.setHide(false);
						}
						dispInfo.add(di);
					}
				}
				request.setAttribute("dispInfo", dispInfo);

				Query query = new Query();
				query.select(props.toArray());
				query.from(rp.getObjectDefinitionName());

				And cond = new And(new Equals(rp.getMappedBy() + ".oid", context.getOid()));
				if (StringUtil.isNotBlank(section.getFilterConditionScript())) {
					Condition filterCond = evm.getMassReferenceSectionCondition(defName, section);
					if (filterCond != null) {
						cond.addExpression(filterCond);
					}
				}

				query.where(cond);

				if (!section.isHidePaging()) {
					// ページング非表示の場合、件数は不要で、limitもかけずに全件取得
					if ("true".equals(isCount)) {
						int count = countEntity(context.getLoadEntityInterrupterHandler(), query, rp, section, outputType);
						request.setAttribute("count", count);
					}

					int limit = section.getLimit();
					if (limit == 0) limit = 10;
					query.limit(limit, offset);
				}

				OrderBy orderBy = getOrderBy(section, red, sortKey, sortType);
				query.setOrderBy(orderBy);

				//User名に変換が必要なプロパティを取得
				final Set<String> userNameProperties = getUseUserPropertyEditorPropertyName(section.getProperties(),
						OutputType.EDIT == outputType);

				//UserのOIDリスト
				final List<String> userOids = new ArrayList<>();

				List<Entity> entityList = search(context.getLoadEntityInterrupterHandler(),
						query, rp, section, outputType, userNameProperties, userOids);

				if (!userOids.isEmpty()) {
					setUserInfoMap(context, userOids);
				}

				try {
					getHtmlData(request, defName, viewName, propName, rp, section.getProperties(), entityList, outputType);
				} catch (IOException e) {
					throw new SystemException(e);
				} catch (ServletException e) {
					throw new SystemException(e);
				}
			}

		}
		return null;
	}

	private OrderBy getOrderBy(MassReferenceSection section, EntityDefinition red, String sortKey, String sortType) {
		OrderBy orderBy = new OrderBy();

		Set<String> addNames = new HashSet<>();
		if (StringUtil.isNotEmpty(sortKey)) {
			//パラメータで指定されている場合は先頭で適用
			String key = getSortKey(section, red, sortKey);
			orderBy.add(new SortSpec(key, getSortType(sortType)));
			addNames.add(key);
		}
		if (!section.getSortSetting().isEmpty()) {
			//デフォルトが指定されている場合は適用
			List<SortSetting> setting = section.getSortSetting();
			for (SortSetting ss : setting) {
				if (ss.getSortKey() != null) {
					String key = getSortKey(section, red, ss.getSortKey());
					if (!addNames.contains(key)) {
						orderBy.add(key, getSortType(ss.getSortType().name()), getNullOrderingSpec(ss.getNullOrderType()));
						addNames.add(key);
					}
				}
			}
		}

		if (orderBy.getSortSpecList().isEmpty()) {
			//ソート項目がない場合はデフォルト設定
			orderBy.add(new SortSpec(getSortKey(section, red, null), getSortType(null)));
		}

		return orderBy;
	}

	/**
	 * ソートキーを取得
	 * @param section
	 * @param ed
	 * @param sortKey
	 * @return
	 */
	private String getSortKey(MassReferenceSection section, EntityDefinition ed, String sortKey) {
		String ret = sortKey;
		if (StringUtil.isBlank(sortKey)) {
			return Entity.OID;
		}

		NestProperty property = getLayoutNestProperty(section, sortKey);
		// 当該項目がセクション上表示されない場合は、利用しない
		if (property == null) {
			return Entity.OID;
		}

		PropertyDefinition pd = getPropertyDefinition(ed, sortKey);
		if (pd == null) {
			ret = Entity.OID;
			pd = ed.getProperty(ret);
		}

		if (pd instanceof ReferenceProperty) {
			ret = sortKey + "." + getDisplayNestProperty(property);
		}

		return ret;
	}

	/**
	 * ソート種別を取得
	 * @param sortType
	 * @return
	 */
	private SortType getSortType(String sortType) {
		if (StringUtil.isBlank(sortType)) {
			return SortType.DESC;
		}
		return SortType.valueOf(sortType);
	}

	private NullOrderingSpec getNullOrderingSpec(NullOrderType type) {
		if (type == null) return null;
		switch (type) {
		case FIRST:
			return NullOrderingSpec.FIRST;
		case LAST:
			return NullOrderingSpec.LAST;
		default:
			break;
		}
		return null;
	}

	private int countEntity(final LoadEntityInterrupterHandler handler, final Query query,
			final ReferenceProperty rp, final MassReferenceSection section, final OutputType outputType) {

 		//検索前処理
		final SearchQueryContext sqContext = handler.beforeSearchMassReference(query.copy(), rp, section, outputType);

 		Integer count = null;
		if (sqContext.isDoPrivileged()) {
			//特権実行
			count = AuthContext.doPrivileged(() -> em.count(sqContext.getQuery()));
		} else {
			if (sqContext.getWithoutConditionReferenceName() != null) {
				count = EntityPermission.doQueryAs(sqContext.getWithoutConditionReferenceName(), () -> em.count(sqContext.getQuery()));
			} else {
				count = em.count(sqContext.getQuery());
			}
		}

 		return count;
	}

	private List<Entity> search(final LoadEntityInterrupterHandler handler, final Query query,
			final ReferenceProperty rp, final MassReferenceSection section, final OutputType outputType,
			final Set<String> userNameProperties, final List<String> userOids) {

 		//検索前処理
		final SearchQueryContext sqContext = handler.beforeSearchMassReference(query.copy(), rp, section, outputType);

 		List<Entity> result = null;
		if (sqContext.isDoPrivileged()) {
			//特権実行
			result = AuthContext.doPrivileged(() -> {
				return searchEntity(handler, sqContext.getQuery(), rp, section, outputType, userNameProperties, userOids);
			});
		} else {
			if (sqContext.getWithoutConditionReferenceName() != null) {
				result = EntityPermission.doQueryAs(sqContext.getWithoutConditionReferenceName(), () -> {
					return searchEntity(handler, sqContext.getQuery(), rp, section, outputType, userNameProperties, userOids);
				});
			} else {
				result = searchEntity(handler, sqContext.getQuery(), rp, section, outputType, userNameProperties, userOids);
			}
		}

 		return result;
	}

 	private List<Entity> searchEntity(final LoadEntityInterrupterHandler handler, final Query query,
			final ReferenceProperty rp, final MassReferenceSection section, 
 			final OutputType outputType, final Set<String> userNameProperties, final List<String> userOids) {

 		final List<Entity> result = new ArrayList<>();
		em.searchEntity(query, (entity) -> {

 			//検索後処理
			handler.afterSearchMassReference(query, rp, section, entity, outputType);

 			//User名が必要な値を取得
			for (String propertyName : userNameProperties) {
				String oid = entity.getValue(propertyName);
				if (oid != null && !userOids.contains(oid)) {
					userOids.add(oid);
				}
			}

 			result.add(entity);
			return true;
		});

 		return result;
	}

	/**
	 * 表示内容をHTMLデータとして取得
	 * @param request
	 * @param defName
	 * @param props
	 * @param result
	 * @param outputType
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getHtmlData(RequestContext request, String defName, String viewName,
			String propName, ReferenceProperty rp, List<NestProperty> props,
			List<Entity> result, OutputType outputType) throws IOException, ServletException {

		final EntityDefinition ed = edm.get(rp.getObjectDefinitionName());

		//EditorのProperty名には被参照プロパティ名を付加
		//参照ダイアログでの編集保存時にSectionからEditorを取得するため
		final String editorPrefix = propName + ".";

		List<Map<String, String>> ret = new ArrayList<>();
		for (final Entity entity : result) {
			final Map<String, String> eval = new LinkedHashMap<>();
			eval.put("orgOid", entity.getOid());
			eval.put("orgVersion", entity.getVersion().toString());
			for (NestProperty property : props) {
				final PropertyDefinition pd = ed.getProperty(property.getPropertyName());
				if (isDispProperty(pd, property, outputType)) {
					final PropertyEditor editor = property.getEditor();
					String path = EntityViewUtil.getJspPath(editor, ViewConst.DESIGN_TYPE_GEM);
					if (path != null) {
						final Object propValue = entity.getValue(property.getPropertyName());
						Func beforeFunc = new Func() {

							@Override
							public void execute(HttpServletRequest req, HttpServletResponse res) {
								//リクエストに必要なパラメータを詰める
								req.setAttribute(Constants.OUTPUT_TYPE, OutputType.SEARCHRESULT);
								req.setAttribute(Constants.EDITOR_EDITOR, editor);
								req.setAttribute(Constants.ENTITY_DATA, entity);
								req.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
								req.setAttribute(Constants.ENTITY_DEFINITION, ed);
								req.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
								req.setAttribute(Constants.EDITOR_REF_ENTITY_VALUE_MAP, eval);
								req.setAttribute(Constants.VIEW_NAME, viewName); //Reference型参照先リンク表示用
								req.setAttribute(Constants.ROOT_DEF_NAME, defName); //Reference型参照先リンク表示用
								req.setAttribute(Constants.VIEW_TYPE, Constants.VIEW_TYPE_DETAIL);
							}
						};
						Func afterFunc = new Func() {

							@Override
							public void execute(HttpServletRequest req, HttpServletResponse res) {
								//パラメータ削除
								req.removeAttribute(Constants.OUTPUT_TYPE);
								req.removeAttribute(Constants.EDITOR_EDITOR);
								req.removeAttribute(Constants.ENTITY_DATA);
								req.removeAttribute(Constants.EDITOR_PROP_VALUE);
								req.removeAttribute(Constants.ENTITY_DEFINITION);
								req.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
								req.removeAttribute(Constants.VIEW_NAME);
								req.removeAttribute(Constants.ROOT_DEF_NAME);
								req.removeAttribute(Constants.VIEW_TYPE);
							}
						};

						//HTML取得
						if (property.getEditor() instanceof RangePropertyEditor) {
							property.getEditor().setPropertyName(property.getPropertyName());
						}else {
							property.getEditor().setPropertyName(editorPrefix + property.getPropertyName());
						}
						String html = ResponseUtil.getIncludeJspContents(path, beforeFunc, afterFunc) .replace("\r\n", "").replace("\n", "").replace("\r", "");

						WebRequestStack stack = WebRequestStack.getCurrent();
						HttpServletRequest req = stack.getRequest();
						Boolean isNest = (Boolean) req.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY_PREFIX + editorPrefix + property.getPropertyName());
						if (isNest != null && isNest) {
							eval.put(property.getPropertyName() + ".name", html);
						} else {
							eval.put(property.getPropertyName(), html);
						}
					}
				}
			}

			//参照プロパティに対するNestプロパティに対してJSPで生成されたhtmlのKEYに
			//被参照のプロパティ名が付加されているので除去する
			Map<String, String> ajustData = eval.entrySet().stream().collect(Collectors.toMap(
					e -> {
						if (e.getKey().startsWith(editorPrefix)) {
							return e.getKey().substring(editorPrefix.length());
						} else {
							return e.getKey();
						}
					},
					Map.Entry::getValue));

			ret.add(ajustData);
		}

		request.setAttribute("htmlData", ret);
	}

	/**
	 * 表示対象のプロパティか
	 * @param pd
	 * @param property
	 * @param outputType
	 * @return
	 */
	private boolean isDispProperty(PropertyDefinition pd, NestProperty property, OutputType outputType) {
		if (property.getEditor() == null) return false;
		if (OutputType.EDIT == outputType) {
			if (property.isHideDetail()) return false;
		} else if (OutputType.VIEW == outputType) {
			if (property.isHideView()) return false;
		}
		return true;
	}

	/**
	 * フォーム内のMassReferenceSectionを取得します。
	 * @param view 画面定義
	 * @return MassReferenceSectionのList
	 */
	private List<MassReferenceSection> getMassReferenceSection(
			EntityDefinition ed, OutputType outputType, DetailFormView view, String viewName, Entity entity) {
		List<MassReferenceSection> sections = new ArrayList<>();
		for (Section section : view.getSections()) {
			if (section instanceof DefaultSection
					&& EntityViewUtil.isDisplayElement(ed.getName(), section.getElementRuntimeId(), outputType, entity)) {
				sections.addAll(getMassReferenceSection(ed, outputType, (DefaultSection) section, entity));
			} else if (section instanceof MassReferenceSection) {
				sections.add((MassReferenceSection) section);
			}
		}
		return sections;
	}

	/**
	 * セクション内のMassReferenceSectionを取得します。
	 * @param section セクション
	 * @return MassReferenceSectionのList
	 */
	private List<MassReferenceSection> getMassReferenceSection(
			EntityDefinition ed, OutputType outputType, DefaultSection section, Entity entity) {
		List<MassReferenceSection> sections = new ArrayList<>();
		for (Element elem : section.getElements()) {
			if (elem instanceof MassReferenceSection) {
				MassReferenceSection _section = (MassReferenceSection) elem;
				if (EntityViewUtil.isDisplayElement(ed.getName(), _section.getElementRuntimeId(), outputType, entity)) {
					sections.add(_section);
				}
			} else if (elem instanceof DefaultSection) {
				sections.addAll(getMassReferenceSection(ed, outputType, (DefaultSection) elem, entity));
			}
		}
		return sections;
	}

	private void addReferenceProperty(List<String> select, List<DisplayInfo> dispInfo, ReferenceProperty rp, NestProperty np, String parent, OutputType outputType) {
		boolean hasNest = false;
		List<NestProperty> nest = null;
		String dispLabelItem = null;
		if (np.getEditor() instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor) np.getEditor();
			nest = rpe.getNestProperties();
			hasNest = !nest.isEmpty();
			dispLabelItem = rpe.getDisplayLabelItem();
		}

		String name = null;
		if (StringUtil.isNotBlank(parent)) {
			name = parent + "." + np.getPropertyName();
		} else {
			name = np.getPropertyName();
		}

		if (!select.contains(name + "." + Entity.NAME)) {
			select.add(name + "." + Entity.NAME);
		}
		if (!select.contains(name + "." + Entity.OID)) {
			select.add(name + "." + Entity.OID);
		}
		if (!select.contains(name + "." + Entity.VERSION)) {
			select.add(name + "." + Entity.VERSION);
		}

		// 表示ラベルとして扱うプロパティ
		if (dispLabelItem != null) {
			if (!select.contains(name + "." + dispLabelItem)) {
				select.add(name + "." + dispLabelItem);
			}
		}

		if (hasNest) {
			EntityDefinition red = getReferenceEntityDefinition(rp);

			for (NestProperty _np : nest) {
				//表示設定確認
				if (OutputType.EDIT == outputType) {
					if (_np.isHideDetail()) continue;
				} else if (OutputType.VIEW == outputType) {
					if (_np.isHideView()) continue;
				}

				PropertyDefinition rpd = red.getProperty(_np.getPropertyName());
				if (rpd instanceof ReferenceProperty) {
					addReferenceProperty(select, dispInfo, (ReferenceProperty) rpd, _np, name, outputType);
				} else {
					if (!select.contains(name + "." + _np.getPropertyName())) {
						select.add(name + "." + _np.getPropertyName());
						if (_np.getEditor() instanceof RangePropertyEditor) {
							RangePropertyEditor rpe = (RangePropertyEditor) _np.getEditor();
							select.add(name + "." + rpe.getToPropertyName());
						}
					}

					DisplayInfo di = new DisplayInfo();

					di.setName(name + "." + _np.getPropertyName());
					if (StringUtil.isNotBlank(_np.getDisplayLabel())) {
						di.setDisplayName(TemplateUtil.getMultilingualString(_np.getDisplayLabel(), _np.getLocalizedDisplayLabelList()));
					} else {
						di.setDisplayName(TemplateUtil.getMultilingualString(rpd.getDisplayName(), rpd.getLocalizedDisplayNameList()));
					}
					di.setWidth(_np.getWidth());

					if (_np.getEditor() != null && _np.getEditor().isHide()) {
						di.setHide(true);
					} else {
						di.setHide(false);
					}

					dispInfo.add(di);
				}
			}

		} else {
			DisplayInfo di = new DisplayInfo();
			di.setName(name);
			if (StringUtil.isNotBlank(np.getDisplayLabel())) {
				di.setDisplayName(TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList()));
			} else {
				di.setDisplayName(TemplateUtil.getMultilingualString(rp.getDisplayName(), rp.getLocalizedDisplayNameList()));
			}
			di.setWidth(np.getWidth());
			if (np.getEditor() != null && np.getEditor().isHide()) {
				di.setHide(true);
			} else {
				di.setHide(false);
			}
			dispInfo.add(di);
		}
	}

	private EntityDefinition getReferenceEntityDefinition(ReferenceProperty pd) {
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		return edm.get(pd.getObjectDefinitionName());
	}

	private Set<String> getUseUserPropertyEditorPropertyName(List<NestProperty> nestProperties, boolean isDetail) {
		Set<String> ret = new HashSet<>();
		for (NestProperty property : nestProperties) {
			if ((isDetail && property.isHideDetail()) || (!isDetail && property.isHideView())) continue;

			String propertyName = property.getPropertyName();

			if (property.getEditor() instanceof ReferencePropertyEditor) {
				// ネストの項目を確認
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				if (!editor.getNestProperties().isEmpty()) {
					Set<String> nest = getUseUserPropertyEditorPropertyName(editor.getNestProperties(), isDetail);
					for (String nestPropertyName : nest) {
						String _nestPropertyName = propertyName + "." + nestPropertyName;
						ret.add(_nestPropertyName);
					}
				}
			} else if (property.getEditor() instanceof UserPropertyEditor) {
				ret.add(propertyName);
			}
		}
		return ret;
	}

	private void setUserInfoMap(DetailCommandContext context, final List<String> userOidList) {
		//UserEntityを検索してリクエストに格納
		final Map<String, Entity> userMap = new HashMap<>();

		Query q = new Query().select(Entity.OID, Entity.NAME)
							 .from(User.DEFINITION_NAME)
							 .where(new In(Entity.OID, userOidList.toArray()));

		if (context.getView().isShowUserNameWithPrivilegedValue()) {
			AuthContext.doPrivileged(() -> {
				em.searchEntity(q, new Predicate<Entity>() {

					@Override
					public boolean test(Entity dataModel) {
						if (!userMap.containsKey(dataModel.getOid())) {
							userMap.put(dataModel.getOid(), dataModel);
						}
						return true;
					}
				});
			});
		} else {
			em.searchEntity(q, new Predicate<Entity>() {

				@Override
				public boolean test(Entity dataModel) {
					if (!userMap.containsKey(dataModel.getOid())) {
						userMap.put(dataModel.getOid(), dataModel);
					}
					return true;
				}
			});
		}

		context.setAttribute(Constants.USER_INFO_MAP, userMap);
	}

	private NestProperty getLayoutNestProperty(MassReferenceSection section, String propName) {
		// 直下に指定されているかチェック
		Optional<NestProperty> property = section.getProperties().stream()
				.filter(e -> propName.equals(e.getPropertyName())).findFirst();
		if (property.isPresent()) {
			return property.get();
		}

		// プロパティ名で一致する列がない場合、参照の各階層をチェック
		int dotIndex = propName.indexOf(".");
		if (dotIndex > -1) {
			String topPropName = propName.substring(0, dotIndex);
			String subPropName = propName.substring(dotIndex + 1);

			// セクション直下を取得
			Optional<NestProperty> opt = section.getProperties().stream()
					.filter(np -> np.getPropertyName().equals(topPropName)).findFirst();
			if (!opt.isPresent()) return null;

			// 参照の先の項目を取得
			NestProperty subProp = opt.get();
			if (subProp.getEditor() instanceof ReferencePropertyEditor) {
				return findLayoutNestPropertyRecursive(subPropName, ((ReferencePropertyEditor) subProp.getEditor()).getNestProperties());
			}
		}
		
		return null;
	}

	/**
	 * プロパティ名に一致するネストプロパティを再帰的に検索し取得する
	 * @param propertyName プロパティ名
	 * @param editor 参照プロパティエディタ
	 * @return ネストプロパティ
	 */
	private NestProperty findLayoutNestPropertyRecursive(String propName, List<NestProperty> properties) {
		if (properties == null || properties.isEmpty()) {
			return null;
		}

		int dotIndex = propName.indexOf(".");
		if (dotIndex > -1) {
			// 子階層を再帰呼び出し
			String topPropName = propName.substring(0, dotIndex);
			String subPropName = propName.substring(dotIndex + 1);

			Optional<NestProperty> opt = properties.stream()
					.filter(np -> np.getPropertyName().equals(topPropName)).findFirst();
			if (!opt.isPresent()) return null;

			NestProperty subProp = opt.get();
			if (subProp.getEditor() instanceof ReferencePropertyEditor) {
				return findLayoutNestPropertyRecursive(subPropName, ((ReferencePropertyEditor) subProp.getEditor()).getNestProperties());
			}
		}

		// 一致するNestPropetyを取得
		Optional<NestProperty> opt = properties.stream().filter(np -> np.getPropertyName().equals(propName)).findFirst();
		return opt.orElse(null);
	}

	/**
	 * 参照プロパティで、セクションに表示されている項目を取得します。
	 * @return 表示項目
	 */
	private String getDisplayNestProperty(NestProperty refProp) {
		PropertyEditor editor = refProp.getEditor();
		
		if (editor instanceof ReferencePropertyEditor
				&& StringUtil.isNotEmpty(((ReferencePropertyEditor) editor).getDisplayLabelItem())) {
			return ((ReferencePropertyEditor)editor).getDisplayLabelItem();
		} else {
			return Entity.NAME;
		}
		
	}

	private PropertyDefinition getPropertyDefinition(EntityDefinition definition, String propName) {
		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);
			PropertyDefinition topProperty = definition.getProperty(topPropName);
			if (topProperty instanceof ReferenceProperty) {
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) topProperty);
				if (red != null) {
					PropertyDefinition pd = getPropertyDefinition(red, subPropName);
					return pd;
				}
			}
		} else {
			return definition.getProperty(propName);
		}
		return null;
	}

	/**
	 * 参照データの表示情報
	 * @author lis3wg
	 */
	public static class DisplayInfo {
		private String name;
		private String displayName;
		private int width;
		private boolean hide;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public boolean isHide() {
			return hide;
		}

		public void setHide(boolean hide) {
			this.hide = hide;
		}
	}
}
