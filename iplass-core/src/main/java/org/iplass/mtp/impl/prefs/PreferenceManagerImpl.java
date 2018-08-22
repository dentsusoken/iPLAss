/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Collections;
import java.util.Map;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.prefs.MetaPreference.PreferenceRuntime;
import org.iplass.mtp.prefs.Preference;
import org.iplass.mtp.prefs.PreferenceManager;
import org.iplass.mtp.prefs.PreferenceSet;
import org.iplass.mtp.spi.ServiceRegistry;

public class PreferenceManagerImpl extends AbstractTypedDefinitionManager<Preference> implements PreferenceManager {

	private PreferenceService service = ServiceRegistry.getRegistry().getService(PreferenceService.class);

	@Override
	public Preference get(String name) {
		return get(name, true);
	}

	public Preference get(String name, boolean nullAsDefault) {
		Preference definition = super.get(name);
		if (definition == null) {
			if (nullAsDefault) {
				return new PreferenceSet(name);
			} else {
				return null;
			}
		}
		return definition;
	}

	@Override
	public Map<String, Object> getAsMap(String name) {
		PreferenceRuntime runtime =  service.getRuntimeByName(name);
		if (runtime == null) {
			return Collections.emptyMap();
		}
		return runtime.getMap();
	}

	@Override
	public Object getRuntime(String name) {
		PreferenceRuntime runtime =  service.getRuntimeByName(name);
		if (runtime == null) {
			return null;
		}
		return runtime.getRuntime();
	}

	@Override
	public Class<Preference> getDefinitionType() {
		return Preference.class;
	}

	@Override
	protected RootMetaData newInstance(Preference definition) {
		return MetaPreference.newMeta(definition);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
