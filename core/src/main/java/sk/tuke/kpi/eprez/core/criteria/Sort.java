package sk.tuke.kpi.eprez.core.criteria;

import java.io.Serializable;

public class Sort implements Serializable {
	private static final long serialVersionUID = 1L;

	private String property;
	private SortOrder order;

	private boolean ignoreCase;

	public Sort() { // default constructor
		this("default");
	}

	public Sort(final String property) {
		this(property, SortOrder.ASC);
	}

	public Sort(final String property, final SortOrder order) {
		this(property, order, false);
	}

	public Sort(final String property, final SortOrder order, final boolean ignoreCase) {
		this.property = property;
		this.order = order;
		this.ignoreCase = ignoreCase;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(final String property) {
		this.property = property;
	}

	public SortOrder getOrder() {
		return order;
	}

	public void setOrder(final SortOrder order) {
		this.order = order;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(final boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public String toString() {
		return "Sort [" + property + " " + order + "]";
	}

}
