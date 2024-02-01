/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.tools.batch.partition.PartitionBatch;
import org.iplass.mtp.tools.gui.MtpJFrameBase;
import org.iplass.mtp.tools.gui.partition.PartitionTable.PartitionTableModel;
import org.iplass.mtp.tools.gui.tenant.TenantManagerApp;
import org.iplass.mtp.tools.gui.widget.menu.BasicMenuBar;

public class PartitionManagerApp extends MtpJFrameBase {

	private static final long serialVersionUID = 7274431196176548612L;

	private JButton btnCreate;
	private JButton btnTenantList;
	private JButton btnRefresh;
	private JTextField txtCount;

	private PartitionTableModel model;

	private final PartitionBatch manager;

	public PartitionManagerApp(PartitionBatch manager) {
		this.manager = manager;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().add(new BasicMenuBar(this), BorderLayout.NORTH);

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		mainPane.setPreferredSize(new Dimension(500, 600));	//テーブルの列幅と調整
		getContentPane().add(mainPane, BorderLayout.CENTER);

		mainPane.add(createHeaderPane(), BorderLayout.NORTH);
		mainPane.add(createPartitionTableList(), BorderLayout.CENTER);
		pack();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowActivated(e);
				//起動後に検索
				searchPartitionList();
			}
		});
	}

	private JPanel createHeaderPane() {
		JPanel headerPane = new JPanel();
		headerPane.setLayout(new BorderLayout());

		JPanel headerMainPane = new JPanel();
		headerMainPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		btnCreate = new JButton("Create Partition");
		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PartitionCreateDialog dialog = new PartitionCreateDialog(PartitionManagerApp.this);
				dialog.setModal(true);
				dialog.addPartitionDataChangeListner(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						searchPartitionList();
					}
				});
				dialog.setVisible(true);
			}
		});
		headerMainPane.add(btnCreate);

		btnTenantList = new JButton("Tenant List");
		btnTenantList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TenantManagerApp.main(new String[]{});

				//自身を消す
				dispose();
			}
		});
		headerMainPane.add(btnTenantList);


		JPanel headerSubPane = new JPanel();
		headerSubPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		btnRefresh = new JButton("Refresh List");
		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchPartitionList();
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

	private JScrollPane createPartitionTableList() {
		PartitionTable table = new PartitionTable();
		model = table.getModel();

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(table);

		return scroll;
	}

	private void searchPartitionList() {
		//tableにセット
		model.setPartitionData(Collections.emptyList());
		model.fireTableDataChanged();

		btnRefresh.setText("search...");
		txtCount.setText("");
		btnRefresh.setEnabled(false);
		SearchPartitionWorker worker = new SearchPartitionWorker();
		worker.execute();
	}

	private class SearchPartitionWorker extends SwingWorker<List<PartitionInfo>, Object> {
		/**
		 * 非同期処理
		 */
		@Override
		protected List<PartitionInfo> doInBackground() throws Exception {
			return manager.getPartitionInfo();
		}

		/**
		 * 非同期処理後処理
		 */
		@Override
		protected void done() {
			try {
				List<PartitionInfo> result = get();

				//tableにセット
				model.setPartitionData(result);
				model.fireTableDataChanged();

				//件数をセット
				txtCount.setText(String.valueOf(result.size()));

				//パーティションテーブルが存在する場合は作成ボタンを有効化
				btnCreate.setEnabled(result.size() > 0);
			} catch (ExecutionException e) {
				JOptionPane.showMessageDialog(PartitionManagerApp.this, rs("Common.errorMsg", e.getMessage()),
						"ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException e) {
				JOptionPane.showMessageDialog(PartitionManagerApp.this, rs("Common.errorMsg", e.getMessage()),
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
    		btnRefresh.setText("Refresh List");
    		btnRefresh.setEnabled(true);
		}
	}

}
