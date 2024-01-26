/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.fulltextsearch;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.interceptor.EntityCountInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityRestoreInvocationImpl;
import org.iplass.mtp.impl.fulltextsearch.FulltextSearchQueryASTTransformer;
import org.iplass.mtp.impl.fulltextsearch.FulltextSearchService;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogInsertSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;

public class FulltextSearchInterceptor extends EntityInterceptorAdapter implements ServiceInitListener<EntityService> {

	private RdbAdapter rdb;
	private FulltextSearchService fss;
	private DeleteLogInsertSql deleteLogInsertSql;

	@Override
	public void inited(EntityService service, Config config) {
		rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
		fss = ServiceRegistry.getRegistry().getService(FulltextSearchService.class);

		if (fss.isUseFulltextSearch()) {
			deleteLogInsertSql = rdb.getUpdateSqlCreator(DeleteLogInsertSql.class);
		}
	}

	@Override
	public void destroyed() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@SuppressWarnings("unchecked")
	@Override
	public void query(EntityQueryInvocation invocation) {

		if (fss.isUseFulltextSearch()) {
			EntityQueryInvocationImpl inv = (EntityQueryInvocationImpl) invocation;

			// containsが含まれていれば全文検索しIN句に変換
			Query query = inv.getQuery();
			String defName = query.getFrom().getEntityName();

			FulltextSearchQueryASTTransformer t = new FulltextSearchQueryASTTransformer(defName);
			Query tQuery = (Query) query.accept(t);

			if (StringUtil.isEmpty(t.getSearchText())) {
				invocation.proceed();
				return;
			}

			inv.setQuery(tQuery);

			final Map<String, Map<String, List<String>>> highlightMap = t.getHighlightMap();

			// ハイライト情報をセット
			if (invocation.getType() == InvocationType.SEARCH_ENTITY) {
				final Predicate<Entity> actual = ((Predicate<Entity>) invocation.getPredicate());
				Predicate<Entity> wrapper = new Predicate<Entity>() {
					@Override
					public boolean test(Entity entity) {

						entity.setValue("highlightInfo", highlightMap.get(entity.getOid()));
						return actual.test(entity);
					}
				};
				invocation.setPredicate(wrapper);
			}
			invocation.proceed();
		} else {
			invocation.proceed();
		}
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		if (fss.isUseFulltextSearch()) {
			EntityCountInvocationImpl inv = (EntityCountInvocationImpl) invocation;

			// containsが含まれていれば全文検索しIN句に変換
			Query query = inv.getQuery();
			String defName = query.getFrom().getEntityName();

			FulltextSearchQueryASTTransformer t = new FulltextSearchQueryASTTransformer(defName);
			Query tQuery = (Query) query.accept(t);

			if (StringUtil.isEmpty(t.getSearchText())) {
				return invocation.proceed();
			}

			inv.setQuery(tQuery);

			return invocation.proceed();
		} else {
			return invocation.proceed();
		}
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {

		if (fss.isUseFulltextSearch()) {
			Entity entity = null;
			EntityHandler eh = ((EntityDeleteInvocationImpl) invocation).getEntityHandler();

			//削除前の状態のEntityの情報がほしいので、ロードする。
			eh = ((EntityDeleteInvocationImpl) invocation).getEntityHandler();
			entity = new EntityLoadInvocationImpl(invocation.getEntity().getOid(), invocation.getEntity().getVersion(), new LoadOption(true, false), false, eh.getService().getInterceptors(), eh).proceed();

			invocation.proceed();

			// クロール対象エンティティの場合は削除ログにデータ追加
			if (eh.getMetaData().isCrawl()) {

				ExecuteContext ctx = ExecuteContext.getCurrentContext();
				int tenantId = ctx.getClientTenantId();
				String objDefId = eh.getMetaData().getId();
				String objId = entity.getOid();
				Long objVer = entity.getVersion();

				SqlExecuter<Void> exec = new SqlExecuter<Void>() {

					@Override
					public Void logic() throws SQLException {

						String sql = deleteLogInsertSql.toSql(tenantId, objDefId, objId, objVer, Status.DELETE, InternalDateUtil.getNow(), rdb);
						getStatement().executeUpdate(sql);

						return null;
					}
				};
				exec.execute(rdb, true);
			}
		} else {
			invocation.proceed();
		}
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		EntityHandler eh = ((EntityRestoreInvocationImpl) invocation).getEntityHandler();

		Entity entity = invocation.proceed();

		if (fss.isUseFulltextSearch()) {
			ExecuteContext ctx = ExecuteContext.getCurrentContext();
			int tenantId = ctx.getClientTenantId();
			String objDefId = eh.getMetaData().getId();
			String objId = entity.getOid();
			Long objVer = entity.getVersion();

			SqlExecuter<Void> exec = new SqlExecuter<Void>() {

				@Override
				public Void logic() throws SQLException {

					String sql = deleteLogInsertSql.toSql(tenantId, objDefId, objId, objVer, Status.RESTORE, InternalDateUtil.getNow(), rdb);
					getStatement().executeUpdate(sql);

					return null;
				}
			};
			exec.execute(rdb, true);
		}

		return entity;
	}
}
