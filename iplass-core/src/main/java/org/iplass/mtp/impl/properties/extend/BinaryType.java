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

package org.iplass.mtp.impl.properties.extend;

import java.util.List;

import org.apache.commons.text.StringTokenizer;
import org.apache.commons.text.StringEscapeUtils;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.controlflow.Case;
import org.iplass.mtp.entity.query.value.expr.Polynomial;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.LobHandler;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.spi.ServiceRegistry;

public class BinaryType extends ComplexWrapperType {
	private static final long serialVersionUID = -3079789714739573857L;
	private static final int hash = 25;

	//TODO BinaryReferenceの定義毎に、LobStoreを変更できるように
	public static final String LOB_STORE_NAME = "binaryStore";

	private PropertyType actualType = new StringType();

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		return true;
	}

	@Override
	public boolean isCompatibleTo(PropertyType another) {
		return equals(another);
	}

	@Override
	public BinaryProperty createPropertyDefinitionInstance() {
		return new BinaryProperty();
	}

	@Override
	public Object createRuntime(
			MetaProperty metaProperty, MetaEntity metaEntity) {
		return null;
	}

	@Override
	public BinaryType copy() {
		return new BinaryType();
	}

	@Override
	public PropertyType actualType() {
		return actualType;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.BINARY;
	}
	
	@Override
	public PropertyDefinitionType getDataStoreEnumType() {
		return PropertyDefinitionType.STRING;
	}

	@Override
	public boolean isNeedPrevStoreTypeValueOnToStoreTypeValue() {
		return true;
	}
	
	@Override
	public Object toStoreTypeValue(Object extendTypeValue,
			Object prevStoreTypeValue, PropertyHandler ph, EntityHandler eh, String oid, Long version, Entity entity) {

		LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
		Binary prevBin = null;
		if (prevStoreTypeValue != null) {//更新
			//不要なLobの削除
			if (prevStoreTypeValue != null) {
				prevBin = new Binary((String) prevStoreTypeValue);
				if (extendTypeValue == null
						|| ((BinaryReference) extendTypeValue).getLobId() != prevBin.lobId) {
					lm.removeBinaryData(prevBin.lobId);
				}
			}
		}

		if (extendTypeValue == null) {
			return null;
		}

		BinaryReference newBinRef = (BinaryReference) extendTypeValue;
		//TODO サイズを取得するため、ここでLOB取得、、、処理をショートカットできないか、、、
		//TODO セキュリティのチェックとかすべき？
		Lob binData = lm.getBinaryData(newBinRef.getLobId());
		if (binData == null) {
			return null;
		}
		if (prevBin == null || prevBin.lobId != binData.getLobId()) {
			String sessionId = ServiceRegistry.getRegistry().getService(SessionService.class).getSession(true).getId();
			if (Lob.STATE_TEMP.equals(binData.getStatus())) {
				//テンポラリLOBを永続化
				if (!lm.markPersistenceBinaryData(binData.getLobId(), sessionId, eh.getMetaData().getId(), ph.getId(), oid, version)) {
					throw new EntityRuntimeException("new lob must user's temporary lob");
				}
			} else {
				//既存の永続LOBの場合、セキュリティ上OKならば、既存をコピーして保存
				if (lm.canAccess(binData)) {
					binData = lm.copyFor(binData.getLobId(), eh.getMetaData().getId(), ph.getId(), oid, version);
				} else {
					throw new EntityRuntimeException("cant reference lob cause security reason.lobId:" + binData.getLobId());
				}
			}
		}
		Binary bin = new Binary(binData.getLobId(), binData.getName(), binData.getType(), binData.getSize(), eh.getMetaData().getId(), ph.getId(), oid, version);
		return bin.toStringExpression();
	}

//	@Override
//	public void notifyAfterStore(Object storeTypeValue,
//			PropertyHandler ph, EntityHandler eh, String oid, Long version) {
//		if (storeTypeValue != null) {
//			LobManager lm = LobHandler.getInstance();
//			Binary bin = new Binary((String) storeTypeValue);
//			if (!lm.markPersistenceBinaryData(bin.lobId, ExecuteContext.getCurrentContext().getSessionId(), eh.getMetaData().getId(), ph.getId(), oid, version)) {
//				throw new EntityRuntimeException("new lob must user's temporary lob");
//			}
//		}
//	}

	@Override
	public void notifyAfterDelete(Object storeTypeValue, PropertyHandler ph,
			EntityHandler eh, String oid, Long rbid) {
		if (storeTypeValue != null) {
			Binary bin = new Binary((String) storeTypeValue);
			LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
			if (rbid == null) {
				lm.removeBinaryData(bin.lobId);
			} else {
				lm.markToRecycleBin(bin.lobId, rbid);
			}
		}
	}

	@Override
	public void notifyAfterPurge(EntityHandler eh, Long rbid) {
		LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
		lm.removeBinaryDataByRbid(rbid);
	}

	@Override
	public void notifyAfterRestore(EntityHandler eh, Long rbid) {
		LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
		lm.markRestoreFromRecycleBin(rbid);
	}

	@Override
	public Class<?> extendType() {
		return BinaryReference.class;
	}

	@Override
	public ComplexWrapperTypeLoadAdapter createLoadAdapter() {
		return new BinaryTypeLoadAdapter();
	}


	public static class Binary {
		private long lobId;
		private String name;
		private String type;
		private long size;

		private String entityDefinitionId;
		private String propertyId;
		private String oid;
		private Long version;

		public Binary() {
		}

		public Binary(long lobId, String name, String type, long size, String entityDefinitionId, String propertyId, String oid, Long version) {
			this.lobId = lobId;
			this.name = name;
			this.type = type;
			this.size = size;
			this.entityDefinitionId = entityDefinitionId;
			this.propertyId = propertyId;
			this.oid = oid;
			this.version = version;
			
			if (name != null && name.indexOf('\t') >= 0) {
				throw new IllegalArgumentException("Binary name can't contains tab");
			}
			if (type != null && type.indexOf('\t') >= 0) {
				throw new IllegalArgumentException("Binary type can't contains tab");
			}
		}

		public Binary(String stringExpression) {
			int tIndex = stringExpression.indexOf('\t');
			if (tIndex >= 0) {
				stringExpression = stringExpression.substring(0, tIndex);
			}
			
			StringTokenizer st = StringTokenizer.getCSVInstance(stringExpression);
			List<String> line = null;
			line = st.getTokenList();
			if (line != null) {
				for (String l: line) {
					if (l != null) {
						if (l.startsWith("lobId=")) {
							lobId = Long.parseLong(l.substring("lobId=".length()));
						} else if (l.startsWith("name=")) {
							name = l.substring("name=".length());
							if (name.length() == 0) {
								name = null;
							}
						} else if (l.startsWith("type=")) {
							type = l.substring("type=".length());
							if (type.length() == 0) {
								type = null;
							}
						} else if (l.startsWith("size=")) {
								size = Long.parseLong(l.substring("size=".length()));
						} else if (l.startsWith("entityDefinitionId=")) {
							entityDefinitionId = l.substring("entityDefinitionId=".length());
							if (entityDefinitionId.length() == 0) {
								entityDefinitionId = null;
							}
						} else if (l.startsWith("propertyId=")) {
							propertyId = l.substring("propertyId=".length());
							if (propertyId.length() == 0) {
								propertyId = null;
							}
						} else if (l.startsWith("oid=")) {
							oid = l.substring("oid=".length());
							if (oid.length() == 0) {
								oid = null;
							}
						} else if (l.startsWith("version=")) {
							String verStr = l.substring("version=".length());
							if (verStr.length() == 0) {
								version = null;
							} else {
								version = Long.parseLong(verStr);
							}
						}
					}
				}
			}
		}

		public long getLobId() {
			return lobId;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public long getSize() {
			return size;
		}

		public String getEntityDefinitionId() {
			return entityDefinitionId;
		}

		public String getPropertyId() {
			return propertyId;
		}

		public String getOid() {
			return oid;
		}

		public Long getVersion() {
			return version;
		}

		public String toStringExpression() {
			StringBuilder sb = new StringBuilder();
			sb.append(StringEscapeUtils.escapeCsv("lobId=" + lobId));
			sb.append(",");
			if (name == null) {
				sb.append("name=");
			} else {
				sb.append(StringEscapeUtils.escapeCsv("name=" + name));
			}
			sb.append(",");
			if (type == null) {
				sb.append("type=");
			} else {
				sb.append(StringEscapeUtils.escapeCsv("type=" + type));
			}
			sb.append(",");
			sb.append(StringEscapeUtils.escapeCsv("size=" + size));
			sb.append(",");
			if (entityDefinitionId == null) {
				sb.append("entityDefinitionId=");
			} else {
				sb.append(StringEscapeUtils.escapeCsv("entityDefinitionId=" + entityDefinitionId));
			}
			sb.append(",");
			if (propertyId == null) {
				sb.append("propertyId=");
			} else {
				sb.append(StringEscapeUtils.escapeCsv("propertyId=" + propertyId));
			}
			sb.append(",");
			if (oid == null) {
				sb.append("oid=");
			} else {
				sb.append(StringEscapeUtils.escapeCsv("oid=" + oid));
			}
			sb.append(",");
			if (version == null) {
				sb.append("version=");
			} else {
				sb.append(StringEscapeUtils.escapeCsv("version=" + version));
			}
			
			//検索用のnameを格納
			sb.append('\t');
			if (name != null) {
				sb.append(name);
			}

			return sb.toString();
		}

	}

	public class BinaryTypeLoadAdapter implements ComplexWrapperTypeLoadAdapter {

		protected LobHandler lobHandler;
		protected EntityContext context;

		@Override
		public void setContext(EntityContext context) {
			this.context = context;
			lobHandler = (LobHandler) LobHandler.getInstance(LOB_STORE_NAME);
		}

		@Override
		public void nextCalled(List<Object> values) {
		}

		@Override
		public Object toComplexWrapperTypeValue(Object value) {
			if (value == null) {
				return null;
			}
			Binary bin = new Binary((String) value);
			EntityHandler eh = context.getHandlerById(bin.entityDefinitionId);
			String entityName = null;
			String propertyName = null;
			if (eh != null) {
				entityName = eh.getMetaData().getName();
				PropertyHandler ph = eh.getPropertyById(bin.propertyId, context);
				if (ph != null) {
					propertyName = ph.getName();
				}
			}
			return new BinaryReference(bin.lobId, bin.name, bin.type, bin.size, entityName, propertyName, bin.oid);
		}

		@Override
		public void close() {
		}

		@Override
		public Object[] newComplexWrapperTypeArray(int size) {
			return new BinaryReference[size];
		}
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else {
			return Long.toString(((BinaryReference) value).getLobId());
		}
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		
		Binary bin = new Binary(strValue);
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler eh = context.getHandlerById(bin.entityDefinitionId);
		String entityName = null;
		String propertyName = null;
		if (eh != null) {
			entityName = eh.getMetaData().getName();
			PropertyHandler ph = eh.getPropertyById(bin.propertyId, context);
			if (ph != null) {
				propertyName = ph.getName();
			}
		}
		
		return new BinaryReference(bin.lobId, bin.name, bin.type, bin.size, entityName, propertyName, bin.oid);
	}

	@Override
	public ValueExpression translate(EntityField field) {
		//CASE WHEN INSTR(bin,'\t') > 0 THEN SUBSTR(bin, INSTR(bin,'\t') + 1) ELSE null END
		return new Case()
				.when(new Greater(new Function("INSTR", field, new Literal("\t")), new Literal(0L)),
						new Function("SUBSTR", field, new Polynomial(new Function("INSTR", field, new Literal("\t"))).add(new Literal(Long.valueOf(1)))))
				.elseClause(new Literal(null));
	}

}
