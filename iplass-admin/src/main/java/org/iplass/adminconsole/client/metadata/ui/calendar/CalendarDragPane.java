/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.calendar;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.common.DefaultMetaDataTreeGrid;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * カレンダーDragパネル
 *
 * 登録されているDrag可能なEntityを表示します。
 */

public class CalendarDragPane extends VLayout {

	private CalendarTreeGrid grid;

	/**
	 * コンストラクタ
	 */
	public CalendarDragPane() {
		setWidth("35%");

		SectionStack menuItemHeaderSection = new SectionStack();

		SectionStackSection menuItemSection = new SectionStackSection("Entity Items");
		menuItemSection.setExpanded(true);
		menuItemSection.setCanCollapse(false);	//CLOSE不可

		//Refreshボタン
		ImgButton refreshButton = new ImgButton();
		refreshButton.setSrc("refresh.png");
		refreshButton.setSize(16);
		refreshButton.setShowFocused(false);
		refreshButton.setShowRollOver(false);
		refreshButton.setShowDown(false);
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarDragPane_refreshItemList"));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		});
		menuItemSection.setControls(refreshButton);
		menuItemHeaderSection.addSection(menuItemSection);

		grid = new CalendarTreeGrid();
		grid.setEmptyMessage("no entity item");
		menuItemSection.addItem(grid);
		addMember(menuItemHeaderSection);
	}

	/**
	 * 画面を再表示します。
	 */
	private void refresh() {
		grid.refresh();
	}

	private class CalendarTreeGrid extends DefaultMetaDataTreeGrid {

		@Override
		protected String definitionClassName() {
			return EntityDefinition.class.getName();
		}

		@Override
		protected boolean isVisibleItem(MetaTreeNode item) {
			if (item.getName().equals(EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME)) {
				return false;
			}
			return true;
		}

		void refresh() {
			initializeData();
		}

	}
}
