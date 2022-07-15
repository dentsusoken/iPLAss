/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.adminconsole.server.metadata.rpc;

import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.mtp.entity.definition.EntityDefinition;

/**
 * Entity定義操作用Controllerのインターフェース
 *
 * @author Y.Yasuda
 */
public interface EntityDefinitionOperationController {

	public static final String DEFAULT = "DEFAULT";

	/**
	 * Entity定義に紐づくメニューを作成
	 * @param definition Entity定義
	 * @return 処理結果
	 */
	public AdminDefinitionModifyResult createMenuItem(EntityDefinition definition);

	/**
	 * Entity定義に紐づくメニューを更新
	 * @param definition Entity定義
	 * @return 処理結果
	 */
	public AdminDefinitionModifyResult updateMenuItem(EntityDefinition definition);

	/**
	 * 画面系の定義をコピー
	 * @param sourceName 変更元のEntity定義名
	 * @param newName 変更後のEntity定義名
	 * @param displayName 表示名
	 * @param description 概要
	 * @param isCopyEntityView 画面定義をコピーするか
	 * @param isCopyEntityFilter フィルタ定義をコピーするか
	 * @param isCopyEntityWebAPI EntityWebAPIをコピーするか
	 * @return 処理結果
	 */
	public AdminDefinitionModifyResult copyViewDefinition(String sourceName, String newName, String displayName, String description,
			boolean isCopyEntityView, boolean isCopyEntityFilter, boolean isCopyEntityWebAPI);

	/**
	 * 画面系の定義を削除
	 * @param name Enitity定義名
	 * @return 処理結果
	 */
	public AdminDefinitionModifyResult deleteViewDefinition(String name);

	/**
	 * 画面系の定義名を変更
	 * @param fromName 変更前のEntity定義名
	 * @param toName 変更後のEntity定義名
	 */
	public void renameViewDefinition(String fromName, String toName);
}
