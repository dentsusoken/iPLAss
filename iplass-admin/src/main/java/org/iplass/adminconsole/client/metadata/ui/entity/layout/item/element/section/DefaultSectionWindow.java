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
import org.iplass.adminconsole.client.base.event.MTPEventHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.DetailDropLayout;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ViewEditWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.ElementWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyElementWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyBaseWindow;
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
public class DefaultSectionWindow extends ViewEditWindow implements SectionWindow {

	/** 内部のレイアウト */
	private DetailDropLayout layout = null;
	private MTPEventHandler editStartHandler;
	private PropertyOperationHandler propertyOperationHandler;

	private EntityDefinition ed;

	/** SectionWindowController */
	private SectionWindowController sectionController = GWT.create(SectionWindowController.class);

	/**
	 * コンストラクタ
	 * @param title
	 * @param colNum
	 */
	private DefaultSectionWindow(final String defName, FieldReferenceType triggerType, String title, int colNum) {
		super(defName, triggerType);

		String bgColor = "#DDFFFF";


		if (title != null && !title.isEmpty()) {
			setTitle(title);
		} else {
			setTitle("Default Section");
		}
		setBackgroundColor(bgColor);
		setDragType("section");
		setAutoSize(true);
		setBorder("1px solid navy");

		setHeaderControls(HeaderControls.MINIMIZE_BUTTON, HeaderControls.HEADER_LABEL, setting, HeaderControls.CLOSE_BUTTON);

		layout = new DetailDropLayout(colNum, defName);
		layout.setDropTypes("section", "property", "element");
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
				layout.setEditStartHandler(editStartHandler);
				layout.setPropertyOperationHandler(propertyOperationHandler);
				layout.setEntityDefinition(ed);

				//カラム変更前のレイアウトからメンバー取得
				List<ViewEditWindow> members = new ArrayList<ViewEditWindow>();
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
				for (ViewEditWindow member : members) {
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
	public DefaultSectionWindow(String defName, FieldReferenceType triggerType, DefaultSection section) {
		this(defName, triggerType, section.getTitle(), section.getColNum());
		setClassName(section.getClass().getName());
		setValueObject(section);
	}

	public void setHandlers(EntityDefinition ed, MTPEventHandler editStartHandler, PropertyOperationHandler propertyOperationHandler) {
		this.ed = ed;
		this.editStartHandler = editStartHandler;
		this.propertyOperationHandler = propertyOperationHandler;

		if (layout != null) {
			//SRB(Null Dereference)対応
			//this(final String defName, String title, int colNum)で生成されるのであり得ないが
			layout.setEditStartHandler(editStartHandler);
			layout.setPropertyOperationHandler(propertyOperationHandler);
			layout.setEntityDefinition(ed);
		}

		restoreMember();
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
	private void restoreMember() {
		DefaultSection section = (DefaultSection)getValueObject();

		//配下の要素を復元
		for (Element element : section.getElements()) {
			if (element instanceof Section) {
				ViewEditWindow child = sectionController.createWindow((Section)element, defName, getTriggerType(), ed);
				if (child instanceof DefaultSectionWindow) {
					((DefaultSectionWindow)child).setHandlers(ed, editStartHandler, propertyOperationHandler);
				}
				addMember(child);
			} else if (element instanceof PropertyItem) {
				PropertyBaseWindow child = new PropertyBaseWindow(defName, getTriggerType(), (PropertyItem) element);
				child.setHandler(propertyOperationHandler);
				MTPEvent event = new MTPEvent();
				event.setValue("name", ((PropertyItem) element).getPropertyName());
				if (!propertyOperationHandler.check(event)) {
					propertyOperationHandler.add(event);
				}
				addMember(child);
			} else if (element instanceof VirtualPropertyItem) {
				VirtualPropertyElementWindow child = new VirtualPropertyElementWindow(defName, getTriggerType(), ed, (VirtualPropertyItem) element);
				child.setHandler(propertyOperationHandler);
				MTPEvent event = new MTPEvent();
				event.setValue("name", ((VirtualPropertyItem) element).getPropertyName());
				if (!propertyOperationHandler.check(event)) {
					propertyOperationHandler.add(event);
				}
				addMember(child);
			} else {
				ElementWindow child = new ElementWindow(defName, getTriggerType(), element);
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
				ViewEditWindow member = layout.getMember(j, i);
				if (member == null) {
					BlankSpace nElem = new BlankSpace();
					nElem.setDispFlag(true);
					elements.add(nElem);
				} else {
					if (member instanceof SectionWindow) {
						SectionWindow sw = (SectionWindow) member;
						Section section = sw.getSection();
						elements.add(section);
					} else if (member instanceof PropertyBaseWindow) {
						PropertyItem prop = ((PropertyBaseWindow) member).getProperty();
						elements.add(prop);
					} else if (member instanceof VirtualPropertyElementWindow) {
						VirtualPropertyItem prop = ((VirtualPropertyElementWindow) member).getViewElement();
						elements.add(prop);
					} else if (member instanceof ElementWindow) {
						Element elem = ((ElementWindow) member).getViewElement();
						elements.add(elem);
					}
				}
			}
		}
		return elements;
	}

}
