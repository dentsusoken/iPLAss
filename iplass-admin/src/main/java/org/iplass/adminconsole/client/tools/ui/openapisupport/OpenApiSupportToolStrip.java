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

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * OpenAPI(Swagger)Support ツールのツールストリップコンポーネント
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportToolStrip extends ToolStrip {
	/**
	 * コンストラクタ
	 */
	public OpenApiSupportToolStrip() {
		super();

		setWidth100();
		setMembersMargin(5);
		setAlign(VerticalAlignment.BOTTOM);
	}

	/**
	 * ツールストリップにボタンを追加します。
	 * @param title ボタンのタイトル
	 * @param handler ボタンの処理
	 * @return 追加したボタン
	 */
	public ToolStripButton addToolStripButton(String title, ClickHandler handler) {
		return addToolStripButton(null, title, handler);
	}

	/**
	 * ツールストリップにボタンを追加します。
	 * @param icon ボタンのアイコン
	 * @param title ボタンのタイトル
	 * @param handler ボタンの処理
	 * @return 追加したボタン
	 */
	public ToolStripButton addToolStripButton(String icon, String title, ClickHandler handler) {
		ToolStripButton button = new ToolStripButton(title);
		if (icon != null && !icon.isEmpty()) {
			button.setIcon(icon);
		}
		button.setWidth(100);
		button.addClickHandler(handler);
		addButton(button);

		return button;
	}
}
