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

package org.iplass.mtp.view.generic.element.section;

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
import org.iplass.mtp.view.generic.element.Element;

/**
 * 標準セクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/DefaultSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class DefaultSection extends Section {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8895232754105370651L;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_section_DefaultSection_hideDetailDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集で非表示にするかを設定します。",
			descriptionKey="generic_element_section_DefaultSection_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_section_DefaultSection_hideViewDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示で非表示にするかを設定します。",
			descriptionKey="generic_element_section_DefaultSection_hideViewDescriptionKey"
	)
	private boolean hideView;




	/** タイトル */
	@MetaFieldInfo(
			displayName="タイトル",
			displayNameKey="generic_element_section_DefaultSection_titleDisplaNameKey",
			description="セクションのタイトルを設定します。",
			descriptionKey="generic_element_section_DefaultSection_titleDescriptionKey",
			inputType=InputType.MULTI_LANG,
			multiLangField = "localizedTitleList",
			displayOrder=300
	)
	@MultiLang()
	private String title;

	/** 多言語設定情報 */
	@MetaFieldInfo(
			displayName="多言語設定",
			displayNameKey="generic_element_section_DefaultSection_localizedTitleListDisplaNameKey",
			inputType=InputType.MULTI_LANG_LIST,
			displayOrder=310
	)
	private List<LocalizedStringDefinition> localizedTitleList;

	/** クラス名 */
	@MetaFieldInfo(
			displayName="クラス名",
			displayNameKey="generic_element_section_DefaultSection_styleDisplaNameKey",
			description="スタイルシートのクラス名を指定します。複数指定する場合は半角スペースで区切ってください。",
			descriptionKey="generic_element_section_DefaultSection_styleDescriptionKey",
			displayOrder=320
	)
	private String style;

	/** id */
	@MetaFieldInfo(
			displayName="id",
			displayNameKey="generic_element_section_DefaultSection_idDisplaNameKey",
			displayOrder=400,
			description="画面上で一意となるIDを設定してください。",
			descriptionKey="generic_element_section_DefaultSection_idDescriptionKey"
	)
	private String id;

	/** セクションの展開可否 */
	@MetaFieldInfo(
			displayName="初期表示時に展開",
			displayNameKey="generic_element_section_DefaultSection_expandableDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=410,
			description="セクションを初期展開するかを指定します。",
			descriptionKey="generic_element_section_DefaultSection_expandableDescriptionKey"
	)
	private boolean expandable;

	/** リンクを表示するか */
	@MetaFieldInfo(
			displayName="リンクを表示するか",
			displayNameKey="generic_element_section_DefaultSection_showLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=420,
			description="詳細画面でのページ内リンクを表示するかを指定します。",
			descriptionKey="generic_element_section_DefaultSection_showLinkDescriptionKey"
	)
	private boolean showLink;




	/** エレメント情報 */
	@MultiLang(isMultiLangValue = false)
	private List<Element> elements;

	/** 列数 */
	@MetaFieldInfo(
			displayName="列数",
			displayNameKey="generic_element_section_DefaultSection_colNumDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=1,
//			maxRange=2,
			displayOrder=1000,
			description="セクションの列数を指定します。<br>" +
					"なお、3列以上指定する場合は、レイアウトが崩れる可能性があるため注意してください。",
			descriptionKey="generic_element_section_DefaultSection_colNumDescriptionKey"
	)
	private int colNum;

	/** 上部のコンテンツ */
	@MetaFieldInfo(
			displayName="上部のコンテンツ",
			displayNameKey="generic_element_section_DefaultSection_upperContentsDisplaNameKey",
			description="セクションの上部に表示するコンテンツを設定します。<br>" +
					"コンテンツの内容にHTMLタグを利用することも可能です。",
			descriptionKey="generic_element_section_DefaultSection_upperContentsDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=1010
	)
	private String upperContents;

	/** 下部のコンテンツ */
	@MetaFieldInfo(
			displayName="下部のコンテンツ",
			displayNameKey="generic_element_section_DefaultSection_lowerContentsDisplaNameKey",
			description="セクションの下部に表示するコンテンツを設定します。<br>" +
					"コンテンツの内容にHTMLタグを利用することも可能です。",
			descriptionKey="generic_element_section_DefaultSection_lowerContentsDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="groovytemplate",
			displayOrder=1020
	)
	private String lowerContents;

	/** カスタムスタイルキー(内部用) */
	private String styleScriptKey;

	/** 上下コンテンツスクリプトのキー(内部用) */
	private String contentScriptKey;

	/**
	 * デフォルトコンストラクタ
	 */
	public DefaultSection() {
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
	 * @return タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * セクションの展開可否を取得します。
	 * @return セクションの展開可否
	 */
	public boolean isExpandable() {
		return expandable;
	}

	/**
	 * セクションの展開可否を設定します。
	 * @param expandable セクションの展開可否
	 */
	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
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
	 * エレメント情報を取得します。
	 * @return エレメント情報
	 */
	public List<Element> getElements() {
		if (this.elements == null) this.elements = new ArrayList<Element>();
		return elements;
	}

	/**
	 * エレメント情報を設定します。
	 * @param properties エレメント情報
	 */
	public void setElements(List<Element> properties) {
		this.elements = properties;
	}

	/**
	 * 列数を取得します。
	 * @return 列数
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * 列数を設定します。
	 * @param colNum 列数
	 */
	public void setColNum(int colNum) {
		this.colNum = colNum;
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
	 * エレメント情報を追加します。
	 * @param val エレメント情報
	 */
	public void addElement(Element val) {
		getElements().add(val);
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
	 * 上部のコンテンツを取得します。
	 * @return 上部のコンテンツ
	 */
	public String getUpperContents() {
	    return upperContents;
	}

	/**
	 * 上部のコンテンツを設定します。
	 * @param upperContents 上部のコンテンツ
	 */
	public void setUpperContents(String upperContents) {
	    this.upperContents = upperContents;
	}

	/**
	 * 下部のコンテンツを取得します。
	 * @return 下部のコンテンツ
	 */
	public String getLowerContents() {
	    return lowerContents;
	}

	/**
	 * 下部のコンテンツを設定します。
	 * @param lowerContents 下部のコンテンツ
	 */
	public void setLowerContents(String lowerContents) {
	    this.lowerContents = lowerContents;
	}

	/**
	 * カスタムスタイルのキーを取得します。
	 * @return カスタムスタイルのキー
	 */
	public String getStyleScriptKey() {
		return styleScriptKey;
	}

	/**
	 * カスタムスタイルのキーを設定します。
	 * @param styleScriptKey カスタムスタイルのキー
	 */
	public void setStyleScriptKey(String styleScriptKey) {
		this.styleScriptKey = styleScriptKey;
	}

	/**
	 * 上下コンテンツスクリプトのキーを取得します。
	 * @return 上下コンテンツスクリプトのキー
	 */
	public String getContentScriptKey() {
		return contentScriptKey;
	}

	/**
	 * 上下コンテンツスクリプトのキーを設定します。
	 * @param contentScriptKey 上下コンテンツスクリプトのキー
	 */
	public void setContentScriptKey(String contentScriptKey) {
		this.contentScriptKey = contentScriptKey;
	}

}
