package org.iplass.mtp.impl.webhook.template;

import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.webhook.WebHookService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinitionManager;

public class WebHookTemplateDefinitionManagerImpl extends AbstractTypedDefinitionManager<WebHookTemplateDefinition> implements
WebHookTemplateDefinitionManager{

	private WebHookService service;
	
	public WebHookTemplateDefinitionManagerImpl() {
		this.service = ServiceRegistry.getRegistry().getService(WebHookService.class);
	}

	@Override
	public Class<WebHookTemplateDefinition> getDefinitionType() {
		return WebHookTemplateDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(WebHookTemplateDefinition definition) {
		return new MetaWebHookTemplate();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
