/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.search.fileport.CsvFileDownloadSearchContext;
import org.iplass.gem.command.generic.search.fileport.ExcelFileDownloadSearchContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
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
import org.iplass.mtp.impl.entity.fileport.EntityCsvException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchFormCsvUploadInterrupter;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.FileItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.FileSupportType;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.web.ResultStreamWriter;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EntityFileDownloadSearchContext extends SearchContextBase {

	private static Logger log = LoggerFactory.getLogger(EntityFileDownloadSearchContext.class);

	private static final String DEFAULT_CHAR_SET = "UTF-8";

	protected final SearchContextBase context;

	private Map<String, String> customColumnNameMap;

	/**
	 * ファイルダウンロード用のコンテキストを返します。
	 *
	 * @param context 検索コンテキスト
	 * @return ファイルダウンロード用のコンテキスト
	 */
	public static EntityFileDownloadSearchContext getContext(SearchContextBase context) {
		FileSupportType fileSupportType = context.getConditionSection().getFileSupportType();

		// 未指定の場合は、GemConfigServiceから取得
		if (fileSupportType == null) {
			GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
			fileSupportType = gemConfigService.getFileSupportType();
		}

		if (fileSupportType == FileSupportType.SPECIFY) {
			String specifyType = context.getRequest().getParam(Constants.FILE_SUPPORT_TYPE);
			if (FileSupportType.CSV.name().equals(specifyType)) {
				fileSupportType = FileSupportType.CSV;
			} else if (FileSupportType.EXCEL.name().equals(specifyType)) {
				fileSupportType = FileSupportType.EXCEL;
			} else {
                fileSupportType = FileSupportType.CSV;
            }
		}

		if (fileSupportType == FileSupportType.CSV) {
			return new CsvFileDownloadSearchContext(context);
		} else if (fileSupportType == FileSupportType.EXCEL) {
			return new ExcelFileDownloadSearchContext(context);
		} else {
			log.error("Unsupported file support type: " + fileSupportType);
			throw new ApplicationException(GemResourceBundleUtil.resourceString("command.generic.search.EntityFileDownloadSearchContext.internalErr"));
        }
	}

	protected EntityFileDownloadSearchContext(SearchContextBase context) {
		this.context = context;
	}

	/**
	 * ファイルのコンテントタイプを返します。
	 *
	 * @return ファイルのコンテントタイプ
	 */
	public abstract String getFileContentType();

	/**
	 * ファイルの拡張子を返します。
	 *
	 * @return ファイルの拡張子
	 */
	public abstract String getFileExtension();

	/**
     * ファイル出力用のWriterを生成します。
     *
     * @return ファイル出力用のWriter
     */
	public abstract ResultStreamWriter createWriter();

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
	public String getViewName() {
		return context.getViewName();
	}

	@Override
	public Select getSelect() {
		if (isOutputSpecifyProperties() && !isOutputResult()) {
			boolean distinct = getConditionSection().isDistinct();
			Set<String> properties = getConditionSection().getCsvdownloadPropertiesSet();
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
		return Boolean.valueOf(getRequest().getParam(Constants.FILE_IS_FOR_UPLOAD));
	}

	public boolean isNoDispName() {
		return getConditionSection().isNonOutputDisplayName()
				|| Boolean.valueOf(getRequest().getParam(Constants.FILE_IS_NO_DISP_NAME));
	}

	public boolean isOutputResult() {
		return Boolean.valueOf(getRequest().getParam(Constants.FILE_IS_OUTPUT_RESULT));
	}

	public boolean isOutputCodeValue() {
		return Boolean.valueOf(getRequest().getParam(Constants.FILE_IS_OUTPUT_CODE_VALUE));
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
	public String getCsvCharacterCode() {
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
	public List<FileColumn> getFileColumns() {

		if (isOutputSpecifyProperties() && !isOutputResult()) {
			//出力列を直接指定の場合
			ArrayList<FileColumn> columns = new ArrayList<>();

			Set<String> propertyNames = getConditionSection().getCsvdownloadPropertiesSet();
			for (String propertyName : propertyNames) {
				FileColumn column = getFileColumn(propertyName);
				if (column != null) {
					columns.add(column);
				}
			}
			return columns;

		} else {
			//検索結果設定の場合
			LinkedHashMap<String, FileColumn> columnMap = new LinkedHashMap<>();

			SearchConditionSection section = getConditionSection();
			//OIDを出力する場合、先頭に出力
			if (!section.isNonOutputOid()) {
				PropertyDefinition pd = getPropertyDefinition(Entity.OID);
				FileColumn fileColumn = new FileColumn(Entity.OID);
				fileColumn.setPropertyDefinition(pd);
				fileColumn.setFileItem(null);
				fileColumn.setColumnLabel(getEntityIdColLabel());
				columnMap.put(fileColumn.getPropertyName(), fileColumn);
			}

			//FileItemでかつ出力対象を取得
			List<FileItem> fileItems = getResultSection().getElements().stream()
					.filter(element -> {
						return EntityViewUtil.isDisplayElement(
								getDefName(), element.getElementRuntimeId(), OutputType.SEARCHRESULT, null);
					})
					.filter(element -> {
						return (element instanceof FileItem) && ((FileItem)element).isOutputCsv();
					})
					.map(e -> (FileItem) e)
					.collect(Collectors.toList());

			for (FileItem fileItem : fileItems) {

				String propertyName = fileItem.getPropertyName();
				if (fileItem.getEditor() instanceof ReferencePropertyEditor) {
					//ReferencePropertyEditorの場合、Nestがあるかで設定
					List<NestProperty> nest = ((ReferencePropertyEditor)fileItem.getEditor()).getNestProperties();
					addColumn(columnMap, propertyName, fileItem, nest.toArray(new NestProperty[nest.size()]));
				} else if (fileItem.getEditor() instanceof JoinPropertyEditor) {
					//JoinPropertyEditorの場合、自身を追加
					addColumn(columnMap, propertyName, fileItem);

					//NestPropertyが存在する場合は、それぞれを追加
					JoinPropertyEditor je = (JoinPropertyEditor) fileItem.getEditor();
					for (NestProperty nest : je.getProperties()) {
						if (!nest.isOutputCsv()) {
							continue;
						}
						addColumn(columnMap, nest.getPropertyName(), nest);
					}
				} else {
					//上記以外は自身を追加
					addColumn(columnMap, propertyName, fileItem);
				}
			}
			return new ArrayList<>(columnMap.values());
		}
	}

	/**
	 * 出力列情報を返します。
	 * 出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param propertyName プロパティ名
	 * @return 出力列情報
	 */
	public FileColumn getFileColumn(String propertyName) {

		FileColumn column = null;
		PropertyDefinition pd = getPropertyDefinition(propertyName);
		if (pd != null) {
			//プロパティとして存在していればOK
			if (pd instanceof ReferenceProperty) {
				// 参照プロパティ自体が設定された場合はname出力
				column = new FileColumn(propertyName + "." + Entity.NAME);
				column.setReferenceProperty(pd);
				EntityDefinition red = getReferenceEntityDefinition((ReferenceProperty) pd);
				column.setPropertyDefinition(red.getProperty(Entity.NAME));
			} else {
				column = new FileColumn(propertyName);
				column.setPropertyDefinition(pd);
			}
			//プロパティ名からFileItemを取得
			FileItem fileItem = getFileItem(propertyName);
			column.setFileItem(fileItem);
		} else {
			//仮想プロパティとして有効かチェックするため、propertyNameからFileItem定義を取得
			FileItem fileItem = getFileItem(propertyName);
			if (fileItem != null) {
				//PropertyDefinitionはnullになる
				column = new FileColumn(propertyName);
				column.setFileItem(fileItem);
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
	 * FileItemに関する列情報を追加します。
	 *
	 * @param columnMap 列情報格納用Map
	 * @param propertyName プロパティ名
	 * @param fileItem 対象FileItem
	 * @param nestProperties ReferencePropertyEditorのNestProperty
	 */
	private void addColumn(LinkedHashMap<String, FileColumn> columnMap, String propertyName, FileItem fileItem, NestProperty... nestProperties) {

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
								FileColumn fileColumn = new FileColumn(propertyName + "." + nestProperty.getPropertyName());
								fileColumn.setPropertyDefinition(rpd);
								fileColumn.setFileItem(nestProperty);
								fileColumn.setColumnLabel(getColumnLabel(fileColumn));
								columnMap.putIfAbsent(fileColumn.getPropertyName(), fileColumn);
							}
						} else {
							String nestPropName = propertyName + "." + nestProperty.getPropertyName();
							if (!(rpd instanceof ReferenceProperty)) {
								//参照以外は項目に追加、参照の場合はネストの項目で判断
								FileColumn fileColumn = new FileColumn(nestPropName);
								fileColumn.setPropertyDefinition(rpd);
								fileColumn.setFileItem(nestProperty);
								fileColumn.setColumnLabel(getColumnLabel(fileColumn));
								columnMap.putIfAbsent(fileColumn.getPropertyName(), fileColumn);
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
						FileColumn oidColumn = new FileColumn(propertyName + "." + Entity.OID);
						oidColumn.setReferenceProperty(pd);
						oidColumn.setPropertyDefinition(red.getProperty(Entity.OID));
						oidColumn.setFileItem(fileItem);
						//OIDの列名はここで取得
						oidColumn.setColumnLabel(getColumnLabel(oidColumn));
						columnMap.putIfAbsent(oidColumn.getPropertyName(), oidColumn);
					}

					GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
					String displayLabelItem = ((ReferencePropertyEditor)fileItem.getEditor()).getDisplayLabelItem();

					FileColumn fileColumn = null;
					if (service.isUseDisplayLabelItemInCsvDownload() && StringUtil.isNotBlank(displayLabelItem)) {
						//表示ラベルとして扱うプロパティが設定されたら表示ラベルを利用する
						fileColumn = new FileColumn(propertyName + "." + displayLabelItem);
						fileColumn.setPropertyDefinition(red.getProperty(displayLabelItem));
					} else {
						fileColumn = new FileColumn(propertyName + "." + Entity.NAME);
						fileColumn.setPropertyDefinition(red.getProperty(Entity.NAME));
					}
					fileColumn.setReferenceProperty(pd);
					fileColumn.setFileItem(fileItem);
					fileColumn.setColumnLabel(getColumnLabel(fileColumn));
					columnMap.putIfAbsent(fileColumn.getPropertyName(), fileColumn);
				}
			}
		} else if (pd instanceof BinaryProperty) {
			//バイナリは出力するかをチェック
			if (!getConditionSection().isNonOutputBinaryRef()) {
				FileColumn fileColumn = new FileColumn(propertyName);
				fileColumn.setPropertyDefinition(pd);
				fileColumn.setFileItem(fileItem);
				fileColumn.setColumnLabel(getColumnLabel(fileColumn));
				columnMap.putIfAbsent(fileColumn.getPropertyName(), fileColumn);
			}
		} else {
			FileColumn fileColumn = new FileColumn(propertyName);
			fileColumn.setPropertyDefinition(pd);
			fileColumn.setFileItem(fileItem);
			fileColumn.setColumnLabel(getColumnLabel(fileColumn));
			columnMap.putIfAbsent(fileColumn.getPropertyName(), fileColumn);
		}
	}

	/**
	 * プロパティ名に一致するFileItemを返します。
	 * 出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param propertyName プロパティ名
	 * @return FileItem
	 */
	private FileItem getFileItem(final String propertyName) {

		int firstDotIndex = propertyName.indexOf('.');
		if (firstDotIndex > -1) {
			//下位のネストあり
			String topPropName = propertyName.substring(0, firstDotIndex);
			String subPropName = propertyName.substring(firstDotIndex + 1);

			FileItem property = getTopFileItem(topPropName);
			if (property != null
					&& property.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) property.getEditor()).getNestProperties().isEmpty()) {
				//下位のネストのFileItemを取得
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				FileItem nestProperty = getNestFileItem(editor.getNestProperties(), subPropName);
				if (nestProperty != null) {
					return nestProperty;
				}
			}
		}

		//Topレベルの定義からプロパティ名に一致するFileItemを取得
		//ネスト項目の直接指定もここで取得される
		FileItem property = getTopFileItem(propertyName);
		return property;
	}

	/**
	 * プロパティ名に一致するTopレベルのFileItemを返します。
	 * 出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param propName プロパティ名
	 * @return FileItem
	 */
	private FileItem getTopFileItem(final String propertyName) {
		Optional<FileItem> fileItem = getResultSection().getElements().stream()
				.filter(element -> element instanceof FileItem)
				.map(element -> (FileItem) element)
				.filter(element -> propertyName.equals(element.getPropertyName())).findFirst();
		if (fileItem.isPresent()) {
			return fileItem.get();
		}
		return null;
	}

	/**
	 * プロパティ名に一致するNestProperty(FileItem)を返します。
	 * 出力項目の直接指定、アップロード形式の場合に利用します。
	 *
	 * @param properties NestProperty
	 * @param propertyName プロパティ名
	 * @return FileItem
	 */
	private FileItem getNestFileItem(final List<NestProperty> properties, final String propertyName) {
		int firstDotIndex = propertyName.indexOf('.');
		if (firstDotIndex > -1) {
			//下位のネストあり
			String topPropName = propertyName.substring(0, firstDotIndex);
			String subPropName = propertyName.substring(firstDotIndex + 1);

			NestProperty property = getNestProperty(properties, topPropName);
			if (property != null
					&& property.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) property.getEditor()).getNestProperties().isEmpty()) {
				//下位のネストのFileItemを取得
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				return getNestFileItem(editor.getNestProperties(), subPropName);
			}
		} else {
			//プロパティ名が一致するネスト(FileItem)を取得
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
	private NestProperty getNestProperty(List<NestProperty> properties, String propertyName) {
		Optional<NestProperty> nest = properties.stream()
			.filter(property -> propertyName.equals(property.getPropertyName()))
			.findFirst();
		if (nest.isPresent()) {
			return nest.get();
		} else {
			return null;
		}
	}

	/**
	 * 出力列名を返します。
	 * アップロード形式の場合に利用します。
	 *
	 * @param property プロパティ定義
	 * @return 出力列名
	 */
	public String getColumnName(PropertyDefinition property) {

		// 出力列名のカスタマイズチェック
		String columnName = getCustomColumnNameMap().get(property.getName());
		if (StringUtil.isNotEmpty(columnName)) {
			return columnName;
		}

		if (isNoDispName()) {
			return property.getName();
		} else {
			return property.getName() + "(" + getColumnLabel(property.getName()) + ")";
		}
	}

	/**
	 * 多重度複数プロパティの出力列名を返します。
	 * アップロード形式の場合に利用します。
	 *
	 * @param property プロパティ定義
	 * @param index 多重度のIndex
	 * @return 出力列名
	 */
	public String getMultipleColumnName(PropertyDefinition property, int index) {

		// 出力列名のカスタマイズチェック
		String columnName = getCustomColumnNameMap().get(property.getName());
		if (StringUtil.isNotEmpty(columnName)) {
			return columnName + "[" + index + "]";
		}

		if (isNoDispName()) {
			return property.getName() + "[" + index + "]";
		} else {
			return property.getName() + "[" + index + "]" + "(" + getColumnLabel(property.getName()) + ")";
		}
	}

	/**
	 * 出力列ラベルを返します。
	 * アップロード形式のラベル出力の場合に利用します。
	 *
	 * @param propertyName プロパティ名
	 * @return 出力列ラベル
	 */
	private String getColumnLabel(String propertyName) {
		FileColumn fileColumn = getFileColumn(propertyName);
		return getColumnLabel(fileColumn);
	}

	/**
	 * FileColumnの状態から出力列ラベルを返します。
	 *
	 * @param fileColumn FileColumn
	 * @return 出力列ラベル
	 */
	private String getColumnLabel(FileColumn fileColumn) {

		if (fileColumn.getFileItem() != null) {
			//画面定義からカラムの表示ラベル取得
			FileItem fileItem = fileColumn.getFileItem();
			if (!StringUtil.isEmpty(fileItem.getDisplayLabel())) {
				String displayLabel = TemplateUtil.getMultilingualString(fileItem.getDisplayLabel(), fileItem.getLocalizedDisplayLabelList());
				if (displayLabel != null) {
					if (fileColumn.getReferenceProperty() != null && fileColumn.getPropertyDefinition().getName().equals(Entity.OID)) {
						return displayLabel + "(ID)";
					} else {
						return displayLabel;
					}
				}
			}
		}

		//取れない場合はEntity定義から取得

		//参照プロパティが設定されている場合、参照プロパティの表示名を出力
		if (fileColumn.getReferenceProperty() != null) {
			PropertyDefinition pd = fileColumn.getReferenceProperty();
			String referenceName = TemplateUtil.getMultilingualString(pd.getDisplayName(), pd.getLocalizedDisplayNameList());
			if (fileColumn.getPropertyDefinition() != null && fileColumn.getPropertyDefinition().getName().equals(Entity.OID)) {
				return referenceName + "(ID)";
			} else {
				return referenceName;
			}
		}

		if (fileColumn.getPropertyDefinition() != null) {
			PropertyDefinition pd = fileColumn.getPropertyDefinition();
			return TemplateUtil.getMultilingualString(pd.getDisplayName(), pd.getLocalizedDisplayNameList());
		}

		//どちらかが存在している想定
		throw new EntityCsvException("not found file column name. name=" + fileColumn.getPropertyName());
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

	/**
	 * プロパティ名に対する出力CSV列名のマッピング定義を返します。
	 *
	 * @return プロパティ名に対する出力CSV列名のマッピング定義
	 */
	private Map<String, String> getCustomColumnNameMap() {

		if (customColumnNameMap != null) {
			return customColumnNameMap;
		}

		SearchFormCsvUploadInterrupter csvUploadInterrupter = null;
		if (getConditionSection() != null && StringUtil.isNotEmpty(getConditionSection().getCsvUploadInterrupterName())) {
			UtilityClassDefinitionManager ucdm = ManagerLocator.getInstance().getManager(UtilityClassDefinitionManager.class);
			try {
				csvUploadInterrupter = ucdm.createInstanceAs(SearchFormCsvUploadInterrupter.class, getConditionSection().getCsvUploadInterrupterName());
			} catch (ClassNotFoundException e) {
				log.error(getConditionSection().getCsvUploadInterrupterName() + " can not instantiate.", e);
				throw new ApplicationException(GemResourceBundleUtil.resourceString("command.generic.search.EntityFileDownloadSearchContext.internalErr"));
			}
		} else {
			csvUploadInterrupter = new SearchFormCsvUploadInterrupter() {};
		}

		customColumnNameMap = csvUploadInterrupter.columnNameMap(getEntityDefinition());

		// 再作成しないように初期化
		if (customColumnNameMap == null) {
			customColumnNameMap = Collections.emptyMap();
		}
		return customColumnNameMap;
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
	 * 列情報
	 */
	public static class FileColumn {

		/** プロパティ名 */
		private String propertyName;

		/** 列出力ラベル */
		private String columnLabel;

		/** プロパティ(仮想プロパティの場合null) */
		private PropertyDefinition propertyDefinition;

		/** PropertyColumn、VirtualPropertyItemなど(EntityのOID出力ではnull) */
		private FileItem fileItem;

		/** ReferencePropertyのOID、Name出力時の対象Reference定義 */
		private PropertyDefinition referenceProperty;

		/**
		 * コンストラクタ
		 *
		 * @param propertyName プロパティ名
		 */
		public FileColumn(String propertyName) {
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
		 * FileItem(PropertyColumn、VirtualPropertyItemなど)を返します。
		 * EntityのOID出力列の場合、または列の直接指定の場合はnullの可能性があります。
		 *
		 * @return
		 */
		public FileItem getFileItem() {
			return fileItem;
		}

		/**
		 * FileItemを設定します。
		 * @param fileItem FileItem
		 */
		public void setFileItem(FileItem fileItem) {
			this.fileItem = fileItem;
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
		 * FileItemが無い場合はnullを返します。
		 *
		 * @return Editor
		 */
		public PropertyEditor getEditor() {
			if (fileItem != null) {
				PropertyEditor editor = fileItem.getEditor();
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
			builder.append("FileColumn [");
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
			builder.append(", fileItem=");
			if (fileItem != null) {
				builder.append(fileItem.getClass().getSimpleName());
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
