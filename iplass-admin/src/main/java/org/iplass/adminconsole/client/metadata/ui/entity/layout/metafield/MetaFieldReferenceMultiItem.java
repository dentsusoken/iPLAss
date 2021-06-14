/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisListDataResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.dto.refrect.RefrectableInfo;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionServiceFactory;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.Refrectable;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaFieldReferenceMultiItem extends MetaFieldCanvasItem {

	private static final String RECORD_ATTRIBUTE_TYPE = "_type";
	private static final String RECORD_ATTRIBUTE_VALUE = "_value";

	private ListGrid grid;

	private RefrectionServiceAsync service = null;

	private MetaFieldSettingPane pane;

	public MetaFieldReferenceMultiItem(final MetaFieldSettingPane pane, final FieldInfo info) {
		this.pane = pane;

		service = RefrectionServiceFactory.get();

		VLayout container = new VLayout();
		container.setAutoHeight();
		container.setWidth100();

		grid = new ListGrid();
		grid.setWidth100();
		grid.setHeight(120);

		grid.setLeaveScrollbarGap(false);	//falseで縦スクロールバー領域が自動表示制御される

		grid.setCanSort(false);			//ソート不可
		grid.setCanGroupBy(false);		//Group化不可
		grid.setCanPickFields(false);	//列の選択不可
		grid.setCanAutoFitFields(false);	//列幅の自動調整不可(崩れるので)
		grid.setCanFreezeFields(false);

		grid.setCanResizeFields(true);	//列幅変更可

		grid.setCanDragRecordsOut(true);				//grid内でのD&Dでの並べ替えを許可
		grid.setCanAcceptDroppedRecords(true);
		grid.setCanReorderRecords(true);

		// カラムの構築
		grid.setFields(new ListGridField("empty"));// 仮設定

		String description = pane.getDescription(info);
		if (SmartGWTUtil.isNotEmpty(description)) {
			SmartGWTUtil.addHoverToCanvas(grid, description);
		}

		// フィールドの値がnullなら空のリストを詰めておく
		boolean isValueNull = pane.getValue(info.getName()) == null;
		@SuppressWarnings("unchecked")
		final List<Refrectable> valueList = isValueNull ? new ArrayList<>() : (List<Refrectable>) pane.getValue(info.getName());
		if (isValueNull) {
			pane.setValue(info.getName(), (Serializable) valueList);
		}

		grid.addRecordDoubleClickHandler(new EditHandler(valueList, info));

		// grid内でのD&D後のデータ並べ替え
		grid.addDragStopHandler(new DragStopHandler() {

			@Override
			public void onDragStop(DragStopEvent dragstopevent) {
				valueList.clear();
				for (ListGridRecord record : grid.getRecords()) {
					Refrectable fieldValue = (Refrectable) record.getAttributeAsObject(RECORD_ATTRIBUTE_VALUE);
					valueList.add(fieldValue);
				}
			}
		});

		IButton addButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_add"));
		addButton.addClickHandler(new AddClickHandler(valueList, info));

		IButton delButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_delete"));
		delButton.addClickHandler(new RemoveClickHandler(valueList));

		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.setMembers(addButton, delButton);

		container.addMember(grid);
		container.addMember(buttonPane);

		setColSpan(2);
		setCanvas(container);

		service.analysisListData(TenantInfoHolder.getId(), info.getReferenceClassName(), valueList,
				new AnalysisListDataAsyncCallback(pane.getSimpleName(info.getReferenceClassName()), grid));
	}

	@Override
	public Boolean validate() {
		return true;
	}

	/**
	 * 複数参照型のレコード更新
	 *
	 * @param record
	 * @param className
	 * @param value
	 * @param fields
	 * @param valueMap
	 */
	private void updateRecord(ListGridRecord record, String className, Refrectable value,
			ListGridField[] fields, Map<String, Serializable> valueMap) {
		record.setAttribute(RECORD_ATTRIBUTE_VALUE, value);
		record.setAttribute(RECORD_ATTRIBUTE_TYPE, pane.getSimpleName(value.getClass().getName()));

		// Gridに表示されている項目のみ更新する
		for (ListGridField field : fields) {
			String fieldName = field.getName();
			if (!RECORD_ATTRIBUTE_TYPE.equals(fieldName)) {
				String simpleName = pane.getSimpleName(fieldName);
				record.setAttribute(fieldName, valueMap.get(simpleName));
			}
		}
	}

	/**
	 * 複数参照型の解析処理のコールバック
	 *
	 */
	private final class AnalysisListDataAsyncCallback extends AdminAsyncCallback<AnalysisListDataResult> {

		private ListGrid grid;
		private String simpleName;

		private AnalysisListDataAsyncCallback(final String simpleName, final ListGrid grid) {
			this.grid = grid;
			this.simpleName = simpleName;
		}

		@Override
		public void onSuccess(AnalysisListDataResult result) {

			List<ListGridField> fieldList = new ArrayList<>();
			for (FieldInfo info : result.getFields()) {
				// 複数型と参照型は表示しない
				if (!info.isMultiple() && info.getInputType() != InputType.REFERENCE
						&& info.getInputType() != InputType.SCRIPT && info.getInputType() != InputType.MULTI_LANG_LIST) {
					if (!pane.isVisileField(info)) {
						continue;
					}
					String displayName = pane.getDisplayName(info);
					fieldList.add(new ListGridField(simpleName + "." + info.getName(), displayName));
				}
			}
			if (fieldList.size() > 3) {
				for (ListGridField field : fieldList) {
					field.setWidth(80);
				}
			}
			grid.setFields(fieldList.toArray(new ListGridField[fieldList.size()]));

			for (RefrectableInfo info : result.getRefrectables()) {
				ListGridRecord record = new ListGridRecord();
				updateRecord(record, simpleName, info.getValue(), grid.getFields(), info.getValueMap());
				grid.addData(record);
			}
		}

	}

	private final class EditHandler implements RecordDoubleClickHandler {

		private final List<Refrectable> valueList;
		private final FieldInfo info;
		private final String simpleName;

		private EditHandler(List<Refrectable> valueList, FieldInfo info) {
			this.valueList = valueList;
			this.info = info;
			this.simpleName = pane.getSimpleName(info.getReferenceClassName());
		}

		@Override
		public void onRecordDoubleClick(RecordDoubleClickEvent event) {

			ListGridRecord record = grid.getRecord(event.getRecordNum());
			if (record == null) {
				return;
			}

			Refrectable fieldValue = (Refrectable) record.getAttributeAsObject(RECORD_ATTRIBUTE_VALUE);
			String className = fieldValue.getClass().getName();
			final MetaFieldSettingDialog dialog = pane.createSubDialog(className, fieldValue, info);
			dialog.setOkHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(MetaFieldUpdateEvent event) {
					// ダイアログ破棄
					dialog.destroy();

					// 表示対象更新
					updateRecord(record, simpleName, event.getValue(), grid.getFields(), event.getValueMap());
					grid.refreshRow(grid.getRecordIndex(record));

					int index = grid.getRecordIndex(record);
					valueList.remove(index);
					valueList.add(index, event.getValue());

					pane.setValue(info.getName(), (Serializable) valueList);
				}
			});

			dialog.setCancelHandler(new MetaFieldUpdateHandler() {
				@Override
				public void execute(MetaFieldUpdateEvent event) {
					dialog.destroy();
				}
			});
			dialog.show();
		}

	}

	/**
	 * 複数参照型の追加ボタンクリックイベント
	 *
	 */
	private final class AddClickHandler implements ClickHandler {

		private final List<Refrectable> valueList;
		private final FieldInfo info;

		private AddClickHandler(List<Refrectable> valueList, FieldInfo info) {
			this.valueList = valueList;
			this.info = info;
		}

		@Override
		public void onClick(ClickEvent event) {
			// 型リストを取得
			service.getSubClass(TenantInfoHolder.getId(), info.getReferenceClassName(), new AdminAsyncCallback<Name[]>() {

				@Override
				public void onSuccess(Name[] names) {
					if (names != null && names.length > 1) {
						// 利用可能なクラスが複数ある場合、インスタンス化するクラスを選択
						AddTypeSelectDialog dialog = new AddTypeSelectDialog(valueList, info, grid, names);
						dialog.show();
					} else if (names != null && names.length == 1) {
						// 一つしかない場合、その型でインスタンス作成
						service.create(TenantInfoHolder.getId(), names[0].getName(),
								new CreateAsyncCallback(valueList, names[0].getName(), info));
					}
				}

			});
		}
	}

	/**
	 * FIXME Multiで対象クラスが複数になるパターンが見当たらない
	 */
	private final class AddTypeSelectDialog extends MtpDialog {

		public AddTypeSelectDialog(final List<Refrectable> valueList, final FieldInfo info, final ListGrid grid, final Name[] names) {

			setTitle("Select Type");
			setHeight(160);
			centerInPage();

			DynamicForm form = new MtpForm();

			SelectItem item = new MtpSelectItem();
			item.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_type"));
			LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
			for (Name clsName : names) {
				valueMap.put(clsName.getName(), clsName.getDisplayName());
			}
			item.setValueMap(valueMap);
			form.setFields(item);

			container.addMember(form);

			// ボタン処理
			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					String className = SmartGWTUtil.getStringValue(item);
					if (SmartGWTUtil.isNotEmpty(className)) {
						service.create(TenantInfoHolder.getId(), className,
								new CreateAsyncCallback(valueList, className, info));
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
		}
	}

	/**
	 * 複数参照型の削除ボタンクリックイベント
	 *
	 */
	private final class RemoveClickHandler implements ClickHandler {

		private final List<Refrectable> valueList;

		private RemoveClickHandler(List<Refrectable> valueList) {
			this.valueList = valueList;
		}

		@Override
		public void onClick(ClickEvent event) {
			ListGridRecord record = grid.getSelectedRecord();
			if (record == null)
				return;

			// フィールド情報から削除
			// valueList.remove(record.getAttributeAsObject(RECORD_ATTRIBUTE_VALUE));
			// レコードのインスタンスがリストのものと違うため削除できない
			int index = grid.getRecordIndex(record);
			valueList.remove(index);

			// グリッドから削除
			grid.removeData(record);
		}
	}

	/**
	 * 参照型（複数）の追加ボタン用コールバック処理
	 */
	private final class CreateAsyncCallback extends AdminAsyncCallback<Refrectable> {

		/** 参照型フィールドの値 */
		private final List<Refrectable> valueList;
		/** 参照型のクラス名 */
		private final String className;
		/** 参照型オブジェクトが保持されるフィールド名 */
		private final FieldInfo info;

		/**
		 * コンストラクタ
		 *
		 * @param grid
		 * @param valueList
		 * @param className
		 * @param name
		 */
		private CreateAsyncCallback(List<Refrectable> valueList, String className, FieldInfo info) {
			this.valueList = valueList;
			this.className = className;
			this.info = info;
		}

		@Override
		public void onSuccess(Refrectable result) {
			// 設定画面表示
			final MetaFieldSettingDialog dialog = pane.createSubDialog(className, result, info);
			dialog.setOkHandler(new MetaFieldUpdateHandler() {

				@Override
				public void execute(final MetaFieldUpdateEvent event) {
					// ダイアログ破棄
					dialog.destroy();

					// ValueObject再設定
					valueList.add(event.getValue());

					// レコード生成
					ListGridRecord record = new ListGridRecord();
					updateRecord(record, pane.getSimpleName(info.getReferenceClassName()), event.getValue(),
							grid.getFields(), event.getValueMap());
					grid.addData(record);

					// 作成したObjectを保存
					pane.setValue(info.getName(), (Serializable) valueList);
				}
			});

			dialog.setCancelHandler(new MetaFieldUpdateHandler() {
				@Override
				public void execute(MetaFieldUpdateEvent event) {
					dialog.destroy();
				}
			});

			dialog.show();
		}

	}

}
