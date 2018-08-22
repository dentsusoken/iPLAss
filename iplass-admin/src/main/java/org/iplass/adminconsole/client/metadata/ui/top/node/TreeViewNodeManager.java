/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.view.top.parts.TreeViewParts;
import org.iplass.mtp.view.treeview.TreeView;

import com.smartgwt.client.widgets.tree.Tree;

/**
 *
 * @author lis3wg
 */
public class TreeViewNodeManager extends ItemNodeManager {

	@Override
	public String getName() {
		return "TreeView";
	}

	@Override
	public void loadChild(final Tree tree, final TopViewNode parent) {
		getMetaDataTree(tree, parent);
	}

	@Override
	protected String treeDefinitionClassName() {
		return TreeView.class.getName();
	}

	@Override
	protected String getDefinitionClassName() {
		return TreeViewParts.class.getName();
	}

	@Override
	protected boolean isParts() {
		return true;
	}

	@Override
	protected boolean isWidget() {
		return true;
	}

	@Override
	protected boolean isUnique() {
		return true;
	}

}
