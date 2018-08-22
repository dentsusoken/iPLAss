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

package org.iplass.adminconsole.shared.base.rpc.entity;

import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;

/**
 * EntityDataを送受信する場合に、
 * プロパティごとの値の型をGWTのシリアライズリストに追加するためのIF。
 *
 * Serviceで、このIFを実装することで自動的にホワイトリスト化されます。
 *
 * @see http://stackoverflow.com/questions/138099/how-do-i-add-a-type-to-gwts-serialization-policy-whitelist
 */
public interface EntityDataTransferService {

	/**
	 * Entityデータを送受信する場合に、値の型をGWTのホワイトリストに追加するためのメソッドです。
	 */
	EntityDataTransferTypeList entityDataTypeWhiteList(EntityDataTransferTypeList param);

}
