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

package org.iplass.adminconsole.server.base.io.upload;

import static gwtupload.shared.UConsts.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileCountLimitExceededException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.spi.ServiceRegistry;

import gwtupload.server.AbstractUploadListener;
import gwtupload.server.HasKey;
import gwtupload.server.UploadAction;
import gwtupload.server.UploadServlet;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.exceptions.UploadCanceledException;
import gwtupload.server.exceptions.UploadException;
import gwtupload.server.exceptions.UploadSizeLimitException;
import gwtupload.server.exceptions.UploadTimeoutException;

/**
 * gwt-uploadのUploadActionで解決できない問題を解決する。
 *
 * <ul>
 * <li>
 * レスポンスに返すSummary情報を生成する際に、文字コードを指定する（デフォルトではマルチバイト文字が化ける）。
 * </li>
 * <li>
 * commons-fileupload の ServletFileUpload インスタンスに、最大ファイル数（fileCountMax）を設定する。
 * </li>
 * </ul>
 *
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AdminUploadAction extends UploadAction {

	private static final long serialVersionUID = -5553465242497700877L;

	/** copy: UploadAction - contextParameter removeSessionFiles */
	private boolean removeSessionFiles = false;
	/** copy: UploadAction - contextParameter removeData */
	private boolean removeData = false;

	/** 最大パラメータ(ファイル）数 */
	protected long maxParameterCount;

	private File contextTempDir;

	private AdminConsoleService acs = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// copy: UploadAction#init
		ServletContext ctx = config.getServletContext();
		removeSessionFiles = Boolean.parseBoolean(ctx.getInitParameter("removeSessionFiles"));
		removeData = Boolean.parseBoolean(ctx.getInitParameter("removeData"));

		WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
		// マルチパートリクエストの最大パラメータ数を設定する。
		maxParameterCount = webFront.getMaxMultipartParameterCount();

		contextTempDir = (File)config.getServletContext().getAttribute("javax.servlet.context.tempdir");

		//サイズ制限についてパラメータで指定されていない場合、Serviceの設定値をセット
		if (getInitParameter("maxSize") == null) {
			maxSize = acs.getMaxUploadFileSize();
		}
		if (getInitParameter("maxFileSize") == null) {
			maxFileSize = acs.getMaxUploadFileSize();
		}
	}

	/**
	 * @return contextTempDir
	 */
	protected File getContextTempDir() {
		return contextTempDir;
	}

	@Override
	protected Map<String, String> getFileItemsSummary(
			HttpServletRequest request, Map<String, String> stat) {
		if (stat == null) {
			stat = new HashMap<String, String>();
		}
		List<FileItem> s = getMyLastReceivedFileItems(request);
		if (s != null) {
			String files = "";
			String params = "";
			for (FileItem i : s) {
				if (i.isFormField()) {
					params += formFieldToXml(i);
				} else {
					files += fileFieldToXml(i);
				}
			}
			stat.put(TAG_FILES, files);
			stat.put(TAG_PARAMS, "<![CDATA[" + params + "]]>");
			stat.put(TAG_FINISHED, "ok");
		}
		return stat;
	}

	protected final byte[] convertFileToByte(File file) {
		byte[] bytes = null;
		FileInputStream is = null;
		FileChannel channel = null;
		try {
			is = new FileInputStream(file);
			channel = is.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
			channel.read(buffer);
			buffer.clear();
			bytes = new byte[buffer.capacity()];
			buffer.get(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return bytes;
	}

	private String formFieldToXml(FileItem i) {
		Map<String, String> item = new HashMap<String, String>();

		//カスタマイズ START
		//item.put(TAG_VALUE, "" + i.getString());
		try {
			item.put(TAG_VALUE, "" + i.getString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new UploadActionException(e);
		}
		//カスタマイズ END

		item.put(TAG_FIELD, "" + i.getFieldName());

		Map<String, String> param = new HashMap<String, String>();
		param.put(TAG_PARAM, statusToString(item));
		return statusToString(param);
	}

	private String fileFieldToXml(FileItem i) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(TAG_CTYPE, i.getContentType() != null ? i.getContentType()
				: "unknown");
		item.put(TAG_SIZE, "" + i.getSize());
		item.put(TAG_NAME, "" + i.getName());
		item.put(TAG_FIELD, "" + i.getFieldName());
		if (i instanceof HasKey) {
			String k = ((HasKey) i).getKeyString();
			item.put(TAG_KEY, k);
		}

		Map<String, String> file = new HashMap<String, String>();
		file.put(TAG_FILE, statusToString(item));
		return statusToString(file);
	}


	@Override
	protected FileItemFactory getFileItemFactory(long requestSize) {
		return new AdminFileItemFactory();
	}

	/**
	 * <p>FileItem生成用クラス</p>
	 *
	 * <p>
	 * 標準の gwtupload.server.UploadServlet#DefaultFileItemFactory では、
	 * 無条件でfieldNameにカウントが付いてしまうため、対応。
	 * </p>
	 */
	public static class AdminFileItemFactory extends DiskFileItemFactory {
		private HashMap<String, Integer> map = new HashMap<String, Integer>();

		@Override
		public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
			Integer cont = map.get(fieldName) != null ? (map.get(fieldName) + 1) : 0;
			map.put(fieldName, cont);

			// 複数の場合のみ、後ろにCountを付加
			if (cont > 0 || fieldName.contains(MULTI_SUFFIX)) {
				fieldName = fieldName.replace(MULTI_SUFFIX, "") + "-" + cont;
			}

			return super.createItem(fieldName, contentType, isFormField, fileName);
		}
	}

	/*
	 * from: UploadAction#doPost
	 *
	 * parsePostRequest の呼び出しを、super コールから this コールに変更する。
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String error = null;
		String message = null;
		Map<String, String> tags = new HashMap<String, String>();

		perThreadRequest.set(request);
		try {
			// Receive the files and form elements, updating the progress status

			// EDIT - parsePostRequest に変更する
			// error = super.parsePostRequest(request, response); // origin code
			error = parsePostRequest(request, response); // edit code

			if (error == null) {
				// Fill files status before executing user code which could remove session files
				getFileItemsSummary(request, tags);
				// Call to the user code
				message = executeAction(request, getMyLastReceivedFileItems(request));
			}
		} catch (UploadCanceledException e) {
			renderXmlResponse(request, response, "<" + TAG_CANCELED + ">true</" + TAG_CANCELED + ">");
			return;
		} catch (UploadActionException e) {
			logger.info("ExecuteUploadActionException when receiving a file.", e);
			error = e.getMessage();
		} catch (Exception e) {
			logger.info("Unknown Exception when receiving a file.", e);
			error = e.getMessage();
		} finally {
			perThreadRequest.set(null);
		}

		String postResponse = null;
		AbstractUploadListener listener = getCurrentListener(request);
		if (error != null) {
			postResponse = "<" + TAG_ERROR + ">" + error + "</" + TAG_ERROR + ">";
			renderXmlResponse(request, response, postResponse);
			if (listener != null) {
				listener.setException(new RuntimeException(error));
			}
			UploadServlet.removeSessionFileItems(request);
		} else {
			if (message != null) {
				// see issue #139
				tags.put("message", "<![CDATA[" + message + "]]>");
			}
			postResponse = statusToString(tags);
			renderXmlResponse(request, response, postResponse, true);
		}
		finish(request, postResponse);

		if (removeSessionFiles) {
			removeSessionFileItems(request, removeData);
		}
	}

	/*
	 * from: UploadServlet#parsePostRequest
	 *
	 * GWT Upload で利用している ServletFileUpload インスタンスに fileCountMax を指定したい。
	 *
	 * そのため、UploadServlet#parsePostRequest をコピー後カスタマイズし fileCountMax を設定する。
	 */
	@Override
	protected String parsePostRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			String delay = request.getParameter(PARAM_DELAY);
			uploadDelay = Integer.parseInt(delay);
		} catch (Exception e) {
		}

		HttpSession session = request.getSession();

		logger.debug("UPLOAD-SERVLET (" + session.getId() + ") new upload request received.");

		AbstractUploadListener listener = getCurrentListener(request);
		if (listener != null) {
			if (!listener.isFrozen() && !listener.isCanceled() && (listener.getPercent() < 100)) {
				String error = getMessage("busy");
				logger.error("UPLOAD-SERVLET (" + session.getId() + ") " + error);
				return error;
			}
			removeCurrentListener(request);
		}

		// Create a file upload progress listener, and put it in the user session,
		// so the browser can use ajax to query status of the upload process
		listener = createNewListener(request);

		List<FileItem> uploadedItems;
		try {

			// Call to a method which the user can override
			checkRequest(request);

			// Create the factory used for uploading files,
			FileItemFactory factory = getFileItemFactory(getContentLength(request));
			ServletFileUpload uploader = new ServletFileUpload(factory);
			uploader.setSizeMax(maxSize);
			uploader.setFileSizeMax(maxFileSize);
			uploader.setProgressListener(listener);
			// EDIT - fileCountMax を設定する
			uploader.setFileCountMax(maxParameterCount);

			// Receive the files
			logger.error("UPLOAD-SERVLET (" + session.getId() + ") parsing HTTP POST request ");
			uploadedItems = uploader.parseRequest(request);
			session.removeAttribute(getSessionLastFilesKey(request));
			logger.error(
					"UPLOAD-SERVLET (" + session.getId() + ") parsed request, " + uploadedItems.size() + " items received.");

			// Received files are put in session
			List<FileItem> sessionFiles = getMySessionFileItems(request);
			if (sessionFiles == null) {
				sessionFiles = new ArrayList<FileItem>();
			}

			String error = "";
			if (uploadedItems.size() > 0) {
				sessionFiles.addAll(uploadedItems);
				String msg = "";
				for (FileItem i : sessionFiles) {
					msg += i.getFieldName() + " => " + i.getName() + "(" + i.getSize() + " bytes),";
				}
				logger.debug("UPLOAD-SERVLET (" + session.getId() + ") puting items in session: " + msg);
				session.setAttribute(getSessionFilesKey(request), sessionFiles);
				session.setAttribute(getSessionLastFilesKey(request), uploadedItems);
			} else if (!isAppEngine()) {
				logger.error("UPLOAD-SERVLET (" + session.getId() + ") error NO DATA received ");
				error += getMessage("no_data");
			}
			return error.length() > 0 ? error : null;

			// So much silly questions in the list about this issue.
		} catch (LinkageError e) {
			logger.error("UPLOAD-SERVLET (" + request.getSession().getId() + ") Exception: " + e.getMessage() + "\n"
					+ stackTraceToString(e));
			RuntimeException ex = new UploadActionException(getMessage("restricted", e.getMessage()), e);
			listener.setException(ex);
			throw ex;
		} catch (SizeLimitExceededException e) {
			RuntimeException ex = new UploadSizeLimitException(e.getPermittedSize(), e.getActualSize());
			listener.setException(ex);
			throw ex;
		} catch (FileCountLimitExceededException e) {
			// EDIT - fileCountMax を設定することで発生する例外をハンドリング
			RuntimeException ex = new UploadActionException(
					"The request was rejected because its parameter count, exceeds the maximum: " + maxParameterCount, e);
			listener.setException(ex);
			throw ex;
		} catch (UploadSizeLimitException e) {
			listener.setException(e);
			throw e;
		} catch (UploadCanceledException e) {
			listener.setException(e);
			throw e;
		} catch (UploadTimeoutException e) {
			listener.setException(e);
			throw e;
		} catch (Throwable e) {
			logger.error("UPLOAD-SERVLET (" + request.getSession().getId() + ") Unexpected Exception -> " + e.getMessage()
			+ "\n" + stackTraceToString(e));
			e.printStackTrace();
			RuntimeException ex = new UploadException(e);
			listener.setException(ex);
			throw ex;
		}
	}

	/*
	 * from: UploadServlet#getContentLength
	 */
	private long getContentLength(HttpServletRequest request) {
		long size = -1;
		try {
			size = Long.parseLong(request.getHeader(FileUploadBase.CONTENT_LENGTH));
		} catch (NumberFormatException e) {
		}
		return size;
	}
}
