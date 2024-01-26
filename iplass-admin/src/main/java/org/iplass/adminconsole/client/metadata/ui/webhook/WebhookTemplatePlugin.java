/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
 * 
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.adminconsole.client.metadata.ui.webhook;
import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.webhook.template.definition.WebhookTemplateDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

/**
 * @author lisf06
 *
 */
public class WebhookTemplatePlugin extends DefaultMetaDataPlugin {
	
	/** カテゴリ名 */
	private static final String CATEGORY_NAME = MetaDataConstants.META_CATEGORY_NOTIFICATION +"/" + "Webhook";

	/** ノード名 */
	private static final String NODE_NAME = "WebhookTemplate";

	/** ノードアイコン */
	private static final String NODE_ICON = "feed_edit.png";

	@Override
	protected String nodeName() {
		return NODE_NAME ;
	}

	@Override
	protected String nodeDisplayName() {
		return AdminClientMessageUtil.getString("ui_metadata_webhook_WebhookTemplatePluginManager_WebhookTemplate");
	}

	public String getCategoryName() {
		return CATEGORY_NAME;
	}
	@Override
	protected String nodeIcon() {
		return NODE_ICON;
	}

	@Override
	protected String definitionClassName() {
		return WebhookTemplateDefinition.class.getName();
	}

	@Override
	protected void itemCreateAction(String folderPath) {
		CreateWebhookTemplateDialog dialog = new CreateWebhookTemplateDialog(definitionClassName(), nodeDisplayName(), folderPath, false);
		dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
		dialog.addDataChangeHandler(new DataChangedHandler() {
			@Override
			public void onDataChanged(DataChangedEvent event) {
				refreshWithSelect(event.getValueName(), null);
			}
		});
		dialog.show();
	}

	@Override
	protected void itemCopyAction(MetaDataItemMenuTreeNode itemNode) {
		CreateWebhookTemplateDialog dialog = new CreateWebhookTemplateDialog(definitionClassName(), nodeDisplayName(), "", true);
		dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
		dialog.addDataChangeHandler(new DataChangedHandler() {
			@Override
			public void onDataChanged(DataChangedEvent event) {
				refreshWithSelect(event.getValueName(), null);
			}
		});
		dialog.setSourceName(itemNode.getDefName());
		dialog.show();
	}

	@Override
	protected void itemDelete(final MetaDataItemMenuTreeNode itemNode) {
		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.deleteDefinition(TenantInfoHolder.getId(), WebhookTemplateDefinition.class.getName(), itemNode.getDefName(), new AsyncCallback<AdminDefinitionModifyResult>() {
			public void onFailure(Throwable caught) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_mail_WebhookTemplatePluginManager_failedToDeleteWebhookTemplate" + caught.getMessage()));
			}
			public void onSuccess(AdminDefinitionModifyResult result) {
				if (result.isSuccess()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_mail_WebhookTemplatePluginManager_completion"),
							AdminClientMessageUtil.getString("ui_metadata_mail_WebhookTemplatePluginManager_deleteWebhookTemplateComp"));
					refresh();
					removeTab(itemNode);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_mail_WebhookTemplatePluginManager_failedToDeleteWebhookTemplate" + result.getMessage()));
				}
			}
		});
	}

	@Override
	protected MetaDataMainEditPane workSpaceContents(MetaDataItemMenuTreeNode itemNode) {
		return new WebhookTemplateEditPane(itemNode, this);
	}

	@Override
	protected Class<?>[] workspaceContentsPaneClass() {
		return new Class[] {WebhookTemplateEditPane.class};
	}

}
