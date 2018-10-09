/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.treeview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.LocalizedStringSettingDialog;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.view.treeview.TreeViewGridColModel;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TreeViewGridColModelPane extends VLayout {

	private TreeViewGridColModelGrid grid;

	public TreeViewGridColModelPane() {
		setHeight(180);
		setWidth100();

		Label label = new Label(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_treeGridSetting"));
		label.setHeight(20);
		addMember(label);

		grid = new TreeViewGridColModelGrid();
		addMember(grid);

		IButton add = new IButton("Add");
		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final TreeViewGridColModelEditDialog dialog = new TreeViewGridColModelEditDialog();
				dialog.addDataChangeHandler(new DataChangedHandler() {

					@Override
					public void onDataChanged(DataChangedEvent event) {
						TreeViewGridColModel definition = event.getValue(TreeViewGridColModel.class, "definition");
						TreeViewGridColModelRecord record = new TreeViewGridColModelRecord(definition);
						if (!grid.addItem(record)) {
							SC.say(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_settingAlreadyExists"));
						} else {
							dialog.destroy();
						}
					}
				});
				dialog.show();
			}
		});
		addMember(add);

	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangedHandler(DataChangedHandler handler) {
		grid.addDataChangedHandler(handler);
	}

	public List<TreeViewGridColModel> getDefinition() {
		return grid.getColModel();
	}

	public void setDefinition(List<TreeViewGridColModel> colModels) {
		grid.setColModel(colModels);
	}

	public class TreeViewGridColModelGrid extends ListGrid {

		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		/**
		 * コンストラクタ
		 */
		public TreeViewGridColModelGrid() {
			setHeight(135);

			setDragDataAction(DragDataAction.MOVE);
			setSelectionType(SelectionStyle.SINGLE);	//単一行選択

			setLeaveScrollbarGap(false);		//←falseで縦スクロールバーの領域が自動表示
			setEmptyMessage("No ColModel Items");	//←空の場合のメッセージ
			setCanReorderRecords(true);			//←Dragによるレコードの並べ替え許可指定（Default false）
			setCanAcceptDroppedRecords(true);	//←レコードのDropの許可指定（Default false）
			setCanDragRecordsOut(true);			//←レコードをDragして他にDropできるか（Default false）
			setCanSort(false);					//←ソートできるか（Default true）
			setCanFreezeFields(false);			//←列を固定できるか（Default null）
			setCanPickFields(false);			//←ヘッダで列を選択できるか（Default true）
			setDropTypes("ColModel");
			setDragType("ColModel");

			setFields(getGridFields());

			//この２つを指定することでcreateRecordComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);
		}

		protected ListGridField[] getGridFields() {
			ListGridField[] fields = new ListGridField[]{
					new ListGridField("name", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_columnName")),
					new ListGridField("displayLabel", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_columnDisplayName")),
					new ListGridField("editAction", " "),
					new ListGridField("delAction", " ")
			};
			fields[2].setWidth(25);
			fields[3].setWidth(25);
			return fields;
		}

		@Override
		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			String fieldName = this.getFieldName(colNum);
			Canvas ret = null;
			if (fieldName.equals("editAction")) {
				ImgButton editBtn = new ImgButton();
				editBtn.setShowDown(false);
				editBtn.setShowRollOver(false);
				editBtn.setLayoutAlign(Alignment.CENTER);
				editBtn.setSrc("icon_edit.png");
				editBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_editItem"));
				editBtn.setHeight(16);
				editBtn.setWidth(16);
				editBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						final TreeViewGridColModelRecord target = (TreeViewGridColModelRecord) record;
						final TreeViewGridColModel definition = target.getValueObject();
						final TreeViewGridColModelEditDialog dialog = new TreeViewGridColModelEditDialog();
						dialog.setDefinition(definition);
						dialog.addDataChangeHandler(new DataChangedHandler() {

							@Override
							public void onDataChanged(DataChangedEvent event) {
								TreeViewGridColModel _definition = event.getValue(TreeViewGridColModel.class, "definition");
								if (!definition.getName().equals(_definition.getName()) && grid.existSameNameNode(_definition.getName())) {
									SC.say(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_settingAlreadyExists"));
								} else {
									target.setDefinition(_definition);
									TreeViewGridColModelGrid.this.refreshFields();

									dialog.destroy();

									fireDataChanged();
								}
							}
						});
						dialog.show();
					}
				});
				ret = editBtn;
			} else if (fieldName.equals("delAction")) {
				ImgButton deleteBtn = new ImgButton();
				deleteBtn.setShowDown(false);
				deleteBtn.setShowRollOver(false);
				deleteBtn.setLayoutAlign(Alignment.CENTER);
				deleteBtn.setSrc("remove.png");
				deleteBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_deleteItem"));
				deleteBtn.setHeight(16);
				deleteBtn.setWidth(16);
				deleteBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						SC.confirm(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_deleteConfirm"),
								AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_deleteItemConf", record.getAttribute("name"))
								, new BooleanCallback() {

							@Override
							public void execute(Boolean value) {
								if (value) {
									removeData(record);

									fireDataChanged();
								}
							}
						});
					}
				});
				ret = deleteBtn;
			}
			return ret;

		}

		/**
		 * ColModelを設定します。
		 * @param definition ColModel
		 */
		public void setColModel(List<TreeViewGridColModel> colModels) {
			clearColModel();
			if (colModels != null && colModels.size() > 0) {
				for (TreeViewGridColModel colModel : colModels) {
					TreeViewGridColModelRecord record = new TreeViewGridColModelRecord(colModel);
					addItem(record);
				}
			}
		}

		/**
		 * ColModelを取得します。
		 * @param definition ColModel
		 */
		public List<TreeViewGridColModel> getColModel() {
			List<TreeViewGridColModel> colModels = new ArrayList<TreeViewGridColModel>();
			for (ListGridRecord record : getRecords()) {
				TreeViewGridColModelRecord _record = (TreeViewGridColModelRecord) record;
				TreeViewGridColModel colModel = _record.getValueObject();
				colModels.add(colModel);
			}
			return colModels;
		}

		/**
		 * 設定されたColModelをクリアします。
		 */
		public void clearColModel() {
			for (ListGridRecord record : getRecords()) {
				removeData(record);
			}

			fireDataChanged();
		}

		/**
		 * ColModelノードを生成します。
		 * @param items
		 * @return
		 */
		protected boolean addItem(TreeViewGridColModelRecord record) {
			if (existSameNameNode(record.getName())) {
				return false;
			}

			addData(record);

			fireDataChanged();

			return true;
		}

		public boolean existSameNameNode(String name) {
			for (ListGridRecord gridRecord : getRecords()) {
				TreeViewGridColModelRecord record = (TreeViewGridColModelRecord) gridRecord;
				if (name.equals(record.getName())) {
					//同名の項目がある
					return true;
				}
			}
			return false;
		}

		/**
		 * {@link DataChangedHandler} を追加します。
		 *
		 * @param handler {@link DataChangedHandler}
		 */
		public void addDataChangedHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		/**
		 * データ変更通知処理
		 */
		public void fireDataChanged() {
			DataChangedEvent event = new DataChangedEvent();
			event.setValue("colModel", (Serializable) getColModel());
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

	/**
	 * ColModelノード
	 * @author lis3wg
	 *
	 */
	public class TreeViewGridColModelRecord extends ListGridRecord {
		public TreeViewGridColModelRecord() {
		}

		public TreeViewGridColModelRecord(TreeViewGridColModel definition) {
			this();

			setDefinition(definition);
		}

		/**
		 * 名前取得
		 * @return
		 */
		public String getName() {
			return getAttribute("name");
		}

		/**
		 * ColModel取得
		 * @return
		 */
		public TreeViewGridColModel getValueObject() {
			return (TreeViewGridColModel) getAttributeAsObject("valueObject");
		}

		/**
		 * ColModel設定
		 * @param definition
		 */
		public void setDefinition(TreeViewGridColModel definition) {

			setAttribute("name", definition.getName());
			setAttribute("displayLabel", definition.getDisplayLabel());
			setAttribute("valueObject", definition);
		}
	}

	/**
	 * ColModel編集ダイアログ
	 * @author lis3wg
	 *
	 */
	public class TreeViewGridColModelEditDialog extends AbstractWindow {

		/** データ変更ハンドラ */
		private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

		private DynamicForm form;

		private TextItem nameField;
		private TextItem displayLabelField;
		private IntegerItem widthField;
		private SelectItem alignField;

		private ButtonItem langBtn;

		private HLayout footer;
		private IButton save;
		private IButton cancel;

		private List<LocalizedStringDefinition> localizedDisplayLabelList;

		public TreeViewGridColModelEditDialog() {

			setTitle("ColModel Setting");

			setWidth(400);
			setHeight(180);

			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(true);

			form = new DynamicForm();
			form.setColWidths(120, "*", "*");
			form.setNumCols(3);

			nameField = new TextItem("name", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_columnName"));
			nameField.setWidth(220);
			nameField.setRequired(true);
			nameField.setColSpan(3);

			displayLabelField = new TextItem("displayLabel", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_displayLabel"));
			displayLabelField.setWidth(134);

			langBtn = new ButtonItem("addDisplayLabel", "Languages");
			langBtn.setShowTitle(false);
			langBtn.setIcon("world.png");
			langBtn.setStartRow(false);	//これを指定しないとButtonの場合、先頭にくる
			langBtn.setEndRow(false);	//これを指定しないと次のFormItemが先頭にいく
			langBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_eachLangDspName"));
			langBtn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

					if (localizedDisplayLabelList == null) {
						localizedDisplayLabelList = new ArrayList<LocalizedStringDefinition>();
					}
					LocalizedStringSettingDialog dialog = new LocalizedStringSettingDialog(localizedDisplayLabelList);
					dialog.show();

				}
			});

			widthField = new IntegerItem("width", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_width"));
			widthField.setColSpan(3);

			LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			valueMap.put("", "");
			valueMap.put("left", "left");
			valueMap.put("center", "center");
			valueMap.put("right", "right");
			alignField = new SelectItem("align", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGridColModelPane_align"));
			alignField.setValueMap(valueMap);
			alignField.setDefaultValue("");
			alignField.setColSpan(3);

			form.setItems(nameField, displayLabelField, langBtn, widthField, alignField);
			addItem(form);

			footer = new HLayout(5);
			footer.setMargin(5);
			footer.setAutoHeight();
			footer.setWidth100();
			footer.setAlign(VerticalAlignment.CENTER);

			save = new IButton("Save");
			save.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (validate()){
						TreeViewGridColModel definition = new TreeViewGridColModel();
						definition.setName(SmartGWTUtil.getStringValue(nameField));
						definition.setDisplayLabel(SmartGWTUtil.getStringValue(displayLabelField));
						definition.setLocalizedDisplayLabelList(localizedDisplayLabelList);
						String width = SmartGWTUtil.getStringValue(widthField);
						if (width != null && !width.isEmpty()) {
							try {
								Integer _width = Integer.parseInt(width);
								definition.setWidth(_width);
							} catch (NumberFormatException e) {
							}
						}
						definition.setAlign(SmartGWTUtil.getStringValue(alignField));

						fireDataChanged(definition);

					}
				}
			});
			footer.addMember(save);

			cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});
			footer.addMember(cancel);
			addItem(footer);

			centerInPage();
		}

		/**
		 * ColModel設定
		 * @param definition
		 */
		public void setDefinition(TreeViewGridColModel definition) {
			if (definition != null) {
				localizedDisplayLabelList = definition.getLocalizedDisplayLabelList();

				nameField.setValue(definition.getName());
				displayLabelField.setValue(definition.getDisplayLabel());
				widthField.setValue(definition.getWidth());
				alignField.setValue(definition.getAlign());
			}
		}

		/**
		 * 入力チェック
		 * @return
		 */
		private boolean validate() {
			return form.validate();
		}

		/**
		 * {@link DataChangedHandler} を追加します。
		 *
		 * @param handler {@link DataChangedHandler}
		 */
		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		/**
		 * データ変更通知処理
		 */
		private void fireDataChanged(TreeViewGridColModel definition) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValue("definition", definition);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

}
