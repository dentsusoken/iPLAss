/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.entity.query.hint;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;

/**
 * EQLの実行結果のキャッシュを行うヒント句です。
 * キャッシュのスコープ、キャッシュの有効期間（秒）を指定可能です。<br>
 * CahceScopeがTRANSACTIONの場合は、同一トランザクション内のみ当該キャッシュが有効です。
 * CahceScopeがGLOBAL（デフォルト）の場合は、共有キャッシュとなります。
 * CahceScope.GLOBALの場合は、有効期間（秒）を指定することが可能です。
 * 有効期間（秒）未指定の場合は無期限（デフォルトは無期限に設定。ただし、バックエンドで利用しているCacheStoreの設定で有効期限を設定している場合は、その限り）となります。
 * CacheScope.GLOBAL利用する際は、Entity定義にてqueryCacheを有効化した上で、EQLのヒント句をつける必要があります。<br>
 * EQLでの記述例： <br>
 * select /*+ cache &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * や、<br>
 * select /*+ cache(transaction) &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * や、<br>
 * select /*+ cache(60) &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * 
 * @author K.Higuchi
 *
 */
public class CacheHint extends EQLHint {
	private static final long serialVersionUID = -2368342891687154508L;
	
	public enum CacheScope {
		TRANSACTION,
		GLOBAL
	}

	private CacheScope scope = CacheScope.GLOBAL;
	private int ttl;
	
	public CacheHint() {
	}
	
	public CacheHint(CacheScope scope) {
		this.scope = scope;
	}
	
	public CacheHint(CacheScope scope, int ttl) {
		this.scope = scope;
		this.ttl = ttl;
	}

	public CacheScope getScope() {
		return scope;
	}

	/**
	 * キャッシュのスコープを指定。TRANSACTION or GLOBALを指定可能。
	 * デフォルトはGLOBAL。
	 * @param scope
	 */
	public void setScope(CacheScope scope) {
		this.scope = scope;
	}

	public int getTTL() {
		return ttl;
	}

	/**
	 * CacheScope.GLOBALの場合のキャッシュの有効期間（秒）を指定。
	 * 未指定、0以下の場合は無限。
	 * 
	 * @param ttl
	 */
	public void setTTL(int ttl) {
		this.ttl = ttl;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(HintVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ttl;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheHint other = (CacheHint) obj;
		if (scope != other.scope)
			return false;
		if (ttl != other.ttl)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (scope == CacheScope.TRANSACTION) {
			return "cache(transaction)";
		}
		if (ttl > 0) {
			return "cache(" + ttl + ")";
		}
		return "cache";
	}

}
