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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.cluster.channel.MessageReceiver;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskState;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanMessageChannel implements MessageChannel, ServiceInitListener<ClusterService> {

	private static Logger logger = LoggerFactory.getLogger(InfinispanMessageChannel.class);
	private static Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.cluster");

	private MessageReceiver receiver;

	private boolean sync;

	private BlockingQueue<Message> msgQueue;
	private ExecutorService ats;


	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	@Override
	public void inited(ClusterService service, Config config) {
		if (!sync) {
			final BlockingQueue<Message> newQueue = new LinkedBlockingQueue<>();
			msgQueue = newQueue;
			//TODO 設定可能に
			ats = Executors.newSingleThreadExecutor();
			ats.submit(new Callable<Void>() {
				@Override
				public Void call() {
					ArrayList<Message> msgList = new ArrayList<>(32);
					while (true) {
						try {
							for (Message msg = newQueue.poll(); msgList.size() < 32 && msg != null; msg = newQueue.poll()) {
								msgList.add(msg);
							}
							if (msgList.size() != 0) {
								Message[] msgArray = msgList.toArray(new Message[msgList.size()]);
								msgList.clear();
								//リトライとかは、Infinispan側に任せる。
								doSendMessage(msgArray);
							}

							//次のメッセージがくるまでブロック
							Message msg = newQueue.take();
							msgList.add(msg);

						} catch (RuntimeException | Error e) {
							fatalLog.error("send message worker failed.error=" + e, e);
						} catch (InterruptedException e) {
							if (!ats.isShutdown()) {
								fatalLog.error("send message worker failed.error=" + e, e);
							} else {
								//shutdown called (maybe)
								return null;
							}

						}
					}
				}
			});
		}
	}

	@Override
	public void destroyed() {

		//		ds.shutdown();
		//		ds = null;
		if (ats != null) {
			ats.shutdownNow();
		}
	}

	@Override
	public void setMessageReceiver(MessageReceiver receiver) {
		this.receiver = receiver;
	}


	void receiveMessage(Message[] msgList) {
		for (Message msg: msgList) {
			if (logger.isDebugEnabled()) {
				logger.debug("receive message :" + msg);
			}
			receiver.receiveMessage(msg);
		}
	}

	@Override
	public void sendMessage(final Message message) {
		if (sync) {
			doSendMessage(new Message[]{message});
		} else {
			if (!msgQueue.offer(message)) {
				fatalLog.error("send message failed. cause cant put to messageQueue. message=" + message);
			};
		}
	}

	private void doSendMessage(Message[] message) {
		if (logger.isDebugEnabled()) {
			logger.debug("send message over infinispan. message=" + Arrays.toString(message));
		}

		InfinispanTaskState<Void> state = InfinispanTaskExecutor.submitRemote(new InfinispanMessageTask(message));
		state.getFuture().forEach(f -> {
			try {
				f.get();
			} catch (Exception e) {
				fatalLog.error("send message failed.error={}, message={}", e.toString(), Arrays.toString(message), e);
			}
		});
	}

}
