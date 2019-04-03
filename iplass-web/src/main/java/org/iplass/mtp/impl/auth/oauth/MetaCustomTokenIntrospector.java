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
package org.iplass.mtp.impl.auth.oauth;

import java.util.Map;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.introspectors.JavaClassCustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.introspectors.ScriptingCustomTokenIntrospectorDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.introspectors.MetaJavaClassCustomTokenIntrospector;
import org.iplass.mtp.impl.auth.oauth.introspectors.MetaScriptingCustomTokenIntrospector;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

@XmlSeeAlso({MetaJavaClassCustomTokenIntrospector.class, MetaScriptingCustomTokenIntrospector.class})
public abstract class MetaCustomTokenIntrospector implements MetaData {
	private static final long serialVersionUID = -7429329401774731834L;

	@Override
	public MetaCustomTokenIntrospector copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public abstract void applyConfig(CustomTokenIntrospectorDefinition def);
	public abstract CustomTokenIntrospectorDefinition currentConfig();
	
	public abstract CustomTokenIntrospectorRuntime createRuntime(String metaId, int index);
	
	public abstract class CustomTokenIntrospectorRuntime {
		
		public abstract boolean handle(Map<String, Object> response, RequestContext request, AccessToken token);
	}

	public static MetaCustomTokenIntrospector createInstance(CustomTokenIntrospectorDefinition customTokenIntrospector) {
		if (customTokenIntrospector instanceof JavaClassCustomTokenIntrospectorDefinition) {
			return new MetaJavaClassCustomTokenIntrospector();
		}
		if (customTokenIntrospector instanceof ScriptingCustomTokenIntrospectorDefinition) {
			return new MetaScriptingCustomTokenIntrospector();
		}
		return null;
	}

}
