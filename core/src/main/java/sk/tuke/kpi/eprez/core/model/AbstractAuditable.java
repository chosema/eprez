package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractAuditable<PK extends Serializable> implements HasId<PK> {
	private static final long serialVersionUID = 1L;

	@DBRef
	@CreatedBy
	protected User createdBy;

	@CreatedDate
	protected Date createdDate;

	@DBRef
	@LastModifiedBy
	@JsonIgnore
	protected User lastModifiedBy;

	@LastModifiedDate
	@JsonIgnore
	protected Date lastModifiedDate;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	@JsonIgnore
	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	@JsonIgnore
	public void setLastModifiedBy(final User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@JsonIgnore
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	@JsonIgnore
	public void setLastModifiedDate(final Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
