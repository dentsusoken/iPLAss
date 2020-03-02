/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.awt.EventQueue;

import javax.swing.UIManager;

import org.iplass.mtp.tools.batch.partition.PostgreSQLPartitionBatch;
import org.iplass.mtp.tools.batch.tenant.TenantBatch.TenantBatchExecMode;

public class PostgreSQLPartitionManagerApp {

	/**
	 * Launch the application.
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
					PartitionManagerApp frame = new PartitionManagerApp(new PostgreSQLPartitionBatch(TenantBatchExecMode.GUI.name()));
					frame.setLocationRelativeTo(null);	// 中央表示
					frame.setTitle("PostgreSQL Partition Manager");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
