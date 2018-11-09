package org.iplass.gem.command.generic.bulk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iplass.gem.command.generic.bulk.BulkCommandContext.BulkUpdatedProperty;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.generic.SearchFormView;

public class BulkUpdateFormViewData{

	/** Entity定義 */
	private EntityDefinition entityDefinition;

	/** 詳細画面定義 */
	private SearchFormView view;

	/** 処理タイプ */
	private String execType;

	/** 選択された行番号とエンティティ */
	private List<SelectedRowEntity> entries;

	/** 更新された項目 */
	private List<BulkUpdatedProperty> updatedProperties;

	public BulkUpdateFormViewData(BulkCommandContext context) {
		this.entityDefinition = context.getEntityDefinition();
		this.view = context.getView();
		this.entries = new ArrayList<>();
	}


	/**
	 * Entity定義を取得します。
	 * @return Entity定義
	 */
	public EntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	/**
	 * Entity定義を設定します。
	 * @param entityDefinition Entity定義
	 */
	public void setEntityDefinition(EntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	/**
	 * 検索画面定義を取得します。
	 * @return 詳細画面定義
	 */
	public SearchFormView getView() {
		return view;
	}

	/**
	 * 検索画面定義を設定します。
	 * @param view 詳細画面定義
	 */
	public void setView(SearchFormView view) {
		this.view = view;
	}

	/**
	 * 処理タイプを取得します。
	 * @return 処理タイプ
	 */
	public String getExecType() {
		return execType;
	}

	/**
	 * 処理タイプを設定します。
	 * @param execType 処理タイプ
	 */
	public void setExecType(String execType) {
		this.execType = execType;
	}

	public List<SelectedRowEntity> getEntries() {
		return entries;
	}

	public void setEntity(Integer row, Entity entity) {
		this.entries.add(new SelectedRowEntity(row, entity));
	}

	public List<BulkUpdatedProperty> getUpdatedProperties() {
		return updatedProperties;
	}

	public void setUpdatedProperties(List<BulkUpdatedProperty> updatedProperties) {
		this.updatedProperties = updatedProperties;
	}

	public void addUpdatedProperty(String propName, Object propValue) {
		Optional<Integer> optional = updatedProperties.stream().map(BulkUpdatedProperty::getUpdateNo)
				.max(Integer::compareTo);
		Integer current = Integer.valueOf(0);
		if(optional.isPresent()) {
			current = optional.get();
		}
		current = current + 1;
		updatedProperties.add(new BulkUpdatedProperty(current, propName, propValue));
	}

	/**
	 * 選択された行番号とエンティティ対象のコンポジットクラス
	 */
	public static class SelectedRowEntity {
		// 一括全更新の場合、行番号にはデータが抽出された順番に番号を付けます。
		private Integer row;
		private Entity entity;

		public SelectedRowEntity(Integer row, Entity entity) {
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
