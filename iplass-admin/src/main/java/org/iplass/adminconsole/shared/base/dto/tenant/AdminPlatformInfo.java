/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.dto.tenant;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminPlatformInfo implements Serializable {

	private static final long serialVersionUID = 8594045462747319422L;

	private String platformErrorMessage = null;

	private Map<String, String> platformInfomations;

	private boolean showServerInfo;

	private Map<String, String> serverInfomations;

	private List<String> noticeLines;

	private List<String> licenseLines;

	public AdminPlatformInfo() {
	}

	public String getPlatformErrorMessage() {
		return platformErrorMessage;
	}

	public void setPlatformErrorMessage(String platformErrorMessage) {
		this.platformErrorMessage = platformErrorMessage;
	}

	public Map<String, String> getPlatformInfomations() {
		return platformInfomations;
	}

	public void setPlatformInfomations(Map<String, String> platformInfomations) {
		this.platformInfomations = platformInfomations;
	}

	public void addPlatformInfomation(String key, String value) {
		if (platformInfomations == null) {
			platformInfomations = new LinkedHashMap<String, String>();
		}
		platformInfomations.put(key, value);
	}

	public boolean isShowServerInfo() {
		return showServerInfo;
	}

	public void setShowServerInfo(boolean showServerInfo) {
		this.showServerInfo = showServerInfo;
	}

	public Map<String, String> getSeverInfomations() {
		return serverInfomations;
	}

	public void setSeverInfomations(Map<String, String> serverInfomations) {
		this.serverInfomations = serverInfomations;
	}

	public void addSeverInfomation(String key, String value) {
		if (serverInfomations == null) {
			serverInfomations = new LinkedHashMap<String, String>();
		}
		serverInfomations.put(key, value);
	}

	public List<String> getNoticeLines() {
		return noticeLines;
	}

	public void setNoticeLines(List<String> noticeLines) {
		this.noticeLines = noticeLines;
	}

	public List<String> getLicenseLines() {
		return licenseLines;
	}

	public void setLicenseLines(List<String> licenseLines) {
		this.licenseLines = licenseLines;
	}

}
