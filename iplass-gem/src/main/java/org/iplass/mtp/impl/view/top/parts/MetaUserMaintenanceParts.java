/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

public class MetaUserMaintenanceParts extends MetaTopViewParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 3563153207799796075L;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaUserMaintenanceParts createInstance(TopViewParts parts) {
		return new MetaUserMaintenanceParts();
	}

	/** ビュー名 */
	private String viewName;

	/**
	 * ビュー名を取得します。
	 * @return ビュー名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * ビュー名を設定します。
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		UserMaintenanceParts ump = (UserMaintenanceParts) parts;

		viewName = ump.getViewName();
	}

	@Override
	public TopViewParts currentConfig() {
		UserMaintenanceParts parts = new UserMaintenanceParts();

		parts.setViewName(viewName);

		return parts;
	}

	@Override
	public TopViewPartsHandler createRuntime() {
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
