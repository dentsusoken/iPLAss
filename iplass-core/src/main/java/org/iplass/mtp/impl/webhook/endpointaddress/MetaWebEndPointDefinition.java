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
	private String webEndPointId;
	private String headerAuthType;

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
		this.webEndPointId = definition.getWebEndPointId();
	}
	
	//Meta → Definition
	@Override
	public WebEndPointDefinition currentConfig() {
		WebEndPointDefinition definition = new WebEndPointDefinition();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setHeaderAuthType(headerAuthType);
		
		definition.setUrl(url);
		definition.setWebEndPointId(this.id);
		return definition;
	}
	public MetaWebEndPointDefinition() {
		
	}
	
	public MetaWebEndPointDefinition(String url) {
		this.url = url;
	}

	public String getWebEndPointId() {
		return webEndPointId;
	}

	public void setWebEndPointId(String webEndPointId) {
		this.webEndPointId = webEndPointId;
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

	@Override
	public WebEndPointRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebEndPointRuntime();
	}

	@Override
	public RootMetaData copy() {
		return ObjectUtil.deepCopy(this);
	}


	public class WebEndPointRuntime extends BaseMetaDataRuntime{
		//FIXME add
		
		public WebEndPointRuntime() {
			super();
		}

		@Override
		public MetaWebEndPointDefinition getMetaData() {
			return MetaWebEndPointDefinition.this;
		}
		
	}

}
