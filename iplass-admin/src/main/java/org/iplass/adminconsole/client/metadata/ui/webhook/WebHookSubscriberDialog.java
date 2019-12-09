/**
 * 
 */
package org.iplass.adminconsole.client.metadata.ui.webhook;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.webhook.template.definition.WebHookSubscriber;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author lisf06
 *
 */
public class WebHookSubscriberDialog extends MtpDialog {
	
	/** メインダイアログ */
	private SubscriberAttributePane subscriberAttrEditPane;
	
	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
	
	/** 当編集対象 */
	private WebHookSubscriber curWebHookSubscriber; 
	
	/** イニシャライザー、ダイアログ開ける時に呼ばれる */
	public WebHookSubscriberDialog (WebHookSubscriber definition) {

		curWebHookSubscriber = definition;
		if (curWebHookSubscriber == null) {
			curWebHookSubscriber = new WebHookSubscriber();
		}

		setTitle("Subscriber Editor");
		setHeight("80%");
		centerInPage();

		subscriberAttrEditPane = new SubscriberAttributePane();
		subscriberAttrEditPane.setHeight100();
		container.addMember(subscriberAttrEditPane);

		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				WebHookSubscriber definition = curWebHookSubscriber;

				definition = subscriberAttrEditPane.getEditDefinition(curWebHookSubscriber);
				fireDataChanged(definition);
				destroy();
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				destroy();
			}
		});
		footer.setMembers(save, cancel);
	}
	
	private class SubscriberAttributePane extends VLayout {
		
		private DynamicForm form;
		private TextItem subscriberNameField;
		private TextItem subscriberUrlField;
		private TextItem subscriberSecurityMethodField;//TODO:いまは簡単のボックス、今後対応のパーツも作る必要がある。

		public SubscriberAttributePane() {
			
			form = new MtpForm2Column();
			subscriberNameField = new MtpTextItem("subscribername", "Subscriber");//TODO: add message AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookSubscriberDialog_subscriberName"));
			subscriberUrlField = new MtpTextItem("subscriberurl", "Subscriber's URL");//TODO: add message AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookSubscriberDialog_subscriberUrl"));
			subscriberSecurityMethodField = new MtpTextItem("subscribersecuritymethod", "Subscriber's Security Option");//TODO: add message AdminClientMessageUtil.getString("ui_metadata_webhook_WebHookSubscriberDialog_subscriberSecurityMethod"));
			
			form.setItems(subscriberNameField, subscriberUrlField, subscriberSecurityMethodField);
			addMember(form);
			
			setDefinition(curWebHookSubscriber);
		}
		/** definition -> dialog */
		public void setDefinition(WebHookSubscriber definition) {
			if (definition != null) {
				subscriberNameField.setValue(definition.getSubscriberName());
				subscriberUrlField.setValue(definition.getUrl());
				subscriberSecurityMethodField.setValue(definition.getSecurityMethod());
			} else {
				subscriberNameField.clearValue();
				subscriberUrlField.clearValue();
				subscriberSecurityMethodField.clearValue();
			}
		}

		/** dialog -> definition */
		public WebHookSubscriber getEditDefinition(WebHookSubscriber definition) {
			definition.setSecurityMethod(SmartGWTUtil.getStringValue(subscriberSecurityMethodField));
			definition.setSubscriberName(SmartGWTUtil.getStringValue(subscriberNameField));
			definition.setUrl(SmartGWTUtil.getStringValue(subscriberUrlField));
			return definition;
		}
	//set title?
	}
	
	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}
	
	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(WebHookSubscriber paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
