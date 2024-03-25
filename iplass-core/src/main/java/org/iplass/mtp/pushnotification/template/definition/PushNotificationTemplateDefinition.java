/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.pushnotification.template.definition;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.definition.Definition;

@XmlRootElement
public class PushNotificationTemplateDefinition implements Definition {
	private static final long serialVersionUID = 3230091704630984421L;

	private String name;
	private String displayName;
	private String description;
	private String title;
	private String body;
	private String icon;
	private List<LocalizedNotificationDefinition> localizedNotificationList;
	
	private String configScript;
	
	private String langOrUserBindingName;

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

	public List<LocalizedNotificationDefinition> getLocalizedNotificationList() {
		return localizedNotificationList;
	}

	public void setLocalizedNotificationList(
			List<LocalizedNotificationDefinition> localizedNotificationList) {
		this.localizedNotificationList = localizedNotificationList;
	}
	
	public void addLocalizedNotification(LocalizedNotificationDefinition localizedNotification) {
		if (localizedNotificationList == null) {
			localizedNotificationList = new ArrayList<>();
		}
		localizedNotificationList.add(localizedNotification);
	}

	public String getConfigScript() {
		return configScript;
	}

	/**
	 * メッセージテキスト以外のPushNotificationの設定を行うためのScript。
	 * メッセージがセットされたPushNotificationのインスタンスが、
	 * 'pn'という変数名でバインドされているので、各種設定が可能。
	 * 
	 * @param configScript
	 */
	public void setConfigScript(String configScript) {
		this.configScript = configScript;
	}

	public String getLangOrUserBindingName() {
		return langOrUserBindingName;
	}

	public void setLangOrUserBindingName(String langOrUserBindingName) {
		this.langOrUserBindingName = langOrUserBindingName;
	}

}
