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

package org.iplass.mtp.impl.tenant.gem;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.MetaTenantConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.gem.TenantGemInfo;

public class MetaTenantGemInfo extends MetaTenantConfig<TenantGemInfo> {

	private static final long serialVersionUID = -8193028509449590924L;

	/** テナント名の利用有無 */
	private boolean useDisplayName = true;

	/** ログイン画面、エラー画面でテナント名を表示するか否か */
	private boolean dispTenantName;

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

	public MetaTenantGemInfo() {
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
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TenantGemInfo definition) {
		setUseDisplayName(definition.isUseDisplayName());
		setDispTenantName(definition.isDispTenantName());
		setSkin(definition.getSkin());
		setTheme(definition.getTheme());
		setTenantImageUrl(definition.getTenantImageUrl());
		setTenantMiniImageUrl(definition.getTenantMiniImageUrl());
		setTenantLargeImageUrl(definition.getTenantLargeImageUrl());
		setIconUrl(definition.getIconUrl());
		setJavascriptFilePath(definition.getJavascriptFilePath());
		setStylesheetFilePath(definition.getStylesheetFilePath());
	}

	@Override
	public TenantGemInfo currentConfig() {
		TenantGemInfo definition = new TenantGemInfo();
		definition.setUseDisplayName(useDisplayName);
		definition.setDispTenantName(dispTenantName);
		definition.setSkin(getSkin() != null ? getSkin().toLowerCase() : null);
		definition.setTheme(getTheme() != null ? getTheme().toLowerCase(): null);
		definition.setTenantImageUrl(getTenantImageUrl());
		definition.setTenantMiniImageUrl(getTenantMiniImageUrl());
		definition.setTenantLargeImageUrl(getTenantLargeImageUrl());
		definition.setIconUrl(getIconUrl());
		definition.setJavascriptFilePath(getJavascriptFilePath());
		definition.setStylesheetFilePath(getStylesheetFilePath());
		return definition;
	}

	@Override
	public MetaTenantGemInfoRuntime createRuntime(MetaTenantHandler tenantRuntime) {
		return new MetaTenantGemInfoRuntime();
	}

	public class MetaTenantGemInfoRuntime extends MetaTenantConfigRuntime {

		public MetaTenantGemInfoRuntime() {
		}

		@Override
		public MetaData getMetaData() {
			return MetaTenantGemInfo.this;
		}

		@Override
		public void applyMetaDataToTenant(Tenant tenant) {
		}
	}

}
