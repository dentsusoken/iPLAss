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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.auth.EntityAuthContext;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobHandler {
	private static final Logger logger = LoggerFactory.getLogger(LobHandler.class);

	private static HashMap<String, LobHandler> handlerMap;
	private static LobHandler defaultLobHandler;
	static {
		ServiceRegistry sr = ServiceRegistry.getRegistry();
		SessionService sessionService = sr.getService(SessionService.class);
		EntityService ehService = sr.getService(EntityService.class);
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		LobStoreService lobStoreService = sr.getService(LobStoreService.class);
		LobDao dao = lobStoreService.getLobDao();

		handlerMap = new HashMap<String, LobHandler>();
		Map<String, LobStore> lobStoreMap = lobStoreService.getLobStoreMap();
		for (Map.Entry<String, LobStore> e: lobStoreMap.entrySet()) {
			handlerMap.put(e.getKey(), new LobHandler(e.getValue(), dao, sessionService, ehService, em, lobStoreService.isManageLobSizeOnRdb()));
		}
		defaultLobHandler = new LobHandler(lobStoreService.getDefaultLobStore(), dao, sessionService, ehService, em, lobStoreService.isManageLobSizeOnRdb());
	}

	public static LobHandler getInstance(String lobStoreName) {
		LobHandler h = handlerMap.get(lobStoreName);
		if (h == null) {
			h = defaultLobHandler;
		}
		return h;
	}

	public static Map<String, LobHandler> getHandlerMap() {
		return new HashMap<String, LobHandler>(handlerMap);
	}

	private LobStore lobStore;
	private LobDao dao;
	private SessionService sessionService;
	private EntityService ehService;
	private EntityManager em;
	private boolean manageLobSizeOnRdb;


	public LobHandler(LobStore lobStore, LobDao dao, SessionService sessionService, EntityService ehService, EntityManager em, boolean manageLobSizeOnRdb) {
		this.lobStore = lobStore;
		this.dao = dao;
		this.sessionService = sessionService;
		this.ehService = ehService;
		this.em = em;
		this.manageLobSizeOnRdb = manageLobSizeOnRdb;
	}

	public boolean canAccess(Lob lob) {
		if (lob.getDefinitionId() == null) {//テンポラリの場合、自身のセッションのもののみ参照可能
			if (!sessionService.getSession(true).getId().equals(lob.getSessionId())) {
				return false;
			}
		} else {
			//当該Entityの項目の参照可否をチェック
			AuthContextHolder user = AuthContextHolder.getAuthContext();

			if (user.isPrivilegedExecution()) {
				if (logger.isDebugEnabled()) {
					logger.debug("privileged updateAll call. shrot cut auth check.");
				}
				return true;
			}

			String entityDefName = null;
			String propDefName = null;
			EntityHandler eh = ehService.getRuntimeById(lob.getDefinitionId());
			entityDefName = eh.getMetaData().getName();
			PropertyHandler ph = eh.getPropertyById(lob.getPropertyId(), EntityContext.getCurrentContext());
			propDefName = ph.getName();

			EntityPermission perm = new EntityPermission(entityDefName, EntityPermission.Action.REFERENCE);

			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			if (!eac.isPermit(perm, user)) {
				throw new NoPermissionException(resourceString("impl.lob.LobHandler.noPermission", EntityPermission.Action.REFERENCE.toString()));
			}
			EntityPropertyPermission propPerm = new EntityPropertyPermission(entityDefName, propDefName, EntityPropertyPermission.Action.REFERENCE);
			if (!eac.isPermit(propPerm, user)) {
				throw new NoPermissionException(resourceString("impl.lob.LobHandler.noPermission", EntityPropertyPermission.Action.REFERENCE.toString()));
			}
			//データ範囲のチェック
			if (eac.hasLimitCondition(perm, user)) {
				Query q = new Query().select(Entity.OID).from(entityDefName)
						.where(new Equals(Entity.OID, lob.getOid()));
				if (eh.isVersioned()) {
					q.versioned();
				}

				if (em.count(q) == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public Lob copyFor(long srcLobId, String defId, String propId, String oid, Long version) {

		Lob src = getBinaryData(srcLobId);
		if (src == null) {
			return null;
		}

		Lob copy = new Lob(src.getTenantId(), -1, src.getName(), src.getType(), defId, propId, oid, version, null, Lob.STATE_VALID, src.getLobDataId(), lobStore, dao, manageLobSizeOnRdb);
		copy = dao.create(copy, lobStore);
		//参照カウントアップ
		if (!dao.refCountUp(copy.getTenantId(), copy.getLobDataId(), 1)) {
			throw new EntityConcurrentUpdateException(resourceString("impl.lob.LobHandler.cantUpdate"));
		}

		return copy;
	}

	public Lob[] getBinaryReference(long[] lobId, EntityContext context) {
		return dao.search(ExecuteContext.getCurrentContext().getClientTenantId(), lobId, lobStore);
	}

	public Lob getBinaryData(long lobId) {
		return dao.load(ExecuteContext.getCurrentContext().getClientTenantId(), lobId, lobStore);
	}

	public Lob crateBinaryDataTemporary(String name, String type, String sessionId) {
		return dao.crateTemporary(ExecuteContext.getCurrentContext().getClientTenantId(), name, type, sessionId, lobStore);
	}

	public Lob createBinaryData(String name, String type, String defId, String propId, String oid, Long version) {
		return dao.create(ExecuteContext.getCurrentContext().getClientTenantId(), name, type, defId, propId, oid, version, lobStore);
	}

	public boolean markPersistenceBinaryData(long lobId, String sessionId,
			String defId, String propId, String oid, Long version) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		Lob bin = dao.loadWithLock(tenantId, lobId, null, null, null, null, null, lobStore);
		if (bin == null) {
			return false;
		}

//		//既に同一プロパティとして保存されている場合はOK
//		if (defId.equals(bin.getDefinitionId())
//				&& propId.equals(bin.getPropertyId())
//				&& oid.equals(bin.getOid())
//				&& version.equals(bin.getVersion())) {
//			return true;
//		}

		//当該セッションが保存したテンポラリLobのみ保存可能
		if (!sessionId.equals(bin.getSessionId())) {
			return false;
		}

		return dao.markPersistence(tenantId, lobId, defId, propId, oid, version);

	}

	public boolean updateBinaryDataInfo(long lobId, String name, String type) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		return dao.updateBinaryDataInfo(tenantId, lobId, name, type);
	}

	public void removeBinaryData(long lobId) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		Lob forLock = dao.loadWithLock(tenantId, lobId, null, null, null, null, null, lobStore);
		if (forLock != null) {
			dao.remove(tenantId, lobId);
			if (forLock.getLobDataId() != Lob.IS_NEW) {
				//ここでは、カウントダウンのみ。実テーブル削除はクリーンナップ処理で。
				dao.refCountUp(tenantId, forLock.getLobDataId(), -1);
			}
		}
	}

	//FIXME BinaryReferenceとBinaryDataの関係の再整理
	public BinaryReference toBinaryReference(Lob bin, EntityContext context) {

		if (bin.getDefinitionId() != null) {//既にEntityとして保存されている場合
			EntityHandler eh = context.getHandlerById(bin.getDefinitionId());
			String defName = eh.getMetaData().getName();
			String propName = null;
			for (PropertyHandler ph: eh.getPropertyList(context)) {
				if (ph.getId().equals(bin.getPropertyId())) {
					propName = ph.getName();
					break;
				}
			}
			return new BinaryReference(bin.getLobId(), bin.getName(), bin.getType(), bin.getSize(), defName, propName, bin.getOid());
		} else {//まだテンポラリの場合
			return new BinaryReference(bin.getLobId(), bin.getName(), bin.getType(), bin.getSize());
		}

	}

	/**
	 * テンポラリデータを削除します。
	 */
	public static void cleanTemporaryBinaryData() {

		//TODO 1件ずつのループだが、微妙か、、、削除対象を確定しなければならないので1回はlobIDをselectする必要あり。（sqlで、update<-selectだとファントムリードが発生するので。）
		final int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();

		LobStoreService lobStoreService = ServiceRegistry.getRegistry().getService(LobStoreService.class);
		final LobDao dao = lobStoreService.getLobDao();

		//保存日数以上経過しているテンポラリを取得
		final List<long[]> cleanupTarget = dao.getLobIdListForCleanTemporary(-1 * lobStoreService.getTemporaryKeepDay(), tenantId);

		int limit = lobStoreService.getCleanCommitLimit() > 0 ? lobStoreService.getCleanCommitLimit() : 100;
		for (int offset = 0; offset < cleanupTarget.size(); offset+=limit) {
			final int offsetFinal = offset;
			try {
				Transaction.requiresNew(t -> {
					int end = (offsetFinal + limit < cleanupTarget.size()) ? offsetFinal + limit: cleanupTarget.size();
					for (int i = offsetFinal; i < end; i++) {
						dao.remove(tenantId, cleanupTarget.get(i)[0]);
						if (cleanupTarget.get(i)[1] != Lob.IS_NEW) {
							//ここでは、カウントダウンのみ。実テーブル削除はクリーンナップ処理で。
							dao.refCountUp(tenantId, cleanupTarget.get(i)[1], -1);
						}
					};
				});
			} catch (RuntimeException e) {
				logger.error("crean up temporary lob process failed:offset=" + offset + ". try to cleanup next offset...", e);
			}
		}
	}

	/**
	 * 参照されていないLobデータを削除します。
	 */
	public static void cleanLobData() {

		final int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();

		LobStoreService lobStoreService = ServiceRegistry.getRegistry().getService(LobStoreService.class);
		final LobDao dao = lobStoreService.getLobDao();
		Map<String, LobStore> lobStoreMap = lobStoreService.getLobStoreMap();

		final List<Long> lobDataIdList = dao.getLobDataIdListForClean(-1 * lobStoreService.getInvalidKeepDay(), tenantId);

		int limit = lobStoreService.getCleanCommitLimit() > 0 ? lobStoreService.getCleanCommitLimit() : 100;
		for (int offset = 0; offset < lobDataIdList.size(); offset+=limit) {
			final int offsetFinal = offset;
			try {
				Transaction.requiresNew(t -> {
						int end = (offsetFinal + limit < lobDataIdList.size()) ? offsetFinal + limit: lobDataIdList.size();
						for (int i = offsetFinal; i < end; i++) {
							Long lobDataId = lobDataIdList.get(i);
							dao.removeData(tenantId, lobDataId);
							for (Map.Entry<String, LobStore> le: lobStoreMap.entrySet()) {
								le.getValue().remove(tenantId, lobDataId);
							}
						}
						return null;
				});

			} catch (RuntimeException e) {
				logger.error("crean up lobData process failed:offset=" + offset + ". try to cleanup next offset...", e);
			}
		}

	}

	public void cleanLobDataImmediately(int tenantId, long lobDataId) {
		dao.removeData(tenantId, lobDataId);
		lobStore.remove(tenantId, lobDataId);
	}

	public void removeBinaryDataByRbid(long rbid) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		List<Long> lobIdList = dao.getLobIdByRbid(tenantId, rbid);
		for (Long lobId: lobIdList) {
			removeBinaryData(lobId);
		}
		dao.removeFromRecycleBin(ExecuteContext.getCurrentContext().getClientTenantId(), rbid);
	}

	public int removeBinaryDataByDefId(int tenantId, String defId) {
		//TODO cleanTemporaryBinaryDataの場合、ファントムリードを意識して１件ずつ削除しているが、defIdレベルでの場合、update<-select でいいかも

		//DefIdに紐づくLobIdを取得
		final List<Long> markTarget = dao.getLobIdByDefId(tenantId, defId);

		int allCount = 0;

		//TODO とりあえず、100件単位でcommit。パラメータ化。
		for (int offset = 0; offset < markTarget.size(); offset+=100) {
			final int offsetFinal = offset;
			try {
				allCount += Transaction.requiresNew(t -> {
						int count = 0;
						for (int i = offsetFinal; i < markTarget.size(); i++) {
							removeBinaryData(markTarget.get(i));
							count++;
						}
						return count;
				});
			} catch (RuntimeException e) {
				//残っても問題は起こらないので、ログのみ出力
				logger.error("mark for clean lob process failed:defId=" + defId + ",offset=" + offset + ". try to mark next offset...", e);
			}
		}

		return allCount;
	}

	/**
	 * Entity定義として参照されていないバイナリデータを削除します。
	 *
	 * @param eh 対象Entity
	 * @return 更新件数
	 */
	public int removeBinaryDataForDefrag(int tenantId, EntityHandler eh) {

		//DefIdに紐づくLobIdを取得
		final List<Long> markTarget = dao.getLobIdForDefrag(tenantId, eh);

		int allCount = 0;

		//TODO とりあえず、100件単位でcommit。パラメータ化。
		for (int offset = 0; offset < markTarget.size(); offset+=100) {
			final int offsetFinal = offset;
			try {
				allCount += Transaction.requiresNew(t -> {
						int count = 0;
						for (int i = offsetFinal; i < markTarget.size(); i++) {
							removeBinaryData(markTarget.get(i));
							count++;
						}
						return count;
				});
			} catch (RuntimeException e) {
				//残っても問題は起こらないので、ログのみ出力
				logger.error("mark for clean lob process failed:defId=" + eh.getMetaData().getId() + ",offset=" + offset + ". try to mark next offset...", e);
			}
		}
		return allCount;
	}

	public void markToRecycleBin(long lobId, long rbid) {
		dao.markToRecycleBin(ExecuteContext.getCurrentContext().getClientTenantId(), lobId, rbid);
	}

	public void markRestoreFromRecycleBin(long rbid) {
		dao.markRestoreFromRecycleBin(ExecuteContext.getCurrentContext().getClientTenantId(), rbid);
	}

	/**
	 * LobStoreのLobサイズを更新します。
	 *
	 */
	public long updateLobStoreSize() {

		final int LIMIT = 1000;

		final int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();

		long allCount = 0;

		final List<Long> lobDataIdList = dao.getLobDataIdListForLobStoreSizeUpdate(tenantId);

		//TODO commit分割。パラメータ化。
		for (int offset = 0; offset < lobDataIdList.size(); offset+=LIMIT) {
			final int offsetFinal = offset;
			try {
				allCount += Transaction.requiresNew(t -> {
						int updateCount = 0;
						int limit = Math.min(offsetFinal + LIMIT, lobDataIdList.size());
						for (int i = offsetFinal; i < limit; i++) {
							long lobDataId = lobDataIdList.get(i);

							LobData lobData = lobStore.load(tenantId, lobDataId);
							//LobStoreテーブルベースでdataIDを取得しているので別のLobStoreのIDである可能性がある
							//このため、nullチェックかつサイズチェックを実施
							//(開発環境などでは、テナントごとにLobStoreが異なることもあるため存在しない可能性あり)
							//loadでは存在チェックを行なっていないので、LobDataがnullということはないが念のためチェック。
							//存在チェックはサイズでチェック(存在しないと0)
							if (lobData != null && lobData.getSize() > 0) {
								dao.updateLobStoreSize(tenantId, lobDataId, lobData.getSize());
								updateCount++;
							}
						}
						return updateCount;
				});

			} catch (RuntimeException e) {
				logger.error("update size of lob store data process failed.", e);
				throw e;
			}

		}

		return allCount;
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
