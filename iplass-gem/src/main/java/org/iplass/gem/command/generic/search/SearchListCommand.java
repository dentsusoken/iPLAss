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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.CreateSearchResultUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
	name=SearchListCommand.WEBAPI_NAME,
	displayName="Entity一覧検索(パーツ)",
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={SearchListCommand.RESULT_PARAM_COUNT, SearchListCommand.RESULT_PARAM_HTML_DATA},
	checkXRequestedWithHeader=true
)
@Template(name="gem/generic/search/list", displayName="検索結果パーツ", path="/jsp/gem/generic/search/list.jsp")
@CommandClass(name="gem/generic/search/SearchListCommand", displayName="Entity一覧検索(パーツ)")
public final class SearchListCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/search/list";

	public static final String RESULT_PARAM_COUNT = "count";
	public static final String RESULT_PARAM_HTML_DATA = "htmlData";

	private EntityDefinitionManager edm;
	private EntityViewManager evm;
	private EntityFilterManager efm;
	private EntityManager em;

	public SearchListCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		SearchListContext context = new SearchListContext();
		context.setRequest(request);
		context.setEntityDefinition(edm.get(context.getDefName()));
		context.setEntityView(evm.get(context.getDefName()));
		context.setFilter(efm.get(context.getDefName()));

		Query query = new Query();
		query.setSelect(context.getSelect());
		query.setFrom(context.getFrom());
		query.setWhere(context.getWhere());

		int count = em.count(query);
		request.setAttribute(RESULT_PARAM_COUNT, count);

		query.setOrderBy(context.getOrderBy());
		query.setLimit(context.getLimit());

		SearchResult<Entity> result = em.searchEntity(query);

		//UserEditorが設定されている場合はUser情報を検索
		//(UserPropertyEditor.jspにて、ここから値を取得する)
		Set<String> userPropertyNames = context.getUseUserPropertyEditorPropertyName();
		Map<String, Entity> userInfoMap = getUserInfoMap(userPropertyNames, result);
		request.setAttribute(Constants.USER_INFO_MAP, userInfoMap);

		try {
			List<Map<String, String>> retList = CreateSearchResultUtil.getHtmlData(result.getList(), context.getEntityDefinition(), context.getResultSection());
			request.setAttribute(RESULT_PARAM_HTML_DATA, retList);
		} catch (IOException e) {
			throw new SystemException(e);
		} catch (ServletException e) {
			throw new SystemException(e);
		}
		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * UserPropertyEditorが指定されたPropertyのUserエンティティを取得します。
	 *
	 * @param userPropertyNames UserPropertyEditorが指定されたProperty名
	 * @param result Entityの検索結果
	 * @return ユーザEntity情報
	 */
	private Map<String, Entity> getUserInfoMap(Set<String> userPropertyNames, SearchResult<Entity> result) {

		if (!userPropertyNames.isEmpty()) {
			Set<String> userOidSet = result.getList().stream()
				.map(entity -> {
					//検索結果から値を取得
					return userPropertyNames.stream()
						.map(propName -> (String)entity.getValue(propName))
						.collect(Collectors.toSet());
				})
				.flatMap(oidSet -> oidSet.stream())
				.filter(oid -> oid != null)
				.collect(Collectors.toSet());

			if (!userOidSet.isEmpty()) {
				//User名の検索
				Query q = new Query().select(Entity.OID, Entity.NAME)
						 .from(User.DEFINITION_NAME)
						 .where(new In(Entity.OID, userOidSet.toArray()));

				return em.searchEntity(q).getList().stream()
						.collect(Collectors.toMap(Entity :: getOid, entity -> entity));
			}
		}
		return null;
	}

}
