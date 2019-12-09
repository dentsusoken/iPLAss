package org.iplass.mtp.impl.webhook.template;


import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;

public class MetaWebHookSubscriber implements MetaData {

	private static final long serialVersionUID = 3954182014266471233L;
	
	/** 送り先 */
	private String url;
	
	/** 申し込んだ方の名前 */
	private String subscriberName;
	
	/**　申し込んだ方のパスワード (多分いらない、実案件の設計に依る)*/
	private String subscriberPassword;

	public MetaWebHookSubscriber()  {
	}
	
	public MetaWebHookSubscriber(String url, String subscriberName, String subscriberPassword)  {
		this.url = url;
		this.subscriberName = subscriberName;
		this.subscriberPassword = subscriberPassword;
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

	@Override
	public MetaWebHookSubscriber copy() {
		return ObjectUtil.deepCopy(this);
	}
	//Definition → Meta
	public void applyConfig(WebHookSubscriber definition) {
		this.subscriberName = definition.getSubscriberName();
		this.subscriberPassword = definition.getSubscriberPassword();
		this.url = definition.getUrl();
	}

	//Meta → Definition
	public WebHookSubscriber currentConfig() {
		WebHookSubscriber definition = new WebHookSubscriber();
		definition.setUrl(this.url);
		definition.setSubscriberName(this.subscriberName);;
		definition.setSubscriberPassword(this.subscriberPassword);;
		return definition;
	}
}
