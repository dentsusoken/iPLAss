package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * RecycleBinに登録された削除データ1件分を表すRPC転送用DTOです。
 * <p>
 * 削除データのRecycleBin ID、表示名、削除日時を保持し、EntityExplorerの一覧画面と
 * サーバ間で受け渡します。
 */
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
