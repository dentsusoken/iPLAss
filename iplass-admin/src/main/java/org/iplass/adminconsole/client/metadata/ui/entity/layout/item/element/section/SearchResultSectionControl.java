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

import java.util.ArrayList;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.EntityViewDragPane;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.VirtualPropertyDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyControl;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.Layout;


/**
 * 検索結果セクション用のウィンドウ
 * @author lis3wg
 *
 */
public class SearchResultSectionControl extends ItemControl implements SectionControl {

	/** 内部のレイアウト */
	private DropLayout layout;

	private EntityDefinition ed;

	/** 切り替えボタン（縦並び） */
	private HeaderControl vertical;
	/** 切り替えボタン（横並び） */
	private HeaderControl horizontal;

	/**
	 * コンストラクタ
	 */
	public SearchResultSectionControl(String defName, FieldReferenceType triggerType) {
		super(defName, triggerType);

		vertical = new HeaderControl(HeaderControl.DOUBLE_ARROW_DOWN, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// VLayoutベースに
				vertical.setVisible(false);
				horizontal.setVisible(true);

				SearchResultSection section = getSection();
				removeItem(layout);
				layout.destroy();

				layout = new VDropLayout();
				addItem(layout);
				restore(section);
			}});
		horizontal = new HeaderControl(HeaderControl.DOUBLE_ARROW_RIGHT, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//HLayoutベースに
				vertical.setVisible(true);
				horizontal.setVisible(false);

				SearchResultSection section = getSection();
				removeItem(layout);
				layout.destroy();

				layout = new HDropLayout();
				addItem(layout);
				restore(section);
			}});
		horizontal.setVisible(false);

		setHeaderControls(HeaderControls.HEADER_LABEL, vertical, horizontal, setting);
		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ResultWindow_searchResult"));
		setHeight("50%");
		setWidth100();
		setMargin(4);
		setCanDrag(false);
		setCanDragReposition(false);
		layout = new HDropLayout();

		addItem(layout);

		SearchResultSection section = new SearchResultSection();
		section.setDispFlag(true);
		setClassName(section.getClass().getName());
		setValueObject(section);
	}

	public void setEntityDefinition(EntityDefinition ed) {
		this.ed = ed;
	}

	/**
	 * 内部レイアウト
	 */
	private abstract class DropLayout extends Layout {

		/**
		 * コンストラクタ
		 */
		private DropLayout() {
			setCanAcceptDrop(true);
			setDropLineThickness(4);
			setBackgroundColor("#CCFFFF");
			setBorder("1px solid navy");
			setMargin(4);
			setMembersMargin(6);
			setPadding(5);

			//cond<=>resultのD&Dをさせないためにresult内でのみ操作可能なタイプも設定
			setDropTypes(EntityViewDragPane.DRAG_TYPE_PROPERTY,
					EntityViewDragPane.DRAG_TYPE_ELEMENT,
					EntityViewDragPane.DRAG_TYPE_PROPERTY + "_r");

			//ドロップ先を表示する設定
			Canvas dropLineProperties = new Canvas();
			dropLineProperties.setBackgroundColor("aqua");
			setDropLineProperties(dropLineProperties);

			//ドラッグ中のコンポーネントを表示する設定
			setShowDragPlaceHolder(true);
			Canvas placeHolderProperties = new Canvas();
			placeHolderProperties.setBorder("2px solid #8289A6");
			setPlaceHolderProperties(placeHolderProperties);

			//ドロップイベント
			addDropHandler(new DropHandlerImpl());
		}

		public abstract String getColWidth();

		/**
		 * 検索結果へのドロップイベント用ハンドラ
		 */
		private class DropHandlerImpl implements DropHandler {

			@Override
			public void onDrop(DropEvent event) {
				//ダイアログ表示後はdrop位置が替わるので、予め取得
				int dropPosition = getDropPosition();

				Canvas dragTarget = EventHandler.getDragTarget();
				if (dragTarget instanceof ListGrid) {
					//ツリーからのdropはWidgetを作成する
					if (EntityViewDragPane.DRAG_TYPE_PROPERTY.equals(dragTarget.getDragType())) {
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						PropertyControl newProperty = new PropertyControl(defName, getTriggerType(), record, new PropertyColumn());
						newProperty.setWidth(getColWidth());
						newProperty.setDragType(EntityViewDragPane.DRAG_TYPE_PROPERTY + "_r");
						addMember(newProperty, dropPosition);
					} else if (EntityViewDragPane.DRAG_TYPE_ELEMENT.equals(dragTarget.getDragType())) {
						// 仮想プロパティ
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						String name = record.getAttribute("name");
						if (VirtualPropertyItem.class.getName().equals(name)) {
							final VirtualPropertyDialog dialog = new VirtualPropertyDialog();
							dialog.addOKClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (!dialog.validate()) return;

									final String name = dialog.getPropertyName();
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

									VirtualPropertyControl newProperty = new VirtualPropertyControl(defName, FieldReferenceType.SEARCHRESULT, ed, property);
									newProperty.setWidth(DropLayout.this.getColWidth());

									DropLayout.this.addMember(newProperty, dropPosition);

									dialog.destroy();
								}
							});

							dialog.show();
						}
					}

					// cancelしないとdrop元自体が移動してしまう
					event.cancel();
				}
			}
		}
	}

	/**
	 * 内部レイアウト（横並び）
	 */
	private class HDropLayout extends DropLayout {

		/**
		 * コンストラクタ
		 */
		private HDropLayout() {
			super();
			setScClassName("HLayout");
		}


		@Override
		public String getColWidth() {
			return "100px";
		}
	}

	/**
	 * 内部レイアウト（縦並び）
	 */
	private class VDropLayout extends DropLayout {

		/**
		 * コンストラクタ
		 */
		private VDropLayout() {
			super();
			setScClassName("VLayout");
		}

		@Override
		public String getColWidth() {
			return "100%";
		}
	}

	/**
	 * 検索結果セクションを復元。
	 * @param section
	 */
	public void restore(SearchResultSection section) {
		for (Element elem : section.getElements()) {
			if (elem instanceof PropertyColumn) {
				PropertyColumn property = (PropertyColumn) elem;
				PropertyControl win = new PropertyControl(defName, getTriggerType(), property);
				win.setWidth(layout.getColWidth());
				win.setDragType(EntityViewDragPane.DRAG_TYPE_PROPERTY + "_r");
				layout.addMember(win);
			} else if (elem instanceof VirtualPropertyItem) {
				VirtualPropertyItem property = (VirtualPropertyItem) elem;
				VirtualPropertyControl win = new VirtualPropertyControl(defName, FieldReferenceType.SEARCHRESULT, ed, property);
				win.setWidth(layout.getColWidth());
				layout.addMember(win);
			}
		}

		setClassName(section.getClass().getName());
		setValueObject(section);
	}

	/**
	 * 検索結果セクションを取得。
	 * @return
	 */
	@Override
	public SearchResultSection getSection() {
		SearchResultSection section = (SearchResultSection) getValueObject();
		if (section.getElements() == null) {
			section.setElements(new ArrayList<Element>());
		} else {
			section.getElements().clear();
		}
		for (Canvas canvas : layout.getMembers()) {
			if (canvas instanceof PropertyControl) {
				PropertyColumn property = ((PropertyControl) canvas).getPropertyColumn();
				section.addElement(property);
			} else if (canvas instanceof VirtualPropertyControl) {
				VirtualPropertyItem prop = ((VirtualPropertyControl) canvas).getViewElement();
				section.addElement(prop);
			}
		}
		return section;
	}

	/**
	 * 検索結果セクションを初期化。
	 */
	@Override
	public void clear() {
		for (Canvas canvas : layout.getMembers()) {
			if (canvas instanceof ItemControl) {
				((ItemControl) canvas).destroy();
			}
		}
	}

}
