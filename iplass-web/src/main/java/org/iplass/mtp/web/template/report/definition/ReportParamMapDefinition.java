/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.web.template.report.definition;

import java.io.Serializable;

public class ReportParamMapDefinition implements Serializable {

	private static final long serialVersionUID = 8121021196519567703L;

	private String name;
	private String mapFrom;
	//Jasper用ParamType指定
	private String paramType;
	//Jxls用Map変換フラグ
	private boolean convertEntityToMap;

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return mapFrom
	 */
	public String getMapFrom() {
		return mapFrom;
	}

	/**
	 * @param mapFrom
	 *            セットする mapFrom
	 */
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
}
