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

import java.io.IOException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.activation.CommandMap;
import jakarta.activation.MailcapCommandMap;
import jakarta.mail.Address;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.Strings;
import org.iplass.mtp.impl.mail.MailService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;


public class SmimeHandler implements ServiceInitListener<MailService> {
	
	static {
		if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
			MailcapCommandMap map = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			map.addMailcap("application/pkcs7-signature;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
			map.addMailcap("application/pkcs7-mime;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
			map.addMailcap("application/x-pkcs7-signature;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
			map.addMailcap("application/x-pkcs7-mime;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
			map.addMailcap("multipart/signed;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");
			CommandMap.setDefaultCommandMap(map);
			Security.addProvider(new BouncyCastleProvider());
		}
	}
	
	private String cmsAlgorithmName = "AES128_CBC";
	private Map<String, String> signatureAlgorithmMap;
	private SmimeCertStore certStore;
	
	private ASN1ObjectIdentifier cmsAlgorithm;
	

	@Override
	public void inited(MailService service, Config config) {
		if (certStore != null) {
			certStore.inited();
		}
		if (signatureAlgorithmMap == null) {
			signatureAlgorithmMap = new HashMap<>();
			signatureAlgorithmMap.put("RSA", "SHA256withRSA");
			signatureAlgorithmMap.put("DSA", "SHA256withDSA");
			signatureAlgorithmMap.put("EC", "SHA256withECDSA");
		}
		
		try {
			cmsAlgorithm = (ASN1ObjectIdentifier) CMSAlgorithm.class.getDeclaredField(cmsAlgorithmName).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new ServiceConfigrationException("cant get cmsAlgorithm: " + e.getMessage(), e);
		}
	}

	@Override
	public void destroyed() {
		if (certStore != null) {
			certStore.destroyed();
		}
	}
	
	public String getCmsAlgorithmName() {
		return cmsAlgorithmName;
	}
	public void setCmsAlgorithmName(String cmsAlgorithmName) {
		this.cmsAlgorithmName = cmsAlgorithmName;
	}
	public Map<String, String> getSignatureAlgorithmMap() {
		return signatureAlgorithmMap;
	}
	public void setSignatureAlgorithmMap(Map<String, String> signatureAlgorithmMap) {
		this.signatureAlgorithmMap = signatureAlgorithmMap;
	}
	public SmimeCertStore getCertStore() {
		return certStore;
	}
	public void setCertStore(SmimeCertStore certStore) {
		this.certStore = certStore;
	}
	
	public MimeMessage handle(Session session, MimeMessage message, boolean sign, String keyPass, boolean encrypt) throws MessagingException {
		if (sign) {
			message.saveChanges();
			message = sign(session, message, keyPass);
		}
		
		if (encrypt) {
			message.saveChanges();
			message = encrypt(session, message);
		}
		message.saveChanges();
		return message;
	}

	private MimeMessage sign(Session session, MimeMessage message, String keyPass) throws MessagingException {
		
		//TODO 先頭のaddressで署名するでよいか？
		Address[] from = message.getFrom();
		
		CertificateKeyPair ckp = certStore.getCertificateKeyPair(((InternetAddress) from[0]).getAddress(), keyPass);
		if (ckp == null) {
			throw new CertificateInvalidException("Valid CertificateKeyPair not found:" + ((InternetAddress) from[0]).getAddress());
		}
		
		//Sign the message
		MimeMultipart mm;
		try {
			//Create the SMIMESignedGenerator
			SMIMECapabilityVector capabilities = new SMIMECapabilityVector();
			capabilities.addCapability(cmsAlgorithm);
			
			ASN1EncodableVector attributes = new ASN1EncodableVector();
			attributes.add(new SMIMEEncryptionKeyPreferenceAttribute(
					new IssuerAndSerialNumber(
							new X500Name((ckp.getCertificate()).getIssuerDN().getName()), ckp.getCertificate().getSerialNumber())));
			attributes.add(new SMIMECapabilitiesAttribute(capabilities));
			
			SMIMESignedGenerator signer = new SMIMESignedGenerator();
			signer.addSignerInfoGenerator(
					new JcaSimpleSignerInfoGeneratorBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).setSignedAttributeGenerator(
							new AttributeTable(attributes)).build(
									signatureAlgorithmMap.get(ckp.getKey().getAlgorithm()), ckp.getKey(), ckp.getCertificate()));
			
			//Add the list of certs to the generator
			List<Object> certList = new ArrayList<>();
			certList.add(ckp.getCertificate());
			Store<?> certs = new JcaCertStore(certList);
			signer.addCertificates(certs);
			
			mm = signer.generate(message);
		} catch (CertificateEncodingException | OperatorCreationException | SMIMEException e) {
			throw new SmimeRuntimeException("can not sign to mail: " + e.getMessage(), e);
		}
		MimeMessage signedMessage = new MimeMessage(session);
		
		///Set all original MIME headers in the signed message
		Enumeration<?> headers = message.getAllHeaderLines();
		while (headers.hasMoreElements()) {
			signedMessage.addHeaderLine((String) headers.nextElement());
		}
		
		//Set the content of the signed message
		signedMessage.setContent(mm);
		
		return signedMessage;
	}
	
	private MimeMessage encrypt(Session session, MimeMessage message) throws MessagingException {
		
		//BCCに関しては現状、未サポートとする
		Address[] bcc = message.getRecipients(RecipientType.BCC);
		if (bcc != null && bcc.length > 0) {
			throw new SmimeRuntimeException("currently no support of bcc recipients with encrypted message");
		}
		
		//Create the encrypter
		SMIMEEnvelopedGenerator encrypter = new SMIMEEnvelopedGenerator();

		//Create a new MimeMessage that contains the encrypted and signed content
		MimeMessage encryptedMessage;
		try {
			for (Address recipient: message.getAllRecipients()) {
				X509Certificate cert = certStore.getCertificate(((InternetAddress) recipient).getAddress());
				if (cert == null) {
					throw new CertificateInvalidException("Valid Certificate not found:" + ((InternetAddress) recipient).getAddress());
				}
				encrypter.addRecipientInfoGenerator(
						new JceKeyTransRecipientInfoGenerator(cert).setProvider(BouncyCastleProvider.PROVIDER_NAME));
			}

			//Encrypt the message
			MimeBodyPart encryptedPart = encrypter.generate(message,
					new JceCMSContentEncryptorBuilder(cmsAlgorithm).setProvider(BouncyCastleProvider.PROVIDER_NAME).build());

			encryptedMessage = new MimeMessage(session);
			encryptedMessage.setContent(encryptedPart.getContent(), encryptedPart.getContentType());
		} catch (CertificateEncodingException | IllegalArgumentException | SMIMEException
				| CMSException | IOException e) {
			throw new SmimeRuntimeException("can not encrypt mail: " + e.getMessage(), e);
		}
		
		//Set all original MIME headers in the encrypted message
		Enumeration<?> headers = message.getAllHeaderLines();
		while (headers.hasMoreElements()) {
			String headerLine = (String) headers.nextElement();
			 //Make sure not to override any content-* headers from the original message
			if (!Strings.toLowerCase(headerLine).startsWith("content-")) {
				encryptedMessage.addHeaderLine(headerLine);
			}
		}

		return encryptedMessage;
	}
	
}
