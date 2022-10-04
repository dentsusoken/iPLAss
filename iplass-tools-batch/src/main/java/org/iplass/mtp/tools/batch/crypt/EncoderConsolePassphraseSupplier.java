/*
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.crypt;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.iplass.mtp.impl.core.config.PassphraseSupplier;

public class EncoderConsolePassphraseSupplier implements PassphraseSupplier {

	@Override
	public char[] getPassphrase() {
		System.out.println("enter passphrase:");
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		try {
			String passphrase1 = console.readLine();
			System.out.println("enter same passphrase again:");
			String passphrase2 = console.readLine();

			if (!passphrase1.equals(passphrase2)) {
				throw new RuntimeException("passphrase unmatch");
			}
			return passphrase1.toCharArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
