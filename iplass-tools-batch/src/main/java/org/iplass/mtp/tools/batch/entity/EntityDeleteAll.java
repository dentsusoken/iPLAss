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

package org.iplass.mtp.tools.batch.entity;

import java.util.function.Function;
import java.util.function.Supplier;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.entity.EntityToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity DeleteAll Batch
 */
public class EntityDeleteAll extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(EntityDeleteAll.class);

	/** 実行モード */
	private ExecMode execMode = ExecMode.WIZARD;

	/** テナントID(引数) */
	private Integer tenantId;

	/** Entity名(引数) */
	private String entityName;

	/** 実行テナント */
	private Tenant tenant;
	
	/** Where条件 */
	private String whereClause;

	/** Listnerを実行する */
	private boolean notifyListeners = true;
	
	/** Commit単位(件数) */
	private Integer commitLimit = 100;

	/** 実行Entity */
	private EntityDefinition ed;

	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private EntityToolService ets = ServiceRegistry.getRegistry().getService(EntityToolService.class);
	private EntityDefinitionManager edm = ManagerLocator.manager(EntityDefinitionManager.class);

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・entity name
	 * args[3]・・・where clause
	 * args[4]・・・notifyListeners
	 * args[5]・・・commitLimit
	 **/
	public static void main(String[] args) {

		EntityDeleteAll instance = null;
		try {
			instance = new EntityDeleteAll(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・entity name
	 * args[3]・・・where clause
	 * args[4]・・・notifyListeners
	 * args[5]・・・commitLimit
	 **/
	public EntityDeleteAll(String... args) {

		if (args != null) {
			if (args.length > 0 && StringUtil.isNotBlank(args[0])) {
				execMode = ExecMode.valueOf(args[0].toUpperCase());
			}
			if (args.length > 1 && StringUtil.isNotBlank(args[1])) {
				tenantId = Integer.parseInt(args[1]);
				//-1の場合は、未指定
				if (tenantId == -1) {
					tenantId = null;
				}
			}
			if (args.length > 2 && StringUtil.isNotBlank(args[2])) {
				entityName = args[2];
			}
			if (args.length > 3 && StringUtil.isNotBlank(args[3])) {
				whereClause = args[3];
			}
			if (args.length > 4 && StringUtil.isNotBlank(args[4])) {
				notifyListeners =  Boolean.parseBoolean(args[4]);;
			}
			if (args.length > 5 && StringUtil.isNotBlank(args[5])) {
				commitLimit =  Integer.parseInt(args[5]);;
			}
		}
	}

	/**
	 * モードに合わせて実行します。
	 *
	 * @return
	 */
	public boolean execute() {

		clearLog();

		//Console出力
		switchLog(true, false);

		//環境情報出力
		logEnvironment();
		
		if (tenantId != null) {
			Tenant tenant = ts.getTenant(tenantId);
			if (tenant == null) {
				logError(rs("Common.notExistsTenantIdMsg", tenantId));
				return false;
			}
			this.tenant = tenant;
		}

		switch (execMode) {
		case WIZARD :
			logInfo("■Start DeleteAll Wizard");
			logInfo("");

			//Wizardの実行
			return wizard();
		case SILENT :
			logInfo("■Start DeleteAll Silent");
			logInfo("");

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			//Silentの実行
			return silent();
		default :
			logError("unsupport execute mode : " + execMode);
			return false;
		}
	}

	public EntityDeleteAll execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public EntityDeleteAll tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public EntityDeleteAll entity(String entityName) {
		this.entityName = entityName;
		return this;
	}

	/**
	 * DeleteAllします。
	 *
	 * @param param DeleteAll情報
	 * @return 実行結果
	 */
	public boolean deleteAll(final EntityDeleteAllParameter param) {

		setSuccess(false);

		boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

			@Override
			public Boolean apply(Transaction t) {
				return executeTask(param, new EntityDeleteAllTask(param));
			}
		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	/**
	 * DeleteAll情報を出力します。
	 */
	public void logArguments(EntityDeleteAllParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\tentity name :" + param.getEntityName());
		logInfo("\twhere clause :" + param.getWhereClause());
		logInfo("\tentity execute listner :" + param.isNotifyListeners());
		logInfo("\tentity commit limit :" + param.getCommitLimit());
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * タスクを実行します。
	 */
	private <T> T executeTask(EntityDeleteAllParameter param, Supplier<T> task) {

		TenantContext tc  = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			return task.get();
		});
	}

	/**
	 * Entityデータを削除します。
	 */
	private class EntityDeleteAllTask implements Supplier<Boolean> {

		private final EntityDeleteAllParameter param;

		public EntityDeleteAllTask(final EntityDeleteAllParameter param) {
			this.param = param;
		}

		@Override
		public Boolean get() {
			return !ets.deleteAll(param.getTenantId(), param.getEntityName(), param.getWhereClause(), param.isNotifyListeners(), param.getCommitLimit()).isError();
		}
	}

	/**
	 * Wizard形式でDeleteAll用のパラメータを生成して、DeleteAll処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean wizard() {

		if (tenant == null) {
			//テナントが未確定の場合
			String tenantUrl = readConsole(rs("Common.inputTenantUrlMsg"));

			if (StringUtil.isEmpty(tenantUrl)) {
				logWarn(rs("Common.requiredTenantUrlMsg"));
				return wizard();
			}
			if (tenantUrl.equalsIgnoreCase("-show")) {
				//一覧を出力
				showValidTenantList();
				return wizard();
			}
			if (tenantUrl.equalsIgnoreCase("-env")) {
				//環境情報を出力
				logEnvironment();
				return wizard();
			}

			//URL存在チェック
			String url = tenantUrl.startsWith("/") ? tenantUrl : "/" + tenantUrl;
			tenant = ts.getTenant(url);
			if (tenant == null) {
				logWarn(rs("Common.notExistsTenantMsg", tenantUrl));
				return wizard();
			}
		}

		EntityDeleteAllParameter param = new EntityDeleteAllParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());
			
			//Entity名
			boolean validEntity = false;
			do {
				String inputEntityName = entityName;
				entityName = null;
				if (StringUtil.isEmpty(inputEntityName)) {
					inputEntityName = readConsole(rs("EntityDeleteAll.Wizard.inputEntityNameMsg"));
				}
				if (StringUtil.isNotBlank(inputEntityName)) {
					param.setEntityName(inputEntityName);

					//存在チェック
					ed = edm.get(inputEntityName);					
					if (ed == null) {
						logWarn(rs("EntityDeleteAll.notExistsEntityMsg", inputEntityName));
						continue;
					}
					validEntity = true;
				} else {
					logWarn(rs("EntityDeleteAll.Wizard.requiredEntityNameMsg"));
				}
			} while(validEntity == false);

			//Where句
			String whereClause = readConsole(rs("EntityDeleteAll.Wizard.inputWhereClauseMsg"));
			if (StringUtil.isNotBlank(whereClause)) {
				param.setWhereClause(whereClause);
			}
			
			//notifyListeners
			boolean isNotifyListner = readConsoleBoolean(rs("EntityDeleteAll.Wizard.confirmNotifyListenerMsg"), notifyListeners);
			param.setNotifyListeners(isNotifyListner);
			
			//commitLimit
			int commitLimit = readConsoleInteger(rs("EntityDeleteAll.Wizard.inputCommitUnitMsg"), 100);
			param.setCommitLimit(commitLimit);
			
			return null;
		});
		
		boolean validExecute = false;
		do {
			//実行情報出力
			logArguments(param);

			boolean isExecute = readConsoleBoolean(rs("EntityDeleteAll.Wizard.confirmDeleteMsg"), false);
			if (isExecute) {
				validExecute = true;
			} else {
				//defaultがfalseなので念のため再度確認
				isExecute = readConsoleBoolean(rs("EntityDeleteAll.Wizard.confirmRetryMsg"), true);

				if (isExecute) {
					//再度実行
					return wizard();
				}
			}
		} while(validExecute == false);

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//DeleteAll処理実行
		return executeTask(param, (paramA) -> {
			return deleteAll(paramA);
		});
	}

	/**
	 * バッチ引数またはコンフィグファイル形式でDeleteAll用のパラメータを生成して、DeleteAll処理を実行します。
	 *
	 * @return false:エラー
	 */
	private boolean silent() {
		
		if (tenant == null) {
			logError(rs("Common.requiredMsg", EntityDeleteAllParameter.PROP_TENANT_ID));
			return false;
		}
		logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());

		EntityDeleteAllParameter param = new EntityDeleteAllParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());
			
			//Entity
			ed = edm.get(entityName);
			if (ed == null) {
				logError(rs("Common.requiredMsg", "Entity name"));
				return false;
			}
			param.setEntityName(ed.getName());
			
			//Where句
			param.setWhereClause(whereClause);
			
			//notifyListeners
			param.setNotifyListeners(notifyListeners);
			
			//commitLimit
			param.setCommitLimit(commitLimit);
			
			//実行情報出力
			logArguments(param);

			//DeleteAll処理実行
			return executeTask(param, (paramA) -> {
				return deleteAll(paramA);
			});
		});
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
