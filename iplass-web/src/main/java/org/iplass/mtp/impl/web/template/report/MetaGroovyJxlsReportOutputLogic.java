/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.OutputStream;
import java.util.Map;

import org.codehaus.groovy.runtime.MethodClosure;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.ReportOutputLogicDefinition;
import org.jxls.builder.JxlsTemplateFillerBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.transform.poi.PoiTransformer;

/**
 * Jxls 帳票出力ロジック（Groovy）メターデータ
 */
public class MetaGroovyJxlsReportOutputLogic extends MetaJxlsReportOutputLogic {

	private static final long serialVersionUID = -2510252715491801950L;

	/** groovy script */
	private String script;

	/**
	 * groovy script を取得する
	 * @return groovy script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * groovy script を設定する
	 * @param script groovy script
	 */
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

	/**
	 * Jxls 帳票出力ロジック（Groovy）ランタイム
	 */
	public class GroovyJxlsReportOutputLogicRuntime extends JxlsReportOutputLogicRuntime {

		private static final String SCRIPT_PREFIX = "GroovyJxlsReportOutputLogicRuntime_script";

		private Script compiledScript;
		private ScriptEngine scriptEngine;

		/**
		 * コンストラクタ
		 * @param reportType MetaJxlsReportType
		 */
		public GroovyJxlsReportOutputLogicRuntime(MetaReportType reportType) {

			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptEngine = tc.getScriptEngine();

			if (script != null) {
				String scriptName = null;
				MetaJxlsReportType jxlsTemp = (MetaJxlsReportType) reportType;
				if (jxlsTemp.getReportOutputLogic() != null) {
					scriptName = SCRIPT_PREFIX + "_" + reportType.getOutputFileType() + "_" + GroovyTemplateCompiler.randomName();
				}

				compiledScript = scriptEngine.createScript(script, scriptName);
			}
		}

		private Object callScript(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out) {
			ScriptContext sc = scriptEngine.newScriptContext();
			// AdminConsole の Groovy Script 実装部分で Hint にバインドパラメータが記載されている為、変更した場合は修正が必要。
			// locale_xx.js の LocaleInfo.ui_metadata_template_report_JxlsReportOutLogicListGrid_scriptHint
			sc.setAttribute("builder", builder);
			sc.setAttribute("reportData", reportData);
			sc.setAttribute("out", out);

			// テンプレート出力のヘルパーメソッドのバインド
			// deprecated メソッド
			sc.setAttribute("processTemplateAtCell", new MethodClosure(this, "processTemplateAtCell"));
			sc.setAttribute("processGridTemplate", new MethodClosure(this, "processGridTemplate"));
			sc.setAttribute("processGridTemplateAtCell", new MethodClosure(this, "processGridTemplateAtCell"));

			return compiledScript.eval(sc);
		}

		@Override
		public void outputReport(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out) {
			callScript(builder, reportData, out);
		}

		/**
		 * @see org.iplass.mtp.web.template.report.JxlsReportOutputLogic#processTemplateAtCell(JxlsTemplateFillerBuilder, Map, OutputStream, String)
		 */
		@SuppressWarnings("javadoc")
		@Deprecated
		public void processTemplateAtCell(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out, String targetCell) {
			final var cellRef = new CellRef(targetCell);
			// シート名の変更
			builder
					.withPreWriteAction((transformer, ctx) -> {
						if (transformer instanceof PoiTransformer poiTransformer) {
							var book = poiTransformer.getWorkbook();

							if (cellRef.getSheetName() != null && cellRef.getSheetName().length() > 0) {
								book.setSheetName(0, cellRef.getSheetName());
							}
						}
					})
					.build()
					.fill(reportData, () -> out);
		}

		/**
		 * @see org.iplass.mtp.web.template.report.JxlsReportOutputLogic#processGridTemplate(JxlsTemplateFillerBuilder, Map, OutputStream, String)
		 */
		@SuppressWarnings("javadoc")
		@Deprecated
		public void processGridTemplate(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out, String objectProps) {
			builder
					// grid コマンドの props に設定
					.withAreaBuilder((transformer, clearTemplateCells) -> {
						var areaList = new XlsCommentAreaBuilder().build(transformer, clearTemplateCells);
						for (var area : areaList) {
							for (var commandData : area.getCommandDataList()) {
								if (commandData.getCommand() instanceof GridCommand gridCommand) {
									gridCommand.setProps(objectProps);
								}
							}
						}
						return areaList;
					})
					.build()
					.fill(reportData, () -> out);
		}

		/**
		 * @see org.iplass.mtp.web.template.report.JxlsReportOutputLogic#processGridTemplateAtCell(JxlsTemplateFillerBuilder, Map, OutputStream, String, String)
		 */
		@SuppressWarnings("javadoc")
		@Deprecated
		public void processGridTemplateAtCell(JxlsTemplateFillerBuilder<?> builder, Map<String, Object> reportData, OutputStream out, String objectProps,
				String targetCell) {
			final var cellRef = new CellRef(targetCell);
			builder
					// grid コマンドの props に設定
					.withAreaBuilder((transformer, clearTemplateCells) -> {
						var areaList = new XlsCommentAreaBuilder().build(transformer, clearTemplateCells);

						GridCommand gridCommand = (GridCommand) areaList.get(0).getCommandDataList().get(0).getCommand();
						gridCommand.setProps(objectProps);

						return areaList;
					})
					// シート名の変更
					.withPreWriteAction((transformer, ctx) -> {
						if (transformer instanceof PoiTransformer poiTransformer) {
							var book = poiTransformer.getWorkbook();

							if (cellRef.getSheetName() != null && cellRef.getSheetName().length() > 0) {
								book.setSheetName(0, cellRef.getSheetName());
							}
						}
					})
					.build()
					.fill(reportData, () -> out);
		}
	}
}
