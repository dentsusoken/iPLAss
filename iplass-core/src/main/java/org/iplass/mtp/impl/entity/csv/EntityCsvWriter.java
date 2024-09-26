/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.text.StringEscapeUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EntityCsvWriter implements AutoCloseable, Flushable {

	private static final Logger logger = LoggerFactory.getLogger(EntityCsvWriter.class);

	private static final String DOUBLE_QUOT = "\"";
	private static final int BOM = '\ufeff';
	private static final String CR = "\n";

	private EntityDefinition definition;
	private Writer writer;
	private EntityWriteOption option;
	private ZipOutputStream binaryStore;

	private AuthContext auth;
	private EntityDefinitionManager edm;
	private EntityManager em;
	private boolean isInit;
	private List<PropertyDefinition> properties;

	private String dateFormat;
	private String dateTimeFormat;
	private String timeFormat;
	private ObjectMapper mapper;

	public EntityCsvWriter(EntityDefinition definition, OutputStream out) throws IOException {
		this(definition, out, new EntityWriteOption());
	}

	public EntityCsvWriter(EntityDefinition definition, OutputStream out, EntityWriteOption option) throws IOException {
		this(definition, out, option, null);
	}

	public EntityCsvWriter(EntityDefinition definition, OutputStream out, EntityWriteOption option, ZipOutputStream binaryStore) throws IOException {
		this.definition = definition;
		this.option = option;
		this.writer = new BufferedWriter(new OutputStreamWriter(out, option.getCharset()));
		this.binaryStore = binaryStore;

		auth = AuthContext.getCurrentContext();
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			logger.warn("fail to close EntityCsvWriter resource. check whether resource is leak or not.", e);
		}
	}

	public List<PropertyDefinition> getProperties() {
		init();

		return properties;
	}

	public void writeHeader() {
		init();

		IntStream.range(0, properties.size()).forEach(i -> {
			if (i != 0) {
				writeComma();
			}

			PropertyDefinition property = properties.get(i);
			if (!(property instanceof ReferenceProperty) && property.getMultiplicity() != 1) {
				IntStream.range(0, property.getMultiplicity()).forEach(j -> {
					if (j != 0) {
						writeComma();
					}
					writeText(option.getMultipleColumnName().apply(property, j));
				});
			} else {
				writeText(option.getColumnName().apply(property));
			}
		});

		newLine();
	}

	public void writeEntity(final Entity entity) {
		init();

		for (Iterator<PropertyDefinition> it = properties.iterator(); it.hasNext();) {
			PropertyDefinition property = it.next();

			if (!(property instanceof ReferenceProperty) && property.getMultiplicity() != 1) {
				Object[] values = (Object[])entity.getValue(property.getName());

				//SelectPropertyの出力方式を取得
				if (property instanceof SelectProperty && option.getSortSelectValue().apply((SelectProperty)property)) {

					//ソート出力
					SelectProperty sp = (SelectProperty)property;
					List<SelectValue> selectableValues = sp.getSelectValueList();

					IntStream.range(0, property.getMultiplicity()).forEach(i -> {
						if (i != 0) {
							writeComma();
						}
						if (values == null || i > selectableValues.size() - 1) {
							//登録値がnullか選択可能値より多重度が多い場合
							writeValue(null, property, binaryStore);
						} else {
							final SelectValue targetValue = selectableValues.get(i);
							boolean exist = Arrays.stream(values).anyMatch(value ->
								value != null && ((SelectValue)value).getValue().equals(targetValue.getValue())
							);
							if (exist) {
								writeValue(targetValue, property, binaryStore);
							} else {
								writeValue(null, property, binaryStore);
							}
						}
					});

				}else {
					//通常出力
					IntStream.range(0, property.getMultiplicity()).forEach(i -> {
						if (i != 0) {
							writeComma();
						}
						if (values == null || i >= values.length) {
							writeValue(null, property, binaryStore);
						} else {
							writeValue(values[i], property, binaryStore);
						}
					});
				}
			} else {
				Object val = entity.getValue(property.getName());
				writeValue(val, property, binaryStore);
			}

			if (it.hasNext()) {
				writeComma();
			} else {
				newLine();
			}
		}
	}

	protected void init() {
		if (isInit) {
			return;
		}

		//BOM対応
		if ("UTF-8".equalsIgnoreCase(option.getCharset())) {
			try {
				writer.write(BOM);
			} catch (IOException e) {
				throw new EntityCsvException(e);
			}
		}

		//対象プロパティ取得
		List<PropertyDefinition> outputProperties = getOutputProperties();

		//対象プロパティの検証
		properties = outputProperties.stream()
				.filter(property ->
					// 参照可能権限を保持している場合のみ追加
					auth.checkPermission(
							new EntityPropertyPermission(definition.getName(), property.getName(), EntityPropertyPermission.Action.REFERENCE))
				)
				.filter(property ->
					// バージョン管理している場合のみversionプロパティを追加
					!Entity.VERSION.equals(property.getName()) || definition.getVersionControlType() != VersionControlType.NONE
				)
				.filter(property -> {
					//被参照の除外チェック
					return option.isWithMappedByReference()
							|| CollectionUtil.isNotEmpty(option.getProperties())
							|| (!(property instanceof ReferenceProperty) || ((ReferenceProperty)property).getMappedBy() == null);
				})
				.filter(property -> {
					//BinaryPropertyの除外チェック
					return option.isWithBinary()
							|| CollectionUtil.isNotEmpty(option.getProperties())
							|| !(property instanceof BinaryProperty);
				})
				.collect(Collectors.toList());

		isInit = true;
	}

	/**
	 * 出力対象のプロパティを返します。
	 *
	 * @return 出力対象のプロパティ
	 */
	private List<PropertyDefinition> getOutputProperties() {
		List<PropertyDefinition> outputProperties = null;
		if (CollectionUtil.isNotEmpty(option.getProperties())) {
			// 出力プロパティの直接指定
			outputProperties = new ArrayList<>();

			// バージョンの出力要否は後続の検証によりチェック
			outputProperties.add(definition.getProperty(Entity.OID));
			outputProperties.add(definition.getProperty(Entity.VERSION));

			outputProperties.addAll(option.getProperties().stream()
					.filter(propertyName ->
						!(propertyName.equals(Entity.OID) || propertyName.equals(Entity.VERSION))
					)
					.map(propertyName -> {
						PropertyDefinition property = definition.getProperty(propertyName);
						if (property == null) {
							throw new EntityCsvException(propertyName + " is invalid property in " + definition.getName());
						}
						return property;
					})
					.collect(Collectors.toList()));

		} else {
			outputProperties = definition.getPropertyList();
		}
		return outputProperties;
	}

	private void writeText(String text) {

		try {
			if (StringUtil.isEmpty(text)) {
				return;
			}

			String outText = StringEscapeUtils.escapeCsv(text);
			if (option.isQuoteAll()) {
				if (outText.startsWith("\"") && outText.endsWith("\"")) {
					writer.write(outText);
				} else {
					writer.write(DOUBLE_QUOT + outText + DOUBLE_QUOT);
				}
			} else {
				if (outText.startsWith(" ") || outText.endsWith(" ")) {
					writer.write(DOUBLE_QUOT + outText + DOUBLE_QUOT);
				} else {
					writer.write(outText);
				}
			}
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void writeComma() {
		try {
			writer.write(",");
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void newLine() {
		try {
			writer.write(CR);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private void writeValue(final Object val, final PropertyDefinition pd, final ZipOutputStream binaryStore) {
		if (val == null) {
			return;
		}

		switch (pd.getType()) {
			case EXPRESSION:
				ExpressionProperty ep = (ExpressionProperty)pd;
				if (ep.getResultType() != null) {
					writeValue(val, ep.getResultType());
				} else {
					writeValue(val, ep.getType());
				}
				break;
			case REFERENCE:
				ReferenceProperty rpd = (ReferenceProperty) pd;
				EntityDefinition ed = edm.get(rpd.getObjectDefinitionName());
				if (rpd.getMultiplicity() == 1) {
					Entity entity = (Entity) val;
					if (ed.getVersionControlType().equals(VersionControlType.NONE) && !option.isWithReferenceVersion()) {
						writeText(entity.getOid());
					} else {
						writeText(entity.getOid() + "." + entity.getVersion());
					}
				} else {
					Entity[] eList = (Entity[]) val;
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < eList.length; i++) {
						if (i != 0) {
							sb.append(",");
						}
						if (eList[i] != null) {
							if (ed.getVersionControlType().equals(VersionControlType.NONE) && !option.isWithReferenceVersion()) {
								sb.append(eList[i].getOid());
							} else {
								sb.append(eList[i].getOid() + "." + eList[i].getVersion());
							}
						}
					}
					writeText(sb.toString());
				}
				break;
			case BINARY:
				BinaryReference br = (BinaryReference)val;
				Map<String, String> valueMap = new LinkedHashMap<>();
				valueMap.put("lobid", String.valueOf((br.getLobId())));
				valueMap.put("name", br.getName());
				valueMap.put("type", br.getType());
				writeText(toJsonString(valueMap));

				writeBinaryData(br, binaryStore);
				break;
			default:
				writeValue(val, pd.getType());
		}
	}

	private void writeValue(Object val, PropertyDefinitionType type) {

		switch (type) {
			case BOOLEAN:
				Boolean b = (Boolean) val;
				writeText(b.booleanValue() ? "1" : "0");
				break;
			case DATE:
				writeText(DateUtil.getSimpleDateFormat(getDateFormat(), false).format((java.sql.Date) val));
				break;
			case DATETIME:
				// Timezoneをtrueにするとアップロード時にもTimezoneが適用されるためダウンロード時はfalseにする。
				writeText(DateUtil.getSimpleDateFormat(getDateTimeFormat(), false).format((Timestamp) val));
				break;
			case TIME:
				writeText(DateUtil.getSimpleDateFormat(getTimeFormat(), false).format((Time) val));
				break;
			case SELECT:
				SelectValue sv = (SelectValue) val;
				writeText(sv.getValue());
				break;
			case DECIMAL:
				writeText(((BigDecimal)val).toPlainString());
				break;
			case FLOAT:
				writeText(BigDecimal.valueOf((Double)val).toPlainString());
				break;
			case INTEGER:
			case LONGTEXT:
			case STRING:
			case AUTONUMBER:
			case EXPRESSION:
				writeText(val.toString());
				break;
			case BINARY:
			case REFERENCE:
				break;
			default:
				throw new EntityCsvException("can not convert from " + type + ":" + val);
		}
	}

	private void writeBinaryData(final BinaryReference br, final ZipOutputStream binaryStore) {
		try {
			//Zipが渡されている場合、Zipに追加する
			if (binaryStore != null) {
				//ファイル名をEntity定義名.LOBIDに設定
				//(参考)データ自体はLOBIDで一意（OracleはDB単位、MySQLはテナント単位）
				String entryName = "lobs/" + definition.getName() + "." + br.getLobId();

				ZipEntry zentry = new ZipEntry(entryName);
				binaryStore.putNextEntry(zentry);
				if(write(br, binaryStore)) {
					binaryStore.closeEntry();
				}

			} else {
				//出力先ディレクトリが指定されている場合はExportする
				if(StringUtil.isNotBlank(option.getExportBinaryDataDir())) {
					File lobDir = new File(option.getExportBinaryDataDir());
					if (!lobDir.exists()) {
						lobDir.mkdir();
					}
					//ファイル名をEntity定義名.LOBIDに設定
					//(参考)データ自体はLOBIDで一意（OracleはDB単位、MySQLはテナント単位）
					File lobFile = new File(option.getExportBinaryDataDir() , definition.getName() + "." + br.getLobId());
					if (lobFile.exists()) {
						lobFile.delete();
					}
					lobFile.createNewFile();

					try (FileOutputStream fileBinaryStore = new FileOutputStream(lobFile);) {
						write(br, fileBinaryStore);
					} catch (IOException e) {
						throw new EntityCsvException(e);
					}
				}
			}
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private boolean write(final BinaryReference br, final OutputStream outputStream) throws IOException {
		InputStream is = em.getInputStream(br);
		if (is != null) {
			try (InputStream bis = new BufferedInputStream(is)) {
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = bis.read(buf)) >= 0) {
					outputStream.write(buf, 0, len);
				}
				return true;
			}
		}
		//エラーにしてしまうと作成と出力が止まるので、ログにWaringメッセージ出力
		logger.warn("cannot output binary data. entity = " + br.getDefinitionName() + ", lobid = " + br.getLobId());
		return false;
	}

	private String toJsonString(Object value) {

		if (mapper == null) {
			mapper = new ObjectMapper();
			//for backward compatibility
			mapper.configOverride(java.sql.Date.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd").withTimeZone(TimeZone.getDefault()));
		}
		try (StringWriter writer = new StringWriter()) {
			mapper.writeValue(writer, value);
			return writer.toString();
		} catch (JsonProcessingException e) {
			throw new EntityCsvException(e);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

	private String getDateFormat() {

		if (dateFormat != null) {
			return dateFormat;
		};
		if (option.getDateFormat() != null) {
			dateFormat = option.getDateFormat();
		} else {
			dateFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDateFormat();
		}
		return dateFormat;

	}

	private String getDateTimeFormat() {

		if (dateTimeFormat != null) {
			return dateTimeFormat;
		};
		if (option.getDatetimeSecFormat() != null) {
			dateTimeFormat = option.getDatetimeSecFormat();
		} else {
			dateTimeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputDatetimeSecFormat();
		}
		return dateTimeFormat;

	}

	private String getTimeFormat() {

		if (timeFormat != null) {
			return timeFormat;
		};
		if (option.getTimeSecFormat() != null) {
			timeFormat = option.getTimeSecFormat();
		} else {
			timeFormat = ExecuteContext.getCurrentContext().getLocaleFormat().getOutputTimeSecFormat();
		}
		return timeFormat;

	}
	
	public void writeFooter(String csvDownloadFooter) {
		try {
			writer.write(csvDownloadFooter);
		} catch (IOException e) {
			throw new EntityCsvException(e);
		}
	}

}
