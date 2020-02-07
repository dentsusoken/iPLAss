/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.FulltextSearchOption;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.entity.auditlog.AuditLogInterceptor;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * <p>AdminConsole用EntityManager</p>
 * <p>AdminConsole独自でAuditLogInterceptorとほぼ同等の操作ログを出力する。
 * 出力レベルのカスタマイズは、service-configにて、AdminAuditLoggingServiceに対して設定する。</p>
 *
 * @see AdminAuditLoggingService
 * @see AuditLogInterceptor
 *
 */
public class AdminEntityManager implements EntityManager {

	//FIXME EntityExplorerでのImportによるデータ登録処理などは、toolsで実行されているので、通常のAuditLogとして出力される。
	//FIXME カテゴリとして「mtp.audit.admin.entity」とする必要があるか？

	private EntityManager em;
	private AdminAuditLoggingService aals;

	private AdminEntityManager() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
	}

	public static EntityManager getInstance() {
		return new AdminEntityManager();
	}

	@Override
	public ValidateResult validate(Entity entity) {
		return em.validate(entity);
	}

	@Override
	public ValidateResult validate(Entity entity, List<String> properties) {
		return em.validate(entity, properties);
	}

	@Override
	public <T extends Entity> SearchResult<T> searchEntity(Query cond) {
		aals.logQuery(cond, false);
		return em.searchEntity(cond);
	}

	@Override
	public <T extends Entity> SearchResult<T> searchEntity(Query cond, SearchOption option) {
		aals.logQuery(cond, false);
		return em.searchEntity(cond, option);
	}

	@Override
	public <T extends Entity> void searchEntity(Query cond, Predicate<T> callback) {
		aals.logQuery(cond, false);
		em.searchEntity(cond, callback);
	}

	@Override
	public SearchResult<Object[]> search(Query cond) {
		aals.logQuery(cond, false);
		return em.search(cond);
	}

	@Override
	public SearchResult<Object[]> search(Query cond, SearchOption option) {
		aals.logQuery(cond, false);
		return em.search(cond, option);
	}

	@Override
	public void search(Query cond, Predicate<Object[]> callback) {
		aals.logQuery(cond, false);
		em.search(cond, callback);
	}

	@Override
	public <T extends Entity> void searchEntity(Query query, SearchOption option, Predicate<T> callback) {
		aals.logQuery(query, false);
		em.searchEntity(query, option, callback);
	}

	@Override
	public void search(Query query, SearchOption option, Predicate<Object[]> callback) {
		aals.logQuery(query, false);
		em.search(query, option, callback);
	}

	@Override
	public int count(Query cond) {
		aals.logQuery(cond, true);
		return em.count(cond);
	}

	@Override
	public Entity load(String oid, String definitionName) {
		aals.logLoad(oid, null, definitionName, null);
		return em.load(oid, definitionName);
	}

	@Override
	public Entity load(String oid, Long version, String definitionName) {
		aals.logLoad(oid, version, definitionName, null);
		return em.load(oid, version, definitionName);
	}

	@Override
	public Entity load(String oid, String definitionName, LoadOption option) {
		aals.logLoad(oid, null, definitionName, option);
		return em.load(oid, definitionName, option);
	}

	@Override
	public Entity load(String oid, Long version, String definitionName, LoadOption option) {
		aals.logLoad(oid, version, definitionName, option);
		return em.load(oid, version, definitionName, option);
	}

	@Override
	public Entity loadAndLock(String oid, String definitionName) {
		aals.logLoad(oid, null, definitionName, null);
		return em.loadAndLock(oid, definitionName);
	}

	@Override
	public Entity loadAndLock(String oid, String definitionName, LoadOption option) {
		aals.logLoad(oid, null, definitionName, option);
		return em.loadAndLock(oid, definitionName, option);
	}

	@Override
	public String insert(Entity entity) {
		String oid = em.insert(entity);
		aals.logInsert(entity);
		return oid;
	}

	@Override
	public String insert(Entity entity, InsertOption option) {
		String oid = em.insert(entity, option);
		aals.logInsert(entity);
		return oid;
	}

	@Override
	public void update(Entity entity, UpdateOption option) {
		em.update(entity, option);
		aals.logUpdate(null, entity, option);
	}

	@Override
	public void delete(Entity entity, DeleteOption option) {
		em.delete(entity, option);
		aals.logDelete(entity);
	}

	@Override
	public int updateAll(UpdateCondition cond) {
		int count = em.updateAll(cond);
		aals.logUpdateAll(cond);
		return count;
	}

	@Override
	public int deleteAll(DeleteCondition cond) {
		int count = em.deleteAll(cond);
		aals.logDeleteAll(cond);
		return count;
	}

	@Override
	public void bulkUpdate(BulkUpdatable bulkUpdatable) {
		em.bulkUpdate(bulkUpdatable);
		aals.logBulkUpdate(bulkUpdatable.getDefinitionName());
	}

	@Override
	public BinaryReference loadBinaryReference(long lobId) {
		return em.loadBinaryReference(lobId);
	}

	@Override
	public BinaryReference createBinaryReference(String name, String type, InputStream is) {
		return em.createBinaryReference(name, type, is);
	}

	@Override
	public BinaryReference createBinaryReference(File file, String name, String type) {
		return em.createBinaryReference(file, name, type);
	}

	@Override
	public InputStream getInputStream(BinaryReference binaryReference) {
		return em.getInputStream(binaryReference);
	}

	@Override
	public OutputStream getOutputStream(BinaryReference binaryReference) {
		return em.getOutputStream(binaryReference);
	}

	@Override
	public boolean lockByUser(String oid, String definitionName) {
		return em.lockByUser(oid, definitionName);
	}

	@Override
	public boolean unlockByUser(String oid, String definitionName) {
		return em.unlockByUser(oid, definitionName);
	}

	@Override
	public void purge(long recycleBinId, String definitionName) {
		em.purge(recycleBinId, definitionName);
		aals.logPurge(recycleBinId);
	}

	@Override
	public Entity restore(long recycleBinId, String definitionName) {
		Entity entity = em.restore(recycleBinId, definitionName);
		aals.logRestore(entity.getOid(), entity.getDefinitionName(), recycleBinId);
		return entity;
	}

	@Override
	public void getRecycleBin(String definitionName, Predicate<Entity> callback) {
		em.getRecycleBin(definitionName, callback);
	}

	@Override
	public Entity getRecycleBin(long recycleBinId, String definitionName) {
		return em.getRecycleBin(recycleBinId, definitionName);
	}

	@Override
	public Timestamp getCurrentTimestamp() {
		return em.getCurrentTimestamp();
	}

	@Override
	public Entity deepCopy(String oid, String definitionName) {
		return em.deepCopy(oid, definitionName);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String definitionName, String keyword) {
		return em.fulltextSearchEntity(definitionName, keyword);
	}

	@Override
	public List<String> fulltextSearchOidList(String definitionName, String keyword) {
		return em.fulltextSearchOidList(definitionName, keyword);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Query query, String keyword, SearchOption option) {
		return em.fulltextSearchEntity(query, keyword, option);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(
			Map<String, List<String>> entityProperties, String keyword) {
		return em.fulltextSearchEntity(entityProperties, keyword);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String keyword, FulltextSearchOption option) {
		return em.fulltextSearchEntity(keyword, option);
	}

}
