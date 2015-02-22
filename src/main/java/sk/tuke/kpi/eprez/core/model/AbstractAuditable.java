package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;
import java.time.LocalTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class AbstractAuditable<U, PK extends Serializable> implements HasId<PK> {
	private static final long serialVersionUID = 1L;

	@CreatedBy
	private U createdBy;

	@CreatedDate
	private LocalTime createdDate;

	@LastModifiedBy
	private U lastModifiedBy;

	@LastModifiedDate
	private LocalTime lastModifiedDate;

	public U getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final U createdBy) {
		this.createdBy = createdBy;
	}

	public LocalTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final LocalTime createdDate) {
		this.createdDate = createdDate;
	}

	public U getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(final U lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public LocalTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(final LocalTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
