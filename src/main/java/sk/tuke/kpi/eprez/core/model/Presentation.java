package sk.tuke.kpi.eprez.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;
import sk.tuke.kpi.eprez.core.model.enums.RestrictionType;

@Document
public class Presentation extends AbstractAuditable<String> {
	private static final long serialVersionUID = 1L;

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

	private byte[] image;

	private PresentationDocument document;
	@DBRef
	private List<Attachment> attachments;
	private List<PresentationCategory> categories;

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

	public byte[] getImage() {
		return image == null ? (image = new byte[0]) : image;
	}

	public void setImage(final byte[] image) {
		this.image = image;
	}

	public List<PresentationCategory> getCategories() {
		return categories == null ? (categories = new ArrayList<>(1)) : categories;
	}

	public void setCategories(final List<PresentationCategory> categories) {
		this.categories = categories;
	}

	public PresentationDocument getDocument() {
		return document;
	}

	public void setDocument(final PresentationDocument document) {
		this.document = document;
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
