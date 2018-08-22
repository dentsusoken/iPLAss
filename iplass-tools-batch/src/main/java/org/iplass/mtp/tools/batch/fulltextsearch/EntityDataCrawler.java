/*
 * Copyright 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityDataをクロールするバッチ。
 *
 */
public class EntityDataCrawler extends MtpCuiBase {

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

	/**
	 * <p>引数について</p>
	 * <ol>
	 * <li>モード：CRAWL(Crawl)、RECRAWL(一度インデックスを削除後、Crawl)</li>
	 * <li>テナントID：対象テナントID（-1の場合、全テナントが対象になります）</li>
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
				if (args.length > 1) {
					tenantId = Integer.parseInt(args[1]);
				}

				if (tenantId >= 0) {
					(new EntityDataCrawler(mode, tenantId)).execute();
				} else {
					List<TenantInfo> tenants = getValidTenantInfoList();
					if (tenants != null) {
						for (TenantInfo t: tenants) {
							(new EntityDataCrawler(mode, t.getId())).execute();
						}
					}
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
	 */
	public EntityDataCrawler(Mode mode, int tenantId) {
		this.mode = mode;
		this.tenantId = tenantId;

		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);
	}

	public boolean execute() throws Exception {

		setSuccess(false);

		clearLog();

		try {
			ExecuteContext.executeAs(tenantContextService.getTenantContext(tenantId), new Executable<Void>() {

				@Override
				public Void execute() {

					logArguments();

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

					return null;
				}

			});

			setSuccess(true);

		} catch (Throwable e) {
			logError("An error has occurred. : " + e.getMessage());
			e.printStackTrace();
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
		}

		return isSuccess();
	}

	/**
	 * パラメータ値ログ出力
	 *
	 */
	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + tenantId);
		logInfo("\tmode :" + mode.name());
		logInfo("");
	}

	private void crawl() {
		logger.debug("### CRAWL Target TenantId :" + tenantId + " ###");
		fsm.crawlAllEntity();
	}

	private void recrawl() {
		logger.debug("### RECRAWL Target TenantId :" + tenantId + " ###");
		fsm.recrawlAllEntity();
	}
}
