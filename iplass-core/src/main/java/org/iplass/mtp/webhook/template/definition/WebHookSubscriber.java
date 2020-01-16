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
package org.iplass.mtp.webhook.template.definition;

import java.io.Serializable;
import java.util.UUID;

public class WebHookSubscriber implements Serializable{

	private static final long serialVersionUID = 921157612085724142L;
	
	/** 固有id、metaのと同じ内容になるはずです */
	private String webHookSubscriberId;
	
	/** 送り先 */
	private String url;
	
	/** 申し込んだ方の名前 */
	private String subscriberName;
	
	/** セキュリティ関連 */
	private String securityUsername;
	private String securityPassword;
	private String securityToken;
	private String securityBearerToken;
	
	public enum WEBHOOKSUBSCRIBERSTATE  {
			MODIFIED,
			DELETE,
			UNCHANGED,
			CREATED,
	}

	private WEBHOOKSUBSCRIBERSTATE state;
	
	//何らかしらの認証用物、他の設置なと TODO:



	public WebHookSubscriber() {
	}
	
	public WebHookSubscriber(String url, String subscriberName,String   securityUsername, String securityPassword, String securityToken, String securityBearerToken) {
		this.url = url;
		this.subscriberName = subscriberName;
		this.securityUsername = securityUsername;
		this.securityPassword = securityPassword;
		this.securityToken = securityToken;
		this.securityBearerToken = securityBearerToken;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}
	
	public String getSecurityUsername() {
		return securityUsername;
	}

	public void setSecurityUsername(String securityUsername) {
		this.securityUsername = securityUsername;
	}

	public String getSecurityPassword() {
		return securityPassword;
	}

	public void setSecurityPassword(String securityPassword) {
		this.securityPassword = securityPassword;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public String getSecurityBearerToken() {
		return securityBearerToken;
	}

	public void setSecurityBearerToken(String securityBearerToken) {
		this.securityBearerToken = securityBearerToken;
	}
	
	public String getWebHookSubscriberId() {
		return webHookSubscriberId;
	}

	public void setWebHookSubscriberId(String webHookSubscriberId) {
		this.webHookSubscriberId = webHookSubscriberId;
	}

	public WEBHOOKSUBSCRIBERSTATE getState() {
		return state;
	}

	public void setState(WEBHOOKSUBSCRIBERSTATE state) {
		this.state = state;
	}
	
	public boolean isCreate() {
		if (state == WEBHOOKSUBSCRIBERSTATE.CREATED) {
			return true;
		}
		return false;
	}
	
	public boolean isDelete() {
		if (state == WEBHOOKSUBSCRIBERSTATE.DELETE) {
			return true;
		}
		return false;
	}
	
	public boolean isChanged() {
		if (state == WEBHOOKSUBSCRIBERSTATE.MODIFIED) {
			return true;
		}
		return false;
	}
	

}
