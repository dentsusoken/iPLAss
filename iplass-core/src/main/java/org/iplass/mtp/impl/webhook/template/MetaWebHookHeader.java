/**
 * 
 */
package org.iplass.mtp.impl.webhook.template;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.definition.WebHookHeader;

/**
 * @author lisf06
 *
 */
public class MetaWebHookHeader implements MetaData {

	private static final long serialVersionUID = 1113045625739189908L;
	
	private String key;
	private String Value;
	
	public MetaWebHookHeader() {
		
	}
	public MetaWebHookHeader(String key, String value) {
		this.key = key;
		this.Value = value;
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
	
	@Override
	public MetaWebHookHeader copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	//Definition → Meta
	public void applyConfig (WebHookHeader definition) {
		this.key = definition.getKey();
		this.Value = definition.getValue();
	}
	//Meta → Definition
	public WebHookHeader currentConfig() {
		WebHookHeader definition = new WebHookHeader();
		definition.setKey(this.key);
		definition.setValue(this.Value);
		return definition;
	}

}
