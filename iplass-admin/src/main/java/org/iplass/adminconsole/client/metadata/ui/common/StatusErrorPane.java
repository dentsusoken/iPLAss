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
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * MetaDataステータスエラー通知パネル
 */
public class StatusErrorPane extends HLayout {

	//Owner
	private Layout workspace;

	/**
	 * コンストラクタ
	 */
	public StatusErrorPane(Layout workspace, String message) {
		this.workspace = workspace;

		//レイアウト設定
		setWidth100();
		setAutoHeight();
		setMargin(6);
		setMembersMargin(5);
		setAlign(Alignment.LEFT);
		setOverflow(Overflow.VISIBLE);

		addMember(new StatusErrorWindow(message));
	}

	private class StatusErrorWindow extends AbstractWindow {

		public StatusErrorWindow(String message) {
			setWidth100();
			setTitle("Status Check Error");
			setHeaderIcon("exclamation.png");
			setHeight(100);
			setCanDragReposition(false);
			setCanDragResize(true);

			addItem(new StatusErrorMessagePane(message));
		}

		@Override
		protected boolean onPreDestroy() {
			workspace.removeMember(StatusErrorPane.this);
			return true;
		}
	}

	private class StatusErrorMessagePane extends VLayout {

		public StatusErrorMessagePane(String message) {
			setWidth100();
			setHeight100();
			setPadding(10);
//			setBackgroundColor("#CCFFFF");

			Label title = new Label(AdminClientMessageUtil.getString("ui_metadata_common_StatusErrorPane_errComment"));
			title.setHeight(20);
			title.setWidth100();


			Canvas msgContents = new Canvas();
			msgContents.setHeight("*");
			msgContents.setWidth100();
			msgContents.setOverflow(Overflow.VISIBLE);
			msgContents.setCanSelectText(true);
			msgContents.setAlign(Alignment.LEFT);
			msgContents.setContents("<font color=\"red\">" + message + "</font>");

			addMember(title);
			addMember(msgContents);
		}
	}

}
