/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.cluster.channel.jgroups;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.cluster.channel.MessageReceiver;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.jgroups.JChannel;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.MessageBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JGroupsベースのMessageChannel実装。
 *
 * @author K.Higuchi
 *
 */
public class JGroupsMessageChannel implements MessageChannel, ServiceInitListener<ClusterService> {

	private static Logger logger = LoggerFactory.getLogger(JGroupsMessageChannel.class);
	private static Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.cluster");

	private String configFilePath;
	private String clusterName;
	private MessageReceiver messageReceiver;

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	private JChannel channel;

	@Override
	public void inited(ClusterService service, Config config) {

		try (InputStream is = inputStreamFromFile();
				BufferedInputStream bis = new BufferedInputStream(is)) {
			channel = new JChannel(is);
		} catch (Exception e) {
			throw new ServiceConfigrationException("can't read JGroups configFile:" + configFilePath, e);
		}

		channel.setReceiver(new Receiver() {
			@Override
			public void receive(org.jgroups.Message msg) {
				if (!channel.getAddress().equals(msg.getSrc())) {
					Message m = msg.getObject();
					messageReceiver.receiveMessage(m);
					if (logger.isDebugEnabled()) {
						logger.debug("receive message:" + m);
					}
				}
			}

			@Override
			public void receive(MessageBatch batch) {
				for (org.jgroups.Message msg: batch) {
					try {
						receive(msg);
					} catch(Throwable t) {
						logger.error("exception occurred while handling MessageBatch: " + msg + ": " + t.getMessage(), t);
					}
				}
			}

			@Override
			public void viewAccepted(View view) {
				if (logger.isDebugEnabled()) {
					logger.debug("JGroups view changed: " + view);
				}
			}
		});

		try {
			channel.connect(clusterName);
		} catch (Exception e) {
			fatalLog.error("Can't connect JGroups cluster:" + clusterName + ", so start as standalone mode", e);
			channel = null;
		}
	}

	protected InputStream inputStreamFromFile() throws IOException {
		InputStream is = getClass().getResourceAsStream(configFilePath);
		if (is == null) {
			is = new FileInputStream(configFilePath);
		}
		return is;
	}

	@Override
	public void destroyed() {
		if (channel != null) {
			channel.close();
		}
	}

	@Override
	public void sendMessage(Message message) {
		if (channel != null) {
			try {
				channel.send(null, message);
				if (logger.isDebugEnabled()) {
					logger.debug("send message:" + message);
				}

			} catch (Exception e) {
				fatalLog.error("send message failed.JGroups cluster=" + clusterName + ", error=" + e + ", message=" + message, e);
			}
		} else {
			fatalLog.warn("can't send message because JGroups cluster not initialized.JGroups cluster=" + clusterName + ", message=" + message);
		}
	}

	public MessageReceiver getMessageReceiver() {
		return messageReceiver;
	}

	@Override
	public void setMessageReceiver(MessageReceiver messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

}
