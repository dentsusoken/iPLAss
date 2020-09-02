/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.top.TopViewHandler;
import org.iplass.mtp.view.top.parts.CsvDownloadSettingsParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

public class MetaCsvDownloadSettingsParts extends MetaTopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 5888655124370169294L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaCsvDownloadSettingsParts createInstance(TopViewParts parts) {
		return new MetaCsvDownloadSettingsParts();
	}

	private boolean specfyCharacterCodeEntityView;

	public boolean isSpecfyCharacterCodeEntityView() {
		return specfyCharacterCodeEntityView;
	}

	public void setSpecfyCharacterCodeEntityView(boolean specfyCharacterCodeEntityView) {
		this.specfyCharacterCodeEntityView = specfyCharacterCodeEntityView;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		CsvDownloadSettingsParts csvDownloadSettingsParts = (CsvDownloadSettingsParts) parts;

		specfyCharacterCodeEntityView = csvDownloadSettingsParts.isSpecfyCharacterCodeEntityView();
	}

	@Override
	public TopViewParts currentConfig() {
		CsvDownloadSettingsParts csvDownloadSettingsParts = new CsvDownloadSettingsParts();

		csvDownloadSettingsParts.setSpecfyCharacterCodeEntityView(specfyCharacterCodeEntityView);

		return csvDownloadSettingsParts;
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
					ServletContext application, PageContext page) throws IOException,
					ServletException {
			}

			@Override
			public void loadParts(HttpServletRequest req, HttpServletResponse res,
					ServletContext application, PageContext page) throws IOException,
					ServletException {
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
			}
		};
	}

}
