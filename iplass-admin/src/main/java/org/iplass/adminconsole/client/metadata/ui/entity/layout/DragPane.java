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

import org.iplass.adminconsole.client.metadata.data.entity.PropertyDS;
import org.iplass.adminconsole.client.metadata.data.entity.layout.ElementItemDS;
import org.iplass.adminconsole.client.metadata.data.entity.layout.SectionItemDS;
import org.iplass.adminconsole.client.metadata.data.entity.layout.ViewType;
import org.iplass.adminconsole.client.metadata.ui.common.EntityPropertyGrid;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.VisibilityMode;
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
public class DragPane extends VLayout {

	/** プロパティ用のスタック */
	private SectionStackSection properties;

	/** セクション用のスタック */
	private SectionStackSection sections;

	/** エレメント用のスタック */
	private SectionStackSection elements;

	/**
	 * コンストラクタ
	 * @param defName
	 */
	public DragPane(String defName) {
		this(defName, true, true, true);
	}

	/**
	 * コンストラクタ
	 * @param defName
	 * @param dispProperty
	 * @param dispSection
	 * @param dispElement
	 */
	public DragPane(String defName, boolean dispProperty, boolean dispSection, boolean dispElement) {
		this(defName, true, false, true, true, ViewType.DETAIL);
	}

	/**
	 * コンストラクタ
	 * @param defName
	 * @param dispProperty
	 * @param dispSection
	 * @param dispElement
	 * @param viewType
	 */
	public DragPane(String defName, boolean dispProperty, boolean dispTreeProperty, boolean dispSection, boolean dispElement, ViewType viewType) {
		setWidth("25%");

    	SectionStack dropItemStack = new SectionStack();
    	dropItemStack.setVisibilityMode(VisibilityMode.MULTIPLE);

    	if (dispProperty) {
			properties = new SectionStackSection("Property");
			properties.setExpanded(true);
			setPropertyList(defName, dispTreeProperty);
			dropItemStack.addSection(properties);
    	}

    	if (dispSection) {
			sections = new SectionStackSection("Section");
			sections.setExpanded(true);
			setSection(viewType);
			dropItemStack.addSection(sections);
    	}

    	if (dispElement) {
			elements = new SectionStackSection("Element");
			elements.setExpanded(true);
			setElement(viewType);
			dropItemStack.addSection(elements);
    	}

    	addMember(dropItemStack);
	}

	/**
	 * DSからエレメントを設定。
	 */
	private void setElement(ViewType viewType) {
		final ListGrid grid = new ListGrid();
		grid.setShowHeader(false);
		grid.setDragType("element");
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

		elements.addItem(grid);
	}

	/**
	 * DSからセクションを設定。
	 */
	private void setSection(ViewType viewType) {
		final ListGrid grid  =new ListGrid();
		grid.setShowHeader(false);
		grid.setDragType("section");
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

		sections.addItem(grid);
	}

	/**
	 * DSからプロパティを設定。
	 * @param defName
	 */
	private void setPropertyList(String defName, boolean dispTreeProperty) {
		if (dispTreeProperty) {
			EntityPropertyGrid grid = new EntityPropertyGrid(false);
			grid.setDragType("property");	//DragされるItemのType設定
			grid.refresh(defName);

			properties.addItem(grid);
		} else {
			final ListGrid grid  =new ListGrid();
			grid.setShowHeader(false);
			grid.setDragType("property");
			grid.setDragDataAction(DragDataAction.NONE);
			grid.setCanDragRecordsOut(true);
			grid.setEmptyMessage("no proprety");
			ListGridField displayName = new ListGridField("displayName");
			grid.setFields(displayName);
//			grid.setAutoFitData(Autofit.VERTICAL);

			PropertyDS dataSource = PropertyDS.create(defName);
			dataSource.fetchData(null, new DSCallback() {

				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					RecordList list = response.getDataAsRecordList();
					if (list != null) {
						grid.setData(list.toArray());
					}
				}
			});

			properties.addItem(grid);
		}
	}

}
