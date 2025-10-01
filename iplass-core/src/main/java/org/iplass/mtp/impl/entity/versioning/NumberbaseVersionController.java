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

package org.iplass.mtp.impl.entity.versioning;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityApplicationException;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.definition.properties.VersionControlReferenceType;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.AsOf.AsOfSpec;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;


public class NumberbaseVersionController implements VersionController {

	static final Long VER_ZERO = Long.valueOf(0);

	static class PropertyUnnester extends ASTTransformerSupport {

		private PropertyUnnester parent;

		@Override
		public ASTNode visit(EntityField entityField) {
			if (parent != null) {
				//サブクエリ内の項目
				int unnestCount = entityField.unnestCount();
				PropertyUnnester target = this;
				for (int i = 0; i < unnestCount; i++) {
					target = target.parent;
					if (target == null) {
						throw new QueryException("can't correlate outer query on :" + entityField);
					}
				}
				if (target.parent == null) {
					//topレベルのクエリとの結合と判断し、.を付与
					return new EntityField("." + entityField.getPropertyName(), entityField.getArrayIndex());
				} else {
					return new EntityField(entityField.getPropertyName(), entityField.getArrayIndex());
				}
			} else {
				return new EntityField("." + entityField.getPropertyName(), entityField.getArrayIndex());
			}
		}

		@Override
		public ASTNode visit(SubQuery subQuery) {

			Query q = subQuery.getQuery().copy();
			Condition on = null;
			if (subQuery.getOn() != null) {
				PropertyUnnester sub = new PropertyUnnester();
				sub.parent = this;
				on = (Condition) subQuery.getOn().accept(sub);
			}

			return new SubQuery(q, on);
		}
	}

	static class RefAdder extends ASTTransformerSupport {

		private String prefixRefPath;
		private RefAdder parent;

		public RefAdder(String prefixRefPath) {
			this.prefixRefPath = prefixRefPath;
		}

		@Override
		public ASTNode visit(EntityField entityField) {
			if (parent != null) {
				//サブクエリ内の項目
				int unnestCount = entityField.unnestCount();
				RefAdder target = this;
				for (int i = 0; i < unnestCount; i++) {
					target = target.parent;
					if (target == null) {
						throw new QueryException("can't correlate outer query on :" + entityField);
					}
				}
				if (target.parent == null) {
					//topレベルのクエリとの結合と判断
					if (prefixRefPath == null) {
						return new EntityField(entityField.getPropertyName());
					} else {
						if (SubQuery.THIS.equalsIgnoreCase(entityField.getPropertyName())) {
							return new EntityField(prefixRefPath);
						} else {
							return new EntityField(prefixRefPath + "." + entityField.getPropertyName());
						}
					}
				} else {
					return new EntityField(entityField.getPropertyName());
				}
			} else {
				if (prefixRefPath == null) {
					return new EntityField(entityField.getPropertyName());
				} else {
					return new EntityField(prefixRefPath + "." + entityField.getPropertyName());
				}
			}
		}

		@Override
		public ASTNode visit(SubQuery subQuery) {

			Query q = subQuery.getQuery().copy();
			Condition on = null;
			if (subQuery.getOn() != null) {
				RefAdder sub = new RefAdder(prefixRefPath);
				sub.parent = this;
				on = (Condition) subQuery.getOn().accept(sub);
			}

			return new SubQuery(q, on);
		}

	}

	@Override
	public void normalizeForInsert(Entity entity, InsertOption option, EntityContext entityContext) {
		if (option.isVersionSpecified()) {
			Long ver = entity.getVersion();
			if (ver != null) {
				entity.setVersion(ver);
			} else {
				entity.setVersion(Long.valueOf(0));
			}
		} else {
			entity.setVersion(Long.valueOf(0));
		}
	}

	@Override
	public Entity[] normalizeRefEntity(Entity[] refEntity,
			ReferencePropertyHandler rph, EntityContext context) {
		if (refEntity == null) {
			return null;
		}

		if (rph.getMetaData().getVersionControlType() == VersionControlReferenceType.AS_OF_EXPRESSION_BASE) {
			//指定されているもので保存（バージョンしていないものは0で初期化）
			for (Entity e: refEntity) {
				if (e != null && e.getVersion() == null) {
					e.setVersion(VER_ZERO);
				}
			}
			return refEntity;
		}
		
		//version指定のないrefEntityに関し現在の最新を取得
		ArrayList<ValueExpression> oids = new ArrayList<ValueExpression>();
		for (Entity e: refEntity) {
			if (e != null && e.getVersion() == null) {
				oids.add(new Literal(e.getOid()));
			}
		}
		if (oids.size() == 0) {
			return refEntity;
		}
		
		EntityHandler targetEh = rph.getReferenceEntityHandler(context);
		Query q = new Query();
		q.select(Entity.OID, Entity.VERSION);
		q.from(targetEh.getMetaData().getName());
		q.where(new In(new EntityField(Entity.OID), oids));
		final Map<String, Long> res = new HashMap<String, Long>();
		targetEh.searchEntity(q, false, null, new Predicate<Entity>() {
			@Override
			public boolean test(Entity dataModel) {
				res.put(dataModel.getOid(), dataModel.getVersion());
				return true;
			}
		});
		
		for (Entity e: refEntity) {
			if (e != null && e.getVersion() == null) {
				Long ver = res.get(e.getOid());
				if (ver != null) {
					e.setVersion(ver);
				} else {
					e.setVersion(VER_ZERO);
				}
			}
		}
		
		return refEntity;
	}

	private Long getCurrentMaxVersionNo(String oid, EntityHandler eh, EntityContext entityContext) {
		Query q = new Query();
		Max val = new Max(Entity.VERSION);
		q.select().add(val);
		q.from(eh.getMetaData().getName());
		q.where(new Equals(Entity.OID, oid));
		q.select().addHint(new FetchSizeHint(1));
		SearchResultIterator it = eh.getStrategy().search(entityContext, q, eh);
		try {
			if (it.next()) {
				return (Long) it.getValue(0);
			} else {
				return null;
			}
		} finally {
			it.close();
		}
	}

	private Entity merge(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext) {
		Entity before = new EntityLoadInvocationImpl(entity.getOid(), entity.getVersion(), new LoadOption(true, false).versioned(), false, eh.getService().getInterceptors(), eh).proceed();
		if (before == null) {
			throw new EntityConcurrentUpdateException(resourceString("impl.core.versioning.NumberbaseVersionController.alreadyOperated", eh.getLocalizedDisplayName()));
		}

		//タイムスタンプ比較
		if (option.isCheckTimestamp()) {
			if (!before.getUpdateDate().equals(entity.getUpdateDate())) {
				throw new EntityConcurrentUpdateException(resourceString("impl.core.versioning.NumberbaseVersionController.alreadyOperated", eh.getLocalizedDisplayName()));
			}
		}

		if (option.getUpdateProperties() != null) {
			for (String propName: option.getUpdateProperties()) {
				PropertyHandler ph = eh.getProperty(propName, entityContext);
				if (ph.getMetaData().isUpdatable()) {
					before.setValue(propName, entity.getValue(propName));
				}
			}
		}

		return before;

	}

	@Override
	public void update(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext) {

		//IndexTypeが、uniqueの項目の更新はできない。
		for (String propName: option.getUpdateProperties()) {
			IndexType it = eh.getProperty(propName, entityContext).getMetaData().getIndexType();
			if (it == IndexType.UNIQUE || it == IndexType.UNIQUE_WITHOUT_NULL) {
				throw new EntityApplicationException(resourceString("impl.core.versioning.NumberbaseVersionController.notChange", eh.getProperty(propName, entityContext).getLocalizedDisplayName()));
			}
		}

		switch (option.getTargetVersion()) {
		case SPECIFIC://指定バージョンの更新
			if (entity.getVersion() == null) {
				throw new EntityRuntimeException("must specify version when TargetVersion.SPECIFIC");
			}
			eh.updateDirect(entity, option, entityContext);
			break;
		case NEW://新しいバージョンとしてインサート
			Entity merged = merge(entity, option, eh, entityContext);
			Long version = getCurrentMaxVersionNo(entity.getOid(), eh, entityContext);
			if (version == null) {
				throw new EntityConcurrentUpdateException(resourceString("impl.core.versioning.NumberbaseVersionController.alreadyOperated", eh.getLocalizedDisplayName()));
			}
			Long newVersion = Long.valueOf(version.longValue() + 1L);
			merged.setVersion(newVersion);
			merged.setCreateBy(entity.getUpdateBy());
			merged.setUpdateBy(entity.getUpdateBy());
			merged.setUpdateDate(null);//更新日は新しい値で更新
			eh.insertDirect(merged, entityContext);

			entity.setVersion(newVersion);
			break;
		case CURRENT_VALID://現時点の有効バージョンに更新
			Entity before = new EntityLoadInvocationImpl(entity.getOid(), null, new LoadOption(true, false), false, eh.getService().getInterceptors(), eh).proceed();
			if (before == null ||
					(option.isCheckTimestamp() && !before.getUpdateDate().equals(entity.getUpdateDate()))) {
				throw new EntityConcurrentUpdateException(resourceString("impl.core.versioning.NumberbaseVersionController.alreadyOperated", eh.getLocalizedDisplayName()));
			}
			entity.setVersion(before.getVersion());
			eh.updateDirect(entity, option, entityContext);
			break;
		default:
			throw new EntityRuntimeException("must specify tergetVersion option.");
		}
	}

	@Override
	public DeleteTarget[] getDeleteTarget(Entity entity, DeleteOption option, EntityHandler eh, EntityContext entityContext) {
		
		//DeleteTargetVersion.SPECIFICの場合、指定されたバージョンのみ削除
		if (option.getTargetVersion() == DeleteTargetVersion.SPECIFIC) {
			return new DeleteTarget[] {
					new DeleteTarget(entity.getOid(), entity.getVersion(), entity.getUpdateDate())};
		}
		
		//TODO 厳密なタイムスタンプチェックは難しい。。

		if (option.isCheckTimestamp()) {
			Entity before = new EntityLoadInvocationImpl(entity.getOid(), null, new LoadOption(true, false), false, eh.getService().getInterceptors(), eh).proceed();
			if (before == null ||
					!before.getUpdateDate().equals(entity.getUpdateDate())) {
				throw new EntityConcurrentUpdateException(resourceString("impl.core.versioning.NumberbaseVersionController.alreadyOperated", eh.getLocalizedDisplayName()));
			}
		}

		Query q = new Query();
		EntityField version = new EntityField(Entity.VERSION);
		EntityField updateDate = new EntityField(Entity.UPDATE_DATE);
		q.select().add(version, updateDate);
		q.from(eh.getMetaData().getName());
		q.where(new Equals(Entity.OID, entity.getOid()));
		SearchResultIterator it = eh.getStrategy().search(entityContext, q, eh);
		ArrayList<DeleteTarget> res = new ArrayList<DeleteTarget>();
		try {
			while (it.next()) {
				res.add(new DeleteTarget(entity.getOid(), (Long) it.getValue(0), (Timestamp) it.getValue(1)));
			}
		} finally {
			it.close();
		}
		if (res.size() == 0) {
			throw new EntityConcurrentUpdateException(resourceString("impl.core.versioning.NumberbaseVersionController.alreadyOperated", eh.getLocalizedDisplayName()));
		}
		return res.toArray(new DeleteTarget[res.size()]);
	}

	@Override
	public String[] getCascadeDeleteTarget(Entity entity, EntityHandler eh,
			ReferencePropertyHandler rph, EntityContext entityContext) {
		Query q = new Query();
		EntityField refOid = new EntityField(rph.getName() + "." + Entity.OID);
		q.selectDistinct(refOid);
		q.from(eh.getMetaData().getName());
		q.where(new Equals(Entity.OID, entity.getOid()));
		q.versioned(true);
		SearchResultIterator it = eh.getStrategy().search(entityContext, q, eh);
		ArrayList<String> res = new ArrayList<String>();
		try {
			while (it.next()) {
				res.add((String) it.getValue(0));
			}
		} finally {
			it.close();
		}
		if (res.size() == 0) {
			return null;
		}
		return res.toArray(new String[res.size()]);
	}

	protected AsOf judgeAsOf(String refPropPath, ReferencePropertyHandler rph, AsOf asOf) {
		if (asOf == null) {
			if (rph.getMetaData().getVersionControlType() == VersionControlReferenceType.RECORD_BASE) {
				return new AsOf(AsOfSpec.UPDATE_TIME);
			} else if (rph.getMetaData().getVersionControlType() == VersionControlReferenceType.AS_OF_EXPRESSION_BASE) {
				ValueExpression ve = rph.getAsOfExpression();
				if (ve == null) {
					throw new IllegalStateException("no versionControlAsOfExpression specified.");
				}
				String prefixRefPath = null;
				int index = refPropPath.lastIndexOf('.');
				if (index >= 0) {
					prefixRefPath = refPropPath.substring(0, index);
				}

				return new AsOf((ValueExpression) ve.accept(new RefAdder(prefixRefPath)));
			} else {
				return new AsOf(AsOfSpec.NOW);
			}
		} else {
			return asOf;
		}
	}

	@Override
	public Condition refEntityQueryCondition(String refPropPath, ReferencePropertyHandler rph, AsOf asOf, EntityContext context) {

		asOf = judgeAsOf(refPropPath, rph, asOf);

		//下記のイメージ
		// ([refPropPath].oid, [refPropPath].version) in (select oid, max(version) from [REF_ENTITY] where state='V' group by oid on .{refPropPath}=this) or [refPropPath].oid is null

		switch (asOf.getSpec()) {
		case UPDATE_TIME:
			return null;
		case NOW:
			In in = new In(new String[]{refPropPath + "." + Entity.OID, refPropPath + "." + Entity.VERSION},
					new SubQuery(
							new Query().select(
									new EntityField(Entity.OID),
									new Max(Entity.VERSION))
									.from(rph.getReferenceEntityHandler(context).getMetaData().getName())
									.where(new Equals(Entity.STATE, Entity.STATE_VALID_VALUE))
									.groupBy(Entity.OID)).on(refPropPath, SubQuery.THIS));

			return in;
		case SPEC_VALUE:
			ValueExpression asOfVal = asOf.getValue();
			if (asOfVal != null) {
				asOfVal = (ValueExpression) asOfVal.accept(new PropertyUnnester());
			}
			In in2 = new In(new String[]{refPropPath + "." + Entity.OID, refPropPath + "." + Entity.VERSION},
					new SubQuery(
							new Query().select(
									new EntityField(Entity.OID),
									new EntityField(Entity.VERSION))
									.from(rph.getReferenceEntityHandler(context).getMetaData().getName())
									.where(new Equals(Entity.STATE, Entity.STATE_VALID_VALUE)))
									.on(new And(
									new Equals(new EntityField("." + refPropPath), new EntityField(SubQuery.THIS)),
									new Equals(Entity.VERSION, asOfVal))));

			return in2;
		default:
			return null;
		}
	}

	@Override
	public Condition mainQueryCondition(EntityHandler eh, AsOf asOf, EntityContext context) {
		//下記のイメージ
		// ([cond]) and version=(select max(version) from [ENTITY] where state='V' on .this=this)

		if (asOf == null || asOf.getSpec() != AsOfSpec.SPEC_VALUE) {
			return new Equals(Entity.VERSION,
					new ScalarSubQuery(new Query()
							.select(new Max(Entity.VERSION))
							.from(eh.getMetaData().getName())
							.where(new Equals(Entity.STATE, Entity.STATE_VALID_VALUE)))
							.on(SubQuery.THIS, SubQuery.THIS));
		} else {
			ValueExpression asOfVal = asOf.getValue();
			if (asOfVal != null) {
				asOfVal = (ValueExpression) asOfVal.accept(new PropertyUnnester());
			}
			return new Equals(Entity.VERSION,
					new ScalarSubQuery(new Query()
							.select(new EntityField(Entity.VERSION))
							.from(eh.getMetaData().getName())
							.where(new Equals(Entity.STATE, Entity.STATE_VALID_VALUE)))
							.on(new And(
									new Equals(new EntityField("." + SubQuery.THIS), new EntityField(SubQuery.THIS)),
									new Equals(Entity.VERSION, asOfVal))));
		}
	}

	@Override
	public Entity[] getCascadeDeleteTargetForUpdate(Entity[] refEntity,
			Entity[] beforeRefEntity, ReferencePropertyHandler rph,
			Entity beforeEntity, EntityHandler eh, EntityContext entityContext) {

		ArrayList<Entity> potentialTarget = new ArrayList<Entity>();
		for (Entity e: beforeRefEntity) {
			if (e != null) {
				if (refEntity == null) {
					potentialTarget.add(e);
				} else {
					boolean match = false;
					for (Entity newE: refEntity) {
						if (newE != null && newE.getOid().equals(e.getOid())) {
							match = true;
							break;
						}
					}
					if (!match) {
						potentialTarget.add(e);
					}
				}
			}
		}

		if (potentialTarget.size() == 0) {
			return null;
		}

		//すべてのバージョンから参照されていないかを確認
		Query q = new Query();
		EntityField refOid = new EntityField(rph.getName() + "." + Entity.OID);
		q.selectDistinct(refOid);
		q.from(eh.getMetaData().getName());
		q.where(new Equals(Entity.OID, beforeEntity.getOid()));
		q.versioned(true);
		SearchResultIterator it = eh.getStrategy().search(entityContext, q, eh);
		ArrayList<String> searched = new ArrayList<String>();
		try {
			while (it.next()) {
				String rov = (String) it.getValue(0);
				if (rov != null) {
					searched.add(rov);
				}
			}
		} finally {
			it.close();
		}

		ArrayList<Entity> ret = new ArrayList<Entity>();
		for (Entity e: potentialTarget) {
			boolean isMatch = false;
			for (String soid: searched) {
				if (soid.equals(e.getOid())) {
					isMatch = true;
					break;
				}
			}
			if (!isMatch) {
				ret.add(e);
			}
		}

		if (ret.size() == 0) {
			return null;
		} else {
			return ret.toArray(new Entity[ret.size()]);
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
