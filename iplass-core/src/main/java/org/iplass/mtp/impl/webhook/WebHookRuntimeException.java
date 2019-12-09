package org.iplass.mtp.impl.webhook;

import org.iplass.mtp.SystemException;

public class WebHookRuntimeException extends SystemException {
	
	private static final long serialVersionUID = 4677719044966530218L;

	public WebHookRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebHookRuntimeException(String message) {
		super(message);
	}

	public WebHookRuntimeException(Throwable cause) {
		super(cause);
	}

}
