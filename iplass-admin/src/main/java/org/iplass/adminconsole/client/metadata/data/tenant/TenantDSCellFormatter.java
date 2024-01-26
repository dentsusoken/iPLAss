/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.data.tenant;

import java.util.Date;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TenantDSCellFormatter implements CellFormatter {

	// Grid上のフォーマットだが、入力可能なので入力形式に合わせる
	private final DateTimeFormat dateFormatter = SmartGWTUtil.createInputDateFormat();

	public TenantDSCellFormatter() {
	}

	@Override
	public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
		if (value == null) {
			return null;
		}

		if (TenantColType.DATE.equals((TenantColType) (record.getAttributeAsObject("colType")))) {
			// 日付のフォーマット
			return dateFormatter.format((Date) value);
		} else if (TenantColType.PASSWORD.equals((TenantColType) (record.getAttributeAsObject("colType")))) {
			// パスワードは表示しない
			return "*****";
		} else {
			return value.toString();
		}
	}

}
