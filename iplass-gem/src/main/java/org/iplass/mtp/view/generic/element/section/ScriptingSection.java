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

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * スクリプトセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/ScriptingSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class ScriptingSection extends AdjustableHeightSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8977266385152999890L;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_section_ScriptingSection_hideDetailDisplayNameKey",
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
			displayNameKey="generic_element_section_ScriptingSection_hideViewDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示で非表示にするかを設定します。",
			descriptionKey="generic_element_section_ScriptingSection_hideViewDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean hideView;

	/** セクション内に配置した場合に枠線を表示 */
	@MetaFieldInfo(
			displayName="セクション内に配置した場合に枠線を表示",
			displayNameKey="generic_element_section_ScriptingSection_dispBorderInSectionDisplayNameKey",
			inputType=InputType.CHECKBOX,
			description="セクション内に配置した場合に枠線を表示するかを指定します。",
			displayOrder=400,
			descriptionKey="generic_element_section_ScriptingSection_dispBorderInSectionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private boolean dispBorderInSection;

	/** リンクを表示するか */
	@MetaFieldInfo(
			displayName="リンクを表示するか",
			displayNameKey="generic_element_section_ScriptingSection_showLinkDisplayNameKey",
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
			displayNameKey="generic_element_section_ScriptingSection_scriptDisplayNameKey",
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

	/** スクリプトのキー */
	private String key;

	/**
	 * デフォルトコンストラクタ
	 */
	public ScriptingSection() {
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

}
