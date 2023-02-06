/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.mtp.view.top.parts.LastLoginParts;

/**
 *
 * @author lis3wg
 */
public class LastLoginItem extends PartsItem {
	private PartsOperationHandler controler;
	private LastLoginParts parts;

	/**
	 * コンストラクタ
	 */
	public LastLoginItem(LastLoginParts parts, PartsOperationHandler controler) {
		this.parts = parts;
		this.controler = controler;

		setTitle("Last Login");
		setBackgroundColor("#F5F5F5");
	}

	@Override
	public LastLoginParts getParts() {
		return parts;
	}

	@Override
	protected void onOpen() {
		TopViewContentItemSettingDialog dialog = new TopViewContentItemSettingDialog(parts);
		dialog.setTitle("Last Login");
		dialog.show();
	}

	@Override
	protected boolean onPreDestroy() {
		MTPEvent e = new MTPEvent();
		e.setValue("key", dropAreaType + "_" + LastLoginParts.class.getName() + "_");
		controler.remove(e);
		return true;
	}

}
