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
package org.iplass.mtp.impl.auth.authenticate.webauthn.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.webauthn4j.data.attestation.statement.AttestationStatement;

class AttestationStatementEnvelope {

	@JsonProperty("attStmt")
	@JsonTypeInfo(
			use = JsonTypeInfo.Id.NAME,
			include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
			property = "fmt"
	)
	private AttestationStatement attestationStatement;

	@JsonCreator
	public AttestationStatementEnvelope(@JsonProperty("attStmt") AttestationStatement attestationStatement) {
		this.attestationStatement = attestationStatement;
	}

	@JsonProperty("fmt")
	public String getFormat() {
		return attestationStatement.getFormat();
	}

	public AttestationStatement getAttestationStatement() {
		return attestationStatement;
	}
}