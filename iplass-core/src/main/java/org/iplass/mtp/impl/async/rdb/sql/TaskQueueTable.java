/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.async.rdb.sql;

public interface TaskQueueTable {
	
	public static final String TABLE_NAME = "TASK_QUEUE";
	public static final String TABLE_NAME_HISTORY = "TASK_QUEUE_HI";
	
	public static final String TENANT_ID = "TENANT_ID";
	public static final String Q_ID = "Q_ID";
	public static final String TASK_ID = "TASK_ID";
	public static final String VISIBLE_TIME = "V_TIME";
	public static final String STATUS = "STATUS";
	public static final String GROUPING_KEY = "G_KEY";
	public static final String VIRTUAL_WORKER_ID = "VW_ID";
	public static final String EXP_MODE = "EXP_MODE";
	public static final String RESULT_FLG = "RES_FLG";
	public static final String VERSION = "VER";
	public static final String UPDATE = "UP_DATE";
	public static final String SERVER_ID = "SERVER_ID";
	public static final String RETRY_COUNT = "RE_CNT";
	public static final String CALLABLE = "CALLABLE";
	public static final String RESULT = "RES";

}
