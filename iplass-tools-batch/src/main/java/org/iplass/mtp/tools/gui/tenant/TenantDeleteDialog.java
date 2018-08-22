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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.tools.tenant.TenantDeleteParameter;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.tools.batch.tenant.TenantBatch;
import org.iplass.mtp.tools.batch.tenant.TenantBatch.TenantBatchExecMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class TenantDeleteDialog extends JDialog {

	private static Logger logger = LoggerFactory.getLogger(TenantDeleteDialog.class);

	private String language;

	private TenantDialogTableModel model;

	protected JCheckBox chkMySQLDropPartition;

	private JButton btnDelete;
	private JButton btnCancel;

	private JTextArea txtMessageArea;

	private List<ChangeListener> dataChangeListners = new ArrayList<ChangeListener>();

	public TenantDeleteDialog(Frame owner, String language) {
		super(owner);

		setLanguage(language);

		createDialog();
	}

	public void addTenantDataChangeListner(ChangeListener listner) {
		dataChangeListners.add(listner);
	}

	public void setTenantInfo(List<TenantInfo> infos) {
		model.setTenantData(infos);
		model.fireTableDataChanged();
	}

	private String getLanguage() {
		return language;
	}

	private void setLanguage(String language) {
		this.language = language;
	}

	private void createDialog() {

		setTitle("Remove Tenant");
		setBounds(64, 64, 300, 310);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);//中央表示

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setPreferredSize(new Dimension(500, 300));
		mainPane.setLayout(new BorderLayout());

		mainPane.add(createInfoPane(), BorderLayout.NORTH);
		mainPane.add(createListPane(), BorderLayout.CENTER);
		mainPane.add(createFooterPane(), BorderLayout.SOUTH);

		getContentPane().add(mainPane, BorderLayout.NORTH);
		getContentPane().add(createMessagePane(), BorderLayout.CENTER);

		pack();
	}

	private JPanel createInfoPane() {

		JPanel infoPane = new JPanel();
		infoPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPane.setLayout(new BorderLayout(5, 5));

		JPanel messagePane = new JPanel();
		messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.Y_AXIS));

		JLabel lblInfo1 = new JLabel(rs("TenantManagerApp.TenantDeleteDialog.removeTenantLabel"));
		messagePane.add(lblInfo1);

		JLabel lblInfo2 = new JLabel(rs("TenantManagerApp.TenantDeleteDialog.warnTrancateLabel"));
		lblInfo2.setForeground(Color.RED);
		messagePane.add(lblInfo2);

		RdbAdapterService adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
		RdbAdapter adapter = adapterService.getRdbAdapter();
		if (adapter instanceof MysqlRdbAdaptor) {
			JLabel lblInfo3 = new JLabel(rs("TenantManagerApp.TenantDeleteDialog.warnAlterTablePermissionLabel"));
			lblInfo3.setForeground(Color.RED);
			messagePane.add(lblInfo3);
		}

		JLabel lblDummy = new JLabel();
		messagePane.add(lblDummy);

		infoPane.add(new JLabel(UIManager.getIcon("OptionPane.warningIcon")), BorderLayout.WEST);
		infoPane.add(messagePane, BorderLayout.CENTER);

		return infoPane;
	}

	private JPanel createListPane() {

		JPanel listPane = new JPanel();
		listPane.setLayout(new BorderLayout());

		listPane.add(createTenantTablePane(), BorderLayout.CENTER);
		listPane.add(createInputPane(), BorderLayout.SOUTH);

		return listPane;
	}


	private JScrollPane createTenantTablePane() {

		JScrollPane scrlTenantList = new JScrollPane();

		model = new TenantDialogTableModel();

		JTable tblTenantList = new JTable(model) {};
		tblTenantList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		scrlTenantList.setViewportView(tblTenantList);

		return scrlTenantList;
	}

	protected JPanel createInputPane() {

		JPanel inputPane = new JPanel();
		inputPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		inputPane.setLayout(gridbag);

		//縦余白(Label、Text間)
		constraints.gridx = 1;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		Box dummy = Box.createHorizontalBox();
		dummy.setPreferredSize(new Dimension(15, 15));
		gridbag.setConstraints(dummy, constraints);
		inputPane.add(dummy);

		//縦余白(Text、Check間)
		constraints.gridx = 3;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		dummy = Box.createHorizontalBox();
		dummy.setPreferredSize(new Dimension(15, 15));
		gridbag.setConstraints(dummy, constraints);
		inputPane.add(dummy);

		//縦余白(Check、後間)
		constraints.gridx = 5;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		dummy = Box.createHorizontalBox();
		dummy.setPreferredSize(new Dimension(15, 15));
		gridbag.setConstraints(dummy, constraints);
		inputPane.add(dummy);

		RdbAdapterService adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
		RdbAdapter adapter = adapterService.getRdbAdapter();
		if (adapter instanceof MysqlRdbAdaptor) {
			chkMySQLDropPartition = new JCheckBox("MySQL Drop Partition");
			chkMySQLDropPartition.setSelected(true);
			createCheckBoxRow(chkMySQLDropPartition, 0, gridbag, constraints, inputPane);
		}

		return inputPane;
	}

	protected void createLableText(JComponent label, JComponent text, JComponent check,
			int row, GridBagLayout gridbag, GridBagConstraints constraints, JPanel pane) {

		constraints.gridx = 0;
		constraints.gridy = (row * 2);
		constraints.gridwidth= 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(label, constraints);
		pane.add(label);

		constraints.gridx = 2;
		constraints.gridheight = 1;
		gridbag.setConstraints(text, constraints);
		pane.add(text);

		if (check != null) {
			constraints.gridx = 4;
			constraints.gridheight = 1;
			gridbag.setConstraints(check, constraints);
			pane.add(check);
		}

		//行余白
		constraints.gridx = 0;
		constraints.gridy = (row * 2) + 1;
		constraints.gridwidth= GridBagConstraints.REMAINDER;
		Box dummy = Box.createHorizontalBox();
		dummy.setPreferredSize(new Dimension(15, 15));
		gridbag.setConstraints(dummy, constraints);
		pane.add(dummy);
	}

	protected void createCheckBoxRow(JComponent check,
			int row, GridBagLayout gridbag, GridBagConstraints constraints, JPanel pane) {

		constraints.gridx = 0;
		constraints.gridy = (row * 2);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		gridbag.setConstraints(check, constraints);
		pane.add(check);

		//行余白
		constraints.gridx = 0;
		constraints.gridy = (row * 2) + 1;
		constraints.gridwidth= GridBagConstraints.REMAINDER;
		Box dummy = Box.createHorizontalBox();
		dummy.setPreferredSize(new Dimension(15, 15));
		gridbag.setConstraints(dummy, constraints);
		pane.add(dummy);
	}

	private JPanel createFooterPane() {

		JPanel footerPane = new JPanel();
		footerPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		btnDelete = new JButton("Remove");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (JOptionPane.showConfirmDialog(TenantDeleteDialog.this,
						rs("TenantManagerApp.TenantDeleteDialog.confirmRemoveTenantMsg"),
						"CONFIRM", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
					return;
				}

				//テナントの削除
				deleteTenant();
			}
		});
		footerPane.add(btnDelete);

		btnCancel = new JButton("Cancel");
		btnCancel.setDefaultCapable(true);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TenantDeleteDialog.this.setVisible(false);
				TenantDeleteDialog.this.dispose();
			}
		});
		footerPane.add(btnCancel);

		return footerPane;
	}

	private JTabbedPane createMessagePane() {

		JTabbedPane tabMessagePane = new JTabbedPane(JTabbedPane.TOP);
		tabMessagePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JScrollPane sclMessagePane = new JScrollPane();
		txtMessageArea = new JTextArea();
		txtMessageArea.setEditable(false);
		txtMessageArea.setFont(new Font(rs("TenantManagerApp.TenantDeleteDialog.messageFont"), Font.PLAIN, 10));
		txtMessageArea.setRows(10);
		sclMessagePane.setViewportView(txtMessageArea);
		tabMessagePane.addTab("Log", sclMessagePane);

		return tabMessagePane;
	}

	private void deleteTenant() {

		btnDelete.setText("remove...");
		btnDelete.setEnabled(false);
		btnCancel.setEnabled(false);
		txtMessageArea.setText("");

		DeleteTenantWorker worker = new DeleteTenantWorker();
		worker.execute();
	}

	private class DeleteTenantWorker extends SwingWorker<Boolean, String> {

		/**
		 * 非同期処理
		 */
		@Override
		protected Boolean doInBackground() throws Exception {

			TenantBatch manager = null;
			try {
				manager = new TenantBatch(TenantBatchExecMode.DELETE.name(), getLanguage());
				manager.addLogListner(new TenantBatch.LogListner() {

					@Override
					public void info(String message) {
						publish(message);
						logger.info(message);
					}

					@Override
					public void warn(String message) {
						publish(message);
						logger.warn(message);
					}

					@Override
					public void error(String message) {
						publish(message);
						logger.error(message);
					}

					@Override
					public void info(String message, Throwable e) {
						publish(message);
						logger.info(message, e);
					}

					@Override
					public void warn(String message, Throwable e) {
						publish(message);
						logger.warn(message, e);
					}

					@Override
					public void error(String message, Throwable e) {
						publish(message);
						logger.error(message, e);
					}
				});

				//削除対象
				List<TenantInfo> tenants = model.getTenantInfos();
				for (TenantInfo tenant : tenants) {
					publish(rs("TenantManagerApp.TenantDeleteDialog.removeStartLog", tenant.getName(), tenant.getId()));

					boolean ret = manager.executeDelete(createParameter(tenant));
					//削除失敗したらそこで終了
					if (!ret) {
						return ret;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return true;
		}

		@Override
		protected void process(List<String> chunks) {
			// パブリッシュされた文字をテキストエリアに追加
			for (String message: chunks) {
				addLog(message);
			}
		}

		/**
		 * 非同期処理後処理
		 */
		@Override
		protected void done() {
			try {
				Boolean isSuccess = get();

				if (isSuccess) {
					JOptionPane.showMessageDialog(TenantDeleteDialog.this, rs("TenantManagerApp.TenantDeleteDialog.removeCompleteMsg"),
							"INFO", JOptionPane.INFORMATION_MESSAGE);

					fireTenantDataChanged();
				} else {
					JOptionPane.showMessageDialog(TenantDeleteDialog.this, getCommonResourceMessage("errorMsg"),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				addLog(getCommonResourceMessage("errorMsg"));
				addLog(e.getMessage());

				JOptionPane.showMessageDialog(TenantDeleteDialog.this, getCommonResourceMessage("errorMsg"),
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			btnDelete.setText("Remove");
			btnDelete.setEnabled(true);
			btnCancel.setEnabled(true);
		}
	}

	private TenantDeleteParameter createParameter(TenantInfo tenant) {
		TenantDeleteParameter param = new TenantDeleteParameter();
		param.setTenantId(tenant.getId());
		param.setTenantName(tenant.getName());
		if (chkMySQLDropPartition != null) {
			param.setMySqlDropPartition(chkMySQLDropPartition.isSelected());
		}
		return param;
	}

	private void addLog(String log) {
		txtMessageArea.append(log + "\n");
	}

	private void fireTenantDataChanged() {
		ChangeEvent e = new ChangeEvent(this);
		for (ChangeListener listner : dataChangeListners) {
			listner.stateChanged(e);
		}
	}

	private String rs(String key, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), key, args);
	}

	private String getCommonResourceMessage(String subKey, Object... args) {
		return ToolsBatchResourceBundleUtil.commonResourceString(getLanguage(), subKey, args);
	}
}
