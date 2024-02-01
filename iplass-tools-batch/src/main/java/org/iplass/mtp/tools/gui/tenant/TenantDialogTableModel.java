/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.gui.tenant;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.iplass.mtp.impl.tools.tenant.TenantInfo;

@SuppressWarnings("serial")
public class TenantDialogTableModel extends DefaultTableModel {

	public static final String[] COLUMNS = {"id", "name", "url"};

	private List<TenantInfo> data;

	public TenantDialogTableModel() {
		super(COLUMNS, 0);
	}

	public void setTenantData(List<TenantInfo> data) {
		this.data = data;
	}

	public TenantInfo getTenantInfo(int row) {
		if (row >= getRowCount()) {
			return null;
		}
		return data.get(row);
	}

	public List<TenantInfo> getTenantInfos() {
		return data;
	}

    @Override
	public int getRowCount() {
		return data !=null ? data.size() : 0;
	}

	@Override
	public Object getValueAt(int row, int column) {
		TenantInfo tenant = getTenantInfo(row);
		if (tenant == null) {
			return null;
		}

		switch (column) {
			case 0:
				return tenant.getId();
			case 1:
				return tenant.getName();
			case 2:
				return tenant.getUrl();
			default:
				break;
		}

		return super.getValueAt(row, column);
	}

	@Override
    public Class<?> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}
