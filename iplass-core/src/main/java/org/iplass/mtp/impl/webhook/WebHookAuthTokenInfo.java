package org.iplass.mtp.impl.webhook;

import org.iplass.mtp.auth.token.AuthTokenInfo;

public class WebHookAuthTokenInfo implements AuthTokenInfo {
	private String type;
	private String key;
	public WebHookAuthTokenInfo(){
		
	}
	public WebHookAuthTokenInfo(String type, String key){
		this.type=type;
		this.key=key;
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
	
	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("The ");
		sb.append(type);
		sb.append(" Token for WebHookTemplate:");
		sb.append(key);
		return sb.toString();
	}

}
