/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.normalizer;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.ICUTransliterator;
import org.iplass.mtp.entity.definition.normalizers.JavaClassNormalizer;
import org.iplass.mtp.entity.definition.normalizers.RegexReplace;
import org.iplass.mtp.entity.definition.normalizers.ScriptingNormalizer;
import org.iplass.mtp.entity.definition.normalizers.UnicodeNormalizer;
import org.iplass.mtp.entity.definition.normalizers.WhiteSpaceTrimmer;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class NormalizerService implements Service {
	
	private Map<Class<? extends NormalizerDefinition>, Class<? extends MetaNormalizer>> map;

	@Override
	public void init(Config config) {
		map = new HashMap<>();
		map.put(ICUTransliterator.class, MetaICUTransliterator.class);
		map.put(JavaClassNormalizer.class, MetaJavaClassNormalizer.class);
		map.put(RegexReplace.class, MetaRegexReplace.class);
		map.put(ScriptingNormalizer.class, MetaScriptingNormalizer.class);
		map.put(UnicodeNormalizer.class, MetaUnicodeNormalizer.class);
		map.put(WhiteSpaceTrimmer.class, MetaWhiteSpaceTrimmer.class);
	}

	@Override
	public void destroy() {
	}

	public MetaNormalizer createNormalizerMetaData(NormalizerDefinition n) {
		try {
			MetaNormalizer meta = map.get(n.getClass()).newInstance();
			meta.applyConfig(n);
			return meta;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
