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
