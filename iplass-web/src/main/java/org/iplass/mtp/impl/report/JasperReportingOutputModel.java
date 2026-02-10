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

package org.iplass.mtp.impl.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.iplass.mtp.impl.web.template.report.MetaReportParamMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperReportingOutputModel implements ReportingOutputModel{

	private JasperReport jrMain ;
	private String type;
	private MetaReportParamMap[] maps;
	private String dataSourceAttributeName;
	private String passwordAttributeName;

	private final String JASPER_STR = "jasper";
	private final String JRXML_STR = "jrxml";

	JasperReportingOutputModel(byte[] binary, String type, String extension) throws Exception{
		ByteArrayInputStream bis = null;
		this.type = type;
		try {
			bis = new ByteArrayInputStream(binary);
			//拡張子と出力種類で切り分け
			if(JRXML_STR.equals(extension)){
				jrMain = JasperCompileManager.compileReport(bis);
			}else if(JASPER_STR.equals(extension)){
				jrMain = (JasperReport)JRLoader.loadObject(bis);
			}

		} catch (JRException e) {
			throw e;
		}finally{
			try {
				if(bis != null){
					bis.close();
					bis = null;
				}
			} catch (IOException e) {
				throw e;
			}
		}
	}

	@Override
	public void close() {
	}

	public JasperReport getJrMain() {
	    return jrMain;
	}

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

	public MetaReportParamMap[] getMaps() {
	    return maps;
	}

	public void setMaps(MetaReportParamMap[] maps) {
	    this.maps = maps;
	}

	public String getDataSourceAttributeName() {
		return dataSourceAttributeName;
	}

	public void setDataSourceAttributeName(String dataSourceAttributeName) {
		this.dataSourceAttributeName = dataSourceAttributeName;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

}
