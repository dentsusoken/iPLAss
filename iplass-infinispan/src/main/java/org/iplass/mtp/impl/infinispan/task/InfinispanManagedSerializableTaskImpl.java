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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
class InfinispanManagedSerializableTaskImpl implements InfinispanManagedSerializableTask {

	private static final long serialVersionUID = 5114700800142336149L;

	private static Logger LOG = LoggerFactory.getLogger(InfinispanManagedSerializableTaskImpl.class);

	private InfinispanSerializableTask task;
	private String requestId;
	private String fromNode;

	public InfinispanManagedSerializableTaskImpl(InfinispanSerializableTask task, String requestId, String fromNode) {
		this.task = task;
		this.requestId = requestId;
		this.fromNode = fromNode;
	}

	@Override
	public void run() {
		LOG.info("Execute request {}({}) from node {}. task = {}.", task.getClass().getSimpleName(), requestId, fromNode);
		try {
			task.run();
			LOG.info("Execution of request {}({}) from remote node {} is finished.", task.getClass().getSimpleName(), requestId, fromNode);
		} catch (RuntimeException e) {
			LOG.error("Execution of request {}({}) from remote node {} failed.", task.getClass().getSimpleName(), requestId, fromNode, e);
			throw e;
		}
	}

	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public String getFromNode() {
		return fromNode;
	}
}
