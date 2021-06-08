/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyService;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.LobHandler;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.spi.ServiceRegistry;


public class LongTextType extends ComplexWrapperType {
	private static final long serialVersionUID = -4263828374127798455L;
	private static final int hash = 26;
	private static final int META_PART_LENGTH = 21;

	public static final String LOB_STORE_NAME = "longTextStore";
	public static final String LOB_NAME = "_LongText";

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
	public LongTextProperty createPropertyDefinitionInstance() {
		return new LongTextProperty();
	}

	@Override
	public Object createRuntime(
			MetaProperty metaProperty, MetaEntity metaEntity) {
		return null;
	}


	@Override
	public LongTextType copy() {
		return new LongTextType();
	}

	@Override
	public PropertyType actualType() {
		return actualType;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.LONGTEXT;
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
	public Object toStoreTypeValue(Object extendTypeValue, Object prevStoreTypeValue, PropertyHandler ph, EntityHandler eh, String oid, Long version, Entity entity) {
		try {
			//既存のLobの削除（Lobで保存されていたら）
			if (prevStoreTypeValue != null) {
				LongText longText = new LongText((String) prevStoreTypeValue);
				if (longText.lobId != -1) {
					LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
					lm.removeBinaryData(longText.lobId);
				}
			}

			//値の保存
			if (extendTypeValue != null && ((String) extendTypeValue).length() != 0) {
				LongText newVal = new LongText(-1, (String) extendTypeValue);
				int inlineStoreMaxLength = inlineStoreMaxLength(eh);
				
				if (!newVal.isInlineStore(inlineStoreMaxLength)) {
					LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
					Lob bin = lm.createBinaryData(LOB_NAME, "text/plain", eh.getMetaData().getId(), ph.getId(), oid, version);
					bin.setByte(((String) extendTypeValue).getBytes("utf-8"));
					newVal.lobId = bin.getLobId();
					newVal.trimOrClearText(inlineStoreMaxLength);
				}
				return newVal.toStringExpression();
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public void notifyAfterDelete(Object storeTypeValue, PropertyHandler ph,
			EntityHandler eh, String oid, Long rbid) {
		if (storeTypeValue != null) {
			LongText longText = new LongText((String) storeTypeValue);
			//Lobとして保存されている場合
			if (longText.lobId != -1) {
				LobHandler lm = LobHandler.getInstance(LOB_STORE_NAME);
				if (rbid == null) {
					lm.removeBinaryData(longText.lobId);
				} else {
					lm.markToRecycleBin(longText.lobId, rbid);
				}
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
		return String.class;
	}

	@Override
	public ComplexWrapperTypeLoadAdapter createLoadAdapter() {
		return new LongTextTypeLoadAdapter();
	}

	private int inlineStoreMaxLength(EntityHandler eh) {
		int dataStoreStringMaxLength = LongText.storeService.getDataStore().stringPropertyStoreMaxLength(eh.getMetaData().getStoreMapping());
		if (dataStoreStringMaxLength < 0) {
			return LongText.service.getLongTextInlineStoreMaxLength();
		} else {
			return Math.min(LongText.service.getLongTextInlineStoreMaxLength(), dataStoreStringMaxLength - META_PART_LENGTH);
		}
	}
	

	public static class LongText {

		private static final char[] SPACE = {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};

		private static PropertyService service = ServiceRegistry.getRegistry().getService(PropertyService.class);
		private static StoreService storeService = ServiceRegistry.getRegistry().getService(StoreService.class);

		//LongTextの格納パターン
		//L=             xxx,T=hogehogeパターン。xxxは固定16桁スペース埋め。検索可能にするため。

		private long lobId;
		private String text;

		public static void appendLongTextFromStringExpression(StringBuilder sb) {
			sb.append("L=").append(SPACE, 0, 14).append("-1");
			sb.append(",T=");
		}

		private LongText() {
		}

		private LongText(long lobId, String text) {
			this.lobId = lobId;
			this.text = text;
		}

		private LongText(String stringExpression) {
			if (stringExpression.startsWith("L=")) {
				lobId = Long.parseLong(stringExpression.substring(2, 18).trim());
				if (stringExpression.length() > META_PART_LENGTH) {
					text = stringExpression.substring(META_PART_LENGTH);
				}
			} else {
				lobId = -1;
			}
		}

		private String toStringExpression() {
			StringBuilder sb = new StringBuilder();
			String lobIdStr = Long.toString(lobId);
			sb.append("L=").append(SPACE, 0, 16 - lobIdStr.length()).append(lobIdStr);
			sb.append(",T=");
			if (text != null) {
				sb.append(text);
			}
			return sb.toString();
		}

		private boolean isInlineStore(int inlineStoreMaxLength) {
			if (text == null) {
				return true;
			}
			if (text.length() <= inlineStoreMaxLength) {
				return true;
			}
			return false;
		}

		private void trimOrClearText(int inlineStoreMaxLength) {
			if (service.isRemainInlineText()) {
				if (text != null && text.length() > inlineStoreMaxLength) {
					text = text.substring(0, inlineStoreMaxLength);
				}
			} else {
				text = null;
			}
		}

	}

	private class LongTextTypeLoadAdapter implements ComplexWrapperTypeLoadAdapter {

		private Map<Long, Lob> currentRowValue;
		private LobHandler lobHandler;
		private EntityContext context;

		@Override
		public void setContext(EntityContext context) {
			this.context = context;
			lobHandler = (LobHandler) LobHandler.getInstance(LOB_STORE_NAME);
		}

		@Override
		public void nextCalled(List<Object> values) {

			//list lob stored value
			ArrayList<Long> lobedValues = new ArrayList<Long>();
			for (Object v: values) {
				LongText lt = new LongText((String) v);
				if (lt.lobId != -1) {
					lobedValues.add(lt.lobId);
				}
			}

			if (lobedValues.size() > 0) {
				long[] lobIdList = new long[lobedValues.size()];
				for (int i = 0; i < lobedValues.size(); i++) {
					lobIdList[i] = lobedValues.get(i).longValue();
				}
				Lob[] res = lobHandler.getBinaryReference(lobIdList, context);
				if (res != null) {
					currentRowValue = new HashMap<Long, Lob>();
					for (Lob bin: res) {
						currentRowValue.put(bin.getLobId(), bin);
					}
				}
			}
		}

		@Override
		public void close() {
		}

		@Override
		public Object toComplexWrapperTypeValue(Object value) {
			if (value == null) {
				return null;
			}
			LongText lt = new LongText((String) value);
			if (lt.lobId == -1) {
				return lt.text;
			}

			if (currentRowValue == null) {
				return null;
			}
			Lob bin = currentRowValue.get(lt.lobId);
			if (bin == null) {
				return null;
			}
			
			try {
				return new String(bin.getByte(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				throw new EntityRuntimeException(e);
			}
		}

		@Override
		public Object[] newComplexWrapperTypeArray(int size) {
			return new String[size];
		}
	}

	@Override
	public String toString(Object value) {
		return (String) value;
	}

	@Override
	public Object fromString(String strValue) {
		return strValue;
	}

	@Override
	public ValueExpression translate(EntityField field) {
		if (LongText.service.isRemainInlineText()) {
			return new Function("SUBSTR", field, new Literal(Long.valueOf(META_PART_LENGTH + 1)));
		} else {
			//呼び元でキャッチしているので、変更の際注意
			throw new EntityRuntimeException("LongText Property can only placed main query's select clause. To use at other clause(eg:where condition...), turn on PropertyService's remainInlineText property of config file.");
		}
	}

}
