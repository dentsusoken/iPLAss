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

package org.iplass.mtp.webhook.template.endpointaddress;

/**
 * org.iplass.mtp.webhook.WebHookに使うエンドポイントのクラス。
 * エンドポイント
 * */
public class WebHookEndPoint {
	String url;
	String endPointName;
	String hmac;
	String hmacResult;
	String headerAuthContent;
	String headerAuthType;
	String headerAuthSchemeName;
	String payloadContent;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEndPointName() {
		return endPointName;
	}
	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public String getHmacResult() {
		return hmacResult;
	}
	public void setHmacResult(String hmacResult) {
		this.hmacResult = hmacResult;
	}
	public String getHeaderAuthContent() {
		return headerAuthContent;
	}
	public void setHeaderAuthContent(String headerAuthContent) {
		this.headerAuthContent = headerAuthContent;
	}
	public String getHeaderAuthType() {
		return headerAuthType;
	}
	public void setHeaderAuthType(String headerAuthType) {
		this.headerAuthType = headerAuthType;
	}
	public String getHeaderAuthSchemeName() {
		return headerAuthSchemeName;
	}
	public void setHeaderAuthSchemeName(String headerAuthSchemeName) {
		this.headerAuthSchemeName = headerAuthSchemeName;
	}
	public String getPayloadContent() {
		return payloadContent;
	}
	public void setPayloadContent(String payloadContent) {
		this.payloadContent = payloadContent;
	}
}
