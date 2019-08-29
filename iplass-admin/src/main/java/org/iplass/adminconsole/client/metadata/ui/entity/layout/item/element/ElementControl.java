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
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.BlankSpace;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.Link;
import org.iplass.mtp.view.generic.element.ScriptingElement;
import org.iplass.mtp.view.generic.element.TemplateElement;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * エレメント用のウィンドウ
 * @author lis3wg
 *
 */
public class ElementControl extends ItemControl {

	/**
	 * コンストラクタ
	 */
	private ElementControl(String defName, FieldReferenceType triggerType) {
		super(defName, triggerType);

		setDragType(EntityViewDragPane.DRAG_TYPE_ELEMENT);

		setShowMinimizeButton(false);
		setBackgroundColor("#FFFFCC");
		setBorder("1px solid olive");
		setHeight(22);
	}

	/**
	 * コンストラクタ
	 * @param record
	 */
	public ElementControl(String defName, FieldReferenceType triggerType, ListGridRecord record) {
		this(defName, triggerType);
		setTitle(record.getAttribute("displayName"));
		setClassName(record.getAttribute("name"));
		setValueObject(createElement());
	}

	/**
	 * コンストラクタ
	 * @param element
	 */
	public ElementControl(String defName, FieldReferenceType triggerType, Element element) {
		this(defName, triggerType);

		//TODO 復元時に保持する内容
		if (element instanceof Button){
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_btn"));
		} else if (element instanceof Link) {
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_link"));
		} else if (element instanceof ScriptingElement) {
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_script"));
		} else if (element instanceof TemplateElement) {
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_template"));
		} else {
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_space"));
		}
		setClassName(element.getClass().getName());
		setValueObject(element);
	}

	/**
	 * エレメントを取得。
	 * @return
	 */
	public Element getViewElement() {
		return (Element) getValueObject();
	}

	/**
	 * エレメントを生成。
	 * @return
	 */
	private Element createElement() {
		String name = getClassName();
		if (Button.class.getName().equals(name)) {
			Button b = new Button();
			b.setDispFlag(true);
			b.setDisplayLabel(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_btn"));
			return b;
		} else if (ScriptingElement.class.getName().equals(name)) {
			ScriptingElement script = new ScriptingElement();
			script.setDispFlag(true);
			return script;
		} else if (TemplateElement.class.getName().equals(name)) {
			TemplateElement template = new TemplateElement();
			template.setDispFlag(true);
			return template;
		} else if (Link.class.getName().equals(name)) {
			Link link = new Link();
			link.setDispFlag(true);
			link.setDisplayLabel(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ElementWindow_link"));
			return link;
		} else {
			BlankSpace blank = new BlankSpace();
			blank.setDispFlag(true);
			return blank;
		}
	}
}
