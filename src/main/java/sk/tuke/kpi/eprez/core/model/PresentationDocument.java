package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import sk.tuke.kpi.eprez.core.model.enums.DocumentState;

@Document
public class PresentationDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String name;
	private DocumentState state;

	@DBRef(lazy = true)
	private List<DocumentPage> pages;

	public PresentationDocument() { // default constructor
	}

	public PresentationDocument(final String name) {
		this(name, DocumentState.PREPARING);
	}

	public PresentationDocument(final String name, final DocumentState state) {
		this.name = name;
		this.state = state;
	}

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

	public DocumentState getState() {
		return state;
	}

	public void setState(final DocumentState state) {
		this.state = state;
	}

	public List<DocumentPage> getPages() {
		return pages == null ? (pages = new ArrayList<>()) : pages;
	}

	public void setPages(final List<DocumentPage> pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "PresentationDocument [id=" + id + ", name=" + name + ", state=" + state + "]";
	}
}
