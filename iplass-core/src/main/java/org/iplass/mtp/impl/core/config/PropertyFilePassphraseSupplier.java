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

import java.util.Properties;

public class PropertyFilePassphraseSupplier implements PassphraseSupplier {
	public static final String PASSPHRASE = "passphrase";
	
	private Properties prop;

	@Override
	public char[] getPassphrase() {
		return prop.getProperty(PASSPHRASE).toCharArray();
	}

	@Override
	public void open(Properties prop) {
		this.prop = prop;
	}

	@Override
	public void close() {
		prop = null;
	}

}
