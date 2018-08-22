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
import org.iplass.adminconsole.client.base.event.MTPEventHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.MultiColumnDropLayout;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.ElementWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyElementWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyBaseWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.DefaultSectionWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.MassReferenceSectionWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.ReferenceSectionWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section.SectionWindowController;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * ビュー編集用レイアウト
 * @author lis3wg
 */
public class DetailDropLayout extends MultiColumnDropLayout {

	//getMemberの代替用
	private MTPEventHandler editStartHandler;
	private PropertyOperationHandler propertyOperationHandler;
	private String defName;

	private EntityDefinition ed;

	/** SectionWindowController */
	private SectionWindowController sectionController = GWT.create(SectionWindowController.class);

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

	/**
	 * 編集開始用のイベントハンドラ設定
	 * @param handler
	 */
	public void setEditStartHandler(MTPEventHandler handler) {
		editStartHandler = handler;
	}

	/**
	 * プロパティの重複チェック用ハンドラ設定
	 * @param handler
	 */
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

					sectionController.createWindow(name, defName, FieldReferenceType.DETAIL, propertyOperationHandler, new SectionWindowController.Callback() {

						@Override
						public void onCreated(ViewEditWindow window) {
							if (window instanceof DefaultSectionWindow) {
								((DefaultSectionWindow)window).setHandlers(ed, editStartHandler, propertyOperationHandler);
							} else if (window instanceof ReferenceSectionWindow) {
								((ReferenceSectionWindow)window).setHandler(propertyOperationHandler);
							} else if (window instanceof MassReferenceSectionWindow) {
								((MassReferenceSectionWindow)window).setHandler(propertyOperationHandler);
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
							PropertyBaseWindow newProperty = new PropertyBaseWindow(defName, FieldReferenceType.DETAIL, record, new PropertyItem());
							newProperty.setHandler(propertyOperationHandler);
							propertyOperationHandler.add(mtpEvent);
							col.addMember(newProperty, dropPosition);
						} else {
							GWT.log(record.getAttribute("name") + " is already added.");
						}
					} else {
						//プロパティの重複チェックしないケースあるか？？
						GWT.log(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propCheckThrought"));
						PropertyBaseWindow newProperty = new PropertyBaseWindow(defName, FieldReferenceType.DETAIL, record, new PropertyItem());
						col.addMember(newProperty, dropPosition);
					}

				} else if ("element".equals(dragTarget.getDragType())) {
					ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
					String name = record.getAttribute("name");
					if (VirtualPropertyItem.class.getName().equals(name)) {
						VirtualPropertyDialog dialog = new VirtualPropertyDialog(dropPosition, col);
						dialog.show();
					} else {
						ElementWindow newElement = new ElementWindow(defName, FieldReferenceType.DETAIL, record);
						col.addMember(newElement, dropPosition);
					}
				}

				// cancelしないとdrop元自体が移動してしまう
				event.cancel();
			}

			if (editStartHandler != null) {
				editStartHandler.execute(new MTPEvent());
			}
		}
	}

	/**
	 * 仮想プロパティ追加時の入力ダイアログ
	 */
	private class VirtualPropertyDialog extends AbstractWindow {
		private TextItem propName = null;
		private TextItem displayLabel = null;
		private IButton ok = null;
		private IButton cancel = null;
		private DynamicForm form = null;

		private VirtualPropertyDialog(final int dropPosition, final ColumnLayout col) {
			setWidth(300);
			setHeight(130);
			setTitle("VirtualProperty Setting");
			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(false);
			centerInPage();

			propName = new TextItem();
			propName.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propName"));
			propName.setRequired(true);

			displayLabel = new TextItem();
			displayLabel.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_displayLabel"));
			displayLabel.setRequired(true);

			ok = new IButton("OK");
			cancel = new IButton("cancel");

			//OK押下時はウィンドウ追加
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					if (!form.validate()) return;

					final String name = propName.getValueAsString();
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
					property.setDisplayLabel(displayLabel.getValueAsString());
					StringPropertyEditor editor = new StringPropertyEditor();
					editor.setDisplayType(StringDisplayType.TEXT);
					property.setEditor(editor);

					VirtualPropertyElementWindow newProperty = new VirtualPropertyElementWindow(defName, FieldReferenceType.DETAIL, ed, property);
					newProperty.setHandler(propertyOperationHandler);

					col.addMember(newProperty, dropPosition);
					destroy();
				}
			});

			//Cancel押下時はダイアログを閉じる
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			form = new DynamicForm();
			form.setAutoFocus(true);
			form.setWidth100();
			form.setPadding(5);
			form.setFields(propName, displayLabel);

			HLayout hl = new HLayout();
			hl.setAlign(Alignment.CENTER);
			hl.setAlign(VerticalAlignment.CENTER);
			hl.addMember(ok);
			hl.addMember(cancel);

			addItem(form);
			addItem(hl);
		}
	}

}
