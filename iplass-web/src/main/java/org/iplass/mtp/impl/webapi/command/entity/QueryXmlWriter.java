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
package org.iplass.mtp.impl.webapi.command.entity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.entity.csv.QueryWriteOption;
import org.iplass.mtp.impl.webapi.command.Constants;
import org.iplass.mtp.impl.webapi.jaxb.JaxbListValue;
import org.iplass.mtp.impl.xml.jaxb.DateXmlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * QueryのXmlStreaming処理クラス
 * </p>
 */

public class QueryXmlWriter implements AutoCloseable, Constants {

	private static final Logger logger = LoggerFactory.getLogger(QueryXmlWriter.class);

	private final Query query;
	private final QueryWriteOption option;
	private final EntityManager em;
	private final boolean isCountTotal;
	
	private DateXmlAdapter dateAdapter;
	private Marshaller marshaller;
	private XMLStreamWriter writer;
	private Map<String, String> nameSpaceList;

	public QueryXmlWriter(OutputStream out, Query query, boolean isCountTotal, JAXBContext context, 
			Map<String, String> nameSpaceMap ,DateXmlAdapter dateAdapter) throws IOException {
		this(out, query, isCountTotal, new QueryWriteOption(), context, nameSpaceMap, dateAdapter);
	}

	public QueryXmlWriter(OutputStream out, Query query, boolean isCountTotal, QueryWriteOption option,
			JAXBContext context, Map<String, String> nameSpaceList ,DateXmlAdapter dateAdapter) throws IOException {
		this.query = query;
		this.isCountTotal = isCountTotal;
		this.option = option;
		this.dateAdapter = dateAdapter;
		this.nameSpaceList = nameSpaceList;
		em = ManagerLocator.manager(EntityManager.class);
		
		//TODO できればインスタンス共有したいが、スレッドセーフ否かは実装依存なので、対応方法要検討
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
			
			marshaller = context.createMarshaller();
			// marshal時にXML宣言を生成しないように
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			writer = factory.createXMLStreamWriter(new BufferedWriter(new OutputStreamWriter(out, option.getCharset())));
		} catch (JAXBException | XMLStreamException e) {
			throw new SystemException(e);
		}
	}

	public void write() throws IOException {
		try {
			writer.writeProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"");

			writer.writeStartElement("webapi", "webApiResponse", "http://mtp.iplass.org/xml/webapi");
			
			for(Map.Entry<String, String> entry : nameSpaceList.entrySet()) {
				writer.writeNamespace(entry.getKey(), entry.getValue());
			}

			writer.writeStartElement("status");
			writer.writeCharacters(CMD_EXEC_SUCCESS);
			writer.writeEndElement();
			
			Query optQuery = option.getBeforeSearch().apply(query);

			//header
			writer.writeStartElement("result");
			writer.writeAttribute("key", "listHeader");
			writer.writeStartElement("value");
			List<Object> headerList = new ArrayList<>(optQuery.getSelect().getSelectValues().size());
			for (ValueExpression ve: optQuery.getSelect().getSelectValues()) {
				headerList.add(ve.toString());
			}
			JAXBElement<JaxbListValue> root = new JAXBElement<>(new QName("http://mtp.iplass.org/xml/webapi", "list"), JaxbListValue.class, new JaxbListValue(headerList));
			marshaller.marshal(root, writer);

			writer.writeEndElement();
			writer.writeEndElement();
			

			writer.writeStartElement("result");
			writer.writeAttribute("key", "list");
			writer.writeStartElement("value");

			// 検索結果のXMLレコードを出力
			int countTotal = search(optQuery);

			writer.writeEndElement();
			
			if (isCountTotal) {
				writer.writeEndElement();
				
				writer.writeStartElement("result");
				writer.writeAttribute("key", "count");
				
				writer.writeStartElement("value");
				
				writer.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				writer.writeNamespace("xs", "http://www.w3.org/2001/XMLSchema");
				writer.writeAttribute("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:int");
				writer.writeCharacters(String.valueOf(countTotal));
			}
			
			writer.writeEndDocument();
		} catch (XMLStreamException e) {
			throw new SystemException(e);
		} catch (JAXBException e) {
			throw new SystemException(e);
		}
		
	}

	@Override
	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (XMLStreamException e) {
				logger.warn("fail to close QueryXmlWriter resource. check whether resource is leak or not.", e);
			}
			writer = null;
		}
	}

	private void writeValues(final Object[] values) {
		List<Object> vList = new ArrayList<>(values.length);
		try {
			for (Object lv : values) {
				vList.add(dateAdapter.marshal(lv));
			}
			
			JAXBElement<JaxbListValue> root = new JAXBElement<>(new QName("http://mtp.iplass.org/xml/webapi", "list"), JaxbListValue.class, new JaxbListValue(vList));
			marshaller.marshal(root, writer);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	private int search(Query optQuery) {

		if (option.getLimit() > 0) {
			optQuery.setLimit(new Limit(option.getLimit()));
		}

		final int[] count = new int[1];
		em.search(optQuery, new Predicate<Object[]>() {

			@Override
			public boolean test(Object[] values) {

				option.getAfterSearch().accept(optQuery.copy(), values);

				writeValues(values);

				count[0] = count[0] + 1;
				return true;
			}
		});

		return count[0];
	}
}
