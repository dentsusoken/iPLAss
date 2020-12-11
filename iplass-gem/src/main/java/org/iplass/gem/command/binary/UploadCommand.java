/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.XmlWriter;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * バイナリデータアップロード用コマンド
 * @author lis3wg
 */
@ActionMapping(
	name=UploadCommand.ACTION_NAME,
	displayName="アップロード",
	result=@Result(type=Type.STREAM, useContentDisposition=false)
)
@CommandClass(name="gem/binary/UploadCommand", displayName="アップロード")
public final class UploadCommand implements Command {
	private static Logger logger = LoggerFactory.getLogger(UploadCommand.class);

	public static final String ACTION_NAME = "gem/binary/upload";

	@Override
	public String execute(RequestContext request) {

		//自前でトークンチェック、なければスルー
		String token = request.getParam(TokenStore.TOKEN_PARAM_NAME);
		if (StringUtil.isNotBlank(token)) {
			TokenStore ts = TokenStore.getTokenStore(request.getSession());
			if (ts == null || !ts.isValid(token, false)) {
				request.setAttribute(Constants.CMD_RSLT_STREAM, new ResultXmlWriter(resourceString("command.binary.UploadCommand.failedMsg")));

				return Constants.CMD_EXEC_FAILURE;
			}
		}

		try {
			UploadFileHandle file = request.getParamAsFile("filePath");
			request.setAttribute("contentType", "application/xml");

			if (file != null && file.getSize() > 0) {
				BinaryReference br = file.toBinaryReference();
				ResultXmlWriter xml = new ResultXmlWriter(br.getName(), br.getType(), br.getLobId());
				request.setAttribute(Constants.CMD_RSLT_STREAM, xml);

				return Constants.CMD_EXEC_SUCCESS;
			}
		} catch (RuntimeException e) {
			Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
				t.setRollbackOnly();
			}

			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}
			if (e instanceof ApplicationException) {
				request.setAttribute(Constants.CMD_RSLT_STREAM, new ResultXmlWriter(e.getMessage()));
				return Constants.CMD_EXEC_FAILURE;
			} else {
				request.setAttribute(Constants.CMD_RSLT_STREAM, new ResultXmlWriter(resourceString("command.binary.UploadCommand.failedMsg")));
				return Constants.CMD_EXEC_FAILURE;
			}
		}

		// TODO 権限制御を入れた際に、ここの動作確認をすること
		request.setAttribute(Constants.CMD_RSLT_STREAM, new ResultXmlWriter(resourceString("command.binary.UploadCommand.failedMsg")));

		return Constants.CMD_EXEC_FAILURE;
	}

	/**
	 * アップロード結果
	 * @author lis3wg
	 */
	class ResultXmlWriter extends XmlWriter {
		/** ファイル名 */
		String _name;

		/** ファイルタイプ */
		String _type;

		/** LobID */
		Long _lobId;

		/** エラーメッセージ */
		String _error;

		/**
		 * コンストラクタ
		 * @param name ファイル名
		 * @param type ファイルタイプ
		 * @param lobId LobID
		 */
		public ResultXmlWriter(String name, String type, Long lobId) {
			_name = name;
			_type = type;
			_lobId = lobId;
		}

		/**
		 * コンストラクタ
		 * @param error エラーメッセージ
		 */
		public ResultXmlWriter(String error) {
			_error = error;
		}

		@Override
		public void write(OutputStream out) throws IOException {
			os = out;
			stackTags = new Stack<String>();
			addHeader();
			addElement("Result");
			addElementWithValue("name", StringUtil.escapeXml10(_name, true));
			addElementWithValue("type", StringUtil.escapeXml10(_type, true));
			addElementWithValue("lobId", _lobId != null ? _lobId.toString() : "");
			addElementWithValue("error", StringUtil.escapeXml10(_error, true));
			closeElement();
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
