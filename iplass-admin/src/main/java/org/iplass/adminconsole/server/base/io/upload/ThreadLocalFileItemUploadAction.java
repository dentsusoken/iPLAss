package org.iplass.adminconsole.server.base.io.upload;

import static gwtupload.shared.UConsts.*;

import java.io.IOException;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import gwtupload.server.AbstractUploadListener;
import gwtupload.server.UploadAction;
import gwtupload.server.UploadServlet;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.exceptions.UploadCanceledException;
import gwtupload.server.exceptions.UploadException;
import gwtupload.server.exceptions.UploadSizeLimitException;
import gwtupload.server.exceptions.UploadTimeoutException;
import gwtupload.shared.UConsts;

public class ThreadLocalFileItemUploadAction extends UploadAction {
	// ===================================== members =====================================
	// ------------------------------------- from UploadAction -------------------------------------
	private boolean removeSessionFiles = false;
	private boolean removeData = false;

	// ===================================== methods =====================================
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

		perThreadRequest.set(request);
		try {
			// Receive the files and form elements, updating the progress status
			error = super.parsePostRequest(request, response);
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

	// ------------------------------------- from UploadServlet -------------------------------------
  /**
	 * Return the list of FileItems stored in session under the session key.
	 */
	// FIXME(manolo): Not sure about the convenience of this and sessionFilesKey.
	@Override
	public List<FileItem> getMySessionFileItems(HttpServletRequest request) {
		return getSessionFileItems(request, getSessionFilesKey(request));
  }
  
	/**
	 * Return the most recent list of FileItems received under the session key
	 */
	@Override
	public List<FileItem> getMyLastReceivedFileItems(HttpServletRequest request) {
		return getLastReceivedFileItems(request, getSessionLastFilesKey(request));
	}

	/**
	 * This method parses the submit action, puts in session a listener where the
	 * progress status is updated, and eventually stores the received data in
	 * the user session.
	 *
	 * returns null in the case of success or a string with the error
	 *
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
			if (listener.isFrozen() || listener.isCanceled() || listener.getPercent() >= 100) {
				removeCurrentListener(request);
			} else {
				String error = getMessage("busy");
				logger.error("UPLOAD-SERVLET (" + session.getId() + ") " + error);
				return error;
			}
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

			// Receive the files
			logger.error("UPLOAD-SERVLET (" + session.getId() + ") parsing HTTP POST request ");
			uploadedItems = uploader.parseRequest(request);
			session.removeAttribute(getSessionLastFilesKey(request));
			logger.error("UPLOAD-SERVLET (" + session.getId() + ") parsed request, " + uploadedItems.size() + " items received.");

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
			logger.error(
					"UPLOAD-SERVLET (" + request.getSession().getId() + ") Exception: " + e.getMessage() + "\n" + stackTraceToString(e));
			RuntimeException ex = new UploadActionException(getMessage("restricted", e.getMessage()), e);
			listener.setException(ex);
			throw ex;
		} catch (SizeLimitExceededException e) {
			RuntimeException ex = new UploadSizeLimitException(e.getPermittedSize(), e.getActualSize());
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

	/**
	 * Method executed each time the client asks the server for the progress status.
	 * It uses the listener to generate the adequate response
	 *
	 * @param request
	 * @param fieldname
	 * @return a map of tag/values to be rendered
	 */
	@Override
	protected Map<String, String> getUploadStatus(HttpServletRequest request, String fieldname, Map<String, String> ret) {
		perThreadRequest.set(request);
		HttpSession session = request.getSession();

		if (ret == null) {
			ret = new HashMap<String, String>();
		}

		long currentBytes = 0;
		long totalBytes = 0;
		long percent = 0;
		AbstractUploadListener listener = getCurrentListener(request);
		if (listener != null) {
			if (listener.isFinished()) {

			} else if (listener.getException() != null) {
				if (listener.getException() instanceof UploadCanceledException) {
					ret.put(TAG_CANCELED, "true");
					ret.put(TAG_FINISHED, TAG_CANCELED);
					logger.error("UPLOAD-SERVLET (" + session.getId() + ") getUploadStatus: " + fieldname + " canceled by the user after "
							+ listener.getBytesRead() + " Bytes");
				} else {
					String errorMsg = getMessage("server_error", listener.getException().getMessage());
					ret.put(TAG_ERROR, errorMsg);
					ret.put(TAG_FINISHED, TAG_ERROR);
					logger.error("UPLOAD-SERVLET (" + session.getId() + ") getUploadStatus: " + fieldname + " finished with error: "
							+ listener.getException().getMessage());
				}
			} else {
				currentBytes = listener.getBytesRead();
				totalBytes = listener.getContentLength();
				percent = totalBytes != 0 ? currentBytes * 100 / totalBytes : 0;
				// logger.debug("UPLOAD-SERVLET (" + session.getId() + ") getUploadStatus: " + fieldname + " " + currentBytes +
				// "/" + totalBytes + " " + percent + "%");
				ret.put(TAG_PERCENT, "" + percent);
				ret.put(TAG_CURRENT_BYTES, "" + currentBytes);
				ret.put(TAG_TOTAL_BYTES, "" + totalBytes);
			}
		} else if (getMySessionFileItems(request) != null) {
			if (fieldname == null) {
				ret.put(TAG_FINISHED, "ok");
				logger.debug("UPLOAD-SERVLET (" + session.getId() + ") getUploadStatus: " + request.getQueryString()
						+ " finished with files: " + session.getAttribute(getSessionFilesKey(request)));
			} else {
				List<FileItem> sessionFiles = getMySessionFileItems(request);
				for (FileItem file : sessionFiles) {
					if (file.isFormField() == false && file.getFieldName().equals(fieldname)) {
						ret.put(TAG_FINISHED, "ok");
						ret.put(UConsts.PARAM_FILENAME, fieldname);
						logger.debug("UPLOAD-SERVLET (" + session.getId() + ") getUploadStatus: " + fieldname + " finished with files: "
								+ session.getAttribute(getSessionFilesKey(request)));
					}
				}
			}
		} else {
			logger.debug("UPLOAD-SERVLET (" + session.getId() + ") getUploadStatus: no listener in session");
			ret.put("wait", "listener is null");
		}
		if (ret.containsKey(TAG_FINISHED)) {
			removeCurrentListener(request);
		}
		perThreadRequest.set(null);
		return ret;
	}

	private long getContentLength(HttpServletRequest request) {
		long size = -1;
		try {
			size = Long.parseLong(request.getHeader(FileUploadBase.CONTENT_LENGTH));
		} catch (NumberFormatException e) {
		}
		return size;
	}

}
