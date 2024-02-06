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
 * メインエリア
 * @author lis3wg
 */
public class TopViewMainAreaPane extends VLayout {

	private MainAreaDropPane dropPane;

	/**
	 * コンストラクタ
	 */
	public TopViewMainAreaPane(PartsOperationHandler controler) {
		setWidth("50%");

		Label mainTitle = new Label("Main Area (Droppable P(Parts))");
		mainTitle.setHeight(20);
		addMember(mainTitle);
		dropPane = new MainAreaDropPane(controler);
		addMember(dropPane);
	}

	public void reset() {
		dropPane.reset();
	}

	public void setParts(List<TopViewParts> parts) {
		dropPane.setParts(parts);
	}

	public List<TopViewParts> getParts() {
		return dropPane.getItem();
	}

	public class MainAreaDropPane extends TopViewDropAreaPane {

		public MainAreaDropPane(PartsOperationHandler controler) {
			super(controler);
			setDropTypes("node", "parts");
		}

		@Override
		protected boolean allowDropParts() {
			return true;
		}

		@Override
		protected boolean allowDropWidget() {
			return false;
		}

		@Override
		protected String getDropAreaType() {
			return "parts";
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
								item.setDragType("parts");
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

		private void setParts(List<TopViewParts> parts) {
			for (TopViewParts p : parts) {
				PartsItem item = convertParts(p);
				if (item != null) {
					item.setDragType("parts");
					addMember(item);
				}
			}
		}
	}
}
