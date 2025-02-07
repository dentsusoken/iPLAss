/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity.UpdateMethod;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreSearchSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.GRdbEntityStoreStrategy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkUpdateState implements AutoCloseable {
	private static Logger logger = LoggerFactory.getLogger(BulkUpdateState.class);
	
	GRdbEntityStoreStrategy storeStrategy;
	int tenantId;
	String clientId;
	EntityContext entityContext;
	EntityHandler eh;
	BulkUpdatable bulkUpdatable;
	
	Connection con;
	RdbAdapter rdb;
	int bufferSize;
	
	private List<BulkUpdateEntity> buffer;
	private List<Object[]> mergeList;
	private Set<OidVer> existsOidVers;
	
	private Statement search;
	
	private BulkInsertHandler insert;
	private BulkUpdateHandler update;
	private BulkDeleteHandler delete;
	
	BulkUpdateState(GRdbEntityStoreStrategy storeStrategy, int tenantId, String clientId, EntityContext entityContext, EntityHandler eh, BulkUpdatable bulkUpdatable, Connection con, RdbAdapter rdb, int bufferSize) {
		this.tenantId = tenantId;
		this.clientId = clientId;
		this.entityContext = entityContext;
		this.eh = eh;
		this.bulkUpdatable = bulkUpdatable;
		this.con = con;
		this.rdb = rdb;
		this.bufferSize = bufferSize;
		buffer = new ArrayList<>(bufferSize);
		existsOidVers = new HashSet<>(bufferSize);
	}
	
	void addToBuffer(BulkUpdateEntity e) {
		buffer.add(e);
		if (e.getMethod() == UpdateMethod.MERGE
				&& e.getEntity().getOid() != null) {
			if (mergeList == null) {
				mergeList = new ArrayList<>();
			}
			mergeList.add(new Object[]{e.getEntity().getOid(), e.getEntity().getVersion()});
		}
	}
	
	private void searchMergeOidVer() throws SQLException {
		if (mergeList != null) {
			if (search == null) {
				search = con.createStatement();
				int fetchSize = bufferSize < rdb.getMaxFetchSize() ? bufferSize: rdb.getMaxFetchSize();
				search.setFetchSize(fetchSize);
			}
			try (ResultSet rs = search.executeQuery(ObjStoreSearchSql.checkExistsByKeysSql(tenantId, eh, mergeList, rdb))) {
				while (rs.next()) {
					existsOidVers.add(new OidVer(rs.getString(1), rs.getLong(2)));
				}
			}
		}
	}

	void doUpdate() throws SQLException {
		searchMergeOidVer();
		
		for (BulkUpdateEntity bue: buffer) {
			
			UpdateMethod um = bue.getMethod();
			if (um == UpdateMethod.MERGE) {
				if (existsOidVers.contains(new OidVer(bue.getEntity().getOid(), bue.getEntity().getVersion()))) {
					um = UpdateMethod.UPDATE;
				} else {
					um = UpdateMethod.INSERT;
				}
			}
			
			switch (um) {
			case DELETE:
				if (delete == null) {
					delete = new BulkDeleteHandler(this);
				}
				delete.addValue(this, bue.getEntity());
				break;
			case INSERT:
				if (insert == null) {
					insert = new BulkInsertHandler(this, bulkUpdatable.isEnableAuditPropertySpecification());
				}
				if (bue.getEntity().getOid() == null) {
					bue.getEntity().setOid(storeStrategy.newOid(entityContext, eh));
				}
				insert.addValue(this, bue.getEntity());
				break;
			case UPDATE:
				if (update == null) {
					update = new BulkUpdateHandler(this);
				}
				//このタイミングでしかUPDATEか否かわからないので。
				//UPDATEの場合、updateByの指定はできないように
				bue.getEntity().setUpdateBy(clientId);
				update.addValue(this, bue.getEntity());
				break;
			default:
				throw new IllegalArgumentException();
			}
			bulkUpdatable.updated(bue);
		}

		clearBuffer();
	}
	
	private void clearBuffer() {
		buffer.clear();
		existsOidVers.clear();
		mergeList = null;
	}
	
	public void flushAll() throws SQLException {
		if (insert != null) {
			insert.flushAll();
		}
		if (update != null) {
			update.flushAll();
		}
		if (delete != null) {
			delete.flushAll(this);
		}
	}
	
	@Override
	public void close() {
		if (search != null) {
			try {
				search.close();
			} catch (SQLException e) {
				logger.error("fail to BulkUpdateState close. maybe resource leak.", e);
			}
		}

		if (insert != null) {
			insert.close();
		}
		if (update != null) {
			update.close();
		}
		if (delete != null) {
			delete.close();
		}
	}

	private static class OidVer {
		String oid;
		long ver;
		
		OidVer(String oid, long ver) {
			this.oid = oid;
			this.ver = ver;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((oid == null) ? 0 : oid.hashCode());
			result = prime * result + (int) (ver ^ (ver >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			OidVer other = (OidVer) obj;
			if (oid == null) {
				if (other.oid != null) {
					return false;
				}
			} else if (!oid.equals(other.oid)) {
				return false;
			}
			if (ver != other.ver) {
				return false;
			}
			return true;
		}
	}
	
}	

