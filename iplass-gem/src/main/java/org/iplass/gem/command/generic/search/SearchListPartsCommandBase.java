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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.SearchQueryInterrupter.SearchQueryType;

/**
 * 検索結果表示パーツ用コマンド基底クラス
 *
 */
public abstract class SearchListPartsCommandBase implements Command {

	private EntityManager em;

	public SearchListPartsCommandBase() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	protected Query toQuery(SearchContext context) {

		Query query = new Query();
		query.setSelect(context.getSelect());
		query.setFrom(context.getFrom());
		query.setWhere(context.getWhere());
		query.setVersiond(context.isVersioned());

		return query;
	}

	protected int count(SearchContext context, Query query) {
		final SearchContextBase _context = (SearchContextBase) context;

		//検索前処理
		final SearchQueryContext sqContext = _context.beforeSearch(query, SearchQueryType.SEACH);

		List<String> referenceNameKeyList = ((SearchContextBase) context).getForm().getWithoutConditionReferenceNameKey();

		if (sqContext.isDoPrivileged()) {
			//特権実行
			return AuthContext.doPrivileged(() -> em.count(sqContext.getQuery()));
		} else {
			if (sqContext.getWithoutConditionReferenceName() != null) {
				return EntityPermission.doQueryAs(sqContext.getWithoutConditionReferenceName(), () -> em.count(sqContext.getQuery()));
				// 限定条件の除外設定がある場合に設定
			} else if (referenceNameKeyList != null && !referenceNameKeyList.isEmpty()) {
				return EntityPermission.doQueryAs(referenceNameKeyList.toArray(new String[referenceNameKeyList.size()]),
						() -> em.count(sqContext.getQuery()));
			} else {
				return em.count(sqContext.getQuery());
			}
		}
	}

	protected SearchResult<Entity> search(SearchContext context, Query query) {
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
						referenceNameKeyList.toArray(new String[referenceNameKeyList.size()]), () -> searchEntity(_context, userOidList, sqContext.getQuery()));
			} else {
				result = searchEntity(_context, userOidList, sqContext.getQuery());
			}
		}

		if (!userOidList.isEmpty()) {
			if (_context.getForm().isShowUserNameWithPrivilegedValue()) {
				AuthContext.doPrivileged(() -> {
					setUserInfoMap(context, userOidList);
				});
			} else {
				setUserInfoMap(context, userOidList);
			}
		}
		return result;
	}

	protected SearchResult<Entity> searchEntity(final SearchContextBase context, final List<String> userOidList, Query query) {
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

	protected void setUserInfoMap(SearchContext context, final List<String> userOidList) {
		//UserEntityを検索してリクエストに格納
		final Map<String, Entity> userMap = new HashMap<>();

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

		context.getRequest().setAttribute(Constants.USER_INFO_MAP, userMap);
	}

}
