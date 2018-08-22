/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.pushnotification.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScript;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.pushnotification.NotificationPayload;
import org.iplass.mtp.pushnotification.PushNotification;
import org.iplass.mtp.pushnotification.template.definition.LocalizedNotificationDefinition;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinition;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;

/**
 * Push通知テンプレートのメタデータ.
 */
@XmlRootElement
public class MetaPushNotificationTemplate extends BaseRootMetaData implements DefinableMetaData<PushNotificationTemplateDefinition> {
	private static final long serialVersionUID = 7600704862048295319L;

	public static final String PUSH_NOTIFICATION_BINDING_NAME = "pn";

	private String title;
	private String body;
	private String icon;
	private List<MetaLocalizedNotification> localizedNotificationList;

	private String configScript;
	private String langOrUserBindingName;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<MetaLocalizedNotification> getLocalizedNotificationList() {
		return localizedNotificationList;
	}

	public void setLocalizedNotificationList(
			List<MetaLocalizedNotification> localizedNotificationList) {
		this.localizedNotificationList = localizedNotificationList;
	}

	public String getConfigScript() {
		return configScript;
	}

	public void setConfigScript(String configScript) {
		this.configScript = configScript;
	}

	public String getLangOrUserBindingName() {
		return langOrUserBindingName;
	}

	public void setLangOrUserBindingName(String langOrUserBindingName) {
		this.langOrUserBindingName = langOrUserBindingName;
	}

	@Override
	public PushNotificationTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new PushNotificationTemplateRuntime();
	}

	@Override
	public MetaPushNotificationTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(PushNotificationTemplateDefinition d) {
		name = d.getName();
		displayName = d.getDisplayName();
		description = d.getDescription();
		title = d.getTitle();
		body = d.getBody();
		icon = d.getIcon();
		if (d.getLocalizedNotificationList() != null) {
			localizedNotificationList = new ArrayList<>();
			for (LocalizedNotificationDefinition lnd: d.getLocalizedNotificationList()) {
				MetaLocalizedNotification mn = new MetaLocalizedNotification();
				mn.setLocaleName(lnd.getLocaleName());
				mn.setTitle(lnd.getTitle());
				mn.setBody(lnd.getBody());
				localizedNotificationList.add(mn);
			}
		} else {
			localizedNotificationList = null;
		}

		configScript = d.getConfigScript();
		langOrUserBindingName = d.getLangOrUserBindingName();
	}

	public PushNotificationTemplateDefinition currentConfig() {
		PushNotificationTemplateDefinition d = new PushNotificationTemplateDefinition();
		d.setName(name);
		d.setDisplayName(displayName);
		d.setDescription(description);
		d.setTitle(title);
		d.setBody(body);
		d.setIcon(icon);
		if (localizedNotificationList != null) {
			for (MetaLocalizedNotification mn : localizedNotificationList) {
				LocalizedNotificationDefinition lnd = new LocalizedNotificationDefinition();
				lnd.setLocaleName(mn.getLocaleName());
				lnd.setTitle(mn.getTitle());
				lnd.setBody(mn.getBody());
				d.addLocalizedNotification(lnd);
			}
		}
		d.setConfigScript(configScript);
		d.setLangOrUserBindingName(langOrUserBindingName);
		return d;
	}

	public class PushNotificationTemplateRuntime extends BaseMetaDataRuntime {

		private GroovyTemplate titleTemplate;
		private GroovyTemplate bodyTemplate;
		private GroovyTemplate iconTemplate;
		private Map<String, TemplateSet> templateSetMap = new HashMap<String, TemplateSet>();

		private GroovyScript configScriptScript;

		private class TemplateSet {
			private GroovyTemplate titleTemplate;
			private GroovyTemplate bodyTemplate;
		}

		public PushNotificationTemplateRuntime() {
			try {
				GroovyScriptEngine se = (GroovyScriptEngine) ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				if (title != null) {
					titleTemplate = GroovyTemplateCompiler.compile(title, "PNTemplate_Title" + getName(), se);
				}
				if (body != null) {
					bodyTemplate = GroovyTemplateCompiler.compile(body, "PNTemplate_Body" + getName(), se);
				}
				if (icon != null) {
					iconTemplate = GroovyTemplateCompiler.compile(icon, "PNTemplate_Icon" + getName(), se);
				}

				if (localizedNotificationList != null && localizedNotificationList.size() > 0) {
					for (MetaLocalizedNotification m: localizedNotificationList) {
						String localeName = m.getLocaleName();
						TemplateSet templateSet = new TemplateSet();

						if (m.getTitle() != null) {
							templateSet.titleTemplate = GroovyTemplateCompiler.compile(m.getTitle(), "PNTemplate_Title" + getName() +"__" + localeName, se);
						}
						if (m.getBody() != null) {
							templateSet.bodyTemplate = GroovyTemplateCompiler.compile(m.getBody(), "PNTemplate_Body" + getName() +"__" + localeName, se);
						}

						templateSetMap.put(localeName, templateSet);
					}
				}

				if (configScript != null) {
					configScriptScript = (GroovyScript) se.createScript(configScript, "PNTemplate_Config" + getName());
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaPushNotificationTemplate getMetaData() {
			return MetaPushNotificationTemplate.this;
		}

		public PushNotification createPushNotification(Map<String, Object> bindings) {
			checkState();

			String lang = ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).getLocale();
			if (StringUtil.isNotEmpty(langOrUserBindingName)) {
				if (bindings.get(langOrUserBindingName) != null) {

					if (bindings.get(langOrUserBindingName) instanceof String) {
						lang = (String) bindings.get(langOrUserBindingName);
					} else if (bindings.get(langOrUserBindingName) instanceof User) {
						User user = (User)bindings.get(langOrUserBindingName);
						String userLang = user.getLanguage();
						if (StringUtil.isNotEmpty(userLang)) {
							lang = userLang;
						}
					}
				}
			}

			GroovyTemplate _titleTemplate = titleTemplate;
			GroovyTemplate _bodyTemplate = bodyTemplate;
			TemplateSet ts = templateSetMap.get(lang);
			if (ts != null) {
				_titleTemplate = ts.titleTemplate;
				_bodyTemplate = ts.bodyTemplate;
			}

			//set NotificationPayload
			PushNotification pn = new PushNotification();
			NotificationPayload np = new NotificationPayload();
			pn.setNotification(np);
			if (_titleTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw, bindings);
				try {
					_titleTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				np.put(NotificationPayload.TITLE, sw.toString());
			}
			if (_bodyTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw, bindings);
				try {
					_bodyTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				np.put(NotificationPayload.BODY, sw.toString());
			}
			if (iconTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw, bindings);
				try {
					iconTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				np.put(NotificationPayload.ICON, sw.toString());
			}

			//config
			if (configScriptScript != null) {

				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				ScriptEngine ss = tc.getScriptEngine();

				ScriptContext sc = ss.newScriptContext();
				if (bindings != null) {
					for (Map.Entry<String, Object> e: bindings.entrySet()) {
						sc.setAttribute(e.getKey(), e.getValue());
					}
				}
				sc.setAttribute(PUSH_NOTIFICATION_BINDING_NAME, pn);
				configScriptScript.eval(sc);
			}

			return pn;
		}
	}
}
