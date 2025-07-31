/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.webapi.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.ws.rs.core.Variant;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.web.LimitRequestBodyHttpServletRequest;
import org.iplass.mtp.impl.web.RequestPath;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.webapi.MetaWebApi;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.impl.webapi.MetaWebApiResultAttribute.WebApiResultAttributeRuntime;
import org.iplass.mtp.impl.webapi.WebApiParameter;
import org.iplass.mtp.impl.webapi.WebApiParameterMap;
import org.iplass.mtp.impl.webapi.WebApiResponse;
import org.iplass.mtp.impl.webapi.jackson.WebApiObjectMapperService;
import org.iplass.mtp.impl.webapi.jaxb.WebApiJaxbService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class RestCommandInvoker {

	private static final String ORIGIN = "Origin";
	private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
	private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	private static Logger logger = LoggerFactory.getLogger(RestCommandInvoker.class);

	private static WebApiJaxbService jbservice = ServiceRegistry.getRegistry().getService(WebApiJaxbService.class);
	private static WebApiObjectMapperService omservice = ServiceRegistry.getRegistry().getService(WebApiObjectMapperService.class);
	private static SessionService sessionService = ServiceRegistry.getRegistry().getService(SessionService.class);

	@Context SAXParserFactory sax;//XXE対策されたSAXParserFactory（jerseyの実装）

	//TODO 共通的な処理をFilterに移動して、methodの数を減らす

	private <R> R process(String apiName, String httpMethod, ServletContext servletContext, Request rsRequest, HttpServletRequest request, HttpServletResponse response,
			BiFunction<WebRequestStack, WebApiRuntime, R> func) {

		//MtpContainerRequestFilterでセット済み
		RequestPath path = (RequestPath) request.getAttribute(RequestPath.ATTR_NAME);
		WebApiRuntime runtime = (WebApiRuntime) request.getAttribute(RestRequestContext.WEB_API_RUNTIME_NAME);

		RestRequestContext context = new RestRequestContext(servletContext, request, rsRequest, runtime.getMetaData().isSupportBearerToken());

		WebRequestStack stack = new WebRequestStack(path, context, servletContext, request, response, null);
		if (runtime.getMetaData().getState() == StateType.STATELESS) {
			sessionService.setSessionStateless(false);
		}

		try {
			stack.setAttribute(WebApiRequestConstants.API_NAME, runtime.getPublicWebApiName());
			return func.apply(stack, runtime);
		} finally {
			stack.finallyProcess();
		}
	}

	private Response executeCommand(WebRequestStack stack, WebApiRuntime runtime) {
		WebApiResponse result = new WebApiResponse();

		try {
			result.setStatus(runtime.executeCommand(stack, MetaWebApi.COMMAND_INTERCEPTOR_NAME));
		} catch (WebApplicationException e) {
			//already handled by code
			throw e;
		} catch (RuntimeException e) {
			//handled by framework
			throw new WrappedRestException(e);
		}

		Object onlyOneRes = null;
		for (WebApiResultAttributeRuntime attributeRuntime : runtime.getResponseResults()) {
			String attributeName = attributeRuntime.getName();
			Object val = stack.getRequestContext().getAttribute(attributeName);
			if (val != null) {
				result.addResult(attributeName, val);
			}
		}

		if (result.getResults() != null && result.getResults().size() == 1) {
			onlyOneRes = result.getResults().values().iterator().next();
		}

		if (runtime.getMetaData().getCacheControlType() != null) {
			switch (runtime.getMetaData().getCacheControlType()) {
			case CACHE:
				WebUtil.setCacheControlHeader(stack, true, runtime.getMetaData().getCacheControlMaxAge());
				break;
			case CACHE_PUBLIC:
				WebUtil.setCacheControlHeader(stack, true, true, runtime.getMetaData().getCacheControlMaxAge());
				break;
			case NO_CACHE:
				WebUtil.setCacheControlHeader(stack, false, -1);
				break;
			default:
				break;
			}
		}

		if (onlyOneRes instanceof ResponseBuilder) {
			return ((ResponseBuilder) onlyOneRes).build();
		} else if (onlyOneRes instanceof StreamingOutput) {
			ResponseBuilder rb = Response.ok(onlyOneRes);
			rb.type(selectVariant(stack, runtime).getMediaType());
			return rb.build();
		} else {
			ResponseBuilder rb = Response.ok(result);
			rb.type(selectVariant(stack, runtime).getMediaType());
			return rb.build();
		}
	}

	private Variant selectVariant(WebRequestStack stack, WebApiRuntime runtime) {
		List<Variant> variants = runtime.getVariants();
		if (variants.size() == 1) {
			return variants.get(0);
		}

		RestRequestContext context = (RestRequestContext) stack.getRequestContext();
		Variant v = context.rsRequest().selectVariant(variants);
		if (v == null) {
			throw new WebApiRuntimeException("Response Type cannot determined. Specify correct Accept header:" + stack.getRequest().getHeader("Accept"));
		}
		return v;
	}

	private void checkValidRequest(WebRequestStack stack, WebApiRuntime runtime) {
		RestRequestContext context = (RestRequestContext) stack.getRequestContext();
		//MtpContainerRequestFilterでチェック済み
		//runtime.checkMethodType(context.methodType());

		runtime.checkRequestType(context.requestType(), stack.getRequest());
		if (!handleCrossOriginResourceSharing(runtime, context, stack.getRequest(), stack.getResponse())) {
			throw new WebApiRuntimeException("Cross Origin Resource Sharing Policy Erorr on WebApi:" + runtime.getMetaData().getName());
		}
		runtime.checkXRequestedWith(stack.getRequest());
	}


	private boolean isSameOrigin(String requestOrigin, HttpServletRequest request) {
		URI originUri;
		try {
			originUri = new URI(requestOrigin);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("requestOrigin not valid:" + requestOrigin, e);
		}
		if (originUri.getHost() == null || !originUri.getHost().equals(request.getServerName())) {
			return false;
		}
		int port = originUri.getPort();
		if (port == -1) {
			if ("http".equals(originUri.getScheme())) {
				port = 80;
			} else if ("https".equals(originUri.getScheme())) {
				port = 443;
			}
		}
		if (port != request.getServerPort()) {
			return false;
		}

		return true;
	}

	private boolean handleCrossOriginResourceSharing(WebApiRuntime runtime, RequestContext context, HttpServletRequest request, HttpServletResponse response) {
		String requestOrigin = request.getHeader(ORIGIN);
		if (requestOrigin == null) {
			return true;
		}
		if (isSameOrigin(requestOrigin, request)) {
			return true;
		}

		boolean allowOrigin = runtime.isCorsAllowOrigin(requestOrigin, context);

		if (logger.isDebugEnabled()) {
			logger.debug("CORS check with Origin: " + requestOrigin + ", api: " + runtime.getMetaData().getName() + " = " + allowOrigin);
		}

		if (allowOrigin) {
			response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, requestOrigin);
			if (runtime.isCorsAllowCredentials()) {
				response.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
			}

			if (request.getMethod().equals(HttpMethod.OPTIONS)) {
				//preflight
				String accessControlRequestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD);
				if (accessControlRequestMethod != null
						&& runtime.getRequestRestriction().isAllowedMethod(accessControlRequestMethod)) {

					//Access-Control-Allow-Methods
					response.addHeader(ACCESS_CONTROL_ALLOW_METHODS, runtime.corsAccessControlAllowMethods());
				}
				String accessControlRequestHeaders = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS);
				if (accessControlRequestHeaders != null) {
					response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, accessControlRequestHeaders);
				}

				//TODO Access-Control-Max-Age
			}
		}
		return allowOrigin;
	}

	/**
	 * リクエストタイプを決定する
	 *
	 * <p>
	 * 本メソッドを利用してリクエストタイプを決定するメソッドは GET, DELETE です。
	 * 基本的な考え方として、GET, DELETE メソッドはリクエスト本文が無い想定とします。
	 * </p>
	 *
	 * <p>
	 * リクエストタイプは以下の順序で決定します。
	 * </p>
	 * <ol>
	 * <li>Content-Typeの指定がある場合は、Content-Type に一致するリクエストとする（REST_FORM をのぞく）</li>
	 * <li>Content-Typeの指定がない、もしくは想定していないContent-Typeの場合は、WebAPI定義の唯一に設定されているリクエストとする</li>
	 * <li>以上で決定できない場合 REST_FORM のリクエストとする</li>
	 * </ol>
	 *
	 * @param request HttpServletRequest
	 * @param runtime WebApiRuntime
	 * @return リクエストタイプ
	 */
	private RequestType decideRequestType(HttpServletRequest request, WebApiRuntime runtime) {
		RequestType type = null;
		String contentType = request.getContentType();
		if (contentType != null) {
			MediaType mt = MediaType.valueOf(contentType);
			if (mt.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType())) {
				if (mt.getSubtype().equals(MediaType.APPLICATION_JSON_TYPE.getSubtype())) {
					type = RequestType.REST_JSON;
				} else if (mt.getSubtype().equals(MediaType.APPLICATION_XML_TYPE.getSubtype())) {
					type = RequestType.REST_XML;
				}
			}
		}

		if (type == null && contentType != null) {
			if (runtime.isAcceptableRestJson(contentType)) {
				type = RequestType.REST_JSON;
			} else if (runtime.isAcceptableRestXml(contentType)) {
				type = RequestType.REST_XML;
			}
		}

		if (type == null) {
			// リクエストタイプが決定できない場合、メタデータの Accepts が 1 つだけならばそのリクエストとして扱う
			// 2つ以上のリクエストタイプを許可している場合は、リクエスト時にConetnt-Typeを指定する必要があり、ここでは決定しない。
			if (runtime.getMetaData().getAccepts() != null && runtime.getMetaData().getAccepts().length == 1) {
				type = runtime.getMetaData().getAccepts()[0];
			}
		}

		if (type == null) {
			// 最後まで未指定の場合は、REST_FORM リクエストとして受け付ける
			type = RequestType.REST_FORM;
		}
		return type;
	}

	// ===========================================================================================================
	// OPTIONS Method
	// ===========================================================================================================
	@OPTIONS
	@Path("{apiName : .+}")
	public Response preflight(@Context ServletContext servletContext,
			@Context Request rsRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName) {
		String accessControlRequestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD);

		return process(apiName, accessControlRequestMethod, servletContext, rsRequest, request, response, (stack, runtime) -> {
			//response CORS preflight

			handleCrossOriginResourceSharing(runtime, stack.getRequestContext(), request, response);

			ResponseBuilder rb = Response.ok();
			return rb.build();
		});
	}

	// ===========================================================================================================
	// GET Method
	// ===========================================================================================================
	@GET
	@Path("{apiName : .+}")
	public Response doGet(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName) {

		return process(apiName, HttpMethod.GET, servletContext, coreRequest, request, response, (stack, runtime) -> {
			RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
			RequestType type = decideRequestType(request, runtime);
			rs.setRequestType(type);
			checkValidRequest(stack, runtime);

			switch (type) {
			case REST_JSON:
				setJsonParameter(stack, runtime, request, null);
				break;
			case REST_XML:
				setXmlParameter(stack, runtime, request, null);
				break;
			default:
				break;
			}
			return executeCommand(stack, runtime);
		});
	}

	// ===========================================================================================================
	// DELETE Method
	// ===========================================================================================================
	@DELETE
	@Path("{apiName : .+}")
	public Response doDelete(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName) {

		return process(apiName, HttpMethod.DELETE, servletContext, coreRequest, request, response, (stack, runtime) -> {
			RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
			RequestType type = decideRequestType(request, runtime);
			rs.setRequestType(type);
			checkValidRequest(stack, runtime);

			switch (type) {
			case REST_JSON:
				setJsonParameter(stack, runtime, request, null);
				break;
			case REST_XML:
				setXmlParameter(stack, runtime, request, null);
				break;
			default:
				break;
			}
			return executeCommand(stack, runtime);
		});
	}

	// ===========================================================================================================
	// PUT Method
	// ===========================================================================================================
	@PUT
	@Path("{apiName : .+}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response doPutForm(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName, MultivaluedMap<String, String> params) {

		return process(apiName, HttpMethod.PUT, servletContext, coreRequest, request, response, (stack, runtime) -> {
			RestRequestContext rc = (RestRequestContext) stack.getRequestContext();
			rc.setRequestType(RequestType.REST_FORM);
			rc.setValueMap(new MultivaluedMapParameterValueMap(params));
			checkValidRequest(stack, runtime);
			return executeCommand(stack, runtime);
		});
	}

	@PUT
	@Path("{apiName : .+}")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response doPutJson(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName, Reader reader) {

		try {
			return process(apiName, HttpMethod.PUT, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
				rs.setRequestType(RequestType.REST_JSON);
				checkValidRequest(stack, runtime);
				setJsonParameter(stack, runtime, request, reader);

				return executeCommand(stack, runtime);
			});
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.debug("can not close request's Reader, may be client disconnected:" + e, e);
				}
			}
		}
	}

	@PUT
	@Path("{apiName : .+}")
	@Consumes({MediaType.APPLICATION_XML})
	public Response doPutXml(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName, Reader reader) {

		try {
			return process(apiName, HttpMethod.PUT, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
				rs.setRequestType(RequestType.REST_XML);
				checkValidRequest(stack, runtime);
				setXmlParameter(stack, runtime, request, reader);

				return executeCommand(stack, runtime);
			});
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.debug("can not close request's Reader, may be client disconnected:" + e, e);
				}
			}
		}
	}

	/**
	 * WebAPI PUT Method その他の content-type
	 *
	 * <p>
	 * 以下の content-type 以外を処理します
	 * </p>
	 *
	 * <ul>
	 * <li>application/x-www-form-urlencoded</li>
	 * <li>application/json</li>
	 * <li>application/xml</li>
	 * </ul>
	 *
	 * @param servletContext ServletContext
	 * @param coreRequest JAX-RS Request
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param apiName API名（/api/ 以降のパス）
	 * @param body body InputStream
	 * @return JAX-RS Response
	 */
	@PUT
	@Path("{apiName : .+}")
	@Consumes({ MediaType.WILDCARD })
	public Response doPutOthers(@Context ServletContext servletContext, @Context Request coreRequest, @Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("apiName") String apiName, InputStream body) {

		try {
			String contentType = request.getContentType();
			WebApiRuntime webApiRuntime = (WebApiRuntime) request.getAttribute(RestRequestContext.WEB_API_RUNTIME_NAME);

			if (webApiRuntime.isAcceptableRestJson(contentType)) {
				// PUT json パターンへディスパッチ
				try (Reader reader = new InputStreamReader(body, StandardCharsets.UTF_8)) {
					return doPutJson(servletContext, coreRequest, request, response, apiName, reader);
				} catch (IOException e) {
					logger.debug("can not close reader, may be client disconnected:" + e, e);
				}

			} else if (webApiRuntime.isAcceptableRestXml(contentType)) {
				// PUT xml パターンへディスパッチ
				String charsetString = request.getCharacterEncoding();
				Charset charset = charsetString == null ? StandardCharsets.UTF_8 : Charset.forName(charsetString);
				try (Reader reader = new InputStreamReader(body, charset)) {
					return doPutXml(servletContext, coreRequest, request, response, apiName, reader);
				} catch (IOException e) {
					logger.debug("can not close reader, may be client disconnected:" + e, e);
				}
			}

			// 上記以外の場合は、REST_OTHERS として処理する
			return process(apiName, HttpMethod.PUT, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rc = (RestRequestContext) stack.getRequestContext();
				rc.setRequestType(RequestType.REST_OTHERS);
				checkValidRequest(stack, runtime);

				// リクエストボディ、エンコーディングを設定
				rc.setAttribute(WebApiRequestConstants.REQUEST_BODY, body);
				rc.setAttribute(WebApiRequestConstants.REQUEST_CHARACTER_ENCODING, request.getCharacterEncoding());

				return executeCommand(stack, runtime);
			});

		} finally {
			if (null != body) {
				try {
					body.close();
				} catch (IOException e) {
					logger.debug("can not close request's InputStream, may be client disconnected:" + e, e);
				}
			}
		}
	}

	// ===========================================================================================================
	// POST Method
	// ===========================================================================================================
	@POST
	@Path("{apiName : .+}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response doPostForm(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName, MultivaluedMap<String, String> params) {

		return process(apiName, HttpMethod.POST, servletContext, coreRequest, request, response, (stack, runtime) -> {
			RestRequestContext rc = (RestRequestContext) stack.getRequestContext();
			rc.setRequestType(RequestType.REST_FORM);
			rc.setValueMap(new MultivaluedMapParameterValueMap(params));
			checkValidRequest(stack, runtime);
			return executeCommand(stack, runtime);
		});
	}

	@POST
	@Path("{apiName : .+}")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response doPostFormMultipart(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName) {

		//HttpServletRequestから自ら取得するため
		Long maxBodySize = (Long) request.getAttribute(RestRequestContext.MAX_BODY_SIZE);
		if (maxBodySize != null) {
			request = new LimitRequestBodyHttpServletRequest(request, maxBodySize);
		}

		return process(apiName, HttpMethod.POST, servletContext, coreRequest, request, response, (stack, runtime) -> {
			((RestRequestContext) stack.getRequestContext()).setRequestType(RequestType.REST_FORM);
			checkValidRequest(stack, runtime);
			return executeCommand(stack, runtime);
		});
	}

	@POST
	@Path("{apiName : .+}")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response doPostJson(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName, Reader reader) {

		try {
			return process(apiName, HttpMethod.POST, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
				rs.setRequestType(RequestType.REST_JSON);
				checkValidRequest(stack, runtime);
				setJsonParameter(stack, runtime, request, reader);

				return executeCommand(stack, runtime);
			});
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.debug("can not close request's Reader, may be client disconnected:" + e, e);
				}
			}
		}
	}

	@POST
	@Path("{apiName : .+}")
	@Consumes({MediaType.APPLICATION_XML})
	public Response doPostXml(@Context ServletContext servletContext,
			@Context Request coreRequest,
			@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("apiName") String apiName, Reader reader) {

		try {
			return process(apiName, HttpMethod.POST, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
				rs.setRequestType(RequestType.REST_XML);
				checkValidRequest(stack, runtime);
				setXmlParameter(stack, runtime, request, reader);

				return executeCommand(stack, runtime);
			});
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.debug("can not close request's Reader, may be client disconnected:" + e, e);
				}
			}
		}
	}

	/**
	 * WebAPI POST Method その他の content-type
	 *
	 * <p>
	 * 以下の content-type 以外を処理します
	 * </p>
	 *
	 * <ul>
	 * <li>application/x-www-form-urlencoded</li>
	 * <li>multipart/form-data</li>
	 * <li>application/json</li>
	 * <li>application/xml</li>
	 * </ul>
	 *
	 * @param servletContext ServletContext
	 * @param coreRequest JAX-RS Request
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param apiName API名（/api/ 以降のパス）
	 * @param body body InputStream
	 * @return JAX-RS Response
	 */
	@POST
	@Path("{apiName : .+}")
	@Consumes({ MediaType.WILDCARD })
	public Response doPostOthers(@Context ServletContext servletContext, @Context Request coreRequest, @Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("apiName") String apiName, InputStream body) {

		try {
			String contentType = request.getContentType();
			WebApiRuntime webApiRuntime = (WebApiRuntime) request.getAttribute(RestRequestContext.WEB_API_RUNTIME_NAME);

			if (webApiRuntime.isAcceptableRestJson(contentType)) {
				// POST json パターンへディスパッチ
				try (Reader reader = new InputStreamReader(body, StandardCharsets.UTF_8)) {
					return doPostJson(servletContext, coreRequest, request, response, apiName, reader);
				} catch (IOException e) {
					logger.debug("can not close reader, may be client disconnected:" + e, e);
				}

			} else if (webApiRuntime.isAcceptableRestXml(contentType)) {
				// POST xml パターンへディスパッチ
				String charsetString = request.getCharacterEncoding();
				Charset charset = charsetString == null ? StandardCharsets.UTF_8 : Charset.forName(charsetString);
				try (Reader reader = new InputStreamReader(body, charset)) {
					return doPostXml(servletContext, coreRequest, request, response, apiName, reader);
				} catch (IOException e) {
					logger.debug("can not close reader, may be client disconnected:" + e, e);
				}
			}

			// 上記以外の場合は、REST_OTHERS として処理する
			return process(apiName, HttpMethod.POST, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rc = (RestRequestContext) stack.getRequestContext();
				rc.setRequestType(RequestType.REST_OTHERS);
				checkValidRequest(stack, runtime);

				// リクエストボディ、エンコーディングを設定
				rc.setAttribute(WebApiRequestConstants.REQUEST_BODY, body);
				rc.setAttribute(WebApiRequestConstants.REQUEST_CHARACTER_ENCODING, request.getCharacterEncoding());

				return executeCommand(stack, runtime);
			});

		} finally {
			if (null != body) {
				try {
					body.close();
				} catch (IOException e) {
					logger.debug("can not close request's InputStream, may be client disconnected:" + e, e);
				}
			}
		}
	}

	// ===========================================================================================================
	// PATCH Method
	// ===========================================================================================================
	/**
	 * WebAPI PATCH Method form parameter
	 * <p>
	 * content-type application/x-www-form-urlencoded を処理します
	 * </p>
	 *
	 * @param servletContext ServletContext
	 * @param coreRequest JAX-RS Request
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param apiName API名（/api/ 以降のパス）
	 * @param params form parameter
	 * @return JAX-RS Response
	 */
	@PATCH
	@Path("{apiName : .+}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response doPatchForm(@Context ServletContext servletContext, @Context Request coreRequest, @Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("apiName") String apiName, MultivaluedMap<String, String> params) {

		return process(apiName, HttpMethod.PATCH, servletContext, coreRequest, request, response, (stack, runtime) -> {
			RestRequestContext rc = (RestRequestContext) stack.getRequestContext();
			rc.setRequestType(RequestType.REST_FORM);
			rc.setValueMap(new MultivaluedMapParameterValueMap(params));
			checkValidRequest(stack, runtime);
			return executeCommand(stack, runtime);
		});
	}

	/**
	 * WebAPI PATCH Methoc json parameter
	 * <p>
	 * content-type application/json を処理します
	 * </p>
	 *
	 * @param servletContext ServletContext
	 * @param coreRequest JAX-RS Request
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param apiName API名（/api/ 以降のパス）
	 * @param reader json body
	 * @return JAX-RS Response
	 */
	@PATCH
	@Path("{apiName : .+}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response doPatchJson(@Context ServletContext servletContext, @Context Request coreRequest, @Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("apiName") String apiName, Reader reader) {

		try {
			return process(apiName, HttpMethod.PATCH, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
				rs.setRequestType(RequestType.REST_JSON);
				checkValidRequest(stack, runtime);
				setJsonParameter(stack, runtime, request, reader);

				return executeCommand(stack, runtime);
			});
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.debug("can not close request's Reader, may be client disconnected:" + e, e);
				}
			}
		}
	}

	/**
	 * WebAPI PATCH Method xml parameter
	 * <p>
	 * content-type application/xml を処理します
	 * </p>
	 *
	 * @param servletContext ServletContext
	 * @param coreRequest JAX-RS Request
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param apiName API名（/api/ 以降のパス）
	 * @param reader xml body
	 * @return JAX-RS Response
	 */
	@PATCH
	@Path("{apiName : .+}")
	@Consumes({ MediaType.APPLICATION_XML })
	public Response doPatchXml(@Context ServletContext servletContext, @Context Request coreRequest, @Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("apiName") String apiName, Reader reader) {

		try {
			return process(apiName, HttpMethod.PATCH, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rs = (RestRequestContext) stack.getRequestContext();
				rs.setRequestType(RequestType.REST_XML);
				checkValidRequest(stack, runtime);
				setXmlParameter(stack, runtime, request, reader);

				return executeCommand(stack, runtime);
			});
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.debug("can not close request's Reader, may be client disconnected:" + e, e);
				}
			}
		}
	}

	/**
	 * WebAPI PUT Method その他の content-type
	 *
	 * <p>
	 * 以下の content-type 以外を処理します
	 * </p>
	 *
	 * <ul>
	 * <li>application/x-www-form-urlencoded</li>
	 * <li>application/json</li>
	 * <li>application/xml</li>
	 * </ul>
	 *
	 * @param servletContext ServletContext
	 * @param coreRequest JAX-RS Request
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param apiName API名（/api/ 以降のパス）
	 * @param body body InputStream
	 * @return JAX-RS Response
	 */
	@PATCH
	@Path("{apiName : .+}")
	@Consumes({ MediaType.WILDCARD })
	public Response doPatchOthers(@Context ServletContext servletContext, @Context Request coreRequest, @Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("apiName") String apiName, InputStream body) {

		try {
			String contentType = request.getContentType();
			WebApiRuntime webApiRuntime = (WebApiRuntime) request.getAttribute(RestRequestContext.WEB_API_RUNTIME_NAME);

			if (webApiRuntime.isAcceptableRestJson(contentType)) {
				// PUT json パターンへディスパッチ
				try (Reader reader = new InputStreamReader(body, StandardCharsets.UTF_8)) {
					return doPatchJson(servletContext, coreRequest, request, response, apiName, reader);
				} catch (IOException e) {
					logger.debug("can not close reader, may be client disconnected:" + e, e);
				}

			} else if (webApiRuntime.isAcceptableRestXml(contentType)) {
				// PUT xml パターンへディスパッチ
				String charsetString = request.getCharacterEncoding();
				Charset charset = charsetString == null ? StandardCharsets.UTF_8 : Charset.forName(charsetString);
				try (Reader reader = new InputStreamReader(body, charset)) {
					return doPatchXml(servletContext, coreRequest, request, response, apiName, reader);
				} catch (IOException e) {
					logger.debug("can not close reader, may be client disconnected:" + e, e);
				}
			}

			// 上記以外の場合は、REST_OTHERS として処理する
			return process(apiName, HttpMethod.PATCH, servletContext, coreRequest, request, response, (stack, runtime) -> {
				RestRequestContext rc = (RestRequestContext) stack.getRequestContext();
				rc.setRequestType(RequestType.REST_OTHERS);
				checkValidRequest(stack, runtime);

				// リクエストボディ、エンコーディングを設定
				rc.setAttribute(WebApiRequestConstants.REQUEST_BODY, body);
				rc.setAttribute(WebApiRequestConstants.REQUEST_CHARACTER_ENCODING, request.getCharacterEncoding());

				return executeCommand(stack, runtime);
			});

		} finally {
			if (null != body) {
				try {
					body.close();
				} catch (IOException e) {
					logger.debug("can not close request's InputStream, may be client disconnected:" + e, e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setJsonParameter(WebRequestStack stack, WebApiRuntime runtime, HttpServletRequest request, Reader reader) {

		RestRequestContext context = (RestRequestContext) stack.getRequestContext();
		MethodType methodType = context.methodType();
		try {
			String paramName = runtime.getMetaData().getRestJsonParameterName();
			if (paramName != null) {
				ObjectMapper mapper = omservice.getObjectMapper();
				Object o = null;
				Class<?> type = runtime.getMetaData().getRestJsonParameterType();
				if (type == Reader.class) {
					if (MethodType.GET == methodType || MethodType.DELETE == methodType) {
						String paramStr = request.getParameter(paramName);
						if (paramStr != null) {
							o = new StringReader(paramStr);
						}
					} else {
						o = reader;
					}
				} else {
					if (type == null || type == void.class) {
						//ParameterTypeが指定されていなかったとき、LinkedHashMapやListに格納する。
						type = Object.class;
					}
					if (MethodType.GET == methodType || MethodType.DELETE == methodType) {
						String paramStr = request.getParameter(paramName);
						if (paramStr != null) {
							o = mapper.readValue(paramStr, type);
						}
					} else {
						o = mapper.readValue(reader, type);
					}

					//WebApiParameterMap -> Map
					if (o instanceof WebApiParameterMap) {
						o = toMap((WebApiParameterMap) o);
					}
				}

				//paramという名前で、Mapの場合は、ParamMapとして扱う。
				if (paramName.equals(WebApiRequestConstants.DEFAULT_PARAM_NAME) && o instanceof Map) {
					context.setValueMap(new MapParameterValueMap((Map<String, Object>) o));
				} else if (o != null) {
					context.setAttribute(paramName, o);
				}
			}
		} catch (NullPointerException | IOException e) {
			throw new WebApplicationException(e, Status.BAD_REQUEST);
		}
	}

	private Map<String, Object> toMap(WebApiParameterMap m) {
		//WebApiParameterMap -> Map
		HashMap<String, Object> map = new HashMap<>();
		for (WebApiParameter webApiParameter : m.getParams()) {
			map.put(webApiParameter.getName(), webApiParameter.getValue());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private void setXmlParameter(WebRequestStack stack, WebApiRuntime runtime, HttpServletRequest request, Reader reader) {
		RestRequestContext context = (RestRequestContext) stack.getRequestContext();
		MethodType methodType = context.methodType();
		try {
			String paramName = runtime.getMetaData().getRestXmlParameterName();
			if (paramName != null) {
				Object o;
				Class<?> type = runtime.getMetaData().getRestXmlParameterType();
				if (type == Reader.class) {
					if (MethodType.GET == methodType || MethodType.DELETE == methodType) {
						String paramStr = request.getParameter(paramName);
						if (paramStr == null) {
							o = null;
						} else {
							o = new StringReader(paramStr);
						}
					} else {
						o = reader;
					}
				} else {
					if (MethodType.GET == methodType
							|| MethodType.DELETE == methodType) {
						String param = request.getParameter(paramName);

						if (StringUtil.isEmpty(param)) {
							o = null;
						} else {
							InputStream bais = new ByteArrayInputStream(param.getBytes());
							JAXBContext jaxb = jbservice.getJAXBContext();
							if (type == null || type == void.class) {
								o = jaxb.createUnmarshaller().unmarshal(new SAXSource(sax.newSAXParser().getXMLReader(), new InputSource(bais)));
							} else {
								o = jaxb.createUnmarshaller().unmarshal(new SAXSource(sax.newSAXParser().getXMLReader(), new InputSource(bais)), type);
							}
						}
					} else {
						JAXBContext jaxb = jbservice.getJAXBContext();
						if (type == null || type == void.class) {
							o = jaxb.createUnmarshaller().unmarshal(new SAXSource(sax.newSAXParser().getXMLReader(), new InputSource(reader)));
						} else {
							o = jaxb.createUnmarshaller().unmarshal(new SAXSource(sax.newSAXParser().getXMLReader(), new InputSource(reader)), type);
						}
					}

					//WebApiParameterMap -> Map
					if (o instanceof WebApiParameterMap) {
						o = toMap((WebApiParameterMap) o);
					}
				}

				//paramという名前で、Mapの場合は、ParamMapとして扱う。
				if (paramName.equals(WebApiRequestConstants.DEFAULT_PARAM_NAME) && o instanceof Map) {
					context.setValueMap(new MapParameterValueMap((Map<String, Object>) o));
				} else if (o != null) {
					context.setAttribute(paramName, o);
				}
			}
		} catch (NullPointerException | JAXBException | SAXException | ParserConfigurationException e) {
			throw new WebApplicationException(e, Status.BAD_REQUEST);
		}
	}
}
