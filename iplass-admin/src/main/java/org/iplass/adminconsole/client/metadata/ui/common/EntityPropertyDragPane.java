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

package org.iplass.adminconsole.client.metadata.ui.common;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class EntityPropertyDragPane extends VLayout {

	protected static final String SECTION_ID_PROPERTY = "propertyItem";

	protected SectionStack propertySectionStack;
	protected EntityPropertyTreeGrid propertyGrid;

	public EntityPropertyDragPane(boolean showRoot) {
		setWidth("30%");

		propertySectionStack = new SectionStack();
		propertySectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);

		SectionStackSection propertySection = new SectionStackSection("Properties");
		propertySection.setExpanded(true);
		propertySection.setCanCollapse(false);	//CLOSE不可
		propertySection.setID(SECTION_ID_PROPERTY);


		propertySectionStack.addSection(propertySection);

		//表示TreeGrid
		propertyGrid = new EntityPropertyTreeGrid(showRoot);
		propertyGrid.setDragType("Property");	//DragされるItemのType設定

		propertySection.addItem(propertyGrid);

		addMember(propertySectionStack);
	}

	/**
	 * 画面を再表示します。
	 */
	public void refresh(String defName) {
		propertyGrid.refresh(defName);
	}
}
