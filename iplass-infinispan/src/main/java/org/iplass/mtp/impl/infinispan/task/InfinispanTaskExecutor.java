/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.infinispan.task;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.impl.infinispan.InfinispanUtil;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 *
 */
public class InfinispanTaskExecutor {
	public static InfinispanTaskState submitAll(InfinispanSerializableTask task) {
		return doSubmit(task, t -> t);
	}

	public static InfinispanTaskState submitRemoteNode(InfinispanSerializableTask task) {
		return doSubmit(task, t -> new InfinispanRemoteOnlySerializableTask(t));
	}

	private static String generateRequestId() {
		// TODO リクエストID見直し
		return UUID.randomUUID().toString();
	}

	private static InfinispanTaskState doSubmit(InfinispanSerializableTask task,
			Function<InfinispanManagedSerializableTask, InfinispanManagedSerializableTask> decorator) {

		InfinispanManagedSerializableTask managedTask = new InfinispanManagedSerializableTaskImpl(task, generateRequestId(),
				InfinispanUtil.getExecutionNode());
		InfinispanManagedSerializableTask decoratedTask = decorator.apply(managedTask);
		CompletableFuture<Void> future = ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager()
				.executor().allNodeSubmission().submit(decoratedTask);

		return new InfinispanTaskState(managedTask.getRequestId(), future);
	}
}
