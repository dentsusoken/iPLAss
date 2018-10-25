/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.dto;

import java.io.Serializable;

public class KeyValue<L, R> implements Serializable {

	private static final long serialVersionUID = -6728591993611690863L;

	private L key;
	private R value;

	public KeyValue() {
	}

	public KeyValue(L key, R value) {
		this.setKey(key);
		this.setValue(value);
	}

	public L getKey() {
		return key;
	}

	public void setKey(L key) {
		this.key = key;
	}

	public R getValue() {
		return value;
	}

	public void setValue(R value) {
		this.value = value;
	}

}
