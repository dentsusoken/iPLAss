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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantConfig;
import org.iplass.mtp.tenant.TenantManager;


/**
 * テナントメタ情報管理サービス
 *
 * @author 藤田　義弘
 *
 */
public class MetaTenantService extends AbstractTypedMetaDataService<MetaTenant, MetaTenantHandler> implements Service {

	/** メタデータ登録パス **/
	public static final String TENANT_META_PATH = "/tenant/";

	/** メタデータ固定名 **/
	private static final String TENANT_FIXED_NAME = "Tenant";

	/** Config Mapping定義 **/
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends TenantConfig>, Class<? extends MetaTenantConfig>> configTypeMap;

	/** Config Script Binding Mapping定義 **/
	private Map<String, Class<? extends TenantConfig>> configBindNameMap;

	public static class TypeMap extends DefinitionMetaDataTypeMap<Tenant, MetaTenant> {
		public TypeMap() {
			super(getFixedPath(), MetaTenant.class, Tenant.class);
		}
		@Override
		public TypedDefinitionManager<Tenant> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(TenantManager.class);
		}
		@Override
		public Tenant toDefinition(MetaTenant metaData) {
			return typedDefinitionManager().get(null);
		}
		@Override
		public String toPath(String defName) {
			//pathは固定
			return pathPrefix + TENANT_FIXED_NAME;
		}
		@Override
		public String toDefName(String path) {
			//nameは固定
			return TENANT_FIXED_NAME;
		}
	}

	public static class ConfigType {
		private String definitionClass;
		private String metadataClass;
		private String scriptBindingName;

		public ConfigType() {}

		public String getDefinitionClass() {
			return definitionClass;
		}

		public void setDefinitionClass(String definitionClass) {
			this.definitionClass = definitionClass;
		}

		public String getMetadataClass() {
			return metadataClass;
		}

		public void setMetadataClass(String metadataClass) {
			this.metadataClass = metadataClass;
		}

		public String getScriptBindingName() {
			return scriptBindingName;
		}

		public void setScriptBindingName(String scriptBindingName) {
			this.scriptBindingName = scriptBindingName;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void init(Config config) {

		List<ConfigType> configTypes = config.getValues("configType", ConfigType.class);
		if (configTypes != null) {
			configTypeMap = new HashMap<>();
			configBindNameMap = new HashMap<>();
			configTypes.forEach(type->{
				Class<? extends TenantConfig> defClass = null;
				try {
					defClass = (Class<? extends TenantConfig>) Class.forName(type.getDefinitionClass());
				} catch (ClassNotFoundException | ClassCastException e) {
					throw new ServiceConfigrationException("configType:" + type.getDefinitionClass() + " can't instanceate.");
				}
				Class<? extends MetaTenantConfig> metaClass = null;
				try {
					metaClass = (Class<? extends MetaTenantConfig>) Class.forName(type.getMetadataClass());
				} catch (ClassNotFoundException | ClassCastException e) {
					throw new ServiceConfigrationException("configType:" + type.getMetadataClass() + " can't instanceate.");
				}
				configTypeMap.put(defClass, metaClass);
				configBindNameMap.put(type.getScriptBindingName(), defClass);
			});
		} else {
			configTypeMap = Collections.emptyMap();
			configBindNameMap = Collections.emptyMap();
		}

	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return TENANT_META_PATH;
	}

	@Override
	public Class<MetaTenant> getMetaDataType() {
		return MetaTenant.class;
	}

	@Override
	public Class<MetaTenantHandler> getRuntimeType() {
		return MetaTenantHandler.class;
	}

	@Override
	public void createMetaData(MetaTenant meta) {
		//id、name固定
		meta.setId(TENANT_FIXED_NAME);
		meta.setName(TENANT_FIXED_NAME);
		MetaDataContext.getContext().store(TENANT_META_PATH + TENANT_FIXED_NAME, meta);
	}

	@Override
	public void updateMetaData(MetaTenant meta) {
		if (get() != null) {
			//id、name固定
			meta.setId(TENANT_FIXED_NAME);
			meta.setName(TENANT_FIXED_NAME);
			super.updateMetaData(meta);
		} else {
			createMetaData(meta);
		}
	}

	public MetaTenantHandler get() {
		//名前は固定
		return MetaDataContext.getContext().getMetaDataHandler(MetaTenantHandler.class, TENANT_META_PATH + TENANT_FIXED_NAME);
	}

	@Override
	public MetaTenantHandler getRuntimeById(String id) {
		return get();// id使わない
	}

	@Override
	public MetaTenantHandler getRuntimeByName(String name) {
		return get();// name使わない
	}

	@SuppressWarnings("rawtypes")
	public MetaTenantConfig toMetaConfig(TenantConfig tenantConfig) {
		Class<? extends MetaTenantConfig> metaClass = configTypeMap.get(tenantConfig.getClass());
		if (metaClass != null) {
			try {
				return metaClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				return null;
			}
		}
		return null;
	}

	public TenantConfig getBindTenantConfig(Tenant tenant, String bindName) {
		Class<? extends TenantConfig> defTypeClass = configBindNameMap.get(bindName);
		if (defTypeClass != null) {
			return tenant.getTenantConfig(defTypeClass);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	Collection<Class<? extends MetaTenantConfig>> getMetaTenantConfigClasses() {
		return configTypeMap.values();
	}

}
