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

package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;

public class MetaReportParamMap implements MetaData {

	private static final long serialVersionUID = -7418907290538937351L;

	private String name;
	private String mapFrom;
	//Jasper用ParamType指定
	private String paramType;
	//Jxls用Map変換フラグ
	private boolean convertEntityToMap;

	public MetaReportParamMap() {
	}

	public MetaReportParamMap(String name, String mapFrom) {
		this.name = name;
		this.mapFrom = mapFrom;
		this.paramType = "string";
		
	}

	public MetaReportParamMap(String name, String mapFrom, String paramType, boolean convertEntityToMap) {
		this.name = name;
		this.mapFrom = mapFrom;
		this.paramType = paramType;
		this.convertEntityToMap = convertEntityToMap;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapFrom() {
		return mapFrom;
	}

	public void setMapFrom(String mapFrom) {
		this.mapFrom = mapFrom;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	public boolean isConvertEntityToMap() {
		return convertEntityToMap;
	}

	public void setConvertEntityToMap(boolean convertEntityToMap) {
		this.convertEntityToMap = convertEntityToMap;
	}

	//Definition → Meta
	public void applyConfig(ReportParamMapDefinition definition) {
		name = definition.getName();
		mapFrom = definition.getMapFrom();

		if (StringUtil.isEmpty(definition.getParamType())) {
			paramType = "string";
		} else {
			paramType = definition.getParamType();
		}
		
		convertEntityToMap = definition.isConvertEntityToMap();
	}

	//Meta → Definition
	public ReportParamMapDefinition currentConfig() {
		ReportParamMapDefinition definition = new ReportParamMapDefinition();
		definition.setName(name);
		definition.setMapFrom(mapFrom);
		if (StringUtil.isEmpty(paramType)) {
			definition.setParamType("string");
		} else {
			definition.setParamType(paramType);
		}
		definition.setConvertEntityToMap(convertEntityToMap);
		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
