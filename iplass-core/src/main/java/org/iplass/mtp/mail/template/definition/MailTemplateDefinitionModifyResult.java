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

package org.iplass.mtp.mail.template.definition;

import org.iplass.mtp.definition.DefinitionModifyResult;

/**
 * メールテンプレート更新結果
 */
public class MailTemplateDefinitionModifyResult extends DefinitionModifyResult {

	private static final long serialVersionUID = 661394500728982196L;

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 */
	public MailTemplateDefinitionModifyResult(boolean isSuccess) {
		this(isSuccess, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 * @param message メッセージ
	 */
	public MailTemplateDefinitionModifyResult(boolean isSuccess, String message) {
		super(isSuccess, message);
	}
}
