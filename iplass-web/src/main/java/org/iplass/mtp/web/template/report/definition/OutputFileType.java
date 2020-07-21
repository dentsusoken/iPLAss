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

public enum OutputFileType {
	PDF("PDF(Jasper)", new String[]{"jasper", "jrxml"}),
	XLS("XLS(Jasper)", new String[]{"jasper", "jrxml"}),
	XLSX("XLSX(Jasper)", new String[]{"jasper", "jrxml"}),
	XLS_POI("XLS(POI)", new String[]{"xls"}),
	XLSX_POI("XLSX(POI)", new String[]{"xlsx"}),
	XLSX_SXSSF_POI("XLSX(POI Streaming)", new String[]{"xlsx"}),
	XLS_JXLS("XLS(JXLS)", new String[]{"jxls", "xls"}),
	XLSX_JXLS("XLSX(JXLS)", new String[]{"jxls", "xlsx"})
	;

	private String displayName;
	private String[] canExtensions;

	private OutputFileType(String displayName, String[] canExtensions) {
		this.displayName = displayName;
		this.canExtensions = canExtensions;
	}

	public String displayName() {
		return displayName;
	}

	public String[] canExtenssions() {
		return canExtensions;
	}

	public static OutputFileType convertOutputFileType(String valueAsString) {
		for (OutputFileType type : OutputFileType.values()) {
			if(type.name().equals(valueAsString)){
				return type;
			}
		}
		return null;
	}
}
