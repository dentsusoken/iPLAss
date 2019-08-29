/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

import com.smartgwt.client.types.HeaderControls;

public class BlankSpaceControl extends ItemControl {

	public BlankSpaceControl(String defName, FieldReferenceType triggerType) {
		super(defName, triggerType);

		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_BlankPropertyWindow_space"));
		setDragType(EntityViewDragPane.DRAG_TYPE_ELEMENT);

		setShowMinimizeButton(false);
		setBackgroundColor("#FFFFCC");
		setBorder("1px solid olive");
		setHeight(22);

		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);
	}

	public PropertyItem getProperty() {
		PropertyItem prop = new PropertyItem();
		prop.setDispFlag(true);
		prop.setBlank(true);
		return prop;
	}
}
