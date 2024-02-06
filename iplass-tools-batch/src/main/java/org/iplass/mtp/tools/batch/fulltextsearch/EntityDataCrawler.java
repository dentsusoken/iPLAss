/*
 * Copyright 2012 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.fulltextsearch;

import java.util.Date;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchManager;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpSilentBatch;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityDataをクロールするバッチ。
 *
 */
public class EntityDataCrawler extends MtpSilentBatch {

	/** 実行モード */
	enum Mode {
		RECRAWL
		,CRAWL
	};

	private static final Logger logger = LoggerFactory.getLogger(EntityDataCrawler.class);

	private TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private FulltextSearchManager fsm = ManagerLocator.getInstance().getManager(FulltextSearchManager.class);

	private Mode mode;
	private int tenantId;
	private String entityName;

	/**
	 * <p>引数について</p>
	 * <ol>
	 * <li>モード：CRAWL(Crawl)、RECRAWL(一度インデックスを削除後、Crawl)</li>
	 * <li>テナントID：対象テナントID（-1の場合、全テナントが対象になります）</li>
	 * <li>Entity名：対象Entity名（未指定の場合、すべてのEntityが対象になります）。CRAWLモードの場合指定可能。</li>
	 * </ol>
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Date sysDate = new java.util.Date();
		logger.debug("### Started Crawling " + "[" + sysDate + "] ###");

		if (args != null && args.length > 0) {
			Mode mode = null;
			if (Mode.CRAWL.name().equalsIgnoreCase(args[0])) {
				mode = Mode.CRAWL;
			} else if (Mode.RECRAWL.name().equalsIgnoreCase(args[0])) {
				mode = Mode.RECRAWL;
			}
			if (mode != null) {
				int tenantId = -1;
				String ename = null;
				if (args.length > 1) {
					tenantId = Integer.parseInt(args[1]);
				}
				if (args.length > 2) {
					if (args[2] != null && !args[2].trim().isEmpty()) {
						ename = args[2];
					}
				}

				try {
					if (tenantId >= 0) {
						(new EntityDataCrawler(mode, tenantId, ename)).execute();
					} else {
						List<TenantInfo> tenants = getValidTenantInfoList();
						if (tenants != null) {
							for (TenantInfo t: tenants) {
								(new EntityDataCrawler(mode, t.getId(), ename)).execute();
							}
						}
					}
				} finally {
					// リソース破棄
					MtpBatchResourceDisposer.disposeResource();
				}
			}
		}

		sysDate = new java.util.Date();
		logger.debug("### Finished Crawling " + "[" + sysDate + "] ###");
	}

	/**
	 * コンストラクタ
	 *
	 * @param mode 実行モード
	 * @param tenantId 対象のテナントID
	 * @param entityName Entity名
	 */
	public EntityDataCrawler(Mode mode, int tenantId, String entityName) {
		this.mode = mode;
		this.tenantId = tenantId;
		this.entityName = entityName;
	}

	public boolean execute() throws Exception {

		setSuccess(false);

		clearLog();

		return executeTask(null, (param) -> {

			ExecuteContext.executeAs(tenantContextService.getTenantContext(tenantId), new Executable<Void>() {

				@Override
				public Void execute() {
					logArguments();

					Transaction.required(t -> {
						switch (mode) {
						case CRAWL:
							crawl();
							break;
						case RECRAWL:
							recrawl();
							break;
						default:
							break;
						}
					});

					return null;
				}
			});

			setSuccess(true);

			return isSuccess();
		});
	}

	/**
	 * パラメータ値ログ出力
	 *
	 */
	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + tenantId);
		logInfo("\tmode :" + mode.name());
		logInfo("\tentity name :" + entityName);
		logInfo("");
	}

	private void crawl() {
		if (entityName != null) {
			logger.debug("### CRAWL Target TenantId :" + tenantId + " EntityName :" + entityName + " ###");
			fsm.crawlEntity(entityName);
		} else {
			logger.debug("### CRAWL Target TenantId :" + tenantId + " ###");
			fsm.crawlAllEntity();
		}
	}

	private void recrawl() {
		if (entityName != null) {
			throw new IllegalArgumentException("EntityName can only be specified in CRAWL mode.");
		}

		logger.debug("### RECRAWL Target TenantId :" + tenantId + " ###");
		fsm.recrawlAllEntity();
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
