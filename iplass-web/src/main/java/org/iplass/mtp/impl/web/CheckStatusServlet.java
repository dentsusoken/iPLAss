/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Servlet implementation class CheckStatusServlet
 */
public class CheckStatusServlet extends HttpServlet {
	//TODO もう少しチェックする項目ふやす。メタデータが取得できるか？GroovyScriptコンパイルできるか？など
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CheckStatusServlet.class);

	private String statusOk = null;

	private String statusNg = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckStatusServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		ServletConfig config = super.getServletConfig();

		String statusOkString = config.getInitParameter("statusOk");
		statusOk = "OK";
		if (StringUtil.isNotBlank(statusOkString)) {
			statusOk = statusOkString;
		}

		String statusNgString = config.getInitParameter("statusNg");
		statusNg = "NG";
		if (StringUtil.isNotBlank(statusNgString)) {
			statusNg = statusNgString;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		exec(request, response);
	}

	private void exec(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			check(request, response);
			response.getWriter().write(statusOk);
		} catch (Throwable t) {
			response.getWriter().write(statusNg);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void check(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		Connection con = null;
		try {
			RdbAdapter rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
			con = rdb.getConnection();
			Statement stmt = con.createStatement();;
			try {
				ResultSet rs = stmt.executeQuery(rdb.checkStatusQuery());
				rs.close();
			} finally {
				stmt.close();
			}
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		exec(request, response);
	}

}
