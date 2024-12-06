/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguageEditDialog.TemplateDataChangedEvent;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.FIELD_NAME;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedBinaryDefinitionInfo;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedReportDefinitionInfo;
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
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TemplateMultiLanguagePaneControllerImpl implements TemplateMultiLanguagePaneController {

	@Override
	public LanguageGrid createLanguageGrid() {
		return new LanguageGridImpl();
	}

	protected static class LanguageGridImpl extends LanguageGrid {

		protected LinkedHashMap<String, String> enableLang;

		protected TemplateType type;
		protected String templateDefName;

		public LanguageGridImpl() {
			setWidth100();
			setHeight(1);

			setShowAllColumns(true); //列を全て表示
			setShowAllRecords(false); //レコードを全て表示
			setCanResizeFields(true); //列幅変更可能
			setCanSort(false); //ソート不可
			setCanPickFields(false); //表示フィールドの選択不可
			setCanGroupBy(false); //GroupByの選択不可
			setAutoFitWidthApproach(AutoFitWidthApproach.BOTH); //AutoFit時にタイトルと値を参照
			setLeaveScrollbarGap(false); //縦スクロールバー自動表示制御
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);

			ListGridField languageKeyField = new ListGridField(FIELD_NAME.LANG_KEY.name(), "LanguageKey");
			languageKeyField.setHidden(true);
			ListGridField languageField = new ListGridField(FIELD_NAME.LANG_DISPLAY_NAME.name(), "Language");
			setFields(languageKeyField, languageField);
		}

		@Override
		public void setEnableLanguages(LinkedHashMap<String, String> enableLang) {
			this.enableLang = enableLang;
		}

		@Override
		public void clearRecord() {
			for (Record record : getRecords()) {
				removeData(record);
			}
		}

		@Override
		public void setDefinition(TemplateType type, TemplateDefinition definition) {
			this.type = type;
			this.templateDefName = definition.getName();

			switch (type) {
			case HTML:
				HtmlTemplateDefinition htd = (HtmlTemplateDefinition) definition;
				initString(htd.getLocalizedSourceList());
				break;
			case GROOVY:
				GroovyTemplateDefinition gtd = (GroovyTemplateDefinition) definition;
				initString(gtd.getLocalizedSourceList());
				break;
			case JSP:
				JspTemplateDefinition jtd = (JspTemplateDefinition) definition;
				initString(jtd.getLocalizedPathList());
				break;
			case BINARY:
				BinaryTemplateDefinition btd = (BinaryTemplateDefinition) definition;
				initBinary(btd.getLocalizedBinaryList());
				break;
			case REPORT:
				ReportTemplateDefinition rtd = (ReportTemplateDefinition) definition;
				initReport(rtd.getLocalizedReportList());
				break;
			default:
			}
		}

		@Override
		public TemplateDefinition getEditDefinition(TemplateDefinition definition) {
			switch (type) {
			case HTML:
				List<LocalizedStringDefinition> htdList = getStringDefinitionList();
				HtmlTemplateDefinition htd = (HtmlTemplateDefinition) definition;
				htd.setLocalizedSourceList(htdList);
				break;
			case GROOVY:
				List<LocalizedStringDefinition> gtdList = getStringDefinitionList();
				GroovyTemplateDefinition gtd = (GroovyTemplateDefinition) definition;
				gtd.setLocalizedSourceList(gtdList);
				break;
			case JSP:
				List<LocalizedStringDefinition> jtdList = getStringDefinitionList();
				JspTemplateDefinition jtd = (JspTemplateDefinition) definition;
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

		@Override
		public List<LocalizedBinaryDefinitionInfo> getEditBinaryDefinitionList() {
			if (type != TemplateType.BINARY) {
				return null;
			}

			List<LocalizedBinaryDefinitionInfo> list = new ArrayList<LocalizedBinaryDefinitionInfo>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrapped = (WrappedListGridRecord) record;

				LocalizedBinaryDefinition definition = (LocalizedBinaryDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
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

		@Override
		public List<LocalizedReportDefinitionInfo> getEditReportDefinitionList() {
			if (type != TemplateType.REPORT) {
				return null;
			}

			List<LocalizedReportDefinitionInfo> list = new ArrayList<LocalizedReportDefinitionInfo>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrapped = (WrappedListGridRecord) record;

				LocalizedReportDefinition definition = (LocalizedReportDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
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

		@Override
		public void removeRecord(ListGridRecord record) {
			removeData(record);
		}

		@Override
		public void editRecord(final ListGridRecord record) {

			final WrappedListGridRecord wrapped = (WrappedListGridRecord) record;

			TemplateMultiLanguageEditDialog dialog = new TemplateMultiLanguageEditDialog(enableLang);

			switch (type) {
			case HTML:
				LocalizedStringDefinition htdLocal = null;
				if (wrapped != null) {
					htdLocal = (LocalizedStringDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				} else {
					htdLocal = new LocalizedStringDefinition();
				}
				dialog.setDefinition(type, htdLocal);
				break;
			case GROOVY:
				LocalizedStringDefinition gtdLocal = null;
				if (wrapped != null) {
					gtdLocal = (LocalizedStringDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				} else {
					gtdLocal = new LocalizedStringDefinition();
				}
				dialog.setDefinition(type, gtdLocal);
				break;
			case JSP:
				LocalizedStringDefinition jtdLocal = null;
				if (wrapped != null) {
					jtdLocal = (LocalizedStringDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
				} else {
					jtdLocal = new LocalizedStringDefinition();
				}
				dialog.setDefinition(type, jtdLocal);
				break;
			case BINARY:
				LocalizedBinaryDefinition btdLocal = null;
				UploadFileItem btdFileItem = null;
				if (wrapped != null) {
					btdLocal = (LocalizedBinaryDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
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
					rtdLocal = (LocalizedReportDefinition) wrapped.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name());
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

					TemplateDataChangedEvent tEvent = (TemplateDataChangedEvent) event;
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

		@Override
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

		@Override
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

		@Override
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

		@Override
		public List<String> getSelectedLocaleList() {
			List<String> localeList = new ArrayList<String>();
			for (ListGridRecord record : getRecords()) {
				WrappedListGridRecord wrap = (WrappedListGridRecord) record;
				localeList.add(wrap.getLocaleName());
			}
			return localeList;
		}

		protected void initString(List<LocalizedStringDefinition> definitionList) {
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedStringDefinition definition : definitionList) {
					WrappedListGridRecord newRecord = createStringRecord(definition, null);
					newRecord.setStoredLocaleName(definition.getLocaleName());
					temp[cnt] = newRecord;
					cnt++;
				}
				setData(temp);
			}
		}

		protected void initBinary(List<LocalizedBinaryDefinition> definitionList) {
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedBinaryDefinition definition : definitionList) {
					WrappedListGridRecord newRecord = createBinaryRecord(definition, null);
					newRecord.setStoredLocaleName(definition.getLocaleName());
					temp[cnt] = newRecord;
					cnt++;

				}
				setData(temp);
			}
		}

		protected void initReport(List<LocalizedReportDefinition> definitionList) {
			if (definitionList != null && definitionList.size() > 0) {
				ListGridRecord[] temp = new ListGridRecord[definitionList.size()];

				int cnt = 0;
				for (LocalizedReportDefinition definition : definitionList) {
					WrappedListGridRecord newRecord = createReportRecord(definition, null);
					newRecord.setStoredLocaleName(definition.getLocaleName());
					temp[cnt] = newRecord;
					cnt++;

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

		protected List<LocalizedStringDefinition> getStringDefinitionList() {

			List<LocalizedStringDefinition> list = new ArrayList<LocalizedStringDefinition>();
			for (ListGridRecord record : getRecords()) {
				list.add((LocalizedStringDefinition) record.getAttributeAsObject(FIELD_NAME.VALUE_OBJECT.name()));
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
}
