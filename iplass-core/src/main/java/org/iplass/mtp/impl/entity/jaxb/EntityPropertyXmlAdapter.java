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

package org.iplass.mtp.impl.entity.jaxb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;

/**
 * GenericEntityのプロパティ（HashMap）のXmlAdapter。
 * 
 * @author K.Higuchi
 *
 */
public class EntityPropertyXmlAdapter extends XmlAdapter<JaxbProperty[], Map<String, Object>> {

	@Override
	public Map<String, Object> unmarshal(JaxbProperty[] v) throws Exception {
		
		HashMap<String, Object> props = new HashMap<String, Object>();
		for (JaxbProperty p: v) {
			if (p.value != null) {
				props.put(p.name, p.value);
			}
		}
		if (props.size() == 0) {
			return null;
		} else {
			return props;
		}
	}

	@Override
	public JaxbProperty[] marshal(Map<String, Object> v) throws Exception {
		List<JaxbProperty> property = new ArrayList<JaxbProperty>();
		for (Map.Entry<String, Object> e: v.entrySet()) {
			Object val = e.getValue();
			if (val != null) {
				JaxbProperty p = new JaxbProperty();
				p.name = e.getKey();
				if (val instanceof Entity[]) {
					if (val.getClass() == GenericEntity[].class) {
						p.value = val;
					} else {
						Entity[] src = (Entity[]) val;
						GenericEntity[] dest = new GenericEntity[src.length];
						System.arraycopy(src, 0, dest, 0, src.length);
						p.value = dest;
					}
				} else {
					p.value = val;
				}
				property.add(p);
			}
		}
		return property.toArray(new JaxbProperty[property.size()]);
	}

}
