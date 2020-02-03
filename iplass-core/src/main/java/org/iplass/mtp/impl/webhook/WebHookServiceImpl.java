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
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
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
import org.apache.http.protocol.HttpContext;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.WebHook;
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
	private AsyncTaskManager atm;
	private WebEndPointDefinitionManager wepdm;
	private boolean webHookUseProxy;
	private boolean webHookIsRetry;
	private int webHookRetryMaximumAttpempts;
	private int webHookRetryInterval;
	private String webHookHmacTokenAlgorithm;
	private String webHookHmacTokenDefaultName;
	private HttpClientConfig webHookHttpClientConfig;
	
	private static Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
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
	}

	@Override
	public void destroy() {
	}

	@Override
	public WebHook createWebHook(Tenant tenant, String charset) {
		return new WebHook();
	}

	/**
	 * 同期非同期を判断してからsendWebHook(WebHook webHook)を呼びます
	 * AsyncTaskManagerのローカルスレッドをつっかています
	 * 
	 * */
	@Override
	public void sendWebHook(Tenant tenant, WebHook webHook){
		if (webHook.isSynchronous()) {
			try {
				sendWebHook(webHook,tenant);
			} catch (Exception e) {
				throw e;
			}
		} else {
			atm.executeOnThread(new WebHookCallable(webHook, tenant));
		}
	}
	

	private void sendWebHook(WebHook webHook,Tenant tenant) {
		HashSet<Subscriber> endPointSet= generateSubscriberList(webHook.getEndPoints(),tenant.getId());
		
		try {
			for (Subscriber subscriber : endPointSet) {
				logger.info("WebHook:"+webHook.getTemplateName()+" Attempted.");
				HttpClientBuilder httpClientBuilder = null;
				if (webHookIsRetry) {
					int retryCount = webHookRetryMaximumAttpempts;
					int retryInterval = webHookRetryInterval;
					HttpRequestRetryHandler requestRetryHandler=new HttpRequestRetryHandler(){
						public boolean retryRequest(final IOException exception, int executionCount, final HttpContext context){
							//特定の失敗ケースだけリトライ
							if (exception.getClass() == InterruptedIOException.class 
									||exception.getClass() == UnknownHostException.class 
									||exception.getClass() == ConnectException.class
									||exception.getClass() == SSLException.class)	{
								if (executionCount < retryCount) {
									try {
										Thread.sleep(retryInterval);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									return true;
								}//TODO log retryInterval exceeded
							}
							logger.info("WebHook:"+webHook.getTemplateName()+"retry limit exceeded");
							return false;
							}
						};
						httpClientBuilder = HttpClientBuilder.create().setRetryHandler(requestRetryHandler);
				} else {
					httpClientBuilder = HttpClientBuilder.create().disableAutomaticRetries();
				}
				if (this.webHookUseProxy) {
					HttpHost proxy = new HttpHost(webHookHttpClientConfig.getProxyHost(),webHookHttpClientConfig.getProxyPort(),"http");
					httpClientBuilder.setProxy(proxy);
				}
				try {
					CloseableHttpClient httpClient= httpClientBuilder.build();
	
	
					//fill in webhook payload
					String payload = webHook.getWebHookContent();
					StringEntity se = new StringEntity(payload);
					se.setContentType(webHook.getContentType());
					
					Exception ex = null;
					
					HttpRequestBase httpRequest = getReqestMethodObject(webHook.getHttpMethod());
					httpRequest.setURI(new URI(subscriber.getUrl()));
					
					// and payload and headers
					if (isEnclosingRequest(httpRequest)) {
						((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
					}
					for(WebHookHeader headerEntry: webHook.getHeaders()) {
						httpRequest.setHeader(headerEntry.getKey(), headerEntry.getValue());
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
					CloseableHttpResponse response = httpClient.execute(httpRequest);
					logger.debug("\n---------------------------\n response headers: \n"+response.getAllHeaders().toString()+"\n response entity: \n"+ response.getEntity().getContentType()+"\n"+response.getEntity().getContent()+"\n---------------------------");
					try {
						//TODO: response handler?
						StatusLine statusLine= response.getStatusLine();
						if (statusLine.getStatusCode() == HttpStatus.SC_OK) {//普通に成功
							//TODO:add handler to command?
						}
						HttpEntity entity = response.getEntity();
						logger.debug("entity :"+entity.getContent().toString()+" success");
					} finally {
						response.close();
						httpClient.close();
					}
				} finally{
					
				}
			}
		}catch(Exception e)
		{
			
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
	
	private HashSet<Subscriber> generateSubscriberList(ArrayList<String> arrayList, int tenantId){
		HashSet<Subscriber> set= new HashSet<Subscriber>();
		for (String wepDefName: arrayList) {
			WebEndPointDefinition temp = wepdm.get(wepDefName);
			Subscriber sb = new Subscriber();
			String metaDataId = temp.getWebEndPointId();
			sb.setUrl(temp.getUrl());
			sb.setHeaderAuthType(temp.getHeaderAuthType());
			sb.setHeaderAuthContent(wepdm.getSecurityToken(tenantId, metaDataId, sb.getHeaderAuthType()));
			sb.setHmac(wepdm.getSecurityToken(tenantId, metaDataId, "WHHM"));
			set.add(sb);
		}
		return set;		
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
