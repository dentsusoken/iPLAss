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

import java.util.List;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.DetailSearchCommand;
import org.iplass.gem.command.generic.search.FixedSearchCommand;
import org.iplass.gem.command.generic.search.NormalSearchCommand;
import org.iplass.gem.command.generic.search.SearchCommandBase;
import org.iplass.gem.command.generic.search.SearchContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.section.SearchResultSection.DeleteAllCommandTransactionType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity全削除コマンド
 * @author lis3wg
 */
@WebApi(
	name=DeleteAllCommand.WEBAPI_NAME,
	displayName="全削除",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={Constants.MESSAGE},
	tokenCheck=@WebApiTokenCheck(consume=false, useFixedToken=true),
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/delete/DeleteAllCommand", displayName="全削除")
public final class DeleteAllCommand extends DeleteCommandBase {

	private static Logger logger = LoggerFactory.getLogger(DeleteAllCommand.class);

	public static final String WEBAPI_NAME = "gem/generic/delete/deleteAll";

	@Override
	public String execute(final RequestContext request) {

		String searchType = request.getParam(Constants.SEARCH_TYPE);
		SearchCommandBase command = null;
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = new NormalSearchCommand();
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = new DetailSearchCommand();
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = new FixedSearchCommand();
		}

		if (command != null) {
			SearchContext context = command.getContext(request);

			// トランザクションタイプを取得します
			EntityDefinition ed = context.getEntityDefinition();
			EntityView view = evm.get(ed.getName());
			String viewName = request.getParam(Constants.VIEW_NAME);
			SearchFormView form= FormViewUtil.getSearchFormView(ed, view, viewName);
			DeleteAllCommandTransactionType transactionType = form.getResultSection().getDeleteAllCommandTransactionType();

			command.setSearchDelete(request, true);
			String ret = command.execute(request);
			if (!Constants.CMD_EXEC_SUCCESS.equals(ret)) return ret;

			@SuppressWarnings("unchecked")
			SearchResult<Entity> result = (SearchResult<Entity>) request.getAttribute("result");

			boolean isPurge = isPurge(context);
			final DeleteOption option = new DeleteOption(false);
			option.setPurge(isPurge);

			List<Entity> list = result.getList();
			int count = list.size();

			//トランザクションタイプによって一括か、分割かを決める(batchSize件毎)
			int batchSize = ServiceRegistry.getRegistry().getService(GemConfigService.class).getDeleteAllCommandBatchSize();
			if (transactionType == DeleteAllCommandTransactionType.ONCE) {
				batchSize = count;
				}
			
			int countPerBatch = count / batchSize;
			if (count % batchSize > 0) countPerBatch++;
			int current = 0;
			for (int i = 0; i < countPerBatch; i++) {
				current = i * batchSize;
				int last = current + batchSize;
				if (last > list.size()) last = list.size();
				final List<Entity> subList = list.subList(current, last);
				Boolean _ret = Transaction.requiresNew(t -> {
					for (Entity entity : subList) {
						try {
							em.delete(entity, option);
						} catch (ApplicationException e) {
							if (logger.isDebugEnabled()) {
								logger.debug(e.getMessage(), e);
							}
							request.setAttribute(Constants.MESSAGE, e.getMessage());
							t.rollback();
							return false;
						}
					}
					return true;
				});
				if (!_ret) {
					break;
				}
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	private boolean isPurge(SearchContext context) {
		boolean isPurge = false;
		EntityView entityView = context.getEntityView();
		String viewName = context.getRequest().getParam(Constants.VIEW_NAME);
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

}
