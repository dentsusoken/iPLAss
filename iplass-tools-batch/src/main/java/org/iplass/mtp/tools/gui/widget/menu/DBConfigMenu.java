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

package org.iplass.mtp.tools.gui.widget.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.core.config.ServiceRegistryInitializer;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.rdb.connection.DriverManagerConnectionFactory;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.rdb.oracle.OracleRdbAdapter;
import org.iplass.mtp.impl.rdb.postgresql.PostgreSQLRdbAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.SqlServerRdbAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

public class DBConfigMenu extends JMenu {

	private static final long serialVersionUID = 493704852361376167L;

	public DBConfigMenu(final Frame owner) {
		super("Config");

		JMenuItem item = new JMenuItem("DB Config Settings");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigSettingDialog dialog = new ConfigSettingDialog(owner);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		add(item);
	}


	public static class ConfigSettingDialog extends JDialog {

		private static final long serialVersionUID = 8378724754491492214L;

		private JTextField txtConfigFileName;
		private JComboBox<String> cbxRdbAdapter;
		private JTextField txtConenctUrl;
		private JTextField txtConenctUser;
//		private JPasswordField txtConenctUserPass;
//		private JTextField txtConenctDriver;
		private JComboBox<String> cbxConenctDriver;

		private ConfigSetting initConfig;

		private RdbAdapterService adapterService;
		private DriverManagerConnectionFactory dmFactory;

		public ConfigSettingDialog(Frame owner) {
			super(owner);
			setTitle("Config Settings");
			setBounds(64, 64, 300, 530);

			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);//中央表示

			JPanel headerPane = new JPanel();
			headerPane.setBorder(new EmptyBorder(5, 5, 5, 5));

			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints constraints = new GridBagConstraints();
			headerPane.setLayout(gridbag);

			//縦余白(Label、Text間)
			constraints.gridx = 1;
			constraints.gridheight = GridBagConstraints.REMAINDER;
			Box dummy = Box.createHorizontalBox();
			dummy.setPreferredSize(new Dimension(15, 15));
			gridbag.setConstraints(dummy, constraints);
			headerPane.add(dummy);

			//縦余白(Text、Check間)
			constraints.gridx = 3;
			constraints.gridheight = GridBagConstraints.REMAINDER;
			dummy = Box.createHorizontalBox();
			dummy.setPreferredSize(new Dimension(15, 15));
			gridbag.setConstraints(dummy, constraints);
			headerPane.add(dummy);

			//縦余白(Check、後間)
			constraints.gridx = 5;
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.gridheight = GridBagConstraints.REMAINDER;
			dummy = Box.createHorizontalBox();
			dummy.setPreferredSize(new Dimension(15, 15));
			gridbag.setConstraints(dummy, constraints);
			headerPane.add(dummy);

			JLabel lblConfigFileName = new JLabel("Service Config File");
			txtConfigFileName = new JTextField();
			txtConfigFileName.setPreferredSize(new Dimension(300, 25));
			txtConfigFileName.setEditable(false);
			createLableText(lblConfigFileName, txtConfigFileName, null, 0, gridbag, constraints, headerPane);

			JLabel lblRdbAdapter = new JLabel("Rdb Adapter");
			cbxRdbAdapter = new JComboBox<String>(new String[]{"OracleRdbAdapter", "MysqlRdbAdaptor", "SqlServerRdbAdapter"});
			cbxRdbAdapter.setPreferredSize(new Dimension(300, 25));
			cbxRdbAdapter.setEnabled(false); //各Serviceなどのインスタンス変数に保持してしまっているのであきらめ
			createLableText(lblRdbAdapter, cbxRdbAdapter, null, 1, gridbag, constraints, headerPane);

			JLabel lblConenctUrl = new JLabel("Conenct Url");
			txtConenctUrl = new JTextField();
			txtConenctUrl.setPreferredSize(new Dimension(300, 25));
			txtConenctUrl.setEditable(false);
			createLableText(lblConenctUrl, txtConenctUrl, null, 2, gridbag, constraints, headerPane);

			JLabel lblConenctUser = new JLabel("Conenct User");
			txtConenctUser = new JTextField();
			txtConenctUser.setPreferredSize(new Dimension(300, 25));
			txtConenctUser.setEditable(false);
			createLableText(lblConenctUser, txtConenctUser, null, 3, gridbag, constraints, headerPane);

//			JLabel lblConenctUserPass = new JLabel("Conenct User Password");
//			txtConenctUserPass = new JPasswordField();
//			txtConenctUserPass.setPreferredSize(new Dimension(300, 25));
//			txtConenctUserPass.setEditable(false);
//			createLableText(lblConenctUserPass, txtConenctUserPass, null, 4, gridbag, constraints, headerPane);

			JLabel lblConenctDriver = new JLabel("Conenct Driver");
//			txtConenctDriver = new JTextField();
//			txtConenctDriver.setPreferredSize(new Dimension(300, 25));
//			createLableText(lblConenctDriver, txtConenctDriver, null, 5, gridbag, constraints, headerPane);
			cbxConenctDriver = new JComboBox<String>(new String[]{"oracle.jdbc.driver.OracleDriver", "com.mysql.jdbc.Driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"});
			cbxConenctDriver.setPreferredSize(new Dimension(300, 25));
			cbxConenctDriver.setEnabled(false);	//RdbAdapterが変更できないのでこれも
			createLableText(lblConenctDriver, cbxConenctDriver, null, 5, gridbag, constraints, headerPane);

			JPanel mainPane = new JPanel();
			mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			mainPane.setLayout(new BorderLayout());
			mainPane.add(headerPane, BorderLayout.CENTER);
			mainPane.add(createFooterPane(), BorderLayout.SOUTH);


			getContentPane().add(mainPane, BorderLayout.NORTH);

			pack();

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					super.windowActivated(e);
					try {
						searchConfig();
					} catch (Throwable err) {
						JOptionPane.showMessageDialog(ConfigSettingDialog.this, err.toString(),
								"エラー", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
		}

		private void createLableText(JComponent label, JComponent text, JComponent check,
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

		private JPanel createFooterPane() {

			JPanel footerPane = new JPanel();
			footerPane.setLayout(new FlowLayout(FlowLayout.CENTER));

			JButton btnCancel = new JButton("Cancel");
			btnCancel.setDefaultCapable(true);
			btnCancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ConfigSettingDialog.this.setVisible(false);
					ConfigSettingDialog.this.dispose();
				}
			});
			footerPane.add(btnCancel);

			return footerPane;
		}

		private void searchConfig() throws Exception {

			initConfig = new ConfigSetting();

			//Config FileName
			initConfig.configFileName = ServiceRegistryInitializer.getConfigFileName();
			txtConfigFileName.setText(initConfig.configFileName);

			//Rdb Adapter
			RdbAdapter adapter = getRdbAdapter();
			initConfig.rdbAdapterName = getRdbAdapterCbxValue(adapter);

			//Connection Factory
			ConnectionFactory factory = ServiceRegistry.getRegistry().getService(ConnectionFactory.class);
			if (factory instanceof DriverManagerConnectionFactory) {
				dmFactory = (DriverManagerConnectionFactory) factory;
				initConfig.conenctUrl = getUrl();

				Properties properties = getInfo();
				if (properties.keySet().contains("user")) {
					initConfig.conenctUser = properties.getProperty("user");
				} else {
					initConfig.conenctUser = "not found";
				}
//				if (properties.keySet().contains("password")) {
//					initConfig.conenctUserPass = properties.getProperty("password");
//				} else {
//					initConfig.conenctUserPass = "not found";
//				}
				if (properties.keySet().contains("driver")) {
					initConfig.conenctDriver = properties.getProperty("driver");
				} else {
					initConfig.conenctDriver = "not found";
				}
			} else {
				throw new ApplicationException("unsupport ConnectionFactory class : " + factory.getClass().getName());
			}

			setConfigSetting(initConfig);
		}

		private void setConfigSetting(ConfigSetting config) {

			//Rdb Adapter
			cbxRdbAdapter.setSelectedItem(initConfig.rdbAdapterName);

			//Connection Factory
			txtConenctUrl.setText(initConfig.conenctUrl);
			txtConenctUser.setText(initConfig.conenctUser);
//			txtConenctUserPass.setText(initConfig.conenctUserPass);
			cbxConenctDriver.setSelectedItem(initConfig.conenctDriver);
		}

		private RdbAdapter getRdbAdapter() {

			adapterService = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
			return adapterService.getRdbAdapter();
		}

		private String getRdbAdapterCbxValue(RdbAdapter adapter) {
			if (adapter instanceof OracleRdbAdapter) {
				return "OracleRdbAdapter";
			} else if (adapter instanceof MysqlRdbAdaptor) {
				return "MysqlRdbAdaptor";
			} else if (adapter instanceof PostgreSQLRdbAdapter) {
				return "PostgreSQLRdbAdaptor";
			} else if (adapter instanceof SqlServerRdbAdapter) {
				return "SqlServerRdbAdapter";
			} else {
				throw new ApplicationException("unsupport RdbAdapter class : " + adapter.getClass().getName());
			}
		}

		private String getUrl() throws Exception {
			//private フィールドなのでリフレクションでセット
			Field urlField = dmFactory.getClass().getDeclaredField("url");
			urlField.setAccessible(true);
			return (String)urlField.get(dmFactory);
		}

		private Properties getInfo() throws Exception {
			//private フィールドなのでリフレクションでセット
			Field infoField = dmFactory.getClass().getDeclaredField("info");
			infoField.setAccessible(true);
			return (Properties)infoField.get(dmFactory);

		}

		private static class ConfigSetting {
			String configFileName;
			String rdbAdapterName;
			String conenctUrl;
			String conenctUser;
			//String conenctUserPass;
			String conenctDriver;
		}

	}

}
