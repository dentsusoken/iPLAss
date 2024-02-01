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

package org.iplass.mtp.impl.prefs;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.prefs.MetaPreference.PreferenceRuntime;
import org.iplass.mtp.prefs.Preference;
import org.iplass.mtp.prefs.PreferenceManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class PreferenceService extends AbstractTypedMetaDataService<MetaPreference, PreferenceRuntime> implements Service {
	public static final String PREFS_META_PATH = "/preference/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<Preference, MetaPreference> {
		public TypeMap() {
			super(getFixedPath(), MetaPreference.class, Preference.class);
		}
		@Override
		public TypedDefinitionManager<Preference> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(PreferenceManager.class);
		}
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return PREFS_META_PATH;
	}

	@Override
	public Class<MetaPreference> getMetaDataType() {
		return MetaPreference.class;
	}

	@Override
	public Class<PreferenceRuntime> getRuntimeType() {
		return PreferenceRuntime.class;
	}

}
