package org.iplass.mtp.webhook.template.definition;

import java.io.Serializable;

public class WebHookSubscriber implements Serializable{

	private static final long serialVersionUID = 921157612085724142L;

	/** 送り先 */
	private String url;
	
	/** 申し込んだ方の名前 */
	private String subscriberName;
	
	/**　申し込んだ方のパスワード */
	private String subscriberPassword;
	
	/** セキュリティ関連 */
	private String securityUsername;
	private String securityPassword;
	private String securityToken;
	private String securityBearerToken;
	
	
	
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

	public String getSubscriberPassword() {
		return subscriberPassword;
	}

	public void setSubscriberPassword(String subscriberPassword) {
		this.subscriberPassword = subscriberPassword;
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

}
