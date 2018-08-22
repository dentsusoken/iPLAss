/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.mail.template.definition.LocalizedMailTemplateDefinition;

public class MetaLocalizedMailTemplate implements MetaData {

	private static final long serialVersionUID = -3105621819550502509L;

	private String localeName;
	private String charset;
	private String subject;
	private MetaPlainTextBodyPart message;
	private MetaHtmlBodyPart htmlMessage;

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


	public MetaLocalizedMailTemplate() {
	}

	public MetaLocalizedMailTemplate(String localeName, String stringValue) {
		this.localeName = localeName;
	}


	@Override
	public MetaLocalizedMailTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}

	// Definition → Meta
	public void applyConfig(LocalizedMailTemplateDefinition definition) {
		this.localeName = definition.getLocaleName();
		this.charset = definition.getCharset();
		this.subject = definition.getSubject();

		if (definition.getPlainMessage() != null) {
			message = new MetaPlainTextBodyPart();
			message.setContent(definition.getPlainMessage().getContent());
		} else {
			message = null;
		}

		if (definition.getHtmlMessage() != null) {
			htmlMessage = new MetaHtmlBodyPart();
			htmlMessage.setContent(definition.getHtmlMessage().getContent());
			htmlMessage.setCharset(definition.getHtmlMessage().getCharset());
		} else {
			htmlMessage = null;
		}

	}

	// Meta → Definition
	public LocalizedMailTemplateDefinition currentConfig() {
		LocalizedMailTemplateDefinition definition = new LocalizedMailTemplateDefinition();
		definition.setLocaleName(getLocaleName());
		definition.setSubject(subject);
		definition.setCharset(charset);
		definition.setPlainMessage(message.currentConfig());
		definition.setHtmlMessage(htmlMessage.currentConfig());

		return definition;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

}
