package sk.tuke.kpi.eprez.web.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;

public abstract class AbstractSpringLazyDataModel<T> extends LazyDataModel<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Map<String, Object> filters = Collections.emptyMap();

	protected final org.springframework.data.domain.PageRequest convert(final PageRequest page) {
		if (page.getSorts() == null || page.getSorts().isEmpty()) {
			return new org.springframework.data.domain.PageRequest(page.getPage(), page.getSize());
		} else {
			final org.springframework.data.domain.Sort sort = new org.springframework.data.domain.Sort(convert(page.getSorts()));
			return new org.springframework.data.domain.PageRequest(page.getPage(), page.getSize(), sort);
		}
	}

	protected final List<org.springframework.data.domain.Sort.Order> convert(final List<Sort> sorts) {
		final List<org.springframework.data.domain.Sort.Order> result = new ArrayList<org.springframework.data.domain.Sort.Order>(sorts.size());
		for (final Sort sort : sorts) {
			final org.springframework.data.domain.Sort.Order order = new org.springframework.data.domain.Sort.Order(convert(sort.getOrder()), sort.getProperty());
			result.add(sort.isIgnoreCase() ? order.ignoreCase() : order);
		}
		return result;
	}

	public static final org.springframework.data.domain.Sort.Direction convert(final SortOrder sortOrder) {
		return sortOrder == SortOrder.ASC ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC;
	}

	@Override
	public List<T> load(final int first, final int pageSize, final String sortField, final org.primefaces.model.SortOrder sortOrder, final Map<String, Object> filters) {
		this.filters = filters == null ? Collections.emptyMap() : filters;

		final PageRequest pageRequest;
		if (sortField == null || sortField.length() == 0) {
			if (getDefaultSort() == null) {
				pageRequest = new PageRequest(calculatePage(first, pageSize), pageSize);
			} else {
				pageRequest = new PageRequest(calculatePage(first, pageSize), pageSize, getDefaultSort());
			}
		} else {
			final Sort sort = new Sort(sortField, org.primefaces.model.SortOrder.ASCENDING == sortOrder ? SortOrder.ASC : SortOrder.DESC, isSortCaseInsensitive(sortField));
			pageRequest = new PageRequest(calculatePage(first, pageSize), pageSize, sort);
		}

		final Page<T> dataPage = getData(pageRequest);
		final List<T> data = dataPage.getContent();
		setRowCount((int) dataPage.getTotalElements());

		if (data.size() > pageSize) {
			try {
				setWrappedData(data.subList(first, first + pageSize));
			} catch (final IndexOutOfBoundsException e) {
				setWrappedData(data.subList(first, first + (data.size() % pageSize)));
			}
		} else {
			setWrappedData(data);
		}
		return data;
	}

	private int calculatePage(final int first, final int pageSize) {
		return pageSize == 0 ? 0 : (first / pageSize);
	}

	/**
	 * To implement.
	 *
	 * @return
	 */
	public Sort getDefaultSort() {
		return null;
	}

	/**
	 * To implement. Default is false.
	 *
	 * @param sortField
	 * @return
	 */
	public boolean isSortCaseInsensitive(final String sortField) {
		return false;
	}

	protected abstract Page<T> getData(PageRequest pageRequest);
}
