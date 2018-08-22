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

package org.iplass.mtp.impl.tools.entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.util.StringUtil;

public class EntityToolService implements Service {

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public void createJavaMappingClass(File file, EntityDefinition definition, String basePackage) {
		Path path = Paths.get(file.getPath().substring(0, file.getPath().length() - file.getName().length()));
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new EntityToolRuntimeException(getRS("createDirectoriesError"), e);
			}
		}

		String directClassName = StringUtil.isNotBlank(basePackage) ? basePackage + '.' : "" + definition.getName();
		try (OutputStream os = new FileOutputStream(file);
			EntityJavaMappingClassWriter writer = new EntityJavaMappingClassWriter(os, definition, directClassName);
		) {
			writer.writeJavaClass();
		} catch (IOException e) {
			throw new EntityToolRuntimeException(getRS("unexpectedError"), e);
		}
	}

	private String getRS(String suffix, Object... arguments) {
		return ToolsResourceBundleUtil.resourceString("entitytool." + suffix, arguments);
	}

}
