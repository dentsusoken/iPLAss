/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.xmlfile.dom;

import java.util.List;
import org.w3c.dom.Document;

/**
 * DOMの中味に応じて外部参照ファイルを扱うインターフェース.
 */
public interface DomHandler {
	public void putUnescapedExtFiles(Document metaDataDoc);
	public void restoreFromUnescapedExtFiles(Document metaDataDoc);
	public List<String> refFiles();	
}
