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

package org.iplass.mtp.command.async;

/**
 * 
 * 非同期Command呼び出しの際、AsyncRequestContextで取得可能なattributeの名前の定数。
 * 
 * @author K.Higuchi
 *
 */
public interface AsyncRequestConstants {
	/** {@link AsyncRequestContext#getParam(String)}で取得される値をattributeに格納する際のキー。値はMap形式で格納することを想定。 */
	public static final String PARAM = "param";
	/** attributeからtaskIdを取得する際のキー */
	public static final String TASK_ID = "taskId";
	/** attributeからqueue名を取得する際のキー */
	public static final String QUEUE = "queue";
}
