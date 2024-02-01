/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.tenant.create;

import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.impl.view.top.MetaTopView;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.parts.InformationParts;

public class CreateTopViewProcess implements TenantCreateProcess {

	/** 初期TopView用の定義名 */
	private static final String DEFAULT_TOP_VIEW = "DEFAULT";

	private KeyGenerator generator = new KeyGenerator();

	@Override
	public boolean execute(TenantCreateParameter param, LogHandler logHandler) {

		TopViewDefinition definition = new TopViewDefinition();
		definition.setName(DEFAULT_TOP_VIEW);
		definition.setDisplayName(DEFAULT_TOP_VIEW);
		definition.addParts(new InformationParts());

		//ReloadさせないためMetaとして登録
		MetaTopView meta = new MetaTopView();
		meta.applyConfig(definition);
		meta.setId(generator.generateId());

		String path = DefinitionService.getInstance().getPathByMeta(MetaTopView.class, meta.getName());
		try {
			//Reloadしない
			MetaDataContext.getContext().store(path, meta, null, false);
		} catch (Exception e) {
			String type = meta.getClass().getSimpleName();
			if (e.getCause() != null) {
				throw new RuntimeException("exception occured during " + type + " create:" + e.getCause().getMessage());
			} else {
				throw new RuntimeException("exception occured during " + type + " create:" + e.getMessage());
			}
		}

		logHandler.info(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.createdTopViewMsg", definition.getName()));

		return true;

	}

}
