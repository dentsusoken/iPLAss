/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth.server;

import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;


public class OAuthResourceServerPlugin extends DefaultMetaDataPlugin {

	/** カテゴリ名 */
	private static final String CATEGORY_NAME = MetaDataConstants.META_CATEGORY_SECURITY + "/OAuth";

	/** ノード名 */
	private static final String NODE_NAME = "OAuthResourceServer";

	/** ノード表示名 */
	private static final String NODE_DISPLAY_NAME = "OAuthResourceServer";

	/** ノードアイコン */
	private static final String NODE_ICON = "server_key.png";

	@Override
	public String getCategoryName() {
		return CATEGORY_NAME;
	}

	@Override
	protected String nodeName() {
		return NODE_NAME;
	}

	@Override
	protected String nodeDisplayName() {
		return NODE_DISPLAY_NAME;
	}

	@Override
	protected String nodeIcon() {
		return NODE_ICON;
	}

	@Override
	protected String definitionClassName() {
		return OAuthResourceServerDefinition.class.getName();
	}

	@Override
	protected void itemCreateAction(String folderPath) {
//		CreateAuthenticationPolicyDialog dialog = new CreateAuthenticationPolicyDialog(definitionClassName(), nodeDisplayName(), folderPath, false);
//		dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
//		dialog.addDataChangeHandler(new DataChangedHandler() {
//
//			@Override
//			public void onDataChanged(DataChangedEvent event) {
//				refreshWithSelect(event.getValueName(), null);
//			}
//		});
//		dialog.show();
	}

	@Override
	protected void itemCopyAction(MetaDataItemMenuTreeNode itemNode) {
//		CreateAuthenticationPolicyDialog dialog = new CreateAuthenticationPolicyDialog(definitionClassName(), nodeDisplayName(), "", true);
//		dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
//		dialog.addDataChangeHandler(new DataChangedHandler() {
//			@Override
//			public void onDataChanged(DataChangedEvent event) {
//				refreshWithSelect(event.getValueName(), null);
//			}
//		});
//		dialog.show();
//		dialog.setSourceName(itemNode.getDefName());
//		dialog.show();
	}

	@Override
	protected void itemDelete(final MetaDataItemMenuTreeNode itemNode) {
//		MetaDataServiceAsync service = MetaDataServiceFactory.get();
//		service.deleteDefinition(TenantInfoHolder.getId(), AuthenticationPolicyDefinition.class.getName(), itemNode.getDefName(), new AsyncCallback<AdminDefinitionModifyResult>() {
//			public void onFailure(Throwable caught) {
//				// 失敗時
//				SC.warn(AdminClientMessageUtil.getString("ui_metadata_auth_AuthenticationPolicyPluginManager_failedToDeleteAuthenticationPolicy") + caught.getMessage());
//			}
//			public void onSuccess(AdminDefinitionModifyResult result) {
//				if (result.isSuccess()) {
//					SC.say(AdminClientMessageUtil.getString("ui_metadata_auth_AuthenticationPolicyPluginManager_completion"),
//							AdminClientMessageUtil.getString("ui_metadata_auth_AuthenticationPolicyPluginManager_deleteAuthenticationPolicyComp"));
//
//					refresh();
//					removeTab(itemNode);
//				} else {
//					SC.warn(AdminClientMessageUtil.getString("ui_metadata_auth_AuthenticationPolicyPluginManager_failedToDeleteAuthenticationPolicy") + result.getMessage());
//				}
//			}
//		});
	}

	@Override
	protected MetaDataMainEditPane workSpaceContents(MetaDataItemMenuTreeNode itemNode) {
//		return new AuthenticationPolicyEditPane(itemNode, this);
		return null;
	}

	@Override
	protected Class<?>[] workspaceContentsPaneClass() {
		return new Class[]{OAuthResourceServerDefinition.class};
	}
}
