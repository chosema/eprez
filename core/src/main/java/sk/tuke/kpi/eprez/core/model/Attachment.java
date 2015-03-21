package sk.tuke.kpi.eprez.core.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Attachment extends AbstractAuditable<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotEmpty
	private String name;
	private long size;
	private boolean available;

	@JsonIgnore
	@DBRef(lazy = true)
	private Data data;

	public Attachment() { // default constructor
	}

	public Attachment(final String name, final Data data) {
		this.name = name;
		this.data = data;
		this.size = data.getSize();
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(final long size) {
		this.size = size;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(final boolean available) {
		this.available = available;
	}

	@JsonIgnore
	public Data getData() {
		return data;
	}

	@JsonIgnore
	public void setData(final Data data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Attachment other = (Attachment) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Attachment [id=" + id + ", name=" + name + ", available=" + available + "]";
	}
}
