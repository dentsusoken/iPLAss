/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.file;

import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.util.DateUtil;

/**
 * ダウンロード時のファイル名用GroovyTemplateBinding
 */
public class FileNameGroovyTemplateBinding extends GroovyTemplateBinding {

	/**
	 *
	 * @param out Writer
	 * @param csvName ${csvName}にマッピングされる値
	 */
	public FileNameGroovyTemplateBinding(Writer out, String csvName) {
		super(out);

		setupFileBinding(csvName);
	}

	/**
	 *
	 * @param out Writer
	 * @param csvName ${csvName}にマッピングされる値
	 * @param customBindings 個別のBind変数定義
	 */
	public FileNameGroovyTemplateBinding(Writer out, String csvName, Map<String, Object> customBindings) {
		super(out, customBindings);

		setupFileBinding(csvName);
	}

	private void setupFileBinding(String csvName) {

		ExecuteContext ex = ExecuteContext.getCurrentContext();
		Timestamp date = ex.getCurrentTimestamp();//同一トランザクション内の時間を一緒にするため
		setVariable("date", date);
		setVariable("user", AuthContextHolder.getAuthContext().newUserBinding());
		SimpleDateFormat f = DateUtil.getSimpleDateFormat("yyyy", true);
		setVariable("yyyy", f.format(date));
		f.applyPattern("MM");
		setVariable("MM", f.format(date));
		f.applyPattern("dd");
		setVariable("dd", f.format(date));
		f.applyPattern("HH");
		setVariable("HH", f.format(date));
		f.applyPattern("mm");
		setVariable("mm", f.format(date));
		f.applyPattern("ss");
		setVariable("ss", f.format(date));

		setVariable("csvName", csvName);
	}

}
