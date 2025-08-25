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

import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigDownloadProperty;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridSelected;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
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
		var selectData = treeGridPane.getSelectionData();
		if (selectData.isNotSelect()) {
			// 選択されていない場合は、エラーメッセージを表示し終了する
			SC.say(AdminClientMessageUtil.getString("ui_tools_openapisupport_OpenApiSupportMainPane_selectExportData"));
			return;
		}
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
		var selectDataString = convertString(selectData);

		PostDownloadFrame frame = new PostDownloadFrame();
		frame.setAction(GWT.getModuleBaseURL() + OpenApiSupportRpcConstant.Export.SERVICE_NAME)
		.addParameter(ConfigDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
		.addParameter(OpenApiSupportRpcConstant.Export.Parameter.VERSION, version)
		.addParameter(OpenApiSupportRpcConstant.Export.Parameter.FILE_TYPE, fileType)
		.addParameter(OpenApiSupportRpcConstant.Export.Parameter.SELECT_VALUE, selectDataString)
		.execute();

	}

	/**
	 * 画面で選択した WebAPI, Entity CRUD API の情報を文字列に変換します。
	 * <p>
	 * 変換した文字列は {@link org.iplass.adminconsole.server.tools.rpc.openapisupport.OpenApiSupportExportService} の #restoreDto で復元されます。
	 * </p>
	 * @param dto 画面で選択した WebAPI, Entity CRUD API の情報
	 * @return 変換された文字列
	 */
	public String convertString(OpenApiSupportTreeGridSelected dto) {
		StringBuilder converted = new StringBuilder();

		// WebAPI は、WebAPI メタデータ定義パスを連携します。区切り文字は、コロンです。
		// （例： path/to/webapi1:path/to/webapi2 ）
		// Entity CRUD API は、エンティティメタデータ定義パスと、許可権限を連携します。エンティティ単位の区切り文字はコロンです。
		// エンティティ定義パスと許可権限の区切り文字はカンマで、カンマ区切りの先頭には必ずエンティティ名が設定されます。
		// （例： path.to.entity1,LOAD:path.to.entity2,LOAD,INSERT,UPDATE ）
		//
		// WebAPI 文字列と、Entity CRUD API 文字列はパイプで区切ります。パイプの左辺は WebAPI、右辺は Entity CRUD API の情報です。
		// （例： path/to/webapi1:path/to/webapi2|path.to.entity1,LOAD:path.to.entity2,LOAD,INSERT,UPDATE ）

		// WebAPI 文字列の作成
		if (dto.isSelectWebApi()) {
			for (String webApi : dto.getWebApiList()) {
				converted.append(webApi).append(":");
			}
			converted.deleteCharAt(converted.length() - 1); // 最後のコロンを削除
		}

		converted.append("|");

		// Entity CRUD API 文字列の作成
		if (dto.isSelectEntityCRUDApi()) {
			for (Map.Entry<String, List<String>> entityCRUDApi : dto.getEntityCRUDApiMap().entrySet()) {
				var definitionName = entityCRUDApi.getKey();
				var authList = entityCRUDApi.getValue();
				// 先頭にエンティティ名を設定
				converted.append(definitionName).append(",");
				// 以降に許可された権限を追加
				converted.append(String.join(",", authList));
				converted.append(":");
			}
			converted.deleteCharAt(converted.length() - 1); // 最後のコロンを削除
		}

		return converted.toString();
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
