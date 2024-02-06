/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.ViewControlSetting;

public class MetaViewControlSetting implements MetaData {

	private static final long serialVersionUID = -263940350620734399L;

	/** View名 */
	private String name;

	/** 検索画面を自動生成 */
	private boolean autoGenerateSearchView;

	/** 詳細画面を自動生成 */
	private boolean autoGenerateDetailView;

	/** 一括更新画面を自動生成 */
	private boolean autoGenerateBulkView;

	/** 許可ロール */
	private String permitRoles;

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return autoGenerateSearchView
	 */
	public boolean isAutoGenerateSearchView() {
		return autoGenerateSearchView;
	}

	/**
	 * @param autoGenerateSearchView セットする autoGenerateSearchView
	 */
	public void setAutoGenerateSearchView(boolean autoGenerateSearchView) {
		this.autoGenerateSearchView = autoGenerateSearchView;
	}

	/**
	 * @return autoGenerateDetailView
	 */
	public boolean isAutoGenerateDetailView() {
		return autoGenerateDetailView;
	}

	/**
	 * @param autoGenerateDetailView セットする autoGenerateDetailView
	 */
	public void setAutoGenerateDetailView(boolean autoGenerateDetailView) {
		this.autoGenerateDetailView = autoGenerateDetailView;
	}

	public boolean isAutoGenerateBulkView() {
		return autoGenerateBulkView;
	}

	public void setAutoGenerateBulkView(boolean autoGenerateBulkView) {
		this.autoGenerateBulkView = autoGenerateBulkView;
	}

	/**
	 * 許可ロールを取得します。
	 * @return 許可ロール
	 */
	public String getPermitRoles() {
		return permitRoles;
	}

	/**
	 * 許可ロールを設定します。
	 * @param permitRoles 許可ロール
	 */
	public void setPermitRoles(String permitRoles) {
		this.permitRoles = permitRoles;
	}

	public void applyConfig(ViewControlSetting setting) {
		name = setting.getName();
		autoGenerateSearchView = setting.isAutoGenerateSearchView();
		autoGenerateDetailView = setting.isAutoGenerateDetailView();
		autoGenerateBulkView = setting.isAutoGenerateBulkView();
		permitRoles = setting.getPermitRoles();
	}

	public ViewControlSetting currentConfig() {
		ViewControlSetting setting = new ViewControlSetting();
		setting.setName(name);
		setting.setAutoGenerateSearchView(autoGenerateSearchView);
		setting.setAutoGenerateDetailView(autoGenerateDetailView);
		setting.setAutoGenerateBulkView(autoGenerateBulkView);
		setting.setPermitRoles(permitRoles);
		return setting;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
}
