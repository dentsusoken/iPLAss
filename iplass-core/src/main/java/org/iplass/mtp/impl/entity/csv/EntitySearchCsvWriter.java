/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.csv.EntityWriteOption.SearchQueryCsvContext;
import org.iplass.mtp.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>EntityのCsvファイル出力クラス。Upload可能な形式で出力を行います。</p>
 */
public class EntitySearchCsvWriter implements AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(EntitySearchCsvWriter.class);

	private final OutputStream out;
	private final String definitionName;
	private final EntityWriteOption option;
	private final ZipOutputStream binaryStore;

	private final EntityManager em;
	private final EntityDefinitionManager edm;

	private EntityCsvWriter writer;

	public EntitySearchCsvWriter(OutputStream out, String definitionName) {
		this(out, definitionName, new EntityWriteOption(), null);
	}

	public EntitySearchCsvWriter(OutputStream out, String definitionName, EntityWriteOption option) {
		this(out, definitionName, option, null);
	}

	public EntitySearchCsvWriter(OutputStream out, String definitionName, EntityWriteOption option, ZipOutputStream binaryStore) {
		this.out = out;
		this.definitionName = definitionName;
		this.option = option;
		this.binaryStore = binaryStore;

		em = ManagerLocator.manager(EntityManager.class);
		edm = ManagerLocator.manager(EntityDefinitionManager.class);
	}

	/**
	 * CSVを出力します。
	 *
	 * @return 処理件数
	 * @throws IOException
	 */
	public int write() throws IOException {

		EntityDefinition ed = edm.get(definitionName);

		//Writer生成
		if (writer != null) {
			close();
		}
		writer = new EntityCsvWriter(ed, out, option, binaryStore);

		//多重度複数のReferenceが存在するか
		boolean hasMultiReference = hasMultiReference(writer.getProperties());

		Query query = createQuery(ed, writer.getProperties(), hasMultiReference);

		//Header出力
		writer.writeHeader();

		// CSV レコードを出力
		return searchEntity(writer, ed, query, hasMultiReference);
	}

	@Override
	public void close() {
		if (writer != null) {
			writer.close();
			writer = null;
		}
	}

	/**
	 * 出力プロパティに多重度複数のReferenceが存在するかを判定します。
	 *
	 * @param properties 出力対象プロパティ
	 * @return 多重度複数のReferenceが存在するか
	 */
	private boolean hasMultiReference(final List<PropertyDefinition> properties) {

		return properties.stream()
				.filter(property ->property instanceof ReferenceProperty)
				.anyMatch(property -> property.getMultiplicity() != 1);
	}

	/**
	 * Queryを生成します。
	 *
	 * @param ed Entity定義
	 * @param properties 出力対象プロパティ
	 * @param hasMultiReference 多重度複数のReferenceを含むか
	 * @return Query
	 */
	private Query createQuery(final EntityDefinition ed, final List<PropertyDefinition> properties, final boolean hasMultiReference) {

		final List<String> select = new ArrayList<>();

		if (hasMultiReference) {
			//OIDとVERSIONのみ。後でLoadする
			select.add(Entity.OID);
			select.add(Entity.VERSION);
		} else {
			properties.forEach(property -> {
				String propName = property.getName();
				if (property instanceof ReferenceProperty) {
					select.add(propName + "." + Entity.OID);
					select.add(propName + "." + Entity.VERSION);
				} else {
					select.add(propName);
				}
			});
		}

		Query query = new Query()
				.select(select.toArray(new Object[select.size()]))
				.from(ed.getName());
		query.setWhere(option.getWhere());
		query.setOrderBy(option.getOrderBy());

		if (ed.getVersionControlType() != VersionControlType.NONE) {
			query.versioned(option.isVersioned());
		} else {
			query.versioned(false);
		}

		if (option.getLimit() > 0) {
			query.setLimit(new Limit(option.getLimit()));
		}

		return query;
	}

	/**
	 * Queryを検証してCSVを出力します。
	 *
	 * @param writer EntityCsvWriter
	 * @param ed Entity定義
	 * @param query Query
	 * @param hasMultiReference 多重度複数のReferenceを含むか
	 * @return 処理件数
	 */
	private int searchEntity(final EntityCsvWriter writer, final EntityDefinition ed, final Query query, final boolean hasMultiReference) {

		final SearchQueryCsvContext context = option.getBeforeSearch().apply(query);
		final Query optQuery = context.getQuery();

		//where条件によるdistinctチェック
		if (!optQuery.getSelect().isDistinct()) {
			//distinctが付いていない場合、Where条件に多重度複数のReferenceが含まれているかをチェック
			if (new MultiReferenceChecker(ed).test(optQuery.getWhere())) {
				//含まれている場合はdistinct指定
				optQuery.getSelect().setDistinct(true);
				logger.debug("specified distinct for select. because [where conditions] contains multiple reference property. query:" + optQuery);

				//orderbyはクリア
				if (optQuery.getOrderBy() != null) {
					logger.debug("query [order by] removed. because [where conditions] contains multiple reference property. removed order by:" + optQuery.getOrderBy());
					optQuery.setOrderBy(null);
				}
			}
		}

		//orderby条件の多重度チェック
		if (optQuery.getOrderBy() != null) {
			if (new MultiReferenceChecker(ed).test(optQuery.getOrderBy())) {
				//多重度が複数のReferenceは指定不可(複数行出力されるが、distinctできないため)
				logger.debug("query [order by] removed. because [order by] contains multiple reference property. removed order by:" + optQuery.getOrderBy());
				optQuery.setOrderBy(null);
			}
		}

		//Limit指定かつOrderBy未指定のチェック（SQLServer対応）
		if (optQuery.getLimit() != null && optQuery.getOrderBy() == null) {
			if (option.isMustOrderByWithLimit()) {
				logger.debug("query [order by] `oid desc` is specified. because mustOrderByWithLimit is true.");
				optQuery.order(new SortSpec(Entity.OID, SortType.DESC));
			}
		}

		if (context.isDoPrivileged()) {
			return AuthContext.doPrivileged(() -> doSearch(writer, optQuery, hasMultiReference));
		} else {
			if (context.getWithoutConditionReferenceName() != null) {
				return EntityPermission.doQueryAs(context.getWithoutConditionReferenceName(),
						() -> doSearch(writer, optQuery, hasMultiReference));
			}
		}
		return doSearch(writer, optQuery, hasMultiReference);
	}

	private int doSearch(final EntityCsvWriter writer, final Query optQuery, final boolean hasMultiReference) {

		// プロパティを直接指定している場合の対象Reference取得
		final List<String> directReferenceNames = getDirectReferenceNames(hasMultiReference, writer.getProperties());

		final int[] count = new int[1];

		em.searchEntity(optQuery, new Predicate<Entity>() {

			@Override
			public boolean test(Entity entity) {

				//多重度複数のReferenceがある場合はLoadする
				//TODO パフォーマンス改善 oid単位にロードしないように
				if (hasMultiReference) {
					LoadOption loadOption = new LoadOption();
					if (CollectionUtil.isNotEmpty(option.getProperties())) {
						// プロパティを直接指定している場合は対象のReferenceのみ指定
						loadOption.setLoadReferences(directReferenceNames);
					} else {
						loadOption.setWithMappedByReference(option.isWithMappedByReference());
					}

					entity = em.load(entity.getOid(), entity.getVersion(), entity.getDefinitionName(), loadOption);
				}

				option.getAfterSearch().accept(optQuery.copy(), entity);

				writer.writeEntity(entity);

				count[0] = count[0] + 1;
				return true;
			}
		});

		return count[0];
	}

	/**
	 * 直接プロパティ指定時の参照プロパティを返します。
	 *
	 * @param hasMultiReference 多重度複数の参照が含まれるか
	 * @param properties 出力対象プロパティ
	 * @return 出力対象参照プロパティ名
	 */
	private List<String> getDirectReferenceNames(boolean hasMultiReference, List<PropertyDefinition> properties) {
		if (!hasMultiReference || CollectionUtil.isEmpty(option.getProperties())) {
			// 対象外
			return null;
		}
		return properties.stream()
				.filter(property -> property instanceof ReferenceProperty)
				.map(property -> property.getName())
				.collect(Collectors.toList());
	}

	private static class MultiReferenceChecker extends QueryVisitorSupport {

		private EntityDefinition ed;
		private boolean hasMultiReference;

		public MultiReferenceChecker(EntityDefinition ed) {
			this.ed = ed;
		}

		public boolean test(Where where) {
			hasMultiReference = false;
			if (where != null && where.getCondition() != null) {
				where.getCondition().accept(this);
			}
			return hasMultiReference;
		}
		public boolean test(OrderBy order) {
			hasMultiReference = false;
			if (order != null) {
				order.accept(this);
			}
			return hasMultiReference;
		}

		@Override
		public boolean visit(EntityField entityField) {
			if (entityField.getPropertyName().contains(".")) {
				//Referenceの場合
				PropertyDefinition pd = ed.getProperty(entityField.getPropertyName().substring(0, entityField.getPropertyName().indexOf(".")));
				if (pd != null && pd instanceof ReferenceProperty) {
					hasMultiReference = ((ReferenceProperty)pd).getMultiplicity() != 1;
				}
			}
			//既に含まれていれば以降をスキップするためfalseを返す
			return !hasMultiReference;
		}

	}

}
