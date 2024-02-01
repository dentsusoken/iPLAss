/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.rdb.postgresql.PostgreSQLRdbAdapter;
import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.rdb.TenantRdbConstants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase.LogListener;
import org.iplass.mtp.tools.batch.partition.MySQLPartitionBatch;
import org.iplass.mtp.tools.batch.partition.PartitionBatch;
import org.iplass.mtp.tools.batch.partition.PostgreSQLPartitionBatch;
import org.iplass.mtp.tools.batch.tenant.TenantBatch.TenantBatchExecMode;
import org.iplass.mtp.tools.gui.MtpJDialogBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartitionCreateDialog extends MtpJDialogBase {

	private static final long serialVersionUID = 3059528383221001309L;

	private static Logger logger = LoggerFactory.getLogger(PartitionCreateDialog.class);

	private JTextField txtMaxTenantId;

	private JTextField txtSubPartitionSize;

	private JButton btnCreate;
	private JButton btnCancel;

	private JTextArea txtMessageArea;

	private List<ChangeListener> dataChangeListners = new ArrayList<>();

	private final RdbAdapter adapter = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();

	public PartitionCreateDialog(Frame owner) {
		super(owner);

		createDialog();
	}

	public void addPartitionDataChangeListner(ChangeListener listner) {
		dataChangeListners.add(listner);
	}

	private void createDialog() {

		setTitle("Create Partition");
		setBounds(64, 64, 300, 500);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);//中央表示

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout());

		mainPane.add(createInfoPane(), BorderLayout.NORTH);
		mainPane.add(createInputPane(), BorderLayout.CENTER);
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

		JLabel lblInfo1 = new JLabel(rs("MySQLPartitionManagerApp.PartitionCreateDialog.createPartitionLabel"));
		messagePane.add(lblInfo1);

		JLabel lblInfo2 = new JLabel(rs("MySQLPartitionManagerApp.PartitionCreateDialog.warnAlterTablePermissionLabel"));
		lblInfo2.setForeground(Color.RED);
		messagePane.add(lblInfo2);

		JLabel lblDummy = new JLabel();
		messagePane.add(lblDummy);

		infoPane.add(new JLabel(UIManager.getIcon("OptionPane.warningIcon")), BorderLayout.WEST);
		infoPane.add(messagePane, BorderLayout.CENTER);

		return infoPane;
	}

	private JPanel createInputPane() {

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

		JLabel lblMaxTenantId = new JLabel("Max Tenant ID");
		txtMaxTenantId = new JTextField();
		txtMaxTenantId.setPreferredSize(new Dimension(200, 25));
		createLableText(lblMaxTenantId, txtMaxTenantId, null, 0, gridbag, constraints, inputPane);

		if (adapter instanceof PostgreSQLRdbAdapter) {
			JLabel lblSubPartitionSize = new JLabel("SubPartition Size");
			txtSubPartitionSize = new JTextField();
			txtSubPartitionSize.setPreferredSize(new Dimension(200, 25));
			txtSubPartitionSize.setText(String.valueOf(TenantRdbConstants.MAX_SUBPARTITION));
			createLableText(lblSubPartitionSize, txtSubPartitionSize, null, 1, gridbag, constraints, inputPane);
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

		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					//numチェック
					Integer.parseInt(txtMaxTenantId.getText());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(PartitionCreateDialog.this,
							rs("MySQLPartitionManagerApp.PartitionCreateDialog.inputMaxTenantIdMsg"),
							"ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (txtSubPartitionSize != null) {
					String strSubPartitionSize = txtSubPartitionSize.getText().trim();
					if (strSubPartitionSize.isEmpty()) {
						JOptionPane.showMessageDialog(PartitionCreateDialog.this,
								rs("PartitionManagerApp.PartitionCreateDialog.inputSubPartitionSizeMsg"),
								"ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						int subPartitionSize = Integer.parseInt(strSubPartitionSize);
						if (subPartitionSize < TenantRdbConstants.MIN_SUBPARTITION) {
							JOptionPane.showMessageDialog(PartitionCreateDialog.this,
									rs("PartitionManagerApp.PartitionCreateDialog.invalidValueSubPartitionSizeMsg", TenantRdbConstants.MIN_SUBPARTITION),
									"ERROR", JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(PartitionCreateDialog.this,
								rs("PartitionManagerApp.PartitionCreateDialog.invalidSubPartitionSizeMsg"),
								"ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				if (JOptionPane.showConfirmDialog(PartitionCreateDialog.this,
						rs("MySQLPartitionManagerApp.PartitionCreateDialog.confirmCreatePartitionMsg"),
						"CONFIRM", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
					return;
				}

				createPartition();
			}
		});
		footerPane.add(btnCreate);

		btnCancel = new JButton("Cancel");
		btnCancel.setDefaultCapable(true);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PartitionCreateDialog.this.setVisible(false);
				PartitionCreateDialog.this.dispose();
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
		txtMessageArea.setFont(new Font(rs("MySQLPartitionManagerApp.PartitionCreateDialog.messageFont"), Font.PLAIN, 10));
		txtMessageArea.setRows(20);
		sclMessagePane.setViewportView(txtMessageArea);
		tabMessagePane.addTab("Log", sclMessagePane);

		return tabMessagePane;
	}

	private void createPartition() {

		btnCreate.setText("create...");
		btnCreate.setEnabled(false);
		btnCancel.setEnabled(false);
		txtMessageArea.setText("");

		CreatePartitionWorker worker = new CreatePartitionWorker();
		worker.execute();
	}

	private class CreatePartitionWorker extends SwingWorker<Boolean, String> {

		/**
		 * 非同期処理
		 */
		@Override
		protected Boolean doInBackground() throws Exception {

			LogListener listener = null;
			try {
				PartitionBatch manager = null;
				if (adapter instanceof MysqlRdbAdaptor) {
					manager = new MySQLPartitionBatch(TenantBatchExecMode.CREATE.name());
				}
				if (adapter instanceof PostgreSQLRdbAdapter) {
					manager = new PostgreSQLPartitionBatch(TenantBatchExecMode.CREATE.name());
				}

				listener = new MySQLPartitionBatch.LogListener() {

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
				};
				manager.addLogListner(listener);

				publish(rs("MySQLPartitionManagerApp.PartitionCreateDialog.startCreatePartitionLog"));

				manager.createPartition(createParameter());

				listener.info("");
				listener.info("■Execute Result : SUCCESS");
				listener.info("");

			} catch (Exception e) {
				listener.error(rs("Common.errorMsg", e.getMessage()), e);
				listener.info("");
				listener.error("■Execute Result : FAILED");
				listener.info("");
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
					JOptionPane.showMessageDialog(PartitionCreateDialog.this,
							rs("MySQLPartitionManagerApp.PartitionCreateDialog.createCompleteMsg"),
							"INFO", JOptionPane.INFORMATION_MESSAGE);

					firePartitionDataChanged();
				} else {
					JOptionPane.showMessageDialog(PartitionCreateDialog.this, rs("Common.errorMsg"),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(PartitionCreateDialog.this, rs("Common.errorMsg", e.getMessage()),
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			btnCreate.setText("Create");
			btnCreate.setEnabled(true);
			btnCancel.setEnabled(true);
		}
	}

	private PartitionCreateParameter createParameter() {
		PartitionCreateParameter param = new PartitionCreateParameter();
		param.setOnlyPartitionCreate(true);
		int tenantId = Integer.parseInt(txtMaxTenantId.getText());
		param.setTenantId(tenantId);
		if (txtSubPartitionSize != null) {
			param.setSubPartitionSize(Integer.parseInt(txtSubPartitionSize.getText().trim()));
		}
		return param;
	}

	private void addLog(String log) {
		txtMessageArea.append(log + "\n");
	}

	private void firePartitionDataChanged() {
		ChangeEvent e = new ChangeEvent(this);
		for (ChangeListener listner : dataChangeListners) {
			listner.stateChanged(e);
		}
	}

}
