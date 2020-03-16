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

package org.iplass.mtp.impl.validation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.ValidationDefinition;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;


@XmlSeeAlso({MetaValidationNotNull.class, MetaValidationRange.class, MetaValidationLength.class,
	MetaValidationRegex.class, MetaValidationScripting.class, MetaValidationJavaClass.class,
	MetaValidationBinarySize.class, MetaValidationBinaryType.class, MetaValidationExists.class})
public abstract class MetaValidation implements MetaData {
	private static final long serialVersionUID = -5030970640762393766L;

	private String description;

	private String errorMessage;
	private String errorCode;

	private String messageCategory;
	private String messageId;

	private List<MetaLocalizedString> localizedErrorMessageList = new ArrayList<>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessageCategory() {
		return messageCategory;
	}

	public void setMessageCategory(String messageCategory) {
		this.messageCategory = messageCategory;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<MetaLocalizedString> getLocalizedErrorMessageList() {
		return localizedErrorMessageList;
	}

	public void setLocalizedErrorMessageList(List<MetaLocalizedString> localizedErrorMessageList) {
		this.localizedErrorMessageList = localizedErrorMessageList;
	}

	protected void fillTo(ValidationDefinition definition) {
		definition.setDescription(description);
		definition.setErrorMessage(errorMessage);
		definition.setErrorCode(errorCode);
		definition.setMessageCategory(messageCategory);
		definition.setMessageId(messageId);
		definition.setLocalizedErrorMessageList(I18nUtil.toDef(localizedErrorMessageList));
	}

//	protected void copyTo(MetaValidation copy) {
//		copy.setErrorMessage(errorMessage);
//	}

	protected void fillFrom(ValidationDefinition definition) {
		description = definition.getDescription();
		errorMessage = definition.getErrorMessage();
		errorCode = definition.getErrorCode();
		messageCategory = definition.getMessageCategory();
		messageId = definition.getMessageId();
		localizedErrorMessageList = I18nUtil.toMeta(definition.getLocalizedErrorMessageList());
	}

	public abstract void applyConfig(ValidationDefinition definition);

	public abstract ValidationDefinition currentConfig(EntityContext context);

	@Override
	public abstract MetaValidation copy();

	public abstract ValidationHandler createRuntime(MetaEntity entity, MetaProperty property);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result
				+ ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime
				* result
				+ ((localizedErrorMessageList == null) ? 0 : localizedErrorMessageList
						.hashCode());
		result = prime * result
				+ ((messageCategory == null) ? 0 : messageCategory.hashCode());
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaValidation other = (MetaValidation) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
				return false;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (localizedErrorMessageList == null) {
			if (other.localizedErrorMessageList != null)
				return false;
		} else if (!localizedErrorMessageList.equals(other.localizedErrorMessageList))
			return false;
		if (messageCategory == null) {
			if (other.messageCategory != null)
				return false;
		} else if (!messageCategory.equals(other.messageCategory))
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		return true;
	}

}
