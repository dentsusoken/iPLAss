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

package org.iplass.mtp.tenant.web;

import org.iplass.mtp.tenant.TenantConfig;

public class TenantWebInfo extends TenantConfig {

	private static final long serialVersionUID = -8157534097214464426L;

	/** プレビュー機能をONにするか否か */
	private boolean usePreview;

	/** ログインURLセレクター(Action名) */
	private String loginUrlSelector;

	/** 再認証URLセレクター */
	private String reAuthUrlSelector;

	/** エラーURLセレクター（Template名） */
	private String errorUrlSelector;

	/** HOMEのURL */
	private String homeUrl;

	/** リクエストパス構築用のテナントURL。
	 * (HTTPサーバにて、/をtenantへマッピングしている場合などの場合に利用)
	 */
	private String urlForRequest;

	public TenantWebInfo() {
	}

	/**
	 * プレビュー機能が有効かどうかを取得します。
	 *
	 * @return
	 */
	public boolean isUsePreview() {
		return usePreview;
	}

	/**
	 * プレビュー機能を有効にするか否かを設定します。
	 *
	 * @param usePreview
	 */
	public void setUsePreview(boolean usePreview) {
		this.usePreview = usePreview;
	}

	public String getLoginUrlSelector() {
		return loginUrlSelector;
	}

	public void setLoginUrlSelector(String loginUrlSelector) {
		this.loginUrlSelector = loginUrlSelector;
	}
	public String getReAuthUrlSelector() {
		return reAuthUrlSelector;
	}

	public void setReAuthUrlSelector(String reAuthUrlSelector) {
		this.reAuthUrlSelector = reAuthUrlSelector;
	}

	public String getErrorUrlSelector() {
		return errorUrlSelector;
	}

	public void setErrorUrlSelector(String errorUrlSelector) {
		this.errorUrlSelector = errorUrlSelector;
	}

	/**
	 * HOMEのURLを取得します。
	 * @return HOMEのURL
	 */
	public String getHomeUrl() {
		return homeUrl;
	}

	/**
	 * HOMEのURLを設定します。
	 *
	 * @param HOMEのURL
	 */
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	/**
	 * urlForRequestを取得します。
	 * @return urlForRequest
	 */
	public String getUrlForRequest() {
		return urlForRequest;
	}

	/**
	 * urlForRequestを設定します。
	 * @param urlForRequest urlForRequest
	 */
	public void setUrlForRequest(String urlForRequest) {
		this.urlForRequest = urlForRequest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (usePreview ? 1231 : 1237);
		result = prime * result
				+ ((loginUrlSelector == null) ? 0 : loginUrlSelector.hashCode());
		result = prime * result
				+ ((reAuthUrlSelector == null) ? 0 : reAuthUrlSelector.hashCode());
		result = prime * result
				+ ((errorUrlSelector == null) ? 0 : errorUrlSelector.hashCode());
		result = prime * result
				+ ((homeUrl == null) ? 0 : homeUrl.hashCode());
		result = prime * result
				+ ((urlForRequest == null) ? 0 : urlForRequest.hashCode());
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

		TenantWebInfo other = (TenantWebInfo) obj;

		if (usePreview != other.usePreview)
			return false;

		if (loginUrlSelector == null) {
			if (other.loginUrlSelector != null)
				return false;
		} else if (!loginUrlSelector.equals(other.loginUrlSelector))
			return false;

		if (reAuthUrlSelector == null) {
			if (other.reAuthUrlSelector != null)
				return false;
		} else if (!reAuthUrlSelector.equals(other.reAuthUrlSelector))
			return false;

		if (errorUrlSelector == null) {
			if (other.errorUrlSelector != null)
				return false;
		} else if (!errorUrlSelector.equals(other.errorUrlSelector))
			return false;

		if (homeUrl == null) {
			if (other.homeUrl != null)
				return false;
		} else if (!homeUrl.equals(other.homeUrl))
			return false;

		if (urlForRequest == null) {
			if (other.urlForRequest != null)
				return false;
		} else if (!urlForRequest.equals(other.urlForRequest))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "TenantWebInfo ["
				+ "usePreview=" + usePreview
				+ ", loginUrlSelector=" + loginUrlSelector
				+ ", reAuthUrlSelector=" + reAuthUrlSelector
				+ ", errorUrlSelector=" + errorUrlSelector
				+ ", homeUrl=" + homeUrl
				+ ", urlForRequest=" + urlForRequest
				+ "]";
	}

}
