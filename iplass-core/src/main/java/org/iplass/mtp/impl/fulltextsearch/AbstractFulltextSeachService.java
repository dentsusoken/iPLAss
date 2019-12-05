/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogDeleteSql;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogInsertSql;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogSearchSql;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogUpdateSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogDeleteSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogSearchSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.properties.extend.ExpressionType;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFulltextSeachService implements FulltextSearchService {

	private static Logger logger = LoggerFactory.getLogger(AbstractFulltextSeachService.class);

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

	@Override
	public void init(Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();

		useFulltextSearch = Boolean.valueOf(config.getValue("useFulltextSearch"));
		maxRows = Integer.valueOf(config.getValue("maxRows"));
		throwExceptionWhenOverLimit = Boolean.valueOf(config.getValue("throwExceptionWhenOverLimit"));

		if (useFulltextSearch) {
			crawlLogSearchSql = rdb.getQuerySqlCreator(CrawlLogSearchSql.class);
			crawlLogDeleteSql = rdb.getUpdateSqlCreator(CrawlLogDeleteSql.class);
			crawlLogInsertSql = rdb.getUpdateSqlCreator(CrawlLogInsertSql.class);
			crawlLogUpdateSql = rdb.getUpdateSqlCreator(CrawlLogUpdateSql.class);
			deleteLogDeleteSql = rdb.getUpdateSqlCreator(DeleteLogDeleteSql.class);
			deleteLogSearchSql = rdb.getQuerySqlCreator(DeleteLogSearchSql.class);
		}
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
						return rs.getTimestamp(1);
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

						CrawlTimestampDto dto = crawlLogSearchSql.toFulltextSearchCrawlTimestampDto(rs);
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
								|| metaProperty.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore)  {
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
									|| metaProperty.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore)  {
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
