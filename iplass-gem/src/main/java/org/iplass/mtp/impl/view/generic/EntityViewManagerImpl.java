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
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
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

import org.iplass.gem.command.GemResourceBundleUtil;
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
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.view.generic.common.MetaAutocompletionSetting.AutocompletionSettingHandler;
import org.iplass.mtp.impl.view.generic.element.ElementHandler;
import org.iplass.mtp.impl.view.generic.element.MetaButton.ButtonHandler;
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
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
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
	public PropertyEditor getPropertyEditor(String defName, String viewType, String viewName, String propName) {

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
			editor = getDetailFormViewEditor(defName, propName, form);
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

	private PropertyEditor getDetailFormViewEditor(String defName, String propName, DetailFormView form) {
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
			if (!isDisplayElement(defName, section.getElementRuntimeId(), outputType, null)) {
				continue;
			}
			if (section instanceof DefaultSection) {
				PropertyEditor editor = getEditor(defName, outputType, (DefaultSection)section, currentPropName, subPropName);
				if (editor != null) {
					return editor;
				}
			} else if (section instanceof MassReferenceSection) {
				PropertyEditor editor = getEditor(defName, outputType, (MassReferenceSection) section, currentPropName, subPropName);
				if (editor != null) {
					return editor;
				}
			}
		}
		return null;
	}

	private PropertyEditor getEditor(String defName, OutputType outputType, DefaultSection section, final String currentPropName, final String subPropName) {
		for (Element element : section.getElements()) {
			if (!isDisplayElement(defName, element.getElementRuntimeId(), outputType, null)) {
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
			} else if (element instanceof DefaultSection) {
				PropertyEditor nest = getEditor(defName, outputType, (DefaultSection)element, currentPropName, subPropName);
				if (nest != null) {
					return nest;
				}
			}
		}
		return null;
	}

	private PropertyEditor getEditor(String defName, OutputType outputType, MassReferenceSection section, final String currentPropName, final String subPropName) {
		if (subPropName == null) return null;

		if (!isDisplayElement(defName, section.getElementRuntimeId(), outputType, null)) {
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
						&& EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHRESULT)) {
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
					&& EntityViewUtil.isDisplayElement(defName, property.getElementRuntimeId(), OutputType.SEARCHRESULT)) {
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
				PropertyEditor editor = getEditor(defName, OutputType.BULK, (DefaultSection)section, currentPropName, subPropName);
				if (editor != null) {
					return editor;
				}
			}
		}
		return null;
	}

	@Override
	public PropertyEditor getPropertyEditor(String defName, String viewType, String viewName, String propName, Integer refSectionIndex) {
		PropertyEditor editor = null;
		if (refSectionIndex == null) {
			editor = getPropertyEditor(defName, viewType, viewName, propName);
		} else {
			editor = getPropertyEditor(defName, viewName, propName, refSectionIndex);
		}

		return editor;
	}

	@Override
	public void executeTemplate(String name, String templateName, HttpServletRequest req,
			HttpServletResponse res, ServletContext application, PageContext page) {
		if (name == null || templateName == null) return;

		EntityViewHandler handler = service.getRuntimeByName(name);
		if (handler == null) return;

		//DynamicScriptをapiからimplに移すと、戻り値がapiからimplの逆参照となってしまう。
		//→戻り値ではなくDynamicScriptをメソッド内で実行する。
		//　→api(jsp)からGroovyTemplateを実行するためのDynamicScriptだったが、
		//　　impl内で完結するならそのままGroovyTemplateで十分。
		GroovyTemplate template = handler.getTemplate(templateName);
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
		EntityViewHandler handler = service.getRuntimeByName(entity.getDefinitionName());
		if (handler == null) {
			throw new ApplicationException(resourceString("impl.view.generic.EntityViewManagerImpl.viewErr"));
		}

		DetailFormViewHandler fh = null;
		for (FormViewHandler view : handler.getFormViews()) {
			if (view instanceof DetailFormViewHandler) {
				//nameが一致するhandlerを検索
				if (viewName == null || viewName.isEmpty()) {
					if (view.getMetaData().getName() == null || view.getMetaData().getName().isEmpty()) {
						fh = (DetailFormViewHandler) view;
						break;
					}
				} else {
					if (viewName.equals(view.getMetaData().getName())) {
						fh = (DetailFormViewHandler) view;
						break;
					}
				}
			}
		}

		if (fh != null) {
			return fh.copyEntity(entity);
		}
		return null;
	}

	@Override
	public Entity initEntity(String definitionName, String viewName, Entity entity) {
		EntityViewHandler handler = service.getRuntimeByName(definitionName);
		if (handler == null) {
			throw new ApplicationException(resourceString("impl.view.generic.EntityViewManagerImpl.viewErr"));
		}

		DetailFormViewHandler fh = null;
		for (FormViewHandler view : handler.getFormViews()) {
			if (view instanceof DetailFormViewHandler) {
				//nameが一致するhandlerを検索
				if (viewName == null || viewName.isEmpty()) {
					if (view.getMetaData().getName() == null || view.getMetaData().getName().isEmpty()) {
						fh = (DetailFormViewHandler) view;
						break;
					}
				} else {
					if (viewName.equals(view.getMetaData().getName())) {
						fh = (DetailFormViewHandler) view;
						break;
					}
				}
			}
		}

		if (fh != null) {
			return fh.initEntity(entity, definitionName);
		}
		return null;
	}

	@Override
	public Map<String, Object> applyDefaultPropertyCondition(String definitionName, String viewName, Map<String, Object> defaultCondMap) {
		EntityViewHandler handler = service.getRuntimeByName(definitionName);
		if (handler == null) {
			//デフォルトなど
			return defaultCondMap;
		}

		SearchFormViewHandler fh = null;
		for (FormViewHandler view : handler.getFormViews()) {
			if (view instanceof SearchFormViewHandler) {
				//nameが一致するhandlerを検索
				if (viewName == null || viewName.isEmpty()) {
					if (view.getMetaData().getName() == null || view.getMetaData().getName().isEmpty()) {
						fh = (SearchFormViewHandler) view;
						break;
					}
				} else {
					if (viewName.equals(view.getMetaData().getName())) {
						fh = (SearchFormViewHandler) view;
						break;
					}
				}
			}
		}

		if (fh != null) {
			return fh.applyDefaultPropertyCondition(defaultCondMap);
		}
		return defaultCondMap;
	}

	@Override
	public String getCsvDownloadFileName(String definitionName, String viewName, String defaultName, Map<String, Object> csvVariableMap) {
		String fileName = defaultName;
		EntityViewHandler handler = service.getRuntimeByName(definitionName);
		if (handler != null) {
			SearchFormViewHandler fh = null;
			for (FormViewHandler view : handler.getFormViews()) {
				if (view instanceof SearchFormViewHandler) {
					//nameが一致するhandlerを検索
					if (StringUtil.isEmpty(viewName)) {
						if (StringUtil.isEmpty(view.getMetaData().getName())) {
							fh = (SearchFormViewHandler) view;
							break;
						}
					} else {
						if (viewName.equals(view.getMetaData().getName())) {
							fh = (SearchFormViewHandler) view;
							break;
						}
					}
				}
			}

			if (fh != null) {
				fileName = fh.getCsvDownloadFileName(defaultName, csvVariableMap).replace("/", "_").replace(" ", "_");
			}
		} else {
			//View未定義はdefaultName
		}
		return fileName.replace("/", "_").replace(" ", "_");
	}

	@Override
	public Condition getMassReferenceSectionCondition(String name, String key) {
		if (name == null || key == null) return null;

		EntityViewHandler handler = service.getRuntimeByName(name);
		if (handler == null) return null;

		PreparedQuery query = handler.getQuery(key);
		if (query == null) return null;

		return query.condition(null);
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
		EntityViewHandler handler = service.getRuntimeByName(definitionName);
		if (handler == null) {
			//取れなかったら呼出元でデフォルトレイアウト生成
			return "";
		}

		Map<String, GroovyTemplate> sectionScriptMap = handler.getCustomStyleScriptMap(scriptKey);
		GroovyTemplate script = sectionScriptMap.get(editorScriptKey);
		if (script == null) {
			//スクリプトが未指定の場合はそのまま
			return "";
		}

		Map<String, Object> bindings = new HashMap<String, Object>();
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

		EntityViewHandler viewHandler = service.getRuntimeByName(definitionName);
		if (viewHandler == null) {
			return false;
		}

		ElementHandler elementHandler = viewHandler.getElementHandler(elementRuntimeId);
		if (elementHandler == null) {
			return false;
		}

		return elementHandler.isDisplay(outputType, entity);
	}

	@Override
	public boolean isDisplayButton(String definitionName, String buttonKey, OutputType outputType, Entity entity) {
		if (definitionName == null || buttonKey == null || outputType == null) {
			return false;
		}

		EntityViewHandler viewHandler = service.getRuntimeByName(definitionName);
		if (viewHandler == null) {
			return false;
		}

		ButtonHandler buttonHandler = viewHandler.getButtonHandler(buttonKey);
		if (buttonHandler == null) {
			return false;
		}

		return buttonHandler.isDisplayButton(outputType, entity);
	}

	@Override
	public String getUrlParameter(String name, String templateName, Entity entity) {
		if (name == null || templateName == null) return "";

		EntityViewHandler handler = service.getRuntimeByName(name);
		if (handler == null) return "";

		GroovyTemplate template = handler.getTemplate(templateName);
		StringWriter sw = new StringWriter();
		if (template != null) {
			try {
				template.doTemplate(new UrlParameterGroovyTemplateBinding(sw, entity));
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

		public UrlParameterGroovyTemplateBinding(Writer writer, Entity entity) {
			super(writer);

			RequestContext request = WebUtil.getRequestContext();
			setVariable("request", request);
			setVariable("session", request.getSession());
			setVariable("parent", entity);
		}
	}

	@Override
	public Object getAutocompletionValue(String definitionName, String viewName, String viewType, String propName, String autocompletionKey, Integer referenceSectionIndex, Map<String, String[]> param) {
		EntityViewHandler view = service.getRuntimeByName(definitionName);
		if (view == null) return null;

		AutocompletionSettingHandler handler = view.getAutocompletionSettingHandler(autocompletionKey);
		if (handler == null) return null;

		PropertyEditor editor = null;
		if (referenceSectionIndex != null) {
			editor = getPropertyEditor(definitionName, viewName, propName, referenceSectionIndex);
		} else {
			editor = getPropertyEditor(definitionName, viewType, viewName, propName);
		}
		boolean isReference = editor instanceof ReferencePropertyEditor;
		Object value = handler.handle(param, isReference);

		Object returnValue = null;
		if (isReference) {
			// 参照型の場合はEntityで返す
			if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				for (Object obj : list) {
					if (!(obj instanceof Entity)) {
						logger.error("For return type, please set Entity class as return value. The result type of this execution is of type " + obj.getClass().getName() + ".");
						throw new AutocompletionHandleException();
					}
				}
			} else if (!(value instanceof Entity)) {
				logger.error("For return type, please set Entity class as return value. The result type of this execution is of type " + value.getClass().getName() + ".");
				throw new AutocompletionHandleException();
			}
			returnValue = value;
		} else {
			if (value instanceof Entity) {
				// スクリプト、EQLの結果がEntityの場合はプロパティの値だけ返す
				returnValue = ((Entity) value).getValue(propName);
			} else if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				List<Object> retList = new ArrayList<>();
				for (Object obj : list) {
					retList.add(convertAutocompletionValue(obj));
				}
				returnValue = retList;
			} else {
				returnValue = convertAutocompletionValue(value);
			}
		}

		return returnValue;
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

		EntityViewHandler handler = service.getRuntimeByName(definitionName);

		if (handler == null) {
			return null;
		}

		List<MetaViewControlSetting> settings = handler.getMetaData().getViewControlSettings();
		if (settings != null && !settings.isEmpty()) {
			//View管理設定あり
			MetaViewControlSetting setting = getViewControl(settings, viewName);
			if (setting != null) {
				if (StringUtil.isNotBlank(setting.getPermitRoles())) {
					String[] permitRoles = setting.getPermitRoles().split(",");
					return Arrays.asList(permitRoles);
				} else {
					return Collections.EMPTY_LIST;
				}
			}

			//viewControlが未指定
			logger.debug("not defined viewControl. defName=" + definitionName + ",viewName=" + viewName);
		}

		SearchFormViewHandler formView = getSearchFormViewHandler(handler.getFormViews(), viewName);
		if (formView != null) {
			//定義はあるが、管理設定が無い→許可ロール未指定扱い
			return Collections.EMPTY_LIST;
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

	private SearchFormViewHandler getSearchFormViewHandler(List<FormViewHandler> handlers, String viewName) {
		final boolean checkDefault = StringUtil.isEmpty(viewName);
		//最初の権限チェックポイントがメニューなのでSearchFormViewの有無で確認
		Optional<SearchFormViewHandler> formView = handlers.stream()
				.filter(view -> view instanceof SearchFormViewHandler)
				.filter(view -> {
					String name = view.getMetaData().getName();
					if (checkDefault) {
						return StringUtil.isEmpty(name);
					} else {
						return viewName.equals(name);
					}
				})
				.map(view -> (SearchFormViewHandler)view)
				.findFirst();

		if (formView.isPresent()) {
			return formView.get();
		}
		return null;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
