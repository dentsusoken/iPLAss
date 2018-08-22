/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.sms.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.mail.template.MetaPlainTextBodyPart;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.sms.SmsService;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.sms.SmsMail;
import org.iplass.mtp.sms.template.definition.LocalizedSmsMailTemplateDefinition;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinition;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;

/**
 * SMSテンプレートのメタデータ.
 */
@XmlRootElement
public class MetaSmsMailTemplate extends BaseRootMetaData implements DefinableMetaData<SmsMailTemplateDefinition> {

	private static final long serialVersionUID = -1361851708003244753L;

	private MetaPlainTextBodyPart message;
	private List<MetaLocalizedSmsMailTemplate> localizedSmsMailTemplateList;
	private String langOrUserBindingName;

	public MetaPlainTextBodyPart getMessage() {
		return message;
	}

	public void setMessage(MetaPlainTextBodyPart message) {
		this.message = message;
	}

	public List<MetaLocalizedSmsMailTemplate> getLocalizedSmsMailTemplateList() {
		return localizedSmsMailTemplateList;
	}

	public void setLocalizedSmsMailTemplateList(
			List<MetaLocalizedSmsMailTemplate> localizedSmsMailTemplateList) {
		this.localizedSmsMailTemplateList = localizedSmsMailTemplateList;
	}

	public String getLangOrUserBindingName() {
		return langOrUserBindingName;
	}

	public void setLangOrUserBindingName(String langOrUserBindingName) {
		this.langOrUserBindingName = langOrUserBindingName;
	}

	@Override
	public SmsMailTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new SmsMailTemplateRuntime();
	}

	@Override
	public MetaSmsMailTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}
	public void applyConfig(SmsMailTemplateDefinition d) {
		name = d.getName();
		displayName = d.getDisplayName();
		description = d.getDescription();
		if (d.getPlainMessage() != null) {
			message = new MetaPlainTextBodyPart();
			message.applyConfig(d.getPlainMessage());
		} else {
			message = null;
		}

		if(d.getLocalizedSmsMailTemplateList() != null) {
			localizedSmsMailTemplateList = new ArrayList<MetaLocalizedSmsMailTemplate>();
			for(LocalizedSmsMailTemplateDefinition ltd : d.getLocalizedSmsMailTemplateList()) {
				MetaLocalizedSmsMailTemplate mt = new MetaLocalizedSmsMailTemplate();
				mt.applyConfig(ltd);
				localizedSmsMailTemplateList.add(mt);
			}
		} else {
			localizedSmsMailTemplateList = null;
		}
		langOrUserBindingName = d.getLangOrUserBindingName();
	}

	public SmsMailTemplateDefinition currentConfig() {
		SmsMailTemplateDefinition d = new SmsMailTemplateDefinition();
		d.setName(name);
		d.setDisplayName(displayName);
		d.setDescription(description);
		if (message != null) {
			d.setPlainMessage(message.currentConfig());
		}
		if(localizedSmsMailTemplateList != null) {
			for(MetaLocalizedSmsMailTemplate mt : localizedSmsMailTemplateList) {
				d.addLocalizedSmsMailTemplate(mt.currentConfig());
			}
		}
		d.setLangOrUserBindingName(langOrUserBindingName);
		return d;
	}


	public class SmsMailTemplateRuntime extends BaseMetaDataRuntime {

		private GroovyTemplate messageTemplate;
		private Map<String, GroovyTemplate> templateSetMap = new HashMap<String, GroovyTemplate>();

		@Override
		public MetaSmsMailTemplate getMetaData() {
			return MetaSmsMailTemplate.this;
		}

		public SmsMailTemplateRuntime() {
			try {
				if(localizedSmsMailTemplateList != null && localizedSmsMailTemplateList.size() > 0) {
					GroovyTemplate messageTemplate = null;
					ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
					for(MetaLocalizedSmsMailTemplate m : localizedSmsMailTemplateList) {
						String localeName = m.getLocaleName();
						if (m.getMessage() != null && m.getMessage().getContent() != null) {
							messageTemplate = GroovyTemplateCompiler.compile(m.getMessage().getContent(), "SmsMailTemplate_Text" + getName() + "__" + localeName, (GroovyScriptEngine) se);
						}
						templateSetMap.put(localeName, messageTemplate);
					}
				}
				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				if (message != null && message.getContent() != null) {
					this.messageTemplate = GroovyTemplateCompiler.compile(message.getContent(), "MailTemplate_Text" + getName(), (GroovyScriptEngine) se);
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		public SmsMail createMail(Map<String, Object> bindings) {
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

			SmsService service = ServiceRegistry.getRegistry().getService(SmsService.class);
			ExecuteContext ex = ExecuteContext.getCurrentContext();

			GroovyTemplate msgTmpl = messageTemplate;
			if (templateSetMap.get(lang) != null) {
				msgTmpl = templateSetMap.get(lang);
			}
			SmsMail smsMail = service.createMail(ex.getCurrentTenant());

			if (msgTmpl != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw);
				if (bindings != null) {
					for (Map.Entry<String, Object> e: bindings.entrySet()) {
						gtb.setVariable(e.getKey(), e.getValue());
					}
				}
				try {
					msgTmpl.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				smsMail.setMessage(sw.toString());
			}
			return smsMail;
		}
	}
}
