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
package org.iplass.mtp.impl.auth.oauth.introspectors;

import java.util.Map;

import org.iplass.mtp.auth.oauth.CustomTokenIntrospector;
import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.introspectors.JavaClassCustomTokenIntrospectorDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.MetaCustomTokenIntrospector;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;

public class MetaJavaClassCustomTokenIntrospector extends MetaCustomTokenIntrospector {
	private static final long serialVersionUID = 3535843225458827L;

	private String className;
	
	public MetaJavaClassCustomTokenIntrospector() {
	}
	
	public MetaJavaClassCustomTokenIntrospector(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void applyConfig(CustomTokenIntrospectorDefinition def) {
		className = ((JavaClassCustomTokenIntrospectorDefinition) def).getClassName();
	}

	@Override
	public CustomTokenIntrospectorDefinition currentConfig() {
		JavaClassCustomTokenIntrospectorDefinition def = new JavaClassCustomTokenIntrospectorDefinition();
		def.setClassName(className);
		return def;
	}

	@Override
	public CustomTokenIntrospectorRuntime createRuntime(String metaId, int index) {
		return new JavaClassCustomTokenIntrospectorRuntime();
	}
	
	public class JavaClassCustomTokenIntrospectorRuntime extends CustomTokenIntrospectorRuntime {
		
		private CustomTokenIntrospector customTokenIntrospector;
		
		private JavaClassCustomTokenIntrospectorRuntime() {
			try {
				customTokenIntrospector = (CustomTokenIntrospector) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new MetaDataRuntimeException("can not instantiate " + className, e);
			} catch (IllegalAccessException e) {
				throw new MetaDataRuntimeException("can not instantiate " + className, e);
			} catch (ClassNotFoundException e) {
				throw new MetaDataRuntimeException("class not found:" + className, e);
			}
		}

		@Override
		public boolean handle(Map<String, Object> response, RequestContext request, AccessToken token) {
			return customTokenIntrospector.handle(response, request, token.getUser());
		}

		@Override
		public MetaCustomTokenIntrospector getMetaData() {
			return MetaJavaClassCustomTokenIntrospector.this;
		}
		
	}

}
