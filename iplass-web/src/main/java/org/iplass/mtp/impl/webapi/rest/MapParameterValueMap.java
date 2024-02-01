/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.webapi.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.web.ParameterValueMap;

public class MapParameterValueMap implements ParameterValueMap {
	
	private Map<String, Object> map;
	
	private Map<String, String[]> arrayCache;
	
	public MapParameterValueMap(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public Object getParam(String name) {
		Object val = map.get(name);
		if (val instanceof Object[]) {
			Object[] valArray = (Object[]) val;
			if (valArray.length == 0) {
				return null;
			} else {
				return valArray[0];
			}
		} else if (val instanceof List) {
			@SuppressWarnings("rawtypes")
			List valList = (List) val;
			if (valList.size() == 0) {
				return null;
			} else {
				return valList.get(0);
			}
		} else {
			return val;
		}
	}
	
	@Override
	public Object[] getParams(String name) {
		if (arrayCache == null) {
			arrayCache = new HashMap<String, String[]>();
		}
		String[] array = arrayCache.get(name);
		if (array == null) {
			Object val = map.get(name);
			if (val != null) {
				if (val instanceof List) {
					@SuppressWarnings("rawtypes")
					List value = (List) val;
					array = new String[value.size()];
					for (int i = 0; i < value.size(); i++) {
						Object o = value.get(i);
						if (o != null) {
							array[i] = o.toString();
						} else {
							array[i] = null;
						}
					}
				} else if (val instanceof Object[]) {
					Object[] value = (Object[]) val;
					array = new String[value.length];
					for (int i = 0; i < value.length; i++) {
						Object o = value[i];
						if (o != null) {
							array[i] = o.toString();
						} else {
							array[i] = null;
						}
					}
				} else {
					array = new String[1];
					array[0] = val.toString();
				}
				arrayCache.put(name, array);
			}
		}
		return array;
	}

	@Override
	public Map<String, Object> getParamMap() {
		return map;
	}

	@Override
	public Iterator<String> getParamNames() {
		return map.keySet().iterator();
	}

	@Override
	public void cleanTempResource() {
	}

}
