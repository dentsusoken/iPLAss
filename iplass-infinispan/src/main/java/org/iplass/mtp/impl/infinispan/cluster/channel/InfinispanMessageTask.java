/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.infinispan.cluster.channel;

import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.infinispan.task.InfinispanSerializableTask;
import org.iplass.mtp.spi.ServiceRegistry;

class InfinispanMessageTask implements InfinispanSerializableTask<Void> {
	private static final long serialVersionUID = 2336468041075113029L;

	private Message[] msg;

	InfinispanMessageTask(Message[] msg) {
		this.msg = msg;
	}

	@Override
	public Void callByNode() {
		ClusterService cs = ServiceRegistry.getRegistry().getService(ClusterService.class);
		MessageChannel mc = cs.getMessageChannel();
		if (mc instanceof InfinispanMessageChannel) {
			((InfinispanMessageChannel) mc).receiveMessage(msg);
		}

		return null;
	}
}
