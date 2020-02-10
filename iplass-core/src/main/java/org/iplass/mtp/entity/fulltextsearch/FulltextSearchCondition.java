package org.iplass.mtp.entity.fulltextsearch;

import java.util.List;

import org.iplass.mtp.entity.query.OrderBy;

/**
 * 全文検索時の検索条件
 */
public class FulltextSearchCondition {

	private List<String> properties;

	private OrderBy order;

	public FulltextSearchCondition() {
	}

	public FulltextSearchCondition(List<String> properties) {
		this(properties, null);
	}

	public FulltextSearchCondition(List<String> properties, OrderBy order) {
		this.properties = properties;
		this.order = order;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	public OrderBy getOrder() {
		return order;
	}

	public void setOrder(OrderBy order) {
		this.order = order;
	}
}
