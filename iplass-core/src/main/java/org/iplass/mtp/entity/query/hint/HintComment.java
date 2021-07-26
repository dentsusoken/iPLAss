/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.query.QueryServiceHolder;
import org.iplass.mtp.impl.query.hint.HintCommentSyntax;

/**
 * <p>
 * EQLのヒントコメントを表現します。
 * </p>
 * <p>
 * ヒントには、EQL上でのインデックス付与に関するヒント、バインド変数化に関するヒント、キャッシュ、クエリのタイムアウト、フェッチサイズに関するヒント、データベースネイティブのヒントなどを付与することが可能です。<br>
 * ヒントコメントは、select句の後に/*+で始まり、&#42;/で終わるコメント形式で指定することが可能です。
 * </p>
 * 
 * <h5>ヒントを追加したEQL例：</h5>
 * <pre>
 * select /*+ fetch_size(100) index(c.x) no_index(a, b) native('ORDERED USE_NL_WITH_INDEX(...)')  &#42;/ a, b, c.x, d.x, d.name from SampleEntity where c.x='hoge' and a=1 and b=15
 * </pre>
 * <p>
 * また、ヒント句は外部のプロパティファイルに定義し、そのプロパティファイルからキー名を指定して読み込むことが可能です。
 * プロパティファイルのパスはQueryServiceに定義します。
 * </p>
 * <h5>プロパティファイルの記述例：</h5>
 * <pre>
 * hint1=native(q0t0, 'FORCE INDEX(obj_store__USER_ISTR_index_3)')
 * hint2=native(q0, 'FORCE INDEX(obj_store_ISTR_index_1)')
 * :
 * </pre>
 * 
 * <h5>外部ファイルからヒントを読み込むEQL例：</h5>
 * <pre>
 * select /*+ @hint(hint1) &#42;/ a, b, c.x, d.x, d.name from SampleEntity where c.x='hoge' and a=1 and b=15
 * </pre>
 * 上記設定ファイルの場合、これが、
 * <pre>
 * select /*+ native(q0t0, 'FORCE INDEX(obj_store__USER_ISTR_index_3)') &#42;/ a, b, c.x, d.x, d.name from SampleEntity where c.x='hoge' and a=1 and b=15
 * </pre>
 * と展開されます。
 * <p>
 * また、{@link Hint#externalHint(String)}で外部ファイルに定義されるHintをインスタンスとして取得可能です。
 * </p>
 * 
 * @see IndexHint
 * @see NoIndexHint
 * @see BindHint
 * @see NoBindHint
 * @see CacheHint
 * @see FetchSizeHint
 * @see TimeoutHint
 * @see SuppressWarningsHint
 * @see ReadOnlyHint
 * @see NativeHint
 * 
 * @author K.Higuchi
 *
 */
public class HintComment implements ASTNode {
	private static final long serialVersionUID = -3352104778089451781L;

	private List<Hint> hintList;
	
	public HintComment() {
	}

	public HintComment(List<Hint> hintList) {
		this.hintList = hintList;
	}
	
	public HintComment(Hint... hint) {
		if (hint != null) {
			hintList = new ArrayList<>(hint.length);
			for (Hint h: hint) {
				hintList.add(h);
			}
		}
	}
	
	public HintComment(String hint) {
		try {
			HintComment hintComment = QueryServiceHolder.getInstance().getQueryParser().parse("/*+ " + hint + " */", HintCommentSyntax.class);
			this.hintList = hintComment.hintList;
		} catch (ParseException e) {
			throw new QueryException(e.getMessage(), e);
		}
	}
	
	public List<Hint> getHintList() {
		return hintList;
	}

	public void setHintList(List<Hint> hintList) {
		this.hintList = hintList;
	}
	
	public HintComment add(Hint hint) {
		if (hintList == null) {
			hintList = new ArrayList<>();
		}
		hintList.add(hint);
		return this;
	}
	
	public HintComment add(List<Hint> hintList) {
		if (this.hintList == null) {
			this.hintList = new ArrayList<>();
		}
		this.hintList.addAll(hintList);
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hintList == null) ? 0 : hintList.hashCode());
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
		HintComment other = (HintComment) obj;
		if (hintList == null) {
			if (other.hintList != null)
				return false;
		} else if (!hintList.equals(other.hintList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("/*+ ");
		if (hintList != null) {
			for (Hint h: hintList) {
				sb.append(h).append(" ");
			}
		}
		sb.append("*/");
		return sb.toString();
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	public void accept(HintVisitor visitor) {
		if (visitor.visit(this)) {
			if (hintList != null) {
				for (Hint h: hintList) {
					h.accept(visitor);
				}
			}
		}
	}

}
