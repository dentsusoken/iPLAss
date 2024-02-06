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
import java.util.List;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.HasEntityProperty;
import org.iplass.mtp.impl.view.generic.common.MetaAutocompletionSetting;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.TextAlign;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;

/**
 *
 * @author lis3wg
 */
public class MetaNestProperty implements MetaData, HasEntityProperty {

	/** SerialVersionUID */
	private static final long serialVersionUID = -5324754433567181221L;

	/** プロパティID */
	private String propertyId;

	/** 表示名 */
	private String displayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayLabelList = new ArrayList<>();

	/** 説明 */
	private String description;

	/** 説明の多言語設定情報 */
	private List<MetaLocalizedString> localizedDescriptionList = new ArrayList<>();

	/** ツールチップ */
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	private List<MetaLocalizedString> localizedTooltipList = new ArrayList<>();

	/** 列幅 */
	private int width;

	/** テキストの配置 */
	private TextAlign textAlign;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** 必須属性表示タイプ */
	private RequiredDisplayType requiredDisplayType;

	/** 通常検索で必須条件にする */
	private boolean requiredNormal;

	/** 詳細検索で必須条件にする */
	private boolean requiredDetail;

	/** ソートを許可 */
	private boolean sortable = true;

	/** CSVの出力 */
	private boolean outputCsv = true;

	/** プロパティエディタ */
	private MetaPropertyEditor editor;

	/** 自動補完設定 */
	private MetaAutocompletionSetting autocompletionSetting;

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
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * 表示名を設定します。
	 * @param displayLabel 表示名
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * 説明を取得します。
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 説明を設定します。
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ツールチップを取得します。
	 * @return ツールチップ
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * ツールチップを設定します。
	 * @param tooltip ツールチップ
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * 列幅を取得します。
	 * @return 列幅
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 列幅を設定します。
	 * @param width 列幅
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * テキストの配置を取得します。
	 * @return テキストの配置
	 */
	public TextAlign getTextAlign() {
		return textAlign;
	}

	/**
	 * テキストの配置を設定します。
	 * @param textAlign テキストの配置
	 */
	public void setTextAlign(TextAlign textAlign) {
		this.textAlign = textAlign;
	}

	/**
	 * 詳細編集非表示設定を取得します。
	 * @return 詳細編集非表示設定
	 */
	public boolean isHideDetail() {
		return hideDetail;
	}

	/**
	 * 詳細編集非表示設定を設定します。
	 * @param hideDetail 詳細編集非表示設定
	 */
	public void setHideDetail(boolean hideDetail) {
		this.hideDetail = hideDetail;
	}

	/**
	 * 詳細表示非表示設定を取得します。
	 * @return 詳細表示非表示設定
	 */
	public boolean isHideView() {
		return hideView;
	}

	/**
	 * 詳細表示非表示設定を設定します。
	 * @param hideView 詳細表示非表示設定
	 */
	public void setHideView(boolean hideView) {
		this.hideView = hideView;
	}

	/**
	 * 必須属性表示タイプを取得します。
	 * @return 必須属性表示タイプ
	 */
	public RequiredDisplayType getRequiredDisplayType() {
		return requiredDisplayType;
	}

	/**
	 * 必須属性表示タイプを設定します。
	 * @param requiredDisplayTypel 必須属性表示タイプ
	 */
	public void setRequiredDisplayType(RequiredDisplayType requiredDisplayType) {
		this.requiredDisplayType = requiredDisplayType;
	}

	/**
	 * 通常検索で必須条件にするを取得します。
	 * @return 通常検索で必須条件にする
	 */
	public boolean isRequiredNormal() {
		return requiredNormal;
	}

	/**
	 * 通常検索で必須条件にするを設定します。
	 * @param requiredNormal 通常検索で必須条件にする
	 */
	public void setRequiredNormal(boolean requiredNormal) {
		this.requiredNormal = requiredNormal;
	}

	/**
	 * 詳細検索で必須条件にするを取得します。
	 * @return 詳細検索で必須条件にする
	 */
	public boolean isRequiredDetail() {
		return requiredDetail;
	}

	/**
	 * 詳細検索で必須条件にするを設定します。
	 * @param requiredDetail 詳細検索で必須条件にする
	 */
	public void setRequiredDetail(boolean requiredDetail) {
		this.requiredDetail = requiredDetail;
	}

	/**
	 * ソートを許可するかを取得します。
	 * @return ソートを許可するか
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * ソートを許可するかを設定します。
	 * @param sortable ソートを許可するか
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * CSVに出力するかを取得します。
	 * @return CSVに出力するか
	 */
	public boolean isOutputCsv() {
		return outputCsv;
	}

	/**
	 * CSVに出力するかを設定します。
	 * @param outputCsv CSVに出力するか
	 */
	public void setOutputCsv(boolean outputCsv) {
		this.outputCsv = outputCsv;
	}

	/**
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	public MetaPropertyEditor getEditor() {
		return editor;
	}

	/**
	 * プロパティエディタを設定します。
	 * @param editor プロパティエディタ
	 */
	public void setEditor(MetaPropertyEditor editor) {
		this.editor = editor;
	}

	/**
	 * 自動補完設定を取得します。
	 * @return setting 自動補完設定
	 */
	public MetaAutocompletionSetting getAutocompletionSetting() {
		return autocompletionSetting;
	}

	/**
	 * 自動補完設定を設定します。
	 * @param autocompletionSetting 自動補完設定
	 */
	public void setAutocompletionSetting(MetaAutocompletionSetting autocompletionSetting) {
		this.autocompletionSetting = autocompletionSetting;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedDisplayLabelList() {
		return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayLabelList(List<MetaLocalizedString> localizedDisplayLabelList) {
		this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * 説明の多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedDescriptionList() {
		return localizedDescriptionList;
	}

	/**
	 * 説明の多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDescriptionList(List<MetaLocalizedString> localizedDescriptionList) {
		this.localizedDescriptionList = localizedDescriptionList;
	}

	/**
	 * ツールチップの多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedTooltipList() {
		return localizedTooltipList;
	}

	/**
	 * ツールチップの多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTooltipList(List<MetaLocalizedString> localizedTooltipList) {
		this.localizedTooltipList = localizedTooltipList;
	}

	/**
	 * DefinitionをMetaDataに変換します。
	 *
	 * @param property プロパティ定義
	 * @param referenceEntity 参照先Entity定義
	 * @param fromEntity 参照元Entity定義
	 * @param rootEntity ルートEntity定義
	 */
	public void applyConfig(NestProperty property, EntityHandler referenceEntity, EntityHandler fromEntity, EntityHandler rootEntity) {
		EntityContext ctx = EntityContext.getCurrentContext();
		PropertyHandler ph = referenceEntity.getProperty(property.getPropertyName(), ctx);
//		if (ph == null || ph.getMetaData().getMultiplicity() != 1) return;
		//検索画面が多重度1でないプロパティを扱えるのでチェックを変更
		//ただし、詳細画面は変わらず扱えないため、JSP側ではじく
		if (ph == null) return;

		propertyId = ph.getId();
		displayLabel = property.getDisplayLabel();
		description = property.getDescription();
		tooltip = property.getTooltip();
		width = property.getWidth();
		textAlign = property.getTextAlign();
		hideDetail = property.isHideDetail();
		hideView = property.isHideView();
		requiredDisplayType = property.getRequiredDisplayType();
		requiredNormal = property.isRequiredNormal();
		requiredDetail = property.isRequiredDetail();
		sortable = property.isSortable();
		outputCsv = property.isOutputCsv();

		MetaPropertyEditor editor = MetaPropertyEditor.createInstance(property.getEditor());

		if (property.getEditor() instanceof JoinPropertyEditor) {
			((JoinPropertyEditor) property.getEditor()).setObjectName(referenceEntity.getMetaData().getName());
		} else if (property.getEditor() instanceof DateRangePropertyEditor) {
			((DateRangePropertyEditor) property.getEditor()).setObjectName(referenceEntity.getMetaData().getName());
		} else if (property.getEditor() instanceof NumericRangePropertyEditor) {
			((NumericRangePropertyEditor) property.getEditor()).setObjectName(referenceEntity.getMetaData().getName());
		} else if (property.getEditor() instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor) property.getEditor();
			if (ph instanceof ReferencePropertyHandler) {
				// 参照先Entity名をセット
				ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
				String objName = rph.getReferenceEntityHandler(ctx).getMetaData().getName();
				rpe.setObjectName(objName);
				// 参照元Entity名をセット
				if (rph.getParent() != null) {
					rpe.setReferenceFromObjectName(rph.getParent().getMetaData().getName());
				}
				// ルートEntity名をセット
				if (rootEntity != null) {
					rpe.setRootObjectName(rootEntity.getMetaData().getName());
				}
			}
		}

		if (editor != null) {
			property.getEditor().setPropertyName(property.getPropertyName());
			editor.applyConfig(property.getEditor());
			this.editor = editor;
		}

		if (property.getAutocompletionSetting() != null) {
			autocompletionSetting = MetaAutocompletionSetting.createInstance(property.getAutocompletionSetting());
			autocompletionSetting.applyConfig(property.getAutocompletionSetting(), referenceEntity, rootEntity);
		}

		// 言語毎の文字情報設定
		localizedDisplayLabelList = I18nUtil.toMeta(property.getLocalizedDisplayLabelList());

		// 説明の言語毎の文字情報設定
		localizedDescriptionList = I18nUtil.toMeta(property.getLocalizedDescriptionList());

		// ツールチップの言語毎の文字情報設定
		localizedTooltipList = I18nUtil.toMeta(property.getLocalizedTooltipList());

	}

	/**
	 * MetaDataをDefinitionに変換します。
	 *
	 * @param referenceEntity 参照先Entity定義
	 * @param fromEntity 参照元Entity定義
	 * @param rootEntity ルートEntity定義
	 * @return Definition
	 */
	public NestProperty currentConfig(EntityHandler referenceEntity, EntityHandler fromEntity, EntityHandler rootEntity) {
		EntityContext ctx = EntityContext.getCurrentContext();
		PropertyHandler ph = referenceEntity.getPropertyById(propertyId, ctx);
//		if (ph == null || ph.getMetaData().getMultiplicity() != 1) return null;
		//検索画面が多重度1でないプロパティを扱えるのでチェックを変更
		//ただし、詳細画面は変わらず扱えないため、JSP側ではじく
		if (ph == null) return null;

		NestProperty property = new NestProperty();
		property.setPropertyName(ph.getName());
		property.setDisplayLabel(displayLabel);
		property.setDescription(description);
		property.setTooltip(tooltip);
		property.setWidth(width);
		property.setTextAlign(textAlign);
		property.setHideDetail(hideDetail);
		property.setHideView(hideView);
		property.setRequiredDisplayType(requiredDisplayType);
		property.setRequiredNormal(requiredNormal);
		property.setRequiredDetail(requiredDetail);
		property.setSortable(sortable);
		property.setOutputCsv(outputCsv);
		if (editor != null) {
			property.setEditor(editor.currentConfig(ph.getName()));
		}

		if (autocompletionSetting != null) {
			property.setAutocompletionSetting(autocompletionSetting.currentConfig(referenceEntity, rootEntity));
		}

		property.setLocalizedDisplayLabelList(I18nUtil.toDef(localizedDisplayLabelList));

		property.setLocalizedDescriptionList(I18nUtil.toDef(localizedDescriptionList));

		property.setLocalizedTooltipList(I18nUtil.toDef(localizedTooltipList));

		return property;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
