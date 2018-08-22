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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.web.template.report.definition.OutputFileType;

public class ReportingEngineServiceImpl implements ReportingEngineService{

	private ReportingEngine[] reportingEngines;

	@Override
	public void init(Config config) {
		//ReportingEngineのリスト取得

		List<?> reportingEngineList = config.getBeans("repotingEngine");
		if (reportingEngineList != null) {
			reportingEngines = reportingEngineList.toArray(new ReportingEngine[reportingEngineList.size()]);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public ReportingEngine createReportingEngine(String type) throws Exception {
		ReportingEngine repEngine = null;

		//帳票出力エンジン設定
		for(ReportingEngine reportingEngine : reportingEngines){
			if(reportingEngine.getReportingType().getName().equals(type)){
				repEngine = reportingEngine;
				break;
			}
		}

		return repEngine;
	}

	@Override
	public List<OutputFileType> getOutputFileTypeList(String type) {
		List<OutputFileType> outputFileTypeList = new ArrayList<OutputFileType>();
		//帳票出力エンジン設定
		for(ReportingEngine reportingEngine : reportingEngines){
			if(reportingEngine.getReportingType().getName().equals(type)){
				for(String supportFile : reportingEngine.getSupportFiles()){
					outputFileTypeList.add(OutputFileType.convertOutputFileType(supportFile));
				}
				break;
			}
		}

	    return outputFileTypeList;
	}

	@Override
	public List<ReportingType> getReportTypeList() {
		List<ReportingType> reportTypeList = new ArrayList<ReportingType>();
		//帳票出力エンジン設定
		for(ReportingEngine reportingEngine : reportingEngines){
			reportTypeList.add(reportingEngine.getReportingType());
		}

	    return reportTypeList;
	}

}
