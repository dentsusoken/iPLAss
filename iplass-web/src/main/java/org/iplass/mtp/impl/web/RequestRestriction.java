/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.ws.rs.core.MediaType;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

public class RequestRestriction implements ServiceInitListener<WebFrontendService> {
	//ファイルサイズ、ボディサイズなど、基本的にはWAF、リバースプロキシ等の前段でやる想定だが、前段でできない場合も想定しておく。
	//後から設定ファイルで制約を付け加えたい項目（特にセキュリティ上）を設定できるようにする
	//synchronizeOnSession、キャッシュ種別などはアプリの作り上の問題なので、それはここでは設定しない
	
	private String pathPattern;
	
	private List<String> allowMethods;
	private List<String> allowContentTypes;
	private CorsConfig cors;
	private Long maxBodySize;
	private Long maxFileSize;
	//MetaDataでの設定を優先するか否かのフラグ
	private boolean force;
	
	private Pattern pathPatternCompile;
	private boolean allowAllMethods;
	private HashSet<String> allowMethodsHash;
	private boolean allowAllContentTypes;
	private MediaType[] allowContentTypesMT;
	
	public RequestRestriction copy() {
		RequestRestriction copy = new RequestRestriction();
		copy.pathPattern = pathPattern;
		if (allowMethods != null) {
			copy.allowMethods = new ArrayList<>(allowMethods);
		}
		if (allowContentTypes != null) {
			copy.allowContentTypes = new ArrayList<>(allowContentTypes);
		}
		if (cors != null) {
			copy.cors = cors.copy();
		}
		copy.maxBodySize = maxBodySize;
		copy.maxFileSize = maxFileSize;
		copy.force = force;
		copy.pathPatternCompile = pathPatternCompile;
		copy.allowAllMethods = allowAllMethods;
		copy.allowMethodsHash = allowMethodsHash;
		copy.allowAllContentTypes = allowAllContentTypes;
		copy.allowContentTypesMT = allowContentTypesMT;
		
		return copy;
	}
	
	public List<String> getAllowMethods() {
		return allowMethods;
	}

	public void setAllowMethods(List<String> allowMethods) {
		this.allowMethods = allowMethods;
	}

	public List<String> getAllowContentTypes() {
		return allowContentTypes;
	}

	public void setAllowContentTypes(List<String> allowContentTypes) {
		this.allowContentTypes = allowContentTypes;
	}

	public CorsConfig getCors() {
		return cors;
	}

	public void setCors(CorsConfig cors) {
		this.cors = cors;
	}

	public Long getMaxBodySize() {
		return maxBodySize;
	}

	public void setMaxBodySize(Long maxBodySize) {
		this.maxBodySize = maxBodySize;
	}

	public Long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(Long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public String getPathPattern() {
		return pathPattern;
	}

	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;
	}
	
	public void init() {
		if (pathPattern != null) {
			if (pathPatternCompile == null
					|| !pathPatternCompile.pattern().equals(pathPattern)) {
				pathPatternCompile = Pattern.compile(pathPattern, Pattern.DOTALL);
			}
		} else {
			pathPatternCompile = null;
		}
		
		allowAllMethods = false;
		allowMethodsHash = null;
		if (allowMethods != null) {
			if (allowMethods.contains("*")) {
				allowAllMethods = true;
			}
			allowMethodsHash = new HashSet<>(allowMethods);
		}
		
		
		allowAllContentTypes = false;
		allowContentTypesMT = null;
		if (allowContentTypes != null) {
			if (allowContentTypes.contains("*/*")) {
				allowAllContentTypes = true;
			}
			allowContentTypesMT = new MediaType[allowContentTypes.size()];
			for (int i = 0; i < allowContentTypesMT.length; i++) {
				allowContentTypesMT[i] = MediaType.valueOf(allowContentTypes.get(i));
			}
		}
	}

	@Override
	public void inited(WebFrontendService service, Config config) {
		init();
	}
	
	public long maxBodySize() {
		if (maxBodySize == null) {
			return -1;
		}
		return maxBodySize.longValue();
	}
	
	public long maxFileSize() {
		if (maxFileSize == null) {
			return -1;
		}
		return maxFileSize.longValue();
	}
	
	public Pattern getPathPatternCompile() {
		return pathPatternCompile;
	}

	@Override
	public void destroyed() {
	}

	public boolean isAllowedMethod(String requestMethod) {
		if (allowAllMethods) {
			return true;
		}
		
		if (allowMethodsHash != null) {
			return allowMethodsHash.contains(requestMethod);
		}
		
		return false;
	}

	public boolean isAllowedContentType(String contentType) {
		if (allowAllContentTypes) {
			return true;
		}
		MediaType ct = MediaType.valueOf(contentType);
		if (ct.isWildcardType() || ct.isWildcardSubtype()) {
			return false;
		}
		
		if (allowContentTypesMT != null) {
			for (MediaType act: allowContentTypesMT) {
				if (ct.isCompatible(act)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAllowedContentType(MediaType contentType) {
		if (allowAllContentTypes) {
			return true;
		}
		if (contentType.isWildcardType() || contentType.isWildcardSubtype()) {
			return false;
		}
		
		if (allowContentTypesMT != null) {
			for (MediaType act: allowContentTypesMT) {
				if (contentType.isCompatible(act)) {
					return true;
				}
			}
		}
		return false;
	}
}
