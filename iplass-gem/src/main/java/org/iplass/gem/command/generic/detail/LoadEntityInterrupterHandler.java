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

package org.iplass.gem.command.generic.detail;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.LoadEntityContext;
import org.iplass.mtp.view.generic.LoadEntityInterrupter;
import org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType;

/**
 * カスタム登録処理ハンドラ
 * @author lis3wg
 */
public class LoadEntityInterrupterHandler {
	/** リクエスト */
	private RequestContext request;
	/** 登録処理クラス */
	private LoadEntityInterrupter interrupter;
	/** 詳細画面Context */
	private RegistrationCommandContext context;

	/**
	 * コンストラクタ
	 * @param request リクエスト
	 * @param context 詳細画面Context
	 * @param interrupter 登録処理クラス
	 */
	public LoadEntityInterrupterHandler(RequestContext request,
			RegistrationCommandContext context, LoadEntityInterrupter interrupter) {
		this.request = request;
		this.context = context;
		this.interrupter = interrupter;
	}

	/**
	 * ロード前処理を行います。
	 * @param defName Entity定義名
	 * @param loadOption ロード時のオプション
	 * @param type ロード処理の種類
	 * @return 実行結果
	 */
	public LoadEntityContext beforeLoadEntity(String defName, LoadOption loadOption, LoadType type) {
		FormView formView = context.getView();
		loadOption.setLocalized(formView.isLocalizationData());
		return interrupter.beforeLoadEntity(request, formView, defName, loadOption, type);
	}

	/**
	 * ロード後処理を行います。
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param type ロード処理の種類
	 */
	public void afterLoadEntity(Entity entity, LoadOption loadOption, LoadType type) {
		FormView formView = context.getView();
		loadOption.setLocalized(formView.isLocalizationData());
		interrupter.afterLoadEntity(request, formView, entity, loadOption, type);
	}

	/**
	 * 参照プロパティに対するロード前処理を行います。
	 * @param defName Entity定義名
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param type ロード処理の種類
	 * @return 実行結果
	 */
	public LoadEntityContext beforeLoadReference(String defName, LoadOption loadOption, ReferenceProperty property, LoadType type) {
		FormView formView = context.getView();
		loadOption.setLocalized(formView.isLocalizationData());
		return interrupter.beforeLoadReference(request, formView, defName, loadOption, property, type);
	}

	/**
	 * 参照プロパティに対するロード後処理を行います。
	 * @param after Entity
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param type ロード処理の種類
	 */
	public void afterLoadReference(Entity entity, LoadOption loadOption, ReferenceProperty property, LoadType type) {
		FormView formView = context.getView();
		loadOption.setLocalized(formView.isLocalizationData());
		interrupter.afterLoadReference(request, formView, entity, loadOption, property, type);
	}

}
