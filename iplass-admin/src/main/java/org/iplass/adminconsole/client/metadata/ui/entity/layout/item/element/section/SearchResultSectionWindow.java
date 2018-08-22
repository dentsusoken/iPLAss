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
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationContext;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ViewEditWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.VirtualPropertyElementWindow;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.property.PropertyBaseWindow;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HeaderControl;
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
import com.smartgwt.client.widgets.layout.Layout;


/**
 * 検索結果セクション用のウィンドウ
 * @author lis3wg
 *
 */
public class SearchResultSectionWindow extends ViewEditWindow implements SectionWindow {
	/** 重複チェック用のリスト */
	private List<String> propList = new ArrayList<String>();

	/** 編集開始イベントハンドラ */
	private MTPEventHandler editStartHandler;

	/** 内部のレイアウト */
	private DropLayout layout;

	/** プロパティチェック用のイベントハンドラ */
	private PropertyOperationHandler handler;

	private EntityDefinition ed;

	/** 切り替えボタン（縦並び） */
	private HeaderControl vertical;
	/** 切り替えボタン（横並び） */
	private HeaderControl horizontal;

	/**
	 * コンストラクタ
	 */
	public SearchResultSectionWindow(String defName, FieldReferenceType triggerType) {
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
		handler = new PropertyOperationHandlerImpl();

		addItem(layout);

		SearchResultSection section = new SearchResultSection();
		section.setDispFlag(true);
		section.setDispRowCount(5);
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
			setDropTypes("property", "property_r", "element");

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
					if ("property".equals(dragTarget.getDragType())) {
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						String name = record.getAttribute("name");
						if (!propList.contains(name)) {
							PropertyBaseWindow newProperty = new PropertyBaseWindow(defName, getTriggerType(), record, new PropertyColumn());
							newProperty.setWidth(getColWidth());
							newProperty.setDragType("property_r");
							newProperty.setHandler(handler);
							propList.add(name);
							addMember(newProperty, dropPosition);
						} else {
							GWT.log(record.getAttribute("name") + " is already added.");
						}
					} else if ("element".equals(dragTarget.getDragType())) {
						// 仮想プロパティ
						ListGridRecord record = ((ListGrid) dragTarget).getSelectedRecord();
						String name = record.getAttribute("name");
						if (VirtualPropertyItem.class.getName().equals(name)) {
							VirtualPropertyDialog dialog = new VirtualPropertyDialog(dropPosition, DropLayout.this);
							dialog.show();
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
	 * 編集開始イベントハンドラを設定。
	 * @param handler
	 */
	public void setEditStartHandler(MTPEventHandler handler) {
		editStartHandler = handler;
	}

	/**
	 * 検索結果のプロパティ削除用イベントハンドラ。
	 */
	private class PropertyOperationHandlerImpl implements
			PropertyOperationHandler {

		@Override
		public void remove(MTPEvent event) {
			String name = (String) event.getValue("name");
			propList.remove(name);
		}

		@Override
		public boolean check(MTPEvent event) {
			//使用しない
			return false;
		}

		@Override
		public void add(MTPEvent event) {
			//使用しない
		}

		@Override
		public PropertyOperationContext getContext() {
			return null;
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
				PropertyBaseWindow win = new PropertyBaseWindow(defName, getTriggerType(), property);
				win.setWidth(layout.getColWidth());
				win.setDragType("property_r");

				String name = property.getPropertyName();

				win.setHandler(handler);
				layout.addMember(win);

				propList.add(name);
			} else if (elem instanceof VirtualPropertyItem) {
				VirtualPropertyItem property = (VirtualPropertyItem) elem;
				VirtualPropertyElementWindow win = new VirtualPropertyElementWindow(defName, FieldReferenceType.SEARCHRESULT, ed, property);
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
			if (canvas instanceof PropertyBaseWindow) {
				PropertyColumn property = ((PropertyBaseWindow) canvas).getPropertyColumn();
				section.addElement(property);
			} else if (canvas instanceof VirtualPropertyElementWindow) {
				VirtualPropertyItem prop = ((VirtualPropertyElementWindow) canvas).getViewElement();
				section.addElement(prop);
			}
		}
		return section;
	}

	/**
	 * 検索結果セクションを初期化。
	 */
	public void clear() {
		propList.clear();
		for (Canvas canvas : layout.getMembers()) {
			if (canvas instanceof ViewEditWindow) {
				((ViewEditWindow) canvas).destroy();
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

		private VirtualPropertyDialog(final int dropPosition, final DropLayout dropLayout) {
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
					property.setDisplayLabel(displayLabel.getValueAsString());
					StringPropertyEditor editor = new StringPropertyEditor();
					editor.setDisplayType(StringDisplayType.TEXT);
					property.setEditor(editor);

					VirtualPropertyElementWindow newProperty = new VirtualPropertyElementWindow(defName, FieldReferenceType.SEARCHRESULT, ed, property);
					newProperty.setWidth(dropLayout.getColWidth());
					newProperty.setHandler(new PropertyOperationHandler() {
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

					dropLayout.addMember(newProperty, dropPosition);
					propList.add(name);

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
