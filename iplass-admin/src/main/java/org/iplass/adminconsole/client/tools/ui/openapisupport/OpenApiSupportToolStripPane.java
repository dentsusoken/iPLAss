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

import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * OpenAPI(Swagger)Support ツールのツールストリップ領域
 * <p>
 * 画面上部のツールストリップを構成します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportToolStripPane extends VLayout {
	/** import ボタンのアイコン */
	private static final String IMPORT_ICON = "[SKINIMG]/SchemaViewer/operation.png";
	/** export ボタンのアイコン */
	private static final String EXPORT_ICON = "[SKINIMG]/actions/download.png";
	/** Expand ボタンのアイコン */
	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	/** Collapse ボタンのアイコン */
	private static final String COLLAPSE_ICON = "[SKIN]/actions/sort_ascending.png";
	/** Refresh ボタンのアイコン */
	private static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";

	/** ツールストリップコンポーネント */
	private OpenApiSupportToolStrip toolStrip;

	/** Import ボタンクリック時操作 */
	private ClickHandler importClickHandler;
	/** Export ボタンクリック時操作 */
	private ClickHandler exportClickHandler;

	/** Expand ボタンクリック時操作 */
	private ClickHandler expandClickHandler;
	/** Collapse ボタンクリック時操作 */
	private ClickHandler collapseClickHandler;
	/** Refresh ボタンクリック時操作 */
	private ClickHandler refreshClickHandler;

	/**
	 * コンストラクタ
	 */
	public OpenApiSupportToolStripPane() {
		super();

		toolStrip = new OpenApiSupportToolStrip();

		toolStrip.addToolStripButton(IMPORT_ICON, "Import", e -> {
			if (importClickHandler != null) {
				importClickHandler.onClick(e);
			}
		});

		toolStrip.addToolStripButton(EXPORT_ICON, "Export", e -> {
			if (exportClickHandler != null) {
				exportClickHandler.onClick(e);
			}
		});

		toolStrip.addSeparator();

		// ↑ 左寄り
		// #addFill() を実行する前に追加した要素が左寄りになる。

		toolStrip.addFill();

		// #addFill() を実行した後に追加した要素は右寄りになる。
		// ↓ 右寄り

		toolStrip.addToolStripButton(EXPAND_ICON, "Expand", e -> {
			if (expandClickHandler != null) {
				expandClickHandler.onClick(e);
			}
		});

		toolStrip.addToolStripButton(COLLAPSE_ICON, "Collapse", e -> {
			if (collapseClickHandler != null) {
				collapseClickHandler.onClick(e);
			}
		});

		toolStrip.addToolStripButton(REFRESH_ICON, "Refresh", e -> {
			if (refreshClickHandler != null) {
				refreshClickHandler.onClick(e);
			}
		});

		addMember(toolStrip);
	}

	/**
	 * Import ボタンのクリックハンドラを設定します。
	 * @param handler クリックハンドラ
	 */
	public void setImportClickHandler(ClickHandler handler) {
		this.importClickHandler = handler;
	}

	/**
	 * Export ボタンのクリックハンドラを設定します。
	 * @param handler クリックハンドラ
	 */
	public void setExportClickHandler(ClickHandler handler) {
		this.exportClickHandler = handler;
	}

	/**
	 * Expand ボタンのクリックハンドラを設定します。
	 * @param handler クリックハンドラ
	 */
	public void setExpandClickHandler(ClickHandler handler) {
		this.expandClickHandler = handler;
	}

	/**
	 * Collapse ボタンのクリックハンドラを設定します。
	 * @param handler クリックハンドラ
	 */
	public void setCollapseClickHandler(ClickHandler handler) {
		this.collapseClickHandler = handler;
	}

	/**
	 * Refresh ボタンのクリックハンドラを設定します。
	 * @param handler クリックハンドラ
	 */
	public void setRefreshClickHandler(ClickHandler handler) {
		this.refreshClickHandler = handler;
	}

	@Override
	public void destroy() {
		toolStrip = null;

		importClickHandler = null;
		exportClickHandler = null;

		expandClickHandler = null;
		collapseClickHandler = null;
		refreshClickHandler = null;

		super.destroy();
	}
}
