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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVirusScanner implements FileScanner {

	private static final Logger logger = LoggerFactory.getLogger(DefaultVirusScanner.class);

	private String commandPath;

	private String timeout;

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
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
		try {

			Process proc = Runtime.getRuntime().exec(command);

			TimerTask task = new ProcessDestroyer(proc);
			Timer timer = new Timer("タイムアウト設定");
			timer.schedule(task, TimeUnit.SECONDS.toMillis(new Long(timeout)));

			while (true) {
				proc.waitFor();
				break;
			}

			timer.cancel();

		} catch (IOException e) {
			throw new ApplicationException(resourceString("impl.web.fileupload.DefaultVirusScanHandle.failed"));
		} catch (NumberFormatException e) {
			throw new ApplicationException(resourceString("impl.web.fileupload.DefaultVirusScanHandle.failed"));
		} catch (InterruptedException e) {
			throw new ApplicationException(resourceString("impl.web.fileupload.DefaultVirusScanHandle.failed"));
		}
	}

	private class ProcessDestroyer extends TimerTask {

		private Process p;

		public ProcessDestroyer(Process p) {
			this.p = p;
		}

		@Override
		public void run() {
			p.destroy();
			logger.warn(resourceString("impl.web.fileupload.DefaultVirusScanHandle.timeout"));
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
