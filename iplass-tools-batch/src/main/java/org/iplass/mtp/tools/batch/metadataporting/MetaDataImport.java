/*
 * Copyright 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadataporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.strategy.ApplyMetaDataStrategy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataJAXBService;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.xmlresource.ContextPath;
import org.iplass.mtp.impl.metadata.xmlresource.MetaDataEntryList;
import org.iplass.mtp.impl.metadata.xmlresource.XmlResourceMetaDataEntryThinWrapper;
import org.iplass.mtp.impl.view.filter.MetaEntityFilter;
import org.iplass.mtp.impl.view.generic.MetaEntityView;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;


public class MetaDataImport extends MtpCuiBase {

	/** テナントID */
	private int tenantId = 154;
	/** インポートファイル名 */
	private String importFileName = "./drpc-metadata-user.xml";

	/** サイレントモード設定 */
	private boolean silent = false;	//デフォルトは確認しながら実行


	/** 実行時の上書き設定 */
	private boolean allOverwrite = false;

	private boolean allReset = false;

//	private RdbMetaDataStore rdb = ServiceRegistry.getRegistry().getService("RdbMetaDataRepository");
	private MetaDataRepository repos = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
	private MetaDataStore rdb = repos.getTenantLocalStore();
	private MetaDataJAXBService jaxb = ServiceRegistry.getRegistry().getService(MetaDataJAXBService.class);
	private ApplyMetaDataStrategy applyMetaStrategy = ServiceRegistry.getRegistry().getService(StoreService.class).getDataStore().getApplyMetaDataStrategy();

	public static void main(String[] args) throws Exception {
		MetaDataImport instance = null;
		try {
			instance = new MetaDataImport(args);

			instance.importFile();

		} finally {
//			if (instance != null) {
//				instance.showLogMessage();
//			}
		}
	}

	/**
	 * tenantId
	 * importFileName
	 * silent("silent" is true)
	 **/
	public MetaDataImport(String... args) {
		//TODO UserUtilを使って、既存のユーザとして登録（可能であれば、passの認証も）

		if (args != null) {
			if (args.length > 0) {
				setTenantId(Integer.parseInt(args[0]));
			}
			if (args.length > 1) {
				setImportFileName(args[1]);
			}
			if (args.length > 2) {
				setSilent(args[2].equalsIgnoreCase(SILENT_MODE));
			}
		}
	}


	private boolean importFile() throws Exception {

		clearLog();

		logArguments();

		logInfo("■Start Execute");
		logInfo("");

		setSuccess(false);

		Transaction t = null;
		try {
			File f = new File(getImportFileName());
			if (!f.exists() || !f.isFile()) {
				logError(getImportFileName() + "が読み込めません。");
				return isSuccess();
			}

			JAXBContext jaxbContext = jaxb.getJAXBContext();
			Unmarshaller um = jaxbContext.createUnmarshaller();

			MetaDataEntryList metaDataList = (MetaDataEntryList) um.unmarshal(f);

			TenantContext tContext = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(getTenantId());
			if (tContext == null) {
				logError("テナントID (" + getTenantId() + ")のテナントが見つかりません。");
				return isSuccess();
			}
			if (!isSilent()) {
				System.out.println("tenantId: " + tContext.getTenantId()
						+ " (" + tContext.loadTenantInfo().getName()
						+ ")のメタデータを更新します。(YES/NO)");
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
				String str = console.readLine();
				if (!"YES".equalsIgnoreCase(str)) {
					logInfo("import処理を中止します。");
					return isSuccess();
				}
			}

			ExecuteContext econtext = new ExecuteContext(tContext);
			econtext.setClientId("0000");
			ExecuteContext.initContext(econtext);

			t = ManagerLocator.getInstance().getManager(TransactionManager.class).newTransaction();

			if (metaDataList.getContextPath() != null) {
				for (ContextPath context: metaDataList.getContextPath()) {
					if (!parseContext(econtext, context, "")) {
						break;
					}
				}
			}
			setSuccess(true);

		} catch (Throwable e) {
			logError("エラーが発生しました。 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE && isSuccess() && !t.isRollbackOnly()) {
				t.commit();
			} else {
				t.rollback();
			}
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
		}
		return isSuccess();
	}

	private boolean parseContext(ExecuteContext econtext, ContextPath context, String prefixPath) throws IOException {

		if (context.getContextPath() != null) {
			for (ContextPath child: context.getContextPath()) {
				if (!parseContext(econtext, child, prefixPath + context.getName() + "/")) {
					return false;
				}
			}
		}

		if (context.getEntry() != null) {
			for (XmlResourceMetaDataEntryThinWrapper w: context.getEntry()) {
				RootMetaData meta = w.getMetaData();

				//pathはEntryのnameが指定されている場合は、それを利用。
				//指定されていない場合はContextPathを利用
				String path = w.getName();
				if (path == null) {
					if (meta instanceof MetaEntity
							|| meta instanceof MetaEntityFilter
							|| meta instanceof MetaEntityView) {
						//上記３つは階層を「.」で表すため変換
						path = context.getName() + "/" + convertPath(meta.getName());
					} else {
						//それ以外は「.」は階層を表さないためそのまま結合
						path = context.getName() + "/" + meta.getName();
					}
				}

				//IDが同じMetaDataがすでに登録されているかのチェック
				MetaDataEntry preID = rdb.loadById(tenantId, meta.getId());
				//Nameが同じMetaDataがすでに登録されているかのチェック
				MetaDataEntry prePath = rdb.load(tenantId, path);

				if (preID != null) {
					//更新
					if (!isSilent() && !isAllOverwrite()) {
						System.out.println("「" + meta.getName() + "(" + meta.getId() + ")」は、すでに同一IDで登録されています。\n"
								+ "更新してよろしいですか？(YES/ALL/SKIP/ABORT)");
						BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
						String str = console.readLine();
						if (!"YES".equalsIgnoreCase(str)) {
							if ("ABORT".equalsIgnoreCase(str)) {
								logInfo("import処理を中止します。");
								return false;
							} else if ("ALL".equalsIgnoreCase(str)) {
								setAllOverwrite(true);
							} else {
								logInfo("「" + meta.getName() + "(" + meta.getId() + ")」のimport処理をSKIPします。");
								return true;
							}
						}
					}

					logInfo(("「" + meta.getName() + "(" + meta.getId() + ")」は、すでに同一IDで登録されているため更新します。"));

					if (meta instanceof MetaEntity) {
						//MetaEntityの場合、データの更新もする
						updateMetaEntity((MetaEntity)meta, preID, path, econtext, w.isSharable(), w.isOverwritable());
					} else {
						MetaDataEntry ent = preID.copy();
						ent.setMetaData(meta);
						ent.setSharable(w.isSharable());
						ent.setOverwritable(w.isOverwritable());
						rdb.update(tenantId, ent);
					}
					logInfo("「" + meta.getName() + "(" + meta.getId() + ")」を更新しました。");
				} else {
					if (prePath != null) {
						//更新
						if (!isSilent() && !isAllReset()) {
							System.out.println("「" + meta.getName() + "(登録済ID:" + prePath.getMetaData().getId() + ")"
									+ "(ImportID：" + meta.getId() + ")"
									+ "(Path:" + path + ")」は、IDが異なりますが、同一Pathで登録されています。\n"
									+ "既存のメタデータを削除後、新規でインポートを行ってよろしいですか？(YES/ALL/SKIP/ABORT)");
							BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
							String str = console.readLine();
							if (!"YES".equalsIgnoreCase(str)) {
								if ("ABORT".equalsIgnoreCase(str)) {
									logInfo("import処理を中止します。");
									return false;
								} else if ("ALL".equalsIgnoreCase(str)) {
									setAllReset(true);
								} else {
									logInfo("「" + meta.getName() + "(" + path + ")」のimport処理をSKIPします。");
									return true;
								}
							}
						}

						logInfo(("「" + meta.getName() + "(" + prePath.getMetaData().getId() + ")」を削除します。"));

						rdb.remove(tenantId, path);

						logInfo("「" + meta.getName() + "(" + prePath.getMetaData().getId() + ")」を削除しました。");
					}

					//新規作成
					if (meta instanceof MetaEntity) {
						//XMLに定義されている可能性があるので再度検索
						EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
						EntityHandler eh = ehs.getRuntimeById(meta.getId());
						if (eh != null) {
							//XML定義上に存在
							logInfo(("「" + meta.getName() + "(" + meta.getId() + ")」は、すでに同一IDでXML登録されているためRDBにデータを作成します。"));

							MetaEntity preEntity = eh.getMetaData();
							MetaDataEntry preEnt = MetaDataContext.getContext().getMetaDataEntryById(preEntity.getId());

							//MetaEntityの場合、データの更新もする
							updateMetaEntity((MetaEntity)meta, preEnt, path, econtext, w.isSharable(), w.isOverwritable());

							logInfo("RDBメタデータとして新規で「" + meta.getName() + "(" + meta.getId() + ")」を作成しました。");
						} else {
							//純粋に新規作成
							applyMetaStrategy.create((MetaEntity) meta, EntityContext.getCurrentContext()/*, 0*/);

							MetaDataEntry ent = new MetaDataEntry(path, meta, State.VALID, 0, w.isOverwritable(), w.isSharable(), w.isDataSharable(), w.isPermissionSharable());

							rdb.store(tenantId, ent);
							logInfo("新規で「" + meta.getName() + "(" + meta.getId() + ")」を作成しました。");
						}
					} else {
						MetaDataEntry ent = new MetaDataEntry(path, meta, State.VALID, 0, w.isOverwritable(), w.isSharable(), w.isDataSharable(), w.isPermissionSharable());
						rdb.store(tenantId, ent);
						logInfo("新規で「" + meta.getName() + "(" + meta.getId() + ")」を作成しました。");
					}
				}
			}
		}
		return true;
	}

	private void updateMetaEntity(MetaEntity newEntity, MetaDataEntry curEntity, String path, ExecuteContext econtext, boolean share, boolean overwrite) {
		boolean prepare = applyMetaStrategy.prepare(newEntity, (MetaEntity) curEntity.getMetaData(), EntityContext.getCurrentContext());
		if (!prepare) {
			throw new EntityRuntimeException("can not prepare for update Entity:" + curEntity);
		}

		boolean result = false;
		try {

			//SharedTenantのデータの場合は、複数のテナントを一括で更新する必要あり
			int tenantId = econtext.getClientTenantId();
			int[] targetTenantIds = null;
			TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
			if (tcService.getSharedTenantId() == tenantId
					&& curEntity.isSharable()
					&& !curEntity.isDataSharable()) {
				List<Integer> allTenantIds = tcService.getAllTenantIdList();
				List<Integer> overwritedTenantIds = MetaDataContext.getContext().getOverwriteTenantIdList(curEntity.getMetaData().getId());
				for (Iterator<Integer> it = allTenantIds.iterator(); it.hasNext();) {
					Integer id = it.next();
					if (overwritedTenantIds.contains(id)) {
						logInfo(id + "'s MetaData:" + curEntity.getPath() + " (" + curEntity.getMetaData().getId() + ") is overwrote. so skip data patch.");
						it.remove();
					}
				}
				targetTenantIds = new int[allTenantIds.size()];
				for (int i = 0; i < allTenantIds.size(); i++) {
					targetTenantIds[i] = allTenantIds.get(i);
				}
			} else {
				targetTenantIds = new int[]{tenantId};
			}

			result = applyMetaStrategy.modify(newEntity, (MetaEntity) curEntity.getMetaData(), EntityContext.getCurrentContext(), targetTenantIds);
			if (result) {
				MetaDataEntry ent = curEntity.copy();
				ent.setMetaData(newEntity);
				ent.setSharable(share);
				ent.setOverwritable(overwrite);
				rdb.update(tenantId, ent);
			}
		} finally {
			applyMetaStrategy.finish(result, newEntity, (MetaEntity) curEntity.getMetaData(), EntityContext.getCurrentContext());
		}
	}

	/**
	 * <p>Pathを有効な値に変換します。</p>
	 *
	 * Entityのnameは「.」区切りなので、nameをそのまま渡された場合などを考慮して変換
	 *
	 * @param path パス
	 * @return 有効なパス
	 */
	private String convertPath(String path) {
		return path.replace(".","/");
	}

	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + getTenantId());
		logInfo("\timport file :" + getImportFileName());
		logInfo("\tsilent mode :" + (isSilent() ? "SILENT" : "NOT SILENT"));
		logInfo("");
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	private boolean isAllOverwrite() {
		return allOverwrite;
	}

	private void setAllOverwrite(boolean allOverwrite) {
		this.allOverwrite = allOverwrite;
	}

	private boolean isAllReset() {
		return allReset;
	}

	private void setAllReset(boolean allReset) {
		this.allReset = allReset;
	}
}
