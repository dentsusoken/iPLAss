/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpListGrid;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.PermissionListGridDS;
import org.iplass.adminconsole.client.tools.data.permissionexplorer.PermissionListGridDS.PermissionListGridRecord;

import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public abstract class PermissionListGrid extends MtpListGrid implements PermissionGrid {

	private PermissionListGridDS ds;

	/**
	 * <p>全Permission削除処理</p>
	 *
	 * @param record 対象Record
	 * @param rowNum 行番号
	 * @param defName 対象Definition名
	 */
	protected abstract void removeAllDefinitionPermission(PermissionListGridRecord record, int rowNum, String defName);

	/**
	 * <p>ロールPermission削除処理</p>
	 *
	 * @param record 対象Record
	 * @param rowNum 行番号
	 * @param colNum 列番号
	 * @param defName 対象Definition名
	 * @param roleCode 対象ロールコード
	 * @param roleIndex 対象ロールIndex
	 */
	protected abstract void removeRolePermission(PermissionListGridRecord record, int rowNum, int colNum, String defName, String roleCode, int roleIndex);

	/**
	 * <p>Permission編集ダイアログ表示処理</p>
	 *
	 * <p>ダイアログを開く際に呼び出されます。
	 * ダイアログに対してPermissionEditPaneを設定し、showしてください。<p>
	 *
	 * @param record 対象Record
	 * @param defName 対象Definition名
	 * @param roleCode 対象ロールコード
	 * @param roleIndex 対象ロールIndex
	 * @param dialog ダイアログ
	 */
	protected abstract void showRolePermissionEditDialog(PermissionListGridRecord record, String defName, String roleCode, int roleIndex, PermissionEditDialog dialog);

	/**
	 * <p>Permission編集ダイアログ結果反映処理</p>
	 *
	 * <p>編集ダイアログでOKされた際に呼び出されます。</p>
	 *
	 * @param record 対象Record
	 * @param rowNum 行番号
	 * @param colNum 列番号
	 * @param defName 対象Definition名
	 * @param roleCode 対象ロールコード
	 * @param roleIndex 対象ロールIndex
	 * @param event 変更イベント
	 */
	protected abstract void applyEditRolePermission(PermissionListGridRecord record, int rowNum, int colNum, String defName, String roleCode, int roleIndex, DataChangedEvent event);


	public PermissionListGrid(PermissionListGridDS ds, final Menu contextMenu) {
		this.ds = ds;

		setWidth100();
		setHeight100();

		//行番号表示
		setShowRowNumbers(true);
		ListGridField rowNumberField = new ListGridField();
		rowNumberField.setWidth(30);
		rowNumberField.setFrozen(true);
		setRowNumberFieldProperties(rowNumberField);

		setShowSelectedStyle(false);

		setCanSelectCells(true);

		//列幅自動調節（タイトルに設定）
		setAutoFitFieldWidths(true);
		//幅が足りないときに先頭行を自動的に伸ばさない
		setAutoFitFieldsFillViewport(false);

		addCellContextClickHandler(new CellContextClickHandler() {

			@Override
			public void onCellContextClick(CellContextClickEvent event) {
				int rowNum = event.getRowNum();
				int colNum = event.getColNum();

				//行番号列は無視
				if (colNum < 1) {
					event.cancel();
					return;
				}

				final PermissionListGridRecord record = (PermissionListGridRecord)event.getRecord();

				if (colNum == 1) {
					//名前選択(全ロール対象)
					contextMenu.setItems(new MenuItem[]{createDefinitionContextMenu(record, rowNum)});

				} else {
					//個別ロール選択
					contextMenu.setItems(createRoleContextMenu(record, rowNum, colNum));
				}
			}
		});

		addCellDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				int rowNum = event.getRowNum();
				int colNum = event.getColNum();

				//行番号、名前列は無視
				if (colNum < 2) {
					event.cancel();
					return;
				}

				//編集処理
				PermissionListGridRecord record = (PermissionListGridRecord)event.getRecord();
				editPermission(record, rowNum, colNum);
			}
		});

		setDataSource(ds);

		setHeaderSpans(ds.getHeaderSpan());
		setHeaderHeight(44);	//デフォルト × 2

		setFields(ds.getListGridField());
	}

	@Override
    protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
		// grid.setBaseStyleだとセルの高さが不安定になる為ここで指定。

		//行番号、名前列は無視
		if (colNum < 2) {
			return CELL_STYLE_DEFAULT;
		}

		PermissionListGridRecord precord = (PermissionListGridRecord)record;
		int roleIndex = ds.getColRoleCodeIndex(colNum);

		if (ds.isDeletingPermission(precord, roleIndex)) {
			return CELL_STYLE_DELETING;
		} else if (ds.isEditingPermission(precord, roleIndex)) {
			return CELL_STYLE_EDITING;
		} else if (ds.isConfiguredPermission(precord, roleIndex)) {
			return CELL_STYLE_CONFIGURED;
		} else {
			return CELL_STYLE_DEFAULT;
		}
	}

	/**
	 * <p>名前列に対するコンテキストメニューを生成します。</p>
	 *
	 * @param record 対象レコード
	 * @param rowNum 行番号
	 *
	 * @return コンテキストメニューアイテム
	 */
	private MenuItem createDefinitionContextMenu(final PermissionListGridRecord record, final int rowNum) {

		final String defName = record.getDefinitionName();

		MenuItem delAllPermissionMenu = new MenuItem("Delete All Permission", "[SKINIMG]/MultiUploadItem/icon_remove_files.png");
		delAllPermissionMenu.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				removeAllDefinitionPermission(record, rowNum, defName);
			}
		});
		delAllPermissionMenu.setEnabled(ds.canDeleteAllPermission(record));

		return delAllPermissionMenu;
	}

	/**
	 * <p>Role列に対するコンテキストメニューを生成します。</p>
	 *
	 * @param record 対象レコード
	 * @param rowNum 行番号
	 * @param colNum 列番号
	 * @return コンテキストメニューアイテム
	 */
	private MenuItem[] createRoleContextMenu(final PermissionListGridRecord record, final int rowNum, final int colNum) {

		final String defName = record.getDefinitionName();
		final String roleCode = ds.getColRoleCode(colNum);
		final int roleIndex = ds.getColRoleCodeIndex(colNum);

		MenuItem editPermissionMenu = new MenuItem("Edit Permission", "[SKINIMG]/MultiUploadItem/icon_add_files.png");
		editPermissionMenu.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				editPermission(record, rowNum, colNum);
			}

		});

		MenuItem delPermissionMenu = new MenuItem("Delete Permission", "[SKINIMG]/MultiUploadItem/icon_remove_files.png");
		delPermissionMenu.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
				removeRolePermission(record, rowNum, colNum, defName, roleCode, roleIndex);
			}

		});
		delPermissionMenu.setEnabled(ds.canDeletePermission(record, roleIndex));

		return new MenuItem[]{editPermissionMenu, delPermissionMenu};
	}

	/**
	 * <p>Permissionの編集を行います。</p>
	 *
	 * <p>コンテキストメニュー、またはセルダブルクリック時に呼び出します。</p>
	 *
	 * @param record 対象レコード
	 * @param rowNum 行番号
	 * @param colNum 列番号
	 */
	private void editPermission(final PermissionListGridRecord record, final int rowNum, final int colNum) {

		final String defName = record.getDefinitionName();
		final String roleCode = ds.getColRoleCode(colNum);
		final int roleIndex = ds.getColRoleCodeIndex(colNum);

		final PermissionEditDialog dialog = new PermissionEditDialog();
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				applyEditRolePermission(record, rowNum, colNum, defName, roleCode, roleIndex, event);
			}
		});

		showRolePermissionEditDialog(record, defName, roleCode, roleIndex, dialog);
	}

}
