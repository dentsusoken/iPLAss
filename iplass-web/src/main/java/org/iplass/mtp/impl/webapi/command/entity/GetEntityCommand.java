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

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.resourceString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.iplass.mtp.entity.query.QueryVisitor;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.entity.query.hint.NativeHint;
import org.iplass.mtp.impl.csv.CsvUploadService;
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
import org.iplass.mtp.webapi.entity.SearchResultLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebApi(name="mtp/entity/GET",
		accepts={RequestType.REST_FORM},
		methods={MethodType.GET},
		results={GetEntityCommand.RESULT_ENTITY_LIST, GetEntityCommand.RESULT_COUNT, GetEntityCommand.RESULT_ENTITY,
				GetEntityCommand.RESULT_CSV, GetEntityCommand.RESULT_JSON, GetEntityCommand.RESULT_XML},
		responseType="application/json, application/xml, text/csv;charset=utf-8",
		overwritable=false)
@CommandClass(name="mtp/entity/GetEntityCommand", displayName="Entity Query/Load Web API", overwritable=false)
public final class GetEntityCommand extends AbstractEntityCommand {
	private static final Logger logger = LoggerFactory.getLogger(GetEntityCommand.class);

	public static final String PARAM_QUERY = "query";
	public static final String PARAM_TABLE_MODE = "tabular";
	public static final String PARAM_COUNT_TOTAL = "countTotal";
	public static final String PARAM_FILTER = "filter";
	public static final String PARAM_WITH_MAPPED_BY_REFERENCE = "withMappedByReference";

	public static final String RESULT_ENTITY_LIST = "list";
	public static final String RESULT_COUNT = "count";
	public static final String RESULT_ENTITY = "entity";
	public static final String RESULT_CSV = "csv";
	public static final String RESULT_JSON = "json";
	public static final String RESULT_XML = "xml";


	private enum ResType {
		CSV,
		JSON,
		XML,
		OTHER
	}

	private final JAXBContext context;
	private final Map<String, String> nameSpaceMap;
	private final JsonFactory jsonFactory;
	private final ObjectMapper mapper;

	public GetEntityCommand() {
		context = ServiceRegistry.getRegistry().getService(WebApiJaxbService.class).getJAXBContext();
		nameSpaceMap = new HashMap<String, String>();
		jsonFactory = new JsonFactory();
		mapper = ServiceRegistry.getRegistry().getService(WebApiObjectMapperService.class).getObjectMapper().copy();
		// write,read時の自動closeを無効に設定
		mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);

		WebApiResponse webApiResponse = new WebApiResponse();
		Document doc;
		Marshaller marshaller;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			marshaller = context.createMarshaller();
			marshaller.marshal(webApiResponse, doc);
		} catch (ParserConfigurationException | JAXBException e) {
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

		queryImpl(query, request, true, resType(request), false);
	}

	// api/entity/[definitionName]?filter=[where clause]
	private void list(String entityDef, RequestContext request) {
		ResType resType = resType(request);
		boolean withMappedBy = withMappedByReference(request,
				resType == ResType.CSV ? entityWebApiService.isCsvListWithMappedByReference(): entityWebApiService.isListWithMappedByReference());
		Query query = new Query().selectAll(entityDef, false, true, false, withMappedBy);
		String filter = request.getParam(PARAM_FILTER);
		if (filter != null) {
			query.where(filter);
		}

		queryImpl(query, request, false, resType, withMappedBy);
	}

	private void queryImpl(Query query, RequestContext request, boolean byQuery, ResType resType, boolean withMappedBy) {

		checkPermission(query.getFrom().getEntityName(), def -> def.getMetaData().isQuery());

		if (!entityWebApiService.isEnableNativeHint()) {
			QueryVisitor qv = new QueryVisitorSupport() {
				@Override
				public boolean visit(HintComment hintComment) {
					if (hintComment.getHintList() != null) {
						List<Hint> checked = new ArrayList<>();
						for (Hint h: hintComment.getHintList()) {
							if (h instanceof NativeHint) {
								logger.warn("Native Hint is disable at Entity Web API, so remove hint: " + h);
							} else {
								checked.add(h);
							}
						}
						hintComment.setHintList(checked);
					}
					return false;
				}

			};
			query.accept(qv);
		}

		SearchOption option = new SearchOption();
		option.setReturnStructuredEntity(true);

		if (resType == ResType.CSV) {
			if (byQuery) {
				queryCsv(query, request);
			} else {
				listCsv(query, request, withMappedBy);
			}
		} else {
			boolean tabular = request.getParam(PARAM_TABLE_MODE, Boolean.class, false);
			boolean countTotal = request.getParam(PARAM_COUNT_TOTAL, Boolean.class, false);

			if (tabular) {
				if (resType == ResType.JSON) {
					queryJson(query, request, countTotal);
				} else if (resType == ResType.XML) {
					queryXml(query, request, countTotal);
				}
			} else {
				queryOther(query, request, countTotal);
			}
		}
	}

	private void queryOther(Query query, RequestContext request, boolean countTotal) {
		SearchOption option = new SearchOption();
		option.setReturnStructuredEntity(true);

		boolean noLimit = (query.getLimit() == null);
		if (countTotal || (entityWebApiService.isThrowSearchResultLimitExceededException() && noLimit)) {
			option.setCountTotal(true);
		}

		if (noLimit) {
			query.limit(entityWebApiService.getMaxLimit());
		}

		if (query.getLimit().getLimit() > entityWebApiService.getMaxLimit()) {
			throw new IllegalArgumentException("Can not specify limit more than " + entityWebApiService.getMaxLimit());
		}

		SearchResult<?> res = em.searchEntity(query, option);

		if (entityWebApiService.isThrowSearchResultLimitExceededException()
				&& noLimit
				&& res.getTotalCount() > entityWebApiService.getMaxLimit()) {
			throw new SearchResultLimitExceededException(resourceString("impl.webapi.command.entity.GetEntityCommand.limitExceeded"));
		}

		request.setAttribute(RESULT_ENTITY_LIST, res.getList());

		if (countTotal) {
			request.setAttribute(RESULT_COUNT, res.getTotalCount());
		}
	}

	private void queryCsv(Query query, RequestContext request) {

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

	private void listCsv(Query query, RequestContext request, boolean withMappedBy) {

		CsvUploadService csvUploadService = ServiceRegistry.getRegistry().getService(CsvUploadService.class);

		//TODO EntitySearchCsvWriter使う場合、queryのselect項目利用できず再度EntitySearchCsvWriterで項目選択させる必要あり、、
		StreamingOutput stream = out -> {

			EntityWriteOption option = new EntityWriteOption()
					.where(query.getWhere())
					.orderBy(query.getOrderBy())
					.dateFormat(entityWebApiService.getCsvDateFormat())
					.datetimeSecFormat(entityWebApiService.getCsvDateTimeFormat())
					.timeSecFormat(entityWebApiService.getCsvTimeFormat())
					.withMappedByReference(withMappedBy)
					.mustOrderByWithLimit(csvUploadService.isMustOrderByWithLimit());
			try (EntitySearchCsvWriter writer = new EntitySearchCsvWriter(out, query.getFrom().getEntityName(), option)) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_CSV, stream);
	}

	private void queryJson(Query query, RequestContext request, boolean countTotal) {

		StreamingOutput stream = out -> {

			try (QueryJsonWriter writer = new QueryJsonWriter(out, query, countTotal, mapper, jsonFactory)) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_JSON, stream);
	}

	private void queryXml(Query query, RequestContext request,  boolean countTotal) {

		StreamingOutput stream = out -> {

			try (QueryXmlWriter writer = new QueryXmlWriter(out, query, countTotal, context, nameSpaceMap, new DateXmlAdapter())) {
				writer.write();
			}
		};
		request.setAttribute(RESULT_XML, stream);
	}

	private ResType resType(RequestContext request) {
		String accept = ((HttpServletRequest) request.getAttribute(WebApiRequestConstants.SERVLET_REQUEST)).getHeader("Accept");
		if (accept != null) {
			if (accept.startsWith("application/json")) {
				return ResType.JSON;
			} else if (accept.startsWith("text/csv")) {
				return ResType.CSV;
			} else if (accept.startsWith("application/xml")) {
				return ResType.XML;
			}
		}
		return ResType.OTHER;

	}

//	private boolean isCSV(String accept) {
//		return accept != null && accept.startsWith("text/csv");
//	}
//
//	private boolean isJSON(String accept) {
//		return accept != null && accept.startsWith("application/json");
//	}
//
//	private boolean isXML(String accept) {
//		return accept != null && accept.startsWith("application/xml");
//	}

	// api/entity/[definitionName]/[oid]
	// api/entity/[definitionName]/[oid]/[version]
	private void load(String entityDef, String oid, String ver, RequestContext request) {
		checkPermission(entityDef, def -> def.getMetaData().isLoad());

		Long version = null;
		if (ver != null) {
			version = Long.parseLong(ver);
		}

		Entity e = em.load(oid, version, entityDef, new LoadOption(true,
				withMappedByReference(request, entityWebApiService.isLoadWithMappedByReference())));
		if (e != null) {
			request.setAttribute(RESULT_ENTITY, e);
		}
	}

	private boolean withMappedByReference(RequestContext request, boolean defaultVale) {
		Boolean wmbr = request.getParamAsBoolean(PARAM_WITH_MAPPED_BY_REFERENCE);
		if (wmbr == null) {
			return defaultVale;
		} else {
			return wmbr.booleanValue();
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