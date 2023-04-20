/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import gwtupload.server.AbstractUploadListener;
import gwtupload.server.HasKey;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.exceptions.UploadCanceledException;
import gwtupload.server.exceptions.UploadException;
import gwtupload.server.exceptions.UploadSizeLimitException;
import gwtupload.server.exceptions.UploadTimeoutException;

// NOTE AdminUploadAction に拡張していくと責務が大きくなりすぎてしまうため、リファクタリング
/**
 * ファイルアップロードアクションクラス（セッションを利用せず、ファイル情報をローカル変数対応版）
 * 
 * <p>
 * 修正概要
 * <ol>
 * <li>アップロードしたファイルをセッションに格納せず、メソッドローカル変数で管理する形に変更</li>
 * <li>レスポンスに返すSummary情報を生成する際に、文字コードを指定（デフォルトではマルチバイト文字が化ける）</li>
 * <li>UploadListener もセッションを利用しない（isAppEngine の返却値を常に true 返却する）</li>
 * </ol>
 * </p>
 * 
 * <p>
 * UploadAction ではアップロード対象ファイルの情報（FileItem）をセッションで管理することを基本としている。
 * 本クラスは、commons-fileupload のバージョンアップの影響により FileItem が Serializable を実装しなくなった為、
 * セッションに格納せず、ローカル変数に格納し情報を管理す方法を取る。 
 * 
 * トレードオフとして、セッションをまたいだファイルアップロード操作ができなくなってしまう。
 * ※AdminConsoleのファイルアップロードでは、ファイルを取得した後にファイル削除しているため、実質影響はない。
 * </p>
 * 
 * <p>
 * UploadListener もセッション利用しないことを選択。
 * 注意点として、サーバをクラスタ構成にしている場合、アップロード処理の管理がサーバ毎になる。
 * Stickyセッションを選択していない場合は、同一セッションで複数アップロード処理が実行できるケースがある。
 * </p>
 * 
 * <p>
 * 注記
 * <ul>
 * <li>
 * {@link gwtupload.server.UploadServlet#getUploadStatus(HttpServletRequest, String, Map)} でセッションファイルを参照している実装があるが、
 * AdminConsole のファイルアップロードではファイルアップロード状況を確認しない実装とした。
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Override メソッドは修正元のメソッドをコピーし、修正したポイントについては、EDIT のメモを記載してある。
 * </p>
 * 
 * @author SEKIGUCHI Naoya
 *
 */
public class UploadActionNotUseSession extends UploadAction {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7875576716469462797L;

	// ===================================== classes =====================================
	// ------------------------------------- CUSTOMIZED -------------------------------------
	/**
	 * ParsePostRequest メソッドの返却値クラス
	 * 文字列 error の他に、アップロード対象ファイル uploadItems を取得したいため作成。
	 * 
	 * @author SEKIGUCHI Naoya
	 *
	 */
	private static class ParsePostRequestResult {
		/** エラー文字列 */
		private String error;
		/** アップロードファイル */
		private List<FileItem> uploadedItems;

		/**
		 * コンストラクタ
		 * @param error エラー文字列
		 * @param uploadedItems アップロードアイテム
		 */
		public ParsePostRequestResult(String error, List<FileItem> uploadedItems) {
			this.error = error;
			this.uploadedItems = uploadedItems;
		}
	}

	// ===================================== members =====================================
	// ------------------------------------- CUSTOMIZED -------------------------------------
	private Charset formFieldToXmlCharset = StandardCharsets.UTF_8;

	// ------------------------------------- from UploadAction -------------------------------------
	private boolean removeSessionFiles = false;
	private boolean removeData = false;

	// ===================================== methods =====================================
	// ------------------------------------- CUSTOMIZED -------------------------------------
	/**
	 * formFieldToXml の復号時の文字コードを設定する
	 * 本メソッドで指定しない場合のデフォルト値は、{@link StandardCharsets.UTF_8} である。
	 * 
	 * @param charset 文字コードセット
	 */
	public void setFormFieldToXmlCharsets(Charset charset) {
		this.formFieldToXmlCharset = charset;
	}

	/**
	 * ServletFileUpload インスタンスを返却する
	 * 
	 * @param factory FileItemFactory
	 * @param listener UploadListener インスタンス
	 * @return ServletFileUpload インスタンス
	 */
	protected ServletFileUpload getServletFileUpload(FileItemFactory factory, AbstractUploadListener listener) {
		ServletFileUpload uploader = new ServletFileUpload(factory);
		uploader.setSizeMax(maxSize);
		uploader.setFileSizeMax(maxFileSize);
		uploader.setProgressListener(listener);

		return uploader;
	}

	/**
	 * ファイルアイテムを削除する
	 * 
	 * @param request HttpServletRequest
	 * @param removeData ファイルを削除するか否か
	 */
	protected void removeFileItems(HttpServletRequest request, boolean removeData, List<FileItem> uploadedItems) {
		// アップロード処理が完了したら、FileItem をクリア
		logger.debug("UPLOAD-SERVLET (" + request.getSession().getId() + ") removeFileItems: removeData=" + removeData);
		// UploadServlet#removeSessionFileItems の実装に合わせる
		if (removeData && uploadedItems != null) {
			logger.debug("UPLOAD-SERVLET (" + request.getSession().getId() + ") removeFileItems: remove section.");
			for (FileItem f : uploadedItems) {
				if (f != null && !f.isFormField()) {
					f.delete();
				}
			}
		}
	}

	// ------------------------------------- from UploadAction -------------------------------------
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext ctx = config.getServletContext();
		removeSessionFiles = Boolean.valueOf(ctx.getInitParameter("removeSessionFiles"));
		removeData = Boolean.valueOf(ctx.getInitParameter("removeData"));

		logger.info("UPLOAD-ACTION init: removeSessionFiles=" + removeSessionFiles + ", removeData=" + removeData);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String error = null;
		String message = null;
		Map<String, String> tags = new HashMap<String, String>();

		// EDIT - アップロード対象FileItemを追加
		List<FileItem> uploadedItems = null;

		perThreadRequest.set(request);
		try {
			try {
				// Receive the files and form elements, updating the progress status
				// EDIT - parsePostRequest に変更する。try 内だけで一時的に利用する。
				// error = super.parsePostRequest(request, response); // origin code
				ParsePostRequestResult parseResult = doParsePostRequest(request, response); // edit code
				error = parseResult.error;
				uploadedItems = parseResult.uploadedItems;
				if (error == null) {
					// EDIT - postリクエストで受信したファイルを利用する
					// // Fill files status before executing user code which could remove session files
					// getFileItemsSummary(request, tags);
					// // Call to the user code
					// message = executeAction(request, getMyLastReceivedFileItems(request));
					// Fill files status before executing user code which could remove session files
					getFileItemsSummary(request, tags, uploadedItems);
					// Call to the user code
					message = executeAction(request, uploadedItems);
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
				// EDIT - セッション利用しないので、セッション内のファイル削除処理を削除
				// UploadServlet.removeSessionFileItems(request);
			} else {
				if (message != null) {
					// see issue #139
					tags.put("message", "<![CDATA[" + message + "]]>");
				}
				postResponse = statusToString(tags);
				renderXmlResponse(request, response, postResponse, true);
			}
			finish(request, postResponse);

			// EDIT - セッション利用しないので、セッション内のファイル削除処理を削除
			// if (removeSessionFiles) {
			// removeSessionFileItems(request, removeData);
			// }
		} finally {
			// EDIT - POST メソッド処理の終了後に FileItem を削除する
			removeFileItems(request, removeData, uploadedItems);
		}
	}

	// ------------------------------------- from UploadServlet -------------------------------------
  /**
	 * Return the list of FileItems stored in session under the session key.
	 */
	// FIXME(manolo): Not sure about the convenience of this and sessionFilesKey.
	@Deprecated
	@Override
	public final List<FileItem> getMySessionFileItems(HttpServletRequest request) {
		// EDIT - セッションは利用しない。セッションをまたいだファイル情報は返却しない。final 化し、deprecated 設定。
		// return getSessionFileItems(request, getSessionFilesKey(request));
		return null;
  }
  
	/**
	 * Return the most recent list of FileItems received under the session key
	 */
	@Deprecated
	@Override
	public final List<FileItem> getMyLastReceivedFileItems(HttpServletRequest request) {
		// EDIT - セッションは利用しない。アップロードしたファイルは POST リクエスト実行中のみ有効とする。final 化し、deprecated 設定。
		// return getLastReceivedFileItems(request, getSessionLastFilesKey(request));
		return null;
	}

	/**
	* Just a method to detect whether the web container is running with appengine
	* restrictions.
	*
	* @return true if the case of the application is running in appengine
	*/
	@Override
	public boolean isAppEngine() {
		// EDIT - APサーバ内で完結する（常にtrue）形にする
		// return appEngine;
		return true;
	}

	/**
	 * This method parses the submit action, puts in session a listener where the
	 * progress status is updated, and eventually stores the received data in
	 * the user session.
	 *
	 * returns null in the case of success or a string with the error
	 *
	 */
	protected ParsePostRequestResult doParsePostRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			String delay = request.getParameter(PARAM_DELAY);
			uploadDelay = Integer.parseInt(delay);
		} catch (Exception e) {
		}

		HttpSession session = request.getSession();

		logger.debug("UPLOAD-SERVLET (" + session.getId() + ") new upload request received.");

		AbstractUploadListener listener = getCurrentListener(request);
		if (listener != null) {
			if (listener.isFrozen() || listener.isCanceled() || listener.getPercent() >= 100) {
				removeCurrentListener(request);
			} else {
				String error = getMessage("busy");
				logger.error("UPLOAD-SERVLET (" + session.getId() + ") " + error);
				// EDIT - 返却値は ParsePosetRequestResult に変更
				// return error;
				return new ParsePostRequestResult(error, null);
			}
		}

		// Create a file upload progress listener, and put it in the user session,
		// so the browser can use ajax to query status of the upload process
		listener = createNewListener(request);

		List<FileItem> uploadedItems;
		ServletFileUpload uploader = null;
		try {

			// Call to a method which the user can override
			checkRequest(request);

			// Create the factory used for uploading files,
			FileItemFactory factory = getFileItemFactory(getContentLength(request));
			// EDIT - uploader 取得実装は、メソッドへ切り出し
			// ServletFileUpload uploader = new ServletFileUpload(factory);
			// uploader.setSizeMax(maxSize);
			// uploader.setFileSizeMax(maxFileSize);
			// uploader.setProgressListener(listener);
			uploader = getServletFileUpload(factory, listener);
			// Receive the files
			logger.error("UPLOAD-SERVLET (" + session.getId() + ") parsing HTTP POST request ");
			uploadedItems = uploader.parseRequest(request);
			session.removeAttribute(getSessionLastFilesKey(request));
			logger.error("UPLOAD-SERVLET (" + session.getId() + ") parsed request, " + uploadedItems.size() + " items received.");

			// EDIT - セッションには格納しない
			// // Received files are put in session
			// List<FileItem> sessionFiles = getMySessionFileItems(request);
			// if (sessionFiles == null) {
			// sessionFiles = new ArrayList<FileItem>();
			// }
			//
			// String error = "";
			// if (uploadedItems.size() > 0) {
			// sessionFiles.addAll(uploadedItems);
			// String msg = "";
			// for (FileItem i : sessionFiles) {
			// msg += i.getFieldName() + " => " + i.getName() + "(" + i.getSize() + " bytes),";
			// }
			// logger.debug("UPLOAD-SERVLET (" + session.getId() + ") puting items in session: " + msg);
			// session.setAttribute(getSessionFilesKey(request), sessionFiles);
			// session.setAttribute(getSessionLastFilesKey(request), uploadedItems);
			// } else if (!isAppEngine()){
			// logger.error("UPLOAD-SERVLET (" + session.getId() + ") error NO DATA received ");
			// error += getMessage("no_data");
			// }
			// return error.length() > 0 ? error : null;

			// uploadedItems を返却する（元ロジックには、isAppEngine で判定する箇所が存在していたが、常に true を返却するため、シンプルな形に変更。
			return new ParsePostRequestResult(null, uploadedItems);

			// So much silly questions in the list about this issue.
		} catch (LinkageError e) {
			logger.error(
					"UPLOAD-SERVLET (" + request.getSession().getId() + ") Exception: " + e.getMessage() + "\n" + stackTraceToString(e));
			RuntimeException ex = new UploadActionException(getMessage("restricted", e.getMessage()), e);
			listener.setException(ex);
			throw ex;
		} catch (SizeLimitExceededException e) {
			RuntimeException ex = new UploadSizeLimitException(e.getPermittedSize(), e.getActualSize());
			listener.setException(ex);
			throw ex;
		} catch (FileCountLimitExceededException e) {
			// EDIT - fileCountMax を設定することで発生する例外をハンドリング。uploader からエラーがスローされるため、このポイントでは uploder != null である。
			RuntimeException ex = new UploadActionException(
					"The request was rejected because its parameter count, exceeds the maximum: " + uploader.getFileCountMax(), e);
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
			logger.error("UPLOAD-SERVLET (" + request.getSession().getId() + ") Unexpected Exception -> " + e.getMessage() + "\n"
					+ stackTraceToString(e));
			e.printStackTrace();
			RuntimeException ex = new UploadException(e);
			listener.setException(ex);
			throw ex;
		}
	}

	@Deprecated
	@Override
	protected final String parsePostRequest(HttpServletRequest request, HttpServletResponse response) {
		// EDIT - 現行のメソッドは廃止。final 化し、Override も禁止。
		throw new UnsupportedOperationException(
				"Method is unavailable. Please use #doParsePostRequest(HttpServletRequest, HttpServletResponse).");
	}

	protected Map<String, String> getFileItemsSummary(HttpServletRequest request, Map<String, String> stat,
			List<FileItem> uploadedItems) {
		if (stat == null) {
			stat = new HashMap<String, String>();
		}
		// EDIT - パラメータのファイルアイテムを利用する
		// List<FileItem> s = getMyLastReceivedFileItems(request);
		List<FileItem> s = uploadedItems;
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
			stat.put(TAG_PARAMS, params);
			stat.put(TAG_FINISHED, "ok");
		}
		return stat;
	}

	@Deprecated
	@Override
	protected final Map<String, String> getFileItemsSummary(HttpServletRequest request, Map<String, String> stat) {
		// EDIT - 現行のメソッドは廃止。final 化し、Override も禁止。
		throw new UnsupportedOperationException(
				"Method is unavailable. Please use #getFileItemsSummary(HttpServletRequest, Map<String, String>, List<FileItem>).");
	}

	private long getContentLength(HttpServletRequest request) {
		long size = -1;
		try {
			size = Long.parseLong(request.getHeader(FileUploadBase.CONTENT_LENGTH));
		} catch (NumberFormatException e) {
		}
		return size;
	}

	private String formFieldToXml(FileItem i) {
		Map<String, String> item = new HashMap<String, String>();
		// EDIT - 文字取得時に文字コードを設定。レスポンスに返すSummary情報を生成する際に、文字コードを指定（デフォルトではマルチバイト文字が化ける）。
		// item.put(TAG_VALUE, "" + i.getString());
		item.put(TAG_VALUE, "" + new String(i.get(), formFieldToXmlCharset));
		item.put(TAG_FIELD, "" + i.getFieldName());

		Map<String, String> param = new HashMap<String, String>();
		param.put(TAG_PARAM, statusToString(item));
		return statusToString(param);
	}

	private String fileFieldToXml(FileItem i) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(TAG_CTYPE, i.getContentType() != null ? i.getContentType() : "unknown");
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
}
