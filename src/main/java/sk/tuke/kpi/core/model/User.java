package sk.tuke.kpi.core.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User implements HasId<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String login;
	private String firstName;
	private String lastName;

	@DBRef(lazy = true)
	private List<Presentation> presentations;

	public User() { // default constructor
	}

	public User(final String login, final String firstName, final String lastName) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public List<Presentation> getPresentations() {
		return presentations;
	}

	public void setPresentations(final List<Presentation> presentations) {
		this.presentations = presentations;
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", " + firstName + " " + lastName + "]";
	}

}
