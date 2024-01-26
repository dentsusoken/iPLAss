/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.base.screen;

public class ScreenModuleBasedUIFactoryGeneratorImpl implements ScreenModuleBasedUIFactoryGenerator {

	@Override
	public ScreenModuleBasedUIFactory generate(String type) {
		if (type.equals("GEM")) {
			return new GemBasedUIFactory();
		}

		// 現状GEMしか画面モジュールは存在しないので、デフォルトはGEM
		return new GemBasedUIFactory();
	}

}
