/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity;

/**
 * 
 * EQLクエリー実行時などの場面において、
 * cast処理において値の変換に失敗した場合スローされる例外。
 * 
 * @author K.Higuchi
 *
 */
public class EntityValueCastException extends EntityApplicationException {
	private static final long serialVersionUID = 543527823174831645L;

	public EntityValueCastException() {
		super();
	}

	public EntityValueCastException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityValueCastException(String message) {
		super(message);
	}

	public EntityValueCastException(Throwable cause) {
		super(cause);
	}

}
