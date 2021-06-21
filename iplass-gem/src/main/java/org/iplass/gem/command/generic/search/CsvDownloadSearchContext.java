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
import java.util.LinkedHashMap;
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
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.CsvItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDownloadSearchContext extends SearchContextBase {

	private static Logger log = LoggerFactory.getLogger(CsvDownloadSearchContext.class);

	private static final String DEFAULT_CHAR_SET = "UTF-8";

	private SearchContextBase context;

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
		if (isOutputSpecifyProperties() && !isOutputResult()) {
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

	@Override
	protected SearchFormView getForm() {
		return context.getForm();
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

	@Override
	protected SearchResultSection getResultSection() {
		return context.getResultSection();
	}

	/**
	 * 出力文字コードを返します。
	 *
	 * @return 出力文字コード
	 */
	public String getCharacterCode() {
		if (StringUtil.isEmpty(getRequest().getParam(Constants.CSV_CHARACTER_CODE))) {
			return DEFAULT_CHAR_SET;
		} else {
			return getRequest().getParam(Constants.CSV_CHARACTER_CODE);
		}
	}

	/**
	 * 多重度出力形式を返します。
	 *
	 * @return 多重度出力形式
	 */
	public MultipleFormat getMultipleFormat() {
		if (getConditionSection().getCsvMultipleFormat() != null) {
			return getConditionSection().getCsvMultipleFormat();
		} else {
			return MultipleFormat.EACH_COLUMN;
		}
	}

	/**
	 * CSV出力列情報を返します。
	 *
	 * @return CSV出力列情報
	 */
	public List<CsvColumn> getCsvColumns() {

		if (isOutputSpecifyProperties() && !isOutputResult()) {
			//出力列を直接指定の場合
			ArrayList<CsvColumn> columns = new ArrayList<>();

			String[] propertyNames = getConditionSection().getCsvdownloadProperties().split(",");
			for (String propertyName : propertyNames) {
				CsvColumn column = getCsvColumn(propertyName);
				if (column != null) {
					columns.add(column);
				}
			}
			return columns;

		} else {
			//検索結果設定の場合
			LinkedHashMap<String, CsvColumn> columnMap = new LinkedHashMap<>();

			SearchConditionSection section = getConditionSection();
			//OIDを出力する場合、先頭に出力
			if (!section.isNonOutputOid()) {
				PropertyDefinition pd = getPropertyDefinition(Entity.OID);
				CsvColumn csvColumn = new CsvColumn(Entity.OID);
				csvColumn.setPropertyDefinition(pd);
				csvColumn.setCsvItem(null);
				csvColumn.setColumnLabel(getEntityIdColLabel());
				columnMap.put(csvColumn.getPropertyName(), csvColumn);
			}

			//CsvItemでかつ出力対象を取得
			List<CsvItem> csvItems = getResultSection().getElements().stream()
					.filter(element -> {
						return EntityViewUtil.isDisplayElement(
								getDefName(), element.getElementRuntimeId(), OutputType.SEARCHRESULT, null);
					})
					.filter(element -> {
						return (element instanceof CsvItem) && ((CsvItem)element).isOutputCsv();
					})
					.map(e -> (CsvItem) e)
					.collect(Collectors.toList());

			for (CsvItem csvItem : csvItems) {

				String propertyName = csvItem.getPropertyName();
				if (csvItem.getEditor() instanceof ReferencePropertyEditor) {
					//ReferencePropertyEditorの場合、Nestがあるかで設定
					List<NestProperty> nest = ((ReferencePropertyEditor)csvItem.getEditor()).getNestProperties();
					addColumn(columnMap, propertyName, csvItem, nest.toArray(new NestProperty[nest.size()]));
				} else if (csvItem.getEditor() instanceof JoinPropertyEditor) {
					//JoinPropertyEditorの場合、自身を追加
					addColumn(columnMap, propertyName, csvItem);

					//NestPropertyが存在する場合は、それぞれを追加
					JoinPropertyEditor je = (JoinPropertyEditor) csvItem.getEditor();
					for (NestProperty nest : je.getProperties()) {
						if (!nest.isOutputCsv()) {
							continue;
						}
						addColumn(columnMap, nest.getPropertyName(), nest);
					}
				} else {
					//上記以外は自身を追加
					addColumn(columnMap, propertyName, csvItem);
				}
			}
			return new ArrayList<>(columnMap.values());
		}
	}

	/**
	 * CSV出力列情報を返します。
	 * CSV出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param propertyName プロパティ名
	 * @return CSV出力列情報
	 */
	public CsvColumn getCsvColumn(String propertyName) {

		CsvColumn column = null;
		PropertyDefinition pd = getPropertyDefinition(propertyName);
		if (pd != null) {
			//プロパティとして存在していればOK
			if (pd instanceof ReferenceProperty) {
				// 参照プロパティ自体が設定された場合はname出力
				column = new CsvColumn(propertyName + "." + Entity.NAME);
				column.setReferenceProperty(pd);
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);
				column.setPropertyDefinition(red.getProperty(Entity.NAME));
			} else {
				column = new CsvColumn(propertyName);
				column.setPropertyDefinition(pd);
			}
			//プロパティ名からCsvItemを取得
			CsvItem csvItem = getCsvItem(propertyName);
			column.setCsvItem(csvItem);
		} else {
			//仮想プロパティとして有効かチェックするため、propertyNameからCsvItem定義を取得
			CsvItem csvItem = getCsvItem(propertyName);
			if (csvItem != null) {
				//PropertyDefinitionはnullになる
				column = new CsvColumn(propertyName);
				column.setCsvItem(csvItem);
			}
		}
		if (column != null) {
			//列名を取得
			column.setColumnLabel(getColumnLabel(column));
		}
		return column;
	}

	/**
	 * CSV項目を直接指定しているかを返します。
	 * @return
	 */
	private boolean isOutputSpecifyProperties() {
		return StringUtil.isNotBlank(getConditionSection().getCsvdownloadProperties());
	}

	/**
	 * CsvItemに関する列情報を追加します。
	 *
	 * @param columnMap 列情報格納用Map
	 * @param propertyName プロパティ名
	 * @param csvItem 対象CsvItem
	 * @param nestProperties ReferencePropertyEditorのNestProperty
	 */
	private void addColumn(LinkedHashMap<String, CsvColumn> columnMap, String propertyName, CsvItem csvItem, NestProperty... nestProperties) {

		PropertyDefinition pd = getPropertyDefinition(propertyName);
		if (pd instanceof ReferenceProperty) {
			if (nestProperties != null && nestProperties.length > 0) {
				//Nestがある場合は、Nestのみ出力

				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);
				for (NestProperty nestProperty : nestProperties) {
					if (!nestProperty.isOutputCsv()) {
						continue;
					}

					PropertyDefinition rpd = red.getProperty(nestProperty.getPropertyName());
					if (rpd != null) {
						if (Entity.OID.equals(nestProperty.getPropertyName())) {
							//OIDは出力可否をチェック
							if (!getConditionSection().isNonOutputOid()) {
								CsvColumn csvColumn = new CsvColumn(propertyName + "." + nestProperty.getPropertyName());
								csvColumn.setPropertyDefinition(rpd);
								csvColumn.setCsvItem(nestProperty);
								csvColumn.setColumnLabel(getColumnLabel(csvColumn));
								columnMap.putIfAbsent(csvColumn.getPropertyName(), csvColumn);
							}
						} else {
							String nestPropName = propertyName + "." + nestProperty.getPropertyName();
							if (!(rpd instanceof ReferenceProperty)) {
								//参照以外は項目に追加、参照の場合はネストの項目で判断
								CsvColumn csvColumn = new CsvColumn(nestPropName);
								csvColumn.setPropertyDefinition(rpd);
								csvColumn.setCsvItem(nestProperty);
								csvColumn.setColumnLabel(getColumnLabel(csvColumn));
								columnMap.putIfAbsent(csvColumn.getPropertyName(), csvColumn);
							}
							//NestPropertyにReferencePropertyEditorが指定されている場合は再帰
							if (nestProperty.getEditor() instanceof ReferencePropertyEditor) {
								ReferencePropertyEditor rpe = (ReferencePropertyEditor) nestProperty.getEditor();
								List<NestProperty> _nest = rpe.getNestProperties();
								addColumn(columnMap, nestPropName, nestProperty, _nest.toArray(new NestProperty[_nest.size()]));
							}
						}
					}
				}
			} else {
				//ネストがなければ参照自身を出力

				//参照の出力可否をチェック
				if (!getConditionSection().isNonOutputReference()) {
					EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);

					//OIDは出力可否をチェック
					if (!getConditionSection().isNonOutputOid()) {
						CsvColumn oidColumn = new CsvColumn(propertyName + "." + Entity.OID);
						oidColumn.setReferenceProperty(pd);
						oidColumn.setPropertyDefinition(red.getProperty(Entity.OID));
						oidColumn.setCsvItem(csvItem);
						//OIDの列名はここで取得
						oidColumn.setColumnLabel(getColumnLabel(oidColumn));
						columnMap.putIfAbsent(oidColumn.getPropertyName(), oidColumn);
					}
					CsvColumn csvColumn = new CsvColumn(propertyName + "." + Entity.NAME);
					csvColumn.setReferenceProperty(pd);
					csvColumn.setPropertyDefinition(red.getProperty(Entity.NAME));
					csvColumn.setCsvItem(csvItem);
					csvColumn.setColumnLabel(getColumnLabel(csvColumn));
					columnMap.putIfAbsent(csvColumn.getPropertyName(), csvColumn);
				}
			}
		} else if (pd instanceof BinaryProperty) {
			//バイナリは出力するかをチェック
			if (!getConditionSection().isNonOutputBinaryRef()) {
				CsvColumn csvColumn = new CsvColumn(propertyName);
				csvColumn.setPropertyDefinition(pd);
				csvColumn.setCsvItem(csvItem);
				csvColumn.setColumnLabel(getColumnLabel(csvColumn));
				columnMap.putIfAbsent(csvColumn.getPropertyName(), csvColumn);
			}
		} else {
			CsvColumn csvColumn = new CsvColumn(propertyName);
			csvColumn.setPropertyDefinition(pd);
			csvColumn.setCsvItem(csvItem);
			csvColumn.setColumnLabel(getColumnLabel(csvColumn));
			columnMap.putIfAbsent(csvColumn.getPropertyName(), csvColumn);
		}
	}

	/**
	 * プロパティ名に一致するCsvItemを返します。
	 * CSV出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param propertyName プロパティ名
	 * @return CsvItem
	 */
	private CsvItem getCsvItem(final String propertyName) {

		int firstDotIndex = propertyName.indexOf('.');
		if (firstDotIndex > -1) {
			//下位のネストあり
			String topPropName = propertyName.substring(0, firstDotIndex);
			String subPropName = propertyName.substring(firstDotIndex + 1);

			CsvItem property = getTopCsvItem(topPropName);
			if (property != null
					&& property.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) property.getEditor()).getNestProperties().isEmpty()) {
				//下位のネストのCsvItemを取得
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				CsvItem nestProperty = getNestCsvItem(editor.getNestProperties(), subPropName);
				if (nestProperty != null) return nestProperty;
			}
		}

		//Topレベルの定義からプロパティ名に一致するCsvItemを取得
		//ネスト項目の直接指定もここで取得される
		CsvItem property = getTopCsvItem(propertyName);
		return property;
	}

	/**
	 * プロパティ名に一致するTopレベルのCsvItemを返します。
	 * CSV出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param propName プロパティ名
	 * @return CsvItem
	 */
	private CsvItem getTopCsvItem(final String propertyName) {
		Optional<CsvItem> csvItem = getResultSection().getElements().stream()
				.filter(element -> element instanceof CsvItem)
				.map(element -> (CsvItem) element)
				.filter(element -> propertyName.equals(element.getPropertyName())).findFirst();
		if (csvItem.isPresent()) {
			return csvItem.get();
		}
		return null;
	}

	/**
	 * プロパティ名に一致するNestProperty(CsvItem)を返します。
	 * CSV出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param properties NestProperty
	 * @param propertyName プロパティ名
	 * @return CsvItem
	 */
	private CsvItem getNestCsvItem(final List<NestProperty> properties, final String propertyName) {
		int firstDotIndex = propertyName.indexOf('.');
		if (firstDotIndex > -1) {
			//下位のネストあり
			String topPropName = propertyName.substring(0, firstDotIndex);
			String subPropName = propertyName.substring(firstDotIndex + 1);

			NestProperty property = getNestProperty(properties, topPropName);
			if (property != null
					&& property.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) property.getEditor()).getNestProperties().isEmpty()) {
				//下位のネストのCsvItemを取得
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				return getNestCsvItem(editor.getNestProperties(), subPropName);
			}
		} else {
			//プロパティ名が一致するネスト(CsvItem)を取得
			NestProperty property = getNestProperty(properties, propertyName);
			if (property != null) {
				return property;
			}
		}

		return null;
	}

	/**
	 * NestPropertyからプロパティ名に一致するNestPropertyを返します。
	 *
	 * @param properties NestProperty
	 * @param propertyName プロパティ名
	 * @return NestProperty
	 */
	private NestProperty getNestProperty(List<NestProperty> properties, String propName) {
		Optional<NestProperty> nest = properties.stream()
			.filter(property -> propName.equals(property.getPropertyName()))
			.findFirst();
		if (nest.isPresent()) {
			return nest.get();
		} else {
			return null;
		}
	}

	/**
	 * 出力列ラベルを返します。
	 * アップロード形式のラベル出力の場合に利用します。
	 *
	 * @param propertyName プロパティ名
	 * @return 出力列ラベル
	 */
	public String getColumnLabel(String propertyName) {
		CsvColumn csvColumn = getCsvColumn(propertyName);
		return getColumnLabel(csvColumn);
	}

	/**
	 * CsvColumnの状態から出力列ラベルを返します。
	 *
	 * @param csvColumn CsvColumn
	 * @return 出力列ラベル
	 */
	private String getColumnLabel(CsvColumn csvColumn) {

		if (csvColumn.getCsvItem() != null) {
			//画面定義からカラムの表示ラベル取得
			CsvItem csvItem = csvColumn.getCsvItem();
			if (!StringUtil.isEmpty(csvItem.getDisplayLabel())) {
				String displayLabel = TemplateUtil.getMultilingualString(csvItem.getDisplayLabel(), csvItem.getLocalizedDisplayLabelList());
				if (displayLabel != null) return displayLabel;
			}
		}

		//取れない場合はEntity定義から取得

		//参照プロパティが設定されている場合、OIDまたはNameを自動的に設定しているので、参照プロパティ名を出力
		if (csvColumn.getReferenceProperty() != null) {
			PropertyDefinition pd = csvColumn.getReferenceProperty();
			String referenceName = TemplateUtil.getMultilingualString(pd.getDisplayName(), pd.getLocalizedDisplayNameList());
			if (csvColumn.getPropertyDefinition() != null && csvColumn.getPropertyDefinition().getName().equals(Entity.OID)) {
				return referenceName + "(ID)";
			} else if (csvColumn.getPropertyDefinition() != null && csvColumn.getPropertyDefinition().getName().equals(Entity.NAME)) {
				return referenceName;
			}
		}

		if (csvColumn.getPropertyDefinition() != null) {
			PropertyDefinition pd = csvColumn.getPropertyDefinition();
			return TemplateUtil.getMultilingualString(pd.getDisplayName(), pd.getLocalizedDisplayNameList());
		}

		//どちらかが存在している想定
		throw new EntityCsvException("not found csv column name. name=" + csvColumn.getPropertyName());
	}

	/**
	 * 先頭に出力するOIDの列出力ラベルを返します。
	 *
	 * @return OIDの列出力ラベル
	 */
	private String getEntityIdColLabel() {
		String displayName = TemplateUtil.getMultilingualString(
				getEntityDefinition().getDisplayName(),
				getEntityDefinition().getLocalizedDisplayNameList());
		String viewTitle = TemplateUtil.getMultilingualString(
				getForm().getTitle(),
				getForm().getLocalizedTitleList());
		if(StringUtil.isNotBlank(viewTitle)) {
			displayName = viewTitle;
		}
		return displayName  + "(ID)";
	}

	/**
	 * SearchQueryInterrupterHandlerを返します。
	 *
	 * @return SearchQueryInterrupterHandler
	 */
	@Override
	public SearchQueryInterrupterHandler getSearchQueryInterrupterHandler() {
		if (isForUpload()) {
			SearchQueryInterrupterHandlerForUpload handler = null;
			if (getForm().isUseInterrupterForCsvDownload()) {
				handler = new SearchQueryInterrupterHandlerForUpload(getRequest(), context, createInterrupter(getForm().getInterrupterName()));
			} else {
				log.debug("not use search query interrupter, set default.");
				handler = new SearchQueryInterrupterHandlerForUpload(getRequest(), context, getDefaultSearchQueryInterrupter());
			}
			return handler;
		} else {
			if (getForm().isUseInterrupterForCsvDownload()) {
				return context.getSearchQueryInterrupterHandler();
			} else {
				//何もしないデフォルトInterrupter生成
				log.debug("not use search query interrupter, set default.");
				return new SearchQueryInterrupterHandler(getRequest(), context, getDefaultSearchQueryInterrupter());
			}
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
			SearchFormView formView = getForm();
			SearchQueryContext ret = super.getInterrupter().beforeSearch(super.getRequest(), formView, query, type);
			query.setLocalized(false);
			return ret;
		}

		@Override
		public void afterSearch(Query query, Entity entity, SearchQueryType type) {
			SearchFormView formView = getForm();
			super.getInterrupter().afterSearch(super.getRequest(), formView, query, entity, type);
		}
	}

	/**
	 * CSV列情報
	 */
	public static class CsvColumn {

		/** プロパティ名 */
		private String propertyName;

		/** 列出力ラベル */
		private String columnLabel;

		/** プロパティ(仮想プロパティの場合null) */
		private PropertyDefinition propertyDefinition;

		/** PropertyColumn、VirtualPropertyItemなど(EntityのOID出力ではnull) */
		private CsvItem csvItem;

		/** ReferencePropertyのOID、Name出力時の対象Reference定義 */
		private PropertyDefinition referenceProperty;

		/**
		 * コンストラクタ
		 *
		 * @param propertyName プロパティ名
		 */
		public CsvColumn(String propertyName) {
			this.propertyName = propertyName;
		}

		/**
		 * プロパティ名を返します。
		 *
		 * @return プロパティ名
		 */
		public String getPropertyName() {
			return propertyName;
		}

		/**
		 * 列ヘッダラベルを返します。
		 *
		 * @return 列ヘッダラベル
		 */
		public String getColumnLabel() {
			return columnLabel;
		}

		/**
		 * 列ヘッダラベルを設定します。
		 *
		 * @param columnLabel 列ヘッダラベル
		 */
		public void setColumnLabel(String columnLabel) {
			this.columnLabel = columnLabel;
		}

		/**
		 * プロパティ定義を返します。
		 * 仮想プロパティの場合、nullです。
		 *
		 * @return プロパティ定義
		 */
		public PropertyDefinition getPropertyDefinition() {
			return propertyDefinition;
		}

		/**
		 * プロパティ定義を設定します。
		 *
		 * @param propertyDefinition プロパティ定義
		 */
		public void setPropertyDefinition(PropertyDefinition propertyDefinition) {
			this.propertyDefinition = propertyDefinition;
		}

		/**
		 * CsvItem(PropertyColumn、VirtualPropertyItemなど)を返します。
		 * EntityのOID出力列の場合、または列の直接指定の場合はnullの可能性があります。
		 *
		 * @return
		 */
		public CsvItem getCsvItem() {
			return csvItem;
		}

		/**
		 * CsvItemを設定します。
		 * @param csvItem CsvItem
		 */
		public void setCsvItem(CsvItem csvItem) {
			this.csvItem = csvItem;
		}

		/**
		 * ReferencePropertyのOID、Name出力時の対象Reference定義を返します。
		 *
		 * @return Reference定義
		 */
		public PropertyDefinition getReferenceProperty() {
			return referenceProperty;
		}

		/**
		 * ReferencePropertyのOID、Name出力時の対象Reference定義を設定します。
		 *
		 * @param referenceProperty Reference定義
		 */
		public void setReferenceProperty(PropertyDefinition referenceProperty) {
			this.referenceProperty = referenceProperty;
		}

		/**
		 * Editorを返します。
		 * CsvItemが無い場合はnullを返します。
		 *
		 * @return Editor
		 */
		public PropertyEditor getEditor() {
			if (csvItem != null) {
				PropertyEditor editor = csvItem.getEditor();
				//WrapしているEditorは設定されているEditorを返す
				if (editor instanceof JoinPropertyEditor) {
					return ((JoinPropertyEditor)editor).getEditor();
				} else if (editor instanceof DateRangePropertyEditor) {
					return ((DateRangePropertyEditor)editor).getEditor();
				} else {
					return editor;
				}
			} else {
				return null;
			}
		}

		/**
		 * 多重度を返します。
		 * プロパティ定義がない場合（仮想プロパティ）、ReferencePropertyの場合は、1を返します。
		 *
		 * @return 多重度
		 */
		public int getMultiplicity() {
			if (propertyDefinition != null
					&& !(propertyDefinition instanceof ReferenceProperty)) {
				return propertyDefinition.getMultiplicity();
			} else {
				//仮想プロパティまたはReferenceProperty
				return 1;
			}
		}

		@Override
		public String toString( ) {
			StringBuilder builder = new StringBuilder();
			builder.append("CsvColumn [");
			builder.append("propertyName=");
			builder.append(propertyName);
			builder.append(", columnName=");
			builder.append(columnLabel);
			builder.append(", multiplicity=");
			builder.append(getMultiplicity());
			builder.append(", property=");
			if (propertyDefinition != null) {
				builder.append(propertyDefinition.getClass().getSimpleName());
			} else {
				builder.append("null");
			}
			builder.append(", csvItem=");
			if (csvItem != null) {
				builder.append(csvItem.getClass().getSimpleName());
			} else {
				builder.append("null");
			}
			builder.append(", editor=");
			builder.append(getEditor());
			builder.append(", referenceProperty=");
			if (referenceProperty != null) {
				builder.append(referenceProperty.getDisplayName());
			} else {
				builder.append("null");
			}
			builder.append("]");
			return builder.toString();
		}

	}
}
