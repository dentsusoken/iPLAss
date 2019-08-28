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

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog.PropertyInfo;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author lis3wg
 */
public class ReferenceSectionControl extends ItemControl implements SectionControl {

	private MetaDataServiceAsync service = null;

	private String entityPropertyDisplayName = null;

	/**
	 * コンストラクタ
	 * @param defName
	 */
	public ReferenceSectionControl(final String defName, FieldReferenceType triggerType, final ReferenceSection section) {
		super(defName, triggerType);

		service = MetaDataServiceFactory.get();

		setBackgroundColor("#99FFCC");
		setDragType(EntityViewDragPane.DRAG_TYPE_SECTION);
		setHeight(22);
		setBorder("1px solid navy");

		setValue("name", section.getPropertyName());

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				String title = null;
				if (event.getValueMap().containsKey("title")) {
					title = (String) event.getValueMap().get("title");
				}

				createTitle(title);
			}
		});

		setClassName(section.getClass().getName());
		setValueObject(section);

		getDisplayLabel(defName, section.getPropertyName());
	}

	private void getDisplayLabel(String defName, final String propertyName) {
		service.getPropertyDisplayName(TenantInfoHolder.getId(), defName, propertyName, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				entityPropertyDisplayName = result;
				createTitle(getSection().getTitle());
			}

		});
	}

	private void createTitle(String itemDisplayName) {
		String title = itemDisplayName != null ? itemDisplayName + " ": "";
		title = title + "(" + entityPropertyDisplayName + "[" + (String)getValue("name") + "])";
		setTitle(title);
	}

	@Override
	public ReferenceSection getSection() {
		ReferenceSection section = (ReferenceSection) getValueObject();
		return section;
	}

	@Override
	protected EntityViewFieldSettingDialog createSubDialog() {
		ReferenceSection section = getSection();
		EntityViewFieldSettingDialog dialog = new EntityViewFieldSettingDialog(getClassName(), getValueObject(), triggerType, defName, section.getDefintionName());

		// ダイアログのタイトルに対象のプロパティ名を表示
		dialog.setTitlePropertyInfo(new PropertyInfo((String)getValue("name"), entityPropertyDisplayName));
		return dialog;
	}

}
