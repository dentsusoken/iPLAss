/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MergePolicy;
import org.apache.lucene.util.InfoStream;

public class IndexWriterSetting {
	
	private int commitLimit;
	private Double ramBufferSizeMB = 64.0;
	private MergePolicy mergePolicy;
	private InfoStream infoStream;
	private IndexDeletionPolicy indexDeletionPolicy;
	
	public IndexDeletionPolicy getIndexDeletionPolicy() {
		return indexDeletionPolicy;
	}
	public void setIndexDeletionPolicy(IndexDeletionPolicy indexDeletionPolicy) {
		this.indexDeletionPolicy = indexDeletionPolicy;
	}

	public int getCommitLimit() {
		return commitLimit;
	}
	public void setCommitLimit(int commitLimit) {
		this.commitLimit = commitLimit;
	}

	public InfoStream getInfoStream() {
		return infoStream;
	}
	public void setInfoStream(InfoStream infoStream) {
		this.infoStream = infoStream;
	}
	public MergePolicy getMergePolicy() {
		return mergePolicy;
	}
	public void setMergePolicy(MergePolicy mergePolicy) {
		this.mergePolicy = mergePolicy;
	}
	public Double getRamBufferSizeMB() {
		return ramBufferSizeMB;
	}
	public void setRamBufferSizeMB(Double ramBufferSizeMB) {
		this.ramBufferSizeMB = ramBufferSizeMB;
	}

	public IndexWriterConfig createIndexWriterConfig(Analyzer analyzer) {
		IndexWriterConfig config;
		if (analyzer == null) {
			config = new IndexWriterConfig();
		} else {
			config = new IndexWriterConfig(analyzer);
		}
		
		if (ramBufferSizeMB != null) {
			config.setRAMBufferSizeMB(ramBufferSizeMB);
		}
		if (mergePolicy != null) {
			config.setMergePolicy(mergePolicy);
		}
		if (infoStream != null) {
			config.setInfoStream(infoStream);
		}
		if (indexDeletionPolicy != null) {
			config.setIndexDeletionPolicy(indexDeletionPolicy);
		}
		return config;
	}

}
