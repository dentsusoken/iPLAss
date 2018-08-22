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

package org.iplass.mtp.impl.tools.metaport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ログ出力Callback
 */
public class MetaDataWriteLoggingCallback implements MetaDataWriteCallback {

	private static Logger logger = LoggerFactory.getLogger(MetaDataWriteLoggingCallback.class);

	@Override
	public void onStarted() {
		logger.debug("start metadata write.");
	}

	@Override
	public void onWrited(String path, String version) {
		logger.debug("metadata writed. path = " + path + ". version = " + version);
	}

	@Override
	public void onFinished() {
		logger.debug("finish metadata write.");
	}

	@Override
	public boolean onWarning(String path, String message, String version) {
		logger.warn("warning metadata write proccess. path = " + path + ". version = " + version + ". message = " + message);
		return true;
	}

	@Override
	public boolean onErrored(String path, String message, String version) {
		logger.error("error metadata write proccess. path = " + path + ". version = " + version + ". message = " + message);
		return true;
	}

}
