/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.refs.view;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.tenant.gem.MetaTenantGemInfo;
import org.iplass.mtp.impl.view.calendar.MetaCalendar;
import org.iplass.mtp.impl.view.generic.MetaEntityView;
import org.iplass.mtp.impl.view.menu.MetaMenu;
import org.iplass.mtp.impl.view.menu.MetaTreeMenu;
import org.iplass.mtp.impl.view.top.MetaTopView;
import org.iplass.mtp.impl.view.treeview.MetaTreeView;

@XmlSeeAlso({
	MetaCalendar.class
	,MetaEntityView.class
	,MetaMenu.class
	,MetaTreeMenu.class
	,MetaTopView.class
	,MetaTreeView.class
	,MetaTenantGemInfo.class
})
class ViewRootMetaDatas {
	// View系のMetaDataへのXmlSeeAlsoを管理するためだけのクラス
}
