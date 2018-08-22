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
package org.iplass.mtp.impl.metadata.xmlfile.dom.extend.template;

import org.iplass.mtp.impl.metadata.xmlfile.dom.AbstractExternalRefPath;
import org.iplass.mtp.impl.metadata.xmlfile.dom.ExternalRefPathAttribute;
import org.iplass.mtp.impl.metadata.xmlfile.dom.ExternalRefPathAttribute.FileExtention;
import org.w3c.dom.Node;

@ExternalRefPathAttribute(
		path = "/metaDataEntry/metaData/binary", 
		base64Tag = true)
public class ReportPath extends AbstractExternalRefPath {

	/**
	 * binaryタグと同じ階層にあるcontentTypeタグ等から外部参照ファイルの拡張子を決定.
	 */
	@Override
	public String getFileExtention(Node textNode) {
		Node metaDataNode = textNode.getParentNode().getParentNode();
		String contentType = getChildNodeValue(metaDataNode, "contentType");
		String fileName = getChildNodeValue(metaDataNode, "fileName");

		if (contentType != null) {
			if (contentType.equals("text/xml")) {
				//xmlだがメタデータとしてはbase64扱い
				return FileExtention.XML.getExt();
			} else {
				if (fileName != null) {
					return "." + fileName.replaceFirst(".*\\.", "");
				}
			}
		}

		return FileExtention.BIN.getExt();
	}
}