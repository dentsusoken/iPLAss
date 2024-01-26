/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.entity.Entity;

/**
 * 参照型の登録処理を行うためのハンドラー
 *
 * @author lis3wg
 */
public interface ReferenceRegistHandler {

	/**
	 * 通常参照の登録処理
	 * @param function
	 * @param inputEntity
	 * @param loadEntity
	 */
	public default void regist(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadEntity){};

	/**
	 * 被参照の登録処理
	 * @param function
	 * @param inputEntity
	 * @param loadEntity
	 */
	public default void registMappedby(ReferenceRegistHandlerFunction function, Entity inputEntity, Entity loadEntity){};

	/**
	 * 強制更新の設定
	 * @param forceUpdate
	 */
	public void setForceUpdate(boolean forceUpdate);
}
