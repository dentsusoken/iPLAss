/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.base.dto.KeyValue;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class AutoNumberValueListDialog extends MtpDialog {

	private String defName;
	private String propertyName;

	private AutoNumberValueListGridPane gridPane;

	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public AutoNumberValueListDialog(String defName, String propertyName) {
		this.defName = defName;
		this.propertyName = propertyName;

		setTitle("AutoNumber Value List");
		setHeight(425);
		centerInPage();

		gridPane = new AutoNumberValueListGridPane();

		container.addMember(gridPane);

		IButton btnSave = new IButton("Save");
		btnSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gridPane.save();
			}
		});

		IButton btnCancel = new IButton("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(btnSave, btnCancel);
	}

	class AutoNumberValueListGridPane extends VLayout {

		private AutoNumberValueListGrid grid;

		public AutoNumberValueListGridPane() {

			setMembersMargin(5);
			setPadding(10);
			setWidth100();
			setHeight100();

			Canvas hintContents = new Canvas();
			hintContents.setHeight(50);
			hintContents.setWidth100();
			hintContents.setPadding(5);
			hintContents.setOverflow(Overflow.AUTO);
			hintContents.setCanSelectText(true);

			StringBuilder contents = new StringBuilder();
			contents.append("<div>")
			.append(AdminClientMessageUtil.getString("ui_metadata_entity_AutoNumberValueListDialog_description"))
			.append("</div>");
			hintContents.setContents(contents.toString());

			grid = new AutoNumberValueListGrid();

			SectionStackSection section1 = new SectionStackSection();
			section1.setTitle("Current Values");
			section1.setItems(grid);
			section1.setExpanded(true);
			section1.setCanCollapse(false);

			ImgButton addButton = new ImgButton();
			addButton.setSrc("add.png");
			addButton.setSize(16);
			addButton.setShowFocused(false);
			addButton.setShowRollOver(false);
			addButton.setShowDown(false);
			addButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					grid.addUnitKey();
				}
			});

			ImgButton removeButton = new ImgButton();
			removeButton.setSrc("remove.png");
			removeButton.setSize(16);
			removeButton.setShowFocused(false);
			removeButton.setShowRollOver(false);
			removeButton.setShowDown(false);
			removeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					grid.removeUnitKey();
				}
			});

			section1.setControls(addButton, removeButton);

			SectionStack sectionStack = new SectionStack();
			sectionStack.setSections(section1);
			sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
			sectionStack.setAnimateSections(true);
			sectionStack.setWidth100();
			sectionStack.setHeight100();
			sectionStack.setOverflow(Overflow.HIDDEN);

			addMember(hintContents);
			addMember(sectionStack);

			getData();
		}

		public void save() {
			final List<KeyValue<String, Long>> values = grid.getSelectedValues();
			if (values == null || values.isEmpty()) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_AutoNumberValueListDialog_selectTarget"));
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_entity_AutoNumberValueListDialog_resetCounterCaution"), new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						update(values);
					}
				}
			});
		}

		private void getData() {
			service.getAutoNumberCurrentValueList(TenantInfoHolder.getId(), defName, propertyName, new AdminAsyncCallback<List<KeyValue<String,Long>>>() {

				@Override
				public void onSuccess(List<KeyValue<String, Long>> result) {
					grid.setUnitKeyData(result);
				}
			});
		}

		private void update(List<KeyValue<String, Long>> values) {
			SmartGWTUtil.showSaveProgress();
			service.resetAutoNumberCounterList(TenantInfoHolder.getId(), defName, propertyName, values, new AdminAsyncCallback<Void>() {

				@Override
				protected void beforeFailure(Throwable caught){
					SmartGWTUtil.hideProgress();
				};

				@Override
				public void onSuccess(Void result) {
					SmartGWTUtil.hideProgress();
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_AutoNumberValueListDialog_resetComp"));
					getData();
				}
			});
		}

	}

	class AutoNumberValueListGrid extends ListGrid {

		private static final String INSERT = "I";
		private static final String UPDATE = "U";
		//private static final String DELETE = "D";
		private static final String NO_CHANGE = "";

		public AutoNumberValueListGrid() {

			setWidth100();
			setHeight100();
			setLeaveScrollbarGap(false);

			setCanFreezeFields(false);
			setCanGroupBy(false);
			setCanPickFields(false);
			setCanSort(false);
			setCanDragRecordsOut(false);
			setCanAcceptDroppedRecords(false);
			setCanReorderRecords(false);
			setCanDragSelectText(true);
			setShowSelectedStyle(false);
			setCanAutoFitFields(false);

			//CheckBox選択設定
			setSelectionType(SelectionStyle.SIMPLE);
			setSelectionAppearance(SelectionAppearance.CHECKBOX);

			//編集設定
			setCanEdit(true);
			setEditEvent(ListGridEditEvent.DOUBLECLICK);
			setEditByCell(true);

			ListGridField statusField = new ListGridField("status", "*");
			statusField.setWidth(30);
			statusField.setAlign(Alignment.CENTER);
			statusField.setCanEdit(false);

			ListGridField unitKeyField = new ListGridField("unitKey", "Unit Key");

			ListGridField valueField = new ListGridField("currentValue", "Value");
			//数値のみ入力可能
			IntegerItem valueInputField = new IntegerItem();
			valueField.setEditorProperties(valueInputField);

			setFields(statusField, unitKeyField, valueField);

			//ステータス変更処理
			addEditCompleteHandler(new EditCompleteHandler() {
				@Override
				public void onEditComplete(EditCompleteEvent event) {
					//変更があった場合のみEvent発生
					int rowNum = event.getRowNum();
					itemValueChanged(rowNum);
				}
			 });
		}

		@Override
		public boolean canEditCell(int rowNum, int colNum) {
			Record record = this.getRecord(rowNum);
			String fieldName = this.getFieldName(colNum);

			//KEYはInsertのみ変更可能
			if ("unitKey".equals(fieldName)) {
				String status = record.getAttribute("status");
				return status.equals(INSERT);
			}
			return super.canEditCell(rowNum, colNum);
		}

		public void setUnitKeyData(List<KeyValue<String, Long>> result) {
			ListGridRecord[] records = new ListGridRecord[result.size()];

			for (int i = 0; i < result.size(); i++) {
				KeyValue<String, Long> value = result.get(i);
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("status", NO_CHANGE);
				record.setAttribute("unitKey", value.getKey());
				record.setAttribute("currentValue",value.getValue());
				records[i] = record;
			}
			setData(records);
		}

		public void addUnitKey() {
			ListGridRecord newRecord = new ListGridRecord();
			newRecord.setAttribute("status", INSERT);
			newRecord.setAttribute("unitKey", "");
			newRecord.setAttribute("currentValue", -1);
			addData(newRecord);
		}

		public void removeUnitKey() {
			ListGridRecord[] records = getSelectedRecords();
			if (records == null || records.length == 0) {
				return;
			}

			for (ListGridRecord record : records) {
				String status = record.getAttribute("status");
				if (status.equals(INSERT)) {
					//Insertデータは削除
					removeData(record);
				}
			}
		}

		public List<KeyValue<String, Long>> getSelectedValues() {
			ListGridRecord[] records = getSelectedRecords();
			if (records == null || records.length == 0) {
				return null;
			}

			List<KeyValue<String, Long>> values = new ArrayList<>();
			for (ListGridRecord record : records) {
				String key = record.getAttribute("unitKey");
				Integer value = record.getAttributeAsInt("currentValue");
				if (value == null) {
					//未入力の場合にエラーになるので-1
					value = -1;
				} else if (value < -1) {
					//-1未満は-1
					value = -1;
				}
				KeyValue<String, Long> keyValue = new KeyValue<>(key, Long.valueOf(value));
				values.add(keyValue);
			}
			return values;
		}

		private void itemValueChanged(int rowNum) {
			ListGridRecord record = (ListGridRecord)getRecord(rowNum);

			//変更がない状態のもののみ変更ありに更新(オリジナルとの値チェックまではしない)
			String status = record.getAttribute("status");
			if (status.equals(NO_CHANGE)) {
				record.setAttribute("status", UPDATE);
			}
		}

	}
}
