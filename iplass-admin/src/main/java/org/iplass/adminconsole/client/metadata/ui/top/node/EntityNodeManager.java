/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.view.top.parts.EntityListParts;

import com.smartgwt.client.widgets.tree.Tree;

/**
 *
 * @author lis3wg
 */
public class EntityNodeManager extends ItemNodeManager {

	@Override
	public String getName() {
		return "EntityData List";
	}

	@Override
	public void loadChild(Tree tree, TopViewNode parent) {
		TopViewNode entityList = new TopViewNode("SearchResult List", EntityListParts.class.getName(), "", true, true, false);
		tree.addList(new TopViewNode[]{entityList}, parent);
	}
}
