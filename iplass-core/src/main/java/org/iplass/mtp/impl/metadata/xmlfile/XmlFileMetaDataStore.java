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

package org.iplass.mtp.impl.metadata.xmlfile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.iplass.mtp.impl.metadata.AbstractXmlMetaDataStore;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataJAXBService;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRepositoryKind;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFileMetaDataStore extends AbstractXmlMetaDataStore {

	private static Logger logger = LoggerFactory.getLogger(XmlFileMetaDataStore.class);

	private String rootPath;
	private String fileStorePath;
	private String groovySourceStorePath;
	private int localTenantId;
	private String suffix = ".xml";
	private XmlExternalRefHandler xmlExternalRefHandler = new DomXmlExternalRefHandler();

	public void setXmlExternalRefHandler(XmlExternalRefHandler xmlExternalRefHandler) {
		this.xmlExternalRefHandler = xmlExternalRefHandler;
	}

	public XmlExternalRefHandler getXmlExternalRefHandler() {
		return xmlExternalRefHandler;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getFileStorePath() {
		if(rootPath == null) {
			return fileStorePath;
		} else {
			return rootPath + fileStorePath;
		}
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

	public int getLocalTenantId() {
		return localTenantId;
	}

	public void setLocalTenantId(int localTenantId) {
		this.localTenantId = localTenantId;
	}

	public XmlFileMetaDataStore() {
	}

	/**
	 * <rootpath>/tenantId
	 */
	@Override
	public MetaDataEntry loadById(final int tenantId, final String id) {
		if(tenantId != localTenantId) {
			return null;
		}
		String prefixpath = getFileStorePath();
		// metaMetaCommandIdなどeclipseフォーマッタでブランクが入るためtrimして渡す
		// ここは設定ファイルからなのでPath Manipulationされない
		MetaDataEntry e = searchFileById(id.trim(), new File(prefixpath));
		if (e == null) {
			return null;
		}

		return e;
	}

	@Override
	public MetaDataEntry loadById(final int tenantId, final String id, int version) {
		return loadById(tenantId, id);
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withInvalid) throws MetaDataRuntimeException {
		if(tenantId != localTenantId) {
			return new ArrayList<MetaDataEntryInfo>();
		}
		
		String path = prefixPath;
		if (path != null) {
			if (!path.endsWith("/")) {
				path = path + "/";
			}
			if (path.contains("..")) {
				// fix Path Manipulation
				throw new MetaDataRuntimeException("invalid path:" + path);
			}
		}
		
		ArrayList<MetaDataEntryInfo> res = new ArrayList<MetaDataEntryInfo>();
		File dir = new File(getFileStorePath() + "/" + path);
		readMetaDataEntryInfoRecursive(res, prefixPath, dir);
		
		return res;
	}

	private void readMetaDataEntryInfoRecursive(ArrayList<MetaDataEntryInfo> res, String prefixPath, File f) {
		if(f.exists()) {
			if (f.isDirectory()) {
				String[] list = f.list();
				for (String l : list) {
					File subdirFile = new File(f, l);
					readMetaDataEntryInfoRecursive(res, prefixPath, subdirFile);
				}
			} else {
				if(isMetaDataXml(f)) {
					MetaDataEntry e = readMetaDataEntry(f);
	
					MetaDataEntryInfo node = new MetaDataEntryInfo();
					node.setPath(e.getPath());
					node.setId(e.getMetaData().getId());
					node.setDisplayName(e.getMetaData().getDisplayName());
					node.setDescription(e.getMetaData().getDescription());
					node.setSharable(false);
					node.setDataSharable(false);
					node.setPermissionSharable(false);
					node.setOverwritable(true);
					node.setRepository(MetaDataRepositoryKind.XMLFILE.getDisplayName());
					node.setState(State.VALID);
					res.add(node);
				}
			}
		}
	}
	
	@Override
	public void inited(MetaDataRepository service, Config config) {
		super.inited(service, config);
		
		if (fileStorePath != null) {
			fileStorePath = fileStorePath.replaceAll("\\\\", "/");
			if (!fileStorePath.endsWith("/")) {
				fileStorePath = fileStorePath + "/";
			}
			logger.debug("XmlFileRepository." + new File(fileStorePath).getAbsolutePath());
			MetaDataJAXBService jaxbService = config.getDependentService(MetaDataJAXBService.class);
			// XmlFileMetaDataEntryThinWrapperのルートタグはMetaDataEntryThinWrapperと同じ. 
			// この場合、MetaDataJAXBServiceのclassesToBeBoundの後ろの要素のクラスが勝つため、JAXBContextインスタンスは共有のものは使わず新規生成.
			this.context = jaxbService.createJAXBContext(XmlFileMetaDataEntryThinWrapper.class);
			
			xmlExternalRefHandler.inited(service, this);
		}
		if (groovySourceStorePath != null) {
			groovySourceStorePath = groovySourceStorePath.replaceAll("\\\\", "/");
			if (!groovySourceStorePath.endsWith("/")) {
				groovySourceStorePath = groovySourceStorePath + "/";
			}
		}
	}

	private MetaDataEntry readMetaDataEntry(File file) {
		MetaDataEntry instance = null;

		try {		
			instance = new MetaDataEntry();
			XmlFileMetaDataEntryThinWrapper meta = unmarshal(file);
			if (meta != null && meta.getMetaData() == null) {
				logger.warn("Cannot unmarshal metadata from XmlFile. filePath:" + file);
			}
			instance.setMetaData(meta.getMetaData());
			if(!(meta.getVersion() == null)){
				instance.setVersion(meta.getVersion());	
			}
			instance.setState(State.VALID);
			
			String storeRoot = getFileStorePath();
			if(storeRoot.endsWith("/")) {
				storeRoot = storeRoot.substring(0, storeRoot.length() - 1);
			}
			
			String filePath = file.getPath();
			String path = filePath.substring(storeRoot.length());
			path = path.substring(0, path.lastIndexOf(".")).replaceAll("\\\\", "/");
			instance.setPath(path);
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}
		
		return instance;
	}
	
	private MetaDataEntry searchFileById(String id, File dir) {
		if (dir.isDirectory()) {
			String[] list = dir.list();
			for (String l: list) {
				File f = new File(dir, l);
				if (f.isDirectory()) {
					MetaDataEntry subDirMeta = searchFileById(id, f);
					if (subDirMeta != null) {
						return subDirMeta;
					}
				} else {
					if(isMetaDataXml(f)) {
						MetaDataEntry e = readMetaDataEntry(f);
						if (id.equals(e.getMetaData().getId())) {
							return e;
						}	
					}
						
				}
			}
		}
		return null;
	}
	
	@Override
	public MetaDataEntry load(int tenantId, String path) {
		if (tenantId != localTenantId) {
			return null;
		}
		if (path.contains("..")) {
			// fix Path Manipulation
			throw new MetaDataRuntimeException("invalid path:" + path);
		}
		
		String filePath = "/" + path;
		try {
			String dirPath = getFileStorePath() + filePath.substring(0, filePath.lastIndexOf("/"));
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

			if (fileName.length() == 0) {
				return null;
			}

			File dir = new File(dirPath);
			if (!dir.exists()) {
				return null;
			}

			String[] list = dir.list();
			for (String l: list) {
				if (l.equals(fileName + suffix)) {
					File file = new File(dir, l);

					MetaDataEntry instance = new MetaDataEntry();
					XmlFileMetaDataEntryThinWrapper meta = unmarshal(file);
					if (meta != null && meta.getMetaData() == null) {
						logger.warn("Cannot unmarshal metadata from XmlFile. filePath:" + file);
					}
					instance.setMetaData(meta.getMetaData());
					if(!(meta.getVersion() == null)) {
						instance.setVersion(meta.getVersion());	
					}
					instance.setState(State.VALID);

					instance.setPath(path);
					return instance;
				}
			}
			return null;

		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	@Override
	public MetaDataEntry load(int tenantId, String path, int version)
			throws MetaDataRuntimeException {
		return load(tenantId, path);
	}

	@Override
	public void store(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		store(tenantId, metaDataEntry, 0);
	}

	//for export rdb to file
	public void store(int tenantId, MetaDataEntry metaDataEntry, int version)
			throws MetaDataRuntimeException {
		if (tenantId != localTenantId) {
			throw new MetaDataRuntimeException("can not save metadata on local file store except tenantId {" + localTenantId + "}");
		}
		
		String filePath = "/" + metaDataEntry.getPath();
		
		XmlFileMetaDataEntryThinWrapper meta = new XmlFileMetaDataEntryThinWrapper(metaDataEntry.getMetaData());
		meta.setVersion(version);
		File xml = new File(getFileStorePath() + filePath + suffix);

		marshal(meta, xml);
	}
	
	@Override
	public void update(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		if (tenantId != localTenantId) {
			throw new MetaDataRuntimeException("can not update metadata on local file store except tenantId {" + localTenantId + "}");
		}
		
		String filePath = "/" + metaDataEntry.getPath();

		File file = new File(getFileStorePath() + filePath + suffix);
		int version = 0;
		try {
			MetaDataEntry prevMeta = loadById(tenantId, metaDataEntry.getMetaData().getId());
			if (prevMeta != null) {
				File oldFile = new File(getFileStorePath() + "/" + prevMeta.getPath() + suffix);	
				XmlFileMetaDataEntryThinWrapper meta = unmarshal(oldFile);
				version = meta.getVersion() == null ? 0 : meta.getVersion() + 1;
				file.delete();
			}
			
			XmlFileMetaDataEntryThinWrapper meta = new XmlFileMetaDataEntryThinWrapper(metaDataEntry.getMetaData());
			meta.setVersion(version);
			marshal(meta, file);
			
			// リネームされてたら前のバージョンは削除
			if (prevMeta != null && !prevMeta.getPath().equals(metaDataEntry.getPath())) {
				File oldFile = new File(getFileStorePath() + "/" + prevMeta.getPath() + suffix);
				File dir = oldFile.getParentFile();
				for (String oldName : dir.list()) {
					if(oldName.startsWith(oldFile.getName())) {
						File unReferencedFile = new File(dir, oldName);
						unReferencedFile.delete();
					}
				}
			}
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}

	}

	private void marshal(XmlFileMetaDataEntryThinWrapper meta, File file) {
		File dir = file.getParentFile(); 
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(meta, file);
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}
		this.xmlExternalRefHandler.putOutExtcontent(file);
	}
	
	private XmlFileMetaDataEntryThinWrapper unmarshal(File f) throws JAXBException {
	    // 外部参照させている内容を復元
		byte[] xmlRestoredExtenalContent = this.xmlExternalRefHandler.readRestoringExtContent(f);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (XmlFileMetaDataEntryThinWrapper) unmarshaller
				.unmarshal(new ByteArrayInputStream(xmlRestoredExtenalContent));
	}
	
	@Override
	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		if (tenantId != localTenantId) {
			throw new MetaDataRuntimeException("can not remove metadata on local file store except tenantId {" + localTenantId + "}");
		}
		
		String filePath = "/" + path;
		String dirPath = getFileStorePath() + filePath.substring(0, filePath.lastIndexOf("/"));
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

		File dir = new File(dirPath);
		if (!dir.exists()) {
			return;
		}

		String[] list = dir.list();
		for (String l: list) {
			if (l.startsWith(fileName)) {
				File file = new File(dir, l);
				if(!file.delete()) {
					throw new MetaDataRuntimeException("指定のMetaDataを削除できませんでした。XMLファイル名=" + fileName);
				}
			}
		}
	}

	@Override
	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		throw new MetaDataRuntimeException(id + "'s meta data is tenant local and overwritable only.");
	}

	@Override
	public List<MetaDataEntryInfo> getHistoryById(int tenantId, String id) {
		return Collections.emptyList();
	}
	
	@Override
	public void purgeById(int tenantId, String id) throws MetaDataRuntimeException {
	}

	@Override
	public List<Integer> getTenantIdsOf(String id) {
		return Collections.emptyList();
	}

	/**
	 * 外部参照のxmlではないメータデータ本体のxmlかどうか.
	 * @param f
	 * @return
	 */
	private boolean isMetaDataXml(File f) {
		int idx = f.getName().lastIndexOf(".");
		if(idx != -1) {
			String base = f.getName().substring(0, idx);
			return !base.contains(suffix);
		} else {
			return false;
		}
	}
}
