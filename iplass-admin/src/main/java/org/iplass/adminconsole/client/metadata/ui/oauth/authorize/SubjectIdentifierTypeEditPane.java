/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.oauth.authorize;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.ui.widget.EditablePane;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.auth.oauth.definition.SubjectIdentifierTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.subtypes.PairwiseSubjectIdentifierTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.subtypes.PublicSubjectIdentifierTypeDefinition;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubjectIdentifierTypeEditPane extends VLayout implements EditablePane<SubjectIdentifierTypeDefinition> {

	/** SubjectIdentifierTypeの種類選択用Map */
	private static LinkedHashMap<String, String> subjectIdentifierTypeMap;
	static {
		subjectIdentifierTypeMap = new LinkedHashMap<String, String>();
		subjectIdentifierTypeMap.put("", "");
		subjectIdentifierTypeMap.put(PairwiseSubjectIdentifierTypeDefinition.class.getName(), "Pairwise");
		subjectIdentifierTypeMap.put(PublicSubjectIdentifierTypeDefinition.class.getName(), "Public");
	}

	private DynamicForm form;

	private SelectItem selSubjectIdentifierType;

	private TextItem txtSubjectIdMappedUserProperty;

	private CheckboxItem chkHashing;

	public SubjectIdentifierTypeEditPane() {

		setAutoHeight();
		setWidth100();

		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(3);	//間延びしないように最後に１つ余分に作成
		form.setColWidths(100, 300, "*");
		form.setMargin(3);

		selSubjectIdentifierType = new SelectItem();
		selSubjectIdentifierType.setTitle("Type");
		selSubjectIdentifierType.setWidth("100%");
		selSubjectIdentifierType.setValueMap(subjectIdentifierTypeMap);
		selSubjectIdentifierType.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				typeChanged();
			}
		});

		txtSubjectIdMappedUserProperty = new TextItem();
		txtSubjectIdMappedUserProperty.setTitle("SubjectId Mapped User Property");
		txtSubjectIdMappedUserProperty.setWidth("100%");
		txtSubjectIdMappedUserProperty.setBrowserSpellCheck(false);

		chkHashing = new CheckboxItem();
		chkHashing.setTitle("Hashing");
		chkHashing.setDisabled(true);

		form.setItems(selSubjectIdentifierType, txtSubjectIdMappedUserProperty, chkHashing);

		addMember(form);
	}

	@Override
	public void setDefinition(SubjectIdentifierTypeDefinition definition) {

		if (definition != null) {
			String value = subjectIdentifierTypeMap.get(definition.getClass().getName());
			if (value != null) {
				selSubjectIdentifierType.setValue(definition.getClass().getName());

				if (definition instanceof PairwiseSubjectIdentifierTypeDefinition) {
					txtSubjectIdMappedUserProperty.setValue(((PairwiseSubjectIdentifierTypeDefinition)definition).getSubjectIdMappedUserProperty());
				} else if (definition instanceof PublicSubjectIdentifierTypeDefinition) {
					txtSubjectIdMappedUserProperty.setValue(((PublicSubjectIdentifierTypeDefinition)definition).getSubjectIdMappedUserProperty());
					chkHashing.setValue(((PublicSubjectIdentifierTypeDefinition)definition).isHashing());
				}
			} else {
				selSubjectIdentifierType.setValue("");
			}
		} else {
			selSubjectIdentifierType.setValue("");
		}

		typeChanged();

	}

	@Override
	public SubjectIdentifierTypeDefinition getEditDefinition(SubjectIdentifierTypeDefinition definition) {

		String type = SmartGWTUtil.getStringValue(selSubjectIdentifierType, true);
		if (type != null) {
			if (type.equals(PairwiseSubjectIdentifierTypeDefinition.class.getName())) {
				PairwiseSubjectIdentifierTypeDefinition edit = new PairwiseSubjectIdentifierTypeDefinition();
				edit.setSubjectIdMappedUserProperty(SmartGWTUtil.getStringValue(txtSubjectIdMappedUserProperty));
				return edit;
			} else 	if (type.equals(PublicSubjectIdentifierTypeDefinition.class.getName())) {
				PublicSubjectIdentifierTypeDefinition edit = new PublicSubjectIdentifierTypeDefinition();
				edit.setSubjectIdMappedUserProperty(SmartGWTUtil.getStringValue(txtSubjectIdMappedUserProperty));
				edit.setHashing(SmartGWTUtil.getBooleanValue(chkHashing));
				return edit;
			}
		}
		return null;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public void clearErrors() {
	}

	private void typeChanged() {

		String type = SmartGWTUtil.getStringValue(selSubjectIdentifierType, true);
		if (type != null) {
			if (type.equals(PublicSubjectIdentifierTypeDefinition.class.getName())) {
				chkHashing.setDisabled(false);
				return;
			}
		}
		chkHashing.setValue(false);
		chkHashing.setDisabled(true);
	}
}
