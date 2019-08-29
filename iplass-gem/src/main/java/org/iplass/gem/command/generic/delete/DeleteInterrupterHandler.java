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

package org.iplass.gem.command.generic.delete;

import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.view.generic.BulkOperationContext;
import org.iplass.mtp.view.generic.BulkOperationInterrupter;
import org.iplass.mtp.view.generic.BulkOperationInterrupter.BulkOperationType;
import org.iplass.mtp.view.generic.FormView;

public class DeleteInterrupterHandler {

	/** リクエスト */
	private RequestContext request;
	/** 一括操作処理クラス */
	private BulkOperationInterrupter interrupter;
	/** 検索画面Context */
	private DeleteCommandContext context;


	public DeleteInterrupterHandler(RequestContext request, DeleteCommandContext context, BulkOperationInterrupter interrupter) {
		this.request = request;
		this.context = context;
		this.interrupter = interrupter;
	}

	/**
	 * 一括更新する前の処理を行います。
	 * @param entities 操作対象エンティティリスト
	 * @return BulkOperationContext
	 */
	public BulkOperationContext beforeOperation(List<Entity> entities) {
		FormView view = context.getView();
		BulkOperationContext ret = interrupter.beforeOperation(entities, request, context.getEntityDefinition(), view, BulkOperationType.DELETE);
		return ret;
	}


	/**
	 * 一括更新した後の処理を行います。
	 * @param entities 操作対象エンティティリスト
	 * @return 入力エラーリスト
	 */
	public void afterOperation(List<Entity> entities) {
		FormView view = context.getView();
		interrupter.afterOperation(entities, request, context.getEntityDefinition(), view, BulkOperationType.DELETE);
	}
}
