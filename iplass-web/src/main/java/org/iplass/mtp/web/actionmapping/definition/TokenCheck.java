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

package org.iplass.mtp.web.actionmapping.definition;

import java.io.Serializable;

/**
 * トランザクショントークンのチェックの定義です。
 * 
 * @author K.Higuchi
 *
 */
public class TokenCheck implements Serializable {

	private static final long serialVersionUID = -6137919446875518790L;

	private boolean consume = true;
	private boolean exceptionRollback = true;
	private boolean useFixedToken = false;

	public TokenCheck() {
	}

	public boolean isUseFixedToken() {
		return useFixedToken;
	}

	/**
	 * トランザクショントークンをセッション毎に固定のものを利用するか否かを設定します。
	 * 固定トークンは、単純にCSRFトークンとしての役割のみをトランザクショントークンに持たす場合に利用可能です。
	 * 
	 * @param useFixedToken
	 */
	public void setUseFixedToken(boolean useFixedToken) {
		this.useFixedToken = useFixedToken;
	}

	public boolean isConsume() {
		return consume;
	}

	/**
	 * トランザクショントークンを当該のActionで消費しない場合、falseを設定します。
	 * デフォルト値はtrueです。
	 * 
	 * @param consume
	 */
	public void setConsume(boolean consume) {
		this.consume = consume;
	}

	public boolean isExceptionRollback() {
		return exceptionRollback;
	}

	/**
	 * 当該Actionで例外が発生した場合、トランザクショントークンの消費をロールバックするか否かを設定します。
	 * デフォルト値はtrueです。
	 * 
	 * @param exceptionRollback
	 */
	public void setExceptionRollback(boolean exceptionRollback) {
		this.exceptionRollback = exceptionRollback;
	}

}
