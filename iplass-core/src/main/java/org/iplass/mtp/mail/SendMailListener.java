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
package org.iplass.mtp.mail;

/**
 * メール送信時のListenerのインタフェース。
 * 
 * @author K.Higuchi
 *
 */
public interface SendMailListener {
	
	/**
	 * メール送信前に呼び出されます。
	 * デフォルト実装では何もしません（return trueします）。
	 * 
	 * @param mail
	 * @return もし、当該メール送信後続処理を中止する場合はfalse
	 */
	public default boolean beforeSend(Mail mail) {
		return true;
	}
	
	/**
	 * メール送信成功時に呼び出されます。
	 * デフォルト実装では何もしません。
	 * 
	 * @param mail
	 */
	public default void onSuccess(Mail mail) {
	}
	
	/**
	 * メール送信失敗時（例外発生時）に呼び出されます。
	 * デフォルト実装では何もしません（return trueします）。
	 * @param mail
	 * @param e
	 * @return もし、例外を上位（アプリケーション側の処理、後続のSendMailListener）に通知せず例外処理終了する場合はfalse
	 */
	public default boolean onFailure(Mail mail, Exception e) {
		return true;
	}

}
