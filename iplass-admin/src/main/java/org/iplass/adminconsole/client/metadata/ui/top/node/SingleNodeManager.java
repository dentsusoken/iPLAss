/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.top.node;

import org.iplass.mtp.view.top.parts.InformationParts;
import org.iplass.mtp.view.top.parts.LastLoginParts;

import com.smartgwt.client.widgets.tree.Tree;

/**
 * フォルダに属さない単一ノードを管理
 * @author lis3wg
 */
public class SingleNodeManager extends ItemNodeManager {

	@Override
	public String getName() {
		return "SingleNode";
	}

	@Override
	public void loadChild(Tree tree, TopViewNode parent) {
		// 親フォルダは削除
		tree.remove(parent);

		TopViewNode lastLogin = new TopViewNode("Last Login", LastLoginParts.class.getName(), "", true, false, true);
		TopViewNode infoList = new TopViewNode("Information List", InformationParts.class.getName(), "", true, false, true);

		// Root直下に表示
		tree.addList(new TopViewNode[]{infoList, lastLogin}, tree.getRoot());
	}

}
