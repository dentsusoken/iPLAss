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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.event.MTPEventHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationContext;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.DefaultSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.MassReferenceSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.ReferenceSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SectionControl;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.Section;

import com.smartgwt.client.types.HeaderControls;

public class DetailFormViewControl extends ItemControl {

	/** 重複チェック用のリスト */
	private List<String> propList = new ArrayList<String>();

	/** 内部のレイアウト */
	private DetailDropLayout editArea = null;

	/** 編集開始イベントハンドラ */
	private MTPEventHandler editStartHandler;

	/** プロパティチェック用イベントハンドラ */
	private PropertyOperationHandler propertyOperationHandler;

	public DetailFormViewControl(String defName) {
		super(defName);
		setHeaderControls(HeaderControls.HEADER_LABEL, setting);
		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_DetailFormWindow_detailScreen"));
		setHeight100();
		setWidth100();
		setMargin(4);
		setCanDrag(false);
		setBorder("1px solid navy");

		//編集用のエリア
		propertyOperationHandler = new PropertyOperationHandlerImpl();
		editArea = new DetailDropLayout(defName);
		editArea.setDropTypes(new String[]{"section"});
		editArea.setPropertyOperationHandler(propertyOperationHandler);
		addItem(editArea);

		DetailFormView fv = new DetailFormView();
		fv.setLoadDefinedReferenceProperty(true);
		setValueObject(fv);
		setClassName(fv.getClass().getName());
	}

	/**
	 * 編集開始イベント設定。
	 * @param handler
	 */
	public void setEditStartHandler(MTPEventHandler handler) {
		editStartHandler = handler;
		editArea.setEditStartHandler(handler);
	}

	/**
	 * Viewをリセット。
	 */
	public void reset() {
		propList.clear();
		editArea.clear();
	}

	/**
	 * Viewの情報を展開。
	 * @param fv
	 */
	public void apply(EntityDefinition ed, DetailFormView fv) {
		setValueObject(fv);
		setClassName(fv.getClass().getName());
		if (fv.getTitle() == null) {
			setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_DetailFormWindow_detailScreen"));
		} else {
			setTitle(fv.getTitle());
		}
		editArea.setEntityDefinition(ed);
		for (Section section : fv.getSections()) {
			ItemControl window = sectionController.createControl(section, defName, FieldReferenceType.DETAIL, ed);

			if (window instanceof DefaultSectionControl) {
				((DefaultSectionControl)window).setHandlers(ed, editStartHandler, propertyOperationHandler);
			} else if (window instanceof ReferenceSectionControl) {
				((ReferenceSectionControl)window).setHandler(propertyOperationHandler);
				MTPEvent propEvent = new MTPEvent();
				String name = ((ReferenceSection) section).getPropertyName();
				propEvent.setValue("name", name);
				if (!propertyOperationHandler.check(propEvent)) {
					propertyOperationHandler.add(propEvent);

					MTPEvent sectionEvent = new MTPEvent();
					sectionEvent.setValue("name", name + ReferenceSectionControl.SECTION_SUFFIX);
					propertyOperationHandler.add(sectionEvent);
				}
				Integer count = (Integer) propertyOperationHandler.getContext().get(name + ReferenceSectionControl.SECTION_COUNT_KEY);
				if (count == null) count = 0;
				propertyOperationHandler.getContext().set(name + ReferenceSectionControl.SECTION_COUNT_KEY, ++count);
			} else if (window instanceof MassReferenceSectionControl) {
				((MassReferenceSectionControl)window).setHandler(propertyOperationHandler);
				MTPEvent event = new MTPEvent();
				event.setValue("name", ((MassReferenceSection) section).getPropertyName());
				propertyOperationHandler.add(event);
			}
			editArea.addElement(window, 0);
		}
	}

	public DetailFormView getForm() {
		DetailFormView form = (DetailFormView) getValueObject();
		form.setSections(getSection());
		return form;
	}

	/**
	 * セクション情報取得
	 * @return
	 */
	private List<Section> getSection() {
		List<Section> sections = new ArrayList<Section>();
		for (int i = 0; i < editArea.getRowNum(); i++) {
			ItemControl editWindow = editArea.getMember(0, i);
			if (editWindow instanceof SectionControl) {
				SectionControl window = (SectionControl) editWindow;
				Section section = window.getSection();
				sections.add(section);
			}
		}
		return sections;
	}

	/**
	 *  プロパティチェック用イベント
	 */
	private final class PropertyOperationHandlerImpl implements PropertyOperationHandler {
		private PropertyOperationContext context = new PropertyOperationContext();

		@Override
		public boolean check(MTPEvent event) {
			String name = (String) event.getValue("name");
			return propList.contains(name);
		}

		@Override
		public void add(MTPEvent event) {
			String name = (String) event.getValue("name");
			propList.add(name);
		}

		@Override
		public void remove(MTPEvent event) {
			String name = (String) event.getValue("name");
			propList.remove(name);
		}

		@Override
		public PropertyOperationContext getContext() {
			return context;
		}
	}
}
