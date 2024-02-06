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

package org.iplass.mtp.impl.view.generic.element.property;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.element.property.validation.MetaViewValidator;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

/**
 * プロパティレイアウト情報のメタデータ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaPropertyItem extends MetaPropertyLayout {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1940139489715422445L;

	public static MetaPropertyItem createInstance(Element element) {
		return new MetaPropertyItem();
	}

	/** 説明 */
	private String description;

	/** 説明の多言語設定情報 */
	private List<MetaLocalizedString> localizedDescriptionList = new ArrayList<MetaLocalizedString>();

	/** ツールチップ */
	private String tooltip;

	/** ツールチップの多言語設定情報 */
	private List<MetaLocalizedString> localizedTooltipList = new ArrayList<MetaLocalizedString>();

	/** 通常検索非表示設定 */
	private boolean hideNormalCondition;

	/** 詳細検索非表示設定 */
	private boolean hideDetailCondition;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** 必須属性表示タイプ */
	private RequiredDisplayType requiredDisplayType;

	/** 通常検索でブランク項目扱いにするか */
	private boolean isBlank;

	/** 通常検索で必須条件にする */
	private boolean requiredNormal;

	/** 詳細検索で必須条件にする */
	private boolean requiredDetail;

	/** 入力チェック */
	private MetaViewValidator validator;

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
	 * 通常検索非表示設定を取得します。
	 * @return 通常検索非表示設定
	 */
	public boolean isHideNormalCondition() {
		return hideNormalCondition;
	}

	/**
	 * 通常検索非表示設定を設定します。
	 * @param hideNormalCondition 通常検索非表示設定
	 */
	public void setHideNormalCondition(boolean hideNormalCondition) {
		this.hideNormalCondition = hideNormalCondition;
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
	 * 通常検索でブランク項目扱いにするかを取得します。
	 * @return 通常検索でブランク項目扱いにするか
	 */
	public boolean isBlank() {
		return isBlank;
	}

	/**
	 * 通常検索でブランク項目扱いにするかを設定します。
	 * @param isBlank 通常検索でブランク項目扱いにするか
	 */
	public void setBlank(boolean isBlank) {
		this.isBlank = isBlank;
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
	 * 入力チェックを取得します。
	 * @return 入力チェック
	 */
	public MetaViewValidator getValidator() {
	    return validator;
	}

	/**
	 * 入力チェックを設定します。
	 * @param validator 入力チェック
	 */
	public void setValidator(MetaViewValidator validator) {
	    this.validator = validator;
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

	@Override
	public MetaPropertyItem copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		PropertyItem prop = (PropertyItem) element;
		this.description = prop.getDescription();
		this.tooltip = prop.getTooltip();
		this.hideNormalCondition = prop.isHideNormalCondition();
		this.hideDetailCondition = prop.isHideDetailCondition();
		this.hideDetail = prop.isHideDetail();
		this.hideView = prop.isHideView();
		this.requiredDisplayType = prop.getRequiredDisplayType();
		this.isBlank = prop.isBlank();
		this.requiredNormal = prop.isRequiredNormal();
		this.requiredDetail = prop.isRequiredDetail();

		if (prop.getValidator() != null) {
			this.validator = MetaViewValidator.createInstance(prop.getValidator());
			this.validator.applyConfig(prop.getValidator(), definitionId);
		}

		// 説明の言語毎の文字情報設定
		localizedDescriptionList = I18nUtil.toMeta(prop.getLocalizedDescriptionList());

		// ツールチップの言語毎の文字情報設定
		localizedTooltipList = I18nUtil.toMeta(prop.getLocalizedTooltipList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		PropertyItem p = new PropertyItem();

		if (isBlank) {
			//検索条件のブランク項目の場合は他の項目を使わないのでフラグだけ設定
			p.setDispFlag(isDispFlag());
			p.setBlank(isBlank);
			return p;
		}

		super.fillTo(p, definitionId);

		//ReferencePropertyEditorは参照先Entityがない場合null。
		//その場合はElementもnullで返す。
		if (p.getEditor() == null) {
			return null;
		}

		//プロパティ名が取得できない場合はnullを返す
		if (p.getPropertyName() == null) {
			return null;
		}

		p.setDescription(this.description);
		p.setTooltip(this.tooltip);
		p.setHideNormalCondition(this.hideNormalCondition);
		p.setHideDetailCondition(this.hideDetailCondition);
		p.setHideDetail(this.hideDetail);
		p.setHideView(this.hideView);
		p.setRequiredDisplayType(this.requiredDisplayType);
		p.setRequiredNormal(this.requiredNormal);
		p.setRequiredDetail(this.requiredDetail);

		if (this.validator != null) {
			p.setValidator(validator.currentConfig(definitionId));
		}

		p.setLocalizedDescriptionList(I18nUtil.toDef(localizedDescriptionList));

		p.setLocalizedTooltipList(I18nUtil.toDef(localizedTooltipList));

		return p;
	}

}
