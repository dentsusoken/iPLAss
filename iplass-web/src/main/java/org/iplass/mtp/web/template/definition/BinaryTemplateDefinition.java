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

package org.iplass.mtp.web.template.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * <p>バイナリデータ（画像等）のTemplate定義。</p>
 *
 * <h5>※注意</h5>
 * <p>
 * バイナリデータは、メモリ内にロード＆キャッシュされるため、
 * 大きなサイズのバイナリデータは当Templateとして利用しないこと。
 * テナントのロゴ、CSS上で利用する簡単なデザイン要素に限る。
 * </p>
 * <p>
 * また、BinaryTemplateDefinitionは、layout機能の利用、別テンプレート内からのincludeはできない。
 * </p>
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class BinaryTemplateDefinition extends TemplateDefinition {

	private static final long serialVersionUID = -6397493149115396048L;

	private String fileName;

	private byte[] binary;

	/** 多言語設定情報 */
	private List<LocalizedBinaryDefinition> localizedBinaryList;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedBinaryDefinition> getLocalizedBinaryList() {
		return localizedBinaryList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedBinaryList(List<LocalizedBinaryDefinition> localizedBinaryList) {
		this.localizedBinaryList = localizedBinaryList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedBinary(LocalizedBinaryDefinition localizedBinary) {
		if (localizedBinaryList == null) {
			localizedBinaryList = new ArrayList<LocalizedBinaryDefinition>();
		}

		localizedBinaryList.add(localizedBinary);
	}

}
