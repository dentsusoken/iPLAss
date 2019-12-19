package org.iplass.mtp.webhook.template.definition;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import org.iplass.mtp.definition.Definition;

@XmlRootElement
public class WebHookTemplateDefinition implements Definition {

	private static final long serialVersionUID = 4835431145639526016L;
	
	private String name;
	private String displayName;
	private String description;
	
	private WebHookContent contentBody;
	private String sender;
	private String addressUrl;
	
	private Boolean synchronous;

	/** サブスクライバー：このwebhookを要求した方達 */
	private ArrayList<WebHookSubscriber> subscribers;
	
	/** headers */
	private ArrayList<WebHookHeader> headers;
	


	/**　リトライ関連　*/
	/** 失敗したらやり直ししますか */
	private boolean retry;
	/** やり直しの最大回数 */
	private int retryLimit;
	/** やり直す度の待ち時間(ms)*/
	private int retryInterval;
	
	
	public WebHookTemplateDefinition() {
	}

	public WebHookContent getContentBody() {
		if (contentBody == null) {
			this.contentBody=new WebHookContent();
		}
		return contentBody;
	}

	public void setContentBody(WebHookContent contentBody) {
		this.contentBody = contentBody;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getAddressUrl() {
		return addressUrl;
	}

	public void setAddressUrl(String addressUrl) {
		this.addressUrl = addressUrl;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public int getRetryLimit() {
		return retryLimit;
	}

	public void setRetryLimit(int retryLimit) {
		this.retryLimit = retryLimit;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}


	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<WebHookSubscriber> getSubscribers() {
		if (subscribers == null) {
			return new ArrayList<WebHookSubscriber>();
		}
		return subscribers;
	}

	public void setSubscribers(ArrayList<WebHookSubscriber> subscribers) {
		this.subscribers = subscribers;
	}
	
	public void addSubscriber(WebHookSubscriber newSubscriber) {
		if (this.subscribers==null) {
			this.subscribers = new ArrayList <WebHookSubscriber>();
		}
		this.subscribers.add(newSubscriber);
	}
	
	/***
	 * 特定のsubscriberオブジェを取得。
	 * 見つけないならnullが返される 
	 */
	public WebHookSubscriber getSubscriberByNameURL (String name, String url){
		
		for (WebHookSubscriber temp : subscribers) {
			if (temp.getUrl()==url&&temp.getSubscriberName()==name) {
				return temp;
			}
		} 
		return null;
	}

	public Boolean isSynchronous() {
		if (synchronous == null) {
			return false;
		}
		
		return synchronous;
	}

	public void setSynchronous(Boolean synchronous) {
		this.synchronous = synchronous;
	}
	
	public ArrayList<WebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<WebHookHeader> headers) {
		this.headers = headers;
	}
	
	public void addHeaders(WebHookHeader entry) {
		if (headers == null) {
			headers = new ArrayList<WebHookHeader>();
		}
		this.headers.add(entry);
	}
}

