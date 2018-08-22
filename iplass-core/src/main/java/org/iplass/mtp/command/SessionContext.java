/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.command;

import java.util.Iterator;

import org.iplass.mtp.entity.BinaryReference;

/**
 * Sessionオブジェクト。
 *
 * @author K.Higuchi
 *
 */
public interface SessionContext {

	//TODO サイズ制限を設ける

	public Object getAttribute(String name);
	public void setAttribute(String name, Object value);
	public void removeAttribute(String name);
	public Iterator<String> getAttributeNames();

	/**
	 * 当該リクエストをしたユーザが事前に同一セッションにてアップしたテンポラリLOB（まだEntityとして保存していないBinaryReference）を取得します。
	 *
	 * @param lobId テンポラリLOBとして保存時に発行されたlobId
	 * @return
	 */
	public BinaryReference loadFromTemporary(long lobId);

}
