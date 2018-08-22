package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.io.Serializable;

public class RecycleBinEntityInfo implements Serializable {

	private static final long serialVersionUID = -4503515537103110614L;

	private String name;
	private String displayName;
	private int count;

	private boolean isError = false;
	private String errorMessage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}