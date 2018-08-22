/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.top;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.TopViewDefinitionModifyResult;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * TOP画面定義を管理するクラス
 * @author lis3wg
 */
@SuppressWarnings("deprecation")
public class TopViewDefinitionManagerImpl extends AbstractTypedDefinitionManager<TopViewDefinition> implements TopViewDefinitionManager {

	/** サービス */
	private TopViewDefinitionService service = ServiceRegistry.getRegistry().getService(TopViewDefinitionService.class);

	@Deprecated
	@Override
	public TopViewDefinitionModifyResult delete(String name) {
		return new TopViewDefinitionModifyResult(remove(name).isSuccess());
	}

	@Override
	public void loadParts(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) {
		String name = getRollName();
		TopViewHandler handler = service.getRuntimeByName(name);
		if (handler == null) return;
		handler.loadParts(req, res, application, page);
	}

	@Override
	public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) {
		String name = getRollName();
		TopViewHandler handler = service.getRuntimeByName(name);
		if (handler == null) return;
		handler.loadWidgets(req, res, application, page);
	}

	/**
	 * ロール名を取得します。
	 * @return
	 */
	private String getRollName() {
		RequestContext context = TemplateUtil.getRequestContext();
		String roleName = (String) context.getAttribute("roleName");
		if (roleName == null || roleName.isEmpty()) roleName = "DEFAULT";
		return roleName;
	}

	@Override
	public Class<TopViewDefinition> getDefinitionType() {
		return TopViewDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(TopViewDefinition definition) {
		return new MetaTopView();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
