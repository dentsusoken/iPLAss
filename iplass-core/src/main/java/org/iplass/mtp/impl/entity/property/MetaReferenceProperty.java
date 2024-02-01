/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.property;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceType;
import org.iplass.mtp.entity.definition.properties.VersionControlReferenceType;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.util.ObjectUtil;


public class MetaReferenceProperty extends MetaProperty {
	private static final long serialVersionUID = -5316413329202006700L;

	/** 属性のデータ型が参照先の汎用データの定義名 */
	private String referenceEntityMetaDataId;
	/** 参照先の汎用データの定義側の参照定義の逆参照として定義する場合の、参照先側のプロパティのID */
	private String mappedByPropertyMetaDataId;

	private ReferenceType referenceType;

	private List<ReferenceSortSpec> orderBy;

	private VersionControlReferenceType versionControlType;
	private String versionControlAsOfExpression;

	private boolean auditLogMappedBy;
	
	public MetaReferenceProperty() {
		//参照なので、NON_INDEXED固定
		setIndexType(IndexType.NON_INDEXED);
	}
	
	@Override
	public IndexType getIndexType() {
		//参照なので、NON_INDEXED固定
		return IndexType.NON_INDEXED;
	}

	@Override
	public void setIndexType(IndexType indexType) {
		//参照なので、NON_INDEXED固定
	}

	public String getVersionControlAsOfExpression() {
		return versionControlAsOfExpression;
	}

	public void setVersionControlAsOfExpression(String versionControlAsOfExpression) {
		this.versionControlAsOfExpression = versionControlAsOfExpression;
	}

	public VersionControlReferenceType getVersionControlType() {
		return versionControlType;
	}

	public void setVersionControlType(VersionControlReferenceType versionControlType) {
		this.versionControlType = versionControlType;
	}

	public List<ReferenceSortSpec> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<ReferenceSortSpec> orderBy) {
		this.orderBy = orderBy;
	}

	public String getReferenceEntityMetaDataId() {
		return referenceEntityMetaDataId;
	}

	public void setReferenceEntityMetaDataId(String referenceEntityMetaDataId) {
		this.referenceEntityMetaDataId = referenceEntityMetaDataId;
	}

	public String getMappedByPropertyMetaDataId() {
		return mappedByPropertyMetaDataId;
	}

	public void setMappedByPropertyMetaDataId(String mappedByPropertyMetaDataId) {
		this.mappedByPropertyMetaDataId = mappedByPropertyMetaDataId;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public boolean isAuditLogMappedBy() {
		return auditLogMappedBy;
	}

	public void setAuditLogMappedBy(boolean auditLogMappedBy) {
		this.auditLogMappedBy = auditLogMappedBy;
	}

	@Override
	public void applyConfig(PropertyDefinition pDef, EntityContext ectx) {
		if (!(pDef instanceof ReferenceProperty)) {
			throw new EntityRuntimeException("Illegal Type Convert. ReferenceProperty to PrimitiveProperty cannot support.");
		}

		fillFrom(pDef, ectx);

		ReferenceProperty refDef = (ReferenceProperty) pDef;
		EntityHandler refEntity = ectx.getHandlerByName(refDef.getObjectDefinitionName());
		if (refEntity == null) {
			throw new EntityRuntimeException(refDef.getObjectDefinitionName() + " is undefined.");
		}
		referenceEntityMetaDataId = refEntity.getMetaData().getId();
		if (refDef.getMappedBy() != null) {
			MetaProperty rh = (MetaProperty) refEntity.getMetaData().getDeclaredProperty(refDef.getMappedBy());
			if (rh == null) {
				throw new EntityRuntimeException(refDef.getObjectDefinitionName() + "." + refDef.getMappedBy() + " is undefined.");
			}
			if (! (rh instanceof MetaReferenceProperty)) {
				throw new EntityRuntimeException(refDef.getObjectDefinitionName() + "." + refDef.getMappedBy() + " is not reference property.");
			}
			mappedByPropertyMetaDataId = rh.getId();
		} else {
			//被参照設定クリア時
			mappedByPropertyMetaDataId = null;
		}

		referenceType = refDef.getReferenceType();

		if (refDef.getOrderBy() != null) {
			OrderBy q;
			try {
				q = QueryServiceHolder.getInstance().getQueryParser().parse("order by " + refDef.getOrderBy(), OrderBySyntax.class);
			} catch (ParseException e) {
				throw new QueryException(e.getMessage(), e);
			}

			if (q.getSortSpecList() != null && q.getSortSpecList().size() > 0) {
				ArrayList<ReferenceSortSpec> sList = new ArrayList<ReferenceSortSpec>();
				for (SortSpec ss: q.getSortSpecList()) {
					if (!(ss.getSortKey() instanceof EntityField)) {
						throw new EntityRuntimeException("sort key must EntityField:" + ss.getSortKey());
					}
					String sortPropName = ((EntityField) ss.getSortKey()).getPropertyName();
					MetaProperty ph = refEntity.getMetaData().getDeclaredProperty(sortPropName);
					if (ph == null) {
						EntityHandler superEh = refEntity.getSuperDataModelHandler(ectx);
						ph = superEh.getMetaData().getDeclaredProperty(sortPropName);
					}
					if (ph == null) {
						throw new EntityRuntimeException("sort key " + ss.getSortKey() + " is not defined at " + refDef.getObjectDefinitionName());
					}
					if (ph instanceof MetaReferenceProperty) {
						throw new EntityRuntimeException("sort key must PrimitiveProperty:" + ss.getSortKey());
					}
					if (ss.getType() == SortType.DESC) {
						sList.add(new ReferenceSortSpec(ph.getId(), ReferenceSortSpec.SortType.DESC));
					} else {
						sList.add(new ReferenceSortSpec(ph.getId(), ReferenceSortSpec.SortType.ASC));
					}
				}
				orderBy = sList;
			}
		} else {
			//ソート条件設定クリア時
			orderBy = null;
		}

		versionControlType = refDef.getVersionControlType();
		versionControlAsOfExpression = refDef.getVersionControlAsOfExpression();
		auditLogMappedBy = refDef.isAuditLogMappedBy();

	}

	@Override
	public MetaReferenceProperty copy() {
		return ObjectUtil.deepCopy(this);

//		MetaReferenceProperty copy = new MetaReferenceProperty();
//		copyTo(copy);
//		copy.referenceEntityMetaDataId = referenceEntityMetaDataId;
//		copy.mappedByPropertyMetaDataId = mappedByPropertyMetaDataId;
//		copy.referenceType = referenceType;
//		return copy;
	}

	@Override
	public PropertyDefinition currentConfig(EntityContext ectx) {
		ReferenceProperty pd = new ReferenceProperty();
		fillTo(pd, ectx);
		pd.setVersionControlType(versionControlType);
		pd.setVersionControlAsOfExpression(versionControlAsOfExpression);

		EntityHandler refEntity = ectx.getHandlerById(referenceEntityMetaDataId);
		if (refEntity != null) {
			pd.setObjectDefinitionName(refEntity.getMetaData().getName());
			if (mappedByPropertyMetaDataId != null) {
				//ここでcheckStateでエラーになる可能性あり
				try {
					ReferencePropertyHandler rh = (ReferencePropertyHandler) refEntity.getDeclaredPropertyById(mappedByPropertyMetaDataId);
					if(rh == null) {
						return null;
					}
					pd.setMappedBy(rh.getName());
				} catch (MetaDataIllegalStateException e) {
					MetaEntity metaRefEntity = refEntity.getMetaData();
					MetaProperty metaProp = metaRefEntity.getDeclaredPropertyById(mappedByPropertyMetaDataId);
					if (metaProp != null) {
						pd.setMappedBy(metaProp.getName());
					}
				}
			}
			pd.setReferenceType(referenceType);

			if (orderBy != null && orderBy.size() > 0) {
				OrderBy q = new OrderBy();
				for (ReferenceSortSpec rss: orderBy) {
					//ここでcheckStateでエラーになる可能性あり
					try {
						PropertyHandler ph = refEntity.getPropertyById(rss.getSortPropertyMetaDataId(), ectx);
						if (ph != null && !(ph instanceof ReferencePropertyHandler)) {
							if (rss.getSortType() == ReferenceSortSpec.SortType.DESC) {
								q.add(new SortSpec(ph.getName(), SortType.DESC));
							} else {
								q.add(new SortSpec(ph.getName(), SortType.ASC));
							}
						}
					} catch (MetaDataIllegalStateException e) {
						MetaProperty ph = refEntity.getMetaData().getDeclaredPropertyById(rss.getSortPropertyMetaDataId());
						if (ph == null && refEntity.getMetaData().getInheritedEntityMetaDataId() != null) {
							//TODO 現状1段階の親まで
							EntityHandler superEh = refEntity.getSuperDataModelHandler(ectx);
							ph = superEh.getMetaData().getDeclaredPropertyById(rss.getSortPropertyMetaDataId());
						}
						if (ph != null && !(ph instanceof MetaReferenceProperty)) {
							if (rss.getSortType() == ReferenceSortSpec.SortType.DESC) {
								q.add(new SortSpec(ph.getName(), SortType.DESC));
							} else {
								q.add(new SortSpec(ph.getName(), SortType.ASC));
							}
						}
					}

				}
				if (q.getSortSpecList() != null && q.getSortSpecList().size() > 0) {
					pd.setOrderBy(q.toString().substring("order by".length()));//TODO リテラル埋め込まない。もう少しきれいな形にする
				}
			}

			pd.setAuditLogMappedBy(auditLogMappedBy);
			return pd;
		} else {
			return null;
		}
	}

	@Override
	public ReferencePropertyHandler createRuntime(MetaEntity metaEntity) {
		return new ReferencePropertyHandler(this, metaEntity);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (auditLogMappedBy ? 1231 : 1237);
		result = prime
				* result
				+ ((mappedByPropertyMetaDataId == null) ? 0
						: mappedByPropertyMetaDataId.hashCode());
		result = prime * result + ((orderBy == null) ? 0 : orderBy.hashCode());
		result = prime
				* result
				+ ((referenceEntityMetaDataId == null) ? 0
						: referenceEntityMetaDataId.hashCode());
		result = prime * result
				+ ((referenceType == null) ? 0 : referenceType.hashCode());
		result = prime
				* result
				+ ((versionControlAsOfExpression == null) ? 0
						: versionControlAsOfExpression.hashCode());
		result = prime
				* result
				+ ((versionControlType == null) ? 0 : versionControlType
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaReferenceProperty other = (MetaReferenceProperty) obj;
		if (auditLogMappedBy != other.auditLogMappedBy)
			return false;
		if (mappedByPropertyMetaDataId == null) {
			if (other.mappedByPropertyMetaDataId != null)
				return false;
		} else if (!mappedByPropertyMetaDataId
				.equals(other.mappedByPropertyMetaDataId))
			return false;
		if (orderBy == null) {
			if (other.orderBy != null)
				return false;
		} else if (!orderBy.equals(other.orderBy))
			return false;
		if (referenceEntityMetaDataId == null) {
			if (other.referenceEntityMetaDataId != null)
				return false;
		} else if (!referenceEntityMetaDataId
				.equals(other.referenceEntityMetaDataId))
			return false;
		if (referenceType != other.referenceType)
			return false;
		if (versionControlAsOfExpression == null) {
			if (other.versionControlAsOfExpression != null)
				return false;
		} else if (!versionControlAsOfExpression
				.equals(other.versionControlAsOfExpression))
			return false;
		if (versionControlType != other.versionControlType)
			return false;
		return true;
	}

}
