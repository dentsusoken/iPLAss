/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TimeZone;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.EntityMapping;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EntityCsvReader implements Iterable<Entity>, AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(EntityCsvReader.class);

	/** 制御フラグのヘッダー項目名 */
	public static final String CTRL_CODE_KEY = "_useCtrl";
	/** 制御フラグ、追加を示す値。 */
	public static final String CTRL_INSERT = "I";
	/** 制御フラグ、更新を示す値。*/
	public static final String CTRL_UPDATE = "U";
	/** 制御フラグ、削除を示す値。 */
	public static final String CTRL_DELETE = "D";
	/** 制御フラグ、マージ(追加更新)を示す値。 */
	public static final String CTRL_MERGE = "M";

	public static final int NVL = -1;

	private EntityDefinition definition;

	/** Referenceプロパティの場合はバージョンも指定  */
	private boolean withReferenceVersion;
	/** OIDに付けるPrefix  */
	private String prefixOid;
	/** 存在しないプロパティは無視  */
	private boolean ignoreNotExistsProperty;
	/** InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定 */
	private boolean enableAuditPropertySpecification;

	private Reader reader;
	private CsvListReader csvListReader;

	private List<String> header;
	private List<String> properties;
	private List<Integer> arrayIndex;
	private Map<String, ReferenceInfo> references;
	private boolean isInit;
	private boolean isIterate;
	private boolean useCtrl;
	private ObjectMapper mapper;
	private boolean usePrefixOid;

	private EntityDefinitionManager edm = ManagerLocator.manager(EntityDefinitionManager.class);
	private EntityManager em = ManagerLocator.manager(EntityManager.class);

	public EntityCsvReader(EntityDefinition definition, InputStream inputStream) throws UnsupportedEncodingException {
		this(definition, inputStream, "UTF-8");
	}
	public EntityCsvReader(EntityDefinition definition, InputStream inputStream, String charset) throws UnsupportedEncodingException {
		Reader reader = new InputStreamReader(inputStream, charset);
		BufferedReader buffered = new BufferedReader(reader);
		this.definition = definition;
		this.reader = buffered;
	}

	public EntityCsvReader(EntityDefinition definition, Reader reader) {
		BufferedReader buffered = null;
		if (reader instanceof BufferedReader) {
			buffered = (BufferedReader)reader;
		} else {
			buffered = new BufferedReader(reader);
		}
		this.definition = definition;
		this.reader = buffered;
	}

	protected void init() {
		if (isInit) {
			return;
		}

		csvListReader = new CsvListReader(reader, new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build());

		List<String> headerLine = null;
		try {
			headerLine = csvListReader.read();
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
		if (headerLine == null) {
			throw new EntityCsvException("CE2000", rs("impl.csv.EntityCsvReader.emptyFile"));
		}

		// read header
		readHeader(headerLine);

		// check valid header
		validateHeader();

		// check prefix oid
		// カスタムOIDが設定されている場合は無視
		usePrefixOid = StringUtil.isNotEmpty(prefixOid) && CollectionUtil.isEmpty(definition.getOidPropertyName());

		isInit = true;
	}

	/**
	 * Referenceプロパティの場合はバージョンも指定するかを設定します。
	 *
	 * @param withReferenceVersion Referenceプロパティの場合はバージョンも指定
	 */
	public void setWithReferenceVersion(boolean withReferenceVersion) {
		this.withReferenceVersion = withReferenceVersion;
	}

	/**
	 * Referenceプロパティの場合はバージョンも指定するかを設定します。
	 *
	 * @param withReferenceVersion Referenceプロパティの場合はバージョンも指定
	 * @return インスタンス
	 */
	public EntityCsvReader withReferenceVersion(boolean withReferenceVersion) {
		this.withReferenceVersion = withReferenceVersion;
		return this;
	}

	/**
	 * OIDに付けるPrefixを設定します。
	 *
	 * @param prefixOid OIDに付けるPrefix
	 */
	public void setPrefixOid(String prefixOid) {
		this.prefixOid = prefixOid;
	}

	/**
	 * OIDに付けるPrefixを設定します。
	 *
	 * @param prefixOid OIDに付けるPrefix
	 * @return インスタンス
	 */
	public EntityCsvReader prefixOid(String prefixOid) {
		this.prefixOid = prefixOid;
		return this;
	}

	/**
	 * 存在しないプロパティは無視するかを設定します。
	 *
	 * @param ignoreNotExistsProperty 存在しないプロパティは無視するか
	 */
	public void setIgnoreNotExistsProperty(boolean ignoreNotExistsProperty) {
		this.ignoreNotExistsProperty = ignoreNotExistsProperty;
	}

	/**
	 * 存在しないプロパティは無視するかを設定します。
	 *
	 * @param ignoreNotExistsProperty 存在しないプロパティは無視するか
	 * @return インスタンス
	 */
	public EntityCsvReader ignoreNotExistsProperty(boolean ignoreNotExistsProperty) {
		this.ignoreNotExistsProperty = ignoreNotExistsProperty;
		return this;
	}

	/**
	 * InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定するかを設定します。
	 *
	 * @param enableAuditPropertySpecification 指定するか
	 */
	public void setEnableAuditPropertySpecification(boolean enableAuditPropertySpecification) {
		this.enableAuditPropertySpecification = enableAuditPropertySpecification;
	}

	/**
	 * InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定するかを設定します。
	 *
	 * @param enableAuditPropertySpecification 指定するか
	 * @return インスタンス
	 */
	public EntityCsvReader enableAuditPropertySpecification(boolean enableAuditPropertySpecification) {
		this.enableAuditPropertySpecification = enableAuditPropertySpecification;
		return this;
	}

	public boolean isUseCtrl() {
		init();
		return useCtrl;
	}

	public List<String> properties() {
		init();
		return properties;
	}

	@Override
	public Iterator<Entity> iterator() {
		init();

		if (isIterate) {
			throw new UnsupportedOperationException("concurrent iterate not supported.");
		}
		isIterate = true;

		return new Iterator<Entity>() {

			private List<String> currentLine;

			@Override
			public boolean hasNext() {
				read();
				if (currentLine == null) {
					return false;
				}
				return true;
			}

			private void read() {
				if (currentLine == null) {
					try {
						currentLine = csvListReader.read();
					} catch (IOException e) {
						throw new EntityCsvException(e);
					}
				}
			}

			@Override
			public Entity next() {
				read();
				if (currentLine == null) {
					throw new NoSuchElementException();
				}
				validateLine(currentLine);

				EntityMapping mapping = definition.getMapping();
				Entity entity = generateEntity(mapping, null);
				entity.setDefinitionName(definition.getName());

				Set<String> multiProp = new HashSet<>();
				for (int i = 0; i < header.size(); i++) {

					String headerName = header.get(i);

					if (!enableAuditPropertySpecification &&
							(headerName.equals(Entity.UPDATE_BY)
							|| headerName.equals(Entity.UPDATE_DATE)
//							|| headerName.equals(Entity.LOCKED_BY)
							|| headerName.equals(Entity.CREATE_BY)
							|| headerName.equals(Entity.CREATE_DATE))) {
						continue;
					}

					String value = currentLine.get(i);

					if (i == 0 && headerName.equals(CTRL_CODE_KEY)) {
						entity.setValue(CTRL_CODE_KEY, value);
						continue;
					}

					try {
						String propName = headerName;
						Object propValue = null;
						if (headerName.contains(".")) {
							//ReferenceのUniqueKey指定対応
							if (references.get(headerName) != null) {
								propName = references.get(headerName).propName;
								propValue = references.get(headerName).convEntity(value);
							} else {
								propValue = conv(value, headerName, null);
							}
						} else {
							propValue = conv(value, headerName, definition.getProperty(propName));
						}
						if (arrayIndex.get(i) == NVL) {
							entity.setValue(propName, propValue);
						} else {
							if (definition.getProperty(propName) != null) {
								Object[] valArray = (Object[]) entity.getValue(propName);
								if (valArray == null) {
									valArray = (Object[]) Array.newInstance(definition.getProperty(propName).getJavaType(), definition.getProperty(propName).getMultiplicity());
									entity.setValue(propName, valArray);
									multiProp.add(propName);
								}
								valArray[arrayIndex.get(i)] = propValue;
							} else {
								entity.setValue(propName, propValue);
							}
						}
					} catch (Exception e) {
						//変換でエラー
						throw new EntityCsvException("CE1001", rs("impl.csv.EntityCsvReader.invalidValue", headerName, value), e);
					}
				}

				//OID Prefixのセット
				if (usePrefixOid && entity.getOid() != null) {
					entity.setOid(prefixOid + entity.getOid());
				}

				//配列のうしろのnull値を削除
				trimMultipleValue(entity, multiProp);

				currentLine = null;
				return entity;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public void close() {
		Closeable closable = csvListReader != null ? csvListReader : reader;
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException e) {
				logger.warn("fail to close EntityCsvReader resource. check whether resource is leak or not.", e);
			}
			csvListReader = null;
			reader = null;
		}
	}

	protected List<String> header() {
		init();
		return header;
	}

	protected List<String> readLine() throws IOException {
		init();
		return csvListReader.read();
	}

	protected void validateLine(List<String> line) {
		init();
		if (line.size() != header.size()) {
			throw new EntityCsvException("CE2006",
					rs("impl.csv.EntityCsvReader.mismatchHeadSize", header.size(), line.size()));
		}
	}

	protected void validateValue(String headerName, String value) {
		init();

		try {
			if (!enableAuditPropertySpecification &&
					(headerName.equals(Entity.UPDATE_BY)
					|| headerName.equals(Entity.UPDATE_DATE)
//					|| headerName.equals(Entity.LOCKED_BY)
					|| headerName.equals(Entity.CREATE_BY)
					|| headerName.equals(Entity.CREATE_DATE))) {
				return;
			}

			//ReferenceのUniqueKey指定対応
			if (headerName.contains(".")) {
				if (references.get(headerName) != null) {
					conv(value, headerName, references.get(headerName).unique);
				} else {
					conv(value, headerName, null);
				}
			} else {
				conv(value, headerName, definition.getProperty(headerName));
			}

		} catch (Exception e) {
			//変換でエラー
			throw new EntityCsvException("CE1001", rs("impl.csv.EntityCsvReader.invalidValue", headerName, value), e);
		}
	}

	private void readHeader(final List<String> headerLine) {

		header = new ArrayList<>(headerLine.size());
		arrayIndex = new ArrayList<>(headerLine.size());
		properties = new ArrayList<>(headerLine.size());
		for (int i = 0; i < headerLine.size(); i++) {
			String headerValue = headerLine.get(i);
			if (StringUtil.isEmpty(headerValue)) {
				throw new EntityCsvException("CE2001", rs("impl.csv.EntityCsvReader.emptyHead", (i + 1)));
			}

			//表示名除去
			String headerName = headerValue.split("\\(")[0];

			//multiple除去
			int index = NVL;
			if (headerName.contains("[")) {
				try {
					index = Integer.parseInt(headerName.substring(headerName.indexOf('[') + 1, headerName.lastIndexOf(']')));
				} catch (NumberFormatException e) {
					throw new EntityCsvException("CE2002", rs("impl.csv.EntityCsvReader.invalidHeadIndex", (i + 1), headerValue));
				}
				headerName = headerName.substring(0, headerName.indexOf('['));
			}
			header.add(headerName);
			arrayIndex.add(index);

			//ReferenceのUniqueKey指定対応
			String propName = headerName;
			if (headerName.contains(".")) {
				propName = headerName.substring(0, headerName.indexOf("."));
			}
			properties.add(propName);
		}

		//UTF-8 BOM対応
		if (header.size() > 0) {
			header.set(0, excludeBOM(header.get(0)));
		}
		if (properties.size() > 0) {
			properties.set(0, excludeBOM(properties.get(0)));
		}
	}

	private String excludeBOM(String value) {
		if (value.charAt(0) == 0xFEFF) {
			return String.copyValueOf(value.toCharArray(), 1, value.toCharArray().length - 1);
		}
		return value;
	}

	private void validateHeader() {

		for (int i = 0; i < header.size(); i++) {

			String headerName = header.get(i);

			if (i ==0 && CTRL_CODE_KEY.equals(headerName)) {
				useCtrl = true;
				continue;
			}

			PropertyDefinition pd = null;
			ReferenceInfo reference = null;
			if (headerName.contains(".")) {
				//ReferenceのUniqueKey指定対応
				String propName = properties.get(i);
				String uniqueKey = headerName.substring(propName.length() + 1);
				ReferenceProperty rp = (ReferenceProperty)definition.getProperty(propName);
				if (rp == null) {
					if (ignoreNotExistsProperty) {
						logger.warn(definition.getName() + " has not " + headerName + " property. skip header.");
					} else {
						throw new EntityCsvException("CE2003", rs("impl.csv.EntityCsvReader.invalidHeadNotFindProp", (i + 1), headerName));
					}
				} else {
					EntityDefinition red = edm.get(rp.getObjectDefinitionName());
					if (red == null) {
						throw new EntityCsvException("CE2003", rs("impl.csv.EntityCsvReader.invalidHeadNotFindProp", (i + 1), headerName));
					}
					PropertyDefinition unique = red.getProperty(uniqueKey);
					if (unique == null) {
						throw new EntityCsvException("CE2003", rs("impl.csv.EntityCsvReader.invalidHeadNotFindProp", (i + 1), headerName));
					}
					//UniqueKeyチェック
					if (!unique.getName().equals(Entity.OID)
							&& unique.getIndexType() != IndexType.UNIQUE
							&& unique.getIndexType() != IndexType.UNIQUE_WITHOUT_NULL) {
						throw new EntityCsvException("CE2009", rs("impl.csv.EntityCsvReader.invalidHeadRefUniqueKey", (i + 1), headerName, uniqueKey));
					}
					reference = new ReferenceInfo(propName, red, unique);
					pd = rp;
				}
			} else {
				pd = definition.getProperty(headerName);
				if (pd == null) {
					if (ignoreNotExistsProperty) {
						logger.warn(definition.getName() + " has not " + headerName + " property. skip header.");
					} else {
						throw new EntityCsvException("CE2003", rs("impl.csv.EntityCsvReader.invalidHeadNotFindProp", (i + 1), headerName));
					}
				} else {
					//Referenceの場合、参照Entity定義取得
					if (pd instanceof ReferenceProperty) {
						EntityDefinition red = edm.get(((ReferenceProperty)pd).getObjectDefinitionName());
						if (red == null) {
							throw new EntityCsvException("CE2003", rs("impl.csv.EntityCsvReader.invalidHeadNotFindProp", (i + 1), headerName));
						}
						reference = new ReferenceInfo(headerName, red, null);
					}
				}
			}

			if (pd != null) {
				if (pd instanceof ReferenceProperty) {
					if (arrayIndex.get(i) != NVL) {
						throw new EntityCsvException("CE2003", rs("impl.csv.EntityCsvReader.invalidHeadRefProp", (i + 1), headerName));
					}
				} else {
					if (pd.getMultiplicity() == 1) {
						if (arrayIndex.get(i) != NVL) {
							throw new EntityCsvException("CE2004", rs("impl.csv.EntityCsvReader.invalidHeadSingleProp", (i + 1), headerName));
						}
					} else {
						if (arrayIndex.get(i) == NVL) {
							throw new EntityCsvException("CE2005", rs("impl.csv.EntityCsvReader.invalidHeadMultiProp", (i + 1), headerName));
						}
					}
				}
			}

			//Reference情報を保持
			if (reference != null) {
				if (references == null) {
					references = new HashMap<>();
				}
				references.put(headerName, reference);
			}
		}
	}

	private Object conv(String valStr, String headerName, PropertyDefinition pd) {

		//Propertyが存在しない場合はそのまま文字列を返す
		if (pd == null) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			} else {
				return valStr;
			}
		}

		if (pd instanceof BinaryProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			BinaryReference br = null;
			try {
				if (mapper == null) {
					mapper = new ObjectMapper();
					//for backward compatibility
					mapper.configOverride(java.sql.Date.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd").withTimeZone(TimeZone.getDefault()));
				}
				JsonNode root = mapper.readValue(valStr, JsonNode.class);

				JsonNode lobidNode = root.get("lobid");
				if (lobidNode != null) {
					br = new BinaryReference();
					br.setLobId(Long.parseLong(lobidNode.asText()));

					JsonNode nameNode = root.get("name");
					if (nameNode != null) {
						br.setName(nameNode.asText());
					}
					JsonNode typeNode = root.get("type");
					if (typeNode != null) {
						br.setType(typeNode.asText());
					}
				}
			} catch (IOException e) {
				throw new EntityCsvException("fail to create binary value. value:" + valStr);
			}

			return br;

		} else if (pd instanceof BooleanProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			if (valStr.equals("1")
					|| valStr.equalsIgnoreCase("true")
					|| valStr.equalsIgnoreCase("t")
					|| valStr.equalsIgnoreCase("yes")
					|| valStr.equalsIgnoreCase("y")) {
				return Boolean.TRUE;
			} else if (valStr.trim().length() == 0
					|| valStr.equals("0")
					|| valStr.equalsIgnoreCase("false")
					|| valStr.equalsIgnoreCase("f")
					|| valStr.equalsIgnoreCase("no")
					|| valStr.equalsIgnoreCase("n")) {
				return Boolean.FALSE;
			}
		} else if (pd instanceof DateProperty) {
			return ConvertUtil.convertFromString(Date.class, valStr);
		} else if (pd instanceof DateTimeProperty) {
			return ConvertUtil.convertFromString(Timestamp.class, valStr);
		} else if (pd instanceof DecimalProperty) {
			//TODO scale roundingMode対応必要か？
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			return new BigDecimal(valStr);
		} else if (pd instanceof ExpressionProperty) {
			return null;
		} else if (pd instanceof FloatProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			return Double.parseDouble(valStr);
		} else if (pd instanceof IntegerProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			return Long.parseLong(valStr);
		} else if (pd instanceof LongTextProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			return valStr;
		} else if (pd instanceof ReferenceProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}

			ReferenceProperty rpd = (ReferenceProperty)pd;

			ReferenceInfo reference = references.get(headerName);

			//参照Entityに設定するOIDPrefix、カスタムOIDが設定されている場合は無視
			String refOidPrefix
				= StringUtil.isNotEmpty(prefixOid) && CollectionUtil.isEmpty(reference.ed.getOidPropertyName()) ? prefixOid : "";

			if (rpd.getMultiplicity() == 1) {
				Entity entity = generateReferenceEntity(valStr, rpd, reference.ed, refOidPrefix);
				return entity;
			} else {
				ArrayList<Entity> eList = new ArrayList<>();
				String[] oidList = valStr.split(",");
				for (String oid: oidList) {
					oid = oid.trim();
					if (oid.length()  == 0) {
						//配列にnullを含むとエラーになるので除外
						//eList.add(null);
					} else {
						eList.add(generateReferenceEntity(oid, rpd, reference.ed, refOidPrefix));
					}
				}

				if (reference.ed.getMapping() != null && reference.ed.getMapping().getMappingModelClass() != null) {
					String className = reference.ed.getMapping().getMappingModelClass();
					try {
						Object[] hoge = (Object[]) Array.newInstance(Class.forName(className).newInstance().getClass(), eList.size());
						return eList.toArray(hoge);
					} catch (Exception e) {
						throw new EntityCsvException("CE2008", rs("impl.csv.EntityCsvReader.invalidRefJavaMappingClass", rpd.getName(), className), e);
					}
				} else {
					return eList.toArray(new Entity[eList.size()]);
				}

			}

		} else if (pd instanceof SelectProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			} else {
				SelectProperty selectDef = (SelectProperty)pd;
				for (SelectValue sv: selectDef.getSelectValueList()) {
					if (valStr.equals(sv.getValue())) {
						return sv;
					}
				}
			}
		} else if (pd instanceof StringProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			return valStr;
		} else if (pd instanceof TimeProperty) {
			return ConvertUtil.convertFromString(Time.class, valStr);
		} else if (pd instanceof AutoNumberProperty) {
			if (StringUtil.isEmpty(valStr)) {
				return null;
			}
			return valStr;
		}
		throw new EntityCsvException("CE2007", rs("impl.csv.EntityCsvReader.invalidValueType", pd.getName(), pd.getClass().getName(), valStr));
	}

	private Entity generateEntity(EntityMapping mapping, String propName) {
		Entity entity = null;
		if (mapping != null) {
			try {
				entity = (Entity) Class.forName(mapping.getMappingModelClass()).newInstance();
			} catch (Exception e) {
				if (propName == null) {
					throw new EntityCsvException("CE2008", rs("impl.csv.EntityCsvReader.invalidJavaMappingClass", mapping.getMappingModelClass()), e);
				} else {
					throw new EntityCsvException("CE2008", rs("impl.csv.EntityCsvReader.invalidRefJavaMappingClass", propName, mapping.getMappingModelClass()), e);
				}
			}
		} else {
			entity = new GenericEntity();
		}
		return entity;
	}

	private Entity generateReferenceEntity(String value, ReferenceProperty rpd, EntityDefinition red, String refOidPrefix) {

		Entity entity = generateEntity(red.getMapping(), rpd.getName());
		entity.setDefinitionName(red.getName());

		String oid;
		String ver;
		if (!withReferenceVersion
				&& red != null && red.getVersionControlType() == VersionControlType.NONE) {
			//バージョンが出力されていない
			oid = value;
			ver = "0";
		} else {
			if (value.lastIndexOf('.') > 0) {
				oid = value.substring(0, value.lastIndexOf('.'));
				ver = value.substring(value.lastIndexOf('.') + 1);
			} else {
				oid = value;
				ver = "0";
			}
		}

		entity.setOid(refOidPrefix + oid);
		entity.setVersion(Long.parseLong(ver));

		return entity;
	}

	private void trimMultipleValue(Entity entity, Set<String> multiProperty) {

		for (String propName : multiProperty) {
			Object[] valArray = (Object[])entity.getValue(propName);
			if (valArray != null) {
				int lastIndex = -1;
				for (int i = valArray.length - 1; i >= 0; i--){
					if (valArray[i] == null) {
						continue;
					}
					lastIndex = i;
					break;
				}
				if (lastIndex < 0) {
					entity.setValue(propName, null);
				} else if (lastIndex != valArray.length - 1){
					entity.setValue(propName, Arrays.copyOf(valArray, lastIndex + 1));
				}
			}
		}
	}

	private static String rs(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}

	private class ReferenceInfo {

		/** 参照Property名 */
		private String propName;
		/** 参照Entity定義 */
		private EntityDefinition ed;
		/** UniqueProperty定義 */
		private PropertyDefinition unique;

		private Query uniqueQuery;

		public ReferenceInfo(String propName, EntityDefinition ed, PropertyDefinition unique) {
			this.propName = propName;
			this.ed = ed;
			this.unique = unique;
		}

		public Object convEntity(String value) {

			if (StringUtil.isEmpty(value)) {
				return null;
			}

			ReferenceProperty rp = (ReferenceProperty)definition.getProperty(propName);
			if (rp.getMultiplicity() == 1) {
				Object uniqueValue = conv(value, null, unique);
				if (uniqueValue != null) {
					//uniqueKey -> Entity変換
					return search(uniqueValue);
				}

				return null;
			} else {
				ArrayList<Entity> eList = new ArrayList<>();
				String[] uniqueList = value.split(",");
				for (String uniqueStr: uniqueList) {
					uniqueStr = uniqueStr.trim();
					if (uniqueStr.length()  == 0) {
						//配列にnullを含むとエラーになるので除外
						//eList.add(null);
					} else {
						Object uniqueValue = conv(uniqueStr, null, unique);
						if (uniqueValue != null) {
							//uniqueKey -> Entity変換
							Entity ref = search(uniqueValue);
							//配列にnullを含むとエラーになるので見つからなかった場合は除外
							if (ref != null) {
								eList.add(ref);
							}
						}
					}
				}
				if (ed.getMapping() != null && ed.getMapping().getMappingModelClass() != null) {
					String className = ed.getMapping().getMappingModelClass();
					try {
						Object[] hoge = (Object[]) Array.newInstance(Class.forName(className).newInstance().getClass(), eList.size());
						return eList.toArray(hoge);
					} catch (Exception e) {
						throw new EntityCsvException("CE2008", rs("impl.csv.EntityCsvReader.invalidRefJavaMappingClass", propName, className), e);
					}
				} else {
					return eList.toArray(new Entity[eList.size()]);
				}
			}

		}

		private Entity search(Object uniqueValue) {

			if (uniqueQuery == null) {
				uniqueQuery = new Query()
						.select(Entity.OID, Entity.VERSION)
						.from(ed.getName());
			}
			Query query = uniqueQuery.copy()
					.where(new Equals(unique.getName(), uniqueValue));
			return em.searchEntity(query).getFirst();
		}
	}

}
