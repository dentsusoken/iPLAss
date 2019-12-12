package org.iplass.mtp.webhook.template.definition;

import java.io.Serializable;

/**
 * @author lisf06
 *
 */
public class WebHookHeader implements Serializable {
	
	private static final long serialVersionUID = 3511112860153636065L;
	
	private String key;
	private String Value;
	public WebHookHeader () {
		
	}
	public WebHookHeader(String key, String value) {
		this.key=key;
		this.Value=value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	
	

}
