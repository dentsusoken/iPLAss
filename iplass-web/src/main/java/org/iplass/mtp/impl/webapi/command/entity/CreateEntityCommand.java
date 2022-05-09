/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.entity;

import java.io.IOException;
import java.io.InputStream;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.RestXml;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.csv.CsvUploadService;
import org.iplass.mtp.impl.csv.CsvUploadStatus;
import org.iplass.mtp.impl.csv.TransactionType;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.fileupload.MultiPartParameterValueMap;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(name="mtp/entity/POST",
		accepts={RequestType.REST_JSON, RequestType.REST_XML, RequestType.REST_FORM},
		methods={MethodType.POST},
		restJson=@RestJson(parameterName=CreateEntityCommand.PARAM_ENTITY, parameterType=Entity.class),
		restXml=@RestXml(parameterName=CreateEntityCommand.PARAM_ENTITY),
		results={CreateEntityCommand.RESULT_OID,
				CreateEntityCommand.RESULT_UPLOAD_INSERT, CreateEntityCommand.RESULT_UPLOAD_UPDATE, CreateEntityCommand.RESULT_UPLOAD_DELETE},
		overwritable=false)
@CommandClass(name="mtp/entity/CreateEntityCommand", displayName="Entity Create Web API", overwritable=false)
public final class CreateEntityCommand extends AbstractEntityCommand {

	public static final String PARAM_ENTITY = "entity";
	public static final String RESULT_OID = "oid";

	public static final String PARAM_UPLOAD_FILE = "uploadFile";
	public static final String PARAM_UPLOAD_UNIQUE_KEY = "uniqueKey";
	public static final String PARAM_UPLOAD_ASYNC = "asyncUpload";

	public static final String RESULT_UPLOAD_INSERT = "insert";
	public static final String RESULT_UPLOAD_UPDATE = "update";
	public static final String RESULT_UPLOAD_DELETE = "delete";

	//entity/[defName]/
	//entity/[defName]/ multipart 一括アップロード（csv）

	@Override
	public String executeImpl(RequestContext request, String[] subPath) {
		if (subPath == null || subPath.length != 1) {
			throw new IllegalArgumentException("illegal path parameter:" + subPath);
		}

		if (isMultipart(request)) {
			doCsvUpload(request, subPath[0]);
		} else {
			doInsert(request, subPath[0]);
		}

		return CMD_EXEC_SUCCESS;
	}

	private boolean isMultipart(RequestContext request) {
		return (request instanceof WebRequestContext
				&& ((WebRequestContext)request).getValueMap() instanceof MultiPartParameterValueMap);
	}

	private void doInsert(RequestContext request, String defName) {

		checkPermission(defName, def -> def.getMetaData().isInsert());

		Entity e = (Entity) request.getAttribute(PARAM_ENTITY);
		e.setDefinitionName(defName);

		String oid = em.insert(e);
		request.setAttribute(RESULT_OID, oid);
	}

	private void doCsvUpload(RequestContext request, String defName) {

		checkPermission(defName, def -> def.getMetaData().isUpdate());

		UploadFileHandle file = request.getParamAsFile(PARAM_UPLOAD_FILE);
		String uniqueKey = StringUtil.stripToNull(request.getParam(PARAM_UPLOAD_UNIQUE_KEY));
		boolean async = request.getParam(PARAM_UPLOAD_ASYNC, Boolean.class, false);

		if (file == null) {
			throw new IllegalArgumentException("illegal parameter:" + PARAM_UPLOAD_FILE);
		}

		CsvUploadService service = ServiceRegistry.getRegistry().getService(CsvUploadService.class);

		if (async) {
			// 非同期アップロード(トランザクション分割なし)
			try (InputStream is = file.getInputStream()) {
				service.asyncUpload(is, file.getFileName(), defName, null, uniqueKey, false, false, false, null, TransactionType.ONCE, 0, true, true);
			} catch (IOException e) {
				throw new SystemException(e);
			}
		} else {
			// ファイルの整合性チェック
			try (InputStream is = file.getInputStream()) {
				service.validate(is, defName, true);
			} catch (IOException e) {
				throw new SystemException(e);
			}

			// 同期アップロード(トランザクション分割なし)
			try (InputStream is = file.getInputStream()) {
				CsvUploadStatus result = service.upload(is, defName, uniqueKey, false, false, false, null, TransactionType.ONCE, 0, true, true);
				if (result.getStatus() != TaskStatus.COMPLETED) {
					throw new EntityCsvException(result.getCode(), result.getMessage());
				}

				request.setAttribute(RESULT_UPLOAD_INSERT, result.getInsertCount());
				request.setAttribute(RESULT_UPLOAD_UPDATE, result.getUpdateCount());
				request.setAttribute(RESULT_UPLOAD_DELETE, result.getDeleteCount());
			} catch (IOException e) {
				throw new SystemException(e);
			}

		}
	}

}
