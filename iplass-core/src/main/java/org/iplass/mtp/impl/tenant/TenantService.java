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

package org.iplass.mtp.impl.tenant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.tenant.sql.TenantControlSQL;
import org.iplass.mtp.impl.tenant.sql.TenantSelectSQL;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

/**
 * テナント情報管理サービス
 *
 * @author 片野　博之
 *
 */
public class TenantService implements Service {
//	private static final Logger log = LoggerFactory.getLogger(TenantService.class);

	//FIXME TenantServiceとMetaTenantServiceの統合

	private RdbAdapter rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
	/** テナント検索SQL */
	private TenantSelectSQL tenantSelect;
	/** テナント登録・更新用SQL */
	private TenantControlSQL tenantControl;

	@Override
	public void init(Config config) {
		tenantSelect = rdb.getQuerySqlCreator(TenantSelectSQL.class);
		tenantControl = rdb.getUpdateSqlCreator(TenantControlSQL.class);
	}

	@Override
	public void destroy() {
	}

	/**
	 * テナント情報を取得する。（テナントURL指定）
	 *
	 * @param url
	 *            テナントURL
	 * @return テナント情報（存在しない場合はNull）
	 */
	public Tenant getTenant(final String url) {
		SqlExecuter<Tenant> exec = new SqlExecuter<Tenant>() {
			@Override
			public Tenant logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(tenantSelect.createSQL(url));
				tenantSelect.setParameter(rdb, ps, url);
				ResultSet rs = ps.executeQuery();
				try {
					return tenantSelect.createTenant(rs);
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(rdb, true);
	}

	/**
	 * テナント情報を取得する。（ID指定）
	 *
	 * @param id
	 *            テナントID
	 * @return テナント情報（存在しない場合はNull）
	 */
	public Tenant getTenant(final int id) {
		SqlExecuter<Tenant> exec = new SqlExecuter<Tenant>() {
			@Override
			public Tenant logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(tenantSelect.createSQL());
				tenantSelect.setParameter(rdb, ps, id);
				ResultSet rs = ps.executeQuery();
				try {
					return tenantSelect.createTenant(rs);
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(rdb, true);
	}

	/**
	 * テナントを作成する。<br>
	 *
	 * @param tenant
	 *            登録するテナント情報
	 */
	public void registTenant(final Tenant tenant, final String registId) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(tenantControl.createRegistSQL(rdb));
				tenantControl.setRegistParameter(rdb, ps, tenant, registId);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	/**
	 * テナントを更新する。<br>
	 * 基本情報は更新できない(ID,HostName,URLは変更できない。)
	 *
	 * @param tenant テナント情報
	 */
	public void updateTenant(final Tenant tenant) {
		updateTenant(tenant, ExecuteContext.getCurrentContext().getClientId());
	}

	public void updateTenant(Tenant tenant, String updateId) {
		updateTenant(tenant, updateId, false);
	}
	/**
	 * テナントを更新する。<br>
	 * 基本情報は更新できない(ID,HostName,URLは変更できない。)
	 *
	 * @param tenant
	 *            テナント情報
	 */
	public void updateTenant(final Tenant tenant, final String updateId, final boolean forceUpdate) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(tenantControl.createUpdateSQL(rdb, forceUpdate));
				tenantControl.setUpdateParameter(rdb, ps, tenant, updateId, forceUpdate);
				int cnt = ps.executeUpdate();
				if (cnt == 0) {
					// FIXME 例外処理
					throw new RuntimeException("すでに更新されているか、削除されている");
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	public List<Integer> getAllTenantIdList() {
		SqlExecuter<List<Integer>> exec = new SqlExecuter<List<Integer>>() {
			@Override
			public List<Integer> logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(tenantSelect.createAllTenantIdListSQL());
				tenantSelect.setAllTenantIdListParameter(rdb, ps);
				ResultSet rs = ps.executeQuery();
				try {
					return tenantSelect.getAllTenantIdList(rs);
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(rdb, true);
	}
}
