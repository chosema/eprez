package sk.tuke.kpi.eprez.web.data;

public class Pagination {

	private final Integer firstResult;
	private final Integer maxResults;

	public Pagination(final Integer firstResult, final Integer maxResults) {
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}
}
