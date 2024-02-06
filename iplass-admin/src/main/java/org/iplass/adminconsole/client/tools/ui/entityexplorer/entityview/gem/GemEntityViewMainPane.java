/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer.entityview.gem;

import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerMainPane;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entity Explorer GEM画面定義 Mainパネル
 */
public class GemEntityViewMainPane extends VLayout {

	private GemEntityViewListPane entityViewListPane;

	/**
	 * コンストラクタ
	 */
	public GemEntityViewMainPane(EntityExplorerMainPane owner) {
		//レイアウト設定
		setWidth100();

		//初期状態では一覧を生成せずに、Tabが選択されたタイミングで生成する
	}

	public void selectedPane() {
		if (entityViewListPane == null) {
			//初期表示時にEntity一覧を作成
			entityViewListPane = new GemEntityViewListPane(this);
			addMember(entityViewListPane);
		}
	}

}
