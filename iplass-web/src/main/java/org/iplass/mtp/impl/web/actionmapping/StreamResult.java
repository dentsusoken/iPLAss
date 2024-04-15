/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.servlet.ServletException;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.LobHandler;
import org.iplass.mtp.impl.properties.extend.BinaryType;
import org.iplass.mtp.impl.web.RangeHeader;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.ResultStreamWriter;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.result.ContentDispositionType;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StreamResultDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StreamResult extends Result {

	private static final long serialVersionUID = -6801751736171646783L;

	private static Logger logger = LoggerFactory.getLogger(StreamResult.class);

	private String inputStreamAttributeName;
	private String contentTypeAttributeName = "contentType";
	private String contentLengthAttributeName = "contentLength";
	private boolean useContentDisposition;
	private ContentDispositionType contentDispositionType;
	private String fileNameAttributeName = "fileName";
	private boolean acceptRanges;

	public StreamResult() {
	}

	public StreamResult(String cmdStatus, String inputStreamAttributeName) {
		setCommandResultStatus(cmdStatus);
		this.inputStreamAttributeName = inputStreamAttributeName;
	}

	public boolean isAcceptRanges() {
		return acceptRanges;
	}

	public void setAcceptRanges(boolean acceptRanges) {
		this.acceptRanges = acceptRanges;
	}

	public String getInputStreamAttributeName() {
		return inputStreamAttributeName;
	}

	public void setInputStreamAttributeName(String inputStreamAttributeName) {
		this.inputStreamAttributeName = inputStreamAttributeName;
	}

	public String getContentTypeAttributeName() {
		return contentTypeAttributeName;
	}

	public void setContentTypeAttributeName(String contentTypeAttributeName) {
		this.contentTypeAttributeName = contentTypeAttributeName;
	}

	public String getContentLengthAttributeName() {
		return contentLengthAttributeName;
	}

	public void setContentLengthAttributeName(String contentLengthAttributeName) {
		this.contentLengthAttributeName = contentLengthAttributeName;
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

	public String getFileNameAttributeName() {
		return fileNameAttributeName;
	}

	public void setFileNameAttributeName(String fileNameAttributeName) {
		this.fileNameAttributeName = fileNameAttributeName;
	}

	@Override
	public ResultRuntime createRuntime() {
		return new StreamResultRuntime();
	}

	@Override
	public void applyConfig(ResultDefinition definition) {
		fillFrom(definition);
		StreamResultDefinition def = (StreamResultDefinition) definition;
		inputStreamAttributeName = def.getInputStreamAttributeName();
		contentTypeAttributeName = def.getContentTypeAttributeName();
		contentLengthAttributeName = def.getContentLengthAttributeName();
		useContentDisposition = def.isUseContentDisposition();
		contentDispositionType = def.getContentDispositionType();
		fileNameAttributeName = def.getFileNameAttributeName();
		acceptRanges = def.isAcceptRanges();
	}

	@Override
	public ResultDefinition currentConfig() {
		StreamResultDefinition definition = new StreamResultDefinition();
		fillTo(definition);
		definition.setInputStreamAttributeName(inputStreamAttributeName);
		definition.setContentTypeAttributeName(contentTypeAttributeName);
		definition.setContentLengthAttributeName(contentLengthAttributeName);
		definition.setUseContentDisposition(useContentDisposition);
		definition.setContentDispositionType(contentDispositionType);
		definition.setFileNameAttributeName(fileNameAttributeName);
		definition.setAcceptRanges(acceptRanges);
		return definition;
	}

	public class StreamResultRuntime extends ResultRuntime {

		@Override
		public StreamResult getMetaData() {
			return StreamResult.this;
		}

		@Override
		public void finallyProcess(WebRequestStack requestContext) {
			RequestContext cmdRequestContext = requestContext.getRequestContext();
			Object bin = cmdRequestContext.getAttribute(inputStreamAttributeName);
			if (bin != null && bin instanceof InputStream) {
				InputStream is = (InputStream) bin;
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("InputSteam.close() failed.maybe leak...", e);
				}
			}
		}

		@Override
		public void handle(WebRequestStack requestContext)
				throws ServletException, IOException {

			RequestContext cmdRequestContext = requestContext.getRequestContext();
			Object bin = cmdRequestContext.getAttribute(inputStreamAttributeName);

			if (bin == null) {
				throw new WebProcessRuntimeException("binaryData is null");
			}

			InputStream is = null;
			String contentType = (String) cmdRequestContext.getAttribute(contentTypeAttributeName);
			Lob binData = null;
			Number size = (Number) cmdRequestContext.getAttribute(contentLengthAttributeName);
			if (bin instanceof InputStream) {
				is = (InputStream) bin;
			} else if (bin instanceof byte[]) {
				is = new ByteArrayInputStream((byte[]) bin);
			} else if (bin instanceof BinaryReference) {
				binData = LobHandler.getInstance(BinaryType.LOB_STORE_NAME).getBinaryData(((BinaryReference) bin).getLobId());
				is = binData.getBinaryInputStream();
				if (contentType == null) {
					contentType = binData.getType();
				}
				if (size == null) {
					if (binData.getSize() > 0) {
						size = binData.getSize();
					}
				}
			}

			//Rangeヘッダ対応
			boolean doRange = (acceptRanges && is != null && size != null && !(bin instanceof ResultStreamWriter));
			RangeHeader range = null;
			if (doRange) {
				range = RangeHeader.getRangeHeader(requestContext, size.longValue());
			}

			try {
				if (contentType != null) {
					requestContext.getResponse().setContentType(contentType.replace("\n", "").replace("\r", ""));
				}
				long contentLength = (size == null ? -1 : size.longValue());
				if (doRange) {
					contentLength = RangeHeader.writeResponseHeader(requestContext, range, contentLength);
				}

				if (contentLength != -1) {
					requestContext.getResponse().setHeader("Content-Length", String.valueOf(contentLength));
				}

				if (useContentDisposition) {
					String fileName = null;
					if (StringUtil.isNotEmpty(fileNameAttributeName)) {
						fileName = (String)cmdRequestContext.getAttribute(fileNameAttributeName);
					}
					if (fileName == null && binData != null) {
						fileName = binData.getName();
					}
					if (fileName == null) {
						fileName = getLastActionName(cmdRequestContext);
					}
					if (fileName != null) {
						WebUtil.setContentDispositionHeader(requestContext, getContentDispositionType(), fileName);
					}
				}

				long start = 0L;
				if (logger.isDebugEnabled()) {
					start = System.currentTimeMillis();
				}

				OutputStream os = requestContext.getResponse().getOutputStream();
				//OutputStreamを使っていることをマーク（一度つかったら、getWriter()使えない。）
				cmdRequestContext.setAttribute(WebRequestContext.MARK_USE_OUTPUT_STREAM, WebRequestContext.MARK_USE_OUTPUT_STREAM);

				if (bin instanceof ResultStreamWriter) {
					try {
						((ResultStreamWriter) bin).write(os);
						os.flush();
					} finally {
						if (logger.isDebugEnabled()) {
							logger.debug("stream download done.time:" + (System.currentTimeMillis() - start));
						}
					}
				} else {

					if (is == null) {
						throw new WebProcessRuntimeException("input stream is null");
					}

					try {
						RangeHeader.writeResponseBody(is, os, range);
					} finally {
						if (logger.isDebugEnabled()) {
							logger.debug("stream download done.time:" + (System.currentTimeMillis() - start));
						}
					}

				}
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.warn("can not close inputstream resource:" + is, e);
					}
				}
			}
		}

		private String getLastActionName(RequestContext cmdRequestContext) {
			String actionName = (String)cmdRequestContext.getAttribute(WebRequestConstants.ACTION_NAME);
			if (actionName == null) {
				return null;
			}
			if (actionName.contains("/")) {
				return actionName.substring(actionName.lastIndexOf("/") + 1);
			} else {
				return actionName;
			}
		}
}

}
