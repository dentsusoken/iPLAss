/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.section.TemplateSection;

public class TemplateSectionControl extends ItemControl implements SectionControl {

	public TemplateSectionControl(String defName, FieldReferenceType triggerType, TemplateSection section) {
		super(defName, triggerType);

		if (section.getTitle() != null && !section.getTitle().isEmpty()) {
			setTitle(section.getTitle());
		} else {
			setTitle("Template Section");
		}

		setBackgroundColor("#BBDDFF");
		setDragType(EntityViewDragPane.DRAG_TYPE_SECTION);
		setHeight(22);
		setBorder("1px solid navy");

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				TemplateSection section = (TemplateSection) event.getValue();

				if (section.getTitle() != null && !section.getTitle().isEmpty()) {
					setTitle(section.getTitle());
				} else {
					setTitle("Template Section");
				}

			}
		});

		setClassName(section.getClass().getName());
		setValueObject(section);
	}

	@Override
	public TemplateSection getSection() {
		TemplateSection section = (TemplateSection) getValueObject();
		return section;
	}

}
