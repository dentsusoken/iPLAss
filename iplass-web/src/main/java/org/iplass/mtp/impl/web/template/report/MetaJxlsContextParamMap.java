/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.template.report.definition.JxlsContextParamMapDefinition;

public class MetaJxlsContextParamMap  implements MetaData {
	private static final long serialVersionUID = -7418907290538937351L;

	private String key;
	private String mapFrom;
	private boolean convertEntityToMap;

	public MetaJxlsContextParamMap() {
	}
	
	public MetaJxlsContextParamMap(String key, String mapFrom, boolean convertEntityToMap) {
		this.key = key;
		this.mapFrom = mapFrom;
		this.setConvertEntityToMap(convertEntityToMap);
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMapFrom() {
		return mapFrom;
	}

	public void setMapFrom(String mapFrom) {
		this.mapFrom = mapFrom;
	}
	
	public boolean isConvertEntityToMap() {
		return convertEntityToMap;
	}

	public void setConvertEntityToMap(boolean convertEntityToMap) {
		this.convertEntityToMap = convertEntityToMap;
	}
	
	//Definition → Meta
	public void applyConfig(JxlsContextParamMapDefinition definition) {
		key = definition.getKey();
		mapFrom = definition.getMapFrom();
		convertEntityToMap = definition.isConvertEntityToMap();
	}

	//Meta → Definition
	public JxlsContextParamMapDefinition currentConfig() {
		JxlsContextParamMapDefinition definition = new JxlsContextParamMapDefinition();
		definition.setKey(key);
		definition.setMapFrom(mapFrom);
		definition.setConvertEntityToMap(convertEntityToMap);
		
		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
}
