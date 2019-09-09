/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.common;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.common.AutocompletionSetting;
import org.iplass.mtp.view.generic.common.WebApiAutocompletionSetting;
import org.iplass.mtp.view.generic.common.WebApiAutocompletionSetting.AutocompletionType;

public class MetaWebApiAutocompletionSetting extends MetaAutocompletionSetting {

	private static final long serialVersionUID = 2616264117606562266L;

	public static MetaAutocompletionSetting createInstance(AutocompletionSetting setting) {
		return new MetaWebApiAutocompletionSetting();
	}

	/** 自動補完タイプ */
	private AutocompletionType autocompletionType;

	/** EQL */
	private String eql;

	/** Groovyscript */
	private String groovyscript;

	/**
	 * @return autocompletionType
	 */
	public AutocompletionType getAutocompletionType() {
		return autocompletionType;
	}

	/**
	 * @param autocompletionType セットする autocompletionType
	 */
	public void setAutocompletionType(AutocompletionType autocompletionType) {
		this.autocompletionType = autocompletionType;
	}

	/**
	 * @return eql
	 */
	public String getEql() {
		return eql;
	}

	/**
	 * @param eql セットする eql
	 */
	public void setEql(String eql) {
		this.eql = eql;
	}

	/**
	 * @return groovyscript
	 */
	public String getGroovyscript() {
		return groovyscript;
	}

	/**
	 * @param groovyscript セットする groovyscript
	 */
	public void setGroovyscript(String groovyscript) {
		this.groovyscript = groovyscript;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(AutocompletionSetting setting, EntityHandler entity, EntityHandler rootEntity) {
		super.fillFrom(setting, entity, rootEntity);

		WebApiAutocompletionSetting _setting = (WebApiAutocompletionSetting) setting;
		autocompletionType = _setting.getAutocompletionType();
		eql = _setting.getEql();
		groovyscript = _setting.getGroovyscript();
	}

	@Override
	public AutocompletionSetting currentConfig(EntityHandler entity, EntityHandler rootEntity) {
		WebApiAutocompletionSetting setting = new WebApiAutocompletionSetting();

		super.fillTo(setting, entity, rootEntity);

		setting.setAutocompletionType(autocompletionType);
		setting.setEql(eql);
		setting.setGroovyscript(groovyscript);
		return setting;
	}

	@Override
	public AutocompletionSettingHandler getHandler(EntityViewHandler entityView) {
		return new WebApiAutocompletionSettingHandler(this, entityView);
	}

	public class WebApiAutocompletionSettingHandler extends AutocompletionSettingHandler {

		public static final String USER_BINDING_NAME = "user";
		public static final String PARAMS_BINDING_NAME = "params";
		public static final String CURRENT_VALUE_BINDING_NAME = "currentValue";

		public final static String AUTOCOMPLETION_EQL = "WebApiAutocompletion_eql";
		public final static String AUTOCOMPLETION_GROOVYSCRIPT = "WebApiAutocompletion_groovyscript";

		private GroovyTemplate eqlTemplate;

		private Script groovyscriptScript;

		public WebApiAutocompletionSettingHandler(MetaAutocompletionSetting metadata, EntityViewHandler entityView) {
			super(metadata, entityView);

			ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			if (AutocompletionType.EQL.equals(getMetaData().getAutocompletionType()) && StringUtil.isNotEmpty(eql)) {
				String templateKey = AUTOCOMPLETION_EQL + "_" + entityView.getMetaData().getId()
						+ GroovyTemplateCompiler.randomName().replace("-", "_");
				eqlTemplate = GroovyTemplateCompiler.compile(eql, templateKey, (GroovyScriptEngine) scriptEngine);
				metadata.setRuntimeKey(templateKey);
			}

			if (AutocompletionType.GROOVYSCRIPT.equals(getMetaData().getAutocompletionType()) && StringUtil.isNotEmpty(groovyscript)) {
				String scriptKey = AUTOCOMPLETION_GROOVYSCRIPT + "_" + entityView.getMetaData().getId()
						+ GroovyTemplateCompiler.randomName().replace("-", "_");
				groovyscriptScript = scriptEngine.createScript(groovyscript, scriptKey);
				metadata.setRuntimeKey(scriptKey);
			}
		}

		@Override
		public Object handle(Map<String, String[]> param, Object currentValue, boolean isReference) {
			if (AutocompletionType.EQL.equals(getMetaData().getAutocompletionType())) {
				return handleEql(param, currentValue, isReference);
			} else if (AutocompletionType.GROOVYSCRIPT.equals(getMetaData().getAutocompletionType())) {
				return handleGroovyScript(param, currentValue);
			}
			return null;
		}

		private Object handleEql(Map<String, String[]> param, Object currentValue, boolean isReference) {
			if (eqlTemplate == null) return null;

			Map<String, Object> bindings = new HashMap<String, Object>();
			bindings.put(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			bindings.put(PARAMS_BINDING_NAME, escapeEql(param));
			bindings.put(CURRENT_VALUE_BINDING_NAME, escapeEql(currentValue));

			StringWriter sw = new StringWriter();
			try {
				eqlTemplate.doTemplate(new GroovyTemplateBinding(sw, bindings));
			} catch (IOException e) {
				//発生しえないが、、
				throw new RuntimeException(e);
			}
			String queryString = sw.toString();
			if (StringUtil.isEmpty(queryString)) return null;

			//先頭、末尾の空白、改行、タブを削除
			queryString = StringUtil.removeLineFeedCode(StringUtil.stripToEmpty(queryString)).replaceAll("\t", "");

			Query query = Query.newQuery(queryString);

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			if (isReference) {
				SearchResult<Entity> result = em.searchEntity(query);
				return result.getList();
			} else {
				//参照じゃない場合は先頭の項目だけを返す
				SearchResult<Object[]> result = em.search(query);
				List<Object> ret = new ArrayList<>();
				for (Object[] data : result.getList()) {
					if (data.length > 0) {
						ret.add(data[0]);
					}
				}
				return ret;
			}
		}

		private Object escapeEql(Object value) {
			if (value instanceof String) {
				return StringUtil.escapeEql((String) value);
			} else if (value instanceof String[]) {
				String[] array = (String[]) value;
				return Arrays.stream(array)
							.map(s -> StringUtil.escapeEql(s))
							.toArray();
			} else if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				return list.stream()
							.map(s -> escapeEql(s))
							.collect(Collectors.toList());
			} else if (value instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) value;
				return map.entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey, entry -> escapeEql(entry.getValue()), (a, b) -> a, LinkedHashMap::new));
			}
			return value;
		}

		private Object handleGroovyScript(Map<String, String[]> param, Object currentValue) {
			if (groovyscriptScript == null) return null;

			UserBinding user = AuthContextHolder.getAuthContext().newUserBinding();

			ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();

			sc.setAttribute(USER_BINDING_NAME, user);
			sc.setAttribute(PARAMS_BINDING_NAME, param);
			sc.setAttribute(CURRENT_VALUE_BINDING_NAME, currentValue);

			Object val = groovyscriptScript.eval(sc);
			return val;
		}

		@Override
		public MetaWebApiAutocompletionSetting getMetaData() {
			return (MetaWebApiAutocompletionSetting) super.getMetaData();
		}
	}
}
