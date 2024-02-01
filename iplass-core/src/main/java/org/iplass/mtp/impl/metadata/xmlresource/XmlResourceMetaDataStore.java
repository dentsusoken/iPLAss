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

package org.iplass.mtp.impl.metadata.xmlresource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.iplass.mtp.impl.metadata.AbstractXmlMetaDataStore;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRepositoryKind;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlResourceMetaDataStore extends AbstractXmlMetaDataStore {

	private static Logger logger = LoggerFactory.getLogger(XmlResourceMetaDataStore.class);

	private List<String> resourcePathList;
	private List<String> filePathList;

	/* <path,RootMetaData> */
	private Map<String, XmlResourceMetaDataEntryThinWrapper> pathMetaMap;

	/* <id,path> */
	private Map<String, String> idPathMap;

	public XmlResourceMetaDataStore() {
	}

	public Map<String, XmlResourceMetaDataEntryThinWrapper> getPathMetaMap() {
		return pathMetaMap;
	}
	
	@Override
	public MetaDataEntry loadById(final int tenantId, final String id) {
		String path = idPathMap.get(id);
		if (path == null) {
			return null;
		}
		XmlResourceMetaDataEntryThinWrapper meta = pathMetaMap.get(path);

		MetaDataEntry instance = new MetaDataEntry();
		if (meta.getMetaData() != null) {
			instance.setMetaData(meta.getMetaData().copy());
		}
		instance.setVersion(0);
		instance.setState(State.VALID);
		instance.setPath(path);
		instance.setSharable(true);
		instance.setDataSharable(meta.isDataSharable());
		instance.setPermissionSharable(meta.isPermissionSharable());
		instance.setOverwritable(meta.isOverwritable());
		return instance;
	}

	@Override
	public MetaDataEntry loadById(final int tenantId, final String id, final int version) {
		return loadById(tenantId, id);
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withInvalid) throws MetaDataRuntimeException {

		String path = prefixPath;
		if (path != null) {
			if (!path.endsWith("/")) {
				path = path + "/";
			}
		}

		ArrayList<MetaDataEntryInfo> res = new ArrayList<MetaDataEntryInfo>();
		for (Map.Entry<String, XmlResourceMetaDataEntryThinWrapper> e: pathMetaMap.entrySet()) {
			if (e.getKey().startsWith(path)) {
				MetaDataEntryInfo node = new MetaDataEntryInfo();
				node.setPath(e.getKey());
				node.setId(e.getValue().getMetaData().getId());
				node.setDisplayName(e.getValue().getMetaData().getDisplayName());
				node.setDescription(e.getValue().getMetaData().getDescription());
				node.setSharable(true);
				node.setDataSharable(e.getValue().isDataSharable());
				node.setPermissionSharable(e.getValue().isPermissionSharable());
				node.setOverwritable(e.getValue().isOverwritable());
				node.setRepository(MetaDataRepositoryKind.XMLRESOURCE.getDisplayName());
				node.setState(State.VALID);
				res.add(node);
			}
		}

		//ソート
		Collections.sort(res, new Comparator<MetaDataEntryInfo>() {
			@Override
			public int compare(MetaDataEntryInfo o1, MetaDataEntryInfo o2) {
				return o1.getPath().toLowerCase().compareTo(o2.getPath().toLowerCase());
			}
		});
		return res;
	}

	@Override
	public void destroyed() {
		super.destroyed();
	}

	@Override
	public void inited(MetaDataRepository service, Config config) {
		super.inited(service, config);
		pathMetaMap = new HashMap<String, XmlResourceMetaDataEntryThinWrapper>();
		idPathMap = new HashMap<String, String>();

		Unmarshaller um;
		try {
			um = context.createUnmarshaller();
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		}

		resourcePathList = config.getValues("resourcePath");
		logger.debug("XmlResourceMetaDataRepository:load resource. " + resourcePathList);
		if (resourcePathList != null) {
			for (String path: resourcePathList) {
				InputStream is = getClass().getResourceAsStream(path);
				if (is != null) {
					parse(is, um, path);
				} else {
					throw new MetaDataRuntimeException("can not find resource file:" + path);
				}
			}
		}

		filePathList = config.getValues("filePath");
		logger.debug("XmlResourceMetaDataRepository:load file. " + filePathList);
		if (filePathList != null) {
			for (String path: filePathList) {
				InputStream is;
				try {
					is = new FileInputStream(path);
				} catch (FileNotFoundException e) {
					throw new MetaDataRuntimeException("can not find file:" + path, e);
				}
				parse(is, um, path);
			}
		}
	}

	private void parse(InputStream is, Unmarshaller um, String resoucePath) {
		try {
			MetaDataEntryList metaList = (MetaDataEntryList) um.unmarshal(is);
			if (metaList.getContextPath() != null) {
				for (ContextPath context: metaList.getContextPath()) {
					parseContextPath(context, "", context.getName(), resoucePath);
				}
			}
		} catch (JAXBException e) {
			throw new MetaDataRuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("can not close resource.maybe resource leak.", e);
				}
			}
		}
	}

	private void parseContextPath(ContextPath context, String prefixPath, String rootPath, String resoucePath) {
		if (context.getContextPath() != null) {
			for (ContextPath child: context.getContextPath()) {
				parseContextPath(child, prefixPath + context.getName() + "/", rootPath, resoucePath);
			}
		}

		if (context.getEntry() != null) {
			for (XmlResourceMetaDataEntryThinWrapper ent: context.getEntry()) {
				if (ent.getMetaData() == null) {
					logger.warn("Cannot unmarshal metadata from XmlResource. resourcePath:" + resoucePath + ", contextpath:" + rootPath);
				} else {
					//pathがMetaDataEntryのnameとして指定されている場合はそれを利用
					String path = ent.getName();
					if (path == null) {
						//指定されていない場合はMetaDataのnameから生成
						path = prefixPath + context.getName() + "/" + ent.getMetaData().getName();
					} else if (path.length() > 0 && path.charAt(0) != '/') {
						//相対指定の場合、contextPath + "/" + path
						path = prefixPath + context.getName() + "/" + path;
					}

					pathMetaMap.put(path, ent);
					if (ent.getMetaData().getId() == null) {
						ent.getMetaData().setId(path);
					}
					idPathMap.put(ent.getMetaData().getId(), path);
				}
			}
		}
	}

	@Override
	public MetaDataEntry load(int tenantId, String path) {
		XmlResourceMetaDataEntryThinWrapper meta = pathMetaMap.get(path);
		if (meta == null) {
			return null;
		}

		MetaDataEntry instance = new MetaDataEntry();
		if (meta.getMetaData() != null) {
			instance.setMetaData((RootMetaData) meta.getMetaData().copy());
		}
		instance.setVersion(0);
		instance.setState(State.VALID);
		instance.setPath(path);
		instance.setSharable(true);
		instance.setDataSharable(meta.isDataSharable());
		instance.setPermissionSharable(meta.isPermissionSharable());
		instance.setOverwritable(meta.isOverwritable());
		return instance;
	}

	@Override
	public MetaDataEntry load(int tenantId, String path, int version)
			throws MetaDataRuntimeException {
		return load(tenantId, path);
	}

	@Override
	public void store(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		throw new MetaDataRuntimeException(metaDataEntry.getPath() + "'s meta data is read only.");
	}

	@Override
	public void update(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		throw new MetaDataRuntimeException(metaDataEntry.getPath() + "'s meta data is read only.");
	}

	@Override
	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		throw new MetaDataRuntimeException(path + "'s meta data is read only.");
	}

	@Override
	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		throw new MetaDataRuntimeException(id + "'s meta data is read only.");
	}

	@Override
	public List<MetaDataEntryInfo> getHistoryById(int tenantId, String id) {
		//未使用
		return Collections.emptyList();
	}

	@Override
	public void purgeById(int tenantId, String id) throws MetaDataRuntimeException {
		//未使用
	}

	@Override
	public List<Integer> getTenantIdsOf(String id) {
		//未使用
		return Collections.emptyList();
	}
	
}
