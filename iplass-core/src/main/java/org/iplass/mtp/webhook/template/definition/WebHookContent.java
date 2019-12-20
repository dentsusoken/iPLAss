package org.iplass.mtp.webhook.template.definition;

import java.io.Serializable;

public class WebHookContent implements Serializable {

	private static final long serialVersionUID = -344879145295664096L;

	//主にJSONが流行らしい、できれば要求された以外、JSONをおすすめします
	
	/** webHook 内容のタイプ */
	private String charset;
	
	/** String content container */
	private String content;
	
	public WebHookContent() {
		this.content = "";
	}
	

	
	public WebHookContent(String content, String charset) {
		setContent(content);
		setCharset(charset);
	}
	
	public void setContent (String content) {
		this.content = content;
	}
	
	
	public String getContent() {
		return String.valueOf(this.content);
	}
	
			
	
	

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	
	public WebHookContent copy() {
		return new WebHookContent(this.content, this.charset);
	}

}
