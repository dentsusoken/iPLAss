/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
 * CahceScope.GLOBAL、CahceScope.GLOBAL_KEEP、CahceScope.GLOBAL_RELOADの場合は、有効期間（秒）を指定することが可能です。
 * 有効期間（秒）未指定の場合は無期限（デフォルトは無期限に設定。ただし、バックエンドで利用しているCacheStoreの設定で有効期限を設定している場合は、その限り）となります。
 * CacheScope.GLOBAL利用する際は、Entity定義にてqueryCacheを有効化した上で、EQLのヒント句をつける必要があります。<br>
 * EQLでの記述例： <br>
 * select /*+ cache &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * や、<br>
 * select /*+ cache(transaction) &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * や、<br>
 * select /*+ cache(60) &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * select /*+ cache(keep, 60) &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * select /*+ cache(reload, 60) &#42;/ a, b from SampleEntity where c.x='hoge' and a=1 and b=15<br>
 * 
 * @author K.Higuchi
 *
 */
public class CacheHint extends EQLHint {
	private static final long serialVersionUID = -2368342891687154508L;
	
	/**
	 * キャッシュのスコープを表す列挙型です。
	 */
	public enum CacheScope {
		/**
		 * 同一トランザクション内でのみキャッシュを保持します
		 */
		TRANSACTION,
		/**
		 * 共有キャッシュとしてキャッシュを保持します。
		 * キャッシュしてるEntityデータに更新があった場合、キャッシュは破棄されます。
		 */
		GLOBAL,
		/**
		 * 共有キャッシュとしてキャッシュを保持します。
		 * キャッシュしてるEntityデータに更新があった場合でもキャッシュは（更新前の状態で）保持されます。
		 */
		GLOBAL_KEEP,
		/**
		 * 共有キャッシュとしてキャッシュを保持します。
		 * キャッシュしてるEntityデータに更新があった場合でもキャッシュは（更新前の状態で）保持されます。
		 * 有効期間（秒）を指定し、その期間経過後に同一EQLにてキャッシュを自動的にリロードします。
		 */
		GLOBAL_RELOAD
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
		if (scope != null) {
			switch (scope) {
			case TRANSACTION:
				return "cache(transaction)";
			case GLOBAL:
				if (ttl > 0) {
					return "cache(" + ttl + ")";
				} else {
					return "cache";
				}
			case GLOBAL_KEEP:
				if (ttl > 0) {
					return "cache(keep, " + ttl + ")";
				} else {
					return "cache(keep)";
				}
			case GLOBAL_RELOAD:
				if (ttl > 0) {
					return "cache(reload, " + ttl + ")";
				} else {
					return "cache(reload)";
				}
			}
		}
		return "cache";
	}

}
