package org.iplass.mtp.auth.oauth.definition;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.oauth.definition.consents.AlwaysConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.OnceConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.ScriptingConsentTypeDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * スコープ承認画面の表示有無を判断するための定義です。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	AlwaysConsentTypeDefinition.class,
	OnceConsentTypeDefinition.class,
	ScriptingConsentTypeDefinition.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class ConsentTypeDefinition implements Serializable {
	private static final long serialVersionUID = 4679081921898475535L;
}
