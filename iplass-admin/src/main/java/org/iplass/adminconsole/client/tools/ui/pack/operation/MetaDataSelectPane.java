/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataTreeGridPane;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataTreeGridPane.AdvancedSearchExecHandler;
import org.iplass.adminconsole.client.tools.ui.pack.CreateOperationPane;
import org.iplass.adminconsole.client.tools.ui.pack.PackageCreateDialog;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageCreateInfo;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class MetaDataSelectPane extends VLayout implements CreateOperationPane {

	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	private SelectItem repositoryType;
	private MetaDataTreeGridPane grid;

	/** Repositoryの種類選択用Map */
	private static LinkedHashMap<String, String> repositoryTypeMap;
	static {
		repositoryTypeMap = new LinkedHashMap<String, String>();
		for (RepositoryType type : RepositoryType.values()) {
			repositoryTypeMap.put(type.typeName(), type.displayName());
		}
	}

	public MetaDataSelectPane(PackageCreateDialog owner) {
		// レイアウト設定
		setWidth100();
		setHeight100();

		setOverflow(Overflow.VISIBLE);

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		toolStrip.addFill();

		//------------------------
		//Type
		//------------------------

		repositoryType = new SelectItem();
		repositoryType.setShowTitle(false);
		repositoryType.setWidth(200);
		repositoryType.setValueMap(repositoryTypeMap);
		//repositoryType.setDefaultValue(RepositoryType.All.name());
		repositoryType.setDefaultValue(RepositoryType.LOCAL.typeName());
		repositoryType.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				refreshGrid();
			}
		});
		toolStrip.addFormItem(repositoryType);

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
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_pack_operation_MetaDataSelectPane_refreshList")));
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
		grid = new MetaDataTreeGridPane(new AdvancedSearchExecHandler() {

			@Override
			public void doSearch() {
				refreshGrid();
			}
		});

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	public void refreshGrid() {
		grid.refreshGrid(RepositoryType.valueOfTypeName(SmartGWTUtil.getStringValue(repositoryType)));
	}

	@Override
	public void applyTo(PackageCreateInfo info) {
		info.setMetaDataPaths(grid.getSelectedPathList());
	}

	@Override
	public void setCurrentCreateInfo(PackageCreateInfo info) {
		//特に利用しない
	}
}
