package org.iplass.mtp.webhook;

import org.iplass.mtp.SystemException;

public class WebHookException extends SystemException {

	private static final long serialVersionUID = 5631212745775599471L;

	public WebHookException() {
		super();
	}
	/**
	 * @param message
	 * @param cause
	 */
	public WebHookException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public WebHookException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WebHookException(Throwable cause) {
		super(cause);
	}
}
