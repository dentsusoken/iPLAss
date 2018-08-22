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

package org.iplass.adminconsole.client.metadata.ui.template;

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
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguageEditDialog.TemplateDataChangedEvent;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.GroovyTemplateDefinition;
import org.iplass.mtp.web.template.definition.HtmlTemplateDefinition;
import org.iplass.mtp.web.template.definition.JspTemplateDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;

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

public class TemplateMultiLanguagePane  extends VLayout {

	private LanguageGrid grid;

	public TemplateMultiLanguagePane() {
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

	public void setDefinition(TemplateType type, TemplateDefinition definition) {
		grid.setDefinition(type, definition);
	}

	public TemplateDefinition getEditDefinition(TemplateDefinition definition) {
		return grid.getEditDefinition(definition);
	}

	public List<LocalizedBinaryDefinitionInfo> getEditBinaryDefinitionList() {
		return grid.getEditBinaryDefinitionList();
	}

	public List<LocalizedReportDefinitionInfo> getEditReportDefinitionList() {
		return grid.getEditReportDefinitionList();
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		Set<String> check = new HashSet<String>();
		for (String locale : grid.getSelectedLocaleList()) {
			if (check.contains(locale)) {
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_template_TemplateEditPane_duplicateLocaleNameErr"));
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

		private TemplateType type;
		private String templateDefName;

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

		public void setDefinition(TemplateType type, TemplateDefinition definition) {
			this.type = type;
			this.templateDefName = definition.getName();

			switch (type) {
				case HTML:
					HtmlTemplateDefinition htd = (HtmlTemplateDefinition)definition;
					initString(htd.getLocalizedSourceList());
					break;
				case GROOVY:
					GroovyTemplateDefinition gtd = (GroovyTemplateDefinition)definition;
					initString(gtd.getLocalizedSourceList());
					break;
				case JSP:
					JspTemplateDefinition jtd = (JspTemplateDefinition)definition;
					initString(jtd.getLocalizedPathList());
					break;
				case BINARY:
					BinaryTemplateDefinition btd = (BinaryTemplateDefinition)definition;
					initBinary(btd.getLocalizedBinaryList());
					break;
				case REPORT:
					ReportTemplateDefinition rtd = (ReportTemplateDefinition)definition;
					initReport(rtd.getLocalizedReportList());
					break;
				default:
			}
		}

		public TemplateDefinition getEditDefinition(TemplateDefinition definition) {

			switch (type) {
				case HTML:
					List<LocalizedStringDefinition> htdList = getStringDefinitionList();
					HtmlTemplateDefinition htd = (HtmlTemplateDefinition)definition;
					htd.setLocalizedSourceList(htdList);
					break;
				case GROOVY:
					List<LocalizedStringDefinition> gtdList = getStringDefinitionList();
					GroovyTemplateDefinition gtd = (GroovyTemplateDefinition)definition;
					gtd.setLocalizedSourceList(gtdList);
					break;
				case JSP:
					List<LocalizedStringDefinition> jtdList = getStringDefinitionList();
					JspTemplateDefinition jtd = (JspTemplateDefinition)definition;
					jtd.setLocalizedPathList(jtdList);
					break;
				case BINARY:
					//BinaryはDefinitionにしないでLocalizedBinaryDefinitionInfoとして渡す
					//List<LocalizedBinaryDefinition> btdList = getBinaryDefinitionList();
					//BinaryTemplateDefinition btd = (BinaryTemplateDefinition)definition;
					//btd.setLocalizedBinaryList(btdList);
					break;
				case REPORT:
					//ReportはDefinitionにしないでLocalizedReportDefinitionInfoとして渡す
					//List<LocalizedReportDefinition> rtdList = getReportDefinitionList();
					//ReportTemplateDefinition rtd = (ReportTemplateDefinition)definition;
					//rtd.setLocalizedReportList(rtdList);
					break;
				default:
			}

			return definition;
		}

		public List<LocalizedBinaryDefinitionInfo> getEditBinaryDefinitionList() {
			if (type != TemplateType.BINARY) {
				return null;
			}

			List<LocalizedBinaryDefinitionInfo> list = new ArrayList<LocalizedBinaryDefinitionInfo>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrapped = (WrappedListGridRecord)record;

				LocalizedBinaryDefinition definition = (LocalizedBinaryDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				UploadFileItem fileItem = wrapped.getUploadFileItem();
				String storedLocaleName = wrapped.getStoredLocaleName();

				LocalizedBinaryDefinitionInfo info = new LocalizedBinaryDefinitionInfo(definition, fileItem, storedLocaleName);
				list.add(info);
			}
			if (list.isEmpty()) {
				return null;
			}
			return list;
		}

		public List<LocalizedReportDefinitionInfo> getEditReportDefinitionList() {
			if (type != TemplateType.REPORT) {
				return null;
			}

			List<LocalizedReportDefinitionInfo> list = new ArrayList<LocalizedReportDefinitionInfo>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrapped = (WrappedListGridRecord)record;

				LocalizedReportDefinition definition = (LocalizedReportDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				UploadFileItem fileItem = wrapped.getUploadFileItem();
				String storedLocaleName = wrapped.getStoredLocaleName();

				LocalizedReportDefinitionInfo info = new LocalizedReportDefinitionInfo(definition, fileItem, storedLocaleName);
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

			final WrappedListGridRecord wrapped = (WrappedListGridRecord)record;

			TemplateMultiLanguageEditDialog dialog = new TemplateMultiLanguageEditDialog(enableLang);

			switch (type) {
				case HTML:
					LocalizedStringDefinition htdLocal = null;
					if (wrapped != null) {
						htdLocal = (LocalizedStringDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
					} else {
						htdLocal = new LocalizedStringDefinition();
					}
					dialog.setDefinition(type, htdLocal);
					break;
				case GROOVY:
					LocalizedStringDefinition gtdLocal = null;
					if (wrapped != null) {
						gtdLocal = (LocalizedStringDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
					} else {
						gtdLocal = new LocalizedStringDefinition();
					}
					dialog.setDefinition(type, gtdLocal);
					break;
				case JSP:
					LocalizedStringDefinition jtdLocal = null;
					if (wrapped != null) {
						jtdLocal = (LocalizedStringDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
					} else {
						jtdLocal = new LocalizedStringDefinition();
					}
					dialog.setDefinition(type, jtdLocal);
					break;
				case BINARY:
					LocalizedBinaryDefinition btdLocal = null;
					UploadFileItem btdFileItem = null;
					if (wrapped != null) {
						btdLocal = (LocalizedBinaryDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
						btdFileItem = wrapped.getUploadFileItem();
					} else {
						btdLocal = new LocalizedBinaryDefinition();
					}
					dialog.setDefinition(type, btdLocal, templateDefName, btdFileItem);
					break;
				case REPORT:
					LocalizedReportDefinition rtdLocal = null;
					UploadFileItem rtdFileItem = null;
					if (wrapped != null) {
						rtdLocal = (LocalizedReportDefinition)wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
						rtdFileItem = wrapped.getUploadFileItem();
					} else {
						rtdLocal = new LocalizedReportDefinition();
					}
					dialog.setDefinition(type, rtdLocal, templateDefName, rtdFileItem);
					break;
				default:
			}

			final TemplateMultiLanguageEditDialog viewDialog = dialog;
			viewDialog.clearDataChangeHandlers();
			viewDialog.addDataChangeHandler(new DataChangedHandler() {

				@Override
				public void onDataChanged(DataChangedEvent event) {

					TemplateDataChangedEvent tEvent = (TemplateDataChangedEvent)event;
					switch (type) {
						case HTML:
						case GROOVY:
						case JSP:
							LocalizedStringDefinition stringParam = tEvent.getValueObject(LocalizedStringDefinition.class);
							updateRecord(wrapped, stringParam);
							break;
						case BINARY:
							LocalizedBinaryDefinition binaryDef = tEvent.getValueObject(LocalizedBinaryDefinition.class);
							UploadFileItem binaryFile = tEvent.getUploadFileItem();
							updateRecord(wrapped, binaryDef, binaryFile);
							break;
						case REPORT:
							LocalizedReportDefinition reportDef = tEvent.getValueObject(LocalizedReportDefinition.class);
							UploadFileItem reportFile = tEvent.getUploadFileItem();
							updateRecord(wrapped, reportDef, reportFile);
							break;
						default:
					}
				}
			});

			dialog.show();
		}

		public void updateRecord(WrappedListGridRecord record, LocalizedStringDefinition definition) {
			ListGridRecord newRecord = createStringRecord(definition, record);

			if (record != null) {
				updateData(newRecord);
			} else {
				//追加
				addData(newRecord);
			}
			refreshFields();
		}

		public void updateRecord(WrappedListGridRecord record, LocalizedBinaryDefinition definition, UploadFileItem fileItem) {

			WrappedListGridRecord newRecord = createBinaryRecord(definition, record);

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

		public void updateRecord(WrappedListGridRecord record, LocalizedReportDefinition definition, UploadFileItem fileItem) {

			WrappedListGridRecord newRecord = createReportRecord(definition, record);

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

		public List<String> getSelectedLocaleList() {
			List<String> localeList = new ArrayList<String>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrap = (WrappedListGridRecord)record;
				localeList.add(wrap.getLocaleName());
			}
			return localeList;
		}

		private void initString(List<LocalizedStringDefinition> definitionList) {
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedStringDefinition definition : definitionList) {
					WrappedListGridRecord newRecord = createStringRecord(definition, null);
					newRecord.setStoredLocaleName(definition.getLocaleName());
					temp[cnt] = newRecord;
					cnt ++;
				}
				setData(temp);
			}
		}

		private void initBinary(List<LocalizedBinaryDefinition> definitionList) {
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedBinaryDefinition definition : definitionList) {
					WrappedListGridRecord newRecord = createBinaryRecord(definition, null);
					newRecord.setStoredLocaleName(definition.getLocaleName());
					temp[cnt] = newRecord;
					cnt ++;

				}
				setData(temp);
			}
		}

		private void initReport(List<LocalizedReportDefinition> definitionList) {
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedReportDefinition definition : definitionList) {
					WrappedListGridRecord newRecord = createReportRecord(definition, null);
					newRecord.setStoredLocaleName(definition.getLocaleName());
					temp[cnt] = newRecord;
					cnt ++;

				}
				setData(temp);
			}
		}

		private WrappedListGridRecord createStringRecord(LocalizedStringDefinition lsd, WrappedListGridRecord record) {

			if (record == null) {
				record = new WrappedListGridRecord();
			}
			record.setAttribute(FIELD_NAME.LANG_KEY.name(), lsd.getLocaleName());
			record.setAttribute(FIELD_NAME.LANG_DISPLAY_NAME.name(), enableLang.get(lsd.getLocaleName()));
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), lsd);

			return record;
		}

		private WrappedListGridRecord createBinaryRecord(LocalizedBinaryDefinition lbd, WrappedListGridRecord record) {

			if (record == null) {
				record = new WrappedListGridRecord();
			}
			record.setAttribute(FIELD_NAME.LANG_KEY.name(), lbd.getLocaleName());
			record.setAttribute(FIELD_NAME.LANG_DISPLAY_NAME.name(), enableLang.get(lbd.getLocaleName()));
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), lbd);

			return record;
		}

		private WrappedListGridRecord createReportRecord(LocalizedReportDefinition lrd, WrappedListGridRecord record) {

			if (record == null) {
				record = new WrappedListGridRecord();
			}
			record.setAttribute(FIELD_NAME.LANG_KEY.name(), lrd.getLocaleName());
			record.setAttribute(FIELD_NAME.LANG_DISPLAY_NAME.name(), enableLang.get(lrd.getLocaleName()));
			record.setAttribute(FIELD_NAME.VALUE_OBJECT.name(), lrd);

			return record;
		}

		private List<LocalizedStringDefinition> getStringDefinitionList() {

			List<LocalizedStringDefinition> list = new ArrayList<LocalizedStringDefinition>();
			for (ListGridRecord record : getRecords()) {
				list.add((LocalizedStringDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
			}
			if (list.isEmpty()) {
				return null;
			}
			return list;
		}

//		private List<LocalizedBinaryDefinition> getBinaryDefinitionList() {
//
//			List<LocalizedBinaryDefinition> list = new ArrayList<LocalizedBinaryDefinition>();
//			for (ListGridRecord record : getRecords()) {
//				list.add((LocalizedBinaryDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
//			}
//			if (list.isEmpty()) {
//				return null;
//			}
//			return list;
//		}
//
//		private List<LocalizedReportDefinition> getReportDefinitionList() {
//
//			List<LocalizedReportDefinition> list = new ArrayList<LocalizedReportDefinition>();
//			for (ListGridRecord record : getRecords()) {
//				list.add((LocalizedReportDefinition)record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
//			}
//			if (list.isEmpty()) {
//				return null;
//			}
//			return list;
//		}

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

	public static class LocalizedBinaryDefinitionInfo {

		private final LocalizedBinaryDefinition definition;

		private final UploadFileItem fileItem;

		private final String storedLang;

		public LocalizedBinaryDefinitionInfo(LocalizedBinaryDefinition definition, UploadFileItem fileItem, String storedLang) {
			this.definition = definition;
			this.fileItem = fileItem;
			this.storedLang = storedLang;
		}

		public LocalizedBinaryDefinition getDefinition() {
			return definition;
		}

		public UploadFileItem getFileItem() {
			return fileItem;
		}

		public String getStoredLang() {
			return storedLang;
		}
	}

	public static class LocalizedReportDefinitionInfo {

		private final LocalizedReportDefinition definition;

		private final UploadFileItem fileItem;

		private final String storedLang;

		public LocalizedReportDefinitionInfo(LocalizedReportDefinition definition, UploadFileItem fileItem, String storedLang) {
			this.definition = definition;
			this.fileItem = fileItem;
			this.storedLang = storedLang;
		}

		public LocalizedReportDefinition getDefinition() {
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
