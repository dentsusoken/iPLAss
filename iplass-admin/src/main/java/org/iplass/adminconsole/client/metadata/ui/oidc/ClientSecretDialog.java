package org.iplass.adminconsole.client.metadata.ui.oidc;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.rpc.AdminAsyncCallback;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;


public class ClientSecretDialog extends MtpDialog {
	
	private final MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private String definitionName;

	/** 編集対象 */
	private TextItem txtClientSecretField;

	public ClientSecretDialog(String definitionName) {
		this.definitionName = definitionName;

		setHeight(150);
		setTitle("ClientSecret");
		centerInPage();

		final DynamicForm form = new MtpForm();
		form.setAutoHeight();
		
		txtClientSecretField = new TextItem();
		txtClientSecretField.setTitle(AdminClientMessageUtil.getString("ui_metadata_oidc_ClientSecretDialog_clientSecret"));
		txtClientSecretField.setWidth("100%");
		txtClientSecretField.setBrowserSpellCheck(false);
		txtClientSecretField.setColSpan(3);
		txtClientSecretField.setStartRow(true);
		txtClientSecretField.setRequired(true);
		
		form.setItems(txtClientSecretField);

		container.addMember(form);

		IButton save = new IButton("Save");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				if (commonValidate) {
					saveClientSecret(SmartGWTUtil.getStringValue(txtClientSecretField));
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);
	}

	private void saveClientSecret(String clientSecret) {
		service.createClientSecret(TenantInfoHolder.getId(), definitionName, clientSecret, new AdminAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				SmartGWTUtil.hideProgress();
				
				SC.say(AdminClientMessageUtil.getString("ui_metadata_oidc_ClientSecretDialog_completion"),
						AdminClientMessageUtil.getString("ui_metadata_oidc_ClientSecretDialog_saveClientSecretComp"));
				
				destroy();
			}

			@Override
			protected void beforeFailure(Throwable caught){
				SmartGWTUtil.hideProgress();
			}
		});
	}
}
