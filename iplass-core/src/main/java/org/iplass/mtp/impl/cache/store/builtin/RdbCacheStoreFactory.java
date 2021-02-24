/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.cache.store.builtin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;
import org.iplass.mtp.impl.cache.store.keyresolver.CacheKeyResolver;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdbCacheStoreFactory extends AbstractBuiltinCacheStoreFactory {
	private static final String DEFAULT_TABLE_NAME = "CACHE_STORE";
	private static final String NAMESPACE = "NS";
	private static final String KEY = "C_KEY";
	private static final String INDEX = "CI_";
	private static final String VAL = "C_VAL";
	private static final String VER = "VER";
	private static final String CRE_TIME = "CRE_TIME";
	private static final String INVALID_TIME = "INV_TIME";
	
	private static final Logger logger = LoggerFactory.getLogger(RdbCacheStoreFactory.class);

	private CacheKeyResolver cacheKeyResolver;
	private List<CacheKeyResolver> cacheIndexResolver;
	/** 接続ファクトリ名 */
	private String connectionFactoryName;
	/** RdbAdapter名 */
	private String rdbArapterName;
	private String tableName = DEFAULT_TABLE_NAME;
	
	private int retryCount;
	private long timeToLive = -1;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getConnectionFactoryName() {
		return connectionFactoryName;
	}

	public void setConnectionFactoryName(String connectionFactoryName) {
		this.connectionFactoryName = connectionFactoryName;
	}

	public String getRdbArapterName() {
		return rdbArapterName;
	}

	public void setRdbArapterName(String rdbArapterName) {
		this.rdbArapterName = rdbArapterName;
	}

	public CacheKeyResolver getCacheKeyResolver() {
		return cacheKeyResolver;
	}

	public void setCacheKeyResolver(CacheKeyResolver cacheKeyResolver) {
		this.cacheKeyResolver = cacheKeyResolver;
	}

	public List<CacheKeyResolver> getCacheIndexResolver() {
		return cacheIndexResolver;
	}

	public void setCacheIndexResolver(List<CacheKeyResolver> cacheIndexResolver) {
		this.cacheIndexResolver = cacheIndexResolver;
	}

	@Override
	public CacheStore createCacheStore(String namespace) {
		return new RdbCacheStore(namespace);
	}

	@Override
	public boolean canUseForLocalCache() {
		return false;
	}

	@Override
	public boolean supportsIndex() {
		return true;
	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		return new SimpleLocalCacheHandler(store, getConcurrencyLevelOfCacheHandler());
	}
	
	public static void deleteInvalidRecord() {
		
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		List<CacheStoreFactory> list = cs.getFactories();
		Set<Target> tlist = new HashSet<>();
		for (CacheStoreFactory csf: list) {
			CacheStoreFactory target = csf;
			while (target.getLowerLevel() != null) {
				target = target.getLowerLevel();
			}
			if (target instanceof RdbCacheStoreFactory) {
				RdbCacheStoreFactory t = (RdbCacheStoreFactory) target;
				tlist.add(new Target(t.connectionFactoryName, t.rdbArapterName, t.tableName));
			}
		}
		
		//transaction制御しない。途中でこけてもロールバックしない。
		
		RdbAdapterService ras = ServiceRegistry.getRegistry().getService(RdbAdapterService.class);
		if (tlist.size() > 0) {
			for (Target t: tlist) {
				if (logger.isDebugEnabled()) {
					logger.debug("delete invalid cache record of " + t);
				}
				RdbAdapter rdb = ras.getRdbAdapter(t.rdbArapterName);
				try (Connection con = rdb.getConnection(t.connectionFactoryName)) {
					String sql = delInvalidSql(t.tableName);
					try (Statement stmt = con.createStatement()) {
						stmt.executeUpdate(sql);
					}
				} catch (SQLException e) {
					throw new SystemException(e);
				}
			}
		}
	}
	
	private static String delInvalidSql(String tableName) {
		return "DELETE FROM " + tableName + " WHERE " + INVALID_TIME + "<=" + System.currentTimeMillis();
	}
	
	private static class Target {
		private String connectionFactoryName;
		private String rdbArapterName;
		private String tableName;
		private Target(String connectionFactoryName, String rdbArapterName,
				String tableName) {
			this.connectionFactoryName = connectionFactoryName;
			this.rdbArapterName = rdbArapterName;
			this.tableName = tableName;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((connectionFactoryName == null) ? 0
							: connectionFactoryName.hashCode());
			result = prime
					* result
					+ ((rdbArapterName == null) ? 0 : rdbArapterName.hashCode());
			result = prime * result
					+ ((tableName == null) ? 0 : tableName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Target other = (Target) obj;
			if (connectionFactoryName == null) {
				if (other.connectionFactoryName != null)
					return false;
			} else if (!connectionFactoryName
					.equals(other.connectionFactoryName))
				return false;
			if (rdbArapterName == null) {
				if (other.rdbArapterName != null)
					return false;
			} else if (!rdbArapterName.equals(other.rdbArapterName))
				return false;
			if (tableName == null) {
				if (other.tableName != null)
					return false;
			} else if (!tableName.equals(other.tableName))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Target [connectionFactoryName=" + connectionFactoryName
					+ ", rdbArapterName=" + rdbArapterName + ", tableName="
					+ tableName + "]";
		}
	}
	
	
	public class RdbCacheStore implements CacheStore {
		
		private String namespace;
		private List<CacheEventListener> listeners;
		private RdbAdapter rdb;
		
		private String getSql;
		private String getSqlWithCheckInvalidDate;
		private String delSql = "DELETE FROM " + tableName + " WHERE " + NAMESPACE + "=? AND " + KEY + "=?";
		private String delSqlWithVersionCheck = "DELETE FROM " + tableName + " WHERE " + NAMESPACE + "=? AND " + KEY + "=? AND " + VER + "=?";
		private String updateSql;
		private String updateSqlWithVersionCheck;
		private String insertSql;
		private String delAllSql = "DELETE FROM " + tableName + " WHERE " + NAMESPACE + "=?";
		private String keySetSql = "SELECT " + KEY + "," + CRE_TIME + " FROM " + tableName + " WHERE " + NAMESPACE + "=? AND " + INVALID_TIME + ">?";
		private String countSql = "SELECT COUNT(*)" + " FROM " + tableName + " WHERE " + NAMESPACE + "=?";
		private String countSqlWithCheckInvalidDate = "SELECT COUNT(*)" + " FROM " + tableName + " WHERE " + NAMESPACE + "=? AND " + INVALID_TIME + ">?";
		
		//FIXME 更新時にINVALID_TIMEを更新
		
		RdbCacheStore(String namespace) {
			this.namespace = namespace;
			listeners = new CopyOnWriteArrayList<CacheEventListener>();
			rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter(rdbArapterName);
			getSql = getSql(false);
			getSqlWithCheckInvalidDate = getSql(true);
			updateSql = updateSql(false);
			updateSqlWithVersionCheck = updateSql(true);
			insertSql = insertSql();
		}
		
		private String getSql(boolean checkInvalidDate) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT " + VAL + "," + VER + "," + CRE_TIME);
			if (getIndexCount() > 0) {
				for (int i = 0; i < getIndexCount(); i++) {
					sql.append("," + INDEX);
					sql.append(i);
				}
			}
			sql.append(" FROM ").append(tableName);
			sql.append(" WHERE " + NAMESPACE + "=? AND " + KEY + "=?");
			if (checkInvalidDate) {
				sql.append(" AND " + INVALID_TIME + ">?");
			}
			return sql.toString();
		}
		
		private String getByIndexSql(int indexKey) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT " + KEY + "," + VAL + "," + VER + "," + CRE_TIME);
			if (getIndexCount() > 0) {
				for (int i = 0; i < getIndexCount(); i++) {
					sql.append("," + INDEX);
					sql.append(i);
				}
			}
			sql.append(" FROM ").append(tableName);
			sql.append(" WHERE " + NAMESPACE + "=? AND " + INDEX + indexKey + "=? AND " + INVALID_TIME + ">?");
			return sql.toString();
		}

		private String updateSql(boolean withVersionCheck) {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ").append(tableName);
			sql.append(" SET ");
			sql.append(VAL + "=?," + VER + "=?," + CRE_TIME + "=?," + INVALID_TIME + "=?");
			if (getIndexCount() > 0) {
				for (int i = 0; i < getIndexCount(); i++) {
					sql.append("," + INDEX);
					sql.append(i);
					sql.append("=?");
				}
			}
			sql.append(" WHERE " + NAMESPACE + "=? AND " + KEY + "=?");
			if (withVersionCheck) {
				sql.append(" AND " + VER + "=?");
			}
			return sql.toString();
		}

		private String insertSql() {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ").append(tableName);
			sql.append("(" + NAMESPACE + ","  + KEY + "," +  VAL + "," + VER + "," + CRE_TIME + "," + INVALID_TIME);
			if (getIndexCount() > 0) {
				for (int i = 0; i < getIndexCount(); i++) {
					sql.append("," + INDEX);
					sql.append(i);
				}
			}
			sql.append(") VALUES(?,?,?,?,?,?");
			if (getIndexCount() > 0) {
				for (int i = 0; i < getIndexCount(); i++) {
					sql.append(",?");
				}
			}
			sql.append(")");
			return sql.toString();
		}
		
		@Override
		public CacheStoreFactory getFactory() {
			return RdbCacheStoreFactory.this;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		protected void notifyRemoved(CacheEntry entry) {
			if (listeners != null) {
				CacheRemoveEvent e = new CacheRemoveEvent(entry);
				for (CacheEventListener l: listeners) {
					l.removed(e);
				}
			}
		}

		protected void notifyPut(CacheEntry entry) {
			if (listeners != null) {
				CacheCreateEvent e = new CacheCreateEvent(entry);
				for (CacheEventListener l: listeners) {
					l.created(e);
				}
			}
		}

		protected void notifyUpdated(CacheEntry preEntry, CacheEntry entry) {
			if (listeners != null) {
				CacheUpdateEvent e = new CacheUpdateEvent(preEntry, entry);
				for (CacheEventListener l: listeners) {
					l.updated(e);
				}
			}
		}

		protected void notifyInvalidated(CacheEntry entry) {
			if (listeners != null) {
				CacheInvalidateEvent e = new CacheInvalidateEvent(entry);
				for (CacheEventListener l: listeners) {
					l.invalidated(e);
				}
			}
		}
		
		@Override
		public void addCacheEventListenner(CacheEventListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeCacheEventListenner(CacheEventListener listener) {
			listeners.remove(listener);
		}
		
		@Override
		public List<CacheEventListener> getListeners() {
			return listeners;
		}


		private boolean isStillAliveOrNull(CacheEntry e) {
			if (e == null) {
				return true;
			}
			return isStillAlive(e.getCreationTime());
		}
		
		private boolean isStillAlive(long createTime) {
			if (getTimeToLive() <= 0) {
				return true;
			}
			if (System.currentTimeMillis() - createTime > getTimeToLive()) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public CacheEntry put(CacheEntry entry, boolean clean) {
			
			for (int count = 0; count <= retryCount; count++) {
				try (Connection con = rdb.getConnection(connectionFactoryName)) {
					CacheEntry pre = getInternal(entry.getKey(), false, con);
					if (pre == null) {
						if (insertInternal(entry, con)) {
							notifyPut(entry);
							return pre;
						};
					} else {
						if (updateInternal(entry, true, pre.getVersion(), con)) {
							if (isStillAliveOrNull(pre)) {
								notifyUpdated(pre, entry);
								return pre;
							} else {
								notifyPut(entry);
								return null;
							}
						}
					}
				} catch (SQLException e) {
					if (!rdb.isDuplicateValueException(e)) {
						throw new SystemException("cant put CacheEntry to RDB:"+ entry, e);
					}
				}
			}
			
			throw new SystemException("cant put CacheEntry cause retry count over:" + entry);
		}
		
		private CacheEntry getInternal(Object key, boolean checkInvalidDate, Connection con) throws SQLException {
			String sql;
			if (checkInvalidDate) {
				sql = getSqlWithCheckInvalidDate;
			} else {
				sql = getSql;
			}
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, getNamespace());
				if (key instanceof NullKey) {
					ps.setString(2, key.toString());
				} else {
					ps.setString(2, cacheKeyResolver.toString(key));
				}
				if (checkInvalidDate) {
					ps.setLong(3, System.currentTimeMillis());
				}
				
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Object[] index = null;
						if (getIndexCount() > 0) {
							index = new Object[getIndexCount()];
							for (int i = 0; i < getIndexCount(); i++) {
								index[i] = cacheIndexResolver.get(i).toCacheKey(rs.getString(INDEX + i));
							}
						}
						
						byte[] valByte = rs.getBytes(VAL);
						Object value = null;
						if (valByte != null && valByte.length > 0) {
							ByteArrayInputStream byteIn = new ByteArrayInputStream(valByte);
							try {
								ObjectInputStream in = new ObjectInputStream(byteIn);
								value = in.readObject();
							} catch (ClassNotFoundException | IOException e) {
								throw new SystemException(e);
							}
						}
						
						return new CacheEntry(key, value, rs.getLong(VER), rs.getLong(CRE_TIME), index);
					} else {
						return null;
					}
				}
			}
		}
		
		private byte[] toBytes(Object val) {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			try {
				ObjectOutputStream out = new ObjectOutputStream(byteOut);
				out.writeObject(val);
			} catch (IOException e) {
				throw new SystemException(e);
			}
			return byteOut.toByteArray();
		}
		
		private boolean updateInternal(CacheEntry entry, boolean checkVersion, long preVersion, Connection con) throws SQLException {
			String sql;
			if (checkVersion) {
				sql = updateSqlWithVersionCheck;
			} else {
				sql = updateSql;
			}
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				
				if (entry.getValue() == null) {
					ps.setNull(1, Types.BLOB);
				} else {
					ps.setBytes(1, toBytes(entry.getValue()));
				}
				ps.setLong(2, entry.getVersion());
				ps.setLong(3, entry.getCreationTime());
				ps.setLong(4, invalidTime(entry.getCreationTime()));
				
				int cnt = 4;
				if (getIndexCount() > 0) {
					for (int i = 0; i < getIndexCount(); i++) {
						Object ival = entry.getIndexValue(i);
						cnt++;
						if (ival == null) {
							ps.setNull(cnt, Types.VARCHAR);
						} else {
							ps.setString(cnt, cacheIndexResolver.get(i).toString(ival));
						}
					}
				}
				cnt++;
				ps.setString(cnt, getNamespace());
				cnt++;
				if (entry.getKey() instanceof NullKey) {
					ps.setString(cnt, entry.getKey().toString());
				} else {
					ps.setString(cnt, cacheKeyResolver.toString(entry.getKey()));
				}
				if (checkVersion) {
					cnt++;
					ps.setLong(cnt, preVersion);
				}
				
				int ret = ps.executeUpdate();
				if (ret > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		private long invalidTime(long creationTime) {
			if (timeToLive <= 0) {
				return Long.MAX_VALUE;
			} else {
				return creationTime + timeToLive;
			}
		}
		
		private boolean insertInternal(CacheEntry entry, Connection con) throws SQLException {
			try (PreparedStatement ps = con.prepareStatement(insertSql)) {
				ps.setString(1, getNamespace());
				if (entry.getKey() instanceof NullKey) {
					ps.setString(2, entry.getKey().toString());
				} else {
					ps.setString(2, cacheKeyResolver.toString(entry.getKey()));
				}
				if (entry.getValue() == null) {
					ps.setNull(3, Types.BLOB);
				} else {
					ps.setBytes(3, toBytes(entry.getValue()));
				}
				ps.setLong(4, entry.getVersion());
				ps.setLong(5, entry.getCreationTime());
				ps.setLong(6, invalidTime(entry.getCreationTime()));

				int cnt = 6;
				if (getIndexCount() > 0) {
					for (int i = 0; i < getIndexCount(); i++) {
						Object ival = entry.getIndexValue(i);
						cnt++;
						if (ival == null) {
							ps.setNull(cnt, Types.VARCHAR);
						} else {
							ps.setString(cnt, cacheIndexResolver.get(i).toString(ival));
						}
					}
				}

				int ret = ps.executeUpdate();
				if (ret > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		private boolean deleteInternal(Object key, boolean checkVersion, long preVersion, Connection con) throws SQLException {
			String sql;
			if (checkVersion) {
				sql = delSqlWithVersionCheck;
			} else {
				sql = delSql;
			}
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				return deleteInternal(key, checkVersion, preVersion, ps);
			}
		}
		
		private boolean deleteInternal(Object key, boolean checkVersion, long preVersion, PreparedStatement ps) throws SQLException {
			ps.setString(1, getNamespace());
			if (key instanceof NullKey) {
				ps.setString(2, key.toString());
			} else {
				ps.setString(2, cacheKeyResolver.toString(key));
			}
			if (checkVersion) {
				ps.setLong(3, preVersion);
			}
			
			int ret = ps.executeUpdate();
			if (ret > 0) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		public CacheEntry putIfAbsent(CacheEntry entry) {
			
			for (int count = 0; count <= retryCount; count++) {
				try (Connection con = rdb.getConnection(connectionFactoryName)) {
					CacheEntry pre = getInternal(entry.getKey(), false, con);
					if (pre == null) {
						if (insertInternal(entry, con)) {
							notifyPut(entry);
							return null;
						};
					} else {
						if (isStillAliveOrNull(pre)) {
							return pre;
						} else {
							if (updateInternal(entry, true, pre.getVersion(), con)) {
								notifyPut(entry);
								return null;
							}
						}
					}
				} catch (SQLException e) {
					if (!rdb.isDuplicateValueException(e)) {
						throw new SystemException("cant putIfAbsent CacheEntry to RDB:" + entry, e);
					}
				}
			}
			
			throw new SystemException("cant putIfAbsent CacheEntry cause retry count over:" + entry);
		}

		@Override
		public CacheEntry get(Object key) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				return getInternal(key, true, con);
			} catch (SQLException e) {
				throw new SystemException("cant get CacheEntry from RDB:key=" + key, e);
			}
		}

		@Override
		public CacheEntry remove(Object key) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				CacheEntry pre = getInternal(key, false, con);
				if (pre == null) {
					return null;
				} else {
					if (deleteInternal(key, true, pre.getVersion(), con)) {
						if (isStillAliveOrNull(pre)) {
							notifyRemoved(pre);
							return pre;
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
			} catch (SQLException e) {
				throw new SystemException("cant remove CacheEntry from RDB:key=" + key, e);
			}
		}

		@Override
		public boolean remove(CacheEntry entry) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				CacheEntry pre = getInternal(entry.getKey(), false, con);
				if (pre == null) {
					return false;
				} else {
					if (entry.getVersion() != pre.getVersion()) {
						return false;
					}
					if (deleteInternal(entry.getKey(), true, pre.getVersion(), con)) {
						if (isStillAliveOrNull(pre)) {
							notifyRemoved(pre);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} catch (SQLException e) {
				throw new SystemException("cant remove CacheEntry from RDB:" + entry, e);
			}
		}

		@Override
		public CacheEntry replace(CacheEntry entry) {
			for (int count = 0; count <= retryCount; count++) {
				try (Connection con = rdb.getConnection(connectionFactoryName)) {
					CacheEntry pre = getInternal(entry.getKey(), false, con);
					if (pre == null) {
						return null;
					} else {
						if (isStillAliveOrNull(pre)) {
							if (updateInternal(entry, true, pre.getVersion(), con)) {
								notifyUpdated(pre, entry);
								return pre;
							} else {
								return null;
							}
						} else {
							return null;
						}
					}
				} catch (SQLException e) {
					if (!rdb.isDuplicateValueException(e)) {
						throw new SystemException("cant replace CacheEntry to RDB:" + entry, e);
					}
				}
			}
			
			throw new SystemException("cant replace CacheEntry cause retry count over:" + entry);
		}

		@Override
		public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				
				CacheEntry pre = getInternal(oldEntry.getKey(), false, con);
				if (pre == null) {
					return false;
				} else {
					if (isStillAliveOrNull(pre)) {
						if (oldEntry.getVersion() != pre.getVersion()) {
							return false;
						}
						if (updateInternal(newEntry, true, oldEntry.getVersion(), con)) {
							notifyUpdated(pre, newEntry);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} catch (SQLException e) {
				throw new SystemException("cant replace CacheEntry to RDB:" + newEntry, e);
			}
		}
		
		protected boolean hasListener() {
			if (listeners == null) {
				return false;
			}
			return listeners.size() > 0;
		}

		@Override
		public void removeAll() {
			if (hasListener()) {
				for (Object k: keySet()) {
					remove(k);
				}
			} else {
				try (Connection con = rdb.getConnection(connectionFactoryName)) {
					try (PreparedStatement ps = con.prepareStatement(delAllSql)) {
						ps.setString(1, getNamespace());
						ps.executeUpdate();
					}
				} catch (SQLException e) {
					throw new SystemException("cant removeAll CacheEntry from RDB", e);
				}
			}
			
		}
		
		private Object toKey(String keyStr) {
			if (keyStr == null) {
				return null;
			}
			if (keyStr.startsWith("NullKey[")) {
				return new NullKey(keyStr.substring("NullKey[".length(), keyStr.length() - 1));
			}
			return cacheKeyResolver.toCacheKey(keyStr);
		}

		@Override
		public List<Object> keySet() {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				try (PreparedStatement ps = con.prepareStatement(keySetSql)) {
					ps.setString(1, getNamespace());
					ps.setLong(2, System.currentTimeMillis());
					try (ResultSet rs = ps.executeQuery()) {
						List<Object> ret = new ArrayList<>();
						while (rs.next()) {
							if (isStillAlive(rs.getLong(2))) {
								ret.add(toKey(rs.getString(1)));
							}
						}
						return ret;
					}
				}
			} catch (SQLException e) {
				throw new SystemException("cant keySet from RDB", e);
			}
		}
		
		
		private List<CacheEntry> getByIndexInternal(int indexKey, Object indexValue, boolean onlyFirst, Connection con) throws SQLException {
			if (indexValue == null) {
				return Collections.emptyList();
			}
			
			try (PreparedStatement ps = con.prepareStatement(getByIndexSql(indexKey))) {
				ps.setString(1, getNamespace());
				ps.setString(2, cacheIndexResolver.get(indexKey).toString(indexValue));
				ps.setLong(3, System.currentTimeMillis());
				
				try (ResultSet rs = ps.executeQuery()) {
					List<CacheEntry> ret = new ArrayList<>();
					while (rs.next()) {
						long createTime = rs.getLong(CRE_TIME);
						if (isStillAlive(createTime)) {
							Object key = toKey(rs.getString(KEY));
							Object[] index = null;
							if (getIndexCount() > 0) {
								index = new Object[getIndexCount()];
								for (int i = 0; i < getIndexCount(); i++) {
									index[i] = cacheIndexResolver.get(i).toCacheKey(rs.getString(INDEX + i));
								}
							}
							
							byte[] valByte = rs.getBytes(VAL);
							Object value = null;
							if (valByte != null && valByte.length > 0) {
								ByteArrayInputStream byteIn = new ByteArrayInputStream(valByte);
								try {
									ObjectInputStream in = new ObjectInputStream(byteIn);
									value = in.readObject();
								} catch (ClassNotFoundException | IOException e) {
									throw new SystemException(e);
								}
							}
							
							ret.add(new CacheEntry(key, value, rs.getLong(VER), rs.getLong(CRE_TIME), index));
							
							if (onlyFirst) {
								return ret;
							}
						}
					}
					return ret;
				}
			}
		}
		
		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				List<CacheEntry> ret = getByIndexInternal(indexKey, indexValue, true, con);
				if (ret.size() > 0) {
					return ret.get(0);
				} else {
					return null;
				}
			} catch (SQLException e) {
				throw new SystemException("cant getByIndex from RDB:indexKey=" + indexKey + ",indexValue=" + indexValue, e);
			}
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				return getByIndexInternal(indexKey, indexValue, false, con);
			} catch (SQLException e) {
				throw new SystemException("cant getListByIndex from RDB:indexKey=" + indexKey + ",indexValue=" + indexValue, e);
			}
		}

		@Override
		public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				List<CacheEntry> ret = getByIndexInternal(indexKey, indexValue, false, con);
				if (ret.size() > 0) {
					try (PreparedStatement ps = con.prepareStatement(delSqlWithVersionCheck)) {
						for (CacheEntry ce: ret) {
							deleteInternal(ce.getKey(), true, ce.getVersion(), ps);
						}
					}
				}
				return ret;
			} catch (SQLException e) {
				throw new SystemException("cant removeByIndex from RDB:indexKey=" + indexKey + ",indexValue=" + indexValue, e);
			}
		}
		
		@Override
		public Integer getSize() {
			try (Connection con = rdb.getConnection(connectionFactoryName)) {
				return getSizeInternal(con, true);
			} catch (SQLException e) {
				throw new SystemException("cant getSize from RDB", e);
			}
		}
		
		public Integer getSizeInternal(Connection con, boolean checkInvalidDate) throws SQLException {
			String sql;
			if (checkInvalidDate) {
				sql = countSqlWithCheckInvalidDate;
			} else {
				sql = countSql;
			}
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, getNamespace());
				if (checkInvalidDate) {
					ps.setLong(2, System.currentTimeMillis());
				}

				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					return rs.getInt(1);
				}
			}
		}

		@Override
		public String trace() {
			StringBuilder builder = new StringBuilder();
			builder.append("-----------------------------------");
			builder.append("\nCacheStore Info");
			builder.append("\nCacheStore:" + this);
			builder.append("\n\tnamespace:" + namespace);
			builder.append("\n-----------------------------------");
			return builder.toString();
		}

		@Override
		public void destroy() {
		}

	}


	@Override
	public CacheStoreFactory getLowerLevel() {
		return null;
	}
	
}
