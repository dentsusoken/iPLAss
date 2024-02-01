/* Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaDataMultiTextGridItem extends MetaFieldCanvasItem {

	private static final String RECORD_ATTRIBUTE_VALUE = "_value";

	private ListGrid grid;

	private FieldInfo info;

	private String displayName;
	private String description;
	private List<String> valueList;

	@SuppressWarnings("unchecked")
	public MetaDataMultiTextGridItem(final MetaFieldSettingPane pane, final FieldInfo info) {
		this.info = info;

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

		displayName = pane.getDisplayName(info);
		description = pane.getDescription(info);
		if (SmartGWTUtil.isNotEmpty(description)) {
			SmartGWTUtil.addHoverToCanvas(grid, description);
		}
		if (info.isRequired()) {
			SmartGWTUtil.setRequired(this);
		}

		grid.addRecordDoubleClickHandler(new EditHandler());

		// grid内でのD&D後のデータ並べ替え
		grid.addDragStopHandler(new GridDragStopHandler());

		// カラムの構築
		grid.setFields(new ListGridField(RECORD_ATTRIBUTE_VALUE, displayName));


		IButton addButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_add"));
		addButton.addClickHandler(new AddClickHandler());

		IButton delButton = new IButton(
				AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_delete"));
		delButton.addClickHandler(new RemoveClickHandler());

		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.setMembers(addButton, delButton);

		container.addMember(grid);
		container.addMember(buttonPane);

		setColSpan(2);
		setCanvas(container);

		valueList = (List<String>)pane.getValue(info.getName());
		if (valueList == null) {
			// フィールドの値がnullなら空のリストを詰めておく(インスタンスを確定するため)
			valueList = new ArrayList<>();
			pane.setValue(info.getName(), (Serializable)valueList);
		}
		for (String textValue : valueList) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(RECORD_ATTRIBUTE_VALUE, textValue);
			grid.addData(record);
		}
	}

	@Override
	public Boolean validate() {
		return true;
	}

	private final class EditHandler implements RecordDoubleClickHandler {

		private EditHandler() {
		}

		@Override
		public void onRecordDoubleClick(RecordDoubleClickEvent event) {

			ListGridRecord record = grid.getRecord(event.getRecordNum());
			if (record == null) {
				return;
			}

			String fieldValue = record.getAttribute(RECORD_ATTRIBUTE_VALUE);

			final ValueInputDialog dialog = new ValueInputDialog(fieldValue);
			dialog.addDataChangeHandler((changeEvent) -> {
				// ダイアログ破棄
				dialog.destroy();

				String updatedValue = changeEvent.getValueObject(String.class);

				int index = grid.getRecordIndex(record);

				//レコード更新
				record.setAttribute(RECORD_ATTRIBUTE_VALUE, updatedValue);
				grid.refreshRow(index);

				//リスト更新
				valueList.remove(index);
				valueList.add(index, updatedValue);
			});
			dialog.show();
		}

	}

	private final class AddClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			final ValueInputDialog dialog = new ValueInputDialog(null);
			dialog.addDataChangeHandler((changeEvent) -> {
				// ダイアログ破棄
				dialog.destroy();

				String updatedValue = changeEvent.getValueObject(String.class);

				// レコード生成
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(RECORD_ATTRIBUTE_VALUE, updatedValue);
				grid.addData(record);

				//リストに追加
				valueList.add(updatedValue);
			});
			dialog.show();
		}
	}

	private final class RemoveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			ListGridRecord[] records = grid.getSelectedRecords();
			if (records == null || records.length == 0)
				return;

			for (ListGridRecord record : records) {
				int index = grid.getRecordIndex(record);

				// グリッドから削除
				grid.removeData(record);

				//リストから削除
				valueList.remove(index);
			}
		}
	}

	private final class GridDragStopHandler implements DragStopHandler {

		@Override
		public void onDragStop(DragStopEvent event) {
			//valueListを作り直し
			valueList.clear();
			for (ListGridRecord record : grid.getRecords()) {
				String fieldValue = record.getAttribute(RECORD_ATTRIBUTE_VALUE);
				valueList.add(fieldValue);
			}
		}

	}

	private final class ValueInputDialog extends MtpDialog {

		private List<DataChangedHandler> handlers = new ArrayList<>();

		public ValueInputDialog(String value) {

			String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
			//見た目だけ変更して、必須チェックは個別に実施(CanvasItemを考慮)
			if (info.isRequired()) {
				title = "<b>" + title + "</b>";
			}

			setTitle(title + " Setting");
			setHeight(160);
			centerInPage();

			DynamicForm form = new MtpForm();

			TextItem item = new MtpTextItem();
			item.setTitle(title);
			if (SmartGWTUtil.isNotEmpty(description)) {
				SmartGWTUtil.addHoverToFormItem(item, description);
			}
			item.setValue(value);
			form.setFields(item);

			container.addMember(form);

			// ボタン処理
			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					String value = SmartGWTUtil.getStringValue(item);
					if (SmartGWTUtil.isNotEmpty(value)) {
						fireDataChanged(value);
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

		public void addDataChangeHandler(DataChangedHandler handler) {
			handlers.add(0, handler);
		}

		private void fireDataChanged(String value) {
			DataChangedEvent event = new DataChangedEvent();
			event.setValueObject(value);
			for (DataChangedHandler handler : handlers) {
				handler.onDataChanged(event);
			}
		}
	}

}
