/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.async.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.iplass.mtp.async.AsyncTaskFuture;
import org.iplass.mtp.async.AsyncTaskOption;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.spi.Config;

/**
 * 内部利用 単一スレッド用の AsyncTaskService
 * <p>
 * テナントが未確定な状態で利用する非同期処理です。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class InternalSingleThreadAsyncTaskService extends AsyncTaskService {
	private ExecutorService executor;

	@Override
	public void init(Config config) {
		executor = Executors.newSingleThreadExecutor();
	}

	@Override
	public void destroy() {
		if (null != executor) {
			executor.close();
		}
	}

	@Override
	public <V> Future<V> execute(Callable<V> task) {
		return executor.submit(task);
	}

	@Override
	public <V> AsyncTaskFuture<V> execute(Callable<V> task, AsyncTaskOption option, boolean inheritAuthContext) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public <V> AsyncTaskFuture<V> getResult(long taskId, String queueName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
