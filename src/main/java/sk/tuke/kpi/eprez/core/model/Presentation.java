package sk.tuke.kpi.eprez.core.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Presentation extends AbstractAuditable<User, String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotNull
	private String name;
	private String description;
	private Date dateCreated;
	private Date startTime;
	private int duration;
	@NotNull
	private RestrictionType restriction;
	private String password;
	private int views;

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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(final Date dateCreated) {
		this.dateCreated = dateCreated;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public int getViews() {
		return views;
	}

	public void setViews(final int views) {
		this.views = views;
	}

}
