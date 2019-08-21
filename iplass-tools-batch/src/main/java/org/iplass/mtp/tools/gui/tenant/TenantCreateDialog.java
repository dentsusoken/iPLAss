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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.tenant.TenantBatch;
import org.iplass.mtp.tools.batch.tenant.TenantBatch.TenantBatchExecMode;
import org.iplass.mtp.tools.gui.MtpJDialogBase;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantCreateDialog extends MtpJDialogBase {

	private static final long serialVersionUID = -8293278349056121678L;

	private static Logger logger = LoggerFactory.getLogger(TenantCreateDialog.class);

	private TenantToolService toolService = ServiceRegistry.getRegistry().getService(TenantToolService.class);
	private I18nService i18nService = ServiceRegistry.getRegistry().getService(I18nService.class);
	private String defaultEnableLanguages = "";

	protected JTextField txtName;

	protected JTextField txtUrl;
	protected JCheckBox chkUrl;

	protected JTextField txtDisplayName;
	protected JCheckBox chkDisplayName;

	protected JTextField txtAdminUserId;

	protected JPasswordField txtAdminUserPass;
	protected JPasswordField txtConfirmAdminUserPass;

	protected JTextField txtTopUrl;
	protected JCheckBox chkTopUrl;

	protected JTextField txtUseLanguages;
	protected JCheckBox chkUseLanguages;

	protected JCheckBox chkBlankTenant;

	protected JCheckBox chkMySQLSubPartition;

	protected JButton btnCreate;
	protected JButton btnCancel;

	protected JTextArea txtMessageArea;

	protected List<ChangeListener> dataChangeListners = new ArrayList<ChangeListener>();

	public TenantCreateDialog(Frame owner) {
		super(owner);

		if (i18nService.getEnableLanguagesMap() != null) {
			for (String languageKey : i18nService.getEnableLanguagesMap().keySet()) {
				defaultEnableLanguages += (languageKey + ",");
			}
			if (defaultEnableLanguages.length() > 1) {
				defaultEnableLanguages = defaultEnableLanguages.substring(0, defaultEnableLanguages.length() - 1);
			}
		}

		createDialog();
	}

	public void addTenantDataChangeListner(ChangeListener listner) {
		dataChangeListners.add(listner);
	}

	protected void createDialog() {

		setTitle("Create Tenant");
		setBounds(64, 64, 300, 530);

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

		setDefaultValues();
	}

	private JPanel createInfoPane() {

		JPanel infoPane = new JPanel();
		infoPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPane.setLayout(new BorderLayout(5, 5));

		JPanel messagePane = new JPanel();
		messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.Y_AXIS));

		JLabel lblInfo1 = new JLabel(rs("TenantManagerApp.TenantCreateDialog.createTenantLabel"));
		messagePane.add(lblInfo1);

		RdbAdapterService adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
		RdbAdapter adapter = adapterService.getRdbAdapter();
		if (adapter instanceof MysqlRdbAdaptor) {
			JLabel lblInfo2 = new JLabel(rs("TenantManagerApp.TenantCreateDialog.warnAlterTablePermissionLabel"));
			lblInfo2.setForeground(Color.RED);
			messagePane.add(lblInfo2);
		}

		JLabel lblDummy = new JLabel();
		messagePane.add(lblDummy);

		infoPane.add(new JLabel(UIManager.getIcon("OptionPane.informationIcon")), BorderLayout.WEST);
		infoPane.add(messagePane, BorderLayout.CENTER);

		return infoPane;
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

		int rowIndex = 0;

		JLabel lblName = new JLabel("*name");
		txtName = new JTextField();
		txtName.setPreferredSize(new Dimension(200, 25));
		createLableText(lblName, txtName, null, rowIndex++, gridbag, constraints, inputPane);
		txtName.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				setDefaultValues();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		JLabel lblAdminUserId = new JLabel("*Admin User Id");
		txtAdminUserId = new JTextField();
		txtAdminUserId.setPreferredSize(new Dimension(200, 25));
		createLableText(lblAdminUserId, txtAdminUserId, null, rowIndex++, gridbag, constraints, inputPane);

		JLabel lblAdminUserPass = new JLabel("*Admin User Password");
		txtAdminUserPass = new JPasswordField();
		txtAdminUserPass.setPreferredSize(new Dimension(200, 25));
		createLableText(lblAdminUserPass, txtAdminUserPass, null, rowIndex++, gridbag, constraints, inputPane);

		JLabel lblConfirmAdminUserPass = new JLabel("*Confirm Password");
		txtConfirmAdminUserPass = new JPasswordField();
		txtConfirmAdminUserPass.setPreferredSize(new Dimension(200, 25));
		createLableText(lblConfirmAdminUserPass, txtConfirmAdminUserPass, null, rowIndex++, gridbag, constraints, inputPane);

		JLabel lblUrl = new JLabel("*url");
		txtUrl = new JTextField();
		txtUrl.setPreferredSize(new Dimension(200, 25));
		txtUrl.setEditable(false);
		chkUrl = new JCheckBox("default");
		chkUrl.setSelected(true);
		createLableText(lblUrl, txtUrl, chkUrl, rowIndex++, gridbag, constraints, inputPane);
		chkUrl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (chkUrl.isSelected()) {
					txtUrl.setEditable(false);
					setDefaultValues();
				} else {
					txtUrl.setEditable(true);
				}
			}
		});

		JLabel lblDisplayName = new JLabel("displayName");
		txtDisplayName = new JTextField();
		txtDisplayName.setPreferredSize(new Dimension(200, 25));
		txtDisplayName.setEditable(false);
		chkDisplayName = new JCheckBox("default");
		chkDisplayName.setSelected(true);
		createLableText(lblDisplayName, txtDisplayName, chkDisplayName, rowIndex++, gridbag, constraints, inputPane);
		chkDisplayName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (chkDisplayName.isSelected()) {
					txtDisplayName.setEditable(false);
					setDefaultValues();
				} else {
					txtDisplayName.setEditable(true);
				}
			}
		});

		JLabel lblTopUrl = new JLabel("TopUrl");
		txtTopUrl = new JTextField();
		txtTopUrl.setPreferredSize(new Dimension(200, 25));
		txtTopUrl.setEditable(false);
		chkTopUrl = new JCheckBox("default");
		chkTopUrl.setSelected(true);
		createLableText(lblTopUrl, txtTopUrl, chkTopUrl, rowIndex++, gridbag, constraints, inputPane);
		chkTopUrl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (chkTopUrl.isSelected()) {
					txtTopUrl.setEditable(false);
					setDefaultValues();
				} else {
					txtTopUrl.setEditable(true);
				}
			}
		});

		JLabel lblUseLanguages = new JLabel("UseLanguages");
		txtUseLanguages = new JTextField();
		txtUseLanguages.setPreferredSize(new Dimension(200, 25));
		txtUseLanguages.setEditable(false);
		chkUseLanguages = new JCheckBox("default");
		chkUseLanguages.setSelected(true);
		createLableText(lblUseLanguages, txtUseLanguages, chkUseLanguages, rowIndex++, gridbag, constraints, inputPane);
		chkUseLanguages.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (chkUseLanguages.isSelected()) {
					txtUseLanguages.setEditable(false);
					setDefaultValues();
				} else {
					txtUseLanguages.setEditable(true);
					txtUseLanguages.setText(defaultEnableLanguages);
				}
			}
		});

		chkBlankTenant = new JCheckBox("create blank Tenant");
		chkBlankTenant.setSelected(false);
		createCheckBoxRow(chkBlankTenant, rowIndex++, gridbag, constraints, inputPane);

		RdbAdapterService adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
		RdbAdapter adapter = adapterService.getRdbAdapter();
		if (adapter instanceof MysqlRdbAdaptor) {
			chkMySQLSubPartition = new JCheckBox("MySQL SubPartition Use");
			chkMySQLSubPartition.setSelected(true);
			createCheckBoxRow(chkMySQLSubPartition, rowIndex++, gridbag, constraints, inputPane);
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

	protected JPanel createFooterPane() {

		JPanel footerPane = new JPanel();
		footerPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!inputValidate()) {
					return;
				}
				if (!dataValidate()) {
					return;
				}

				if (JOptionPane.showConfirmDialog(TenantCreateDialog.this,
						rs("TenantManagerApp.TenantCreateDialog.confirmCreateTenantMsg"),
						"CONFIRM", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
					return;
				}

				//テナントの作成
				createTenant();
			}
		});
		footerPane.add(btnCreate);

		btnCancel = new JButton("Cancel");
		btnCancel.setDefaultCapable(true);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TenantCreateDialog.this.setVisible(false);
				TenantCreateDialog.this.dispose();
			}
		});
		footerPane.add(btnCancel);

		return footerPane;
	}

	protected JTabbedPane createMessagePane() {
		JTabbedPane tabMessagePane = new JTabbedPane(JTabbedPane.TOP);
		tabMessagePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JScrollPane sclMessagePane = new JScrollPane();
		txtMessageArea = new JTextArea();
		txtMessageArea.setEditable(false);
		txtMessageArea.setFont(new Font(rs("TenantManagerApp.TenantCreateDialog.messageFont"), Font.PLAIN, 10));
		txtMessageArea.setRows(10);
		sclMessagePane.setViewportView(txtMessageArea);
		tabMessagePane.addTab("Log", sclMessagePane);

		return tabMessagePane;

	}


	protected void setDefaultValues() {
		if (chkUrl.isSelected()) {
			txtUrl.setText("/" + txtName.getText());
		}
		if (chkDisplayName.isSelected()) {
			txtDisplayName.setText("");
		}
		if (chkTopUrl.isSelected()) {
			txtTopUrl.setText("");
		}
		if (chkUseLanguages.isSelected()) {
			txtUseLanguages.setText(defaultEnableLanguages);
		}
	}

	protected boolean inputValidate() {
		StringBuilder messages = new StringBuilder();
		if (txtName.getText().trim().isEmpty()) {
			messages.append(rs("Common.requiredMsg", "name") + "\n");
		}
		if (txtAdminUserId.getText().trim().isEmpty()) {
			messages.append(rs("Common.requiredMsg", "AdminUserId") + "\n");
		}
		if (txtAdminUserPass.getPassword().length == 0) {
			messages.append(rs("Common.requiredMsg", "AdminUserPassword") + "\n");
		}
		if (txtConfirmAdminUserPass.getPassword().length == 0) {
			messages.append(rs("TenantManagerApp.TenantCreateDialog.unmatchAdminPWMsg") + "\n");
		} else {
			String checkPW = new String(txtAdminUserPass.getPassword());
			String confirmPW = new String(txtConfirmAdminUserPass.getPassword());
			if (!checkPW.equals(confirmPW)) {
				messages.append(rs("TenantManagerApp.TenantCreateDialog.unmatchAdminPWMsg") + "\n");
			}
		}

		if (txtUrl.getText().trim().isEmpty()) {
			messages.append(rs("Common.requiredMsg", "url") + "\n");
		}

		if (messages.length() > 0) {
			JOptionPane.showMessageDialog(this, messages.toString(),
					"WARN", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	protected boolean dataValidate() {

		String tenantUrl = txtUrl.getText().trim();
		if (toolService.existsURL(tenantUrl)) {
			JOptionPane.showMessageDialog(this, rs("TenantManagerApp.TenantCreateDialog.existsTenantMsg"),
					"WARN", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}

	protected void createTenant() {

		btnCreate.setText("create...");
		btnCreate.setEnabled(false);
		btnCancel.setEnabled(false);
		txtMessageArea.setText("");

		CreateTenantWorker worker = new CreateTenantWorker();
		worker.execute();
	}

	private class CreateTenantWorker extends SwingWorker<TenantBatch, String> {

		/**
		 * 非同期処理
		 */
		@Override
		protected TenantBatch doInBackground() throws Exception {
			TenantBatch manager = null;
			try {
				manager = new TenantBatch(TenantBatchExecMode.CREATE.name());
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

				manager.executeCreate(createParameter());

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return manager;
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
				TenantBatch manager = get();

				if (manager.isSuccess()) {
					JOptionPane.showMessageDialog(TenantCreateDialog.this,
							rs("TenantManagerApp.TenantCreateDialog.createCompleteMsg"),
							"INFO", JOptionPane.INFORMATION_MESSAGE);

					fireTenantDataChanged();
				} else {
					JOptionPane.showMessageDialog(TenantCreateDialog.this, rs("Common.errorMsg", ""),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				addLog(rs("Common.errorMsg", ""));
				addLog(e.getMessage());

				JOptionPane.showMessageDialog(TenantCreateDialog.this, rs("Common.errorMsg", ""),
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			btnCreate.setText("Create");
			btnCreate.setEnabled(true);
			btnCancel.setEnabled(true);
		}
	}

	private TenantCreateParameter createParameter() {
		String name = txtName.getText().trim();
		String adminId = txtAdminUserId.getText().trim();
		String adminPW = new String(txtAdminUserPass.getPassword());

		TenantCreateParameter param = new TenantCreateParameter(name, adminId, adminPW);
		if (StringUtil.isNotEmpty(txtUrl.getText().trim())) {
			param.setTenantUrl(txtUrl.getText().trim());
		}
		if (StringUtil.isNotEmpty(txtDisplayName.getText().trim())) {
			param.setTenantDisplayName(txtDisplayName.getText().trim());
		}
		if (StringUtil.isNotEmpty(txtTopUrl.getText().trim())) {
			param.setTopUrl(txtTopUrl.getText().trim());
		}
		if (StringUtil.isNotEmpty(txtUseLanguages.getText().trim())) {
			param.setUseLanguages(txtUseLanguages.getText().trim());
		}
		param.setCreateBlankTenant(chkBlankTenant.isSelected());
		if (chkMySQLSubPartition != null) {
			param.setMySqlUseSubPartition(chkMySQLSubPartition.isSelected());
		}

		return param;
	}

	protected void addLog(String log) {
		txtMessageArea.append(log + "\n");
	}

	protected void fireTenantDataChanged() {
		ChangeEvent e = new ChangeEvent(txtName.getText().trim());
		for (ChangeListener listner : dataChangeListners) {
			listner.stateChanged(e);
		}
	}

}
