package org.iplass.mtp.impl.webhook;

import java.util.ArrayList;

import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.WebHook;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;

public interface WebHookService extends TypedMetaDataService<MetaWebHookTemplate, WebHookTemplateRuntime>{

	WebHook createWebHook(Tenant tenant, String charset);
	
	void sendWebHook(Tenant tenant, WebHook webHook);
	
	public WebHookTemplateDefinition fillSubscriberListByDef(WebHookTemplateDefinition definition);
	public WebHookTemplateDefinition updateSubscriberListByDef(WebHookTemplateDefinition definition);
}