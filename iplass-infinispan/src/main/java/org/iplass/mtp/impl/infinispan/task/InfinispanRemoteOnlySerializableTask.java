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

import org.iplass.mtp.impl.infinispan.InfinispanUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
class InfinispanRemoteOnlySerializableTask implements InfinispanManagedSerializableTask {
	private static final long serialVersionUID = 2352076303611416994L;

	private static Logger LOG = LoggerFactory.getLogger(InfinispanRemoteOnlySerializableTask.class);

	private InfinispanManagedSerializableTask task;

	public InfinispanRemoteOnlySerializableTask(InfinispanManagedSerializableTask task) {
		this.task = task;
	}

	@Override
	public void runNode() {
		String fromNode = getFromNode();
		String requestId = getRequestId();
		String executionNode = InfinispanUtil.getExecutionNode();

		LOG.info("{} accepted the request {} from {}.", executionNode, requestId, fromNode);

		boolean isRemoteNodeRequest = !StringUtil.equals(executionNode, fromNode);
		if (isRemoteNodeRequest) {
			task.run();

		} else {
			LOG.info("Request {} is not executed from {}, because it is the same node.", requestId, fromNode);
		}
	}

	@Override
	public String getRequestId() {
		return task.getRequestId();
	}

	@Override
	public String getFromNode() {
		return task.getFromNode();
	}

}
