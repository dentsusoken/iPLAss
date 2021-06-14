/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.csv.MultipleFormat;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDownloadSearchContext extends SearchContextBase {

	private static Logger log = LoggerFactory.getLogger(CsvDownloadSearchContext.class);

	private static final String DEFAULT_CHAR_SET = "UTF-8";

	private SearchContextBase context;

	private HashMap<String, ReferenceDisplayLabelInfo> referenceDisplayLabelMap = new HashMap<>();

	public CsvDownloadSearchContext(SearchContextBase context) {
		this.context = context;
	}

	@Override
	public RequestContext getRequest() {
		return context.getRequest();
	}

	@Override
	public void setRequest(RequestContext request) {
		super.setRequest(request);
		context.setRequest(request);
	}

	@Override
	public EntityDefinition getEntityDefinition() {
		return context.getEntityDefinition();
	}

	@Override
	public void setEntityDefinition(EntityDefinition definition) {
		context.setEntityDefinition(definition);
	}

	@Override
	public EntityView getEntityView() {
		return context.getEntityView();
	}

	@Override
	public void setEntityView(EntityView view) {
		context.setEntityView(view);
	}

	@Override
	public String getDefName() {
		return context.getDefName();
	}

	@Override
	protected String getViewName() {
		return context.getViewName();
	}

	@Override
	public Select getSelect() {
		if (outputSpecifyProperties() && !isOutputResult()) {
			boolean distinct = getConditionSection().isDistinct();
			String[] properties = getConditionSection().getCsvdownloadProperties().split(",");
			Select select = new Select();
			select.setDistinct(distinct);
			for (String property : properties) {
				PropertyDefinition pd = context.getPropertyDefinition(property);
				if (pd != null) {
					if (pd instanceof ReferenceProperty) {
						// 参照プロパティ自体が設定された場合はname出力
						select.add(property + "." + Entity.NAME);
						referenceDisplayLabelMap.put(property + "." + Entity.NAME, new ReferenceDisplayLabelInfo(property, false));
					} else {
						select.add(property);
					}
				}
			}
			return select;
		} else {
			return context.getSelect();
		}
	}

	@Override
	public Where getWhere() {
		return context.getWhere();
	}

	@Override
	public OrderBy getOrderBy() {
		return context.getOrderBy();
	}

	@Override
	public Limit getLimit() {
		return context.getLimit();
	}

	@Override
	public boolean isVersioned() {
		return context.isVersioned();
	}

	@Override
	public boolean isSearch() {
		return context.isSearch();
	}

	@Override
	public boolean isCount() {
		return context.isCount();
	}

	@Override
	public boolean checkParameter() {
		return context.checkParameter();
	}

	@Override
	public boolean validation() {
		return context.validation();
	}

	public boolean isForUpload() {
		return Boolean.valueOf(getRequest().getParam("isForUpload"));
	}

	public boolean isNoDispName() {
		return Boolean.valueOf(getRequest().getParam("isNoDispName"));
	}

	public boolean isOutputResult() {
		return Boolean.valueOf(getRequest().getParam("isOutputResult"));
	}

	public boolean isOutputCodeValue() {
		return Boolean.valueOf(getRequest().getParam(Constants.CSV_IS_OUTPUT_CODE_VALUE));
	}

	@Override
	public SearchConditionSection getConditionSection() {
		return context.getConditionSection();
	}

	@Override
	public PropertyDefinition getPropertyDefinition(String propName) {
		return context.getPropertyDefinition(propName);
	}

	public List<String> getColumns() {
		ArrayList<String> cols = new ArrayList<>();
		if (outputSpecifyProperties() && !isOutputResult()) {
			String[] properties = getConditionSection().getCsvdownloadProperties().split(",");
			for (String property : properties) {
				PropertyDefinition pd = context.getPropertyDefinition(property);
				if (pd != null) {
					if (pd instanceof ReferenceProperty) {
						// 参照プロパティ自体が設定された場合はname出力
						cols.add(property + "." + Entity.NAME);
						referenceDisplayLabelMap.put(property + "." + Entity.NAME, new ReferenceDisplayLabelInfo(property, false));
					} else {
						cols.add(property);
					}
				}
			}
		} else {
			List<PropertyColumn> properties = context.getResultSection().getElements().stream()
					.filter(e -> e instanceof PropertyColumn)
					.map(e -> (PropertyColumn) e)
					.filter(column -> column.isOutputCsv())
					.collect(Collectors.toList());
			for (PropertyColumn col : properties) {
				if (EntityViewUtil.isDisplayElement(getDefName(), col.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
					String propName = col.getPropertyName();
					if (col.getEditor() instanceof ReferencePropertyEditor) {
						List<NestProperty> nest = ((ReferencePropertyEditor)col.getEditor()).getNestProperties();
						addColumn(cols, propName, nest.toArray(new NestProperty[nest.size()]));
					} else if (col.getEditor() instanceof JoinPropertyEditor) {
						addColumn(cols, propName);
						JoinPropertyEditor je = (JoinPropertyEditor) col.getEditor();
						for (NestProperty nest : je.getProperties()) {
							if (!nest.isOutputCsv()) {
								continue;
							}
							addColumn(cols, nest.getPropertyName());
						}
					} else {
						addColumn(cols, propName);
					}
				}
			}
		}

		return cols;
	}

	public MultipleFormat getMultipleFormat() {
		if (getConditionSection().getCsvMultipleFormat() != null) {
			return getConditionSection().getCsvMultipleFormat();
		} else {
			return MultipleFormat.EACH_COLUMN;
		}
	}

	public String getEntityLabel() {
		String displayName = TemplateUtil.getMultilingualString(
				context.getEntityDefinition().getDisplayName(),
				context.getEntityDefinition().getLocalizedDisplayNameList());
		String viewTitle = TemplateUtil.getMultilingualString(
				context.getForm().getTitle(),
				context.getForm().getLocalizedTitleList());
		if(StringUtil.isNotBlank(viewTitle)) {
			displayName = viewTitle;
		}
		return displayName;
	}

	public String getDisplayLabel(String propName) {
		if (referenceDisplayLabelMap.containsKey(propName)) {
			//参照の場合はselectした時の項目そのまま引っ張るとoidとnameになってしまうので、
			//予め保持しておいた情報から参照の表示名を取得する
			ReferenceDisplayLabelInfo info = referenceDisplayLabelMap.get(propName);
			String displayLabel = getDisplayLabel(info.propName);
			if (info.isOid) {
				return displayLabel + "(ID)";
			} else {
				return displayLabel;
			}
		}

		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > -1) {
			//下位のネストあり
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);

			PropertyColumn property = getPropertyColumn(topPropName);
			if (property != null
					&& property.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) property.getEditor()).getNestProperties().isEmpty()) {
				//下位のネストの表示ラベル取得
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				String displayLabel = _getDisplayLabel(editor.getNestProperties(), subPropName);
				if (displayLabel != null) return displayLabel;
			}
		}

		//画面定義からカラムの表示ラベル取得
		PropertyColumn property = getPropertyColumn(propName);
		if (property != null && !StringUtil.isEmpty(property.getDisplayLabel())) {
			String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList());
			if (displayLabel != null) return displayLabel;
		}

		//取れない場合はEntity定義から取得
		PropertyDefinition pd = context.getPropertyDefinition(propName);
		return TemplateUtil.getMultilingualString(pd.getDisplayName(), pd.getLocalizedDisplayNameList());
	}

	private String _getDisplayLabel(List<NestProperty> properties, String propName) {
		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > -1) {
			//下位のネストあり
			String topPropName = propName.substring(0, firstDotIndex);
			String subPropName = propName.substring(firstDotIndex + 1);

			NestProperty property = getNestProperty(properties, topPropName);
			if (property != null
					&& property.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) property.getEditor()).getNestProperties().isEmpty()) {
				//下位のネストの表示ラベル取得
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				return _getDisplayLabel(editor.getNestProperties(), subPropName);
			}
		} else {
			//画面定義からネストの表示ラベル取得
			NestProperty property = getNestProperty(properties, propName);
			if (property != null) {
				return TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList());
			}
		}

		return null;
	}

	private NestProperty getNestProperty(List<NestProperty> properties, String propName) {
		for (NestProperty property : properties) {
			if (propName.equals(property.getPropertyName())) return property;
		}
		return null;
	}

	/**
	 * 対象のPropertyColumnを返します。
	 *
	 * @param propName プロパティ名
	 * @return 対象のPropertyColumn
	 */
	public PropertyColumn getPropertyColumn(String propName) {
		Optional<PropertyColumn> property = context.getResultSection().getElements().stream()
				.filter(e -> e instanceof PropertyColumn).map(e -> (PropertyColumn) e)
				.filter(e -> propName.equals(e.getPropertyName())).findFirst();
		if (property.isPresent()) {
			return property.get();
		}
		return null;
	}

	/**
	 * UserPropertyEditorが設定されている列を返します。
	 *
	 * @return UserPropertyEditorが設定されているプロパティ名
	 */
	public List<String> getUserPropertyColumns() {
		ArrayList<String> cols = new ArrayList<>();
		List<PropertyColumn> properties = context.getResultSection().getElements().stream()
				.filter(e -> e instanceof PropertyColumn).map(e -> (PropertyColumn) e)
				.collect(Collectors.toList());
		for (PropertyColumn col : properties) {
			if (EntityViewUtil.isDisplayElement(getDefName(), col.getElementRuntimeId(), OutputType.SEARCHRESULT, null)) {
				String propName = col.getPropertyName();
				if (col.getEditor() instanceof UserPropertyEditor) {
					cols.add(propName);
				} else if (col.getEditor() instanceof ReferencePropertyEditor) {
					//ReferencePropertyEditorのNestをチェック
					getNestUserPropertyColumns(cols, propName + ".", ((ReferencePropertyEditor)col.getEditor()).getNestProperties());
				} else if (col.getEditor() instanceof JoinPropertyEditor) {
					JoinPropertyEditor je = (JoinPropertyEditor) col.getEditor();
					if (je.getEditor() instanceof UserPropertyEditor) {
						cols.add(propName);
					} else if (je.getEditor() instanceof ReferencePropertyEditor) {
						//JoinPropertyEditorのEditorがReferencePropertyEditorの場合、そのNestをチェック
						getNestUserPropertyColumns(cols, propName + ".", ((ReferencePropertyEditor)je.getEditor()).getNestProperties());
					}
					//JoinPropertyEditorのNestをチェック
					getNestUserPropertyColumns(cols, "", je.getProperties());
				}
			}
		}

		return cols;
	}

	/**
	 * NestPropertyにUserPropertyEditorが設定されている列を追加します。
	 *
	 * @param cols UserPropertyEditorが設定されている列情報
	 * @param prefixPropName プロパティ名の接頭語（Reference用）
	 * @param nestProperties NestProperty
	 */
	private void getNestUserPropertyColumns(final List<String> cols, final String prefixPropName, final List<NestProperty> nestProperties) {

		if (nestProperties == null) {
			return;
		}

		for (NestProperty nest : nestProperties) {
			String nestPropName = prefixPropName + nest.getPropertyName();
			if (nest.getEditor() instanceof UserPropertyEditor) {
				cols.add(nestPropName);
			} else if (nest.getEditor() instanceof ReferencePropertyEditor) {
				//ReferencePropertyEditorのNestをチェック
				getNestUserPropertyColumns(cols, nestPropName + ".", ((ReferencePropertyEditor)nest.getEditor()).getNestProperties());
			} else if (nest.getEditor() instanceof JoinPropertyEditor) {
				JoinPropertyEditor je = (JoinPropertyEditor) nest.getEditor();
				if (je.getEditor() instanceof UserPropertyEditor) {
					cols.add(nestPropName);
				} else if (je.getEditor() instanceof ReferencePropertyEditor) {
					//JoinPropertyEditorのEditorがReferencePropertyEditorの場合、そのNestをチェック
					getNestUserPropertyColumns(cols, nestPropName + ".", ((ReferencePropertyEditor)je.getEditor()).getNestProperties());
				}
				//JoinPropertyEditorのNestをチェック
				getNestUserPropertyColumns(cols, prefixPropName, je.getProperties());
			}
		}
	}

	private void addColumn(ArrayList<String> cols, String propName, NestProperty... nest) {

		PropertyDefinition pd = getPropertyDefinition(propName);
		if (pd instanceof ReferenceProperty) {
			if (nest != null && nest.length > 0) {
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);
				for (NestProperty np : nest) {
					if (!np.isOutputCsv()) {
						continue;
					}
					PropertyDefinition rpd = red.getProperty(np.getPropertyName());
					if (rpd != null) {
						if (Entity.OID.equals(np.getPropertyName())) {
							if (!getConditionSection().isNonOutputOid()) {
								cols.add(propName + "." + Entity.OID);
							}
						} else {
							String nestPropName = propName + "." + np.getPropertyName();
							if (!(rpd instanceof ReferenceProperty)) {
								//参照以外は項目に追加、参照の場合はネストの項目で判断
								if (!cols.contains(nestPropName)) {
									cols.add(nestPropName);
								}
							}
							if (np.getEditor() instanceof ReferencePropertyEditor) {
								ReferencePropertyEditor rpe = (ReferencePropertyEditor) np.getEditor();
								List<NestProperty> _nest = rpe.getNestProperties();
								addColumn(cols, nestPropName, _nest.toArray(new NestProperty[_nest.size()]));
							}
						}
					}
				}
			} else {
				//ネストがなければ参照自身を出力
				if (!getConditionSection().isNonOutputReference()) {
					if (!getConditionSection().isNonOutputOid()) {
						cols.add(propName + "." + Entity.OID);
						//nestのプロパティ名だけだと表示名で表示するときに不都合なので変換用の情報を保持しとく
						referenceDisplayLabelMap.put(propName + "." + Entity.OID, new ReferenceDisplayLabelInfo(propName, true));
					}
					cols.add(propName + "." + Entity.NAME);
					//nestのプロパティ名だけだと表示名で表示するときに不都合なので変換用の情報を保持しとく
					referenceDisplayLabelMap.put(propName + "." + Entity.NAME, new ReferenceDisplayLabelInfo(propName, false));
				}
			}
		} else if (pd instanceof BinaryProperty) {
			if (!getConditionSection().isNonOutputBinaryRef()) {
				if (!cols.contains(pd.getName())) cols.add(pd.getName());
			}
		} else {
			if (!cols.contains(pd.getName())) cols.add(pd.getName());
		}
	}

	public boolean outputSpecifyProperties() {
		return StringUtil.isNotBlank(getConditionSection().getCsvdownloadProperties());
	}

	public SearchQueryInterrupterHandler getSearchQueryInterrupterHandler(boolean isUpload) {
		if (isUpload) {
			SearchQueryInterrupterHandlerForUpload handler = null;
			if (context.getForm().isUseInterrupterForCsvDownload()) {
				handler = new SearchQueryInterrupterHandlerForUpload(getRequest(), context, createInterrupter(context.getForm().getInterrupterName()));
			} else {
				log.debug("not use search query interrupter, set default.");
				handler = new SearchQueryInterrupterHandlerForUpload(getRequest(), context, getDefaultSearchQueryInterrupter());
			}
			return handler;
		} else {
			if (context.getForm().isUseInterrupterForCsvDownload()) {
				return context.getSearchQueryInterrupterHandler();
			} else {
				//何もしないデフォルトInterrupter生成
				log.debug("not use search query interrupter, set default.");
				return new SearchQueryInterrupterHandler(getRequest(), context, getDefaultSearchQueryInterrupter());
			}
		}
	}

	public String getCharacterCode() {
		if (StringUtil.isEmpty(getRequest().getParam(Constants.CSV_CHARACTER_CODE))) {
			return DEFAULT_CHAR_SET;
		} else {
			return getRequest().getParam(Constants.CSV_CHARACTER_CODE);
		}
	}

	private class SearchQueryInterrupterHandlerForUpload extends SearchQueryInterrupterHandler {

		public SearchQueryInterrupterHandlerForUpload(RequestContext request, SearchContextBase context, SearchQueryInterrupter interrupter) {
			super(request, context, interrupter);
		}

		@Override
		public SearchQueryContext beforeSearch(Query query, SearchQueryType type) {
			// アップロード用では言語フラグは常にfalseに
			log.debug("query localized flag is not set.");
			SearchFormView formView = super.getContext().getForm();
			SearchQueryContext ret = super.getInterrupter().beforeSearch(super.getRequest(), formView, query, type);
			query.setLocalized(false);
			return ret;
		}

		@Override
		public void afterSearch(Query query, Entity entity, SearchQueryType type) {
			SearchFormView formView = super.getContext().getForm();
			super.getInterrupter().afterSearch(super.getRequest(), formView, query, entity, type);
		}
	}

	private class ReferenceDisplayLabelInfo {
		private String propName;
		private boolean isOid;
		public ReferenceDisplayLabelInfo(String propName, boolean isOid) {
			this.propName = propName;
			this.isOid = isOid;
		}
	}
}
