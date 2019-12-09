
package org.iplass.mtp.webhook;

import java.util.Map;

import org.iplass.mtp.Manager;

/**
 * @author lisf06
 *
 */
public interface WebHookManager extends Manager {
	
	/** 送る */
	public void sendWebHook(String webHookDefinitionName, Map<String, Object> parameters); 
}
