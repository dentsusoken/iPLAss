/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.rpc.entity;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.AdminEntityManager;
import org.iplass.adminconsole.shared.base.dto.entity.EqlResultInfo;
import org.iplass.adminconsole.shared.base.rpc.entity.EqlService;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

/**
 * Eql用Service実装クラス
 */
public class EqlServiceImpl extends XsrfProtectedServiceServlet implements EqlService {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 3613072451578590442L;

	private EntityManager em = AdminEntityManager.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(EqlServiceImpl.class);
	private static final Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal");

	public EqlResultInfo execute(int tenantId, final String eql, final boolean isSearchAllVersion) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<EqlResultInfo>() {
			@Override
			public EqlResultInfo call() {

				EqlResultInfo result = new EqlResultInfo();

				result.setEql(eql);
				result.setSearchAllVersion(isSearchAllVersion);
				result.addLogMessage(resourceString("runEql") + eql + (isSearchAllVersion ? " versioned" : ""));

				Query query = null;
				try {
//					query = Query.newQuery(eql);
					query = new PreparedQuery(eql).query(null);
					if (!query.isVersiond() && isSearchAllVersion) {
						query.setVersiond(true);
					}
				} catch (QueryException e) {
					logger.warn(e.getMessage(), e);
					result.addLogMessage(resourceString("errEqlAnalysis"));
					result.addLogMessage(e.getMessage());
					result.setError(true);
					return result;
				} catch (Throwable e) {
				    if (e instanceof Error) {
				        fatalLogger.error(e.getMessage(), e);
				    } else {
				        logger.error(e.getMessage(), e);
				    }
					result.addLogMessage(resourceString("errEqlAnalysis"));
					result.addLogMessage(e.getMessage());
					result.setError(true);
					return result;
				}

//TODO 現在「*」は、Query.newQuery(eql)でエラーとなるためコメント化
//				List<ValueExpression> list = query.select().getSelectValues();
//				for (int i = 0; i < list.size(); i++) {
//					ValueExpression valueExpression = list.get(i);
//					if(valueExpression instanceof Literal) {
//						if("*".equals(((Literal)valueExpression).getValue())) {
//							EntityDefinition ed = edm.get(query.getFrom().getEntityName());
//							if(ed == null) throw new NullPointerException("Entity定義が取得できない");
//							List<PropertyDefinition> pdList = ed.getPropertyList();
//							list.remove(i);
//							int addCount = 0;
//							for (int j = 0; j < pdList.size(); j++) {
//								PropertyDefinition pd = pdList.get(j);
//								if(!(pd instanceof ReferenceProperty)) {
//									list.add(i + addCount, new EntityField(pd.getName()));
//									addCount++;
//								}
//							}
//							i = i + addCount;
//						}
//					}
//				}
//				query.select().setSelectValues(list);

				//検索結果をObjectからString形式に変換するCallbackを生成(GWTのSerialize対応)
				EqlSearchPredicate callback = new EqlSearchPredicate();
				long start = System.nanoTime();
				try {
					em.search(query, callback);
				} catch (QueryException e) {
					logger.warn(e.getMessage(), e);
					result.addLogMessage(resourceString("errRunEql"));
					result.addLogMessage(e.getMessage());
					result.setError(true);
					return result;
				} catch (Throwable e) {
				    if (e instanceof Error) {
				        fatalLogger.error(e.getMessage(), e);
				    } else {
				        logger.error(e.getMessage(), e);
				    }
					result.addLogMessage(resourceString("errRunEql"));
					result.addLogMessage(e.getMessage());
					result.setError(true);
					return result;
				}
				result.addLogMessage(resourceString("runTime") + ((double)(System.nanoTime() - start)) / 1000000 + "ms");

				//クライアントに返すためValueExpressionをStringに変換
				List<ValueExpression> colValues = query.getSelect().getSelectValues();
				List<String> colList = new ArrayList<String>(colValues.size());
				for (ValueExpression colValue : colValues) {
					colList.add(colValue.toString());
				}
				result.setColumns(colList);

				//検索結果を格納
				result.setRecords(callback.getStrRecordList());

				if (callback.getStrRecordList().isEmpty()) {
					result.addLogMessage(resourceString("noData"));
				} else {
					if (callback.isLimitSearchResult()) {
						result.addLogMessage(resourceString("resultOutput", String.valueOf(callback.getLimitSize())));
					}

					result.addLogMessage(resourceString("exeResult") + result.getRecords().size());
				}

				return result;
			}
		});
	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("tools.eql.EqlServiceImpl." + suffix, arguments);
	}

}
