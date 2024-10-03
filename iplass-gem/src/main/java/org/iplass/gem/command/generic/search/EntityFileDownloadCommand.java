/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.gem.command.generic.search;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.web.ResultStreamWriter;
import org.iplass.mtp.web.actionmapping.ActionUtil;
import org.iplass.mtp.web.actionmapping.ResponseHeader;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.Cookie;

@ActionMapping(
		name=EntityFileDownloadCommand.ACTION_NAME,
		result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.STREAM, useContentDisposition=true),
				@Result(status=Constants.CMD_EXEC_ERROR_PARAMETER, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
		}
)
@CommandClass(name="gem/generic/search/EntityFileDownloadCommand", displayName="エンティティファイルダウンロード")
public final class EntityFileDownloadCommand  implements Command {

	public static final String ACTION_NAME = "gem/generic/search/download";

	private static final Logger auditLogger = LoggerFactory.getLogger("mtp.audit.download");

	private static final String ENTITY_NAME_BINDING_NAME = "entityName";
	private static final String ENTITY_DISP_BINDING_NAME = "entityDisplayName";
	private static final String VIEW_NAME_BINDING_NAME = "viewName";

	private final EntityDefinitionManager edm;
	private final EntityViewManager evm;

	public EntityFileDownloadCommand() {
		edm = ManagerLocator.manager(EntityDefinitionManager.class);
		evm = ManagerLocator.manager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {

		EntityFileDownloadSearchContext context = getContext(request);

		if (!context.checkParameter()) {
			return Constants.CMD_EXEC_ERROR_PARAMETER;
		}

		SearchConditionSection section = context.getConditionSection();
		section.setNonOutputBinaryRef(section.isNonOutputBinaryRef());
		section.setNonOutputReference(section.isNonOutputReference());
		section.setNonOutputOid(section.isNonOutputOid());

		EntityDefinition ed = context.getEntityDefinition();

//		ResultStreamWriter writer = null;
//		if (context.isForUpload()) {
//			writer = new EntityFileDownloadUploadableWriter(context);
//		} else {
//			writer = new EntityFileDownloadSearchViewWriter(context);
//		}
		ResultStreamWriter writer = context.createWriter();

		// 保存ファイル名
		Map<String, Object> fileNameVariableMap = new HashMap<>();
		fileNameVariableMap.put(ENTITY_NAME_BINDING_NAME, ed.getName());
		fileNameVariableMap.put(ENTITY_DISP_BINDING_NAME, TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList()));
		fileNameVariableMap.put(VIEW_NAME_BINDING_NAME, StringUtil.isEmpty(context.getViewName()) ? null : context.getViewName());
		String defaultName = TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
		String filename = evm.getEntityDownloadFileName(context.getDefName(), context.getViewName(), defaultName, fileNameVariableMap) + context.getFileExtension();

		auditLogger.info("EntityFileDownload," + filename + "," + context.getDefName() + " " + context.getWhere() + " isForUpload:" + context.isForUpload());

		request.setAttribute(Constants.CMD_RSLT_STREAM_FILENAME, filename);
		request.setAttribute(Constants.CMD_RSLT_STREAM_CONTENT_TYPE, context.getFileContentType());

		// ダウンロード用の ResultStreamWriterを設定
		request.setAttribute(Constants.CMD_RSLT_STREAM, writer);

		String token = request.getParam(Constants.FILE_DOWNLOAD_TOKEN);
		if (StringUtil.isNotBlank(token)) {
			//サブミットされたトークン(時刻)をそのままCookieに設定、ブラウザでCookieを監視し、DLを検知させる
			ResponseHeader header = ActionUtil.getResponseHeader();
			Cookie cookie = new Cookie(Constants.FILE_DOWNLOAD_TOKEN, token);
			String path = TemplateUtil.getTenantContextPath();
			if (StringUtil.isBlank(path)) {
				path = "/";
			}
			cookie.setPath(path);
			header.addCookie(cookie);
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	private EntityFileDownloadSearchContext getContext(RequestContext request) {
		SearchContext context = null;

		String searchType = request.getParam(Constants.SEARCH_TYPE);

		SearchCommandBase command = null;
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = new NormalSearchCommand();
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = new DetailSearchCommand();
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = new FixedSearchCommand();
		}
		try {
			context = command.getContextClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new EntityRuntimeException(resourceString("command.generic.search.EntityFileDownloadCommand.internalErr"), e);
		}

		if (context != null) {
			context.setRequest(request);
			context.setEntityDefinition(edm.get(context.getDefName()));
			context.setEntityView(evm.get(context.getDefName()));
		}

		return EntityFileDownloadSearchContext.getContext((SearchContextBase) context);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
