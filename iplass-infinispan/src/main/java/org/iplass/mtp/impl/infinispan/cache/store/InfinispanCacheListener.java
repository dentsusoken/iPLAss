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
package org.iplass.mtp.impl.infinispan.cache.store;

import java.util.List;
import java.util.function.Consumer;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryInvalidatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infinispan キャッシュリスナー
 *
 * <p>
 * キャッシュ操作時のリスナー
 * </p>
 *
 * <p>
 * InfinispanCacheStoreFactory の内部クラスから移管
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
@Listener
public class InfinispanCacheListener {
	/** ロガー */
	private static final Logger LOG = LoggerFactory.getLogger(InfinispanCacheListener.class);
	/** iPLAssキャッシュイベントリスナーリスト */
	private List<CacheEventListener> listeners;

	/**
	 * コンストラクタ
	 * @param listeners iPLAssキャッシュイベントリスナーリスト
	 */
	public InfinispanCacheListener(List<CacheEventListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * キャッシュ登録時イベント
	 * @param event イベントインスタンス
	 */
	@CacheEntryCreated
	public void created(CacheEntryCreatedEvent<Object, CacheEntry> event) {
		if (event.isPre()) {
			return;
		}

		LOG.debug("infinispan cache created. key={}, value={}, commandRetried={}, originLocal={}, currentState={}",
				event.getKey(), event.getValue(), event.isCommandRetried(), event.isOriginLocal(), event.isCurrentState());

		doCreated(new CacheCreateEvent(event.getValue()));
	}

	/**
	 * キャッシュ変更イベント
	 * @param event イベントインスタンス
	 */
	@CacheEntryModified
	public void modified(CacheEntryModifiedEvent<Object, CacheEntry> event) {
		if (event.isPre()) {
			return;
		}

		LOG.debug("infinispan cache modified. key={}, oldValue = {}, newValue = {}, commandRetried={}, originLocal={}, currentState={}",
				event.getKey(), event.getOldValue(), event.getNewValue(), event.isCommandRetried(), event.isOriginLocal(), event.isCurrentState());


		if (event.isCreated()) {
			doCreated(new CacheCreateEvent(event.getNewValue()));

		} else {
			doUpdated(new CacheUpdateEvent(event.getOldValue(), event.getNewValue()));
		}
	}

	/**
	 * キャッシュ削除イベント
	 * @param event イベントインスタンス
	 */
	@CacheEntryRemoved
	public void removed(CacheEntryRemovedEvent<Object, CacheEntry> event) {
		if (event.isPre()) {
			return;
		}

		LOG.debug("infinispan cache removed. key={}, oldValue = {}, commandRetried={}, originLocal={}, currentState={}",
				event.getKey(), event.getOldValue(), event.isCommandRetried(), event.isOriginLocal(), event.isCurrentState());

		doRemoved(new CacheRemoveEvent(event.getOldValue()));
	}

	/**
	 * キャッシュ無効化イベント
	 * @param event イベントインスタンス
	 */
	@CacheEntryInvalidated
	public void invalidated(CacheEntryInvalidatedEvent<Object, CacheEntry> event) {
		// TODO 動作未確認

		if (event.isPre()) {
			return;
		}

		LOG.debug("infinispan cache invalidated. key={}, value = {}, originLocal={}, currentState={}",
				event.getKey(), event.getValue(), event.isOriginLocal(), event.isCurrentState());

		doInvalidated(new CacheInvalidateEvent(event.getValue()));
	}

	//		@CacheEntryRemoved
	//		@CacheEntryActivated
	//		@CacheEntryCreated
	//		@CacheEntryLoaded
	//		@CacheEntryPassivated
	//		@CacheEntryVisited
	//		public void otherEvented(CacheEntryEvent<Object, CacheEntry> event) {
	//			if (logger.isDebugEnabled()) {
	//				logger.debug("event:" + event);
	//			}
	//
	//		}

	/**
	 * listeners の forEach 処理を行う。
	 *
	 * <p>
	 * listeners が null の場合、処理を実行しない。
	 * </p>
	 *
	 * @param processEvent イベント処理
	 */
	private void forEachListener(Consumer<CacheEventListener> processEvent) {
		if (listeners != null) {
			listeners.forEach(l -> processEvent.accept(l));
		}
	}

	/**
	 * 新規作成イベントを実行する
	 * @param event イベント
	 */
	private void doCreated(CacheCreateEvent event) {
		forEachListener(l -> l.created(event));
	}

	/**
	 * 更新イベントを実行する
	 * @param event イベント
	 */
	private void doUpdated(CacheUpdateEvent event) {
		forEachListener(l -> l.updated(event));
	}

	/**
	 * 削除イベントを実行する
	 * @param event イベント
	 */
	private void doRemoved(CacheRemoveEvent event) {
		forEachListener(l -> l.removed(event));
	}

	/**
	 * 無効イベントを実行する
	 * @param event イベント
	 */
	private void doInvalidated(CacheInvalidateEvent event) {
		forEachListener(l -> l.invalidated(event));
	}
}
