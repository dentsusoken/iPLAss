/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch.lucene;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.tika.exception.EncryptedDocumentException;
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
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.fulltextsearch.AbstractFulltextSeachService;
import org.iplass.mtp.impl.fulltextsearch.FulltextSearchResult;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryNameTypeParser;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryReferenceParseException;
import org.iplass.mtp.impl.fulltextsearch.parser.BinaryReferenceParser;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneFulltextSearchService extends AbstractFulltextSeachService {

	private static Logger logger = LoggerFactory.getLogger(LuceneFulltextSearchService.class);

	private AnalyzerSetting analyzerSetting;
	private Operator defaultOperator;
	private long redundantTimeMinutes;

	private List<BinaryReferenceParser> binaryParsers;
	private int binaryParseLimitLength = 100000;

	private long searcherAutoRefreshTimeMinutes = -1;
	private Timer timer;
	
	private IndexWriterSetting indexWriterSetting;
	
	private String directory;
	private Class<?> luceneFSDirectoryClass;
	private int maxChunkSizeMB;
	
	private String scorePropertyName = "score";
	private boolean includeMappedByReferenceIfNoPropertySpecified = false;
	
	private ConcurrentHashMap<Integer, LuceneFulltextSearchContext> contexts;
	
	
	IndexDir newIndexDir(int tenantId, String defId) {
		EntityService entityService = ServiceRegistry.getRegistry().getService(EntityService.class);
		EntityHandler eh = entityService.getRuntimeById(defId);
		if (eh == null || !eh.getMetaData().isCrawl()) {
			logger.debug("defId:" + defId + " not foud or disable crawl.");
			return null;
		}
		
		Path dirPath = Paths.get(directory, String.valueOf(tenantId), defId);
		if (!Files.exists(dirPath)) {
			logger.debug(dirPath.toString() + " not exists.so create new directory");
		}
		FSDirectory dir = null;
		try {
			if (luceneFSDirectoryClass != null) {
				if (MMapDirectory.class.equals(luceneFSDirectoryClass)) {
					dir = new MMapDirectory(dirPath, maxChunkSizeMB);
				} else {
					dir = (FSDirectory) luceneFSDirectoryClass.getConstructor(Path.class).newInstance(dirPath);
				}
			} else {
				dir = FSDirectory.open(dirPath);
			}
			
			return new SimpleIndexDir(tenantId, defId, dir, searcherAutoRefreshTimeMinutes, timer);
		} catch (Exception e) {
			if (dir != null) {
				try {
					dir.close();
				} catch (IOException e1) {
					e.addSuppressed(e1);
				}
			}
			throw new FulltextSearchRuntimeException("Failed to initialize the Lucene index directory:" + tenantId + "/" + defId, e);
		}
	}
	
	private boolean existsDir(int tenantId, String defId) {
		Path dirPath = Paths.get(directory, String.valueOf(tenantId), defId);
		return Files.exists(dirPath);
	}
	
	@Override
	public void initTenantContext(TenantContext tenantContext) {
		contexts.computeIfAbsent(tenantContext.getTenantId(), key -> new LuceneFulltextSearchContext(this, tenantContext));
	}

	@Override
	public void destroyTenantContext(TenantContext tenantContext) {
		LuceneFulltextSearchContext fsc = contexts.remove(tenantContext.getTenantId());
		if (fsc != null) {
			fsc.destroy();
		}
	}

	@Override
	public void destroy() {
		if (isUseFulltextSearch()) {
			if (timer != null) {
				timer.cancel();
			}
			for (LuceneFulltextSearchContext fsc: contexts.values()) {
				fsc.destroy();
			}
		}
	}

	@Override
	public void init(Config config) {
		super.init(config);
		
		if (isUseFulltextSearch()) {
			contexts = new ConcurrentHashMap<>();
			
			directory = config.getValue("directory");
			if (directory == null) {
				throw new NullPointerException("directory is null");
			}
			String className = config.getValue("luceneFSDirectory");
			if (className != null) {
				try {
					luceneFSDirectoryClass = Class.forName(className);
				} catch (ClassNotFoundException e) {
					throw new ServiceConfigrationException(e);
				}
				if (!FSDirectory.class.isAssignableFrom(luceneFSDirectoryClass)) {
					throw new ServiceConfigrationException(className + " is not sub class of FSDirectory");
				}
			}
			maxChunkSizeMB = config.getValue("luceneFSDirectoryMaxChunkSizeMB", Integer.TYPE, MMapDirectory.DEFAULT_MAX_CHUNK_SIZE);
			
			binaryParsers = (List<BinaryReferenceParser>) config.getValues("binaryParser", BinaryReferenceParser.class);
			if (binaryParsers == null) {
				//未指定の場合は、NameTypeParserを設定
				binaryParsers = Collections.singletonList(new BinaryNameTypeParser());
			} else {
				//最後にNameTypeParserを設定
				if (!(binaryParsers.get(binaryParsers.size() - 1) instanceof BinaryNameTypeParser)) {
					binaryParsers.add(new BinaryNameTypeParser());
				}
			}
			binaryParseLimitLength = config.getValue("binaryParseLimitLength", Integer.TYPE, 100000);
			
			searcherAutoRefreshTimeMinutes = config.getValue("searcherAutoRefreshTimeMinutes", Long.TYPE, -1L);
			if (searcherAutoRefreshTimeMinutes > 0) {
				timer = new Timer("Searcher refresh timer", true);
			}
			
			indexWriterSetting = config.getValue("indexWriterSetting", IndexWriterSetting.class, new IndexWriterSetting());
			redundantTimeMinutes = config.getValue("redundantTimeMinutes", Long.TYPE, 10L);
			
			analyzerSetting = config.getValue("analyzerSetting", AnalyzerSetting.class);
			if (analyzerSetting == null) {
				analyzerSetting = new JapaneseAnalyzerSetting();
				analyzerSetting.inited(this, config);
			}
		}
		
		defaultOperator = config.getValue("defaultOperator", Operator.class);
		scorePropertyName = config.getValue("scorePropertyName", String.class, "score");
		includeMappedByReferenceIfNoPropertySpecified = config.getValue("includeMappedByReferenceIfNoPropertySpecified", Boolean.class, false);
	}

	@Override
	public void execRefresh() {
		if (isUseFulltextSearch()) {
			LuceneFulltextSearchContext fsc = contexts.get(ExecuteContext.getCurrentContext().getClientTenantId());
			if (fsc != null) {
				fsc.refreshAll();
			}
		}
	}

	@Override
	public void execCrawlEntity(String... defNames) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		if (defNames == null || defNames.length == 0) {
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

		// Crawl対象プロパティが0件以下の場合は終了
		if (meta.getCrawlPropertyId() == null || meta.getCrawlPropertyId().isEmpty()) {
			logger.warn(defName + " have no crawl target property. so skip crawl.");
			return;
		}


		AuthContext.doPrivileged(() -> {
			// 対象プロパティのリストを作成する
			Map<String, String> crawlPropertyNameMap = generateCrawlPropMap(meta);

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
			try (EntityIndexWriter writer = new EntityIndexWriter(tenantId, meta)) {

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
	
	private Map<String, String> generateCrawlPropMap(MetaEntity meta) {
		// 対象プロパティのリストを作成する
		Map<String, String> crawlPropertyNameMap = new HashMap<String, String>();

		EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
		MetaEntity superMeta = ehs.getRuntimeById(meta.getInheritedEntityMetaDataId()).getMetaData();

		for (String crawlId : meta.getCrawlPropertyId()) {
			MetaProperty metaProperty = meta.getDeclaredPropertyById(crawlId);
			if (metaProperty == null) {
				metaProperty = superMeta.getDeclaredPropertyById(crawlId);
			}
			
			if (metaProperty != null) {
				String propertyName = metaProperty.getName();
				if (metaProperty instanceof MetaReferenceProperty) {
					crawlPropertyNameMap.put(propertyName + ".name", metaProperty.getId());
				} else {
					crawlPropertyNameMap.put(propertyName, metaProperty.getId());
				}
			} else {
				logger.warn("### DefinitionName " + "[" + meta.getName() + "] ### crawlId " + crawlId + " is not found.");
			}
		}
			
		return crawlPropertyNameMap;
	}

	private LocaleFormat getLocaleFormat() {
		return ExecuteContext.getCurrentContext().getLocaleFormat();
	}

	private boolean createIndexByQuery(final Query query, final int tenantId, final String objDefId, final EntityIndexWriter writer, final Map<String, String> crawlPropertyNameMap) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		if (logger.isDebugEnabled()) {
			logger.debug("### EQL : " + query.toString() + "###");
		}

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
				doc.add(new StringField("OBJ_ID", entity.getOid(), Field.Store.YES));
				if (entity.getVersion() != null) {
					doc.add(new StringField("OBJ_VER", Long.toString(entity.getVersion()), Field.Store.YES));
				}

				for(Map.Entry<String, String> e : crawlPropertyNameMap.entrySet()) {
					String propName = e.getKey();
					Object val = entity.getValue(propName);
					if (val != null) {
						String fieldName = e.getValue();
						if (val.getClass().isArray()) {
							Object[] valArray = (Object[]) val;
							for (int i = 0; i < valArray.length; i++) {
								doc.add(toField(fieldName, valArray[i]));
							}
						} else {
							doc.add(toField(fieldName, val));
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
	
	private Field toField(String fieldName, Object val) throws IOException {
		if (val instanceof Timestamp) {
			final SimpleDateFormat dateTimeFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDatetimeSecFormat(), true);
			dateTimeFormat.setLenient(false);
			return new TextField(fieldName, dateTimeFormat.format((Timestamp) val), Field.Store.NO);
		} else if (val instanceof Date) {
			final SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputDateFormat(), false);
			dateFormat.setLenient(false);
			return new TextField(fieldName, dateFormat.format((Date) val), Field.Store.NO);
		} else if (val instanceof Time) {
			final SimpleDateFormat timeFormat = DateUtil.getSimpleDateFormat(getLocaleFormat().getOutputTimeSecFormat(), false);
			timeFormat.setLenient(false);
			return new TextField(fieldName, timeFormat.format((Time) val), Field.Store.NO);
		} else if (val instanceof BigDecimal) {
			return new StringField(fieldName, ((BigDecimal) val).toPlainString(), Field.Store.NO);
		} else if (val instanceof Long) {
			return new StringField(fieldName, val.toString(), Field.Store.NO);
		} else if (val instanceof Double) {
			return new StringField(fieldName, val.toString(), Field.Store.NO);
		} else if (val instanceof BinaryReference) {
			return new TextField(fieldName, parseBinaryReference((BinaryReference) val), Field.Store.NO);
		} else {
			return new TextField(fieldName, val.toString(), Field.Store.NO);
		}
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
	
	private static class IndexedEntity {
		String defName;
		String oid;
		float score;
		
		IndexedEntity(String defName, String oid, float score) {
			this.defName = defName;
			this.oid = oid;
			this.score = score;
		}
	}
	
	
	private List<IndexedEntity> fulltextSearchImpl(Integer tenantId, EntityHandler eh, String fulltext, int limit) {
		
		if (eh == null || StringUtil.isEmpty(fulltext)) {
			return Collections.emptyList();
		}
		IndexDir dir = contexts.get(tenantId).getIndexDir(eh.getMetaData().getId());
		if (dir == null) {
			return Collections.emptyList();
		}
		if (eh.getMetaData().getCrawlPropertyId() == null || eh.getMetaData().getCrawlPropertyId().isEmpty()) {
			return Collections.emptyList();
		}
		String[] fields = eh.getMetaData().getCrawlPropertyId().toArray(new String[eh.getMetaData().getCrawlPropertyId().size()]);
		MultiFieldQueryParser qp = new MultiFieldQueryParser(fields, analyzerSetting.getAnalyzer(tenantId, eh.getMetaData().getName()));
		qp.setDefaultOperator(defaultOperator);
		
		IndexSearcher searcher = null;
		try {
			searcher = dir.getSearcherManager().acquire();

			// searchaerでlimit未指定はできない為，全index数をmaxとして指定する
			if (limit < 0) {
				limit = searcher.getIndexReader().numDocs();
			}

			org.apache.lucene.search.Query fulltextQuery = qp.parse(fulltext);
			if (logger.isDebugEnabled()) {
				logger.debug("lucene query : " + fulltextQuery);
			}

			TopDocs docs = searcher.search(fulltextQuery, limit);
			ScoreDoc[] hits = docs.scoreDocs;

			List<IndexedEntity> result = new ArrayList<>(hits.length);
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				String oid = doc.get("OBJ_ID");

				result.add(new IndexedEntity(eh.getMetaData().getName(), oid, hit.score));
			}

			return result;
		} catch (ParseException | IOException e) {
			throw new FulltextSearchRuntimeException("Fulltext search(lucene) error.:" + e.toString(), e);
		} finally {
			if (searcher != null) {
				try {
					dir.getSearcherManager().release(searcher);
				} catch (IOException e) {
					logger.error("Error occurred when IndexSearcher releasing, maybe resource leak", e);
				} finally {
					searcher = null;
				}
			}
		}
	}
	
	private <T extends Entity> SearchResult<T> entitySearchImpl(List<IndexedEntity> oidList, EntityHandler eh, FulltextSearchCondition condition) {
		if (oidList.isEmpty()) {
			return new SearchResult<T>(-1, null);
		}
		
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Query query = new Query();
		
		if (condition == null || condition.getProperties() == null) {
			query.selectAll(eh.getMetaData().getName(), true, true, true, includeMappedByReferenceIfNoPropertySpecified);
		} else {
			for (String prop: condition.getProperties()) {
				query.select().add(prop);
			}
			if (!condition.getProperties().contains(Entity.OID)) {
				//scoreのマッピングのため最低限必要
				query.select().add(Entity.OID);
			}
			//order by項目がない場合、追加
			if (condition != null && condition.getOrder() != null) {
				for (SortSpec sortSpec : condition.getOrder().getSortSpecList()) {
					String sortKey = sortSpec.getSortKey().toString();
					if (!condition.getProperties().contains(sortKey)) {
						query.select().add(sortKey);
					}
				}
			}
			
			query.from(eh.getMetaData().getName());
		}
		
		In in = new In();
		in.setPropertyName(Entity.OID);
		List<ValueExpression> inValues = new ArrayList<ValueExpression>(oidList.size());
		for (IndexedEntity ie: oidList) {
			inValues.add(new Literal(ie.oid));
		}
		in.setValue(inValues);
		query.where(in);
		
		if (condition != null && condition.getOrder() != null) {
			query.setOrderBy(condition.getOrder());
		}
		
		SearchResult<T> searched = em.searchEntity(query);
		if (searched.getList().isEmpty()) {
			return searched;
		}
		
		if (condition == null || condition.getOrder() == null) {
			//sort by score
			//and set score value to each entity
			Map<String, T> map = new HashMap<>((int)(searched.getList().size() / 0.75f) + 1, 0.75f);
			for (T e: searched.getList()) {
				map.put(e.getOid(), e);
			}
			ArrayList<T> mergedList = new ArrayList<>(searched.getList().size());
			for (IndexedEntity ie: oidList) {
				T e = map.get(ie.oid);
				if (e != null) {
					e.setValue(scorePropertyName, ie.score);
					mergedList.add(e);
				}
			}
			return new SearchResult<T>(searched.getTotalCount(), mergedList);
		} else {
			//set score value to each entity 
			Map<String, IndexedEntity> map = new HashMap<>((int)(oidList.size() / 0.75f) + 1, 0.75f);
			for (IndexedEntity ie: oidList) {
				map.put(ie.oid, ie);
			}
			for (T e: searched.getList()) {
				IndexedEntity ie = map.get(e.getOid());
				e.setValue(scorePropertyName, ie.score);
			}
			return searched;
		}
	}
	
	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String searchDefName, String fulltext) {
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(searchDefName);
		List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, getMaxRows());
		return entitySearchImpl(fromIndexList, eh, null);
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
	
	private interface GetScore<T> {
		float get(T t);
	}

	private <T> List<T> mergeSortByScore(List<T> list1, List<T> list2, int maxSize, GetScore<T> func) {
		//list1, list2それぞれ事前にscore順でソートされている前提
		if (list2.isEmpty()) {
			return list1;
		}
		if (list1.isEmpty()) {
			return list2;
		}
		int size = list1.size() + list2.size();
		if (maxSize > 0 && size > maxSize) {
			size = maxSize;
		}
		
		ArrayList<T> mergeList = new ArrayList<>(size);
		for (int i = 0, i1 = 0, i2 = 0; i < size; i++) {
			if (i1 >= list1.size()) {
				mergeList.add(list2.get(i2));
				i2++;
			} else if (i2 >= list2.size()) {
				mergeList.add(list1.get(i1));
				i1++;
			} else {
				T ie1 = list1.get(i1);
				T ie2 = list2.get(i2);
				if (func.get(ie1) > func.get(ie2)) {
					mergeList.add(ie1);
					i1++;
				} else {
					mergeList.add(ie2);
					i2++;
				}
			}
		}
		
		return mergeList;
	}
	
	private static class TempEntityList {
		EntityHandler eh;
		List<IndexedEntity> oids = new ArrayList<>();
		FulltextSearchCondition cond;
		
		TempEntityList(EntityHandler eh, FulltextSearchCondition cond) {
			this.eh = eh;
			this.cond = cond;
		}
	}

	@Override
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String fulltext, FulltextSearchOption option) {
		if (option.getConditions().size() <= 1) {
			EntityContext ec = EntityContext.getCurrentContext();
			Map.Entry<String, FulltextSearchCondition> e = option.getConditions().entrySet().iterator().next();
			EntityHandler eh = ec.getHandlerByName(e.getKey());
			List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, getMaxRows());
			return entitySearchImpl(fromIndexList, eh, e.getValue());
		} else {
			//複数Entity全体でmaxRowsに絞る
			EntityContext ec = EntityContext.getCurrentContext();
			List<IndexedEntity> fromIndexList = Collections.emptyList();
			Map<String, TempEntityList> tempEntityListMap = new LinkedHashMap<>();
			boolean hasOrderBy = false;
			
			for (Map.Entry<String, FulltextSearchCondition> e: option.getConditions().entrySet()) {
				EntityHandler eh = ec.getHandlerByName(e.getKey());
				List<IndexedEntity> iel = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, getMaxRows());
				fromIndexList = mergeSortByScore(fromIndexList, iel, getMaxRows(), t -> t.score);
				tempEntityListMap.put(e.getKey(), new TempEntityList(eh, e.getValue()));
				hasOrderBy = hasOrderBy || (e.getValue() != null && e.getValue().getOrder() != null);
			}
			
			if (fromIndexList.isEmpty()) {
				return new SearchResult<>(-1, null);
			}
			
			//divide by defName
			for (IndexedEntity ie: fromIndexList) {
				TempEntityList tel = tempEntityListMap.get(ie.defName);
				tel.oids.add(ie);
			}
			
			List<T> resultList = new ArrayList<>();
			for (Map.Entry<String, TempEntityList> e: tempEntityListMap.entrySet()) {
				TempEntityList tel = e.getValue();
				if (tel.oids.size() > 0) {
					SearchResult<T> resPerEntity = entitySearchImpl(tel.oids, tel.eh, tel.cond);
					if (hasOrderBy) {
						resultList.addAll(resPerEntity.getList());
					} else {
						resultList = mergeSortByScore(resultList, resPerEntity.getList(), getMaxRows(), t -> t.getValue(scorePropertyName));
					}
				}
			}
			
			return new SearchResult<>(-1, resultList);
		}
	}

	@Override
	public List<String> fulltextSearchOidList(String searchDefName, String fulltext) {
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(searchDefName);
		List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, fulltext, -1);
		List<String> res = new ArrayList<>(fromIndexList.size());
		for (IndexedEntity ie: fromIndexList) {
			res.add(ie.oid);
		}
		return res;
	}

	@Override
	public List<FulltextSearchResult> execFulltextSearch(String searchDefName, String keywords) {
		EntityContext ec = EntityContext.getCurrentContext();
		EntityHandler eh = ec.getHandlerByName(searchDefName);
		List<IndexedEntity> fromIndexList = fulltextSearchImpl(ec.getTenantId(eh), eh, keywords, getMaxRows());
		List<FulltextSearchResult> res = new ArrayList<>(fromIndexList.size());
		for (IndexedEntity ie: fromIndexList) {
			FulltextSearchResult r = new FulltextSearchResult();
			r.setOid(ie.oid);
			res.add(r);
		}
		return res;
	}

	@Override
	public void deleteAllIndex() {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		EntityService es = ServiceRegistry.getRegistry().getService(EntityService.class);
		List<MetaDataEntryInfo> defList = es.list();
		for (MetaDataEntryInfo def: defList) {
			if (existsDir(tenantId, def.getId())) {
				EntityHandler eh = es.getRuntimeById(def.getId());
				IndexDir dir = contexts.get(tenantId).getIndexDir(eh.getMetaData().getId());
				if (dir != null) {
					try (IndexWriter writer = new IndexWriter(dir.getDirectory(),
							new IndexWriterConfig(analyzerSetting.getAnalyzer(tenantId, eh.getMetaData().getName())))) {
						Term term = new Term("tenant_id", String.valueOf(tenantId));
						writer.deleteDocuments(term);
					} catch (IOException e) {
						throw new FulltextSearchRuntimeException("Index error occurred when deleting.", e);
					}
				}
			}
			
		}
		
		// CrawlLogのデータを削除
		removeAllCrawlLog();

		// DeleteLogのデータを削除
		removeAllDeleteLog();
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
		private int tenantId;
		private MetaEntity metaEntity;

		private int counter;

		public EntityIndexWriter(int tenantId, MetaEntity metaEntity) throws IOException {
			this.tenantId = tenantId;
			this.metaEntity = metaEntity;
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
			if (indexWriterSetting.getCommitLimit() > 0 && counter >= indexWriterSetting.getCommitLimit()) {
				recreateWriter();
			}
		}

		private void recreateWriter() throws IOException {
			commit();
			close();
			counter = 0;
			logger.info("commit lucene index writer. because the operation count has exceeded the upper limit value(" + indexWriterSetting.getCommitLimit() + ").");
			createWriter();
		}

		private void createWriter() throws IOException {
			IndexWriterConfig config = indexWriterSetting.createIndexWriterConfig(analyzerSetting.getAnalyzer(tenantId, metaEntity.getName()));
			IndexDir dir = contexts.get(tenantId).getIndexDir(metaEntity.getId());
			writer = new IndexWriter(dir.getDirectory(), config);
		}

	}

}
