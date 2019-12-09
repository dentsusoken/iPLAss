/**
 * 
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
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

/**
 * @author lisf06
 *
 */
public class WebHookTemplatePlugin extends DefaultMetaDataPlugin {
	
	/** カテゴリ名 */
	private static final String CATEGORY_NAME = MetaDataConstants.META_CATEGORY_NOTIFICATION;

	/** ノード名 */
	private static final String NODE_NAME = "WebHookTemplate";

	/** ノードアイコン */
	private static final String NODE_ICON = "";//webhook.png?

	@Override
	protected String nodeName() {
		return NODE_NAME ;
	}

	@Override
	protected String nodeDisplayName() {
		// TODO Auto-generated method stub, add this message to util
		return AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookTemplatePluginManager_WebHookTemplate");
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
		return WebHookTemplateDefinition.class.getName();
	}

	@Override
	protected void itemCreateAction(String folderPath) {
		CreateWebHookTemplateDialog dialog = new CreateWebHookTemplateDialog(definitionClassName(), nodeDisplayName(), folderPath, false);
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
		CreateWebHookTemplateDialog dialog = new CreateWebHookTemplateDialog(definitionClassName(), nodeDisplayName(), "", false);
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
		service.deleteDefinition(TenantInfoHolder.getId(), WebHookTemplateDefinition.class.getName(), itemNode.getDefName(), new AsyncCallback<AdminDefinitionModifyResult>() {
			public void onFailure(Throwable caught) {//TODO add this message
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_mail_WebHookTemplatePluginManager_failedToDeleteWebHookTemplate" + caught.getMessage()));
			}
			public void onSuccess(AdminDefinitionModifyResult result) {
				if (result.isSuccess()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_mail_WebHookTemplatePluginManager_completion"),
							AdminClientMessageUtil.getString("ui_metadata_mail_WebHookTemplatePluginManager_deleteWebHookTemplateComp"));
					refresh();
					removeTab(itemNode);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_mail_WebHookTemplatePluginManager_failedToDeleteWebHookTemplate" + result.getMessage()));
				}
			}
		});
	}

	@Override
	protected MetaDataMainEditPane workSpaceContents(MetaDataItemMenuTreeNode itemNode) {
		return new WebHookTemplateEditPane(itemNode, this);
	}

	@Override
	protected Class<?>[] workspaceContentsPaneClass() {
		return new Class[] {WebHookTemplateEditPane.class};
	}

}
