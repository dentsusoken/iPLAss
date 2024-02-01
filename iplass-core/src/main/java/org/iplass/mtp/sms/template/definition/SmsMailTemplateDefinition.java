/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.sms.template.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.mail.template.definition.PlainTextBodyPart;

/**
 * SMSメールテンプレート定義
 */
@XmlRootElement
public class SmsMailTemplateDefinition implements Definition {
	
	private static final long serialVersionUID = -4968797167511470126L;
	private String name;
	private String displayName;
	private String description;
	private PlainTextBodyPart plainMessage;
	private List<LocalizedSmsMailTemplateDefinition> localizedSmsMailTemplateList;
	private String langOrUserBindingName;
	
	public void addLocalizedSmsMailTemplate(LocalizedSmsMailTemplateDefinition localizedSmsMailTemplate) {
		if (localizedSmsMailTemplateList == null) {
			localizedSmsMailTemplateList = new ArrayList<LocalizedSmsMailTemplateDefinition>();
		}
		localizedSmsMailTemplateList.add(localizedSmsMailTemplate);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PlainTextBodyPart getPlainMessage() {
		return plainMessage;
	}

	public void setPlainMessage(PlainTextBodyPart plainMessage) {
		this.plainMessage = plainMessage;
	}

	public List<LocalizedSmsMailTemplateDefinition> getLocalizedSmsMailTemplateList() {
		return localizedSmsMailTemplateList;
	}

	public void setLocalizedSmsMailTemplateList(
			List<LocalizedSmsMailTemplateDefinition> localizedSmsMailTemplateList) {
		this.localizedSmsMailTemplateList = localizedSmsMailTemplateList;
	}

	public String getLangOrUserBindingName() {
		return langOrUserBindingName;
	}

	public void setLangOrUserBindingName(String langOrUserBindingName) {
		this.langOrUserBindingName = langOrUserBindingName;
	}
}
