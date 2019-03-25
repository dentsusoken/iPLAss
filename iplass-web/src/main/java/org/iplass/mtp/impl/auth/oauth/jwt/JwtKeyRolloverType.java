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
package org.iplass.mtp.impl.auth.oauth.jwt;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWTの署名に利用するキーのロールオーバー方式に関する設定。
 * 
 * @author K.Higuchi
 *
 */
public enum JwtKeyRolloverType {
	
	/**
	 * 古いキーを有効期間が切れるまで使い続ける方式
	 */
	OLDER {
		@Override
		CertificateKeyPair select(SimpleJwtKeyStore jwtKeyStore) {
			CertificateKeyPair candidate = null;
			for (CertificateKeyPair pair: jwtKeyStore.list()) {
				try {
					pair.getCertificate().checkValidity();
					if (candidate == null || candidate.getCertificate().getNotAfter().getTime() > pair.getCertificate().getNotAfter().getTime()) {
						candidate = pair;
					}
				} catch (CertificateExpiredException | CertificateNotYetValidException e) {
					//ignore
				}
			}
			return candidate;
		}
	},
	/**
	 * 有効期間の切れるn日前に新しいキーに切り替える方式
	 */
	BEFORE_N_DAYS {
		@Override
		CertificateKeyPair select(SimpleJwtKeyStore jwtKeyStore) {
			CertificateKeyPair candidateOldest = null;
			CertificateKeyPair candidateNewest = null;
			
			for (CertificateKeyPair pair: jwtKeyStore.list()) {
				try {
					pair.getCertificate().checkValidity();
					
					if (candidateOldest == null || candidateOldest.getCertificate().getNotAfter().getTime() > pair.getCertificate().getNotAfter().getTime()) {
						candidateOldest = pair;
					}
					if (candidateNewest == null || candidateNewest.getCertificate().getNotAfter().getTime() < pair.getCertificate().getNotAfter().getTime()) {
						candidateNewest = pair;
					}

				} catch (CertificateExpiredException | CertificateNotYetValidException e) {
					//ignore
				}
			}
			
			CertificateKeyPair candidate = null;
			if (candidateOldest == candidateNewest) {
				candidate = candidateOldest;
			} else if (candidateOldest != null) {
				Instant now = Instant.now();
				Date nowPlus = new Date(now.plus(jwtKeyStore.getRollOverDaysBeforeExpire(), ChronoUnit.DAYS).toEpochMilli());
				
				try {
					candidateOldest.getCertificate().checkValidity(nowPlus);
					candidate = candidateOldest;
				} catch (CertificateExpiredException | CertificateNotYetValidException e) {
					candidate = candidateNewest;
				}
			}
			return candidate;
		}
	},
	/**
	 * 新しいキーの有効期間に到達したら即座に切り替える方式
	 */
	NEWER {
		@Override
		CertificateKeyPair select(SimpleJwtKeyStore jwtKeyStore) {
			CertificateKeyPair candidate = null;
			for (CertificateKeyPair pair: jwtKeyStore.list()) {
				try {
					pair.getCertificate().checkValidity();
					if (candidate == null || candidate.getCertificate().getNotAfter().getTime() < pair.getCertificate().getNotAfter().getTime()) {
						candidate = pair;
					}
				} catch (CertificateExpiredException | CertificateNotYetValidException e) {
					//ignore
				}
			}
			return candidate;
		}
	};
	
	abstract CertificateKeyPair select(SimpleJwtKeyStore jwtKeyStore);

}
