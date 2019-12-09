package org.iplass.mtp.impl.webhook.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

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
import org.iplass.mtp.impl.webhook.WebHookService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.template.definition.WebHookContent;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;

@XmlRootElement
public class MetaWebHookTemplate extends BaseRootMetaData implements DefinableMetaData<WebHookTemplateDefinition> {

	private static final long serialVersionUID = 6383360434482999137L;
	
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
	private ArrayList<WebHookSubscriber> subscribers;
	
	private HashMap<String, String> headers;
	
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

		subscribers = definition.getSubscribers();

		retry = definition.isRetry();
		retryInterval = definition.getRetryInterval();
		retryLimit = definition.getRetryLimit();
		synchronous = definition.isSynchronous();
		
		headers = definition.getHeaders();
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

		definition.setSubscribers(subscribers);

		definition.setRetry(retry);
		definition.setRetryInterval(retryInterval);
		definition.setRetryLimit(retryLimit);
		definition.setSynchronous(synchronous);

		definition.setHeaders(headers);
		
		return definition;
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
			webHook.setSubscribers(subscribers);//should throw exception if there is no subscribers registered
			webHook.setRetry(retry);
			webHook.setRetryInterval(retryInterval);
			webHook.setRetryLimit(retryLimit);

			webHook.setHeaders(headers);

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
				WebHookContent content = contentBody;
				content.setContent(sw.toString());
				webHook.setContent(content);
			}

			return webHook;
		}
		
		
		
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

	public ArrayList<WebHookSubscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(ArrayList<WebHookSubscriber> subscribers) {
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
	
	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}



}
