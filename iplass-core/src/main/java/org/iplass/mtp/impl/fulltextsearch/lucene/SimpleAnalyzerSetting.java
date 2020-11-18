/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch.lucene;

import java.lang.reflect.InvocationTargetException;

import org.apache.lucene.analysis.Analyzer;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

public class SimpleAnalyzerSetting implements AnalyzerSetting, ServiceInitListener<LuceneFulltextSearchService> {

	private String className;
	
	private Analyzer analyzer;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public Analyzer getAnalyzer(int tenantId, String entityDefName) {
		return analyzer;
	}
	
	@Override
	public void inited(LuceneFulltextSearchService service, Config config) {
		if (service.isUseFulltextSearch()) {
			try {
				analyzer = (Analyzer) Class.forName(className).getConstructor().newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				throw new FulltextSearchRuntimeException("can not instantiate Analyzer class: " + className, e1);
			}
		}
	}
	
	@Override
	public void destroyed() {
		if (analyzer != null) {
			analyzer.close();
		}
	}
}
