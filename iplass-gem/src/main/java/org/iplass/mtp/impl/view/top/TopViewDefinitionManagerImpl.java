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

package org.iplass.mtp.impl.view.top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.PageContext;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.view.top.parts.TopViewPartsHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.TopViewDefinitionModifyResult;
import org.iplass.mtp.view.top.parts.HasNestParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
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
		TopViewHandler handler = getRequestTopViewHandler();
		if (handler == null) return;
		handler.loadParts(req, res, application, page);
	}

	@Override
	public void loadWidgets(HttpServletRequest req, HttpServletResponse res,
			ServletContext application, PageContext page) {
		TopViewHandler handler = getRequestTopViewHandler();
		if (handler == null) return;
		handler.loadWidgets(req, res, application, page);
	}

	@Override
	public TopViewDefinition getRequestTopView() {
		TopViewHandler handler = getRequestTopViewHandler();
		if (handler == null) return null;
		return handler.getMetaData().currentConfig();
	}

	@Override
	public <T extends TopViewParts> T getRequestTopViewParts(Class<T> type) {
		return getTopViewParts(getRequestTopView(), type);
	}

	@Override
	public <T extends TopViewParts> List<T> getRequestTopViewPartsList(Class<T> type) {
		return getTopViewPartsList(getRequestTopView(), type);
	}

	@Override
	public <T extends TopViewPartsHandler> T getRequestTopViewPartsHandler(Class<T> type) {
		return getTopViewPartsHandler(getRequestTopViewHandler(), type);
	}

	@Override
	public <T extends TopViewPartsHandler> List<T> getRequestTopViewPartsHandlerList(Class<T> type) {
		return getTopViewPartsHandlerList(getRequestTopViewHandler(), type);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TopViewParts> T getTopViewParts(TopViewDefinition definition, Class<T> type) {
		if (definition != null) {
			Optional<T> typeParts = getAllParts(definition).stream()
					.filter(type::isInstance)
					.map(part -> (T)part)
					.findFirst();
			return typeParts.isPresent() ? typeParts.get() : null;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TopViewParts> List<T> getTopViewPartsList(TopViewDefinition definition, Class<T> type) {
		if (definition != null) {
			List<T> typeParts = getAllParts(definition).stream()
					.filter(type::isInstance)
					.map(part -> (T)part)
					.collect(Collectors.toList());
			return typeParts;
		}
		return Collections.emptyList();
	}

	private List<TopViewParts> getAllParts(TopViewDefinition definition) {
		if (definition != null) {
			final List<TopViewParts> allParts = new ArrayList<>();
			definition.getParts().forEach(parts -> {
				allParts.add(parts);
				if (parts instanceof HasNestParts) {
					allParts.addAll(((HasNestParts)parts).getNestParts());
				}
			});
			return allParts;
		}
		return Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TopViewPartsHandler> T getTopViewPartsHandler(TopViewHandler handler, Class<T> type) {
		if (handler != null) {
			Optional<T> typeParts = handler.getAllParts().stream()
					.filter(type::isInstance)
					.map(part -> (T)part)
					.findFirst();
			return typeParts.isPresent() ? typeParts.get() : null;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TopViewPartsHandler> List<T> getTopViewPartsHandlerList(TopViewHandler handler, Class<T> type) {
		if (handler != null) {
			List<T> typeParts = handler.getAllParts().stream()
					.filter(type::isInstance)
					.map(part -> (T)part)
					.collect(Collectors.toList());
			return typeParts;
		}
		return Collections.emptyList();
	}

	/**
	 * ロール名を取得します。
	 * @return
	 */
	private String getRollName() {
		RequestContext context = TemplateUtil.getRequestContext();
		String roleName = (String) context.getSession().getAttribute("roleName");
		if (roleName == null || roleName.isEmpty()) roleName = "DEFAULT";
		return roleName;
	}

	private TopViewHandler getRequestTopViewHandler() {
		String name = getRollName();
		return service.getRuntimeByName(name);
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
