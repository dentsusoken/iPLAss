/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

import static java.nio.file.StandardOpenOption.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlConverter;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.ServiceRegistry;

public class EntityViewDDLWriter implements Closeable {

	private static final String DEFAULT_LOB_ID_SUFFIX = "_LOBID";

	private RdbAdapter rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();

	// 作成した短縮名と最大個数を保持(重複チェック及び連番生成用)
	private Map<String, Integer> shrinkedEntityNameMap = new HashMap<>();

	// Binary及びLongTextのLobIDカラム名のサフィックス
	private String lobIdSuffix = DEFAULT_LOB_ID_SUFFIX;

	private Writer writer;

	/**
	 * コンストラクタ
	 * 
	 * @param path ファイルパス
	 * @throws IOException I/O例外
	 */
	public EntityViewDDLWriter(Path path) throws IOException {
		this.writer = Files.newBufferedWriter(path, CREATE, TRUNCATE_EXISTING, WRITE);
	}

	/**
	 * Binary及びLongTextのLobIDカラム名のサフィックスを設定します。
	 * 
	 * @param suffix Binary及びLongTextのLobIDカラム名のサフィックス
	 */
	public void setLobIdSuffix(String suffix) {
		this.lobIdSuffix = suffix;
	}

	@Override
	public void close() throws IOException {
		if (writer != null) {
			writer.flush();
			writer.close();
			writer = null;
		}
	}

	/**
	 * DDL出力処理
	 * 
	 * @param definitions EntityDefinition
	 * @throws IOException I/O例外
	 */
	public void write(EntityDefinition... definitions) throws IOException {
		for (EntityDefinition ed : definitions) {
			// EntityビューテーブルDDL出力
			writeEntityViewDDL(ed);
			// Referenceビューテーブル(中間ビューテーブル)出力
			writeReferenceViewDDL(ed);
		}
	}

	private String shrinkEntityPath(String path, int index) {
		String[] splitedPaths = path.split("\\.");
		if (splitedPaths[index] != null && !splitedPaths[index].isEmpty()) {
			splitedPaths[index] = splitedPaths[index].substring(0, 1);
		}
		return String.join(".", splitedPaths);
	}

	private String joinEntityPathName(String path, String name) {
		return path != null && !path.isEmpty() ? path + "." + name : name;
	}

	private String shrinkEntityName(String entityName) {
		int maxLength = rdb.getMaxViewNameLength();
		if (maxLength < 0) return entityName;

		String shrinkedEntityName = entityName;

		if (entityName.length() > maxLength) {
			String path = null;
			String name = null;

			int pathIndex = entityName.lastIndexOf('.');

			if (pathIndex != -1) {
				path = entityName.substring(0, pathIndex);
				name = entityName.substring(pathIndex + 1);
			} else {
				name = entityName;
			}

			if (path != null && !path.isEmpty()) {
				int index = 0;
				int pathSize = path.split("\\.").length;
				while (shrinkedEntityName.length() > maxLength) {
					if (index >= pathSize) {
						shrinkedEntityName = shrinkedEntityName.substring(0, maxLength);
						break;
					}
					path = shrinkEntityPath(path, index);
					shrinkedEntityName = joinEntityPathName(path, name);
					index++;
				}
			} else {
				shrinkedEntityName = name.substring(0, maxLength);
			}
		}

		if (shrinkedEntityNameMap.containsKey(shrinkedEntityName)) {
			int cnt = shrinkedEntityNameMap.get(shrinkedEntityName).intValue() + 1;
			shrinkedEntityNameMap.put(shrinkedEntityName, Integer.valueOf(cnt));
			String suffix = String.format("#%d", cnt);
			shrinkedEntityName = shrinkedEntityName.substring(0, shrinkedEntityName.length() - suffix.length()) + suffix;
		} else {
			shrinkedEntityNameMap.put(shrinkedEntityName, Integer.valueOf(1));
		}

		return shrinkedEntityName;
	}

	private String toViewName(String entityName) {
		return shrinkEntityName(entityName).replaceAll("\\.", "_");
	}

	private String toReferenceViewName(String entityName, String refColName) {
		return toViewName(String.format("%s$%s", entityName, refColName));
	}

	private void addColumn(StringBuilder sb, String column) {
		if(sb.length() > 0) {
			sb.append(",");
		}
		sb.append(column);
	}

	private String createViewColumn(int colNo, String colName, PropertyDefinitionType type) {
		switch (type) {
		case BINARY:
			return rdb.createBinaryViewColumnSql(colNo, colName, lobIdSuffix);
		case LONGTEXT:
			return rdb.createLongTextViewColumnSql(colNo, colName, lobIdSuffix);
		default:
			return rdb.createViewColumnSql(colNo, colName);
		}
	}

	private String toCreateViewDDL(String entityName, String columnSql, String selectSql) {
		String sql = String.format("SELECT %s FROM (%s) %s", columnSql, selectSql, rdb.getViewSubQueryAlias());
		return rdb.toCreateViewDDL(toViewName(entityName), sql, true);
	}

	private String generateEntityViewDDL(EntityDefinition ed) {
		StringBuilder sbColumn = new StringBuilder();
		StringBuilder sbProperty = new StringBuilder();

		int colNo = 1;
		for (PropertyDefinition pd : ed.getPropertyList()) {
			int multiplicity = pd.getMultiplicity();

			switch (pd.getType()) {
			case EXPRESSION:
				// Expression型は未対応のためNULL値固定出力とする
				addColumn(sbProperty, "null");
				addColumn(sbColumn, createViewColumn(colNo, pd.getName(), pd.getType()));
				colNo++;
				break;
			case REFERENCE:
				// Reference型は中間ビューテーブルのため出力対象外
				break;
			default:
				addColumn(sbProperty, pd.getName());
				if (multiplicity > 1) {
					for (int i = 0; i < multiplicity; i++) {
						addColumn(sbColumn, createViewColumn(colNo, String.format("%s_%d", pd.getName(), i + 1), pd.getType()));
						colNo++;
					}
				} else {
					addColumn(sbColumn, createViewColumn(colNo, pd.getName(), pd.getType()));
					colNo += multiplicity;
				}
				break;
			}
		};

		EntityContext ec = EntityContext.getCurrentContext();
		SqlQueryContext sqc = new SqlQueryContext(ec.getHandlerByName(ed.getName()), ec, rdb);
		SqlConverter sc = new SqlConverter(sqc, false);

		String eql = String.format("SELECT %s FROM %s", sbProperty.toString(), ed.getName());
		Query.newQuery(eql).accept(sc);

		return toCreateViewDDL(ed.getName(), sbColumn.toString(), sqc.toSelectSql());
	}

	private void writeEntityViewDDL(EntityDefinition ed) throws IOException {
		String ddl = generateEntityViewDDL(ed);

		// コメント出力
		writer.write(String.format("/* drop/create %s */\n", ed.getName()));

		// DDL出力
		writer.write(ddl);
	}

	private String generateReferenceViewDDL(EntityDefinition ed, PropertyDefinition pd) {
		String eql = String.format("SELECT oid, version, %s.oid, %s.version FROM %s", pd.getName(), pd.getName(), ed.getName());

		EntityContext ec = EntityContext.getCurrentContext();
		SqlQueryContext sqc = new SqlQueryContext(ec.getHandlerByName(ed.getName()), ec, rdb);
		SqlConverter sc = new SqlConverter(sqc, false);

		Query.newQuery(eql).accept(sc);

		StringBuilder sbCol = new StringBuilder();
		addColumn(sbCol, createViewColumn(1, "oid", PropertyDefinitionType.STRING));
		addColumn(sbCol, createViewColumn(2, "version", PropertyDefinitionType.INTEGER));
		addColumn(sbCol, createViewColumn(3, pd.getName() + "_oid", PropertyDefinitionType.STRING));
		addColumn(sbCol, createViewColumn(4, pd.getName() + "_version", PropertyDefinitionType.INTEGER));

		return toCreateViewDDL(toReferenceViewName(ed.getName(), pd.getName()), sbCol.toString(), sqc.toSelectSql());
	}

	private void writeReferenceViewDDL(EntityDefinition ed) throws IOException {
		List<PropertyDefinition> pdList =
				ed.getPropertyList().stream().filter(
						pd -> pd.getType() == PropertyDefinitionType.REFERENCE).collect(Collectors.toList());
		for (PropertyDefinition pd : pdList) {
			String ddl = generateReferenceViewDDL(ed, pd);

			// コメント出力
			writer.write(String.format("/* drop/create %s$%s */\n", ed.getName(), pd.getName()));

			// DDL出力
			writer.write(ddl);
		}
	}

}
