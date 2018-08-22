/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.web.template.definition.BinaryTemplateDefinition;
import org.iplass.mtp.web.template.definition.LocalizedBinaryDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;

public class MetaBinaryTemplate extends MetaTemplate {
	private static final long serialVersionUID = -1789466133481644517L;

	private String fileName;
	private byte[] binary;
	private List<MetaLocalizedBinary> localizedBinaryList = new ArrayList<MetaLocalizedBinary>();

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	public List<MetaLocalizedBinary> getLocalizedBinaryList() {
		return localizedBinaryList;
	}

	public void setLocalizedBinaryList(List<MetaLocalizedBinary> localizedBinaryList) {
		this.localizedBinaryList = localizedBinaryList;
	}

	@Override
	public void applyConfig(TemplateDefinition definition) {
		fillFrom(definition);
		BinaryTemplateDefinition def = (BinaryTemplateDefinition) definition;

		fileName = def.getFileName();
		binary = def.getBinary();

		if (def.getLocalizedBinaryList() != null) {
			localizedBinaryList = new ArrayList<MetaLocalizedBinary>();
			for (LocalizedBinaryDefinition locale: def.getLocalizedBinaryList()) {

				MetaLocalizedBinary mlb = new MetaLocalizedBinary();
				mlb.applyConfig(locale);

				localizedBinaryList.add(mlb);
			}
		} else {
			localizedBinaryList = null;
		}
	}

	@Override
	public TemplateDefinition currentConfig() {
		BinaryTemplateDefinition definition = new BinaryTemplateDefinition();
		fillTo(definition);

		definition.setFileName(fileName);
		definition.setBinary(binary);

		if (localizedBinaryList != null) {
			for (MetaLocalizedBinary mlb: localizedBinaryList) {
				definition.addLocalizedBinary(mlb.currentConfig());
			}
		}
		return definition;
	}

	@Override
	public BinaryTemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new BinaryTemplateRuntime();
	}

	public class BinaryTemplateRuntime extends TemplateRuntime {

		public MetaBinaryTemplate getMetaData() {
			return MetaBinaryTemplate.this;
		}

		@Override
		public void handleContent(WebRequestStack requestContext)
				throws IOException, ServletException {
			checkState();

			String lang = ExecuteContext.getCurrentContext().getLanguage();
			byte[] tempBinary = binary;
			if (localizedBinaryList != null) {
				for (MetaLocalizedBinary mlb : localizedBinaryList) {
					if (mlb.getLocaleName().equals(lang)) {
						if (mlb.getBinaryValue() != null && mlb.getBinaryValue().length > 0) {
							tempBinary = mlb.getBinaryValue();
						}
					}
				}
			}

			if (requestContext.getPageContext() != null) {
				throw new TemplateRuntimeException("binary tempalte can not include from jsp... templateName:" + getName());
			} else {
				requestContext.getResponse().getOutputStream().write(tempBinary);
			}
		}
	}
}
