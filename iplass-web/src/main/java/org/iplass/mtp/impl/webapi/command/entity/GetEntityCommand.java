/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.entity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.entity.csv.EntitySearchCsvWriter;
import org.iplass.mtp.impl.entity.csv.EntityWriteOption;
import org.iplass.mtp.impl.entity.csv.QueryCsvWriter;
import org.iplass.mtp.impl.entity.csv.QueryWriteOption;
import org.iplass.mtp.impl.webapi.WebApiResponse;
import org.iplass.mtp.impl.webapi.jackson.WebApiObjectMapperService;
import org.iplass.mtp.impl.webapi.jaxb.WebApiJaxbService;
import org.iplass.mtp.impl.xml.jaxb.DateXmlAdapter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebApi(name="mtp/entity/GET",
		accepts={RequestType.REST_FORM},
		methods={MethodType.GET},
		results={GetEntityCommand.RESULT_ENTITY_LIST, GetEntityCommand.RESULT_COUNT, GetEntityCommand.RESULT_ENTITY, 
				GetEntityCommand.RESULT_CSV, GetEntityCommand.RESULT_JSON, GetEntityCommand.RESULT_XML },
		responseType="application/json, application/xml, text/csv;charset=utf-8",
		overwritable=false)
@CommandClass(name="mtp/entity/GetEntityCommand", displayName="Entity Query/Load Web API", overwritable=false)
public final class GetEntityCommand extends AbstractEntityCommand {

	public static final String PARAM_QUERY = "query";
	public static final String PARAM_TABLE_MODE = "tabular";
	public static final String PARAM_COUNT_TOTAL = "countTotal";
	public static final String PARAM_FILTER = "filter";

	public static final String RESULT_ENTITY_LIST = "list";
	public static final String RESULT_COUNT = "count";
	public static final String RESULT_ENTITY = "entity";
	public static final String RESULT_CSV = "csv";
	public static final String RESULT_JSON = "json";
	public static final String RESULT_XML = "xml";
	
	private final JAXBContext context;
	private final Map<String, String> nameSpaceMap;

	public GetEntityCommand() {
		context = ServiceRegistry.getRegistry().getService(WebApiJaxbService.class).getJAXBContext();
		nameSpaceMap = new HashMap<String, String>();

		WebApiResponse webApiResponse = new WebApiResponse();
		Document doc;
		Marshaller marshaller;
		try { 
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			throw new SystemException(e);
		}
		
		try {
			marshaller = context.createMarshaller();
			marshaller.marshal(webApiResponse, doc);
		} catch (JAXBException e) {
			throw new SystemException(e);
		}
		
		Element element = doc.getDocumentElement();
		NamedNodeMap namedNodeMap = element.getAttributes();
		
		for(int i=0;i<namedNodeMap.getLength();i++) {
			String[] str = namedNodeMap.item(i).getNodeName().split(":");
			nameSpaceMap.put(str[1], namedNodeMap.item(i).getNodeValue());
		}
	}

	// api/entity?query=SELECT...
	private void query(RequestContext request) {
		String eql = request.getParam(PARAM_QUERY);
		if (eql == null) {
			throw new NullPointerException("query must specify");
		}
		Query query = Query.newQuery(eql);

		boolean isCSV = isCSV(request);
		if (!isCSV) {
			queryImpl(query, request);
		} else {
			queryCsv(query, request);
		}
	}

	// api/entity/[definitionName]?filter=[where clause]
	private void list(String entityDef, RequestContext request) {
		Query query = new Query().selectAll(entityDef, false, true);
		String filter = request.getParam(PARAM_FILTER);
		if (filter != null) {
			query.where(filter);
		}

		boolean isCSV = isCSV(request);
		if (!isCSV) {
			queryImpl(query, request);
		} else {
			listCsv(query, request);
		}
	}

	private void queryImpl(Query query, RequestContext request) {
		checkPermission(query.getFrom().getEntityName(), def -> def.getMetaData().isQuery());
	
		boolean tabular = request.getParam(PARAM_TABLE_MODE, Boolean.class, false);
		boolean countTotal = request.getParam(PARAM_COUNT_TOTAL, Boolean.class, false);
		boolean isJSON = isJSON(request);
		boolean isXML = isXML(request);
		
		SearchOption option = new SearchOption();

		if (countTotal) {
			option.setCountTotal(true);
		}

		if (tabular && isJSON) {
			option.setReturnStructuredEntity(true);
			queryJson(query, request, option);
		} else if (tabular && isXML) {
			option.setReturnStructuredEntity(true);
			queryXml(query, request, option);
		} else {
			if (query.getLimit() == null) {
				query.limit(entityWebApiService.getMaxLimit());
			}
			
			if (query.getLimit().getLimit() > entityWebApiService.getMaxLimit()) {
				throw new IllegalArgumentException("Can not specify limit more than " + entityWebApiService.getMaxLimit());
			}
			
			SearchResult<?> res = em.searchEntity(query, option);
			
			request.setAttribute(RESULT_ENTITY_LIST, res.getList());
			if (countTotal) {
				request.setAttribute(RESULT_COUNT, res.getTotalCount());
			}
		}
	}

	private void queryCsv(Query query, RequestContext request) {
		checkPermission(query.getFrom().getEntityName(), def -> def.getMetaData().isQuery());

		StreamingOutput stream = out -> {

			QueryWriteOption option = new QueryWriteOption();
			option.setDateFormat(entityWebApiService.getCsvDateFormat());
			option.setDatetimeSecFormat(entityWebApiService.getCsvDateTimeFormat());
			option.setTimeSecFormat(entityWebApiService.getCsvTimeFormat());
			try (QueryCsvWriter writer = new QueryCsvWriter(out, query, option)) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_CSV, stream);
	}

	private void listCsv(Query query, RequestContext request) {
		checkPermission(query.getFrom().getEntityName(), def -> def.getMetaData().isQuery());

		StreamingOutput stream = out -> {
			
			EntityWriteOption option = new EntityWriteOption()
					.where(query.getWhere())
					.orderBy(query.getOrderBy());
			option.setDateFormat(entityWebApiService.getCsvDateFormat());
			option.setDatetimeSecFormat(entityWebApiService.getCsvDateTimeFormat());
			option.setTimeSecFormat(entityWebApiService.getCsvTimeFormat());
			try (EntitySearchCsvWriter writer = new EntitySearchCsvWriter(out, query.getFrom().getEntityName(), option)) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_CSV, stream);
	}

	private void queryJson(Query query, RequestContext request, SearchOption option) {
		checkPermission(query.getFrom().getEntityName(), def -> def.getMetaData().isQuery());

		ObjectMapper mapper = ServiceRegistry.getRegistry().getService(WebApiObjectMapperService.class).getObjectMapper().copy();
		// write,read時の自動closeを無効に設定
		mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
		
		StreamingOutput stream = out -> {
			
			try (QueryJsonWriter writer = new QueryJsonWriter(out, query, option, mapper)) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_JSON, stream);
	}
	
	private void queryXml(Query query, RequestContext request, SearchOption option) {
		checkPermission(query.getFrom().getEntityName(), def -> def.getMetaData().isQuery());
		
		StreamingOutput stream = out -> {
			
			try (QueryXmlWriter writer = new QueryXmlWriter(out, query, option, context, nameSpaceMap, new DateXmlAdapter())) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_XML, stream);
	}

	private boolean isCSV(RequestContext request) {
		String accept = ((HttpServletRequest) request.getAttribute(WebApiRequestConstants.SERVLET_REQUEST)).getHeader("Accept");
		return accept != null && accept.startsWith("text/csv");
	}
	
	private boolean isJSON(RequestContext request) {
		String accept = ((HttpServletRequest) request.getAttribute(WebApiRequestConstants.SERVLET_REQUEST)).getHeader("Accept");
		return accept != null && accept.startsWith("application/json");
	}
	
	private boolean isXML(RequestContext request) {
		String accept = ((HttpServletRequest) request.getAttribute(WebApiRequestConstants.SERVLET_REQUEST)).getHeader("Accept");
		return accept != null && accept.startsWith("application/xml");
	}

	// api/entity/[definitionName]/[oid]
	// api/entity/[definitionName]/[oid]/[version]
	private void load(String entityDef, String oid, String ver, RequestContext request) {
		checkPermission(entityDef, def -> def.getMetaData().isLoad());

		Long version = null;
		if (ver != null) {
			version = Long.parseLong(ver);
		}

		Entity e = em.load(oid, version, entityDef, new LoadOption(true, false));
		if (e != null) {
			request.setAttribute(RESULT_ENTITY, e);
		}
	}

	@Override
	public String executeImpl(RequestContext request, String[] subPath) {
		if (subPath == null || subPath.length == 0) {
			query(request);
		} else {
			switch (subPath.length) {
			case 1:
				list(subPath[0], request);
				break;
			case 2:
				load(subPath[0], subPath[1], null, request);
				break;
			case 3:
				load(subPath[0], subPath[1], subPath[2], request);
				break;
			default:
				throw new IllegalArgumentException("illegal path parameter:" + subPath);
			}
		}

		return CMD_EXEC_SUCCESS;
	}

}