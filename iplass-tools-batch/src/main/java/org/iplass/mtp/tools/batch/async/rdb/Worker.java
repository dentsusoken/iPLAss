/*
 * Copyright 2013 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.async.rdb;

import org.iplass.mtp.impl.async.rdb.RdbQueueService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker {
	
	private static Logger logger = LoggerFactory.getLogger(Worker.class);
	
	public static void main(String[] args) {
		
		logger.info("start Woker process...");
		
		final ServiceRegistry sr = ServiceRegistry.getRegistry();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				
				logger.info("shuthing down Woker process...");
				
				sr.destroyAllService();
			}
		}));
		
		//start queue
		sr.getService(RdbQueueService.class);
		
	}

}
