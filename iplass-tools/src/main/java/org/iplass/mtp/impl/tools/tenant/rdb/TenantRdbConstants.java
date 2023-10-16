/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.tenant.rdb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface TenantRdbConstants {

	/**
	 * テーブルリストとテーブルの削除単位カラム。削除単位カラムが不明な場合は空文字を設定。（t_tenant, t_account を除く）
	 * 削除単位カラムは、削除時に件数指定が不可能な場合に使用する。実質利用はPostgreSQLのみ利用するカラムとなっている。
	 */
	@SuppressWarnings("serial")
	public static final Map<String, String> TABLE_LIST_DELETION_UNIT_COLS = Collections
			.unmodifiableMap(new HashMap<String, String>() {
		{
			put("counter", "tenant_id, cnt_name, inc_unit_key");
			put("crawl_log", "tenant_id, obj_def_id, obj_def_ver");
			// NOTE delete_log - PK なし
			put("delete_log", "tenant_id, obj_def_id, obj_id, obj_ver");
			put("lob_store", "tenant_id, lob_data_id");
			// NOTE obj_blob - PK なし
			put("obj_blob", "tenant_id, lob_id");
			// NOTE obj_blob_rb - PK なし
			put("obj_blob_rb", "tenant_id, rb_id");

			// NOTE ver 3系では obj_data、obj_data_rb テーブルは存在しない
			put("obj_data", "");
			put("obj_data_rb", "");

			put("obj_store", "tenant_id, obj_def_id, obj_id, obj_ver, pg_no");
			put("obj_store_rb", "tenant_id, obj_def_id, rb_id");

			// NOTE obj_ref - PK なし (ReferenceDeleteSql#deleteByOidAndVersion を参考に列指定）
			put("obj_ref", "tenant_id, obj_def_id, ref_def_id, obj_id, obj_ver");
			// NOTE obj_ref_rb - PK なし (obj_ref に rb_id を加えて列指定）
			put("obj_ref_rb", "rb_id, tenant_id, obj_def_id, ref_def_id, obj_id, obj_ver");

			// NOTE obj_index_* - PK なし (IndexDeleteSql#deleteByOidAndVersion を参考に列を指定)
			put("obj_index_date", "tenant_id, obj_def_id, col_name, obj_id, obj_ver");
			put("obj_index_dbl", "tenant_id, obj_def_id, col_name, obj_id, obj_ver");
			put("obj_index_num", "tenant_id, obj_def_id, col_name, obj_id, obj_ver");
			put("obj_index_str", "tenant_id, obj_def_id, col_name, obj_id, obj_ver");
			put("obj_index_ts", "tenant_id, obj_def_id, col_name, obj_id, obj_ver");

			// NOTE obj_index_* - PK なし (IndexDeleteSql#deleteByOidAndVersion を参考に列を指定)
			put("obj_unique_date", "tenant_id, obj_def_id, col_name, obj_id");
			put("obj_unique_dbl", "tenant_id, obj_def_id, col_name, obj_id");
			put("obj_unique_num", "tenant_id, obj_def_id, col_name, obj_id");
			put("obj_unique_str", "tenant_id, obj_def_id, col_name, obj_id");
			put("obj_unique_ts", "tenant_id, obj_def_id, col_name, obj_id");

			put("obj_meta", "tenant_id, obj_def_id, obj_def_ver");
			put("schema_ctrl", "tenant_id, obj_def_id");
			put("task_queue", "q_id, tenant_id, task_id");
			put("task_queue_hi", "q_id, tenant_id, task_id");
		}
	});

	/** テーブルリスト（t_tenant、t_accountを除く） */
	public static final String[] TABLE_LIST = TABLE_LIST_DELETION_UNIT_COLS.keySet()
			.toArray(new String[TABLE_LIST_DELETION_UNIT_COLS.size()]);

	/**
	 * <p>Partition除外テーブルリスト</p>
	 * <p>除外するテーブルを管理(ほとんどはPartition対象なので)</p>
	 */
	public static final Set<String> EXCLUDE_PARTITION_TABLE = Stream.of(
		"crawl_log"
		,"obj_meta"
		,"task_queue"
	).collect(Collectors.toSet());

	/** 最大(デフォルト)SubPartition数 */
	public static final int MAX_SUBPARTITION = 8;

	/** 最小SubPartition数 */
	public static final int MIN_SUBPARTITION = 0;

	/**
	 * <p>SubPartition除外テーブルリスト</p>
	 * <p>除外するテーブルを管理(ほとんどはSubPartition対象なので)</p>
	 */
	public static final Set<String> EXCLUDE_SUB_PARTITION_TABLE = Stream.of(
		"obj_blob"
		,"lob_store"
		,"obj_blob_rb"
		,"counter"
		,"task_queue_hi"
	).collect(Collectors.toSet());

	/**
	 * <p>テナント操作時にテーブルの存在チェックが必要なテーブルリスト</p>
	 *
	 * <p>
	 * iPLAssバージョンアップなどで後から追加されたテーブルなど、存在しない可能性があるテーブル。
	 * ただしStorageSpace対象のテーブル({@linkplain TenantRdbConstants#STORAGE_SPACE_TABLE})については、
	 * ここに追加されていない場合でもテーブルの存在チェックは必要となる。
	 * </p>
	 */
	public static final Set<String> CHECK_EXIST_TABLE = Stream.of(
		"crawl_log"
		,"delete_log"
		,"task_queue"
		,"task_queue_hi"
		,"obj_data"
		,"obj_data_rb"
	).collect(Collectors.toSet());

	/**
	 * <p>StorageSpace用テーブルリスト</p>
	 *
	 * <p>StorageSpaceのtableNamePostfix分テーブルが作成される可能性があるテーブル。</p>
	 *
	 */
	public static final Set<String> STORAGE_SPACE_TABLE = Stream.of(
		"obj_store"
		,"obj_store_rb"

		,"obj_ref"
		,"obj_ref_rb"

		,"obj_index_date"
		,"obj_index_dbl"
		,"obj_index_num"
		,"obj_index_str"
		,"obj_index_ts"

		,"obj_unique_date"
		,"obj_unique_dbl"
		,"obj_unique_num"
		,"obj_unique_str"
		,"obj_unique_ts"
	).collect(Collectors.toSet());

}
