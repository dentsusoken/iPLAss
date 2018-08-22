/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.tools.gui.partition.MySQLPartitionManagerApp;
import org.iplass.mtp.tools.gui.widget.menu.BasicMenuBar;

@SuppressWarnings("serial")
public class TenantManagerApp extends JFrame {

	private TenantToolService tenantToolService;

	private String language;

	private JButton btnRefresh;
	private TenantTableModel model;
	private JTextField txtCount;

	/**
	 * Launch the application.
	 *
	 * args[0]・・・language
	 */
	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String language = null;
					if (args != null) {
						if (args.length > 0) {
							if (!"system".equals(args[0])) {
								language = args[0];
							}
						}
					}

					TenantManagerApp frame = new TenantManagerApp(language);

//					frame.setExtendedState(Frame.MAXIMIZED_BOTH);	//最大化
					frame.setLocationRelativeTo(null);//中央表示
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TenantManagerApp(String language) {
		setLanguage(language);

		setTitle("Tenant Manager");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().add(new BasicMenuBar(this), BorderLayout.NORTH);

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		mainPane.setPreferredSize(new Dimension(830, 600));	//テーブルの列幅と調整
		getContentPane().add(mainPane, BorderLayout.CENTER);

		mainPane.add(createHeaderPane(), BorderLayout.NORTH);
		mainPane.add(createTenantList(), BorderLayout.CENTER);
		pack();

		tenantToolService = ServiceRegistry.getRegistry().getService(TenantToolService.class);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowActivated(e);
				//起動後に検索
				searchTenantList();
			}
		});
	}

	private String getLanguage() {
		return language;
	}

	private void setLanguage(String language) {
		this.language = language;
	}

	private JPanel createHeaderPane() {

		JPanel headerPane = new JPanel();
		headerPane.setLayout(new BorderLayout());

		JPanel headerMainPane = new JPanel();
		headerMainPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton btnAdd = new JButton("Create Default Tenant");
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TenantCreateDialog dialog = new TenantCreateDialog(TenantManagerApp.this, getLanguage());
				dialog.setModal(true);
				dialog.addTenantDataChangeListner(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						searchTenantList();
					}
				});
				dialog.setVisible(true);
			}
		});
		headerMainPane.add(btnAdd);

		JButton btnDelete = new JButton("Remove Tenant");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<TenantInfo> selected = model.getSelectedTenantInfo();
				if (selected.size() == 0) {
					JOptionPane.showMessageDialog(TenantManagerApp.this, rs("TenantManagerApp.requiredSelectTenantMsg"),
							"WARN", JOptionPane.WARNING_MESSAGE);
					return;
				}
				TenantDeleteDialog dialog = new TenantDeleteDialog(TenantManagerApp.this, getLanguage());
				dialog.setTenantInfo(selected);
				dialog.setModal(true);
				dialog.addTenantDataChangeListner(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						searchTenantList();
					}
				});
				dialog.setVisible(true);
			}
		});
		headerMainPane.add(btnDelete);

		RdbAdapterService adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
		RdbAdapter adapter = adapterService.getRdbAdapter();

		//TODO SQLServerはどうする？
		if (adapter != null && adapter instanceof MysqlRdbAdaptor) {
			JButton btnPartitionList = new JButton("Partition List");
			btnPartitionList.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					MySQLPartitionManagerApp.main(new String[]{getLanguage()});

					//自身を消す
					dispose();
				}
			});
			headerMainPane.add(btnPartitionList);
		}

		JPanel headerSubPane = new JPanel();
		headerSubPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		btnRefresh = new JButton("Refresh List");
		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchTenantList();
			}
		});

		JLabel lblCount = new JLabel("Count");
		lblCount.setHorizontalAlignment(JTextField.RIGHT);
		txtCount = new JTextField();
		txtCount.setPreferredSize(new Dimension(30,25));
		txtCount.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		txtCount.setHorizontalAlignment(JTextField.RIGHT);
		txtCount.setEditable(false);

		headerSubPane.add(btnRefresh);
		headerSubPane.add(lblCount);
		headerSubPane.add(txtCount);

		headerPane.add(headerMainPane, BorderLayout.CENTER);
		headerPane.add(headerSubPane, BorderLayout.EAST);

		return headerPane;
	}

	private JScrollPane createTenantList() {

		JScrollPane scrlTenantList = new JScrollPane();

		initColumnInfo();
		model = new TenantTableModel();

		JTable tblTenantList = new JTable(model);
		tblTenantList.setAutoCreateRowSorter(true);	//ソート機能

		tblTenantList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int i = 0;
		for (TenantTableColumnInfo columnInfo : COLUMNS) {
			tblTenantList.getColumnModel().getColumn(i++).setPreferredWidth(columnInfo.getColSize());
		}

		scrlTenantList.setViewportView(tblTenantList);

		return scrlTenantList;
	}

	private List<TenantTableColumnInfo> COLUMNS = new ArrayList<TenantTableColumnInfo>();
	private String[] COLUMN_NAMES;
	private static final int CHECKED_COL_NUM = 0;

	private void initColumnInfo () {
		COLUMNS.add(new TenantTableColumnInfo("*", 40));
		COLUMNS.add(new TenantTableColumnInfo("id", 60));
		COLUMNS.add(new TenantTableColumnInfo("name", 150));
		COLUMNS.add(new TenantTableColumnInfo("url", 150));
		COLUMNS.add(new TenantTableColumnInfo("yukoDateFrom", 100));
		COLUMNS.add(new TenantTableColumnInfo("yukoDateTo", 100));
		COLUMNS.add(new TenantTableColumnInfo("createDate", 100));
		COLUMNS.add(new TenantTableColumnInfo("updateDate", 100));

		COLUMN_NAMES = new String[COLUMNS.size()];
		int i = 0;
		for (TenantTableColumnInfo columnInfo : COLUMNS) {
			COLUMN_NAMES[i++] = columnInfo.getName();
		}
	}

	public class TenantTableColumnInfo {
		String name;
		int colSize;

		public TenantTableColumnInfo(String name, int colSize) {
			this.name = name;
			this.colSize = colSize;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getColSize() {
			return colSize;
		}
		public void setColSize(int colSize) {
			this.colSize = colSize;
		}

	}

	private class TenantTableModel extends DefaultTableModel {

		private List<TenantInfo> data;
		private List<Boolean> isSelects;

		public TenantTableModel() {
			super(COLUMN_NAMES, 0);
		}

		public void setTenantData(List<TenantInfo> data) {
			this.data = data;
			if (data == null) {
				isSelects = null;
			} else {
				isSelects = new ArrayList<Boolean>(data.size());
				for (int i = 0; i < data.size(); i++) {
					isSelects.add(Boolean.FALSE);
				}
			}
		}

		public TenantInfo getTenantInfo(int row) {
			if (row >= getRowCount()) {
				return null;
			}
			return data.get(row);
		}

		public List<TenantInfo> getSelectedTenantInfo() {
			List<TenantInfo> selected = new ArrayList<TenantInfo>();
			if (isSelects != null) {
				for (int i = 0; i < isSelects.size(); i++) {
					if (isSelects.get(i)) {
						selected.add(getTenantInfo(i));
					}
				}
			}
			return selected;
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
					return isSelects.get(row);
				case 1:
					return tenant.getId();
				case 2:
					return tenant.getName();
				case 3:
					return tenant.getUrl();
				case 4:
					return tenant.getYukoDateFrom();
				case 5:
					return tenant.getYukoDateTo();
				case 6:
					return tenant.getCreateDate();
				case 7:
					return tenant.getUpdateDate();
				default:
					break;
			}

			return super.getValueAt(row, column);
		}



		@Override
		public void setValueAt(Object aValue, int row, int column) {
			TenantInfo tenant = getTenantInfo(row);
			if (tenant == null) {
				return;
			}
			switch (column) {
				case 0:
					isSelects.set(row, (Boolean)aValue);
					return;
				default:
					break;
			}
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
            return col == CHECKED_COL_NUM;
        }
	}

	private void searchTenantList() {
		//tableにセット
		model.setTenantData(Collections.emptyList());
		model.fireTableDataChanged();

		btnRefresh.setText("search...");
		txtCount.setText("");
		btnRefresh.setEnabled(false);
		SearchTenantWorker worker = new SearchTenantWorker();
		worker.execute();
	}

	private class SearchTenantWorker extends SwingWorker<List<TenantInfo>, Object> {

		/**
		 * 非同期処理
		 */
		@Override
		protected List<TenantInfo> doInBackground() throws Exception {
			return tenantToolService.getAllTenantInfoList();
		}

		/**
		 * 非同期処理後処理
		 */
		@Override
		protected void done() {
			try {
				List<TenantInfo> result = get();

				//tableにセット
				model.setTenantData(result);
				model.fireTableDataChanged();

				//件数をセット
				txtCount.setText(String.valueOf(result.size()));
			} catch (ExecutionException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(TenantManagerApp.this, getCommonResourceMessage("errorMsg"),
						"ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(TenantManagerApp.this, getCommonResourceMessage("errorMsg"),
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
    		btnRefresh.setText("Refresh List");
    		btnRefresh.setEnabled(true);
		}

	}

	private String rs(String key, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), key, args);
	}

	private String getCommonResourceMessage(String subKey, Object... args) {
		return ToolsBatchResourceBundleUtil.commonResourceString(getLanguage(), subKey, args);
	}

}
