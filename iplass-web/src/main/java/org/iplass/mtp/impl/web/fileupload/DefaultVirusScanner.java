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

package org.iplass.mtp.impl.web.fileupload;

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.resourceString;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVirusScanner implements FileScanner {

	private static final Logger logger = LoggerFactory.getLogger(DefaultVirusScanner.class);

	private String commandPath;

	//TODO change to Long
	private String timeout;
	private boolean errorOnTimeout;
	private List<Integer> successExitValue;
	
	private Long timeoutVal;

	public boolean isErrorOnTimeout() {
		return errorOnTimeout;
	}

	public void setErrorOnTimeout(boolean errorOnTimeout) {
		this.errorOnTimeout = errorOnTimeout;
	}

	public List<Integer> getSuccessExitValue() {
		return successExitValue;
	}

	public void setSuccessExitValue(List<Integer> successExitValue) {
		this.successExitValue = successExitValue;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
		if (timeout != null) {
			this.timeoutVal = Long.valueOf(timeout);
		} else {
			this.timeoutVal = null;
		}
	}

	public String getCommandPath() {
		return commandPath;
	}

	public void setCommandPath(String comandPath) {
		this.commandPath = comandPath;
	}

	@Override
	public void scan(String filePath) {
		String command = commandPath.replace("${file}", filePath);
		Process proc = null;
		try {

			proc = Runtime.getRuntime().exec(command);
			if (timeoutVal == null) {
				throw new NullPointerException("timeout must be specified");
			}
			boolean ret = proc.waitFor(timeoutVal, TimeUnit.SECONDS);
			if (ret) {
				int exitVal = proc.exitValue();
				if (successExitValue != null && successExitValue.size() > 0) {
					for (Integer i: successExitValue) {
						if (i.intValue() == exitVal) {
							return;
						}
					}
					throw new RuntimeException("Scan failed. Illegal exit value:" + exitVal + ", file: " + filePath);
				}
			} else {
				if (errorOnTimeout) {
					throw new RuntimeException("Scan failed (timeouted). file: " + filePath);
				} else {
					logger.warn(resourceString("impl.web.fileupload.DefaultVirusScanHandle.timeout"));
				}
			}

		} catch (IOException | InterruptedException | RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw new ApplicationException(resourceString("impl.web.fileupload.DefaultVirusScanHandle.failed"), e);
		} finally {
			if (proc != null && proc.isAlive()) {
				proc.destroy();
			}
		}
	}

}
