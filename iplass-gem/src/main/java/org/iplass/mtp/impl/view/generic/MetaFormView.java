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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.element.MetaButton;
import org.iplass.mtp.impl.view.generic.element.section.MetaMassReferenceSection;
import org.iplass.mtp.impl.view.generic.element.section.MetaReferenceSection;
import org.iplass.mtp.impl.view.generic.element.section.MetaSection;
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * レイアウト情報のスーパークラス
 * @author lis3wg
 */
@XmlSeeAlso({MetaDetailFormView.class, MetaSearchFormView.class, MetaBulkFormView.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MetaFormView implements MetaData {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1033584608508358891L;

	public static MetaFormView createInstance(FormView view) {
		if (view instanceof DetailFormView) {
			return MetaDetailFormView.createInstance(view);
		} else if (view instanceof SearchFormView) {
			return MetaSearchFormView.createInstance(view);
		} else if (view instanceof BulkFormView) {
			return MetaBulkFormView.createInstance(view);
		}
		return null;
	}

	/** View名 */
	private String name;

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<MetaLocalizedString>();

	/** データを多言語化するかどうか */
	private boolean localizationData;

	/** ダイアログ表示時に最大化 */
	private boolean dialogMaximize;

	/** セクション */
	private List<MetaSection> sections;

	/** イメージカラー */
	private String imageColor;

	/** アイコンタグ */
	private String iconTag;

	/** ボタン */
	private List<MetaButton> buttons;

	/** スクリプトのキー(内部用) */
	@XmlTransient
	String scriptKey;

	/**
	 * View名を取得します。
	 * @return View名
	 */
	public String getName() {
		return name;
	}

	/**
	 * View名を設定します。
	 * @param name View名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * タイトルを取得します。
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * データを多言語化するかどうかを取得します。
	 * @return データを多言語化するかどうか
	 */
	public boolean isLocalizationData() {
		return localizationData;
	}

	/**
	 * データを多言語化するかどうかを設定します。
	 * @param localizationData データを多言語化するかどうか
	 */
	public void setLocalizationData(boolean localizationData) {
		this.localizationData = localizationData;
	}

	/**
	 * ダイアログ表示時に最大化を取得します。
	 * @return ダイアログ表示時に最大化
	 */
	public boolean isDialogMaximize() {
	    return dialogMaximize;
	}

	/**
	 * ダイアログ表示時に最大化を設定します。
	 * @param dialogMaximize ダイアログ表示時に最大化
	 */
	public void setDialogMaximize(boolean dialogMaximize) {
	    this.dialogMaximize = dialogMaximize;
	}

	/**
	 * セクションを取得します。
	 * @return セクション
	 */
	public List<MetaSection> getSections() {
		if (this.sections == null) this.sections = new ArrayList<MetaSection>();
		return sections;
	}

	/**
	 * セクションを設定します。
	 * @param sections セクション
	 */
	public void setSections(List<MetaSection> sections) {
		this.sections = sections;
	}

	/**
	 * セクションを追加します。
	 * @param section セクション
	 */
	public void addSection(MetaSection section) {
		if (this.sections == null) this.sections = new ArrayList<MetaSection>();
		this.sections.add(section);
	}

	/**
	 * イメージカラーを取得します。
	 * @return イメージカラー
	 */
	public String getImageColor() {
	    return imageColor;
	}

	/**
	 * イメージカラーを設定します。
	 * @param imageColor イメージカラー
	 */
	public void setImageColor(String imageColor) {
	    this.imageColor = imageColor;
	}

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	/**
	 * ボタンを取得します。
	 * @return ボタン
	 */
	public List<MetaButton> getButtons() {
		if (this.buttons == null) this.buttons = new ArrayList<MetaButton>();
		return buttons;
	}

	/**
	 * ボタンを設定します。
	 * @param buttons ボタン
	 */
	public void setButtons(List<MetaButton> buttons) {
		this.buttons = buttons;
	}

	/**
	 * ボタンを追加します。
	 * @param button ボタン
	 */
	public void addButton(MetaButton button) {
		getButtons().add(button);
	}

	public List<MetaLocalizedString> getLocalizedTitleList() {
		return localizedTitleList;
	}

	public void setLocalizedTitleList(List<MetaLocalizedString> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	@Override
	public MetaFormView copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * レイアウト情報の内容を自身に適用します。
	 * @param form レイアウト情報
	 * @param definitionId Entity定義のID
	 */
	public abstract void applyConfig(FormView form, String definitionId);

	/**
	 * レイアウト情報の内容を自身に適用します。
	 * @param form レイアウト情報
	 * @param definitionId Entity定義のID
	 */
	protected void fillFrom(FormView form, String definitionId) {
		this.name = form.getName();
		this.title = form.getTitle();
		this.localizationData = form.isLocalizationData();
		this.dialogMaximize = form.isDialogMaximize();
		this.imageColor = form.getImageColor();
		this.iconTag = form.getIconTag();
		for (Button button : form.getButtons()) {
			MetaButton meta = new MetaButton();
			meta.applyConfig(button, definitionId);
			addButton(meta);
		}

		if (form.getSections().size() > 0) {
			for (Section section : form.getSections()) {
				MetaSection meta = MetaSection.createInstance(section);
				meta.applyConfig(section, definitionId);
				if (section instanceof ReferenceSection) {
					MetaReferenceSection rs = (MetaReferenceSection) meta;
					if (rs.getPropertyId() != null) this.addSection(rs);
				} else if (section instanceof MassReferenceSection) {
					MetaMassReferenceSection ms = (MetaMassReferenceSection) meta;
					if (ms.getPropertyId() != null) this.addSection(ms);
				} else {
					this.addSection(meta);
				}
			}
		}

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(form.getLocalizedTitleList());
	}

	/**
	 * 自身の内容をレイアウト情報に適用します。
	 * @param definitionId Entity定義のID
	 * @return レイアウト情報
	 */
	public abstract FormView currentConfig(String definitionId);

	/**
	 * 自身の内容をレイアウト情報に適用します。
	 * @param form レイアウト情報
	 * @param definitionId Entity定義のID
	 */
	protected void fillTo(FormView form, String definitionId) {

		form.setScriptKey(scriptKey);

		form.setName(this.name);
		form.setTitle(this.title);
		form.setLocalizationData(this.localizationData);
		form.setDialogMaximize(this.dialogMaximize);
		form.setImageColor(this.imageColor);
		form.setIconTag(this.iconTag);
		for (MetaButton button : getButtons()) {
			form.addButton((Button) button.currentConfig(definitionId));
		}

		if (this.getSections().size() > 0) {
			for (MetaSection section : this.getSections()) {
				Section s = (Section) section.currentConfig(definitionId);
				if (s != null) form.addSection(s);
			}
		}

		form.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

	}

	/**
	 * ランタイムを生成します。
	 * @return ランタイム
	 */
	public FormViewRuntime createRuntime(EntityViewRuntime entityView) {
		return new FormViewRuntime(this, entityView);
	}

	public String getTypeName() {
		return this.getClass().getSimpleName();
	}
}
