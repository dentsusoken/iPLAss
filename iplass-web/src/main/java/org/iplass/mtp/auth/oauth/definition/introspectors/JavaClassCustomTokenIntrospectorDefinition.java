/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.auth.oauth.CustomTokenIntrospector;
import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;

/**
 * Javaクラスで実装されるCustomTokenIntrospector定義です。
 * classNameに CustomTokenIntrospectorの実装クラスを指定します。
 * 
 * @see CustomTokenIntrospector
 * @author K.Higuchi
 *
 */
public class JavaClassCustomTokenIntrospectorDefinition extends CustomTokenIntrospectorDefinition {
	private static final long serialVersionUID = 7805854304702805608L;

	private String className;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
