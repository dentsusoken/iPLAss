package org.iplass.mtp.view.generic.editor;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;

import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * 参照選択フィルタ設定
 * @author lish0p
 */
public class ReferenceSelectFilterSetting implements Refrectable {

	/** SerialVersionUID */
	private static final long serialVersionUID = 6265414317824562671L;

	/** 再度検索パターン（再検索時の選択状態の扱い） */
    @XmlType(namespace = "http://mtp.iplass.org/xml/definition/view/generic")
    public enum SelectFilterResearchPattern {
        /** 選択値を保持する（再検索後も現在の選択を維持する） */
		@XmlEnumValue("Keep")
        KEEP,
        /** 選択値をクリアする（再検索時に選択を解除する） */
		@XmlEnumValue("Clear")
        CLEAR
    }

	/** 検索パターン */
	@XmlType(namespace = "http://mtp.iplass.org/xml/definition/view/generic")
	public enum SelectFilterMatchPattern {
		/** 前方一致 */
		@XmlEnumValue("Prefix")
		PREFIX,
		/** 後方一致 */
		@XmlEnumValue("Postfix")
		POSTFIX,
		/** 部分一致 */
		@XmlEnumValue("Partial")
		PARTIAL
	}

	/** 選択フィルター用プロパティ */
    @MetaFieldInfo(
            displayName = "選択フィルター用のプロパティ",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_propertyDisplaNameKey",
            inputType = InputType.PROPERTY,
            displayOrder = 1100,
            description = "<b>表示タイプ: SelectFilter</b><br>選択フィルター用のプロパティを指定します。<br>指定したプロパティに入力された値をもとに参照データを絞り込みます。",
            descriptionKey = "generic_editor_ReferenceSelectFilterSetting_propertyDescriptionKey",
			excludePropertyType = { "BINARY", "TIME", "DATE", "DATETIME", "BOOLEAN", "EXPRESSION", "LONGTEXT", "FLOAT" }
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    private String propertyName;

    /** 検索条件 */
    @MetaFieldInfo(
            displayName = "検索条件",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_conditionDisplaNameKey",
            description = "表示する選択肢を検索する際に付与する条件を設定します。",
            descriptionKey = "generic_editor_ReferenceSelectFilterSetting_conditionDescriptionKey"
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    private String condition;

    /** ソートアイテム */
    @MetaFieldInfo(
            displayName = "ソートアイテム",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_sortItemDisplaNameKey",
            inputType = InputType.PROPERTY,
            description = "<b>表示タイプ: SelectFilter</b><br>参照データをソートする項目を指定します。",
            descriptionKey = "generic_editor_ReferenceSelectFilterSetting_sortItemDescriptionKey"
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    private String sortItem;

    /** ソート種別 */
    @MetaFieldInfo(
            displayName = "ソート種別",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_sortTypeDisplaNameKey",
            inputType = InputType.ENUM,
            enumClass = RefSortType.class,
            description = "<b>表示タイプ: SelectFilter</b><br>参照データをソートする順序を指定します。",
            descriptionKey = "generic_editor_ReferenceSelectFilterSetting_sortTypeDescriptionKey"
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    private RefSortType sortType;

    /** 選択フィルターの1回あたりの検索件数 */
    @MetaFieldInfo(
            displayName = "検索件数",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_searchCountDisplaNameKey",
            inputType = InputType.NUMBER,
            displayOrder = 1110,
            description = "<b>表示タイプ: SelectFilter</b><br>選択フィルターで1回ごとに取得する検索件数を指定します。",
            descriptionKey = "generic_editor_ReferenceSelectFilterSetting_searchCountDescriptionKey"
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    private int selectFilterSearchPageSize;

    /** 選択フィルター検索パターン */
    @MetaFieldInfo(
			displayName = "検索パターン",
			displayNameKey = "generic_editor_ReferenceSelectFilterSetting_searchPatternDisplaNameKey",
			inputType = InputType.ENUM,
			enumClass = SelectFilterMatchPattern.class,
			displayOrder = 1130,
			description = "<b>表示タイプ: SelectFilter</b><br>検索パターンを指定します。<br>PREFIX: 前方一致<br>PARTIAL: 部分一致<br>POSTFIX: 後方一致<br>未指定の場合は前方一致が適用されます。",
			descriptionKey = "generic_editor_ReferenceSelectFilterSetting_searchPatternDescriptionKey"
	)
	@EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
	)
	private SelectFilterMatchPattern selectFilterSearchPattern;

	/** 選択フィルター再度検索パターン */
	@MetaFieldInfo(
			displayName = "再検索時の選択動作",
			displayNameKey = "generic_editor_ReferenceSelectFilterSetting_researchPatternDisplaNameKey",
			inputType = InputType.ENUM,
			enumClass = SelectFilterResearchPattern.class,
			displayOrder = 1140,
			description = "<b>表示タイプ: SelectFilter</b><br>再検索実行時に既存の選択を維持するかクリアするかを指定します。KEEP_SELECTION: 選択を保持、CLEAR_SELECTION: 選択を解除します。",
			descriptionKey = "generic_editor_ReferenceSelectFilterSetting_researchPatternDescriptionKey"
	)
	@EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
	)
	private SelectFilterResearchPattern selectFilterResearchPattern;

    /** 選択フィルターのプレースホルダー */
    @MetaFieldInfo(
            displayName = "選択フィルターのプレースホルダー",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_placeholderDisplaNameKey",
            inputType = InputType.MULTI_LANG,
			displayOrder = 1150,
            description = "<b>表示タイプ: SelectFilter</b><br>選択フィルターの入力欄に表示するプレースホルダーを設定します。",
            descriptionKey = "generic_editor_ReferenceSelectFilterSetting_placeholderDescriptionKey",
            multiLangField = "localizedPlaceholderList"
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    @MultiLang(itemNameGetter = "getPropertyName", isUseSuperForItemName = true)
    private String selectFilterPlaceholder;

	/** プレースホルダーの多言語設定 */
    @MetaFieldInfo(
            displayName = "プレースホルダーの多言語設定",
            displayNameKey = "generic_editor_ReferenceSelectFilterSetting_placeholderListDisplaNameKey",
            inputType = InputType.MULTI_LANG_LIST,
			displayOrder = 1160
    )
    @EntityViewField(
			referenceTypes = { FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL, FieldReferenceType.BULK }
    )
    private List<LocalizedStringDefinition> localizedPlaceholderList;

	/**
	 * 選択フィルター用プロパティを取得します
	 * @return 選択フィルター用プロパティ
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * 選択フィルター用プロパティを設定します
	 * @param propertyName 選択フィルター用プロパティ
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 検索条件を取得します。
	 * @return 検索条件
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * 検索条件を設定します。
	 * @param condition 検索条件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * ソートアイテムを取得します。
	 * @return ソートアイテム
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートアイテムを設定します。
	 * @param sortItem ソートアイテム
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public RefSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(RefSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * 選択フィルターの検索件数を取得するメソッド
	 * @return 選択フィルターの検索件数
	 */
	public int getSelectFilterSearchPageSize() {
		return selectFilterSearchPageSize;
	}

	/**
	 * 選択フィルターの検索件数を設定するメソッド
	 * @param selectFilterSearchPageSize 選択フィルターの検索件数
	 */
	public void setSelectFilterSearchPageSize(int selectFilterSearchPageSize) {
		this.selectFilterSearchPageSize = selectFilterSearchPageSize;
	}

	/**
	 * 選択フィルターの検索パターンを取得するメソッド
	 * @return 選択フィルターの検索パターン
	 */
	public SelectFilterMatchPattern getSelectFilterSearchPattern() {
		return selectFilterSearchPattern;
	}

	/**
	 * 選択フィルターの検索パターンを設定するメソッド
	 * @param selectFilterSearchPattern 選択フィルターの検索パターン
	 */
	public void setSelectFilterSearchPattern(SelectFilterMatchPattern selectFilterSearchPattern) {
		this.selectFilterSearchPattern = selectFilterSearchPattern;
	}

	/**
	 * 選択フィルターの再度検索パターンを取得するメソッド
	 * @return 選択フィルターの再度検索パターン
	 */
	public SelectFilterResearchPattern getSelectFilterResearchPattern() {
		return selectFilterResearchPattern;
	}

	/**
	 * 選択フィルターの再度検索パターンを設定するメソッド
	 * @param selectFilterResearchPattern 選択フィルターの再度検索パターン
	 */
	public void setSelectFilterResearchPattern(SelectFilterResearchPattern selectFilterResearchPattern) {
		this.selectFilterResearchPattern = selectFilterResearchPattern;
	}

	/**
	 * 選択フィルターのプレースホルダーを取得するメソッド
	 * @return 選択フィルターのプレースホルダー
	 */
	public String getSelectFilterPlaceholder() {
		return selectFilterPlaceholder;
	}

	/**
	 * 選択フィルターのプレースホルダーを設定するメソッド
	 * @param selectFilterPlaceholder 選択フィルターのプレースホルダー
	 */
	public void setSelectFilterPlaceholder(String selectFilterPlaceholder) {
		this.selectFilterPlaceholder = selectFilterPlaceholder;
	}

	/**
	 * プレースホルダの多言語設定情報を取得します。
	 * @return プレースホルダの多言語設定情報リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedPlaceholderList() {
		return localizedPlaceholderList;
	}

	/**
	 * プレースホルダの多言語設定情報を設定します。
	 * @param localizedPlaceholderList プレースホルダの多言語設定情報リスト
	 */
	public void setLocalizedPlaceholderList(List<LocalizedStringDefinition> localizedPlaceholderList) {
		this.localizedPlaceholderList = localizedPlaceholderList;
	}

	/**
	 * プレースホルダの多言語設定情報を追加します。
	 * @param localizedPlaceholder プレースホルダの多言語設定情報
	 */
	public void addLocalizedPlaceholder(LocalizedStringDefinition localizedPlaceholder) {
		if (localizedPlaceholderList == null) {
			localizedPlaceholderList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedPlaceholderList.add(localizedPlaceholder);
	}
}
