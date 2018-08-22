/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.mtp.entity.definition.EntityDefinition;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class EntityAttributePane extends HLayout {

	private DynamicForm form;

	private EntityAttributePaneController controller = GWT.create(EntityAttributePaneController.class);

	public EntityAttributePane() {

		setHeight(controller.getHeight());
		setMargin(5);

		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(5);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(120, "*", 120, "*", "*");

		form.setFields(controller.getFormItems().toArray(new FormItem[0]));
		addMember(form);
	}

	public void setDefinition(EntityDefinition definition) {
		controller.applyFrom(definition);
	}

	public EntityDefinition getEditDefinition(EntityDefinition definition) {
		controller.applyTo(definition);
		return definition;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return form.validate();
	}

	/**
	 * エラー表示をクリアします。
	 */
	public void clearErrors() {
		form.clearErrors(true);
	}

	/**
	 * タイプ変更イベントを設定します。
	 * @param handler
	 */
	public void setCrawlStatusChangedHandler(ChangedHandler handler) {
		controller.setCrawlStatusChangedHandler(handler);
	}

	public boolean isCrawlEntity() {
		return controller.isCrawlEntity();
	}

}
