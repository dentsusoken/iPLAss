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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataStatusCheckDialog;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataStatusCheckDialog.StatusCheckResultHandler;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataTreeDS.FIELD_NAME;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataTagSelectDialog.MetaDataTagSelectedHandler;
import org.iplass.adminconsole.client.tools.ui.metaexplorer.MetaDataTreeGridPane.AdvancedSearchExecHandler;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.NameListDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.TargetMode;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import com.smartgwt.client.widgets.tree.TreeNode;

public class MetaDataListPane extends VLayout {

	private static final String DELETE_ICON = "[SKINIMG]/MultiUploadItem/icon_remove_files.png";
	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String EXPORT_ICON = "[SKINIMG]/actions/download.png";
	private static final String IMPORT_ICON = "[SKINIMG]/SchemaViewer/operation.png";
	private static final String TAG_CREATE_ICON = "tag_blue_add.png";
	private static final String TAG_LIST_ICON = "tag_blue.png";
	private static final String STATUS_ICON = "tick.png";

	/** Repositoryの種類選択用Map */
	private static LinkedHashMap<String, String> repositoryTypeMap;
	static {
		repositoryTypeMap = new LinkedHashMap<String, String>();
		for (RepositoryType type : RepositoryType.values()) {
			repositoryTypeMap.put(type.typeName(), type.displayName());
		}
	}

	private MetaDataExplorerMainPane mainPane;

	private SelectItem repositoryType;

	private MetaDataTreeGridPane grid;

	/**
	 * コンストラクタ
	 *
	 */
	public MetaDataListPane(MetaDataExplorerMainPane mainPane) {
		this.mainPane = mainPane;

		// レイアウト設定
		setWidth100();
		setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		//------------------------
		//Delete
		//------------------------
		final ToolStripButton deleteButton = new ToolStripButton();
		deleteButton.setIcon(DELETE_ICON);
		deleteButton.setTitle("Delete");
		deleteButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_deleteSelectMetaData"));
		deleteButton.setHoverWrap(false);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteMetaData();
			}
		});
		toolStrip.addButton(deleteButton);

		toolStrip.addSeparator();

		//------------------------
		//Export / Import
		//------------------------

		ToolStripMenuButton portingButton = new ToolStripMenuButton();
		portingButton.setTitle("Export / Import");
		portingButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_portingMetaDataMenuTooltip"));
		portingButton.setHoverWrap(false);

		Menu portingMenu = new Menu();
		portingMenu.setShowShadow(true);
		portingMenu.setShadowDepth(3);

		final MenuItem exportXMLButton = new MenuItem("Export XML", EXPORT_ICON);
		exportXMLButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				exportConfig();
			}
		});

		final MenuItem importXMLButton = new MenuItem("Import XML", IMPORT_ICON);
		importXMLButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				importConfig();
			}
		});

		final MenuItem exportNameListButton = new MenuItem("Export Name List", EXPORT_ICON);
		exportNameListButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				exportNameList();
			}
		});

		portingMenu.setItems(exportXMLButton, importXMLButton, new MenuItemSeparator(), exportNameListButton);
		portingButton.setMenu(portingMenu);
		toolStrip.addMenuButton(portingButton);

		toolStrip.addSeparator();

		//------------------------
		//Tag Create / Import
		//------------------------

		ToolStripMenuButton tagButton = new ToolStripMenuButton();
		tagButton.setTitle("Tags");
		tagButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_saveMetaDataTagImport"));
		tagButton.setHoverWrap(false);

		Menu tagMenu = new Menu();
		tagMenu.setShowShadow(true);
		tagMenu.setShadowDepth(3);

		final MenuItem listTagButton = new MenuItem("Tag List (Import)", TAG_LIST_ICON);
		listTagButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				showTagList();
			}
		});

		final MenuItem createTagButton = new MenuItem("Create Tag", TAG_CREATE_ICON);
		createTagButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				tagCreate();
			}
		});

		tagMenu.setItems(listTagButton, createTagButton);
		tagButton.setMenu(tagMenu);
		toolStrip.addMenuButton(tagButton);

		toolStrip.addSeparator();

		//------------------------
		//Status Check
		//------------------------

		final ToolStripButton statusCheckButton = new ToolStripButton();
		statusCheckButton.setIcon(STATUS_ICON);
		statusCheckButton.setTitle("Status Check");
		statusCheckButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_verifyStatusMetaData"));
		statusCheckButton.setHoverWrap(false);
		statusCheckButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				statusCheck();
			}
		});
		toolStrip.addButton(statusCheckButton);

		toolStrip.addFill();

		//------------------------
		//Type
		//------------------------

		repositoryType = new SelectItem();
		repositoryType.setShowTitle(false);
		repositoryType.setWidth(200);
		repositoryType.setValueMap(repositoryTypeMap);
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
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_refreshListComment"));
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

		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record record = event.getRecord();
				if (record == null) {
					return;
				}

				TreeNode node = (TreeNode)record;
				if (grid.getTree().isFolder(node)) {
					//Folderは対象外
					return;
				}

				//表示
				String className = node.getAttribute(FIELD_NAME.CLASS_NAME.name());
				String defName = node.getAttribute(FIELD_NAME.NAME.name());

				ViewMetaDataEvent.fire(className, defName, AdminConsoleGlobalEventBus.getEventBus());
			}
		});

		addMember(toolStrip);
		addMember(grid);

		refreshGrid();
	}

	public void refreshGrid() {
		grid.refreshGrid(RepositoryType.valueOfTypeName(SmartGWTUtil.getStringValue(repositoryType)));
	}

	private void deleteMetaData() {

		//選択チェック
		if (!isSelected()) {
			return;
		}

		//ダイアログ表示
		MetaDataDeleteDialog dialog = new MetaDataDeleteDialog(this, grid.getSelectedPathArray());
		dialog.show();
	}

	private void exportConfig() {

		//選択チェック
		if (!isSelected()) {
			return;
		}

		//Sharedチェック
		if (grid.isSelectedShared()) {
			SC.ask(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_confirm"),
					AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_shareMetaDataInclud"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						showXMLDownloadDialog();
					}
				}
			});
		} else {
			showXMLDownloadDialog();
		}
	}

	private boolean isSelected() {
		if (!grid.isSelected()) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_selectMetaDataTarget"));
			return false;
		}
		return true;
	}

	private void showXMLDownloadDialog() {
		//ダイアログ表示
		MetaDataXMLDownloadDialog dialog = new MetaDataXMLDownloadDialog(
				grid.getSelectedPathArray(), repositoryType.getValueAsString());
		dialog.show();
	}

	private void importConfig() {
		MetaDataXMLUploadDialog dialog = new MetaDataXMLUploadDialog(this);
		dialog.show();
	}

	private void exportNameList() {

		//選択チェック
		if (!isSelected()) {
			return;
		}

		String[] paths = grid.getSelectedPathArray();
		String type = repositoryType.getValueAsString();

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + NameListDownloadProperty.ACTION_URL)
			.addParameter(NameListDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
			.addParameter(NameListDownloadProperty.TARGET_MODE, TargetMode.LIVE.name())
			.addParameter(NameListDownloadProperty.TARGET_PATH, pathArrayString(paths))
			.addParameter(NameListDownloadProperty.REPOSITORY_TYPE, type)
			.execute();

	}

	private String pathArrayString(String[] paths) {
		if (paths == null || paths.length == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (String path : paths) {
			builder.append(path + ",");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	private void tagCreate() {
		MetaDataTagCreateDialog dialog = new MetaDataTagCreateDialog();
		dialog.show();
	}

	private void showTagList() {
		MetaDataTagSelectDialog dialog = new MetaDataTagSelectDialog(new MetaDataTagSelectedHandler() {

			@Override
			public void selected(String fileOid, String name, Date createDate) {
				MetaDataImportDialog.showFullScreen(fileOid, MetaDataListPane.this);
			}
		}, AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataListPane_importTagLocalMetaData"));
		dialog.show();
	}

	private void statusCheck() {

		List<String> selectPaths = new ArrayList<String>();

		//trueを指定することでPathは全て選択されていないと含まれない
		ListGridRecord[] records = grid.getSelectedRecords(true);
		if (records == null || records.length == 0) {
			//全件対象
//			records = grid.getRecords();
			records = grid.getTree().getAllNodes();
		}

		for (ListGridRecord record : records) {
			String path = record.getAttribute(FIELD_NAME.PATH.name());
			//Rootは除外
			if (path == null || path.isEmpty()) {
				continue;
			}

			if (record instanceof TreeNode) {
				TreeNode node = (TreeNode)record;
				if (grid.getTree().isLeaf(node)) {
					selectPaths.add(path);
				}
			} else {
				GWT.log("not TreeNode instance:" + path);
			}
		}
		GWT.log("status check target size:" + selectPaths.size());
		MetaDataStatusCheckDialog dialog = new MetaDataStatusCheckDialog(selectPaths, new StatusCheckResultHandler() {

			@Override
			public void onError(LinkedHashMap<String, String> result) {

				//既にエラーが表示されている場合は削除
				if (mainPane.getMember(0) instanceof StatusErrorListPane) {
					mainPane.removeMember(mainPane.getMember(0));
				}
				mainPane.addMember(new StatusErrorListPane(mainPane, result), 0);

			}
		});
		dialog.show();
	}

}
