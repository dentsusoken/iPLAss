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

package org.iplass.adminconsole.client.metadata.ui.selectvalue;

import java.util.List;

import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SelectValueListGridRecord extends ListGridRecord {

	private final String VALUE = "value";
	private final String DISPNAME = "dispName";
	private final String LOCALLANG = "localizedDisplayNameList";

	public SelectValueListGridRecord() {

	}

	public SelectValueListGridRecord(String selectValue, String dispName, List<LocalizedStringDefinition> localizedDispNameList) {
		setAttribute(VALUE, selectValue);
		setAttribute(DISPNAME, dispName);
		setAttribute(LOCALLANG, localizedDispNameList);
	}

	public String getSelectValue() {
		return getAttribute(VALUE);
	}

	public void setSelectValue(String value) {
		setAttribute(VALUE, value);
	}

	public String getDispName() {
		return getAttribute(DISPNAME);
	}

	public void setDispName(String value) {
		setAttribute(DISPNAME, value);
	}

	@SuppressWarnings("unchecked")
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {

		if (getAttributeAsObject(LOCALLANG) == null) {
			return null;
		} else {
			Object object = getAttributeAsObject(LOCALLANG);
			return (List<LocalizedStringDefinition>)JSOHelper.convertToJava((JavaScriptObject) object);
		}
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDispNameList) {
		setAttribute(LOCALLANG, localizedDispNameList);
	}
}
