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
package org.iplass.mtp.impl.web.staticresource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.definition.binary.ArchiveBinaryDefinition;
import org.iplass.mtp.definition.binary.BinaryDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.binary.ArchiveBinaryMetaData;
import org.iplass.mtp.impl.metadata.binary.BinaryMetaData;
import org.iplass.mtp.impl.metadata.binary.SimpleBinaryMetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.RangeHeader;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.staticresource.EntryPathTranslator;
import org.iplass.mtp.web.staticresource.definition.LocalizedStaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaStaticResource extends BaseRootMetaData implements DefinableMetaData<StaticResourceDefinition> {
	private static final long serialVersionUID = -1254930892273250498L;

	private static Logger logger = LoggerFactory.getLogger(MetaStaticResource.class);

	private BinaryMetaData resource;
	private List<MetaLocalizedStaticResource> localizedResourceList;
	private String contentType;

	private List<MetaMimeTypeMapping> mimeTypeMapping;
	private String entryTextCharset;
	private MetaEntryPathTranslator entryPathTranslator;

	public BinaryMetaData getResource() {
		return resource;
	}

	public void setResource(BinaryMetaData resource) {
		this.resource = resource;
	}
	public List<MetaLocalizedStaticResource> getLocalizedResourceList() {
		return localizedResourceList;
	}

	public void setLocalizedResourceList(
			List<MetaLocalizedStaticResource> localizedResourceList) {
		this.localizedResourceList = localizedResourceList;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getEntryTextCharset() {
		return entryTextCharset;
	}

	public void setEntryTextCharset(String entryTextCharset) {
		this.entryTextCharset = entryTextCharset;
	}

	public List<MetaMimeTypeMapping> getMimeTypeMapping() {
		return mimeTypeMapping;
	}

	public void setMimeTypeMapping(List<MetaMimeTypeMapping> mimeTypeMapping) {
		this.mimeTypeMapping = mimeTypeMapping;
	}

	public MetaEntryPathTranslator getEntryPathTranslator() {
		return entryPathTranslator;
	}

	public void setEntryPathTranslator(MetaEntryPathTranslator entryPathTranslator) {
		this.entryPathTranslator = entryPathTranslator;
	}

	@Override
	public StaticResourceRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new StaticResourceRuntime();
	}

	@Override
	public MetaStaticResource copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	public void applyConfig(StaticResourceDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(def.getLocalizedDisplayNameList());

		if (def.getResource() == null) {
			resource = null;
		} else if (def.getResource() instanceof ArchiveBinaryDefinition) {
			resource = new ArchiveBinaryMetaData((ArchiveBinaryDefinition) def.getResource());
		} else {
			resource = new SimpleBinaryMetaData(def.getResource());
		}

		if (def.getLocalizedResourceList() != null) {
			localizedResourceList = new ArrayList<>(def.getLocalizedResourceList().size());
			for (LocalizedStaticResourceDefinition lr: def.getLocalizedResourceList()) {
				MetaLocalizedStaticResource mlr = new MetaLocalizedStaticResource();
				mlr.setLocaleName(lr.getLocaleName());
				if (lr.getResource() instanceof ArchiveBinaryDefinition) {
					mlr.setResource(new ArchiveBinaryMetaData((ArchiveBinaryDefinition) lr.getResource()));
				} else if (lr.getResource() instanceof BinaryDefinition) {
					mlr.setResource(new SimpleBinaryMetaData(lr.getResource()));
				}
				localizedResourceList.add(mlr);
			}
		}

		contentType = def.getContentType();
		if (def.getMimeTypeMapping() == null) {
			mimeTypeMapping = null;
		} else {
			mimeTypeMapping = new ArrayList<MetaMimeTypeMapping>(def.getMimeTypeMapping().size());
			for (MimeTypeMappingDefinition md: def.getMimeTypeMapping()) {
				mimeTypeMapping.add(new MetaMimeTypeMapping(md.getExtension(), md.getMimeType()));
			}
		}
		entryTextCharset = def.getEntryTextCharset();
		entryPathTranslator = MetaEntryPathTranslator.toMeta(def.getEntryPathTranslator());
	}

	//Meta → Definition
	public StaticResourceDefinition currentConfig() {
		StaticResourceDefinition def = new StaticResourceDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		def.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		if (resource != null) {
			def.setResource(resource.currentConfig());
		}
		if (localizedResourceList != null && localizedResourceList.size() > 0) {
			def.setLocalizedResourceList(new ArrayList<>(localizedResourceList.size()));
			for (MetaLocalizedStaticResource mlr: localizedResourceList) {
				LocalizedStaticResourceDefinition lr = new LocalizedStaticResourceDefinition();
				lr.setLocaleName(mlr.getLocaleName());
				if (mlr.getResource() != null) {
					lr.setResource(mlr.getResource().currentConfig());
				}
				def.getLocalizedResourceList().add(lr);
			}
		}

		def.setContentType(contentType);
		if (mimeTypeMapping != null && mimeTypeMapping.size() > 0) {
			def.setMimeTypeMapping(new ArrayList<>(mimeTypeMapping.size()));
			for (MetaMimeTypeMapping mm: mimeTypeMapping) {
				MimeTypeMappingDefinition md = new MimeTypeMappingDefinition();
				md.setExtension(mm.getExtension());
				md.setMimeType(mm.getMimeType());
				def.getMimeTypeMapping().add(md);
			}
		}
		def.setEntryTextCharset(entryTextCharset);
		if (entryPathTranslator != null) {
			def.setEntryPathTranslator(entryPathTranslator.currentConfig());
		}

		return def;
	}

	public class StaticResourceRuntime extends BaseMetaDataRuntime {
		private Map<String, String> mimeMap;
		private EntryPathTranslator eptInstance;
		private Map<String, BinaryMetaData> langMap;

		public StaticResourceRuntime() {
			try {
				if (mimeTypeMapping != null && mimeTypeMapping.size() > 0) {
					mimeMap = new HashMap<>();
					for (MetaMimeTypeMapping mm: mimeTypeMapping) {
						mimeMap.put(mm.getExtension(), mm.getMimeType());
					}
				}
				if (entryPathTranslator != null) {
					eptInstance = entryPathTranslator.createEntryPathTranslator(getName());
				}

				if (localizedResourceList != null) {
					langMap = new HashMap<>();
					for (MetaLocalizedStaticResource mls : localizedResourceList) {
						langMap.put(mls.getLocaleName(), mls.getResource());
					}
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaStaticResource getMetaData() {
			return MetaStaticResource.this;
		}

		public boolean isArchive() {
			return (resource() instanceof ArchiveBinaryMetaData);
		}

		private String resolveContentType(String entryPath, WebRequestStack requestStack) {
			String mimeType = null;
			int lioDot = entryPath.lastIndexOf('.');
			if (lioDot >= 0) {
				String ext = entryPath.substring(lioDot + 1);
				if (mimeMap != null) {
					mimeType = mimeMap.get(ext);
				}
				if (mimeType == null) {
					mimeType = requestStack.getServletContext().getMimeType(entryPath);
				}
				if (mimeType == null) {
					mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(entryPath);
				}
			}
			if (mimeType == null) {
				//unknown
				mimeType = "application/octet-stream";
			}

			if (entryTextCharset != null && mimeType.toLowerCase().startsWith("text/")) {
				mimeType = mimeType + "; charset=" + entryTextCharset;
			}
			return mimeType;
		}

		private String transratePath(String entryPath) {
			if (eptInstance != null) {
				return eptInstance.translate(entryPath);
			} else {
				return entryPath;
			}
		}

		private boolean isGetHeadRequest(String method) {
			switch (method) {
			case "HEAD":
			case "GET":
				return true;
			default:
				return false;
			}
		}

		private BinaryMetaData resource() {
			if (langMap != null) {
				String lang = ExecuteContext.getCurrentContext().getLanguage();
				BinaryMetaData ret = langMap.get(lang);
				if (ret != null) {
					return ret;
				}
			}

			return resource;
		}

		public void handle(WebRequestStack requestStack, String entryPath,
				boolean useContentDisposition, ContentDispositionType contentDispositionType) throws IOException, ServletException {
			checkState();

			InputStream is = null;
			try {
				String method = requestStack.getRequest().getMethod();
				boolean isHead = "HEAD".equals(method);

				long size = -1;
				String contentType = null;
				BinaryMetaData _resource = resource();
				if (_resource instanceof ArchiveBinaryMetaData) {
					ArchiveBinaryMetaData archive = (ArchiveBinaryMetaData) _resource;
					String pathTrans = transratePath(entryPath);
					if (!archive.hasEntry(pathTrans)) {
						requestStack.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
					size = archive.getEntrySize(pathTrans);
					contentType = resolveContentType(pathTrans, requestStack);
					if (!isHead) {
						is = archive.getEntryAsStream(pathTrans);
					}
				} else {
					if (_resource == null) {
						size = 0;
					} else {
						size = _resource.getSize();
						contentType = MetaStaticResource.this.contentType;
						if (!isHead) {
							is = _resource.getInputStream();
						}
					}
				}

				//Content-Disposition
				if (useContentDisposition) {
					String fileName = entryPath.substring(entryPath.lastIndexOf('/') + 1);
					WebUtil.setContentDispositionHeader(requestStack, contentDispositionType, fileName);
				}

				//Content-Type
				if (contentType != null) {
					requestStack.getResponse().setContentType(contentType);
				}

				//Range
				RangeHeader range = null;
				if (isGetHeadRequest(method)) {
					range = RangeHeader.getRangeHeader(requestStack, size);
					size = RangeHeader.writeResponseHeader(requestStack, range, size);
				}

				//Content-Length
				requestStack.getResponse().setHeader("Content-Length", String.valueOf(size));

				//write body
				if (is != null) {
					OutputStream os = requestStack.getResponse().getOutputStream();
					//OutputStreamを使っていることをマーク（一度つかったら、getWriter()使えない。）
					requestStack.getRequestContext().setAttribute(WebRequestContext.MARK_USE_OUTPUT_STREAM, WebRequestContext.MARK_USE_OUTPUT_STREAM);
					RangeHeader.writeResponseBody(is, os, range);
				}
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.warn("can not close staticResource's inputstream:" + is, e);
					}
				}
			}
		}
	}

}
