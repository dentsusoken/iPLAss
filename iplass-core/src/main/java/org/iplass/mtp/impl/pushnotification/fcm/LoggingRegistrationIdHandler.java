/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.pushnotification.fcm;

import org.iplass.mtp.pushnotification.fcm.RegistrationIdHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingRegistrationIdHandler implements RegistrationIdHandler {
	
	private static Logger logger = LoggerFactory.getLogger("mtp.push");

	@Override
	public void refreshRegistrationId(String currentId, String newId) {
		logger.info("need refresh RegistrationId," + currentId + "," + newId);
	}

	@Override
	public void removeRegistrationId(String registrationId) {
		logger.info("need remove RegistrationId," + registrationId);
	}

}
