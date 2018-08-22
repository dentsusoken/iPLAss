/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.generic;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

/**
 * Entityロード時にカスタムで処理を行わせるインターフェース
 * @author lis3wg
 *
 */
public interface LoadEntityInterrupter {

	/**
	 * ロード処理の種類
	 */
	public enum LoadType {
		/** 画面表示時 */
		VIEW,
		/** 更新処理時 */
		UPDATE
	}

	/**
	 * ロード前処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param defName Entity定義名
	 * @param loadOption ロード時のオプション
	 * @param type ロード処理の種類
	 * @return 実行結果
	 */
	public LoadEntityContext beforeLoadEntity(RequestContext request, DetailFormView view,
			String defName, LoadOption loadOption, LoadType type);

	/**
	 * ロード後処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param type ロード処理の種類
	 */
	default public void afterLoadEntity(RequestContext request, DetailFormView view,
			Entity entity, LoadOption loadOption, LoadType type) {
	}

	/**
	 * 参照プロパティに対するロード前処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param defName Entity定義名
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param type ロード処理の種類
	 * @return 実行結果
	 */
	public LoadEntityContext beforeLoadReference(RequestContext request, DetailFormView view,
			String defName, LoadOption loadOption, ReferenceProperty property, LoadType type);

	/**
	 * 参照プロパティに対するロード後処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param type ロード処理の種類
	 */
	default public void afterLoadReference(RequestContext request, DetailFormView view,
			Entity entity, LoadOption loadOption, ReferenceProperty property, LoadType type) {
	}

}
