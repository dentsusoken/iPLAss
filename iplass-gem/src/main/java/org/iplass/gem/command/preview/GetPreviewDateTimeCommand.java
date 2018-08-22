/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.preview;

import java.sql.Timestamp;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.web.preview.PreviewHandler;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

/**
 * プレビュー日付取得Command（WebAPI）。
 *
 * @author K.Higuchi
 *
 */
@WebApi(
		name=GetPreviewDateTimeCommand.WEBAPI_NAME,
		accepts={RequestType.REST_JSON, RequestType.REST_FORM},
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="params"),
		results=GetPreviewDateTimeCommand.RESULT_DATE_TIME,
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/preview/GetPreviewDateTimeCommand", displayName="現在のプレビュー日時取得")
public final class GetPreviewDateTimeCommand implements Command {

	public static final String WEBAPI_NAME = "gem/preview/getPreviewDateTime";

	public static final String RESULT_DATE_TIME = "dateTime";
	
	private PreviewHandler preview = new PreviewHandler();

	@Override
	public String execute(RequestContext request) {
		Timestamp ts = preview.getPreviewDate(request);
		request.setAttribute(RESULT_DATE_TIME, ts);
		return "SUCCESS";
	}

}
