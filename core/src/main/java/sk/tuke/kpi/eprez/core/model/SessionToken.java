package sk.tuke.kpi.eprez.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SessionToken implements HasId<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private boolean presenter;

	@DBRef
	private User user;

	public SessionToken() { // default constructor
	}

	public SessionToken(final User user) {
		this.user = user;
	}

	public SessionToken(final boolean presenter, final User user) {
		this.presenter = presenter;
		this.user = user;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public boolean isPresenter() {
		return presenter;
	}

	public void setPresenter(final boolean presenter) {
		this.presenter = presenter;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SessionToken [id=" + id + ", presenter=" + presenter + "]";
	}
}
