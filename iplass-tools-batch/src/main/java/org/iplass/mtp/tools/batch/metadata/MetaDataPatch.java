/*
 * Copyright 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.core.config.ConfigImpl;
import org.iplass.mtp.impl.core.config.NameValue;
import org.iplass.mtp.impl.datastore.DataStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.strategy.ApplyMetaDataStrategy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataJAXBService;
import org.iplass.mtp.impl.metadata.xmlresource.XmlResourceMetaDataStore;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;


public class MetaDataPatch {
	private static int tenantId;//FIXME 間違わないこと！！
	private static String newDefaultMetaData;
	private static String oldDefaultMetaData;
	private static boolean silent = false;

	static MetaDataJAXBService jaxb = ServiceRegistry.getRegistry().getService(MetaDataJAXBService.class);
	static ApplyMetaDataStrategy applyMetaStrategy = ServiceRegistry.getRegistry().getService(StoreService.class).getDataStore().getApplyMetaDataStrategy();

	public static void main(String[] args) throws Exception {
		//TODO UserUtilを使って、既存のユーザとして登録（可能であれば、passの認証も）

		if (args.length > 0) {
			tenantId = Integer.parseInt(args[0]);
		}
		if (args.length > 1) {
			newDefaultMetaData = args[1];
		}
		if (args.length > 2) {
			oldDefaultMetaData = args[2];
		}
		if (args.length > 3) {
			if (args[3].equalsIgnoreCase("silent")) {
				silent = true;
			}
		}
		patchData();
	}

	private static void patchData() throws Exception {
		TenantContext tContext = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);

		if (!silent) {
			System.out.println("tenantId: " + tContext.getTenantId() + " (" + tContext.loadTenantInfo().getName() + ")のデータを" + oldDefaultMetaData + "から、" + newDefaultMetaData + "へデータをパッチします。(YES/NO)");
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			String str = console.readLine();
			if (!str.equals("YES")) {
				System.out.println("abort!");
				return;
			}
		}

		ConfigImpl newConfig = new ConfigImpl("forPatchNew", new NameValue[] {new NameValue("filePath", newDefaultMetaData)});
		newConfig.addDependentService(MetaDataJAXBService.class.getName(), jaxb);
		XmlResourceMetaDataStore newRepo = new XmlResourceMetaDataStore();
		newRepo.inited(null, newConfig);

		ConfigImpl oldConfig = new ConfigImpl("forPatchOld", new NameValue[] {new NameValue("filePath", oldDefaultMetaData)});
		oldConfig.addDependentService(MetaDataJAXBService.class.getName(), jaxb);
		XmlResourceMetaDataStore oldRepo = new XmlResourceMetaDataStore();
		oldRepo.inited(null, oldConfig);


		final ExecuteContext econtext = new ExecuteContext(tContext);
		econtext.setClientId("0000");
		ExecuteContext.initContext(econtext);

		Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).newTransaction();
		try {
			List<MetaDataEntryInfo> entityList = newRepo.definitionList(tContext.getTenantId(), "/entity");
			if (entityList != null) {
				for (MetaDataEntryInfo entry : entityList) {
					MetaEntity newMetaEntity = (MetaEntity) newRepo.load(tContext.getTenantId(), entry.getPath()).getMetaData();
					MetaEntity oldMetaEntity = (MetaEntity) oldRepo.load(tContext.getTenantId(), entry.getPath()).getMetaData();

					if (oldMetaEntity != null) {
						StoreService storeService = ServiceRegistry.getRegistry().getService(StoreService.class);
						DataStore srds = storeService.getDataStore();
						EntityContext ec = EntityContext.getCurrentContext();
						srds.getApplyMetaDataStrategy().patchData(newMetaEntity, oldMetaEntity, ec, EntityContext.getCurrentContext().getLocalTenantId());
					}
				}
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
		System.out.println("done!");
	}

}
