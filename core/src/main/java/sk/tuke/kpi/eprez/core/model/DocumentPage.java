package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class DocumentPage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private int index;
	private String format;

	private String dataRef;

	public DocumentPage() { // default constructor
	}

	public DocumentPage(final int index, final String format, final String dataRef) {
		this.index = index;
		this.format = format;
		this.dataRef = dataRef;
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

	public String getDataRef() {
		return dataRef;
	}

	public void setDataRef(final String dataRef) {
		this.dataRef = dataRef;
	}
}
