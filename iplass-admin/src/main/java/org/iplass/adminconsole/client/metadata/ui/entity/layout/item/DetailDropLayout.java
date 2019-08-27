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

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.HasPropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.MultiColumnDropLayout;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.ElementControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.DefaultSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.MassReferenceSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.ReferenceSectionControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SectionController;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * ビュー編集用レイアウト
 * @author lis3wg
 */
public class DetailDropLayout extends MultiColumnDropLayout implements HasPropertyOperationHandler {

	private PropertyOperationHandler propertyOperationHandler;
	private String defName;

	private EntityDefinition ed;

	/** SectionWindowController */
	private SectionController sectionController = GWT.create(SectionController.class);

	/**
	 * コンストラクタ
	 */
	public DetailDropLayout(String defName) {
		this(1, defName);
	}

	/**
	 * コンストラクタ
	 * @param numColumns
	 */
	public DetailDropLayout(int numColumns, String defName) {
		super(numColumns);
		this.defName = defName;
	}

	public void setEntityDefinition(EntityDefinition ed) {
		this.ed = ed;
	}

	@Override
	public void setPropertyOperationHandler(PropertyOperationHandler handler) {
		propertyOperationHandler = handler;
	}

	@Override
	protected DropHandler getDropHandler() {
		return new DropHandlerImpl();
	}

	/**
	 * ドロップイベント
	 * @author lis3wg
	 *
	 */
	private final class DropHandlerImpl implements DropHandler {

		@Override
		public void onDrop(DropEvent event) {
			ColumnLayout col = (ColumnLayout) event.getSource();

			//ダイアログ表示後はdrop位置が替わるので、予め取得
			final int dropPosition = col.getDropPosition();

			//ツリーからのdropはWindowを作成する
			final Canvas dragTarget = EventHandler.getDragTarget();
			if (dragTarget instanceof ListGrid) {
				if ("section".equals(dragTarget.getDragType())) {
					//タイトルとカラム数をダイアログ経由で設定
					ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
					String name = record.getAttribute("name");

					sectionController.createControl(name, defName, FieldReferenceType.DETAIL, propertyOperationHandler, new SectionController.Callback() {

						@Override
						public void onCreated(ItemControl window) {
							if (window instanceof DefaultSectionControl) {
								DefaultSectionControl dsChild = (DefaultSectionControl)window;
								dsChild.setEntityDefinition(ed);
								dsChild.setPropertyOperationHandler(propertyOperationHandler);
								dsChild.restoreMember();
							} else if (window instanceof ReferenceSectionControl) {
								((ReferenceSectionControl)window).setPropertyOperationHandler(propertyOperationHandler);
							} else if (window instanceof MassReferenceSectionControl) {
								((MassReferenceSectionControl)window).setPropertyOperationHandler(propertyOperationHandler);
							}
							col.addMember(window, dropPosition);
						}
					});

				} else if ("property".equals(dragTarget.getDragType())) {
					ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
					MTPEvent mtpEvent = new MTPEvent();
					mtpEvent.setValue("name", record.getAttribute("name"));
					if (propertyOperationHandler != null) {
						if (!propertyOperationHandler.check(mtpEvent)) {
							PropertyControl newProperty = new PropertyControl(defName, FieldReferenceType.DETAIL, record, new PropertyItem());
							newProperty.setPropertyOperationHandler(propertyOperationHandler);
							propertyOperationHandler.add(mtpEvent);
							col.addMember(newProperty, dropPosition);
						} else {
							GWT.log(record.getAttribute("name") + " is already added.");
						}
					} else {
						//プロパティの重複チェックしないケースあるか？？
						GWT.log(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propCheckThrought"));
						PropertyControl newProperty = new PropertyControl(defName, FieldReferenceType.DETAIL, record, new PropertyItem());
						col.addMember(newProperty, dropPosition);
					}

				} else if ("element".equals(dragTarget.getDragType())) {
					ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
					String name = record.getAttribute("name");
					if (VirtualPropertyItem.class.getName().equals(name)) {
						final VirtualPropertyDialog dialog = new VirtualPropertyDialog();
						dialog.addOKClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								//OK押下時はウィンドウ追加

								if (!dialog.validate()) return;

								final String name = dialog.getPropertyName();
								MTPEvent mtpEvent = new MTPEvent();
								mtpEvent.setValue("name", name);
								if (propertyOperationHandler.check(mtpEvent)) {
									SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropExistsErr"));
									return;
								}
								if (ed.getProperty(name) != null) {
									SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_checkPropDefExistsErr"));
									return;
								}

								VirtualPropertyItem property = new VirtualPropertyItem();
								property.setDispFlag(true);
								property.setPropertyName(name);
								property.setDisplayLabel(dialog.getDisplayLabel());
								StringPropertyEditor editor = new StringPropertyEditor();
								editor.setDisplayType(StringDisplayType.TEXT);
								property.setEditor(editor);

								VirtualPropertyControl newProperty = new VirtualPropertyControl(defName, FieldReferenceType.DETAIL, ed, property);
								newProperty.setPropertyOperationHandler(propertyOperationHandler);

								col.addMember(newProperty, dropPosition);
								dialog.destroy();
							}
						});

						dialog.show();
					} else {
						ElementControl newElement = new ElementControl(defName, FieldReferenceType.DETAIL, record);
						col.addMember(newElement, dropPosition);
					}
				}

				// cancelしないとdrop元自体が移動してしまう
				event.cancel();
			}
		}
	}

}
