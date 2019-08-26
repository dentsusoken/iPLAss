/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.element;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.OutputType;

/**
 * 要素のランタイム
 * @author lis3wg
 */
public class ElementHandler {

	private static final String REQUEST_BINDING_NAME = "request";
	private static final String SESSION_BINDING_NAME = "session";
	private static final String USER_BINDING_NAME = "user";
	private static final String OUTPUT_TYPE_BINDING_NAME = "outputType";
	private static final String ENTITY_BINDING_NAME = "entity";

	private final static String ELEMENT_GROOVYSCRIPT_PREFIX = "Element";

	/** メタデータ */
	private MetaElement metadata;

	private Script dispGroovyScript;

	/**
	 * コンストラクタ
	 * @param metadata メタデータ
	 * @param entityView 画面定義
	 */
	public ElementHandler(MetaElement metadata, EntityViewHandler entityView) {
		this.metadata = metadata;

		//ElementのRuntimeKEY
		String elementRuntimeId = ELEMENT_GROOVYSCRIPT_PREFIX
				+ "_" + entityView.getMetaData().getId()
				+ "_" + GroovyTemplateCompiler.randomName().replace("-", "_");
		metadata.setElementRuntimeId(elementRuntimeId);

		if (StringUtil.isNotEmpty(metadata.getDisplayScript())) {
			ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			dispGroovyScript = scriptEngine.createScript(metadata.getDisplayScript(), elementRuntimeId);
		}

		entityView.addElementHandler(this);
	}

	/**
	 * メタデータを取得します。
	 * @return メタデータ
	 */
	public MetaElement getMetaData() {
		return metadata;
	}

	/**
	 * エレメントが表示可能かを返します。
	 *
	 * @param outputType 表示タイプ
	 * @param entity 表示対象のエンティティ
	 * @return true 表示可能
	 */
	public boolean isDisplay(OutputType outputType, Entity entity) {
		if (!metadata.isDispFlag()) {
			return false;
		}

		if (dispGroovyScript != null) {
			UserBinding user = AuthContextHolder.getAuthContext().newUserBinding();

			ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();

			sc.setAttribute(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
			sc.setAttribute(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
			sc.setAttribute(USER_BINDING_NAME, user);
			sc.setAttribute(OUTPUT_TYPE_BINDING_NAME, outputType);
			sc.setAttribute(ENTITY_BINDING_NAME, entity);

			boolean val = (Boolean)dispGroovyScript.eval(sc);
			return val;
		}

		return true;
	}
}
