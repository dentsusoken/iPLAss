/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.staticresource.StaticResourceMultiLanguageEditDialog.StaticResourceDataChangedEvent;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.LocalizedStaticResourceInfo;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceInfo;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class StaticResourceMultiLanguagePane extends VLayout {

	private LanguageGrid grid;

	public StaticResourceMultiLanguagePane() {
		grid = new LanguageGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editLanguage((ListGridRecord)event.getRecord());
			}
		});

		IButton addLang = new IButton("Add");
		addLang.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addLanguage();
			}
		});

		IButton delLang = new IButton("Remove");
		delLang.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (grid.getSelectedRecord() != null) {
					deleteLanguage(grid.getSelectedRecord());
				}
			}
		});

		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.addMember(addLang);
		buttonPane.addMember(delLang);

		addMember(grid);
		addMember(buttonPane);
	}

	public void setEnableLanguages(LinkedHashMap<String, String> enableLang) {
		grid.setEnableLanguages(enableLang);
	}

	public void clearRecord() {
		grid.clearRecord();
	}

	public void setDefinition(StaticResourceInfo definition) {
		grid.clearRecord();
		grid.setDefinition(definition);
	}

	public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {
		return grid.getEditDefinition(definition);
	}

	public List<LocalizedStaticResourceDefinitionInfo> getEditDefinitionList() {
		return grid.getEditStaticResourceDefinitionList();
	}

	public boolean validate() {
		Set<String> check = new HashSet<String>();
		for (String locale : grid.getSelectedLocaleList()) {
			if (check.contains(locale)) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_staticresource_StaticResourceEditPane_duplicateLocaleNameErr"));
				return false;
			}
			check.add(locale);
		}
		return true;
	}

	private void addLanguage() {
		editLanguage(null);
	}

	private void editLanguage(final ListGridRecord record) {
		grid.editRecord(record);
	}

	private void deleteLanguage(final ListGridRecord record) {
		grid.removeRecord(record);
	}

	private enum FIELD_NAME {
		STORED_LANG_KEY,
		LANG_KEY,
		LANG_DISPLAY_NAME,
		VALUE_OBJECT,
		UPLOAD_FILE
	}

	private static class LanguageGrid extends ListGrid {
		private LinkedHashMap<String, String> enableLang;

		public LanguageGrid() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true);							//列を全て表示
			setShowAllRecords(false);							//レコードを全て表示
			setCanResizeFields(true);							//列幅変更可能
			setCanSort(false);									//ソート不可
			setCanPickFields(false);							//表示フィールドの選択不可
			setCanGroupBy(false);								//GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);	//AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false);						//縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			ListGridField languageKeyField = new ListGridField(FIELD_NAME.LANG_KEY.name(), "LanguageKey");
			languageKeyField.setHidden(true);
			ListGridField languageField = new ListGridField(FIELD_NAME.LANG_DISPLAY_NAME.name(), "Language");
			setFields(languageKeyField, languageField);
		}

		public void setEnableLanguages(LinkedHashMap<String, String> enableLang) {
			this.enableLang = enableLang;
		}

		public void clearRecord() {
			for (Record record : getRecords()) {
				removeData(record);
			}
		}

		public void setDefinition(StaticResourceInfo definition) {
			List<LocalizedStaticResourceInfo> localizedResourceList = definition.getLocalizedResourceList();

			if (localizedResourceList != null && !localizedResourceList.isEmpty()) {
				ListGridRecord[] temp = new ListGridRecord[localizedResourceList.size()];

				int cnt = 0;
				for (LocalizedStaticResourceInfo info : localizedResourceList) {
					WrappedListGridRecord newRecord = createRecord(info, null);
					newRecord.setStoredLocaleName(info.getLocaleName());
					temp[cnt] = newRecord;
					cnt++;
				}
				setData(temp);
			}
		}

		public StaticResourceInfo getEditDefinition(StaticResourceInfo definition) {
			return definition;
		}

		public List<LocalizedStaticResourceDefinitionInfo> getEditStaticResourceDefinitionList() {
			List<LocalizedStaticResourceDefinitionInfo> list = new ArrayList<LocalizedStaticResourceDefinitionInfo>();

			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrapped = (WrappedListGridRecord) record;

				LocalizedStaticResourceInfo definition = (LocalizedStaticResourceInfo) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				UploadFileItem fileItem = wrapped.getUploadFileItem();
				String storedLocaleName = wrapped.getStoredLocaleName();

				LocalizedStaticResourceDefinitionInfo info = new LocalizedStaticResourceDefinitionInfo(definition, fileItem, storedLocaleName);
				list.add(info);
			}

			if (list.isEmpty()) {
				return null;
			}

			return list;
		}

		public void removeRecord(ListGridRecord record) {
			removeData(record);
		}

		public void editRecord(final ListGridRecord record) {
			final WrappedListGridRecord wrapped = (WrappedListGridRecord) record;

			LocalizedStaticResourceInfo definition = null;
			UploadFileItem fileItem = null;

			if (wrapped != null) {
				definition = (LocalizedStaticResourceInfo) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				fileItem = wrapped.getUploadFileItem();
			} else {
				definition = new LocalizedStaticResourceInfo();
			}

			StaticResourceMultiLanguageEditDialog dialog = new StaticResourceMultiLanguageEditDialog(enableLang);

			dialog.setDefinition(definition, fileItem);
			dialog.clearDataChangeHandlers();
			dialog.addDataChangeHandler(new DataChangedHandler() {
				@Override
				public void onDataChanged(DataChangedEvent event) {
					StaticResourceDataChangedEvent srdcEvent = (StaticResourceDataChangedEvent) event;
					LocalizedStaticResourceInfo lsrDef = srdcEvent.getValueObject(LocalizedStaticResourceInfo.class);
					UploadFileItem binaryFile = srdcEvent.getUploadFileItem();
					updateRecord(wrapped, lsrDef, binaryFile);
				}
			});

			dialog.show();
		}

		private void updateRecord(WrappedListGridRecord record, LocalizedStaticResourceInfo definition, UploadFileItem fileItem) {
			WrappedListGridRecord newRecord = createRecord(definition, record);

			//選択ファイル情報入れ替え
			newRecord.setUploadFileItem(fileItem);

			if (record != null) {
				updateData(newRecord);
			} else {
				//追加
				addData(newRecord);
			}
			refreshFields();
		}

		private WrappedListGridRecord createRecord(LocalizedStaticResourceInfo info, WrappedListGridRecord record) {
			if (record == null) {
				record = new WrappedListGridRecord();
			}

			record.setAttribute(FIELD_NAME.LANG_KEY.name(), info.getLocaleName());
			record.setAttribute(FIELD_NAME.LANG_DISPLAY_NAME.name(), enableLang.get(info.getLocaleName()));
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), info);

			return record;
		}

		private List<String> getSelectedLocaleList() {
			List<String> localeList = new ArrayList<String>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrap = (WrappedListGridRecord) record;
				localeList.add(wrap.getLocaleName());
			}
			return localeList;
		}
	}

	private static class WrappedListGridRecord extends ListGridRecord {

		private UploadFileItem fileItem;

		public WrappedListGridRecord() {
		}

		public void setUploadFileItem(UploadFileItem fileItem) {
			this.fileItem = fileItem;
		}

		public UploadFileItem getUploadFileItem() {
			return fileItem;
		}

		public void setStoredLocaleName(String localeName) {
			setAttribute(FIELD_NAME.STORED_LANG_KEY.name(), localeName);
		}

		public String getStoredLocaleName() {
			return getAttribute(FIELD_NAME.STORED_LANG_KEY.name());
		}

		public String getLocaleName() {
			String localeName = getAttribute(FIELD_NAME.LANG_KEY.name());
			if (SmartGWTUtil.isEmpty(localeName)) {
				return "";
			} else {
				return localeName;
			}
		}
	}

	public static class LocalizedStaticResourceDefinitionInfo {

		private final LocalizedStaticResourceInfo definition;

		private final UploadFileItem fileItem;

		private final String storedLang;

		public LocalizedStaticResourceDefinitionInfo(LocalizedStaticResourceInfo definition, UploadFileItem fileItem, String storedLang) {
			this.definition = definition;
			this.fileItem = fileItem;
			this.storedLang = storedLang;
		}

		public LocalizedStaticResourceInfo getDefinition() {
			return definition;
		}

		public UploadFileItem getFileItem() {
			return fileItem;
		}

		public String getStoredLang() {
			return storedLang;
		}
	}

}
