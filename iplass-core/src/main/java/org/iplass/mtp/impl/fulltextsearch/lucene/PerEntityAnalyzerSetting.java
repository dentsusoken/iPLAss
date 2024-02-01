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

package org.iplass.mtp.impl.fulltextsearch.lucene;

import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.iplass.mtp.spi.Config;

public class PerEntityAnalyzerSetting implements AnalyzerSetting {
	
	private AnalyzerSetting defaultSetting;
	private Map<String, AnalyzerSetting> settingsPerEntity;
	
	public AnalyzerSetting getDefaultSetting() {
		return defaultSetting;
	}

	public void setDefaultSetting(AnalyzerSetting defaultSetting) {
		this.defaultSetting = defaultSetting;
	}

	public Map<String, AnalyzerSetting> getSettingsPerEntity() {
		return settingsPerEntity;
	}

	public void setSettingsPerEntity(Map<String, AnalyzerSetting> settingsPerEntity) {
		this.settingsPerEntity = settingsPerEntity;
	}

	@Override
	public void inited(LuceneFulltextSearchService service, Config config) {
		if (defaultSetting != null) {
			defaultSetting.inited(service, config);
		}
		if (settingsPerEntity != null) {
			for (AnalyzerSetting as: settingsPerEntity.values()) {
				as.inited(service, config);
			}
		}
	}

	@Override
	public void destroyed() {
		if (defaultSetting != null) {
			defaultSetting.destroyed();
		}
		if (settingsPerEntity != null) {
			for (AnalyzerSetting as: settingsPerEntity.values()) {
				as.destroyed();
			}
		}
	}

	@Override
	public Analyzer getAnalyzer(int tenantId, String entityDefName) {
		AnalyzerSetting as = null;
		if (settingsPerEntity != null) {
			as = settingsPerEntity.get(entityDefName);
		}
		
		if (as == null) {
			as = defaultSetting;
		}
		
		return as.getAnalyzer(tenantId, entityDefName);
	}

}
