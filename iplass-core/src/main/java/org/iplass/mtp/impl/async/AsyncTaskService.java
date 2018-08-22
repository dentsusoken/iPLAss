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

package org.iplass.mtp.impl.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.iplass.mtp.async.AsyncTaskFuture;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.spi.Service;


public abstract class AsyncTaskService implements Service {
	public static final String DEFAULT_QUEUE_BASE_ASYNC_TASK_SERVICE = "DefaultQueueBaseAsyncTaskService";

	//あくまで基盤内で非同期実行するためのインタフェース。外部連携（JMS経由で、他のシステムへ連携）は別途考える

	public abstract <V> Future<V> execute(Callable<V> task);

	public abstract <V> AsyncTaskFuture<V> execute(Callable<V> task, AsyncTaskOption option, boolean inheritAuthContext);

	public abstract <V> AsyncTaskFuture<V> getResult(long taskId, String queueName);
}
