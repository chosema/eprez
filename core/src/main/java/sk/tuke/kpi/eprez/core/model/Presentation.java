package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import sk.tuke.kpi.eprez.core.model.enums.DocumentState;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;
import sk.tuke.kpi.eprez.core.model.enums.RestrictionType;

@Document
public class Presentation extends AbstractAuditable<String> {
	private static final long serialVersionUID = 1L;

	public static class Document implements Serializable {
		private static final long serialVersionUID = 1L;

		private String name;
		private DocumentState state;

		@DBRef(lazy = true)
		private List<DocumentPage> pages;

		public Document() { // default constructor
		}

		public Document(final String name) {
			this(name, DocumentState.PREPARING);
		}

		public Document(final String name, final DocumentState state) {
			this.name = name;
			this.state = state;
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
			return "PresentationDocument [name=" + name + ", state=" + state + "]";
		}
	}

	public static class Session implements Serializable {
		private static final long serialVersionUID = 1L;

		private boolean running;
		private int currentPageIndex;
		private Date started;

		@DBRef(lazy = true)
		private List<SessionToken> tokens;

		public boolean isRunning() {
			return running;
		}

		public void setRunning(final boolean running) {
			this.running = running;
		}

		public int getCurrentPageIndex() {
			return currentPageIndex;
		}

		public void setCurrentPageIndex(final int currentPageIndex) {
			this.currentPageIndex = currentPageIndex;
		}

		public Date getStarted() {
			return started;
		}

		public void setStarted(final Date started) {
			this.started = started;
		}

		public List<SessionToken> getTokens() {
			return tokens == null ? (tokens = new ArrayList<>(1)) : tokens;
		}

		public void setTokens(final List<SessionToken> tokens) {
			this.tokens = tokens;
		}
	}

	@Id
	private String id;

	@NotEmpty
	private String name;
	private String description;
	private Date startTime;
	private int duration;

	@NotNull
	private RestrictionType restriction;
	private boolean published;

	private int views;

	private Document document;
	private Session session;
	private List<PresentationCategory> categories;
	@DBRef
	private List<Attachment> attachments;
	@DBRef(lazy = true)
	private Data image;

	public Presentation() { // default constructor
	}

	public Presentation(final String name, final RestrictionType restriction) {
		this.name = name;
		this.restriction = restriction;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(final String text) {
		this.description = text;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	public RestrictionType getRestriction() {
		return restriction;
	}

	public void setRestriction(final RestrictionType restriction) {
		this.restriction = restriction;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(final boolean published) {
		this.published = published;
	}

	public int getViews() {
		return views;
	}

	public void setViews(final int views) {
		this.views = views;
	}

	public Data getImage() {
		return image;
	}

	public void setImage(final Data image) {
		this.image = image;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(final Document document) {
		this.document = document;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(final Session instance) {
		this.session = instance;
	}

	public List<PresentationCategory> getCategories() {
		return categories == null ? (categories = new ArrayList<>(1)) : categories;
	}

	public void setCategories(final List<PresentationCategory> categories) {
		this.categories = categories;
	}

	public List<Attachment> getAttachments() {
		return attachments == null ? (attachments = new ArrayList<>(1)) : attachments;
	}

	public void setAttachments(final List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "Presentation [id=" + id + ", name=" + name + ",\n\tstartTime=" + startTime + ", duration=" + duration + ", restriction=" + restriction + ", published=" + published
				+ ", views=" + views + ",\n\tattachments=" + attachments + ",\n\tcategories=" + categories + "\n] user=" + (createdBy == null ? lastModifiedBy : createdBy);
	}
}
