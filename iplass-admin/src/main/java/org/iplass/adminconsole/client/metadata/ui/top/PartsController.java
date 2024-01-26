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

package org.iplass.adminconsole.client.metadata.ui.top;

import java.util.List;

import org.iplass.adminconsole.client.metadata.ui.top.item.PartsItem;
import org.iplass.adminconsole.client.metadata.ui.top.node.ItemNodeManager;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNode;
import org.iplass.mtp.view.top.parts.TopViewParts;

public interface PartsController {

	PartsItem createWindow(TopViewParts parts, PartsOperationHandler controler, String dropAreaType);

	PartsItem createWindow(TopViewNode node, PartsOperationHandler controler, String dropAreaType);

	List<ItemNodeManager> partsNodeList();
}
