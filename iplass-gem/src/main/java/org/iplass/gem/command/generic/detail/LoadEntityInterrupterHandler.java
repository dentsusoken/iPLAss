/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.view.generic.LoadEntityContext;
import org.iplass.mtp.view.generic.LoadEntityInterrupter;
import org.iplass.mtp.view.generic.LoadEntityInterrupter.LoadType;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.SearchQueryContext;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;

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
	 * @param element エレメント
	 * @param type ロード処理の種類
	 * @return 実行結果
	 */
	public LoadEntityContext beforeLoadReference(String defName, LoadOption loadOption, ReferenceProperty property, Element element, LoadType type) {
		FormView formView = context.getView();
		loadOption.setLocalized(formView.isLocalizationData());

		return interrupter.beforeLoadReference(request, formView, defName, loadOption, property, element, type);
	}

	/**
	 * 参照プロパティに対するロード後処理を行います。
	 * @param after Entity
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param element エレメント
	 * @param type ロード処理の種類
	 */
	public void afterLoadReference(Entity entity, LoadOption loadOption, ReferenceProperty property, Element element, LoadType type) {
		FormView formView = context.getView();
		loadOption.setLocalized(formView.isLocalizationData());

		interrupter.afterLoadReference(request, formView, entity, loadOption, property, element, type);
	}

	/**
	 * 大量データ用参照セクションの検索前処理を行います。
	 * @param query 検索用クエリ
	 * @param referenceProperty 参照プロパティ定義
	 * @param section 大量データ用参照セクション
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 * @return 実行結果
	 */

	public SearchQueryContext beforeSearchMassReference(Query query, ReferenceProperty referenceProperty, MassReferenceSection section, OutputType outputType) {
		FormView formView = context.getView();
		query.setLocalized(formView.isLocalizationData());
		SearchQueryContext ret = interrupter.beforeSearchMassReference(request, formView, query, referenceProperty, section, outputType);
		return ret;
	}

	/**
	 * 大量データ用参照セクションの検索後処理を行います。
	 *
	 * @param query 検索用クエリ
	 * @param referenceProperty 参照プロパティ定義
	 * @param section 大量データ用参照セクション
	 * @param entity 検索結果
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 */
	public void afterSearchMassReference(Query query, ReferenceProperty referenceProperty, MassReferenceSection section, Entity entity, OutputType outputType) {
		FormView formView = context.getView();
		interrupter.afterSearchMassReference(request, formView, query, referenceProperty, section, entity, outputType);
	}

}
