package org.iplass.mtp.web.template.report.definition;

import java.io.Serializable;

public class JxlsContextParamMapDefinition implements Serializable {

	private static final long serialVersionUID = 2115542869820436482L;
	
	private String key;
	private String mapFrom;
	
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
}
