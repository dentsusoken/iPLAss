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
package org.iplass.mtp.impl.core.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.iplass.mtp.spi.ServiceConfigrationException;

public class ConsolePassphraseSupplier implements PassphraseSupplier {

	@Override
	public char[] getPassphrase() {
		System.out.println("enter passphrase:");
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		try {
			return console.readLine().toCharArray();
		} catch (IOException e) {
			throw new ServiceConfigrationException(e);
		}
	}

}
