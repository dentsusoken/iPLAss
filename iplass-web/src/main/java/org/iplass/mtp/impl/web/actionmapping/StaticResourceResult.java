/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;

import javax.servlet.ServletException;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.staticresource.StaticResourceService;
import org.iplass.mtp.impl.web.staticresource.MetaStaticResource.StaticResourceRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StaticResourceResultDefinition;


public class StaticResourceResult extends Result {

	private static final long serialVersionUID = -6801751736171646783L;

	private String staticResourceId;
	private String staticResourceName;
	private boolean resolveByName;

	private boolean useContentDisposition;
	private ContentDispositionType contentDispositionType;

	private String entryPathAttributeName;

	public StaticResourceResult() {
	}

	public StaticResourceResult(String cmdStatus, boolean resolveByName, String idOrName) {
		setCommandResultStatus(cmdStatus);
		this.resolveByName = resolveByName;
		if (resolveByName) {
			staticResourceName = idOrName;
		} else {
			staticResourceId = idOrName;
		}
	}

	public String getEntryPathAttributeName() {
		return entryPathAttributeName;
	}

	public void setEntryPathAttributeName(String entryPathAttributeName) {
		this.entryPathAttributeName = entryPathAttributeName;
	}

	public String getStaticResourceId() {
		return staticResourceId;
	}

	public void setStaticResourceId(String staticResourceId) {
		this.staticResourceId = staticResourceId;
	}

	public String getStaticResourceName() {
		return staticResourceName;
	}

	public void setStaticResourceName(String staticResourceName) {
		this.staticResourceName = staticResourceName;
	}

	public boolean isResolveByName() {
		return resolveByName;
	}

	public void setResolveByName(boolean resolveByName) {
		this.resolveByName = resolveByName;
	}

	public boolean isUseContentDisposition() {
		return useContentDisposition;
	}

	public void setUseContentDisposition(boolean useContentDisposition) {
		this.useContentDisposition = useContentDisposition;
	}

	public ContentDispositionType getContentDispositionType() {
		return contentDispositionType;
	}

	public void setContentDispositionType(ContentDispositionType contentDispositionType) {
		this.contentDispositionType = contentDispositionType;
	}

	@Override
	public ResultRuntime createRuntime() {
		return new StaticResourceResultRuntime();
	}

	@Override
	public void applyConfig(ResultDefinition definition) {
		fillFrom(definition);
		StaticResourceResultDefinition def = (StaticResourceResultDefinition) definition;
		StaticResourceService service = ServiceRegistry.getRegistry().getService(StaticResourceService.class);
		StaticResourceRuntime srr = service.getRuntimeByName(def.getStaticResourceName());
		if (srr == null) {
			throw new NullPointerException(def.getStaticResourceName() + " not found");
		}
		staticResourceId = srr.getMetaData().getId();
		resolveByName = false;
		useContentDisposition = def.isUseContentDisposition();
		contentDispositionType = def.getContentDispositionType();
		entryPathAttributeName = def.getEntryPathAttributeName();
	}

	@Override
	public ResultDefinition currentConfig() {
		StaticResourceResultDefinition definition = new StaticResourceResultDefinition();
		fillTo(definition);
		StaticResourceService service = ServiceRegistry.getRegistry().getService(StaticResourceService.class);
		StaticResourceRuntime sr;
		if (resolveByName) {
			sr = service.getRuntimeByName(staticResourceName);
		} else {
			sr = service.getRuntimeById(staticResourceId);
		}
		if (sr != null) {
			definition.setStaticResourceName(sr.getMetaData().getName());
		}

		definition.setUseContentDisposition(useContentDisposition);
		definition.setContentDispositionType(contentDispositionType);
		definition.setEntryPathAttributeName(entryPathAttributeName);

		return definition;
	}

	public class StaticResourceResultRuntime extends ResultRuntime {

		@Override
		public StaticResourceResult getMetaData() {
			return StaticResourceResult.this;
		}

		@Override
		public void finallyProcess(WebRequestStack request) {
		}

		@Override
		public void handle(WebRequestStack request)
				throws ServletException, IOException {

			RequestContext cmdRequestContext = request.getRequestContext();

			StaticResourceService srs = ServiceRegistry.getRegistry().getService(StaticResourceService.class);
			StaticResourceRuntime sr;
			if (resolveByName) {
				sr = srs.getRuntimeByName(staticResourceName);
			} else {
				sr = srs.getRuntimeById(staticResourceId);
			}

			if (sr == null) {
				if (resolveByName) {
					throw new WebProcessRuntimeException("can not find staticResource... name:" + staticResourceName);
				} else {
					throw new WebProcessRuntimeException("can not find staticResource... id:" + staticResourceId);
				}
			}

			String entryPath = null;
			if (sr.isArchive()) {
				entryPath = (String) cmdRequestContext.getAttribute(entryPathAttributeName);
				if (entryPath == null) {
					entryPath = cmdRequestContext.getParam(entryPathAttributeName);
				}
				if (entryPath == null) {
					throw new IllegalArgumentException("entryPath must specify");
				}

			} else {
				entryPath = (String) cmdRequestContext.getAttribute(WebRequestConstants.ACTION_NAME);
			}

			sr.handle(request, entryPath, useContentDisposition, contentDispositionType);

		}

	}


}
