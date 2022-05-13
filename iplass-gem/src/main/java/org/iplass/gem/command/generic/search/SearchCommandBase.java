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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;

/**
 * 検索用コマンド基底クラス
 * <pre>
 * 本クラスはマルチテナント基盤用の基底コマンドです。
 * 予告なくインターフェースが変わる可能性があるため、
 * 継承は出来る限り行わないでください。
 * </pre>
 * @author lis3wg
 *
 */
public abstract class SearchCommandBase implements Command {

	protected EntityDefinitionManager edm = null;
	protected EntityViewManager evm = null;
	protected EntityManager em = null;


	public SearchCommandBase() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		SearchContext context = getContext(request);

		if (!context.checkParameter()) {
			return Constants.CMD_EXEC_ERROR_PARAMETER;
		}

		if (!context.validation()) {
			//エラーメッセージはContext内で設定
			return Constants.CMD_EXEC_ERROR_VALIDATE;
		}

		//Queryを生成して正しくEQLに変換できるかをチェック
		Query query = null;
		if (isSearchDelete(request)) {
			// 全削除
			query = toQueryForDelete(context);
		} else if (isSearchBulk(request)) {
			// 全一括更新
			query = toQueryForBulkUpdate(context);
		} else {
			query = toQuery(context);
		}

		if (query == null) {
			return Constants.CMD_EXEC_ERROR_SEARCH;
		}

		//検証の場合はここで終了
		if (isValidateCondition(request)) {
			return Constants.CMD_EXEC_SUCCESS;
		}

		if (context.isCount()) {
			count(context, query.copy());
		}

		if (isSearchDelete(request)) {
			search(context, query.copy());
		} else if (isSearchBulk(request)) {
			search(context, query.copy());
		} else if (context.isSearch()) {
			Query q = query.copy();
			// 検索時にEQLにOrderByとLimitを付けます。
			setOrderBy(context, q);
			setLimit(context, q);
			search(context, q);
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	public SearchContext getContext(RequestContext request) {
		SearchContext context = null;
		try {
			context = getContextClass().newInstance();
		} catch (InstantiationException e) {
			throw new EntityRuntimeException(resourceString("command.generic.search.SearchCommandBase.internalErr"), e);
		} catch (IllegalAccessException e) {
			throw new EntityRuntimeException(resourceString("command.generic.search.SearchCommandBase.internalErr"), e);
		}

		if (context != null) {
			context.setRequest(request);
			context.setEntityDefinition(edm.get(context.getDefName()));
			context.setEntityView(evm.get(context.getDefName()));
		}

		return context;
	}

	protected abstract Class<? extends SearchContext> getContextClass();

	protected Query toQuery(SearchContext context) {

		Query query = new Query();
		query.setSelect(context.getSelect());
		query.setFrom(context.getFrom());
		query.setWhere(context.getWhere());
		query.setVersiond(context.isVersioned());

		return query;
	}

	protected Query toQueryForDelete(SearchContext context) {
		Query query = new Query();
		// 全削除する場合、抽出項目はSearchResultSectionの定義を見なくて良いです。
		query.select(Entity.OID, Entity.VERSION);
		query.from(context.getDefName());
		query.setWhere(context.getWhere());
		query.setVersiond(context.isVersioned());

		return query;
	}

	protected Query toQueryForBulkUpdate(SearchContext context) {
		Query query = new Query();
		// 全一括更新する場合、抽出項目はSearchResultSectionの定義を見なくて良いです。
		query.select(Entity.OID, Entity.VERSION, Entity.UPDATE_DATE);
		query.from(context.getDefName());
		query.setWhere(context.getWhere());
		query.setVersiond(context.isVersioned());

		return query;
	}

	protected void setLimit(SearchContext context, Query query) {
		query.setLimit(context.getLimit());
	}

	protected void setOrderBy(SearchContext context, Query query) {
		query.setOrderBy(context.getOrderBy());
	}

	/**
	 * 全削除するモードかを返す
	 */
	public boolean isSearchDelete(RequestContext request) {
		return (request.getAttribute("isSearchDelete") != null
				&& (Boolean) request.getAttribute("isSearchDelete"));
	}

	/**
	 * 全削除するモードかを設定する
	 */
	public void setSearchDelete(RequestContext request, boolean isDelete) {
		request.setAttribute("isSearchDelete", isDelete);
	}

	/**
	 * 全一括更新するモードかを返す
	 */
	public boolean isSearchBulk(RequestContext request) {
		return (request.getAttribute("isSearchBulk") != null
				&& (Boolean) request.getAttribute("isSearchBulk"));
	}

	/**
	 * 全一括更新するモードかを設定する
	 */
	public void setSearchBulk(RequestContext request, boolean isBulk) {
		request.setAttribute("isSearchBulk", isBulk);
	}

	/**
	 * 条件を検証するモードかを返す
	 */
	boolean isValidateCondition(RequestContext request) {
		return (request.getAttribute("isValidateCondition") != null
				&& (Boolean)request.getAttribute("isValidateCondition"));
	}

	/**
	 * 条件を検証するモードかを設定する
	 */
	void setValidateCondition(RequestContext request, boolean check) {
		request.setAttribute("isValidateCondition", check);
	}

	final protected void search(SearchContext context, Query query) {
		final SearchContextBase _context = (SearchContextBase) context;
		final List<String> userOidList = new ArrayList<>();

		//検索前処理
		final SearchQueryContext sqContext = _context.beforeSearch(query, SearchQueryType.SEACH);
		
		List<String> referenceNameKeyList = ((SearchContextBase) context).getForm().getWithoutConditionReferenceNameKey();

		SearchResult<Entity> result = null;
		if (sqContext.isDoPrivileged()) {
			//特権実行
			result = AuthContext.doPrivileged(() -> searchEntity(_context, userOidList, sqContext.getQuery()));
		} else {
			if (sqContext.getWithoutConditionReferenceName() != null) {
				result = EntityPermission.doQueryAs(sqContext.getWithoutConditionReferenceName(), () -> searchEntity(_context, userOidList, sqContext.getQuery()));
				// 限定条件の除外設定がある場合に設定
			} else if (referenceNameKeyList != null && !referenceNameKeyList.isEmpty()) {
				result = EntityPermission.doQueryAs(
						referenceNameKeyList.toArray(new String[referenceNameKeyList.size()]),
						() -> searchEntity(_context, userOidList, sqContext.getQuery()));
			} else {
				result = searchEntity(_context, userOidList, sqContext.getQuery());
			}
		}
		context.getRequest().setAttribute("result", result);

		if (!userOidList.isEmpty()) {
			setUserInfoMap(context, userOidList);
		}
	}

	private SearchResult<Entity> searchEntity(final SearchContextBase context, final List<String> userOidList, Query query) {
		final List<Entity> entityList = new ArrayList<>();
		em.searchEntity(query, new Predicate<Entity>() {

			@Override
			public boolean test(Entity dataModel) {
				context.afterSearch(query, dataModel, SearchQueryType.SEACH);

				if (context.isUseUserPropertyEditor()) {
					for (String propertyName : context.getUseUserPropertyEditorPropertyName()) {
						String oid = dataModel.getValue(propertyName);
						if (oid != null && !userOidList.contains(oid)) {
							userOidList.add(oid);
						}
					}
				}
				entityList.add(dataModel);
				return true;
			}

		});
		return new SearchResult<>(0, entityList);
	}

	private void setUserInfoMap(SearchContext context, final List<String> userOidList) {
		//UserEntityを検索してリクエストに格納
		final Map<String, Entity> userMap = new HashMap<String, Entity>();
		final SearchContextBase searchContextBase = (SearchContextBase) context;

		if (searchContextBase.getForm().isShowUserNameWithPrivilegedValue()) {
			AuthContext.doPrivileged(() -> {
				searchUserMap(userMap, userOidList);
			});
		} else {
			searchUserMap(userMap, userOidList);
		}
		
		context.getRequest().setAttribute(Constants.USER_INFO_MAP, userMap);
	}

	private void searchUserMap(Map<String, Entity> userMap, final List<String> userOidList) {
		Query q = new Query().select(Entity.OID, Entity.NAME)
							 .from(User.DEFINITION_NAME)
							 .where(new In(Entity.OID, userOidList.toArray()));
		
		em.searchEntity(q, new Predicate<Entity>() {

			@Override
			public boolean test(Entity dataModel) {
				if (!userMap.containsKey(dataModel.getOid())) {
					userMap.put(dataModel.getOid(), dataModel);
				}
				return true;
			}
		});
	}
	
	final protected void count(SearchContext context, Query query) {
		final SearchContextBase _context = (SearchContextBase) context;

		//検索前処理
		final SearchQueryContext sqContext = _context.beforeSearch(query, SearchQueryType.SEACH);
		
		List<String> referenceNameKeyList = ((SearchContextBase) context).getForm().getWithoutConditionReferenceNameKey();

		Integer count = null;
		if (sqContext.isDoPrivileged()) {
			//特権実行
			count = AuthContext.doPrivileged(() -> em.count(sqContext.getQuery()));
		} else {
			if (sqContext.getWithoutConditionReferenceName() != null) {
				count = EntityPermission.doQueryAs(sqContext.getWithoutConditionReferenceName(), () -> em.count(sqContext.getQuery()));
				// 限定条件の除外設定がある場合に設定
			} else if (referenceNameKeyList != null && !referenceNameKeyList.isEmpty()) {
				count = EntityPermission.doQueryAs(
						referenceNameKeyList.toArray(new String[referenceNameKeyList.size()]),
						() -> em.count(sqContext.getQuery()));
			} else {
				count = em.count(sqContext.getQuery());
			}
		}

		context.getRequest().setAttribute("count", count);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
