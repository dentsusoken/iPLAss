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
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * ボタンを表す要素
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/Button.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class Button extends Element {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5328561239331062170L;

	/** 表示タイプ */
	@MetaFieldInfo(
			displayName="表示タイプ",
			displayNameKey="generic_element_Button_displayTypeDisplaNameKey",
			description="詳細編集、詳細表示のどちらに表示するかを設定します。",
			descriptionKey="generic_element_Button_displayTypeDescriptionKey",
			inputType=InputType.ENUM,
			enumClass=DisplayType.class
	)
	@EntityViewField(referenceTypes=FieldReferenceType.DETAIL)
	private DisplayType displayType;

	/** タイトル */
	@MetaFieldInfo(displayName="タイトル", description="ヘッダに表示するタイトルを設定します。", 
			displayNameKey="generic_element_Button_titleDisplaNameKey",
			descriptionKey="generic_element_Button_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList"
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_Button_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** 表示ラベル */
	@MetaFieldInfo(displayName="表示ラベル", description="ボタンに表示するラベルを設定します。", 
			displayNameKey="generic_element_Button_displayLabelDisplaNameKey",
			descriptionKey="generic_element_Button_displayLabelDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedDisplayLabelList"
	)
	@MultiLang()
	private String displayLabel;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_Button_localizedDisplayLabelListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST
	)
	private List<LocalizedStringDefinition> localizedDisplayLabelList;

	/** プライマリー */
	@MetaFieldInfo(
			displayName="プライマリー",
			displayNameKey="generic_element_Button_primaryDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="設定するとボタンが強調表示されます。",
			descriptionKey="generic_element_Button_primaryDescriptionKey"
	)
	private boolean primary = true;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_Button_styleDisplaNameKey",
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_Button_styleDescriptionKey"
	)
	private String style;

	/** 入力カスタムスタイル */
	@MetaFieldInfo(
			displayName="入力カスタムスタイル",
			displayNameKey="generic_element_Button_inputCustomStyleDisplayNameKey",
			description="編集画面のinput要素に対するスタイルを指定します。(例)width:100px;",
			descriptionKey="generic_element_Button_inputCustomStyleDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="CSS"
	)
	private String inputCustomStyle;

	/** クリックイベント */
	@MetaFieldInfo(
			displayName="クリックイベント",
			displayNameKey="generic_element_Button_onclickEventDisplaNameKey",
			description="クリック時に実行されるJavaScriptコードを設定します。",
			descriptionKey="generic_element_Button_onclickEventDescriptionKey"
	)
	private String onclickEvent;

	/** 入力カスタムスタイルキー */
	private String inputCustomStyleScriptKey;

	/** 表示判定用スクリプト */
	@MetaFieldInfo(
			displayName="表示判定用スクリプト",
			displayNameKey="generic_element_Button_customDisplayTypeScriptDisplayNameKey",
			inputType=InputType.SCRIPT,
			mode="groovyscript",
			description="表示タイプ：Customの場合に、表示可否を判定するスクリプトを設定します。<BR />" +
					"スクリプトの実行結果がtrueの場合、ボタンが表示されます。<BR />" +
					"以下のオブジェクトがバインドされています。<BR />" +
					"バインド変数名  ：内容<BR />" +
					"request         ：リクエスト<BR />" +
					"session         ：セッション<BR />" +
					"user            ：ユーザ<BR />" +
					"outputType      ：表示タイプ<BR />" +
					"entity          ：表示対象のエンティティ",

			descriptionKey="generic_element_Button_customDisplayTypeScriptDescriptionKey"
	)
	@EntityViewField(referenceTypes=FieldReferenceType.DETAIL)
	private String customDisplayTypeScript;

	/** 表示判定用スクリプトキー */
	private String customDisplayTypeScriptKey;

	/**
	 * デフォルトコンストラクタ
	 */
	public Button() {
	}

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public DisplayType getDisplayType() {
	    return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(DisplayType displayType) {
	    this.displayType = displayType;
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
	 * プライマリーを取得します。
	 * @return プライマリー
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * プライマリーを設定します。
	 * @param primary プライマリー
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
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
	 * クリックイベントを取得します。
	 * @return クリックイベント
	 */
	public String getOnclickEvent() {
		return onclickEvent;
	}

	/**
	 * クリックイベントを設定します。
	 * @param onclickEvent クリックイベント
	 */
	public void setOnclickEvent(String onclickEvent) {
		this.onclickEvent = onclickEvent;
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

	/**
	 * 表示判定用スクリプトを取得します。
	 * @return 表示判定用スクリプト
	 */
	public String getCustomDisplayTypeScript() {
	    return customDisplayTypeScript;
	}

	/**
	 * 表示判定用スクリプトを設定します。
	 * @param customDisplayTypeScript 表示判定用スクリプト
	 */
	public void setCustomDisplayTypeScript(String customDisplayTypeScript) {
	    this.customDisplayTypeScript = customDisplayTypeScript;
	}

	/**
	 * 表示判定用スクリプトキーを取得します。
	 * @return 表示判定用スクリプトキー
	 */
	public String getCustomDisplayTypeScriptKey() {
	    return customDisplayTypeScriptKey;
	}

	/**
	 * 表示判定用スクリプトキーを設定します。
	 * @param customDisplayTypeScriptKey 表示判定用スクリプトキー
	 */
	public void setCustomDisplayTypeScriptKey(String customDisplayTypeScriptKey) {
	    this.customDisplayTypeScriptKey = customDisplayTypeScriptKey;
	}

}
