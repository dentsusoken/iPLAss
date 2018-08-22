/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.refs.web;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.tenant.web.MetaTenantWebInfo;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping;
import org.iplass.mtp.impl.web.staticresource.MetaStaticResource;
import org.iplass.mtp.impl.web.template.MetaTemplate;
import org.iplass.mtp.impl.webapi.MetaEntityWebApi;
import org.iplass.mtp.impl.webapi.MetaWebApi;

@XmlSeeAlso({
	MetaActionMapping.class
	,MetaTemplate.class
	,MetaEntityWebApi.class
	,MetaWebApi.class
	,MetaStaticResource.class
	,MetaTenantWebInfo.class
})
class WebRootMetaDatas {
	//Web系のMetaDataへのXmlSeeAlsoを管理するためだけのクラス
}
