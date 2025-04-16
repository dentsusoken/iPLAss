/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.webauthn;

import java.security.cert.TrustAnchor;
import java.util.Set;
import java.util.stream.Collectors;

import com.webauthn4j.anchor.TrustAnchorRepository;
import com.webauthn4j.data.attestation.authenticator.AAGUID;
import com.webauthn4j.metadata.MetadataStatementRepository;

class TrustAnchorRepositoryImpl implements TrustAnchorRepository {
	
	private MetadataStatementRepository metadataStatementRepository;

	TrustAnchorRepositoryImpl(MetadataStatementRepository metadataStatementRepository) {
		this.metadataStatementRepository = metadataStatementRepository;
	}

	@Override
	public Set<TrustAnchor> find(AAGUID aaguid) {
		return metadataStatementRepository.find(aaguid).stream()
				.flatMap(item -> item.getAttestationRootCertificates().stream())
				.map(item -> new TrustAnchor(item, null))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<TrustAnchor> find(byte[] attestationCertificateKeyIdentifier) {
		return metadataStatementRepository.find(attestationCertificateKeyIdentifier).stream()
				.flatMap(item -> item.getAttestationRootCertificates().stream())
				.map(item -> new TrustAnchor(item, null))
				.collect(Collectors.toSet());
	}

}
