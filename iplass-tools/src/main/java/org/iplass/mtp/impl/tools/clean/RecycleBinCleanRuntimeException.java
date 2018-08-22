package org.iplass.mtp.impl.tools.clean;

import org.iplass.mtp.ApplicationException;

/**
 * ゴミ箱内のデータを削除する時に発生するException
 */
public class RecycleBinCleanRuntimeException extends ApplicationException {

	private static final long serialVersionUID = -6222869520155162566L;

	public RecycleBinCleanRuntimeException() {
		super();
	}

	public RecycleBinCleanRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecycleBinCleanRuntimeException(String message) {
		super(message);
	}

	public RecycleBinCleanRuntimeException(Throwable cause) {
		super(cause);
	}

}
