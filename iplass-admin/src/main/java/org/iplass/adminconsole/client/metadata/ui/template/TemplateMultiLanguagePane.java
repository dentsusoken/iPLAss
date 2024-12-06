/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePaneController.LanguageGrid;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TemplateMultiLanguagePane extends VLayout {

	private final TemplateMultiLanguagePaneController controller = GWT.create(TemplateMultiLanguagePaneController.class);

	private LanguageGrid grid;

	public TemplateMultiLanguagePane() {
		grid = controller.createLanguageGrid();
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				editLanguage((ListGridRecord) event.getRecord());
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

	protected enum FIELD_NAME {
		STORED_LANG_KEY,
		LANG_KEY,
		LANG_DISPLAY_NAME,
		VALUE_OBJECT,
		UPLOAD_FILE
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
