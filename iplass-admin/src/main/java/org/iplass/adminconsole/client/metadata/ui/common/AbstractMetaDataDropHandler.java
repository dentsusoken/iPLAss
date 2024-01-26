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

package org.iplass.adminconsole.client.metadata.ui.common;

import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataPluginTreeGrid;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;

import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.events.DropOutEvent;
import com.smartgwt.client.widgets.events.DropOutHandler;
import com.smartgwt.client.widgets.events.DropOverEvent;
import com.smartgwt.client.widgets.events.DropOverHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * MetaDataItemMenuTreeNodeのDropHandler
 */
public abstract class AbstractMetaDataDropHandler implements DropOverHandler, DropOutHandler, DropHandler {

	private Canvas owner;

	/**
	 * itemNodeのDropを許可するかを判定します。
	 *
	 * @param itemNode Drop対象のNode
	 * @return true：許可
	 */
	protected abstract boolean canAcceptDrop(MetaDataItemMenuTreeNode itemNode);

	/**
	 * DropされたitemNodeに対しての処理を実装します。
	 *
	 * @param itemNode DropされたNode
	 */
	protected abstract void onMetaDataDrop(MetaDataItemMenuTreeNode itemNode);

	public AbstractMetaDataDropHandler() {
	}

	@Override
	public void onDropOver(DropOverEvent event) {
		MetaDataItemMenuTreeNode item = getItemNode();
		if (item != null && canAcceptDrop(item)) {
			owner.setBackgroundColor(dropOverBackgroundColor(item));
		}
	}

	@Override
	public void onDropOut(DropOutEvent event) {
		owner.setBackgroundColor(dropOutBackgroundColor());
	}

	@Override
	public void onDrop(DropEvent event) {
		MetaDataItemMenuTreeNode item = getItemNode();
		if (item != null && canAcceptDrop(item)) {
			onMetaDataDrop(item);
		}

		event.cancel();
	}

	/**
	 * 指定されたownerに対して、Drop設定を行います。
	 *
	 * @param owner Dropを許可するCanvas
	 */
	public void setTarget(Canvas owner) {
		this.owner = owner;

		owner.setDropTypes(new String[]{MetaDataConstants.METADATA_DRAG_DROP_TYPE});
		owner.setCanAcceptDrop(true);

		if (owner instanceof Layout) {
			((Layout)owner).setShowDropLines(false);
		}

		owner.addDropOverHandler(this);
		owner.addDropOutHandler(this);
		owner.addDropHandler(this);
	}

	/**
	 * DropされたitemNodeごとに背景色を変えたい場合は、このメソッドをオーバーライドしてください。
	 *
	 * @param itemNode Drop対象のNode
	 * @return Drop中のownerの背景色
	 */
	protected String dropOverBackgroundColor(MetaDataItemMenuTreeNode itemNode) {
		return "#b3ffff";
	}

	/**
	 * itemNodeがownerの外に出た場合に、ownerの背景色を戻します。
	 *
	 * @return 通常時のownerの背景色
	 */
	protected String dropOutBackgroundColor() {
		return "#ffffff";
	}

	private MetaDataItemMenuTreeNode getItemNode() {
		Canvas dragTarget = EventHandler.getDragTarget();
		if (dragTarget instanceof MetaDataPluginTreeGrid) {
			TreeNode node = ((MetaDataPluginTreeGrid)dragTarget).getSelectedRecord();
			return (MetaDataItemMenuTreeNode)node;
		}
		return null;
	}

}
