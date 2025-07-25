/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.strategy;

import java.sql.Timestamp;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;

public interface EntityStoreStrategy extends StoreStrategy {

	public int count(EntityContext context, Query query);
	public void delete(EntityContext context, Entity model, EntityHandler handler, DeleteOption option);
	public String insert(EntityContext context, EntityHandler handler, Entity model);
	public SearchResultIterator search(EntityContext context, Query query, EntityHandler handler);
	public void update(EntityContext context, EntityHandler handler, Entity model, UpdateOption option);
	public int updateAll(UpdateCondition cond, EntityContext entityContext, EntityHandler handler, String clientId);
	public int deleteAll(DeleteCondition cond, EntityContext entityContext, EntityHandler handler, String clientId);
	public boolean lock(EntityContext context, EntityHandler handler, String oid);
//	public boolean lock(EntityContext context, EntityHandler handler, Condition condition);

	public String newOid(EntityContext context, EntityHandler handler);

	//ごみ箱関連(oid単位（複数バージョン一括）での操作とする)
	public Long copyToRecycleBin(EntityContext context, EntityHandler handler, String oid, String clientId);
	public void copyFromRecycleBin(EntityContext context, EntityHandler handler, Long rbid, String clientId);
	public void deleteFromRecycleBin(EntityContext context, EntityHandler handler, Long rbid, String clientId);
	public RecycleBinIterator getRecycleBin(EntityContext context, EntityHandler handler, Long rbid);
	public int countRecycleBin(EntityContext context, EntityHandler handler, Timestamp ts);
	public void bulkUpdate(BulkUpdatable bulkUpdatable, EntityContext entityContext, EntityHandler entityHandler, String clientId);

	/**
	 * <p>
	 * 指定されたEntityデータを全て物理削除します。
	 * </p>
	 * <p>
	 * 違うPathに同一IDのメタデータをインポートした場合などに、すでに登録されているEntityデータをクリアするために利用します。
	 * データは全て物理削除します。
	 * </p>
	 *
	 * @deprecated 現状使っていないので将来的に削除。purgeByIdを利用
	 * @param context
	 * @param handler
	 */
	@Deprecated
	public void clean(EntityContext context, EntityHandler handler);

	/**
	 * <p>
	 * 指定されたEntityデータを全て物理削除します。
	 * </p>
	 * <p>
	 * 無効化されたEntity定義のデータなどを含め、指定されたIDに紐づくEntityデータを全て物理削除します。
	 * メンテナンス用です。
	 * </p>
	 *
	 * @param id Entity定義ID
	 */
	public void purgeById(EntityContext context, String defId);

	/**
	 * <p>
	 * 指定されたEntityデータに対してデフラグ処理を実行します。
	 * </p>
	 * <p>
	 * EntityのProperty定義を変更することで未使用化されたデータ領域のデータを物理削除します。
	 * メンテナンス用です。
	 * </p>
	 * <b>※デフラグ処理で、メタデータの変更の可能性もあるので、ApplyMetaDataStorategy#defrag -> #defragDataの順で呼び出される</b>
	 * 
	 *
	 * @param context
	 * @param handler
	 */
	public void defragData(EntityContext context, EntityHandler handler);
	

}
