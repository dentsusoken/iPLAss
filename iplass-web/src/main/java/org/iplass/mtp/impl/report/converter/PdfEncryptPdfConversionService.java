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
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.util.StringUtil;

/**
 * 変換結果の PDF にパスワード暗号化を施す {@link PdfConversionService} の Decorator。
 *
 * <p>本クラスは Service レジストリには登録せず、パスワード指定時にコード上で
 * レジストリから取得した変換 Service を包んで利用する。</p>
 *
 * <p>Decorator として差し替え可能なのは被委譲先の PDF 化 Service（{@link PdfConversionService} 実装）であり、
 * service-config の定義書き換えで {@link GotenbergPdfConversionService} 以外の PDF 化実装へ入れ替えても
 * 本クラスの暗号化処理はそのまま適用される。</p>
 */
public class PdfEncryptPdfConversionService implements PdfConversionService {

	private final PdfConversionService delegate;

	/**
	 * 暗号化を適用する変換 Service を指定して生成する
	 * @param delegate 被委譲 conversion service（PDF への変換を行う本体）
	 */
	public PdfEncryptPdfConversionService(PdfConversionService delegate) {
		this.delegate = delegate;
	}

	@Override
	public void init(Config config) {
		// 本 Decorator はレジストリ管理外（コード上で生成）。ライフサイクルは被委譲 Service に従うため何もしない
	}

	@Override
	public void destroy() {
		// 本 Decorator はレジストリ管理外（コード上で生成）。ライフサイクルは被委譲 Service に従うため何もしない
	}

	@Override
	public byte[] convert(byte[] input, String fileName, ConvertContext context) {
		byte[] converted = delegate.convert(input, fileName, context);
		String password = context.getPassword();
		String ownerPassword = context.getOwnerPassword();
		boolean hasPassword = StringUtil.isNotEmpty(password);
		boolean hasOwnerPassword = StringUtil.isNotEmpty(ownerPassword);
		if (!hasPassword && !hasOwnerPassword) {
			return converted;
		}

		// owner/user 分離
		// user 未設定＋owner 設定：開くのにパスワード不要（user パスワードを空にする）で権限のみ owner に設定
		String userPassword = hasPassword ? password : "";
		// owner 未設定＋user 設定：後方互換性のため owner パスワードに user パスワードをセット
		String effectiveOwnerPassword = hasOwnerPassword ? ownerPassword : password;
		try (PDDocument doc = Loader.loadPDF(converted)) {
			// 権限制限なし（印刷・抽出等は全て許可）・AES-256（鍵長 256bit を明示指定）
			StandardProtectionPolicy policy = new StandardProtectionPolicy(effectiveOwnerPassword, userPassword, new AccessPermission());
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
