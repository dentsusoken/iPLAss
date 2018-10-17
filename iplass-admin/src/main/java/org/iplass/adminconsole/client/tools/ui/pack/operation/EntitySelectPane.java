/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.pack.operation;

import java.util.Arrays;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist.EntityTreeGrid;
import org.iplass.adminconsole.client.tools.ui.pack.CreateOperationPane;
import org.iplass.adminconsole.client.tools.ui.pack.PackageCreateDialog;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateInfo;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class EntitySelectPane extends VLayout implements CreateOperationPane {

	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	private CheckboxItem showCountItem;

	private EntityTreeGrid grid;

	public EntitySelectPane(PackageCreateDialog owner) {
		// レイアウト設定
		setWidth100();
		setHeight100();

		setOverflow(Overflow.VISIBLE);

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		//------------------------
		//Get Data Count
		//------------------------

		showCountItem = new CheckboxItem();
		showCountItem.setTitle("Get Data Count");
		showCountItem.setTooltip(SmartGWTUtil.getHoverString(
				AdminClientMessageUtil.getString("ui_tools_pack_operation_EntitySelectPane_dataNumOften")));
		showCountItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addFormItem(showCountItem);

		toolStrip.addFill();

		//------------------------
		//Expand / Contract
		//------------------------

		final ToolStripButton expandAllButton = new ToolStripButton();
		expandAllButton.setIcon(EXPAND_ICON);
		expandAllButton.setTitle("Expand");
		expandAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.expandAll();
			}
		});
		toolStrip.addButton(expandAllButton);

		final ToolStripButton contractAllButton = new ToolStripButton();
		contractAllButton.setIcon(CONTRACT_ICON);
		contractAllButton.setTitle("Collapse");
		contractAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.expandRoot();
			}
		});
		toolStrip.addButton(contractAllButton);

		//------------------------
		//Refresh
		//------------------------

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_pack_operation_EntitySelectPane_refreshList"));
		refreshButton.setHoverWrap(false);
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addButton(refreshButton);

		//------------------------
		//Grid
		//------------------------
		grid = new EntityTreeGrid();
		//Packageエンティティを除外
		grid.setDisabledPathList(Arrays.asList("/entity/mtp/maintenance/Package"));

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	public void refreshGrid() {
		grid.refreshGrid(SmartGWTUtil.getBooleanValue(showCountItem));
	}

	@Override
	public void applyTo(PackageCreateInfo info) {
		info.setEntityPaths(grid.getSelectedPathList());
	}

	@Override
	public void setCurrentCreateInfo(PackageCreateInfo info) {
		//特に利用しない
	}
}
