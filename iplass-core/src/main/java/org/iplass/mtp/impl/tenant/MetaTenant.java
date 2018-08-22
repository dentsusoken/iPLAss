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

package org.iplass.mtp.impl.tenant;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.tenant.MetaTenantConfig.MetaTenantConfigRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

/**
 * テナントのメタ情報
 * @author 藤田 義弘
 *
 */
@XmlRootElement
public class MetaTenant extends BaseRootMetaData implements DefinableMetaData<Tenant> {

	/** SerialVersionUID */
	private static final long serialVersionUID = -891000660283577842L;

	/** テナント設定情報 */
	@SuppressWarnings("rawtypes")
	private List<MetaTenantConfig> tenantConfigs;

	/**
	 * デフォルトコンストラクタ
	 *
	 */
	public MetaTenant() {
	}

	/**
	 * コンストラクタ
	 *
	 * <p>{@link Tenant} から {@link MetaTenant} を作成します。
	 *
	 * @param tenant {@link Tenant}
	 */
	public MetaTenant(Tenant tenant) {
		applyConfig(tenant);
	}

	@Override
	public MetaTenant copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaTenantHandler createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaTenantHandler();
	}

	@SuppressWarnings("rawtypes")
	public List<MetaTenantConfig> getTenantConfigs() {
		return tenantConfigs;
	}

	@SuppressWarnings("rawtypes")
	public void setTenantConfigs(List<MetaTenantConfig> tenantConfigs) {
		this.tenantConfigs = tenantConfigs;
	}

	/**
	 * {@link Tenant} に値を設定します。
	 *
	 * @param tenant {@link Tenant}
	 */
	public void applyToTenant(final Tenant tenant) {

		//DB側優先
		//tenant.setName(getName());
		tenant.setDescription(getDescription());
		tenant.setDisplayName(getDisplayName());
		tenant.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		if (tenantConfigs != null) {
			tenant.setTenantConfigs(
					tenantConfigs.stream()
							.map(config -> config.currentConfig())
							.collect(Collectors.toList()));
		} else {
			tenant.setTenantConfigs(null);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void applyConfig(Tenant definition) {

		setName(definition.getName());
		setDescription(definition.getDescription());
		setDisplayName(definition.getDisplayName());

		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());

		if (definition.getTenantConfigs() != null) {
			final MetaTenantService mts = ServiceRegistry.getRegistry().getService(MetaTenantService.class);
			//クラス名でソート
			tenantConfigs = definition.getTenantConfigs().stream()
					.map(defConfig -> {
						@SuppressWarnings("rawtypes")
						MetaTenantConfig metaConfig = mts.toMetaConfig(defConfig);
						if (metaConfig != null) {
							metaConfig.applyConfig(defConfig);
						}
						return metaConfig;
					})
					.filter(metaConfig -> metaConfig != null)
					.sorted((config1, config2) -> config1.getClass().getName().compareTo(config2.getClass().getName()))
					.collect(Collectors.toList());
		} else {
			tenantConfigs = null;
		}
	}

	@Override
	public Tenant currentConfig() {
		Tenant definition = new Tenant();
		applyToTenant(definition);
		return definition;
	}


	public class MetaTenantHandler extends BaseMetaDataRuntime {

		@SuppressWarnings("rawtypes")
		private Map<String, MetaTenantConfigRuntime> tenantConfigsRuntimes;

		public MetaTenantHandler() {

			if (tenantConfigs != null) {
				tenantConfigsRuntimes = tenantConfigs.stream().map(config -> config.createRuntime(this))
				.collect(Collectors.toMap(runtime -> runtime.getClass().getName(), runtime -> runtime));
			} else {
				tenantConfigsRuntimes = Collections.emptyMap();
			}

		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public <R extends MetaTenantConfigRuntime> R getConfigRuntime(Class<R> tenantConfigClass) {
			return (R)tenantConfigsRuntimes.get(tenantConfigClass.getName());
		}

		@Override
		public MetaTenant getMetaData() {
			return MetaTenant.this;
		}

		public void applyMetaDataToTenant(final Tenant tenant) {
			applyToTenant(tenant);

			tenantConfigsRuntimes.values().forEach(runtime -> runtime.applyMetaDataToTenant(tenant));
		}
	}

}
