package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Data implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private byte[] content;
	private String contentType;
	private long size;

	public Data() { // default constructor
	}

	public Data(final byte[] content, final String contentType, final long size) {
		this.content = content;
		this.contentType = contentType;
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(final long size) {
		this.size = size;
	}
}
