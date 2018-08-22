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
package org.iplass.mtp.impl.metadata.xmlfile.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractExternalRefPath implements ExternalRefPath {
	private static final int FILE_SEQ_DIGIT_DEFAULT = 3; 

	private int fileSequenceDigit = FILE_SEQ_DIGIT_DEFAULT;

	private String fileExt = null;
	
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public void setFileSequenceDigit(int fileSequenceDigit) {
		this.fileSequenceDigit = fileSequenceDigit;
	}

	@Override
	public final List<Node> getNodeList(Document doc) {
		Node node = doc.getDocumentElement();

		List<Node> result = new ArrayList<Node>();
		int idx = 0;
		traceNode(node, idx, result);

		return result;
	}

	@Override
	public final String getTagName() {
		return getPathLastElement();
	}

	@Override
	public String getFileExtention(Node node) {
		String ret = null;
		if(fileExt != null) {
			ret = fileExt;
			if(!ret.startsWith(".")) {
				ret = "." + ret;
			}
		} else {
			ExternalRefPathAttribute pathAttr = getClass().getAnnotation(ExternalRefPathAttribute.class);
			ret = pathAttr.fileExtension().getExt(); 
		}
		return ret;
	}

	@Override
	public String getTemplateType(Node node) {
		ExternalRefPathAttribute pathAttr = getClass().getAnnotation(ExternalRefPathAttribute.class);
		return pathAttr.templateType().getType();
	}
	
	@Override
	public boolean isBase64Tag(Node node) {
		ExternalRefPathAttribute pathAttr = getClass().getAnnotation(ExternalRefPathAttribute.class);
		return pathAttr.base64Tag();
	}
	
	@Override
	public String getLocaleName(Node node) {
		return null;
	}

	@Override
	public String getSequenceNumber(int num) {
		if (getClass().getAnnotation(ExternalRefPathAttribute.class).useFileSequence()) {
			String seqFormat = "%1$0" + fileSequenceDigit + "d";
			num++;
			return String.format(seqFormat, num);
		} else {
			return "";
		}
	}
	
	/**
	 * 指定した名前の子ノードの値を取得.
	 */
	protected final String getChildNodeValue(Node parent, String childNodeName) {
		NodeList nl = parent.getChildNodes();
		String value = null;
		for (int i = 0; i < nl.getLength(); i++) {
			Node childNode = nl.item(i);
			if (childNode != null && childNode.getNodeName().equals(childNodeName)) {
				value = childNode.getFirstChild().getNodeValue();
				break;
			}
		}
		return value;
	}
	
	private void traceNode(Node node, int idx, List<Node> result) {
		Node child = node.getFirstChild();
		idx++;

		String tagName = getPathList().get(idx);
		while (child != null) {
			if (child.getNodeName().equals(tagName)) {
				if (!getPathLastElement().equals(tagName)) {
					traceNode(child, idx, result);
				}

				if (child.getNodeName().equals(getPathLastElement())) {
					result.add(child);
				}
			}

			child = child.getNextSibling();
		}
	}

	private List<String> getPathList() {
		return Arrays.asList(getPath().split("/"));
	}

	private String getPathLastElement() {
		return getPath().replaceFirst(".*/", "");
	}
	
	private String getPath() {
		ExternalRefPathAttribute pathAttr = this.getClass().getAnnotation(ExternalRefPathAttribute.class);
		String path = pathAttr.path();
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		if(path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		return path;
	}
}
