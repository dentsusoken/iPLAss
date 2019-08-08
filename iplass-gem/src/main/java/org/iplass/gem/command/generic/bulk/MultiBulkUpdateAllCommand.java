/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.bulk;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.DetailSearchCommand;
import org.iplass.gem.command.generic.search.FixedSearchCommand;
import org.iplass.gem.command.generic.search.NormalSearchCommand;
import org.iplass.gem.command.generic.search.SearchCommandBase;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.section.SearchResultSection.BulkUpdateAllCommandTransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(name=MultiBulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME,
			displayName="更新",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
			},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_BULK_MULTI_EDIT),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_BULK_MULTI_EDIT),
				@Result(status=Constants.CMD_EXEC_ERROR_SEARCH, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			},
			tokenCheck=@TokenCheck
	)
})
@CommandClass(name = "gem/generic/bulk/MultiBulkUpdateAllCommand", displayName = "一括全更新")
public class MultiBulkUpdateAllCommand extends MultiBulkCommandBase {

	private static Logger logger = LoggerFactory.getLogger(MultiBulkUpdateAllCommand.class);

	public static final String BULK_UPDATE_ALL_ACTION_NAME = "gem/generic/bulk/updateAll";

	@Override
	public String execute(RequestContext request) {
		final MultiBulkCommandContext context = getContext(request);
		String searchType = request.getParam(Constants.SEARCH_TYPE);

		SearchCommandBase command = null;
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = new NormalSearchCommand();
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = new DetailSearchCommand();
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = new FixedSearchCommand();
		}

		String ret = Constants.CMD_EXEC_SUCCESS;
		if (command != null) {
			command.setSearchBulk(request, true);
			ret = command.execute(request);
			if (!Constants.CMD_EXEC_SUCCESS.equals(ret)) return ret;

			// トランザクションタイプを取得します
			EntityDefinition ed = context.getEntityDefinition();
			EntityView view = evm.get(ed.getName());
			String viewName = request.getParam(Constants.VIEW_NAME);
			SearchFormView form= FormViewUtil.getSearchFormView(ed, view, viewName);
			BulkUpdateAllCommandTransactionType transactionType = form.getResultSection().getBulkUpdateAllCommandTransactionType();

			@SuppressWarnings("unchecked")
			SearchResult<Entity> result = (SearchResult<Entity>) request.getAttribute("result");
			List<Entity> entities = result.getList();
			if (entities.size() > 0) {
				// 先頭に「行番号_」を付加する
				List<String> oid = IntStream.range(0, entities.size())
						.mapToObj(i -> i + "_" + entities.get(i).getOid())
						.collect(Collectors.toList());
				List<String> version = IntStream.range(0, entities.size())
						.mapToObj(i -> i + "_" + entities.get(i).getVersion())
						.collect(Collectors.toList());
				List<String> updateDate = IntStream.range(0, entities.size())
						.mapToObj(i -> i + "_" + entities.get(i).getUpdateDate().getTime())
						.collect(Collectors.toList());


				int count = oid.size();

				//トランザクションタイプによって一括か、分割かを決める(batchSize件毎)
				int batchSize = ServiceRegistry.getRegistry().getService(GemConfigService.class).getBulkUpdateAllCommandBatchSize();
				if (transactionType == BulkUpdateAllCommandTransactionType.ONCE) {
					batchSize = count;
					}

				int countPerBatch = count / batchSize;
				if (count % batchSize > 0) countPerBatch++;
				for (int i = 0; i < countPerBatch; i++) {
					int current = i * batchSize;
					List<String> subOidList = oid.stream()
							.skip(current).limit(batchSize).collect(Collectors.toList());
					List<String> subVersionList = version.stream()
							.skip(current).limit(batchSize).collect(Collectors.toList());
					List<String> subUpdateDate = updateDate.stream()
							.skip(current).limit(batchSize).collect(Collectors.toList());
					ret = Transaction.requiresNew(t -> {
						String r = null;
						try {
							// 一括全更新の場合、リクエストスコープに一括更新しようとするエンティティ情報をセットします。
							request.setAttribute(Constants.OID, subOidList.toArray(new String[] {}));
							request.setAttribute(Constants.VERSION, subVersionList.toArray(new String[] {}));
							request.setAttribute(Constants.TIMESTAMP, subUpdateDate.toArray(new String[] {}));
							// 一括更新処理を呼び出します。
							MultiBulkUpdateListCommand updateCommand = new MultiBulkUpdateListCommand();
							r = updateCommand.execute(request);
						} catch (ApplicationException e) {
							if (logger.isDebugEnabled()) {
								logger.debug(e.getMessage(), e);
							}
							request.setAttribute(Constants.MESSAGE, e.getMessage());
							t.rollback();
							return Constants.CMD_EXEC_ERROR;
						} finally {
							request.removeAttribute(Constants.OID);
							request.removeAttribute(Constants.VERSION);
							request.removeAttribute(Constants.TIMESTAMP);
						}
						return r;
					});
					if (!Constants.CMD_EXEC_SUCCESS.equals(ret)) {
						break;
					}
				}
			} else {
				// 一件もない場合、画面表示するために空のフォームビューデータを設定します。
				request.setAttribute(Constants.DATA, new MultiBulkUpdateFormViewData(context));
				request.setAttribute(Constants.ENTITY_DATA, context.createEntity());
				request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
				request.setAttribute(Constants.BULK_UPDATE_SELECT_TYPE, context.getSelectAllType());
				request.setAttribute(Constants.BULK_UPDATE_SELECT_ALL_PAGE, context.getSelectAllPage());
			}
		}

		return ret;
	}
}
