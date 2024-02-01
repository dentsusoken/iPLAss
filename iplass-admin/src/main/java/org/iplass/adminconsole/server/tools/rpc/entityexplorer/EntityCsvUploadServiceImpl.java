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

package org.iplass.adminconsole.server.tools.rpc.entityexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.upload.AdminUploadAction;
import org.iplass.adminconsole.server.base.io.upload.UploadRuntimeException;
import org.iplass.adminconsole.server.base.io.upload.UploadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.tools.rpc.entityexplorer.exception.EntityCsvFatalException;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportResult;
import org.iplass.mtp.impl.tools.entityport.EntityPortingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import gwtupload.server.exceptions.UploadActionException;

@SuppressWarnings("serial")
public class EntityCsvUploadServiceImpl extends AdminUploadAction {

	private static final Logger logger = LoggerFactory.getLogger(EntityCsvUploadServiceImpl.class);

	/* (非 Javadoc)
	 * @see gwtupload.server.UploadAction#executeAction(javax.servlet.http.HttpServletRequest, java.util.List)
	 */
	@Override
	public String executeAction(final HttpServletRequest request,
			final List<FileItem> sessionFiles) throws UploadActionException {

		final EntityImportResponseInfo result = new EntityImportResponseInfo();
		final HashMap<String,Object> args = new HashMap<>();
		try {

			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

		    //リクエスト情報の検証
			validateRequest(args);

			//テナントID
			int tenantId = Integer.parseInt((String)args.get("tenantId"));

			EntityDataImportResult importResult = AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<EntityDataImportResult>() {

				@Override
				public EntityDataImportResult call() {

				    //リクエスト->EntityDataImportCondition
				    EntityDataImportCondition cond = new EntityDataImportCondition();
					cond.setTruncate(args.containsKey("chkTruncate"));
					cond.setBulkUpdate(args.containsKey("chkBulkUpdate"));
					if (args.containsKey("commitLimit")) {
						cond.setCommitLimit(Integer.parseInt((String)args.get("commitLimit")));
					}
					cond.setNotifyListeners(args.containsKey("chkNotifyListeners"));
					if (args.containsKey("chkUpdateDisupdatableProperty")) {
						//更新不可項目も更新する場合はValidationはfalse
						cond.setUpdateDisupdatableProperty(true);
						cond.setWithValidation(false);
					} else {
						cond.setUpdateDisupdatableProperty(false);
						cond.setWithValidation(args.containsKey("chkWithValidation"));
					}
					cond.setInsertEnableAuditPropertySpecification(args.containsKey("chkInsertEnableAuditPropertySpecification"));
					cond.setErrorSkip(args.containsKey("chkErrorSkip"));
					cond.setIgnoreNotExistsProperty(args.containsKey("chkIgnoreNotExistsProperty"));
					cond.setFourceUpdate(args.containsKey("chkForceUpdate"));
					if (args.containsKey("prefixOid")) {
						cond.setPrefixOid((String)args.get("prefixOid"));
					}
					if (args.containsKey("locale")) {
						cond.setLocale((String)args.get("locale"));
					}
					if (args.containsKey("timeZone")) {
						cond.setTimezone((String)args.get("timeZone"));
					}
					if (args.containsKey("uniqueKey")) {
						cond.setUniqueKey((String)args.get("uniqueKey"));
					}

					//MetaDataEntry
					String entityPath =  EntityService.ENTITY_META_PATH + ((String)args.get("defName")).replace(".", "/");
					MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);
					if (entry == null) {
						throw new EntityCsvFatalException(rs("tools.entityexplorer.EntityCsvUploadServiceImpl.canNotGetEntityInfo", entityPath));
					}

					//File
					File tempFile = (File)args.get("targetFile");
					String fileName = (String)args.get("targetFileName");

					try (FileInputStream fis = new FileInputStream(tempFile)) {
						EntityPortingService service = ServiceRegistry.getRegistry().getService(EntityPortingService.class);
						return service.importEntityData(fileName, fis, entry, cond, null);
					} catch (IOException e) {
						throw new EntityCsvFatalException(rs("tools.entityexplorer.EntityCsvUploadServiceImpl.errReadFile"), e);
					}
				}

			});

			//EntityDataImportResult->EntityImportResponseInfo
			if (importResult.isError()) {
				result.setStatusError();
			} else {
				result.setStatusSuccess();
			}
			result.setMessages(importResult.getMessages());
			result.setInsertCount(importResult.getInsertCount());
			result.setUpdateCount(importResult.getUpdateCount());
			result.setErrorCount(importResult.getErrorCount());

		} catch (EntityCsvFatalException e) {
			logger.error(e.getMessage(), e);

			result.setStatusError();
			result.addMessage(e.getMessage());
		} catch (Exception e) {
			throw new UploadActionException(e);
		} finally {
			//Tempファイルを削除
			File tempFile = (File)args.get("targetFile");
			if (tempFile != null) {
				if (!tempFile.delete()) {
					logger.warn("Fail to delete temporary resource:" + tempFile.getPath());
				}
			}
		}

	    try {
			return createJsonResponseInfo(result);
		} catch (IOException e) {
			throw new UploadActionException(e);
		}
	}

	private void readRequest(final HttpServletRequest request, List<FileItem> sessionFiles, HashMap<String,Object> args) {

		//セッションからファイルを取得
		File tempFile = null;
		try {
			for (FileItem item : sessionFiles) {
				if (item.isFormField()) {
			    	//File以外のもの
			        args.put(item.getFieldName(), UploadUtil.getValueAsString(item));
				} else {
					tempFile = UploadUtil.writeFileToTemporary(item, getContextTempDir());
					args.put("targetFile", tempFile);
					args.put("targetFileName", item.getName());
				}
			}
		} catch (UploadRuntimeException e) {
			throw new EntityCsvFatalException(rs("tools.entityexplorer.EntityCsvUploadServiceImpl.errReadFile"), e);
		}
	}

	private void validateRequest(HashMap<String,Object> args) {
		if (!args.containsKey("tenantId")) {
			throw new EntityCsvFatalException(rs("tools.entityexplorer.EntityCsvUploadServiceImpl.canNotGetTenantInfo"));
		}
		if (!args.containsKey("defName")) {
			throw new EntityCsvFatalException(rs("tools.entityexplorer.EntityCsvUploadServiceImpl.canNotGetImportTargetEntity"));
		}
		if (!args.containsKey("targetFile")) {
			throw new EntityCsvFatalException(rs("tools.entityexplorer.EntityCsvUploadServiceImpl.canNotGetImportFile"));
		}
	}

	private String createJsonResponseInfo(EntityImportResponseInfo result) throws IOException {

		if (result == null) {
			return "";
		}

		final StringBuilder builder = new StringBuilder();
		//JSONで出力
		ObjectMapper mapper = new ObjectMapper();
		//for backward compatibility
		mapper.configOverride(java.sql.Date.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd").withTimeZone(TimeZone.getDefault()));

		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createGenerator(new Writer() {

			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				builder.append(cbuf, off, len);
			}

			@Override
			public void flush() throws IOException {
			}

			@Override
			public void close() throws IOException {
			}
		});

		mapper.writeValue(g, result);
		g.close();

		return builder.toString();
	}

	/**
	 * インポート結果情報
	 */
	private class EntityImportResponseInfo extends LinkedHashMap<String, Object>{

		private static final long serialVersionUID = 1L;

		private long insertCount = 0;
		private long updateCount = 0;
		private long errorCount = 0;

		public EntityImportResponseInfo() {
			setStatus("INIT");
			put("messages", new ArrayList<String>());
		}

		public String getStatus() {
			return (String)get("status");
		}

		public void setStatus(String status) {
			put("status", status);
		}

		public void setStatusError() {
			setStatus("ERROR");
		}

		public void setStatusSuccess() {
			if (!"ERROR".equals(getStatus()) && !"WARN".equals(getStatus())) {
				setStatus("SUCCESS");
			}
		}

		@SuppressWarnings("unchecked")
		public void setMessages(List<String> message) {
			List<String> messages = (List<String>)get("messages");
			messages.clear();
			messages.addAll(message);
		}

		@SuppressWarnings("unchecked")
		public void addMessage(String message) {
			List<String> messages = (List<String>)get("messages");
			messages.add(message);
		}

		@SuppressWarnings("unused")
		public long getInsertCount() {
			return insertCount;
		}
		public void setInsertCount(long insertCount) {
			this.insertCount = insertCount;
		}

		@SuppressWarnings("unused")
		public long getUpdateCount() {
			return updateCount;
		}
		public void setUpdateCount(long updateCount) {
			this.updateCount = updateCount;
		}

		@SuppressWarnings("unused")
		public long getErrorCount() {
			return errorCount;
		}
		public void setErrorCount(long errorCount) {
			this.errorCount = errorCount;
		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
