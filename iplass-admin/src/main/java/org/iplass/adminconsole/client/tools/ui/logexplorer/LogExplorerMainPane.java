/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.logexplorer;

import org.iplass.adminconsole.client.base.plugin.ContentClosedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentStateChangeHandler;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * LogViewer
 */
public class LogExplorerMainPane extends VLayout implements ContentStateChangeHandler {

	/**
	 * コンストラクタ
	 */
	public LogExplorerMainPane() {

		//レイアウト設定
		setWidth100();

		HLayout layout = new HLayout();
		layout.setWidth100();

		LogExplorerFileListPane listPane = new LogExplorerFileListPane();
		listPane.setWidth("50%");
		listPane.setShowResizeBar(true);	//リサイズ可能
		listPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、下を収縮

		LogExplorerConditionPane conditionPane = new LogExplorerConditionPane();
		conditionPane.setWidth("50%");

		layout.addMember(listPane);
		layout.addMember(conditionPane);

		addMember(layout);

	}

	@Override
	public void onContentClosed(ContentClosedEvent event) {
	}

	@Override
	public void onContentSelected(ContentSelectedEvent event) {
	}

}
