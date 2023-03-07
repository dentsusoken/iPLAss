package org.iplass.mtp.impl.web;

import org.iplass.mtp.SystemException;

/**
 * リクエストパラメータ数上限超過例外
 *
 * @author SEKIGUCHI Naoya
 */
public class RequestParameterCountLimitExceededRuntimeException extends SystemException {
	/** serialVersionUID */
	private static final long serialVersionUID = -2142613930705878581L;

	/**
	 * コンストラクタ
	 */
	public RequestParameterCountLimitExceededRuntimeException() {
		super();
	}

	/**
	 * コンストラクタ
	 * @param message 例外メッセージ
	 * @param cause 発生原因例外
	 */
	public RequestParameterCountLimitExceededRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * コンストラクタ
	 * @param message 例外メッセージ
	 */
	public RequestParameterCountLimitExceededRuntimeException(String message) {
		super(message);
	}

	/**
	 * コンストラクタ
	 * @param cause 発生原因例外
	 */
	public RequestParameterCountLimitExceededRuntimeException(Throwable cause) {
		super(cause);
	}
}
