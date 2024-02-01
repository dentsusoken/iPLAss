/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.cluster;

import java.util.List;

import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.cluster.channel.MessageReceiver;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterService implements Service {
	private static Logger logger = LoggerFactory.getLogger(ClusterService.class);
	
	private MessageChannel messageChannel;

	private final ListenerMap listenerMap = new ListenerMap();
	
	@Override
	public void init(Config config) {
		listenerMap.init();
		
		messageChannel = (MessageChannel) config.getBean("messageChannel");
		if (messageChannel != null) {
			messageChannel.setMessageReceiver(new MessageReceiver() {
				
				@Override
				public void receiveMessage(Message msg) {
					List<ClusterEventListener> listeners = listenerMap.getListener(msg.getEventName());
					for (ClusterEventListener l: listeners) {
						try {
							l.onMessage(msg);
						} catch (RuntimeException e) {
							logger.error("exception occurred while handling message at " + l + ":" + e + ", " + msg, e);
						}
					}
				}
			});
		}
	}

	@Override
	public void destroy() {
		
	}
	
	public MessageChannel getMessageChannel() {
		return messageChannel;
	}

	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}
	
	public void sendMessage(Message msg) {
		if (messageChannel != null) {
			messageChannel.sendMessage(msg);
		}
	}
	
//	public void registerListener(String eventName, ClusterEventListener listener) {
//		listenerMap.addListener(eventName, listener);
//	}
	
	public void registerListener(String[] eventNames, ClusterEventListener listener) {
		for (String e: eventNames) {
			listenerMap.addListener(e, listener);
		}
	}
	
	public void removeListener(String[] eventNames, ClusterEventListener listener) {
		for (String e: eventNames) {
			listenerMap.removeListener(e, listener);
		}
	}

}
