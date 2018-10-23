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

import org.iplass.adminconsole.view.annotation.IgnoreField;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.PagingPosition;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.element.Element;

/**
 * 検索結果を保持するセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/element/section/SearchResultSection.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
@IgnoreField({"dispFlag"})
public class SearchResultSection extends Section {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -700624023196624864L;

	/** 要素 */
	private List<Element> elements;

	/** 検索結果の表示行数 */
	@MetaFieldInfo(
			displayName="検索結果の表示件数",
			displayNameKey="generic_element_section_SearchResultSection_dispRowCountDisplaNameKey",
			inputType=InputType.NUMBER,
			rangeCheck=true,
			minRange=1,
			maxRange=200,
			description="検索結果の一覧に表示する件数を指定します。",
			descriptionKey="generic_element_section_SearchResultSection_dispRowCountDescriptionKey"
	)
	private int dispRowCount;

	/** 編集リンク非表示設定 */
	@MetaFieldInfo(
			displayName="編集リンク非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hideDetailLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="検索結果の編集リンクを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hideDetailLinkDescriptionKey"
	)
	private boolean hideDetailLink;

	/** 削除ボタン非表示設定 */
	@MetaFieldInfo(
			displayName="削除ボタン非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hideDeleteDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="削除ボタンと検索結果のチェックボックスを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hideDeleteDescriptionKey"
	)
	private boolean hideDelete;

	/** ページング非表示設定 */
	@MetaFieldInfo(
			displayName="ページング非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hidePagingDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="参照の一覧のページングを非表示にします。<br>" +
					"非表示にした場合はページングが行えないため、対象データを全件取得します。",
			descriptionKey="generic_element_section_SearchResultSection_hidePagingDescriptionKey"
	)
	private boolean hidePaging;

	/** 件数非表示設定 */
	@MetaFieldInfo(
			displayName="件数非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hideCountDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="検索結果のページングで件数を非表示にします。<br>" +
					"非表示にした場合、全件数はカウントされません。<br>" +
					"また、ページジャンプ、ページリンクも非表示になります。",
			descriptionKey="generic_element_section_SearchResultSection_hideCountDescriptionKey"
	)
	private boolean hideCount;

	/** ページジャンプ非表示設定 */
	@MetaFieldInfo(
			displayName="ページジャンプ非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hidePageJumpDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="検索結果のページングでページジャンプを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hidePageJumpDescriptionKey"
	)
	private boolean hidePageJump;

	/** ページリンク非表示設定 */
	@MetaFieldInfo(
			displayName="ページリンク非表示設定",
			displayNameKey="generic_element_section_SearchResultSection_hidePageLinkDisplaNameKey",
			inputType=InputType.CHECKBOX,
			description="検索結果のページングでページリンクを非表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_hidePageLinkDescriptionKey"
	)
	private boolean hidePageLink;

	/** ページング表示位置 */
	@MetaFieldInfo(
			displayName="ページング表示位置",
			displayNameKey="generic_element_section_SearchResultSection_pagingPositionDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=PagingPosition.class,
			description="ページングの表示位置を指定します。<br>" +
					"<b>BOTH   :</b> グリッドの上下<br>" +
					"<b>TOP    :</b> グリッドの上部<br>" +
					"<b>BOTTOM :</b> グリッドの下部<br>",
			descriptionKey="generic_element_section_SearchResultSection_pagingPositionDescriptionKey"
	)
	private PagingPosition pagingPosition;

	/** 一括更新ボタン表示設定 */
	@MetaFieldInfo(
			displayName="一括更新ボタン表示設定",
			displayNameKey="generic_element_section_SearchResultSection_showBulkUpdateDisplayNameKey",
			inputType=InputType.CHECKBOX,
			description="一括更新ボタンを表示にします。",
			descriptionKey="generic_element_section_SearchResultSection_showBulkUpdateDescriptionKey"
	)
	private boolean showBulkUpdate;

	/** カスタムスタイルキー */
	private String scriptKey;

	/**
	 * デフォルトコンストラクタ
	 */
	public SearchResultSection() {
	}

	/**
	 * 要素を取得します。
	 * @return 要素
	 */
	public List<Element> getElements() {
		if (elements == null) elements = new ArrayList<Element>();
		return elements;
	}

	/**
	 * 要素を設定します。
	 * @param elements 要素
	 */
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/**
	 * 検索結果の表示行数を取得します。
	 * @return 検索結果の表示行数
	 */
	public int getDispRowCount() {
		return dispRowCount;
	}

	/**
	 * 検索結果の表示行数を設定します。
	 * @param dispRowCount 検索結果の表示行数
	 */
	public void setDispRowCount(int dispRowCount) {
		this.dispRowCount = dispRowCount;
	}

	/**
	 * 編集リンク非表示設定を取得します。
	 * @return 編集リンク非表示設定
	 */
	public boolean isHideDetailLink() {
	    return hideDetailLink;
	}

	/**
	 * 編集リンク非表示設定を設定します。
	 * @param hideDetailLink 編集リンク非表示設定
	 */
	public void setHideDetailLink(boolean hideDetailLink) {
	    this.hideDetailLink = hideDetailLink;
	}

	/**
	 * 削除ボタン非表示設定を取得します。
	 * @return 削除ボタン非表示設定
	 */
	public boolean isHideDelete() {
	    return hideDelete;
	}

	/**
	 * 削除ボタン非表示設定を設定します。
	 * @param hideDelete 削除ボタン非表示設定
	 */
	public void setHideDelete(boolean hideDelete) {
	    this.hideDelete = hideDelete;
	}

	/**
	 * ページング非表示設定を取得します。
	 * @return ページング非表示設定
	 */
	public boolean isHidePaging() {
	    return hidePaging;
	}

	/**
	 * ページング非表示設定を設定します。
	 * @param hidePaging ページング非表示設定
	 */
	public void setHidePaging(boolean hidePaging) {
	    this.hidePaging = hidePaging;
	}

	/**
	 * 件数非表示設定を取得します。
	 * @return 件数非表示設定
	 */
	public boolean isHideCount() {
	    return hideCount;
	}

	/**
	 * 件数非表示設定を設定します。
	 * @param hideCount 件数非表示設定
	 */
	public void setHideCount(boolean hideCount) {
	    this.hideCount = hideCount;
	}

	/**
	 * ページジャンプ非表示設定を取得します。
	 * @return ページジャンプ非表示設定
	 */
	public boolean isHidePageJump() {
	    return hidePageJump;
	}

	/**
	 * ページジャンプ非表示設定を設定します。
	 * @param hidePageJump ページジャンプ非表示設定
	 */
	public void setHidePageJump(boolean hidePageJump) {
	    this.hidePageJump = hidePageJump;
	}

	/**
	 * ページリンク非表示設定を取得します。
	 * @return ページリンク非表示設定
	 */
	public boolean isHidePageLink() {
	    return hidePageLink;
	}

	/**
	 * ページリンク非表示設定を設定します。
	 * @param hidePageLink ページリンク非表示設定
	 */
	public void setHidePageLink(boolean hidePageLink) {
	    this.hidePageLink = hidePageLink;
	}

	/**
	 * ページング表示位置を取得します。
	 * @return ページング表示位置
	 */
	public PagingPosition getPagingPosition() {
	    return pagingPosition;
	}

	/**
	 * ページング表示位置を設定します。
	 * @param pagingPosition ページング表示位置
	 */
	public void setPagingPosition(PagingPosition pagingPosition) {
	    this.pagingPosition = pagingPosition;
	}

	/**
	 * 要素を追加します。
	 * @param val プロパティ情報
	 */
	public void addElement(Element val) {
		getElements().add(val);
	}

	@Override
	public boolean isShowLink() {
		return false;
	}

	/**
	 * 一括更新ボタン表示設定を取得します。
	 * @return 一括更新ボタン表示設定
	 */
	public boolean isShowBulkUpdate() {
		return showBulkUpdate;
	}

	/**
	 * 一括更新ボタン表示設定を設定します。
	 * @param showBulkUpdate 一括更新ボタン表示設定 
	 */
	public void setShowBulkUpdate(boolean showBulkUpdate) {
		this.showBulkUpdate = showBulkUpdate;
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
