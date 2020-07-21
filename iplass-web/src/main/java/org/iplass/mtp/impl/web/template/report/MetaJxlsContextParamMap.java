package org.iplass.mtp.impl.web.template.report;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.web.template.report.definition.JxlsContextParamMapDefinition;

public class MetaJxlsContextParamMap  implements MetaData {
	private static final long serialVersionUID = -7418907290538937351L;

	private String key;

	private String mapFrom;

	public MetaJxlsContextParamMap() {
	}
	
	public MetaJxlsContextParamMap(String key, String mapFrom) {
		this.key = key;
		this.mapFrom = mapFrom;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMapFrom() {
		return mapFrom;
	}

	public void setMapFrom(String mapFrom) {
		this.mapFrom = mapFrom;
	}
	
	//Definition → Meta
	public void applyConfig(JxlsContextParamMapDefinition definition) {
		key = definition.getKey();
		mapFrom = definition.getMapFrom();
	}

	//Meta → Definition
	public JxlsContextParamMapDefinition currentConfig() {
		JxlsContextParamMapDefinition definition = new JxlsContextParamMapDefinition();
		definition.setKey(key);
		definition.setMapFrom(mapFrom);

		return definition;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}
	
}
