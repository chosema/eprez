package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DocumentPage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private int index;
	private String format;

	@JsonIgnore
	@DBRef(lazy = true)
	private Data data;

	public DocumentPage() { // default constructor
	}

	public DocumentPage(final int index, final String format, final Data data) {
		this.index = index;
		this.format = format;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(final String imageFormat) {
		this.format = imageFormat;
	}

	@JsonIgnore
	public Data getData() {
		return data;
	}

	@JsonIgnore
	public void setData(final Data data) {
		this.data = data;
	}
}
