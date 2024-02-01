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

import java.util.List;

import org.iplass.adminconsole.client.metadata.ui.top.item.PartsItem;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNode;
import org.iplass.mtp.view.top.parts.TopViewParts;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * メニューエリア
 * @author lis3wg
 */
public class TopViewMenuAreaPane extends VLayout {

	private MenuAreaDropPane dropPane;

	/**
	 * コンストラクタ
	 */
	public TopViewMenuAreaPane(PartsOperationHandler controler) {
		setWidth("25%");

		Label menuTitle = new Label("Widget Area (Droppable W(Widget))");
		menuTitle.setHeight(20);
		addMember(menuTitle);
		dropPane = new MenuAreaDropPane(controler);
		addMember(dropPane);
	}

	public void reset() {
		dropPane.reset();
	}

	public void setWidgets(List<TopViewParts> widgets) {
		dropPane.setWidgets(widgets);
	}

	public List<TopViewParts> getWidgets() {
		return dropPane.getItem();
	}

	public class MenuAreaDropPane extends TopViewDropAreaPane {

		public MenuAreaDropPane(PartsOperationHandler controler) {
			super(controler);
			setDropTypes("node", "widget");
		}

		@Override
		protected boolean allowDropParts() {
			return false;
		}

		@Override
		protected boolean allowDropWidget() {
			return true;
		}

		@Override
		protected String getDropAreaType() {
			return "widget";
		}

		@Override
		protected DropItemHandler createHandler() {
			return new DropItemHandler() {

				@Override
				public void onDrop(TopViewNode node, final int dropPosition) {
					final PartsItem item = convertNode(node);
					if (item != null) {
						item.doDropAction(new PartsItem.DropActionCallback() {

							@Override
							public void handle() {
								item.setDragType("widget");
								addMember(item, dropPosition);
							}
						});
					}
				}

				@Override
				public boolean onDrag(PartsItem item, int dropPosition) {
					return true;
				}
			};
		}

		private void setWidgets(List<TopViewParts> widgets) {
			for (TopViewParts w : widgets) {
				PartsItem item = convertParts(w);
				if (item != null) {
					item.setDragType("widget");
					addMember(item);
				}
			}
		}

	}
}
