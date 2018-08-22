package org.iplass.mtp.impl.tools.clean;

import java.sql.Timestamp;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.tools.entityport.EntityDataPortingRuntimeException;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecycleBinCleanService implements Service {

	private static Logger logger = LoggerFactory.getLogger(RecycleBinCleanService.class);

	private EntityManager em = null;
	private EntityDefinitionManager edm = null;

	@Override
	public void init(Config config) {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	public void destroy() {

	}

	/**
	 * ごみ箱内のデータを削除します。
	 * 指定されたクリーナー対象日より前の削除日を持つごみ箱内のデータを削除します。
	 * クリーナーエンティティが未設定の場合、テナントに定義された全てのエンティティがクリーナー対象となります。
	 * 
	 * @param purgeTargetDate クリーナー対象日
	 * @param entityDefinitionNames クリーナー対象エンティティ
	 */
	public void clean(final Timestamp purgeTargetDate, final String[] entityDefinitionNames) {

		Transaction.required(new Consumer<Transaction>() {

			@Override
			public void accept(Transaction t) {
				if (purgeTargetDate == null) {
					logger.error("Purge target date is null.");
					throw new EntityDataPortingRuntimeException("Purge target date can not be null.");
				}
				String[] definitionNames = entityDefinitionNames;
				if (definitionNames == null) {
					definitionNames = edm.definitionList().toArray(new String[] {});
				}

				for (final String definitionName : definitionNames) {

					em.getRecycleBin(definitionName, new Predicate<Entity>() {

						@Override
						public boolean test(Entity dataModel) {
							// getRecycleBinで取得するUpdateDateにはrbDateがセットされている
							if (dataModel.getUpdateDate().before(purgeTargetDate)) {

								if (logger.isDebugEnabled()) {
									logger.debug("purge " + definitionName + " data. purgeTargetDate=" + purgeTargetDate);
								}

								em.purge(dataModel.getRecycleBinId(), definitionName);
							}
							return true;
						}
					});
				}
			}
		});
	}
}
