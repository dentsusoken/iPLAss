/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.tools.ui.openapisupport;

import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportSelectedConverter;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI(Swagger)Support ツールメイン領域
 * <p>
 * 画面を構成し、操作を実装します。
 * </p>
 *
 * <h3>画面構成</h3>
 * <pre>
 * OpenApiSupportMainPane(本クラス)
 *   +-- OpenApiSupportToolStripPane（ToolStrip 画面構成）
 *   |     +-- OpenApiSupportToolStrip (ToolStrip コンポーネント）
 *   +-- OpenApiSupportTreeGridPane（TreeGrid 画面構成）
 *         +-- OpenApiSupportTreeGrid (TreeGrid コンポーネント）
 * </pre>
 *
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportMainPane extends VLayout {
	/** ToolStrip 領域 */
	private OpenApiSupportToolStripPane toolStripPane;
	/** TreeGrid 領域 */
	private OpenApiSupportTreeGridPane treeGridPane;

	/**
	 * コンストラクタ
	 * @param plugin プラグインインスタンス
	 */
	public OpenApiSupportMainPane(OpenApiSupportPlugin plugin) {
		super();

		setWidth100();

		toolStripPane = new OpenApiSupportToolStripPane();
		toolStripPane.setImportClickHandler(this::onClickImport);
		toolStripPane.setExportClickHandler(this::onClickExport);
		toolStripPane.setExpandClickHandler(this::onClickExpand);
		toolStripPane.setCollapseClickHandler(this::onClickCollapse);
		toolStripPane.setRefreshClickHandler(this::onClickRefresh);

		treeGridPane = new OpenApiSupportTreeGridPane();

		addMember(toolStripPane);
		addMember(treeGridPane);

		refershGrid();
	}

	/**
	 * ToolStrip の Import ボタンがクリックされた時の処理
	 * @param event クリックイベント
	 */
	private void onClickImport(ClickEvent event) {
		var dialog = new OpenApiSupportImportDialog();
		dialog.setDialogCloseHandler(() -> {
			// インポート後にグリッドをリフレッシュ
			refershGrid();
		});
		dialog.show();
	}

	/**
	 * ToolStrip の Export ボタンがクリックされた時の処理
	 * @param event クリックイベント
	 */
	private void onClickExport(ClickEvent event) {
		var dialog = new OpenApiSupportExportDialog();
		dialog.setExportAction(this::doExecuteExport);
		dialog.show();
	}

	/**
	 * ToolStrip の Expand ボタンがクリックされた時の処理
	 * @param event クリックイベント
	 */
	private void onClickExpand(ClickEvent event) {
		treeGridPane.expandTree();
	}

	/**
	 * ToolStrip の Collapse ボタンがクリックされた時の処理
	 * @param event クリックイベント
	 */
	private void onClickCollapse(ClickEvent event) {
		treeGridPane.collapseTree();
	}

	/**
	 * ToolStrip の Refresh ボタンがクリックされた時の処理
	 * @param event クリックイベント
	 */
	private void onClickRefresh(ClickEvent event) {
		refershGrid();
	}

	/**
	 * ExportDialog の Export ボタンがクリックされた時の処理
	 * @param event クリックイベント
	 * @param version OpenAPIのバージョン
	 * @param fileType ファイルタイプ（JSON, YAML）
	 */
	private void doExecuteExport(ClickEvent event, String version, String fileType) {
		var selectData = treeGridPane.getSelectionData();
		var selectDataString = OpenApiSupportSelectedConverter.convertString(selectData);

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + OpenApiSupportRpcConstant.Export.SERVICE_NAME)
		.addParameter(ConfigDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
		.addParameter(OpenApiSupportRpcConstant.Export.Parameter.VERSION, version)
		.addParameter(OpenApiSupportRpcConstant.Export.Parameter.FILE_TYPE, fileType)
		.addParameter(OpenApiSupportRpcConstant.Export.Parameter.SELECT_VALUE, selectDataString)
		.execute();

	}

	/**
	 * グリッドリフレッシュ操作
	 */
	private void refershGrid() {
		// ボタン無効化
		toolStripPane.disable();

		treeGridPane.refresh(() -> {
			// ボタン有効化
			toolStripPane.enable();
			treeGridPane.collapseTree();
		});
	}

	@Override
	public void destroy() {
		toolStripPane = null;
		treeGridPane = null;

		super.destroy();
	}
}
