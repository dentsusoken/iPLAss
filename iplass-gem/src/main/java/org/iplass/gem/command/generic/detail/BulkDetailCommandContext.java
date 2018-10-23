package org.iplass.gem.command.generic.detail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.ViewUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.Section;

public class BulkDetailCommandContext extends DetailCommandContext {

	/**
	 * oidマップ。 key: oid, value: 選択された行番号。
	 */
	private Map<String, Integer> oidRowMap;
	/**
	 * versionマップ。 key: 選択された番号, value: version
	 */
	private Map<Integer, Long> versionMap;

	/**
	 * updateDateマップ。 key: 選択された行番号, value: 更新日時
	 */
	private Map<Integer, Long> updateDateMap;
	
	/**
	 * パラメータパターン
	 */
	private Pattern pattern = Pattern.compile("^(\\d+)\\_([^_]+)$");
	
	/**
	 * 更新対象外のプロパティリスト
	 */
	@SuppressWarnings("serial")
	private Set<String> skipProps = new HashSet<String>() {
		{
			add(Entity.OID);
			add(Entity.VERSION);
			add(Entity.UPDATE_BY);
		}
	};

	public BulkDetailCommandContext(RequestContext request, EntityManager entityLoader, EntityDefinitionManager definitionLoader) {
		super(request, entityLoader, definitionLoader);
		this.oidRowMap = new HashMap<String, Integer>();
		this.versionMap = new HashMap<Integer, Long>();
		this.updateDateMap = new HashMap<Integer, Long>();
		populateParamMap();
	}

	private void populateParamMap() {
		// BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] oid = (String[]) request.getAttribute(Constants.OID);
		if (oid == null || oid.length == 0) {
			oid = getParams(Constants.OID);
		}
		if (oid != null) {
			for (int i = 0; i < oid.length; i++) {
				// oidには先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(oid[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				String targetOid = params[1];
				if (oidRowMap.containsKey(targetOid) || rowExists(targetRow)) {
					// TODO
					throw new ApplicationException();
				}
				oidRowMap.put(targetOid, targetRow);
			}
		}

		// BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] version = (String[]) request.getAttribute(Constants.VERSION);
		if (version == null || version.length == 0) {
			version = getParams(Constants.VERSION);
		}
		if (version != null) {
			for (int i = 0; i < version.length; i++) {
				// versionには先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(version[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				Long targetVersion = Long.parseLong(params[1]);
				if (versionMap.containsKey(targetRow) || !rowExists(targetRow)) {
					// TODO
					throw new ApplicationException();
				}
				versionMap.put(targetRow, targetVersion);
			}
			if (versionMap.size() != oidRowMap.size()) {
				// TODO
				throw new ApplicationException();
			}
		}

		// BulkUpdateAllCommandからのChainの可能性があるので、Attributeから取得する
		String[] timestamp = (String[]) request.getAttribute(Constants.TIMESTAMP);
		if (timestamp == null || timestamp.length == 0) {
			timestamp = getParams(Constants.TIMESTAMP);
		}
		if (timestamp != null) {
			for (int i = 0; i < timestamp.length; i++) {
				// timestampには先頭に「行番号_」が付加されているので分離する
				String[] params = splitRowParam(timestamp[i]);
				Integer targetRow = Integer.parseInt(params[0]);
				Long targetTimestamp = Long.parseLong(params[1]);
				if (updateDateMap.containsKey(targetRow) || !rowExists(targetRow)) {
					// TODO
					throw new ApplicationException();
				}
				updateDateMap.put(targetRow, targetTimestamp);
			}
			if (updateDateMap.size() != oidRowMap.size()) {
				// TODO
				throw new ApplicationException();
			}
		}
	}

	private String[] splitRowParam(String rowParam) {
		Matcher m = pattern.matcher(rowParam);
		if (!m.matches()) {
			// TODO
			throw new ApplicationException();
		}
		String[] params = new String[] { m.group(1), m.group(2) };
		return params;
	}
	
	private boolean rowExists(Integer row) {
		Optional<Integer> optional = oidRowMap.values().stream()
				.filter(e -> e.equals(row))
				.findAny();
		return optional.isPresent();
	}
	
	/* 
	 * 一括更新側で呼び出せないように制限します。
	 */
	@Override
	public String getOid() {
		throw new UnsupportedOperationException();
	}

	/* 
	 * 一括更新側で呼び出せないように制限します。
	 */
	@Override
	public Long getVersion() {
		throw new UnsupportedOperationException();
	}

	/* 
	 * 一括更新側で呼び出せないように制限します。
	 */
	@Override
	public Entity createEntity() {
		throw new UnsupportedOperationException();
	}
	
	public Set<String> getOids() {
		return oidRowMap.keySet();
	}

	public Integer getRow(String oid) {
		return oidRowMap.get(oid);
	}

	public Long getVersion(String oid) {
		Integer row = oidRowMap.get(oid);
		return versionMap.get(row);
	}

	public Timestamp getTimestamp(String oid) {
		Timestamp ts = null;
		Integer row = oidRowMap.get(oid);
		Long l = updateDateMap.get(row);
		if (l != null)
			ts = new Timestamp(l);
		return ts;
	}

	public String getBulkUpdatePropName() {
		return getParam(Constants.BULK_UPDATE_PROP_NM);
	}

	public Entity createEntity(String oid) {
		Entity entity = createEntity("" , null);
		entity.setOid(oid);
		entity.setUpdateDate(getTimestamp(oid));
//		if (isVersioned()) {
		// バージョン管理にかかわらず、セットする問題ないかな..
		entity.setVersion(getVersion(oid));
//		}
//		setVirtualPropertyValue(entity);
		getRegistrationInterrupterHandler().dataMapping(entity);
		validate(entity);
		return entity;
	}

	private Entity createEntity(String paramPrefix, String errorPrefix) {
		Entity entity = newEntity();
		for (PropertyDefinition p : getPropertyList()) {
			if (skipProps.contains(p.getName())) continue;
			Object value = getPropValue(p, paramPrefix);
			entity.setValue(p.getName(), value);
			if (errorPrefix != null) {
				String name = paramPrefix + p.getName();
				// Entity生成時にエラーが発生していないかチェックして置き換え
				String errorName = errorPrefix + p.getName();
				getErrors().stream()
					.filter(error -> error.getPropertyName().equals(name))
					.forEach(error -> error.setPropertyName(errorName));
			}
		}
		return entity;
	}

	public List<PropertyItem> getProperty() {
		String execType = getExecType();
		List<PropertyItem> propList = new ArrayList<PropertyItem>();
		String propName = getParam(Constants.BULK_UPDATE_PROP_NM);
		if (StringUtil.isEmpty(propName)) {
			// TODO
			throw new ApplicationException();
		}
		PropertyDefinition pd = getEntityDefinition().getProperty(propName);
		if (pd == null) {
			// TODO
			throw new ApplicationException();
		}

		for (Section section : getView().getSections()) {
			if (section instanceof DefaultSection) {
				if (section.isDispFlag() && !((DefaultSection) section).isHideDetail() && ViewUtil.dispElement(execType, section)) {
					List<PropertyItem> propertyItems = getProperty((DefaultSection) section);
					Optional<PropertyItem> ret = propertyItems.stream()
							.filter(p -> p.getPropertyName().equals(propName))
							.findFirst();
					if (ret.isPresent()) {
						PropertyItem pi = ret.get();
						propList.add(pi);
						// 更新するプロパティが複数の場合があります。
						if (pi.getEditor() instanceof DateRangePropertyEditor) {
							DateRangePropertyEditor de = (DateRangePropertyEditor) pi.getEditor();
							Optional<PropertyItem> subRet = propertyItems.stream()
									.filter(p -> p.getPropertyName().equals(de.getToPropertyName()))
									.findFirst();
							if (subRet.isPresent()) {
								propList.add(subRet.get());
							}
						} else if (pi.getEditor() instanceof JoinPropertyEditor) {
							JoinPropertyEditor je = (JoinPropertyEditor) pi.getEditor();
							for (NestProperty nestProperty : je.getProperties()) {
								String nestPropName = nestProperty.getPropertyName();
								Optional<PropertyItem> subRet = propertyItems.stream()
										.filter(p -> p.getPropertyName().equals(nestPropName))
										.findFirst();
								if (subRet.isPresent()) {
									propList.add(subRet.get());
								}
							}
						}
						break;
					}
				}
			}
		}
		return propList;
	}
}
