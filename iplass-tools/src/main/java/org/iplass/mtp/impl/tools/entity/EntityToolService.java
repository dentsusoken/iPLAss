/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.SearchResult.ResultMode;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.internal.InternalCredential;
import org.iplass.mtp.impl.entity.csv.QueryCsvWriter;
import org.iplass.mtp.impl.entity.csv.QueryWriteOption;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;

public class EntityToolService implements Service {

	private AuthService as = null;

	@Override
	public void init(Config config) {
		as = ServiceRegistry.getRegistry().getService(AuthService.class);
	}

	@Override
	public void destroy() {
	}

	public void createJavaMappingClass(File file, EntityDefinition definition, String basePackage) {
		Path path = Paths.get(file.getPath().substring(0, file.getPath().length() - file.getName().length()));
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new EntityToolRuntimeException(getRS("createDirectoriesError"), e);
			}
		}

		String directClassName = StringUtil.isNotBlank(basePackage) ? basePackage + '.' : "" + definition.getName();
		try (OutputStream os = new FileOutputStream(file);
			EntityJavaMappingClassWriter writer = new EntityJavaMappingClassWriter(os, definition, directClassName);
		) {
			writer.writeJavaClass();
		} catch (IOException e) {
			throw new EntityToolRuntimeException(getRS("unexpectedError"), e);
		}
	}

	public int executeEQL(String eql, boolean isSearchAllVersion, boolean isCount) {
		Query query = new PreparedQuery(eql).query(null);
		if (!query.isVersiond() && isSearchAllVersion) {
			query.setVersiond(true);
		}

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

		int count = 0;
		try (SearchResult<Object[]> result = em.search(query, new SearchOption(ResultMode.STREAM).unnotifyListeners())) {
			if (!isCount) {
				result.getFirst();
				return -1;
			}
			for (Object[] row : result) {
				if (row != null) {
					count++;
				}
			}
		}
		return count;
	}

	public int executeEQL(OutputStream out, String charset, String eql, boolean isSearchAllVersion) {
		Query query = null;

		try {
			query = new PreparedQuery(eql).query(null);
		} catch (QueryException e) {
			throw new EntityToolRuntimeException(getRS("invalidEQLStatement", eql), e);
		}

		if (!query.isVersiond() && isSearchAllVersion) {
			query.setVersiond(true);
		}

		try (QueryCsvWriter writer = new QueryCsvWriter(out, query, new QueryWriteOption().charset(charset))) {
			return writer.write();
		} catch (IOException e) {
			throw new EntityToolRuntimeException(getRS("errorOutputSearchResult"), e);
		}
	}

	public int executeEQLWithAuth(String eql, boolean isSearchAllVersion, boolean isCount) {
		return as.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () -> {
			return executeEQL(eql, isSearchAllVersion, isCount);
		}).intValue();
	}

	public int executeEQLWithAuth(OutputStream out, String charset, String eql, boolean isSearchAllVersion) {
		return as.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () -> {
			return executeEQL(out, charset, eql, isSearchAllVersion);
		}).intValue();
	}

	public int executeEQLWithAuth(String eql, boolean isSearchAllVersion, boolean isCount, String userId, String password) {
		try {
			as.login(StringUtil.isNotEmpty(password) ? new IdPasswordCredential(userId, password) : new InternalCredential(userId));
			return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
				return executeEQL(eql, isSearchAllVersion, isCount);
			}).intValue();
		} finally {
			as.logout();
		}
	}

	public int executeEQLWithAuth(OutputStream out, String charset, String eql, boolean isSearchAllVersion, String userId, String password) {
		try {
			as.login(StringUtil.isNotEmpty(password) ? new IdPasswordCredential(userId, password) : new InternalCredential(userId));
			return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
				return executeEQL(out, charset, eql, isSearchAllVersion);
			}).intValue();
		} finally {
			as.logout();
		}
	}

	private String getRS(String suffix, Object... arguments) {
		return ToolsResourceBundleUtil.resourceString("entitytool." + suffix, arguments);
	}

}
