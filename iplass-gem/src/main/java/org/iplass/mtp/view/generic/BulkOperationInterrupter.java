/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;

public interface BulkOperationInterrupter {

	/**
	 * 一括操作処理の種類
	 */
	public enum BulkOperationType {
		/** 更新 */
		UPDATE,
		/** 削除 */
		DELETE
	}

	/**
	 * 一括操作前の処理を行います。
	 * @param entities 操作対象リスト
	 * @param request リクエスト
	 * @param definition Entity定義
	 * @param view 画面定義
	 * @param bulkOperationType 一括操作処理の種類
	 * @return BulkOperationContext
	 */
	public default BulkOperationContext beforeOperation(List<Entity> entities, RequestContext request,
			EntityDefinition definition, FormView view, BulkOperationType bulkOperationType) {
		return new BulkOperationContext(entities);
	}


	/**
	 * 一括操作後の処理を行います。
	 * @param entities 操作対象リスト
	 * @param request リクエスト
	 * @param definition Entity定義
	 * @param view 画面定義
	 * @param bulkOperationType 一括操作処理の種類
	 */
	public default void afterOperation(List<Entity> entities, RequestContext request,
			EntityDefinition definition, FormView view, BulkOperationType bulkOperationType) {
	}
}
