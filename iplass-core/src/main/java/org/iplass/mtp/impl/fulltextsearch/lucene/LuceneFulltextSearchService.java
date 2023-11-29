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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.fulltextsearch.AbstractFulltextSearchService;
import org.iplass.mtp.impl.fulltextsearch.IndexedEntity;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneFulltextSearchService extends AbstractFulltextSearchService {

	private static Logger logger = LoggerFactory.getLogger(LuceneFulltextSearchService.class);

	private AnalyzerSetting analyzerSetting;
	private Operator defaultOperator;

	private long searcherAutoRefreshTimeMinutes = -1;
	private Timer timer;
	
	private IndexWriterSetting indexWriterSetting;
	
	private String directory;
	private Class<?> luceneFSDirectoryClass;
	private int maxChunkSizeMB;
	
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
		if (isUseFulltextSearch()) {
			contexts.computeIfAbsent(tenantContext.getTenantId(), key -> new LuceneFulltextSearchContext(this, tenantContext));
		}
	}

	@Override
	public void destroyTenantContext(TenantContext tenantContext) {
		if (isUseFulltextSearch()) {
			LuceneFulltextSearchContext fsc = contexts.remove(tenantContext.getTenantId());
			if (fsc != null) {
				fsc.destroy();
			}
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
			
			searcherAutoRefreshTimeMinutes = config.getValue("searcherAutoRefreshTimeMinutes", Long.TYPE, -1L);
			if (searcherAutoRefreshTimeMinutes > 0) {
				timer = new Timer("Searcher refresh timer", true);
			}
			
			indexWriterSetting = config.getValue("indexWriterSetting", IndexWriterSetting.class, new IndexWriterSetting());
			
			analyzerSetting = config.getValue("analyzerSetting", AnalyzerSetting.class);
			if (analyzerSetting == null) {
				analyzerSetting = new JapaneseAnalyzerSetting();
				analyzerSetting.inited(this, config);
			}
		}
		
		defaultOperator = config.getValue("defaultOperator", Operator.class);
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
	protected void createIndexData(final int tenantId, String defName) {
		// 存在しないdefinitionNameが指定された場合は終了
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(DefinitionService.getInstance().getPath(EntityDefinition.class, defName));
		if (entry == null) {
			logger.warn(defName + " is not found.");
			return;
		}

		// Crawl対象Entityでない場合は終了
		MetaEntity meta = (MetaEntity) entry.getMetaData();
		if (!meta.isCrawl()) {
			logger.debug(defName + " is not crawl target entity.");
			return;
		} else {
			logger.info("start crawl " + defName);
		}

		// Crawl対象プロパティが0件以下の場合は終了
		if (meta.getCrawlPropertyId() == null || meta.getCrawlPropertyId().isEmpty()) {
			logger.warn(defName + " have no crawl target property. so skip crawl.");
			logger.info("end crawl " + defName);
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
					logger.info("end crawl " + defName);
				} catch (IOException | RuntimeException e) {
					writer.rollback();
					throw e;
				}
			} catch (IOException e) {
				throw new FulltextSearchRuntimeException("Cant create index cause " + e.toString(), e);
			}
		});
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
		return new TextField(fieldName, toValue(val), Field.Store.NO);
	}
	
	@Override
	protected List<IndexedEntity> fulltextSearchImpl(Integer tenantId, EntityHandler eh, String fulltext, int limit) {
		
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
