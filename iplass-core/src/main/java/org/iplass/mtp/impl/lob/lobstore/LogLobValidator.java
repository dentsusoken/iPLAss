/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.lobstore.file.FileLobData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>単純にログに出力するLobValidatorの実装例。</p>
 * 
 * <p>
 * 設定ファイルの設定例：binaryStoreのlobValidatorとしてこのLogLobValidatorを指定する場合
 * <pre>
 * &lt;service&gt;
 *   &lt;interfaceName&gt;org.iplass.mtp.impl.lob.LobStoreService&lt;/interfaceName&gt;
 *   
 *   &lt;property name="binaryStore" className="org.iplass.mtp.impl.lob.lobstore.file.FileLobStore"&gt;
 *     &lt;property name="rootDir" value="D:\tmp\fileLobStore" /&gt;
 *     &lt;property name="lobValidator" className="org.iplass.mtp.dev.lob.LogLobValidator" /&gt;
 *   &lt;/property&gt;
 * &lt;/service&gt;
 * </pre>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class LogLobValidator implements LobValidator {
	
	private static Logger logger = LoggerFactory.getLogger("lob");
	

	@Override
	public void stored(Lob lob, String streamWriteChecksum) {
		//ユーザーのoid
		String user = ExecuteContext.getCurrentContext().getClientId();

		//FileLobStoreの場合にファイルパスを取得
		String filePath = null;
		if (lob.getLobData() instanceof FileLobData) {
			filePath = ((FileLobData) lob.getLobData()).getFilePath();
		}
		logger.info("user:" + user + ", fileName:" + lob.getName() + ", lobId:" + lob.getLobId() + ", path=" + filePath + ", checksum:" + streamWriteChecksum);
	}

	/**
	 * checksumのアルゴリズムを指定。<br>
	 * Adler-32/CRC-32/MD5/SHA-1/SHA-256<br>
	 * のいずれか指定。
	 * 
	 */
	@Override
	public String getChecksumAlgorithm() {
		return "MD5";
	}

}
