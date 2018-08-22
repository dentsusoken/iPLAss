/*
 * Copyright 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */
package org.iplass.mtp.auth.login.token;

import org.iplass.mtp.auth.token.AuthTokenInfo;

public class SimpleAuthTokenInfo implements AuthTokenInfo {
	
	private String type;
	private String key;
	private String application;
	
	public SimpleAuthTokenInfo() {
	}

	public SimpleAuthTokenInfo(String type, String application) {
		this.type = type;
		this.application = application;
	}
	
	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	
	@Override
	public String getDescription() {
		//TODO 多言語化
		StringBuilder sb = new StringBuilder();
		sb.append("Simple Persistant Auth Token for ");
		sb.append(application);
		return sb.toString();
	}

}
