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

package org.iplass.mtp.impl.metadata.rdb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.iplass.mtp.impl.metadata.AbstractXmlMetaDataStore;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataEntryThinWrapper;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RdbMetaDataStore extends AbstractXmlMetaDataStore {
	private static Logger logger = LoggerFactory.getLogger(RdbMetaDataStore.class);

	private RdbAdapter rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
	private SelectSQL select;
	private UpdateSQL update;
	private UpdateConfigSQL updateConfig;
	private DeleteSQL delete;
	private InsertSQL insert;

	/** OBJ_DEF_NAMEカラムを利用するか(1.xとの並行利用用) */
	private boolean useObjDefNameAndType;

	public RdbMetaDataStore() {
		select = rdb.getQuerySqlCreator(SelectSQL.class);
		update = rdb.getUpdateSqlCreator(UpdateSQL.class);
		updateConfig = rdb.getUpdateSqlCreator(UpdateConfigSQL.class);
		delete = rdb.getUpdateSqlCreator(DeleteSQL.class);
		insert = rdb.getUpdateSqlCreator(InsertSQL.class);
	}

	@Override
	public MetaDataEntry loadById(final int tenantId, final String id) {
		return loadById(tenantId, id, -1);
	}

	@Override
	public MetaDataEntry loadById(final int tenantId, final String id, final int version) {
		SqlExecuter<MetaDataEntry> exec = new SqlExecuter<MetaDataEntry>() {
			@Override
			public MetaDataEntry logic() throws SQLException {
				// SQLの取得
				String sql = select.createLoadByIdSQL(version);
				PreparedStatement ps = getPreparedStatement(sql);

				// 条件設定
				select.setLoadByIdParameter(rdb, ps, tenantId, id, version);
				//検索SQL実行
				ResultSet rs = ps.executeQuery();
				MetaDataEntry ret;
				try {
					ret = select.createLoadResultData(rdb, rs, context);
					if (ret != null && ret.getMetaData() == null) {
						logger.warn("Cannot unmarshal metadata from Rdb. id:" + id + ", version:" + version);
					}
				} catch (JAXBException e) {
					throw new MetaDataRuntimeException("cant parse xml to metaData instance. id:" + id + ",version:" + version, e);
				} finally {
					rs.close();
				}
				return ret;
			}
		};
		MetaDataEntry metaData = exec.execute(rdb, true);
		return metaData;
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withInvalid)
		throws MetaDataRuntimeException {
		SqlExecuter<List<MetaDataEntryInfo>> exec = new SqlExecuter<List<MetaDataEntryInfo>>() {
			@Override
			public List<MetaDataEntryInfo> logic() throws SQLException {
				// SQLの取得
				String sql = select.createNodeListSQL(rdb, prefixPath, withInvalid);
				PreparedStatement ps = getPreparedStatement(sql);
				// 条件設定
				select.setNodeListParameter(rdb, ps, tenantId, prefixPath);
				//検索SQL実行
				ResultSet rs = ps.executeQuery();
				List<MetaDataEntryInfo> ret;
				try {
					ret = select.createNodeListResultData(rs, rdb);
				} finally {
					rs.close();
				}
				return ret;
			}
		};
		return exec.execute(rdb, true);
	}

	@Override
	public void destroyed() {
		super.destroyed();
	}

	@Override
	public void inited(MetaDataRepository service, Config config) {
		super.inited(service, config);

		if (config.getValue("useObjDefNameAndType") != null) {
			useObjDefNameAndType = config.getValue("useObjDefNameAndType").equalsIgnoreCase("true");
		}
	}

	private MetaDataEntry loadInternal(final int tenantId, final String path, final int version) {
		SqlExecuter<MetaDataEntry> exec = new SqlExecuter<MetaDataEntry>() {
			@Override
			public MetaDataEntry logic() throws SQLException {
				// SQLの取得
				String sql = select.createLoadSQL(version);
				PreparedStatement ps = getPreparedStatement(sql);

				// 条件設定
				select.setLoadParameter(rdb, ps, tenantId, path, version);
				//検索SQL実行
				ResultSet rs = ps.executeQuery();
				MetaDataEntry ret;
				try {
					ret = select.createLoadResultData(rdb, rs, context);
					if (ret != null && ret.getMetaData() == null) {
						logger.warn("Cannot unmarshal metadata from Rdb. path:" + path + ", version:" + version);
					}
				} catch (JAXBException e) {
					throw new MetaDataRuntimeException("cant parse xml to metaData instance. path:" + path + ",version=" + version, e);
				} finally {
					rs.close();
				}
				return ret;
			}

		};
		MetaDataEntry metaData = exec.execute(rdb, true);
		return metaData;
	}

	@Override
	public MetaDataEntry load(final int tenantId, final String path) throws MetaDataRuntimeException {
		final int version = -1;
		return loadInternal(tenantId, path, version);
	}

	@Override
	public MetaDataEntry load(int tenantId, String path, int version) throws MetaDataRuntimeException {
		return loadInternal(tenantId, path, version);
	}

	//FIXME ロジック効率化。sqlは1回で情報取得する（version, stateを一括で取得。）
	private int getStoreVersion(final int tenantId, final MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {

		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				//全データ件数の取得（無効データも含む）
				String sql = select.createDataCountSQL(false);
				PreparedStatement ps = getPreparedStatement(sql);
				select.setDataCountParameter(rdb, ps, tenantId, metaDataEntry.getMetaData());
				ResultSet rs = ps.executeQuery();
				int count = 0;
				try {
					count = select.getDataCountResultData(rs);
				} finally {
					rs.close();
				}

				int maxVersion = -1;
				if (count > 0) {
					// 最大Version番号の取得(削除データも含む)
					sql = select.createMaxVersionSQL();
					ps = getPreparedStatement(sql);
					select.setMaxVersionParameter(rdb, ps, tenantId, metaDataEntry.getMetaData());
					rs = ps.executeQuery();
					try {
						maxVersion = select.getMaxVersionResultData(rs);
						if(maxVersion < 0) {
							maxVersion = -1;
						}
					} finally {
						rs.close();
					}

					// 同一IDで既に登録済のデータがある場合、有効なデータの存在チェック
					if (maxVersion >= 0) {
						sql = select.createDataCountSQL(true);
						ps = getPreparedStatement(sql);
						select.setDataCountParameter(rdb, ps, tenantId, metaDataEntry.getMetaData());
						rs = ps.executeQuery();
						int validCount = 0;
						try {
							validCount = select.getDataCountResultData(rs);
							if(validCount > 0) {
								//有効なデータがある場合はエラー
								throw new MetaDataRuntimeException("Registered metadata already exists. path=" + metaDataEntry.getPath());
							}
						} finally {
							rs.close();
						}
					}
				}

				return maxVersion + 1;
			}
		};
		return exec.execute(rdb, true);
	}

	@Override
	public void store(final int tenantId, final MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		int ver = getStoreVersion(tenantId, metaDataEntry);
		storeImpl(tenantId, metaDataEntry, ver, false);
	}

	private void storeImpl(final int tenantId, final MetaDataEntry metaDataEntry, final int asVersion, final boolean isUpdate) throws MetaDataRuntimeException {
		//履歴で削除済み含めすべて保持。
		//登録も更新も同一処理で、新しいバージョンでinsert
		//異なる点は、insert前のチェック
		//登録⇒既存データあった（or有効の）場合、エラー
		//更新⇒既存データなかった（or無効の）場合、エラー

		//1.同一IDの最新バージョン(A)を取得
		//2.(A)がある場合、かつ(A)が有効な場合、(A)を無効に
		//3.(A)と登録（更新）するメタデータのpathが異なる場合、既存のDB内の同一IDのメタデータのpathを変更
		//4.登録（更新）するメタデータを新しいバージョンでInsert

		//※同一パスで別IDが複数登録できてしまう。
		//　パスがリネーム可能なことが前提で、
		//　メタデータExport/Importを考えると、これを許可しておく必要がある。
		//　検索時複数の有効なメタデータがあった場合、更新日が大きいほうを有効とすることで、ある程度うまく動くと思う。

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				//現在の情報取得
				MetaDataEntryInfo info = null;
				String sql = select.createGetMetadataInfoSQL(rdb, false);
				try (PreparedStatement ps = getPreparedStatement(sql)) {
					ps.setFetchSize(1);
					select.setGetMetadataInfoParameter(rdb, ps, tenantId, metaDataEntry.getMetaData().getId());
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							info = select.createMetaDataEntryInfo(rs, rdb);
						}
					}
				}

				//既存データチェック
				if (isUpdate) {
					//共通のメタデータから更新の場合は、RDB上データはない
//					if (info == null || !info.getState().equals("V")) {
//						throw new MetaDataRuntimeException("MetaData:" + metaDataEntry.getPath() + "(id=" + metaDataEntry.getMetaData().getId() + ") is not found or not valid.");
//					}
				} else {
					if (info != null && info.getState() == State.VALID) {
						throw new MetaDataRuntimeException("MetaData:" + metaDataEntry.getPath() + "(id=" + metaDataEntry.getMetaData().getId() + ") is already exist.");
					}
				}

				boolean withPathChange = false;
				if (info != null && !info.getPath().equals(metaDataEntry.getPath())) {
					withPathChange = true;
				}

				if (withPathChange) {
					// 現状データの論理削除とパス変更
					sql = update.createUpdateWithPathSQL(rdb);
					try (PreparedStatement ps = getPreparedStatement(sql)) {
						update.setUpdateWithPathParameter(rdb, ps, tenantId, metaDataEntry.getMetaData().getId(), metaDataEntry.getPath());
						ps.executeUpdate();
					}
				} else {
					if (info != null && info.getState() == State.VALID) {
						// 現状データの論理削除
						sql = update.createUpdateSQL(rdb);
						try (PreparedStatement ps = getPreparedStatement(sql)) {
							update.setUpdateParameter(rdb, ps, tenantId, metaDataEntry.getMetaData().getId());
							ps.executeUpdate();
						}
					}
				}

				//MetaData変換
				Blob blob = null;
				byte[] metaData = null;
				if (rdb.isSupportBlobType()) {
					blob = getConnection().createBlob();
					try (OutputStream os = blob.setBinaryStream(1L)) {
						Marshaller marshaller = context.createMarshaller();
						marshaller.marshal(new MetaDataEntryThinWrapper(metaDataEntry.getMetaData()), os);
					} catch (JAXBException e) {
						throw new SQLException("Marshalに失敗", e);
					} catch (IOException e) {
						throw new SQLException("Marshallに失敗", e);
					}
				} else {
					try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
						Marshaller marshaller = context.createMarshaller();
						marshaller.marshal(new MetaDataEntryThinWrapper(metaDataEntry.getMetaData()), os);
						metaData = os.toByteArray();
					} catch (JAXBException e) {
						throw new SQLException("Marshalに失敗", e);
					} catch (IOException e) {
						throw new SQLException("Marshallに失敗", e);
					}
				}

				// 最新データのインサート
				sql = insert.createStoreSQL(rdb, useObjDefNameAndType);
				try (PreparedStatement ps = getPreparedStatement(sql)) {
					int newVersion = 0;
					if (asVersion >= 0) {
						newVersion = asVersion;
					} else {
						if (info != null) {
							newVersion = info.getVersion() + 1;
						}
					}

					if (rdb.isSupportBlobType()) {
						insert.setStoreParameter(rdb, ps, tenantId, newVersion,
								metaDataEntry.getMetaData(), metaDataEntry.getPath(), blob,
								metaDataEntry.isSharable(), metaDataEntry.isDataSharable(),
								metaDataEntry.isPermissionSharable(), metaDataEntry.isOverwritable(), useObjDefNameAndType);
					} else {
						insert.setStoreParameter(rdb, ps, tenantId, newVersion,
								metaDataEntry.getMetaData(), metaDataEntry.getPath(), metaData,
								metaDataEntry.isSharable(), metaDataEntry.isDataSharable(),
								metaDataEntry.isPermissionSharable(), metaDataEntry.isOverwritable(), useObjDefNameAndType);
					}
					ps.executeUpdate();
				}

				if (blob != null) {
					blob.free();
				}
				return null;
			}
		};
		exec.execute(rdb, true);
	}

	@Override
	public void update(final int tenantId, final MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		storeImpl(tenantId, metaDataEntry, -1, true);
	}

	@Override
	public void remove(final int tenantId, final String path) throws MetaDataRuntimeException {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {

				MetaDataEntry meta = load(tenantId, path);
				if (meta == null) {
					throw new MetaDataRuntimeException("No metaData found.path=" + path);
				}

				// 現状データの論理削除
				String sql = update.createUpdateSQL(rdb);
				try (PreparedStatement ps = getPreparedStatement(sql)) {
					update.setUpdateParameter(rdb, ps, tenantId, meta.getMetaData().getId());
					int delCount = ps.executeUpdate();
					if (delCount < 1) {
						throw new SQLException("Delete faied. update count=" + delCount);
					}
				}
				return null;
			}
		};
		exec.execute(rdb, true);
	}

	@Override
	public void updateConfigById(final int tenantId, final String id, final MetaDataConfig config) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				String sql = updateConfig.createUpdateSQL(rdb);
				PreparedStatement ps = getPreparedStatement(sql);
				updateConfig.setUpdateParameter(rdb, ps, tenantId, id,
						config.isSharable(), config.isDataSharable(),
						config.isPermissionSharable(), config.isOverwritable());
				int ret = ps.executeUpdate();
				if (ret != 1) {
					throw new SQLException("updateConfig failed. id=" + id + ",upate counts=" + ret);
				}
				return null;
			}
		};
		exec.execute(rdb, true);
	}

	public List<Integer> getTenantIdsOf(String id) {
		SqlExecuter<List<Integer>> exec = new SqlExecuter<List<Integer>>() {
			@Override
			public List<Integer> logic() throws SQLException {
				String sql = select.createTenantIdListSQL();
				PreparedStatement ps = getPreparedStatement(sql);
				select.setTenantIdListParameter(rdb, ps, id);
				ResultSet rs = ps.executeQuery();
				try {
					return select.getTenantIdListResultData(rs);
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(rdb, true);
	}

	public void purgeById(final int tenantId, final String id) throws MetaDataRuntimeException {

		Transaction.required(t -> {
				SqlExecuter<Void> exec = new SqlExecuter<Void>() {
					@Override
					public Void logic() throws SQLException {

						String sql = delete.createPurgeByIdSQL();
						PreparedStatement ps = getPreparedStatement(sql);
						delete.setPurgeByIdParameter(rdb, ps, tenantId, id);

						int delCount = ps.executeUpdate();
						if (delCount <= 0) {
							logger.warn("purge meta data, but no record deleted. tenant id = " + tenantId + ",defId = " + id);
						}


						return null;
					}
				};
				exec.execute(rdb, true);
		});
	}

	@Override
	public List<MetaDataEntryInfo> getHistoryById(final int tenantId, final String metaDataId)
			throws MetaDataRuntimeException {
		SqlExecuter<List<MetaDataEntryInfo>> exec = new SqlExecuter<List<MetaDataEntryInfo>>() {
			@Override
			public List<MetaDataEntryInfo> logic() throws SQLException {
				// SQLの取得
				String sql = select.createMetaHistorySQL();
				PreparedStatement ps = getPreparedStatement(sql);
				// 条件設定
				select.setMetaHistoryParameter(rdb, ps, tenantId, metaDataId);
				//検索SQL実行
				ResultSet rs = ps.executeQuery();
				List<MetaDataEntryInfo> ret;
				try {
					ret = select.createNodeListResultData(rs, rdb);
				} finally {
					rs.close();
				}
				return ret;
			}
		};
		return exec.execute(rdb, true);
	}

}
