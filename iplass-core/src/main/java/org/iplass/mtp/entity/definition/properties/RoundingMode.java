/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.properties;

/**
 * DecimalPropertyにおける、数値の丸め方式。
 *
 * @see java.math.RoundingMode
 * @author K.Higuchi
 *
 */
public enum RoundingMode {
	//gwtでjava.math.RoundingModeサポートしてないので、独自にenum定義
	/** 0から離れるようにする(正数切り上げ/負数切り上げ) */
	UP,
	/** 0に近づける(正数切り下げ/負数切り下げ) */
    DOWN,
	/** 正の無限大に近づける(正数切り上げ/負数切り下げ) */
    CEILING,
	/** 負の無限大に近づける(正数切り下げ/負数切り上げ) */
    FLOOR,
    /** 四捨五入 */
    HALF_UP,
    /** 五捨六入 */
    HALF_DOWN,
    /** 銀行型丸め */
    HALF_EVEN
}
