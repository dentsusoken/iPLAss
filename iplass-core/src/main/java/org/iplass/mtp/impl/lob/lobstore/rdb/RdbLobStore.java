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

package org.iplass.mtp.impl.lob.lobstore.rdb;

import org.iplass.mtp.impl.lob.LobStoreService;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.lob.lobstore.LobValidator;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;

public class RdbLobStore implements LobStore {

	private RdbAdapter rdb;
	private boolean manageLobSizeOnRdb;
	
	private LobValidator lobValidator;

	@Override
	public LobValidator getLobValidator() {
		return lobValidator;
	}

	public void setLobValidator(LobValidator lobValidator) {
		this.lobValidator = lobValidator;
	}

	@Override
	public void inited(LobStoreService service, Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		manageLobSizeOnRdb = service.isManageLobSizeOnRdb();
	}

	@Override
	public void destroyed() {
	}

	@Override
	public LobData create(final int tenantId, final long lobId) {

		return new RdbLobData(tenantId, lobId, manageLobSizeOnRdb, rdb);

		//呼び出し元でレコードが作成されているはず（参照カウントを管理するテーブルと統合してるので）。
//		SqlExecuter<LobData> exec = new SqlExecuter<LobData>() {
//
//			@Override
//			public LobData logic() throws SQLException {
//
//				getStatement().executeUpdate(insertSql.toSql(tenantId, lobId, rdb));
//				return new RdbLobData(tenantId, lobId, rdb);
//			}
//		};
//		return exec.execute(rdb, true);
	}

	@Override
	public LobData load(int tenantId, long lobId) {
		//基本的に存在しているLobしか呼ばれないはず。
		return new RdbLobData(tenantId, lobId, manageLobSizeOnRdb, rdb);
	}

	@Override
	public void remove(final int tenantId, final long lobId) {
		//呼び出し側での削除と同時に削除される
	}

}
