/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.definition;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiResultAttribute;
import org.iplass.mtp.impl.definition.DefinitionPath;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.impl.webapi.command.Constants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@WebApi(
		name = "mtp/definition/checkStatus/GET",
		accepts = { RequestType.REST_FORM },
		methods = { MethodType.GET },
		responseResults = {
				@WebApiResultAttribute(name = DefinitionStatusCheckCommand.RESULT_STATE, dataType = String.class),
				@WebApiResultAttribute(name = DefinitionStatusCheckCommand.RESULT_MESSAGE, dataType = String.class),
				@WebApiResultAttribute(name = DefinitionStatusCheckCommand.RESULT_STACK_TRACE, dataType = String.class) },
		supportBearerToken = true,
		overwritable = false)
@CommandClass(name = "mtp/definition/DefinitionStatusCheckCommand", displayName = "Definition Status Check Web API", overwritable = false)
public final class DefinitionStatusCheckCommand implements Command, Constants {
	private static final Logger logger = LoggerFactory.getLogger(DefinitionStatusCheckCommand.class);

	/** メタデータの状態（SUCCESS / ILLEGAL / NOT_FOUND） */
	public static final String RESULT_STATE = "definitionState";
	/** 状態に問題がある場合のエラーメッセージ */
	public static final String RESULT_MESSAGE = "message";
	/** 状態に問題がある場合のスタックトレース */
	public static final String RESULT_STACK_TRACE = "stackTrace";

	/** メタデータの状態：問題なし */
	public static final String STATE_SUCCESS = "SUCCESS";
	/** メタデータの状態：問題あり（Groovyのコンパイルエラーなど） */
	public static final String STATE_ILLEGAL = "ILLEGAL";
	/** メタデータの状態：指定されたメタデータが存在しない */
	public static final String STATE_NOT_FOUND = "NOT_FOUND";

	WebApiService webApiService = ServiceRegistry.getRegistry()
			.getService(WebApiService.class);
	DefinitionService service = DefinitionService.getInstance();

	@Override
	public String execute(RequestContext request) {
		if (!webApiService.isEnableDefinitionApi()) {
			logger.warn("Definition API is disabled on WebApiService configuration.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		if (!AuthContext.getCurrentContext()
				.getUser()
				.isAdmin()) {
			throw new NoPermissionException("User is not admin.");
		}
		DefinitionPath typeAndPath = typeAndPath(request);

		String metaDataPath = service.getPath(typeAndPath.getType(), typeAndPath.getPath());

		// 存在チェック。checkStateは存在しない場合も同じ例外をスローし区別できないため、先に確認する。
		// Definitionへの変換を伴うTypedDefinitionManager#getは、不正な定義で例外となるため使用しない
		if (MetaDataContext.getContext()
				.getMetaDataEntry(metaDataPath) == null) {
			request.setAttribute(RESULT_STATE, STATE_NOT_FOUND);
			return CMD_EXEC_SUCCESS;
		}

		// 状態チェック。Groovyのコンパイルエラーなどは、MetaDataRuntime生成時に記録されている
		try {
			MetaDataContext.getContext()
					.checkState(metaDataPath);
			request.setAttribute(RESULT_STATE, STATE_SUCCESS);
		} catch (MetaDataIllegalStateException e) {
			request.setAttribute(RESULT_STATE, STATE_ILLEGAL);
			request.setAttribute(RESULT_MESSAGE, toMessage(e));
			request.setAttribute(RESULT_STACK_TRACE, toStackTrace(e));
		}
		return CMD_EXEC_SUCCESS;
	}

	// リクエストが送られてきたときにパスから型などを判明させる
	private DefinitionPath typeAndPath(RequestContext request) {
		String subPath = (String) request.getAttribute(WebApiRequestConstants.SUB_PATH);
		if (subPath == null || subPath.length() == 0) {
			throw new NullPointerException("definition path must specify.");
		}

		DefinitionPath path = service.resolvePath(subPath);
		if (path == null) {
			throw new IllegalArgumentException("illegal definition path:" + subPath);
		}
		return path;
	}

	/**
	 * 状態チェックで発生した例外から、レスポンス用のエラーメッセージを取得します。
	 * メッセージが取得できない場合は、例外クラス名を返します。
	 *
	 * @param e 状態チェックで発生した例外
	 * @return エラーメッセージ
	 */
	private String toMessage(MetaDataIllegalStateException e) {
		if (e.getMessage() != null) {
			return e.getMessage();
		}
		Throwable cause = e.getCause();
		if (cause != null) {
			return cause.getMessage() != null ? cause.getMessage()
					: cause.getClass()
							.getName();
		}
		return e.getClass()
				.getName();
	}

	/**
	 * 状態チェックで発生した例外から、レスポンス用のスタックトレースを取得します。
	 * 原因となった例外（Groovyのコンパイルエラーなど）がある場合は、そのスタックトレースを返します。
	 *
	 * @param e 状態チェックで発生した例外
	 * @return スタックトレース文字列
	 */
	private String toStackTrace(MetaDataIllegalStateException e) {
		Throwable target = e.getCause() != null ? e.getCause() : e;
		StringWriter writer = new StringWriter();
		target.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

}
