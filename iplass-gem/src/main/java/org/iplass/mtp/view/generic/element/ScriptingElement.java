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

package org.iplass.mtp.view.generic.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * スクリプトを表す要素
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/ScriptingElement.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class ScriptingElement extends Element {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2708119901042084930L;

	/** タイトル */
	@MetaFieldInfo(displayName="タイトル", description="ヘッダに表示するタイトルを設定します。", useMultiLang=true,
			displayNameKey="generic_element_ScriptingElement_titleDisplaNameKey",
			descriptionKey="generic_element_ScriptingElement_titleDescriptionKey")
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_ScriptingElement_localizedTitleListDisplaNameKey",
			inputType=InputType.LANGUAGE
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_ScriptingElement_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細編集の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_ScriptingElement_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_ScriptingElement_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="詳細表示の項目として非表示にするかを設定します。",
			descriptionKey="generic_element_ScriptingElement_hideViewDescriptionKey"
	)
	private boolean hideView;

	/** スクリプト */
	@MetaFieldInfo(displayName="スクリプト", inputType=InputType.SCRIPT,
			mode="groovytemplate",
			description="HTMLコードを含むスクリプトを設定します。",
			displayNameKey="generic_element_ScriptingElement_scriptDisplaNameKey",
			descriptionKey="generic_element_ScriptingElement_scriptDescriptionKey")
	private String script;

	/** スクリプトのキー */
	private String key;

	/**
	 * デフォルトコンストラクタ
	 */
	public ScriptingElement() {
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

}
