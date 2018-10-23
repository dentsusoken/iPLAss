package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.Entity;

public class BulkDetailFormViewData extends DetailFormViewData {

	private List<SelectedRowEntityEntry> entries;

	public BulkDetailFormViewData(DetailCommandContext context) {
		super(context);
		this.entries = new ArrayList<>();
	}

	/* 
	 * 一括更新側で呼び出せないように制限します。
	 */
	@Override
	public Entity getEntity() {
		throw new UnsupportedOperationException();
	}
	
	/* 
	 * 一括更新側で呼び出せないように制限します。
	 */
	@Override
	public void setEntity(Entity entity) {
		throw new UnsupportedOperationException();
	}

	public List<SelectedRowEntityEntry> getEntries() {
		return entries;
	}

	public void setEntity(Integer row, Entity entity) {
		this.entries.add(new SelectedRowEntityEntry(row, entity));
	}

	public static class SelectedRowEntityEntry {
		// 一括全更新の場合、行番号にはデータが抽出された順番に番号を付けます。
		private Integer row;
		private Entity entity;

		public SelectedRowEntityEntry(Entity entity) {
			this(null, entity);
		}

		public SelectedRowEntityEntry(Integer row, Entity entity) {
			this.row = row;
			this.entity = entity;
		}

		public Integer getRow() {
			return this.row;
		}

		public Entity getEntity() {
			return this.entity;
		}
	}
}
