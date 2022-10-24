/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.entityexplorer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.server.base.io.download.DownloadUtil;
import org.iplass.adminconsole.server.base.service.auditlog.AdminAuditLoggingService;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.ENCODE;
import org.iplass.adminconsole.shared.base.dto.io.download.DownloadProperty.FILETYPE;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.EntityConfigDownloadProperty.TARGET;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.view.menu.MetaEntityMenu;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

/**
 * EntityConfigExport用Service実装クラス
 */
public class EntityConfigDownloadServiceImpl extends AdminDownloadService {

	private static final long serialVersionUID = -3459617043325559477L;

	private EntityDefinitionManager edm;
	private DefinitionService ds;
	private AdminAuditLoggingService aals;

	@Override
	public void init() throws ServletException {
		super.init();

		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		ds = ServiceRegistry.getRegistry().getService(DefinitionService.class);
		aals = ServiceRegistry.getRegistry().getService(AdminAuditLoggingService.class);
	}

	@Override
	protected void doDownload(final HttpServletRequest req, final HttpServletResponse resp, final int tenantId) {

		//パラメータの取得
		final String fileType = req.getParameter("fileType");
		final String defName = req.getParameter("definitionName");
		final String defNames = req.getParameter("definitionNames");
		final String csvOutputTarget = req.getParameter("csvOutputTarget");
		final String csvEncode = req.getParameter("csvEncode");
		final String xmlEntity = req.getParameter("xmlEntity");
		final String xmlEntityView = req.getParameter("xmlEntityView");
		final String xmlEntityFilter = req.getParameter("xmlEntityFilter");
		final String xmlEntityMenuItem = req.getParameter("xmlEntityMenuItem");
		final String xmlEntityWebAPI = req.getParameter("xmlEntityWebAPI");

		if (FILETYPE.CSV.equals(FILETYPE.valueOf(fileType))) {
			String execEncode = null;
			if (ENCODE.valueOfByValue(csvEncode) == null) {
				execEncode = ENCODE.UTF8.getValue();
			} else {
				execEncode = csvEncode;
			}
			csvDownload(tenantId, getDefNames(defName, defNames), csvOutputTarget, execEncode, resp);
		} else if (FILETYPE.XML.equals(FILETYPE.valueOf(fileType))) {
			boolean isOutEntity = Boolean.valueOf(xmlEntity);
			boolean isOutEntityView = Boolean.valueOf(xmlEntityView);
			boolean isOutEntityFilter = Boolean.valueOf(xmlEntityFilter);
			boolean isOutEntityMenuItem = Boolean.valueOf(xmlEntityMenuItem);
			boolean isOutEntityWebAPI = Boolean.valueOf(xmlEntityWebAPI);
			xmlDownload(tenantId, getDefNames(defName, defNames), isOutEntity, isOutEntityView, isOutEntityFilter, isOutEntityMenuItem, isOutEntityWebAPI, resp);
		}
	}

	private String[] getDefNames(String defName, String defNames) {
		if (defName != null && !defName.trim().isEmpty()) {
			return new String[]{defName};
		}
		if (defNames == null || defNames.isEmpty()) {
			throw new IllegalArgumentException(rs("tools.entityexplorer.EntityConfigDownloadServiceImpl.canNotGetEntityTarget"));
		}
		return defNames.split(",");
	}

	private void csvDownload(final int tenantId, final String[] defNames, final String outputTarget, final String encode, final HttpServletResponse resp) {

		if (TARGET.PROPERTY.equals(TARGET.valueOf(outputTarget))) {
			downloadProperty(resp, defNames, encode);
		} else if (TARGET.VIEW.equals(TARGET.valueOf(outputTarget))) {
			downloadView(resp, defNames, encode);
		}
	}

	private void xmlDownload(final int tenantId, final String[] defNames, final boolean isOutEntity,
			final boolean isOutEntityView, final boolean isOutEntityFilter, final boolean isOutEntityMenuItem, final boolean isOutEntityWebAPI,
			final HttpServletResponse resp) {

		List<String> entryPaths = new ArrayList<>();
		List<String> entityIds = new ArrayList<>();

		for (String defName : defNames) {

			//名前が一致するMetaData定義を取得
			String path = ds.getPath(EntityDefinition.class, defName);
			MetaDataEntry entity = MetaDataContext.getContext().getMetaDataEntry(path);
			if (entity == null) {
				continue;
			}
			if (isOutEntity) {
				entryPaths.add(entity.getPath());
			}
			entityIds.add(entity.getMetaData().getId());

			if (isOutEntityView) {
				path = ds.getPath(EntityView.class, defName);
				MetaDataEntry view = MetaDataContext.getContext().getMetaDataEntry(path);
				if (view != null) {
					entryPaths.add(view.getPath());
				}
			}
			if (isOutEntityFilter) {
				path = ds.getPath(EntityFilter.class, defName);
				MetaDataEntry filter = MetaDataContext.getContext().getMetaDataEntry(path);
				if (filter != null) {
					entryPaths.add(filter.getPath());
				}
			}
			if (isOutEntityWebAPI) {
				path = ds.getPath(EntityWebApiDefinition.class, defName);
				MetaDataEntry webapi = MetaDataContext.getContext().getMetaDataEntry(path);
				if (webapi != null) {
					entryPaths.add(webapi.getPath());
				}
			}
		}

		if (isOutEntityMenuItem) {
			//Menuを出力する場合は、EntityMenuItemの参照Entityをチェックして対象にする
			List<MetaDataEntryInfo> menuList = MetaDataContext.getContext().definitionList(ds.getPrefixPath(MenuItem.class));
			if (menuList != null) {
				for (MetaDataEntryInfo info : menuList) {
					MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(info.getPath());
					if (entry.getMetaData() instanceof MetaEntityMenu) {
						MetaEntityMenu menu = (MetaEntityMenu)entry.getMetaData();
						if (entityIds.contains(menu.getDefinitionId())) {
							entryPaths.add(entry.getPath());
						}
					}
				}
			}
		}

		//ソート(コンテキストPathを合わせるため)
        Collections.sort(entryPaths, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});

		String fileName = null;
		StringBuilder sbParam = new StringBuilder();
		if (defNames.length == 1) {
			fileName = tenantId + "-" + defNames[0] + "_PropertyConfig" + ".xml";
			sbParam.append("entityName:" + defNames[0]);
		} else {
			fileName = tenantId + "-" + "SelectEntities_PropertyConfig" + ".xml";
			sbParam.append("entityName:" + Arrays.toString(defNames));
		}

		aals.logDownload("EntityConfigDownload", fileName, sbParam);

		//出力処理
		try (
			OutputStreamWriter os = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(os);
		) {
	        // ファイル名を設定
			DownloadUtil.setResponseHeader(resp, MediaType.TEXT_XML, fileName);

			//Export
			MetaDataPortingService metaService = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
			metaService.write(writer, entryPaths);

		} catch (IOException e) {
			throw new DownloadRuntimeException(e);
		}
	}

	private void downloadProperty(final HttpServletResponse resp, final String[] defNames, final String encode) {

		try (EntityPropertyCsvWriter writer = new EntityPropertyCsvWriter(resp.getOutputStream(), encode)){

			String fileName = null;
			StringBuilder sbParam = new StringBuilder();
			if (defNames.length == 1) {
				fileName = defNames[0] + "_PropertyConfig.csv";
				sbParam.append("entityName:" + defNames[0]);
			} else {
				fileName = "SelectEntities_PropertyConfig.csv";
				sbParam.append("entityName:" + Arrays.toString(defNames));
			}

			aals.logDownload("EntityConfigDownload", fileName, sbParam);

			//レスポンス設定
			DownloadUtil.setCsvResponseHeader(resp, fileName, encode);

			//ヘッダ出力
			writer.writeHeader();

			for (String defName : defNames) {
				//EntityDefinitionの取得
				final EntityDefinition definition = edm.get(defName);

				//定義出力
				writer.writeConfig(definition);
			}

        } catch (IOException e) {
        	throw new DownloadRuntimeException(e);
        }
	}

	private void downloadView(final HttpServletResponse resp, final String[] defNames, final String encode) {

		try (EntityViewCsvWriter writer = new EntityViewCsvWriter(resp.getOutputStream(), encode)){

			String fileName = null;
			StringBuilder sbParam = new StringBuilder();
			if (defNames.length == 1) {
				fileName = defNames[0] + "_ViewConfig.csv";
				sbParam.append("entityName:" + defNames[0]);
			} else {
				fileName = "SelectEntities_ViewConfig.csv";
				sbParam.append("entityName:" + Arrays.toString(defNames));
			}

			aals.logDownload("EntityConfigDownload", fileName, sbParam);

			//レスポンス設定
			DownloadUtil.setCsvResponseHeader(resp, fileName, encode);

			EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
			int i = 1;
			for (String defName : defNames) {
				//EntityViewの取得
				final EntityView view = evm.get(defName);

				//定義出力
				writer.writeView(view, i, defName);
				i++;
			}

        } catch (IOException e) {
        	throw new DownloadRuntimeException(e);
		}
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
