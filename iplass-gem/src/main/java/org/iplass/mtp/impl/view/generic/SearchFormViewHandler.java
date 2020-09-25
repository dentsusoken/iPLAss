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

package org.iplass.mtp.impl.view.generic;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.view.csv.CsvFileNameGroovyTemplateBinding;
import org.iplass.mtp.impl.view.generic.element.section.MetaSearchConditionSection;
import org.iplass.mtp.impl.view.generic.element.section.MetaSection;
import org.iplass.mtp.impl.view.generic.element.section.SectionHandler;
import org.iplass.mtp.util.StringUtil;

public class SearchFormViewHandler extends FormViewHandler {
	public static final String INIT_CONDTION_MAP_BINDING_NAME = "initCondMap";
	public static final String REQUEST_BINDING_NAME = "request";
	public static final String SESSION_BINDING_NAME = "session";
	public static final String USER_BINDING_NAME = "user";

	private static final String SCRIPT_PREFIX = "SearchFormViewHandler_defaultPropertyConditionScript";

	private List<SectionHandler> sections;

	private Script compiledDefaultPropertyConditionScript;

	private GroovyTemplate compiledCsvFileNameScript;

	public SearchFormViewHandler(MetaFormView metaData, EntityViewRuntime entityView) {
		super(metaData, entityView);

		MetaSearchFormView form = (MetaSearchFormView) metaData;
		sections = new ArrayList<SectionHandler>();
		if (form.getTopSection1() != null) {
			sections.add(form.getTopSection1().createRuntime(entityView));
		}
		if (form.getTopSection2() != null) {
			sections.add(form.getTopSection2().createRuntime(entityView));
		}
		if (form.getCenterSection() != null) {
			sections.add(form.getCenterSection().createRuntime(entityView));
		}
		if (form.getBottomSection() != null) {
			sections.add(form.getBottomSection().createRuntime(entityView));
		}

		if (form.getSections() != null) {
			MetaSearchConditionSection condSection = null;
			for (MetaSection section : form.getSections()) {
				if (section instanceof MetaSearchConditionSection) {
					condSection = (MetaSearchConditionSection)section;
					break;
				}
			}
			if (condSection != null) {
				if (condSection.getDefaultPropertyConditionScript() != null) {
					ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
					String scriptName = null;
					if (metaData.getName() == null || metaData.getName().isEmpty()) {
						scriptName = SCRIPT_PREFIX + "_" + entityView.getMetaData().getId() + "_default";
					} else {
						scriptName = SCRIPT_PREFIX + "_" + entityView.getMetaData().getId() + "_" + metaData.getName();
					}
					compiledDefaultPropertyConditionScript = scriptEngine.createScript(condSection.getDefaultPropertyConditionScript(), scriptName);
				}
				if (condSection.getCsvdownloadFileNameFormat() != null) {
					ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
					String scriptName = null;
					if (metaData.getName() == null || metaData.getName().isEmpty()) {
						scriptName = "_" + entityView.getMetaData().getId() + "_SearchFormView_csvFileNameScript_default";
					} else {
						scriptName = "_" + entityView.getMetaData().getId() + "_SearchFormView_csvFileNameScript_" + metaData.getName();
					}
					compiledCsvFileNameScript = GroovyTemplateCompiler.compile(
							condSection.getCsvdownloadFileNameFormat(),
							scriptName, (GroovyScriptEngine) scriptEngine);
				}
			}
		}
	}

	public Map<String, Object> applyDefaultPropertyCondition(Map<String, Object> defaultCondMap) {
		if (compiledDefaultPropertyConditionScript == null) {
			return defaultCondMap;
		}
		Map<String, Object> dynamicCondMap = new HashMap<>();
		if (defaultCondMap != null) {
			dynamicCondMap.putAll(defaultCondMap);
		}

		UserBinding user = AuthContextHolder.getAuthContext().newUserBinding();

		ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		ScriptContext sc = scriptEngine.newScriptContext();
		sc.setAttribute(INIT_CONDTION_MAP_BINDING_NAME, dynamicCondMap);
		sc.setAttribute(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
		sc.setAttribute(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
		sc.setAttribute(USER_BINDING_NAME, user);

		compiledDefaultPropertyConditionScript.eval(sc);
		return valueToArray(dynamicCondMap);
	}

	public String getCsvDownloadFileName(String defaultName, Map<String, Object> csvVariableMap) {
		if (compiledCsvFileNameScript == null) {
			return defaultName;
		} else {
			StringWriter sw = new StringWriter();
			try {
				compiledCsvFileNameScript.doTemplate(new CsvFileNameGroovyTemplateBinding(sw, defaultName, csvVariableMap));
			} catch (IOException e) {
				//発生しえないが、、
				throw new RuntimeException(e);
			}
			String fileName = sw.toString();
			if (StringUtil.isNotEmpty(fileName)) {
				//空白、改行、タブを削除
				fileName = StringUtil.removeLineFeedCode(StringUtil.deleteWhitespace(fileName)).replaceAll("\t", "");
			}

			return fileName;
		}

	}

	private Map<String, Object> valueToArray(Map<String, Object> dynamicCondMap) {
		if (dynamicCondMap == null) {
			return null;
		}
		for (Map.Entry<String, Object> e: dynamicCondMap.entrySet()) {
			dynamicCondMap.put(e.getKey(), toArray(e.getValue()));
		}
		return dynamicCondMap;
	}

	private Object toArray(Object value) {
		if (value != null && !value.getClass().isArray()) {
			Object[] array = (Object[])Array.newInstance(value.getClass(), 1);
			array[0] = value;
			return array;
		}
		return value;
	}

}
