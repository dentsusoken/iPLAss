/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.definition.LocalizedStringDefinition;

public class PreviewDateParts extends TopViewParts {

	private static final long serialVersionUID = -4339834567647662309L;

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedTitleList;

	/** プレビュー日付機能を利用するか否か */
	private boolean usePreviewDate;

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
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedTitle(LocalizedStringDefinition localizedTitle) {
		if (localizedTitleList == null) {
			localizedTitleList = new ArrayList<>();
		}

		localizedTitleList.add(localizedTitle);
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<LocalizedStringDefinition> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedTitleList 多言語設定情報
	 */
	public void setLocalizedTitleList(List<LocalizedStringDefinition> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	/**
	 * プレビュー日付機能を利用するかを取得します。
	 * @return プレビュー日付機能を利用するか
	 */
	public boolean isUsePreviewDate() {
		return usePreviewDate;
	}

	/**
	 * プレビュー日付機能を利用するかを設定します。
	 * @param usePreviewDate true:利用
	 */
	public void setUsePreviewDate(boolean usePreviewDate) {
		this.usePreviewDate = usePreviewDate;
	}

}
