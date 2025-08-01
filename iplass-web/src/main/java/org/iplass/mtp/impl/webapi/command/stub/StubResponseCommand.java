/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.stub;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebAPI スタブレスポンスを返却するコマンド
 * <p>
 * WebAPI 定義でスタブレスポンスを返却する {@link org.iplass.mtp.webapi.definition.WebApiDefinition#isReturnStubResponse()} が true の場合に実行されます。
 * </p>
 * @author SEKIGUCHI Naoya
 */
@CommandClass(name = StubResponseCommand.NAME, displayName = "Return stub response for Web API.", overwritable = false)
public class StubResponseCommand implements Command {
	/** コマンド名 */
	public static final String NAME = "mtp/webapi/StubResponseCommand";
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(StubResponseCommand.class);

	@Override
	public String execute(RequestContext request) {
		var isWebApi = (Boolean) request.getAttribute(WebApiRequestConstants.WEB_API);
		if (Boolean.TRUE != isWebApi) {
			// WebAPI 経由の実行ではない場合は SUCCESS を返却し終了。
			logger.warn("This command should be set as a WebAPI command.");
			return "SUCCESS";
		}

		var name = (String) request.getAttribute(WebApiRequestConstants.API_NAME);

		// スタブであることのログを出力する
		logger.debug("Stub command executed. This command is used for testing purposes only. " +
				"Do not use this command in production environment. WebAPI name: {}", name);

		var service = ServiceRegistry.getRegistry().getService(WebApiService.class);
		var runtime = service.getRuntimeByName(name);

		// レスポンスを属性に設定する
		for (var entry : runtime.getStubResponseJsonMap().entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}

		// ステータスコードを設定する
		return runtime.getStubResponseStatusValue();
	}
}
