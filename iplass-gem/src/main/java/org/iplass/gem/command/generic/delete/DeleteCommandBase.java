/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.gem.command.generic.ResultType;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 削除用コマンドのスーパークラス
 * <pre>
 * 本クラスはマルチテナント基盤用の基底コマンドです。
 * 予告なくインターフェースが変わる可能性があるため、
 * 継承は出来る限り行わないでください。
 * </pre>
 * @author lis3wg
 */
public abstract class DeleteCommandBase implements Command {

	private static Logger logger = LoggerFactory.getLogger(DeleteCommandBase.class);

	/** EntityManager */
	protected EntityManager em = null;
	protected EntityViewManager evm = null;
	protected EntityDefinitionManager edm = null;

	/**
	 * コンストラクタ
	 */
	public DeleteCommandBase() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	public DeleteCommandContext getContext(RequestContext request) {
		DeleteCommandContext context = new DeleteCommandContext(request);
		context.setEntityDefinition(edm.get(context.getDefinitionName()));
		context.setEntityView(evm.get(context.getDefinitionName()));

		return context;
	}

	/**
	 * Entityをロードします。
	 * @param defName Entity定義名
	 * @param oid OID
	 * @return Entity
	 */
	protected Entity loadEntity(String defName, String oid) {
		return loadEntity(defName, oid, null);
	}

	/**
	 * Entityをロードします。
	 * @param defName Entity定義名
	 * @param oid OID
	 * @param version version
	 * @return Entity
	 */
	protected Entity loadEntity(String defName, String oid, Long version) {
		if (oid != null) {
			//データ取得
			Entity e = em.load(oid, version, defName, new LoadOption(false, false));
			if (e != null) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Entityを削除します。
	 * @param entity 削除するEntity
	 * @param isPurge 物理削除するか
	 */
	protected DeleteResult deleteEntity(Entity entity, boolean isPurge, DeleteTargetVersion targetVersion) {
		DeleteResult ret = new DeleteResult();
		try {
			DeleteOption option = new DeleteOption(false, targetVersion);
			if (targetVersion == DeleteTargetVersion.ALL) {
				option.setPurge(isPurge);
			}
			em.delete(entity, option);
			ret.setResultType(ResultType.SUCCESS);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			ret.setResultType(ResultType.ERROR);
			ret.setMessage(e.getMessage());
		}
		return ret;
	}

	/**
	 * 削除結果
	 * @author lis3wg
	 */
	public class DeleteResult {

		/** 結果 */
		private ResultType resultType;

		/** メッセージ */
		private String message;

		/** コンストラクタ */
		public DeleteResult() {}

		/**
		 * 結果を取得します。
		 * @return 結果
		 */
		public ResultType getResultType() {
			return resultType;
		}

		/**
		 * 結果を設定します。
		 * @param resultType 結果
		 */
		public void setResultType(ResultType resultType) {
			this.resultType = resultType;
		}

		/**
		 * メッセージを取得します。
		 * @return メッセージ
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * メッセージを設定します。
		 * @param message メッセージ
		 */
		public void setMessage(String message) {
			this.message = message;
		}

	}
}
