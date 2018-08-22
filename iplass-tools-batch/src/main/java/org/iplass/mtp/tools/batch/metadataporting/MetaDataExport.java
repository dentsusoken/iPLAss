/*
 * Copyright 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadataporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataJAXBService;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.impl.metadata.xmlresource.XmlResourceMetaDataEntryThinWrapper;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;

public class MetaDataExport extends MtpCuiBase {

	//テナントID
	private int tenantId = -1;

	//XML出力ディレクトリパス
	private String exportDir = "./";

	//出力対象MetaDataパス（未指定の場合は全て対象）
	private String[] metaDataPathes = {
//		"/tenant",
//		"/entity",
//		"/view/generic",
//		"/view/filter",
//		"/template",
//		"/commandClass",
//		"/action",
//		"/view/menu/item",
//		"/view/menu/tree",
//		"/message",
//		"/view/calendar",
	};

	/** サイレントモード設定 */
	private boolean silent = false;	//デフォルトは確認しながら実行

//	private RdbMetaDataStore rdb = ServiceRegistry.getRegistry().getService("RdbMetaDataRepository");
	private MetaDataRepository repos = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
	private MetaDataStore rdb = repos.getTenantLocalStore();
	private MetaDataJAXBService jaxb = ServiceRegistry.getRegistry().getService(MetaDataJAXBService.class);


	public static void main(String[] args) throws Exception {
		MetaDataExport instance = null;
		try {
			instance = new MetaDataExport(args);

			instance.export();

		} finally {
//			if (instance != null) {
//				instance.showLogMessage();
//			}
		}
	}

	/**
	 * tenantId
	 * exportDir
	 * metaDataPathes
	 * silent("silent" is true)
	 **/
	public MetaDataExport(String... args) {
		//TODO UserUtilを使って、既存のユーザとして登録（可能であれば、passの認証も）
		if (args != null) {
			if (args.length > 0) {
				setTenantId(Integer.parseInt(args[0]));
			}
			if (args.length > 1) {
				setExportDir(args[1]);
			}
			if (args.length > 2) {
				setMetaDataPathes(args[2].split(","));
			}
			if (args.length > 3) {
				setSilent(args[3].equalsIgnoreCase(SILENT_MODE));
			}
		}
	}

	private void output(Marshaller m, PrintWriter w, MetaDataEntry meta) throws Exception {
		XmlResourceMetaDataEntryThinWrapper entry = new XmlResourceMetaDataEntryThinWrapper(meta.getMetaData());
		//Entryのnameにパスをセット
		entry.setName(meta.getPath());
		entry.setOverwritable(meta.isOverwritable());
		entry.setSharable(meta.isSharable());
		m.marshal(entry, w);
		w.println();
		logInfo("\t" + meta.getPath() + "を出力しました。");
	}

	private boolean export() throws Exception {

		clearLog();

		logArguments();

		logInfo("■Start Execute");
		logInfo("");

		setSuccess(false);

		Transaction t = null;
		try {

			TenantContext tContext = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(getTenantId());
			if (tContext == null) {
				logError("テナントID (" + getTenantId() + ")のテナントが見つかりません。");
				return isSuccess();
			}
			if (!isSilent()) {
				System.out.println("tenantId: " + tContext.getTenantId()
						+ " (" + tContext.loadTenantInfo().getName()
						+ ")のメタデータをexportします。(YES/NO)");
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
				String str = console.readLine();
				if (!"YES".equalsIgnoreCase(str)) {
					logInfo("export処理を中止します。");
					return isSuccess();
				}
			}

			JAXBContext jaxbContext = jaxb.createJAXBContext(XmlResourceMetaDataEntryThinWrapper.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty("jaxb.formatted.output", Boolean.TRUE);
			m.setProperty("jaxb.fragment", Boolean.TRUE);

			String fileName =  tenantId + "-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".xml";
			logInfo(exportDir + "に" + fileName + "を出力します。");

			File dir = new File(exportDir);
			File exportFile = new File(dir, fileName);

			t = ManagerLocator.getInstance().getManager(TransactionManager.class).newTransaction();

			PrintWriter writer = new PrintWriter(exportFile, "UTF-8");
			try {
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
				writer.println("<metaDataList>");

				if (metaDataPathes.length > 0) {
					//個別に指定されている場合
					for (String path: metaDataPathes) {
						logInfo(path + "を出力します。");
						writer.println("<contextPath name=\"" + path + "\">");
						List<MetaDataEntryInfo> list = rdb.definitionList(tenantId, path + "/");
						for (MetaDataEntryInfo entry: list) {
							MetaDataEntry meta = rdb.load(tenantId, entry.getPath());
							output(m, writer, meta);
						}
						writer.println("</contextPath>");
					}
				} else {
					//全MetaData出力
					List<MetaDataEntryInfo> list = rdb.definitionList(tenantId, "/");
					String beforeContextPath = "";
					for (MetaDataEntryInfo entry: list) {
						MetaDataEntry meta = rdb.load(tenantId, entry.getPath());
						String contextPath = getContextPath(entry.getPath(), meta.getMetaData().getName());
						if (!beforeContextPath.equals(contextPath)) {
							if (!beforeContextPath.isEmpty()) {
								writer.println("</contextPath>");
							}
							logInfo(contextPath + "を出力します。");
							beforeContextPath = contextPath;
							writer.println("<contextPath name=\"" + contextPath + "\">");
						}
						output(m, writer, meta);
					}
					writer.println("</contextPath>");
				}

				writer.println("</metaDataList>");

			} finally {
				if (writer != null) {
					writer.close();
				}
			}
			setSuccess(true);

		} catch (Throwable e) {
			logError("エラーが発生しました。 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
				t.rollback();
			}

			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
		}
		return isSuccess();
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

	private void logArguments() {
		logInfo("■Execute Argument");
		logInfo("\ttenant id :" + getTenantId());
		logInfo("\texport dir :" + getExportDir());
		logInfo("\tMetaDataPathes :" + getMetaDataPathesString());
		logInfo("\tsilent mode :" + (isSilent() ? "SILENT" : "NOT SILENT"));
		logInfo("");
	}


	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public String getExportDir() {
		return exportDir;
	}

	public void setExportDir(String exportDir) {
		this.exportDir = exportDir;
	}

	public String[] getMetaDataPathes() {
		return metaDataPathes;
	}

	public void setMetaDataPathes(String[] metaDataPathes) {
		this.metaDataPathes = metaDataPathes;
	}

	public String getMetaDataPathesString() {
		if (metaDataPathes == null || metaDataPathes.length == 0) {
			return "ALL";
		}
		StringBuilder buf = new StringBuilder();
		for (String path : metaDataPathes) {
			buf.append(path + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}
}
