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

package org.iplass.adminconsole.client.base.data;

import org.iplass.adminconsole.shared.base.dto.AdminUncaughtException;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;

public abstract class AbstractAdminDataSource extends DataSource {

	public AbstractAdminDataSource() {
		setDataProtocol(DSProtocol.CLIENTCUSTOM);
		setDataFormat(DSDataFormat.CUSTOM);
		setClientOnly(false);
	}

	@Override
	protected Object transformRequest(DSRequest request) {

		String requestId = request.getRequestId();
		DSResponse response = new DSResponse();
		response.setAttribute("clientContext", request.getAttributeAsObject("clientContext"));
		response.setStatus(0);
		switch (request.getOperationType()) {
			case FETCH:
				executeFetch(requestId, request, response);
				break;
			case ADD:
				executeAdd(requestId, request, response);
				break;
			case UPDATE:
				executeUpdate(requestId, request, response);
				break;
			case REMOVE:
				executeRemove(requestId, request, response);
				break;
			default:
		}
		return request.getData();
	}

	protected abstract void executeFetch(String requestId, DSRequest request, DSResponse response);

	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
		throw new AdminUncaughtException("Unsupported operation.");
	}

	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		throw new AdminUncaughtException("Unsupported operation.");
	}

	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		throw new AdminUncaughtException("Unsupported operation.");
	}

}
