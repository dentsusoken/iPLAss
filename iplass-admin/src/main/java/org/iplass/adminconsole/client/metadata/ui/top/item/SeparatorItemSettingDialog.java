package org.iplass.adminconsole.client.metadata.ui.top.item;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.view.top.parts.TopViewContentParts;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class SeparatorItemSettingDialog extends MtpDialog {
	private TextItem styleField;
	
	private TextItem maxHeightField;

	/**
	 * コンストラクタ
	 */
	public SeparatorItemSettingDialog(TopViewContentParts parts) {

		setTitle("Last Login");
		setHeight(180);
		centerInPage();

		final DynamicForm form = new MtpForm();
		form.setAutoFocus(true);

		styleField = new MtpTextItem("style", "Class");
		styleField.setValue(parts.getStyle());
		SmartGWTUtil.addHoverToFormItem(styleField, AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_styleDescriptionKey"));

		maxHeightField = new MtpTextItem("maxHeight", "MaxHeight");
		if (parts.getMaxHeight() != null && parts.getMaxHeight() > 0) {
			maxHeightField.setValue(parts.getMaxHeight()
					.toString());
		}
		SmartGWTUtil.addHoverToFormItem(maxHeightField,
				AdminClientMessageUtil.getString("ui_metadata_top_item_TopViewContentParts_maxHeightDescriptionKey"));

		form.setItems(styleField, maxHeightField);

		container.addMember(form);
	
		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (form.validate()){
					//入力情報をパーツに
					parts.setStyle(SmartGWTUtil.getStringValue(styleField));
					parts.setMaxHeight(SmartGWTUtil.getIntegerValue(maxHeightField));
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