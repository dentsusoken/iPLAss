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

package org.iplass.mtp.impl.fulltextsearch;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchCondition;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchOption;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryNameTypeParser;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryReferenceParseException;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryReferenceParser;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogDeleteSql;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogInsertSql;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogSearchSql;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogUpdateSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogDeleteSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogSearchSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.impl.properties.extend.ExpressionType;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFulltextSearchService implements FulltextSearchService {

	private static Logger logger = LoggerFactory.getLogger(AbstractFulltextSearchService.class);

	private RdbAdapter rdb;
	private CrawlLogDeleteSql crawlLogDeleteSql;
	private CrawlLogSearchSql crawlLogSearchSql;
	private CrawlLogInsertSql crawlLogInsertSql;
	private CrawlLogUpdateSql crawlLogUpdateSql;
	private DeleteLogDeleteSql deleteLogDeleteSql;
	private DeleteLogSearchSql deleteLogSearchSql;

	private boolean useFulltextSearch;
	private int maxRows = 0;
	private boolean throwExceptionWhenOverLimit;
	protected long redundantTimeMinutes;
	
	private List<BinaryReferenceParser> binaryParsers;
	private int binaryParseLimitLength = 100000;
	
	private String scorePropertyName = "score";
	private boolean includeMappedByReferenceIfNoPropertySpecified = false;

	@Override
	public void init(Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();

		useFulltextSearch = Boolean.valueOf(config.getValue("useFulltextSearch"));
		maxRows = Integer.valueOf(config.getValue("maxRows"));
		throwExceptionWhenOverLimit = Boolean.valueOf(config.getValue("throwExceptionWhenOverLimit"));
		redundantTimeMinutes = config.getValue("redundantTimeMinutes", Long.TYPE, 10L);
		
		binaryParsers = (List<BinaryReferenceParser>) config.getValues("binaryParser", BinaryReferenceParser.class);
		if (binaryParsers == null) {
			//未指定の場合は、NameTypeParserを設定
			binaryParsers = Collections.singletonList(new BinaryNameTypeParser());
		} else {
			//最後にNameTypeParserを設定
			if (!(binaryParsers.get(binaryParsers.size() - 1) instanceof BinaryNameTypeParser)) {
				binaryParsers.add(new BinaryNameTypeParser());
			}
		}
		binaryParseLimitLength = config.getValue("binaryParseLimitLength", Integer.TYPE, 100000);

		scorePropertyName = config.getValue("scorePropertyName", String.class, "score");
		includeMappedByReferenceIfNoPropertySpecified = config.getValue("includeMappedByReferenceIfNoPropertySpecified", Boolean.class, false);
		
		if (useFulltextSearch) {
			crawlLogSearchSql = rdb.getQuerySqlCreator(CrawlLogSearchSql.class);
			crawlLogDeleteSql = rdb.getUpdateSqlCreator(CrawlLogDeleteSql.class);
			crawlLogInsertSql = rdb.getUpdateSqlCreator(CrawlLogInsertSql.class);
			crawlLogUpdateSql = rdb.getUpdateSqlCreator(CrawlLogUpdateSql.class);
			deleteLogDeleteSql = rdb.getUpdateSqlCreator(DeleteLogDeleteSql.class);
			deleteLogSearchSql = rdb.getQuerySqlCreator(DeleteLogSearchSql.class);
		}
	}
	
	@Override
	public void initTenantContext(TenantContext tenantContext) {
	}
	@Override
	public void destroyTenantContext(TenantContext tenantContext) {
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public boolean isUseFulltextSearch() {
		return useFulltextSearch;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public boolean isThrowExceptionWhenOverLimit() {
		return throwExceptionWhenOverLimit;
	}
	
	@Override
	public void execCrawlEntity(String... defNames) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		if (defNames == null || defNames.length == 0) {
			// 対象エンティティの取得
			List<DefinitionSummary> defList = edm.definitionNameList();

			// エンティティ毎にデータをクロールする
			for (final DefinitionSummary def : defList) {
				createIndexData(tenantId, def.getName());
			}
		} else {
			for (String defName : defNames) {
				createIndexData(tenantId, defName);
			}
		}
	}

	protected abstract void createIndexData(final int tenantId, String defName);
	
	protected String toValue(Object val) throws IOException {
		if (val instanceof Timestamp) {
			final SimpleDateFormat dateTimeFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDatetimeSecFormat(), true);
			dateTimeFormat.setLenient(false);
			return dateTimeFormat.format((Timestamp) val);
		} else if (val instanceof Date) {
			final SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDateFormat(), false);
			dateFormat.setLenient(false);
			return dateFormat.format((Date) val);
		} else if (val instanceof Time) {
			final SimpleDateFormat timeFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputTimeSecFormat(), false);
			timeFormat.setLenient(false);
			return timeFormat.format((Time) val);
		} else if (val instanceof BigDecimal) {
			return ((BigDecimal) val).toPlainString();
		} else if (val instanceof Long) {
			return val.toString();
		} else if (val instanceof Double) {
			return val.toString();
		} else if (val instanceof BinaryReference) {
			return parseBinaryReference((BinaryReference) val);
		} else {
			return val.toString();
		}
	}
	
	private LocaleFormat getLocaleFormat() {
		return ExecuteContext.getCurrentContext().getLocaleFormat();
	}
	
	private String parseBinaryReference(BinaryReference br) throws IOException {

		//順番にサポートしているかをチェック
		for (int i = 0; i < binaryParsers.size(); i++) {
			BinaryReferenceParser support = binaryParsers.get(i).getParser(br);
			if (support != null) {
				try {
					String value = support.parse(br, binaryParseLimitLength);

					//EmptyParserの場合、空なのでチェック
					if (StringUtil.isNotEmpty(value)) {
						logger.debug("binary reference parsed on " + support.getClass().getSimpleName()
								+ ". type=" + br.getType());
						return value;
					}
				} catch(BinaryReferenceParseException e) {
					logger.warn("binary reference parse error. so try to parse on next paser. type="
								+ br.getType() + ",parser=" + support.getClass().getSimpleName(), e);
				}
			}
		}

		//必ずNameTypeParserが設定されるのでありえない
		throw new FulltextSearchRuntimeException("invalid service status.");
	}
	
	protected Map<String, String> generateCrawlPropMap(MetaEntity meta) {
		// 対象プロパティのリストを作成する
		Map<String, String> crawlPropertyNameMap = new HashMap<String, String>();

		EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
		MetaEntity superMeta = ehs.getRuntimeById(meta.getInheritedEntityMetaDataId()).getMetaData();

		for (String crawlId : meta.getCrawlPropertyId()) {
			MetaProperty metaProperty = meta.getDeclaredPropertyById(crawlId);
			if (metaProperty == null) {
				metaProperty = superMeta.getDeclaredPropertyById(crawlId);
			}
			
			if (metaProperty != null) {
				String propertyName = metaProperty.getName();
				if (metaProperty instanceof MetaReferenceProperty) {
					crawlPropertyNameMap.put(propertyName + ".name", metaProperty.getId());
				} else {
					crawlPropertyNameMap.put(propertyName, metaProperty.getId());
				}
			} else {
				logger.warn("### DefinitionName " + "[" + meta.getName() + "] ### crawlId " + crawlId + " is not found.");
			}
		}
		
		return crawlPropertyNameMap;
	}

	@Override
	public Map<String, Timestamp> getLastCrawlTimestamp(String... defNames) {

		Map<String, Timestamp> result = new HashMap<String, Timestamp>();
		Map<String, CrawlTimestampDto> crawlData = getAllLastCrawlTimestamp();

		EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
		if (defNames.length == 0) {
			crawlData.forEach((key, value) -> {
				EntityHandler entity = ehs.getRuntimeById(key);
				if (entity != null) {
					result.put(entity.getMetaData().getName(), value.getUpDate());
				}
			});
		} else {
			for (String defName : defNames) {
				EntityHandler entity = ehs.getRuntimeByName(defName);
				if (entity == null) {
					throw new FulltextSearchRuntimeException("A target entity is not exist. [Entity：" + defName + "]");
				}
				String key = entity.getMetaData().getId();
				if (crawlData.containsKey(key)) {
					result.put(defName, crawlData.get(key).getUpDate());
				}
			}
		}
		return result;
	}

	protected Timestamp getLastCrawlTimestamp(String defId, int version) {

		SqlExecuter<Timestamp> exec = new SqlExecuter<Timestamp>() {

			@Override
			public Timestamp logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = crawlLogSearchSql.toGetLastCrawlTimestampSql(tenantId, defId, version, rdb);
				ResultSet rs = getStatement().executeQuery(sql);
				try {
					while(rs.next()) {
						return rs.getTimestamp(1, rdb.rdbCalendar());
					}
				} finally {
					rs.close();
				}

				return null;
			}
		};
		return exec.execute(rdb, true);
	}

	private Map<String, CrawlTimestampDto> getAllLastCrawlTimestamp() {

		SqlExecuter<Map<String, CrawlTimestampDto>> exec = new SqlExecuter<Map<String, CrawlTimestampDto>>() {

			@Override
			public Map<String, CrawlTimestampDto> logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = crawlLogSearchSql.toGetAllLastCrawlTimestampSql(tenantId, rdb);
				ResultSet rs = getStatement().executeQuery(sql);

				Map<String, CrawlTimestampDto> data = new HashMap<String, CrawlTimestampDto>();
				try {
					while(rs.next()) {

						CrawlTimestampDto dto = crawlLogSearchSql.toFulltextSearchCrawlTimestampDto(rs, rdb);
						data.put(dto.getObjDefId(), dto);
					}
				} finally {
					rs.close();
				}

				return data;
			}
		};
		return exec.execute(rdb, true);
	}
	
	protected interface GetScore<T> {
		double get(T t);
	}

	protected <T> List<T> mergeSortByScore(List<T> list1, List<T> list2, int maxSize, GetScore<T> func) {
		//list1, list2それぞれ事前にscore順でソートされている前提
		if (list2.isEmpty()) {
			return list1;
		}
		if (list1.isEmpty()) {
			return list2;
		}
		int size = list1.size() + list2.size();
		if (maxSize > 0 && size > maxSize) {
			size = maxSize;
		}
		
		ArrayList<T> mergeList = new ArrayList<>(size);
		for (int i = 0, i1 = 0, i2 = 0; i < size; i++) {
			if (i1 >= list1.size()) {
				mergeList.add(list2.get(i2));
				i2++;
			} else if (i2 >= list2.size()) {
				mergeList.add(list1.get(i1));
				i1++;
			} else {
				T ie1 = list1.get(i1);
				T ie2 = list2.get(i2);
				if (func.get(ie1) > func.get(ie2)) {
					mergeList.add(ie1);
					i1++;
				} else {
					mergeList.add(ie2);
					i2++;
				}
			}
		}
		
		return mergeList;
	}
	
	protected abstract List<IndexedEntity> fulltextSearchImpl(Integer tenantId, EntityHandler eh, String fulltext, int limit);
	
	private <T extends Entity> SearchResult<T> entitySearchImpl(List<IndexedEntity> oidList, EntityHandler eh, FulltextSearchCondition condition) {
		if (oidList.isEmpty()) {
			return new SearchResult<T>(-1, null);
		}
		
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Query query = new Query();
		
		if (condition == null || condition.getProperties() == null) {
			query.selectAll(eh.getMetaData().getName(), true, true, true, includeMappedByReferenceIfNoPropertySpecified);
		} else {
			for (String prop: condition.getProperties()) {
				query.select().add(prop);
			}
			if (!condition.getProperties().contains(Entity.OID)) {
				//scoreのマッピングのため最低限必要
				query.select().add(Entity.OID);
			}
			//order by項目がない場合、追加
			if (condition != null && condition.getOrder() != null) {
				for (SortSpec sortSpec : condition.getOrder().getSortSpecList()) {
					String sortKey = sortSpec.getSortKey().toString();
					if (!condition.getProperties().contains(sortKey)) {
						query.select().add(sortKey);
					}
				}
			}
			
			query.from(eh.getMetaData().getName());
		}
		
		In in = new In();
		in.setPropertyName(Entity.OID);
		List<ValueExpression> inValues = new ArrayList<ValueExpression>(oidList.size());
		for (IndexedEntity ie: oidList) {
			inValues.add(new Literal(ie.getOid()));
		}
		in.setValue(inValues);
		query.where(in);
		
		if (condition != null && condition.getOrder() != null) {
			query.setOrderBy(condition.getOrder());
		}
		
		SearchResult<T> searched = em.searchEntity(query);
		if (searched.getList().isEmpty()) {
			return searched;
		}
		
		if (condition == null || condition.getOrder() == null) {
			//sort by score
			//and set score value to each entity
			Map<String, T> map = new HashMap<>((int)(searched.getList().size() / 0.75f) + 1, 0.75f);
			for (T e: searched.getList()) {
				map.put(e.getOid(), e);
			}
			ArrayList<T> mergedList = new ArrayList<>(searched.getList().size());
			for (IndexedEntity ie: oidList) {
				T e = map.get(ie.getOid());
				if (e != null) {
					e.setValue(scorePropertyName, ie.getScore());
					mergedList.add(e);
				}
			}
			return new SearchResult<T>(searched.getTotalCount(), mergedList);
		} else {
			//set score value to each entity 
			Map<String, IndexedEntity> map = new HashMap<>((int)(oidList.size() / 0.75f) + 1, 0.75f);
			for (IndexedEntity ie: oidList) {
				map.put(ie.getOid(), ie);
			}
			for (T e: searched.getList()) {
				IndexedEntity ie = map.get(e.getOid());
				e.setValue(scorePropertyName, ie.getScore());
			}
			return searched;
		}
	}
	
	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String searchDefName, String fulltext) {
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(searchDefName);
		List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, getMaxRows());
		return entitySearchImpl(fromIndexList, eh, null);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Map<String, List<String>> entityProperties, String fulltext) {
		FulltextSearchOption option = new FulltextSearchOption();
		for (Map.Entry<String, List<String>> data : entityProperties.entrySet()) {
			FulltextSearchCondition cond = new FulltextSearchCondition(data.getValue());
			option.getConditions().put(data.getKey(), cond);
		}
		return fulltextSearchEntity(fulltext, option);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String fulltext, FulltextSearchOption option) {
		if (option.getConditions().size() <= 1) {
			EntityContext ec = EntityContext.getCurrentContext();
			Map.Entry<String, FulltextSearchCondition> e = option.getConditions().entrySet().iterator().next();
			EntityHandler eh = ec.getHandlerByName(e.getKey());
			List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, getMaxRows());
			return entitySearchImpl(fromIndexList, eh, e.getValue());
		} else {
			//複数Entity全体でmaxRowsに絞る
			EntityContext ec = EntityContext.getCurrentContext();
			List<IndexedEntity> fromIndexList = Collections.emptyList();
			Map<String, TempEntityList> tempEntityListMap = new LinkedHashMap<>();
			boolean hasOrderBy = false;
			
			for (Map.Entry<String, FulltextSearchCondition> e: option.getConditions().entrySet()) {
				EntityHandler eh = ec.getHandlerByName(e.getKey());
				List<IndexedEntity> iel = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, getMaxRows());
				fromIndexList = mergeSortByScore(fromIndexList, iel, getMaxRows(), t -> t.getScore());
				tempEntityListMap.put(e.getKey(), new TempEntityList(eh, e.getValue()));
				hasOrderBy = hasOrderBy || (e.getValue() != null && e.getValue().getOrder() != null);
			}
			
			if (fromIndexList.isEmpty()) {
				return new SearchResult<>(-1, null);
			}
			
			//divide by defName
			for (IndexedEntity ie: fromIndexList) {
				TempEntityList tel = tempEntityListMap.get(ie.getDefName());
				tel.addOids(ie);
			}
			
			List<T> resultList = new ArrayList<>();
			for (Map.Entry<String, TempEntityList> e: tempEntityListMap.entrySet()) {
				TempEntityList tel = e.getValue();
				if (tel.getOids().size() > 0) {
					SearchResult<T> resPerEntity = entitySearchImpl(tel.getOids(), tel.getEh(), tel.getCond());
					if (hasOrderBy) {
						resultList.addAll(resPerEntity.getList());
					} else {
						resultList = mergeSortByScore(resultList, resPerEntity.getList(), getMaxRows(), t -> t.getValue(scorePropertyName));
					}
				}
			}
			
			return new SearchResult<>(-1, resultList);
		}
	}

	@Override
	public List<String> fulltextSearchOidList(String searchDefName, String fulltext) {
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(searchDefName);
		List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, -1);
		List<String> res = new ArrayList<>(fromIndexList.size());
		for (IndexedEntity ie: fromIndexList) {
			res.add(ie.getOid());
		}
		return res;
	}

	@Override
	public List<FulltextSearchResult> execFulltextSearch(String searchDefName, String keywords) {
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(searchDefName);
		List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, keywords, getMaxRows());
		List<FulltextSearchResult> res = new ArrayList<>(fromIndexList.size());
		for (IndexedEntity ie: fromIndexList) {
			FulltextSearchResult r = new FulltextSearchResult();
			r.setOid(ie.getOid());
			res.add(r);
		}
		return res;
	}
	
	protected void insertCrawlLog(String defId, int version, Timestamp sysdate) {

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = crawlLogInsertSql.toSql(tenantId, defId, version, sysdate, rdb);
				logger.debug("insert crawl log sql : " + sql);
				getStatement().executeUpdate(sql);

				return null;
			}
		};
		exec.execute(rdb, true);
	}

	protected void updateCrawlLog(String defId, int version, Timestamp sysdate) {

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = crawlLogUpdateSql.toSql(tenantId, defId, version, sysdate, rdb);
				logger.debug("update crawl log sql : " + sql);
				getStatement().executeUpdate(sql);

				return null;
			}
		};
		exec.execute(rdb, true);
	}

	protected List<RestoreDto> getRestoreIndexData(String defId, Timestamp baseDate) {

		SqlExecuter<List<RestoreDto>> exec = new SqlExecuter<List<RestoreDto>>() {

			@Override
			public List<RestoreDto> logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = deleteLogSearchSql.toGetDeleteIndexDataSql(tenantId, defId, baseDate, rdb);
				ResultSet rs = getStatement().executeQuery(sql);

				List<RestoreDto> dtoList = new ArrayList<RestoreDto>();
				try {
					while(rs.next()) {
						dtoList.add(deleteLogSearchSql.toFulltextSearchRestoreDto(rs));
					}
				} finally {
					rs.close();
				}

				return dtoList;
			}
		};
		return exec.execute(rdb, true);
	}

	protected void removeDeleteLog(String defId, Timestamp baseDate) {

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = deleteLogDeleteSql.deleteProcessedLog(tenantId, defId, baseDate, rdb);
				getStatement().executeUpdate(sql);

				return null;
			}
		};
		exec.execute(rdb, true);
	}

	protected void removeAllDeleteLog() {

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = deleteLogDeleteSql.deleteAll(tenantId, rdb);
				getStatement().executeUpdate(sql);

				return null;
			}
		};
		exec.execute(rdb, true);
	}

	protected void removeAllCrawlLog() {

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {

				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				String sql = crawlLogDeleteSql.deleteAll(tenantId, rdb);
				getStatement().executeUpdate(sql);

				return null;
			}
		};
		exec.execute(rdb, true);
	}

	protected Map<String, String> generateCrawlPropInfo(MetaEntity meta) {
		// 対象プロパティのリストを作成する
		Map<String, String> crawlPropertyNameMap = new HashMap<String, String>();

		final List<MetaProperty> declaredProperties = meta.getDeclaredPropertyList();
		List<MetaProperty> superProperties = null;

		int refCnt = 1;
		int expCnt = 1;
		int columnIndex = 1;
		String columnStr = "V_";
		for (String crawlId : meta.getCrawlPropertyId()) {
			boolean match = false;
			for (MetaProperty metaProperty : declaredProperties) {

				String propertyName = metaProperty.getName();

				if (metaProperty.getId().equals(crawlId)) {
					if (metaProperty instanceof MetaReferenceProperty) {
						crawlPropertyNameMap.put(propertyName + ".name", "R_" + refCnt);
						refCnt += 1;
					} else {

						MetaPrimitiveProperty metaPrimitiveProperty = (MetaPrimitiveProperty) metaProperty;

						// ReferenceとExpression以外はすべてV_1～V_256にに割り当てられる
						if (metaPrimitiveProperty.getType() instanceof ExpressionType) {
							crawlPropertyNameMap.put(propertyName, "E_" + expCnt);
							expCnt += 1;
						} else {
							String columnName = "";
							if (metaProperty.getEntityStoreProperty() instanceof MetaGRdbPropertyStore
								|| metaProperty.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
								columnName = columnStr + columnIndex;
								columnIndex++;
//							} else {
//								RHCMetaPropertyStore metaPropertyStore = (RHCMetaPropertyStore) metaProperty.getEntityStoreProperty();
//								String[] columnNames = metaPropertyStore.getColumnNames();
//								columnName = columnNames[0];
							}
							if ("OBJ_ID".equals(columnName) || "OBJ_VER".equals(columnName)) {
								columnName = "OPT_" + columnName;
							}

							crawlPropertyNameMap.put(propertyName, columnName);
						}
					}

					match = true;
					break;
				}

			}

			if (!match) {
				//Superの検索
				if (superProperties == null) {
					EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
					EntityHandler superEntity = ehs.getRuntimeById(meta.getInheritedEntityMetaDataId());
					if (superEntity != null) {
						superProperties = superEntity.getMetaData().getDeclaredPropertyList();
					}
				}
				if (superProperties != null) {
					for (MetaProperty metaProperty : superProperties) {
						if (metaProperty.getId().equals(crawlId)) {
							String propertyName = metaProperty.getName();
							//ReferencePropertyは外す
							if (!(metaProperty instanceof MetaReferenceProperty)) {
								String columnName = "";
								if (metaProperty.getEntityStoreProperty() instanceof MetaGRdbPropertyStore
									|| metaProperty.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
									columnName = columnStr + columnIndex;
									columnIndex++;
//								} else {
//									RHCMetaPropertyStore metaPropertyStore = (RHCMetaPropertyStore) metaProperty.getEntityStoreProperty();
//									String[] columnNames = metaPropertyStore.getColumnNames();
//									columnName = columnNames[0];
								}
								if ("OBJ_ID".equals(columnName) || "OBJ_VER".equals(columnName)) {
									columnName = "OPT_" + columnName;
								}

								crawlPropertyNameMap.put(propertyName, columnName);
							}
							match = true;
							break;
						}
					}
				}
			}

			if (!match) {
				logger.warn("### DefinitionName " + "[" + meta.getName() + "] ###" + "\r\n" + "crawlId " + crawlId + " is not found.");
			}
		}

		return crawlPropertyNameMap;
	}

	public static class CrawlTimestampDto {

		private String objDefId;

		private String objDefVer;

		private Timestamp upDate;

		public String getObjDefId() {
			return objDefId;
		}

		public void setObjDefId(String objDefId) {
			this.objDefId = objDefId;
		}

		public String getObjDefVer() {
			return objDefVer;
		}

		public void setObjDefVer(String objDefVer) {
			this.objDefVer = objDefVer;
		}

		public Timestamp getUpDate() {
			return upDate;
		}

		public void setUpDate(Timestamp upDate) {
			this.upDate = upDate;
		}

	}

	public static class RestoreDto {

		private String id;

		private int tenantId;

		private String objDefId;

		private String objId;

		private Long objVer;

		private Status status;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getTenantId() {
			return tenantId;
		}

		public void setTenantId(int tenantId) {
			this.tenantId = tenantId;
		}

		public String getObjDefId() {
			return objDefId;
		}

		public void setObjDefId(String objDefId) {
			this.objDefId = objDefId;
		}

		public String getObjId() {
			return objId;
		}

		public void setObjId(String objId) {
			this.objId = objId;
		}

		public Long getObjVer() {
			return objVer;
		}

		public void setObjVer(Long objVer) {
			this.objVer = objVer;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}
	}

}
