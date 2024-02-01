/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.jwt;

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
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * KeyStoreベースのJwtKeyStore実装。
 * KeyStoreに格納されている鍵を利用してJWTの署名に利用。
 * aliasがkidとして利用される。
 * KeyStoreには、複数の鍵を格納可能。
 * 鍵ペア生成時に生成される公開鍵証明書の有効期間を利用して、
 * ロールオーバーさせることが可能。
 * 
 * @author K.Higuchi
 *
 */
public class SimpleJwtKeyStore implements JwtKeyStore {
	private static Logger logger = LoggerFactory.getLogger(SimpleJwtKeyStore.class);
	
	private String keyStoreType = "PKCS12";
	private String keyStoreProvider;
	private String keyStoreFilePath;
	private String keyStorePassword;
	private Map<String, String> keyPasswordMap;
	private Integer keyStoreReloadIntervalMinutes;
	private JwtKeyRolloverType rollOverType = JwtKeyRolloverType.OLDER;
	private long rollOverDaysBeforeExpire;
	
	private volatile CertStore certStore;
	
	private class CertStore {
		private final long expires;
		private final KeyStore store;
		private final List<String> aliases;

		private CertStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException {
			
			KeyStore ks;
			if (keyStoreProvider == null) {
				ks = KeyStore.getInstance(keyStoreType);
			} else {
				ks = KeyStore.getInstance(keyStoreType, keyStoreProvider);
			}
			if (keyStoreFilePath != null) {
				logger.debug("load keyStore: " + keyStoreFilePath);
				try (InputStream is = inputStreamFromFile();
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
			
			List<String> al = new ArrayList<>();
			Enumeration<String> en = ks.aliases();
			while (en.hasMoreElements()) {
				String name = en.nextElement();
				Certificate c = ks.getCertificate(name);
				if (c instanceof X509Certificate) {
					X509Certificate cert = (X509Certificate) c;
					boolean valid = false;
					try {
						cert.checkValidity();
						valid = true;
					} catch (CertificateExpiredException e) {
						//ignore
						logger.warn("JwtKey:" + name + " is expired.");
					} catch(CertificateNotYetValidException e) {
						valid = true;
					}

					if (valid) {
						try {
							String keyPass = null;
							if (keyPasswordMap != null) {
								keyPass = keyPasswordMap.get(name);
							}
							if (keyPass == null) {
								keyPass = keyStorePassword;
							}
							
							Key k = ks.getKey(name, keyPass.toCharArray());
							if (k instanceof PrivateKey) {
								al.add(name);
							}
						} catch (UnrecoverableKeyException e) {
							logger.error("JwtKey(PrivateKey):" + name + " can't recover by password.", e);
						}
					}
				}
			}
			
			if (keyStoreReloadIntervalMinutes != null && keyStoreReloadIntervalMinutes > 0) {
				expires = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(keyStoreReloadIntervalMinutes);
			} else {
				expires = Long.MAX_VALUE;
			}
			
			aliases = al;
			
			if (aliases.size() == 0) {
				logger.warn("Currently non expired JwtKey does not exists.");
			}
		}
	}
	
	protected InputStream inputStreamFromFile() throws IOException {
		InputStream is = getClass().getResourceAsStream(keyStoreFilePath);
		if (is == null) {
			is = new FileInputStream(keyStoreFilePath);
		}
		return is;
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
	
	public JwtKeyRolloverType getRollOverType() {
		return rollOverType;
	}

	public void setRollOverType(JwtKeyRolloverType rollOverType) {
		this.rollOverType = rollOverType;
	}

	public long getRollOverDaysBeforeExpire() {
		return rollOverDaysBeforeExpire;
	}

	public void setRollOverDaysBeforeExpire(long rollOverDaysBeforeExpire) {
		this.rollOverDaysBeforeExpire = rollOverDaysBeforeExpire;
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
	public CertificateKeyPair getCertificateKeyPair() {
		return rollOverType.select(this);
	}
	
	private CertificateKeyPair getCertificateKeyPair(String keyId, CertStore cs) throws CertificateExpiredException, CertificateNotYetValidException {
		String keyPass = null;
		if (cs != null) {
			if (keyPasswordMap != null) {
				keyPass = keyPasswordMap.get(keyId);
			}
			if (keyPass == null) {
				keyPass = keyStorePassword;
			}
		}
			
		try {
			X509Certificate c = (X509Certificate) cs.store.getCertificate(keyId);
			try {
				Key k = cs.store.getKey(keyId, keyPass.toCharArray());
				if (k instanceof PrivateKey) {
					return new CertificateKeyPair(keyId, c, (PrivateKey) k);
				}
			} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
				logger.error("JwtKey:" + keyId + " can't recover by password.", e);
			}
		} catch (KeyStoreException e) {
			logger.error("Can't get JwtKey:" + keyId + " cause:" + e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public List<CertificateKeyPair> list() {
		CertStore cs = getStore();
		if (cs != null) {
			List<CertificateKeyPair> list = new ArrayList<>();
			for (String keyId: cs.aliases) {
				CertificateKeyPair pair = null;
				try {
					pair = getCertificateKeyPair(keyId, cs);
					pair.getCertificate().checkValidity();
				} catch (CertificateExpiredException e) {
					pair = null;
					logger.debug("JwtKey:" + keyId + " is expired.");
				} catch (CertificateNotYetValidException e) {
					//今後利用されうるので結果として含めて返す
					logger.debug("JwtKey:" + keyId + " is not yet valid.");
				}
				
				if (pair != null) {
					list.add(pair);
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public void destroyed() {
		certStore = null;
	}

	@Override
	public void inited(OAuthAuthorizationService service, Config config) {
		if (keyStoreType != null) {
			try {
				certStore = new CertStore();
			} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | NoSuchProviderException e) {
				throw new ServiceConfigrationException("Cant load KeyStore", e);
			}
		}
	}
	
}
