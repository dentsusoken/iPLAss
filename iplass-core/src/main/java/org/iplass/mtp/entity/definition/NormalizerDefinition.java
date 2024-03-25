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

package org.iplass.mtp.entity.definition;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.normalizers.ICUTransliterator;
import org.iplass.mtp.entity.definition.normalizers.JavaClassNormalizer;
import org.iplass.mtp.entity.definition.normalizers.NewlineNormalizer;
import org.iplass.mtp.entity.definition.normalizers.RegexReplace;
import org.iplass.mtp.entity.definition.normalizers.ScriptingNormalizer;
import org.iplass.mtp.entity.definition.normalizers.UnicodeNormalizer;
import org.iplass.mtp.entity.definition.normalizers.WhiteSpaceTrimmer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * <% if (doclang == "ja") {%>
 * プロパティの正規化処理の定義。
 * <%} else {%>
 * Definition of property normalization process.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
		ICUTransliterator.class,
		JavaClassNormalizer.class,
		NewlineNormalizer.class,
		RegexReplace.class,
		ScriptingNormalizer.class,
		UnicodeNormalizer.class,
		WhiteSpaceTrimmer.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class NormalizerDefinition implements Serializable {
	private static final long serialVersionUID = -5463919265806923883L;
}
