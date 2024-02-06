/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.binary;

import java.util.List;

import org.iplass.gem.BinaryDownloadLoggingTargetProperty;
import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * バイナリデータダウンロード用コマンド
 * @author lis3wg
 */
@ActionMappings({
@ActionMapping(
	name=DownloadCommand.DOWNLOAD_ACTION_NAME,
	displayName="バイナリダウンロード",
	result=@Result(type=Type.STREAM, useContentDisposition=true, acceptRanges=true)
),
@ActionMapping(
	name=DownloadCommand.REFERENCE_ACTION_NAME,
	displayName="バイナリ参照",
	result=@Result(type=Type.STREAM, useContentDisposition=true, contentDispositionType=ContentDispositionType.INLINE, acceptRanges=true)
),
@ActionMapping(
	name=DownloadCommand.PDFVIEWER_ACTION_NAME,
	displayName="PDFビューア",
	command={},
	result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_HTML_PDFVIEWER_PATH, templateName="gem/binary/pdfviewer",
	useContentDisposition=true, contentDispositionType=ContentDispositionType.INLINE, acceptRanges=true)
),
@ActionMapping(
	name=DownloadCommand.IMGVIEWER_ACTION_NAME,
	displayName="画像ビューア",
	result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_HTML_IMGVIEWER_PATH, templateName="gem/binary/imgviewer",
	useContentDisposition=true, contentDispositionType=ContentDispositionType.INLINE, acceptRanges=true)
)
})
@CommandClass(name="gem/binary/DownloadCommand", displayName="ダウンロード")
public final class DownloadCommand implements Command {

	public static final String DOWNLOAD_ACTION_NAME = "gem/binary/download";
	public static final String REFERENCE_ACTION_NAME = "gem/binary/ref";
	public static final String PDFVIEWER_ACTION_NAME = "gem/binary/pdfviewer";
	public static final String IMGVIEWER_ACTION_NAME = "gem/binary/imgviewer";

	private GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	private static final Logger auditLogger = LoggerFactory.getLogger("mtp.audit.download");

	@Override
	public String execute(RequestContext request) {

		Long lobId = request.getParamAsLong(Constants.ID);
		String defName = request.getParam(Constants.DEF_NAME);
		String propName = request.getParam(Constants.PROP_NAME);

		if (defName != null && propName != null) {
			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			BinaryReference br = em.loadBinaryReference(lobId);

			List<BinaryDownloadLoggingTargetProperty> targetProperties = service.getBinaryDownloadLoggingTargetProperty();
			for (BinaryDownloadLoggingTargetProperty targetProperty : targetProperties) {
				if (defName.equals(targetProperty.getEntityName()) && propName.equals(targetProperty.getPropertyName())) {
					// ダウンロード用ログの出力（操作名称,ファイル名,条件）
					auditLogger.info("BinaryPropertyDataDownload," + br.getName() + ",lobId:" + lobId + " entityName:" + defName + " propertyName:" + propName);
				}
			}

			if (br != null
					&& defName.equals(br.getDefinitionName())
					&& propName.equals(br.getPropertyName())) {
				request.setAttribute(Constants.CMD_RSLT_STREAM, br);
			}
		} else {
			SessionContext session = request.getSession(false);
			if (session != null) {
				request.setAttribute(Constants.CMD_RSLT_STREAM, session.loadFromTemporary(lobId));
			}
		}
		return Constants.CMD_EXEC_SUCCESS;
	}

}
