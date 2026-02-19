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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNode;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNodeManager;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * パーツDragパネル
 * 登録されているDrag可能なパーツを表示します。
 */
public class TopViewDragPane extends VLayout {

	private SectionStack sectionStack;

	/** パーツ用Section */
	private SectionStackSection partsSection;

	private TopViewItemGrid grid;
	/** 内部保持Tree */
	private Tree tree;

	/**
	 * コンストラクタ
	 */
	public TopViewDragPane() {
		setWidth("25%");

		sectionStack = new SectionStack();
		partsSection = new SectionStackSection();
		partsSection.setTitle(AdminClientMessageUtil.getString("ui_metadata_top_TopViewDragPane_items"));
		partsSection.setExpanded(true);
		sectionStack.addSection(partsSection);

		grid = new TopViewItemGrid();
		grid.setDragType("node");
		partsSection.addItem(grid);

		loadData();

		ImgButton refreshButton = new ImgButton();
		refreshButton.setSrc("refresh.png");
		refreshButton.setSize(16);
		refreshButton.setShowFocused(false);
		refreshButton.setShowRollOver(false);
		refreshButton.setShowDown(false);
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_top_TopViewDragPane_refreshList"));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		});

		partsSection.setControls(refreshButton);

		addMember(sectionStack);
	}

	/**
	 * 画面を再表示します。
	 */
	private void refresh() {
		//データ再作成
		loadData();
	}

	private void loadData() {
		tree = new Tree();
		tree.setModelType(TreeModelType.CHILDREN);

		TopViewNodeManager nm = new TopViewNodeManager();
		TopViewNode root = new TopViewNode("root");
		tree.setRoot(root);
		nm.setNode(tree, root);

		grid.setData(tree);
		grid.getData().openAll();
	}

	private class TopViewItemGrid extends MtpTreeGrid {

		/**
		 * コンストラクタ
		 */
		public TopViewItemGrid() {

			setLeaveScrollbarGap(false);
			setShowHeader(false);
			setDragDataAction(DragDataAction.NONE);
			setSelectionType(SelectionStyle.SINGLE);
			setCanDragRecordsOut(true);
			setBorder("none");

			setCanSort(false);
			setCanFreezeFields(false);
			setCanPickFields(false);

			tree = new Tree();
			tree.setModelType(TreeModelType.CHILDREN);
			//TreeGridFieldの生成(TreeNodeのattribute属性と一致させること)
			TreeGridField nameField = new TreeGridField("name", AdminClientMessageUtil.getString("ui_metadata_top_TopViewDragPane_nodeName"));

			setFields(nameField);
			setData(tree);

			addDragStartHandler(new DragStartHandler() {

				@Override
				public void onDragStart(DragStartEvent event) {
					TopViewNode record = (TopViewNode) getSelectedRecord();
					Boolean isForlder = record.isFolder();
					if (isForlder != null && isForlder) {
						event.cancel();
					}
				}
			});
		}

	}
}
