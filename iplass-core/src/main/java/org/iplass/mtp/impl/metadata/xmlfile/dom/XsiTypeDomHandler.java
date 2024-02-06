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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XsiTypeDomHandler implements DomHandler {

	/** 外部参照するファイルがある場合、ファイル名に付与するプレフィックス. */
	private static final String TMPL_PREFIX = "#{";
	/** 外部参照するファイルがある場合、ファイル名に付与するサフィックス. */
	private static final String TMPL_SUFFIX = "}";

	private File xml;
	private File fileRootDir;
	private File groovySourceRootDir;
	private String charset;
	private List<String> refFiles = new ArrayList<String>();
	private XsiType xsiType;

	public XsiTypeDomHandler(File xml, File fileRootDir, File groovySourceRootDir, String charset, XsiType xsiType) {
		this.xml = xml;
		this.fileRootDir = fileRootDir;
		this.groovySourceRootDir = groovySourceRootDir;
		this.charset = charset;
		this.xsiType = xsiType;
	}

	@Override
	public void putUnescapedExtFiles(Document doc) {
		for (ExternalRefPath targetPath : xsiType.getExternalRefPaths()) {
			toUnescapedExtFile(targetPath, doc);
		}
	}

	@Override
	public void restoreFromUnescapedExtFiles(Document doc) {
		for (ExternalRefPath targetPath : xsiType.getExternalRefPaths()) {
			readFromUnescapedExtFile(targetPath, doc);
		}
	}

	@Override
	public List<String> refFiles() {
		return refFiles;
	}

	private String getMidExtension(ExternalRefPath targetPath, Node node) {
		List<String> midExt = new ArrayList<String>();

		String groovyTemplateType = targetPath.getTemplateType(node);
		if (groovyTemplateType != null) {
			midExt.add(groovyTemplateType);
		}

		String localeName = targetPath.getLocaleName(node);
		if (localeName != null) {
			midExt.add(localeName);
		}

		return midExt.size() > 0 ? "." + String.join(".", midExt) : "";
	}

	/**
	 * タグの中味を外部参照ファイルに出力して、そのパスに変える.
	 * 
	 * @param tagetTagName
	 * @param doc
	 */
	private void toUnescapedExtFile(ExternalRefPath targetPath, Document doc) {
		List<Node> scriptTags = targetPath.getNodeList(doc);
		String tagetTagName = targetPath.getTagName();
		for (int i = 0; i < scriptTags.size(); i++) {
			Node childNode = scriptTags.get(i).getFirstChild();
			if (childNode != null) {
				// getNodeValueの戻り値はunescape済み. &amp;ltまでunescapeしてしまうのでそのまま取得.
				//String unescapedContent = StringUtil.unescapeXml(childNode.getNodeValue());
				String unescapedContent = childNode.getNodeValue();
				
				String lastExt = targetPath.getFileExtention(childNode);
				String fileExtension = getMidExtension(targetPath, childNode) + lastExt;

				String refName = tagetTagName + sequenceNumber(i, targetPath);
				String refFile = getRefFileName(refName, fileExtension, targetPath);
				String key = TMPL_PREFIX + refFile + TMPL_SUFFIX;

				childNode.setNodeValue(key);

				if (targetPath.isBase64Tag(childNode)) {
					writeBase64ContentToFile(unescapedContent,
							new File(resolveDir(targetPath, xml.getParentFile()), refFile));
				} else {
					writeContentToFile(unescapedContent,
							new File(resolveDir(targetPath, xml.getParentFile()), refFile));
				}
				refFiles.add(refFile);
			}
		}
	}

	/**
	 * 外部参照ファイルの内容をDOMに復帰させる.
	 * 
	 * @param tagetTagName
	 * @param doc
	 */
	private void readFromUnescapedExtFile(ExternalRefPath targetPath, Document doc) {
		List<Node> scriptTags = targetPath.getNodeList(doc);
		for (int i = 0; i < scriptTags.size(); i++) {
			Node childNode = scriptTags.get(i).getFirstChild();
			if (childNode != null) {
				String val = childNode.getNodeValue();
				if (val != null) {
					val = val.trim();
					if (val.startsWith(TMPL_PREFIX) && val.endsWith(TMPL_SUFFIX)) {
						val = val.replace(TMPL_PREFIX, "").replace(TMPL_SUFFIX, "");
						File extFile = new File(resolveDir(targetPath, xml.getParentFile()), val);

						String content = null;
						if (targetPath.isBase64Tag(childNode)) {
							content = readBase64FromBinFile(extFile);
						} else {
							content = readContentFromFile(extFile);
						}
						// エスケープもされる
						childNode.setNodeValue(content);
					}
				}
			}
		}
	}

	private void writeContentToFile(String content, File file) {
		try (FileOutputStream fo = new FileOutputStream(file);
				OutputStreamWriter ow = new OutputStreamWriter(fo, charset);
				PrintWriter out = new PrintWriter(ow)) {
			out.print(content);
		} catch (IOException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	private String readContentFromFile(File file) {
		String content = "";
		try {
			byte[] b = Files.readAllBytes(file.toPath());
			content = new String(b, charset);
		} catch (IOException e) {
			throw new MetaDataRuntimeException(e);
		}
		return content;
	}

	private void writeBase64ContentToFile(String base64, File file) {
		try(FileOutputStream out = new FileOutputStream(file)) {
			out.write(Base64.getDecoder().decode(base64));
			out.close();
		} catch (IOException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	private String readBase64FromBinFile(File file) {
		String base64 = null;
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			base64 = Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			throw new MetaDataRuntimeException(e);
		}
		return base64;
	}

	private String getRefFileName(String refName, String ext, ExternalRefPath path) {
		if(path.getClass().getAnnotation(ExternalRefPathAttribute.class).useGroovyDir()) {
			String fileName = xml.getName().substring(0, xml.getName().lastIndexOf(".xml")); 
			return fileName + ExternalRefPathAttribute.FileExtention.GROOVY.getExt();
		} else {
			return xml.getName() + "." + refName + ext;	
		}
	}

	private String sequenceNumber(int index, ExternalRefPath path) {
		return path.getSequenceNumber(index);
	}

	private File resolveDir(ExternalRefPath path, File parentDir) {
		if (groovySourceRootDir != null
				&& path.getClass().getAnnotation(ExternalRefPathAttribute.class).useGroovyDir()) {
			String fRoot = fileRootDir.getAbsolutePath().replace(File.separator, "/");
			String grvyRoot = groovySourceRootDir.getAbsolutePath().replace(File.separator, "/");
			String parent = parentDir.getAbsolutePath().replace(File.separator, "/");

			String scriptPath = parent.replace(fRoot, grvyRoot);
			String relativeFromRoot = scriptPath.replace(grvyRoot + "/", "");
			List<String> relativeDelimited = new ArrayList<String>(Arrays.asList(relativeFromRoot.split("/")));
			// メタデータ種別のフォルダを消す
			int dirToPackage = 1;
			for (int i = 0; i < dirToPackage; i++) {
				relativeDelimited.remove(0);	
			}
			relativeFromRoot = String.join("/", relativeDelimited);
			scriptPath = grvyRoot + "/" + relativeFromRoot;

			File dir = new File(scriptPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return parentDir;
		}
	}
}
