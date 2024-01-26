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

package org.iplass.mtp.view.top.parts;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * Entity一覧パーツ
 * @author lis3wg
 */
public class EntityListParts extends TemplateParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 2468488513006879707L;

	/** アイコンタグ */
	private String iconTag;

	/** 高さ */
	private Integer height;

	/** 非同期検索するか */
	private boolean searchAsync;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedTitleList;

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	/**
	 * 定義名を取得します。
	 * @return 定義名
	 */
	public String getDefName() {
		return getParam("defName");
	}

	/**
	 * 定義名を設定します。
	 * @param defName 定義名
	 */
	public void setDefName(String defName) {
		setParam("defName", defName);
	}

	/**
	 * View名を取得します。
	 * @return View名
	 */
	public String getViewName() {
		return getParam("viewName");
	}

	/**
	 * View名を設定します。
	 * @param viewName View名
	 */
	public void setViewName(String viewName) {
		setParam("viewName", viewName);
	}

	/**
	 * 検索リンク用のview名を取得します。
	 * @return 検索リンク用のview名
	 */
	public String getViewNameForLink() {
		return getParam("viewNameForLink");
	}

	/**
	 * 検索リンク用のview名を設定します。
	 * @param viewNameForLink 検索リンク用のview名
	 */
	public void setViewNameForLink(String viewNameForLink) {
		setParam("viewNameForLink", viewNameForLink);
	}

	/**
	 * 詳細リンク用のView名を取得します。
	 * @return 詳細リンク用のView名
	 */
	public String getViewNameForDetail() {
		return getParam("viewNameForDetail");
	}

	/**
	 * 詳細リンク用のView名を設定します。
	 * @param viewNameForDetail 詳細リンク用のView名
	 */
	public void setViewNameForDetail(String viewNameForDetail) {
		setParam("viewNameForDetail", viewNameForDetail);
	}

	/**
	 * フィルタ名を取得します。
	 * @return フィルタ名
	 */
	public String getFilterName() {
		return getParam("filterName");
	}

	/**
	 * フィルタ名を設定します。
	 * @param filterName フィルタ名
	 */
	public void setFilterName(String filterName) {
		setParam("filterName", filterName);
	}

	/**
	 * タイトルを取得します。
	 * @return タイトル
	 */
	@MultiLang(itemKey = "title", itemGetter = "getTitle", itemSetter = "setTitle", multiLangGetter = "getLocalizedTitleList", multiLangSetter = "setLocalizedTitleList")
	public String getTitle() {
		return getParam("title");
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		setParam("title", title);
	}

	/**
	 * 高さを取得します。
	 * @return 高さ
	 */
	public Integer getHeight() {
	    return height;
	}

	/**
	 * 高さを設定します。
	 * @param height 高さ
	 */
	public void setHeight(Integer height) {
	    this.height = height;
	}

	/**
	 * 非同期検索するかを取得します。
	 * @return 非同期検索するか
	 */
	public boolean isSearchAsync() {
		return searchAsync;
	}

	/**
	 * 非同期検索するかを設定します。
	 * @param searchAsync 
	 */
	public void setSearchAsync(boolean searchAsync) {
		this.searchAsync = searchAsync;
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
			localizedTitleList = new ArrayList<>();
		}

		localizedTitleList.add(localizedTitle);
	}

}
