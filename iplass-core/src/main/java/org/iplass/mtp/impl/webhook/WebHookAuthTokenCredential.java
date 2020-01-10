package org.iplass.mtp.impl.webhook;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;
/**
 * 
 * @author lisf06
 */
public class WebHookAuthTokenCredential implements Credential, Serializable{

	private static final long serialVersionUID = -6802648562931555161L;
	
	private String id;
	private String token;
	private Map<String, Object> authenticationFactor;


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public Object getAuthenticationFactor(String name) {
		if (authenticationFactor == null) {
			return null;
		}
		return authenticationFactor.get(name);
	}

	@Override
	public void setAuthenticationFactor(String name, Object value) {
		if (authenticationFactor == null) {
			authenticationFactor = new HashMap<String, Object>();
		}
		authenticationFactor.put(name, value);
	}
	
	

}
