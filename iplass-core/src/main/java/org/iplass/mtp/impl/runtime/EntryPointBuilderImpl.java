/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.runtime;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.runtime.EntryPoint;
import org.iplass.mtp.runtime.EntryPointBuilder;

public class EntryPointBuilderImpl implements EntryPointBuilder {
	
	private Map<String, String> customProps;
	
	private Map<String, String> customProps() {
		if (customProps == null) {
			customProps = new HashMap<>();
		}
		return customProps;
	}

	@Override
	public EntryPoint build() {
		return EntryPointImpl.newEntryPointImpl(customProps);
	}

	@Override
	public EntryPointBuilder serverEnvFile(String serverEnvFile) {
		customProps().put(BootstrapProps.SERVER_ENV_PROP_FILE_NAME, serverEnvFile);
		return this;
	}

	@Override
	public EntryPointBuilder serverId(String serverId) {
		customProps().put(BootstrapProps.SERVER_ID, serverId);
		return this;
	}

	@Override
	public EntryPointBuilder serverRole(String... serverRole) {
		String concatRoles = null;
		if (serverRole != null) {
			if (serverRole.length == 1) {
				concatRoles = serverRole[0];
			} else {
				concatRoles = String.join(",", serverRole);
			}
		}
		
		customProps().put(BootstrapProps.SERVER_ROLES, concatRoles);
		return this;
	}

	@Override
	public EntryPointBuilder config(String configFileName) {
		customProps().put(BootstrapProps.CONFIG_FILE_NAME, configFileName);
		return this;
	}

	@Override
	public EntryPointBuilder crypt(String configCryptFileName) {
		customProps().put(BootstrapProps.CRYPT_CONFIG_FILE_NAME, configCryptFileName);
		return this;
	}

	@Override
	public EntryPointBuilder loader(String loaderClassName) {
		customProps().put(BootstrapProps.CONFIG_LOADER_CLASS_NAME, loaderClassName);
		return this;
	}

	@Override
	public EntryPointBuilder property(String name, String value) {
		customProps().put(name, value);
		return this;
	}

}
