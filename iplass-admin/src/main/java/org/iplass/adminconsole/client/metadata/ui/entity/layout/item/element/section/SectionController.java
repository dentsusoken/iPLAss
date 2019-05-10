/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.List;

import org.iplass.adminconsole.client.metadata.data.entity.layout.ViewType;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.element.section.Section;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public interface SectionController {

	public interface Callback {
		void onCreated(ItemControl window);
	}

	ItemControl createControl(Section section, String defName, FieldReferenceType triggerType, EntityDefinition ed);

	void createControl(String sectionClassName, String defName, FieldReferenceType triggerType, PropertyOperationHandler propertyOperationHandler, Callback callback);

	List<ListGridRecord> sectionItemList(ViewType viewType);
}
