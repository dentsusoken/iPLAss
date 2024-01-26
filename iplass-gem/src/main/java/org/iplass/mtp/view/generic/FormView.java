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

package org.iplass.mtp.view.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * 汎用データ画面のFormに相当するレイアウト情報
 *
 * @author lis3wg
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DetailFormView.class, SearchFormView.class, BulkFormView.class})
@FieldOrder(manual=true)
public abstract class FormView implements Refrectable {

	/** シリアルバージョンID */
	private static final long serialVersionUID = 4261191932604990665L;

	/** View名 */
	private String name;

	/** セクション */
	@MultiLang(isMultiLangValue = false)
	private List<Section> sections;

	/** タイトル */
	@MetaFieldInfo(
			displayName="画面タイトル",
			description="画面に表示されるタイトルを入力します",
			displayNameKey="generic_FormView_titleDisplaNameKey",
			descriptionKey="generic_FormView_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=10
	)
	@MultiLang(itemNameGetter = "getName")
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定情報",
			displayNameKey="generic_FormView_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=20
	)
	private List<LocalizedStringDefinition> localizedTitleList;




	/** イメージカラー */
	@MetaFieldInfo(
			displayName="イメージカラー",
			inputType=InputType.TEXT,
			description="ビューのイメージカラーを選択します。",
			displayNameKey="generic_FormView_imageColorDisplaNameKey",
			descriptionKey="generic_FormView_imageColorDescriptionKey",
			displayOrder=100
	)
	private String imageColor;

	/** アイコンタグ */
	@MetaFieldInfo(
			displayName="アイコンタグ",
			description="タイトルの前に表示するiタグ等を利用した独自のアイコンを設定できます。",
			displayNameKey="generic_FormView_iconTagDisplaNameKey",
			descriptionKey="generic_FormView_iconTagDescriptionKey",
			displayOrder=110
	)
	private String iconTag;

	/** ダイアログ表示時に最大化 */
	@MetaFieldInfo(
			displayName="ダイアログ表示時に最大化",
			inputType=InputType.CHECKBOX,
			displayOrder=120,
			description="ダイアログ表示時に最大化するかを設定します。",
			displayNameKey="generic_FormView_dialogMaximizeDisplaNameKey",
			descriptionKey="generic_FormView_dialogMaximizeDescriptionKey")
	private boolean dialogMaximize;




	/** ボタン */
	@MetaFieldInfo(
			displayName="ボタン",
			displayNameKey="generic_FormView_buttonsDisplaNameKey",
			inputType=InputType.REFERENCE,
			multiple=true,
			referenceClass=Button.class,
			displayOrder=1000,
			description="編集画面上下にカスタムボタンを設定します",
			descriptionKey="generic_FormView_buttonsDescriptionKey"
	)
	@MultiLang(itemNameGetter = "getName", isMultiLangValue = false)
	private List<Button> buttons;




	/** データを多言語化するかどうか */
	@MetaFieldInfo(
			displayName="データを多言語化",
			inputType=InputType.CHECKBOX,
			displayOrder=1500,
			description="データを多言語化するかを設定します。",
			displayNameKey="generic_FormView_localizationDataDisplaNameKey",
			descriptionKey="generic_FormView_localizationDataDescriptionKey")
	private boolean localizationData;

	/** カスタムスタイルキー */
	private String scriptKey;

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
	 * セクションを取得します。
	 * @return セクション
	 */
	public List<Section> getSections() {
		if (this.sections == null) this.sections = new ArrayList<Section>();
		return sections;
	}

	/**
	 * セクションを設定します。
	 * @param sections セクション
	 */
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	/**
	 * セクションを追加します。
	 * @param val セクション
	 */
	public void addSection(Section val) {
		getSections().add(val);
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
	public List<Button> getButtons() {
		if (this.buttons == null) this.buttons = new ArrayList<Button>();
		return buttons;
	}

	/**
	 * ボタンを設定します。
	 * @param buttons ボタン
	 */
	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	/**
	 * ボタンを追加します。
	 * @param button ボタン
	 */
	public void addButton(Button button) {
		getButtons().add(button);
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTitleList(List<LocalizedStringDefinition> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTitle(LocalizedStringDefinition localizedTitle) {
		if (localizedTitleList == null) {
			localizedTitleList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedTitleList.add(localizedTitle);
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
