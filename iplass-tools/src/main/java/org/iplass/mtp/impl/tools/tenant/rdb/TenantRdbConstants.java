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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface TenantRdbConstants {

	/** テーブルリスト（t_tenant、t_accountを除く） */
	public static final String[] TABLE_LIST = {
		"counter",
		"crawl_log",
		"delete_log",
		"lob_store",
		"obj_blob",
		"obj_blob_rb",

		"obj_data",
		"obj_data_rb",

		"obj_store",
		"obj_store_rb",

		"obj_ref",
		"obj_ref_rb",

		"obj_index_date",
		"obj_index_dbl",
		"obj_index_num",
		"obj_index_str",
		"obj_index_ts",

		"obj_unique_date",
		"obj_unique_dbl",
		"obj_unique_num",
		"obj_unique_str",
		"obj_unique_ts",

		"obj_meta",
		"schema_ctrl",
		"task_queue",
		"task_queue_hi"
	};

	/**
	 * <p>Partition除外テーブルリスト</p>
	 * <p>除外するテーブルを管理(ほとんどはPartition対象なので)</p>
	 */
	public static final Set<String> EXCLUDE_PARTITION_TABLE = Stream.of(
		"crawl_log"
		,"obj_meta"
		,"task_queue"
	).collect(Collectors.toSet());

	/** SubPartition数 */
	public static int MAX_SUBPARTITION = 8;


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
