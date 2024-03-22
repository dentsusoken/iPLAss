/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.metaexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.io.FilenameUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.upload.AdminUploadAction;
import org.iplass.adminconsole.server.base.io.upload.MultipartRequestParameter;
import org.iplass.adminconsole.server.base.io.upload.UploadActionException;
import org.iplass.adminconsole.server.base.io.upload.UploadResponseInfo;
import org.iplass.adminconsole.server.base.io.upload.UploadRuntimeException;
import org.iplass.adminconsole.server.base.io.upload.UploadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.rpc.util.TransactionUtil;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ConfigUploadProperty;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.metadata.MetaDataJAXBService;
import org.iplass.mtp.impl.tools.metaport.MetaDataTagEntity;
import org.iplass.mtp.impl.xml.jaxb.SecureSAXParserFactory;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class MetaDataConfigUploadServiceImpl extends AdminUploadAction {

	private static final Logger logger = LoggerFactory.getLogger(MetaDataConfigUploadServiceImpl.class);

	@Override
	public String executeAction(final HttpServletRequest request,
			final List<MultipartRequestParameter> sessionFiles) throws UploadActionException {

		final MetaDataConfigUploadResponseInfo result = new MetaDataConfigUploadResponseInfo();
		final HashMap<String,Object> args = new HashMap<String,Object>();
		try {

			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

			//リクエスト情報の検証
			validateRequest(args);

			//テナントIDの取得
			int tenantId = Integer.parseInt((String)args.get(ConfigUploadProperty.TENANT_ID));

			//ここでトランザクションを開始
			AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<Void>() {

				@Override
				public Void call() {
					File file = (File)args.get(ConfigUploadProperty.UPLOAD_FILE);
					String fileName = (String)args.get(ConfigUploadProperty.UPLOAD_FILE_NAME);

					//ファイルの変換チェック
					validateFile(file);

					//ファイルの保存
					MetaDataPortingLogic logic = MetaDataPortingLogic.getInstance();
					Entity entity = logic.saveImportFile(file, fileName, null, MetaDataTagEntity.TYPE_IMPORT_FILE);

					//ステータスの書き込み
					result.setStatusSuccess();
					result.addStatusMessage(resourceString("storedFileBasis") + entity.getName() + "(" + entity.getOid() + ")");

					//OIDを格納
					result.setFileOid(entity.getOid());

					return null;
				}
			});

		} catch (UploadRuntimeException e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
			result.setStatusError();
			result.addMessage(e.getMessage());
		} catch (Exception e) {
			throw new UploadActionException(e);
		} finally {
			//Tempファイルを削除
			File file = (File)args.get(ConfigUploadProperty.UPLOAD_FILE);
			if (file != null) {
				if (!file.delete()) {
					logger.warn("Fail to delete temporary resource:" + file.getPath());
				}
			}
		}

		//ResultをJSON形式に変換
		try {
			return UploadUtil.toJsonResponse(result);
		} catch (UploadRuntimeException e) {
			throw new UploadActionException(e);
		}
	}

	private void readRequest(final HttpServletRequest request, List<MultipartRequestParameter> sessionFiles, HashMap<String, Object> args) {

		//リクエスト情報の取得
		try {
			for (MultipartRequestParameter item : sessionFiles) {
				if (item.isFormField()) {
					//File以外のもの
					args.put(item.getFieldName(), UploadUtil.getValueAsString(item));
				} else {
					//Fileの場合、tempに書きだし
					args.put(ConfigUploadProperty.UPLOAD_FILE_NAME, FilenameUtils.getName(item.getName()));
					File tempFile = UploadUtil.writeFileToTemporary(item, getContextTempDir());
					args.put(ConfigUploadProperty.UPLOAD_FILE, tempFile);
				}
			}
		} catch (UploadRuntimeException e) {
			throw new UploadRuntimeException(resourceString("errReadingRequestInfo"), e);
		}
	}

	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(ConfigUploadProperty.UPLOAD_FILE) == null) {
			throw new UploadRuntimeException(resourceString("canNotGetImportFile"));
		}

		if (args.get(ConfigUploadProperty.TENANT_ID) == null) {
			throw new UploadRuntimeException(resourceString("tenantInfoCannotGet"));
		}
	}

	private void validateFile(File file) {

		try {
			//変換できるかチェック
			MetaDataJAXBService jaxb = ServiceRegistry.getRegistry().getService(MetaDataJAXBService.class);
			JAXBContext jaxbContext = jaxb.getJAXBContext();
			try (FileInputStream fis = new FileInputStream(file)) {
				Unmarshaller um = jaxbContext.createUnmarshaller();
				um.unmarshal(toSaxSource(fis));
			} catch (IOException e) {
				throw new JAXBException(e);
			}

		} catch (JAXBException e) {
			throw new UploadRuntimeException(resourceString("canNotConvertXml"), e);
		}

	}

	private SAXSource toSaxSource(InputStream is) throws JAXBException {
		//外部参照を処理しない
		SAXParserFactory f = SAXParserFactory.newInstance();
		f.setNamespaceAware(true);
		f.setValidating(false);
		f = new SecureSAXParserFactory(f);
		try {
			return new SAXSource(f.newSAXParser().getXMLReader(), new InputSource(is));
		} catch (SAXException | ParserConfigurationException e) {
			throw new JAXBException(e);
		}
	}

	private class MetaDataConfigUploadResponseInfo extends UploadResponseInfo {

		public MetaDataConfigUploadResponseInfo() {
			super();
		}

		public void setFileOid(String fileOid) {
			put(ConfigUploadProperty.FILE_OID, fileOid);
		}

	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("tools.metaexplorer.MetaDataConfigUploadServiceImpl." + suffix, arguments);
	}

}
