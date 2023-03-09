/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.ViewUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.view.generic.common.MetaAutocompletionSetting.AutocompletionSettingRuntime;
import org.iplass.mtp.impl.view.generic.element.ElementRuntime;
import org.iplass.mtp.impl.view.generic.element.MetaButton.ButtonRuntime;
import org.iplass.mtp.impl.view.generic.element.section.MetaMassReferenceSection.MassReferenceSectionRuntime;
import org.iplass.mtp.impl.view.generic.element.section.MetaSearchConditionSection.SearchConditionSectionRuntime;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.template.MetaGroovyTemplate;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.common.AutocompletionHandleException;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimeFormatSetting;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.UrlParameterActionType;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 画面定義を管理するクラス。
 * @author lis3wg
 */
public class EntityViewManagerImpl extends AbstractTypedDefinitionManager<EntityView> implements EntityViewManager {

	private static Logger logger = LoggerFactory.getLogger(EntityViewManagerImpl.class);

	private static final String REQUEST_BINDING_NAME = "request";
	private static final String SESSION_BINDING_NAME = "session";

	/** 画面定義を扱うサービス */
	private EntityViewService service;
	private EntityDefinitionManager edm;

	/**
	 * コンストラクタ
	 */
	public EntityViewManagerImpl() {
		service = ServiceRegistry.getRegistry().getService(EntityViewService.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	@Deprecated
	public DefinitionModifyResult create(String name, EntityView entityView) {
		return create(entityView);
	}

	@Override
	@Deprecated
	public DefinitionModifyResult update(String name, EntityView entityView) {
		return update(entityView);
	}

	@Override
	public PropertyEditor getPropertyEditor(String defName, String viewType, String viewName, String propName, Entity entity) {

		EntityView ev = get(defName);
		if (ev == null) {
			return null;
		}

		PropertyEditor editor = null;
		if ("detail".equals(viewType)) {
			DetailFormView form = null;
			if (viewName == null || viewName.isEmpty()) {
				form = ev.getDefaultDetailFormView();
			} else {
				form = ev.getDetailFormView(viewName);
			}
			editor = getDetailFormViewEditor(defName, propName, form, entity);
		} else if ("search".equals(viewType)) {
			SearchFormView form = null;
			if (viewName == null || viewName.isEmpty()) {
				form = ev.getDefaultSearchFormView();
			} else {
				form = ev.getSearchFormView(viewName);
			}
			editor = getSearchFormViewEditor(defName, propName, form);
		} else if ("searchResult".equals(viewType)) {
			SearchFormView form = null;
			if (viewName == null || viewName.isEmpty()) {
				form = ev.getDefaultSearchFormView();
			} else {
				form = ev.getSearchFormView(viewName);
			}
			editor = getSearchResultEditor(defName, propName, form);
		} else if ("bulk".equals(viewType)) {
			SearchFormView form = null;
			if (viewName == null || viewName.isEmpty()) {
				form = ev.getDefaultSearchFormView();
			} else {
				form = ev.getSearchFormView(viewName);
			}
			editor = getBulkUpdateEditor(defName, propName, form);
		} else if ("multiBulk".equals(viewType)) {
			BulkFormView form = null;
			if (viewName == null || viewName.isEmpty()) {
				form = ev.getDefaultBulkFormView();
			} else {
				form = ev.getBulkFormView(viewName);
			}
			editor = getBulkFormViewEditor(defName, propName, form);
		}

		return editor;
	}

	private PropertyEditor getDetailFormViewEditor(String defName, String propName, DetailFormView form, Entity entity) {
		String currentPropName = null;
		String subPropName = null;
		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}

		//FIXME outputtypeを判定
		OutputType outputType =  OutputType.EDIT;
		for (Section section : form.getSections()) {
			if (!isDisplayElement(defName, section.getElementRuntimeId(), outputType, entity)) {
				continue;
			}
			if (section instanceof DefaultSection) {
				PropertyEditor editor = getEditor(defName, outputType, (DefaultSection)section, currentPropName, subPropName, entity);
				if (editor != null) {
					return editor;
				}
			} else if (section instanceof MassReferenceSection) {
				PropertyEditor editor = getEditor(defName, outputType, (MassReferenceSection) section, currentPropName, subPropName, entity);
				if (editor != null) {
					return editor;
				}
			}
		}
		return null;
	}

	private PropertyEditor getEditor(String defName, OutputType outputType, DefaultSection section, final String currentPropName, final String subPropName, final Entity entity) {
		for (Element element : section.getElements()) {
			if (!isDisplayElement(defName, element.getElementRuntimeId(), outputType, entity)) {
				continue;
			}
			if (element instanceof PropertyItem) {
				PropertyItem property = (PropertyItem) element;
				if (property.getPropertyName().equals(currentPropName)) {
					//FIXME なぜセットが必要？
//					if (property.getEditor() instanceof ReferencePropertyEditor) {
//						property.getEditor().setPropertyName(property.getPropertyName());
//					}
					if (subPropName == null) {
						property.getEditor().setPropertyName(property.getPropertyName());	//念のためセット
						return property.getEditor();
					} else {
						return getEditor(subPropName, property.getEditor());
					}
				}
				//JoinPropertyEditorからネスとされたPropertyEditorを探します。
				if (property.getEditor() instanceof JoinPropertyEditor) {
					PropertyEditor editor = getEditor(currentPropName, property.getEditor());
					if (editor == null) continue;
					if (subPropName == null) {
						return editor;
					} else {
						return getEditor(subPropName, editor);
					}
				}
			} else if (element instanceof VirtualPropertyItem) {
				VirtualPropertyItem property = (VirtualPropertyItem) element;
				if (property.getPropertyName().equals(currentPropName)) {
					if (subPropName == null) {
						property.getEditor().setPropertyName(property.getPropertyName());
						return property.getEditor();
					} else {
						return getEditor(subPropName, property.getEditor());
					}
				}
			} else if (element instanceof DefaultSection) {
				PropertyEditor nest = getEditor(defName, outputType, (DefaultSection)element, currentPropName, subPropName, entity);
				if (nest != null) {
					return nest;
				}
			}
		}
		return null;
	}

	private PropertyEditor getEditor(String defName, OutputType outputType, MassReferenceSection section, final String currentPropName, final String subPropName, final Entity entity) {
		if (subPropName == null) return null;

		if (!isDisplayElement(defName, section.getElementRuntimeId(), outputType, entity)) {
			return null;
		}

		if (section.getPropertyName().equals(currentPropName)) {
			for (NestProperty np : section.getProperties()) {
				if (subPropName.indexOf(".") > -1) {
					PropertyEditor editor = getEditor(subPropName, np.getEditor());
					if (editor != null) {
						return editor;
					}
				} else {
					if (np.getPropertyName().equals(subPropName)) {
						return np.getEditor();
					}
				}
			}
		}
		return null;
	}

	private PropertyEditor getSearchFormViewEditor(String defName, String propName, SearchFormView form) {
		String currentPropName = null;
		String subPropName = null;

		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			// 子階層Entityのプロパティ
			SearchConditionSection section = form.getCondSection();
			for (Element element : section.getElements()) {
				if (!(element instanceof PropertyItem)) continue;

				if (!isDisplayElement(defName, element.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
					continue;
				}

				PropertyItem property = (PropertyItem) element;
				if (!property.isBlank() && property.getPropertyName().equals(propName)) {
					return property.getEditor();
				}
			}

			//ネストテーブルのネストプロパティ
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}

		SearchConditionSection section = form.getCondSection();
		for (Element element : section.getElements()) {
			if (!(element instanceof PropertyItem)) continue;

			if (!isDisplayElement(defName, element.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)) {
				continue;
			}

			PropertyItem property = (PropertyItem) element;
			if (!property.isBlank() && property.getPropertyName().equals(currentPropName)) {
				//FIXME なぜセットが必要？
//				if (property.getEditor() instanceof ReferencePropertyEditor) {
//					property.getEditor().setPropertyName(property.getPropertyName());
//				}
				if (subPropName == null) {
					property.getEditor().setPropertyName(property.getPropertyName());	//念のためセット
					return property.getEditor();
				} else {
					return getEditor(subPropName, property.getEditor());
				}
			}
		}
		return null;
	}

	private PropertyEditor getEditor(String propName, PropertyEditor editor) {
		List<NestProperty> nestProperties = null;
		// nest構造のPropertyEditorを取得する
		if (editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor refEditor = (ReferencePropertyEditor) editor;
			nestProperties = refEditor.getNestProperties();
		} else if (editor instanceof JoinPropertyEditor) {
			JoinPropertyEditor joinEditor = (JoinPropertyEditor) editor;
			nestProperties = joinEditor.getProperties();
		} else {
			return null;
		}

		if (nestProperties == null || nestProperties.isEmpty()) {
			return null;
		}

		String currentPropName = null;
		String subPropName = null;
		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}

		for (NestProperty property : nestProperties) {
			if (property.getPropertyName().equals(currentPropName)) {
				if (subPropName == null) {
					property.getEditor().setPropertyName(property.getPropertyName());	//念のためセット
					return property.getEditor();
				} else {
					return getEditor(subPropName, property.getEditor());
				}
			}
			// JoinPropertyEditorからネスとされたPropertyEditorを探します。
			if (property.getEditor() instanceof JoinPropertyEditor) {
				PropertyEditor nestEditor = getEditor(currentPropName, property.getEditor());
				if (nestEditor == null) continue;
				if (subPropName == null) {
					return nestEditor;
				} else {
					return getEditor(subPropName, nestEditor);
				}
			}
		}
		return null;
	}

	private PropertyEditor getSearchResultEditor(String defName, String propName, SearchFormView form) {
		String currentPropName = null;
		String subPropName = null;
		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			// 子階層Entityのプロパティ
			SearchResultSection section = form.getResultSection();
			for (Element element : section.getElements()) {
				if (!(element instanceof PropertyColumn)) continue;
				PropertyColumn property = (PropertyColumn) element;
				if (property.getPropertyName().equals(propName)
						&& EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
					return property.getEditor();
				}
			}

			//ネストテーブルのネストプロパティ
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}

		SearchResultSection section = form.getResultSection();
		for (Element element : section.getElements()) {
			if (!(element instanceof PropertyColumn)) continue;
			PropertyColumn property = (PropertyColumn) element;
			if (property.getPropertyName().equals(currentPropName)
					&& EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
				//FIXME なぜセットが必要？
//				if (property.getEditor() instanceof ReferencePropertyEditor) {
//					property.getEditor().setPropertyName(property.getPropertyName());
//				}
				if (subPropName == null) {
					property.getEditor().setPropertyName(property.getPropertyName());	//念のためセット
					return property.getEditor();
				} else {
					return getEditor(subPropName, property.getEditor());
				}
			}
		}
		return null;
	}

	private PropertyEditor getBulkUpdateEditor(String defName, String propName, SearchFormView form) {
		String currentPropName = null;
		String subPropName = null;
		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}

		SearchResultSection section = form.getResultSection();
		for (Element element : section.getElements()) {
			if (!(element instanceof PropertyColumn)) continue;

			if (!isDisplayElement(defName, element.getElementRuntimeId(), OutputType.BULK, null)) {
				continue;
			}

			PropertyColumn property = (PropertyColumn) element;
			if (property.getBulkUpdateEditor() == null) continue;
			if (property.getPropertyName().equals(currentPropName)) {
				if (subPropName == null) {
					property.getBulkUpdateEditor().setPropertyName(property.getPropertyName());
					return property.getBulkUpdateEditor();
				} else {
					return getEditor(subPropName, property.getBulkUpdateEditor());
				}
			}
			//JoinPropertyEditorからネストされたPropertyEditorを探します。
			if (property.getBulkUpdateEditor() instanceof JoinPropertyEditor) {
				PropertyEditor editor = getEditor(currentPropName, property.getBulkUpdateEditor());
				if (editor == null) continue;
				if (subPropName == null) {
					return editor;
				} else {
					return getEditor(subPropName, editor);
				}
			}
		}
		return null;
	}

	private PropertyEditor getBulkFormViewEditor(String defName, String propName, BulkFormView form) {
		String currentPropName = null;
		String subPropName = null;
		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}

		for (Section section : form.getSections()) {
			if (!isDisplayElement(defName, section.getElementRuntimeId(), OutputType.BULK, null)) {
				continue;
			}
			if (section instanceof DefaultSection) {
				PropertyEditor editor = getEditor(defName, OutputType.BULK, (DefaultSection)section, currentPropName, subPropName, null);
				if (editor != null) {
					return editor;
				}
			}
		}
		return null;
	}

	@Override
	public PropertyEditor getPropertyEditor(String defName, String viewType, String viewName, String propName, Integer refSectionIndex, Entity entity) {
		PropertyEditor editor = null;
		if (refSectionIndex == null) {
			editor = getPropertyEditor(defName, viewType, viewName, propName, entity);
		} else {
			editor = getPropertyEditor(defName, viewName, propName, refSectionIndex);
		}

		return editor;
	}

	@Override
	public void executeTemplate(String name, String templateName, HttpServletRequest req,
			HttpServletResponse res, ServletContext application, PageContext page) {
		if (name == null || templateName == null) return;

		EntityViewRuntime entityView = service.getRuntimeByName(name);
		if (entityView == null) return;

		//DynamicScriptをapiからimplに移すと、戻り値がapiからimplの逆参照となってしまう。
		//→戻り値ではなくDynamicScriptをメソッド内で実行する。
		//　→api(jsp)からGroovyTemplateを実行するためのDynamicScriptだったが、
		//　　impl内で完結するならそのままGroovyTemplateで十分。
		GroovyTemplate template = entityView.getTemplate(templateName);
		if (template != null) {
			try {
				template.doTemplate(new MetaGroovyTemplate.WebGroovyTemplateBinding(WebUtil.getRequestContext(), req, res, application, page));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Entity copyEntity(String viewName, Entity entity) {
		EntityViewRuntime entityView = service.getRuntimeByName(entity.getDefinitionName());
		if (entityView == null) {
			throw new ApplicationException(resourceString("impl.view.generic.EntityViewManagerImpl.viewErr"));
		}

		DetailFormViewRuntime detailView = null;
		for (FormViewRuntime formView : entityView.getFormViews()) {
			if (formView instanceof DetailFormViewRuntime) {
				//nameが一致するhandlerを検索
				if (viewName == null || viewName.isEmpty()) {
					if (formView.getMetaData().getName() == null || formView.getMetaData().getName().isEmpty()) {
						detailView = (DetailFormViewRuntime) formView;
						break;
					}
				} else {
					if (viewName.equals(formView.getMetaData().getName())) {
						detailView = (DetailFormViewRuntime) formView;
						break;
					}
				}
			}
		}

		if (detailView != null) {
			return detailView.copyEntity(entity);
		}
		return null;
	}

	@Override
	public Entity initEntity(String definitionName, String viewName, Entity entity) {
		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) {
			throw new ApplicationException(resourceString("impl.view.generic.EntityViewManagerImpl.viewErr"));
		}

		DetailFormViewRuntime detailView = null;
		for (FormViewRuntime formView : entityView.getFormViews()) {
			if (formView instanceof DetailFormViewRuntime) {
				//nameが一致するhandlerを検索
				if (viewName == null || viewName.isEmpty()) {
					if (formView.getMetaData().getName() == null || formView.getMetaData().getName().isEmpty()) {
						detailView = (DetailFormViewRuntime) formView;
						break;
					}
				} else {
					if (viewName.equals(formView.getMetaData().getName())) {
						detailView = (DetailFormViewRuntime) formView;
						break;
					}
				}
			}
		}

		if (detailView != null) {
			return detailView.initEntity(entity, definitionName);
		}
		return null;
	}

	@Override
	public Map<String, Object> applyDefaultPropertyCondition(String definitionName, String viewName, Map<String, Object> defaultCondMap) {
		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) {
			//デフォルトなど
			return defaultCondMap;
		}

		SearchFormViewRuntime searchView = null;
		for (FormViewRuntime formView : entityView.getFormViews()) {
			if (formView instanceof SearchFormViewRuntime) {
				//nameが一致するhandlerを検索
				if (viewName == null || viewName.isEmpty()) {
					if (formView.getMetaData().getName() == null || formView.getMetaData().getName().isEmpty()) {
						searchView = (SearchFormViewRuntime) formView;
						break;
					}
				} else {
					if (viewName.equals(formView.getMetaData().getName())) {
						searchView = (SearchFormViewRuntime) formView;
						break;
					}
				}
			}
		}

		if (searchView != null) {
			return searchView.applyDefaultPropertyCondition(defaultCondMap);
		}
		return defaultCondMap;
	}

	@Override
	public String getCsvDownloadFileName(String definitionName, String viewName, String defaultName, Map<String, Object> csvVariableMap) {
		String fileName = defaultName;
		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView != null) {
			SearchFormViewRuntime searchView = null;
			for (FormViewRuntime formView : entityView.getFormViews()) {
				if (formView instanceof SearchFormViewRuntime) {
					//nameが一致するhandlerを検索
					if (StringUtil.isEmpty(viewName)) {
						if (StringUtil.isEmpty(formView.getMetaData().getName())) {
							searchView = (SearchFormViewRuntime) formView;
							break;
						}
					} else {
						if (viewName.equals(formView.getMetaData().getName())) {
							searchView = (SearchFormViewRuntime) formView;
							break;
						}
					}
				}
			}

			if (searchView != null) {
				fileName = searchView.getCsvDownloadFileName(defaultName, csvVariableMap).replace("/", "_").replace(" ", "_");
			}
		} else {
			//View未定義はdefaultName
		}
		return fileName.replace("/", "_").replace(" ", "_");
	}

	@Override
	public Condition getSearchConditionSectionDefaultCondition(String name, SearchConditionSection section) {
		return getEntityViewCondition(name, section, SearchConditionSectionRuntime.DEFAULT_CONDITION_PREFIX);
	}

	@Override
	public Condition getMassReferenceSectionCondition(String name, MassReferenceSection section) {
		return getEntityViewCondition(name, section, MassReferenceSectionRuntime.FILTER_CONDITION_PREFIX);
	}

	/**
	 * EntityViewに格納されたQueryを取得します。
	 *
	 * @param name Entity定義名
	 * @param element 検索対象のエレメント
	 * @param prefixKey 格納KEYの接頭辞
	 * @return 条件
	 *
	 */
	private Condition getEntityViewCondition(String name, Element element, String prefixKey) {
		if (name == null || element == null) return null;

		EntityViewRuntime entityView = service.getRuntimeByName(name);
		if (entityView == null) return null;

		PreparedQuery query = entityView.getQuery(prefixKey + element.getElementRuntimeId());
		if (query == null) return null;

		Map<String, Object> bindings = new HashMap<>();
		bindings.put(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
		bindings.put(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
		return query.condition(bindings);
	}

	@Override
	public SearchFormView createDefaultSearchFormView(String definitionName) {
		// AdminConsoleの標準ロード時は各種表示名を空で作成する為falseを指定
		return FormViewUtil.createDefaultSearchFormView(edm.get(definitionName), false);
	}

	@Override
	public DetailFormView createDefaultDetailFormView(String definitionName) {
		// AdminConsoleの標準ロード時は各種表示名を空で作成する為falseを指定
		return FormViewUtil.createDefaultDetailFormView(edm.get(definitionName), false);
	}

	@Override
	public BulkFormView createDefaultBulkFormView(String definitionName) {
		// AdminConsoleの標準ロード時は各種表示名を空で作成する為falseを指定
		return FormViewUtil.createDefaultBulkFormView(edm.get(definitionName), false);
	}

	@Override
	public String getCustomStyle(String definitionName, String scriptKey, String editorScriptKey, Entity entity, Object propValue) {
		if (definitionName == null || scriptKey == null || editorScriptKey == null) {
			//GroovyTemplateをキャッシュしているKEYが未指定の場合は取得しない
			return "";
		}
		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) {
			//取れなかったら呼出元でデフォルトレイアウト生成
			return "";
		}

		Map<String, GroovyTemplate> sectionScriptMap = entityView.getCustomStyleScriptMap(scriptKey);
		GroovyTemplate script = sectionScriptMap.get(editorScriptKey);
		if (script == null) {
			//スクリプトが未指定の場合はそのまま
			return "";
		}

		Map<String, Object> bindings = new HashMap<>();
		bindings.put("today", DateUtil.getCurrentTimestamp());
		bindings.put("entity", entity);
		bindings.put("value", propValue);

		StringWriter sw = new StringWriter();
		try {
			script.doTemplate(new GroovyTemplateBinding(sw, bindings));
		} catch (IOException e) {
			//発生しえないが、、
			throw new RuntimeException(e);
		}
		String style = sw.toString();
		if (StringUtil.isNotEmpty(style)) {
			//先頭、末尾の空白、改行、タブを削除
			style = StringUtil.removeLineFeedCode(StringUtil.stripToEmpty(style)).replaceAll("\t", "");
		}

		return style;
	}

	@Override
	public boolean isDisplayElement(String definitionName, String elementRuntimeId,
			OutputType outputType, Entity entity) {

		//Defaultで生成された場合にelementRuntimeIdが未指定なのでtrueとする
		if (definitionName == null || elementRuntimeId == null || outputType == null) {
			return true;
		}

		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) {
			return false;
		}

		ElementRuntime element = entityView.getElement(elementRuntimeId);
		if (element == null) {
			return false;
		}

		return element.isDisplay(outputType, entity);
	}

	@Override
	public boolean isDisplayButton(String definitionName, String buttonKey, OutputType outputType, Entity entity) {
		if (definitionName == null || buttonKey == null || outputType == null) {
			return false;
		}

		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) {
			return false;
		}

		ButtonRuntime button = entityView.getButton(buttonKey);
		if (button == null) {
			return false;
		}

		return button.isDisplayButton(outputType, entity);
	}

	@Override
	public String getUrlParameter(String definitionName, ReferencePropertyEditor editor, Entity entity, UrlParameterActionType actionType) {
		if (definitionName == null || editor == null || editor.getUrlParameterScriptKey() == null || actionType == null) return "";

		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) return "";

		//ActionTypeの検証
		List<UrlParameterActionType> actions = editor.getUrlParameterAction();
		if (actions == null) {
			//未指定の場合は、SELECTとADDはOK
			if (actionType != UrlParameterActionType.SELECT && actionType != UrlParameterActionType.ADD) {
				return "";
			}
		} else {
			//指定されている場合は、対象かどうかをチェック
			if (!actions.contains(actionType)) {
				return "";
			}
		}

		GroovyTemplate template = entityView.getTemplate(editor.getUrlParameterScriptKey());
		StringWriter sw = new StringWriter();
		if (template != null) {
			try {
				template.doTemplate(new UrlParameterGroovyTemplateBinding(sw, entity, actionType));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		String urlParam = sw.toString();
		if (StringUtil.isNotEmpty(urlParam)) {
			//先頭、末尾の空白、改行、タブを削除
			urlParam = StringUtil.removeLineFeedCode(StringUtil.stripToEmpty(urlParam)).replaceAll("\t", "");
		}

		return urlParam;
	}

	private class UrlParameterGroovyTemplateBinding extends GroovyTemplateBinding {

		public UrlParameterGroovyTemplateBinding(Writer writer, Entity entity, UrlParameterActionType actionType) {
			super(writer);

			RequestContext request = WebUtil.getRequestContext();
			setVariable("request", request);
			setVariable("session", request.getSession());
			setVariable("parent", entity);
			setVariable("actionType", actionType);
		}
	}

	@Override
	public Object getAutocompletionValue(String definitionName, String viewName, String viewType, String propName, String autocompletionKey, Integer referenceSectionIndex, Map<String, String[]> param, List<String> currentValue, Entity entity) {
		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);
		if (entityView == null) return null;

		AutocompletionSettingRuntime autocompletionSetting = entityView.getAutocompletionSetting(autocompletionKey);
		if (autocompletionSetting == null) return null;

		PropertyEditor editor = null;
		if (referenceSectionIndex != null) {
			editor = getPropertyEditor(definitionName, viewName, propName, referenceSectionIndex);
		} else {
			editor = getPropertyEditor(definitionName, viewType, viewName, propName, entity);
		}
		
		PropertyDefinition pd = null;
		//連動先の多重度が複数の場合、Listで格納
		//連動先の多重度が単数の場合、値をそのまま格納
		try {
			pd = EntityViewUtil.getPropertyDefinition(propName, edm.get(definitionName));
		} catch (IllegalArgumentException e) {
			// 仮想プロパティでentityからPropertyDefinitionが取得できない場合
		}
		Object currValue = (pd == null) || (pd.getMultiplicity() == 1) ? (currentValue.size() > 0 ? currentValue.get(0) : "") : currentValue;
		boolean isReference = editor instanceof ReferencePropertyEditor;
		Object value = autocompletionSetting.handle(param, currValue, isReference);

		Object returnValue = null;
		if (isReference) {
			// 参照型の場合はEntityで返す
			if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				for (Object obj : list) {
					if (obj != null && !(obj instanceof Entity)) {
						logger.error("For return type, please set Entity class as return value. The result type of this execution is of type " + obj.getClass().getName() + ".");
						throw new AutocompletionHandleException();
					}
				}
			} else if (value != null && !(value instanceof Entity)) {
				logger.error("For return type, please set Entity class as return value. The result type of this execution is of type " + value.getClass().getName() + ".");
				throw new AutocompletionHandleException();
			}
			returnValue = value;
		} else {

			DateTimeFormatSetting formatInfo = null;
			Format format = null;

			if (editor instanceof DateTimePropertyEditor && editor.getDisplayType() == DateTimeDisplayType.LABEL) {
				formatInfo = ViewUtil.getFormatInfo(((DateTimePropertyEditor) editor).getLocalizedDatetimeFormatList(),
						((DateTimePropertyEditor) editor).getDatetimeFormat());
			}

			if (editor instanceof NumberPropertyEditor && editor.getDisplayType() == NumberDisplayType.LABEL) {
				format = getDisplayNumberFormat((NumberPropertyEditor) editor);
			} else if (editor instanceof TimePropertyEditor && editor.getDisplayType() == DateTimeDisplayType.LABEL) {
				format = getDisplayTimeFormat(((TimePropertyEditor) editor).getDispRange(), formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale());
			} else if (editor instanceof TimestampPropertyEditor && editor.getDisplayType() == DateTimeDisplayType.LABEL) {
				format = getDisplayTimestampFormat(((TimestampPropertyEditor) editor).getDispRange(), formatInfo.getDatetimeFormat(),
						formatInfo.getDatetimeLocale(), ((TimestampPropertyEditor) editor).isShowWeekday());
			} else if (editor instanceof DatePropertyEditor && editor.getDisplayType() == DateTimeDisplayType.LABEL) {
				format = getDisplayDateFormat(formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), ((DatePropertyEditor) editor).isShowWeekday());
			}
			
			if (value instanceof Entity) {
				// スクリプト、EQLの結果がEntityの場合はプロパティの値だけ返す
				returnValue = ((Entity) value).getValue(propName);
			} else if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				List<Object> retList = new ArrayList<>();
				for (Object obj : list) {
					retList.add(makeConvertAutocompletionValue(obj, editor, format));
				}
				returnValue = retList;
			} else {
				returnValue = makeConvertAutocompletionValue(value, editor, format);
			}
		}

		return returnValue;
	}

	private Object makeConvertAutocompletionValue(Object value, PropertyEditor editor, Format format) {

		Object convertAutocompletionValue = convertAutocompletionValue(value);

		// フォーマット対象のプロパティでなくラベルでない場合はconvertした値を返却
		if (!(editor instanceof DateTimePropertyEditor && editor.getDisplayType() == DateTimeDisplayType.LABEL)
				&& !(editor instanceof NumberPropertyEditor && editor.getDisplayType() == NumberDisplayType.LABEL)) {
			return convertAutocompletionValue;
		}

		if (convertAutocompletionValue == null || convertAutocompletionValue.toString().isEmpty()) {
			return convertAutocompletionValue;
		}

		// PropertyEditorにあわせて値の型変換
		if (editor instanceof IntegerPropertyEditor) {
			value = ConvertUtil.convert(Long.class, value);
		} else if (editor instanceof DecimalPropertyEditor) {
			value = ConvertUtil.convert(BigDecimal.class, value);
		} else if (editor instanceof FloatPropertyEditor) {
			value = ConvertUtil.convert(Double.class, value);
		} else if (editor instanceof TimePropertyEditor) {
			value = ConvertUtil.convert(Time.class, value);
		} else if (editor instanceof TimestampPropertyEditor) {
			value = ConvertUtil.convert(Timestamp.class, value);
		} else if (editor instanceof DatePropertyEditor) {
			value = ConvertUtil.convert(Date.class, value);
		}

		String labelStr = format.format(value);

		// ラベルと値を設定
		Map<String, Object> labelValue = new HashMap<>();
		labelValue.put("value", convertAutocompletionValue);
		labelValue.put("label", labelStr);
		return labelValue;
	}

	private Format getDisplayNumberFormat(NumberPropertyEditor editor) {
		GemConfigService gemConfig = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		String format = editor.getNumberFormat();
		DecimalFormat df = new DecimalFormat();
		if (gemConfig.isFormatNumberWithComma()) {
			// カンマでフォーマットする場合は指定のフォーマットがある場合だけフォーマット適用
			if (format != null) {
				df.applyPattern(format);
				return df;
			} else {
				NumberFormat nf = NumberFormat.getInstance(TemplateUtil.getLocale());
				return nf;
			}
		} else {
			// カンマでフォーマットしない場合はフォーマットがない場合に数値のみのフォーマットを適用
			if (format == null)
				format = "#.###";
			df.applyPattern(format);
			return df;
		}
	}

	private DateFormat getDisplayTimestampFormat(TimeDispRange dispRange, String datetimeFormatPattern, String datetimeLocale, boolean showWeekday) {
		DateFormat format = null;

		if (datetimeFormatPattern != null) {
			// フォーマットの指定がある場合、指定されたフォーマットで表記する
			format = ViewUtil.getDateTimeFormat(datetimeFormatPattern, datetimeLocale);
			return format;
		}

		String timeFormat = "";
		if (TimeDispRange.isDispSec(dispRange)) {
			timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeSecFormat();
		} else if (TimeDispRange.isDispMin(dispRange)) {
			timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeMinFormat();
		} else if (TimeDispRange.isDispHour(dispRange)) {
			timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeHourFormat();
		}

		if (showWeekday) {
			String dateFormat = TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat();
			// テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
			format = DateUtil.getSimpleDateFormat(dateFormat + timeFormat, true, true);
		} else {
			String dateFormat = TemplateUtil.getLocaleFormat().getOutputDateFormat();
			format = DateUtil.getSimpleDateFormat(dateFormat + timeFormat, true);
		}
		return format;
	}

	private DateFormat getDisplayTimeFormat(TimeDispRange dispRange, String datetimeFormatPattern, String datetimeLocale) {

		DateFormat format = null;
		if(datetimeFormatPattern != null){
			format = ViewUtil.getDateTimeFormat(datetimeFormatPattern, datetimeLocale);
			return format;
		}

		if (TimeDispRange.isDispSec(dispRange)) {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeSecFormat(), false);
		} else if (TimeDispRange.isDispMin(dispRange)) {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeMinFormat(), false);
		} else if (TimeDispRange.isDispHour(dispRange)) {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeHourFormat(), false);
		}

		return format;
	}

	private DateFormat getDisplayDateFormat(String datetimeFormatPattern, String datetimeLocale, boolean showWeekday) {
		DateFormat format = null;

		if (datetimeFormatPattern != null) {
			format = ViewUtil.getDateTimeFormat(datetimeFormatPattern, datetimeLocale);
			return format;
		}

		if (showWeekday) {
			// テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat(), false,
					true);
		} else {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false);
		}
		return format;
	}

	private PropertyEditor getPropertyEditor(String definitionName, String viewName, String propName, Integer referenceSectionIndex) {
		EntityView ev = get(definitionName);
		if (ev == null) {
			return null;
		}

		DetailFormView form = null;
		if (viewName == null || viewName.isEmpty()) {
			form = ev.getDefaultDetailFormView();
		} else {
			form = ev.getDetailFormView(viewName);
		}

		//参照プロパティの名前取得
		String currentPropName = null;
		String subPropName = null;
		if (propName.indexOf(".") == -1) {
			currentPropName = propName;
		} else {
			currentPropName = propName.substring(0, propName.indexOf("."));
			subPropName = propName.substring(propName.indexOf(".") + 1);
		}
		final String _currentPropName = currentPropName;

		//プロパティ名が一致する参照セクション抽出
		List<ReferenceSection> secList = form.getSections().stream()
				.filter(s -> s instanceof ReferenceSection).map(s -> (ReferenceSection) s)
				.filter(s -> _currentPropName.equals(s.getPropertyName()))
				.collect(Collectors.toList());

		//インデックスに合うセクション取得
		ReferenceSection refSection = null;
		if (!secList.isEmpty()) {
			PropertyDefinition pd = edm.get(definitionName).getProperty(currentPropName);
			if (pd.getMultiplicity() == 1) {
				refSection = secList.get(0);
			} else {
				if (secList.size() > referenceSectionIndex) {
					refSection = secList.get(referenceSectionIndex);
				}
			}
		}

		//NestPropertyからPropertyEditor取得
		PropertyEditor editor = null;
		if (refSection != null) {
			for (NestProperty nest : refSection.getProperties()) {
				if (subPropName.equals(nest.getPropertyName())) {
					editor = nest.getEditor();
					break;
				}
			}
		}

		return editor;
	}

	private Object convertAutocompletionValue(Object value) {
		if (value instanceof BinaryReference) {
			//詳細でBinary型の自動補完はないか？検索なら条件としてファイル名はありそう
			return ((BinaryReference) value).getName();
		} else if (value instanceof Date) {
			//日付入力形式の文字列に
			return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), false).format((Date) value);
		} else if (value instanceof Timestamp) {
			//日時入力形式の文字列に
			return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), false).format((Timestamp) value);
		} else if (value instanceof Time) {
			//時間入力形式の文字列に
			return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerTimeFormat(), false).format((Time) value);
		} else if (value instanceof SelectValue) {
			return ((SelectValue) value).getValue();
		}
		return value;
	}

	@Override
	public Class<EntityView> getDefinitionType() {
		return EntityView.class;
	}

	@Override
	protected RootMetaData newInstance(EntityView definition) {
		return new MetaEntityView();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPermitRoles(final String definitionName, final String viewName) {

		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);

		if (entityView == null) {
			//EntityViewがない場合は自動生成、許可ロールは未指定
			return null;
		}

		List<MetaViewControlSetting> settings = entityView.getMetaData().getViewControlSettings();
		if (settings != null && !settings.isEmpty()) {
			//View管理設定あり
			MetaViewControlSetting setting = getViewControl(settings, viewName);
			if (setting != null) {
				if (StringUtil.isNotEmpty(setting.getPermitRoles())) {
					String[] permitRoles = setting.getPermitRoles().split(",");
					return Arrays.asList(permitRoles);
				} else {
					//許可ロール未指定の場合は全許可
					return Collections.EMPTY_LIST;
				}
			}

			//viewControlが未指定
			logger.debug("not defined view's viewControl. defName=" + definitionName + ",viewName=" + viewName);
		} else {
			//viewControlが未定義
			logger.debug("not defined any viewControl. defName=" + definitionName);
		}

		SearchFormViewRuntime searchView = getSearchFormView(entityView.getFormViews(), viewName);
		if (searchView != null) {
			//View定義はあるが、管理設定が無い場合は全許可
			return Collections.EMPTY_LIST;
		} else {
			if (StringUtil.isEmpty(viewName)) {
				//デフォルトの場合はView定義がない場合も自動生成、許可ロールは未指定
				return null;
			}
		}

		//view名が不正、存在しない
		throw new ApplicationException("invalid viewName. defName=" + definitionName + ",viewName=" + viewName);
	}


	private MetaViewControlSetting getViewControl(List<MetaViewControlSetting> settings, String viewName) {
		final boolean checkDefault = StringUtil.isEmpty(viewName);
		Optional<MetaViewControlSetting> ret = settings.stream()
				.filter(setting -> {
					String name = setting.getName();
					if (checkDefault) {
						return StringUtil.isEmpty(name);
					} else {
						return viewName.equals(name);
					}
				})
				.findFirst();

		if (ret.isPresent()) {
			return ret.get();
		}
		return null;
	}

	private SearchFormViewRuntime getSearchFormView(List<FormViewRuntime> formViews, String viewName) {
		final boolean checkDefault = StringUtil.isEmpty(viewName);
		//最初の権限チェックポイントがメニューなのでSearchFormViewの有無で確認
		Optional<SearchFormViewRuntime> searchView = formViews.stream()
				.filter(view -> view instanceof SearchFormViewRuntime)
				.filter(view -> {
					String name = view.getMetaData().getName();
					if (checkDefault) {
						return StringUtil.isEmpty(name);
					} else {
						return viewName.equals(name);
					}
				})
				.map(view -> (SearchFormViewRuntime)view)
				.findFirst();

		if (searchView.isPresent()) {
			return searchView.get();
		}
		return null;
	}

	@Override
	public void checkState(String definitionName) {

		EntityViewRuntime entityView = service.getRuntimeByName(definitionName);

		if (entityView == null) {
			return;
		}

		entityView.checkState();
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}

}
