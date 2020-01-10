package org.iplass.mtp.webhook.template.definition;

import java.io.Serializable;

/**
 * @author lisf06
 *
 */
public class WebHookHeader implements Serializable {
	
	private static final long serialVersionUID = 3511112860153636065L;
	
	private String key;
	private String value;
	private String id;//keyとして使う、metaには入らない

	public WebHookHeader () {
		
	}
	public WebHookHeader(String key, String value) {
		this.key=key;
		this.value=value;
	}
	public WebHookHeader(String key, String value,String id) {
		this.key=key;
		this.value=value;
		this.id=id;
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
