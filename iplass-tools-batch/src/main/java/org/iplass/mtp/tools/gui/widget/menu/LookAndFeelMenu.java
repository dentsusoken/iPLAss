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

package org.iplass.mtp.tools.gui.widget.menu;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookAndFeelMenu extends JMenu {

	private static final long serialVersionUID = 2636738214546769106L;
	private static Logger logger = LoggerFactory.getLogger(LookAndFeelMenu.class);

	public LookAndFeelMenu(final Frame owner) {
		super("LookAndFeel");

		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		if (infos != null && infos.length > 0) {
			ButtonGroup group = new ButtonGroup();
			for (final LookAndFeelInfo info : infos) {
				JMenuItem item = new JRadioButtonMenuItem(info.getName());
				item.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						logger.debug("set LookAndFeel :" + info.getName());
						try {
							UIManager.setLookAndFeel(info.getClassName());
							for(Window window: Frame.getWindows()) {
							    SwingUtilities.updateComponentTreeUI(window);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				group.add(item);

				add(item);
				if (UIManager.getSystemLookAndFeelClassName().equals(info.getClassName())) {
					item.setSelected(true);	//イベント発生せず
				}
			}
		}
	}

}
