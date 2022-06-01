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

import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyElement;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;

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
		/** 更新処理時初期ロード */
		BEFORE_UPDATE,
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
	default public LoadEntityContext beforeLoadEntity(RequestContext request, FormView view,
			String defName, LoadOption loadOption, LoadType type) {
		return new LoadEntityContext(loadOption);
	}

	/**
	 * ロード後処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param type ロード処理の種類
	 */
	default public void afterLoadEntity(RequestContext request, FormView view,
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
	 * @deprecated use {@link #beforeLoadReference(RequestContext, FormView, String, LoadOption, ReferenceProperty, PropertyElement, LoadType)}
	 */
	@Deprecated
	default public LoadEntityContext beforeLoadReference(RequestContext request, FormView view,
			String defName, LoadOption loadOption, ReferenceProperty property, LoadType type) {
		return new LoadEntityContext(loadOption);
	}

	/**
	 * 参照プロパティに対するロード前処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param defName Entity定義名
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param element エレメント
	 * @param type ロード処理の種類
	 * @return 実行結果
	 */
	default public LoadEntityContext beforeLoadReference(RequestContext request, FormView view,
			String defName, LoadOption loadOption, ReferenceProperty property, Element element, LoadType type) {
		return beforeLoadReference(request, view, defName, loadOption, property, type);
	}

	/**
	 * 参照プロパティに対するロード後処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param type ロード処理の種類
	 * @deprecated use {@link #afterLoadReference(RequestContext, FormView, Entity, LoadOption, ReferenceProperty, PropertyElement, LoadType)}
	 */
	@Deprecated
	default public void afterLoadReference(RequestContext request, FormView view,
			Entity entity, LoadOption loadOption, ReferenceProperty property, LoadType type) {
	}

	/**
	 * 参照プロパティに対するロード後処理を行います。
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param entity Entity
	 * @param loadOption ロード時のオプション
	 * @param property プロパティ定義
	 * @param element エレメント
	 * @param type ロード処理の種類
	 */
	default public void afterLoadReference(RequestContext request, FormView view,
			Entity entity, LoadOption loadOption, ReferenceProperty property, Element element, LoadType type) {
		afterLoadReference(request, view, entity, loadOption, property, type);
	}

	/**
	 * 大量データ用参照セクションの検索前処理を行います。
	 *
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param query 検索用クエリ
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 * @return 実行結果
	 * @deprecated use {@link #beforeSearchMassReference(RequestContext, FormView, Query, ReferenceProperty, MassReferenceSection, OutputType)}
	 */
	@Deprecated
	default public SearchQueryContext beforeSearchMassReference(RequestContext request, FormView view,
			Query query, OutputType outputType) {
		return new SearchQueryContext(query);
	}

	/**
	 * 大量データ用参照セクションの検索前処理を行います。
	 * 
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param query 検索用クエリ
	 * @param referenceProperty 参照プロパティ定義
	 * @param section 大量データ用参照セクション
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 * @return 実行結果
	 */
	default public SearchQueryContext beforeSearchMassReference(RequestContext request, FormView view, Query query,
			ReferenceProperty referenceProperty, MassReferenceSection section, OutputType outputType) {
		SearchQueryContext searchQueryContext =  beforeSearchMassReference(request, view, query, outputType);
		List<String> withoutConditionReferenceNameKey = section.getWithoutConditionReferenceNameKey();
		if (withoutConditionReferenceNameKey != null && !withoutConditionReferenceNameKey.isEmpty()) {
			searchQueryContext.setWithoutConditionReferenceName(
					withoutConditionReferenceNameKey.toArray(new String[withoutConditionReferenceNameKey.size()]));
		}
		return searchQueryContext;
	}

	/**
	 * 大量データ用参照セクションの検索後処理を行います。
	 *
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param query 検索用クエリ
	 * @param entity 検索結果
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 * @deprecated use {@link #afterSearchMassReference(RequestContext, FormView, Query, ReferenceProperty, MassReferenceSection, OutputType)}
	 */
	@Deprecated
	default public void afterSearchMassReference(RequestContext request, FormView view,
			Query query, Entity entity, OutputType outputType) {
	}

	/**
	 * 大量データ用参照セクションの検索後処理を行います。
	 *
	 * @param request リクエスト
	 * @param view 詳細画面定義
	 * @param query 検索用クエリ
	 * @param referenceProperty 参照プロパティ定義
	 * @param section 大量データ用参照セクション
	 * @param entity 検索結果
	 * @param outputType 出力タイプ(VIEWまたはEDIT)
	 */
	default public void afterSearchMassReference(RequestContext request, FormView view, Query query,
			ReferenceProperty referenceProperty, MassReferenceSection section, Entity entity, OutputType outputType) {
		afterSearchMassReference(request, view, query, entity, outputType);
	}
}
