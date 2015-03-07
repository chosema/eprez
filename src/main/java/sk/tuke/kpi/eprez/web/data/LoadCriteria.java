package sk.tuke.kpi.eprez.web.data;

public class LoadCriteria {

	private Pagination pagination;
	private SortOrder sortOrder;
	private String sortField;

	public LoadCriteria() { // Default constructor
	}

	public LoadCriteria(final Pagination pagination) {
		this.pagination = pagination;
	}

	public LoadCriteria(final SortOrder sortOrder, final String sortField) {
		this.sortOrder = sortOrder;
		this.sortField = sortField;
	}

	public LoadCriteria(final Pagination pagination, final SortOrder sortOrder, final String sortField) {
		this.pagination = pagination;
		this.sortOrder = sortOrder;
		this.sortField = sortField;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(final Pagination pagination) {
		this.pagination = pagination;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(final SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(final String sortField) {
		this.sortField = sortField;
	}
}
