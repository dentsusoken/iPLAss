/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tenant;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;

/**
 * テナント認証情報のメタデータ
 *
 * @author 藤田 義弘
 *
 */
public class MetaTenantAuthInfo extends MetaTenantConfig<TenantAuthInfo> {

	/** Serial Version UID	 */
	private static final long serialVersionUID = -1957829955963005918L;

	/** Remember Me 利用有無*/
	private boolean useRememberMe;
	private boolean useWebAuthn;
	private String webAuthnDefinitionName;

	private List<String> userAdminRoles;

	/**
	 * Constractor
	 */
	public MetaTenantAuthInfo() {
	}

	/**
	 * ユーザーを管理可能とするロールを取得します。
	 *
	 * @return
	 */
	public List<String> getUserAdminRoles() {
		return userAdminRoles;
	}

	/**
	 * ユーザーを管理可能（パスワードリセット、accountPlicyの設定、adminフラグの設定など）とするロールを指定します。
	 * adminフラグを更新不可としたい場合は、別途当該ロールにEntity権限を設定します。
	 *
	 * @param userAdminRoles
	 */
	public void setUserAdminRoles(List<String> userAdminRoles) {
		this.userAdminRoles = userAdminRoles;
	}

	public boolean isUseRememberMe() {
		return useRememberMe;
	}

	public void setUseRememberMe(boolean useRememberMe) {
		this.useRememberMe = useRememberMe;
	}

	public boolean isUseWebAuthn() {
		return useWebAuthn;
	}

	public void setUseWebAuthn(boolean useWebAuthn) {
		this.useWebAuthn = useWebAuthn;
	}

	public String getWebAuthnDefinitionName() {
		return webAuthnDefinitionName;
	}

	public void setWebAuthnDefinitionName(String webAuthnDefinitionName) {
		this.webAuthnDefinitionName = webAuthnDefinitionName;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TenantAuthInfo definition) {
		setUseRememberMe(definition.isUseRememberMe());
		setUseWebAuthn(definition.isUseWebAuthn());
		setWebAuthnDefinitionName(definition.getWebAuthnDefinitionName());
		if (definition.getUserAdminRoles() != null) {
			setUserAdminRoles(new ArrayList<>(definition.getUserAdminRoles()));
		} else {
			setUserAdminRoles(null);
		}
	}

	@Override
	public TenantAuthInfo currentConfig() {
		TenantAuthInfo definition = new TenantAuthInfo();
		definition.setUseRememberMe(useRememberMe);
		definition.setUseWebAuthn(useWebAuthn);
		definition.setWebAuthnDefinitionName(webAuthnDefinitionName);
		if (userAdminRoles != null) {
			definition.setUserAdminRoles(new ArrayList<>(userAdminRoles));
		}
		return definition;
	}

	@Override
	public MetaTenantAuthInfoRuntime createRuntime(MetaTenantHandler tenantRuntime) {
		return new MetaTenantAuthInfoRuntime();
	}

	public class MetaTenantAuthInfoRuntime extends MetaTenantConfigRuntime {

		@Override
		public MetaData getMetaData() {
			return MetaTenantAuthInfo.this;
		}

		@Override
		public void applyMetaDataToTenant(Tenant tenant) {
		}

	}

}
