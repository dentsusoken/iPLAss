/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.mail.smime;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * KeyStoreベースのシンプルなSmimeCertStore実装です。
 * 
 * keyStoreに格納されている証明書はクライアントの証明書も含めて、
 * 信頼されたものとして扱います。
 * 実行時には有効期間のチェックのみ行い、証明書チェーンの検証は行いません。
 * （keyStoreにimportする際に、検証されているものとします）
 * 
 * @author K.Higuchi
 *
 */
public class SimpleSmimeCertStore implements SmimeCertStore {
	private static Logger logger = LoggerFactory.getLogger(SimpleSmimeCertStore.class);
	
	//java8から証明書のみでStoreできるようになり、java9以降はデフォルトのStoreとなったのでこれで
	private String keyStoreType = "PKCS12";
	private String keyStoreProvider;

	private String keyStoreFilePath;
	private String keyStorePassword;
	private Map<String, String> keyPasswordMap;
	
	private Integer keyStoreReloadIntervalMinutes;
	
	private volatile CertStore certStore;
	
	private class CertStore {
		private final long expires;
		private final KeyStore store;
		
		private CertStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException {
			
			KeyStore ks;
			if (keyStoreProvider == null) {
				ks = KeyStore.getInstance(keyStoreType);
			} else {
				ks = KeyStore.getInstance(keyStoreType, keyStoreProvider);
			}
			if (keyStoreFilePath != null) {
				try (InputStream is = new FileInputStream(keyStoreFilePath);
						BufferedInputStream bis = new BufferedInputStream(is)) {
					if (keyStorePassword != null) {
						ks.load(bis, keyStorePassword.toCharArray());
					} else {
						ks.load(bis, null);
					}
				}
			} else {
				if (keyStorePassword != null) {
					ks.load(null, keyStorePassword.toCharArray());
				} else {
					ks.load(null, null);
				}
			}
			store = ks;
			
			if (keyStoreReloadIntervalMinutes != null && keyStoreReloadIntervalMinutes > 0) {
				expires = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(keyStoreReloadIntervalMinutes);
			} else {
				expires = Long.MAX_VALUE;
			}
		}
	}
	
	private CertStore getStore() {
		if (certStore == null) {
			return null;
		}
		
		long now = System.currentTimeMillis();
		if (certStore.expires < now) {
			synchronized (this) {
				if (certStore.expires < now) {
					try {
						certStore = new CertStore();
					} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | NoSuchProviderException e) {
						logger.error("Can't load KeyStore", e);
					}
				}
			}
		}
		return certStore;
	}
	
	public String getKeyStoreType() {
		return keyStoreType;
	}
	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}
	public String getKeyStoreProvider() {
		return keyStoreProvider;
	}
	public void setKeyStoreProvider(String keyStoreProvider) {
		this.keyStoreProvider = keyStoreProvider;
	}
	public String getKeyStoreFilePath() {
		return keyStoreFilePath;
	}
	public void setKeyStoreFilePath(String keyStoreFilePath) {
		this.keyStoreFilePath = keyStoreFilePath;
	}
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	public Map<String, String> getKeyPasswordMap() {
		return keyPasswordMap;
	}
	public void setKeyPasswordMap(Map<String, String> keyPasswordMap) {
		this.keyPasswordMap = keyPasswordMap;
	}
	public Integer getKeyStoreReloadIntervalMinutes() {
		return keyStoreReloadIntervalMinutes;
	}
	public void setKeyStoreReloadIntervalMinutes(Integer keyStoreReloadIntervalMinutes) {
		this.keyStoreReloadIntervalMinutes = keyStoreReloadIntervalMinutes;
	}

	@Override
	public X509Certificate getCertificate(String mailAddress) {
		CertStore cs = getStore();
		if (cs != null) {
			try {
				X509Certificate c = (X509Certificate) cs.store.getCertificate(mailAddress);
				c.checkValidity();
				return c;
			} catch (CertificateExpiredException e) {
				logger.warn("Certificate:" + mailAddress + " is expired.");
			} catch (CertificateNotYetValidException e) {
				logger.warn("Certificate:" + mailAddress + " is not yet valid.");
			} catch (KeyStoreException e) {
				logger.error("Can't get Certificate:" + mailAddress + " cause:" + e.getMessage(), e);
			}
		}
		return null;
	}
	@Override
	public CertificateKeyPair getCertificateKeyPair(String mailAddress, String keyPass) {
		CertStore cs = getStore();
		if (cs != null) {
			if (keyPass == null) {
				if (keyPasswordMap != null) {
					keyPass = keyPasswordMap.get(mailAddress);
				}
				if (keyPass == null) {
					keyPass = keyStorePassword;
				}
			}
			
			try {
				X509Certificate c = (X509Certificate) cs.store.getCertificate(mailAddress);
				c.checkValidity();
				try {
					Key k = cs.store.getKey(mailAddress, keyPass.toCharArray());
					if (k instanceof PrivateKey) {
						return new CertificateKeyPair(c, (PrivateKey) k);
					}
				} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
					logger.error("PrivateKey:" + mailAddress + " can't recover by password.", e);
				}
			} catch (CertificateExpiredException e) {
				logger.warn("Certificate:" + mailAddress + " is expired.");
			} catch (CertificateNotYetValidException e) {
				logger.warn("Certificate:" + mailAddress + " is not yet valid.");
			} catch (KeyStoreException e) {
				logger.error("Can't get Certificate:" + mailAddress + " cause:" + e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public void inited() {
		if (keyStoreType != null) {
			try {
				certStore = new CertStore();
			} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | NoSuchProviderException e) {
				throw new ServiceConfigrationException("Cant load KeyStore", e);
			}
		}
	}
	
	@Override
	public void destroyed() {
		certStore = null;
	}
	
}
