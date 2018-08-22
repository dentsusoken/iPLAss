/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.element;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.TextAlign;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;

public class MetaVirtualProperty extends MetaElement {

	private static final long serialVersionUID = 9190843316518646290L;

	public static MetaVirtualProperty createInstance(Element element) {
		return new MetaVirtualProperty();
	}

	/** プロパティ名 */
	private String propertyName;

	/** 画面表示時のラベル */
	private String displayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayLabelList = new ArrayList<MetaLocalizedString>();

	/** クラス名 */
	private String style;

	/** 説明 */
	private String description;

	/** 説明の多言語設定情報 */
	private List<MetaLocalizedString> localizedDescriptionList = new ArrayList<MetaLocalizedString>();

	/** ツールチップ */
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	private List<MetaLocalizedString> localizedTooltipList = new ArrayList<MetaLocalizedString>();

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** 必須属性表示タイプ */
	private RequiredDisplayType requiredDisplayType;

	/** プロパティエディタ */
	private MetaPropertyEditor editor;

	/** 列幅 */
	private int width;

	/** テキストの配置 */
	private TextAlign textAlign;

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
	    return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
	    this.propertyName = propertyName;
	}

	/**
	 * 画面表示時のラベルを取得します。
	 * @return 画面表示時のラベル
	 */
	public String getDisplayLabel() {
	    return displayLabel;
	}

	/**
	 * 画面表示時のラベルを設定します。
	 * @param displayLabel 画面表示時のラベル
	 */
	public void setDisplayLabel(String displayLabel) {
	    this.displayLabel = displayLabel;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedDisplayLabelList() {
	    return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedDisplayLabelList 多言語設定情報
	 */
	public void setLocalizedDisplayLabelList(List<MetaLocalizedString> localizedDisplayLabelList) {
	    this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * クラス名を取得します。
	 * @return クラス名
	 */
	public String getStyle() {
	    return style;
	}

	/**
	 * クラス名を設定します。
	 * @param style クラス名
	 */
	public void setStyle(String style) {
	    this.style = style;
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
	 * 説明の多言語設定情報を取得します。
	 * @return 説明の多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedDescriptionList() {
	    return localizedDescriptionList;
	}

	/**
	 * 説明の多言語設定情報を設定します。
	 * @param localizedDescriptionList 説明の多言語設定情報
	 */
	public void setLocalizedDescriptionList(List<MetaLocalizedString> localizedDescriptionList) {
	    this.localizedDescriptionList = localizedDescriptionList;
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
	 * ツールチップの多言語設定情報を取得します。
	 * @return ツールチップの多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedTooltipList() {
	    return localizedTooltipList;
	}

	/**
	 * ツールチップの多言語設定情報を設定します。
	 * @param localizedTooltipList ツールチップの多言語設定情報
	 */
	public void setLocalizedTooltipList(List<MetaLocalizedString> localizedTooltipList) {
	    this.localizedTooltipList = localizedTooltipList;
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
	 * @param requiredDisplayType 必須属性表示タイプ
	 */
	public void setRequiredDisplayType(RequiredDisplayType requiredDisplayType) {
	    this.requiredDisplayType = requiredDisplayType;
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

	@Override
	public MetaVirtualProperty copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		VirtualPropertyItem property = (VirtualPropertyItem) element;
		super.fillFrom(property, definitionId);

		this.propertyName = property.getPropertyName();
		this.displayLabel = property.getDisplayLabel();
		this.style = property.getStyle();
		this.description = property.getDescription();
		this.tooltip = property.getTooltip();
		this.hideDetail = property.isHideDetail();
		this.hideView = property.isHideView();
		this.requiredDisplayType = property.getRequiredDisplayType();
		this.width = property.getWidth();
		this.textAlign = property.getTextAlign();

		MetaPropertyEditor editor = MetaPropertyEditor.createInstance(property.getEditor());
		if (editor != null) {
			property.getEditor().setPropertyName(propertyName);
			editor.applyConfig(property.getEditor());
			this.editor = editor;
		}

		// 言語毎の文字情報設定
		localizedDisplayLabelList = I18nUtil.toMeta(property.getLocalizedDisplayLabelList());

		// 説明の言語毎の文字情報設定
		localizedDescriptionList = I18nUtil.toMeta(property.getLocalizedDescriptionList());

		// ツールチップの言語毎の文字情報設定
		localizedTooltipList = I18nUtil.toMeta(property.getLocalizedTooltipList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		VirtualPropertyItem property = new VirtualPropertyItem();
		super.fillTo(property, definitionId);

		property.setPropertyName(propertyName);
		property.setDisplayLabel(displayLabel);
		property.setStyle(style);
		property.setDescription(description);
		property.setTooltip(tooltip);
		property.setHideDetail(hideDetail);
		property.setHideView(hideView);
		property.setRequiredDisplayType(requiredDisplayType);
		property.setWidth(width);
		property.setTextAlign(textAlign);
		if (editor != null) {
			property.setEditor(editor.currentConfig(propertyName));
		}
		property.setLocalizedDisplayLabelList(I18nUtil.toDef(localizedDisplayLabelList));
		property.setLocalizedDescriptionList(I18nUtil.toDef(localizedDescriptionList));
		property.setLocalizedTooltipList(I18nUtil.toDef(localizedTooltipList));
		return property;
	}

}
