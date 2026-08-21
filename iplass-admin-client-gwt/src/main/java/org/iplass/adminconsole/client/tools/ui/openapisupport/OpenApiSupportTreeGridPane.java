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

import org.iplass.adminconsole.client.tools.data.openapisupport.OpenApiSupportTreeGridDS;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridSelected;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI(Swagger)Support 用の TreeGrid 領域
 *
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportTreeGridPane extends VLayout {
	/** ツリーグリッド */
	private OpenApiSupportTreeGrid grid;

	/**
	 * コンストラクタ
	 * <p>
	 * 画面を構成します。
	 * </p>
	 */
	public OpenApiSupportTreeGridPane() {
		super();
		setWidth100();
		setHeight100();

		grid = new OpenApiSupportTreeGrid();
		grid.setWidth100();
		grid.setHeight100();
		grid.enableSelectionTree();

		grid.addField(OpenApiSupportTreeGridDS.AttributeName.PATH.name(), "Path", 400);

		addMember(grid);
	}

	/**
	 * 選択済みのデータ一覧を取得します。
	 * @return 選択済みのデータ一覧
	 */
	public OpenApiSupportTreeGridSelected getSelectionData() {
		var result = new OpenApiSupportTreeGridSelected();

		// グリッドから選択済みのデータ一覧を取得（フォルダ情報も含む）
		var selectedRecords = grid.getSelectedRecords();
		if (null == selectedRecords || 0 == selectedRecords.length) {
			// 選択データ無しの場合は、空インスタンスを返却
			return result;
		}

		for (var record : selectedRecords) {
			boolean isFolder = record.getAttributeAsBoolean(OpenApiSupportTreeGridDS.AttributeName.IS_FOLDER.name());
			if (isFolder) {
				// フォルダは除外する
				continue;
			}

			String rootNode = record.getAttribute(OpenApiSupportTreeGridDS.AttributeName.ROOT_NODE.name());
			String definitionName = record.getAttribute(OpenApiSupportTreeGridDS.AttributeName.DEFINITION_NAME.name());

			if (OpenApiSupportRpcConstant.Service.RootNode.WEB_API.equals(rootNode)) {
				// WebAPI：選択されたノード情報を追加する

				result.addWebApi(definitionName);

			} else if (OpenApiSupportRpcConstant.Service.RootNode.ENTITY_CRUD_API.equals(rootNode)) {
				// Entity CRUD API：エンティティに対して、許可された操作を追加していく。

				String name = record.getAttribute(OpenApiSupportTreeGridDS.AttributeName.PATH.name());
				result.addEntityCRUDApi(definitionName, name);

			} else {
				// 未知のノード
				continue;
			}

		}

		return result;

	}

	/**
	 * ツリーを展開します。
	 */
	public void expandTree() {
		grid.expandTree();
	}

	/**
	 * ツリーを折りたたみます。
	 */
	public void collapseTree() {
		grid.collapseTree();
	}

	/**
	 * 画面に表示されているデータをリフレッシュします。
	 * @param callback リフレッシュ完了後処理
	 */
	public void refresh(Runnable callback) {
		OpenApiSupportTreeGridDS instance = OpenApiSupportTreeGridDS.getInstance();
		instance.setCompleteFetchCallback(callback);
		grid.refresh(instance);
	}

	@Override
	public void destroy() {
		grid = null;
		super.destroy();
	}
}
