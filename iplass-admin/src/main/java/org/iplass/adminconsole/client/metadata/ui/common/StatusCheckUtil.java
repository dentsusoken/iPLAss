/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.Layout;

public final class StatusCheckUtil {

	private StatusCheckUtil() {
	}

	/**
	 * <p>MetaDataのステータスチェックを行います。</p>
	 * <p>エラー発生時はエラーパネルを表示します。</p>
	 *
	 * @param className メタデータクラス名
	 * @param defName    定義名
	 * @param workspace  エラーパネルを表示するターゲットパネル
	 */
	public static void statuCheck(final String className, final String defName, final Layout workspace) {

		//既にエラーが表示されている場合は削除
		if (workspace.getMember(0) instanceof StatusErrorPane) {
			workspace.removeMember(workspace.getMember(0));
		}

		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.checkStatus(TenantInfoHolder.getId(), className, defName, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				if (result != null) {
					workspace.addMember(new StatusErrorPane(workspace, result), 0);
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_common_StatusCheckUtil_failed"),
						AdminClientMessageUtil.getString("ui_metadata_common_StatusCheckUtil_failedToCheckStatusMetaData"));

				GWT.log(caught.toString(), caught);
			}
		});

	}
}
