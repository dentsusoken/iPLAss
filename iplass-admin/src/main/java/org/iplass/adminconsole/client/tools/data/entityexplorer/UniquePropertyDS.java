/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UniquePropertyDS extends AbstractAdminDataSource {

	private static final DataSourceField[] fields;

	static {
        DataSourceField name = new DataSourceField(
        		DataSourceConstants.FIELD_NAME,
        		FieldType.TEXT,
        		DataSourceConstants.FIELD_NAME_TITLE);

        fields = new DataSourceField[] {name};
	}

	private EntityDefinition definition;

	/**
	 * <p>{@link EntityDefinition} に対応するDataSourceを返します。</p>
	 *
	 * @return 対象 {@link EntityDefinition} 用のDataSource
	 */
	public static UniquePropertyDS getInstance(EntityDefinition definition) {
		return new UniquePropertyDS(definition);
	}

    /**
     * SelectItemに対してDataSourceを設定します。
     *
     * @param item 対象SelectItem
     */
    public static void setDataSource(SelectItem item, EntityDefinition definition) {
    	item.setOptionDataSource(UniquePropertyDS.getInstance(definition));
    	item.setValueField(DataSourceConstants.FIELD_NAME);
    }

	/**
	 * コンストラクタ
	 *
	 */
	private UniquePropertyDS(EntityDefinition definition) {
		this.definition = definition;

        setFields(fields);
        setClientOnly(true);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		List<ListGridRecord> records = new ArrayList<ListGridRecord>();

		ListGridRecord record = new ListGridRecord();
		record.setAttribute(DataSourceConstants.FIELD_NAME, Entity.OID);
		records.add(record);
		for (PropertyDefinition pd : definition.getPropertyList()) {
			if (pd.getIndexType() == IndexType.UNIQUE || pd.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
				record = new ListGridRecord();
				record.setAttribute(DataSourceConstants.FIELD_NAME, pd.getName());
				records.add(record);
			}
		}
		response.setData(records.toArray(new ListGridRecord[]{}));
		response.setTotalRows(records.size());
		processResponse(requestId, response);
	}

}
