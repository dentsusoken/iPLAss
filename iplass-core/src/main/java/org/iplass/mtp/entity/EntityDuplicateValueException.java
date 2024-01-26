/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
 * Entityが一意制約により登録、更新できなかった場合スローされる例外。
 * 一意制約例外は、PKの重複、ユニークインデックスの重複で発生しうる。
 * 
 * @author K.Higuchi
 *
 */
public class EntityDuplicateValueException extends EntityApplicationException {
	private static final long serialVersionUID = 9013578485881836107L;

	public EntityDuplicateValueException() {
		super();
	}

	public EntityDuplicateValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityDuplicateValueException(String message) {
		super(message);
	}

	public EntityDuplicateValueException(Throwable cause) {
		super(cause);
	}

}
