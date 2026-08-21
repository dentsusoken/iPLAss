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

import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.io.upload.UploadFileItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.FIELD_NAME;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedBinaryDefinitionInfo;
import org.iplass.adminconsole.client.metadata.ui.template.TemplateMultiLanguagePane.LocalizedReportDefinitionInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public interface TemplateMultiLanguagePaneController {
	LanguageGrid createLanguageGrid();

	abstract static class LanguageGrid extends ListGrid {

		abstract void setEnableLanguages(LinkedHashMap<String, String> enableLang);

		abstract void clearRecord();

		abstract void setDefinition(TemplateType type, TemplateDefinition definition);

		abstract TemplateDefinition getEditDefinition(TemplateDefinition definition);

		abstract List<LocalizedBinaryDefinitionInfo> getEditBinaryDefinitionList();

		abstract List<LocalizedReportDefinitionInfo> getEditReportDefinitionList();

		abstract void removeRecord(ListGridRecord record);

		abstract void editRecord(final ListGridRecord record);

		abstract void updateRecord(WrappedListGridRecord record, LocalizedStringDefinition definition);

		abstract void updateRecord(WrappedListGridRecord record, LocalizedBinaryDefinition definition, UploadFileItem fileItem);

		abstract void updateRecord(WrappedListGridRecord record, LocalizedReportDefinition definition, UploadFileItem fileItem);

		abstract List<String> getSelectedLocaleList();

	}

	static class WrappedListGridRecord extends ListGridRecord {

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

}
