package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.io.Serializable;
import java.sql.Timestamp;

public class RecycleBinDataInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long recycleBinId;
	private String name;
	private Timestamp recycleDate;

	public Long getRecycleBinId() {
		return recycleBinId;
	}

	public void setRecycleBinId(Long recycleBinId) {
		this.recycleBinId = recycleBinId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getRecycleDate() {
		return recycleDate;
	}

	public void setRecycleDate(Timestamp recycleDate) {
		this.recycleDate = recycleDate;
	}
}
