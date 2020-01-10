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
