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
package org.iplass.adminconsole.server.base.io.upload;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

/**
 * マルチパートリクエスト パラメータ解析機能
 *
 * <p>
 * 本インターフェースの実現クラスでは、マルチパートリクエストのパラメータを解析し、
 * {@link org.iplass.adminconsole.server.base.io.upload.MultipartRequestParameter} を返却する。
 * </p>
 *
 * <p>
 * 実現クラスはスレッドセーフである必要がある。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface MultipartRequestParameterParser {
	/**
	 * パラメータ解析を実施する。
	 *
	 * @param req HttpServletRequest
	 * @return 解析したパラメータ情報
	 */
	List<MultipartRequestParameter> parse(HttpServletRequest req);
}
