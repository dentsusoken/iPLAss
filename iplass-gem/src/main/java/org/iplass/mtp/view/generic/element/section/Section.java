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
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.element.Element;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * フォーム内の要素を複数保持できるセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ TemplateSection.class, DefaultSection.class, ScriptingSection.class, VersionSection.class,
		SearchConditionSection.class, ReferenceSection.class, MassReferenceSection.class, SearchResultSection.class })
@FieldOrder(manual=true)
public abstract class Section extends Element {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8864732096685759293L;

	/** タイトル */
	@MetaFieldInfo(
			displayName="タイトル",
			displayNameKey="generic_element_section_Section_titleDisplayNameKey",
			description="セクションのタイトルを設定します。",
			descriptionKey="generic_element_section_Section_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=300
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_section_Section_localizedTitleListDisplayNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_section_Section_styleDisplayNameKey",
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_section_Section_styleDescriptionKey",
			displayOrder=320
	)
	private String style;

	/** id */
	@MetaFieldInfo(
			displayName="id",
			displayNameKey="generic_element_section_Section_idDisplayNameKey",
			displayOrder=330,
			description="画面上で一意となるIDを設定してください。",
			descriptionKey="generic_element_section_Section_idDescriptionKey"
	)
	private String id;

	/** セクション高さ設定（px） */
	@MetaFieldInfo(
			displayName = "セクション高さ設定（px）",
			displayNameKey = "generic_element_section_Section_sectionHeightDisplayNameKey",
			inputType = InputType.NUMBER,
			displayOrder = 350,
			description = "セクションの高さをピクセル単位で指定します。",
			descriptionKey = "generic_element_section_Section_sectionHeightDescriptionKey"
	)
	private int sectionHeight;

	/**
	 * タイトルを取得します。
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * タイトルを設定します。
	 * @return タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * idを取得します。
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * idを設定します。
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * セクション高さ設定（px）を取得します。
	 * @return セクション高さ設定（px）
	 */
	public int getSectionHeight() {
		return sectionHeight;
	}

	/**
	 * セクション高さ設定（px）を設定します。
	 * @param sectionHeight セクション高さ設定（px）
	 */
	public void setSectionHeight(int sectionHeight) {
		this.sectionHeight = sectionHeight;
	}

	public abstract boolean isShowLink();
}
