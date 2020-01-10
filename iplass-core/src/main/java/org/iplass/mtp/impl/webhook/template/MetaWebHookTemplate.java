package org.iplass.mtp.impl.webhook.template;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.auth.authenticate.simpletoken.SimpleAuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.webhook.WebHookServiceImpl;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.template.definition.WebHookContent;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class MetaWebHookTemplate extends BaseRootMetaData implements DefinableMetaData<WebHookTemplateDefinition> {

	private static final long serialVersionUID = 6383360434482999137L;
	private static Logger logger = LoggerFactory.getLogger(MetaWebHookTemplate.class);
	/**
	//BaseRootMetaData にいる内容
	private String name;
	private String displayName;
	private String description;
	*/
	
	private WebHookContent contentBody;
	private String sender;
	private String addressUrl;
	
	/** サブスクライバー：このwebhookを要求した方達 */
	private ArrayList<MetaWebHookSubscriber> subscribers;

	private ArrayList<WebHookHeader> headers;
	private String tokenHeader;
	
	/**　リトライ関連　*/
	/** 失敗したらやり直ししますか */
	private boolean retry;
	/** やり直しの最大回数 */
	private int retryLimit;
	/** やり直す度の待ち時間(ms)*/
	private int retryInterval;
	/**　同期非同期　*/
	private boolean synchronous;


	
	@Override
	public WebHookTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebHookTemplateRuntime();
	}

	@Override
	public MetaWebHookTemplate copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	@Override
	public void applyConfig(WebHookTemplateDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		description = definition.getDescription();
		
		contentBody = definition.getContentBody();
		addressUrl = definition.getAddressUrl();
		sender = definition.getSender();
		tokenHeader = definition.getTokenHeader();
		subscribers =new ArrayList<MetaWebHookSubscriber>(); 
		for (WebHookSubscriber ws : definition.getSubscribers()) {
			MetaWebHookSubscriber mws = new MetaWebHookSubscriber();
			mws.setSubscriberName(ws.getSubscriberName());
			mws.setUrl(ws.getUrl());
			mws.setWebHookSubscriberId(ws.getWebHookSubscriberId());
			subscribers.add(mws);
		}
		
		
		retry = definition.isRetry();
		retryInterval = definition.getRetryInterval();
		retryLimit = definition.getRetryLimit();
		synchronous = definition.isSynchronous();
		
		headers = definition.getHeaders();
		
		if (definition.getMetaDataId()!=this.getId()) {
			logger.warn("Definition<->Meta id mismatch. template:"+definition.getMetaDataId()+"; Meta:"+this.getId()+"\n");
		}
	}



	//Meta → Definition
	@Override
	public WebHookTemplateDefinition currentConfig() {
		WebHookTemplateDefinition definition = new WebHookTemplateDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		
		definition.setContentBody(contentBody);
		definition.setAddressUrl(addressUrl);
		definition.setSender(sender);
		definition.setTokenHeader(tokenHeader);
		ArrayList<WebHookSubscriber> tempList = new ArrayList<WebHookSubscriber>();
		if (subscribers !=null) {
			for (MetaWebHookSubscriber metaSubscriber : subscribers) {
				WebHookSubscriber ws = new WebHookSubscriber();
				ws.setSubscriberName(metaSubscriber.getSubscriberName());
				ws.setUrl(metaSubscriber.getUrl());
				ws.setWebHookSubscriberId(metaSubscriber.getWebHookSubscriberId());
				tempList.add(ws);
			}
		}
		definition.setSubscribers(tempList);

		
		definition.setRetry(retry);
		definition.setRetryInterval(retryInterval);
		definition.setRetryLimit(retryLimit);
		definition.setSynchronous(synchronous);

		definition.setHeaders(headers);
		definition.setMetaDataId(id);
		
		return definition;
	}
	
	public WebHookContent getContentBody() {
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

	public ArrayList<MetaWebHookSubscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(ArrayList<MetaWebHookSubscriber> subscribers) {
		this.subscribers = subscribers;
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

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
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
	
	public ArrayList<WebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<WebHookHeader> headers) {
		this.headers = headers;
	}
	
	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	public class WebHookTemplateRuntime extends BaseMetaDataRuntime {

		public WebHookTemplateRuntime() {
			super();
			try {
			ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			contentTemplate = GroovyTemplateCompiler.compile(contentBody.getContent(), "WebHookTemplate_Text" + getName(), (GroovyScriptEngine) se);
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		private GroovyTemplate contentTemplate;
		
		@Override
		public MetaWebHookTemplate getMetaData() {
			return MetaWebHookTemplate.this;
		}
		
		public WebHook createWebHook(Map<String, Object> parameter) {
			checkState();
//			WebHookService ws = ServiceRegistry.getRegistry().getService(WebHookService.class);
//			ExecuteContext ex = ExecuteContext.getCurrentContext();
//			WebHookTemplate webHook = ws.createWebHook(ex.getCurrentTenant(), _charset);
			
			//fill up the info to webhooktemplate
			WebHook webHook = new WebHook(); 
			//webHook.setContent(contentBody); TODO modify with contentTemplate
			webHook.setName(name);
			webHook.setAddressUrl(addressUrl);
			webHook.setRetry(retry);
			webHook.setRetryInterval(retryInterval);
			webHook.setRetryLimit(retryLimit);

			webHook.setHeaders(headers);
			webHook.setTokenHeader(tokenHeader);

			webHook.setSynchronous(synchronous);

			if (contentTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw);
				gtb.setVariable("webHook", webHook);
				if (parameter != null) {
					for (Map.Entry<String, Object> e: parameter.entrySet()) {
						gtb.setVariable(e.getKey(), e.getValue());
					}
				}
				try {
					contentTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				WebHookContent content = contentBody.copy();
				content.setContent(sw.toString());
				webHook.setContent(content);
			}
			ArrayList<WebHookSubscriber> tempList = new ArrayList<WebHookSubscriber>();
			if (subscribers !=null) {
				for (MetaWebHookSubscriber metaSubscriber : subscribers) {
					WebHookSubscriber ws = new WebHookSubscriber();
					ws.setSubscriberName(metaSubscriber.getSubscriberName());
					ws.setUrl(metaSubscriber.getUrl());
					ws.setWebHookSubscriberId(metaSubscriber.getWebHookSubscriberId());
					tempList.add(ws);
				}
			}
			webHook.setSubscribers(tempList);
			webHook.setMetaDataId(id);
			return webHook;
		}
		
		
		
	} 
}
