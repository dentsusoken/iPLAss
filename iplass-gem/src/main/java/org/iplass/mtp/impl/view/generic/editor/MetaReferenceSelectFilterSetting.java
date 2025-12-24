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

package org.iplass.mtp.impl.view.generic.editor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;
import org.iplass.mtp.view.generic.editor.ReferenceSelectFilterSetting;
import org.iplass.mtp.view.generic.editor.ReferenceSelectFilterSetting.SelectFilterMatchPattern;
import org.iplass.mtp.view.generic.editor.ReferenceSelectFilterSetting.SelectFilterResearchPattern;

/**
 * 選択フィルター設定
 * 
 * @author lish0p
 */
public class MetaReferenceSelectFilterSetting implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = 6970880710124556126L;

	/** プロパティID */
	private String propertyId;

	/** 検索条件 */
	private String condition;

	/** ソートアイテム */
	private String sortItem;

	/** ソート種別 */
	private RefSortType sortType;

	/** 選択フィルターの検索件数を保持するフィールド */
	private int selectFilterSearchPageSize;

	/** 選択フィルターの検索パターンを保持するフィールド */
	private SelectFilterMatchPattern selectFilterSearchPattern;

	/** 選択フィルター再度検索パターン */
	private SelectFilterResearchPattern selectFilterResearchPattern;

	/** 選択フィルターのプレースホルダーを保持するフィールド */
	private String selectFilterPlaceholder;

	/** 選択フィルターのプレースホルダーの多言語設定情報 */
	private List<MetaLocalizedString> localizedPlaceholderList = new ArrayList<MetaLocalizedString>();

	// 排除するプロパティタイプ（将来拡張しやすい形で保持）
	private static final Set<PropertyDefinitionType> EXCLUDED_TYPES = EnumSet.of(
			PropertyDefinitionType.BINARY,
			PropertyDefinitionType.TIME,
			PropertyDefinitionType.DATE,
			PropertyDefinitionType.DATETIME,
			PropertyDefinitionType.BOOLEAN,
			PropertyDefinitionType.EXPRESSION,
			PropertyDefinitionType.LONGTEXT,
			PropertyDefinitionType.FLOAT);

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * プロパティIDを取得します。
	 * @return プロパティID
	 */
	public String getPropertyId() {
	    return propertyId;
	}

	/**
	 * プロパティIDを設定します。
	 * @param propertyId プロパティID
	 */
	public void setPropertyId(String propertyId) {
	    this.propertyId = propertyId;
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
	 * 選択フィルター再度検索パターンを取得するメソッド
	 * @return 選択フィルター再度検索パターン
	 */
	public SelectFilterResearchPattern getSelectFilterResearchPattern() {
		return selectFilterResearchPattern;
	}

	/**
	 * 選択フィルター再度検索パターンを設定するメソッド
	 * @param selectFilterResearchPattern 選択フィルター再度検索パターン
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
	 * プレースホルダーの多言語設定情報を取得するメソッド
	 * @return プレースホルダーの多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedPlaceholderList() {
		return localizedPlaceholderList;
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
	 * プレースホルダーの多言語設定情報を設定するメソッド
	 * @param localizedPlaceholderList プレースホルダーの多言語設定情報
	 */
	public void setLocalizedPlaceholderList(List<MetaLocalizedString> localizedPlaceholderList) {
		this.localizedPlaceholderList = localizedPlaceholderList;
	}

	/**
	 * 除外するプロパティタイプかどうかを判定します。
	 * @param pd プロパティタイプ
	 * @return 除外するプロパティタイプの場合はtrue、そうでない場合はfalse
	 */
	private static boolean isExcludedPropertyType(PropertyDefinitionType pd) {
		return pd != null && EXCLUDED_TYPES.contains(pd);
	}

	/**
	 * 設定情報を適用します。
	 * @param setting 設定情報
	 * @param entity エンティティハンドラ
	 */
	public void applyConfig(ReferenceSelectFilterSetting setting, EntityHandler entity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler property = entity.getProperty(setting.getPropertyName(), ctx);
		if (property == null)
			return;

		PropertyDefinitionType pd = property.getEnumType();
		if (isExcludedPropertyType(pd)) {
			throw new IllegalArgumentException("Invalid property type for ReferenceSelectFilterSetting: " + pd);
		}

		this.propertyId = property.getId();
		this.condition = setting.getCondition();

		if (setting.getSortItem() != null && !setting.getSortItem()
				.isEmpty()) {
			PropertyHandler sortProperty = entity.getProperty(setting.getSortItem(), ctx);
			if (sortProperty != null) {
				this.sortItem = sortProperty.getId();
			}
		}
		this.sortType = setting.getSortType();
		this.selectFilterSearchPageSize = setting.getSelectFilterSearchPageSize();
		this.selectFilterSearchPattern = setting.getSelectFilterSearchPattern();
		this.selectFilterResearchPattern = setting.getSelectFilterResearchPattern();
		this.selectFilterPlaceholder = setting.getSelectFilterPlaceholder();
		this.localizedPlaceholderList = I18nUtil.toMeta(setting.getLocalizedPlaceholderList());

	}

	/**
	 * 現在の設定情報を取得します。
	 * @param entity エンティティハンドラ
	 * @return 設定情報
	 */
	public ReferenceSelectFilterSetting currentConfig(EntityHandler entity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler property = entity.getPropertyById(propertyId, ctx);
		if (property == null)
			return null;

		//プロパティ名を取得
		ReferenceSelectFilterSetting setting = new ReferenceSelectFilterSetting();

		setting.setPropertyName(property.getName());
		setting.setCondition(this.condition);

		if (sortItem != null) {
			PropertyHandler sortProperty = entity.getPropertyById(sortItem, ctx);
			if (sortProperty != null) {
				setting.setSortItem(sortProperty.getName());
			}
		}

		setting.setSortType(sortType);
		setting.setSelectFilterSearchPageSize(selectFilterSearchPageSize);
		setting.setSelectFilterSearchPattern(selectFilterSearchPattern);
		setting.setSelectFilterResearchPattern(selectFilterResearchPattern);
		setting.setSelectFilterPlaceholder(selectFilterPlaceholder);
		setting.setLocalizedPlaceholderList(I18nUtil.toDef(localizedPlaceholderList));

		return setting;
	}

}
