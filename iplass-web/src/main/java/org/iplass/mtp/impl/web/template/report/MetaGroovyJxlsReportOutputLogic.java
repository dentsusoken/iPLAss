package org.iplass.mtp.impl.web.template.report;

import java.io.InputStream;
import java.io.OutputStream;

import org.iplass.mtp.entity.definition.listeners.EventType;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.jxls.common.Context;

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
		
		private Object callScript(InputStream is, OutputStream os, Context context) {
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute("context", context);
			sc.setAttribute("is", is);
			sc.setAttribute("os", os);
			return compiledScript.eval(sc);
		}

		@Override
		public void outputReport(InputStream is, OutputStream os, Context context) {
			callScript(is, os, context);
		}

	}

}
