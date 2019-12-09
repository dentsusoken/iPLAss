package org.iplass.mtp.impl.webhook.template;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.definition.WebHookContent;
import org.iplass.mtp.webhook.template.definition.WebHookContent.webHookContentType;
public class MetaWebHookContent implements MetaData {
	
	private static final long serialVersionUID = -619523045085876676L;

	/** webHook 内容のタイプ */
	private webHookContentType contentType;
	private String charset;
	
	/** String content container */
	private String content;
	
	public MetaWebHookContent() {
	}
	public MetaWebHookContent(String charset, webHookContentType contentType, String content) {
		this.charset = charset;
		this.contentType = contentType;
		this.content = content;
	}

	public webHookContentType getContentType() {
		return contentType;
	}

	public void setContentType(webHookContentType contentType) {
		this.contentType = contentType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public MetaWebHookContent copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	//Definition → Meta
	public void applyConfig(WebHookContent definition) {
		this.content = definition.getContent();
		this.charset = definition.getCharset();
		this.contentType = definition.getContentType();
	}

	//Meta → Definition
	public WebHookContent currentConfig() {
		WebHookContent definition = new WebHookContent();
		definition.setContent(this.content);
		definition.setCharset(this.charset);
		definition.setContentType(this.contentType);
		return definition;
	}

}
