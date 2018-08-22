/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.lang;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LangDataPortingService implements Service {

	private static Logger logger = LoggerFactory.getLogger(LangDataPortingService.class);

	private DefinitionService definitionService;

	@Override
	public void init(Config config) {
		definitionService = config.getDependentService(DefinitionService.class);
	}

	@Override
	public void destroy() {
	}

	/**
	 * パスを元に定義情報を取得
	 * @param paths
	 * @return
	 */
	public List<LangDataPortingInfo> getDefinitionInfo(List<String> paths) {
		List<LangDataPortingInfo> infoList = new ArrayList<LangDataPortingInfo>();
		for (String path : paths) {
			LangDataPortingInfo info = getLangDataPortingInfo(path);
			if (info == null) {
				continue;
			}

			infoList.add(info);
		}
		return infoList;
	}

	/**
	 * パスを元に定義情報を取得
	 * @param path
	 * @return
	 */
	public LangDataPortingInfo getLangDataPortingInfo(String path) {
		String replacedPath = definitionService.getPath(path);
		logger.debug("path(" + path + ") to (" + replacedPath + ")");

		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(replacedPath);
		if (entry == null) {
			logger.warn("metadata " + replacedPath + " is not found.");
			return null;
		}

		LangDataPortingInfo info = null;
		MetaData meta = entry.getMetaData();
		if (meta instanceof RootMetaData && meta instanceof DefinableMetaData<?>) {
			info = currentConfig((RootMetaData) meta);
			if (info.getDefinition() == null) return null;
		}

		return info;
	}

	/**
	 * メタデータから定義を取得
	 * @param meta
	 * @return
	 */
	private LangDataPortingInfo currentConfig(RootMetaData meta) {
		//definition取得時にcontextPathを判別しておく
		Definition definition = definitionService.toDefinition(meta);
		String name = meta.getName();
		String contextPath = definitionService.getPrefixPath(definition.getClass());

		LangDataPortingInfo info = new LangDataPortingInfo();
		info.setDefinition(definition);
		info.setContextPath(contextPath);
		info.setName(name);

		return info;
	}

}
