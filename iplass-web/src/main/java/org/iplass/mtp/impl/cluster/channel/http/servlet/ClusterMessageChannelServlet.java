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

package org.iplass.mtp.impl.cluster.channel.http.servlet;

import java.io.IOException;
import java.util.Enumeration;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.impl.cluster.channel.MessageChannel;
import org.iplass.mtp.impl.cluster.channel.http.HttpMessageChannel;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterMessageChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 8940534146769969242L;
	
	private static Logger logger = LoggerFactory.getLogger(ClusterMessageChannelServlet.class);
	
	private HttpMessageChannel messageChannel;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ClusterService cs = ServiceRegistry.getRegistry().getService(ClusterService.class);
		MessageChannel mc = cs.getMessageChannel();
		if (mc instanceof HttpMessageChannel) {
			messageChannel = (HttpMessageChannel) mc;
		}
	}
	
	private Message toMessage(HttpServletRequest req) {
		String eventName = req.getParameter(HttpMessageChannel.EVENT_NAME_NAME);
		if (eventName != null) {
			Message msg = new Message(eventName);
			for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();) {
				String key = e.nextElement();
				msg.addParameter(key, req.getParameter(key));
			}
			msg.removeParameter(HttpMessageChannel.CERT_KEY_NAME);
			msg.removeParameter(HttpMessageChannel.EVENT_NAME_NAME);
			return msg;
		}
		return null;
		
	}
	
	
	private void doMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (messageChannel != null) {
			try {
				String certKey = req.getParameter(HttpMessageChannel.CERT_KEY_NAME);
				Message msg = toMessage(req);
				if (msg != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("receive message :" + msg);
					}
					messageChannel.doReceiveMessage(msg, certKey);
				}
			} catch (RuntimeException e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doMessage(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doMessage(req, resp);
	}
	
	

}
