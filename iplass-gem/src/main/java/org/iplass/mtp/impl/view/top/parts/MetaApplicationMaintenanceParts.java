/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

public class MetaApplicationMaintenanceParts extends MetaTopViewParts {

	private static final long serialVersionUID = 3462315351176378934L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaApplicationMaintenanceParts createInstance(TopViewParts parts) {
		return new MetaApplicationMaintenanceParts();
	}

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();
	
	/** 個人アクセストークン機能を利用するか否か */
	private boolean usePersonalAccessToken;

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
	 * 個人アクセストークン機能を利用の有無を示すフラグを取得します。
	 * @return usePersonalAccessToken 個人アクセストークン機能を利用の有無を示すフラグ
	 */
	public boolean isUsePersonalAccessToken() {
		return usePersonalAccessToken;
	}

	/**
	 * 個人アクセストークン機能を利用するか否かを設定します。
	 * @param usePersonalAccessToken 個人アクセストークン機能を利用の有無を示すフラグ
	 */
	public void setUsePersonalAccessToken(boolean usePersonalAccessToken) {
		this.usePersonalAccessToken = usePersonalAccessToken;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		ApplicationMaintenanceParts amp = (ApplicationMaintenanceParts) parts;

		title = amp.getTitle();
		usePersonalAccessToken = amp.isUsePersonalAccessToken();
		localizedTitleList = I18nUtil.toMeta(amp.getLocalizedTitleList());
	}

	@Override
	public TopViewParts currentConfig() {
		ApplicationMaintenanceParts parts = new ApplicationMaintenanceParts();

		parts.setTitle(title);
		parts.setUsePersonalAccessToken(usePersonalAccessToken);
		parts.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

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
