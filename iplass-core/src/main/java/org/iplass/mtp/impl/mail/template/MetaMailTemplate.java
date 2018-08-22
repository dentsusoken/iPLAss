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

package org.iplass.mtp.impl.mail.template;

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
import org.iplass.mtp.impl.mail.MailService;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.mail.HtmlMessage;
import org.iplass.mtp.mail.Mail;
import org.iplass.mtp.mail.template.definition.LocalizedMailTemplateDefinition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;

/**
 * MailTemplateのメタ。
 * メールの文面をテンプレートとして登録しておき、適宜利用する。
 * テンプレートはGroovyTemplateなので、バインド可能。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class MetaMailTemplate extends BaseRootMetaData implements DefinableMetaData<MailTemplateDefinition> {
	private static final long serialVersionUID = -6379003462059117529L;

	private String charset;

	private String subject;
	private MetaPlainTextBodyPart message;
	private MetaHtmlBodyPart htmlMessage;
	private List<MetaLocalizedMailTemplate> localizedMailTemplateList;

	private String from;
	private String replyTo;
	private String returnPath;

	private String langOrUserBindingName;

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public String getReturnPath() {
		return returnPath;
	}
	public void setReturnPath(String returnPath) {
		this.returnPath = returnPath;
	}

	public List<MetaLocalizedMailTemplate> getLocalizedMailTemplateList() {
		return localizedMailTemplateList;
	}
	public void setLocalizedMailTemplateList(
			List<MetaLocalizedMailTemplate> localizedMailTemplateList) {
		this.localizedMailTemplateList = localizedMailTemplateList;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public MetaPlainTextBodyPart getMessage() {
		return message;
	}
	public void setMessage(MetaPlainTextBodyPart message) {
		this.message = message;
	}
	public MetaHtmlBodyPart getHtmlMessage() {
		return htmlMessage;
	}
	public void setHtmlMessage(MetaHtmlBodyPart htmlMessage) {
		this.htmlMessage = htmlMessage;
	}

	public String getLangOrUserBindingName() {
		return langOrUserBindingName;
	}
	public void setLangOrUserBindingName(String bindKey) {
		this.langOrUserBindingName = bindKey;
	}

	@Override
	public MailTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MailTemplateRuntime();
	}
	@Override
	public MetaMailTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	public void applyConfig(MailTemplateDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		charset = definition.getCharset();
		subject = definition.getSubject();
		from = definition.getFrom();
		replyTo = definition.getReplyTo();
		returnPath = definition.getReturnPath();

		if (definition.getPlainMessage() != null) {
			message = new MetaPlainTextBodyPart();
			message.applyConfig(definition.getPlainMessage());
		} else {
			message = null;
		}
		if (definition.getHtmlMessage() != null) {
			htmlMessage = new MetaHtmlBodyPart();
			htmlMessage.applyConfig(definition.getHtmlMessage());
		} else {
			htmlMessage = null;
		}

		if (definition.getLocalizedMailTemplateList() != null) {
			localizedMailTemplateList = new ArrayList<MetaLocalizedMailTemplate>();
			for (LocalizedMailTemplateDefinition ed: definition.getLocalizedMailTemplateList()) {

				MetaLocalizedMailTemplate mls = new MetaLocalizedMailTemplate();
//				mls.setLocaleName(ed.getLocaleName());
//				mls.setCharset(ed.getCharset());
//				mls.setSubject(ed.getSubject());
				mls.applyConfig(ed);

				localizedMailTemplateList.add(mls);
			}
		} else {
			localizedMailTemplateList = null;
		}

		langOrUserBindingName = definition.getLangOrUserBindingName();

	}

	//Meta → Definition
	public MailTemplateDefinition currentConfig() {
		MailTemplateDefinition definition = new MailTemplateDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setCharset(charset);
		definition.setSubject(subject);
		definition.setFrom(from);
		definition.setReplyTo(replyTo);
		definition.setReturnPath(returnPath);
		if (message != null) {
			definition.setPlainMessage(message.currentConfig());
		}
		if (htmlMessage != null) {
			definition.setHtmlMessage(htmlMessage.currentConfig());
		}

		if (localizedMailTemplateList != null) {
			for (MetaLocalizedMailTemplate mls: localizedMailTemplateList) {
				definition.addLocalizedMailTemplate(mls.currentConfig());
			}
		}
		definition.setLangOrUserBindingName(langOrUserBindingName);
		return definition;
	}

	public class MailTemplateRuntime extends BaseMetaDataRuntime {

		private GroovyTemplate subjectTemplate;
		private GroovyTemplate messageTemplate;
		private GroovyTemplate htmlMessageTemplate;

		private Map<String, TemplateSet> templateSetMap = new HashMap<String, TemplateSet>();

		private class TemplateSet {

			private GroovyTemplate subjectTemplate;
			private GroovyTemplate messageTemplate;
			private GroovyTemplate htmlMessageTemplate;
			private String charset;

		}

		public MailTemplateRuntime() {
			try {

				if (localizedMailTemplateList != null && localizedMailTemplateList.size() > 0) {

					ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();

					for (MetaLocalizedMailTemplate meta : localizedMailTemplateList) {

						TemplateSet templateSet = new TemplateSet();

						String localeName = meta.getLocaleName();

						if (meta.getSubject() != null) {
							templateSet.subjectTemplate = GroovyTemplateCompiler.compile(meta.getSubject(), "MailTemplate_Subject" + getName() +"__" + localeName, (GroovyScriptEngine) se);
						}

						if (meta.getMessage() != null && meta.getMessage().getContent() != null) {
							templateSet.messageTemplate = GroovyTemplateCompiler.compile(meta.getMessage().getContent(), "MailTemplate_Text" + getName() + "__" + localeName, (GroovyScriptEngine) se);
						}
						if (meta.getHtmlMessage() != null && meta.getHtmlMessage().getContent() != null) {
							templateSet.htmlMessageTemplate = GroovyTemplateCompiler.compile(meta.getHtmlMessage().getContent(), "MailTemplate_Html" + getName() + "__" + localeName, (GroovyScriptEngine) se);
						}

						if (meta.getCharset() != null) {
							templateSet.charset = meta.getCharset();
						}

						templateSetMap.put(localeName, templateSet);
					}
				}

				ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
				if (subject != null) {
					subjectTemplate = GroovyTemplateCompiler.compile(subject, "MailTemplate_Subject" + getName(), (GroovyScriptEngine) se);
				}
				if (message != null && message.getContent() != null) {
					messageTemplate = GroovyTemplateCompiler.compile(message.getContent(), "MailTemplate_Text" + getName(), (GroovyScriptEngine) se);
				}
				if (htmlMessage != null && htmlMessage.getContent() != null) {
					htmlMessageTemplate = GroovyTemplateCompiler.compile(htmlMessage.getContent(), "MailTemplate_Html" + getName(), (GroovyScriptEngine) se);
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaMailTemplate getMetaData() {
			return MetaMailTemplate.this;
		}

		public Mail createMail(Map<String, Object> bindings) {

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

			MailService ms = ServiceRegistry.getRegistry().getService(MailService.class);
			ExecuteContext ex = ExecuteContext.getCurrentContext();

			String _charset = charset;
			GroovyTemplate _subjectTemplate = subjectTemplate;
			GroovyTemplate _messageTemplate = messageTemplate;
			GroovyTemplate _htmlMessageTemplate = htmlMessageTemplate;
			if (templateSetMap.get(lang) != null) {
				_charset = templateSetMap.get(lang).charset;
				_subjectTemplate = templateSetMap.get(lang).subjectTemplate;
				_messageTemplate = templateSetMap.get(lang).messageTemplate;
				_htmlMessageTemplate = templateSetMap.get(lang).htmlMessageTemplate;
			}

			Mail mail = ms.createMail(ex.getCurrentTenant(), _charset);

			if (from != null && from.length() != 0) {
				mail.setFrom(from);
			}
			if (replyTo != null && replyTo.length() != 0) {
				mail.setReplyTo(replyTo);
			}
			if (returnPath != null && returnPath.length() != 0) {
				mail.setReturnPath(returnPath);
			}

			if (_subjectTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw);
				if (bindings != null) {
					for (Map.Entry<String, Object> e: bindings.entrySet()) {
						gtb.setVariable(e.getKey(), e.getValue());
					}
				}
				try {
					_subjectTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				mail.setSubject(sw.toString());
			}

			if (_messageTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw);
				if (bindings != null) {
					for (Map.Entry<String, Object> e: bindings.entrySet()) {
						gtb.setVariable(e.getKey(), e.getValue());
					}
				}
				try {
					_messageTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				mail.setMessage(sw.toString());
			}

			if (_htmlMessageTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw);
				if (bindings != null) {
					for (Map.Entry<String, Object> e: bindings.entrySet()) {
						gtb.setVariable(e.getKey(), e.getValue());
					}
				}
				try {
					_htmlMessageTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				String htmlCharset = htmlMessage.getCharset();
				if (htmlCharset == null) {
					htmlCharset = mail.getCharset();
				}
				mail.setHtmlMessage(new HtmlMessage(sw.toString(), htmlCharset));
			}

			return mail;
		}



	}


}
