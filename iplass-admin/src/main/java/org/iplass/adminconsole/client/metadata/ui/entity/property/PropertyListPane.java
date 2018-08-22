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

package org.iplass.adminconsole.client.metadata.ui.entity.property;

import java.util.Map;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.entity.definition.EntityDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author lis2s8
 *
 */
public class PropertyListPane extends VLayout {

	private PropertyListGrid grid;

	public PropertyListPane() {
		setMargin(5);

		IButton btnAddTop = new IButton("Add");
		btnAddTop.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.addProperty();
			}
		});
		IButton btnDelTop = new IButton("Remove");
		btnDelTop.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeSelectedProperty();
			}
		});

		VLayout spacer1 = new VLayout();
		spacer1.setWidth100();
		DynamicForm form = new DynamicForm();
		form.setNumCols(2);
		form.setColWidths(120, "*");
		final CheckboxItem chkShowInherited = new CheckboxItem("showInherited", "Show Inherited Property");
		chkShowInherited.setShowTitle(false);
		chkShowInherited.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				grid.setShowInheritedProperty(SmartGWTUtil.getBooleanValue(chkShowInherited));
			}
		});
		form.setFields(chkShowInherited);
		VLayout spacer2 = new VLayout();
		spacer2.setWidth(5);

		HLayout pnlTopButton = new HLayout(5);
		pnlTopButton.setMargin(5);
		pnlTopButton.setAutoHeight();
		pnlTopButton.addMember(btnAddTop);
		pnlTopButton.addMember(btnDelTop);
		pnlTopButton.addMember(spacer1);
		pnlTopButton.addMember(form);
		pnlTopButton.addMember(spacer2);

		grid = new PropertyListGrid(SmartGWTUtil.getBooleanValue(chkShowInherited));

		IButton bntAddBottom = new IButton("Add");
		bntAddBottom.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.addProperty();
			}
		});
		IButton btnDelBottom = new IButton("Remove");
		btnDelBottom.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.removeSelectedProperty();
			}
		});

		// Propertyボタン用レイアウト
		HLayout pnlBottomButton = new HLayout(5);
		pnlBottomButton.setMargin(5);
		pnlBottomButton.setAutoHeight();
		pnlBottomButton.addMember(bntAddBottom);
		pnlBottomButton.addMember(btnDelBottom);

		addMember(pnlTopButton);
		addMember(grid);
		addMember(pnlBottomButton);
	}

	public void setEnableLangMap(Map<String, String> enableLangMap) {
		grid.setEnableLangMap(enableLangMap);
	}

	public void setDefinition(EntityDefinition definition) {
		grid.setDefinition(definition);
	}

	public EntityDefinition getEditDefinition(EntityDefinition definition) {
		grid.getEditDefinition(definition);
		return definition;
	}

	public Map<String, String> getRenamePropertyMap() {
		return grid.getRenamePropertyMap();
	}

	/**
	 * 名前が重複したProperty名を返します。
	 *
	 * @return 重複したProperty名(複数ある場合は先頭。ない場合はnull)
	 */
	public String getDuplicatePropertyName() {
		return grid.getDuplicatePropertyName();
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return true;
	}

	/**
	 * エラー表示をクリアします。
	 */
	public void clearErrors() {
	}

	public void setCrawlStatusChanged(boolean isCrawlEntity) {
		grid.setCrawlStatusChanged(isCrawlEntity);
	}

}
