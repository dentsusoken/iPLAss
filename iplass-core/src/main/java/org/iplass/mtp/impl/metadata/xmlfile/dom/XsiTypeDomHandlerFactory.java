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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.xmlfile.XmlFileMetaDataStore;
import org.iplass.mtp.impl.metadata.xmlfile.dom.extend.XsiTypeList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XsiTypeDomHandlerFactory implements DomHandlerFactory {
	
	private static final String TAGNAME_METADATA = "metaData";
	private List<XsiType> xsiType;
	private String fileStorePath;
	private String groovySourceStorePath;
	
	public String getFileStorePath() {
		return fileStorePath;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}

	public String getGroovySourceStorePath() {
		return groovySourceStorePath;
	}

	public void setGroovySourceStorePath(String groovySourceStorePath) {
		this.groovySourceStorePath = groovySourceStorePath;
	}
	
	@Override
	public DomHandler createDomHamdler(File xml, String charset, Document doc) {		
		String xtStr = getXsiTypeName(doc);
		String xtCapitalized = xtStr.substring(0, 1).toUpperCase() + xtStr.substring(1);

		XsiType xt = findXsiType(xtCapitalized);
		DomHandler handler = null;
		if(xt != null) {
			handler = new XsiTypeDomHandler(xml, new File(fileStorePath), new File(groovySourceStorePath), charset, xt);
		} else {
			handler = new NoopDomHandler();
		}

		return handler;	 		
	}
	
	private String getXsiTypeName(Document doc) {
		String typeName = null;
		NodeList metaDataTags = doc.getElementsByTagName(TAGNAME_METADATA);
		Node node = metaDataTags.item(0);
		if (node != null) {
			NamedNodeMap m = node.getAttributes();
			Node typeAtr = m.getNamedItem("xsi:type");
			
			if (typeAtr != null) {
				typeName = typeAtr.getNodeValue();
			}
		}
		return typeName;
	}
	
	private XsiType findXsiType(String xsiTypeCapitalized) {
		XsiType ret = null;
		for(XsiType type : xsiType) {
			if(type.getClass().getSimpleName().startsWith(xsiTypeCapitalized)) {
				ret = type;
				break;
			}
		}
		return ret;
	}

	@Override
	public void inited(MetaDataRepository service, XmlFileMetaDataStore xmlfileMetaDataStore) {
		
		fileStorePath = xmlfileMetaDataStore.getFileStorePath();
		groovySourceStorePath = xmlfileMetaDataStore.getGroovySourceStorePath();

		if (groovySourceStorePath == null) {
			groovySourceStorePath = fileStorePath;
		}
		
		if (!groovySourceStorePath.endsWith("/")) {
			groovySourceStorePath = groovySourceStorePath + "/";
		}
		
		xsiType = new ArrayList<XsiType>();
		for (Class<?> x : XsiTypeList.class.getAnnotation(XsiTypeClass.class).value()) {
			try {
				xsiType.add((XsiType) x.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new MetaDataRuntimeException("can not instantiate " + x.getName(), e);
			}
		}
	}
}