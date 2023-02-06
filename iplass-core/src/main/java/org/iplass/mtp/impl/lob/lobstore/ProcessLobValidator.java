/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.lob.lobstore;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.lobstore.file.FileLobData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>外部プロセスを起動するLobValidatorの実装例。</p>
 *
 * <p>
 * 設定ファイルの設定例：binaryStoreのlobValidatorとしてこのLobValidatorを指定する場合
 * <pre>
 * &lt;service&gt;
 *   &lt;interfaceName&gt;org.iplass.mtp.impl.lob.LobStoreService&lt;/interfaceName&gt;
 *
 *   &lt;property name="binaryStore" className="org.iplass.mtp.impl.lob.lobstore.file.FileLobStore"&gt;
 *     &lt;property name="rootDir" value="/hogehoge/fileLobStore" /&gt;
 *     &lt;property name="lobValidator" className="org.iplass.mtp.dev.lob.ProcessLobValidator"&gt;
 *       &lt;property name="command" value="/hogehoge/fugafuga.sh" /&gt;
 *       &lt;property name="checksumAlgorithm" value="CRC-32" /&gt;
 *     &lt;/property&gt;
 *   &lt;/property&gt;
 * &lt;/service&gt;
 * </pre>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class ProcessLobValidator implements LobValidator {

	private static Logger logger = LoggerFactory.getLogger(ProcessLobValidator.class);
	private List<String> command;
	private String checksumAlgorithm;

	@Override
	public String getChecksumAlgorithm() {
		return checksumAlgorithm;
	}

	/**
	 * checksumのアルゴリズムを指定。<br>
	 * Adler-32/CRC-32/MD5/SHA-1/SHA-256<br>
	 * のいずれか指定。
	 *
	 */
	public void setChecksumAlgorithm(String checksumAlgorithm) {
		this.checksumAlgorithm = checksumAlgorithm;
	}

	public List<String> getCommand() {
		return command;
	}

	public void setCommand(List<String> command) {
		this.command = command;
	}

	@Override
	public void stored(Lob lob, String streamWriteChecksum) {
		//ユーザーのoid
		String user = ExecuteContext.getCurrentContext().getClientId();

		//FileLobStoreの場合にファイルパスを取得
		String filePath = null;
		if (lob.getLobData() instanceof FileLobData) {
			filePath = ((FileLobData) lob.getLobData()).getFilePath();
		} else {
			filePath = "null";
		}

		//外部プロセス起動

		ArrayList<String> cmd = new ArrayList<>();
		cmd.addAll(command);

		//[ファイルパス] [チェックサム] [ファイル名] [ユーザーOID]
		cmd.add(filePath);
		cmd.add(streamWriteChecksum);
		cmd.add(lob.getName());
		cmd.add(user);

		if (logger.isDebugEnabled()) {
			logger.debug("process start:" + toStr(cmd));
		}
		//投げっぱなし
		try {
//			Process process = new ProcessBuilder(cmd).redirectOutput(Redirect.INHERIT).redirectErrorStream(true).start();
			new ProcessBuilder(cmd).redirectOutput(Redirect.INHERIT).redirectErrorStream(true).start();
		} catch (IOException e) {
			logger.error("process start fail:" + e, e);
		}
	}

	private String toStr(ArrayList<String> command) {
		StringBuilder sb = new StringBuilder();
		for (String c: command) {
			sb.append(c);
			sb.append(" ");
		}
		return sb.toString();
	}

}
