/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.strategy.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.impl.datastore.grdb.ColumnPosition;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.RawColIndexType;
import org.iplass.mtp.impl.datastore.grdb.RawColType;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColConverter.NoneColConverter;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaRdbColumnMapping;
import org.iplass.mtp.impl.entity.MetaSchemalessRdbStoreMapping;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

public class ColResolver {
	
	private static ColConverterFactory ccFactory = ServiceRegistry.getRegistry().getService(ColConverterFactory.class);
	
	private StorageSpaceMap storage;
	
	private MetaGRdbEntityStore metaStore;
	private MetaSchemalessRdbStoreMapping newStoreMap;
	private ColContext colContext;
	
	private EnumMap<RawColType, EnumMap<RawColIndexType, List<ColumnPosition>>> unused = new EnumMap<>(RawColType.class);
	
	private HashSet<UsedCol> usedCol = new HashSet<>();
	
	private boolean storageSpaceMismatch;
	
	public ColResolver(MetaEntity currentMetaEntity, MetaSchemalessRdbStoreMapping newStoreMap, StorageSpaceMap storage, RdbAdapter rdb) {
		if (newStoreMap != null) {
			if (!storage.getStorageSpaceName().equals(newStoreMap.getStorageSpace())) {
				storageSpaceMismatch = true;
			}
		}
		
		if (currentMetaEntity == null) {
			this.metaStore = new MetaGRdbEntityStore();
		} else {
			metaStore = (MetaGRdbEntityStore) currentMetaEntity.getEntityStoreDefinition().copy();
			for (MetaProperty p: currentMetaEntity.getDeclaredPropertyList()) {
				if (p instanceof MetaPrimitiveProperty) {
					initUsed((MetaPrimitiveProperty) p);
				}
			}
			//check unused
			for (RawColType ct: RawColType.values()) {
				for (RawColIndexType cit: RawColIndexType.values()) {
					ColumnPosition currentMax = ct.getColumnPositionOf(metaStore, cit);
					if (currentMax != null) {
						for (int pageNo = currentMax.getPageNo(); pageNo >= 0; pageNo--) {
							for (int colNo = (pageNo == currentMax.getPageNo() ? currentMax.getColumnNo(): ct.getMaxCol(storage, cit)); colNo > 0; colNo--) {
								ColumnPosition cp = new ColumnPosition(pageNo, colNo);
								if (!usedCol.contains(new UsedCol(ct, cit, cp))) {
									EnumMap<RawColIndexType, List<ColumnPosition>> perType = unused.get(ct);
									if (perType == null) {
										perType = new EnumMap<>(RawColIndexType.class);
										unused.put(ct, perType);
									}
									List<ColumnPosition> perIndex = perType.get(cit);
									if (perIndex == null) {
										perIndex = new ArrayList<>();
										perType.put(cit, perIndex);
									}
									perIndex.add(cp);
								}
							}
						}
					}
				}
			}
		}
		
		this.newStoreMap = newStoreMap;
		
		this.storage = storage;
		this.colContext = new ColContext();
	}
	
	private void initUsed(MetaPrimitiveProperty p) {
		if (p.getType().isVirtual()) {
			return;
		}
		RawColType ct = RawColType.typeOf(p.getType());
		if (p.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
			for (int i = 0; i < p.getMultiplicity(); i++) {
				MetaGRdbPropertyStore col = ((MetaGRdbMultiplePropertyStore) p.getEntityStoreProperty()).getStore().get(i);
				addUsedCol(p, col, ct);
			}
		} else {
			MetaGRdbPropertyStore col = (MetaGRdbPropertyStore) p.getEntityStoreProperty();
			addUsedCol(p, col, ct);
			addUsedIndex(p, col, ct);
		}
	}
	
	private void addUsedCol(MetaPrimitiveProperty p, MetaGRdbPropertyStore col, RawColType ct) {
		if (col.getColumnName().startsWith(ct.getColNamePrefix())) {
			int no = -1;
			try {
				no = Integer.parseInt(col.getColumnName().substring(ct.getColNamePrefix().length()));
			} catch (NumberFormatException e) {
			}
			if (no != -1) {
				usedCol.add(new UsedCol(ct, RawColIndexType.NONE, new ColumnPosition(col.getPageNo(), no), col, p));
			}
		}
	}
	
	private void addUsedIndex(MetaPrimitiveProperty p, MetaGRdbPropertyStore col, RawColType ct) {
		RawColIndexType cit = RawColIndexType.typeOf(p.getIndexType());
		if (!col.isNative() && !col.isExternalIndex() && cit != RawColIndexType.NONE) {
			usedCol.add(new UsedCol(ct, cit, new ColumnPosition(col.getIndexPageNo(), col.getIndexColumnNo()), col, p));
		}
	}
	
	public MetaGRdbEntityStore getMetaStore() {
		return metaStore;
	}
	
	public ColContext getColContext() {
		return colContext;
	}

	private MetaRdbColumnMapping getMapping(MetaProperty prop) {
		//存在しないstorageStapce指定した場合は、カラムマップしない
		if (newStoreMap != null && !storageSpaceMismatch) {
			if (newStoreMap.getColumnMappingList() != null) {
				for (MetaRdbColumnMapping cm: newStoreMap.getColumnMappingList()) {
					if (prop.getId().equals(cm.getPropertyId())) {
						return cm;
					}
				}
			}
		}
		return null;
	}
	
	private MetaGRdbPropertyStore newCol(RawColType pt) {
		ColumnPosition current = pt.getColumnPositionOf(metaStore, RawColIndexType.NONE);
		if (current == null) {
			current = new ColumnPosition();
			pt.setColumnPositionOf(metaStore, RawColIndexType.NONE, current);
		}
		
		if (current.getColumnNo() >= pt.getMaxNormalCol(storage)) {
			current.setPageNo(current.getPageNo() + 1);
			current.setColumnNo(1);
		} else {
			current.setColumnNo(current.getColumnNo() + 1);
		}
		
		MetaGRdbPropertyStore col = new MetaGRdbPropertyStore(current.getPageNo(), pt.getColNamePrefix() + current.getColumnNo());
		return col;
	}
	
	private boolean isNative(MetaPrimitiveProperty prop) {
		if (prop.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
			return ((MetaGRdbMultiplePropertyStore) prop.getEntityStoreProperty()).getStore().get(0).isNative();
		} else if (prop.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
			return ((MetaGRdbPropertyStore) prop.getEntityStoreProperty()).isNative();
		} else {
			return false;
		}
	}
	
//	private boolean canUseSameCol(PropertyType to, PropertyType from) {
//		switch (to.getEnumType()) {
//		case DECIMAL:
//			if (from instanceof DecimalType) {
//				DecimalType toDec = (DecimalType) to;
//				DecimalType fromDec = (DecimalType) from;
//				return toDec.getScale() == fromDec.getScale();
//			}
//			return false;
//		case SELECT:
//			if (from instanceof SelectType) {
//				return true;
//			}
//			if (from instanceof BooleanType) {
//				return true;
//			}
//			return false;
//		case DATETIME:
//			if (from instanceof DateTimeType) {
//				return true;
//			}
//			if (from instanceof DateType) {
//				return true;
//			}
//			if (from instanceof TimeType) {
//				return true;
//			}
//			return false;
//		case AUTONUMBER:
//			if (from instanceof AutoNumberType) {
//				return true;
//			}
//			if (from instanceof StringType) {
//				return true;
//			}
//			return false;
//		case STRING:
//			switch (from.getEnumType()) {
//				case AUTONUMBER:
//				case BOOLEAN:
//				case SELECT:
//				case STRING:
//					return true;
//				default:
//					return false;
//			}
//		default:
//			return to.equals(from);
//		}
//	}
	
	public void allocateCol(MetaPrimitiveProperty prop, MetaPrimitiveProperty old, VersionControlType vcType) {
		if (prop.getType().isVirtual()) {
			return;
		}
		
		MetaRdbColumnMapping colMapped = getMapping(prop);
		RawColType newType = RawColType.typeOf(prop.getType());
		
		if (prop.getMultiplicity() == 1) {
			//MetaGRdbPropertyStore
			if (colMapped != null) {
				//native
				prop.setEntityStoreProperty(new MetaGRdbPropertyStore(0, colMapped.getColumnName(), true));

			} else {
				//generic
				ColConverter converter = (old == null || isNative(old)) ? null : ccFactory.getColConverter(old.getType(), prop.getType());
				MetaGRdbPropertyStore oldPs = null;
				if (old != null) {
					if (old.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
						oldPs = ((MetaGRdbMultiplePropertyStore) old.getEntityStoreProperty()).getStore().get(0);
					} else if (old.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
						oldPs = (MetaGRdbPropertyStore) old.getEntityStoreProperty();
					}
				}
				
				MetaGRdbPropertyStore newPs;
				if (converter != null && converter.canUseSameCol()) {
					//既存があり、そのまま使えるのであれば、それを引き継ぎ
					newPs = oldPs.copy();
				} else {
					//新規に作成
					newPs = newCol(newType);
					//過去データの変換
					if (converter != null && oldPs != null) {
						colContext.addConvert(converter, newPs, oldPs);
					}
				}
				prop.setEntityStoreProperty(newPs);
				
				//indexカラムの設定
				IndexType newIt = (prop.getIndexType() == null) ? IndexType.NON_INDEXED : prop.getIndexType();
				IndexType oldIt = (old == null || old.getMultiplicity() > 1 || old.getIndexType() == null) ? IndexType.NON_INDEXED : old.getIndexType();
				if (newIt == IndexType.NON_INDEXED) {
					//indexなし。index設定をクリア
					((MetaGRdbPropertyStore) prop.getEntityStoreProperty()).resetIndex();
				} else {
					//indexあり
					//(0)新規							：　newIndex割り当て
					//(1)更新
					//	(A)型同一
					//		(a)同一IndexTypeかつ外部フラグ	：　再利用
					//		(b)それ以外					：　newIndex割り当て、既存カラムから（変換）コピー
					//	(B)型互換あり
					//		(a)同一IndexTypeかつ外部フラグ	：　newIndex割り当て、既存カラムから（変換）コピー
					//		(b)それ以外					：　newIndex割り当て、既存カラムから（変換）コピー
					//	(C)型互換なし						：　newIndex割り当て
					
					if (old == null) {
						newIndex(prop.getType(), prop.getIndexType(), vcType, newPs, oldPs, null);
					} else {
						if (converter != null && converter.canUseSameCol()
								&& newIt == oldIt && newPs.isExternalIndex() == oldPs.isExternalIndex()) {
							//再利用
						} else if (converter != null) {
							//newIndex割り当て、既存カラムから（変換）コピー
							newIndex(prop.getType(), prop.getIndexType(), vcType, newPs, oldPs, converter);
						} else {
							//newIndex割り当て
							newIndex(prop.getType(), prop.getIndexType(), vcType, newPs, oldPs, null);
						}
					}
				}
			}
			
		} else {
			//MetaGRdbMultiplePropertyStore
			if (colMapped != null) {
				//native
				MetaGRdbMultiplePropertyStore ps = new MetaGRdbMultiplePropertyStore();
				ArrayList<MetaGRdbPropertyStore> cols = new ArrayList<>();
				for (int i = 0; i < prop.getMultiplicity(); i++) {
					cols.add(new MetaGRdbPropertyStore(0, colMapped.getColumnName() + "_" + i, true));
				}
				ps.setStore(cols);
				prop.setEntityStoreProperty(ps);
				
			} else {
				//generic
				ColConverter converter = (old == null || isNative(old)) ? null : ccFactory.getColConverter(old.getType(), prop.getType());
				
				//multiの場合、既存の情報を参照しつつ、追加もしくは削除or全く新規アロケートを判断
				ArrayList<MetaGRdbPropertyStore> cols = new ArrayList<>();
				int needSize = prop.getMultiplicity();
				if (converter != null && converter.canUseSameCol()) {
					if (old.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
						MetaGRdbPropertyStore copy = (MetaGRdbPropertyStore) old.getEntityStoreProperty().copy();
						copy.resetIndex();
						cols.add(copy);
						needSize--;
					} else if (old.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
						MetaGRdbMultiplePropertyStore mulStore = (MetaGRdbMultiplePropertyStore) old.getEntityStoreProperty();
						for (MetaGRdbPropertyStore s: mulStore.getStore()) {
							if (needSize <= 0) {
								break;
							}
							MetaGRdbPropertyStore copy = s.copy();
							copy.resetIndex();
							cols.add(copy);
							needSize--;
						}
					}
					//残りをアロケート
					for (int i = 0; i < needSize; i++) {
						cols.add(newCol(newType));
					}
					prop.setEntityStoreProperty(new MetaGRdbMultiplePropertyStore(cols));
				} else {
					
					//新規アロケート
					for (int i = 0; i < prop.getMultiplicity(); i++) {
						cols.add(newCol(newType));
					}
					
					MetaGRdbMultiplePropertyStore ret = new MetaGRdbMultiplePropertyStore(cols);
					if (converter != null) {
						List<MetaGRdbPropertyStore> oldStoreCols = null;
						if (old.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
							oldStoreCols = ((MetaGRdbMultiplePropertyStore) old.getEntityStoreProperty()).getStore();
						} else if (old.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
							oldStoreCols = Collections.singletonList((MetaGRdbPropertyStore) old.getEntityStoreProperty());
						}
						
						if (oldStoreCols != null) {
							int loops = Math.min(cols.size(), oldStoreCols.size());
							for (int i = 0; i < loops; i++) {
								colContext.addConvert(converter, cols.get(i), oldStoreCols.get(i));
							}
						}
					}
					prop.setEntityStoreProperty(ret);
					return;
				}
			}
		}
	}
	
	private boolean isVersionedUnique(IndexType iType, VersionControlType vcType) {
		return (iType == IndexType.UNIQUE || iType == IndexType.UNIQUE_WITHOUT_NULL)
				&& (vcType == VersionControlType.VERSIONED || vcType == VersionControlType.TIMEBASE
				|| vcType == VersionControlType.SIMPLE_TIMEBASE || vcType == VersionControlType.STATEBASE);
	}
	
//	public void allocateIndex(MetaPrimitiveProperty prop, VersionControlType vcType) {
//		if (prop.getType().isVirtual()) {
//			return;
//		}
//		
//		//multipleはindexしない
//		if (prop.getMultiplicity() > 1) {
//			return;
//		}
//		
//		if (prop.getIndexType() == null || prop.getIndexType() == IndexType.NON_INDEXED) {
//			return;
//		}
//		
//		MetaGRdbPropertyStore ps = (MetaGRdbPropertyStore) prop.getEntityStoreProperty();
//		if (ps.isNative()) {
//			return;
//		}
//		
//		if (isVersionedUnique(prop.getIndexType(), vcType)) {
//			//Uniqueかつ、バージョン管理は外部Index利用
//			ps.setExternalIndex(true);
//		} else {
//			//未アロケート（カラムが新規追加された）の場合
//			if (ps.getIndexPageNo() == -1) {
//				newIndex(prop.getType(), prop.getIndexType(), ps, vcType);
//			}
//		}
//		
//	}
	
	private void newIndex(PropertyType pt, IndexType indexType, VersionControlType vcType, MetaGRdbPropertyStore ps, MetaGRdbPropertyStore fromStore, ColConverter converter) {
		RawColType rawColType = RawColType.typeOf(pt);
		int maxCol = rawColType.getMaxCol(storage, indexType);
		boolean external = (maxCol == 0 || isVersionedUnique(indexType, vcType));

		ps.setExternalIndex(external);
		
		if (!external) {
			ColumnPosition current = rawColType.getColumnPositionOf(metaStore, indexType);
			if (current == null) {
				current = new ColumnPosition();
				rawColType.setColumnPositionOf(metaStore, indexType, current);
			}
			
			RawColIndexType rawColIndexType = RawColIndexType.typeOf(indexType);
			
			//check can re-use
			boolean reuse = false;
			ColumnPosition currentPosi = null;
			EnumMap<RawColIndexType, List<ColumnPosition>> perType = unused.get(rawColType);
			if (perType != null) {
				List<ColumnPosition> list = perType.get(rawColIndexType);
				if (list != null && list.size() > 0) {
					currentPosi = list.remove(list.size() - 1);
					reuse = true;
				}
			}

			if (reuse) {
				ps.setIndexPageNo(currentPosi.getPageNo());
				ps.setIndexColumnNo(currentPosi.getColumnNo());
			} else {
				//allocate new col
				if (current.getColumnNo() >= maxCol) {
					current.setPageNo(current.getPageNo() + 1);
					current.setColumnNo(1);
				} else {
					current.setColumnNo(current.getColumnNo() + 1);
				}

				ps.setIndexPageNo(current.getPageNo());
				ps.setIndexColumnNo(current.getColumnNo());
			}
			
			if (converter != null) {
				colContext.addIndexConvert(converter, indexType, ps, fromStore);
			} else if (reuse && fromStore == null) {
				//新規プロパティかつ、再利用の場合は、既存のデータが残っているので
				colContext.addIndexConvert(new NoneColConverter(pt, pt), indexType, ps, ps);
			}
		}
	}
	
	public void moveToUnusedCol(UsedCol col) {
		if (!col.getCol().isNative()) {
			EnumMap<RawColIndexType, List<ColumnPosition>> perType = unused.get(col.getRawColType());
			if (perType != null) {
				List<ColumnPosition> list = perType.get(col.getRawColIndexType());
				if (list != null && list.size() > 0) {
					ColumnPosition toMove = list.remove(list.size() - 1);
					//現在のColumnPositionより小さいカラムが再利用可能な場合、
					if (toMove.compareTo(col.getPosition()) < 0) {
						IndexType it;
						switch (col.getRawColIndexType()) {
						case NONE:
							it = null;
							break;
						case INDEX:
						case UNIQUE_INDEX:
							it = col.getProperty().getIndexType();
							break;
						default:
							it = null;
							break;
						}
						colContext.addMove(toMove, col.getPosition(), col.getRawColType(), it);
						colContext.addSetNull(col.getPosition(), col.getRawColType(), it);
						col.getPosition().setPageNo(toMove.getPageNo());
						col.getPosition().setColumnNo(toMove.getColumnNo());
						MetaGRdbPropertyStore store = col.getCol();
						switch (col.getRawColIndexType()) {
						case NONE:
							store.setPageNo(col.getPosition().getPageNo());
							store.setColumnName(col.getRawColType().getColNamePrefix() + col.getPosition().getColumnNo());
							break;
						case INDEX:
						case UNIQUE_INDEX:
							store.setIndexPageNo(col.getPosition().getPageNo());
							store.setIndexColumnNo(col.getPosition().getColumnNo());
						default:
							break;
						}
					} else {
						list.add(toMove);
					}
				}
			}
		}
	}
	
	
	public void shrink() {
		if (unused.size() > 0) {
			UsedCol[] orderedUsedCol = usedCol.toArray(new UsedCol[usedCol.size()]);
			Arrays.sort(orderedUsedCol, Comparator.naturalOrder());
			for (UsedCol uc: orderedUsedCol) {
				moveToUnusedCol(uc);
			}
			
			//未使用のカラムをnullに
			for (Map.Entry<RawColType, EnumMap<RawColIndexType, List<ColumnPosition>>> perType: unused.entrySet()) {
				for (Map.Entry<RawColIndexType, List<ColumnPosition>> list: perType.getValue().entrySet()) {
					if (list.getValue() != null) {
						for (ColumnPosition cp: list.getValue()) {
							IndexType it;
							switch (list.getKey()) {
							case NONE:
								it = null;
								break;
							case INDEX:
								it = IndexType.NON_UNIQUE;
								break;
							case UNIQUE_INDEX:
								it = IndexType.UNIQUE;
								break;
							default:
								it = null;
								break;
							}
							colContext.addSetNull(cp, perType.getKey(), it);
						}
					}
				}
			}
			
			//currentMaxを更新
			metaStore.clearColumnPosition();
			for (UsedCol col: usedCol) {
				ColumnPosition cp = col.getRawColType().getColumnPositionOf(metaStore, col.getRawColIndexType());
				if (cp == null) {
					cp = new ColumnPosition();
					col.getRawColType().setColumnPositionOf(metaStore, col.getRawColIndexType(), cp);
				}
				if (cp.compareTo(col.getPosition()) < 0) {
					cp.setPageNo(col.getPosition().getPageNo());
					cp.setColumnNo(col.getPosition().getColumnNo());
				}
			}
		}
	}
	
//	public void adjustPropertyDef(MetaProperty prop, MetaEntity entity) {
//		if (prop instanceof MetaReferenceProperty) {
//			//Referenceの場合は、IndexなしStoreProperyなし
//			prop.setIndexType(IndexType.NON_INDEXED);
//			prop.setEntityStoreProperty(null);
//		} else if (((MetaPrimitiveProperty) prop).getType().isVirtual()) {
//			//Virtualの場合は、IndexなしStoreProperyなし
//			prop.setIndexType(IndexType.NON_INDEXED);
//			prop.setEntityStoreProperty(null);
//		} else {
//			//nativeの場合は、Indexなし、物理カラム名をアロケート
//			//native出ない場合は、(その後の処理で)汎用カラムをアロケート
//			
//			MetaRdbColumnMapping matched = null;
//			MetaSchemalessRdbStoreMapping storeMap = (MetaSchemalessRdbStoreMapping) entity.getStoreMapping();
//			if (storeMap.getColumnMappingList() != null) {
//				for (MetaRdbColumnMapping cm: storeMap.getColumnMappingList()) {
//					if (prop.getId().equals(cm.getPropertyId())) {
//						matched = cm;
//						break;
//					}
//				}
//			}
//			
//			MetaGRdbPropertyStore ps = (MetaGRdbPropertyStore) prop.getEntityStoreProperty();
//			if (matched != null) {
//				prop.setIndexType(IndexType.NON_INDEXED);
//				ps.setNative(true);
//				if (prop.getMultiplicity() == 1) {
//					ps.setColumnName(matched.getColumnName());
//				} else {
//					String[] colNames = new String[prop.getMultiplicity()];
//					for (int i = 0; i < prop.getMultiplicity(); i++) {
//						colNames[i] = matched.getColumnName() + "_" + i;
//					}
//					ps.setColumnName(colNames);
//				}
//			} else {
//				ps.setNative(false);
//			}
//		}
//	}

}
