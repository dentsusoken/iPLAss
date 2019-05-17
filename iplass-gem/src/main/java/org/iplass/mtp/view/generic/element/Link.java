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
import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * リンクを表す要素
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/Link.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@FieldOrder(manual=true)
public class Link extends Element {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 131576062106926116L;

	/** タイトル */
	@MetaFieldInfo(displayName="タイトル", description="ヘッダに表示するタイトルを設定します。",
			displayNameKey="generic_element_Link_titleDisplaNameKey",
			descriptionKey="generic_element_Link_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=300
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_Link_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** 表示ラベル */
	@MetaFieldInfo(displayName="表示ラベル", description="リンクに表示するラベルを設定します。",
			displayNameKey="generic_element_Link_displayLabelDisplaNameKey",
			descriptionKey="generic_element_Link_displayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDisplayLabelList",
			displayOrder=320
	)
	@MultiLang()
	private String displayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_Link_localizedDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=330
	)
	private List<LocalizedStringDefinition> localizedDisplayLabelList;




	/** URL */
	@MetaFieldInfo(
			displayName="URL",
			description="リンクのURLを設定します。",
			displayOrder=1000,
			displayNameKey="generic_element_Link_urlDisplaNameKey",
			descriptionKey="generic_element_Link_urlDescriptionKey")
	private String url;

	/** 別ウィンドウで表示するか */
	@MetaFieldInfo(
			displayName="別ウィンドウで表示",
			displayNameKey="generic_element_Link_dispNewWindowDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1010,
			description="別ウィンドウで表示するかを指定します。",
			descriptionKey="generic_element_Link_dispNewWindowDescriptionKey"
	)
	private boolean dispNewWindow;




	/** 入力カスタムスタイル */
	@MetaFieldInfo(
			displayName="入力カスタムスタイル",
			displayNameKey="generic_element_Link_inputCustomStyleDisplayNameKey",
			description="編集画面のinput要素に対するスタイルを指定します。(例)width:100px;",
			descriptionKey="generic_element_Link_inputCustomStyleDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="CSS",
			displayOrder=2000
	)
	private String inputCustomStyle;

	/** 入力カスタムスタイルキー */
	private String inputCustomStyleScriptKey;

	/**
	 * デフォルトコンストラクタ
	 */
	public Link() {
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
	 * 表示ラベルを取得します。
	 * @return 表示ラベル
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * 表示ラベルを設定します。
	 * @param displayLabel 表示ラベル
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * URLを取得します。
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * URLを設定します。
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 別ウィンドウで表示するかを取得します。
	 * @return 別ウィンドウで表示するか
	 */
	public boolean isDispNewWindow() {
		return dispNewWindow;
	}

	/**
	 * 別ウィンドウで表示するかを設定します。
	 * @param dispNewWindow 別ウィンドウで表示するか
	 */
	public void setDispNewWindow(boolean dispNewWindow) {
		this.dispNewWindow = dispNewWindow;
	}

	/**
	 * 入力カスタムスタイルを取得します。
	 * @return 入力カスタムスタイル
	 */
	public String getInputCustomStyle() {
		return inputCustomStyle;
	}

	/**
	 * 入力カスタムスタイルを設定します。
	 * @param inputCustomStyle 入力カスタムスタイル
	 */
	public void setInputCustomStyle(String inputCustomStyle) {
		this.inputCustomStyle = inputCustomStyle;
	}

	/**
	 * 入力カスタムスタイルキー
	 * @return 入力カスタムスタイルキー
	 */
	public String getInputCustomStyleScriptKey() {
		return inputCustomStyleScriptKey;
	}

	/**
	 * 入力カスタムスタイルキーを設定します。
	 * @param inputCustomStyleScriptKey 入力カスタムスタイルキー
	 */
	public void setInputCustomStyleScriptKey(String inputCustomStyleScriptKey) {
		this.inputCustomStyleScriptKey = inputCustomStyleScriptKey;
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
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayLabelList() {
		return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayLabelList(List<LocalizedStringDefinition> localizedDisplayLabelList) {
		this.localizedDisplayLabelList = localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedDisplayLabel(LocalizedStringDefinition localizedDisplayLabel) {
		if (localizedDisplayLabelList == null) {
			localizedDisplayLabelList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayLabelList.add(localizedDisplayLabel);
	}

}
