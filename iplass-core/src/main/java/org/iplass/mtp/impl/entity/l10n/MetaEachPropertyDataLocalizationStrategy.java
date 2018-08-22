/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.entity.l10n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.definition.DataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachPropertyDataLocalizationStrategy;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;


public class MetaEachPropertyDataLocalizationStrategy extends MetaDataLocalizationStrategy {
	private static final long serialVersionUID = 7582698366671336713L;
	
	public MetaEachPropertyDataLocalizationStrategy() {
	}
	public MetaEachPropertyDataLocalizationStrategy(List<String> languages) {
		super(languages);
	}
	
	@Override
	public MetaDataLocalizationStrategy copy() {
		MetaEachPropertyDataLocalizationStrategy copy = new MetaEachPropertyDataLocalizationStrategy();
		copyTo(copy);
		return copy;
	}

	@Override
	public void applyConfig(DataLocalizationStrategy def) {
		fillFrom(def);
	}

	@Override
	public DataLocalizationStrategy currentConfig() {
		EachPropertyDataLocalizationStrategy def = new EachPropertyDataLocalizationStrategy();
		fillTo(def);
		return def;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public DataLocalizationStrategyRuntime createDataLocalizationStrategyRuntime(EntityHandler eh) {
		return new EachPropertyDataLocalizationStrategyRuntime(eh);
	}
	
	public class EachPropertyDataLocalizationStrategyRuntime extends DataLocalizationStrategyRuntime {
		private Map<String, Map<String, String>> map;
		private Map<String, String> defaultMap;
		private EntityHandler eh;
		
		private EachPropertyDataLocalizationStrategyRuntime(EntityHandler eh) {
			this.eh = eh;
			map = new HashMap<>();
			
			Set<String> propNames = new HashSet<>();
			MetaEntity meta = eh.getMetaData();
			while (meta != null) {
				List<MetaProperty> plist = meta.getDeclaredPropertyList();
				for (MetaProperty p: plist) {
					propNames.add(p.getName());
				}
				if (meta.getInheritedEntityMetaDataId() != null) {
					EntityHandler superEh = EntityContext.getCurrentContext().getHandlerById(meta.getInheritedEntityMetaDataId());
					if (superEh != null) {
						meta = superEh.getMetaData();
					} else {
						meta = null;
					}
				} else {
					meta = null;
				}
			}
			
			Set<String> subProps = new HashSet<>();
			if (getLanguages() != null) {
				for (String lang: getLanguages()) {
					String userLangPostFix = "_" + lang.replace('-', '_');
					Map<String, String> propMap = new HashMap<>();
					for (String pname: propNames) {
						String lpn = pname + userLangPostFix;
						if (propNames.contains(lpn)) {
							propMap.put(pname, lpn);
							subProps.add(lpn);
						} else if (!subProps.contains(pname)) {
							propMap.put(pname, null);
						}
					}
					map.put(lang, propMap);
				}
			}
			
			for (Map.Entry<String, Map<String, String>> e: map.entrySet()) {
				Iterator<Map.Entry<String, String>> it = e.getValue().entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> ee = it.next();
					if (subProps.contains(ee.getKey())) {
						it.remove();
					}
				}
			}
			
			//create defaultMap
			defaultMap = new HashMap<>();
			if (getLanguages() != null && getLanguages().size() > 0) {
				Map<String, String> ent = map.values().iterator().next();
				for (String k: ent.keySet()) {
					defaultMap.put(k, null);
				}
				
			} else {
				for (String pn: propNames) {
					defaultMap.put(pn, null);
				}
			}
			
			
		}
		
		public Map<String, String> getPropMap(String userLang) {
			return map.getOrDefault(userLang, defaultMap);
		}

		@Override
		public void handleEntityForInsert(Entity e, InsertOption option) {
			String userLang = ExecuteContext.getCurrentContext().getLanguage();
			//special for name
			String entityNameValue = e.getName();
			Map<String, String> propMap = map.get(userLang);
			if (propMap != null) {
				for (Map.Entry<String, String> ft: propMap.entrySet()) {
					if (ft.getValue() != null) {
						Object val = e.getValue(ft.getKey());
						if (val != null) {
							e.setValue(ft.getKey(), null);
							e.setValue(ft.getValue(), val);
						}
					}
				}
			}
			e.setName(entityNameValue);
		}

		@Override
		public void handleEntityForUpdate(Entity e, UpdateOption option) {
			String userLang = ExecuteContext.getCurrentContext().getLanguage();
			Map<String, String> propMap = map.get(userLang);
			if (propMap != null && propMap.size() > 0) {
				ArrayList<String> ups = new ArrayList<>();
				for (String prop: option.getUpdateProperties()) {
					String lpn = propMap.get(prop);
					if (lpn != null) {
						Object val = e.getValue(prop);
						if (val != null) {
							e.setValue(prop, null);
							e.setValue(lpn, val);
						}
						ups.add(lpn);
					} else {
						ups.add(prop);
					}
				}
				option.setUpdateProperties(ups);
			}
		}

		@Override
		public Entity handleEntityForLoad(Entity e) {
			if (e == null) {
				return null;
			}
			String userLang = ExecuteContext.getCurrentContext().getLanguage();
			Map<String, String> propMap = map.getOrDefault(userLang, defaultMap);
			if (propMap != null && propMap.size() > 0) {
				Entity ret = eh.newInstance();
				for (Map.Entry<String, String> me: propMap.entrySet()) {
					String lpn = me.getValue();
					if (lpn == null) {
						lpn = me.getKey();
					}
					ret.setValue(me.getKey(), e.getValue(lpn));
				}
				return ret;
			} else {
				return e;
			}
		}
	}


}
