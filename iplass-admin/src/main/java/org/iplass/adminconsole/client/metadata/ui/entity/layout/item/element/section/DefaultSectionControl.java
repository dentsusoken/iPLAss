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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.HasPropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.DetailDropLayout;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.ElementControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.element.BlankSpace;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.Section;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Canvas;

/**
 * デフォルトセクション用のウィンドウ
 * @author lis3wg
 *
 */
public class DefaultSectionControl extends ItemControl implements SectionControl, HasPropertyOperationHandler {

	/** 内部のレイアウト */
	private DetailDropLayout layout = null;

	private PropertyOperationHandler propertyOperationHandler;

	private EntityDefinition ed;

	/** SectionWindowController */
	private SectionController sectionController = GWT.create(SectionController.class);

	/**
	 * コンストラクタ
	 * @param title
	 * @param colNum
	 */
	private DefaultSectionControl(final String defName, FieldReferenceType triggerType, String title, int colNum) {
		super(defName, triggerType);

		String bgColor = "#DDFFFF";


		if (title != null && !title.isEmpty()) {
			setTitle(title);
		} else {
			setTitle("Default Section");
		}
		setBackgroundColor(bgColor);
		setDragType(EntityViewDragPane.DRAG_TYPE_SECTION);
		setAutoSize(true);
		setBorder("1px solid navy");

		setHeaderControls(HeaderControls.MINIMIZE_BUTTON, HeaderControls.HEADER_LABEL, setting, HeaderControls.CLOSE_BUTTON);

		layout = new DetailDropLayout(colNum, defName);
		layout.setDropTypes(
				EntityViewDragPane.DRAG_TYPE_SECTION, 
				EntityViewDragPane.DRAG_TYPE_PROPERTY, 
				EntityViewDragPane.DRAG_TYPE_ELEMENT);
		layout.setCanDropComponents(true);
		layout.setPadding(5);
		layout.setBackgroundColor(bgColor);

		addItem(layout);

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				//カラム数が変更されていたら再構築
				DefaultSection section = (DefaultSection) event.getValue();

				if (section.getTitle() != null && !section.getTitle().isEmpty()) {
					setTitle(section.getTitle());
				} else {
					setTitle("Default Section");
				}

				if (section.getColNum() == layout.getColNum()) return;

				DetailDropLayout old = layout;
				layout = new DetailDropLayout(section.getColNum(), defName);
				layout.setPropertyOperationHandler(propertyOperationHandler);
				layout.setEntityDefinition(ed);

				//カラム変更前のレイアウトからメンバー取得
				List<ItemControl> members = new ArrayList<ItemControl>();
				int mCol = old.getColNum();
				int mRow = old.getRowNum();
				for (int i = 0; i < mRow; i++) {
					for (int j = 0; j < mCol; j++) {
						if (old.getMember(j, i) != null) {
							members.add(old.getMember(j, i));
						}
					}
				}

				//カラム変更後のレイアウトにメンバー設定
				currentCols = 0;
				for (ItemControl member : members) {
					addMember(member);
				}

				removeItem(old);
				addItem(layout);
			}
		});
	}

	/**
	 * コンストラクタ
	 * @param section
	 */
	public DefaultSectionControl(String defName, FieldReferenceType triggerType, DefaultSection section) {
		this(defName, triggerType, section.getTitle(), section.getColNum());
		setClassName(section.getClass().getName());
		setValueObject(section);
	}

	public void setEntityDefinition(EntityDefinition ed) {
		this.ed = ed;

		if (layout != null) {
			//SRB(Null Dereference)対応
			layout.setEntityDefinition(ed);
		}
	}

	@Override
	public void setPropertyOperationHandler(PropertyOperationHandler handler) {
		this.propertyOperationHandler = handler;

		if (layout != null) {
			//SRB(Null Dereference)対応
			layout.setPropertyOperationHandler(propertyOperationHandler);
		}
	}

	@Override
	protected boolean onPreDestroy() {
		for (Canvas canvas : layout.getMembers()) {
			canvas.destroy();
		}
		return true;
	}

	/**
	 * 配下のメンバーを復元
	 * @param section
	 * @param editStartHandler
	 * @param propertyOperationHandler
	 */
	public void restoreMember() {
		DefaultSection section = (DefaultSection)getValueObject();

		//配下の要素を復元
		for (Element element : section.getElements()) {
			if (element instanceof Section) {
				ItemControl child = sectionController.createControl((Section)element, defName, getTriggerType(), ed);
				if (child instanceof DefaultSectionControl) {
					DefaultSectionControl dsChild = (DefaultSectionControl)child;
					dsChild.setEntityDefinition(ed);
					dsChild.setPropertyOperationHandler(propertyOperationHandler);
					dsChild.restoreMember();
				}
				addMember(child);
			} else if (element instanceof PropertyItem) {
				PropertyControl child = new PropertyControl(defName, getTriggerType(), (PropertyItem) element);
				child.setPropertyOperationHandler(propertyOperationHandler);
				MTPEvent event = new MTPEvent();
				event.setValue("name", ((PropertyItem) element).getPropertyName());
				if (!propertyOperationHandler.check(event)) {
					propertyOperationHandler.add(event);
				}
				addMember(child);
			} else if (element instanceof VirtualPropertyItem) {
				VirtualPropertyControl child = new VirtualPropertyControl(defName, getTriggerType(), ed, (VirtualPropertyItem) element);
				child.setPropertyOperationHandler(propertyOperationHandler);
				MTPEvent event = new MTPEvent();
				event.setValue("name", ((VirtualPropertyItem) element).getPropertyName());
				if (!propertyOperationHandler.check(event)) {
					propertyOperationHandler.add(event);
				}
				addMember(child);
			} else {
				ElementControl child = new ElementControl(defName, getTriggerType(), element);
				addMember(child);
			}
		}
	}

	/**  */
	private int currentCols = 0;

	@Override
	public void addMember(Canvas component) {
		layout.addElement(component, currentCols);
		if (currentCols == layout.getColNum() - 1) {
			currentCols = 0;
		} else {
			currentCols++;
		}
	}

	/* (非 Javadoc)
	 * @see org.iplass.adminconsole.client.ui.layout.SectionWindow#getSection()
	 */
	@Override
	public DefaultSection getSection() {
		DefaultSection section = (DefaultSection) getValueObject();
		section.getElements().clear();
		section.setElements(getData());
		return section;
	}

	/**
	 * エレメント情報取得
	 * @param data
	 * @return
	 */
	private List<Element> getData() {
		List<Element> elements = new ArrayList<Element>();
		int mCol = layout.getColNum();
		int mRow = layout.getRowNum();
		for (int i = 0; i < mRow; i++) {
			for (int j = 0; j < mCol; j++) {
				ItemControl member = layout.getMember(j, i);
				if (member == null) {
					BlankSpace nElem = new BlankSpace();
					nElem.setDispFlag(true);
					elements.add(nElem);
				} else {
					if (member instanceof SectionControl) {
						SectionControl sw = (SectionControl) member;
						Section section = sw.getSection();
						elements.add(section);
					} else if (member instanceof PropertyControl) {
						PropertyItem prop = ((PropertyControl) member).getProperty();
						elements.add(prop);
					} else if (member instanceof VirtualPropertyControl) {
						VirtualPropertyItem prop = ((VirtualPropertyControl) member).getViewElement();
						elements.add(prop);
					} else if (member instanceof ElementControl) {
						Element elem = ((ElementControl) member).getViewElement();
						elements.add(elem);
					}
				}
			}
		}
		return elements;
	}

}
