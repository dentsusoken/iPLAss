/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.auth.authenticate.token.sql.AuthTokenSelectSQL;
import org.iplass.mtp.impl.auth.authenticate.token.sql.AuthTokenUpdateSQL;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.ServiceRegistry;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

public class RdbAuthTokenStore implements AuthTokenStore {
	
	private AuthTokenSelectSQL selectSql;
	private AuthTokenUpdateSQL updateSql;
	private RdbAdapter rdb;
	
	private ObjectMapper objectMapper;

	public RdbAuthTokenStore() {
		rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
		selectSql = rdb.getQuerySqlCreator(AuthTokenSelectSQL.class);
		updateSql = rdb.getUpdateSqlCreator(AuthTokenUpdateSQL.class);
		objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE, As.PROPERTY);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Override
	public void create(final AuthToken token) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(updateSql.createInsertSQL());
				try {
					updateSql.setInsertParameter(rdb, ps, token, objectMapper);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
				int cnt = ps.executeUpdate();
				if (cnt != 1) {
					throw new RuntimeException("fail to insert RememberMeToken:" + token.getSeries());
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void update(AuthToken newToken, AuthToken currentToken) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(updateSql.createUpdateStrictSQL());
				try {
					updateSql.setUpdateStrictParameter(rdb, ps, newToken, currentToken, objectMapper);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
				int cnt = ps.executeUpdate();
				if (cnt != 1) {
					throw new RuntimeException("fail to update RememberMeToken:" + newToken.getSeries());
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void delete(final int tenantId, final String type, final String uniqueKey) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(updateSql.createDeleteSQL());
				updateSql.setDeleteParameter(rdb, ps, tenantId, type, uniqueKey);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void deleteByDate(final int tenantId, final String type, final Timestamp ts) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(updateSql.createDeleteByDateSQL());
				updateSql.setDeleteByDateParameter(rdb, ps, tenantId, type, ts);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public AuthToken getBySeries(final int tenantId, final String type, final String series) {
		SqlExecuter<AuthToken> executer = new SqlExecuter<AuthToken>() {
			@Override
			public AuthToken logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(selectSql.createSelectSQL());
				selectSql.setSelectParameter(rdb, ps, tenantId, type, series);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return selectSql.toAuthToken(rs, objectMapper);
					} else {
						return null;
					}
				}
			}
		};
		return executer.execute(rdb, true);
	}

	@Override
	public void deleteBySeries(int tenantId, final String type, String series) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(updateSql.createDeleteBySeriesSQL());
				updateSql.setDeleteBySeriesParameter(rdb, ps, tenantId, type, series);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public List<AuthToken> getByUser(int tenantId, String userUniqueKey) {
		SqlExecuter<List<AuthToken>> executer = new SqlExecuter<List<AuthToken>>() {
			@Override
			public List<AuthToken> logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(selectSql.createSelectByUserUniqueKeySQL());
				selectSql.setSelectByUserUniqueKeyParameter(rdb, ps, tenantId, userUniqueKey);
				List<AuthToken> res = new ArrayList<>();
				
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						res.add(selectSql.toAuthToken(rs, objectMapper));
					}
				}
				return res;
			}
		};
		return executer.execute(rdb, true);
	}

}
