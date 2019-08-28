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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog.PropertyInfo;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;

import com.smartgwt.client.util.SC;

/**
 * 仮想プロパティ用のウィンドウ
 * @author lis3wg
 *
 */
public class VirtualPropertyControl extends ItemControl {

	private String propertyName = null;

	private EntityDefinition ed;

	/**
	 * コンストラクタ
	 */
	private VirtualPropertyControl(String defName, FieldReferenceType triggerType) {
		super(defName, triggerType);

		setDragType(EntityViewDragPane.DRAG_TYPE_ELEMENT);

		setShowMinimizeButton(false);
		setBackgroundColor("#9ACD32");
		setBorder("1px solid olive");
		setHeight(22);

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				//プロパティ名
				String oldPropertyName = propertyName;
				propertyName = (String) event.getValueMap().get("propertyName");

				if (!oldPropertyName.equals(propertyName)) {
					//本物チェック
					if (ed.getProperty(propertyName) != null) {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_VirtualPropertyWindow_checkPropDefExistsErr"));
						propertyName = oldPropertyName;
						getViewElement().setPropertyName(propertyName);
						return;
					}
				}

				//表示名
				String displayLabel = (String) event.getValueMap().get("displayLabel");
				setTitle(displayLabel + "([" + propertyName + "])");
			}
		});
	}

	/**
	 * コンストラクタ
	 * @param element
	 */
	public VirtualPropertyControl(String defName, FieldReferenceType triggerType, EntityDefinition ed, Element element) {
		this(defName, triggerType);

		this.ed = ed;

		VirtualPropertyItem property = (VirtualPropertyItem) element;
		propertyName = property.getPropertyName();
		setTitle(property.getDisplayLabel()  + "([" + propertyName + "])");

		setClassName(element.getClass().getName());
		setValueObject(element);

	}

	/**
	 * エレメントを取得。
	 * @return
	 */
	public VirtualPropertyItem getViewElement() {
		return (VirtualPropertyItem) getValueObject();
	}

	@Override
	protected EntityViewFieldSettingDialog createSubDialog() {
		EntityViewFieldSettingDialog dialog = new EntityViewFieldSettingDialog(getClassName(), getValueObject(), triggerType, defName);

		// ダイアログのタイトルに対象のプロパティ名を表示
		dialog.setTitlePropertyInfo(new PropertyInfo(propertyName, ""));
		return dialog;
	}
}
