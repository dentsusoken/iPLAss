package org.iplass.mtp.impl.webhook.template;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webhook.template.definition.WebHookContent;
public class MetaWebHookContent implements MetaData {
	
	private static final long serialVersionUID = -619523045085876676L;

	/** webHook 内容のタイプ */
	private String charset;
	
	/** String content container */
	private String content;
	
	public MetaWebHookContent() {
	}
	public MetaWebHookContent(String charset, String content) {
		this.charset = charset;
		this.content = content;
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
	}

	//Meta → Definition
	public WebHookContent currentConfig() {
		WebHookContent definition = new WebHookContent();
		definition.setContent(this.content);
		definition.setCharset(this.charset);
		return definition;
	}

}
