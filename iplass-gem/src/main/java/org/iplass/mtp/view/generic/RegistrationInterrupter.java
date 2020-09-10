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

package org.iplass.mtp.view.generic;

import java.util.Collections;
import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;

/**
 * 汎用登録処理にカスタムで登録処理を行わせるインターフェース
 * @author lis3wg
 */
public interface RegistrationInterrupter {

	/**
	 * 登録処理の種類
	 */
	public enum RegistrationType {
		/** 新規追加 */
		INSERT,
		/** 更新 */
		UPDATE
	}

	/**
	 * 登録用のデータにリクエストのデータをマッピングします。
	 *
	 * @param entity 登録用のデータ
	 * @param request リクエスト
	 * @param definition Entity定義
	 * @param view 画面定義
	 */
	default public void dataMapping(Entity entity, RequestContext request, EntityDefinition definition, FormView view) {
	}

	/**
	 * {@link #getAdditionalProperties()} で全てのプロパティを対象にするかを判断します。
	 * デフォルトはfalseです。
	 *
	 * @return trueの場合、{@link #getAdditionalProperties()} の戻り値のプロパティのみを更新対象<BR>
	 *         falseの場合、汎用登録処理が自動で設定した更新対象に、{@link #getAdditionalProperties()} の戻り値のプロパティを追加
	 */
	default public boolean isSpecifyAllProperties() {
		return false;
	}

	/**
	 * <p>
	 * 更新対象のプロパティを取得します。
	 * 更新対象のプロパティは {@link #isSpecifyAllProperties()} により対象範囲が変わります。
	 * </p>
	 * 
	 * <ul>
	 * <li>trueの場合、このメソッドの戻り値のプロパティのみを更新対象にする</li>
	 * <li>falseの場合、汎用登録処理が自動で設定した更新対象に、このメソッドの戻り値のプロパティを追加する</li>
	 * </ul>
	 *
	 * @return 更新対象プロパティ
	 */
	default public String[] getAdditionalProperties() {
		return new String[]{};
	}

	/**
	 * 登録前処理を行います。
	 * 
	 * @param entity 登録用のデータ
	 * @param request リクエスト
	 * @param definition Entity定義
	 * @param view 画面定義
	 * @param registrationType 登録処理の種類
	 * @return 入力エラーリスト
	 */
	default public List<ValidateError> beforeRegist(Entity entity, RequestContext request,
			EntityDefinition definition, FormView view, RegistrationType registrationType) {
		return Collections.emptyList();
	}

	/**
	 * 登録後処理を行います。
	 * 
	 * @param entity 登録用のデータ
	 * @param request リクエスト
	 * @param definition Entity定義
	 * @param view 画面定義
	 * @param registrationType 登録処理の種類
	 * @return 入力エラーリスト
	 */
	default public List<ValidateError> afterRegist(Entity entity, RequestContext request,
			EntityDefinition definition, FormView view, RegistrationType registType) {
		return Collections.emptyList();
	}

}
