/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.impl.webapi.command.Constants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebApi(
		name = "mtp/bin/GET",
		accepts = { RequestType.REST_FORM },
		methods = { MethodType.GET },
		supportBearerToken = true,
		overwritable = false
		)
@WebApi(
		name = "mtp/bin/POST",
		accepts = { RequestType.REST_FORM },
		methods = { MethodType.POST },
		results = { BinaryCommand.RESULT_LOB_ID },
		supportBearerToken = true,
		overwritable = false
		)
@CommandClass(name = "mtp/binary/BinaryCommand", displayName = "Binary Web API", overwritable = false)
public final class BinaryCommand implements Command, Constants {
	private static Logger logger = LoggerFactory.getLogger(BinaryCommand.class);

	public static final String PARAM_UPLOAD_FILE = "uploadFile";

	public static final String RESULT_LOB_ID = "lobId";

	DefinitionService service = DefinitionService.getInstance();
	WebApiService webApiService = ServiceRegistry.getRegistry().getService(WebApiService.class);

	@Override
	public String execute(RequestContext request) {
		if (!webApiService.isEnableBinaryApi()) {
			logger.warn("bin web api is disabled on WebApiService configration.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		MethodType method = (MethodType) request.getAttribute(WebApiRequestConstants.HTTP_METHOD);

		switch (method) {
		case GET:
			return doGet(request);
		case POST:
			return doPost(request);
		default:
			throw new WebApplicationException(Status.METHOD_NOT_ALLOWED);
		}
	}

	private String doPost(RequestContext request) {
		UploadFileHandle file = request.getParamAsFile(PARAM_UPLOAD_FILE);

		// ファイルのtypeを確認する
		if (null != file && isRejectMimeTypes(file.getType())) {
			logger.error("File upload rejected. fileName = {}, fileType = {}, fileSize = {}.", file.getFileName(), file.getType(),
					file.getSize());
			throw new WebApplicationException(Status.UNSUPPORTED_MEDIA_TYPE);
		}

		BinaryReference br = file.toBinaryReference();
		request.setAttribute(RESULT_LOB_ID, br.getLobId());

		return CMD_EXEC_SUCCESS;
	}

	// api/bin/[lobId]
	private String doGet(RequestContext request) {
		String subPath = (String) request.getAttribute(WebRequestConstants.SUB_PATH);
		if (subPath != null && subPath.startsWith("/")) {
			subPath = subPath.substring(1);
		}
		long lobId = Long.parseLong(subPath);

		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		BinaryReference br = em.loadBinaryReference(lobId);
		if (br == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		InputStream stream = em.getInputStream(br);
		if (stream == null) {
			logger.error("no content");
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		ResponseBuilder rb = Response.ok(stream, br.getType());
		if (br.getType() != null && br.getType().length() != 0) {
			rb.type(br.getType());
		}
		if (br.getSize() != 0) {
			rb.header("Content-Length", br.getSize());
		}
		if (br.getName() != null && br.getName().length() != 0) {
			if (webApiService.isWriteEncodedFilenameInBinaryApi()) {
				String encodedFileName;
				try {
					encodedFileName = getEncodedFileName(br.getName());
				} catch (IOException e) {
					throw new SystemException(e);
				}
				rb.header("Content-Disposition",
						String.format("attachment;filename*=%s''%s", StandardCharsets.UTF_8.name(), encodedFileName));
			} else {
				rb.header("Content-Disposition", "attachment;filename=" + br.getName());
			}
		}
		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, rb);

		return CMD_EXEC_SUCCESS;
	}

	private String getEncodedFileName(String fileName) throws IOException {
		byte[] source = fileName.getBytes(StandardCharsets.UTF_8.name());

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length)) {
			for (byte b : source) {
				if (b < 0) {
					b += 256;
				}
				if (isUnescape(b)) {
					bos.write(b);
				} else {
					bos.write('%');
					char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
					char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
					bos.write(hex1);
					bos.write(hex2);
				}
			}
			return new String(bos.toByteArray(), "US-ASCII");
		}
	}

	private boolean isUnescape(int ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
			return true;
		}
		if (ch >= '0' && ch <= '9') {
			return true;
		}

		if (webApiService.getUnescapeFilenameCharacterInBinaryApi() != null) {
			return webApiService.getUnescapeFilenameCharacterInBinaryApi().indexOf(ch) >= 0;
		}
		return false;
	}

	/**
	 * アップロードファイルは拒否される MIME Type（メディアタイプ）であるか確認する
	 *
	 * @param type MIME Type（メディアタイプ）
	 * @return 判定結果（拒否される場合 true ）
	 */
	private boolean isRejectMimeTypes(String type) {
		boolean isAccept = true;

		WebApiService service = ServiceRegistry.getRegistry().getService(WebApiService.class);

		if (null != service.getAcceptMimeTypesPatternInBinaryApi()) {
			// WebApiServiceの受け入れ許可設定が存在する場合は、WebApiService の設定で許可設定を行う
			isAccept = service.getAcceptMimeTypesPatternInBinaryApi().matcher(type).matches();
		}
		// else {
		// // WebApiService に設定が無い場合はチェックしない
		// }

		// 拒否判定の為、accept を反転する
		return !isAccept;
	}
}
