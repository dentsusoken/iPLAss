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

/**
 * WebHookを表すクラス、WebHookの送り情報などを記録する
 */
package org.iplass.mtp.webhook;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.iplass.mtp.webhook.template.definition.WebHookContent;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;

/**
 * @author lisf06
 */

public class WebHook  {
	
	/** このwebhookの名前 */
	private String name;

	/** 発信者の名前 */
	private String senderName;
	private String addressUrl;
	
	/** 記録用のid */
	private int webHookId;

	/** 送る内容 */
	private WebHookContent content;
	
	/** 送るメソッド */
	private String httpMethod;

	private ArrayList<WebHookSubscriber> subscribers;

	/** headers */
	private ArrayList<WebHookHeader> headers;
	private String tokenHeader;

	Map<String, Object> binding;
	
	/** 失敗したらやり直ししますか */
	private boolean retry;

	/** やり直しの最大回数 */
	private int retryLimit;
	
	/** やり直す度の待ち時間(ms) */
	private int retryInterval;
	
	/**　同期非同期　*/
	private boolean synchronous;
	
	/**
	 * 記録用
	 * */
	private String TemplateName;
	
	/**
	 * ｄｂ接続用
	 * */
	private String metaDataId;
	
	public String getMetaDataId() {
		return metaDataId;
	}

	public void setMetaDataId(String metaDataId) {
		this.metaDataId = metaDataId;
	}

	public String getTemplateName() {
		if (TemplateName == null) {
			return "";
		}
		return TemplateName;
	}

	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}

	public String getAddressUrl() {
		return addressUrl;
	}

	public void setAddressUrl(String addressUrl) {
		this.addressUrl = addressUrl;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public int getWebHookId() {
		return webHookId;
	}

	public void setWebHookId(int webHookId) {
		this.webHookId = webHookId;
	}

	public WebHookContent getContent() {
		return content;
	}

	public void setContent(WebHookContent content) {
		this.content = content;
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

	public ArrayList<WebHookHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<WebHookHeader> headers) {
		this.headers = headers;
	}

	public ArrayList<WebHookSubscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(ArrayList<WebHookSubscriber> subscriber) {
		this.subscribers = subscriber;
	}

	public void addSubscriber(WebHookSubscriber subscriber) {
		this.subscribers.add(subscriber);
	}
	public Map<String, Object> getBinding() {
		return binding;
	}

	public void setBinding(Map<String, Object> binding) {
		this.binding = binding;
	}
	
	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}
	
	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

}
