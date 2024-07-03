/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.view.generic.RegistrationInterrupter;
import org.iplass.mtp.view.generic.RegistrationInterrupter.RegistrationType;

/**
 * カスタム登録処理ハンドラ
 * 
 * @author lis3wg
 * @author Y.Ishida
 */
public class RegistrationInterrupterHandler {
	/** リクエスト */
	private RequestContext request;
	/** 登録処理クラス */
	private RegistrationInterrupter interrupter;
	/** 詳細画面Context */
	private RegistrationCommandContext context;

	/**
	 * コンストラクタ
	 * 
	 * @param request     リクエスト
	 * @param context     詳細画面Context
	 * @param interrupter 登録処理クラス
	 */
	public RegistrationInterrupterHandler(RequestContext request, RegistrationCommandContext context,
			RegistrationInterrupter interrupter) {
		this.request = request;
		this.context = context;
		this.interrupter = interrupter;
	}

	/**
	 * 登録用のデータにリクエストのデータをマッピングします。
	 * 
	 * @param entity 登録用のデータ
	 */
	public void dataMapping(Entity entity) {
		interrupter.dataMapping(entity, request, context.getEntityDefinition(), context.getView());
	}

	/**
	 * 更新対象のプロパティを取得します。
	 * 
	 * @param updatePropNames 汎用登録処理で指定された更新対象プロパティ
	 * @return 更新対象プロパティ
	 */
	public String[] getAdditionalProperties(List<String> updatePropNames) {
		List<String> propertyList = new ArrayList<String>();
		if (!interrupter.isSpecifyAllProperties()) {
			propertyList.addAll(updatePropNames);
		}
		if (interrupter.getAdditionalProperties() != null && interrupter.getAdditionalProperties().length > 0) {
			for (String propertyName : interrupter.getAdditionalProperties()) {
				PropertyDefinition pd = context.getProperty(propertyName);
				if (pd != null && !propertyList.contains(propertyName)) {
					propertyList.add(propertyName);
				}
			}
		}
		return propertyList.toArray(new String[propertyList.size()]);
	}

	/**
	 * 登録前処理を行います。
	 * 
	 * @param entity           登録用のデータ
	 * @param registrationType 登録処理の種類
	 * @return 入力エラーリスト
	 */
	public List<ValidateError> beforeRegister(Entity entity, RegistrationType registrationType) {
		List<ValidateError> ret = interrupter.beforeRegister(entity, request, context.getEntityDefinition(),
				context.getView(), registrationType);
		if (ret == null) {
			ret = Collections.emptyList();
		}
		return ret;
	}

	/**
	 * 登録後処理を行います。
	 * 
	 * @param entity           登録用のデータ
	 * @param registrationType 登録処理の種類
	 * @return 入力エラーリスト
	 */
	public List<ValidateError> afterRegister(Entity entity, RegistrationType registType) {
		List<ValidateError> ret = interrupter.afterRegister(entity, request, context.getEntityDefinition(),
				context.getView(), registType);
		if (ret == null) {
			ret = Collections.emptyList();
		}
		return ret;
	}

	/**
	 * NestTableの更新オプションを取得します。
	 * 
	 * @param ed              Entity定義
	 * @param refPropertyName 参照プロパティ名
	 * @return NestTableの更新オプション
	 */
	public ReferenceRegistOption getNestTableRegistOption(EntityDefinition ed, String refPropertyName) {
		ReferenceRegistOption option = new ReferenceRegistOption();

		option.setSpecifyAllProperties(interrupter.isSpecifyAllProperties());

		// 更新対象のプロパティが指定された場合
		if (interrupter.getAdditionalProperties() != null && interrupter.getAdditionalProperties().length > 0) {
			for (String addtionalProperty : interrupter.getAdditionalProperties()) {
				if (addtionalProperty.equals(refPropertyName)) {
					option.setSpecifiedAsReference(true);
				} else if (addtionalProperty.startsWith(refPropertyName + ".")) {
					String refEntityProp = addtionalProperty.substring(addtionalProperty.indexOf(".") + 1);
					PropertyDefinition pd = ed.getProperty(refEntityProp);
					if (pd != null && pd.isUpdatable()) {
						option.getSpecifiedUpdateNestProperties().add(refEntityProp);
					}
				}
			}
		}

		return option;
	}
}
