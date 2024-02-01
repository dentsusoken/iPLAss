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

import javax.ws.rs.core.MultivaluedMap;

import org.iplass.mtp.impl.web.ParameterValueMap;

public class MultivaluedMapParameterValueMap implements ParameterValueMap {
	
	private MultivaluedMap<String, String> multivaluedMap;
	
	private HashMap<String, Object> paramMap;
	
	public MultivaluedMapParameterValueMap(MultivaluedMap<String, String> multivaluedMap) {
		this.multivaluedMap = multivaluedMap;
	}

	@Override
	public Object getParam(String name) {
		return multivaluedMap.getFirst(name);
	}

	@Override
	public Object[] getParams(String name) {
		Object val = getParamMap().get(name);
		if (val instanceof String) {
			return new String[]{(String) val};
		} else {
			return (String[]) val;
		}
	}

	@Override
	public Map<String, Object> getParamMap() {
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
			for (Map.Entry<String, List<String>> e: multivaluedMap.entrySet()) {
				List<String> vals = e.getValue();
				if (vals != null && vals.size() > 0) {
					paramMap.put(e.getKey(), vals.toArray(new String[vals.size()]));
				}
			}
		}
		return paramMap;
	}

	@Override
	public Iterator<String> getParamNames() {
		return multivaluedMap.keySet().iterator();
	}

	@Override
	public void cleanTempResource() {
	}

}
