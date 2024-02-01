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
package org.iplass.mtp.impl.metadata.xmlfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.xmlfile.dom.DomHandler;
import org.iplass.mtp.impl.metadata.xmlfile.dom.DomHandlerFactory;
import org.iplass.mtp.impl.metadata.xmlfile.dom.XsiTypeDomHandlerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XMLファイル形式のメタデータの外部参照ファイルを扱うクラス.
 * <p>
 * 渡されたファイルのDOMをパースして、スクリプト内容等を外部ファイルに保存、元のタグ値を
 * 相対パス(#{orginal.xml.tagname000N.ext}の形式)に置換する.
 * あるいはその逆の操作として、相対パスで参照される内容をxmlに復帰させる.
 * </p>
 * @author T.Nishida
 *
 */
public class DomXmlExternalRefHandler implements XmlExternalRefHandler {
	private static final int XML_INDENT_NUM = 4;
	private String charset = "UTF-8";
	private DomHandlerFactory domHandlerFactory = new XsiTypeDomHandlerFactory();
	
	public DomHandlerFactory getDomHandlerFactory() {
		return domHandlerFactory;
	}

	/**
	 * xmlから特定タグの値を外部参照ファイルに出力しつつ、元の値をそのファイルの相対パスに置換.
	 * @return
	 */
	@Override
	public void putOutExtcontent(File xml) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
			doc.getDocumentElement().normalize();

			DomHandler handler = resolveDomHamdler(doc, xml);
			handler.putUnescapedExtFiles(doc);

			transform(doc, xml);
			deleteUnrefFiles(xml, handler.refFiles());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	/**
	 * 外部参照ファイルの内容をxmlに復元させつつ読み込む.
	 * @return
	 */
	@Override
	public byte[] readRestoringExtContent(File xml) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
			doc.getDocumentElement().normalize();

			DomHandler handler = resolveDomHamdler(doc, xml);
			handler.restoreFromUnescapedExtFiles(doc);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(out));
		} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
			throw new MetaDataRuntimeException(e);
		}
		return out.toByteArray();
	}

	/**
	 * xmlを外部参照したものに更新.
	 * @param document
	 * @param refNames
	 */
	private void transform(Document document, File xml) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", new Integer(XML_INDENT_NUM));
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			ByteArrayOutputStream bos = createDocType();
			transformer.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(bos, charset)));

			Files.write(xml.toPath(), bos.toByteArray());
		} catch (TransformerException | IOException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	/**
	 * DOCTYPEを入れる.
	 * @param refNames
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private ByteArrayOutputStream createDocType() throws UnsupportedEncodingException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, charset));
		writer.println("<?xml version=\"1.0\" encoding=\"" + charset + "\"" + " standalone=\"yes\"?>");
		writer.println("<!DOCTYPE metaDataEntry>");
		writer.flush();
		writer.close();
		return bos;
	}
	
	/**
	 * 外部参照されなくなったファイルを削除.
	 */
	private void deleteUnrefFiles(File xml, List<String> refFiles) {
		File dir = xml.getParentFile();
		for (String fileName : dir.list()) {
			if(fileName.startsWith(xml.getName() + ".")) {
				if(!refFiles.contains(fileName)) {
					File unReferencedFile = new File(dir, fileName);
					unReferencedFile.delete();
				}
			}
		}
	}
	
	/**
	 * メタデータのタイプに応じたDomHandlerを返す.
	 * @param type
	 * @return
	 */
	private DomHandler resolveDomHamdler(Document doc, File xml) {
		return domHandlerFactory.createDomHamdler(xml, charset, doc);	 		
	}

	@Override
	public void inited(MetaDataRepository service, XmlFileMetaDataStore xmlfileMetaDataStore) {
		domHandlerFactory.inited(service, xmlfileMetaDataStore);
	}
}
