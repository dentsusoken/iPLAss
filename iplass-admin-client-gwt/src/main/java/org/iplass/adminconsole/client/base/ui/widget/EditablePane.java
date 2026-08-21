/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.widget;

/**
 * 編集可能な部品の共通インターフェース
 *
 * @param <D> 対象のDefinition
 */
public interface EditablePane<D> {

	/**
	 * 編集対象のDefinitionを設定します。
	 *
	 * @param definition 編集対象のDefinition
	 */
	void setDefinition(D definition);

	/**
	 * 編集結果をDefinitionに反映します。
	 *
	 * @param definition 編集対象のDefinition
	 * @return 編集結果
	 */
	D getEditDefinition(D definition);

	/**
	 * 編集値を検証します。
	 *
	 * @return 検証結果
	 */
	boolean validate();

	/**
	 * エラー情報をクリアします。
	 */
	void clearErrors();

}
