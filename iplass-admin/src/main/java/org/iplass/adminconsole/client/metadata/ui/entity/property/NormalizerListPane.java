/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpSelectItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.NormalizerListGridRecord.NormalizerType;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.NormalizerEditDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.property.normalizer.NormalizerEditDialog.NormalizerEditDialogHandler;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.NewlineNormalizer;
import org.iplass.mtp.entity.definition.normalizers.NewlineType;
import org.iplass.mtp.entity.definition.normalizers.WhiteSpaceTrimmer;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class NormalizerListPane extends VLayout implements PropertyAttributePane {

	/** Trim */
	private CheckboxItem chkTrimSpace;

	/** 改行文字 */
	private SelectItem selNewLineType;

	private NormalizerListGrid grid;

	private IButton addButton;
	private IButton delButton;

	/** 読み取り専用 */
	private boolean readOnly;

	public NormalizerListPane() {

		chkTrimSpace = new CheckboxItem();
		chkTrimSpace.setTitle("Trim White Space");
		chkTrimSpace.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				changeTrimSpace(SmartGWTUtil.getBooleanValue(chkTrimSpace));
			}
		});
		SmartGWTUtil.addHoverToFormItem(chkTrimSpace, rs("ui_metadata_entity_property_NormalizerListPane_chkTrimSpace"));

		selNewLineType = new MtpSelectItem();
		selNewLineType.setTitle("Unify Line Breaks Code");
		LinkedHashMap<String, String> newLineTypeMap = new LinkedHashMap<>();
		newLineTypeMap.put("", "");
		for (NewlineType type : NewlineType.values()) {
			newLineTypeMap.put(type.name(), type.name());
		}
		selNewLineType.setValueMap(newLineTypeMap);
		selNewLineType.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				changeNewLineType(SmartGWTUtil.getStringValue(selNewLineType));
			}
		});
		SmartGWTUtil.addHoverToFormItem(selNewLineType, rs("ui_metadata_entity_property_NormalizerListPane_selNewLineType"));

		final DynamicForm formShortCut = new MtpForm2Column();
		formShortCut.setItems(chkTrimSpace, selNewLineType);

		grid = new NormalizerListGrid();

		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				showNormalizerEditDialog(false, (NormalizerListGridRecord)event.getRecord());
			}
		});

		addButton = new IButton("Add");
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showNormalizerEditDialog(true, new NormalizerListGridRecord(null));
			}
		});
		delButton = new IButton("Remove");
		delButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				grid.removeSelectedData();

				checkTrimSpaceRecord();
				checkNewLineRecord();
			}
		});

		HLayout footer = new HLayout(5);
		footer.setWidth100();
		footer.setHeight(30);
		footer.setAlign(Alignment.LEFT);
		footer.setMargin(5);
		footer.addMember(addButton);
		footer.addMember(delButton);

		addMember(SmartGWTUtil.titleLabel("Normalizers"));
		addMember(formShortCut);
		addMember(grid);
		addMember(footer);
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord propertyRecord, PropertyAttribute typeAttribute) {
		if (propertyRecord.isInherited()) {
			readOnly = true;
			chkTrimSpace.setDisabled(true);
			selNewLineType.setDisabled(true);
			addButton.setDisabled(true);
			delButton.setDisabled(true);
		}

		if (SmartGWTUtil.isNotEmpty(propertyRecord.getNormalizerList())) {
			List<NormalizerListGridRecord> recordList = new ArrayList<>();

			for (NormalizerDefinition definition : propertyRecord.getNormalizerList()) {
				NormalizerListGridRecord normalizerRecord = new NormalizerListGridRecord(definition);
				recordList.add(normalizerRecord);
			}

			NormalizerListGridRecord[] records = new NormalizerListGridRecord[recordList.size()];
			grid.setData(recordList.toArray(records));
		}

		checkTrimSpaceRecord();
		checkNewLineRecord();
	}

	@Override
	public void applyTo(PropertyListGridRecord propertyRecord) {
		List<NormalizerDefinition> normalizerList = new ArrayList<>();
		for (ListGridRecord record : grid.getRecords()) {
			NormalizerListGridRecord nrecord = (NormalizerListGridRecord)record;
			normalizerList.add(nrecord.getNormalizerDefinition());
		}
		propertyRecord.setNormalizerList(normalizerList);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public int panelHeight() {
		return 190;
	}

	/**
	 * Trim White Space 変更処理
	 *
	 * @param true:Trim White Space
	 */
	private void changeTrimSpace(boolean isTrimeSpace) {

		//一度WhiteSpaceTrimmerを全部削除
		RecordList list = grid.getRecordList();
		Record[] records = list.findAll(NormalizerListGridRecord.TYPE_NAME, NormalizerType.TRIM_WHITE_SPACE.displayName());
		if (records != null) {
			for (Record record : records) {
				grid.removeData(record);
			}
		}

		if (isTrimeSpace) {
			//選択された場合、WhiteSpaceTrimmerを追加
			WhiteSpaceTrimmer definition = new WhiteSpaceTrimmer();
			NormalizerListGridRecord recode = new NormalizerListGridRecord(definition);

			//先頭に追加
			grid.getRecordList().addAt(recode, 0);
			grid.refreshFields();
		}
	}

	/**
	 * レコードのTrim White Spaceチェック処理
	 */
	private void checkTrimSpaceRecord() {
		boolean isExistTrimSpace = false;
		ListGridRecord[] records = grid.getRecords();
		for (ListGridRecord record : records) {
			NormalizerListGridRecord nrecord = (NormalizerListGridRecord)record;
			if (NormalizerType.TRIM_WHITE_SPACE.equals(nrecord.getType())) {
				isExistTrimSpace = true;
				break;
			}
		}

		chkTrimSpace.setValue(isExistTrimSpace);
	}

	/**
	 * 改行コード 変更処理
	 * @param newLineType 改行コード
	 */
	private void changeNewLineType(String newLineType) {

		//一度NewlineNormalizerを全部削除
		RecordList list = grid.getRecordList();
		Record[] records = list.findAll(NormalizerListGridRecord.TYPE_NAME, NormalizerType.NORMALIZE_NEW_LINE.displayName());
		if (records != null) {
			for (Record record : records) {
				grid.removeData(record);
			}
		}

		if (SmartGWTUtil.isNotEmpty(newLineType)) {

			//選択された場合、NewlineNormalizerを追加
			NewlineNormalizer definition = new NewlineNormalizer();
			definition.setType(NewlineType.valueOf(newLineType));

			NormalizerListGridRecord recode = new NormalizerListGridRecord(definition);

			//最後に追加
			grid.addData(recode);
		}
	}

	/**
	 * レコードの改行コードチェック処理
	 */
	private void checkNewLineRecord() {
		NewlineType newLineType = null;
		ListGridRecord[] records = grid.getRecords();
		for (ListGridRecord record : records) {
			NormalizerListGridRecord nrecord = (NormalizerListGridRecord)record;
			if (NormalizerType.NORMALIZE_NEW_LINE.equals(nrecord.getType())) {
				NewlineNormalizer definition = (NewlineNormalizer)nrecord.getNormalizerDefinition();
				newLineType = definition.getType();
			}
		}

		if (newLineType != null) {
			selNewLineType.setValue(newLineType.name());
		} else {
			selNewLineType.clearValue();
		}
	}

	/**
	 * 編集ダイアログを表示します。
	 *
	 * @param isNewRow true:追加
	 * @param target 対象レコード
	 */
	private void showNormalizerEditDialog(final boolean isNewRow, final NormalizerListGridRecord target) {

		NormalizerEditDialog dialog = new NormalizerEditDialog(target, readOnly, new NormalizerEditDialogHandler() {
			@Override
			public void onSaved(NormalizerListGridRecord record) {
				if (isNewRow) {
					grid.addData(record);
				} else {
					grid.updateData(record);
					grid.refreshFields();
				}

				checkTrimSpaceRecord();
				checkNewLineRecord();
			}
		});
		dialog.show();
	}

	private String rs(String key) {
		return AdminClientMessageUtil.getString(key);
	}

}
