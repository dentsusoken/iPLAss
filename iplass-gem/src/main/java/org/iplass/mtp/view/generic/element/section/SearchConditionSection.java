/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.IgnoreField;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.csv.MultipleFormat;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.element.Element;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * 検索条件を保持するセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/SearchConditionSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@IgnoreField({"dispFlag", "displayScript", "title"})
public class SearchConditionSection extends Section {

	/** 検索条件のソートタイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum ConditionSortType {
		ASC,DESC;
	}

	/** Entityデータのサポートファイルタイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum FileSupportType {
		CSV, EXCEL, SPECIFY
	}

	/** ダウンロード時の文字コード指定 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/generic")
	public enum CsvDownloadSpecifyCharacterCode {
		NONE, SPECIFY, NOT_SPECIFY
	}

	/** アップロード時のトランザクション制御設定 */
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
			displayNameKey="generic_element_section_SearchConditionSection_colNumDisplayNameKey",
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
			displayNameKey="generic_element_section_SearchConditionSection_conditionDispCountDisplayNameKey",
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
			displayNameKey="generic_element_section_SearchConditionSection_hideDetailConditionDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=220,
			description="詳細検索を非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideDetailConditionDescriptionKey"
	)
	private boolean hideDetailCondition;

	/** 定型検索非表示設定 */
	@MetaFieldInfo(
			displayName="定型検索非表示設定",
			displayNameKey="generic_element_section_SearchConditionSection_hideFixedConditionDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=230,
			description="定型検索を非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideFixedConditionDescriptionKey"
	)
	private boolean hideFixedCondition;

	/** Entityデータのファイルタイプ */
	@MetaFieldInfo(
			displayName="Entityデータのファイルタイプ",
			displayNameKey="generic_element_section_SearchConditionSection_fileSupportTypeDisplayNameKey",
			inputType=InputType.ENUM,
			enumClass=FileSupportType.class,
			displayOrder=1000,
			description="ダウンロード、アップロードで許可するファイルタイプを指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_fileSupportTypeDescriptionKey"
	)
	private FileSupportType fileSupportType;

	/** ダウンロードボタン非表示設定 */
	@MetaFieldInfo(
			displayName="ダウンロードボタン非表示設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_hideCsvdownloadDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1005,
			description="ダウンロードボタンを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideCsvdownloadDescriptionKey"
	)
	private boolean hideCsvdownload;

	/** ダウンロードダイアログ非表示設定 */
	@MetaFieldInfo(
			displayName="ダウンロードダイアログ非表示設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_hideCsvdownloadDialogDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1010,
			description="ダウンロード時に全項目出力するかを選択するダイアログを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideCsvdownloadDialogDescriptionKey"
	)
	private boolean hideCsvdownloadDialog;

	/** ダウンロード時oid非出力設定 */
	@MetaFieldInfo(
			displayName="ダウンロード時oid非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputOidDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1020,
			description="ダウンロード時oidを出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputOidDescriptionKey"
	)
	private boolean nonOutputOid;

	/** ダウンロード時BinaryReference非出力設定 */
	@MetaFieldInfo(
			displayName="ダウンロード時BinaryReference非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputBinaryRefDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1030,
			description="ダウンロード時BinaryReferenceのnameを出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputBinaryRefDescriptionKey"
	)
	private boolean nonOutputBinaryRef;

	/** ダウンロード時Reference非出力設定 */
	@MetaFieldInfo(
			displayName="ダウンロード時Reference非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputReferenceDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1040,
			description="ダウンロード時Referenceを出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputReferenceDescriptionKey"
	)
	private boolean nonOutputReference;

	/** ダウンロード時表示名非出力設定 */
	@MetaFieldInfo(
			displayName="ダウンロード時表示名非出力設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_nonOutputDisplayNameDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1045,
			description="Upload形式のダウンロード時表示名を出力しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_nonOutputDisplayNameDescriptionKey"
	)
	private boolean nonOutputDisplayName;

	/** 多重度プロパティの出力フォーマット */
	@MetaFieldInfo(
			displayName="多重度プロパティの出力フォーマット",
			displayNameKey="generic_element_section_SearchConditionSection_csvMultipleFormatNameKey",
			inputType=InputType.ENUM,
			enumClass=MultipleFormat.class,
			displayOrder=1050,
			description="多重度プロパティの出力形式を指定します。Referenceは対象外です。",
			descriptionKey="generic_element_section_SearchConditionSection_csvMultipleFormatDescriptionKey"
	)
	private MultipleFormat csvMultipleFormat = MultipleFormat.EACH_COLUMN;

	/** ダウンロード時文字コード指定を可能にする */
	@MetaFieldInfo(
			displayName="ダウンロード時文字コードを指定可能にする ",
			displayNameKey="generic_element_section_SearchConditionSection_specifyCharacterCodeNameKey",
			inputType=InputType.ENUM,
			enumClass=CsvDownloadSpecifyCharacterCode.class,
			displayOrder=1060,
			description="ダウンロード時に文字コードを指定可能かを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_specifyCharacterCodeDescriptionKey"
	)
	private CsvDownloadSpecifyCharacterCode specifyCharacterCode = CsvDownloadSpecifyCharacterCode.NONE;

	/** ダウンロード件数の上限値 */
	@MetaFieldInfo(
			displayName="ダウンロード件数の上限値",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadMaxCountDisplayNameKey",
			inputType=InputType.NUMBER,
			displayOrder=1070,
			description="ダウンロード件数の上限値を設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadMaxCountDescriptionKey"
	)
	private Integer csvdownloadMaxCount;

	/** Upload形式のダウンロード時に一括でロードする */
	@MetaFieldInfo(
			displayName="Upload形式のダウンロード時に一括でロードする ",
			displayNameKey="generic_element_section_SearchConditionSection_uploadableCsvdownloadLoadAtOnceDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1073,
			description="Upload形式のダウンロードで、多重度複数の参照を含む場合に一括でロードをするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_uploadableCsvdownloadLoadAtOnceDescriptionKey"
	)
	private boolean uploadableCsvdownloadLoadAtOnce;

	/** Upload形式のダウンロード時のロード単位 */
	@MetaFieldInfo(
			displayName="Upload形式のダウンロード時のロード単位",
			displayNameKey="generic_element_section_SearchConditionSection_uploadableCsvdownloadLoadSizeDisplayNameKey",
			inputType=InputType.NUMBER,
			displayOrder=1075,
			description="Upload形式のダウンロードで、多重度複数の参照を含む場合に一度にロードする件数を設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_uploadableCsvdownloadLoadSizeDescriptionKey"
	)
	private Integer uploadableCsvdownloadLoadSize;

	/** ダウンロード項目 */
	@MetaFieldInfo(
			displayName="ダウンロード項目",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadPropertiesDisplayNameKey",
			inputType=InputType.TEXT_AREA,
			displayOrder=1080,
			description="出力対象の項目を指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadPropertiesDescriptionKey"
	)
	private String csvdownloadProperties;

	/** Upload形式のダウンロード項目 */
	@MetaFieldInfo(
			displayName="ダウンロード項目(Upload形式)",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadUploadablePropertiesDisplayNameKey",
			inputType=InputType.TEXT_AREA,
			displayOrder=1085,
			description="Upload形式の出力対象の項目を指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadUploadablePropertiesDescriptionKey"
	)
	private String csvdownloadUploadableProperties;

	/** ファイル名Format */
	@MetaFieldInfo(
			displayName="ファイル名Format",
			displayNameKey="generic_element_section_SearchConditionSection_csvdownloadFileNameScriptDisplayNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=1090,
			descriptionKey="generic_element_section_SearchConditionSection_csvdownloadFileNameScriptDescriptionKey"
	)
	private String csvdownloadFileNameFormat;




	/** アップロードボタン非表示設定 */
	@MetaFieldInfo(
			displayName="アップロードボタン非表示設定 ",
			displayNameKey="generic_element_section_SearchConditionSection_hideCsvUploadDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2000,
			description="アップロードボタンを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_hideCsvUploadDescriptionKey"
	)
	private boolean hideCsvUpload;

	/** アップロードで登録を許可しない */
	@MetaFieldInfo(
			displayName="アップロードで登録を許可しない",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadDenyInsertNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2010,
			description="アップロードで登録を許可しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadDenyInsertDescriptionKey"
	)
	private boolean csvUploadDenyInsert;

	/** アップロードで更新を許可しない */
	@MetaFieldInfo(
			displayName="アップロードで更新を許可しない",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadDenyUpdateNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2020,
			description="アップロードで更新を許可しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadDenyUpdateDescriptionKey"
	)
	private boolean csvUploadDenyUpdate;


	/** アップロードで削除を許可しない */
	@MetaFieldInfo(
			displayName="アップロードで削除を許可しない",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadDenyDeleteNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=2030,
			description="アップロードで削除を許可しないかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadDenyDeleteDescriptionKey"
	)
	private boolean csvUploadDenyDelete;

	/** アップロード登録項目 */
	@MetaFieldInfo(
			displayName="アップロード登録項目",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadInsertPropertiesDisplayNameKey",
			inputType=InputType.TEXT_AREA,
			displayOrder=2040,
			description="アップロードで登録する対象の項目を指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadInsertPropertiesDescriptionKey"
	)
	private String csvUploadInsertProperties;

	/** アップロード更新項目 */
	@MetaFieldInfo(
			displayName="アップロード更新項目",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadUpdatePropertiesDisplayNameKey",
			inputType=InputType.TEXT_AREA,
			displayOrder=2050,
			description="アップロードで更新する対象の項目を指定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadUpdatePropertiesDescriptionKey"
	)
	private String csvUploadUpdateProperties;

	/** アップロード時のトランザクション制御設定 */
	@MetaFieldInfo(
			displayName="アップロード時のトランザクション制御設定",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadTransactionTypeDisplayNameKey",
			inputType=InputType.ENUM,
			enumClass=CsvUploadTransactionType.class,
			displayOrder=2060,
			description="アップロードボタンを非表示にするかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadTransactionTypeDescriptionKey"
	)
	private CsvUploadTransactionType csvUploadTransactionType = CsvUploadTransactionType.ONCE;

	/** バージョン管理Entity以外の場合のアップロード更新データTargetVersion */
	@MetaFieldInfo(
			displayName="バージョン管理Entity以外の場合のアップロード更新データTargetVersion",
			inputType=InputType.ENUM,
			enumClass=TargetVersion.class,
			fixedEnumValue = {"CURRENT_VALID", "SPECIFIC"},
			displayOrder=2063,
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadTargetVersionForNoneVersionedEntityDisplayNameKey",
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadTargetVersionForNoneVersionedEntityDescriptionKey"
	)
	private TargetVersion csvUploadTargetVersionForNoneVersionedEntity;

	/** バージョン管理Entity以外の場合にアップロード画面でTargetVersionの指定を許可 */
	@MetaFieldInfo(
			displayName="バージョン管理Entity以外の場合にアップロード画面でTargetVersionの指定を許可",
			inputType=InputType.CHECKBOX,
			displayOrder=2065,
			displayNameKey="generic_element_section_SearchConditionSection_canCsvUploadTargetVersionSelectForNoneVersionedEntityDisplayNameKey",
			descriptionKey="generic_element_section_SearchConditionSection_canCsvUploadTargetVersionSelectForNoneVersionedEntityDescriptionKey"
	)
	private boolean canCsvUploadTargetVersionSelectForNoneVersionedEntity;

	/** カスタムアップロード処理クラス名 */
	@MetaFieldInfo(
			displayName="カスタムアップロード処理クラス名",
			displayNameKey="generic_element_section_SearchConditionSection_csvUploadInterrupterNameDisplayNameKey",
			displayOrder=2070,
			descriptionKey="generic_element_section_SearchConditionSection_csvUploadInterrupterNameDescriptionKey"
	)
	private String csvUploadInterrupterName;


	/** 重複行をまとめるか */
	@MetaFieldInfo(
			displayName="重複行をまとめるか",
			displayNameKey="generic_element_section_SearchConditionSection_distinctDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3000,
			description="重複行をまとめるかを設定します。",
			descriptionKey="generic_element_section_SearchConditionSection_distinctDescriptionKey"
	)
	private boolean distinct;

	/** デフォルト検索条件 */
	@MetaFieldInfo(
			displayName="検索時デフォルトフィルター条件",
			displayNameKey="generic_element_section_SearchConditionSection_defaultConditionDisplayNameKey",
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
			displayNameKey="generic_element_section_SearchConditionSection_useDefaultConditionWithFilterDefinitionDisplayNameKey",
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
			displayNameKey="generic_element_section_SearchConditionSection_defaultPropertyConditionScriptDisplayNameKey",
			inputType=InputType.SCRIPT,
			mode="groovy_script",
			displayOrder=3040,
			description="検索画面初期表示にデフォルトで設定する条件を指定するGroovyScriptです。<BR />" +
					"変数としてinitCondMap(Map<String, Object>)がバインドされています。<BR />" +
					"このスクリプト内でinitCondMapに「key:プロパティ名(sc_なしです)、value：初期条件とする値」という形式で値を設定することで初期条件として値が設定されます。<BR />" +
					"<BR/>" +
					"バインド変数として、「user：ユーザー情報」、「request：リクエスト情報」、「session：セッション情報」があらかじめバインドされています。<BR/>" +
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
			displayNameKey="generic_element_section_SearchConditionSection_unsortedDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3060,
			description="検索時にソートしないかを設定します。<br>" +
					"検索結果からソート項目を指定した場合はソートされます。",
			descriptionKey="generic_element_section_SearchConditionSection_unsortedDescriptionKey"
	)
	private boolean unsorted;

	@MetaFieldInfo(
			displayName="全文検索時にソートするか",
			displayNameKey="generic_element_section_SearchConditionSection_fulltextSearchSortedDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3070,
			description="全文検索時にソートするかを設定します。<br>" +
					"全文検索結果からソート項目を指定した場合はソートされます。",
			descriptionKey="generic_element_section_SearchConditionSection_fulltextSearchSortedDescriptionKey"
	)
	private boolean fulltextSearchSorted;

	/** カスタムスタイルキー */
	private String scriptKey;

	/**
	 * デフォルトコンストラクタ
	 */
	public SearchConditionSection() {
	}


	/**
	 * 要素を取得します。
	 * @return 要素
	 */
	public List<Element> getElements() {
		if (elements == null) {
			elements = new ArrayList<>();
		}
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
	 * 要素を追加します。
	 * @param val プロパティ情報
	 */
	public void addElement(Element val) {
		getElements().add(val);
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
	 * Entityデータのファイルタイプを取得します。
	 * @return Entityデータのファイルタイプ
	 */
	public FileSupportType getFileSupportType() {
		return fileSupportType;
	}

	/**
	 * Entityデータのファイルタイプを設定します。
	 * @param fileSupportType Entityデータのファイルタイプ
	 */
	public void setFileSupportType(FileSupportType fileSupportType) {
		this.fileSupportType = fileSupportType;
	}

	/**
	 * ダウンロードボタン非表示設定を取得します。
	 * @return ダウンロードボタン非表示設定
	 */
	public boolean isHideCsvdownload() {
		return hideCsvdownload;
	}

	/**
	 * ダウンロードボタン非表示設定を設定します。
	 * @param hideCsvdownload ダウンロードボタン非表示設定
	 */
	public void setHideCsvdownload(boolean hideCsvdownload) {
		this.hideCsvdownload = hideCsvdownload;
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


	public boolean isNonOutputDisplayName() {
		return nonOutputDisplayName;
	}


	public void setNonOutputDisplayName(boolean nonOutputDisplayName) {
		this.nonOutputDisplayName = nonOutputDisplayName;
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
	 * ダウンロードの上限件数を取得します。
	 * @return ダウンロードの上限件数
	 */
	public Integer getCsvdownloadMaxCount() {
		return csvdownloadMaxCount;
	}


	/**
	 * ダウンロードの上限件数を設定します。
	 * @param csvdownloadMaxCount ダウンロードの上限件数
	 */
	public void setCsvdownloadMaxCount(Integer csvdownloadMaxCount) {
		this.csvdownloadMaxCount = csvdownloadMaxCount;
	}


	/**
	 * Upload形式のダウンロード時に一括でロードするかを取得します。
	 * @return Upload形式のダウンロード時に一括でロードするか
	 */
	public boolean isUploadableCsvdownloadLoadAtOnce() {
		return uploadableCsvdownloadLoadAtOnce;
	}


	/**
	 * Upload形式のダウンロード時に一括でロードするかを設定します。
	 * @param uploadableCsvdownloadLoadAtOnce Upload形式のダウンロード時に一括でロードするか
	 */
	public void setUploadableCsvdownloadLoadAtOnce(boolean uploadableCsvdownloadLoadAtOnce) {
		this.uploadableCsvdownloadLoadAtOnce = uploadableCsvdownloadLoadAtOnce;
	}


	/**
	 * Upload形式のダウンロード時のロード単位を取得します。
	 * @return Upload形式のダウンロード時のロード単位
	 */
	public Integer getUploadableCsvdownloadLoadSize() {
		return uploadableCsvdownloadLoadSize;
	}


	/**
	 * Upload形式のダウンロード時のロード単位を設定します。
	 * @param uploadableCsvdownloadLoadSize Upload形式のダウンロード時のロード単位
	 */
	public void setUploadableCsvdownloadLoadSize(Integer uploadableCsvdownloadLoadSize) {
		this.uploadableCsvdownloadLoadSize = uploadableCsvdownloadLoadSize;
	}


	/**
	 * ダウンロード項目を取得します。
	 * @return ダウンロード項目
	 */
	public String getCsvdownloadProperties() {
	    return csvdownloadProperties;
	}


	/**
	 * ダウンロード項目をSet形式で取得します。
	 * @return ダウンロード項目
	 */
	public Set<String> getCsvdownloadPropertiesSet() {
		if(csvdownloadProperties == null) {
			return null;
		}
		return splitCommaString(csvdownloadProperties);
	}


	/**
	 * ダウンロード項目を設定します。
	 * @param csvdownloadProperties ダウンロード項目
	 */
	public void setCsvdownloadProperties(String csvdownloadProperties) {
	    this.csvdownloadProperties = csvdownloadProperties;
	}


	/**
	 * Upload形式のダウンロード項目を取得します。
	 * @return Upload形式のダウンロード項目
	 */
	public String getCsvdownloadUploadableProperties() {
	    return csvdownloadUploadableProperties;
	}


	/**
	 * Upload形式のダウンロード項目をSet形式で取得します。
	 * @return Upload形式のダウンロード項目
	 */
	public Set<String> getCsvdownloadUploadablePropertiesSet() {
		if(csvdownloadUploadableProperties == null) {
			return null;
		}
		return splitCommaString(csvdownloadUploadableProperties);
	}


	/**
	 * Upload形式のダウンロード項目を設定します。
	 * @param csvdownloadUploadableProperties Upload形式のダウンロード項目
	 */
	public void setCsvdownloadUploadableProperties(String csvdownloadUploadableProperties) {
	    this.csvdownloadUploadableProperties = csvdownloadUploadableProperties;
	}


	/**
	 * ファイル名Format(GroovyTemplate)を取得します。
	 *
	 * @return ファイル名Format(GroovyTemplate)
	 */
	public String getCsvdownloadFileNameFormat() {
		return csvdownloadFileNameFormat;
	}


	/**
	 * ファイル名Format(GroovyTemplate)を設定します。
	 * @param csvdownloadFileNameFormat ファイル名Format(GroovyTemplate)
	 */
	public void setCsvdownloadFileNameFormat(String csvdownloadFileNameFormat) {
		this.csvdownloadFileNameFormat = csvdownloadFileNameFormat;
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


	public Set<String> getCsvUploadInsertPropertiesSet() {
		if(csvUploadInsertProperties == null) {
			return null;
		}
		return splitCommaString(csvUploadInsertProperties);
	}


	public void setCsvUploadInsertProperties(String csvUploadInsertProperties) {
	    this.csvUploadInsertProperties = csvUploadInsertProperties;
	}


	public String getCsvUploadUpdateProperties() {
	    return csvUploadUpdateProperties;
	}


	public Set<String> getCsvUploadUpdatePropertiesSet() {
		if(csvUploadUpdateProperties == null) {
			return null;
		}
		return splitCommaString(csvUploadUpdateProperties);
	}


	public void setCsvUploadUpdateProperties(String csvUploadUpdateProperties) {
	    this.csvUploadUpdateProperties = csvUploadUpdateProperties;
	}


	public CsvUploadTransactionType getCsvUploadTransactionType() {
		return csvUploadTransactionType;
	}


	public void setCsvUploadTransactionType(CsvUploadTransactionType csvUploadTransactionType) {
		this.csvUploadTransactionType = csvUploadTransactionType;
	}


	/**
	 * アップロード更新データTargetVersionを取得します。
	 * @return アップロード更新データTargetVersion
	 */
	public TargetVersion getCsvUploadTargetVersionForNoneVersionedEntity() {
		return csvUploadTargetVersionForNoneVersionedEntity;
	}


	/**
	 * アップロード更新データTargetVersionを設定します。
	 * @param csvUploadTargetVersionForNoneVersionedEntity アップロード更新データTargetVersion
	 */
	public void setCsvUploadTargetVersionForNoneVersionedEntity(TargetVersion csvUploadTargetVersionForNoneVersionedEntity) {
		this.csvUploadTargetVersionForNoneVersionedEntity = csvUploadTargetVersionForNoneVersionedEntity;
	}


	/**
	 * アップロード画面でTargetVersionの指定を許可するかを取得します。
	 * @return アップロード画面でTargetVersionの指定を許可するか
	 */
	public boolean isCanCsvUploadTargetVersionSelectForNoneVersionedEntity() {
		return canCsvUploadTargetVersionSelectForNoneVersionedEntity;
	}


	/**
	 * アップロード画面でTargetVersionの指定を許可するかを設定します。
	 * @param canCsvUploadTargetVersionSelectForNoneVersionedEntity アップロード画面でTargetVersionの指定を許可するか
	 */
	public void setCanCsvUploadTargetVersionSelectForNoneVersionedEntity(boolean canCsvUploadTargetVersionSelectForNoneVersionedEntity) {
		this.canCsvUploadTargetVersionSelectForNoneVersionedEntity = canCsvUploadTargetVersionSelectForNoneVersionedEntity;
	}


	/**
	 * カスタムアップロード処理クラス名を取得します。
	 * @return カスタムアップロード処理クラス名
	 */
	public String getCsvUploadInterrupterName() {
		return csvUploadInterrupterName;
	}


	/**
	 * カスタムアップロード処理クラス名を設定します。
	 * @param csvUploadInterrupterName カスタムアップロード処理クラス名
	 */
	public void setCsvUploadInterrupterName(String csvUploadInterrupterName) {
		this.csvUploadInterrupterName = csvUploadInterrupterName;
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
	 * フィルタ設定を取得します。
	 * @return フィルタ設定
	 */
	public List<FilterSetting> getFilterSetting() {
		if (filterSetting == null) {
			filterSetting = new ArrayList<>();
		}
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
		if (sortSetting == null) {
			sortSetting = new ArrayList<>();
		}
		return sortSetting;
	}


	public void setSortSetting(List<SortSetting> sortSetting) {
		this.sortSetting = sortSetting;
	}


	public void addSortSetting(SortSetting setting) {
		getSortSetting().add(setting);
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


	@Override
	public boolean isShowLink() {
		return false;
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

	/**
	 * カンマ区切りを分割します。
	 * StringUtilが使えないので、独自処理します。
	 * @param value 値
	 * @return 分割結果
	 */
	private Set<String> splitCommaString(String value) {
		String sanitized = value.replace(" ","").replace("\n", "").replace("\r", "");
	    return Stream.of(sanitized.split(",")).collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
