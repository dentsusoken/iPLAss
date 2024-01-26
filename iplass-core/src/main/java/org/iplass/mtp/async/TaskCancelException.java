/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.async;

import org.iplass.mtp.SystemException;

/**
 * 非同期タスクを実行中に明示的にタスクをキャンセルされた場合に、スローされる例外。
 * 
 * @author K.Higuchi
 *
 */
public class TaskCancelException extends SystemException {
	private static final long serialVersionUID = -155460682642579152L;

	public TaskCancelException() {
		super();
	}

	public TaskCancelException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskCancelException(String message) {
		super(message);
	}

	public TaskCancelException(Throwable cause) {
		super(cause);
	}

}
