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


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

public class VersioningXmlFileMetaDataStore extends AbstractXmlMetaDataStore {
	private static Logger logger = LoggerFactory.getLogger(VersioningXmlFileMetaDataStore.class);
	private static final String DEFAULT_VERSION_FORMAT = "000";
	private String versionFormat = DEFAULT_VERSION_FORMAT;
	private String fileStorePath;
	private String suffix = ".xml"; // <fileStorePath>/<path>/<basename>.<V|D>.<version>.xml

	
	public String getFileStorePath() {
		return fileStorePath;
	}
	
	public void setFileStorePath(String fileStorePath) {
		if (fileStorePath != null) {
			fileStorePath = fileStorePath.replaceAll("\\\\", "/");
			if (!fileStorePath.endsWith("/")) {
				fileStorePath = fileStorePath + "/";
			}
		}
		this.fileStorePath = fileStorePath;
	}

	public String getVersionFormat() {
		return versionFormat;
	}

	public void setVersionFormat(String versionFormat) {
		this.versionFormat = versionFormat;
	}

	/**
	 * 
	 */
	@Override
	public MetaDataEntry loadById(final int tenantId, final String id) {		
		return loadById(tenantId, id, -1);
	}

	/**
	 * 
	 */
	@Override
	public MetaDataEntry loadById(final int tenantId, final String id, int version) {
		String prefixpath = getFileStorePath() + tenantId;
		// ここは設定ファイルからなのでPath Manipulationされない
		MetaDataEntry e = searchFileById(tenantId, id.trim(), new File(prefixpath), version);
		if (e == null) {
			return null;
		}
		return e;
	}

	/**
	 * 
	 */
	@Override
	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withInvalid) throws MetaDataRuntimeException {
		boolean readHistorical = false;
		return definitionListImpl(tenantId, prefixPath, readHistorical, withInvalid);
	}

	private List<MetaDataEntryInfo> definitionListImpl(final int tenantId, final String prefixPath, boolean readHistorical, boolean withInvalid) throws MetaDataRuntimeException {
		String path = prefixPath;
		if (prefixPath != null) {
			if (!path.endsWith("/")) {
				path = path + "/";
			}
			if (path.contains("..")) {
				// fix Path Manipulation
				throw new MetaDataRuntimeException("invalid path:" + path);
			}
		}
		HashMap<String, MetaDataEntryInfo> res = new HashMap<>();
		File dir = new File(getFileStorePath() + "/" + tenantId + "/" + path);
		readMetaDataEntryInfoRecursive(res, tenantId, dir, readHistorical, withInvalid);
		return new ArrayList<MetaDataEntryInfo>(res.values());
	}
	
	/**
	 * 
	 */
	private void readMetaDataEntryInfoRecursive(HashMap<String, MetaDataEntryInfo> res, int tenantId, File f, boolean readHistorical, boolean withInvalid) {
		if(f.exists()) {
			if (f.isDirectory()) {
				String[] list = f.list();
				for (String l : list) {
					File subdirFile = new File(f, l);
					readMetaDataEntryInfoRecursive(res, tenantId, subdirFile, readHistorical, withInvalid);
				}
			} else {
				if (withInvalid) {
					MetaDataFileAttribute atr = new MetaDataFileAttribute(f.getName());
					MetaDataEntryInfo node = makeMetaDataEntryInfo(tenantId, f, atr);
					MetaDataEntryInfo preNode = res.get(node.getPath());
					if (preNode == null) {
						res.put(node.getPath(), node);
					} else {
						if (preNode.getVersion() < node.getVersion()) {
							res.put(node.getPath(), node);
						}
					}
				} else {
					MetaDataFileAttribute atr = new MetaDataFileAttribute(f.getName());
					if (readHistorical) {
						MetaDataEntryInfo node = makeMetaDataEntryInfo(tenantId, f, atr);
						res.put(node.getPath() + "." + node.getVersion(), node);
					} else {
						final int latestVersion = -1;
						if(isTargetVersion(atr, latestVersion)) {
							MetaDataEntryInfo node = makeMetaDataEntryInfo(tenantId, f, atr);
							res.put(node.getPath(), node);
						}
					}
				}
			}
		}
	}
	
	private MetaDataEntryInfo makeMetaDataEntryInfo(int tenantId, File file, MetaDataFileAttribute atr) {
		MetaDataEntry e = readMetaDataEntry(tenantId, file);
		
		MetaDataEntryInfo node = new MetaDataEntryInfo();
		node.setPath(e.getPath());
		node.setId(e.getMetaData().getId());
		node.setDisplayName(e.getMetaData().getDisplayName());
		node.setDescription(e.getMetaData().getDescription());
		node.setSharable(e.isSharable());
		node.setDataSharable(e.isDataSharable());
		node.setPermissionSharable(e.isPermissionSharable());
		node.setOverwritable(e.isOverwritable());
		node.setRepository(MetaDataRepositoryKind.XMLFILE.getDisplayName());
		if (atr.status == FileNameStatePart.DELETED) {
			node.setState(State.INVALID);
		} else {
			node.setState(State.VALID);
		}
		node.setVersion(atr.getVersionNum());
		return node;
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
			this.context = jaxbService.createJAXBContext(VersioningMetaDataEntryThinWrapper.class);
		}
	}

	/**
	 * 
	 */
	private MetaDataEntry readMetaDataEntry(int tenantId, File file) {
		MetaDataEntry instance = null;

		try {		
			instance = new MetaDataEntry();
			VersioningMetaDataEntryThinWrapper meta = unmarshal(file);
			instance.setMetaData(meta.getMetaData());
			if (!(meta.getVersion() == null)) {
				instance.setVersion(meta.getVersion());	
			}
			MetaDataFileAttribute atr = new MetaDataFileAttribute(file.getName()); 
			instance.setState(atr.getStatus().equals(FileNameStatePart.VALID) ? State.VALID : State.INVALID);
	
			String filePath = file.getPath();
			String pathDirpart = filePath.substring(getFileStorePath().length() + String.valueOf(tenantId).length(), filePath.lastIndexOf(atr.getBaseName()));
			String path = (pathDirpart + atr.getBaseName()).replaceAll("\\\\", "/"); 
			//path = path.substring(0, path.lastIndexOf(".")).replaceAll("\\\\", "/");
			//String path = file.getPath().substring(getFileStorePath().length() + String.valueOf(tenantId).length()) + atr.getBaseName();
			instance.setPath(path);
			instance.setSharable(meta.isSharable());
			instance.setDataSharable(meta.isDataSharable());
			instance.setPermissionSharable(meta.isPermissionSharable());
			instance.setOverwritable(meta.isOverwritable());
			
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}
		
		return instance;
	}
	
	/**
	 * 
	 */
	private MetaDataEntry searchFileById(int tenantId, String id, File dir, int version) {
		if (dir.isDirectory()) {
			String[] list = dir.list();
			for (String l: list) {
				File f = new File(dir, l);
				if (f.isDirectory()) {
					MetaDataEntry subDirMeta = searchFileById(tenantId, id, f, version);
					if (subDirMeta != null) {
						return subDirMeta;
					}
				} else {
					MetaDataFileAttribute atr = new MetaDataFileAttribute(l); 
					if(isTargetVersion(atr, version)) {
						MetaDataEntry e = readMetaDataEntry(tenantId, f);
						if (id.equals(e.getMetaData().getId())) {
							return e;
						}	
					}
						
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public MetaDataEntry load(int tenantId, String path) throws MetaDataRuntimeException {
		final int version = -1;
		return loadInternal(tenantId, path, version);
	}

	/**
	 * 
	 */
	@Override
	public MetaDataEntry load(int tenantId, String path, int version) throws MetaDataRuntimeException {
		return loadInternal(tenantId, path, version);
	}
	
	/**
	 * 
	 */
	public MetaDataEntry loadInternal(int tenantId, String path, int version) throws MetaDataRuntimeException {
		if (path.contains("..")) {
			// fix Path Manipulation
			throw new MetaDataRuntimeException("invalid path:" + path);
		}
		try {
			final String filePath = tenantId + path;
			final String dirPath = getFileStorePath() + filePath.substring(0, filePath.lastIndexOf("/"));
			final String baseName = path.substring(path.lastIndexOf("/") + 1);
			
			if (baseName.length() == 0) {
				return null;
			}

			final File dir = new File(dirPath);
			if (!dir.exists()) {
				return null;
			}

			String[] list = dir.list();
			for (String l: list) {
				File file = new File(dir, l);
				if (!file.isDirectory()) {				
					MetaDataFileAttribute atr = new MetaDataFileAttribute(l);
					if (atr.getBaseName().equals(baseName)) {
						if (isTargetVersion(atr, version)) {
							MetaDataEntry instance = new MetaDataEntry();
							VersioningMetaDataEntryThinWrapper meta = unmarshal(file);
							instance.setMetaData(meta.getMetaData());
							if(!(meta.getVersion() == null)) {
								instance.setVersion(meta.getVersion());	
							}
							instance.setState(atr.getStatus().equals(FileNameStatePart.VALID) ? State.VALID : State.INVALID);
							instance.setPath(path);
							return instance;	
						}
					}
				}
			}
			return null;

		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}
	}

	private int getStoreVersion(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		if(metaDataEntry == null) {
			return 0;			
		}
		
		final String filePath = "/" + tenantId + metaDataEntry.getPath();
		final String dirPath = getFileStorePath() + filePath.substring(0, filePath.lastIndexOf("/"));
		final String baseName = filePath.substring(filePath.lastIndexOf("/") + 1);
		final File dir = new File(dirPath);
		
		if (baseName.length() == 0 || !dir.exists()) {
			return 0;
		}

		int maxVersion = -1;
		String[] list = dir.list();
		for (String l : list) {
			MetaDataFileAttribute atr = new MetaDataFileAttribute(l);
			if (atr.getBaseName().equals(baseName)) {
				//if (atr.getStatus() == FileNameStatePart.VALID) {
				//	throw new MetaDataRuntimeException("Registered metadata already exists. path=" + metaDataEntry.getPath());
				//}
				if (atr.getVersionNum() > maxVersion) {
					maxVersion = atr.getVersionNum();
				}
			}
		}
		return maxVersion + 1;
	}

	/**
	 * 
	 */
	@Override
	public void store(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		int ver = getStoreVersion(tenantId, metaDataEntry);	
		storeImpl(tenantId, metaDataEntry, ver);
	}

//	/**
//	 * 
//	 */
//	@Override
//	public void store(int tenantId, MetaDataEntry metaDataEntry, int version)
//			throws MetaDataRuntimeException {
//		storeImpl(tenantId, metaDataEntry, version);
//	}

	/**
	 * 
	 */
	private void storeImpl(int tenantId, MetaDataEntry metaDataEntry, int version) {
		final String id = metaDataEntry.getMetaData().getId();
		final String path = metaDataEntry.getPath();
		int newVersion = 0;
		
		if (path.contains("..")) {
			// fix Path Manipulation
			throw new MetaDataRuntimeException("invalid path:" + path);
		}
		
		MetaDataEntryInfo existing = null;
		for (MetaDataEntryInfo ei : definitionList(tenantId, "/", true)) {
			if(ei.getId().equals(id)) {
				existing = ei;
			}
		}
		
		// 同一IDのものが既にあるなら無効にする
		final String dirPath = getFileStorePath() + "/" + tenantId;
		if (existing != null) {
			final String existingFilePath = dirPath  + "/"+ existing.getPath();
			if(existing.getState().equals(State.VALID)) {
				String versionStr = ".v" + new DecimalFormat(this.versionFormat).format(existing.getVersion());
				File valid = new File(existingFilePath + ".V" + versionStr + suffix);
				File inValid = new File(existingFilePath + ".D" + versionStr + suffix);
				valid.renameTo(inValid);			
				newVersion = existing.getVersion() + 1;
			}

			// 既存とパスが異なる場合、既存の同一DBのパスを変更
			if (!existing.getPath().equals(metaDataEntry.getPath())) {
				File existingVersionsDir = new File(existingFilePath.substring(0, existingFilePath.lastIndexOf("/"))); 
				String existingBaseName = existingFilePath.substring(existingFilePath.lastIndexOf("/") + 1);				
				for(File l : existingVersionsDir.listFiles()) {
					if(l.isFile()) {
						MetaDataFileAttribute atr = new MetaDataFileAttribute(l.getName());
						if(atr.getBaseName().equals(existingBaseName)) {
							// 移動
							String newPathDir = path.substring(0, path.lastIndexOf("/"));
							String newBaseName = path.substring(path.lastIndexOf("/") + 1);
							String metaDataNameAttribute = newPathDir.substring(newPathDir.indexOf("/")); 
							atr.setBaseName(newBaseName);
							String newFileName = atr.getFileName();
							File to = new File(dirPath + newPathDir + "/" +newFileName);
							File toDir = to.getParentFile(); 
							if(!toDir.exists()) {
								toDir.mkdirs();
							}
							l.renameTo(to);
							
							// name属性も更新
							MetaDataEntry existingMeta = readMetaDataEntry(tenantId, to);
							existingMeta.getMetaData().setName(metaDataNameAttribute);
							VersioningMetaDataEntryThinWrapper meta = new VersioningMetaDataEntryThinWrapper(existingMeta.getMetaData());
							marshal(meta, to);
						}
					}
				}
				if (existingVersionsDir.listFiles() != null && existingVersionsDir.listFiles().length == 0) {
					existingVersionsDir.delete();
				}
			}		
		}
		
		VersioningMetaDataEntryThinWrapper meta = new VersioningMetaDataEntryThinWrapper(metaDataEntry.getMetaData());
		meta.setVersion(newVersion);
		String newPathDir = path.substring(0, path.lastIndexOf("/"));
		String newBaseName = path.substring(path.lastIndexOf("/") + 1);
		String newVersionStr = ".v" + new DecimalFormat(this.versionFormat).format(newVersion);

		File xml = new File(dirPath + "/" + newPathDir + "/" + newBaseName + "." +FileNameStatePart.VALID.getValue() + newVersionStr + suffix);
		marshal(meta, xml);
	}

	/**
	 * 
	 */
	@Override
	public void update(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		storeImpl(tenantId, metaDataEntry, -1);
	}
	
	/**
	 * 
	 */
	@Override
	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		if (path.contains("..")) {
			// fix Path Manipulation
			throw new MetaDataRuntimeException("invalid path:" + path);
		}
		
		String baseName = path.substring(path.lastIndexOf("/") + 1);
		// 同じパスのファイル探して論理削除
		String filePath = "/" + tenantId + path;
		String dirPath = getFileStorePath() + filePath.substring(0, filePath.lastIndexOf("/"));
		for(File l : new File(dirPath).listFiles()) {
			if(l.isFile()) {
				MetaDataFileAttribute atr = new MetaDataFileAttribute(l.getName());
				if(atr.getBaseName().equals(baseName) && atr.status.equals(FileNameStatePart.VALID)) {
					atr.setStatus(FileNameStatePart.DELETED);
					String fileNameAsDeleted = atr.getFileName();
					File to = new File(dirPath + "/" + fileNameAsDeleted);
					l.renameTo(to);
					break;
				}
			}
		}
	}

	@Override
	public void purgeById(int tenantId, String id) throws MetaDataRuntimeException {
		for (MetaDataEntryInfo e : definitionListImpl(tenantId, "/", true, true)) {
			if(e.getId().equals(id)) {
				String ver = ".v" + new DecimalFormat(this.versionFormat).format(e.getVersion());
				String state = (e.getState() == State.VALID) ? FileNameStatePart.VALID.getValue() : FileNameStatePart.DELETED.getValue();
				File xml = new File(getFileStorePath() + tenantId + "/" + e.getPath() + "." + state	+ ver + suffix);
				xml.delete();
			}
		}
	}

	@Override
	public List<Integer> getTenantIdsOf(String id) {
		ArrayList<Integer> tids = new ArrayList<>();
		
		final File dir = new File(getFileStorePath());
		if (!dir.exists()) {
			return tids;
		}
		
		String[] list = dir.list();
		for (String l: list) {
			int tid = Integer.parseInt(l);
			if (loadById(tid, id) != null) {
				tids.add(tid);
			}
		}
		
		return tids;
	}

	/**
	 * 
	 */
	@Override
	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		MetaDataEntry e = loadById(tenantId, id, -1);

		VersioningMetaDataEntryThinWrapper meta = new VersioningMetaDataEntryThinWrapper(e.getMetaData());
		meta.setVersion(e.getVersion());
		meta.setSharable(config.isSharable());
		meta.setDataSharable(config.isDataSharable());
		meta.setPermissionSharable(config.isPermissionSharable());
		meta.setOverwritable(config.isOverwritable());

		String ver = ".v" + new DecimalFormat(this.versionFormat).format(e.getVersion());
		File xml = new File(getFileStorePath() + tenantId + "/" + e.getPath() + "." + FileNameStatePart.VALID.getValue()
				+ ver + suffix);
		marshal(meta, xml);
	}

	/**
	 * 
	 */
	@Override
	public List<MetaDataEntryInfo> getHistoryById(int tenantId, String id) {
		List<MetaDataEntryInfo> result = new ArrayList<MetaDataEntryInfo>();
		
		boolean readHistorical = true;
		for (MetaDataEntryInfo e : definitionListImpl(tenantId, "/", readHistorical, false)) {
			if(e.getId().equals(id)) {
				result.add(e);
			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	private void marshal(VersioningMetaDataEntryThinWrapper meta, File file) {
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
	}
	
	/**
	 * 
	 */
	private VersioningMetaDataEntryThinWrapper unmarshal(File f) throws JAXBException {
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (VersioningMetaDataEntryThinWrapper) unmarshaller.unmarshal(f);
	}
	
	private boolean isTargetVersion(MetaDataFileAttribute atr, int version) {
		return (version < 0 && atr.getStatus() == FileNameStatePart.VALID) || atr.getVersionNum() == version;
	}
	
	private class MetaDataFileAttribute {
		private FileNameStatePart status;
		private int versionNum;
		private String baseName;

		// ファイルが存在していて、ファイル名に全部含まれます！という前提で使ってね
		public MetaDataFileAttribute(String fileName) {
			String bn = fileName.substring(0, fileName.lastIndexOf("."));
			versionNum = Integer.valueOf(bn.substring(bn.lastIndexOf(".") + ".v".length()));
			
			bn = bn.substring(0, bn.lastIndexOf("."));
			status = FileNameStatePart.getStatus(bn.substring(bn.lastIndexOf(".") + ".".length()));
			
			baseName = bn.substring(0, bn.lastIndexOf("."));
		}
		
		
		public FileNameStatePart getStatus() {
			return status;
		}

		public void setStatus(FileNameStatePart status) {
			this.status = status;
		}

		public int getVersionNum() {
			return versionNum;
		}

		public void setVersionNum(int versionNum) {
			this.versionNum = versionNum;
		}

		public String getBaseName() {
			return baseName;
		}

		public void setBaseName(String baseName) {
			this.baseName = baseName;
		}

		public String getFileName() {
			return baseName + "." +status.getValue() + ".v" + getVersion() + suffix;
		}

		public String getVersion() {
			return new DecimalFormat(versionFormat).format(versionNum);
		}
	}
	
	private enum FileNameStatePart {
		VALID("V"), DELETED("D");
		private String value;

		private FileNameStatePart(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static FileNameStatePart getStatus(String value) {
			for (FileNameStatePart s : FileNameStatePart.values()) {
				if (value.equals(s.getValue())) {
					return s;
				}
			}
			return null;
		}
	}

}