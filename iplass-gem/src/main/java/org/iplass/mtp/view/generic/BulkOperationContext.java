/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;

/**
 *  BulkOperationInterrupterの実行結果を保持するContextです。
 *
 */
public class BulkOperationContext {

	/**
	 * バリデーションエラー
	 */
	private List<ValidateError> errors;

	/**
	 * 一括操作対象エンティティリスト
	 */
	private List<Entity> entities;


	public BulkOperationContext(List<Entity> entities) {
		this.entities = entities;
	}

	public BulkOperationContext(List<Entity> entities, List<ValidateError> errors) {
		this.errors = errors;
		this.entities = entities;
	}

	/**
	 * バリデーションエラーリストを取得します。
	 * @return バリデーションエラー
	 */
	public List<ValidateError> getErrors() {
		if (errors == null) {
			errors = new ArrayList<ValidateError>();
		}
		return errors;
	}

	/**
	 * バリデーションエラーを設定します。
	 * @param errors バリデーションエラー
	 */
	public void setErrors(List<ValidateError> errors) {
		this.errors = errors;
	}

	public void addError(ValidateError error) {
		for (ValidateError e : getErrors()) {
			//同じプロパティのものがあればメッセージだけ追加
			if (e.getPropertyName().equals(error.getPropertyName())) {
				e.getErrorMessages().addAll(error.getErrorMessages());
				return;
			}
		}

		//同じプロパティがない場合はエラー自体を追加
		getErrors().add(error);
	}

	/**
	 * 一括操作対象エンティティリストを取得します。
	 * @return 一括操作対象エンティティリスト
	 */
	public List<Entity> getEntities() {
		if (entities == null) {
			entities = new ArrayList<Entity>();
		}
		return entities;
	}

	/**
	 * 一括操作対象エンティティリストを設定します。
	 * @param entities 一括操作対象エンティティリスト
	 */
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
}
