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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreSearchSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreUpdateSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.GRdbEntityStoreStrategy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EachRecordBulkUpdateStrategy implements BulkUpdateStrategy {
	private static Logger log = LoggerFactory.getLogger(EachRecordBulkUpdateStrategy.class);

	private GRdbEntityStoreStrategy storeStrategy;
	private RdbAdapter rdb;
	private ObjStoreSearchSql searchSql;
	
	public EachRecordBulkUpdateStrategy(GRdbEntityStoreStrategy storeStrategy, RdbAdapter rdb) {
		this.storeStrategy = storeStrategy;
		this.rdb = rdb;
		searchSql = rdb.getQuerySqlCreator(ObjStoreSearchSql.class);
	}

	@Override
	public void bulkUpdate(BulkUpdatable bulkUpdatable,
			EntityContext entityContext, EntityHandler entityHandler,
			String clientId) {
		
		final int tenantId = entityContext.getLocalTenantId();
		DeleteOption delOp = new DeleteOption(false);
		UpdateOption upOp = new UpdateOption(false);
		List<String> updateProps = new ArrayList<String>();
		if (bulkUpdatable.getUpdateProperties() != null) {
			for (String pn: bulkUpdatable.getUpdateProperties()) {
				if (ObjStoreUpdateSql.canUpdateProperty(entityHandler.getProperty(pn, entityContext))) {
					updateProps.add(pn);
				}
			}
		} else {
			for (PropertyHandler ph: entityHandler.getPropertyList(entityContext)) {
				if (ph instanceof PrimitivePropertyHandler) {
					if (ObjStoreUpdateSql.canUpdateProperty(ph)) {
						updateProps.add(ph.getName());
					}
				} else if (ph instanceof ReferencePropertyHandler) {
					if (((ReferencePropertyHandler) ph).getMetaData().getMappedByPropertyMetaDataId() == null) {
						updateProps.add(ph.getName());
					}
				}
			}
		}
		upOp.setUpdateProperties(updateProps);

		PreparedStatement stmt = null;
		
		try {
			
			for (BulkUpdateEntity target: bulkUpdatable) {
				if (target.getEntity().getVersion() == null) {
					target.getEntity().setVersion(Long.valueOf(0));
				}
				switch (target.getMethod()) {
				case DELETE:
					storeStrategy.delete(entityContext, target.getEntity(), entityHandler, delOp);
					break;
				case INSERT:
					if (target.getEntity().getOid() == null) {
						target.getEntity().setOid(storeStrategy.newOid(entityContext, entityHandler));
					}
					storeStrategy.insert(entityContext, entityHandler, target.getEntity());
					break;
				case MERGE:
					String oid = target.getEntity().getOid();
					if (oid == null) {
						target.getEntity().setOid(storeStrategy.newOid(entityContext, entityHandler));
						storeStrategy.insert(entityContext, entityHandler, target.getEntity());
					} else {
						if (stmt == null) {
							stmt = rdb.getConnection().prepareStatement(searchSql.checkExistsSql(tenantId, entityHandler, target.getEntity(), rdb));
						}
						searchSql.checkExistsParameter(stmt, tenantId, entityHandler, target.getEntity());
						try (ResultSet rs = stmt.executeQuery()) {
							if (rs.next()) {
								storeStrategy.update(entityContext, entityHandler, target.getEntity(), upOp);
							} else {
								storeStrategy.insert(entityContext, entityHandler, target.getEntity());
							}
						}
					}
					break;
				case UPDATE:
					storeStrategy.update(entityContext, entityHandler, target.getEntity(), upOp);
					break;
				default:
					throw new IllegalArgumentException();
				}
				bulkUpdatable.updated(target);
			}
			
		} catch (SQLException e) {
			throw new EntityRuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
				}
			}
		}
	}

}
