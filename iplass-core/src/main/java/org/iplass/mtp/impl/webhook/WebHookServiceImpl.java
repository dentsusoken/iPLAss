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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.AsyncTaskManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.WebHookHeader;
import org.iplass.mtp.webhook.WebHookResponse;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinitionManager;
import org.iplass.mtp.webhook.template.endpointaddress.WebHookEndPointDefinitionManager;
import org.iplass.mtp.webhook.template.endpointaddress.WebHookEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHookServiceImpl extends AbstractTypedMetaDataService<MetaWebHookTemplate, WebHookTemplateRuntime>
		implements WebHookService {
	public final String WEBHOOK_USE_PROXY= "webHookUseProxy";
	public final String WEBHOOK_ISRETRY= "webHookIsRetry";
	public final String WEBHOOK_RETRY_MAXIMUMATTEMPTS = "webHookRetryMaximumAttempts";
	public final String WEBHOOK_RETRY_INTERVAL = "webHookRetryInterval";
	public final String WEBHOOK_HMACTOKEN_ALGORITHM = "webHookHmacHashAlgorithm";
	public final String WEBHOOK_HMACTOKEN_DEFAULTNAME = "webHookHmacTokenDefaultName";
	public final String WEBHOOK_HTTP_CLIENT_CONFIG = "httpClientConfig";
	private AsyncTaskManager atm;
	private WebHookEndPointDefinitionManager wepdm;
	private boolean webHookIsRetry;
	private int webHookRetryMaximumAttpempts;
	private int webHookRetryInterval;
	private String webHookHmacHashAlgorithm;
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
			case WEBHOOK_ISRETRY:
				 String tempIsRetry = config.getValue(WEBHOOK_ISRETRY);
				 if ("true".equals(tempIsRetry.replaceAll("\\s","").toLowerCase())) {
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
				webHookHmacHashAlgorithm = config.getValue(WEBHOOK_HMACTOKEN_ALGORITHM);
				if (webHookHmacHashAlgorithm==null||webHookHmacHashAlgorithm.replaceAll("\\s","").isEmpty()) {
					webHookHmacHashAlgorithm="HmacSHA256";
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
				}
				webHookHttpClientConfig.inited(this, config);
				break;
			}
		}

		atm = ManagerLocator.getInstance().getManager(AsyncTaskManager.class);
		wepdm = ManagerLocator.getInstance().getManager(WebHookEndPointDefinitionManager.class);
		
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
		logger.debug("WebHook:"+webHook.getTemplateName()+" Attempted.");
		try {
			for (WebHookEndPoint subscriber : webHook.getWebHookEndPointList()) {
				if (webHook.getHttpMethod()==null) {
					logger.debug("WebHook:"+webHook.getTemplateName()+" request method undefined.");
				}
				HttpRequestBase httpRequest = getReqestMethodObject(webHook.getHttpMethod());
				HttpContext httpContext = new BasicHttpContext();
				httpContext.setAttribute(WEBHOOK_ISRETRY, webHookIsRetry);
				httpContext.setAttribute(WEBHOOK_RETRY_MAXIMUMATTEMPTS, webHookRetryMaximumAttpempts);
				httpContext.setAttribute(WEBHOOK_RETRY_INTERVAL, webHookRetryInterval);

				//payload
				if (isEnclosingRequest(httpRequest)) {
					if (subscriber.getPayloadContent()!=null&&!subscriber.getPayloadContent().replaceAll("\\s","").isEmpty()) {
							StringEntity se = new StringEntity(subscriber.getPayloadContent(),"UTF-8");
							se.setContentType(webHook.getContentType());
							((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
						}
				} else {
					if (webHook.getContentType()!=null&&!webHook.getContentType().replaceAll("\\s","").isEmpty()) {
						httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, webHook.getContentType());
					}
				}

				//and headers
				if (webHook.getHeaders()!=null) {
					for(WebHookHeader headerEntry: webHook.getHeaders()) {
						httpRequest.setHeader(headerEntry.getKey(), headerEntry.getValue());
					}
				}
				
				if (subscriber.getHmac()!=null) {
					if (!subscriber.getHmac().isEmpty()) {
						String tokenHeader;
						if (webHook.getTokenHeader()==null||webHook.getTokenHeader().replaceAll("\\s","").isEmpty()) {
							tokenHeader = webHookHmacTokenDefaultName;
						} else {
							tokenHeader = webHook.getTokenHeader();
						}
						httpRequest.setHeader(tokenHeader, subscriber.getHmacResult());
					}
				}
				//headerのauthorizationでの認証情報
				if (subscriber.getHeaderAuthType()==null || subscriber.getHeaderAuthType().isEmpty()) {

				}else {
					String scheme = null;
					String authContent = null;
					if ("WHBT".equals(subscriber.getHeaderAuthType())) {
						scheme = "Bearer";
						authContent = subscriber.getHeaderAuthContent();
					} else if ("WHBA".equals(subscriber.getHeaderAuthType())) {
						scheme = "Basic";
						authContent = Base64.encodeBase64String(subscriber.getHeaderAuthContent().getBytes());
					}
					if (subscriber.getHeaderAuthSchemeName()!=null) {
						String authTypeName = subscriber.getHeaderAuthSchemeName().replace("\n", "").replaceAll("\\s+", "");
						if (!authTypeName.isEmpty()) {
							scheme = authTypeName;
						}
					}
					httpRequest.setHeader(HttpHeaders.AUTHORIZATION, scheme+ " " + authContent);
				}

				httpRequest.setURI(new URI(subscriber.getUrl()));
				
				CloseableHttpResponse response = null;
				//if retry
				if (webHookIsRetry) {
					for(int i=0; i<webHookRetryMaximumAttpempts; i++) {
						try {
							response = webHookHttpClient.execute(httpRequest,httpContext);
							break;
						} catch (Exception e) {
							if (e.getClass() == InterruptedIOException.class 
									||e.getClass() == UnknownHostException.class 
									||e.getClass() == ConnectException.class
									||e.getClass() == SSLException.class)	{//リトライ不可のException
								logger.info("WebHook:"+e.getClass().getName()+" has occured. Stop retrying.");
								break;
							}else{
								try {
									Thread.sleep(webHookRetryInterval);
								} catch (InterruptedException exception) {
									exception.printStackTrace();
								}
							}
						}
					}
				} else {
					response = webHookHttpClient.execute(httpRequest,httpContext);
				}
				try {
					if (response != null) {
						WebHookResponse whr = generateWebHookResponse(response);
						webHook.getResultHandler().handleResponse(whr);
					}
				} finally {
					if (response!=null) {
						response.close();
					}
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
		    Mac Hmac = Mac.getInstance(this.webHookHmacHashAlgorithm);
		    SecretKeySpec secret_key = new SecretKeySpec(token.getBytes("UTF-8"), this.webHookHmacHashAlgorithm);
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

	private void initWebHookHttpClient() {
		if (webHookHttpClient == null) {
			try {
				webHookHttpClient = webHookHttpClientConfig.getInstance();
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
		ArrayList<WebHookHeader> headers = new ArrayList<WebHookHeader>();
		if (response.getAllHeaders()!=null) {
			for (Header hd : response.getAllHeaders()) {
				headers.add(new WebHookHeader(hd.getName(), hd.getValue()));
			}
		}
		whr.setHeaders(headers);
		return whr;
	}

	public WebHook generateWebHook(String webHookDefinitionName, Map<String, Object> binding, List<String> endPointDefName) {
		WebHookTemplateRuntime runtime = this.getRuntimeByName(webHookDefinitionName);
		if (runtime == null) {
			throw new SystemException("WebHookTemplate:" + webHookDefinitionName + " not found");
		}
		WebHook webHook = runtime.createWebHook(binding);
		webHook.setTemplateName(webHookDefinitionName);
		GroovyTemplate contentTemplate = runtime.getContentTemplate();
		GroovyTemplate urlQueryTemplate = runtime.getUrlQueryTemplate();

		binding.put("webhook", webHook);

		ArrayList<WebHookEndPoint> webHookEndPointList= new ArrayList<WebHookEndPoint>();
		for (String endPointDefinitionName: endPointDefName) {
			WebHookEndPoint webHookEndPoint = wepdm.generateEndPointInstance(endPointDefinitionName);
			GroovyTemplate urlTemp = wepdm.getUrlTemplateByName(endPointDefinitionName);
			if (contentTemplate != null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
				gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACTOKEN, webHookEndPoint.getHmac());
				if (webHookEndPoint.getHeaderAuthType()!=null) {
					if ("WHBA".equals(webHookEndPoint.getHeaderAuthType())) {
						String[] basicArray = null;
						if (webHookEndPoint.getHeaderAuthContent()!=null) {
							basicArray = webHookEndPoint.getHeaderAuthContent().replaceAll("\\s","").split(":");
						} 
						if (basicArray==null||basicArray.length<2) {
						} else {
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, basicArray[0]);
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, basicArray[1]);
						}
					} else if ("WHBT".equals(webHookEndPoint.getHeaderAuthType())){
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, webHookEndPoint.getHeaderAuthContent());	
					}
				}
				try {
					contentTemplate.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				webHookEndPoint.setPayloadContent(sw.toString());
			}

			//process hmac
			String hmacResult =null;
			if (webHookEndPoint.getHmac()!=null) {
				if (!webHookEndPoint.getHmac().isEmpty()) {
					hmacResult= getHmacSha256(webHookEndPoint.getHmac(), webHookEndPoint.getPayloadContent());
					webHookEndPoint.setHmacResult(hmacResult);
				}
			}

			String urlQuery = "";
			if (isUrlQueryAllowed(webHook.getHttpMethod())) {
				if (urlQueryTemplate !=null) {
					StringWriter sw = new StringWriter();
					GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
					gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACTOKEN, webHookEndPoint.getHmac());
					gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACRESULT, (webHookEndPoint.getHmacResult()==null||webHookEndPoint.getHmacResult().length()==0)?null:webHookEndPoint.getHmacResult());
					if (webHookEndPoint.getHeaderAuthType()!=null) {
						if ("WHBA".equals(webHookEndPoint.getHeaderAuthType())) {
							String[] basicArray = null;
							if (webHookEndPoint.getHeaderAuthContent()!=null) {
								basicArray = webHookEndPoint.getHeaderAuthContent().replaceAll("\\s","").split(":");
							}
							if (basicArray==null||basicArray.length<2) {
							} else {
								gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, basicArray[0]);
								gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, basicArray[1]);
							}
						} else if ("WHBT".equals(webHookEndPoint.getHeaderAuthType())){
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, webHookEndPoint.getHeaderAuthContent());
						}
					}
					try {
						urlQueryTemplate.doTemplate(gtb);
						urlQuery=sw.toString()==null?"":sw.toString();
					} catch (IOException e) {
						throw new ScriptRuntimeException(e);
					}
				}
			}
			if (urlTemp !=null) {
				StringWriter sw = new StringWriter();
				GroovyTemplateBinding gtb = new GroovyTemplateBinding(sw,binding);
				gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACTOKEN, webHookEndPoint.getHmac());
				gtb.setVariable(WEBHOOK_SECURITY_BINDING_HMACRESULT, (webHookEndPoint.getHmacResult()==null||webHookEndPoint.getHmacResult().length()==0)?null:webHookEndPoint.getHmacResult());
				if (webHookEndPoint.getHeaderAuthType()!=null) {
					if ("WHBA".equals(webHookEndPoint.getHeaderAuthType())) {
						String[] basicArray = null;
						if (webHookEndPoint.getHeaderAuthContent()!=null) {
							basicArray = webHookEndPoint.getHeaderAuthContent().replaceAll("\\s","").split(":");
						}
						if (basicArray==null||basicArray.length<2) {
						} else {
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICNAME, basicArray[0]);
							gtb.setVariable(WEBHOOK_SECURITY_BINDING_BASICPASSWORD, basicArray[1]);
						}
					} else if ("WHBT".equals(webHookEndPoint.getHeaderAuthType())){
						gtb.setVariable(WEBHOOK_SECURITY_BINDING_BEARER, webHookEndPoint.getHeaderAuthContent());
					}
				}
				try {
					urlTemp.doTemplate(gtb);
				} catch (IOException e) {
					throw new ScriptRuntimeException(e);
				}
				webHookEndPoint.setUrl((sw.toString()+urlQuery).replace("\n", "").replaceAll("\\s",""));//スベース、改行抜き。TODO: encodeする?
			}
			webHookEndPointList.add(webHookEndPoint);
			}
		webHook.setWebHookEndPointList(webHookEndPointList);
		return webHook;
	}
}
