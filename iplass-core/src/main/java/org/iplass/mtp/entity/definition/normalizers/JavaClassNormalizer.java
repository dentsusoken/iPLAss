/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.normalizers;

import org.iplass.mtp.entity.PropertyNormalizer;
import org.iplass.mtp.entity.definition.NormalizerDefinition;

/**
 * <% if (doclang == "ja") {%>
 * JavaのクラスによるNormalizer定義です。
 * {@link PropertyNormalizer}の実装クラスを指定します。
 * asArrayフラグがtrueにセットされる場合、正規化対象が配列の場合、分解せず配列のままPropertyNormalizerのvalueへ渡します。
 * <%} else {%>
 * This is the Normalizer definition by Java class implementation.
 * Specify the implementation class of {@link PropertyNormalizer}.
 * When the asArray flag is set to true, if the normalization target is an array,
 * it will be passed as an array to the value of PropertyNormalizer.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class JavaClassNormalizer extends NormalizerDefinition {
	private static final long serialVersionUID = 573541326551088096L;

	private String className;
	private boolean asArray = false;

	public JavaClassNormalizer() {
	}

	public JavaClassNormalizer(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}

}
