/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.mtp.impl.report.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.iplass.mtp.util.StringUtil;

/**
 * 変換結果の PDF にパスワード暗号化を施す {@link DocumentConverter} の装飾器。
 *
 * <p>{@link ConvertContext#getPassword()} が空の場合は被装飾 converter の結果をそのまま返す
 * （パスワード未指定時のデフォルト動作は不変）。パスワードが設定されている場合は
 * owner/user 同値の StandardProtectionPolicy で 暗号化する。</p>
 */
public class PdfEncryptDocumentConverter implements DocumentConverter {

	private final DocumentConverter delegate;

	/**
	 * 暗号化で装飾する converter を指定して生成する
	 * @param delegate 被装飾 converter（PDF への変換を行う本体）
	 */
	public PdfEncryptDocumentConverter(DocumentConverter delegate) {
		this.delegate = delegate;
	}

	@Override
	public byte[] convert(byte[] input, String fileName, ConvertContext context) {
		byte[] converted = delegate.convert(input, fileName, context);
		if (StringUtil.isEmpty(context.getPassword())) {
			return converted;
		}

		String password = context.getPassword();
		try (PDDocument doc = Loader.loadPDF(converted)) {
			// owner/user 同値・権限制限なし（印刷・抽出等は全て許可）・AES-256（鍵長 256bit を明示指定）
			StandardProtectionPolicy policy = new StandardProtectionPolicy(password, password, new AccessPermission());
			policy.setEncryptionKeyLength(256);
			policy.setPreferAES(true);
			doc.protect(policy);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			doc.save(out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new DocumentConversionException("PDF encryption failed for " + fileName + ": " + e.getMessage(), -1, e);
		}
	}
}
