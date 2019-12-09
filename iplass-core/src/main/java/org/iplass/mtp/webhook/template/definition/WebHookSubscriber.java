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
	
	/** このサブが使いたい安全対策 */
	private String securityMethod;
	//何らかしらの認証用物、他の設置なと TODO:


	public WebHookSubscriber() {
	}
	
	public WebHookSubscriber(String url, String subscriberName, String securityMethod) {
		this.url = url;
		this.subscriberName = subscriberName;
		this.securityMethod = securityMethod;
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
	
	public String getSecurityMethod() {
		return securityMethod;
	}

	public void setSecurityMethod(String securityMethod) {
		this.securityMethod = securityMethod;
	}
}
