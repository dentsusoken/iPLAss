/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate;

import java.io.IOException;
import java.io.StringWriter;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.script.GroovyScriptContext;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.GroovyScriptService;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptService;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AccountHandleを元にUserEntityを作成するEntityResolver。
 * マッピング定義にしたがい、AccountHandleの属性をUserEntityにマップする。
 * デフォルト設定では、AccountHandleのCredentialのidをUser.accountIdおよびnameに、
 * unmodifiableUniqueKeyをUser.oidにセットする。
 * 
 * @author K.Higuchi
 *
 */
public class AccountBaseUserEntityResolver implements UserEntityResolver {
	public static final String UNMODIFIABLE_UNIQUE_KEY_NAME = "unmodifiableUniqueKey";
	public static final String ID_NAME = "id";
	
	private static Logger logger = LoggerFactory.getLogger(AccountBaseUserEntityResolver.class);
	
	public static class AttributeMapping {

		private String propertyName;
		private String accountAttributeName;
		private Object defaultValue;
		private Class<?> type;
		private String valueConvertScript;
		
		private GroovyTemplate accountAttributeNameTemplate;
		private Script valueConverterScript;
		
		public AttributeMapping() {
		}
		
		public AttributeMapping(String propertyName) {
			this.propertyName = propertyName;
		}
		
		public AttributeMapping(String propertyName, String accountAttributeName) {
			this.propertyName = propertyName;
			this.accountAttributeName = accountAttributeName;
		}
		
		public String getPropertyName() {
			return propertyName;
		}
		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}
		public String getAccountAttributeName() {
			return accountAttributeName;
		}
		public void setAccountAttributeName(String accountAttributeName) {
			this.accountAttributeName = accountAttributeName;
		}
		public Object getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(Object defaultValue) {
			this.defaultValue = defaultValue;
		}
		public Class<?> getType() {
			return type;
		}
		public void setType(Class<?> type) {
			this.type = type;
		}
		public String getValueConvertScript() {
			return valueConvertScript;
		}

		public void setValueConvertScript(String valueConvertScript) {
			this.valueConvertScript = valueConvertScript;
		}

	}
	
	private AttributeMapping[] attributeMapping;
	
	private ScriptEngine se;

	public AttributeMapping[] getAttributeMapping() {
		return attributeMapping;
	}

	public void setAttributeMapping(AttributeMapping[] attributeMapping) {
		this.attributeMapping = attributeMapping;
	}

	@Override
	public void inited(AuthService service, AuthenticationProvider provider) {
		if (attributeMapping == null) {
			attributeMapping = new AttributeMapping[3];
			attributeMapping[0] = new AttributeMapping(User.OID, UNMODIFIABLE_UNIQUE_KEY_NAME);
			attributeMapping[1] = new AttributeMapping(User.ACCOUNT_ID, ID_NAME);
			attributeMapping[2] = new AttributeMapping(User.NAME, ID_NAME);
		}
		
		for (AttributeMapping am: attributeMapping) {
			if (am.accountAttributeName != null) {
				if (am.accountAttributeName.contains("${")) {
					if (se == null) {
						se = ((GroovyScriptService) ServiceRegistry.getRegistry().getService(ScriptService.class)).createScriptEngine(true);
					}
					am.accountAttributeNameTemplate = GroovyTemplateCompiler.compile(am.accountAttributeName, "_" + provider.getProviderName() + "_ABUER_AN_" + am.propertyName, (GroovyScriptEngine) se);
				}
			}
			if (am.valueConvertScript != null) {
				if (se == null) {
					se = ((GroovyScriptService) ServiceRegistry.getRegistry().getService(ScriptService.class)).createScriptEngine(true);
				}
				am.valueConverterScript = se.createScript(am.valueConvertScript, "_" + provider.getProviderName() + "_ABUER_VC_" + am.propertyName);
			}
		}
		
	}

	@Override
	public User searchUser(AccountHandle account) {
		User user = new User();
		for (AttributeMapping am: attributeMapping) {
			Object val = null;
			if (am.getAccountAttributeName() != null) {
				switch (am.getAccountAttributeName()) {
				case ID_NAME:
					val = account.getCredential().getId();
					break;
				case UNMODIFIABLE_UNIQUE_KEY_NAME:
					val = account.getUnmodifiableUniqueKey();
					break;
				default:
					if (am.accountAttributeNameTemplate == null) {
						val = account.getAttributeMap().get(am.getAccountAttributeName());
					} else {
						StringWriter sb = new StringWriter();
						try {
							am.accountAttributeNameTemplate.doTemplate(new GroovyTemplateBinding(sb, account.getAttributeMap()));
						} catch(IOException e) {
							//発生しない
							logger.error(e.getMessage(), e);
						}
						val = sb.toString();
					}
					break;
				}
			}
			
			if (val == null && am.getDefaultValue() != null) {
				val = am.getDefaultValue();
			}
			
			if (val != null && am.valueConverterScript != null) {
				//TODO ...
				GroovyScriptContext ctx = new GroovyScriptContext();
				ctx.setAttribute("value", val);
				val = am.valueConverterScript.eval(ctx);
			}
			
			if (val != null && am.getType() != null) {
				if (am.getType() != val.getClass()) {
					val = ConvertUtil.convert(am.getType(), val);
				}
			}
			user.setValue(am.getPropertyName(), val);
		}
		return user;
	}

	@Override
	public String getUnmodifiableUniqueKeyProperty() {
		return Entity.OID;
	}

}
