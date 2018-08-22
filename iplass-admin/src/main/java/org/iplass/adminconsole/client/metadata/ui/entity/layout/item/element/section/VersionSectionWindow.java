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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section;

import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ViewEditWindow;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.section.VersionSection;

import com.smartgwt.client.types.HeaderControls;

public class VersionSectionWindow extends ViewEditWindow implements SectionWindow {

	public VersionSectionWindow(String defName, FieldReferenceType triggerType, VersionSection section) {
		super(defName, triggerType);

		setTitle("Version Section");
		setBackgroundColor("#BBBBFF");
		setDragType("section");
		setHeight(22);
		setBorder("1px solid navy");

		setHeaderControls(HeaderControls.MINIMIZE_BUTTON, HeaderControls.HEADER_LABEL, setting, HeaderControls.CLOSE_BUTTON);

		setClassName(section.getClass().getName());
		setValueObject(section);
	}

	@Override
	public VersionSection getSection() {
		VersionSection section = (VersionSection) getValueObject();
		return section;
	}

}
