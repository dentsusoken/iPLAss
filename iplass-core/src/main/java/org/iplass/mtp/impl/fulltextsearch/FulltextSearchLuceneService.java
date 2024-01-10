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

package org.iplass.mtp.impl.fulltextsearch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedSetSortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.InfoStream;
import org.apache.tika.exception.EncryptedDocumentException;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchCondition;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchOption;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryNameTypeParser;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryReferenceParseException;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryReferenceParser;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class FulltextSearchLuceneService extends AbstractFulltextSeachService {

	private static Logger logger = LoggerFactory.getLogger(FulltextSearchLuceneService.class);

	private static final String[] searchFields = new String[] {
			"OPT_OBJ_ID", "OPT_OBJ_VER", "STATUS", "DEL_FLG",
			"OBJ_NAME", "OBJ_DESC", "CRE_DATE", "UP_DATE", "S_DATE", "E_DATE", "LOCK_USER", "CRE_USER", "UP_USER",
			"V_1", "V_2", "V_3", "V_4", "V_5", "V_6", "V_7", "V_8", "V_9", "V_10", "V_11", "V_12",
			"V_13", "V_14", "V_15", "V_16", "V_17", "V_18", "V_19", "V_20","V_21", "V_22", "V_23",
			"V_24", "V_25", "V_26", "V_27", "V_28", "V_29", "V_30", "V_31", "V_32", "V_33", "V_34",
			"V_35", "V_36", "V_37", "V_38", "V_39", "V_40", "V_41", "V_42", "V_43", "V_44", "V_45",
			"V_46", "V_47", "V_48", "V_49", "V_50", "V_51", "V_52", "V_53", "V_54", "V_55", "V_56",
			"V_57", "V_58", "V_59", "V_60", "V_61", "V_62", "V_63", "V_64", "V_65", "V_66", "V_67",
			"V_68", "V_69", "V_70", "V_71", "V_72", "V_73", "V_74", "V_75", "V_76", "V_77", "V_78",
			"V_79", "V_80", "V_81", "V_82", "V_83", "V_84", "V_85", "V_86", "V_87", "V_88", "V_89",
			"V_90", "V_91", "V_92", "V_93", "V_94", "V_95", "V_96", "V_97", "V_98", "V_99", "V_100",
			"V_101", "V_102", "V_103", "V_104", "V_105", "V_106", "V_107", "V_108", "V_109", "V_110",
			"V_111", "V_112", "V_113", "V_114", "V_115", "V_116", "V_117", "V_118", "V_119", "V_120",
			"V_121", "V_122", "V_123", "V_124", "V_125", "V_126", "V_127", "V_128", "V_129", "V_130",
			"V_131", "V_132", "V_133", "V_134", "V_135", "V_136", "V_137", "V_138", "V_139", "V_140",
			"V_141", "V_142", "V_143", "V_144", "V_145", "V_146", "V_147", "V_148", "V_149", "V_150",
			"V_151", "V_152", "V_153", "V_154", "V_155", "V_156", "V_157", "V_158", "V_159", "V_160",
			"V_161", "V_162", "V_163", "V_164", "V_165", "V_166", "V_167", "V_168", "V_169", "V_170",
			"V_171", "V_172", "V_173", "V_174", "V_175", "V_176", "V_177", "V_178", "V_179", "V_180",
			"V_181", "V_182", "V_183", "V_184", "V_185", "V_186", "V_187", "V_188", "V_189", "V_190",
			"V_191", "V_192", "V_193", "V_194", "V_195", "V_196", "V_197", "V_198", "V_199", "V_200",
			"V_201", "V_202", "V_203", "V_204", "V_205", "V_206", "V_207", "V_208", "V_209", "V_210",
			"V_211", "V_212", "V_213", "V_214", "V_215", "V_216", "V_217", "V_218", "V_219", "V_220",
			"V_221", "V_222", "V_223", "V_224", "V_225", "V_226", "V_227", "V_228", "V_229", "V_230",
			"V_231", "V_232", "V_233", "V_234", "V_235", "V_236", "V_237", "V_238", "V_239", "V_240",
			"V_241", "V_242", "V_243", "V_244", "V_245", "V_246", "V_247", "V_248", "V_249", "V_250",
			"V_251", "V_252", "V_253", "V_254", "V_255", "V_256",
			"R_1", "R_2", "R_3", "R_4", "R_5", "R_6", "R_7", "R_8", "R_9", "R_10", "R_11", "R_12",
			"R_13", "R_14", "R_15", "R_16", "R_17", "R_18", "R_19", "R_20", "R_21", "R_22", "R_23",
			"R_24", "R_25", "R_26", "R_27", "R_28", "R_29", "R_30", "R_31", "R_32", "R_33", "R_34",
			"R_35", "R_36", "R_37", "R_38", "R_39", "R_40", "R_41", "R_42", "R_43", "R_44", "R_45",
			"R_46", "R_47", "R_48", "R_49", "R_50", "R_51", "R_52", "R_53", "R_54", "R_55", "R_56",
			"R_57", "R_58", "R_59", "R_60", "R_61", "R_62", "R_63", "R_64",
			"E_1", "E_2", "E_3", "E_4", "E_5", "E_6", "E_7", "E_8", "E_9", "E_10", "E_11", "E_12",
			"E_13", "E_14", "E_15", "E_16", "E_17", "E_18", "E_19", "E_20", "E_21", "E_22", "E_23",
			"E_24", "E_25", "E_26", "E_27", "E_28", "E_29", "E_30", "E_31", "E_32"};

	private Directory directory;
	private Analyzer analyzer;
	private Operator defaultOperator;
	private double indexWriterRAMBufferSizeMB;
	private long redundantTimeMinutes;
	private int indexWriterCommitLimit;

	private List<BinaryReferenceParser> binaryParsers;
	private int binaryParseLimitLength = 100000;

	private volatile SearcherManager searcherManager;
	private long searcherAutoRefreshTimeMinutes = -1;
	private volatile Timer timer;

	@Override
	public void destroy() {
		if (isUseFulltextSearch()) {
			if (timer != null) {
				timer.cancel();
			}
			try {
				if (searcherManager != null) {
					searcherManager.close();
				}
			} catch (IOException e) {
				logger.warn("Error occured when closing Searcher Manager");
			}
			searcherManager = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		super.init(config);

		if (isUseFulltextSearch()) {
			try {
				Path path = new File(config.getValue("directory")).toPath();
				String className = config.getValue("luceneFSDirectory");
				if (StringUtil.isNotEmpty(className) && FSDirectory.class.isAssignableFrom(Class.forName(className))) {
					String maxChunkSizeMB = config.getValue("luceneFSDirectoryMaxChunkSizeMB");
					if (MMapDirectory.class.getName().equals(className) && StringUtil.isNotEmpty(maxChunkSizeMB)) {
						directory = new MMapDirectory(path, Integer.parseInt(maxChunkSizeMB));
					} else {
						directory = (Directory) Class.forName(className).getConstructor(Path.class).newInstance(path);
					}
				} else {
					directory = new SimpleFSDirectory(path);
				}

				binaryParsers = (List<BinaryReferenceParser>) config.getBeans("binaryParser");
				if (binaryParsers == null) {
					//未指定の場合は、NameTypeParserを設定
					binaryParsers = Collections.singletonList(new BinaryNameTypeParser());
				} else {
					//最後にNameTypeParserを設定
					if (!(binaryParsers.get(binaryParsers.size() - 1) instanceof BinaryNameTypeParser)) {
						binaryParsers.add(new BinaryNameTypeParser());
					}
				}
				binaryParseLimitLength = Integer.parseInt(config.getValue("binaryParseLimitLength"));

				if (StringUtil.isNotEmpty(config.getValue("searcherAutoRefreshTimeMinutes"))) {
					searcherAutoRefreshTimeMinutes = Long.parseLong(config.getValue("searcherAutoRefreshTimeMinutes"));
				}
				if (searcherAutoRefreshTimeMinutes > 0) {
					timer = new Timer("Searcher refresh timer", true);
				}
				try {
					searcherManager = new SearcherManager(directory, new EntitySearcherFactory());
				} catch (IOException e) {
					logger.warn("Failed to create SearcherManager.", e);
				}

			} catch (Exception e) {
				throw new FulltextSearchRuntimeException("Failed to initialize the Lucene index for the directory.", e);
			}
		}

		indexWriterRAMBufferSizeMB = Double.parseDouble(config.getValue("indexWriterRAMBufferSizeMB"));
		redundantTimeMinutes = Long.parseLong(config.getValue("redundantTimeMinutes"));
		indexWriterCommitLimit = Integer.parseInt(config.getValue("indexWriterCommitLimit"));

		try {
			String className = config.getValue("analyzer");
			if (StringUtil.isNotEmpty(className)) {
				analyzer = AnalyzerFactory.createAnalyzer(className, config.getValue("analyzerSetting", AnalyzerSetting.class));
			} else {
				analyzer = (Analyzer) config.getBean("analyzer");
			}
		} catch (Exception e) {
			logger.warn("Analyzer not found. use JapaneseAnalyzer(mode:search). : " + config.getValue("analyzer"), e);
			analyzer = new JapaneseAnalyzer();
		}
		defaultOperator = Operator.valueOf(config.getValue("defaultOperator"));
	}

	@Override
	public void execCrawlEntity(String... defNames) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		if (defNames == null || defNames.length == 0) {
			// init処理で実行するとinterceptorとの絡みで無限ループするのでここでgetする
			EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
			// 対象エンティティの取得
			List<DefinitionSummary> defList = edm.definitionNameList();

			// エンティティ毎にデータをクロールする
			for (final DefinitionSummary def : defList) {
				createIndexData(tenantId, def.getName());
			}
		} else {
			for (String defName : defNames) {
				createIndexData(tenantId, defName);
			}
		}
	}

	@Override
	public void execRefresh() {
		if (isUseFulltextSearch()) {
			try {
				if (searcherManager == null) {
					searcherManager = new SearcherManager(directory, new EntitySearcherFactory());
				}
				searcherManager.maybeRefreshBlocking();
			} catch (AlreadyClosedException | IOException e) {
				throw new FulltextSearchRuntimeException("Error occurred when refreshing searcher.", e);
			}
		}
	}

	private LocaleFormat getLocaleFormat() {
		return ExecuteContext.getCurrentContext().getLocaleFormat();
	}

	private boolean createIndexByQuery(final Query query, final int tenantId, final String objDefId, final EntityIndexWriter writer, final Map<String, String> crawlPropertyNameMap) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		logger.debug("### EQL : " + query.toString() + "###");

		final boolean[] isIndexed = new boolean[]{false};

		em.searchEntity(query, entity -> {

			isIndexed[0] = true;

			String id = tenantId + "_" + objDefId + "_" + entity.getOid() + "_" + entity.getVersion();

			try {
				//更新の場合もあるのでdelete/insert

				Term term = new Term("id", id);
				writer.deleteDocuments(term);

				Document doc = new Document();
				doc.add(new StringField("id", id, Field.Store.YES));
				doc.add(new StringField("tenant_id", Integer.toString(tenantId), Field.Store.YES));
				doc.add(new StringField("def_name", entity.getDefinitionName(), Field.Store.YES));
				doc.add(new SortedDocValuesField("def_name", new BytesRef(entity.getDefinitionName())));
				doc.add(new StringField("OBJ_ID", entity.getOid(), Field.Store.YES));
				if (entity.getVersion() != null) {
					doc.add(new StringField("OBJ_VER", Long.toString(entity.getVersion()), Field.Store.YES));
				}

				for(Map.Entry<String, String> e : crawlPropertyNameMap.entrySet()) {
					// v1～に保存されるプロパティ
					String propName = e.getKey();
					Object val = entity.getValue(propName);
					if (val != null) {
						String fieldName = e.getValue();
						if (val.getClass().isArray()) {
							Object[] valArray = (Object[]) val;
							for (int i = 0; i < valArray.length; i++) {
								String strVal = convertValue(valArray[i]);
								doc.add(new TextField(fieldName, strVal, Field.Store.NO));
							}
						} else {
							String strVal = convertValue(val);
							doc.add(new TextField(fieldName, strVal, Field.Store.NO));
						}
					}
				}

				writer.addDocument(doc);
			} catch (IOException e) {
				throw new FulltextSearchRuntimeException("Exception occured on index creating process.", e);
			}

			return true;

		});

		return isIndexed[0];
	}

	private String convertValue(Object val) throws IOException {
		if (val == null) {
			return "";
		}
		
		String strVal = null;
		if (val instanceof Timestamp) {
			final SimpleDateFormat dateTimeFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDatetimeSecFormat(), true);
			dateTimeFormat.setLenient(false);
			strVal = dateTimeFormat.format((Timestamp) val);
		} else if (val instanceof Date) {
			final SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDateFormat(), false);
			dateFormat.setLenient(false);
			strVal = dateFormat.format((Date) val);
		} else if (val instanceof Time) {
			final SimpleDateFormat timeFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputTimeSecFormat(), false);
			timeFormat.setLenient(false);
			strVal = timeFormat.format((Time) val);
		} else if (val instanceof BigDecimal) {
			strVal = ((BigDecimal) val).toPlainString();
		} else if (val instanceof BinaryReference) {
			strVal = parseBinaryReference((BinaryReference) val);
		} else {
			strVal = val.toString();
		}
		return strVal;
	}

	private String parseBinaryReference(BinaryReference br) throws IOException {

		//順番にサポートしているかをチェック
		for (int i = 0; i < binaryParsers.size(); i++) {
			BinaryReferenceParser support = binaryParsers.get(i).getParser(br);
			if (support != null) {
				try {
					String value = support.parse(br, binaryParseLimitLength);

					//EmptyParserの場合、空なのでチェック
					if (StringUtil.isNotEmpty(value)) {
						logger.debug("binary reference parsed on " + support.getClass().getSimpleName()
								+ ". type=" + br.getType());
						return value;
					}
				} catch(BinaryReferenceParseException e) {
					if (e.getCause() != null && e.getCause() instanceof EncryptedDocumentException) {
						//パスワードエラーの場合は解析できないのでNameTypeParserで出力
						logger.warn("binary reference parse error. document is encrypted. so try to NameTypeParser.");
						i = binaryParsers.size() - 2;
					} else {
						//それ以外の場合はWARNログに出力して、次のParserへ
						logger.warn("binary reference parse error. so try to parse on next paser. type="
								+ br.getType() + ",parser=" + support.getClass().getSimpleName(), e);
					}
				}
			}
		}

		//必ずNameTypeParserが設定されるのでありえない
		throw new FulltextSearchRuntimeException("invalid service status.");
	}

	private void createIndexData(final int tenantId, String defName) {

		// 存在しないdefinitionNameが指定された場合は終了
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(DefinitionService.getInstance().getPath(EntityDefinition.class, defName));
		if (entry == null) {
			logger.warn(defName + " is not found.");
			return;
		}

		// Crawl対象Entityでない場合は終了
		MetaEntity meta = (MetaEntity)entry.getMetaData();
		if (!meta.isCrawl()) {
			logger.debug(defName + " is not crawl target entity.");
			return;
		} else {
			logger.info("start crawl " + defName);
		}


		// Crawl対象プロパティを取得
		List<String> crwalProppertyIdList = meta.getCrawlPropertyId();

		// Crawl対象プロパティが0件以下の場合は終了
		if (crwalProppertyIdList == null || crwalProppertyIdList.isEmpty()) {
			logger.warn(defName + " have no crawl target property. so skip crawl.");
			return;
		}


		AuthContext.doPrivileged(() -> {
			// 対象プロパティのリストを作成する
			Map<String, String> crawlPropertyNameMap = generateCrawlPropInfo(meta);

			Select select = new Select();
			select.add(new EntityField(Entity.OID), new EntityField(Entity.VERSION));
			crawlPropertyNameMap.keySet().forEach(propName -> {
				select.add(new EntityField(propName));
			});

			String objDefId = meta.getId();
			int objDefVer = entry.getVersion();

			// 差分クロールする為にINDEX記録テーブルから最終更新日を取得
			Timestamp fromDBTime = getLastCrawlTimestamp(objDefId, objDefVer);
			//コミットのタイミングで漏れてしまったEntityデータを考慮して、一定時間前の更新日時のデータから更新対象にする
			Timestamp lastUpdate = null;
			if (fromDBTime != null) {
				lastUpdate = new Timestamp(fromDBTime.getTime() - TimeUnit.MINUTES.toMillis(redundantTimeMinutes));
			}
			//index生成日時
			Timestamp now = InternalDateUtil.getNow();
			Timestamp delTargetDatetime = new Timestamp(now.getTime() - TimeUnit.MINUTES.toMillis(redundantTimeMinutes));

			// 取得したMetaの情報をもとにindexを作成（EntityのプロパティとDBのカラムをマッピングさせる）
			try (EntityIndexWriter writer = new EntityIndexWriter()) {

				try {
					// 前回Crawl時から削除されたデータが存在する場合はindexを削除する
					List<RestoreDto> dtoList = getRestoreIndexData(objDefId, delTargetDatetime);

					//削除
					dtoList.stream()
					.filter(dto -> dto.getStatus().equals(Status.DELETE))
					.forEach(dto -> {

						try {
							Term term = new Term("id", dto.getId());
							writer.deleteDocuments(term);
						} catch (IOException e) {
							throw new FulltextSearchRuntimeException("Cant create index cause " + e.toString(), e);
						}
					});

					//リストア
					List<ValueExpression> oidList = new ArrayList<>();

					dtoList.stream()
					.filter(dto -> dto.getStatus().equals(Status.RESTORE))
					.forEach(dto -> {

						oidList.add(new Literal(dto.getObjId()));

						if (oidList.size() == 1000) {
							//Oracle 1000件制限対応
							// oidList（メモリ保持）を解放する目的
							Query query = new Query();
							query.setSelect(select);
							query.from(defName);
							In in = new In(new EntityField(Entity.OID), new ArrayList<ValueExpression>(oidList));
							query.where(in);
							createIndexByQuery(query, tenantId, objDefId, writer, crawlPropertyNameMap);
							oidList.clear();
						}
					});

					if (oidList.size() > 0) {
						Query query = new Query();
						query.setSelect(select);
						query.from(defName);
						In in = new In(new EntityField(Entity.OID), new ArrayList<ValueExpression>(oidList));
						query.where(in);
						createIndexByQuery(query, tenantId, objDefId, writer, crawlPropertyNameMap);
					}

					//追加、更新データ
					// 差分クロールする為のQueryを作成
					Query query = new Query();
					query.setSelect(select);
					query.from(defName);
					if (lastUpdate != null) {
						query.where(new GreaterEqual(Entity.UPDATE_DATE, lastUpdate));
					}
					createIndexByQuery(query, tenantId, objDefId, writer, crawlPropertyNameMap);


					//DeleteLogテーブルから削除
					removeDeleteLog(objDefId, delTargetDatetime);

					// 差分クロールする為にINDEX記録テーブルに最終更新日を登録
					if (lastUpdate == null) {
						// INSERT
						insertCrawlLog(objDefId, objDefVer, now);
					} else {
						// UPDATE
						updateCrawlLog(objDefId, objDefVer, now);
					}
					writer.commit();
				} catch (IOException | RuntimeException e) {
					writer.rollback();
					throw e;
				}
			} catch (IOException e) {
				throw new FulltextSearchRuntimeException("Cant create index cause " + e.toString(), e);
			}
		});
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String searchDefName, String fulltext) {
		FulltextSearchOption option = new FulltextSearchOption();
		if (StringUtil.isNotEmpty(searchDefName)) {
			option.getConditions().put(searchDefName, null);
		}
		return fulltextSearchEntity(fulltext, option);
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Map<String, List<String>> entityProperties, String fulltext) {
		FulltextSearchOption option = new FulltextSearchOption();
		for (Map.Entry<String, List<String>> data : entityProperties.entrySet()) {
			FulltextSearchCondition cond = new FulltextSearchCondition(data.getValue());
			option.getConditions().put(data.getKey(), cond);
		}
		return fulltextSearchEntity(fulltext, option);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String fulltext, FulltextSearchOption option) {
		if (searcherManager == null) {
			return new SearchResult<T>(-1, null);
		}

		MultiFieldQueryParser qp = new MultiFieldQueryParser(searchFields, analyzer);
		qp.setDefaultOperator(defaultOperator);

		IndexSearcher searcher = null;
		try {

			try {
				searcher = searcherManager.acquire();
			} catch (IOException | AlreadyClosedException e) {
				return new SearchResult<T>(-1, null);
			}
			BooleanQuery.Builder luceneQuery = new BooleanQuery.Builder();

			org.apache.lucene.search.Query fulltextQuery = qp.parse(fulltext);
			luceneQuery.add(fulltextQuery, BooleanClause.Occur.MUST);

			luceneQuery = setTenantCondition(qp, luceneQuery);

			Map<String, FulltextSearchCondition> conditions = option.getConditions();

			List<String> defNameList = new ArrayList<>(conditions.keySet());
			luceneQuery = setEntityDefCondition(luceneQuery, defNameList.toArray(new String[defNameList.size()]));

			if (logger.isDebugEnabled()) {
				logger.debug("lucene query : " + luceneQuery.build().toString());
			}

			TopDocs docs = searcher.search(luceneQuery.build(), getMaxRows(), getDefaultSort(), true);
			ScoreDoc[] hits = docs.scoreDocs;

			// 検索結果からoidを取得
			Map<String, FulltextSearchDto> tempDtoMap = new HashMap<>();
			for (ScoreDoc hit : hits) {
				// entity毎にsearchMapに詰め込む
				Document doc = searcher.doc(hit.doc);
				String oid = doc.get("OBJ_ID");
				String defName = doc.get("def_name");

				if (!tempDtoMap.containsKey(defName)) {
					tempDtoMap.put(defName, new FulltextSearchDto(defName));
				}

				FulltextSearchDto dto = tempDtoMap.get(defName);
				dto.addOid(oid);
				dto.addScore(oid, hit.score);
			}

			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			// Entity毎にsearchする
			List<T> resultList = new ArrayList<T>();
			// スコアでソースする
			List<T> tempSortList = new ArrayList<T>();
			for(FulltextSearchDto dto : tempDtoMap.values()) {
				String tempDefName = dto.getDefName();
				if (conditions.isEmpty() || conditions.containsKey(tempDefName)) {

					FulltextSearchCondition cond = conditions.get(tempDefName);

					Query query = new Query();

					OrderBy order = null;
					if (cond != null && cond.getOrder() != null) {
						order = cond.getOrder();
						query.setOrderBy(order);
					}

					List<String> properties = null;
					if (cond != null && cond.getProperties() != null) {
						properties = cond.getProperties();
					}

					if (properties != null && properties.size() > 0) {
						query
							.select(properties.toArray(new Object[properties.size()]))
							.from(tempDefName)
							.where(new In("oid", dto.getOidList().toArray()));

						if (order != null) {
							for (SortSpec sortSpec : order.getSortSpecList()) {
								String sortKey = sortSpec.getSortKey().toString();
								if (!properties.contains(sortKey)) query.select().add(sortKey);
							}
						}

					} else {
						query.selectAll(tempDefName, true, true, true).where(new In("oid", dto.getOidList().toArray()));
					}
					// luceneの返却したoidを利用してEntityを検索
					SearchResult<T> temp = em.searchEntity(query);

					for (T t : temp.getList()) {
						Entity entity = (Entity) t;
						if (entity != null) {
							entity.setValue("score", dto.getScore().get(entity.getOid()));
							if (order == null) {
								// score情報も入れる
								tempSortList.add((T) entity);
							} else {
								// EQLのソート順で入れる
								resultList.add((T) entity);
							}
						}
					}
				}
			}

			if (!tempSortList.isEmpty()) {
				// EQLのソート順がない場合、スコアでソートします。
				Collections.sort(tempSortList, new Comparator<T>() {
					public int compare(T e1, T e2) {
						Float score1 = ((Entity) e1).getValue("score");
						Float score2 = ((Entity) e2).getValue("score");

						if (score1 != null && score2 != null) {
							return score2.compareTo(score1);
						} else if (score1 != null) {
							return -1;
						} else {
							return 1;
						}

					}
				});
				resultList.addAll(tempSortList);
			}

			return new SearchResult<T>(-1, resultList);
		} catch (CorruptIndexException e) {
			logger.error("Fulltext search(lucene) error.", e);
			throw new FulltextSearchRuntimeException(e);
		} catch (ApplicationException e) {
			logger.error("Fulltext search(lucene) error.", e);
			throw new FulltextSearchRuntimeException(e);
		} catch (ParseException e) {
			logger.error("Fulltext search(lucene) error.", e);
			throw new FulltextSearchRuntimeException(e);
		} catch (IOException e) {
			logger.error("Fulltext search(lucene) error.", e);
			throw new FulltextSearchRuntimeException(e);
		} finally {
			if (searcher != null) {
				try {
					searcherManager.release(searcher);
				} catch (IOException e) {
					throw new FulltextSearchRuntimeException("Index error occurred when closing", e);
				} finally {
					searcher = null;
				}
			}
		}
	}

	@Override
	public List<String> fulltextSearchOidList(String searchDefName, String fulltext) {
		List<String> oidList = new ArrayList<String>();
		if (StringUtil.isEmpty(searchDefName) || StringUtil.isEmpty(fulltext) || searcherManager == null) {
			return oidList;
		}

		MultiFieldQueryParser qp = new MultiFieldQueryParser(searchFields, analyzer);
		qp.setDefaultOperator(defaultOperator);
		IndexSearcher searcher = null;
		try {

			try {
				searcher = searcherManager.acquire();
			} catch (AlreadyClosedException | IOException e) {
				return new ArrayList<String>();
			}

			// searchaerでlimit未指定はできない為，全index数をmaxとして指定する
			int allIndexCount = searcher.getIndexReader().numDocs();

			BooleanQuery.Builder luceneQuery = new BooleanQuery.Builder();
			org.apache.lucene.search.Query fulltextQuery = qp.parse(fulltext);
			luceneQuery.add(fulltextQuery, BooleanClause.Occur.MUST);
			luceneQuery = setTenantCondition(qp, luceneQuery);
			luceneQuery = setEntityDefCondition(luceneQuery, searchDefName);

			if (logger.isDebugEnabled()) {
				logger.debug("lucene query : " + luceneQuery.build().toString());
			}

			TopDocs docs = searcher.search(luceneQuery.build(), allIndexCount);
			ScoreDoc[] hits = docs.scoreDocs;

			for (ScoreDoc hit : hits) {

				// entity毎にsearchMapに詰め込む
				Document doc = searcher.doc(hit.doc);
				String oid = doc.get("OBJ_ID");

				oidList.add(oid);
			}

			return oidList;
		} catch (ParseException e) {
			logger.error("Fulltext search(lucene) error.", e);
			throw new FulltextSearchRuntimeException(e);
		} catch (IOException e) {
			logger.error("Fulltext search(lucene) error.", e);
			throw new FulltextSearchRuntimeException(e);
		} finally {
			if (searcher != null) {
				try {
					searcherManager.release(searcher);
				} catch (IOException e) {
					throw new FulltextSearchRuntimeException("Index error occurred when closing", e);
				} finally {
					searcher = null;
				}
			}
		}
	}

	@Deprecated
	@Override
	public Map<String, List<String>> fulltextSearchOidList(List<String> searchDefNames, String fulltext) {
		//未実装
		throw new UnsupportedOperationException("Method is unavailable.");
	}

	@Override
	public List<FulltextSearchResult> execFulltextSearch(String searchDefName, String keywords) {
		if (searcherManager == null) {
			return new ArrayList<FulltextSearchResult>();
		}

		MultiFieldQueryParser qp = new MultiFieldQueryParser(searchFields, analyzer);
		qp.setDefaultOperator(defaultOperator);
		IndexSearcher searcher = null;
		try {

			List<FulltextSearchResult> resultList = new ArrayList<FulltextSearchResult>();

			searcher = searcherManager.acquire();
			BooleanQuery.Builder luceneQuery = new BooleanQuery.Builder();

			org.apache.lucene.search.Query fulltextQuery = qp.parse(keywords);
			luceneQuery.add(fulltextQuery, BooleanClause.Occur.MUST);

			luceneQuery = setTenantCondition(qp, luceneQuery);
			luceneQuery = setEntityDefCondition(luceneQuery, searchDefName);

			if (logger.isDebugEnabled()) {
				logger.debug("lucene query : " + luceneQuery.build().toString());
			}

			TopDocs docs = searcher.search(luceneQuery.build(), getMaxRows(), getDefaultSort());
			ScoreDoc[] hits = docs.scoreDocs;

			for (ScoreDoc hit : hits) {
				FulltextSearchResult result = new FulltextSearchResult();
				Document doc = searcher.doc(hit.doc);
				String oid = doc.get("OBJ_ID");
				result.setOid(oid);
				resultList.add(result);
			}

			return resultList;
		} catch (CorruptIndexException e) {
			throw new RuntimeException("Fulltext search(lucene) error.", e);
		} catch (ApplicationException e) {
			throw new RuntimeException("Fulltext search(lucene) error.", e);
		} catch (ParseException e) {
			throw new RuntimeException("Fulltext search(lucene) error.", e);
		} catch (IOException e) {
			throw new RuntimeException("Fulltext search(lucene) error.", e);
		} finally {
			if (searcher != null) {
				try {
					searcherManager.release(searcher);
				} catch (IOException e) {
					throw new FulltextSearchRuntimeException("Index error occurred when closing", e);
				} finally {
					searcher = null;
				}
			}
		}
	}

	@Override
	public void deleteAllIndex() {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(analyzer));
			Term term = new Term("tenant_id", String.valueOf(tenantId));
			writer.deleteDocuments(term);
		} catch (IOException e) {
			throw new FulltextSearchRuntimeException("Index error occurred when deleting.", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					throw new FulltextSearchRuntimeException("Index error occurred when closing.", e);
				}
			}
		}

		// CrawlLogのデータを削除
		removeAllCrawlLog();

		// DeleteLogのデータを削除
		removeAllDeleteLog();

	}

	/**
	 * Queryにデフォルトソート条件（スコア、Entity定義名）を追加する
	 */
	private Sort getDefaultSort() {
		return new Sort(SortField.FIELD_SCORE, new SortedSetSortField("def_name", false));
	}

	/**
	 * Queryに条件：テナントIDを追加する
	 */
	private BooleanQuery.Builder setTenantCondition(MultiFieldQueryParser qp, BooleanQuery.Builder luceneQuery) throws ParseException {
		org.apache.lucene.search.Query tenantQuery = qp.parse(
				"tenant_id:" + ExecuteContext.getCurrentContext().getCurrentTenant().getId()
		);
		luceneQuery.add(tenantQuery, BooleanClause.Occur.MUST);
		return luceneQuery;
	}

	/**
	 * QueryにEntity名を追加する
	 */
	private BooleanQuery.Builder setEntityDefCondition (BooleanQuery.Builder luceneQuery, String... defNames) {
		BooleanQuery.Builder defNameOrQuery = new BooleanQuery.Builder();
//		BooleanQuery defNameOrQuery = new BooleanQuery();
		for (String defName : defNames) {
			Term term = new Term("def_name",  defName);
			defNameOrQuery.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
		}
		luceneQuery.add(defNameOrQuery.build(), BooleanClause.Occur.MUST);
		return luceneQuery;
	}
	/**
	 * 取得データの整理で利用するDTO
	 */
	@SuppressWarnings("unused")
	private class FulltextSearchDto {
		private String defName;

		private List<String> oidList;

		private Map<String, Float> score;

		public FulltextSearchDto(String defName) {
			this.defName = defName;
		}

		public String getDefName() {
			return defName;
		}

		public void setDefName(String defName) {
			this.defName = defName;
		}

		public List<String> getOidList() {
			return oidList;
		}

		public void setOidList(List<String> oidList) {
			this.oidList = oidList;
		}

		public void addOid(String oid) {
			if (this.oidList == null) {
				this.oidList = new ArrayList<>();
			}
			this.oidList.add(oid);
		}

		public Map<String, Float> getScore() {
			return score;
		}

		public void setScore(Map<String, Float> score) {
			this.score = score;
		}

		public void addScore(String oid, Float score) {
			if (this.score == null) {
				this.score = new HashMap<>();
			}
			this.score.put(oid, score);
		}
	}

	private class EntityIndexWriter implements Closeable {

		private IndexWriter writer;

		private int counter;

		public EntityIndexWriter() throws IOException {
			createWriter();
		}

		@Override
		public void close() throws IOException {
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}

		public final long commit() throws IOException {
			if (writer != null) {
				return writer.commit();
			}
			return -1;
		}

		public void rollback() throws IOException {
			if (writer != null) {
				writer.rollback();
			}
		}

		public long deleteDocuments(Term... terms) throws IOException {

			if (writer != null) {
				checkLimit();
			} else {
				createWriter();
			}

			long sequence = writer.deleteDocuments(terms);

			//Termで消しているが１件としてカウント
			counter++;

			return sequence;
		}

		public long addDocument(Iterable<? extends IndexableField> doc) throws IOException {

			if (writer != null) {
				checkLimit();
			} else {
				createWriter();
			}

			long sequence = writer.addDocument(doc);

			counter++;

			return sequence;
		}

		private void checkLimit() throws IOException {
			if (indexWriterCommitLimit > 0 && counter >= indexWriterCommitLimit) {
				recreateWriter();
			}
		}

		private void recreateWriter() throws IOException {
			commit();
			close();
			counter = 0;
			logger.info("commit lucene index writer. because the operation count has exceeded the upper limit value(" + indexWriterCommitLimit + ").");
			createWriter();
		}

		private void createWriter() throws IOException {
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			config.setRAMBufferSizeMB(indexWriterRAMBufferSizeMB);
			if (logger.isDebugEnabled()) {
				config.setInfoStream(new InfoStream() {

					@Override
					public void close() throws IOException {
					}

					@Override
					public void message(String component, String message) {

						logger.debug("lucene: " + component + ": " + message);
					}

					@Override
					public boolean isEnabled(String component) {
						return true;
					}
				});
			}

			writer = new IndexWriter(directory, config);
		}

	}

	private class EntitySearcherFactory extends SearcherFactory {

		private RefreshSearcherTimerTask task = null;

		@Override
		public IndexSearcher newSearcher(IndexReader reader, IndexReader previousReader) throws IOException {
			if (logger.isDebugEnabled()) {
				logger.debug("New index searcher created.");
			}
			IndexSearcher searcher = new IndexSearcher(reader);
			// TODO warmup処理
			// ......
			if (timer != null && (task == null || task.cancel())) {
				task = new RefreshSearcherTimerTask();
				timer.purge();
				timer.scheduleAtFixedRate(task, TimeUnit.MINUTES.toMillis(searcherAutoRefreshTimeMinutes),
						TimeUnit.MINUTES.toMillis(searcherAutoRefreshTimeMinutes));
			}

			return searcher;
		}
	}

	private class RefreshSearcherTimerTask extends TimerTask {

		@Override
		public void run() {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Refresh index searcher.");
				}
				searcherManager.maybeRefresh();
			} catch (IOException e) {
				logger.warn("Error occured when refreshing searcher.", e);
			}
		}
	}
}
