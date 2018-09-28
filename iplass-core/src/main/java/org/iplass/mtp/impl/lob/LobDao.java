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

package org.iplass.mtp.impl.lob;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.lob.sql.BlobDeleteSql;
import org.iplass.mtp.impl.lob.sql.BlobInsertSql;
import org.iplass.mtp.impl.lob.sql.BlobLobDataIdUpdateSql;
import org.iplass.mtp.impl.lob.sql.BlobRecycleBinSql;
import org.iplass.mtp.impl.lob.sql.BlobSearchSql;
import org.iplass.mtp.impl.lob.sql.BlobStatUpdateSql;
import org.iplass.mtp.impl.lob.sql.LobStoreDeleteSql;
import org.iplass.mtp.impl.lob.sql.LobStoreInsertSql;
import org.iplass.mtp.impl.lob.sql.LobStoreSearchSql;
import org.iplass.mtp.impl.lob.sql.LobStoreTable;
import org.iplass.mtp.impl.lob.sql.LobStoreUpdateSql;
import org.iplass.mtp.impl.lob.sql.ObjBlobTable;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class LobDao {
	private static final String COUNTER_SERVICE_INC_KEY = "lobid";
	
	private RdbAdapter rdb;
	private CounterService counterService;
	private boolean manageLobSizeOnRdb;

	private BlobInsertSql blobInsertSql;
	private BlobSearchSql searchSql;
	private BlobDeleteSql deleteSql;
	private BlobStatUpdateSql statUpdateSql;
	private BlobLobDataIdUpdateSql lobDataIdUpdateSql;
	private BlobRecycleBinSql recycleBinSql;

	private LobStoreDeleteSql lobStoreDelSql;
	private LobStoreInsertSql lobStoreInsertSql;
	private LobStoreSearchSql lobStoreSearchSql;
	private LobStoreUpdateSql lobStoreUpdateSql;

	public LobDao() {
	}

	public void init(RdbAdapter rdb, CounterService counterService, boolean manageLobSizeOnRdb) {
		this.rdb = rdb;
		this.counterService = counterService;
		this.manageLobSizeOnRdb = manageLobSizeOnRdb;
		blobInsertSql = rdb.getUpdateSqlCreator(BlobInsertSql.class);
		searchSql = rdb.getQuerySqlCreator(BlobSearchSql.class);
		deleteSql = rdb.getUpdateSqlCreator(BlobDeleteSql.class);
		statUpdateSql = rdb.getUpdateSqlCreator(BlobStatUpdateSql.class);
		lobDataIdUpdateSql = rdb.getUpdateSqlCreator(BlobLobDataIdUpdateSql.class);
		recycleBinSql = rdb.getUpdateSqlCreator(BlobRecycleBinSql.class);

		lobStoreDelSql = rdb.getUpdateSqlCreator(LobStoreDeleteSql.class);
		lobStoreInsertSql = rdb.getUpdateSqlCreator(LobStoreInsertSql.class);
		lobStoreSearchSql = rdb.getQuerySqlCreator(LobStoreSearchSql.class);
		lobStoreUpdateSql = rdb.getUpdateSqlCreator(LobStoreUpdateSql.class);
	}

	public Lob create(int tenantId, String name, String type, String defId, String propId, String oid, Long version, LobStore lobStore) {
		return createInternal(tenantId, name, type, false, null, defId, propId, oid, version, Lob.IS_NEW, lobStore);
	}

	public Lob crateTemporary(int tenantId, String name, String type, String sessionId, LobStore lobStore) {
		return createInternal(tenantId, name, type, true, sessionId, null, null, null, null, Lob.IS_NEW, lobStore);
	}

	public Lob create(Lob toCreate, LobStore lobStore) {
		return createInternal(toCreate.getTenantId(), toCreate.getName(), toCreate.getType(), toCreate.getSessionId() != null, toCreate.getSessionId(), toCreate.getDefinitionId(), toCreate.getPropertyId(), toCreate.getOid(), toCreate.getVersion(), toCreate.getLobDataId(), lobStore);
	}

	private Lob createInternal(final int tenantId, final String name, final String type, final boolean isTemporary,
			final String sessionId, final String defId, final String propId, final String oid, final Long version, final long lobDataId, final LobStore lobStore) {

		final long lobId = nextLobId(tenantId);
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				//更新SQLの発行
				String updateSql = blobInsertSql.toSql(tenantId, lobId, name, type, isTemporary, sessionId, defId, propId, oid, version, lobDataId, rdb);
				return getStatement().executeUpdate(updateSql);
			}
		};

		exec.execute(rdb, true);
		String status = null;
		if (isTemporary) {
			status = " ";
		} else {
			status = Lob.STATE_VALID;
		}
		Lob bin = new Lob(tenantId, lobId, name, type, defId, propId, oid, version, sessionId, status, lobDataId, lobStore, this, manageLobSizeOnRdb);
		return bin;
	}

	public long nextLobId(final int tenantId) {
		return counterService.increment(tenantId, COUNTER_SERVICE_INC_KEY, 1);
	}

	public Lob load(final int tenantId, final long lobId, final LobStore lobStore) {
		return loadInternal(tenantId, lobId, null, null, null, null, null, false, lobStore);
	}

	public Lob loadWithLock(int tenantId, long lobId, String sessionId,
			String defId, String propId, String oid, Long version, final LobStore lobStore) {
		return loadInternal(tenantId, lobId, sessionId, defId, propId, oid, version, true, lobStore);
	}

	private Lob loadInternal(final int tenantId, final long lobId, final String sessionId, final String defId, final String propId, final String oid, final Long version, final boolean withLock, final LobStore lobStore) {
		SqlExecuter<Lob> exec = new SqlExecuter<Lob>() {

			@Override
			public Lob logic() throws SQLException {

				ResultSet rs = getStatement().executeQuery(searchSql.toSql(rdb, tenantId, lobId, sessionId,defId, propId, oid, version, withLock));
				Lob bin = null;
				if (rs.next()) {
					bin = searchSql.toBinaryData(rs, lobStore, LobDao.this, manageLobSizeOnRdb);
				}
				rs.close();
				return bin;
			}
		};

		return exec.execute(rdb, true);
	}

	public Lob[] search(final int clientTenantId, final long[] lobId, final LobStore lobStore) {
		SqlExecuter<Lob[]> exec = new SqlExecuter<Lob[]>() {

			@Override
			public Lob[] logic() throws SQLException {

				ResultSet rs = getStatement().executeQuery(searchSql.toSearchSql(rdb, clientTenantId, lobId));
				ArrayList<Lob> res = new ArrayList<Lob>();

				while (rs.next()) {
					Lob bin = searchSql.toBinaryData(rs, lobStore, LobDao.this, manageLobSizeOnRdb);
					res.add(bin);
				}
				rs.close();
				return res.toArray(new Lob[res.size()]);
			}
		};
		return exec.execute(rdb, true);
	}

	public void remove(final int tenantId, final long lobId) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(deleteSql.toSql(tenantId, lobId, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	public void remove(final int tenantId, final String defId, final String propId,
			final String[] oid) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(deleteSql.toSql(tenantId, defId, propId, oid, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	public boolean markPersistence(final int tenantId, final long lobId, final String defId,
			final String propId, final String oid, final Long version) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				int count = getStatement().executeUpdate(statUpdateSql.toSql(tenantId, lobId, defId, propId, oid, version, rdb));
				return Boolean.valueOf(count > 0);
			}
		};

		return exec.execute(rdb, true).booleanValue();
	}
	
	public boolean updateBinaryDataInfo(final int tenantId, final long lobId, final String name, final String type) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				int count = getStatement().executeUpdate(statUpdateSql.toInfoUpdateSql(tenantId, lobId, name, type, rdb));
				return Boolean.valueOf(count > 0);
			}
		};

		return exec.execute(rdb, true).booleanValue();
	}


	public void cleanTemporary() {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(deleteSql.toSqlForCleanTemporary(rdb, -1));
			}
		};

		exec.execute(rdb, true);
	}


	public List<Long> getLobIdByRbid(final int tenantId, final long rbid) {
		SqlExecuter<List<Long>> exec = new SqlExecuter<List<Long>>() {

			@Override
			public List<Long> logic() throws SQLException {
				List<Long> res = new ArrayList<Long>();
				ResultSet rs = getStatement().executeQuery(recycleBinSql.searchSql(rdb, tenantId, rbid));
				try {
					while (rs.next()) {
						res.add(rs.getLong(ObjBlobTable.LOB_ID));
					}
				} finally {
					rs.close();
				}
				return res;
			}
		};

		return exec.execute(rdb, true);
	}

	public void removeFromRecycleBin(final int tenantId, final long rbid) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(recycleBinSql.deleteByRbIdSql(tenantId, rbid, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	public void markToRecycleBin(final int tenantId, final long lobId, final long rbid) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(recycleBinSql.insertSql(tenantId, rbid, lobId, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	public void markRestoreFromRecycleBin(final int tenantId, final long rbid) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(recycleBinSql.deleteByRbIdSql(tenantId, rbid, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	public boolean refCountUp(final int tenantId, final long lobDataId, final int countUpValue) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {

				PreparedStatement stmt = getPreparedStatement(lobStoreUpdateSql.toPrepareSqlForRefCountUpdate(rdb));
				stmt.setInt(1, countUpValue);
				stmt.setInt(2, tenantId);
				stmt.setLong(3, lobDataId);
				return stmt.executeUpdate() == 1;
			}
		};

		return exec.execute(rdb, true);
	}

	public long nextLobDataId(final int tenantId) {
		//lobIdと同じ採番体系を使いまわし
		return nextLobId(tenantId);
	}


	public boolean updateLobDataId(final int tenantId, final long lobId, final long prevLobDataId, final long newLobDataId) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {

				int count = getStatement().executeUpdate(lobDataIdUpdateSql.toSql(tenantId, lobId, prevLobDataId, newLobDataId, rdb));
				return Boolean.valueOf(count > 0);
			}
		};

		return exec.execute(rdb, true).booleanValue();
	}

	public void initLobData(final int tenantId, final long lobDataId, Long size) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(lobStoreInsertSql.toSql(tenantId, lobDataId, size, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	/**
	 * 
	 * @param day
	 * @param tenantId
	 * @return LOB_ID,LOB_DATA_IDのリスト
	 */
	public List<long[]> getLobIdListForCleanTemporary(final int day, final int tenantId) {
		SqlExecuter<List<long[]>> exec = new SqlExecuter<List<long[]>>() {

			@Override
			public List<long[]> logic() throws SQLException {
				List<long[]> res = new ArrayList<>();
				ResultSet rs = getStatement().executeQuery(searchSql.toSqlForCleanTemporary(rdb, day, tenantId));
				try {
					while (rs.next()) {
						res.add(new long[]{rs.getLong(ObjBlobTable.LOB_ID), rs.getLong(ObjBlobTable.LOB_DATA_ID)});
					}
				} finally {
					rs.close();
				}
				return res;
			}
		};

		return exec.execute(rdb, true);
	}

	public List<Long> getLobDataIdListForClean(final int tenantId) {
		SqlExecuter<List<Long>> exec = new SqlExecuter<List<Long>>() {

			@Override
			public List<Long> logic() throws SQLException {
				List<Long> res = new ArrayList<Long>();
				ResultSet rs = getStatement().executeQuery(lobStoreSearchSql.toSqlForClean(tenantId, rdb));
				try {
					while (rs.next()) {
						res.add(rs.getLong(LobStoreTable.LOB_DATA_ID));
					}
				} finally {
					rs.close();
				}
				return res;
			}
		};

		return exec.execute(rdb, true);
	}

	public void removeData(final int tenantId, final long lobDataId) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {
				return getStatement().executeUpdate(lobStoreDelSql.toSql(tenantId, lobDataId, rdb));
			}
		};

		exec.execute(rdb, true);
	}

	public List<Long> getLobIdByDefId(final int tenantId, final String defId) {
		SqlExecuter<List<Long>> exec = new SqlExecuter<List<Long>>() {

			@Override
			public List<Long> logic() throws SQLException {
				List<Long> res = new ArrayList<Long>();
				ResultSet rs = getStatement().executeQuery(searchSql.toSqlByDefId(rdb, tenantId, defId));
				try {
					while (rs.next()) {
						res.add(rs.getLong(ObjBlobTable.LOB_ID));
					}
				} finally {
					rs.close();
				}
				return res;
			}
		};

		return exec.execute(rdb, true);
	}

	/**
	 * Entity定義として参照していないLobデータ情報を取得するためのSQL定義
	 *
	 * @param tenantId
	 * @param eh
	 * @return
	 */
	public List<Long> getLobIdForDefrag(final int tenantId, final EntityHandler eh) {

		//有効なPropertyのIDを取得する
		final List<String> binPropertyIds = new ArrayList<>();
		for (PropertyHandler ph : eh.getDeclaredPropertyList()) {
			if (ph instanceof PrimitivePropertyHandler) {
				PrimitivePropertyHandler pph = (PrimitivePropertyHandler)ph;
				if (pph.getEnumType() == PropertyDefinitionType.BINARY || pph.getEnumType() == PropertyDefinitionType.LONGTEXT) {
					binPropertyIds.add(pph.getId());
				}
			}
		}

		SqlExecuter<List<Long>> exec = new SqlExecuter<List<Long>>() {

			@Override
			public List<Long> logic() throws SQLException {
				List<Long> res = new ArrayList<Long>();
				ResultSet rs = getStatement().executeQuery(searchSql.toSqlForDefrag(rdb, tenantId, eh.getMetaData().getId(), binPropertyIds));
				try {
					while (rs.next()) {
						res.add(rs.getLong(ObjBlobTable.LOB_ID));
					}
				} finally {
					rs.close();
				}
				return res;
			}
		};

		return exec.execute(rdb, true);
	}


	/**
	 * LobStoreのLobサイズが登録されていないLobDataIdを返します。
	 *
	 * @param tenantId 対象テナントID
	 * @return LobDataIdのリスト
	 */
	public List<Long> getLobDataIdListForLobStoreSizeUpdate(final int tenantId) {
		SqlExecuter<List<Long>> exec = new SqlExecuter<List<Long>>() {

			@Override
			public List<Long> logic() throws SQLException {
				List<Long> res = new ArrayList<Long>();
				ResultSet rs = getStatement().executeQuery(lobStoreSearchSql.toSqlForSizeUpdate(tenantId, rdb));
				try {
					while (rs.next()) {
						res.add(rs.getLong(LobStoreTable.LOB_DATA_ID));
					}
				} finally {
					rs.close();
				}
				return res;
			}
		};

		return exec.execute(rdb, true);
	}


	/**
	 * LobStoreのLobサイズを更新します。
	 *
	 * @param tenantId テナントID
	 * @param lobDataId LobDataId
	 * @param size サイズ
	 */
	public boolean updateLobStoreSize(final int tenantId, final long lobDataId, final long size) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {

				PreparedStatement stmt = getPreparedStatement(lobStoreUpdateSql.toPrepareSqlForLobSizeUpdate(rdb));
				stmt.setLong(1, size);
				stmt.setInt(2, tenantId);
				stmt.setLong(3, lobDataId);
				return stmt.executeUpdate() == 1;
			}

		};
		return exec.execute(rdb, true);
	}
	
}
