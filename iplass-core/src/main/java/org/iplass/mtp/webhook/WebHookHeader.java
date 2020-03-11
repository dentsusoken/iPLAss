package org.iplass.mtp.webhook;

public class WebHookHeader {
	private String key;
	private String value;
	
	public WebHookHeader () {
		
	}
	public WebHookHeader(String key, String value) {
		this.key=key;
		this.value=value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
