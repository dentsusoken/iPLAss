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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.entity.EntityToolService;
import org.iplass.mtp.impl.tools.entity.EntityUpdateAllCondition;
import org.iplass.mtp.impl.tools.entity.UpdateAllValue;
import org.iplass.mtp.impl.tools.entity.UpdateAllValue.UpdateAllValueType;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity UpdateAll Batch
 */
public class EntityUpdateAll extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(EntityUpdateAll.class);

	/** 実行モード */
	private ExecMode execMode = ExecMode.WIZARD;

	/** テナントID(引数) */
	private Integer tenantId;

	/** Entity名(引数) */
	private String entityName;

	/** 実行テナント */
	private Tenant tenant;

	/** 更新する値 */
	private String updateAllValuesStr;

	/** Where条件 */
	private String whereClause;

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
	 * args[3]・・・update all values str
	 * args[4]・・・where clause
	 **/
	public static void main(String[] args) {

		EntityUpdateAll instance = null;
		try {
			instance = new EntityUpdateAll(args);
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
	 * args[3]・・・update all values str
	 * args[4]・・・where clause
	 **/
	public EntityUpdateAll(String... args) {

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
				updateAllValuesStr = args[3];
			}
			if (args.length > 4 && StringUtil.isNotBlank(args[4])) {
				whereClause = args[4];
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
			logInfo("■Start UpdateAll Wizard");
			logInfo("");

			//Wizardの実行
			return wizard();
		case SILENT :
			logInfo("■Start UpdateAll Silent");
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

	public EntityUpdateAll execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public EntityUpdateAll tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public EntityUpdateAll entity(String entityName) {
		this.entityName = entityName;
		return this;
	}

	/**
	 * UpdateAllします。
	 *
	 * @param param UpdateAll情報
	 * @return 実行結果
	 */
	public boolean updateAll(final EntityUpdateAllParameter param) {

		setSuccess(false);

		boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

			@Override
			public Boolean apply(Transaction t) {
				return executeTask(param, new EntityUpdateAllTask(param));
			}
		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	/**
	 * UpdateAll情報を出力します。
	 */
	public void logArguments(EntityUpdateAllParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());

		EntityUpdateAllCondition condition = param.getEntityUpdateAllCondition();
		logInfo("\tentity name :" + condition.getDefinitionName());
		logInfo("\tupdate values :");
		condition.getValues().stream()
			.forEach(u ->logInfo("\t\t\t" + u.getPropertyName() + "=" + u.getValue()));
		logInfo("\twhere clause :" + condition.getWhere());
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * タスクを実行します。
	 */
	private <T> T executeTask(EntityUpdateAllParameter param, Supplier<T> task) {

		TenantContext tc  = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			return task.get();
		});
	}

	/**
	 * Entityデータを更新します。
	 */
	private class EntityUpdateAllTask implements Supplier<Boolean> {

		private final EntityUpdateAllParameter param;

		public EntityUpdateAllTask(final EntityUpdateAllParameter param) {
			this.param = param;
		}

		@Override
		public Boolean get() {
			return !ets.updateAll(param.getEntityUpdateAllCondition()).isError();
		}
	}
	
	/**
	 * プロパティの一覧を出力します。
	 */
	protected void showValidPropertyList(EntityDefinition ed) {
		try {
			logInfo("-----------------------------------------------------------");
			logInfo("■Property List");
			for (PropertyDefinition propertyDefinition : ed.getPropertyList()) {
				if (isShowRecord(propertyDefinition) && isEnable(propertyDefinition)) {
					logInfo(propertyDefinition.getName());
				}
			}
			logInfo("-----------------------------------------------------------");
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	private boolean isShowRecord(PropertyDefinition prop) {
		//キー項目、audit項目は更新不可のため表示しない
		if (prop.isInherited()) {
			if (prop.getName().equals(Entity.OID) || prop.getName().equals(Entity.VERSION)
					|| prop.getName().equals(Entity.CREATE_BY) || prop.getName().equals(Entity.CREATE_DATE)
					|| prop.getName().equals(Entity.UPDATE_BY) || prop.getName().equals(Entity.UPDATE_DATE)) {
				return false;
			}
		}
		return true;
	}

	private boolean isEnable(PropertyDefinition prop) {
		if (prop.isReadOnly()) {
			return false;
		} else if (prop instanceof BinaryProperty
				|| prop instanceof LongTextProperty
				|| prop instanceof ReferenceProperty
				|| prop instanceof ExpressionProperty) {
			return false;
		}
		return true;
	}

	private List<UpdateAllValue> getUpdateAllValue(EntityDefinition ed, String updateAllValueListStr) {
		if (ed == null) {
			return null;
		}
		
		boolean validUpdateProperty = true; 
		List<UpdateAllValue> updateAllValues = new ArrayList<>();
		for(String updateAllValueStr : updateAllValueListStr.split(",")) {
			int index = updateAllValueStr.indexOf("=");
			if(index == -1) {
				logWarn(rs("EntityUpdateAll.invalidUpdateAllValueMsg", updateAllValueStr));
				validUpdateProperty = false;
				continue;
			}
			
			String propertyName = updateAllValueStr.substring(0, index);
			String value = StringUtils.isEmpty(updateAllValueStr.substring(index + 1)) 
					? null
					: updateAllValueStr.substring(index + 1);
			PropertyDefinition propertyDefinition = ed.getProperty(propertyName);
			
			if(propertyDefinition == null || !isShowRecord(propertyDefinition) || !isEnable(propertyDefinition)) {
				logWarn(rs("EntityUpdateAll.notExistsPropertyNameMsg", propertyName));
				validUpdateProperty = false;
				continue;
			}
			updateAllValues.add(new UpdateAllValue(propertyName, value, UpdateAllValueType.VALUE_EXPRESSION));
		}
		
		if(validUpdateProperty && updateAllValues.size() > 0) {
			return updateAllValues;
		} else {
			return null;
		}
	}

	/**
	 * Wizard形式でUpdateAll用のパラメータを生成して、UpdateAll処理を実行します。
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

		EntityUpdateAllParameter param = new EntityUpdateAllParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//オプションをコンフィグから取得
			EntityUpdateAllCondition condition = new EntityUpdateAllCondition();
			
			//Entity名
			boolean validEntity = false;
			do {
				String inputEntityName = entityName;
				entityName = null;
				if (StringUtil.isEmpty(inputEntityName)) {
					inputEntityName = readConsole(rs("EntityUpdateAll.Wizard.inputEntityNameMsg"));
				}
				if (StringUtil.isNotBlank(inputEntityName)) {
					condition.setDefinitionName(inputEntityName);

					//存在チェック
					ed = edm.get(inputEntityName);					
					if (ed == null) {
						logWarn(rs("EntityUpdateAll.notExistsEntityMsg", inputEntityName));
						continue;
					}
					validEntity = true;
				} else {
					logWarn(rs("EntityUpdateAll.Wizard.requiredEntityNameMsg"));
				}
			} while(validEntity == false);

			//UpdateAllValue
			boolean validUpdateValue = false;
			do {
				String inputUpdateAllValue = updateAllValuesStr;
				if (StringUtil.isEmpty(inputUpdateAllValue)) {
					inputUpdateAllValue = readConsole(rs("EntityUpdateAll.Wizard.inputUpdateAllValuesMsg"));
				}
				if (StringUtil.isNotBlank(inputUpdateAllValue)) {
					if (inputUpdateAllValue.equalsIgnoreCase("-show")) {
						//一覧を出力
						showValidPropertyList(ed);
						continue;
					}
					List<UpdateAllValue> updateAllValues = getUpdateAllValue(ed, inputUpdateAllValue);
					if(updateAllValues == null) {
						continue;
					}
					condition.addValues(updateAllValues);
					validUpdateValue = true;
				} else {
					logWarn(rs("EntityUpdateAll.Wizard.requiredUpdateAllValuesMsg"));
				}
			} while(validUpdateValue == false);
			
			//Where条件
			String whereClause = readConsole(rs("EntityUpdateAll.Wizard.inputWhereClauseMsg"));
			if (StringUtil.isNotBlank(whereClause)) {
				condition.setWhere(whereClause);
			}
			
			//条件をセット
			param.setEntityUpdateAllCondition(condition);
			
			boolean validExecute = false;
			do {
				//実行情報出力
				logArguments(param);

				boolean isExecute = readConsoleBoolean(rs("EntityUpdateAll.Wizard.confirmUpdateMsg"), false);
				if (isExecute) {
					validExecute = true;
				} else {
					//defaultがfalseなので念のため再度確認
					isExecute = readConsoleBoolean(rs("EntityUpdateAll.Wizard.confirmRetryMsg"), true);

					if (isExecute) {
						//再度実行
						return wizard();
					}
				}
			} while(validExecute == false);
			
			return null;
		});

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//UpdateAll処理実行
		return executeTask(param, (paramA) -> {
			return updateAll(paramA);
		});
	}

	/**
	 * バッチ引数またはコンフィグファイル形式でUpdateAll用のパラメータを生成して、UpdateAll処理を実行します。
	 *
	 * @return false:エラー
	 */
	private boolean silent() {

		Tenant tenant = ts.getTenant(tenantId);
		if (tenant == null) {
			logError(rs("Common.requiredMsg", EntityUpdateAllParameter.PROP_TENANT_ID));
			return false;
		}
		logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());

		EntityUpdateAllParameter param = new EntityUpdateAllParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());
			
			//オプションをコンフィグから取得
			EntityUpdateAllCondition condition = new EntityUpdateAllCondition();
			
			//Entity
			ed = edm.get(entityName);
			if (ed == null) {
				logError(rs("Common.requiredMsg", "Entity name"));
				return false;
			}
			condition.setDefinitionName(ed.getName());
			
			//UpdateAllValue
			if (StringUtil.isBlank(updateAllValuesStr)) {
				logWarn(rs("EntityUpdateAll.Wizard.requiredUpdateAllValuesMsg"));
				return false;
			}
			List<UpdateAllValue> updateAllValues = getUpdateAllValue(ed, updateAllValuesStr);
			if(updateAllValues == null) {
				return false;
			}
			condition.addValues(updateAllValues);
			
			//Where条件
			condition.setWhere(whereClause);
			
			param.setEntityUpdateAllCondition(condition);
			
			//実行情報出力
			logArguments(param);

			//UpdateAll処理実行
			return executeTask(param, (paramA) -> {
				return updateAll(paramA);
			});
		});
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
