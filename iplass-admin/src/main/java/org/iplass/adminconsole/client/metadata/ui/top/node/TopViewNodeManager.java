/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.metadata.ui.top.PartsController;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.tree.Tree;

/**
 *
 * @author lis3wg
 */
public class TopViewNodeManager {

	PartsController partsController = GWT.create(PartsController.class);

	public TopViewNodeManager() {
	}

	public void setNode(Tree tree, TopViewNode root) {
		for (ItemNodeManager item : partsController.partsNodeList()) {
			TopViewNode node = item.getFolder();
			tree.add(node, root);
			item.loadChild(tree, node);
		}
	}

}

