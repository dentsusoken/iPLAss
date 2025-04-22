/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.warmup;

import java.util.HashMap;
import java.util.Map;

/**
 * ウォームアップコンテキスト
 * @author SEKIGUCHI Naoya
 */
public class WarmupContext {
	/** コンテキスト本体 */
	private Map<String, Object> context = new HashMap<>();

	/**
	 * コンテキストの値を取得する
	 * @param <T> 値の型
	 * @param key キー
	 * @param value 値
	 */
	public <T> void set(String key, T value) {
		context.put(key, value);
	}

	/**
	 * コンテキストの値を取得する
	 * @param <T> 値の型
	 * @param key キー
	 * @return 値
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) context.get(key);
	}
}
