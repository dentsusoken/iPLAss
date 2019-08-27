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
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.MultiColumnDropLayout;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationContext;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.VirtualPropertyDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.ElementControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.element.BlankSpace;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.HeaderControls;
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
 * 検索条件セクション用のウィンドウ
 * @author lis3wg
 *
 */
public class SearchConditionSectionControl extends ItemControl implements SectionControl {

	/** 重複チェック用のリスト */
	private List<String> propList = new ArrayList<String>();

	/** 内部のレイアウト */
	private DropLayout layout;

	/** プロパティチェック用のイベントハンドラ */
	private PropertyOperationHandler handler;

	private EntityDefinition ed;

	/**
	 * コンストラクタ
	 */
	public SearchConditionSectionControl(String defName, FieldReferenceType triggerType) {
		super(defName, triggerType);
		setHeaderControls(HeaderControls.HEADER_LABEL, setting);
		setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_item_ConditionWindow_searchCondition"));
		setHeight("50%");
		setWidth100();
		setMargin(4);
		setCanDrag(false);
		setCanDragReposition(false);
		handler = new PropertyOperationHandlerImpl();

		SearchConditionSection section = new SearchConditionSection();
		section.setDispFlag(true);
		section.setColNum(1);
		section.setConditionDispCount(5);
		setClassName(section.getClass().getName());
		setValueObject(section);

		layout = new DropLayout(section.getColNum());
		addItem(layout);

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				//カラム数が変更されていたら再構築
				SearchConditionSection section = (SearchConditionSection) event.getValue();

				if (section.getColNum() == layout.getColNum()) return;

				DropLayout old = layout;
				layout = new DropLayout(section.getColNum());

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
	 * 内部レイアウト
	 */
	private class DropLayout extends MultiColumnDropLayout {

		/**
		 * コンストラクタ
		 */
		private DropLayout(int colNum) {
			super(colNum);

			setBackgroundColor("#CCFFFF");
			setBorder("1px solid navy");
			setMargin(4);
			setMembersMargin(6);
			setPadding(5);

			//cond<=>resultのD&DをさせないためにCondition内でのみ操作可能なタイプも設定
			setDropTypes("property", "property_c", "element");
		}

		@Override
		protected DropHandler getDropHandler() {
			return new DropHandlerImpl();
		}

		/**
		 * 検索条件へのドロップイベント用ハンドラ
		 */
		private class DropHandlerImpl implements DropHandler {

			@Override
			public void onDrop(DropEvent event) {
				ColumnLayout col = (ColumnLayout) event.getSource();

				//ダイアログ表示後はdrop位置が替わるので、予め取得
				int dropPosition = col.getDropPosition();

				Canvas dragTarget = EventHandler.getDragTarget();
				if (dragTarget instanceof ListGrid) {
					//ツリーからのdropはWidgetを作成する
					if ("property".equals(dragTarget.getDragType())) {
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						String name = record.getAttribute("name");
						if (!propList.contains(name)) {
							PropertyControl newProperty = new PropertyControl(defName, getTriggerType(), record, new PropertyItem());
							newProperty.setDragType("property_c");
							newProperty.setPropertyOperationHandler(handler);
							propList.add(name);
							col.addMember(newProperty, dropPosition);
						} else {
							GWT.log(record.getAttribute("name") + " is already added.");
						}
					} else if ("element".equals(dragTarget.getDragType())) {
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						String name = record.getAttribute("name");
						if (VirtualPropertyItem.class.getName().equals(name)) {
							final VirtualPropertyDialog dialog = new VirtualPropertyDialog();
							dialog.addOKClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (!dialog.validate()) return;

									final String name = dialog.getPropertyName();
									if (propList.contains(name)) {
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

									VirtualPropertyControl newProperty = new VirtualPropertyControl(defName, FieldReferenceType.SEARCHCONDITION, ed, property);
									newProperty.setPropertyOperationHandler(new PropertyOperationHandler() {
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
											return null;
										}
									});

									col.addMember(newProperty, dropPosition);
									propList.add(name);
									dialog.destroy();
								}
							});
							dialog.show();
						} else if (name.equals(BlankSpace.class.getName())) {
							BlankSpace space = new BlankSpace();
							space.setDispFlag(true);
							ElementControl blank = new ElementControl(defName, getTriggerType(), space);
							col.addMember(blank);
						}
					}

					// cancelしないとdrop元自体が移動してしまう
					event.cancel();
				}
			}
		}
	}

	public void setEntityDefinition(EntityDefinition ed) {
		this.ed = ed;
	}

	/**
	 * 検索条件のプロパティ削除用イベントハンドラ
	 */
	private class PropertyOperationHandlerImpl implements
			PropertyOperationHandler {

		private PropertyOperationHandlerImpl() {
		}

		@Override
		public void remove(MTPEvent event) {
			String name = (String) event.getValue("name");
			propList.remove(name);
		}

		@Override
		public boolean check(MTPEvent event) {
			return false;
		}

		@Override
		public void add(MTPEvent event) {}

		@Override
		public PropertyOperationContext getContext() {
			return null;
		}
	}

	/**
	 * 検索条件セクションを復元。
	 * @param section
	 */
	public void restore(SearchConditionSection section) {
		//列数追加前のデータは0になってるため、補正する
		if (section.getColNum() == 0) section.setColNum(1);

		setClassName(section.getClass().getName());
		setValueObject(section);

		clear();

		for (Element elem : section.getElements()) {
			if (elem instanceof PropertyItem) {
				PropertyItem property = (PropertyItem) elem;
				if (property.isBlank()) {
					BlankSpace space = new BlankSpace();
					space.setDispFlag(true);
					ElementControl win = new ElementControl(defName, getTriggerType(), space);
					addMember(win);
				} else {
					PropertyControl win = new PropertyControl(defName, getTriggerType(), property);
					win.setDragType("property_c");

					String name = property.getPropertyName();

					win.setPropertyOperationHandler(handler);
					addMember(win);

					propList.add(name);
				}
			} else if (elem instanceof VirtualPropertyItem) {
				VirtualPropertyItem property = (VirtualPropertyItem) elem;
				VirtualPropertyControl win = new VirtualPropertyControl(defName, FieldReferenceType.SEARCHCONDITION, ed, property);
				addMember(win);
			} else if (elem instanceof BlankSpace) {
				ElementControl win = new ElementControl(defName, getTriggerType(), elem);
				addMember(win);
			}
		}
	}

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

	/**
	 * 検索条件セクションを取得。
	 * @return
	 */
	public SearchConditionSection getSection() {
		SearchConditionSection section = (SearchConditionSection) getValueObject();
		if (section.getElements() == null) {
			section.setElements(new ArrayList<Element>());
		} else {
			section.getElements().clear();
		}
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
					//ブランクのデータ
					BlankSpace blank = new BlankSpace();
					blank.setDispFlag(true);
					elements.add(blank);
				} else {
					if (member instanceof PropertyControl) {
						PropertyItem prop = ((PropertyControl) member).getProperty();
						elements.add(prop);
					} else if (member instanceof VirtualPropertyControl) {
						VirtualPropertyItem prop = ((VirtualPropertyControl) member).getViewElement();
						elements.add(prop);
					} else if (member instanceof ElementControl) {
						Element element = ((ElementControl) member).getViewElement();
						elements.add(element);
					}
				}
			}
		}
		return elements;
	}

	/**
	 * 検索条件セクションを初期化。
	 */
	public void clear() {
		propList.clear();

		currentCols = 0;
		if (layout != null && contains(layout)) {
			removeItem(layout);
		}

		SearchConditionSection section = (SearchConditionSection) getValueObject();
		layout = new DropLayout(section.getColNum());
		addItem(layout);
	}

}
