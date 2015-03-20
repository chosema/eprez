package sk.tuke.kpi.eprez.web.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;

import sk.tuke.kpi.eprez.core.criteria.LoadCriteria;
import sk.tuke.kpi.eprez.core.criteria.Pagination;

public abstract class AbstractLazyDataModel<T> extends LazyDataModel<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Map<String, Object> filters = Collections.emptyMap();

	@Override
	@SuppressWarnings("unchecked")
	public List<T> load(final int first, final int pageSize, final String sortField, final org.primefaces.model.SortOrder sortOrder, final Map<String, Object> filters) {
		this.filters = filters == null ? Collections.emptyMap() : filters;

		setRowCount((int) getCount());
		if (getRowCount() > 0) {
			sk.tuke.kpi.eprez.core.criteria.SortOrder order = null;
			if (sortOrder == org.primefaces.model.SortOrder.ASCENDING) {
				order = sk.tuke.kpi.eprez.core.criteria.SortOrder.ASC;
			} else if (sortOrder == org.primefaces.model.SortOrder.DESCENDING) {
				order = sk.tuke.kpi.eprez.core.criteria.SortOrder.DESC;
			}

			final List<T> data = getData(new LoadCriteria(new Pagination(first, pageSize), order, sortField));
			if (data.size() > pageSize) {
				try {
					setWrappedData(data.subList(first, first + pageSize));
				} catch (final IndexOutOfBoundsException e) {
					setWrappedData(data.subList(first, first + (data.size() % pageSize)));
				}
			} else {
				setWrappedData(data);
			}
		}
		return (List<T>) getWrappedData();
	}

	protected abstract List<T> getData(LoadCriteria loadCriteria);

	protected abstract long getCount();
}
