/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth.definition.introspectors;

import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;

/**
 * <p>
 * GroovyScriptで実装されるCustomTokenIntrospector定義です。
 * </p>
 * <p>
 * GroovyScriptでは、レスポンス返却有無をbooleanで返却するように実装します。次の変数がバインドされます。
 * </p>
 * <ul>
 * <li>request: RequestContextBinding</li>
 * <li>response: 返却する値を表現するMap</li>
 * <li>resourceOwner: Userエンティティ</li>
 * </ul>
 * 
 * 
 * @author K.Higuchi
 *
 */
public class ScriptingCustomTokenIntrospectorDefinition extends CustomTokenIntrospectorDefinition {
	private static final long serialVersionUID = 2347657870175988864L;

	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}


}
