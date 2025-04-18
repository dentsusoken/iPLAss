/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.metaport;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.apache.commons.collections4.CollectionUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.internal.InternalCredential;
import org.iplass.mtp.impl.command.MetaMetaCommand;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.ConfigImpl;
import org.iplass.mtp.impl.core.config.NameValue;
import org.iplass.mtp.impl.datastore.DataStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataContextListener;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.impl.metadata.MetaDataJAXBService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.metadata.xmlresource.ContextPath;
import org.iplass.mtp.impl.metadata.xmlresource.MetaDataEntryList;
import org.iplass.mtp.impl.metadata.xmlresource.XmlResourceMetaDataEntryThinWrapper;
import org.iplass.mtp.impl.metadata.xmlresource.XmlResourceMetaDataStore;
import org.iplass.mtp.impl.script.GroovyScriptService;
import org.iplass.mtp.impl.script.MetaUtilityClass;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.tenant.MetaTenantService;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportStatus.ImportAction;
import org.iplass.mtp.impl.view.filter.EntityFilterService;
import org.iplass.mtp.impl.view.generic.EntityViewService;
import org.iplass.mtp.impl.webapi.EntityWebApiService;
import org.iplass.mtp.impl.xml.jaxb.SecureSAXParserFactory;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * MetaDataのExport/Import用Service
 */
public class MetaDataPortingServiceImpl implements MetaDataPortingService {

	private static Logger logger = LoggerFactory.getLogger(MetaDataPortingServiceImpl.class);
	private static Logger toolLogger = LoggerFactory.getLogger("mtp.tools.metadata");

	private AuthService authService;
	private MetaDataJAXBService jaxbService;
	private MetaTenantService metaTenantService;
	private TenantContextService tContextService;
	private MetaDataImportHandler importHandler;

	@Override
	public void init(Config config) {
		authService = config.getDependentService(AuthService.class);
		jaxbService = config.getDependentService(MetaDataJAXBService.class);
		metaTenantService = config.getDependentService(MetaTenantService.class);
		tContextService = config.getDependentService(TenantContextService.class);

		importHandler = (MetaDataImportHandler) config.getBean("importHandler");
	}

	@Override
	public void destroy() {
	}

	@Override
	public void write(PrintWriter writer, List<String> paths) {
		write(writer, paths, new MetaDataWriteLoggingCallback());
	}

	@Override
	public void write(PrintWriter writer, List<String> paths, MetaDataWriteCallback callback) {

		try {
			JAXBContext jaxbContext = jaxbService.getJAXBContext();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
			marshaller.setProperty("jaxb.fragment", Boolean.TRUE);

			//Header出力
			callback.onStarted();
			writeHeader(writer);

			String beforeContextPath = "";
			boolean isWriteContext = false; //有効なEntityがない場合の時用
			for (String path : paths) {
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
				if (entry == null) {
					if (callback.onWarning(path, "not found metadata configure.", null)) {
						continue;
					} else {
						break;
					}
				}
				String contextPath = getContextPath(entry.getPath(), entry.getMetaData().getName());
				if (!beforeContextPath.equals(contextPath)) {
					if (!beforeContextPath.isEmpty()) {
						writer.println("</contextPath>");
					}
					beforeContextPath = contextPath;
					writer.println("<contextPath name=\"" + StringUtil.escapeXml10(contextPath) + "\">");
					isWriteContext = true;
				}
				//Entry出力
				writeMetaDataEntry(writer, marshaller, entry);
				callback.onWrited(path, String.valueOf(entry.getVersion()));
			}
			if (isWriteContext) {
				writer.println("</contextPath>");
			}

			//Footer出力
			writeFooter(writer);
			callback.onFinished();
		} catch (JAXBException e) {
			throw new MetaDataPortingRuntimeException(getRS("canNotParsed", getJAXBExceptionMessage(e)), e);
		}
	}

	@Override
	public XMLEntryInfo getXMLMetaDataEntryInfo(InputStream is) {
		//XML->RootMetaDataの変換
		JAXBContext jaxbContext = jaxbService.getJAXBContext();
		MetaDataEntryList metaDataList = null;
		try {
			Unmarshaller um = jaxbContext.createUnmarshaller();
			//一旦、クライアントにダウンロードされた可能性のあるMetaDataなのでXXE対策しとく
			metaDataList = (MetaDataEntryList) um.unmarshal(toSaxSource(is));
		} catch (JAXBException e) {
			//エラー内容がUnmarshalExceptionのSAXParseExceptionに格納されているのでチェック
			String detail = "";
			if (e.getMessage() != null) {
				detail = e.getMessage();
			} else {
				if (e.getCause() != null) {
					if (e.getCause().getMessage() != null) {
						detail = e.getCause().getMessage();
					}
				}
			}
			throw new MetaDataPortingRuntimeException(getRS("canNotParsed", detail), e);
		}

		return parse(metaDataList);
	}

	@Override
	public void writeHistory(PrintWriter writer, String definitionId, String[] versions) {
		writeHistory(writer, definitionId, versions, new MetaDataWriteLoggingCallback());

	}

	@Override
	public void writeHistory(PrintWriter writer, String definitionId, String[] versions, MetaDataWriteCallback callback) {

		try {
			JAXBContext jaxbContext = jaxbService.getJAXBContext();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
			marshaller.setProperty("jaxb.fragment", Boolean.TRUE);

			//Header出力
			callback.onStarted();
			writeHeader(writer);

			String beforeContextPath = "";
			boolean isWriteContext = false; //有効なEntityがない場合の時用
			for (String temp : versions) {

				int version = Integer.parseInt(temp);

				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntryById(definitionId, version);
				if (entry == null) {
					if (callback.onWarning(null, "not found metadata configure.", null)) {
						continue;
					} else {
						break;
					}
				}
				String contextPath = getContextPath(entry.getPath(), entry.getMetaData().getName());
				if (!beforeContextPath.equals(contextPath)) {
					if (!beforeContextPath.isEmpty()) {
						writer.println("</contextPath>");
					}
					beforeContextPath = contextPath;
					writer.println("<contextPath name=\"" + StringUtil.escapeXml10(contextPath) + "\">");
					isWriteContext = true;
				}
				//Entry出力
				writeMetaDataEntry(writer, marshaller, entry);
				callback.onWrited(entry.getPath(), String.valueOf(entry.getVersion()));
			}
			if (isWriteContext) {
				writer.println("</contextPath>");
			}

			//Footer出力
			writeFooter(writer);
			callback.onFinished();

		} catch (JAXBException e) {
			throw new MetaDataPortingRuntimeException(getRS("canNotParsed", getJAXBExceptionMessage(e)), e);
		}
	}

	@Override
	public MetaDataImportResult importMetaData(String targetName, XMLEntryInfo entryInfo, final Tenant importTenant) {

		toolLogger.info("start metadata import. {target:{}}", targetName);

		MetaDataImportResult result = new MetaDataImportResult();
		try {
			//Listに変換
			List<MetaDataEntry> entryList = new ArrayList<>(entryInfo.getPathEntryMap().values());

			return doImportMetaData(entryInfo, entryList, result, importTenant);
		} finally {
			toolLogger.info("finish metadata import. {target:{}, result:{}}", targetName, (result.isError() ? "failed" : "success"));
		}
	}

	@Override
	public MetaDataImportResult importMetaData(String targetName, XMLEntryInfo entryInfo, final List<String> selectedPaths, final Tenant importTenant) {

		toolLogger.info("start metadata import. {target:{}}", targetName);

		MetaDataImportResult result = new MetaDataImportResult();
		try {
			//XMLEntryを選択Pathで絞り込み
			XMLEntryInfo filterEntryInfo = filterSelectedPaths(entryInfo, selectedPaths);

			//Listに変換
			List<MetaDataEntry> entryList = new ArrayList<>();
			String curPath = null;
			try {
				for (String path : selectedPaths) {
					curPath = path;

					//対象メタデータ取得
					MetaDataEntry entry = filterEntryInfo.getPathEntry(path);
					if (entry == null) {
						throw new MetaDataPortingRuntimeException(getRS("canNotGetMeta", path));
					}
					entryList.add(entry);
				}
			} catch (Exception e) {
				errorForImportMetaDataEntry(curPath, e, result);
			}

			if (result.isError()) {
				return result;
			}

			return doImportMetaData(filterEntryInfo, entryList, result, importTenant);
		} finally {
			toolLogger.info("finish metadata import. {target:{}, result:{}}", targetName, (result.isError() ? "failed" : "success"));
		}
	}

	@Override
	public MetaDataImportStatus checkStatus(XMLEntryInfo xmlInfo, String importPath) {

		//インポート対象のXMLデータ取得(IDは未指定の可能性があるのでPathで検索)
		MetaDataEntry importEntry = xmlInfo.getPathEntry(importPath);
		if (importEntry == null) {
			throw new MetaDataPortingRuntimeException(getRS("canNotGetMeta", importPath));
		}
		RootMetaData importMetaData = importEntry.getMetaData();
		String importId = importMetaData.getId();

		MetaDataImportStatus status = new MetaDataImportStatus(importId, importPath);

		boolean isTenant = isTenantMeta(importPath);

		//Tenantの場合はNameが一致していない場合はWarningとして返す
		//(Tenantインポート時に選択プロパティのみ取り込む)
		if (isTenant) {
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			if (!tenant.getName().equals(importMetaData.getName())) {
				status.setWarn(true);
				status.setMessage(getRS("statusIncludeUnMatchTenant"));
				status.setMessageDetail(getRS("statusIncludeUnMatchTenantDetail"));
			} else {
				status.setInfo(true);
				status.setMessage(getRS("statusIncludeTenant"));
				status.setMessageDetail(getRS("statusIncludeTenantDetail"));
			}
			//テナントはID、Nameが異なってもUpdate固定
			status.setAction(ImportAction.UPDATE);
			return status;
		}

		//ID一致データ取得
		MetaDataEntry storedIDEntry = null;
		if (StringUtil.isNotEmpty(importId)) {
			storedIDEntry = MetaDataContext.getContext().getMetaDataEntryById(importId);
		}

		if (storedIDEntry != null) {
			//ID一致データあり
			//IDが一致している既存データをUpdate(保留)するが、既存メタデータの更新可否、Pathのチェックを行う

			//既存メタデータが更新できるかチェック
			if (RepositoryType.SHARED == storedIDEntry.getRepositryType() && !storedIDEntry.isOverwritable()) {
				//Sharedでかつ上書き禁止はエラー
				status.setAction(ImportAction.ERROR);
				status.setError(true);
				status.setMessage(getRS("statusCanNotOverwrite", importId, importPath));
				status.setMessageDetail(getRS("statusCanNotOverwriteDetail", importId, importPath));
				return status;
			}

			//Path一致チェック
			if (storedIDEntry.getPath().equals(importPath)) {
				//ID一致、Path一致
				//IDが一致している既存データをUpdate(確定)、リネームを伴わないUpdate
				status.setAction(ImportAction.UPDATE);
				return status;
			} else {
				//ID一致、Path不一致
				//IDが一致している既存データをUpdate(保留)するが、
				//Pathが異なるため、Pathを変更できるかをチェックする

				if (RepositoryType.SHARED == storedIDEntry.getRepositryType()) {
					//Sharedの場合は、リネーム不可
					status.setAction(ImportAction.ERROR);
					status.setError(true);
					status.setMessage(getRS("statusCanNotRenameSharedData", importId, storedIDEntry.getPath(), importPath));
					status.setMessageDetail(getRS("statusCanNotRenameSharedDataDetail", importId, storedIDEntry.getPath(), importPath));
					return status;
				}

				//リネーム後のPahに一致している既存メタデータの存在チェック
				MetaDataEntry storedRenamePathEntry = MetaDataContext.getContext().getMetaDataEntry(importPath);
				if (storedRenamePathEntry == null) {
					//IDが一致している既存データをUpdate(確定)、リネームを伴うUpdate
					status.setAction(ImportAction.RENAME);
					status.setInfo(true);
					status.setMessage(getRS("statusRename", importId, storedIDEntry.getPath(), importPath));
					status.setMessageDetail(getRS("statusRenameDetail", importId, storedIDEntry.getPath(), importPath));
					return status;
				} else {
					//リネーム後のPathが一致している既存メタデータをDelete(保留)することでインポートデータを有効にするが、
					//インポート対象データ内に、既存メタデータのIDに対してPathを変更している可能性があるためDeleteは保留。
					//インポートデータ内に既存メタデータのIDに一致するメタデータがあるかをチェック

					MetaDataEntry importRenamePathEntry = xmlInfo.getIdEntry(storedRenamePathEntry.getMetaData().getId());
					if (importRenamePathEntry != null) {
						//IDが一致している既存データをUpdate(確定)、リネームを伴うUpdate
						//リネーム後のPathに一致する既存データはDeleteしない(確定)
						status.setAction(ImportAction.RENAMEWITHCOMBI);
						status.setCombiMetaDataId(importRenamePathEntry.getMetaData().getId());
						status.setCombiMetaDataPath(importRenamePathEntry.getPath());
						status.setCombiMetaData(importRenamePathEntry.getMetaData());
						status.setInfo(true);
						status.setMessage(getRS("statusRenameWithCombi",
								importId, storedIDEntry.getPath(), importPath,
								importRenamePathEntry.getMetaData().getId(), importRenamePathEntry.getPath()));
						status.setMessageDetail(getRS("statusRenameWithCombiDetail",
								importId, storedIDEntry.getPath(), importPath,
								importRenamePathEntry.getMetaData().getId(), importRenamePathEntry.getPath()));
						return status;
					} else {
						//リネーム後のPathに一致する既存データがDelete可能かをチェック
						if (RepositoryType.SHARED == storedRenamePathEntry.getRepositryType()
								|| RepositoryType.SHARED_OVERWRITE == storedRenamePathEntry.getRepositryType()) {
							//Sharedデータは削除できないため、リネーム不可
							status.setAction(ImportAction.ERROR);
							status.setError(true);
							status.setMessage(getRS("statusCanNotRenameWithSharedDelete",
									importId, storedIDEntry.getPath(), importPath,
									storedRenamePathEntry.getMetaData().getId()));
							status.setMessageDetail(getRS("statusCanNotRenameWithSharedDeleteDetail",
									importId, storedIDEntry.getPath(), importPath,
									storedRenamePathEntry.getMetaData().getId()));
							return status;
						} else {
							//IDが一致している既存データをUpdate(確定)、リネームを伴うUpdate
							//リネーム後のPathに一致する既存データはDelete(確定)
							status.setAction(ImportAction.RENAMEWITHDELETE);
							status.setRemoveMetaDataId(storedRenamePathEntry.getMetaData().getId());
							status.setRemoveMetaDataPath(storedRenamePathEntry.getPath());
							status.setRemoveMetaData(storedRenamePathEntry.getMetaData());
							status.setInfo(true);
							status.setMessage(getRS("statusRenameWithDelete",
									importId, storedIDEntry.getPath(), importPath,
									storedRenamePathEntry.getMetaData().getId()));
							status.setMessageDetail(getRS("statusRenameWithDeleteDetail",
									importId, storedIDEntry.getPath(), importPath,
									storedRenamePathEntry.getMetaData().getId()));
							return status;
						}
					}
				}
			}
		} else {
			//ID一致データなし
			//インポートデータをInsertするが、Pathが一致する既存メタデータの存在チェックを行う
			MetaDataEntry storedPathEntry = MetaDataContext.getContext().getMetaDataEntry(importPath);
			if (storedPathEntry != null) {
				//Pathが一致している既存メタデータをDelete(保留)することでインポートデータを有効にするが、
				//インポート対象データ内に、既存メタデータのIDに対してPathを変更している可能性があるためDeleteは保留。
				//インポートデータ内に既存メタデータのIDに一致するメタデータがあるかをチェック

				MetaDataEntry importRenamePathEntry = xmlInfo.getIdEntry(storedPathEntry.getMetaData().getId());
				if (importRenamePathEntry != null) {
					//インポートデータをInsert(確定)
					//Pathに一致する既存データはDeleteしない(確定)
					status.setAction(ImportAction.INSERTWITHCOMBI);
					status.setCombiMetaDataId(importRenamePathEntry.getMetaData().getId());
					status.setCombiMetaDataPath(importRenamePathEntry.getPath());
					status.setCombiMetaData(importRenamePathEntry.getMetaData());
					status.setInfo(true);
					status.setMessage(getRS("statusInsertWithCombi",
							importId, importPath,
							importRenamePathEntry.getMetaData().getId(), importRenamePathEntry.getPath()));
					status.setMessageDetail(getRS("statusInsertWithCombiDetail",
							importId, importPath,
							importRenamePathEntry.getMetaData().getId(), importRenamePathEntry.getPath()));
					return status;
				} else {
					//Pathに一致する既存データがDelete可能かをチェック
					if (RepositoryType.SHARED == storedPathEntry.getRepositryType()
							|| RepositoryType.SHARED_OVERWRITE == storedPathEntry.getRepositryType()) {
						//Sharedデータは削除できないため、Insert不可
						status.setAction(ImportAction.ERROR);
						status.setError(true);
						status.setMessage(getRS("statusCanNotInsertWithSharedDelete",
								importId, importPath,
								storedPathEntry.getMetaData().getId()));
						status.setMessageDetail(getRS("statusCanNotInsertWithSharedDeleteDetail",
								importId, importPath,
								storedPathEntry.getMetaData().getId()));
						return status;
					} else {
						//インポートデータをInsert(確定)
						//Pathに一致する既存データはDelete(確定)
						status.setAction(ImportAction.INSERTWITHDELETE);
						status.setRemoveMetaDataId(storedPathEntry.getMetaData().getId());
						status.setRemoveMetaDataPath(storedPathEntry.getPath());
						status.setRemoveMetaData(storedPathEntry.getMetaData());
						status.setInfo(true);
						status.setMessage(getRS("statusInsertWithDelete",
								importId, importPath,
								storedPathEntry.getMetaData().getId()));
						status.setMessageDetail(getRS("statusInsertWithDeleteDetail",
								importId, importPath,
								storedPathEntry.getMetaData().getId()));
						return status;
					}
				}
			} else {
				//Insert(確定)、新規ID、新規Path
				status.setAction(ImportAction.INSERT);
				return status;
			}
		}

	}

	@Override
	public boolean isTenantMeta(String importPath) {
		return importHandler.isTenantMeta(importPath);
	}

	private String getJAXBExceptionMessage(JAXBException e) {

		//エラー内容がUnmarshalExceptionのSAXParseExceptionに格納されているのでチェック
		String detail = "";
		if (e.getMessage() != null) {
			detail = e.getMessage();
		} else {
			if (e.getCause() != null) {
				if (e.getCause().getMessage() != null) {
					detail = e.getCause().getMessage();
				}
			}
		}
		return detail;
	}

	private SAXSource toSaxSource(InputStream is) throws JAXBException {
		//外部参照を処理しない
		SAXParserFactory f = SAXParserFactory.newInstance();
		f.setNamespaceAware(true);
		f.setValidating(false);
		f = new SecureSAXParserFactory(f);
		try {
			return new SAXSource(f.newSAXParser().getXMLReader(), new InputSource(is));
		} catch (SAXException | ParserConfigurationException e) {
			throw new JAXBException(e);
		}
	}

	/**
	 * XMLに含まれるEntryInfoを選択Pathで絞り込みます。
	 *
	 * @param entryInfo 取り込み対象が含まれたMetaData情報
	 * @param selectedPaths 取り込み対象のList
	 * @return 選択Pathで絞りこんだMetaData情報
	 */
	private XMLEntryInfo filterSelectedPaths(XMLEntryInfo entryInfo, List<String> selectedPaths) {
		XMLEntryInfo filterInfo = new XMLEntryInfo();
		for (String path : selectedPaths) {
			MetaDataEntry entry = entryInfo.getPathEntry(path);
			if (entry != null) {
				filterInfo.putPathEntry(path, entry);
				String id = entry.getMetaData().getId();
				if (StringUtil.isNotEmpty(id)) {
					filterInfo.putIdEntry(id, entry);
				} else {
					filterInfo.addIdBlankEntry(entry);
				}
			}
		}
		return filterInfo;
	}

	private MetaDataImportResult doImportMetaData(XMLEntryInfo entryInfo, final List<MetaDataEntry> entryList, final MetaDataImportResult result,
			final Tenant importTenant) {

		String curPath = null;
		//対象メタデータのステータスをチェックして、順番を振り分ける
		final List<ImportMetaDataInfo> removeList = new ArrayList<>();
		final Map<String, ImportMetaDataInfo> normalList = new HashMap<>();
		final List<ImportMetaDataInfo> normalCombiList = new ArrayList<>();
		final Map<String, ImportMetaDataInfo> individualList = new HashMap<>();
		final List<ImportMetaDataInfo> individualCombiList = new ArrayList<>();
		final List<String> combiIdList = new ArrayList<>();

		boolean needTenantReload = false;
		boolean needMetaContextReload = false;

		try {
			for (MetaDataEntry entry : entryList) {
				curPath = entry.getPath();
				RootMetaData metaData = entry.getMetaData();

				//ステータスチェック
				MetaDataImportStatus status = checkStatus(entryInfo, entry.getPath());
				if (status.isError()) {
					throw new MetaDataPortingRuntimeException(status.getMessage());
				}
				if (status.isWarn()) {
					//ログ出力
					logger.warn(status.getMessage());
				}

				//保持するためのインスタンス生成
				ImportMetaDataInfo info = new ImportMetaDataInfo(curPath, metaData, entry, status);

				//取り込み順の選定
				if (metaData instanceof MetaEntity) {
					if (status.hasRemoveMetaData()) {
						//削除をする必要がある場合は個別にする
						individualList.put(info.status.getImportId(), info);
					} else {
						//追加の場合は、通常のインポート
						if (status.isInsert()) {
							if (status.hasCombiMetaData()) {
								//他と合わせて追加する必要がある場合は個別にする
								//追加の場合は先に依存先側が変更されないとPathが重複するため
								individualList.put(info.status.getImportId(), info);
								combiIdList.add(info.status.getCombiMetaDataId());
							} else {
								normalList.put(info.status.getImportId(), info);
							}
						} else if (status.isUpdate()) {
							//TODO 更新の場合は、diffチェック、Listner存在チェック
							individualList.put(info.status.getImportId(), info);
						}
					}
					//現状、UtilityClass以外の場合はMetaDataContext全体をクリアするように対応
					//（個別にするとそのタイミングでinitRuntimeを実行しないといけないため）
					needMetaContextReload = true;

				} else {
					if (status.isInsert() && status.hasCombiMetaData()) {
						//他と合わせて追加する必要がある場合は個別にする
						//追加の場合は先に依存先側が変更されないとPathが重複するため
						individualList.put(info.status.getImportId(), info);
						combiIdList.add(info.status.getCombiMetaDataId());
					} else {
						//Entity以外は追加、更新に関係なく通常のインポート
						normalList.put(info.status.getImportId(), info);
					}

					if (metaData instanceof MetaUtilityClass) {
						//UtilityClassが含まれる場合はTenant全体をクリア
						needTenantReload = true;
					} else if (metaData instanceof MetaMetaCommand) {
						//MetaMetaCommandが含まれる場合はMetaDataContext全体をクリア
						needMetaContextReload = true;
					} else {
						//現状、UtilityClass以外の場合はMetaDataContext全体をクリアするように対応
						//（個別にするとそのタイミングでinitRuntimeを実行しないといけないため）
						needMetaContextReload = true;
					}
				}

				//削除対象がある場合は追加
				if (status.hasRemoveMetaData()) {
					removeList.add(new ImportMetaDataInfo(status.getRemoveMetaDataPath(), status.getRemoveMetaData()));
				}
			}

			if (!combiIdList.isEmpty()) {
				//コンビデータが含まれている場合は、normalList、individualListから抜き出す(先に更新するため)
				for (String combiId : combiIdList) {
					if (normalList.containsKey(combiId)) {
						normalCombiList.add(normalList.get(combiId));
						normalList.remove(combiId);
					}
					if (individualList.containsKey(combiId)) {
						individualCombiList.add(individualList.get(combiId));
						individualList.remove(combiId);
					}
				}
			}

		} catch (Exception e) {
			errorForImportMetaDataEntry(curPath, e, result);
		}

		//通常の更新処理
		if (!result.isError()) {
			doImportNormalMetaData(removeList, normalCombiList, new ArrayList<>(normalList.values()),
					importTenant, needTenantReload, needMetaContextReload, result);
		}

		//特殊の更新処理
		if (!result.isError()) {
			doImportIndividualMetaData(individualCombiList, new ArrayList<>(individualList.values()), result);
		}

		// 正常終了してる場合は、RuntimeのcheckStatus結果を追加する
		if (!result.isError()) {
			this.addCheckStatusResult(result, entryList);
		}

		return result;
	}

	private void doImportNormalMetaData(final List<ImportMetaDataInfo> removeList,
			final List<ImportMetaDataInfo> normalCombiList, final List<ImportMetaDataInfo> normalList,
			final Tenant importTenant, final boolean needTenantReload, final boolean needMetaContextReload, final MetaDataImportResult result) {

		if (removeList.isEmpty() && normalList.isEmpty()) {
			return;
		}

		//見やすくするためソート
		Collections.sort(removeList, new ImportMetaDataInfoComparator());
		Collections.sort(normalCombiList, new ImportMetaDataInfoComparator());
		Collections.sort(normalList, new ImportMetaDataInfoComparator());

		//MetaDataContextのListnerを生成
		final MetaDataContextListener listener = new MetaDataContextListener() {

			@Override
			public void updated(String path, String pathBefore) {
				logger.debug("metadata context updated. path=" + path + " pathBefore=" + pathBefore);
			}

			@Override
			public void removed(String path) {
				logger.debug("metadata context removed. path=" + path);
			}

			@Override
			public void created(String path) {
				logger.debug("metadata context created. path=" + path);
			}
		};

		try {

			//削除と通常のインポート処理はまとめて１トランザクションで行う
			Transaction.requiresNew(t -> {

				//個別にリロードするかの判定
				boolean doAutoReload = (!needTenantReload && !needMetaContextReload);

				MetaDataContext.getContext().addMetaDataContextListener(listener);

				//削除から開始
				for (ImportMetaDataInfo info : removeList) {
					try {
						importHandler.removeMetaData(info.path, info.metaData, doAutoReload);

						logger.debug("metadata removed. path=" + info.path);
						result.addMessages(getRS("removeMeta", info.path));
					} catch (Exception e) {
						errorForImportMetaDataEntry(info.path, e, result);
						return null;
					}
				}

				//登録処理(Combi)
				for (ImportMetaDataInfo info : normalCombiList) {
					try {
						doImportNormalMetaData(info, importTenant, doAutoReload, result);
					} catch (Exception e) {
						errorForImportMetaDataEntry(info.path, e, result);
						return null;
					}
				}

				//登録処理(通常)
				for (ImportMetaDataInfo info : normalList) {
					try {
						doImportNormalMetaData(info, importTenant, doAutoReload, result);
					} catch (Exception e) {
						errorForImportMetaDataEntry(info.path, e, result);
						return null;
					}
				}

				return null;
			});

			//リロード処理(別トランザクションで実行)
			Transaction.requiresNew(t -> {
				if (needTenantReload) {
					tContextService.reloadTenantContext(ExecuteContext.getCurrentContext().getTenantContext().getTenantId(), false);
					logger.debug("reload tenant context. trigger is metadata import.");
					result.addMessages(getRS("reloadTenantContext"));
				} else if (needMetaContextReload) {
					MetaDataContext.getContext().clearAllCache();
					logger.debug("clear metadata context. trigger is metadata import.");
					result.addMessages(getRS("clearAllMetaDataContext"));
				} else {
					//それ以外の場合は個別に反映される
					logger.debug("update metadata context only target. trigger is metadata import.");
					result.addMessages(getRS("updateMetaDataContext"));
				}

			});

		} finally {
			MetaDataContext.getContext().removeMetaDataContextListener(listener);
		}
	}

	private void doImportNormalMetaData(ImportMetaDataInfo info, Tenant importTenant,
			boolean doAutoReload, MetaDataImportResult result) {

		if (info.metaData instanceof MetaTenant) {
			Tenant currentTenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			String tenantPath = new MetaTenantService.TypeMap().toPath(currentTenant.getName());
			if (importTenant == null) {
				//テナントはスキップする
				logger.debug("Tenant metadata skipped import.");
				result.addMessages(getRS("tenantImportSkip", tenantPath));
			} else {
				//テナントの更新(info.metaDataではなく、importTenantで更新)
				MetaTenant updateMetaTenant = new MetaTenant(importTenant);

				//ID、Nameを更新対象で上書きする
				MetaTenantHandler handler = metaTenantService.getRuntimeByName(currentTenant.getName());
				updateMetaTenant.setId(handler.getMetaData().getId());
				updateMetaTenant.setName(handler.getMetaData().getName());

				importHandler.updateMetaData(tenantPath, updateMetaTenant, info.entry, doAutoReload);

				logger.debug("metadata updated. path=" + tenantPath);
				result.addMessages(getRS("tenantImport", tenantPath));
			}
		} else {
			if (info.status.isInsert()) {
				importHandler.storeMetaData(info.path, info.metaData, info.entry, doAutoReload);

				logger.debug("metadata stored. path=" + info.path);
				result.addMessages(getRS("insertMeta", info.path));
			} else if (info.status.isUpdate()) {
				importHandler.updateMetaData(info.path, info.metaData, info.entry, doAutoReload);

				logger.debug("metadata updated. path=" + info.path);
				result.addMessages(getRS("updateMeta", info.path));
			}
		}
	}

	private void doImportIndividualMetaData(
			final List<ImportMetaDataInfo> individualCombiList, final List<ImportMetaDataInfo> individualList,
			final MetaDataImportResult result) {

		if (individualList.isEmpty()) {
			return;
		}

		//見やすくするためソート
		Collections.sort(individualList, new ImportMetaDataInfoComparator());

		Transaction.requiresNew(t -> {
			final ExecuteContext current = ExecuteContext.getCurrentContext();
			TenantContext tenantContext = tContextService.getTenantContext(ExecuteContext.getCurrentContext().getCurrentTenant().getId());
			ExecuteContext.executeAs(tenantContext, () -> {
				ExecuteContext.getCurrentContext().setLanguage(current.getLanguage());

				//特殊メタデータについては個別にリロードする
				boolean doAutoReload = true;

				//現状は、Entityの更新またはコンビ更新(追加)が対象

				//登録処理(Combi)
				for (ImportMetaDataInfo info : individualCombiList) {
					try {
						doImportIndividualMetaData(info, doAutoReload, result);
					} catch (Exception e) {
						errorForImportMetaDataEntry(info.path, e, result);
						return null;
					}
				}

				//登録処理(通常)
				for (ImportMetaDataInfo info : individualList) {
					try {
						doImportIndividualMetaData(info, doAutoReload, result);
					} catch (Exception e) {
						errorForImportMetaDataEntry(info.path, e, result);
						return null;
					}
				}
				return null;
			});

			return null;
		});
	}

	private void doImportIndividualMetaData(ImportMetaDataInfo info, boolean doAutoReload, MetaDataImportResult result) {
		if (info.status.isInsert()) {
			importHandler.storeMetaData(info.path, info.metaData, info.entry, doAutoReload);

			logger.debug("metadata stored. path=" + info.path);
			result.addMessages(getRS("insertMeta", info.path));
		} else if (info.status.isUpdate()) {
			importHandler.updateMetaData(info.path, info.metaData, info.entry, doAutoReload);

			logger.debug("metadata updated. path=" + info.path);
			result.addMessages(getRS("updateMeta", info.path));
		}
	}

	private void errorForImportMetaDataEntry(String path, Exception e, MetaDataImportResult result) {
		//Exceptionをキャッチするのでロールバック設定
		TransactionManager tm = ManagerLocator.getInstance().getManager(TransactionManager.class);
		Transaction t = tm.currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE && !t.isRollbackOnly()) {
			logger.debug("set transaction rollback.");
			t.setRollbackOnly();
		}

		logger.error("meta data import failed. target path=" + path, e);

		result.setError(true);
		List<String> msgStack = result.getMessages();
		result.clearMessages();
		result.addMessages(getRS("errorImportMetaData", path));
		result.addMessages(getRS("causeTitle", e.getMessage()));
		if (msgStack != null) {
			result.addMessages(getRS("logTitle"));
			result.addMessages("-----------------------------------------");
			result.addMessages(msgStack);
			result.addMessages("-----------------------------------------");
		}
	}

	/**
	 * 
	 * <p>
	 * メタデータ新規作成や定義名変更した場合、別トランザクションで
	 * {@link org.iplass.mtp.impl.metadata.MetaDataContext#checkState(String) checkState}を実行しないと</br>
	 * MetaDataRuntimeが見つからないエラーが返ってきてしまって、実際はcheckStatusエラーではないのcheckStatusエラーになってしまう</br>
	 * なので、新規トランザクションでcheckStatusを実行する
	 * </p>
	 * 
	 * @param result インポート結果
	 * @param entryList インポートしたメタデータ
	 */
	private void addCheckStatusResult(final MetaDataImportResult result, final List<MetaDataEntry> entryList) {
		if (CollectionUtils.isEmpty(entryList)) {
			return;
		}

		List<String> errorPathList = Transaction.requiresNew(t -> {
			return entryList.stream().filter(entry -> {
				String path = entry.getPath();
				if (StringUtil.isEmpty(path)) {
					return false;
				}

				try {
					MetaDataContext.getContext().checkState(entry.getPath());
					return false;
				} catch (MetaDataIllegalStateException e) {
					return true;
				}
			}).map(MetaDataEntry::getPath).toList();
		});

		if (CollectionUtils.isEmpty(errorPathList)) {
			return;
		}

		// TODO メッセージにする
		result.addMessages("-----------------------------------------");
		result.addMessages("以下インポートしたメタデータに不整合が発生している可能性があります。");
		result.addMessages("詳しくはStatusCheckで確認してください。");

		errorPathList.forEach(path -> {
			result.addMessages(String.format("[%1$s]", path));
		});
	}

	private void writeHeader(PrintWriter writer) {
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		writer.println("<metaDataList>");
	}

	private void writeFooter(PrintWriter writer) {
		writer.println("</metaDataList>");
	}

	private void writeMetaDataEntry(PrintWriter writer, Marshaller marshaller, MetaDataEntry entry) throws JAXBException {
		XmlResourceMetaDataEntryThinWrapper meta = new XmlResourceMetaDataEntryThinWrapper(entry.getMetaData());
		meta.setOverwritable(entry.isOverwritable());
		meta.setSharable(entry.isSharable());
		meta.setDataSharable(entry.isDataSharable());
		meta.setPermissionSharable(entry.isPermissionSharable());

		marshaller.marshal(meta, writer);
		writer.println();
	}

	private String getContextPath(String path, String name) {
		//pathからnameを抜いた部分がcontextPathとして扱う

		String checkName = name.replace(".", "/");
		String contextPath = null;
		if (path.endsWith(checkName)) {
			contextPath = path.substring(0, path.length() - checkName.length() - 1);
		} else if (path.endsWith(name)) {
			contextPath = path.substring(0, path.length() - name.length() - 1);
		} else {
			contextPath = path;
		}
		return contextPath;
	}

	//-----------------------------------------------
	//XmlResourceMetaDataRepositoryから移植 START
	//TODO XmlResourceMetaDataRepositoryと統合
	//-----------------------------------------------
	private XMLEntryInfo parse(MetaDataEntryList metaList) {
		XMLEntryInfo entryInfo = new XMLEntryInfo();
		if (metaList.getContextPath() != null) {
			for (ContextPath context : metaList.getContextPath()) {
				parseContextPath(entryInfo, context, "", context.getName());
			}
		}
		return entryInfo;
	}

	private void parseContextPath(XMLEntryInfo entryInfo, ContextPath context, String prefixPath, String rootPath) {
		if (context.getContextPath() != null) {
			for (ContextPath child : context.getContextPath()) {
				parseContextPath(entryInfo, child, prefixPath + context.getName() + "/", rootPath);
			}
		}

		if (context.getEntry() != null) {
			for (XmlResourceMetaDataEntryThinWrapper xmlEntry : context.getEntry()) {

				if (xmlEntry.getMetaData() == null) {
					//現在存在しないMetaDataが指定されたなどして、MetaDataが読めていない可能性。
					String path = xmlEntry.getName();
					if (path == null) {
						path = prefixPath + context.getName() + "/null(unknown)";
					}
					logger.warn(path + "'s Entry is null, maybe Old(No longer available) MetaData Type specified.");
					continue;
				}

				//pathがMetaDataEntryのnameとして指定されている場合はそれを利用
				String path = xmlEntry.getName();
				if (path == null) {
					//指定されていない場合はContextPathから生成
					path = convertPath(prefixPath + context.getName() + "/" + xmlEntry.getMetaData().getName());
				}

				MetaDataEntry entry = new MetaDataEntry(path, xmlEntry.getMetaData(), State.VALID, 0, xmlEntry.isOverwritable(), xmlEntry.isSharable(),
						xmlEntry.isDataSharable(), xmlEntry.isPermissionSharable());

				entryInfo.putPathEntry(path, entry);
				if (StringUtil.isEmpty(entry.getMetaData().getId())) {
					//IDが未指定のものは別で保持(Import時に採番される)
					entryInfo.addIdBlankEntry(entry);
				} else {
					entryInfo.putIdEntry(entry.getMetaData().getId(), entry);
				}

			}
		}
	}

	private String convertPath(String path) {
		//FIXME configから取得するほうがいいかも
		if (path.startsWith(EntityService.ENTITY_META_PATH)
				|| path.startsWith(EntityViewService.ENTITY_META_PATH)
				|| path.startsWith(EntityFilterService.META_PATH)
				|| path.startsWith(EntityWebApiService.META_PATH)
				|| path.startsWith(GroovyScriptService.UTILITY_CLASS_META_PATH)) {
			return path.replace(".", "/");
		}
		return path;
	}

	//-----------------------------------------------
	//XmlResourceMetaDataRepositoryから移植 END
	//-----------------------------------------------

	private String getRS(String suffix, Object... arguments) {
		return ToolsResourceBundleUtil.resourceString("metaport." + suffix, arguments);
	}

	private class ImportMetaDataInfo {
		String path;
		RootMetaData metaData;
		MetaDataEntry entry; /* 削除の場合は未設定 */
		MetaDataImportStatus status; /* 削除の場合は未設定 */

		public ImportMetaDataInfo(String path, RootMetaData metaData) {
			this.path = path;
			this.metaData = metaData;
		}

		public ImportMetaDataInfo(String path, RootMetaData metaData, MetaDataEntry entry, MetaDataImportStatus status) {
			this.path = path;
			this.metaData = metaData;
			this.entry = entry;
			this.status = status;
		}
	}

	private class ImportMetaDataInfoComparator implements Comparator<ImportMetaDataInfo> {

		@Override
		public int compare(ImportMetaDataInfo o1, ImportMetaDataInfo o2) {
			if (o1 == null && o2 == null) {
				return 0;
			} else if (o1 == null) {
				return 1;
			} else if (o2 == null) {
				return -1;
			}
			return o1.path.compareTo(o2.path);
		}
	}

	@Override
	public void patchEntityData(final PatchEntityDataParameter param) {
		ConfigImpl newConfig = new ConfigImpl("forPatchNew", new NameValue[] { new NameValue("filePath", param.getNewMetaDataFilePath()) }, null);
		newConfig.addDependentService(MetaDataJAXBService.class.getName(), jaxbService);
		XmlResourceMetaDataStore newRepo = new XmlResourceMetaDataStore();
		newRepo.inited(null, newConfig);

		ConfigImpl oldConfig = new ConfigImpl("forPatchOld", new NameValue[] { new NameValue("filePath", param.getOldMetaDataFilePath()) }, null);
		oldConfig.addDependentService(MetaDataJAXBService.class.getName(), jaxbService);
		XmlResourceMetaDataStore oldRepo = new XmlResourceMetaDataStore();
		oldRepo.inited(null, oldConfig);

		Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).newTransaction();
		try {
			List<MetaDataEntryInfo> entityList = newRepo.definitionList(param.getTenantId(), "/entity");
			if (entityList != null) {
				entityList.forEach(entry -> {
					MetaEntity newMetaEntity = (MetaEntity) newRepo.load(param.getTenantId(), entry.getPath()).getMetaData();
					MetaEntity oldMetaEntity = (MetaEntity) oldRepo.load(param.getTenantId(), entry.getPath()).getMetaData();

					if (oldMetaEntity != null) {
						StoreService storeService = ServiceRegistry.getRegistry().getService(StoreService.class);
						DataStore srds = storeService.getDataStore();
						EntityContext ec = EntityContext.getCurrentContext();
						srds.getApplyMetaDataStrategy().patchData(newMetaEntity, oldMetaEntity, ec,
								EntityContext.getCurrentContext().getLocalTenantId());
					}
				});
			}
		} catch (Exception e) {
			if (t.getStatus() == TransactionStatus.ACTIVE) {
				t.rollback();
				throw e;
			}
		} finally {
			if (t.getStatus() == TransactionStatus.ACTIVE) {
				t.commit();
			}
		}
	}

	@Override
	public void patchEntityDataWithPrivilegedAuth(final PatchEntityDataParameter param) {
		authService.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () -> {
			patchEntityData(param);
			return null;
		});
	}

	@Override
	public void patchEntityDataWithUserAuth(final PatchEntityDataParameter param, String userId, String password) {
		try {
			authService.login(StringUtil.isNotEmpty(password) ? new IdPasswordCredential(userId, password) : new InternalCredential(userId));
			authService.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
				patchEntityData(param);
				return null;
			});
		} finally {
			authService.logout();
		}
	}

}
