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
package org.iplass.mtp.impl.webhook;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import java.io.InterruptedIOException;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.webhook.responsehandler.JustLogWebHookResponseHandlerImpl;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookResponseHandler;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinitionManager;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinition;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHookServiceImpl extends AbstractTypedMetaDataService<MetaWebHookTemplate, WebHookTemplateRuntime>
		implements WebHookService {
	public final String WEBHOOK_USE_PROXY= "webHook.Use.Proxy";
	public final String WEBHOOK_ISRETRY= "webHook.IsRetry";
	public final String WEBHOOK_RETRY_MAXIMUMATTEMPTS = "webHook.Retry.MaximumAttempts";
	public final String WEBHOOK_RETRY_INTERVAL = "webHook.Retry.Interval";
	public final String WEBHOOK_HMACTOKEN_ALGORITHM = "webHook.HmacToken.Algorithm";
	public final String WEBHOOK_HMACTOKEN_DEFAULTNAME = "webHook.HmacToken.DefaultName";
	public final String WEBHOOK_HTTP_CLIENT_CONFIG = "httpClientConfig";
	public final String CONTENT_TYPE_FORM_URLENCODED= "application/x-www-form-urlencoded";
	private AsyncTaskManager atm;
	private WebEndPointDefinitionManager wepdm;
	private boolean webHookUseProxy;
	private boolean webHookIsRetry;
	private int webHookRetryMaximumAttpempts;
	private int webHookRetryInterval;
	private String webHookHmacTokenAlgorithm;
	private String webHookHmacTokenDefaultName;
	private HttpClientConfig webHookHttpClientConfig;
	public static CloseableHttpClient webHookHttpClient;
	public static Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
	public static final String WEBHOOK_TEMPLATE_META_PATH = "/webhook/template/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<WebHookTemplateDefinition, MetaWebHookTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaWebHookTemplate.class, WebHookTemplateDefinition.class);
		}

		@Override
		public TypedDefinitionManager<WebHookTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebHookTemplateDefinitionManager.class);
		}
	}

	public static String getFixedPath() {
		return WEBHOOK_TEMPLATE_META_PATH;
	}

	@Override
	public Class<MetaWebHookTemplate> getMetaDataType() {
		return MetaWebHookTemplate.class;
	}

	@Override
	public Class<WebHookTemplateRuntime> getRuntimeType() {
		return WebHookTemplateRuntime.class;
	}

	@Override
	public void init(Config config) {
		for (String name: config.getNames()) {
			switch (name) {
			case WEBHOOK_USE_PROXY:
				 String tempUseProxy = config.getValue(WEBHOOK_USE_PROXY);
				 if (tempUseProxy.replaceAll("\\s","").toLowerCase().equals("true")) {
					 webHookUseProxy = true;
				 } else {
					 webHookUseProxy = false;
				 }
				 break;
			case WEBHOOK_ISRETRY:
				 String tempIsRetry = config.getValue(WEBHOOK_ISRETRY);
				 if (tempIsRetry.replaceAll("\\s","").toLowerCase().equals("true")) {
					 webHookIsRetry = true;
				 } else {
					 webHookIsRetry = false;
				 }
				break;
			case WEBHOOK_RETRY_MAXIMUMATTEMPTS:
				webHookRetryMaximumAttpempts = Integer.valueOf(config.getValue(WEBHOOK_RETRY_MAXIMUMATTEMPTS));
				break;
			case WEBHOOK_RETRY_INTERVAL:
				webHookRetryInterval = Integer.valueOf(config.getValue(WEBHOOK_RETRY_INTERVAL));
				break;
			case WEBHOOK_HMACTOKEN_ALGORITHM:
				webHookHmacTokenAlgorithm = config.getValue(WEBHOOK_HMACTOKEN_ALGORITHM);
				break;
			case WEBHOOK_HMACTOKEN_DEFAULTNAME:
				webHookHmacTokenDefaultName = config.getValue(WEBHOOK_HMACTOKEN_DEFAULTNAME);
				break;
			case WEBHOOK_HTTP_CLIENT_CONFIG:
				webHookHttpClientConfig = config.getValue(WEBHOOK_HTTP_CLIENT_CONFIG, HttpClientConfig.class);
				if (webHookHttpClientConfig == null) {
					webHookHttpClientConfig = new HttpClientConfig();
					webHookHttpClientConfig.inited(this, config);
				}
				break;
			}
		}
		//TODO:add default values to the necessary items, such as hmac algorithm
		atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);
		wepdm = ManagerLocator.getInstance().getManager(WebEndPointDefinitionManager.class);
		
		//再利用の為、httpclientをstaticにします
		initWebHookHttpClient();
	}

	@Override
	public void destroy() {
		try {
			webHookHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public WebHook createWebHook(Tenant tenant, String charset) {
		return new WebHook();
	}

	/**
	 * 同期sendWebHook
	 * 
	 * */
	@Override
	public void sendWebHook(Tenant tenant, WebHook webHook){
		try {
			sendWebHook(webHook,tenant);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 非同期sendWebHook
	 * AsyncTaskManagerのローカルスレッドをつっかています
	 * */
	@Override
	public void sendWebHookAsync(Tenant tenant, WebHook webHook){
			atm.executeOnThread(new WebHookCallable(webHook, tenant));
	}
	

	private void sendWebHook(WebHook webHook,Tenant tenant) {
		HashSet<Subscriber> endPointSet= generateSubscriberList(webHook.getEndPoints(),tenant.getId(),webHook.getBinding());
		logger.info("WebHook:"+webHook.getTemplateName()+" Attempted.");
		try {
			//fill in webhook configures
			for (Subscriber subscriber : endPointSet) {
				String payload = webHook.getWebHookContent();
			
				HttpRequestBase httpRequest = getReqestMethodObject(webHook.getHttpMethod());
				httpRequest.setURI(new URI(subscriber.getUrl()));
				HttpContext httpContext = new BasicHttpContext();
				httpContext.setAttribute(WEBHOOK_ISRETRY, webHookIsRetry);
				httpContext.setAttribute(WEBHOOK_RETRY_MAXIMUMATTEMPTS, webHookRetryMaximumAttpempts);
				httpContext.setAttribute(WEBHOOK_RETRY_INTERVAL, webHookRetryInterval);
				
				//payload
				if (webHook.getContentType().equals(CONTENT_TYPE_FORM_URLENCODED)) {
//					String encodedPayload = URLEncoder.encode(payload. replaceAll("\\s",""), "UTF-8");slackのapiテストで見ると、いらないかな
					String url = subscriber.getUrl()+ "?" + payload;
					httpRequest.setURI(new URI(url ));
				} else	if (isEnclosingRequest(httpRequest)) {
					StringEntity se = new StringEntity(payload);
					se.setContentType(webHook.getContentType());
					((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
				} 
				
				//and headers
				if (webHook.getHeaders()!=null) {
					for(WebHookHeader headerEntry: webHook.getHeaders()) {
						httpRequest.setHeader(headerEntry.getKey(), headerEntry.getValue());
					}
				}
				//hmac
				if (subscriber.getHmac()!=null) {
					if (!subscriber.getHmac().isEmpty()) {
						String hmacToken= getHmacSha256(subscriber.getHmac(), payload);
						String tokenHeader;
						if (webHook.getTokenHeader()==null||webHook.getTokenHeader().replaceAll("\\s","").isEmpty()) {
							tokenHeader = webHookHmacTokenDefaultName;
						} else {
							tokenHeader = webHook.getTokenHeader();
						}
						httpRequest.setHeader(tokenHeader, hmacToken);
					}
				}
				//headerのauthorizationでの認証情報
				if (subscriber.getHeaderAuthType()==null || subscriber.getHeaderAuthType().isEmpty()) {
					
				}else {
					if (subscriber.getHeaderAuthType()=="WHBT") {
						httpRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " +subscriber.getHeaderAuthContent());
					} else if(subscriber.getHeaderAuthType()=="WHBA") {
						httpRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " +subscriber.getHeaderAuthContent());
					}
				}
				CloseableHttpResponse response = webHookHttpClient.execute(httpRequest,httpContext);
				logger.debug("\n---------------------------\n response headers: \n"+response.getAllHeaders().toString()+"\n response entity: \n"+ response.getEntity().getContentType()+"\n"+response.getEntity().getContent()+"\n---------------------------");
				try {
					//TODO: response handler?
					WebHookResponseHandler whrh= new JustLogWebHookResponseHandlerImpl();
					whrh.handleResponse(response);
				} finally {
					response.close();
				}
			}
		} catch (Exception e) {
			throw (RuntimeException)e;
		}
	}

	
	/**
	 * Stringのtokenとpayloadでhmac暗号化する
	 * @param: String token
	 * @param: String message
	 * @return: base64String
	 * */
	public String getHmacSha256(String token, String message) {
		try {
		    Mac Hmac = Mac.getInstance(this.webHookHmacTokenAlgorithm);
		    SecretKeySpec secret_key = new SecretKeySpec(token.getBytes("UTF-8"), this.webHookHmacTokenAlgorithm);
		    Hmac.init(secret_key);

		    String hash = Base64.encodeBase64String(Hmac.doFinal(message.getBytes("UTF-8")));
			return hash;
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return null;
		
	}
	
	private class WebHookCallable implements Callable<Void>{
		WebHook webHook;
		Tenant tenant;
		public WebHookCallable(WebHook webHook,Tenant tenant) {
			this.webHook = webHook;
			this.tenant = tenant;
		}
		@Override
		public Void call() throws Exception {
			sendWebHook(webHook,tenant);
			return null;
		}
		
	}
	
	private HttpRequestBase getReqestMethodObject(String methodName){
		if (methodName.equals("GET")) {
			return new HttpGet();
		}else if (methodName.equals("POST")) {
			return new HttpPost();
		}else if (methodName.equals("DELETE")) {
			return new HttpDelete();
		}else if (methodName.equals("PUT")) {
			return new HttpPut();
		}else if (methodName.equals("PATCH")) {
			return new HttpPatch();
		}else if (methodName.equals("HEAD")) {
			return new HttpHead();
		}else if (methodName.equals("OPTIONS")) {
			return new HttpOptions();
		}else if (methodName.equals("TRACE")) {
			return new HttpTrace();
		}else {
			return new HttpPost();
		}
	}
	private boolean isEnclosingRequest(HttpRequestBase request) {
		if (request.getMethod().equals("PATCH")
				|| request.getMethod().equals("POST")
				|| request.getMethod().equals("PUT")) {
			return true;
		} else {
			return false;
		}
	}
	
	private HashSet<Subscriber> generateSubscriberList(ArrayList<String> arrayList, int tenantId, Map<String, Object>bindings){
		HashSet<Subscriber> set= new HashSet<Subscriber>();
		ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		for (String wepDefName: arrayList) {
			
			WebEndPointDefinition temp = wepdm.get(wepDefName);
			Subscriber sb = new Subscriber();

			//ws.setUrl(metaSubscriber.getUrl());
			
			String metaDataId = temp.getWebEndPointId();
			sb.setHeaderAuthType(temp.getHeaderAuthType());
			sb.setHeaderAuthContent(wepdm.getSecurityToken(tenantId, metaDataId, sb.getHeaderAuthType()));
			sb.setHmac(wepdm.getSecurityToken(tenantId, metaDataId, "WHHM"));
			StringWriter sw = new StringWriter();
			GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,bindings);
			GroovyTemplate _urlTemp = GroovyTemplateCompiler.compile(temp.getUrl(), "WebHookTemplate_Subscriber_" + temp.getWebEndPointId() + "_" + temp.getName(), (GroovyScriptEngine) se);
			try {
				_urlTemp.doTemplate(gtb);
			} catch (IOException e) {
				throw new ScriptRuntimeException(e);
			}
			sb.setUrl(sw.toString());
			set.add(sb);
		}
		return set;
	}
	private void initWebHookHttpClient() {
		if (webHookHttpClient == null) {
			try {
				HttpRequestRetryHandler requestRetryHandler=new HttpRequestRetryHandler(){
				public boolean retryRequest(final IOException exception, int executionCount, final HttpContext context){
					if (!(boolean)context.getAttribute(WEBHOOK_ISRETRY)) {
						return false;
					}
					//特定の失敗ケースだけリトライ
					if (exception.getClass() == InterruptedIOException.class 
							||exception.getClass() == UnknownHostException.class 
							||exception.getClass() == ConnectException.class
							||exception.getClass() == SSLException.class)	{		
						logger.info("WebHook:"+exception.getClass().getName()+" has occured. Stop retrying.");
					}else{
						if (executionCount < (int)context.getAttribute(WEBHOOK_RETRY_MAXIMUMATTEMPTS)) {
							try {
								Thread.sleep((int)context.getAttribute(WEBHOOK_RETRY_MAXIMUMATTEMPTS));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							return true;
						}
					}
						return false;
					}
				};
				HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setRetryHandler(requestRetryHandler);
				httpClientBuilder = HttpClientBuilder.create().disableAutomaticRetries();
				
				if (this.webHookUseProxy) {
					HttpHost proxy = new HttpHost(webHookHttpClientConfig.getProxyHost(),webHookHttpClientConfig.getProxyPort(),"http");
					httpClientBuilder.setProxy(proxy);
				}
				webHookHttpClient= httpClientBuilder.build();
			} catch(Exception e){
				throw e;
			}
		}
	}
	private class Subscriber{

		String url;
		String hmac;
		String headerAuthContent;
		String headerAuthType;
		
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getHmac() {
			return hmac;
		}
		public void setHmac(String hmac) {
			this.hmac = hmac;
		}
		public String getHeaderAuthContent() {
			return headerAuthContent;
		}
		public void setHeaderAuthContent(String headerAuthContent) {
			this.headerAuthContent = headerAuthContent;
		}
		public String getHeaderAuthType() {
			return headerAuthType;
		}
		public void setHeaderAuthType(String headerAuthType) {
			this.headerAuthType = headerAuthType;
		}
	}
}
