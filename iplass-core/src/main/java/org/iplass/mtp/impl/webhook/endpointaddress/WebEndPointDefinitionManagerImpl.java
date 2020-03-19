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

import java.util.ArrayList;
import java.util.List;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebEndPointDefinition.WebEndPointRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinition;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebEndPointDefinitionManagerImpl extends AbstractTypedDefinitionManager<WebEndPointDefinition> implements WebEndPointDefinitionManager{
	private static final Logger logger = LoggerFactory.getLogger(WebEndPointDefinitionManager.class);
	private WebEndPointService service;
	
	public WebEndPointDefinitionManagerImpl() {
		this.service = ServiceRegistry.getRegistry().getService(WebEndPointService.class);
	}
	
	@Override
	public Class<WebEndPointDefinition> getDefinitionType() {
		return WebEndPointDefinition.class; 
	}

	@Override
	protected RootMetaData newInstance(WebEndPointDefinition definition) {
		return new MetaWebEndPointDefinition();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

	/**
	 * remove時、自動でdb削除。
	 * */
	@Override
	public DefinitionModifyResult remove(String definitionName) {
		WebEndPointDefinition definition = super.get(definitionName);
		
		WebEndPointService weps = (WebEndPointService) getService();
		try {
		weps.deleteSecurityTokenByDef(definition.getWebEndPointId());
		} catch (Exception e) {
			logger.warn("Exception occured while removing the data from Database for :"+definitionName+". Caused by: "+e.getCause()+". With following message : "+e.getCause().getMessage());
			throw (RuntimeException) e;
		}
		
		return super.remove(definitionName);
	}
	
	/**
	 * <p>updateToken</p> 
	 *  トークンタイプ:WHHM,WHBT,WHBA
	 * 
	 * */
	@Override
	public void modifySecurityToken(int tenantId, String metaDataId, String secret, String tokenType) {
		if(tokenType==null) {
			throw new RuntimeException("null TokenType");
			}
		if( tokenType.equals("WHHM")){
			service.updateHmacSecurityTokenByDef(tenantId,metaDataId,secret);
			}
		if( tokenType.equals("WHBT")){
			service.updateBearerSecurityTokenByDef(tenantId,metaDataId,secret);
			}
		if( tokenType.equals("WHBA")){
			service.updateBasicSecurityTokenByDef(tenantId,metaDataId,secret);
			}
	}
	
	/**
	 * <p>getToken</p> 
	 * トークンタイプ:WHHM,WHBT,WHBA
	 * */
	@Override
	public String getSecurityToken(int tenantId, String metaDataId,  String tokenType) {
		if(tokenType==null||tokenType.replaceAll("\\s","").isEmpty()) {
			return null;
			}
		if( tokenType.equals("WHHM")){
			return service.getHmacTokenByDef(tenantId,metaDataId);
			}
		if( tokenType.equals("WHBT")){
			return service.getBearerTokenByDef(tenantId,metaDataId);
			}
		if( tokenType.equals("WHBA")){
			return service.getBasicTokenByDef(tenantId,metaDataId);
			}
		throw new RuntimeException("unknown TokenType");
	}
	
	@Override
	public String generateHmacTokenString() {
		return service.generateHmacTokenString();
	}
	
	public List<WebEndPointRuntime> generateRuntimeInstanceList(List<String> endPointName){
		ArrayList<WebEndPointRuntime> result = new ArrayList<WebEndPointRuntime>();
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		int tenantId = tenant.getId();
		for (String wepDefName: endPointName) {
			WebEndPointRuntime endPointRuntime = (service.getRuntimeByName(wepDefName)).copy();
			endPointRuntime.setHeaderAuthContent(getSecurityToken(tenantId, endPointRuntime.getEndPointId(), endPointRuntime.getHeaderAuthType()));
			endPointRuntime.setHmac(getSecurityToken(tenantId, endPointRuntime.getEndPointId(), "WHHM"));	
			result.add(endPointRuntime);
		}
		return result;
	}

}
