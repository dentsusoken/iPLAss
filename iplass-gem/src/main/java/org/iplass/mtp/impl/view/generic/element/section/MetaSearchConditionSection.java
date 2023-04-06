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

package org.iplass.mtp.impl.view.generic.element.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iplass.mtp.entity.csv.MultipleFormat;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;
import org.iplass.mtp.impl.view.generic.element.MetaElement;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyItem;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.FilterSetting;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.CsvDownloadSpecifyCharacterCode;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.CsvUploadTransactionType;
import org.iplass.mtp.view.generic.element.section.SortSetting;

/**
 * 検索条件セクションのメタデータ
 * @author lis3wg
 */
public class MetaSearchConditionSection extends MetaSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9083146736532796083L;

	public static MetaSearchConditionSection createInstance(Element element) {
		return new MetaSearchConditionSection();
	}

	/** 詳細条件の表示件数 */
	private int conditionDispCount;

	/** 列数 */
	private int colNum;

	/** 検索時にソートしないか */
	private boolean unsorted;

	/** 全文検索時にソートするか */
	private boolean fulltextSearchSorted;

	/** 重複行をまとめるか */
	private boolean distinct;

	/** 詳細検索非表示設定 */
	private boolean hideDetailCondition;

	/** 定型検索非表示設定 */
	private boolean hideFixedCondition;

	/** CSVアップロードボタン非表示設定 */
	private boolean hideCsvUpload;

	/** CSVアップロードで登録を許可しない */
	private boolean csvUploadDenyInsert;

	/** CSVアップロードで更新を許可しない */
	private boolean csvUploadDenyUpdate;

	/** CSVアップロードで削除を許可しない */
	private boolean csvUploadDenyDelete;

	/** CSVアップロード登録項目 */
	private String csvUploadInsertProperties;

	/** CSVアップロード更新項目 */
	private String csvUploadUpdateProperties;

	/** CSVアップロード時のトランザクション制御設定 */
	private CsvUploadTransactionType csvUploadTransactionType = CsvUploadTransactionType.ONCE;

	/** カスタムCSVアップロード処理クラス名 */
	private String csvUploadInterrupterName;

	/** CSVダウンロードボタン非表示設定 */
	private boolean hideCsvdownload;

	/** CSVダウンロードダイアログ非表示設定 */
	private boolean hideCsvdownloadDialog;

	/** CSVダウンロード文字コード指定可否設定 */
	private CsvDownloadSpecifyCharacterCode specifyCharacterCode = CsvDownloadSpecifyCharacterCode.NONE;

	/** CSVダウンロード時oid出力設定 */
	private boolean nonOutputOid;

	/** CSVダウンロード時reference出力設定 */
	private boolean nonOutputReference;

	/** CSVダウンロード時BinaryReference出力設定 */
	private boolean nonOutputBinaryRef;

	/** CSVダウンロード件数の上限値 */
	private Integer csvdownloadMaxCount;

	/** CSVダウンロード項目 */
	private String csvdownloadProperties;

	/** Upload形式のCSVダウンロード項目 */
	private String csvdownloadUploadableProperties;

	/** CSVダウンロードファイル名Format(GroovyTemplate) */
	private String csvdownloadFileNameFormat;

	/** 多重度プロパティのCSV出力フォーマット */
	private MultipleFormat csvMultipleFormat = MultipleFormat.EACH_COLUMN;

	/** デフォルト検索条件 */
	private String defaultCondition;

	/** デフォルト検索条件をフィルタ定義と一緒に利用するか */
	private boolean useDefaultConditionWithFilterDefinition;

	/** デフォルトプロパティ条件設定スクリプト */
	private String defaultPropertyConditionScript;

	/** ソート設定 */
	private List<MetaSortSetting> sortSetting;

	/** フィルタ設定 */
	private List<MetaFilterSetting> filterSetting;

	/** 要素 */
	private List<MetaElement> elements;

	/** スクリプトのキー(内部用) */
	private String scriptKey;


	public Integer getCsvdownloadMaxCount() {
		return csvdownloadMaxCount;
	}

	public void setCsvdownloadMaxCount(Integer csvdownloadMaxCount) {
		this.csvdownloadMaxCount = csvdownloadMaxCount;
	}

	public boolean isHideCsvdownloadDialog() {
		return hideCsvdownloadDialog;
	}

	public void setHideCsvdownloadDialog(boolean hideCsvdownloadDialog) {
		this.hideCsvdownloadDialog = hideCsvdownloadDialog;
	}

	public CsvDownloadSpecifyCharacterCode isSpecifyCharacterCode() {
		return specifyCharacterCode;
	}

	public void setSpecifyCharacterCode(CsvDownloadSpecifyCharacterCode specifyCharacterCode) {
		this.specifyCharacterCode = specifyCharacterCode;
	}

	public CsvUploadTransactionType getCsvUploadTransactionType() {
		return csvUploadTransactionType;
	}

	public void setCsvUploadTransactionType(CsvUploadTransactionType csvUploadTransactionType) {
		this.csvUploadTransactionType = csvUploadTransactionType;
	}

	/**
	 * カスタムCSVアップロード処理クラス名を取得します。
	 * @return カスタムCSVアップロード処理クラス名
	 */
	public String getCsvUploadInterrupterName() {
		return csvUploadInterrupterName;
	}

	/**
	 * カスタムCSVアップロード処理クラス名を設定します。
	 * @param csvUploadInterrupterName カスタムCSVアップロード処理クラス名
	 */
	public void setCsvUploadInterrupterName(String csvUploadInterrupterName) {
		this.csvUploadInterrupterName = csvUploadInterrupterName;
	}

	public boolean isHideCsvUpload() {
		return hideCsvUpload;
	}

	public void setHideCsvUpload(boolean hideCsvUpload) {
		this.hideCsvUpload = hideCsvUpload;
	}

	public boolean isCsvUploadDenyInsert() {
		return csvUploadDenyInsert;
	}

	public void setCsvUploadDenyInsert(boolean csvUploadDenyInsert) {
		this.csvUploadDenyInsert = csvUploadDenyInsert;
	}

	public boolean isCsvUploadDenyUpdate() {
		return csvUploadDenyUpdate;
	}

	public void setCsvUploadDenyUpdate(boolean csvUploadDenyUpdate) {
		this.csvUploadDenyUpdate = csvUploadDenyUpdate;
	}

	public boolean isCsvUploadDenyDelete() {
		return csvUploadDenyDelete;
	}

	public void setCsvUploadDenyDelete(boolean csvUploadDenyDelete) {
		this.csvUploadDenyDelete = csvUploadDenyDelete;
	}

	public String getCsvUploadInsertProperties() {
		return csvUploadInsertProperties;
	}

	public void setCsvUploadInsertProperties(String csvUploadInsertProperties) {
		this.csvUploadInsertProperties = csvUploadInsertProperties;
	}

	public String getCsvUploadUpdateProperties() {
		return csvUploadUpdateProperties;
	}

	public void setCsvUploadUpdateProperties(String csvUploadUpdateProperties) {
		this.csvUploadUpdateProperties = csvUploadUpdateProperties;
	}

	public boolean isNonOutputOid() {
		return nonOutputOid;
	}

	public void setNonOutputOid(boolean nonOutputOid) {
		this.nonOutputOid = nonOutputOid;
	}

	public boolean isNonOutputReference() {
		return nonOutputReference;
	}

	public void setNonOutputReference(boolean nonOutputReference) {
		this.nonOutputReference = nonOutputReference;
	}

	public boolean isNonOutputBinaryRef() {
		return nonOutputBinaryRef;
	}

	public void setNonOutputBinaryRef(boolean nonOutputBinaryRef) {
		this.nonOutputBinaryRef = nonOutputBinaryRef;
	}

	/**
	 * 詳細条件の表示件数を取得します。
	 * @return 詳細条件の表示件数
	 */
	public int getConditionDispCount() {
		return conditionDispCount;
	}

	/**
	 * 詳細条件の表示件数を設定します。
	 * @param conditionDispCount 詳細条件の表示件数
	 */
	public void setConditionDispCount(int conditionDispCount) {
		this.conditionDispCount = conditionDispCount;
	}

	/**
	 * 列数を取得します。
	 * @return 列数
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * 列数を設定します。
	 * @param colNum 列数
	 */
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	/**
	 * 検索時にソートしないかを取得します。
	 * @return 検索時にソートしないか
	 */
	public boolean isUnsorted() {
		return unsorted;
	}

	/**
	 * 検索時にソートしないかを設定します。
	 * @param unsorted 検索時にソートしないか
	 */
	public void setUnsorted(boolean unsorted) {
		this.unsorted = unsorted;
	}

	/**
	 * 全文検索時にソートするかを取得します。
	 * @return 全文検索時にソートするか
	 */
	public boolean isFulltextSearchSorted() {
		return fulltextSearchSorted;
	}

	/**
	 * 全文検索時にソートするかを設定します。
	 * @param fulltextSearchSorted 全文検索時にソートするか
	 */
	public void setFulltextSearchSorted(boolean fulltextSearchSorted) {
		this.fulltextSearchSorted = fulltextSearchSorted;
	}

	/**
	 * 重複行をまとめるかを取得します。
	 * @return 重複行をまとめるか
	 */
	public boolean isDistinct() {
	    return distinct;
	}

	/**
	 * 重複行をまとめるかを設定します。
	 * @param distinct 重複行をまとめるか
	 */
	public void setDistinct(boolean distinct) {
	    this.distinct = distinct;
	}

	/**
	 * 詳細検索非表示設定を取得します。
	 * @return 詳細検索非表示設定
	 */
	public boolean isHideDetailCondition() {
		return hideDetailCondition;
	}

	/**
	 * 詳細検索非表示設定を設定します。
	 * @param hideDetailCondition 詳細検索非表示設定
	 */
	public void setHideDetailCondition(boolean hideDetailCondition) {
		this.hideDetailCondition = hideDetailCondition;
	}

	/**
	 * 定型検索非表示設定を取得します。
	 * @return 定型検索非表示設定
	 */
	public boolean isHideFixedCondition() {
		return hideFixedCondition;
	}

	/**
	 * 定型検索非表示設定を設定します。
	 * @param hideFixedCondition 定型検索非表示設定
	 */
	public void setHideFixedCondition(boolean hideFixedCondition) {
		this.hideFixedCondition = hideFixedCondition;
	}

	/**
	 * CSVダウンロードボタン非表示設定を取得します。
	 * @return CSVダウンロードボタン非表示設定
	 */
	public boolean isHideCsvdownload() {
		return hideCsvdownload;
	}

	/**
	 * CSVダウンロードボタン非表示設定を設定します。
	 * @param hideCsvdownload CSVダウンロードボタン非表示設定
	 */
	public void setHideCsvdownload(boolean hideCsvdownload) {
		this.hideCsvdownload = hideCsvdownload;
	}

	/**
	 * CSVダウンロード項目を取得します。
	 * @return CSVダウンロード項目
	 */
	public String getCsvdownloadProperties() {
	    return csvdownloadProperties;
	}

	/**
	 * CSVダウンロード項目を設定します。
	 * @param csvdownloadProperties CSVダウンロード項目
	 */
	public void setCsvdownloadProperties(String csvdownloadProperties) {
	    this.csvdownloadProperties = csvdownloadProperties;
	}

	/**
	 * Upload形式のCSVダウンロード項目を取得します。
	 * @return Upload形式のCSVダウンロード項目
	 */
	public String getCsvdownloadUploadableProperties() {
	    return csvdownloadUploadableProperties;
	}

	/**
	 * Upload形式のCSVダウンロード項目を設定します。
	 * @param csvdownloadUploadableProperties Upload形式のCSVダウンロード項目
	 */
	public void setCsvdownloadUploadableProperties(String csvdownloadUploadableProperties) {
	    this.csvdownloadUploadableProperties = csvdownloadUploadableProperties;
	}

	/**
	 * CSVダウンロードファイル名Format(GroovyTemplate)を取得します。
	 * @return CSVダウンロードファイル名Format(GroovyTemplate)
	 */
	public String getCsvdownloadFileNameFormat() {
		return csvdownloadFileNameFormat;
	}

	/**
	 * CSVダウンロードファイル名Format(GroovyTemplate)を設定します。
	 * @param csvdownloadFileNameFormat CSVダウンロードファイル名Format(GroovyTemplate)
	 */
	public void setCsvdownloadFileNameFormat(String csvdownloadFileNameFormat) {
		this.csvdownloadFileNameFormat = csvdownloadFileNameFormat;
	}

	/**
	 * 多重度プロパティのCSV出力フォーマットを取得します。
	 * @return 多重度プロパティのCSV出力フォーマット
	 */
	public MultipleFormat getCsvMultipleFormat() {
		return csvMultipleFormat;
	}

	/**
	 * 多重度プロパティのCSV出力フォーマットを設定します。
	 * @param csvMultipleFormat 多重度プロパティのCSV出力フォーマット
	 */
	public void setCsvMultipleFormat(MultipleFormat csvMultipleFormat) {
		this.csvMultipleFormat = csvMultipleFormat;
	}

	/**
	 * デフォルト検索条件を取得します。
	 * @return デフォルト検索条件
	 */
	public String getDefaultCondition() {
	    return defaultCondition;
	}

	/**
	 * デフォルト検索条件を設定します。
	 * @param defaultFilter デフォルト検索条件
	 */
	public void setDefaultCondition(String defaultFilter) {
	    this.defaultCondition = defaultFilter;
	}

	/**
	 * デフォルト検索条件をフィルタ定義と一緒に利用するかを取得します。
	 * @return デフォルト検索条件をフィルタ定義と一緒に利用するか
	 */
	public boolean isUseDefaultConditionWithFilterDefinition() {
	    return useDefaultConditionWithFilterDefinition;
	}

	/**
	 * デフォルト検索条件をフィルタ定義と一緒に利用するかを設定します。
	 * @param useDefaultConditionWithFilterDefinition デフォルト検索条件をフィルタ定義と一緒に利用するか
	 */
	public void setUseDefaultConditionWithFilterDefinition(boolean useDefaultConditionWithFilterDefinition) {
	    this.useDefaultConditionWithFilterDefinition = useDefaultConditionWithFilterDefinition;
	}

	/**
	 * デフォルトプロパティ条件設定スクリプトを取得します。
	 * @return デフォルトプロパティ条件設定スクリプト
	 */
	public String getDefaultPropertyConditionScript() {
	    return defaultPropertyConditionScript;
	}

	/**
	 * デフォルトプロパティ条件設定スクリプトを設定します。
	 * @param defaultPropertyConditionScript デフォルトプロパティ条件設定スクリプト
	 */
	public void setDefaultPropertyConditionScript(String defaultPropertyConditionScript) {
	    this.defaultPropertyConditionScript = defaultPropertyConditionScript;
	}

	/**
	 * フィルタ設定を取得します。
	 * @return フィルタ設定
	 */
	public List<MetaFilterSetting> getFilterSetting() {
		if (filterSetting == null) filterSetting = new ArrayList<>();
	    return filterSetting;
	}

	/**
	 * フィルタ設定を設定します。
	 * @param filterSetting フィルタ設定
	 */
	public void setFilterSetting(List<MetaFilterSetting> filterSetting) {
	    this.filterSetting = filterSetting;
	}

	public void addFilterSetting(MetaFilterSetting setting) {
		getFilterSetting().add(setting);
	}

	/**
	 * 要素を取得します。
	 * @return 要素
	 */
	public List<MetaElement> getElements() {
		if (elements == null) elements = new ArrayList<>();
	    return elements;
	}

	/**
	 * 要素を設定します。
	 * @param elements 要素
	 */
	public void setElements(List<MetaElement> elements) {
	    this.elements = elements;
	}

	public List<MetaSortSetting> getSortSetting() {
		if (sortSetting == null) sortSetting = new ArrayList<>();
		return sortSetting;
	}

	public void setSortSetting(List<MetaSortSetting> sortSetting) {
		this.sortSetting = sortSetting;
	}

	/**
	 * 要素を追加。
	 * @param element 要素
	 */
	public void addElement(MetaElement element) {
		getElements().add(element);
	}

	public void addSortSetting(MetaSortSetting setting) {
		getSortSetting().add(setting);
	}

	@Override
	public MetaSearchConditionSection copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler handler = ctx.getHandlerById(definitionId);

		SearchConditionSection section = (SearchConditionSection) element;

		this.conditionDispCount = section.getConditionDispCount();
		this.colNum = section.getColNum();
		this.unsorted = section.isUnsorted();
		this.fulltextSearchSorted = section.isFulltextSearchSorted();
		this.distinct = section.isDistinct();
		this.hideDetailCondition = section.isHideDetailCondition();
		this.hideFixedCondition = section.isHideFixedCondition();
		this.hideCsvdownload = section.isHideCsvdownload();
		this.defaultCondition = section.getDefaultCondition();
		this.useDefaultConditionWithFilterDefinition = section.isUseDefaultConditionWithFilterDefinition();
		this.defaultPropertyConditionScript = section.getDefaultPropertyConditionScript();

		// 仮想プロパティ追加によりMetaPropertyからMetaElementへフィールドを変更
//		if (section.getProperties().size() > 0) {
//			for (PropertyItem prop : section.getProperties()) {
//				MetaPropertyItem p = new MetaPropertyItem();
//				p.applyConfig(prop, definitionId);
//				if (p.getPropertyId() != null || p.isBlank()) this.addProperty(p);
//			}
//		}
		if (section.getElements().size() > 0) {
			for (Element elem : section.getElements()) {
				MetaElement e = MetaElement.createInstance(elem);
				e.applyConfig(elem, definitionId);
				this.addElement(e);
			}
		}
		this.nonOutputOid = section.isNonOutputOid();
		this.nonOutputReference = section.isNonOutputReference();
		this.nonOutputBinaryRef = section.isNonOutputBinaryRef();
		this.hideCsvUpload = section.isHideCsvUpload();
		this.csvUploadDenyInsert = section.isCsvUploadDenyInsert();
		this.csvUploadDenyUpdate = section.isCsvUploadDenyUpdate();
		this.csvUploadDenyDelete = section.isCsvUploadDenyDelete();
		this.csvUploadInsertProperties = section.getCsvUploadInsertProperties();
		this.csvUploadUpdateProperties = section.getCsvUploadUpdateProperties();
		this.hideCsvdownloadDialog = section.isHideCsvdownloadDialog();
		this.specifyCharacterCode = section.getSpecifyCharacterCode();
		this.csvUploadTransactionType = section.getCsvUploadTransactionType();
		this.csvUploadInterrupterName = section.getCsvUploadInterrupterName();
		this.csvdownloadMaxCount = section.getCsvdownloadMaxCount();
		this.csvdownloadProperties = section.getCsvdownloadProperties();
		this.csvdownloadUploadableProperties = section.getCsvdownloadUploadableProperties();
		this.csvdownloadFileNameFormat = section.getCsvdownloadFileNameFormat();
		this.csvMultipleFormat = section.getCsvMultipleFormat();

		if (!section.getSortSetting().isEmpty()) {
			for (SortSetting setting : section.getSortSetting()) {
				MetaSortSetting meta = new MetaSortSetting();
				meta.applyConfig(setting, ctx, handler);
				addSortSetting(meta);
			}
		}

		if (!section.getFilterSetting().isEmpty()) {
			for (FilterSetting setting : section.getFilterSetting()) {
				MetaFilterSetting meta = new MetaFilterSetting();
				meta.applyConfig(setting, ctx, handler);
				addFilterSetting(meta);
			}
		}
	}

	@Override
	public Element currentConfig(String definitionId) {
		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler handler = ctx.getHandlerById(definitionId);

		SearchConditionSection section = new SearchConditionSection();
		super.fillTo(section, definitionId);

		section.setScriptKey(scriptKey);

		section.setConditionDispCount(this.conditionDispCount);
		section.setColNum(this.colNum);
		section.setUnsorted(unsorted);
		section.setFulltextSearchSorted(this.fulltextSearchSorted);
		section.setDistinct(distinct);
		section.setHideDetailCondition(this.hideDetailCondition);
		section.setHideFixedCondition(this.hideFixedCondition);
		section.setHideCsvdownload(this.hideCsvdownload);
		section.setDefaultCondition(this.defaultCondition);
		section.setUseDefaultConditionWithFilterDefinition(this.useDefaultConditionWithFilterDefinition);
		section.setDefaultPropertyConditionScript(this.defaultPropertyConditionScript);

		if (this.getElements().size() > 0) {
			for (MetaElement elem : this.getElements()) {
				Element e = elem.currentConfig(definitionId);
				//プロパティが無効な場合など、生成できない場合は追加しない
				if (e != null) {
					section.addElement(e);
				}
			}
		}
		section.setNonOutputOid(this.nonOutputOid);
		section.setNonOutputReference(this.nonOutputReference);
		section.setNonOutputBinaryRef(this.nonOutputBinaryRef);
		section.setHideCsvUpload(this.hideCsvUpload);
		section.setCsvUploadDenyInsert(this.csvUploadDenyInsert);
		section.setCsvUploadDenyUpdate(this.csvUploadDenyUpdate);
		section.setCsvUploadDenyDelete(this.csvUploadDenyDelete);
		section.setCsvUploadInsertProperties(this.csvUploadInsertProperties);
		section.setCsvUploadUpdateProperties(this.csvUploadUpdateProperties);
		section.setHideCsvdownloadDialog(this.hideCsvdownloadDialog);
		section.setSpecifyCharacterCode(this.specifyCharacterCode);
		section.setCsvUploadTransactionType(this.csvUploadTransactionType);
		section.setCsvUploadInterrupterName(this.csvUploadInterrupterName);
		section.setCsvdownloadMaxCount(this.csvdownloadMaxCount);
		section.setCsvdownloadProperties(this.csvdownloadProperties);
		section.setCsvdownloadUploadableProperties(this.csvdownloadUploadableProperties);
		section.setCsvdownloadFileNameFormat(this.csvdownloadFileNameFormat);
		section.setCsvMultipleFormat(this.csvMultipleFormat);

		if (!getSortSetting().isEmpty()) {
			for (MetaSortSetting meta : getSortSetting()) {
				SortSetting s = meta.currentConfig(ctx, handler);
				//プロパティが無効な場合など、生成できない場合は追加しない
				if (s != null) {
					section.addSortSetting(s);
				}
			}
		}

		if (!getFilterSetting().isEmpty()) {
			for (MetaFilterSetting meta : getFilterSetting()) {
				section.addFilterSetting(meta.currentConfig(ctx, handler));
			}
		}
		return section;
	}

	@Override
	public SearchConditionSectionRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new SearchConditionSectionRuntime(this, entityView, formView);
	}

	public class SearchConditionSectionRuntime extends SectionRuntime {

		public static final String DEFAULT_CONDITION_PREFIX = "SearchConditionSection_defaultCondition_";

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public SearchConditionSectionRuntime(MetaSearchConditionSection metadata, EntityViewRuntime entityView, FormViewRuntime formView) {
			super(metadata, entityView);

			EntityContext context = EntityContext.getCurrentContext();
			EntityHandler eh = context.getHandlerById(entityView.getMetaData().getDefinitionId());

			Map<String, GroovyTemplate> customStyleMap = new HashMap<>();
			List<MetaPropertyItem> properties = metadata.getElements().stream()
					.filter(e -> e instanceof MetaPropertyItem)
					.map(e -> (MetaPropertyItem) e)
					.collect(Collectors.toList());
			for (MetaPropertyItem property : properties) {
				property.createRuntime(entityView, formView);

				MetaPropertyEditor editor = property.getEditor();
				if (editor != null) {
					PropertyEditorRuntime runtime = (PropertyEditorRuntime)editor.createRuntime(entityView, formView, property, context, eh);
					customStyleMap.put(editor.getInputCustomStyleScriptKey(), runtime.getInputCustomStyleScript());
					customStyleMap.put(editor.getOutputCustomStyleScriptKey(), runtime.getOutputCustomStyleScript());
				}
			}
			//Script用のKEYを設定
			metadata.scriptKey = "SearchConditionSection_Style_" + GroovyTemplateCompiler.randomName().replace("-", "_");

			//EntityViewに登録
			entityView.addCustomStyle(scriptKey , customStyleMap);

			if (StringUtil.isNotEmpty(defaultCondition)) {
				PreparedQuery query = new PreparedQuery(defaultCondition);
				entityView.addQuery(DEFAULT_CONDITION_PREFIX + metadata.getElementRuntimeId(), query);
			}
		}
	}
}
