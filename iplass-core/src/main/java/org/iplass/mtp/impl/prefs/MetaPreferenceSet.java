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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.prefs.Preference;
import org.iplass.mtp.prefs.PreferenceSet;

public class MetaPreferenceSet extends MetaPreference {
	private static final long serialVersionUID = -4873425413256055751L;

	private List<MetaPreference> subSet;

	public List<MetaPreference> getSubSet() {
		return subSet;
	}

	public void setSubSet(List<MetaPreference> subSet) {
		this.subSet = subSet;
	}

	@Override
	public PreferenceSetRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new PreferenceSetRuntime();
	}

	@Override
	public void applyConfig(Preference def) {
		super.applyConfig(def);
		PreferenceSet defSet = (PreferenceSet) def;
		if (defSet.getSubSet() != null) {
			ArrayList<MetaPreference> set = new ArrayList<>();
			for (Preference p: defSet.getSubSet()) {
				MetaPreference mp = MetaPreference.newMeta(p);
				mp.applyConfig(p);
				set.add(mp);
			}
			subSet = set;
		} else {
			subSet = null;
		}
	}

	@Override
	public Preference currentConfig() {
		PreferenceSet def = new PreferenceSet();
		fillTo(def);
		if (subSet != null) {
			ArrayList<Preference> set = new ArrayList<>();
			for (MetaPreference mp: subSet) {
				set.add(mp.currentConfig());
			}
			def.setSubSet(set);
		}
		return def;
	}

	public class PreferenceSetRuntime extends PreferenceRuntime {
		protected Map<String, Object> map;

		public PreferenceSetRuntime() {
			map = new LinkedHashMap<>();
			if (getValue() != null) {
				map.put(VALUE_KEY, getValue());
			}
			if (subSet != null) {
				applyToMap(map, subSet);
			}
			map = Collections.unmodifiableMap(map);

			try {
				if (subSet != null && runtime != null) {
					ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
					GroovyScriptEngine gse = (GroovyScriptEngine) se;
					ClassLoader cl = gse.getSharedClassLoader();
					applyToBean(runtime, subSet, cl);
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			} catch (Exception e) {
				setIllegalStateException(new IllegalStateException(e));
			}
		}

		@Override
		public Map<String, Object> getMap() {
			return map;
		}

		@SuppressWarnings("unchecked")
		private void applyToMap(Map<String, Object> map, List<MetaPreference> set) {
			if (set != null) {
				for (MetaPreference p: set) {
					Object pre = map.get(p.getName());
					if (p instanceof MetaPreferenceSet) {
						Map<String, Object> subMap = new LinkedHashMap<>();
						applyToMap(subMap, ((MetaPreferenceSet) p).getSubSet());
						subMap = Collections.unmodifiableMap(subMap);
						if (pre == null) {
							map.put(p.getName(), subMap);
						} else if (pre instanceof Map) {
							List<Map> newMaps = new ArrayList<>();
							newMaps.add((Map) pre);
							newMaps.add(subMap);
							map.put(p.getName(), newMaps);
						} else if (pre instanceof List) {
							((List) pre).add(subMap);
						}
					} else {
						if (pre == null) {
							map.put(p.getName(), p.getValue());
						} else if (pre instanceof String) {
							List<String> newStrs = new ArrayList<>();
							newStrs.add((String) pre);
							newStrs.add(p.getValue());
							map.put(p.getName(), newStrs);
						} else if (pre instanceof List) {
							((List) pre).add(p.getValue());
						}
					}
				}

				//Listのunmodifiable化
				for (Map.Entry<String, Object> e: map.entrySet()) {
					if (e.getValue() instanceof List) {
						e.setValue(Collections.unmodifiableList((List) e.getValue()));
					}
				}
			}
		}

		@Override
		public MetaPreferenceSet getMetaData() {
			return MetaPreferenceSet.this;
		}

	}
}
