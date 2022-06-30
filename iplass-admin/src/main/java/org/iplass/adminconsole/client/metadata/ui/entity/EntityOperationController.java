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
package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entity定義作成時の処理を制御するController
 *
 * @author Y.Yasuda
 */
public interface EntityOperationController {

	/**
	 * Entity定義を作成します。
	 * @param definition Entity定義
	 * @param callback コールバック
	 */
	public void createEntityDefinition(EntityDefinition definition, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * Entity定義をコピーします。
	 * @param sourceName 元の定義名
	 * @param newName 新しい定義名
	 * @param displayName 表示名
	 * @param description 概要
	 * @param isCopyEntityView 画面定義をコピーするか
	 * @param isCopyEntityFilter フィルター定義をコピーするか
	 * @param isCopyEntityWebAPI EntityWebAPIをコピーするか
	 * @param callback コールバック
	 */
	public void copyEntityDefinition(String sourceName, String newName, String displayName, String description,
			boolean isCopyEntityView, boolean isCopyEntityFilter, boolean isCopyEntityWebAPI,
			AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * Entity定義を削除します。
	 * @param defName 定義名
	 * @param callback コールバック
	 */
	public void deleteEntityDefinition(String defName, AsyncCallback<AdminDefinitionModifyResult> callback);

	/**
	 * Entity定義名を変更します。
	 * @param className 定義のクラス名
	 * @param fromName 変更前の定義名
	 * @param toName 変更後の定義名
	 * @param callback コールバック
	 */
	public void renameDefinition(String className, String fromName, String toName, AsyncCallback<AdminDefinitionModifyResult> callback);
}
