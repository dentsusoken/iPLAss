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
