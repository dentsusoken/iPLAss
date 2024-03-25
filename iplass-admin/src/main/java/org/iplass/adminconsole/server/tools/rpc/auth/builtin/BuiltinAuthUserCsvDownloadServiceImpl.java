/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.auth.builtin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchOperator;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchType;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSpecificType;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthToolService;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthUserSearchParameter;
import org.iplass.mtp.impl.tools.auth.builtin.cond.SearchOperator;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserAttributeCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition.SpecificType;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Builtin Auth User CSV Export用Service実装クラス
 */
public class BuiltinAuthUserCsvDownloadServiceImpl extends AdminDownloadService {

	private static final Logger logger = LoggerFactory.getLogger(BuiltinAuthUserCsvDownloadServiceImpl.class);

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 7829298292170482278L;

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		final BuiltinAuthUserSearchParameter param = new BuiltinAuthUserSearchParameter();
		BuiltinAuthUserSearchType searchType = BuiltinAuthUserSearchType.valueOf(req.getParameter("searchType"));
		if (BuiltinAuthUserSearchType.SPECIFIC == searchType) {
			UserSpecificCondition searchCond = new UserSpecificCondition();

			BuiltinAuthUserSpecificType specificType = BuiltinAuthUserSpecificType.valueOf(req.getParameter("specificType"));
			if (BuiltinAuthUserSpecificType.LOCKED == specificType) {
				searchCond.setType(SpecificType.LOCKED);
				searchCond.setLockedUserPolicyName(req.getParameter("lockedUserPolicyName"));
			} else if (BuiltinAuthUserSpecificType.PASSWORDDAYS == specificType) {
				searchCond.setType(SpecificType.EXPIRED_PASSWORD);
				searchCond.setPasswordRemainDaysPolicyName(req.getParameter("passwordRemainDaysPolicyName"));
				BuiltinAuthUserSearchOperator operator = BuiltinAuthUserSearchOperator.valueOf(req.getParameter("passwordRemainDaysOparator"));
				if (BuiltinAuthUserSearchOperator.LESSTHAN == operator) {
					searchCond.setPasswordRemainDaysOparator(SearchOperator.LESSEQUAL);
				} else if (BuiltinAuthUserSearchOperator.EQUAL == operator) {
					searchCond.setPasswordRemainDaysOparator(SearchOperator.EQUAL);
				}
				if (StringUtil.isNotEmpty(req.getParameter("passwordRemainDays"))) {
					searchCond.setPasswordRemainDays(Integer.valueOf(req.getParameter("passwordRemainDays")));
				}
			} else if (BuiltinAuthUserSpecificType.LASTLOGIN == specificType) {
				searchCond.setType(SpecificType.LAST_LOGIN);
				if (StringUtil.isNotEmpty(req.getParameter("lastLoginFrom"))) {
					searchCond.setLastLoginFromDate(convertDate(new Date(Long.valueOf(req.getParameter("lastLoginFrom")))));
				}
				if (StringUtil.isNotEmpty(req.getParameter("lastLoginTo"))) {
					searchCond.setLastLoginToDate(convertDate(new Date(Long.valueOf(req.getParameter("lastLoginTo")))));
				}
			}
			param.setCondition(searchCond);
		} else if (BuiltinAuthUserSearchType.ATTRIBUTE == searchType) {
			UserAttributeCondition searchCond = new UserAttributeCondition();

			if (StringUtil.isNotEmpty(req.getParameter("accountId"))) {
				searchCond.setAccountId(req.getParameter("accountId"));
			}
			if (StringUtil.isNotEmpty(req.getParameter("name"))) {
				searchCond.setName(req.getParameter("name"));
			}
			if (StringUtil.isNotEmpty(req.getParameter("mail"))) {
				searchCond.setMail(req.getParameter("mail"));
			}
			if (StringUtil.isNotEmpty(req.getParameter("validTermRemainDaysOparator"))) {
				BuiltinAuthUserSearchOperator operator = BuiltinAuthUserSearchOperator.valueOf(req.getParameter("validTermRemainDaysOparator"));
				if (BuiltinAuthUserSearchOperator.LESSTHAN == operator) {
					searchCond.setValidTermRemainDaysOparator(SearchOperator.LESSEQUAL);
				} else if (BuiltinAuthUserSearchOperator.EQUAL == operator) {
					searchCond.setValidTermRemainDaysOparator(SearchOperator.EQUAL);
				}
				if (StringUtil.isNotEmpty(req.getParameter("validTermRemainDays"))) {
					searchCond.setValidTermRemainDays(Integer.valueOf(req.getParameter("validTermRemainDays")));
				}
			}
			if (StringUtil.isNotEmpty(req.getParameter("directWhere"))) {
				searchCond.setDirectWhere(req.getParameter("directWhere"));
			}
			param.setCondition(searchCond);
		}
		param.setLimit(-1);
		param.setOffset(-1);

		final String fileName = tenantId + "-user.csv";

		AdminAuditLoggingService aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
		aals.logDownload("AuthExplorerCsvDownload", fileName, param.getCondition());

		//Export
		try {
			DownloadUtil.setCsvResponseHeader(resp, fileName);

			BuiltinAuthToolService service = ServiceRegistry.getRegistry().getService(BuiltinAuthToolService.class);
			service.exportCsv(resp.getOutputStream(), param);
		} catch (IOException e) {
			logger.error("failed to export user.", e);
			throw new DownloadRuntimeException(e);
		}
	}

	private java.sql.Date convertDate(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}

}
