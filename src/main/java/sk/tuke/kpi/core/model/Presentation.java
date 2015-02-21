package sk.tuke.kpi.core.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Presentation implements HasId<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String name;
	private String text;
	private Date dateCreated;
	private Date startTime;
	private int duration;
	private boolean restricted;
	private String password;
	private int views;

	private User user;

	public Presentation() { // default constructor
	}

	public Presentation(final String name, final Date dateCreated, final Date startTime, final int duration, final int views, final User user, final String text) {
		this.name = name;
		this.dateCreated = dateCreated;
		this.startTime = startTime;
		this.duration = duration;
		this.views = views;
		this.user = user;
		this.text = text;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
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

	public boolean isRestricted() {
		return restricted;
	}

	public void setRestricted(final boolean restricted) {
		this.restricted = restricted;
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

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}
