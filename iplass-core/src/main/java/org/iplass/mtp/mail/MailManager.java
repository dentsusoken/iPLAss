/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Map;

import org.iplass.mtp.Manager;



/**
 * メールを送信する際利用するインタフェース。
 * 
 * @author K.Higuchi
 *
 */
public interface MailManager extends Manager {

	/**
	 * Mail情報を生成する。
	 * @return メール送信情報
	 */
	public Mail createMail();
	
	/**
	 * tmplDefNameで指定されるMailTemplateを利用して、Mailのを生成する。
	 * 
	 * 
	 * @param tmplDefName MailTemplate名
	 * @param bindings テンプレートにバインドする変数のMap
	 * @return テンプレート文面が反映されたMailのインスタンス
	 */
	public Mail createMail(String tmplDefName, Map<String, Object> bindings);

	/**
	 * テナント情報に設定されているサーバ情報を利用しメールを送信する。
	 * @param mail メール送信情報
	 */
	public void sendMail(Mail mail);
	
}
