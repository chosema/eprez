package sk.tuke.kpi.eprez.core.criteria;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class PageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private int page;
	private int size;

	private List<Sort> sorts;

	/**
	 * Default constructor with pagination for first page with 10 elements
	 */
	public PageRequest() {
		this(0, 10);
	}

	/**
	 * @param page - zero-based page index
	 * @param size - page size
	 * @param sorts - sorting constraints
	 */
	public PageRequest(final int page, final int size, final Sort... sorts) {
		this.page = page;
		this.size = size;
		this.sorts = Arrays.asList(sorts);
	}

	public int getPage() {
		return page;
	}

	public void setPage(final int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public List<Sort> getSorts() {
		return sorts;
	}

	public void setSorts(final List<Sort> sorts) {
		this.sorts = sorts;
	}

	public Sort getSort() {
		return sorts == null || sorts.isEmpty() ? null : sorts.get(0);
	}

	@Override
	public String toString() {
		return "PageRequest [page=" + page + ", size=" + size + "]";
	}
}
