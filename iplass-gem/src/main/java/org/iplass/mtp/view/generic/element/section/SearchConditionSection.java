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

package org.iplass.mtp.view.generic.element.section;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.IgnoreField;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.entity.csv.MultipleFormat;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.element.Element;

/**
 * 検索条件を保持するセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/SearchConditionSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@IgnoreField({"dispFlag", "displayScript"})
public class SearchConditionSection extends Section {

	/** 検索条件のソートタイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum ConditionSortType {
		ASC,DESC;
	}

	/** CSVダウンロード時の文字コード指定 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum CsvDownloadSpecifyCharacterCode {
		NONE, SPECIFY, NOT_SPECIFY
	}

	/** CSVアップロード時のトランザクション制御設定 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum CsvUploadTransactionType {
		ONCE, DIVISION
	}

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2187298450937471228L;

	/** 要素 */
	@MultiLang(isMultiLangValue=false)
	private List<Element> elements;

	/** 列数 */
	@MetaFieldInfo(
			displayName="列数",
			displayNameKey="generic_element_section_SearchConditionSection_colNumDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=1,
//			maxRange=2,
			displayOrder=200,
			description="セクションの列数を指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_colNumDescriptionKey"
	)
	private int colNum;

	/** 詳細条件の表示件数 */
	@MetaFieldInfo(
			displayName="詳細条件の表示件数",
			displayNameKey="generic_element_section_SearchConditionSection_conditionDispCountDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=1,
			maxRange=9,
			displayOrder=210,
			description="詳細条件に初期表示する条件の数を設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_conditionDispCountDescriptionKey"
	)
	private int conditionDispCount;

	/** 詳細検索非表示設定 */
	@MetaFieldInfo(
			displayName="詳細検索非表示設定",
			displayNameKey="generic_element_section_SearchConditionSection_hideDetailConditionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=220,
			description="詳細検索を非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideDetailConditionDescriptionKey"
	)
	private boolean hideDetailCondition;

	/** 定型検索非表示設定 */
	@MetaFieldInfo(
			displayName="定型検索非表示設定",
			displayNameKey="generic_element_section_SearchConditionSection_hideFixedConditionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=230,
			description="定型検索を非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideFixedConditionDescriptionKey"
	)
	private boolean hideFixedCondition;




	/** CSVダウンロードボタン非表示設定 */
	@MetaFieldInfo(
			displayName="CSVダウンロードボタン非表示設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_hideCsvdownloadDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1000,
			description="CSVダウンロードボタンを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideCsvdownloadDescriptionKey"
	)
	private boolean hideCsvdownload;

	/** CSVダウンロードダイアログ非表示設定 */
	@MetaFieldInfo(
			displayName="CSVダウンロードダイアログ非表示設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_hideCsvdownloadDialogDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1010,
			description="CSVダウンロード時に全項目出力するかを選択するダイアログを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideCsvdownloadDialogDescriptionKey"
	)
	private boolean hideCsvdownloadDialog;

	/** CSVダウンロード時oid非出力設定 */
	@MetaFieldInfo(
			displayName="CSVダウンロード時oid非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputOidDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1020,
			description="CSVダウンロード時oidを出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputOidDescriptionKey"
	)
	private boolean nonOutputOid;

	/** CSVダウンロード時BinaryReference非出力設定 */
	@MetaFieldInfo(
			displayName="CSVダウンロード時BinaryReference非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputBinaryRefDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1030,
			description="CSVダウンロード時BinaryReferenceのnameを出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputBinaryRefDescriptionKey"
	)
	private boolean nonOutputBinaryRef;

	/** CSVダウンロード時Reference非出力設定 */
	@MetaFieldInfo(
			displayName="CSVダウンロード時Reference非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputReferenceDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1040,
			description="CSVダウンロード時Referenceを出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputReferenceDescriptionKey"
	)
	private boolean nonOutputReference;

	/** 多重度プロパティのCSV出力フォーマット */
	@MetaFieldInfo(
			displayName="多重度プロパティのCSV出力フォーマット",
			displayNameKey="generic_element_section_SearchConditionSection_csvMultipleFormatNameKey",
			inputType=InputType.ENUM,
			enumClass=MultipleFormat.class,
			displayOrder=1050,
			description="CSVの多重度プロパティの出力形式を指定します。Referenceは対象外です。",
			descriptionKey="generic_element_section_SearchConditionSection_csvMultipleFormatDescriptionKey"
	)
	private MultipleFormat csvMultipleFormat = MultipleFormat.EACH_COLUMN;

	/** CSVダウンロード時文字コード指定を可能にする */
	@MetaFieldInfo(
			displayName="CSVダウンロード時文字コードを指定可能にする ",
			displayNameKey="generic_element_section_SearchConditionSection_specifyCharacterCodeNameKey",
			inputType=InputType.ENUM,
			enumClass=CsvDownloadSpecifyCharacterCode.class,
			displayOrder=1060,
			description="CSVダウンロード時に文字コードを指定可能かを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_specifyCharacterCodeDescriptionKey"
	)
	private CsvDownloadSpecifyCharacterCode specifyCharacterCode = CsvDownloadSpecifyCharacterCode.NONE;

	/** CSVダウンロード件数の上限値 */
	@MetaFieldInfo(
			displayName="CSVダウンロード件数の上限値",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadMaxCountDisplaNameKey",
			inputType=InputType.TEXT,
			displayOrder=1070,
			description="CSVダウンロード件数の上限値を設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadMaxCountDescriptionKey"
	)
	private Integer csvdownloadMaxCount;

	/** CSVダウンロード項目 */
	@MetaFieldInfo(
			displayName="CSVダウンロード項目",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadPropertiesDisplaNameKey",
			inputType=InputType.TEXT,
			displayOrder=1080,
			description="CSV出力対象の項目を指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadPropertiesDescriptionKey"
	)
	private String csvdownloadProperties;

	/** CSVファイル名Format */
	@MetaFieldInfo(
			displayName="CSVファイル名Format",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadFileNameScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=1090,
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadFileNameScriptDescriptionKey"
	)
	private String csvdownloadFileNameFormat;




	/** CSVアップロードボタン非表示設定 */
	@MetaFieldInfo(
			displayName="CSVアップロードボタン非表示設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_hideCsvUploadDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2000,
			description="CSVアップロードボタンを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideCsvUploadDescriptionKey"
	)
	private boolean hideCsvUpload;

	/** CSVアップロード時のトランザクション制御設定 */
	@MetaFieldInfo(
			displayName="CSVアップロード時のトランザクション制御設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadTransactionTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=CsvUploadTransactionType.class,
			displayOrder=2010,
			description="CSVアップロードボタンを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadTransactionTypeDescriptionKey"
	)
	private CsvUploadTransactionType csvUploadTransactionType = CsvUploadTransactionType.ONCE;;




	/** 重複行をまとめるか */
	@MetaFieldInfo(
			displayName="重複行をまとめるか",
			displayNameKey="generic_element_section_SearchConditionSection_distinctDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3000,
			description="重複行をまとめるかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_distinctDescriptionKey"
	)
	private boolean distinct;

	/** デフォルト検索条件 */
	@MetaFieldInfo(
			displayName="検索時デフォルトフィルター条件",
			displayNameKey="generic_element_section_SearchConditionSection_defaultConditionDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=3010,
			description="検索画面上には表示されず、検索時に自動的に付与される検索条件です。" +
					"EQL形式でWhere句の条件を記述します。",
			descriptionKey="generic_element_section_SearchConditionSection_defaultConditionDescriptionKey"
	)
	private String defaultCondition;

	/** フィルタ設定 */
	@MetaFieldInfo(
			displayName="フィルタ設定",
			displayNameKey="generic_element_section_SearchConditionSection_filterSettingDisplayNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=FilterSetting.class,
			multiple=true,
			displayOrder=3020,
			description="定型検索で利用するフィルタを指定します。<br>"
					+ "未指定の場合は全てのフィルタが対象になります。",
			descriptionKey="generic_element_section_SearchConditionSection_filterSettingDescriptionKey"
	)
	private List<FilterSetting> filterSetting;

	/** デフォルト検索条件をフィルタ定義と一緒に利用するか */
	@MetaFieldInfo(
			displayName="デフォルト検索条件をフィルタ定義と一緒に利用するか ",
			displayNameKey="generic_element_section_SearchConditionSection_useDefaultConditionWithFilterDefinitionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3030,
			description="フィルタ定義を使う検索(定型、検索結果一覧パーツ、検索結果一覧ウィジェット)で、フィルタ定義と一緒にデフォルトフィルタ条件を使うかを設定します。<br>"
					+ "ONの場合：フィルタの条件＋デフォルト条件<br>"
					+ "OFFの場合：フィルタの条件のみ",
			descriptionKey="generic_element_section_SearchConditionSection_useDefaultConditionWithFilterDefinitionDescriptionKey"
	)
	private boolean useDefaultConditionWithFilterDefinition;

	/** デフォルトプロパティ条件設定スクリプト */
	@MetaFieldInfo(
			displayName="デフォルトプロパティ条件設定スクリプト",
			displayNameKey="generic_element_section_SearchConditionSection_defaultPropertyConditionScriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=3040,
			description="検索画面初期表示にデフォルトで設定する条件を指定するGroovyScriptです。<BR />" +
					"変数としてinitCondMap(Map<String, Object>)がバインドされています。<BR />" +
					"このスクリプト内でinitCondMapに「key:プロパティ名(sc_なしです)、value：初期条件とする値」という形式で値を設定することで初期条件として値が設定されます。<BR />" +
					"<BR/>" +
					"バインド変数として、「user：ユーザ情報」、「request：リクエスト情報」、「session：セッション情報」があらかじめバインドされています。<BR/>" +
					"リクエストパラメータで「sc_プロパティ名」として指定された条件がある場合は、予めinitCondMapに設定されています。<BR />" +
					"Reference Propertyの場合はOIDを値に設定する必要があります。<BR />" +
					"同一プロパティに複数の条件を設定する場合は配列で設定してください。",
			descriptionKey="generic_element_section_SearchConditionSection_defaultPropertyConditionScriptDescriptionKey"
	)
	private String defaultPropertyConditionScript;

	/** ソート設定 */
	@MetaFieldInfo(
			displayName="ソート設定",
			displayNameKey="generic_element_section_SearchConditionSection_sortSettingDisplayNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=SortSetting.class,
			multiple=true,
			displayOrder=3050,
			description="検索時にデフォルトで設定されるソート条件を設定します。<br>" +
					"検索画面でソートが行われた場合、設定された内容は2番目以降のソート条件として機能します。",
			descriptionKey="generic_element_section_SearchConditionSection_sortSettingDescriptionKey"
	)
	private List<SortSetting> sortSetting;

	/** 検索時にソートしないか */
	@MetaFieldInfo(
			displayName="検索時にソートしないか",
			displayNameKey="generic_element_section_SearchConditionSection_unsortedDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3060,
			description="検索時にソートしないかを設定します。<br>" +
					"検索結果からソート項目を指定した場合はソートされます。",
			descriptionKey="generic_element_section_SearchConditionSection_unsortedDescriptionKey"
	)
	private boolean unsorted;

	@MetaFieldInfo(
			displayName="全文検索時にソートしないか",
			displayNameKey="generic_element_section_SearchConditionSection_fulltextSearchUnsortedDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3070,
			description="全文検索時にソートしないかを設定します。<br>" +
					"全文検索結果からソート項目を指定した場合はソートされます。",
			descriptionKey="generic_element_section_SearchConditionSection_fulltextSearchUnsortedDescriptionKey"
	)
	private boolean fulltextSearchUnsorted;

	/** カスタムスタイルキー */
	private String scriptKey;

	/**
	 * デフォルトコンストラクタ
	 */
	public SearchConditionSection() {
	}


	public boolean isHideCsvdownloadDialog() {
		return hideCsvdownloadDialog;
	}


	public void setHideCsvdownloadDialog(boolean hideCsvdownloadDialog) {
		this.hideCsvdownloadDialog = hideCsvdownloadDialog;
	}

	public boolean isNonOutputOid() {
		return nonOutputOid;
	}

	public void setNonOutputOid(boolean nonOutputOid) {
		this.nonOutputOid = nonOutputOid;
	}

	public boolean isNonOutputBinaryRef() {
		return nonOutputBinaryRef;
	}

	public void setNonOutputBinaryRef(boolean nonOutputBinaryRef) {
		this.nonOutputBinaryRef = nonOutputBinaryRef;
	}

	public boolean isNonOutputReference() {
		return nonOutputReference;
	}

	public void setNonOutputReference(boolean nonOutputReference) {
		this.nonOutputReference = nonOutputReference;
	}

	public boolean isHideCsvUpload() {
		return hideCsvUpload;
	}

	public void setHideCsvUpload(boolean hideCsvUpload) {
		this.hideCsvUpload = hideCsvUpload;
	}

	public CsvUploadTransactionType getCsvUploadTransactionType() {
		return csvUploadTransactionType;
	}

	public void setCsvUploadTransactionType(CsvUploadTransactionType csvUploadTransactionType) {
		this.csvUploadTransactionType = csvUploadTransactionType;
	}

	public MultipleFormat getCsvMultipleFormat() {
		return csvMultipleFormat;
	}

	public void setCsvMultipleFormat(MultipleFormat csvMultipleFormat) {
		this.csvMultipleFormat = csvMultipleFormat;
	}

	public CsvDownloadSpecifyCharacterCode getSpecifyCharacterCode() {
		return specifyCharacterCode;
	}

	public void setSpecifyCharacterCode(CsvDownloadSpecifyCharacterCode specifyCharacterCode) {
		this.specifyCharacterCode = specifyCharacterCode;
	}

	/**
	 * 要素を取得します。
	 * @return 要素
	 */
	public List<Element> getElements() {
		if (elements == null) elements = new ArrayList<>();
	    return elements;
	}

	/**
	 * 要素を設定します。
	 * @param elements 要素
	 */
	public void setElements(List<Element> elements) {
	    this.elements = elements;
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
	 * 全文検索時にソートしないかを取得します。
	 * @return 全文検索時にソートしないか
	 */
	public boolean isFulltextSearchUnsorted() {
		return fulltextSearchUnsorted;
	}

	/**
	 * 全文検索時にソートしないかを設定します。
	 * @param fulltextSearchUnsorted 全文検索時にソートしないか
	 */
	public void setFulltextSearchUnsorted(boolean fulltextSearchUnsorted) {
		this.fulltextSearchUnsorted = fulltextSearchUnsorted;
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

	public Integer getCsvdownloadMaxCount() {
		return csvdownloadMaxCount;
	}


	public void setCsvdownloadMaxCount(Integer csvdownloadMaxCount) {
		this.csvdownloadMaxCount = csvdownloadMaxCount;
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
	 * CSVファイル名Format(GroovyTemplate)を取得します。
	 *
	 * @return CSVファイル名Format(GroovyTemplate)
	 */
	public String getCsvdownloadFileNameFormat() {
		return csvdownloadFileNameFormat;
	}

	/**
	 * CSVファイル名Format(GroovyTemplate)を設定します。
	 * @param csvdownloadFileNameFormat CSVファイル名Format(GroovyTemplate)
	 */
	public void setCsvdownloadFileNameFormat(String csvdownloadFileNameFormat) {
		this.csvdownloadFileNameFormat = csvdownloadFileNameFormat;
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

	public List<SortSetting> getSortSetting() {
		if (sortSetting == null) sortSetting = new ArrayList<>();
		return sortSetting;
	}

	public void setSortSetting(List<SortSetting> sortSetting) {
		this.sortSetting = sortSetting;
	}

	/**
	 * 要素を追加します。
	 * @param val プロパティ情報
	 */
	public void addElement(Element val) {
		getElements().add(val);
	}

	public void addSortSetting(SortSetting setting) {
		getSortSetting().add(setting);
	}

	@Override
	public boolean isShowLink() {
		return false;
	}

	/**
	 * フィルタ設定を取得します。
	 * @return フィルタ設定
	 */
	public List<FilterSetting> getFilterSetting() {
		if (filterSetting == null) filterSetting = new ArrayList<>();
	    return filterSetting;
	}

	/**
	 * フィルタ設定を設定します。
	 * @param filterSetting フィルタ設定
	 */
	public void setFilterSetting(List<FilterSetting> filterSetting) {
	    this.filterSetting = filterSetting;
	}

	public void addFilterSetting(FilterSetting setting) {
		getFilterSetting().add(setting);
	}

	/**
	 * カスタムスタイルのキーを取得します。
	 * @return カスタムスタイルのキー
	 */
	public String getScriptKey() {
		return scriptKey;
	}

	/**
	 * カスタムスタイルのキーを設定します。
	 * @param scriptKey カスタムスタイルのキー
	 */
	public void setScriptKey(String scriptKey) {
		this.scriptKey = scriptKey;
	}
}
