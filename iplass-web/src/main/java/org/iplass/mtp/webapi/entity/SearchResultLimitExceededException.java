/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.webapi.entity;

import org.iplass.mtp.ApplicationException;

/**
 * 
 * <% if (doclang == "ja") {%>
 * Entity CRUD Web APIにて、検索件数のリミットに達した場合にスローされる例外。
 * ただし、例外がスローされるのは、EntityWebApiServiceの設定にて
 * リミットオーバー時に例外をスローすると設定した場合。
 * <%} else {%>
 * Exception that is thrown when the search limit is reached in Entity CRUD Web API.<br>
 * Note: This exception is thrown if the EntityWebApiService settings are set to throw an exception when the limit is exceeded.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class SearchResultLimitExceededException extends ApplicationException {
	private static final long serialVersionUID = -5171809709146937565L;

	public SearchResultLimitExceededException() {
		super();
	}

	public SearchResultLimitExceededException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchResultLimitExceededException(String message) {
		super(message);
	}

	public SearchResultLimitExceededException(Throwable cause) {
		super(cause);
	}

}
