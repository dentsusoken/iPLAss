/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.bulk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	/** 選択された行番号とエンティティ */
	private Map<Integer, Entity> selected;

	/** 更新された項目 */
	private List<BulkUpdatedProperty> updatedProperties;

	public BulkUpdateFormViewData(BulkCommandContext context) {
		this.entityDefinition = context.getEntityDefinition();
		this.view = context.getView();
		this.selected = new HashMap<>();
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

	public Map<Integer, Entity> getSelected() {
		return selected;
	}

	public void setSelected(Integer row, Entity entity) {
		this.selected.put(row, entity);
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

}
