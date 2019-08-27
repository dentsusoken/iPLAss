package org.iplass.gem.command.generic.bulk;

import java.util.List;

import org.iplass.gem.command.generic.detail.RegistrationCommandContext;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.view.generic.BulkOperationContext;
import org.iplass.mtp.view.generic.BulkOperationInterrupter;
import org.iplass.mtp.view.generic.BulkOperationInterrupter.BulkOperationType;
import org.iplass.mtp.view.generic.FormView;

public class BulkUpdateInterrupterHandler {

	/** リクエスト */
	private RequestContext request;
	/** 一括操作処理クラス */
	private BulkOperationInterrupter interrupter;
	/** 詳細画面Context */
	private RegistrationCommandContext context;


	public BulkUpdateInterrupterHandler(RequestContext request, RegistrationCommandContext context, BulkOperationInterrupter interrupter) {
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
		BulkOperationContext ret = interrupter.beforeOperation(entities, request, context.getEntityDefinition(), view, BulkOperationType.UPDATE);
		return ret;
	}


	/**
	 * 一括更新した後の処理を行います。
	 * @param entities 操作対象エンティティリスト
	 * @return 入力エラーリスト
	 */
	public void afterOperation(List<Entity> entities) {
		FormView view = context.getView();
		interrupter.afterOperation(entities, request, context.getEntityDefinition(), view, BulkOperationType.UPDATE);
	}
}
