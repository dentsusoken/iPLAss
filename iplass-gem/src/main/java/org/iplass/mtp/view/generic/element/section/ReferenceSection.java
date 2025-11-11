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
import org.iplass.mtp.view.generic.HasNestProperty;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.NestProperty;

/**
 *
 * @author lis3wg
 */
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/ReferenceSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class ReferenceSection extends AdjustableHeightSection implements HasNestProperty {

	/** SerialVersionUID */
	private static final long serialVersionUID = -5340250243884655144L;

	/** dispBorderInSectionのデフォルト設定 */
	@Deprecated
	private static boolean defaultDispBorderInSection;

	static {
		 // システムプロパティorデフォルトtrueで初期化
		String value = System.getProperty("mtp.generic.dispBorderInSection", "true");
		defaultDispBorderInSection = Boolean.parseBoolean(value);
	}

	/** 参照先Entity定義名 */
	private String defintionName;

	/** プロパティ名 */
	private String propertyName;

	/** 詳細編集非表示設定 */
	@MetaFieldInfo(
			displayName="詳細編集非表示設定",
			displayNameKey="generic_element_section_ReferenceSection_hideDetailDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=200,
			description="詳細編集で非表示にするかを設定します。",
			descriptionKey="generic_element_section_ReferenceSection_hideDetailDescriptionKey"
	)
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	@MetaFieldInfo(
			displayName="詳細表示非表示設定",
			displayNameKey="generic_element_section_ReferenceSection_hideViewDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=210,
			description="詳細表示で非表示にするかを設定します。",
			descriptionKey="generic_element_section_ReferenceSection_hideViewDescriptionKey"
	)
	private boolean hideView;




	/** セクション内に配置した場合に枠線を表示 */
	@MetaFieldInfo(
			displayName="セクション内に配置した場合に枠線を表示",
			displayNameKey="generic_element_section_ReferenceSection_dispBorderInSectionDisplayNameKey",
			inputType=InputType.CHECKBOX,
			description="セクション内に配置した場合に枠線を表示するかを指定します。",
			displayOrder=400,
			descriptionKey="generic_element_section_ReferenceSection_dispBorderInSectionDescriptionKey"
	)
	private boolean dispBorderInSection = defaultDispBorderInSection;

	/** セクションの展開可否 */
	@MetaFieldInfo(
			displayName="初期表示時に展開",
			displayNameKey="generic_element_section_ReferenceSection_expandableDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=410,
			description="セクションを初期展開するかを指定します。",
			descriptionKey="generic_element_section_ReferenceSection_expandableDescriptionKey"
	)
	private boolean expandable;

	/** リンクを表示するか */
	@MetaFieldInfo(
			displayName="リンクを表示するか",
			displayNameKey="generic_element_section_ReferenceSection_showLinkDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=420,
			description="詳細画面でのページ内リンクを表示するかを指定します。",
			descriptionKey="generic_element_section_ReferenceSection_showLinkDescriptionKey"
	)
	private boolean showLink;




	/** 列数 */
	@MetaFieldInfo(
			displayName="列数",
			displayNameKey="generic_element_section_ReferenceSection_colNumDisplayNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=1,
			displayOrder=1000,
			description="セクションの列数を指定します。<br>" +
					"なお、3列以上指定する場合は、レイアウトが崩れる可能性があるため注意してください。",
			descriptionKey="generic_element_section_ReferenceSection_colNumDescriptionKey"
	)
	private int colNum;




	/** 表示プロパティ */
	@MetaFieldInfo(displayName="参照型の表示プロパティ",
			displayNameKey="generic_element_section_ReferenceSection_propertiesDisplayNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=NestProperty.class,
			multiple=true,
			displayOrder=2000,
			description="参照セクションに表示するプロパティを設定します。",
			descriptionKey="generic_element_section_ReferenceSection_propertiesDescriptionKey"
	)
	@MultiLang(isMultiLangValue = false)
	private List<NestProperty> properties;

	/** 表示順プロパティ */
	@MetaFieldInfo(
			displayName="表示順プロパティ",
			displayNameKey="generic_element_section_ReferenceSection_orderPropNameDisplayNameKey",
			description="データの表示順序を示すプロパティを指定します。<br>" +
					"<b>参照プロパティの多重度が1<b><br>" +
					"使用しません。<br>" +
					"<b>参照プロパティの多重度が1以外<b><br>" +
					"指定したプロパティに設定された値とデータのインデックスが一致するデータを表示します。",
			descriptionKey="generic_element_section_ReferenceSection_orderPropNameDescriptionKey",
			inputType=InputType.PROPERTY,
			displayOrder=2010
	)
	private String orderPropName;

	/** データのインデックス */
	@MetaFieldInfo(
			displayName="データのインデックス",
			displayNameKey="generic_element_section_ReferenceSection_indexDisplayNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=0,
			displayOrder=2020,
			description="このセクションに表示する参照データのインデックスを指定します。<br>" +
					"<b>参照プロパティの多重度が1<b><br>" +
					"使用しません。<br>" +
					"<b>参照プロパティの多重度が1以外<b><br>" +
					"表示順プロパティの値と一致するデータを表示します。<br>" +
					"同一プロパティの参照セクションを複数配置する場合、この項目の値が重複しないようにしてください。",
			descriptionKey="generic_element_section_ReferenceSection_indexDescriptionKey"
	)
	private int index;




	/** 更新時に強制的に更新処理を行う */
	@MetaFieldInfo(
			displayName="更新時に強制的に更新処理を行う",
			displayNameKey="generic_element_section_ReferenceSection_forceUpadteDisplayNameKey",
			inputType=InputType.CHECKBOX,
			displayOrder=3000,
			description="変更項目が一つもなくとも、強制的に更新処理（更新日時、更新者が更新される）を行います。",
			descriptionKey="generic_element_section_ReferenceSection_forceUpadteDescriptionKey"
	)
	private boolean forceUpadte;




	/** 上部のコンテンツ */
	@MetaFieldInfo(
			displayName="上部のコンテンツ",
			displayNameKey="generic_element_section_ReferenceSection_upperContentsDisplayNameKey",
			displayOrder=4000,
			description="セクションの上部に表示するコンテンツを設定します。<br>" +
					"コンテンツの内容にHTMLタグを利用することも可能です。",
			descriptionKey="generic_element_section_ReferenceSection_upperContentsDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="html"
	)
	private String upperContents;

	/** 下部のコンテンツ */
	@MetaFieldInfo(
			displayName="下部のコンテンツ",
			displayNameKey="generic_element_section_ReferenceSection_lowerContentsDisplayNameKey",
			description="セクションの下部に表示するコンテンツを設定します。<br>" +
					"コンテンツの内容にHTMLタグを利用することも可能です。",
			descriptionKey="generic_element_section_ReferenceSection_lowerContentsDescriptionKey",
			inputType=InputType.SCRIPT,
			mode="html",
			displayOrder=4010
	)
	private String lowerContents;

	/** 上下コンテンツスクリプトのキー(内部用) */
	private String contentScriptKey;

	/**
	 * 参照先Entity定義名を取得します。
	 * @return Entity定義名
	 */
	public String getDefintionName() {
	    return defintionName;
	}

	/**
	 * 参照先Entity定義名を設定します。
	 * @param defintionName Entity定義名
	 */
	public void setDefintionName(String defintionName) {
	    this.defintionName = defintionName;
	}

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
	    return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
	    this.propertyName = propertyName;
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
	@Override
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
	 * 表示プロパティを取得します。
	 * @return 表示プロパティ
	 */
	public List<NestProperty> getProperties() {
		if (properties == null) properties = new ArrayList<>();
	    return properties;
	}

	/**
	 * 表示プロパティを設定します。
	 * @param properties 表示プロパティ
	 */
	public void setProperties(List<NestProperty> properties) {
	    this.properties = properties;
	}

	/**
	 * 表示プロパティを追加します。
	 * @param property 表示プロパティ
	 */
	public void addProperty(NestProperty property) {
		getProperties().add(property);
	}

	/**
	 * 表示順プロパティを取得します。
	 * @return 表示順プロパティ
	 */
	public String getOrderPropName() {
	    return orderPropName;
	}

	/**
	 * 表示順プロパティを設定します。
	 * @param orderPropName 表示順プロパティ
	 */
	public void setOrderPropName(String orderPropName) {
	    this.orderPropName = orderPropName;
	}

	/**
	 * データのインデックスを取得します。
	 * @return データのインデックス
	 */
	public int getIndex() {
	    return index;
	}

	/**
	 * データのインデックスを設定します。
	 * @param index データのインデックス
	 */
	public void setIndex(int index) {
	    this.index = index;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを取得します。
	 * @return forceUpdate 更新時に強制的に更新処理を行うか
	 */
	public boolean isForceUpadte() {
		return forceUpadte;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを設定します。
	 * @param forceUpadte 更新時に強制的に更新処理を行うか
	 */
	public void setForceUpadte(boolean forceUpadte) {
		this.forceUpadte = forceUpadte;
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
