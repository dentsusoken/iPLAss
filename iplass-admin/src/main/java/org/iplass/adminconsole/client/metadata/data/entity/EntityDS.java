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

package org.iplass.adminconsole.client.metadata.data.entity;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class EntityDS extends MetaDataNameDS {

    public static void setDataSource(final SelectItem item) {
    	setDataSource(item, EntityDefinition.class);
    }
    public static void setDataSource(final SelectItem item, final MetaDataNameDSOption option) {
    	setDataSource(item, EntityDefinition.class, option);
    }

    public static void setDataSource(final ComboBoxItem item) {
    	setDataSource(item, EntityDefinition.class);
    }
    public static void setDataSource(final ComboBoxItem item, final MetaDataNameDSOption option) {
    	setDataSource(item, EntityDefinition.class, option);
    }

    public static void setDataSource(final ListGrid grid) {
    	setDataSource(grid, new MetaDataNameDSOption());
    }
    public static void setDataSource(final ListGrid grid, final MetaDataNameDSOption option) {
    	grid.setDataSource(getInstance(option));
		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME, DataSourceConstants.FIELD_NAME_TITLE);
		grid.setFields(nameField);
    }

	public static MetaDataNameDS getInstance() {
		return getInstance(EntityDefinition.class, new MetaDataNameDSOption());
	}

	public static MetaDataNameDS getInstance(MetaDataNameDSOption option) {
		return getInstance(EntityDefinition.class, option);
	}

	protected EntityDS(Class<? extends Definition> definition) {
		super(definition, new MetaDataNameDSOption());
	}

}
