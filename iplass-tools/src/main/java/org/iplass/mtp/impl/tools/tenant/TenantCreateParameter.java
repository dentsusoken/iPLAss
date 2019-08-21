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

package org.iplass.mtp.impl.tools.tenant;

/**
 * テナント作成用パラメータ情報
 *
 */
public class TenantCreateParameter {

	// テナント名
	private String tenantName;
	// テナントURL
	private String tenantUrl;
	// テナント表示名
	private String tenantDisplayName;
	// 初期ユーザID
	private String adminUserId;
	// パスワード
	private String adminPassword;
	//Top画面URL
	private String topUrl;
	//アイコンURL
	private String iconUrl;
	//利用言語(カンマ指定、ex ja,en)
	private String useLanguages;

	//ブランクテナントの作成（Tenant情報、管理者のみ作成）
	private boolean createBlankTenant = false;

	//TODO 暫定対応 サブパーティション利用有無 (MySQL時のみ利用)
	private boolean isMySqlUseSubPartition = false;

	//テナント作成者（誰が使ってるテナントかわかるよう、出来れば変えたい）
	private String registId = "program";

	//ログ出力時のLang
	private String loggerLanguage;

	public TenantCreateParameter(String tenantName, String adminUserId, String adminPassword) {
		this.tenantName = tenantName;
		this.adminUserId = adminUserId;
		this.adminPassword = adminPassword;

		tenantUrl = "/" + tenantName;
		tenantDisplayName = "";
		topUrl = "";
		iconUrl = "/favicon.ico";
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getTenantUrl() {
		return tenantUrl;
	}

	public void setTenantUrl(String tenantUrl) {
		this.tenantUrl = tenantUrl;
	}

	public String getTenantDisplayName() {
		return tenantDisplayName;
	}

	public void setTenantDisplayName(String tenantDisplayName) {
		this.tenantDisplayName = tenantDisplayName;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getRegistId() {
		return registId;
	}

	public void setRegistId(String registId) {
		this.registId = registId;
	}

	public String getTopUrl() {
		return topUrl;
	}

	public void setTopUrl(String topUrl) {
		this.topUrl = topUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public boolean isCreateBlankTenant() {
		return createBlankTenant;
	}

	public void setCreateBlankTenant(boolean createBlankTenant) {
		this.createBlankTenant = createBlankTenant;
	}

	public boolean isMySqlUseSubPartition() {
	    return isMySqlUseSubPartition;
	}

	public void setMySqlUseSubPartition(boolean isMySqlUseSubPartition) {
	    this.isMySqlUseSubPartition = isMySqlUseSubPartition;
	}

	public String getUseLanguages() {
		return useLanguages;
	}
	public void setUseLanguages(String useLanguages) {
	    this.useLanguages = useLanguages;
	}

	public String getLoggerLanguage() {
		return loggerLanguage;
	}

	public void setLoggerLanguage(String loggerLanguage) {
		this.loggerLanguage = loggerLanguage;
	}

}
