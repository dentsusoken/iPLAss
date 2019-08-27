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

package org.iplass.adminconsole.client.metadata.ui.entity.layout;

import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.metadata.data.entity.layout.ElementItemDS;
import org.iplass.adminconsole.client.metadata.data.entity.layout.SectionItemDS;
import org.iplass.adminconsole.client.metadata.data.entity.layout.ViewType;
import org.iplass.adminconsole.client.metadata.ui.common.EntityPropertyGrid;
import org.iplass.adminconsole.client.metadata.ui.common.EntityPropertyListGrid;
import org.iplass.adminconsole.client.metadata.ui.common.EntityPropertyTreeGrid;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * ドラッグ用のパネル
 * @author lis3wg
 *
 */
public class EntityViewDragPane extends VLayout {
	
	public static final String DRAG_TYPE_PROPERTY = "property";
	public static final String DRAG_TYPE_SECTION = "section";
	public static final String DRAG_TYPE_ELEMENT = "element";
			

	/** プロパティ用のスタック */
	private SectionStackSection propertySection;

	/** プロパティ用のGrid */
	private EntityPropertyGrid propertyGrid;

	/** セクション用のスタック */
	private SectionStackSection sectionSection;

	/** エレメント用のスタック */
	private SectionStackSection elementSection;

	/**
	 * コンストラクタ
	 * @param defName
	 * @param showTree
	 * @param viewType
	 */
	public EntityViewDragPane(String defName, boolean showTree, ViewType viewType) {
		setWidth("25%");

    	SectionStack dropItemStack = new SectionStack();
    	dropItemStack.setVisibilityMode(VisibilityMode.MULTIPLE);

		propertySection = new SectionStackSection("Property");
		propertySection.setExpanded(true);
		setPropertyList(defName, showTree);
		dropItemStack.addSection(propertySection);

		sectionSection = new SectionStackSection("Section");
		sectionSection.setExpanded(true);
		setSection(viewType);
		dropItemStack.addSection(sectionSection);

		elementSection = new SectionStackSection("Element");
		elementSection.setExpanded(true);
		setElement(viewType);
		dropItemStack.addSection(elementSection);

    	addMember(dropItemStack);
	}

	/**
	 * DSからエレメントを設定。
	 */
	private void setElement(ViewType viewType) {
		final ListGrid grid = new ListGrid();
		grid.setShowHeader(false);
		grid.setDragType(DRAG_TYPE_ELEMENT);
		grid.setDragDataAction(DragDataAction.NONE);
		grid.setCanDragRecordsOut(true);
		grid.setEmptyMessage("no element");
		ListGridField displayName = new ListGridField("displayName");
		grid.setFields(displayName);
//		grid.setAutoFitData(Autofit.VERTICAL);

		ElementItemDS dataSource = ElementItemDS.getInstance(viewType);
		dataSource.fetchData(null, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				RecordList list = response.getDataAsRecordList();
				if (list != null) {
					grid.setData(list.toArray());
				}
			}
		});

		//高さ調整
		grid.setHeight(150);

		elementSection.addItem(grid);
	}

	/**
	 * DSからセクションを設定。
	 */
	private void setSection(ViewType viewType) {
		final ListGrid grid  =new ListGrid();
		grid.setShowHeader(false);
		grid.setDragType(DRAG_TYPE_SECTION);
		grid.setDragDataAction(DragDataAction.NONE);
		grid.setCanDragRecordsOut(true);
		grid.setEmptyMessage("no section");
		ListGridField displayName = new ListGridField("displayName");
		grid.setFields(displayName);
//		grid.setAutoFitData(Autofit.VERTICAL);

		SectionItemDS dataSource = SectionItemDS.getInstance(viewType);
		dataSource.fetchData(null, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				RecordList list = response.getDataAsRecordList();
				if (list != null) {
					grid.setData(list.toArray());
				}
			}
		});

		//高さ調整
		grid.setHeight(150);

		sectionSection.addItem(grid);
	}

	/**
	 * DSからプロパティを設定。
	 * @param defName
	 * @param showTree
	 */
	private void setPropertyList(final String defName, final boolean showTree) {

		if (showTree) {
			EntityPropertyTreeGrid grid = new EntityPropertyTreeGrid(false);
			grid.setDragType(DRAG_TYPE_PROPERTY);
			grid.refresh(defName);
			propertySection.addItem(grid);

			propertyGrid = grid;
		} else {
			EntityPropertyListGrid grid = new EntityPropertyListGrid();
			grid.setDragType(DRAG_TYPE_PROPERTY);
			grid.refresh(defName);

			propertySection.addItem(grid);

			propertyGrid = grid;
		}

		ImgButton btnRefesh = new ImgButton();
		btnRefesh.setSrc(MtpWidgetConstants.ICON_REFRESH);
		btnRefesh.setSize(16);
		btnRefesh.setShowFocused(false);
		btnRefesh.setShowRollOver(false);
		btnRefesh.setShowDown(false);
		btnRefesh.setHoverWrap(false);
		btnRefesh.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				propertyGrid.refresh(defName);
			}
		});
		propertySection.setControls(btnRefesh);

	}

}
