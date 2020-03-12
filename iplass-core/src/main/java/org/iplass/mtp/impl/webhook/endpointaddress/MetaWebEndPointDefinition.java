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
package org.iplass.mtp.impl.webhook.endpointaddress;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinition;

@XmlRootElement
public class MetaWebEndPointDefinition extends BaseRootMetaData implements DefinableMetaData<WebEndPointDefinition>{

	private static final long serialVersionUID = 7029271819447338103L;
	/** 固有id、metaのidと同じ内容になるはずです */
	private String headerAuthType;
	private String headerAuthTypeName;

	/** 送り先 */
	private String url;
	
	/** セキュリティ関連部分はtemplateManagerによってdbで管理しています */
	
	//Definition → Meta
	@Override
	public void applyConfig(WebEndPointDefinition definition) {
		this.name = definition.getName();
		this.displayName = definition.getDisplayName();
		this.description = definition.getDescription();
		this.headerAuthType = definition.getHeaderAuthType();
		this.url = definition.getUrl();
		this.headerAuthTypeName = definition.getHeaderAuthTypeName();
	}
	
	//Meta → Definition
	@Override
	public WebEndPointDefinition currentConfig() {
		WebEndPointDefinition definition = new WebEndPointDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setHeaderAuthType(headerAuthType);
		definition.setHeaderAuthTypeName(headerAuthTypeName);
		definition.setUrl(url);
		return definition;
	}
	public MetaWebEndPointDefinition() {
		
	}
	
	public MetaWebEndPointDefinition(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHeaderAuthType() {
		return headerAuthType;
	}

	public void setHeaderAuthType(String headerAuthType) {
		this.headerAuthType = headerAuthType;
	}
	
	public String getHeaderAuthTypeName() {
		return headerAuthTypeName;
	}

	public void setHeaderAuthTypeName(String headerAuthTypeName) {
		this.headerAuthTypeName = headerAuthTypeName;
	}


	@Override
	public WebEndPointRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebEndPointRuntime();
	}

	@Override
	public RootMetaData copy() {
		return ObjectUtil.deepCopy(this);
	}


	public class WebEndPointRuntime extends BaseMetaDataRuntime{
		String url;
		String hmac;
		String hmacResult;
		String headerAuthContent;
		String headerAuthType;
		String headerAuthTypeName;
		String contentForThisEndPoint;
		String endPointName;

		public WebEndPointRuntime() {
			super();
			url = MetaWebEndPointDefinition.this.url;
			endPointName = MetaWebEndPointDefinition.this.name;
			headerAuthType = MetaWebEndPointDefinition.this.headerAuthType;
			headerAuthTypeName = MetaWebEndPointDefinition.this.headerAuthTypeName;
		}

		@Override
		public MetaWebEndPointDefinition getMetaData() {
			return MetaWebEndPointDefinition.this;
		}

		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getHmac() {
			return hmac;
		}
		public void setHmac(String hmac) {
			this.hmac = hmac;
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
		public String getHeaderAuthTypeName() {
			return headerAuthTypeName;
		}
		public void setHeaderAuthTypeName(String headerAuthTypeName) {
			this.headerAuthTypeName = headerAuthTypeName;
		}
		
		public String getContentForThisEndPoint() {
			return contentForThisEndPoint;
		}
		public void setContentForThisEndPoint(String contentForThisEndPoint) {
			this.contentForThisEndPoint = contentForThisEndPoint;
		}
		
		public String getHmacResult() {
			return hmacResult;
		}
		public void setHmacResult(String hmacResult) {
			this.hmacResult = hmacResult;
		}
		public String getEndPointName() {
			return endPointName;
		}
		public void setEndPointName(String endPointName) {
			this.endPointName = endPointName;
		}
		public WebEndPointRuntime copy() {
			return new WebEndPointRuntime();
		}
	}

}
