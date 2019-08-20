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
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

/**
 * Entity一括削除コマンド
 * @author lis3wg
 */
@WebApi(
	name=DeleteListCommand.WEBAPI_NAME,
	displayName="一括削除",
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={Constants.MESSAGE},
	tokenCheck=@WebApiTokenCheck(consume=false, useFixedToken=true),
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/delete/DeleteListCommand", displayName="一括削除")
public final class DeleteListCommand extends DeleteCommandBase {

	public static final String WEBAPI_NAME = "gem/generic/delete/deleteList";

	@Override
	public String execute(RequestContext request) {

		String name = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String[] oid = null;
		Object val = request.getParamMap().get(Constants.OID);
		if (val instanceof String) {
			oid = new String[]{(String)val};
		} else if (val instanceof ArrayList<?>) {
			ArrayList<?> list = (ArrayList<?>) val;
			oid = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				oid[i] = list.get(i).toString();
			}
		}
		boolean isPurge = isPurge(name, viewName);

		DeleteCommandContext context = getContext(request);
		//行番号、oidを保持するマップ
		Map<String, Integer> oidMap = splitOid(oid);
		List<Entity> list = getEntities(context.getDefinitionName(), oidMap.keySet());

		//削除前の処理を呼び出します。
		BulkOperationContext bulkContext = context.getDeleteInterrupterHandler().beforeOperation(list);
		List<ValidateError> errors = bulkContext.getErrors();
		List<Entity> entities = bulkContext.getEntities();

		String retKey = Constants.CMD_EXEC_SUCCESS;
		if (!errors.isEmpty()) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.delete.DeleteListCommand.inputErr"));
			retKey = Constants.CMD_EXEC_ERROR;
		} else if (entities.size() > 0) {
			for (Entity entity : entities) {
				String targetOid = entity.getOid();
				Integer targetRow = oidMap.getOrDefault(targetOid, -1);
				entity = loadEntity(name, targetOid);
				if (entity != null) {
					DeleteResult ret = deleteEntity(entity, isPurge);
					if (ret.getResultType() == ResultType.ERROR) {
						//削除でエラーが出てたら終了
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

		//削除前の処理を呼び出します。
		context.getDeleteInterrupterHandler().afterOperation(entities);

		//削除後は一覧画面へ
		return retKey;
	}

	private boolean isPurge(String defName, String viewName) {
		boolean isPurge = false;
		EntityView entityView = evm.get(defName);
		SearchFormView view = null;
		if (viewName == null || viewName.equals("")) {
			//デフォルトレイアウトを利用
			if (entityView != null && entityView.getSearchFormViewNames().length > 0) {
				view = entityView.getDefaultSearchFormView();
			}
		} else {
			//指定レイアウトを利用
			if (entityView != null) view = entityView.getSearchFormView(viewName);
		}
		if (view != null) isPurge = view.isPurge();
		return isPurge;
	}

	private Map<String, Integer> splitOid(String[] oid) {
		Map<String, Integer> oidMap = new HashMap<String, Integer>();
		if (oid != null) {
			for (int i = 0; i < oid.length; i++) {
				//oidには先頭に「行番号_」が付加されているので分離する
				int targetRow = -1;
				String targetOid = oid[i];
				if (targetOid.indexOf("_") != -1) {
					targetRow = Integer.parseInt(oid[i].substring(0, targetOid.indexOf("_")));
					targetOid = oid[i].substring(targetOid.indexOf("_") + 1);
				}
				// 行番号を保存します。 
				// TODO もし多重度が１より大きい場合、oidが同じで、行番号が異なるデータが存在するので、1件目の行番号のみ保持します。
				oidMap.putIfAbsent(targetOid, targetRow);
			}
		}
		return oidMap;
	}

	private List<Entity> getEntities(String defName, Set<String> oidSet) {
		List<Entity> entities = new ArrayList<Entity>();
		for (String oid : oidSet) {
			// OIDのみ設定されるので、エンティティのMappingクラスを見なくてもいいはずです。
			entities.add(new GenericEntity(defName, oid, null));
		}
		return entities;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
