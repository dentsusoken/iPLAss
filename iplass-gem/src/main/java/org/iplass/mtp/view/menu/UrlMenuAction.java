/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.menu;

public class UrlMenuAction {

	/** 遷移先URL */
	private String url;

	/** パラメータ */
	private String parameter;

	/** 新しいページで表示 */
	private boolean showNewPage;

	public UrlMenuAction() {
	}

	public UrlMenuAction(UrlMenuItem menuItem) {
		setUrl(menuItem.getUrl());
		setParameter(menuItem.getParameter());
		setShowNewPage(menuItem.isShowNewPage());
	}

	/**
	 * 遷移先URLを取得します。
	 * @return 遷移先URL
	 */
	public String getUrl() {
	    return url;
	}

	/**
	 * 遷移先URLを設定します。
	 * @param url 遷移先URL
	 */
	public void setUrl(String url) {
	    this.url = url;
	}

	/**
	 * パラメータを取得します。
	 * @return パラメータ
	 */
	public String getParameter() {
	    return parameter;
	}

	/**
	 * パラメータを設定します。
	 * @param parameter パラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
	}

	/**
	 * 新しいページで表示を取得します。
	 * @return 新しいページで表示
	 */
	public boolean isShowNewPage() {
	    return showNewPage;
	}

	/**
	 * 新しいページで表示を設定します。
	 * @param showNewPage 新しいページで表示
	 */
	public void setShowNewPage(boolean showNewPage) {
	    this.showNewPage = showNewPage;
	}

}
