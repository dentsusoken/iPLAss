package org.iplass.mtp.impl.webhook;

import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.mail.template.MetaMailTemplate;
import org.iplass.mtp.impl.mail.template.MetaMailTemplate.MailTemplateRuntime;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate;
import org.iplass.mtp.impl.webhook.template.MetaWebHookTemplate.WebHookTemplateRuntime;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.WebHook;

public interface WebHookService extends TypedMetaDataService<MetaWebHookTemplate, WebHookTemplateRuntime>{

	WebHook createWebHook(Tenant tenant, String charset);
	
	void sendWebHook(Tenant tenant, WebHook webHook);
}
