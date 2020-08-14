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

import org.iplass.mtp.entity.definition.listeners.EventType;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.web.template.report.MtpJxlsHelper;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;

public class MetaGroovyJxlsReportOutputLogic extends MetaJxlsReportOutputLogic {

	private static final long serialVersionUID = -2510252715491801950L;
	
	private String script;
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public MetaGroovyJxlsReportOutputLogic copy() {
		MetaGroovyJxlsReportOutputLogic copy = new MetaGroovyJxlsReportOutputLogic();
		copy.script = script;
		return copy;
	}

	@Override
	public void applyConfig(ReportOutputLogicDefinition def) {
		GroovyReportOutputLogicDefinition d = (GroovyReportOutputLogicDefinition) def;
		script = d.getScript();
	}

	@Override
	public ReportOutputLogicDefinition currentConfig() {
		GroovyReportOutputLogicDefinition d = new GroovyReportOutputLogicDefinition();
		fillTo(d);
		d.setScript(script);
		return d;
	}

	@Override
	public JxlsReportOutputLogicRuntime createRuntime(MetaReportType reportType) {
		return new GroovyJxlsReportOutputLogicRuntime(reportType);
	}
	
	public class GroovyJxlsReportOutputLogicRuntime extends JxlsReportOutputLogicRuntime {

		private static final String SCRIPT_PREFIX = "GroovyJxlsReportOutputLogicRuntime_script";

		private Script compiledScript;
		private ScriptEngine scriptEngine;

		public GroovyJxlsReportOutputLogicRuntime(MetaReportType reportType) {

			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			if (script != null) {
				String scriptWithImport = "import " + EventType.class.getName() + ";\n" + script;
				String scriptName = null;
				MetaJxlsReportType jxlsTemp = (MetaJxlsReportType)reportType;
				if (jxlsTemp.getReportOutputLogic() != null) {
					scriptName = SCRIPT_PREFIX + "_" + reportType.getOutputFileType() + "_" + GroovyTemplateCompiler.randomName();
				}

				compiledScript = scriptEngine.createScript(scriptWithImport, scriptName);
			}
		}
		
		private Object callScript(Transformer transformer, Context context, MtpJxlsHelper jxlsHelper) {
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute("jxlsHelper", jxlsHelper);
			sc.setAttribute("transformer", transformer);
			sc.setAttribute("context", context);
			return compiledScript.eval(sc);
		}

		@Override
		public void outputReport(Transformer transformer, Context context, MtpJxlsHelper jxlsHelper) {
			callScript(transformer, context, jxlsHelper);
		}


	}

}
