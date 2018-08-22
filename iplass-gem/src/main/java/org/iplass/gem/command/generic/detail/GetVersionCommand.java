/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

/**
 * 別バージョン取得コマンド
 * @author lis3wg
 */
@WebApi(
	name=GetVersionCommand.WEBAPI_NAME,
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/detail/GetVersionCommand", displayName="別バージョン取得")
public final class GetVersionCommand extends DetailCommandBase {

	public static final String WEBAPI_NAME = "gem/generic/detail/getVersion";

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String oid = request.getParam(Constants.OID);
		String version = request.getParam(Constants.VERSION);

		Query q = new Query();
		q.select(Entity.OID, Entity.NAME, Entity.VERSION)
		 .from(defName)
		 .where(new And(new Equals(Entity.OID, oid),
				new NotEquals(Entity.VERSION, version)))
		 .order(new SortSpec(Entity.VERSION, SortType.DESC));

		List<Entity> list = em.searchEntity(q).getList();

		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, toSimpleList(request, list));
		return Constants.CMD_EXEC_SUCCESS;
	}

	private List<SimpleEntity> toSimpleList(RequestContext request, List<Entity> list) {
		List<SimpleEntity> simpleList = new ArrayList<SimpleEntity>();
		for (Entity entity : list) {
			simpleList.add(new SimpleEntity(entity));
		}
		return simpleList;
	}

	class SimpleEntity {
		private String oid;
		private String name;
		private Long version;
		/**
		 * コンストラクタ
		 */
		public SimpleEntity(Entity entity) {
			this.oid = entity.getOid();
			this.name = entity.getName();
			this.version = entity.getVersion();
		}
		/**
		 * oidを取得します。
		 * @return oid
		 */
		public String getOid() {
			return oid;
		}
		/**
		 * oidを設定します。
		 * @param oid oid
		 */
		public void setOid(String oid) {
			this.oid = oid;
		}
		/**
		 * nameを取得します。
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * nameを設定します。
		 * @param name name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * versionを取得します。
		 * @return version
		 */
		public Long getVersion() {
			return version;
		}
		/**
		 * versionを設定します。
		 * @param version version
		 */
		public void setVersion(Long version) {
			this.version = version;
		}

	}
}
