package org.iplass.adminconsole.client.metadata.ui.webhook;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;

public class CreateWebHookTemplateDialog extends MetaDataCreateDialog {

	public CreateWebHookTemplateDialog(String definitionClassName, String nodeDisplayName, String folderPath,
			boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		
		checkExist(saveInfo.getName(), new Callable<Void>() {
			@Override
			public Void call() {
				createWebHookTemplate(saveInfo, isCopyMode);
				return null;
			}
		});
	}
	
	
	private void createWebHookTemplate(SaveInfo saveInfo, boolean isCopyMode) {
		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(), saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			WebHookTemplateDefinition definition = new WebHookTemplateDefinition();
			
			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());
			
			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}

}
