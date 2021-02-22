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

package org.iplass.mtp.impl.view.top.parts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.top.TopViewHandler;
import org.iplass.mtp.view.top.parts.PreviewDateParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

public class MetaPreviewDateParts extends MetaTopViewParts {

	private static final long serialVersionUID = 3061302323808893757L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaPreviewDateParts createInstance(TopViewParts parts) {
		return new MetaPreviewDateParts();
	}

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();

	/** プレビュー日付機能を利用するか否か */
	private boolean usePreviewDate = true;

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
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTitleList(List<MetaLocalizedString> localizedTitleList) {
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

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		PreviewDateParts pdp = (PreviewDateParts) parts;

		title = pdp.getTitle();
		localizedTitleList = I18nUtil.toMeta(pdp.getLocalizedTitleList());
		usePreviewDate = pdp.isUsePreviewDate();
	}

	@Override
	public TopViewParts currentConfig() {
		PreviewDateParts parts = new PreviewDateParts();

		parts.setTitle(title);
		parts.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));
		parts.setUsePreviewDate(usePreviewDate);

		return parts;
	}

	@Override
	public TopViewPartsHandler createRuntime(TopViewHandler topView) {
		//直接トップページに出力しないのでなにもしない
		return new TopViewPartsHandler(this) {

			@Override
			public void setAttribute(HttpServletRequest req) {
			}

			@Override
			public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
					ServletContext application, PageContext page) throws IOException, ServletException {
			}

			@Override
			public void loadParts(HttpServletRequest req, HttpServletResponse res,
					ServletContext application, PageContext page) throws IOException, ServletException {
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
			}
		};
	}

}
