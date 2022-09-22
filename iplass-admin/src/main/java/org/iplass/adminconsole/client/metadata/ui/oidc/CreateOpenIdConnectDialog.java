/*
 * Copyright 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.adminconsole.client.metadata.ui.oidc;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;

/**
 * OpenIDConnect新規作成ダイアログ
 */
public class CreateOpenIdConnectDialog extends MetaDataCreateDialog {

	public CreateOpenIdConnectDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {

		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {

			@Override
			public Void call() {
				createDefinition(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	/**
	 * OpenIDConnectを登録します。
	 */
	private void createDefinition(SaveInfo saveInfo, boolean isCopyMode) {

		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(),
					saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			OpenIdConnectDefinition definition = new OpenIdConnectDefinition();

			definition.setName(saveInfo.getName());
			definition.setDisplayName(saveInfo.getDisplayName());
			definition.setDescription(saveInfo.getDescription());

			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}
}
