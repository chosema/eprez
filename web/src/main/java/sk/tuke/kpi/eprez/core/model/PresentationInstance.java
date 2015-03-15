package sk.tuke.kpi.eprez.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PresentationInstance implements HasId<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private boolean running;

	@DBRef
	private DocumentPage page;

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(final boolean running) {
		this.running = running;
	}

	public DocumentPage getPage() {
		return page;
	}

	public void setPage(final DocumentPage page) {
		this.page = page;
	}
}
