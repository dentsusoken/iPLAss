/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui;

import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.plugin.AdminPlugin;

public interface MetaDataPlugin extends AdminPlugin {

	/**
	 * <p>ノードリフレッシュ処理</p>
	 *
	 * <p>ルートノード配下を再作成します。</p>
	 */
	public void refresh();

	/**
	 * <p>指定されたメタデータが編集対象かを判定します。</p>
	 * <p>MetaDataExplorerからの直接起動時用です。</p>
	 *
	 * @param event イベント
	 * @return 判定結果
	 */
	public boolean isEditSupportMetaData(ViewMetaDataEvent event);

}
