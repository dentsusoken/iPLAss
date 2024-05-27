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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.cluster.channel.MessageReceiver;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskState;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class InfinispanMessageChannel implements MessageChannel, ServiceInitListener<ClusterService> {

	private static Logger logger = LoggerFactory.getLogger(InfinispanMessageChannel.class);
	private static Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.cluster");

	private MessageReceiver receiver;

	private boolean sync;

	private BlockingQueue<InternalMessage> msgQueue;
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
			final BlockingQueue<InternalMessage> newQueue = new LinkedBlockingQueue<>();
			msgQueue = newQueue;
			//TODO 設定可能に
			ats = Executors.newSingleThreadExecutor();
			ats.submit(new Callable<Void>() {
				@Override
				public Void call() {
					ArrayList<InternalMessage> msgList = new ArrayList<>(32);
					while (true) {
						try {
							for (InternalMessage msg = newQueue.poll(); msgList.size() < 32 && msg != null; msg = newQueue.poll()) {
								msgList.add(msg);
							}
							if (msgList.size() != 0) {
								InternalMessage[] msgArray = msgList.toArray(new InternalMessage[msgList.size()]);
								msgList.clear();
								//リトライとかは、Infinispan側に任せる。
								doSendMessage(msgArray);
							}

							//次のメッセージがくるまでブロック
							InternalMessage msg = newQueue.take();
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
			InternalMessage sendMessage = new InternalMessage(message, MDC.get(ExecuteContext.MDC_TRACE_ID));
			doSendMessage(new InternalMessage[] { sendMessage });
		} else {
			if (!msgQueue.offer(new InternalMessage(message, MDC.get(ExecuteContext.MDC_TRACE_ID)))) {
				fatalLog.error("send message failed. cause cant put to messageQueue. message=" + message);
			};
		}
	}

	private void doSendMessage(InternalMessage[] message) {
		if (logger.isDebugEnabled()) {
			logger.debug("send message over infinispan. message=" + Arrays.toString(message));
		}

		Map<String, List<Message>> mdcTraceIdGroup = new HashMap<>();
		for (InternalMessage m : message) {
			List<Message> group = mdcTraceIdGroup.get(m.getMdcTraceId());
			if (null == group) {
				group = new ArrayList<Message>();
				mdcTraceIdGroup.put(m.getMdcTraceId(), group);
			}
			group.add(m.getMessage());
		}

		for (Map.Entry<String, List<Message>> entry : mdcTraceIdGroup.entrySet()) {
			Message[] messageArray = entry.getValue().toArray(new Message[entry.getValue().size()]);
			String mdcTraceId = entry.getKey();
			InfinispanTaskState<Void> state = InfinispanTaskExecutor.submitRemote(new InfinispanMessageTask(messageArray), mdcTraceId);
			state.getFuture().forEach(f -> {
				try {
					f.get();
				} catch (Exception e) {
					fatalLog.error("send message failed.error={}, message={}", e.toString(), Arrays.toString(message), e);
				}
			});
		}
	}

	/**
	 * 内部メッセージ管理クラス
	 *
	 * <p>
	 * キューで管理される情報。
	 * 非同期でメッセージ送信する可能性もある為、アクターから渡されたメッセージと、送信時スレッドに紐づく情報を管理する。
	 * </p>
	 */
	private static class InternalMessage {
		/** メッセージ */
		private Message message;
		/** ログ MDC traceId の値 */
		private String mdcTraceId;

		/**
		 * コンストラクタ
		 * @param message メッセージ
		 * @param mdcTraceId ログ MDC traceId の値
		 */
		private InternalMessage(Message message, String mdcTraceId) {
			this.message = message;
			this.mdcTraceId = mdcTraceId;
		}

		/**
		 * メッセージを取得する
		 * @return メッセージ
		 */
		public Message getMessage() {
			return message;
		}

		/**
		 * ログ MDC traceId の値を取得する
		 * @return ログ MDC traceId の値
		 */
		public String getMdcTraceId() {
			return mdcTraceId;
		}

		@Override
		public String toString() {
			return "InternalMessage [message=" + message.toString() + ", mdcTraceId=" + mdcTraceId + "]";
		}
	}
}
