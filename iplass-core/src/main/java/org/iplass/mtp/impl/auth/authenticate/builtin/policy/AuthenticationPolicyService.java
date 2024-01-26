/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin.policy;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class AuthenticationPolicyService extends AbstractTypedMetaDataService<MetaAuthenticationPolicy, AuthenticationPolicyRuntime> implements Service {
	public static final String AUTHN_POLICY_PATH = "/authNPolicy/";
	public static final String DEFAULT_NAME = "DEFAULT";

	public static class TypeMap extends DefinitionMetaDataTypeMap<AuthenticationPolicyDefinition, MetaAuthenticationPolicy> {
		public TypeMap() {
			super(getFixedPath(), MetaAuthenticationPolicy.class, AuthenticationPolicyDefinition.class);
		}
		@Override
		public TypedDefinitionManager<AuthenticationPolicyDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(AuthenticationPolicyDefinitionManager.class);
		}
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return AUTHN_POLICY_PATH;
	}

	public AuthenticationPolicyRuntime getOrDefault(String name) {
		if (name == null) {
			name = DEFAULT_NAME;
		}
		AuthenticationPolicyRuntime r = MetaDataContext.getContext().getMetaDataHandler(AuthenticationPolicyRuntime.class, AUTHN_POLICY_PATH  + name);
		if (r == null && !DEFAULT_NAME.equals(name)) {
			r = MetaDataContext.getContext().getMetaDataHandler(AuthenticationPolicyRuntime.class, AUTHN_POLICY_PATH  + DEFAULT_NAME);
		}
		return r;
	}

	@Override
	public Class<MetaAuthenticationPolicy> getMetaDataType() {
		return MetaAuthenticationPolicy.class;
	}

	@Override
	public Class<AuthenticationPolicyRuntime> getRuntimeType() {
		return AuthenticationPolicyRuntime.class;
	}

}
