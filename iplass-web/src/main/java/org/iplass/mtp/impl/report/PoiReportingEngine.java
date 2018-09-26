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

package org.iplass.mtp.impl.report;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.template.report.MetaPoiReportOutputLogic.PoiReportOutputLogicRuntime;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.ReportOutputException;
import org.iplass.mtp.web.template.report.definition.PoiReportType;


public class PoiReportingEngine implements ReportingEngine{

	private String[] supportFiles;

	public PoiReportingEngine() {
	}

	@Override
	public ReportingOutputModel createOutputModel(byte[] binary, String type, String extension) throws Exception{
		return new PoiReportingOutputModel(binary, type, extension);
	}

	@Override
	public boolean isSupport(String type) {
		for(String supportFile : this.supportFiles){
			if(supportFile.equals(type)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void exportReport( WebRequestStack context, ReportingOutputModel model ) throws Exception {
		PoiReportingOutputModel poiModel = (PoiReportingOutputModel)model;
		PoiReportOutputLogicRuntime logicRuntime = poiModel.getLogicRuntime();
		if( logicRuntime != null){
			logicRuntime.outputReport(context.getRequestContext(), poiModel.getBook());
		}

		try {
			String password = null;
			if (StringUtil.isNotEmpty(poiModel.getPasswordAttributeName())) {
				password = (String)context.getRequestContext().getAttribute(poiModel.getPasswordAttributeName());
			}
			poiModel.write(context.getResponse().getOutputStream(), password);
		} catch (IOException e) {
			throw new ReportOutputException(e);
		} catch (InvalidFormatException e) {
			throw new ReportOutputException(e);
		} catch (GeneralSecurityException e) {
			throw new ReportOutputException(e);
		}
	}

	public String[] getSupportFiles() {
	    return supportFiles;
	}

	public void setSupportFiles(String[] supportFiles) {
	    this.supportFiles = supportFiles;
	}

	@Override
	public ReportingType getReportingType() {
		ReportingType type = new ReportingType();
		type.setName(PoiReportType.class.getName());
		type.setDisplayName("POI");
		return type;
	}

}
