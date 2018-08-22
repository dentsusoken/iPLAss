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

package org.iplass.adminconsole.client.tools.ui.langexplorer;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.tools.data.metaexplorer.MetaDataTreeDS.FIELD_NAME;
import org.iplass.adminconsole.client.tools.ui.langexplorer.LangTreeGridPane.AdvancedSearchExecHandler;
import org.iplass.adminconsole.shared.tools.dto.langexplorer.OutputMode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;

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
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import com.smartgwt.client.widgets.tree.TreeNode;

public class LangListPane extends VLayout {

	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	private static final String EXPORT_ICON = "[SKINIMG]/actions/download.png";
	private static final String IMPORT_ICON = "[SKINIMG]/SchemaViewer/operation.png";

	/** Repositoryの種類選択用Map */
	private static LinkedHashMap<String, String> repositoryTypeMap;
	static {
		repositoryTypeMap = new LinkedHashMap<String, String>();
		for (RepositoryType type : RepositoryType.values()) {
			repositoryTypeMap.put(type.typeName(), type.displayName());
		}
	}

	private SelectItem repositoryType;

	private LangTreeGridPane grid;
	private LangEditListPane langEditListPane;

	private LangExplorerSettingController controller = GWT.create(LangExplorerSettingController.class);

	/**
	 * コンストラクタ
	 *
	 */
	public LangListPane() {
		// レイアウト設定
		setWidth100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		//------------------------
		//Export / Import
		//------------------------

		ToolStripMenuButton portingButton = new ToolStripMenuButton();
		portingButton.setTitle("Export / Import");
		portingButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangListPane_portingMetaDataMenuTooltip"));
		portingButton.setHoverWrap(false);

		Menu portingMenu = new Menu();
		portingMenu.setShowShadow(true);
		portingMenu.setShadowDepth(3);

		final MenuItem exportXMLButton = new MenuItem("Export CSV", EXPORT_ICON);
		exportXMLButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				exportCsv();
			}
		});

		final MenuItem importXMLButton = new MenuItem("Import CSV", IMPORT_ICON);
		importXMLButton.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				importCsv();
			}
		});

		portingMenu.setItems(exportXMLButton, importXMLButton);
		portingButton.setMenu(portingMenu);
		toolStrip.addMenuButton(portingButton);

		toolStrip.addSeparator();

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
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangListPane_refreshListComment"));
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
		grid = new LangTreeGridPane(new AdvancedSearchExecHandler() {

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

				TreeNode node = (TreeNode) record;
				if (grid.getTree().isFolder(node)) {
					if (grid.getTree().isOpen(node)) {
						grid.getTree().closeFolder(node);
					} else {
						grid.getTree().openFolder(node);
					}
					return;
				}

				//表示
				String path = node.getAttribute(FIELD_NAME.PATH.name());
				String defName = node.getAttribute(FIELD_NAME.NAME.name());
				String defClassName = node.getAttribute(FIELD_NAME.CLASS_NAME.name());

				// ここで下部に多言語情報を表示
				controller.displayMultiLangInfo(langEditListPane, defClassName, defName, path);
			}
		});

		langEditListPane = new LangEditListPane();

		addMember(toolStrip);
		addMember(grid);
		addMember(langEditListPane);

		refreshGrid();
	}

	public void refreshGrid() {
		grid.refreshGrid(RepositoryType.valueOfTypeName(SmartGWTUtil.getStringValue(repositoryType)));
	}

	private void exportCsv() {

		//選択チェック
		if (!isSelected()) {
			return;
		}

		//Sharedチェック
		if (grid.isSelectedShared()) {
			SC.ask(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangListPane_confirm"),
					AdminClientMessageUtil.getString("ui_tools_langexplorer_LangListPane_shareMetaDataInclud"), new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (value) {
								doDownloadCsv();
							}
						}
					});
		} else {
			doDownloadCsv();
		}
	}

	private boolean isSelected() {
		if (!grid.isSelected()) {
			SC.say(AdminClientMessageUtil.getString("ui_tools_langexplorer_LangListPane_selectMetaDataTarget"));
			return false;
		}
		return true;
	}

	private void doDownloadCsv() {
		String[] paths = grid.getSelectedPathArray();
		controller.exportMultiLangInfo(paths, repositoryType.getValueAsString());
	}

	private void importCsv() {
		LangCsvUploadDialog dialog = new LangCsvUploadDialog(OutputMode.MULTI, null, null);
		dialog.addDialogCloseHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				//gridが表示されてたらリフレッシュ
				if (langEditListPane.isShowGrid()) {
					//pathとdefinitionNameはgrid表示時に設定してるはず
					controller.displayMultiLangInfo(langEditListPane, langEditListPane.getDefinitionClassName(), langEditListPane.getDefinitionName(), langEditListPane.getPath());
				}
			}
		});
		dialog.show();
	}

}
