/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.annotation.MetaDataSeeAlso;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRepositoryKind;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationMetaDataStore implements MetaDataStore {

	private static Logger logger = LoggerFactory.getLogger(AnnotationMetaDataStore.class);


	@SuppressWarnings("rawtypes")
	private List<AnnotatableMetaDataFactory> annotatableMetaDataFactory;

	/* <path,RootMetaData> */
	private Map<String, AnnotateMetaDataEntry> pathMetaMap;


	@SuppressWarnings("rawtypes")
	public List<AnnotatableMetaDataFactory> getAnnotatableMetaDataFactory() {
		return annotatableMetaDataFactory;
	}

	@SuppressWarnings("rawtypes")
	public void setAnnotatableMetaDataFactory(
			List<AnnotatableMetaDataFactory> annotatableMetaDataFactory) {
		this.annotatableMetaDataFactory = annotatableMetaDataFactory;
	}

	@Override
	public MetaDataEntry loadById(int tenantId, String id) {
		//Annotationの場合、id＝pathとしているためidをPathとして検索
		return load(tenantId, id);
	}

	@Override
	public MetaDataEntry loadById(int tenantId, String id, int version) {
		return loadById(tenantId, id);
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath)
			throws MetaDataRuntimeException {
		String path = prefixPath;
		if (path != null) {
			if (!path.endsWith("/")) {
				path = path + "/";
			}
		}

		ArrayList<MetaDataEntryInfo> res = new ArrayList<MetaDataEntryInfo>();
		for (Map.Entry<String, AnnotateMetaDataEntry> e: pathMetaMap.entrySet()) {
			if (e.getKey().startsWith(path)) {
				MetaDataEntryInfo node = new MetaDataEntryInfo();
				node.setPath(e.getKey());
				node.setId(e.getValue().getMetaData().getId());
				node.setDisplayName(e.getValue().getMetaData().getDisplayName());
				node.setDescription(e.getValue().getMetaData().getDescription());
				node.setRepository(MetaDataRepositoryKind.ANNOTATION.getDisplayName());
				node.setState(State.VALID);
				node.setSharable(true);
				node.setDataSharable(false);
				node.setOverwritable(e.getValue().isOverwritable());
				node.setPermissionSharable(e.getValue().isPermissionSharable());
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
	@SuppressWarnings("rawtypes")
	public void inited(MetaDataRepository service, Config config) {
		pathMetaMap = new HashMap<String, AnnotateMetaDataEntry>();

		Map<Class<Annotation>, AnnotatableMetaDataFactory> factories = createFactories(annotatableMetaDataFactory);

		try {
			List<Class> checkedList = new ArrayList<Class>();
			List<String> annotatedClassNames = config.getValues("annotatedClass");
			if (annotatedClassNames != null) {
				for (String n: annotatedClassNames) {
					if (logger.isDebugEnabled()) {
						logger.debug("check " + n + " 's Annotation...");
					}

					Class c = Class.forName(n);
					checkAndCreateMetaData(c, checkedList, factories);
				}
			}
		} catch (ClassNotFoundException e) {
			throw new MetaDataRuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("created metaData from Annotation.");
			if (logger.isTraceEnabled()) {
				for (Map.Entry<String, AnnotateMetaDataEntry> e: pathMetaMap.entrySet()) {
					logger.trace(e.getKey() + ", id=" + e.getValue().getMetaData().getId());
				}
			}
		}

	}

	@Override
	public void destroyed() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkAndCreateMetaData(Class annotatedClass, List<Class> checkedList, Map<Class<Annotation>, AnnotatableMetaDataFactory> factories) {
		//すでにチェック済みのクラスは再検査しない
		if (checkedList.contains(annotatedClass)) {
			return;
		}
		checkedList.add(annotatedClass);

		Annotation[] annotations = annotatedClass.getAnnotations();
		if (annotations != null) {
			for (Annotation a: annotations) {
				if (a instanceof MetaDataSeeAlso) {
					Class[] seeAlso = ((MetaDataSeeAlso) a).value();
					if (seeAlso != null) {
						for (Class c: seeAlso) {
							checkAndCreateMetaData(c, checkedList, factories);
						}
					}
				} else {
					AnnotatableMetaDataFactory amdf = factories.get(a.annotationType());
					if (amdf != null) {
						if (amdf.getAnnotatedClass().isAssignableFrom(annotatedClass)) {
							Map<String, AnnotateMetaDataEntry> metaMap = amdf.toMetaData(annotatedClass);
							for (Map.Entry<String, AnnotateMetaDataEntry> e: metaMap.entrySet()) {
								String path = e.getKey();
								if (pathMetaMap.containsKey(path)) {
									if (logger.isDebugEnabled()) {
										logger.debug("already use the path:" + path + " metaData:" + pathMetaMap.get(path).getClass()
												+ " annotatedClass:" + annotatedClass);
									}
								}
								AnnotateMetaDataEntry metaData = e.getValue();
								metaData.getMetaData().setId(path);
//								if (logger.isDebugEnabled()) {
//								logger.debug("put(" + path + "," + metaData + ")");
//							}
								pathMetaMap.put(path, metaData);
							}
						}

					}
				}
			}
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<Class<Annotation>, AnnotatableMetaDataFactory> createFactories(List<AnnotatableMetaDataFactory> factoryList) {
			Map<Class<Annotation>, AnnotatableMetaDataFactory> factories = new HashMap<Class<Annotation>, AnnotatableMetaDataFactory>();
			for (AnnotatableMetaDataFactory factory: factoryList) {
				factories.put(factory.getAnnotationClass(), factory);
				if (logger.isDebugEnabled()) {
					logger.debug("regist AnnotatableMetaDataFactory:" + factory.getClass().getName() + " for " + factory.getAnnotationClass().getName());
				}
			}
			return factories;
	}

	@Override
	public MetaDataEntry load(int tenantId, String path)
			throws MetaDataRuntimeException {
		AnnotateMetaDataEntry meta = pathMetaMap.get(path);
		if (meta == null) {
			return null;
		}

		MetaDataEntry instance = new MetaDataEntry();
		instance.setMetaData(meta.getMetaData().copy());
		instance.setVersion(0);
		instance.setState(State.VALID);
		instance.setPath(path);
		instance.setSharable(true);
		instance.setDataSharable(false);
		instance.setOverwritable(meta.isOverwritable());
		instance.setPermissionSharable(meta.isPermissionSharable());
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
		// TODO 未使用
		return null;
	}
}
