/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.tenant.gem;

import java.util.List;

import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.tenant.TenantConfig;

public class TenantGemInfo extends TenantConfig {

	private static final long serialVersionUID = -760234330179208809L;

	/** テナント名表示 */
	private boolean useDisplayName = true;

	/** ログイン画面、エラー画面でテナント名表示 */
	private boolean dispTenantName = true;
	
	/** テナント名制御Script */
	private String screenTitle;
	
	/** 多言語設定用テナント名制御Script */
	private List<LocalizedStringDefinition> localizedScreenTitle;

	/** スキン */
	private String skin;

	/** テーマ */
	private String theme;

	/** テナント画像URL */
	private String tenantImageUrl;

	/** テナント画像URL(縮小時) */
	private String tenantMiniImageUrl;

	/** テナント画像URL(大) */
	private String tenantLargeImageUrl;

	/** アイコンURL */
	private String iconUrl;

	/** Javascriptファイルパス */
	private String javascriptFilePath;

	/** スタイルシートファイルパス */
	private String stylesheetFilePath;

	public TenantGemInfo() {
	}

	/**
	 * テナント名表示設定を取得します。
	 * @return true:テナント名を表示
	 */
	public boolean isUseDisplayName() {
		return useDisplayName;
	}

	/**
	 * テナント名表示設定を設定します。
	 * @param useDisplayName 表示設定(true:表示)
	 */
	public void setUseDisplayName(boolean useDisplayName) {
		this.useDisplayName = useDisplayName;
	}

	/**
	 * <p>システム画面、テナント名表示設定を取得します。</p>
	 * <p>ログイン画面、エラー画面でテナント名を表示するかを取得します。</p>
	 *
	 * @return true:テナント名を表示
	 */
	public boolean isDispTenantName() {
		return dispTenantName;
	}

	/**
	 * <p>システム画面、テナント名表示設定を設定します。</p>
	 * <p>ログイン画面、エラー画面でテナント名を表示するかを設定します。</p>
	 *
	 * @param dispTenantName 表示設定(true:表示)
	 */
	public void setDispTenantName(boolean dispTenantName) {
		this.dispTenantName = dispTenantName;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public List<LocalizedStringDefinition> getLocalizedScreenTitle() {
		return localizedScreenTitle;
	}

	public void setLocalizedScreenTitle(List<LocalizedStringDefinition> localizedScreenTitle) {
		this.localizedScreenTitle = localizedScreenTitle;
	}

	/**
	 * スキンを取得します。
	 * @return スキン
	 */
	public String getSkin() {
	    return skin;
	}

	/**
	 * スキンを設定します。
	 * @param skin スキン
	 */
	public void setSkin(String skin) {
	    this.skin = skin;
	}

	/**
	 * テーマを取得します。
	 * @return テーマ
	 */
	public String getTheme() {
	    return theme;
	}

	/**
	 * テーマを設定します。
	 * @param theme テーマ
	 */
	public void setTheme(String theme) {
	    this.theme = theme;
	}

	/**
	 * テナント画像URLを取得します。
	 * @return テナント画像URL
	 */
	public String getTenantImageUrl() {
	    return tenantImageUrl;
	}

	/**
	 * テナント画像URLを設定します。
	 * @param tenantImageUrl テナント画像URL
	 */
	public void setTenantImageUrl(String tenantImageUrl) {
	    this.tenantImageUrl = tenantImageUrl;
	}

	/**
	 * テナント画像URL(縮小時)を取得します。
	 * @return テナント画像URL(縮小時)
	 */
	public String getTenantMiniImageUrl() {
	    return tenantMiniImageUrl;
	}

	/**
	 * テナント画像URL(縮小時)を設定します。
	 * @param tenantMiniImageUrl テナント画像URL(縮小時)
	 */
	public void setTenantMiniImageUrl(String tenantMiniImageUrl) {
	    this.tenantMiniImageUrl = tenantMiniImageUrl;
	}

	/**
	 * テナント画像URL(大)を取得します。
	 * @return テナント画像URL(大)
	 */
	public String getTenantLargeImageUrl() {
	    return tenantLargeImageUrl;
	}

	/**
	 * テナント画像URL(大)を設定します。
	 * @param tenantLargeImageUrl テナント画像URL(大)
	 */
	public void setTenantLargeImageUrl(String tenantLargeImageUrl) {
	    this.tenantLargeImageUrl = tenantLargeImageUrl;
	}

	/**
	 * アイコンURLを取得します。
	 * @return アイコンURL
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * アイコンURLを設定します。
	 * @param stylesheetFilePath アイコンURL
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * Javascriptファイルパスを取得します。
	 * @return Javascriptファイルパス
	 */
	public String getJavascriptFilePath() {
	    return javascriptFilePath;
	}

	/**
	 * Javascriptファイルパスを設定します。
	 * @param javascriptFilePath Javascriptファイルパス
	 */
	public void setJavascriptFilePath(String javascriptFilePath) {
	    this.javascriptFilePath = javascriptFilePath;
	}

	/**
	 * スタイルシートファイルパスを取得します。
	 * @return スタイルシートファイルパス
	 */
	public String getStylesheetFilePath() {
	    return stylesheetFilePath;
	}

	/**
	 * スタイルシートファイルパスを設定します。
	 * @param stylesheetFilePath スタイルシートファイルパス
	 */
	public void setStylesheetFilePath(String stylesheetFilePath) {
	    this.stylesheetFilePath = stylesheetFilePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (useDisplayName ? 1231 : 1237);
		result = prime * result + (dispTenantName ? 1231 : 1237);
		result = prime * result
				+ ((screenTitle == null) ? 0 : screenTitle.hashCode());
		result = prime * result
				+ ((localizedScreenTitle == null) ? 0 : localizedScreenTitle.hashCode());
		result = prime * result
				+ ((skin == null) ? 0 : skin.hashCode());
		result = prime * result
				+ ((theme == null) ? 0 : theme.hashCode());
		result = prime * result
				+ ((tenantImageUrl == null) ? 0 : tenantImageUrl.hashCode());
		result = prime * result
				+ ((tenantMiniImageUrl == null) ? 0 : tenantMiniImageUrl.hashCode());
		result = prime * result
				+ ((tenantLargeImageUrl == null) ? 0 : tenantLargeImageUrl.hashCode());
		result = prime * result
				+ ((iconUrl == null) ? 0 : iconUrl.hashCode());
		result = prime * result
				+ ((javascriptFilePath == null) ? 0 : javascriptFilePath.hashCode());
		result = prime * result
				+ ((stylesheetFilePath == null) ? 0 : stylesheetFilePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		TenantGemInfo other = (TenantGemInfo) obj;

		if (useDisplayName != other.useDisplayName)
			return false;

		if (dispTenantName != other.dispTenantName)
			return false;
		
		if (screenTitle == null) {
			if (other.screenTitle != null)
				return false;
		} else if (!screenTitle.equals(other.screenTitle))
			return false;

		if (localizedScreenTitle == null) {
			if (other.localizedScreenTitle != null)
				return false;
		} else if (!localizedScreenTitle.equals(other.localizedScreenTitle))
			return false;

		if (skin == null) {
			if (other.skin != null)
				return false;
		} else if (!skin.equals(other.skin))
			return false;

		if (theme == null) {
			if (other.theme != null)
				return false;
		} else if (!theme.equals(other.theme))
			return false;

		if (tenantImageUrl == null) {
			if (other.tenantImageUrl != null)
				return false;
		} else if (!tenantImageUrl.equals(other.tenantImageUrl))
			return false;

		if (tenantMiniImageUrl == null) {
			if (other.tenantMiniImageUrl != null)
				return false;
		} else if (!tenantMiniImageUrl.equals(other.tenantMiniImageUrl))
			return false;

		if (tenantLargeImageUrl == null) {
			if (other.tenantLargeImageUrl != null)
				return false;
		} else if (!tenantLargeImageUrl.equals(other.tenantLargeImageUrl))
			return false;

		if (iconUrl == null) {
			if (other.iconUrl != null)
				return false;
		} else if (!iconUrl.equals(other.iconUrl))
			return false;

		if (javascriptFilePath == null) {
			if (other.javascriptFilePath != null)
				return false;
		} else if (!javascriptFilePath.equals(other.javascriptFilePath))
			return false;

		if (stylesheetFilePath == null) {
			if (other.stylesheetFilePath != null)
				return false;
		} else if (!stylesheetFilePath.equals(other.stylesheetFilePath))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "TenantWebInfo ["
				+ "useDisplayName=" + useDisplayName
				+ ", dispTenantName=" + dispTenantName
				+ ", screenTitle=" + screenTitle
				+ ", localizedScreenTitle=" + localizedScreenTitle
				+ ", skin=" + skin
				+ ", theme=" + theme
				+ ", tenantImageUrl=" + tenantImageUrl
				+ ", tenantMiniImageUrl=" + tenantMiniImageUrl
				+ ", tenantLargeImageUrl=" + tenantLargeImageUrl
				+ ", iconUrl=" + iconUrl
				+ ", javascriptFilePath=" + javascriptFilePath
				+ ", stylesheetFilePath=" + stylesheetFilePath
				+ "]";
	}

}
