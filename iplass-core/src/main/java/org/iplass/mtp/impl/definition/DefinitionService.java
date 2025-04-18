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
package org.iplass.mtp.impl.definition;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * MetaDataをDefinitionとしてapiに公開するためのService。
 *
 * @author K.Higuchi
 *
 */
public class DefinitionService implements Service {

	public static DefinitionService getInstance() {
		return ServiceRegistry.getRegistry().getService(DefinitionService.class);
	}

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Definition>, DefinitionMetaDataTypeMap> defMap;
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends RootMetaData>, DefinitionMetaDataTypeMap> metaMap;
	@SuppressWarnings("rawtypes")
	private Map<String, DefinitionMetaDataTypeMap> typeNameMap;

	private MetaDataContextNode<Definition> contextNode;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init(Config config) {
		defMap = new HashMap<>();
		metaMap = new HashMap<>();
		typeNameMap = new HashMap<>();
		contextNode = new MetaDataContextNode("root");

		if (config.getNames() != null) {
			for (String typeMapClass : config.getValues("typeMap")) {
				try {
					DefinitionMetaDataTypeMap typeMap = (DefinitionMetaDataTypeMap) Class.forName(typeMapClass).newInstance();
					defMap.put(typeMap.defType, typeMap);
					metaMap.put(typeMap.metaType, typeMap);
					typeNameMap.put(typeMap.typeName(), typeMap);
					contextNode.addChild(typeMap.pathPrefix, typeMap);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					throw new ServiceConfigrationException("TypeMap:" + typeMapClass + " can't instanceate.");
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Definition> getClassbyTypeName(String typeName) {
		@SuppressWarnings("rawtypes")
		DefinitionMetaDataTypeMap m = typeNameMap.get(typeName);
		if (m != null) {
			return m.defType;
		} else {
			return null;
		}
	}

	/**
	 * MetaDataのパスからDefinitionの型、Definitionのプレフィックスパス以降の相対パスを取得。
	 * 
	 * @param path
	 * @return
	 */
	public DefinitionPath resolvePath(String path) {
		DefinitionMetaDataTypeMap<Definition, RootMetaData> typeMap = contextNode.getTypeMap(path);
		if (typeMap == null) {
			return null;
		}

		return new DefinitionPath(typeMap.defType, path.substring(typeMap.pathPrefix.length()));
	}

	@SuppressWarnings("rawtypes")
	private <D extends Definition> DefinitionMetaDataTypeMap getByDef(Class<D> defType) {
		DefinitionMetaDataTypeMap ret = defMap.get(defType);
		if (ret != null) {
			return ret;
		}
		for (Class<?> superType = defType.getSuperclass(); Definition.class.isAssignableFrom(superType); superType = superType.getSuperclass()) {
			ret = defMap.get(superType);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private <M extends RootMetaData> DefinitionMetaDataTypeMap getByMeta(Class<M> metaType) {
		DefinitionMetaDataTypeMap ret = metaMap.get(metaType);
		if (ret != null) {
			return ret;
		}
		for (Class<?> superType = metaType.getSuperclass(); RootMetaData.class.isAssignableFrom(superType); superType = superType.getSuperclass()) {
			ret = metaMap.get(superType);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <D extends Definition> TypedDefinitionManager<D> getTypedDefinitionManager(Class<D> defType) {
		return getByDef(defType).typedDefinitionManager();
	}

	public <D extends Definition> String getPrefixPath(Class<D> defType) {
		return getByDef(defType).pathPrefix;
	}

	@SuppressWarnings("rawtypes")
	public <D extends Definition> String getPath(Class<D> defType, String defName) {
		if (defName == null) {
			return null;
		}
		DefinitionMetaDataTypeMap td = getByDef(defType);
		return td.toPath(defName);
	}

	@SuppressWarnings("rawtypes")
	public <M extends RootMetaData> String getPathByMeta(Class<M> metaType, String defName) {
		if (defName == null) {
			return null;
		}
		DefinitionMetaDataTypeMap td = getByMeta(metaType);
		return td.toPath(defName);
	}

	@SuppressWarnings("rawtypes")
	public String getPath(String path) {
		// prefixを除去した定義名を変換
		DefinitionMetaDataTypeMap td = contextNode.getTypeMap(path);
		String defName = getDefinitionName(path);
		return td.toPath(defName);
	}

	@SuppressWarnings("unchecked")
	public <D extends Definition, M extends RootMetaData> Class<D> getDefinitionType(Class<M> metaType) {
		return (Class<D>) getByMeta(metaType).defType;
	}

	@SuppressWarnings("unchecked")
	public <D extends Definition, M extends RootMetaData> D toDefinition(M metaData) {
		return (D) getByMeta(metaData.getClass()).toDefinition(metaData);
	}

	@SuppressWarnings("rawtypes")
	public <D extends Definition> String getDefinitionName(Class<D> defType, String path) {
		//Definitionクラスが指定された場合は、そのprefixを消して返す
		DefinitionMetaDataTypeMap td = getByDef(defType);
		return td.toDefName(path);
	}

	public String getDefinitionName(String path) {
		//Definitionクラスが未指定の場合は、Pathの先頭からチェックして返す
		return contextNode.toName(path);
	}

	/**
	 * メタデータ定義名チェック
	 * 
	 * @param <M> メタデータの型
	 * @param metaType メタデータのクラス
	 * @param path パス
	 * @param defName 定義名
	 * @return チェックエラーメッセージ
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <M extends RootMetaData> Optional<String> validateDefinitionName(Class<M> metaType, String path, String defName) {
		if (Objects.isNull(metaType)) {
			return Optional.empty();
		}

		// メタデータ定義Map取得
		DefinitionMetaDataTypeMap typeMap = getByMeta(metaType);
		if (Objects.isNull(typeMap)) {
			return Optional.empty();

		}

		return typeMap.validateDefinitionName(path, defName);
	}

	private static class MetaDataContextNode<D extends Definition> {
		String name;
		DefinitionMetaDataTypeMap<Definition, RootMetaData> typeDef;
		LinkedHashMap<String, MetaDataContextNode<D>> children;

		private MetaDataContextNode(String name) {
			this.name = name;
			this.children = new LinkedHashMap<>();
		}

		private void addChild(String path, DefinitionMetaDataTypeMap<Definition, RootMetaData> typeDef) {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			String[] nodes = path.split("/");
			MetaDataContextNode<D> child = children.get(nodes[0]);
			if (child == null) {
				child = new MetaDataContextNode<D>(nodes[0]);
				children.put(nodes[0], child);
			}
			if (nodes.length > 1) {
				child.addChild(path.substring(nodes[0].length()), typeDef);
			} else {
				//最後なのでTypeDefを保持
				child.typeDef = typeDef;
			}
		}

		private DefinitionMetaDataTypeMap<Definition, RootMetaData> getTypeMap(String path) {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			int index = path.indexOf('/');
			if (index > 0) {
				MetaDataContextNode<D> child = children.get(path.substring(0, index));
				if (child != null) {
					return child.getTypeMap(path.substring(index));
				}
			}
			return typeDef;
		}

		public String toName(String path) {
			DefinitionMetaDataTypeMap<Definition, RootMetaData> typeMap = getTypeMap(path);
			if (typeMap != null) {
				return typeMap.toDefName(path);
			} else {
				//不明...
				return path;
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{name:" + name + ", children:[");
			if (children.size() > 0) {
				for (MetaDataContextNode<D> child : children.values()) {
					sb.append(child.toString() + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]}");
			return sb.toString();
		}
	}

}
