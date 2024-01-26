/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.validations;

import org.iplass.mtp.entity.definition.ValidationDefinition;

/**
 * Referenceプロパティの存在チェックを行うValidation。
 * 参照先のEntityが存在しない場合、エラー。
 */
public class ExistsValidation extends ValidationDefinition {

	private static final long serialVersionUID = 7454625855150871109L;

}
