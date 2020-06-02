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

package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.top.PartsOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.top.TopViewDropAreaPane;
import org.iplass.adminconsole.client.metadata.ui.top.node.TopViewNode;
import org.iplass.mtp.view.top.parts.SeparatorParts;
import org.iplass.mtp.view.top.parts.TopViewParts;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 *
 * @author lis3wg
 */
public class SeparatorItem extends PartsItem {

	private SeparatorParts parts;
	private SeparatorDropPane leftArea;
	private SeparatorDropPane rightArea;

	/**
	 * コンストラクタ
	 */
	public SeparatorItem(SeparatorParts parts, PartsOperationHandler controler) {
		this.parts = parts;
		setTitle("Separator");
		setBackgroundColor("#FFFFFF");
		setHeight(56);
		
		setHeaderControls(HeaderControls.HEADER_LABEL, new HeaderControl(HeaderControl.SETTINGS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SeparatorItemSettingDialog dialog = new SeparatorItemSettingDialog();
				dialog.show();
			}

		}), HeaderControls.CLOSE_BUTTON);

		HLayout layout = new HLayout();
		layout.setMembersMargin(2);
		leftArea = new SeparatorDropPane(controler);
		layout.addMember(leftArea);
		rightArea = new SeparatorDropPane(controler);
		layout.addMember(rightArea);
		addItem(layout);

		if (parts.getLeftParts() != null) {
			leftArea.setParts(parts.getLeftParts());
		}
		if (parts.getRightParts() != null) {
			rightArea.setParts(parts.getRightParts());
		}
	}
	
	private class SeparatorItemSettingDialog extends MtpDialog {

		private TextItem styleField;

		/**
		 * コンストラクタ
		 */
		public SeparatorItemSettingDialog() {

			setTitle("Separator");
			setHeight(130);
			centerInPage();

			final DynamicForm form = new MtpForm();
			form.setAutoFocus(true);

			styleField = new MtpTextItem("style", "Class");
			styleField.setValue(parts.getStyle());
			SmartGWTUtil.addHoverToFormItem(styleField, AdminClientMessageUtil.getString("ui_metadata_top_item_SeparatorItem_styleDescriptionKey"));

			form.setItems(styleField);

			container.addMember(form);

			IButton save = new IButton("OK");
			save.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (form.validate()){
						//入力情報をパーツに
						parts.setStyle(SmartGWTUtil.getStringValue(styleField));
						destroy();
					}
				}
			});

			IButton cancel = new IButton("Cancel");
			cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(save, cancel);
		}
	}

	@Override
	public SeparatorParts getParts() {
		TopViewParts leftParts = null;
		PartsItem leftItem = leftArea.getFirst();
		if (leftItem != null) leftParts = leftItem.getParts();

		TopViewParts rightParts = null;
		PartsItem rightItem = rightArea.getFirst();
		if (rightItem != null) rightParts = rightItem.getParts();

		parts.setLeftParts(leftParts);
		parts.setRightParts(rightParts);
		return parts;
	}

	public class SeparatorDropPane extends TopViewDropAreaPane {

		/**
		 * コンストラクタ
		 * @param controler
		 */
		public SeparatorDropPane(PartsOperationHandler controler) {
			super(controler);
			setWidth("50%");
			setDropTypes("node", "parts");
			setBorder(null);
		}

		@Override
		protected boolean allowDropParts() {
			return true;
		}

		@Override
		protected boolean allowDropWidget() {
			return false;
		}

		@Override
		protected String getDropAreaType() {
			return "parts";
		}

		@Override
		protected DropItemHandler createHandler() {
			return new DropItemHandler() {

				@Override
				public void onDrop(TopViewNode node, int dropPosition) {
					if (SeparatorParts.class.getName().equals(node.getType())) {
						//セパレータ内にセパレータは配置不可
						return;
					}

					//配置するのは一つだけ
					for (PartsItem item : getParts()) {
						removeMember(item);
						item.destroy();
					}

					final PartsItem item = convertNode(node);
					if (item != null) {
						item.doDropAction(new DropActionCallback() {

							@Override
							public void handle() {
								item.setDragType("parts");
								addMember(item);
							}
						});
					}
				}

				@Override
				public boolean onDrag(PartsItem parts, int dropPosition) {
					if (parts instanceof SeparatorItem) {
						return false;
					}

					for (PartsItem item : getParts()) {
						removeMember(item);
						item.destroy();
					}
					addMember(parts, 0);
					return true;
				}
			};
		}

		private PartsItem getFirst() {
			PartsItem[] parts = getParts();
			return parts.length > 0 ? parts[0] : null;
		}

		private void setParts(TopViewParts parts) {
			PartsItem item = convertParts(parts);
			if (item != null) {
				item.setDragType("parts");
				addMember(item);
			}
		}
	}
}
