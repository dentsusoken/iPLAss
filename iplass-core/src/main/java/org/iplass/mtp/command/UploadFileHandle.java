/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command;

import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;

import org.iplass.mtp.entity.BinaryReference;


/**
 * アップロードされたファイルを表すインタフェース。
 * 
 * @author K.Higuchi
 * @see RequestContext#getParamAsFile(String)
 * @see RequestContext#getParamsAsFile(String)
 *
 */
public interface UploadFileHandle {
	
	/**
	 * アップロードされたファイルをBinaryReferenceのインスタンスに変換。
	 * 変換されたBinaryReferenceはEntityの属性として保存されるまでは、
	 * テンポラリLOBとしてアップロードしたユーザーのみが参照可能な状態。
	 * 
	 * 
	 * @return
	 * @throws UploadFileSizeOverException アップロードされたファイルのファイルサイズが上限値を超えていた場合
	 */
	public BinaryReference toBinaryReference();
	
	/**
	 * アップロードされたファイルのバイナリを読み込むためのInputStreamを取得。
	 * 
	 * @return
	 * @throws UploadFileSizeOverException アップロードされたファイルのファイルサイズが上限値を超えていた場合
	 */
	public InputStream getInputStream();
	
	/**
	 * アップロードされたファイルを指定のtargetへコピーする。
	 * 
	 * @param target
	 * @param options
	 * @return コピー先のpath
	 */
	public Path copyTo(Path target, CopyOption... options);
	
	/**
	 * アップロードされたファイルを指定のtargetへ移動する。
	 * 
	 * @param target
	 * @param options
	 * @return 移動先のpath
	 */
	public Path moveTo(Path target, CopyOption... options);
	
	public String getType();
	
	public String getFileName();
	
	public long getSize();
	
	/**
	 * アップロードされたファイルのファイルサイズが上限値を超えていた場合true。
	 * 
	 * @return
	 */
	public boolean isSizeOver();
	
}
