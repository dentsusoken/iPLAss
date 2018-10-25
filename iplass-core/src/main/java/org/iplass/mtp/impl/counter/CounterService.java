/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.counter;

import java.util.Set;

import org.iplass.mtp.spi.Service;

/**
 * 採番サービス。
 *
 * @author K.Higuchi
 *
 */
public interface CounterService extends Service {

	public static final String OID_COUNTER_SERVICE_NAME = "OidCounter";

	/**
	 * tenantId、incrementUnitKey単位でユニークな値を取得します。
	 * ただし、tenantId、incrementUnitKeyで連続して採番されるとは限りません。
	 *
	 * @param tenantId テナントID
	 * @param incrementUnitKey KEY
	 * @param initialCount まだカウンターが初期化されていない場合の初期値
	 * @return 採番値
	 */
	public long increment(int tenantId, String incrementUnitKey, long initialCount);

	/**
	 * 現在の採番値をリセットします。
	 * 
	 * @param tenantId テナントID
	 * @param incrementUnitKey KEY
	 */
	public void resetCounter(int tenantId, String incrementUnitKey);

	/**
	 * 現在の採番値を{@code currentCount}でリセットします。
	 * 
	 * @param tenantId テナントID
	 * @param incrementUnitKey KEY
	 * @param currentCount リセット値
	 */
	public void resetCounter(int tenantId, String incrementUnitKey, long currentCount);

	/**
	 * 採番データを削除します。
	 *
	 * @param tenantId テナントID
	 * @param incrementUnitKey KEY
	 */
	public void deleteCounter(int tenantId, String incrementUnitKey);

	/**
	 * 現在のカレント値を返します。
	 * もしまだ初期化されていない場合は-1が返却される。
	 *
	 * @param tenantId テナントID
	 * @param incrementUnitKey KEY
	 * @return カレント値
	 */
	public long current(int tenantId, String incrementUnitKey);

	/**
	 * 採番キーのリストを返します。
	 * {@code prefixIncrementUnitKey}が指定された場合は、前方一致で該当するKEYを返します。
	 *
	 * @param tenantId  テナントID
	 * @param prefixIncrementUnitKey KEYのPrefix
	 * @return 採番キーのリスト
	 */
	public Set<String> keySet(int tenantId, String prefixIncrementUnitKey);

}
