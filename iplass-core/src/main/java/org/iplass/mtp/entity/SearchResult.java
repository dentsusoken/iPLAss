/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 検索結果をあらわすクラスです。
 *
 * @author K.Higuchi
 *
 */
public class SearchResult<T> implements Iterable<T>, AutoCloseable {

	/**
	 * 検索結果の読み込みモードです。
	 * EntityManagerの検索時に{@link SearchOption}にて指定可能です。
	 * デフォルトはAT_ONCEです。
	 *
	 * @author K.Higuchi
	 *
	 */
	public enum ResultMode {
		/**
		 * 一括で検索結果はメモリ上のListに読み込みます。
		 * 件数が限定される場合などに利用することを想定しています。
		 * {@link SearchResult#close()}は呼び出さずとも問題ありません。
		 */
		AT_ONCE,
		/**
		 * 逐次、1件ずつ読み込み(pull形式)を行います。
		 * Iterator#next()呼び出しで1件ずつ取得します。
		 * STREAMで利用する場合は、{@link SearchResult#close()}の呼び出しが必須です。
		 * iteratorは1度しか取得出来ないので注意してください。
		 * また、STREAMが指定された場合は、Query結果キャッシュは行われません。
		 * また、 STREAMが指定された場合は、{@link SearchOption}にてreturnStructuredEntity指定は利用できません。
		 */
		STREAM
	}

	private int totalCount;

	private List<T> list;

	public SearchResult(int totalCount, List<T> list) {
		this.totalCount = totalCount;
		this.list = list;
	}

	/**
	 * Limitで取得件数を絞っていた場合、実際の全件数を返却する。
	 * この値を取得するためには、EntityManagerの検索メソッド呼び出し時に、checkTotalCountsフラグをtrueにして検索する必要がある。
	 * このフラグをfalseで検索した場合は、totalCountの値は-1となる。
	 *
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public List<T> getList() {
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	/**
	 * 先頭の1件目のデータを取得する。
	 *
	 * @return　1件目のデータ。検索結果がない場合はnull。
	 */
	public T getFirst() {
		if (list == null) {
			return null;
		}
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 検索結果EntityListのうち、当該propertyName項目のみのリストを取得する。
	 * TがEntityの場合（EntityManager#searchEntity()）のみ当該メソッド利用可能。
	 *
	 * @param propertyName Listとして取得したいpropertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <P> List<P> getValueList(String propertyName) {
		if (list == null || list.size() == 0) {
			return Collections.emptyList();
		}
		if (!(list.get(0) instanceof Entity)) {
			throw new EntityRuntimeException("for use getValueList(propertyName), result type must Entity.");
		}
		List<P> res = new ArrayList<P>();
		for (Entity e: (List<Entity>) list) {
			res.add((P) e.getValue(propertyName));
		}
		return res;
	}

	/**
	 * 検索結果Listのうち、当該index項目のみのリストを取得する。
	 * TがObject[]の場合（EntityManager#search()）のみ当該メソッド利用可能。
	 *
	 * @param index　Listとして取得したい値のインデックス（0始まり）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <P> List<P> getValueList(int index) {
		if (list == null || list.size() == 0) {
			return Collections.emptyList();
		}
		if (!(list.get(0) instanceof Object[])) {
			throw new EntityRuntimeException("for use getValueList(index), result type must Object[].");
		}
		List<P> res = new ArrayList<P>();
		for (Object[] e: (List<Object[]>) list) {
			res.add((P) e[index]);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		if (list == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return list.iterator();
	}

	/**
	 * 当該SearchResultのResultModeを取得。
	 *
	 * @return
	 */
	public ResultMode getResultMode() {
		return ResultMode.AT_ONCE;
	}

	/**
	 * SearchResultをクローズする。
	 * {@link ResultMode#STREAM}の場合は、当該SearchResult利用後、呼び出し必須。
	 *
	 */
	@Override
	public void close() {
	}

}
