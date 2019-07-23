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

import javax.sql.DataSource;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.rdb.connection.DataSourceConnectionFactory;
import org.iplass.mtp.impl.rdb.connection.DriverManagerConnectionFactory;
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
		private JTextField txtRdbAdapter;
		private JTextField txtConnectionFactory;
		private JTextField txtConnectionUrl;
		private JTextField txtConnectionUser;
		private JTextField txtConnectionDriver;
		private JTextField txtDataSourceClass;

		public ConfigSettingDialog(Frame owner) {
			super(owner);
			setTitle("Config Settings");
			setBounds(64, 64, 300, 590);

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

			int row = 0;
			JLabel lblConfigFileName = new JLabel("Service Config File");
			txtConfigFileName = new JTextField();
			txtConfigFileName.setPreferredSize(new Dimension(300, 25));
			txtConfigFileName.setEditable(false);
			createLableText(lblConfigFileName, txtConfigFileName, null, ++row, gridbag, constraints, headerPane);

			JLabel lblRdbAdapter = new JLabel("Rdb Adapter");
			txtRdbAdapter = new JTextField();
			txtRdbAdapter.setPreferredSize(new Dimension(300, 25));
			txtRdbAdapter.setEditable(false);
			createLableText(lblRdbAdapter, txtRdbAdapter, null, ++row, gridbag, constraints, headerPane);

			JLabel lblConnectionFactory = new JLabel("Connection Factory");
			txtConnectionFactory = new JTextField();
			txtConnectionFactory.setPreferredSize(new Dimension(300, 25));
			txtConnectionFactory.setEditable(false);
			createLableText(lblConnectionFactory, txtConnectionFactory, null, ++row, gridbag, constraints, headerPane);

			JLabel lblConnectionUrl = new JLabel("Connection Url");
			txtConnectionUrl = new JTextField();
			txtConnectionUrl.setPreferredSize(new Dimension(300, 25));
			txtConnectionUrl.setEditable(false);
			createLableText(lblConnectionUrl, txtConnectionUrl, null, ++row, gridbag, constraints, headerPane);

			JLabel lblConnectionUser = new JLabel("Connection User");
			txtConnectionUser = new JTextField();
			txtConnectionUser.setPreferredSize(new Dimension(300, 25));
			txtConnectionUser.setEditable(false);
			createLableText(lblConnectionUser, txtConnectionUser, null, ++row, gridbag, constraints, headerPane);

			JLabel lblConnectionDriver = new JLabel("Connection Driver");
			txtConnectionDriver = new JTextField();
			txtConnectionDriver.setPreferredSize(new Dimension(300, 25));
			txtConnectionDriver.setEditable(false);	//RdbAdapterが変更できないのでこれも
			createLableText(lblConnectionDriver, txtConnectionDriver, null, ++row, gridbag, constraints, headerPane);

			JLabel lblDataSourceClass = new JLabel("DataSource Class");
			txtDataSourceClass = new JTextField();
			txtDataSourceClass.setPreferredSize(new Dimension(300, 25));
			txtDataSourceClass.setEditable(false);	//RdbAdapterが変更できないのでこれも
			createLableText(lblDataSourceClass, txtDataSourceClass, null, ++row, gridbag, constraints, headerPane);

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

			ConfigSetting initConfig = new ConfigSetting();

			//Config FileName
			initConfig.configFileName = BootstrapProps.getInstance().getProperty(BootstrapProps.CONFIG_FILE_NAME, BootstrapProps.DEFAULT_CONFIG_FILE_NAME);
			txtConfigFileName.setText(initConfig.configFileName);

			//Rdb Adapter
			RdbAdapter adapter = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
			initConfig.rdbAdapterName = adapter.getClass().getSimpleName();

			//Connection Factory
			ConnectionFactory factory = ServiceRegistry.getRegistry().getService(ConnectionFactory.class);
			initConfig.connectionFactory = factory.getClass().getSimpleName();
			if (factory instanceof DriverManagerConnectionFactory) {
				DriverManagerConnectionFactory dmFactory = (DriverManagerConnectionFactory) factory;
				initConfig.connectionUrl = getDriverUrl(dmFactory);

				Properties properties = getDriverInfo(dmFactory);
				if (properties.keySet().contains("user")) {
					initConfig.connectionUser = properties.getProperty("user");
				} else {
					initConfig.connectionUser = "not found";
				}
				if (properties.keySet().contains("driver")) {
					initConfig.connectionDriver = properties.getProperty("driver");
				} else {
					initConfig.connectionDriver = "not found";
				}
			} else if (factory instanceof DataSourceConnectionFactory) {
				DataSourceConnectionFactory dsFactory = (DataSourceConnectionFactory) factory;
				initConfig.dataSourceClass = getDataSourceClass(dsFactory);
			}

			setConfigSetting(initConfig);
		}

		private void setConfigSetting(ConfigSetting config) {

			//Rdb Adapter
			txtRdbAdapter.setText(config.rdbAdapterName);

			//Connection Factory
			txtConnectionFactory.setText(config.connectionFactory);
			txtConnectionUrl.setText(config.connectionUrl);
			txtConnectionUser.setText(config.connectionUser);
			txtConnectionDriver.setText(config.connectionDriver);
			txtDataSourceClass.setText(config.dataSourceClass);
		}

		private String getDriverUrl(DriverManagerConnectionFactory dmFactory) throws Exception {
			//private フィールドなのでリフレクションでセット
			Field urlField = dmFactory.getClass().getDeclaredField("url");
			urlField.setAccessible(true);
			return (String)urlField.get(dmFactory);
		}

		private Properties getDriverInfo(DriverManagerConnectionFactory dmFactory) throws Exception {
			//private フィールドなのでリフレクションでセット
			Field infoField = dmFactory.getClass().getDeclaredField("info");
			infoField.setAccessible(true);
			return (Properties)infoField.get(dmFactory);

		}

		private String getDataSourceClass(DataSourceConnectionFactory dsFactory) throws Exception {
			//private フィールドなのでリフレクションでセット
			Field dataSourceField = dsFactory.getClass().getDeclaredField("dataSource");
			dataSourceField.setAccessible(true);
			return ((DataSource)dataSourceField.get(dsFactory)).getClass().getName();
		}

		private static class ConfigSetting {
			String configFileName;
			String rdbAdapterName;
			String connectionFactory;
			String connectionUrl;
			String connectionUser;
			String connectionDriver;
			String dataSourceClass;
		}

	}

}
