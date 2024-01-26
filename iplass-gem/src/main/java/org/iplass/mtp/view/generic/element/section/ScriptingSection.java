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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * スクリプトセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/ScriptingSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class ScriptingSection extends Section {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8977266385152999890L;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_section_ScriptingSection_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集で非表示にするかを設定します。",
			descriptionKey="generic_element_section_ScriptingSection_hideDetailDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_section_ScriptingSection_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示で非表示にするかを設定します。",
			descriptionKey="generic_element_section_ScriptingSection_hideViewDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideView;

	/** タイトル */
	@MetaFieldInfo(
			displayName="タイトル",
			displayNameKey="generic_element_section_ScriptingSection_titleDisplaNameKey",
			description="セクションのタイトルを設定します。",
			descriptionKey="generic_element_section_ScriptingSection_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=300
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_section_ScriptingSection_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_section_ScriptingSection_styleDisplayNameKey",
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_section_ScriptingSection_styleDescriptionKey",
			displayOrder=320
	)
	private String style;

	/** id */
	@MetaFieldInfo(
			displayName="id",
			displayNameKey="generic_element_section_ScriptingSection_idDisplaNameKey",
			displayOrder=400,
			description="画面上で一意となるIDを設定してください。",
			descriptionKey="generic_element_section_ScriptingSection_idDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String id;

	/** リンクを表示するか */
	@MetaFieldInfo(
			displayName="リンクを表示するか",
			displayNameKey="generic_element_section_ScriptingSection_showLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=410,
			description="詳細画面でのページ内リンクを表示するかを指定します。",
			descriptionKey="generic_element_section_ScriptingSection_showLinkDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean showLink;

	/** スクリプト */
	@MetaFieldInfo(
			displayName="スクリプト",
			displayNameKey="generic_element_section_ScriptingSection_scriptDisplaNameKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=1000,
			description="HTMLコードを含むスクリプトを設定します。",
			descriptionKey="generic_element_section_ScriptingSection_scriptDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String script;

	/** セクション内に配置した場合に枠線を表示 */
	@MetaFieldInfo(
			displayName="セクション内に配置した場合に枠線を表示",
			displayNameKey="generic_element_section_ScriptingSection_dispBorderInSectionDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="セクション内に配置した場合に枠線を表示するかを指定します。",
			displayOrder=410,
			descriptionKey="generic_element_section_ScriptingSection_dispBorderInSectionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean dispBorderInSection;

	/** スクリプトのキー */
	private String key;

	/**
	 * デフォルトコンストラクタ
	 */
	public ScriptingSection() {
	}

	/**
	 * スクリプトを取得します。
	 * @return script スクリプト
	 */
	public String getScript() {
		return script;
	}

	/**
	 * スクリプトを設定します。
	 * @param script スクリプト
	 */
	public void setScript(String script) {
		this.script = script;
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
	 * リンクを表示するかを取得します。
	 * @return リンクを表示するか
	 */
	public boolean isShowLink() {
	    return showLink;
	}

	/**
	 * リンクを表示するかを設定します。
	 * @param showLink リンクを表示するか
	 */
	public void setShowLink(boolean showLink) {
	    this.showLink = showLink;
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
	 * スクリプトのキーを取得します。
	 * @return スクリプトのキー
	 */
	public String getKey() {
		return key;
	}

	/**
	 * スクリプトのキーを設定します。
	 * @param key スクリプトのキー
	 */
	public void setKey(String key) {
		this.key = key;
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
	 * セクション内に配置した場合に枠線を表示を取得します。
	 * @return セクション内に配置した場合に枠線を表示
	 */
	public boolean isDispBorderInSection() {
		return dispBorderInSection;
	}

	/**
	 * セクション内に配置した場合に枠線を表示を設定します。
	 * @param dispBorderInSection セクション内に配置した場合に枠線を表示
	 */
	public void setDispBorderInSection(boolean dispBorderInSection) {
		this.dispBorderInSection = dispBorderInSection;
	}

}
