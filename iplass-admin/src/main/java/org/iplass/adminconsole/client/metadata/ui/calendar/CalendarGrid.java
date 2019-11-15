/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.MetaDataSelectItem.ItemOption;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogCondition;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendarItem;
import org.iplass.mtp.view.calendar.EntityCalendarItem.CalendarSearchType;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

/**
 * カレンダー編集TreeGrid
 *
 */
public class CalendarGrid extends MtpTreeGrid {

	/** 内部保持Tree */
	private Tree tree;

	/**
	 * コンストラクタ
	 */
	public CalendarGrid() {

		//setAutoFetchData(true);
		setDragDataAction(DragDataAction.MOVE);
		setSelectionType(SelectionStyle.SINGLE);	//単一行選択
		setBorder("none");					//外のSectionと線がかぶるので消す

		setLeaveScrollbarGap(false);		//←falseで縦スクロールバーの領域が自動表示
//		setShowHeader(true);  				//←trueで上に列タイトルが表示される（Default true）
//		setEmptyMessage("No Item Data");	//←空の場合のメッセージ
//		//setManyItemsImage("cubes_all.png");
		setCanReorderRecords(true);			//←Dragによるレコードの並べ替え許可指定（Default false）
		setCanAcceptDroppedRecords(true);	//←レコードのDropの許可指定（Default false）
		setCanDragRecordsOut(true);			//←レコードをDragして他にDropできるか（Default false）
//		setShowEdges(false);				//←trueで周りに枠が表示される（Default false）
		setCanSort(false);					//←ソートできるか（Default true）
		setCanFreezeFields(false);			//←列を固定できるか（Default null）
		setCanPickFields(false);			//←ヘッダで列を選択できるか（Default true）

		//メッセージのカスタマイズ
		//Drop先Nodeにすでに同じNodeが存在する場合、FolderDropEvent自体が発生しないが、
		//エラーメッセージが表示されるためメッセージをカスタマイズ
		//This item already contains a child item with that name.
		//項目は既に含まれます
		//setParentAlreadyContainsChildMessage(parentAlreadyContainsChildMessage);
		setParentAlreadyContainsChildMessage(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_menuAlreadyExists"));

		//以下にメッセージとして関連するプロパティがあるが呼ばれない（設定による）
		//You can't drag an item into one of it's children.
		//子項目の一つにドラッグできません
		//setCantDragIntoChildMessage(cantDragIntoChildMessage);
		//You can't drag an item into itself.
		//同一の項目にはドラッグできません
		//setCantDragIntoSelfMessage(cantDragIntoSelfMessage);

		//Treeの生成
		tree = new Tree();
		tree.setModelType(TreeModelType.CHILDREN);

		//TreeGridFieldの生成(TreeNodeのattribute属性と一致させること)
		TreeGridField defNameField = new TreeGridField("defName", "EntityName");
		TreeGridField editActionField = new TreeGridField("editAction", " ");
		editActionField.setWidth(25);
		TreeGridField delActionField = new TreeGridField("delAction", " ");
		delActionField.setWidth(25);
		setFields(defNameField, editActionField, delActionField);

		//Drag＆Drop用EventHandlerの設定
		addFolderDropHandler(new CalendarFolderDropHandler());

		//この２つを指定することでcreateRecordComponentが有効
		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);

		//以下の方法で簡単に削除可能だが、確認メッセージを出せないため
		//createRecordComponentを利用して自力ボタン生成
		//レコードの削除を許可（右端にアイコンが表示される）
		//setCanRemoveRecords(true);
		//setRemoveIcon("icon_delete.png");

		//一覧ダブルクリック処理
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				//編集ダイアログ表示
				showItemDialog(event.getRecord());
			}
		});

		//データのセット
		setData(tree);
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
			//deleteBtn.setSrc("icon_delete.png");
			editBtn.setSrc("icon_edit.png");
			editBtn.setHeight(16);
			editBtn.setWidth(16);
			editBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showItemDialog(record);
				}
			});
			ret = editBtn;
		} else if (fieldName.equals("delAction")) {
			ImgButton deleteBtn = new ImgButton();
			deleteBtn.setShowDown(false);
			deleteBtn.setShowRollOver(false);
			deleteBtn.setLayoutAlign(Alignment.CENTER);
			//deleteBtn.setSrc("icon_delete.png");
			deleteBtn.setSrc("remove.png");
			deleteBtn.setHeight(16);
			deleteBtn.setWidth(16);
			deleteBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					SC.confirm(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_deleteConfirm"),
							AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_deleteCategoryConf", record.getAttribute("defName"))
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (value) {
								TreeNode target = Tree.nodeForRecord(record);
								tree.remove(target);
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
	 * Itemの編集ダイアログを表示します。
	 *
	 * @param record 対象レコード
	 */
	private void showItemDialog(Record record) {
		EntityCalendarItem item = (EntityCalendarItem)record.getAttributeAsObject("valueObject");
		EntityCalendarItemEditDialog dialog = new EntityCalendarItemEditDialog(item);
		dialog.show();
	}

	/**
	 * Calendarを展開します。
	 *
	 * @param calendar Calendar
	 */
	public void setCalendar(EntityCalendar calendar) {

		CalendarNode root = new CalendarNode("cubes_all.png");

		List<EntityCalendarItem> items = calendar.getItems();
		if (items.size() > 0) {
			CalendarNode[] children = createEntityCalendarItemNodes(items);
			root.setChildren(children);
		}

		tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
		tree.setRoot(root);
		tree.setModelType(TreeModelType.CHILDREN);

		setData(tree);
		getData().openAll();
	}

	public List<EntityCalendarItem> getItems() {
		List<EntityCalendarItem> items = new ArrayList<>();

		CalendarNode root = (CalendarNode) tree.getRoot();
		TreeNode[] children = tree.getChildren(root);

		for (TreeNode child : children) {
			EntityCalendarItem item = (EntityCalendarItem) child.getAttributeAsObject("valueObject");
			items.add(item);
		}

		return items;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return true;
	}

	private CalendarNode[] createEntityCalendarItemNodes(List<EntityCalendarItem> children) {
		List<CalendarNode> childList = new ArrayList<>(children.size());
		for (EntityCalendarItem child : children) {
			CalendarNode childNode = new CalendarNode(child, "cube_blue.png");
			childList.add(childNode);
		}
		return childList.toArray(new CalendarNode[0]);
	}

	/**
	 * カスタマイズTreeNode
	 */
	private class CalendarNode extends TreeNode {

		public CalendarNode(String icon) {
			setIcon(icon);
		}

		public CalendarNode(EntityCalendarItem item, String icon) {
			setAttribute("valueObject", item);

			setName(item.getDefinitionName());
			setIcon(icon);

			setAttribute("defName", item.getDefinitionName());
		}
	}

	private class CalendarFolderDropHandler implements FolderDropHandler {

		@Override
		public void onFolderDrop(FolderDropEvent event) {
			CalendarNode dropTargetNode = null;
			if (event.getFolder() instanceof CalendarNode) {
				dropTargetNode = (CalendarNode) event.getFolder();
			}

			CalendarNode moveTargetNode = null;
			CalendarNode addTargetNode = null;
			if (event.getSourceWidget() instanceof CalendarGrid) {
				//ツリー内移動
				//ルート直下はルート直下のみ移動可能
				//それ以外はそのノードが属する階層内のみ移動可能
				TreeNode[] nodes = event.getNodes();
				if (nodes != null && nodes.length ==1) {
					if (nodes[0] instanceof CalendarNode) {
						moveTargetNode = (CalendarNode) nodes[0];
					}
				}
				if (moveTargetNode == null) {
					throw new IllegalStateException(
							AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_dragOriginalInfoNotGet") + nodes);
				}
			} else if (event.getSourceWidget() instanceof ListGrid) {
				//Drag&Drop
				Canvas dragTarget = event.getSourceWidget();

				//対象レコードの取得
				ListGridRecord record = ((ListGrid)dragTarget).getSelectedRecord();
				String name = record.getAttribute("defName");
				EntityCalendarItem item = new EntityCalendarItem();
				item.setDefinitionName(name);
				item.setCalendarSearchType(CalendarSearchType.DATE);
				item.setPropertyName(Entity.CREATE_DATE);//TODO デフォルトはどうする？
				item.setLimit(1000);
				item.setDisplayTime(false);
				addTargetNode = new CalendarNode(item, "cube_blue.png");
			}

			//追加処理
			if (dropTargetNode != null && addTargetNode != null) {
				tree.add(addTargetNode, dropTargetNode, event.getIndex());
			}

			//Nodeの移動処理
			if (dropTargetNode != null && moveTargetNode != null) {
				if (tree.getParentPath(moveTargetNode).equals(tree.getPath(dropTargetNode))) {
					int curIndex = getCurrentIndex(moveTargetNode);
					int addIndex = event.getIndex();
					if (addIndex > curIndex) addIndex--;

					tree.remove(moveTargetNode);
					tree.add(moveTargetNode, dropTargetNode, addIndex);
				}
			}

			//デフォルトイベントのキャンセル
			event.cancel();

		}

		/**
		 * 親Nodeに対するIndexを返します。
		 *
		 * @param node 対象Node
		 * @return Index
		 */
		private int getCurrentIndex(TreeNode node) {

			if (tree.getParent(node) != null) {
				TreeNode[] children = tree.getChildren(tree.getParent(node));

				int i = 0;
				for (TreeNode child : children) {
					if (child == node) {
						return i;
					}
					i++;
				}
			}
			//ありえない
			throw new IllegalStateException(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_dragIndexNotGet") + node);
		}

	}

	private class EntityCalendarItemEditDialog extends MtpDialog {

		private EntityCalendarItem item;

		/** Entity定義名 */
		private TextItem defNameField;
		/** Entitycolor */
		private TextItem entityColorField;
		/** ColorConfig */
		private TextAreaItem colorConfigField;
		/** カレンダーの検索方法 */
		private SelectItem calendarSearchTypeField;
		/** 検索対象プロパティ名 */
		private SelectItem propertyNameField;
		/** 検索対象プロパティ名(From) */
		private SelectItem fromPropertyNameField;
		/** 検索対象プロパティ名(To) */
		private SelectItem toPropertyNameField;
		/** フィルタ条件 */
		private TextAreaItem filterConditionField;
		/** 表示上限 */
		private SpinnerItem limitField;
		/** カレンダー上に時間を表示するか */
		private CheckboxItem displayTimeField;

		/** 詳細アクション名 */
		private SelectItem viewActionField;
		/** 追加アクション名 */
		private SelectItem addActionField;
		/** ビュー名 */
		private TextItem viewNameField;

		private EntityDefinition ed;

		public EntityCalendarItemEditDialog(EntityCalendarItem calendarItem) {
			this.item = calendarItem;

			setTitle("Calendar Item");
			setHeight(650);
			centerInPage();

			RequiredIfValidator propertyNameValidator = new RequiredIfValidator(
				new RequiredIfFunction() {

					@Override
					public boolean execute(FormItem formItem, Object value) {
						String calendarSearchType = SmartGWTUtil.getStringValue(calendarSearchTypeField);
						String name = formItem.getName();
						if (CalendarSearchType.DATE.name().equals(calendarSearchType)) {
							//Dateの場合はFromとToはノーチェック
							if ("fromPropertyName".equals(name) || "toPropertyName".equals(name)) {
								return false;
							}
						} else {
							//Periodの場合はpropertyNameはノーチェック
							if ("propertyName".equals(formItem.getName())) {
								return false;
							}
						}

						return value == null || value.toString().isEmpty();
					}
				});
			propertyNameValidator.setErrorMessage(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_requiredField"));

			//設定項目の入力フィールド作成
			List<FormItem> items = new ArrayList<>();

			defNameField = new MtpTextItem("defName", "Entity Name");
			SmartGWTUtil.setReadOnly(defNameField);
			items.add(defNameField);

			entityColorField = new MtpTextItem("entityColor", "Entity Color");
			items.add(entityColorField);

			ButtonItem editScript = new ButtonItem("editScript", "Edit");
			editScript.setWidth(100);
			editScript.setColSpan(3);
			editScript.setAlign(Alignment.RIGHT);
			editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_displayDialogEditScript")));
			editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
							SmartGWTUtil.getStringValue(colorConfigField),
							ScriptEditorDialogCondition.CALENDAR_COLOR_CONFIG_SCRIPT,
							"ui_metadata_calendar_CalendarGrid_scriptHint",
							null,
							new ScriptEditorDialogHandler() {

								@Override
								public void onSave(String text) {
									colorConfigField.setValue(text);
								}
								@Override
								public void onCancel() {
								}
							});
				}
			});
			items.add(editScript);

			colorConfigField = new MtpTextAreaItem("colorConfig", "Color Config");
			colorConfigField.setColSpan(2);
			colorConfigField.setHeight(100);
			items.add(colorConfigField);
			SmartGWTUtil.setReadOnlyTextArea(colorConfigField);

			calendarSearchTypeField = new MtpSelectItem("calendarSearchType", "Matching Type");
			SmartGWTUtil.addHoverToFormItem(calendarSearchTypeField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_howToSearchCalendar"));
			HashMap<String, String> valueMap = new HashMap<>();
			valueMap.put(CalendarSearchType.DATE.name(), "Date");
			valueMap.put(CalendarSearchType.PERIOD.name(), "Period");
			calendarSearchTypeField.setValueMap(valueMap);
			items.add(calendarSearchTypeField);

			propertyNameField = new MtpSelectItem("propertyName", "Target Property<br/>(for Date)");
			SmartGWTUtil.addHoverToFormItem(propertyNameField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speEntityItemCalendarDate"));
			propertyNameField.setValidators(propertyNameValidator);
			items.add(propertyNameField);

			fromPropertyNameField = new MtpSelectItem("fromPropertyName", "From Property<br/>(for Period)");
			SmartGWTUtil.addHoverToFormItem(fromPropertyNameField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speEntityItemCalendarPeriodStart"));
			fromPropertyNameField.setValidators(propertyNameValidator);
			items.add(fromPropertyNameField);

			toPropertyNameField = new MtpSelectItem("toPropertyName", "To Property<br/>(for Period)");
			SmartGWTUtil.addHoverToFormItem(toPropertyNameField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speEntityItemCalendarPeriodEnd"));
			toPropertyNameField.setValidators(propertyNameValidator);
			items.add(toPropertyNameField);

			filterConditionField = new MtpTextAreaItem();
			filterConditionField.setTitle("Filter Condition");
			filterConditionField.setHeight(100);
			SmartGWTUtil.addHoverToFormItem(filterConditionField, AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speCommonFilterCond"));
			items.add(filterConditionField);

			limitField = new SpinnerItem("limit", "Limit");
			SmartGWTUtil.addHoverToFormItem(limitField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speUpperLimit"));
			limitField.setDefaultValue(1000);
			limitField.setMin(1);
			limitField.setStep(1);
			items.add(limitField);

			displayTimeField = new CheckboxItem("displayTime", "show time value");
			displayTimeField.setWidth("100%");
			SmartGWTUtil.addHoverToFormItem(displayTimeField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_displayTimeOnCalendar"));
			items.add(displayTimeField);

			viewActionField = new MetaDataSelectItem(ActionMappingDefinition.class, "Detail Action", new ItemOption(false, true));
			SmartGWTUtil.addHoverToFormItem(viewActionField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speDisplayDetailAction"));
			items.add(viewActionField);

			addActionField = new MetaDataSelectItem(ActionMappingDefinition.class, "Add Action", new ItemOption(false, true));
			SmartGWTUtil.addHoverToFormItem(addActionField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speAddDataAction"));
			items.add(addActionField);

			viewNameField = new MtpTextItem("viewName", "Entity View");
			SmartGWTUtil.addHoverToFormItem(viewNameField,
					AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_speDisplayDetailAndAddViewName"));
			items.add(viewNameField);

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);
			form.setFields(items.toArray(new FormItem[items.size()]));

			container.addMember(form);

			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (form.validate()){
						item.setEntityColor(SmartGWTUtil.getStringValue(entityColorField));
						item.setColorConfig(SmartGWTUtil.getStringValue(colorConfigField));
						String calendarSearchType = SmartGWTUtil.getStringValue(calendarSearchTypeField);
						if (CalendarSearchType.DATE.name().equals(calendarSearchType)) {
							item.setCalendarSearchType(CalendarSearchType.DATE);
							item.setPropertyName(SmartGWTUtil.getStringValue(propertyNameField));
							item.setFromPropertyName(null);
							item.setToPropertyName(null);
						} else {
							item.setCalendarSearchType(CalendarSearchType.PERIOD);
							item.setPropertyName(null);
							item.setFromPropertyName(SmartGWTUtil.getStringValue(fromPropertyNameField));
							item.setToPropertyName(SmartGWTUtil.getStringValue(toPropertyNameField));
						}
						item.setFilterCondition(SmartGWTUtil.getStringValue(filterConditionField));
						item.setLimit((Integer) limitField.getValue());
						item.setDisplayTime(SmartGWTUtil.getBooleanValue(displayTimeField));

						String viewAction = SmartGWTUtil.getStringValue(viewActionField);
						if ("#default".equals(viewAction)) viewAction = null;
						item.setViewAction(viewAction);
						String addAction = SmartGWTUtil.getStringValue(addActionField);
						if ("#default".equals(addAction)) addAction = null;
						item.setAddAction(addAction);
						item.setViewName(SmartGWTUtil.getStringValue(viewNameField));

						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(ok, cancel);

			initialize();
		}

		private void initialize() {
			defNameField.setValue(item.getDefinitionName());
			entityColorField.setValue(item.getEntityColor());
			colorConfigField.setValue(item.getColorConfig());
			calendarSearchTypeField.setValue(item.getCalendarSearchType() != null ?
					item.getCalendarSearchType().name() : "DATE");
			propertyNameField.setValue(item.getPropertyName());
			fromPropertyNameField.setValue(item.getFromPropertyName());
			toPropertyNameField.setValue(item.getToPropertyName());
			filterConditionField.setValue(item.getFilterCondition());
			limitField.setValue(item.getLimit());
			displayTimeField.setValue(item.getDisplayTime() != null ? item.getDisplayTime() : false);

			String viewAction = item.getViewAction();
			if (viewAction == null || viewAction.isEmpty()) viewAction = "#default";
			viewActionField.setValue(viewAction);
			String addAction = item.getAddAction();
			if (addAction == null || addAction.isEmpty()) addAction = "#default";
			addActionField.setValue(addAction);
			viewNameField.setValue(item.getViewName());


			//対象EntityのProperty定義を取得
			MetaDataServiceAsync service  = MetaDataServiceFactory.get();
			service.getEntityDefinition(TenantInfoHolder.getId(), item.getDefinitionName(), new AsyncCallback<EntityDefinition>() {

				@Override
				public void onSuccess(EntityDefinition result) {
					ed = result;

					//プロパティの一覧を取得して、選択型の選択肢とかを作成する
					List<PropertyDefinition> propertyList = ed.getPropertyList();
					Collections.sort(propertyList, new Comparator<PropertyDefinition>() {

						@Override
						public int compare(PropertyDefinition o1, PropertyDefinition o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});

					List<String> params = new ArrayList<>();
					for (PropertyDefinition pd : propertyList) {
						//選択型の項目作成
						if (pd instanceof DateProperty
								|| pd instanceof DateTimeProperty
								|| pd instanceof TimeProperty) {
							//TODO Time型必要か？
							params.add(pd.getName());
						}
					}
					String[] array = params.toArray(new String[params.size()]);
					propertyNameField.setValueMap(array);
					fromPropertyNameField.setValueMap(array);
					toPropertyNameField.setValueMap(array);
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_calendar_CalendarGrid_failedToGetEntitiyDef") + caught.getMessage());
				}
			});
		}

	}
}
