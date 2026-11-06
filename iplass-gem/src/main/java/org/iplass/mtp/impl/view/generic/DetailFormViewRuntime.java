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

package org.iplass.mtp.impl.view.generic;

import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.view.generic.DetailFormViewButtonType;
import org.iplass.mtp.view.generic.OutputType;

public class DetailFormViewRuntime extends FormViewRuntime {
	public static final String ENTITY_BINDING_NAME = "entity";
	public static final String ENTITY_DEFINITION_BINDING_NAME = "entityDefinition";
	public static final String ENTITY_MANAGER_BINDING_NAME = "entityManager";

	public static final String WF_START_PARAM_MAP_BINDING_NAME = "startParamMap";
	public static final String WF_REQUEST_BINDING_NAME = "request";
	public static final String WF_SESSION_BINDING_NAME = "session";
	public static final String WF_USER_BINDING_NAME = "user";

	public static final String REQUEST_BINDING_NAME = "request";
	public static final String SESSION_BINDING_NAME = "session";
	public static final String USER_BINDING_NAME = "user";
	public static final String OUTPUT_TYPE_BINDING_NAME = "outputType";

	private static final String SCRIPT_PREFIX_CUSTOM_COPY_SCRIPT = "DetailFormViewHandler_customCopyScript";
	private static final String SCRIPT_PREFIX_INIT_SCRIPT = "DetailFormViewHandler_initScript";
	private static final String SCRIPT_PREFIX_DISPLAY_TYPE_SCRIPT = "DetailFormViewRuntime_displayTypeScript";

	private Script compiledCustomCopyScript;
	private Script compiledInitScript;
	private ScriptEngine scriptEngine;
	private EntityManager em;
	private final StandardButtonViewParts standardButtonParts;

	public DetailFormViewRuntime(MetaDetailFormView metaData, EntityViewRuntime entityView) {
		super(metaData, entityView);

		em = ManagerLocator.getInstance()
				.getManager(EntityManager.class);

		TenantContext tc = ExecuteContext.getCurrentContext()
				.getTenantContext();
		scriptEngine = tc.getScriptEngine();
		if (metaData.getCustomCopyScript() != null) {
			String scriptName = null;
			if (metaData.getName() == null || metaData.getName()
					.isEmpty()) {
				scriptName = SCRIPT_PREFIX_CUSTOM_COPY_SCRIPT + "_" + entityView.getMetaData()
						.getId() + "_default";
			} else {
				scriptName = SCRIPT_PREFIX_CUSTOM_COPY_SCRIPT + "_" + entityView.getMetaData()
						.getId() + "_" + metaData.getName();
			}

			compiledCustomCopyScript = scriptEngine.createScript(metaData.getCustomCopyScript(), scriptName);
		}
		if (metaData.getInitScript() != null) {
			String scriptName = null;
			if (metaData.getName() == null || metaData.getName()
					.isEmpty()) {
				scriptName = SCRIPT_PREFIX_INIT_SCRIPT + "_" + entityView.getMetaData()
						.getId() + "_default";
			} else {
				scriptName = SCRIPT_PREFIX_INIT_SCRIPT + "_" + entityView.getMetaData()
						.getId() + "_" + metaData.getName();
			}

			compiledInitScript = scriptEngine.createScript(metaData.getInitScript(), scriptName);
		}

		String scriptNamePrefix = SCRIPT_PREFIX_DISPLAY_TYPE_SCRIPT + "_" + entityView.getMetaData()
				.getId() + "_"
				+ (metaData.getName() == null || metaData.getName()
						.isEmpty() ? "default" : metaData.getName());
		standardButtonParts = StandardButtonViewParts.create(metaData, scriptEngine, scriptNamePrefix);
	}

	@Override
	public MetaDetailFormView getMetaData() {
		return (MetaDetailFormView) super.getMetaData();
	}

	public Entity copyEntity(Entity entity) {
		if (compiledCustomCopyScript == null) {
			throw new ApplicationException(resourceString("impl.view.generic.DetailFormViewHandler.noScript"));
		}
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(entity.getDefinitionName());

		UserBinding user = AuthContextHolder.getAuthContext()
				.newUserBinding();

		ScriptContext sc = scriptEngine.newScriptContext();
		sc.setAttribute(ENTITY_BINDING_NAME, entity);
		sc.setAttribute(ENTITY_DEFINITION_BINDING_NAME, eh.getMetaData()
				.currentConfig(ec));
		sc.setAttribute(ENTITY_MANAGER_BINDING_NAME, em);
		sc.setAttribute(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
		sc.setAttribute(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
		sc.setAttribute(USER_BINDING_NAME, user);
		Object val = compiledCustomCopyScript.eval(sc);
		if (val != null && val instanceof Entity) {
			return (Entity) val;
		}
		return null;
	}

	public Entity initEntity(Entity entity, String definitionName) {
		Entity ret = entity;
		if (ret == null) {
			EntityContext ec = EntityContext.getCurrentContext();
			EntityHandler eh = ec.getHandlerByName(definitionName);
			ret = eh.newInstance();
		}
		UserBinding user = AuthContextHolder.getAuthContext()
				.newUserBinding();
		ScriptContext sc = scriptEngine.newScriptContext();
		sc.setAttribute(ENTITY_BINDING_NAME, ret);
		sc.setAttribute(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
		sc.setAttribute(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
		sc.setAttribute(USER_BINDING_NAME, user);
		compiledInitScript.eval(sc);
		return ret;
	}

	/**
	 * 標準ボタンを表示するか判定します。
	 *
	 * @param type ボタン種別
	 * @param outputType 表示タイプ
	 * @param entity 対象のエンティティ（新規登録時はnull）
	 * @return 表示する場合true
	 */
	public boolean isStandardButtonVisible(DetailFormViewButtonType type, OutputType outputType, Entity entity) {
		return standardButtonParts.isStandardButtonVisible(type, outputType, entity);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
