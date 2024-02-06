/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.top.item.PartsItem;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNode;
import org.iplass.mtp.view.top.parts.TopViewParts;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * ドロップ領域
 * @author lis3wg
 */
public abstract class TopViewDropAreaPane extends VStack {

	private PartsOperationHandler controler;
	private PartsController partsController = GWT.create(PartsController.class);

	/**
	 * コンストラクタ
	 */
	public TopViewDropAreaPane(PartsOperationHandler controler) {
		this.controler = controler;

		setWidth100();
		setPadding(5);
		setMembersMargin(6);
		setCanAcceptDrop(true);
		setDropLineThickness(4);
		setBorder("1px solid navy");

		//ドロップ先を表示する設定
		Canvas dropLineProperties = new Canvas();
		dropLineProperties.setBackgroundColor("aqua");
		setDropLineProperties(dropLineProperties);

		//ドラッグ中のコンポーネントを表示する設定
		setShowDragPlaceHolder(true);
		Canvas placeHolderProperties = new Canvas();
		placeHolderProperties.setBorder("2px solid #8289A6");
		setPlaceHolderProperties(placeHolderProperties);

		addDropHandler(new TopViewDropHandler());
	}

	public PartsItem[] getParts() {
		List<PartsItem> items = new ArrayList<PartsItem>();
		for (Canvas canvas : getMembers()) {
			if (canvas instanceof PartsItem) items.add((PartsItem) canvas);
		}
		return items.toArray(new PartsItem[items.size()]);
	}

	public List<TopViewParts> getItem() {
		List<TopViewParts> parts = new ArrayList<TopViewParts>();
		for (PartsItem item : getParts()) {
			parts.add(item.getParts());
		}
		return parts;
	}

	public void reset() {
		for (PartsItem item : getParts()) {
			item.destroy();
		}
	}

	protected PartsItem convertNode(TopViewNode node) {

		return partsController.createWindow(node, controler, getDropAreaType());
	}

	protected PartsItem convertParts(TopViewParts parts) {

		return partsController.createWindow(parts, controler, getDropAreaType());
	}

	protected abstract boolean allowDropWidget();

	protected abstract boolean allowDropParts();

	protected abstract String getDropAreaType();

	protected abstract DropItemHandler createHandler();

	private DropItemHandler handler;
	private DropItemHandler getDropItemHandler() {
		if (handler == null) handler = createHandler();
		return handler;
	}

	public interface DropItemHandler {
		public void onDrop(TopViewNode node, int dropPosition);
		public boolean onDrag(PartsItem item, int dropPosition);
	}

	private class TopViewDropHandler implements DropHandler {

		@Override
		public void onDrop(DropEvent event) {
			//ダイアログ表示後はdrop位置が替わるので、予め取得
			final int dropPosition = getDropPosition();
			final Canvas dragTarget = EventHandler.getDragTarget();
			if (dragTarget instanceof ListGrid) {
				TreeNode record = (TreeNode) ((TreeGrid) dragTarget).getSelectedRecord();
				if (record instanceof TopViewNode) {
					TopViewNode node = (TopViewNode) record;
					checkAndOnDrop(dropPosition, node, allowDropParts(), node.isParts(), node.isWidget());
					checkAndOnDrop(dropPosition, node, allowDropWidget(), node.isWidget(), node.isParts());
				}

				//Drag元が動かないように
				event.cancel();
			} else if (dragTarget instanceof PartsItem) {
				boolean cancel = getDropItemHandler().onDrag((PartsItem) dragTarget, dropPosition);
				if (!cancel) event.cancel();
			}
		}

		private void checkAndOnDrop(int dropPosition, TopViewNode node, boolean allow, boolean type1, boolean type2) {
			if (allow) {
				//ドロップを許可
				if (type1) {
					if (node.isUnique() && controler != null) {
						MTPEvent e = new MTPEvent();
						e.setValue("key", getDropAreaType() + "_" + node.getKey());
						if (controler.check(e)) {
							getDropItemHandler().onDrop(node, dropPosition);
							controler.add(e);
						} else {
							SC.say(AdminClientMessageUtil.getString("ui_metadata_top_TopViewDropAreaPane_canNotArrangMultiple"));
						}
					} else {
						getDropItemHandler().onDrop(node, dropPosition);
					}
				}
			} else {
				if (type1 && !type2) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_top_TopViewDropAreaPane_canNotPlacedThisArea"));
				}
			}
		}
	}
}
