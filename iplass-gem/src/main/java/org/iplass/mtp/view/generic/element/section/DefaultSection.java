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
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.element.Element;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

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

	/** dispBorderInSectionのデフォルト設定 */
	@Deprecated
	private static boolean defaultDispBorderInSection;

	static {
		 // システムプロパティorデフォルトtrueで初期化
		String value = System.getProperty("mtp.generic.dispBorderInSection", "true");
		defaultDispBorderInSection = Boolean.parseBoolean(value);
	}

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_section_DefaultSection_hideDetailDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集で非表示にするかを設定します。",
			descriptionKey="generic_element_section_DefaultSection_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_section_DefaultSection_hideViewDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示で非表示にするかを設定します。",
			descriptionKey="generic_element_section_DefaultSection_hideViewDescriptionKey"
	)
	private boolean hideView;



	/** セクション内に配置した場合に枠線を表示 */
	@MetaFieldInfo(
			displayName="セクション内に配置した場合に枠線を表示",
			displayNameKey="generic_element_section_DefaultSection_dispBorderInSectionDisplayNameKey",
			inputType=InputType.CHECKBOX,
			description="セクション内に配置した場合に枠線を表示するかを指定します。",
			displayOrder=400,
			descriptionKey="generic_element_section_DefaultSection_dispBorderInSectionDescriptionKey"
	)
	private boolean dispBorderInSection = defaultDispBorderInSection;

	/** セクションの展開可否 */
	@MetaFieldInfo(
			displayName="初期表示時に展開",
			displayNameKey="generic_element_section_DefaultSection_expandableDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=410,
			description="セクションを初期展開するかを指定します。",
			descriptionKey="generic_element_section_DefaultSection_expandableDescriptionKey"
	)
	private boolean expandable;

	/** リンクを表示するか */
	@MetaFieldInfo(
			displayName="リンクを表示するか",
			displayNameKey="generic_element_section_DefaultSection_showLinkDisplayNameKey",
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
			displayNameKey="generic_element_section_DefaultSection_colNumDisplayNameKey",
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
			displayNameKey="generic_element_section_DefaultSection_upperContentsDisplayNameKey",
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
			displayNameKey="generic_element_section_DefaultSection_lowerContentsDisplayNameKey",
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
	 * エレメント情報を追加します。
	 * @param val エレメント情報
	 */
	public void addElement(Element val) {
		getElements().add(val);
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
