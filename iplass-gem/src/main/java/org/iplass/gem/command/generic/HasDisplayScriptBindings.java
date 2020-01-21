package org.iplass.gem.command.generic;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.util.StringUtil;

/**
 * 表示判断スクリプト用情報を保持しているインターフェース
 */
public interface HasDisplayScriptBindings {

	public default Entity getBindingEntity(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String entityOid = request.getParam(Constants.DISPLAY_SCRIPT_ENTITY_OID);
		String entityVersion = request.getParam(Constants.DISPLAY_SCRIPT_ENTITY_VERSION);

		if (StringUtil.isNotBlank(entityOid) && StringUtil.isNotBlank(entityVersion)) {
			Entity e = new GenericEntity(defName);
			e.setOid(entityOid);
			e.setVersion(Long.valueOf(entityVersion));
			return e;
		}
		return null;
	}

}
