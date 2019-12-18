
package org.iplass.mtp.webhook;

import java.util.Map;

import org.iplass.mtp.Manager;

/**
 * @author lisf06
 *
 */
public interface WebHookManager extends Manager {
	
	WebHook createWebHook(String webHookDefinitionName, Map<String, Object> binding);
	
	public void sendWebHook(WebHook webHook);
	
	/** 送る */
	public void sendWebHook(String webHookDefinitionName, Map<String, Object> parameters); 
}
