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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.view.generic.BulkOperationContext;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity選択削除コマンド
 * @author lis3wg
 */
@WebApi(
	name=DeleteListCommand.WEBAPI_NAME,
	displayName="選択削除",
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={Constants.MESSAGE},
	tokenCheck=@WebApiTokenCheck(consume=false, useFixedToken=true),
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/delete/DeleteListCommand", displayName="選択削除")
public final class DeleteListCommand extends DeleteCommandBase {

	private static Logger logger = LoggerFactory.getLogger(DeleteListCommand.class);

	public static final String WEBAPI_NAME = "gem/generic/delete/deleteList";

	@Override
	public String execute(RequestContext request) {

		String name = request.getParam(Constants.DEF_NAME);

		//削除対象の取得
		String[] oidArray = null;
		Object val = request.getParamMap().get(Constants.OID);
		if (val instanceof String) {
			oidArray = new String[]{(String)val};
		} else if (val instanceof ArrayList<?>) {
			ArrayList<?> list = (ArrayList<?>) val;
			oidArray = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				oidArray[i] = list.get(i).toString();
			}
		}

		DeleteCommandContext context = getContext(request);

		SearchFormView view = context.getView();

		boolean isPurge = view.isPurge();

		//行番号とOID_Versionを分離
		Map<String, Integer> rowIdMap = splitRowId(oidArray);

		//Entityを生成
		List<Entity> entities = getEntities(context.getDefinitionName(), rowIdMap.keySet());

		String retKey = Constants.CMD_EXEC_SUCCESS;
		try {
			//削除前の処理を呼び出します。
			BulkOperationContext bulkContext = context.getDeleteInterrupterHandler().beforeOperation(entities);
			List<ValidateError> errors = bulkContext.getErrors();
			entities = bulkContext.getEntities();

			if (!errors.isEmpty()) {
				request.setAttribute(Constants.MESSAGE, resourceString("command.generic.delete.DeleteListCommand.inputErr"));
				retKey = Constants.CMD_EXEC_ERROR;
			} else if (entities.size() > 0) {
				for (Entity paramEntity : entities) {
					Entity entity = loadEntity(name, paramEntity.getOid(), paramEntity.getVersion());
					if (entity != null) {
						DeleteResult ret = deleteEntity(entity, isPurge, context.getSearchDeleteTargetVersion());
						if (ret.getResultType() == ResultType.ERROR) {
							//削除でエラーが出てたら終了

							//行番号の取得
							String key = paramEntity.getOid();
							if (paramEntity.getVersion() != null) {
								key += ("_" + paramEntity.getVersion());
							}
							Integer targetRow = rowIdMap.getOrDefault(key, -1);

							if (targetRow > 0) {
								request.setAttribute(Constants.MESSAGE,
										resourceString("command.generic.delete.DeleteListCommand.deleteListErr", ret.getMessage(), targetRow));
							} else {
								request.setAttribute(Constants.MESSAGE, ret.getMessage());
							}
							ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction().rollback();
							break;
						}
					}
				}
			}

			//削除後の処理を呼び出します。
			context.getDeleteInterrupterHandler().afterOperation(entities);
		} catch (ApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);

				retKey = Constants.CMD_EXEC_ERROR;
				request.setAttribute(Constants.MESSAGE, e.getMessage());
			}
		}

		//削除後は一覧画面へ
		return retKey;
	}

	private Map<String, Integer> splitRowId(String[] oidArray) {
		Map<String, Integer> rowIdMap = new HashMap<>();
		if (oidArray != null) {
			for (int i = 0; i < oidArray.length; i++) {
				//oidには先頭に「行番号_」が付加されているので分離する
				int targetRow = -1;
				String targetOidVersion = oidArray[i];
				if (targetOidVersion.indexOf("_") != -1) {
					targetRow = Integer.parseInt(targetOidVersion.substring(0, targetOidVersion.indexOf("_")));
					targetOidVersion = targetOidVersion.substring(targetOidVersion.indexOf("_") + 1);
				}
				// 行番号を保存します。
				rowIdMap.putIfAbsent(targetOidVersion, targetRow);
			}
		}
		return rowIdMap;
	}

	private List<Entity> getEntities(String defName, Set<String> oidVersionSet) {
		List<Entity> entities = new ArrayList<>();
		for (String oidVersion : oidVersionSet) {
			String targetOid = oidVersion;
			Long targetVersion = null;
			if (oidVersion.indexOf("_") != -1) {
				//_が含まれている場合、最後はversion
				targetOid = oidVersion.substring(0, oidVersion.lastIndexOf("_"));
				targetVersion = Long.parseLong(oidVersion.substring(oidVersion.lastIndexOf("_") + 1));
			}
			Entity entity = new GenericEntity(defName, targetOid, null);
			entity.setVersion(targetVersion);
			entities.add(entity);
		}
		return entities;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
