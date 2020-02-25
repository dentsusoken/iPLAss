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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
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
	public static final String WEBHOOK_SECURITY_BINDING_HMACRESULT ="webhookHmacResult";
	public static final String WEBHOOK_SECURITY_BINDING_HMACTOKEN ="webhookHmacToken";
	public static final String WEBHOOK_SECURITY_BINDING_BASIC ="webhookBasic";
	public static final String WEBHOOK_SECURITY_BINDING_BASICNAME ="webhookBasicName";
	public static final String WEBHOOK_SECURITY_BINDING_BASICPASSWORD ="webhookBasicPassword";
	public static final String WEBHOOK_SECURITY_BINDING_BEARER ="webhookBearer";

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
				if (webHookHmacTokenAlgorithm==null||webHookHmacTokenAlgorithm.replaceAll("\\s","").isEmpty()) {
					webHookHmacTokenAlgorithm="HmacSHA256";
				}
				break;
			case WEBHOOK_HMACTOKEN_DEFAULTNAME:
				webHookHmacTokenDefaultName = config.getValue(WEBHOOK_HMACTOKEN_DEFAULTNAME);
				if (webHookHmacTokenDefaultName==null||webHookHmacTokenDefaultName.replaceAll("\\s","").isEmpty()) {
					webHookHmacTokenDefaultName="X-IPLASS-HMAC";
				}
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
		String urlQuery = "";
		if (isUrlQueryAllowed(webHook.getHttpMethod())) {
			urlQuery = webHook.getUrlQuery();
		}
		HashSet<Subscriber> endPointSet= generateSubscriberList(webHook.getEndPoints(),tenant.getId(),webHook.getBinding(),urlQuery);
		logger.debug("WebHook:"+webHook.getTemplateName()+" Attempted.");
		try {
			//fill in webhook configures
			for (Subscriber subscriber : endPointSet) {			
				HttpRequestBase httpRequest = getReqestMethodObject(webHook.getHttpMethod());
				
				HttpContext httpContext = new BasicHttpContext();
				httpContext.setAttribute(WEBHOOK_ISRETRY, webHookIsRetry);
				httpContext.setAttribute(WEBHOOK_RETRY_MAXIMUMATTEMPTS, webHookRetryMaximumAttpempts);
				httpContext.setAttribute(WEBHOOK_RETRY_INTERVAL, webHookRetryInterval);
				
				
				String payLoadContent = null;
				GroovyTemplate contentTemplate = webHook.getGroovyTemplateContent();
				if (contentTemplate != null) {
					StringWriter sw = new StringWriter();
					GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,webHook.getBinding());
					gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACTOKEN, subscriber.getHmac());
					if (subscriber.getHeaderAuthType().equals("WHBA")) {
						String[] basicArray = subscriber.getHeaderAuthContent().replaceAll("\\s","").split(":");
						if (basicArray==null||basicArray.length<2) {
						} else {
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, basicArray[0]);
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, basicArray[1]);
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, "");	
						}
					} else if (subscriber.getHeaderAuthType().equals("WHBT")){
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, "");
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, "");
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, subscriber.getHeaderAuthContent());	
					}
					try {
						contentTemplate.doTemplate(gtb);
					} catch (IOException e) {
						throw new ScriptRuntimeException(e);
					}
					payLoadContent = sw.toString();
				}
				//payload
				if (isEnclosingRequest(httpRequest)) {
					StringEntity se = new StringEntity(payLoadContent,"UTF-8");
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
				String hmacToken =null;
				if (subscriber.getHmac()!=null) {
					if (!subscriber.getHmac().isEmpty()) {
						hmacToken= getHmacSha256(subscriber.getHmac(), payLoadContent);
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
					String scheme = null;
					String authContent = null;
					if (subscriber.getHeaderAuthType().equals("WHBT")) {
						scheme = "Bearer";
						authContent = subscriber.getHeaderAuthContent();
					} else if (subscriber.getHeaderAuthType().equals("WHBA")) {
						scheme = "Basic";
						authContent = Base64.encodeBase64String(subscriber.getHeaderAuthContent().getBytes());
					}
					if (subscriber.getHeaderAuthTypeName()!=null) {
						String authTypeName = subscriber.getHeaderAuthTypeName().replace("\n", "").replaceAll("\\s+", "");
						if (!authTypeName.isEmpty()) {
							scheme = authTypeName;
						}
					}
					httpRequest.setHeader(HttpHeaders.AUTHORIZATION, scheme+ " " + authContent);
				}
				
				
				if (subscriber.getUrlTemplate()!=null) {
					StringWriter sw = new StringWriter();
					GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,webHook.getBinding());
					gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACTOKEN, subscriber.getHmac());
					
					gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACRESULT, (hmacToken==null||hmacToken.length()==0)?null:hmacToken);
					if (subscriber.getHeaderAuthType().equals("WHBA")) {
						String[] basicArray = subscriber.getHeaderAuthContent().replaceAll("\\s","").split(":");
						if (basicArray==null||basicArray.length<2) {
						} else {
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, basicArray[0]);
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, basicArray[1]);
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, "");	
						}
					} else if (subscriber.getHeaderAuthType().equals("WHBT")){
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, "");
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, "");
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, subscriber.getHeaderAuthContent());	
					}
					try {
						subscriber.getUrlTemplate().doTemplate(gtb);
					} catch (IOException e) {
						throw new ScriptRuntimeException(e);
					}
					subscriber.setUrl(sw.toString().replaceAll("\\s",""));//スベース抜き。でないとurlになれない
				}
				
				httpRequest.setURI(new URI(subscriber.getUrl()));
				CloseableHttpResponse response = webHookHttpClient.execute(httpRequest,httpContext);
				logger.debug("\n---------------------------\n response headers: \n"+response.getAllHeaders().toString()+"\n response entity: \n"+ response.getEntity().getContentType()+"\n"+response.getEntity().getContent()+"\n---------------------------");
				try {
					WebHookResponse whr = generateWebHookResponse(response);
					webHook.getResultHandler().handleResponse(whr);
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
	private boolean isUrlQueryAllowed(String methodName){
		if (methodName.equals("GET")) {
			return true;
		}else if (methodName.equals("DELETE")) {
			return true;
		}else if (methodName.equals("POST")) {
			return true;
		}else {
			return false;
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
	
	private HashSet<Subscriber> generateSubscriberList(ArrayList<String> arrayList, int tenantId, Map<String, Object>bindings,String urlQuery){
		HashSet<Subscriber> set= new HashSet<Subscriber>();
		ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		for (String wepDefName: arrayList) {
			WebEndPointDefinition temp = wepdm.get(wepDefName);
			Subscriber sb = new Subscriber();
			String tempUrl = temp.getUrl() + urlQuery;
			tempUrl = tempUrl.replace("\n", "");
			tempUrl= tempUrl.replaceAll("\\s+", "");
			GroovyTemplate _urlTemp = GroovyTemplateCompiler.compile(tempUrl, "WebHookTemplate_Subscriber_" + temp.getWebEndPointId() + "_" + temp.getName(), (GroovyScriptEngine) se);
			
			String metaDataId = temp.getWebEndPointId();
			sb.setHeaderAuthType(temp.getHeaderAuthType()==null?"":temp.getHeaderAuthType());//headerAuthがないなら""のは、.equals()の使用を邪魔されせないため
			sb.setHeaderAuthContent(wepdm.getSecurityToken(tenantId, metaDataId, sb.getHeaderAuthType()));
			sb.setHmac(wepdm.getSecurityToken(tenantId, metaDataId, "WHHM"));
			sb.setUrlTemplate(_urlTemp);
			sb.setHeaderAuthTypeName(temp.getHeaderAuthTypeName());

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
	private WebHookResponse generateWebHookResponse(HttpResponse response) {
		WebHookResponse whr = new WebHookResponse();
		if (response.getStatusLine()== null) {
			whr.setStatusCode(0);
			whr.setReasonPhrase(null);
		} else {
			whr.setStatusCode(response.getStatusLine().getStatusCode());
			whr.setReasonPhrase(response.getStatusLine().getReasonPhrase());
		}
		if (response.getEntity()==null) {
			whr.setContentType(null);
			whr.setContentEncoding(null);
			whr.setResponseBody(null);
		}else {
			try {
				String entity = EntityUtils.toString(response.getEntity());
				whr.setResponseBody(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			whr.setContentType(response.getEntity().getContentType()==null?null:response.getEntity().getContentType().getValue());
			whr.setContentEncoding(response.getEntity().getContentEncoding()==null?null:response.getEntity().getContentEncoding().getValue());
		}
		HashMap<String,String> headers = new HashMap<String,String>();
		for (Header hd : response.getAllHeaders()) {
			headers.put(hd.getName(), hd.getValue());
		}
		whr.setHeaders(headers);
		return whr;
	}
		
	private class Subscriber{

		String url;
		String hmac;
		String headerAuthContent;
		String headerAuthType;
		GroovyTemplate urlTemplate;
		String headerAuthTypeName;

		public GroovyTemplate getUrlTemplate() {
			return urlTemplate;
		}
		public void setUrlTemplate(GroovyTemplate urlTemplate) {
			this.urlTemplate = urlTemplate;
		}
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
		public String getHeaderAuthTypeName() {
			return headerAuthTypeName;
		}
		public void setHeaderAuthTypeName(String headerAuthTypeName) {
			this.headerAuthTypeName = headerAuthTypeName;
		}

	}
}
