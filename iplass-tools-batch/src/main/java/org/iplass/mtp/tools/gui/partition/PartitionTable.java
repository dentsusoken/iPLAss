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

package org.iplass.mtp.tools.gui.partition;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.iplass.mtp.impl.tools.tenant.PartitionInfo;

public class PartitionTable extends JTable {

	private static final long serialVersionUID = 6158350463892104161L;

	private PartitionTableModel model;

	public PartitionTable() {

		setAutoCreateRowSorter(true);	//ソート機能
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		List<PartitionTableColumnInfo> cols = initColumnInfo();

		model = new PartitionTableModel(cols);
		setModel(model);

		for (int i = 0; i < cols.size(); i ++) {
			getColumnModel().getColumn(i).setPreferredWidth(cols.get(i).getColSize());
		}

	}

	@Override
	public PartitionTableModel getModel() {
		return model;
	}

	private List<PartitionTableColumnInfo> initColumnInfo () {

		List<PartitionTableColumnInfo> cols = new ArrayList<PartitionTableColumnInfo>();
		cols.add(new PartitionTableColumnInfo("TableName", 300));
		cols.add(new PartitionTableColumnInfo("MaxTenantId", 150));

		return cols;
	}

	private static class PartitionTableColumnInfo {
		private String name;
		private int colSize;

		public PartitionTableColumnInfo(String name, int colSize) {
			this.name = name;
			this.colSize = colSize;
		}

		public String getName() {
			return name;
		}
		public int getColSize() {
			return colSize;
		}
	}

	public static class PartitionTableModel extends DefaultTableModel {

		private static final long serialVersionUID = -7508376391663965403L;

		private List<PartitionInfo> data;

		public PartitionTableModel(List<PartitionTableColumnInfo> cols) {
			for (PartitionTableColumnInfo col : cols) {
				addColumn(col.getName());
			}
		}

		public void setPartitionData(List<PartitionInfo> data) {
			this.data = data;
		}

		public PartitionInfo getPartitionInfo(int row) {
			if (row >= getRowCount()) {
				return null;
			}
			return data.get(row);
		}

        @Override
		public int getRowCount() {
			return data !=null ? data.size() : 0;
		}

		@Override
		public Object getValueAt(int row, int column) {
			PartitionInfo info = getPartitionInfo(row);
			if (info == null) {
				return null;
			}

			switch (column) {
				case 0:
					return info.getTableName();
				case 1:
					return info.getMaxTenantId();
				default:
					break;
			}

			return super.getValueAt(row, column);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
		}

		@Override
        public Class<?> getColumnClass(int column) {
			if (getRowCount() == 0) {
				return String.class;
			}
			Object value = getValueAt(0, column);
			if (value == null) {
				return String.class;
			} else {
				return value.getClass();
			}
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
	}

}
