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

package org.iplass.adminconsole.client.base.data.eql;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.shared.base.dto.entity.EqlResultInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class EqlDS extends AbstractAdminDataSource {

	private EqlResultInfo result;

	public static EqlDS getInstance(EqlResultInfo result) {
		return new EqlDS(result);
	}

	/**
	 * コンストラクタ
	 *
	 */
	private EqlDS(EqlResultInfo result) {
		this.result = result;

		//Fieldの作成
		List<String> cols = result.getColumns();

		List<DataSourceField> fields = new ArrayList<DataSourceField>(cols.size());
		for (int i = 0; i < cols.size() ; i++) {
			DataSourceField field = new DataSourceTextField("col_" + i, SafeHtmlUtils.htmlEscape(cols.get(i)));
			fields.add(field);
		}
		setFields(fields.toArray(new DataSourceField[]{}));
	}

	@Override
	protected void executeFetch(String requestId, DSRequest request,
			DSResponse response) {

		GWT.log("EqlDS DEBUG AllCount    -> " + result.getRecords().size());
		GWT.log("EqlDS DEBUG getStartRow -> " + request.getStartRow());
		GWT.log("EqlDS DEBUG getEndRow   -> " + request.getEndRow());

		int start = request.getStartRow() != null ? request.getStartRow() : 0;
		int end = request.getEndRow() != null ? request.getEndRow() : -1;

		int size = result.getRecords().size();
		if (end >= 0) {
			//指定されている場合
			if (end < start) {
				size = 0;
			} else if (end > size){
				//size = size;
			} else {
				size = end;
			}
		}
		GWT.log("EqlDS DEBUG fetchSize   -> " + size);

		List<ListGridRecord> list = new ArrayList<ListGridRecord>(size);
		if (size > 0) {
			List<String> cols = result.getColumns();
			List<String[]> datas = result.getRecords();

			for (String[] fields : datas) {
				ListGridRecord record = new ListGridRecord();
				for (int j = 0; j < cols.size() ; j++) {
					//同一のフィールドがSelect指定されてもいいようにnameに列番号を付加
					record.setAttribute("col_" + j, SafeHtmlUtils.htmlEscape(fields[j]));
				}
				list.add(record);
			}
		}

		response.setData(list.toArray(new ListGridRecord[]{}));
		response.setTotalRows(result.getRecords().size ());

		processResponse(requestId, response);

	}

}
