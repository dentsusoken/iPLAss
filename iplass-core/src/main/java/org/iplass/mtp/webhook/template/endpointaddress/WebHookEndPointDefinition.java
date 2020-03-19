/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.webhook.template.endpointaddress;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.definition.Definition;

@XmlRootElement
public class WebHookEndPointDefinition implements Definition{

	private static final long serialVersionUID = 8660365992932082923L;

	private String name;
	private String displayName;
	private String description;
	private String headerAuthType;
	private String headerAuthTypeName;// 例：Authorizationヘッダーでbasic認証時、内容のbasic username:passwordのbasicをほかの名前にしたいなら
	
	/** 送り先 */
	private String url;
	
	public WebHookEndPointDefinition() {
	}
	
	public WebHookEndPointDefinition(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getHeaderAuthType() {
		return headerAuthType;
	}

	public void setHeaderAuthType(String headerAuthType) {
		this.headerAuthType = headerAuthType;
	}
	
	public String getHeaderAuthTypeName() {
		return headerAuthTypeName;
	}

	public void setHeaderAuthTypeName(String headerAuthTypeName) {
		this.headerAuthTypeName = headerAuthTypeName;
	}

}
