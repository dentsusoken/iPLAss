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

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;

import com.webauthn4j.data.attestation.authenticator.AAGUID;
import com.webauthn4j.metadata.DefaultMetadataStatementRepository;
import com.webauthn4j.metadata.LocalFileMetadataBLOBProvider;
import com.webauthn4j.metadata.LocalFilesMetadataStatementsProvider;
import com.webauthn4j.metadata.MetadataBLOBBasedMetadataStatementRepository;
import com.webauthn4j.metadata.MetadataStatementRepository;
import com.webauthn4j.metadata.data.statement.MetadataStatement;

public class CacheableMetadataStatementRepository implements MetadataStatementRepository, ServiceInitListener<WebAuthnService> {
	private static final String CACHE_NAMESPACE = "mtp.auth.webauthn.metadataStatementStore";

	private String metadataBLOBFilePath;
	private boolean notFidoCertifiedAllowed = false;
	private boolean selfAssertionSubmittedAllowed = false;
	private List<String> individualMetadataStatementFilePath;

	private CacheStore cacheStore;
	private MetadataBLOBBasedMetadataStatementRepository metadataBLOBBasedMetadataStatementRepository;
	private DefaultMetadataStatementRepository defaultMetadataStatementRepository;

	public String getMetadataBLOBFilePath() {
		return metadataBLOBFilePath;
	}

	public void setMetadataBLOBFilePath(String metadataBLOBFilePath) {
		this.metadataBLOBFilePath = metadataBLOBFilePath;
	}

	public boolean isNotFidoCertifiedAllowed() {
		return notFidoCertifiedAllowed;
	}

	public void setNotFidoCertifiedAllowed(boolean notFidoCertifiedAllowed) {
		this.notFidoCertifiedAllowed = notFidoCertifiedAllowed;
	}

	public boolean isSelfAssertionSubmittedAllowed() {
		return selfAssertionSubmittedAllowed;
	}

	public void setSelfAssertionSubmittedAllowed(boolean selfAssertionSubmittedAllowed) {
		this.selfAssertionSubmittedAllowed = selfAssertionSubmittedAllowed;
	}

	public List<String> getIndividualMetadataStatementFilePath() {
		return individualMetadataStatementFilePath;
	}

	public void setIndividualMetadataStatementFilePath(List<String> individualMetadataStatementFilePath) {
		this.individualMetadataStatementFilePath = individualMetadataStatementFilePath;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<MetadataStatement> find(AAGUID aaguid) {
		CacheEntry e = cacheStore.computeIfAbsent(aaguid, key -> {
			if (defaultMetadataStatementRepository != null) {
				Set<MetadataStatement> metadataStatements = defaultMetadataStatementRepository.find(aaguid);
				if (metadataStatements != null && !metadataStatements.isEmpty()) {
					return new CacheEntry(key, metadataStatements);
				}
			}
			if (metadataBLOBBasedMetadataStatementRepository != null) {
				Set<MetadataStatement> metadataStatements = metadataBLOBBasedMetadataStatementRepository.find(aaguid);
				if (metadataStatements != null && !metadataStatements.isEmpty()) {
					return new CacheEntry(key, metadataStatements);
				}
			}
			//Empty Entry
			return new CacheEntry(key, Collections.emptySet());
		});

		return (Set<MetadataStatement>) e.getValue();
	}

	@Override
	public Set<MetadataStatement> find(byte[] attestationCertificateKeyIdentifier) {
		//currently not supported
		throw new UnsupportedOperationException();
	}

	@Override
	public void inited(WebAuthnService service, Config config) {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		cacheStore = cs.getCache(CACHE_NAMESPACE);
		if (metadataBLOBFilePath != null) {
			metadataBLOBBasedMetadataStatementRepository = new MetadataBLOBBasedMetadataStatementRepository(
					new LocalFileMetadataBLOBProvider(service.getObjectConverter(), Path.of(metadataBLOBFilePath)));
			metadataBLOBBasedMetadataStatementRepository.setNotFidoCertifiedAllowed(notFidoCertifiedAllowed);
			metadataBLOBBasedMetadataStatementRepository.setSelfAssertionSubmittedAllowed(selfAssertionSubmittedAllowed);
		}
		if (individualMetadataStatementFilePath != null && individualMetadataStatementFilePath.size() > 0) {
			Path[] paths = new Path[individualMetadataStatementFilePath.size()];
			for (int i = 0; i < individualMetadataStatementFilePath.size(); i++) {
				paths[i] = Path.of(individualMetadataStatementFilePath.get(i));
			}
			defaultMetadataStatementRepository = new DefaultMetadataStatementRepository(
					new LocalFilesMetadataStatementsProvider(service.getObjectConverter(), paths));
		}
	}

	@Override
	public void destroyed() {
	}

}
