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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SimpleIndexDir implements IndexDir {
	private static Logger logger = LoggerFactory.getLogger(SimpleIndexDir.class);

	private int tenantId;
	private String defId;
	private FSDirectory directory;
	private volatile SearcherManager searcherManager;
	private RefreshSearcherTimerTask timerTask;
	
	
	SimpleIndexDir(int tenantId, String defId, FSDirectory directory, long searcherAutoRefreshTimeMinutes, Timer timer) {
		this.tenantId = tenantId;
		this.defId = defId;
		this.directory = directory;
		
		if (searcherAutoRefreshTimeMinutes > 0) {
			timerTask = new RefreshSearcherTimerTask();
			timer.scheduleAtFixedRate(timerTask, TimeUnit.MINUTES.toMillis(searcherAutoRefreshTimeMinutes),
					TimeUnit.MINUTES.toMillis(searcherAutoRefreshTimeMinutes));
		}
	}
	
	public String getDefId() {
		return defId;
	}
	
	public FSDirectory getDirectory() {
		return directory;
	}
	
	public SearcherManager getSearcherManager() throws IOException {
		SearcherManager sm = searcherManager;
		//dcl with volatile
		if (sm == null) {
			synchronized (this) {
				if (searcherManager == null) {
					sm = new SearcherManager(directory, null);
					searcherManager = sm;
				} else {
					sm = searcherManager;
				}
			}
		}
		return sm;
	}
	
	public void refresh() throws IOException {
		SearcherManager sm = searcherManager;
		if (sm != null) {
			sm.maybeRefreshBlocking();
		}
	}
	
	private class RefreshSearcherTimerTask extends TimerTask {

		@Override
		public void run() {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Refresh index searcher of " + tenantId + "/" + defId);
				}
				SearcherManager sm = searcherManager;
				if (sm != null) {
					sm.maybeRefresh();
				}
			} catch (IOException e) {
				logger.warn("Error occured when refreshing searcher of " + tenantId + "/" + defId, e);
			}
		}
	}
	
	public void close() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("close IndexDir of "  + tenantId + "/" + defId);
		}
		if (timerTask != null) {
			timerTask.cancel();
		}
		
		IOException ex = null;
		try {
			synchronized (this) {
				if (searcherManager != null) {
					searcherManager.close();
				}
			}
		} catch (IOException e) {
			ex = e;
		} finally {
			try {
				directory.close();
				
			} catch (IOException e) {
				if (ex != null) {
					ex.addSuppressed(e);
				} else {
					ex = e;
				}
			}
			
			if (ex != null) {
				throw ex;
			}
		}
	}
	
}
